package com.riyad.go4lunch.networking;

import androidx.lifecycle.MutableLiveData;

import com.riyad.go4lunch.datadetail.DetailRestaurant;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.List;

public class DetailRestaurantRepository {
    private static DetailRestaurant detailRestaurant;

    public static DetailRestaurant getInstance(){
        if(detailRestaurant == null){
            detailRestaurant = new DetailRestaurant();
        }
        return detailRestaurant;
    }

    private GooglePlacesAPI googlePlacesAPI;

    public DetailRestaurantRepository(){
        googlePlacesAPI = RetrofitService.createService(GooglePlacesAPI.class);
    }


}
