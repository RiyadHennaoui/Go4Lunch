package com.riyad.go4lunch.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.data.Restaurants;
import com.riyad.go4lunch.networking.RestaurantRepository;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.List;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.CURRENT_DEVICE_LOCATION;
import static com.riyad.go4lunch.utils.Constants.RESTAURANT_TYPE;

public class RestaurantsViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> restaurantsMutableLiveData;
    private RestaurantRepository restaurantRepository;


    public void init(String currentLocation){
        if (restaurantsMutableLiveData != null) {
            return;
        }


        restaurantRepository = RestaurantRepository.getInstance();
        restaurantsMutableLiveData = restaurantRepository.getRestaurants(currentLocation,
                "1500",
                RESTAURANT_TYPE,
                API_KEY_PLACES);
    }

    public LiveData<List<Restaurant>> getRestaurantRepository(){
        return restaurantsMutableLiveData;
    }

}
