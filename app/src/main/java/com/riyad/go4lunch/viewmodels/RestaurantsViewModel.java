package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.data.Restaurants;
import com.riyad.go4lunch.networking.RestaurantRepository;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.RESTAURANT_TYPE;

public class RestaurantsViewModel extends ViewModel {

    private MutableLiveData<Restaurants> restaurantsMutableLiveData;
    private RestaurantRepository restaurantRepository;
    private String currentLocation;

    public void init(){
        if (restaurantsMutableLiveData != null) {
            return;
        }
        restaurantRepository = RestaurantRepository.getInstance();
        restaurantsMutableLiveData = restaurantRepository.getRestaurants(currentLocation,
                "1500",
                RESTAURANT_TYPE,
                "cruise",
                API_KEY_PLACES);
    }

    public LiveData<Restaurants> getRestaurantRepository(){
        return restaurantsMutableLiveData;
    }
}
