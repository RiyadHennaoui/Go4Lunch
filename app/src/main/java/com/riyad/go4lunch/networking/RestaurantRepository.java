package com.riyad.go4lunch.networking;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.riyad.go4lunch.AppControler;
import com.riyad.go4lunch.data.Restaurants;
import com.riyad.go4lunch.data.Result;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.BASE_PHOTO_URL;

public class RestaurantRepository {

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
        googlePlacesAPI.getRestaurant(location, radius, type, key)
                .enqueue(new Callback<Restaurants>() {
                    @Override
                    public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                        if (response.isSuccessful()) {

                            ArrayList<Restaurant> restaurantsList = new ArrayList<>(mapResult(response.body()));
                            if (response.body().getNextPageToken() == null) {
                                Log.i("RestaurantCall", "noNextPageToken");
                                //TODO trié les restaurants par distance : Collection.sort
                                
                                restaurantData.setValue(restaurantsList);
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
                            ArrayList<Restaurant> restaurantsList = new ArrayList<>(mapResult(response.body()));

                            restaurants.addAll(restaurantsList);



                            if (response.body().getNextPageToken() == null) {
                                //TODO trié les restaurants par distance
                                restaurantData.setValue(restaurants);
                            } else {
                                getNextPageRestaurants(restaurantData, restaurants, response.body().getNextPageToken());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Restaurants> call, Throwable t) {

                    }
                });
    }

    private List<Restaurant> mapResult(Restaurants restaurant) {

        List<Restaurant> restaurants = new ArrayList<>();

        for (Result resto : restaurant.getResults()) {
            if (resto.getPhotos() != null && !resto.getPhotos().isEmpty()) {


                String imageReference = resto.getPhotos().get(0).getPhotoReference();
                String endOfUrl = "&sensor=false&key=";
                String imageUrl = BASE_PHOTO_URL + imageReference + endOfUrl + API_KEY_PLACES;

                restaurants.add(new Restaurant(resto.getId(),
                        resto.getName(),
                        resto.getRating().toString(),
                        resto.getGeometry().getLocation().getLat(),
                        resto.getGeometry().getLocation().getLng(),
                        imageUrl,
                        resto.getGeometry().getLocation()));
            }
        }

        return restaurants;
    }

}
