package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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

    public static RestaurantRepository getInstance(){
        if (restaurantRepository == null){
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }

    private GooglePlacesAPI googlePlacesAPI;

    public RestaurantRepository(){
        googlePlacesAPI = RetrofitService.createService(GooglePlacesAPI.class);
    }

    public MutableLiveData<List<Restaurant>> getRestaurants(String location, String radius, String type, String keyword, String key){
        MutableLiveData<List<Restaurant>> restaurantData = new MutableLiveData<>();
        googlePlacesAPI.getRestaurant(location, radius, type, keyword, key)
                .enqueue(new Callback<Restaurants>() {
                    @Override
                    public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                        if (response.isSuccessful()){
                            //TODO faire le if pour vérifier si response.body est null
                            restaurantData.setValue(mapResult(response.body()));
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

    private List<Restaurant> mapResult(Restaurants restaurant){

        List<Restaurant> restaurants = new ArrayList<>();

        for(Result resto : restaurant.getResults()){
            if (resto.getPhotos() != null && !resto.getPhotos().isEmpty()){


                String imageReference = resto.getPhotos().get(0).getPhotoReference();
                String endOfUrl = "&sensor=false&key=";
                String imageUrl = BASE_PHOTO_URL + imageReference + endOfUrl + API_KEY_PLACES;

                restaurants.add(new Restaurant(resto.getName(),
                        resto.getRating().toString(),
                        resto.getGeometry().getLocation().getLat(),
                        resto.getGeometry().getLocation().getLng(),
                        imageUrl));
            }
        }

        return restaurants;
    }

//    private void setNewRestaurants(List<Restaurant> restaurants){
//        restaurantsData = restaurants;
//        restaurantAdapter.setData(restaurants);
//    }
}
