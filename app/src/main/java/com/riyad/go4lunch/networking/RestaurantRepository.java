package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.gson.Gson;
import com.riyad.go4lunch.data.Restaurants;
import com.riyad.go4lunch.data.Result;
import com.riyad.go4lunch.datadetail.DetailRestaurant;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.ui.RestaurantDetail;
import com.riyad.go4lunch.utils.SortByDistance;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.BASE_PHOTO_URL;

public class RestaurantRepository {
    private int countRestaurants;
    private FirebaseFirestore restaurantDb = FirebaseFirestore.getInstance();

    private static RestaurantRepository restaurantRepository;


    public static RestaurantRepository getInstance() {
        if (restaurantRepository == null) {
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }

    private GooglePlacesAPI googlePlacesAPI;

    public RestaurantRepository() {
        googlePlacesAPI = RetrofitService.createService(GooglePlacesAPI.class);
    }

    public MutableLiveData<List<Restaurant>> getRestaurants(String location, String radius, String type, String key) {
        MutableLiveData<List<Restaurant>> restaurantData = new MutableLiveData<>();

        restaurantDb.collection("restaurants")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            googlePlacesAPI.getRestaurant(location, radius, type, key)
                                    .enqueue(new Callback<Restaurants>() {
                                        @Override
                                        public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                                            if (response.isSuccessful()) {

                                                ArrayList<Restaurant> restaurantsList = new ArrayList<>(mapResult(response.body()));
                                                if (response.body().getNextPageToken() == null) {
                                                    Log.i("RestaurantCall", "noNextPageToken");

                                                    getDetailRestaurant(restaurantData, restaurantsList);
                                                } else {
                                                    Log.i("RestaurantCall", "NextPageToken");
                                                    getNextPageRestaurants(restaurantData, restaurantsList, response.body().getNextPageToken());
                                                }

                                                Log.i("RestaurantCall", "Success");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Restaurants> call, Throwable t) {
                                            restaurantData.setValue(null);
                                            Log.e("RestaurantCall", "arf onFailure", t);
                                        }
                                    });
                        } else {
                            Log.i("else", "yes");
                            ArrayList<Restaurant> restaurants = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Restaurant restaurant = document.toObject(Restaurant.class);
                                restaurants.add(restaurant);
                            }
                            Collections.sort(restaurants, new SortByDistance());
                            restaurantData.setValue(restaurants);
                        }
                    }
                });
        return restaurantData;
    }

    private void getNextPageRestaurants(MutableLiveData<List<Restaurant>> restaurantData, ArrayList<Restaurant> restaurants, String nextPageToken) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        googlePlacesAPI.getNextPageRestaurant(API_KEY_PLACES, nextPageToken)
                .enqueue(new Callback<Restaurants>() {
                    @Override
                    public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {

                        if (response.isSuccessful()) {
                            Log.i("next page", "success");
                            ArrayList<Restaurant> restaurantsList = new ArrayList<>(mapResult(response.body()));

                            restaurants.addAll(restaurantsList);

                            if (response.body().getNextPageToken() == null) {
                                Log.i("next page", "success 2");
                                getDetailRestaurant(restaurantData, restaurants);
                            } else {
                                Log.i("next page", "success 3");
                                getNextPageRestaurants(restaurantData, restaurants, response.body().getNextPageToken());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Restaurants> call, Throwable t) {
                    }
                });
    }

    private void getDetailRestaurant(MutableLiveData<List<Restaurant>> restaurantData, ArrayList<Restaurant> restaurants) {
        Log.i("method", " detail restaurant" + restaurants.size());
        countRestaurants = 0;
        for (int i = 0; i < restaurants.size(); i++) {

            int finalI = i;
            googlePlacesAPI.getRestaurantDetail(restaurants.get(i).getId(), "name,formatted_phone_number,opening_hours,photos,website,vicinity,formatted_address", API_KEY_PLACES)
                    .enqueue(new Callback<DetailRestaurant>() {
                        @Override
                        public void onResponse(Call<DetailRestaurant> call, Response<DetailRestaurant> response) {
                            if (response.isSuccessful()) {
                                restaurants.get(finalI).setRestaurantDetail(restaurantDetailMapResult(response.body()));
                                countRestaurants++;
                                Log.i("Detail Restaurant", finalI + "");
                                if (countRestaurants == restaurants.size()) {
                                    Collections.sort(restaurants, new SortByDistance());
                                    restaurantData.setValue(restaurants);
                                    saveRestaurants(restaurants);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DetailRestaurant> call, Throwable t) {
                        }
                    });
        }
    }

    private List<Restaurant> mapResult(Restaurants restaurant) {

        List<Restaurant> restaurants = new ArrayList<>();

        for (Result resto : restaurant.getResults()) {
            if (resto.getPhotos() != null && !resto.getPhotos().isEmpty()) {

                String imageUrl = getImageUrlFormat(resto);

                restaurants.add(new Restaurant(resto.getPlaceId(),
                        resto.getName(),
                        resto.getRating().toString(),
                        imageUrl,
                        resto.getGeometry().getLocation(),
                        resto.getVicinity()));
            }
        }
        return restaurants;
    }

    public RestaurantDetail restaurantDetailMapResult(DetailRestaurant restaurantDetail) {

        String photoReference = restaurantDetail.getResult().getPhotos().get(0).getPhotoReference();
        String photoUrlFormated = BASE_PHOTO_URL + photoReference + "&key=" + API_KEY_PLACES;
        Gson gson = new Gson();

        Log.e("restaurant detail", gson.toJson(restaurantDetail));
        return new RestaurantDetail(restaurantDetail.getResult().getName(),
                restaurantDetail.getResult().getVicinity(),
                restaurantDetail.getResult().getWebsite(),
                restaurantDetail.getResult().getFormattedPhoneNumber(),
                photoUrlFormated,
                restaurantDetail.getResult().getOpeningHours());
    }

    @NotNull
    public static String getImageUrlFormat(Result photoReference) {
        String imageReference = photoReference.getPhotos().get(0).getPhotoReference();
        String endOfUrl = "&sensor=false&key=";
        return BASE_PHOTO_URL + imageReference + endOfUrl + API_KEY_PLACES;
    }

    public void saveRestaurants(List<Restaurant> restaurants) {

        CollectionReference firestoreRestaurants = restaurantDb.collection("restaurants");

        restaurantDb.collection("restaurants")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Restaurant> restaurantsFirestore = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            restaurantsFirestore.add(restaurant);
                        }
                        for (int i = 0; i < restaurants.size() - 1; i++) {
                            Log.i("saveRestaurant", "in for");
                            Boolean isPresent = false;
                            for (Restaurant restaurant : restaurantsFirestore) {
                                if (restaurant.getId().equals(restaurants.get(i).getId())) {
                                    isPresent = true;
                                    break;
                                }
                            }
                            if (!isPresent) {
                                firestoreRestaurants.document(restaurants.get(i).getId()).set(restaurants.get(i));
                            }
                        }
                    }
                });
    }
}
