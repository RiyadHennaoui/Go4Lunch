package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.datadetail.DetailRestaurant;
import com.riyad.go4lunch.networking.DetailRestaurantRepository;
import com.riyad.go4lunch.ui.RestaurantDetail;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;

public class DetailRestaurantViewModel extends ViewModel {

    private MutableLiveData <RestaurantDetail> restaurantDetailMutableLiveData;
    private DetailRestaurantRepository detailRestaurantRepository;
    public void init(String restaurantId){

        if(restaurantDetailMutableLiveData != null){
            return;
        }

        detailRestaurantRepository = DetailRestaurantRepository.getInstance();
        restaurantDetailMutableLiveData = detailRestaurantRepository.getRestaurantDetail(restaurantId, API_KEY_PLACES);
    }

    public LiveData<RestaurantDetail> getDetailRestaurant(){
        return restaurantDetailMutableLiveData;
    }
}
