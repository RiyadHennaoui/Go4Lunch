package com.riyad.go4lunch.viewmodels;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.networking.RestaurantRepository;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.List;

public class RestaurantsViewModel extends ViewModel {

    private RestaurantRepository restaurantRepository;
    String currentLocation;

    public void init(String currentLocation){
        restaurantRepository = RestaurantRepository.getInstance();
        this.currentLocation = currentLocation;

    }

    public LiveData<List<Restaurant>> getRestaurantRepository(){
        return  restaurantRepository.getRestaurants(currentLocation);
    }

}
