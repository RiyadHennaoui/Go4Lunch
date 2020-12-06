package com.riyad.go4lunch.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.RatingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.DetailRestaurantRepository;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.ArrayList;

public class DetailRestaurantViewModel extends ViewModel {

    private MutableLiveData<Restaurant> restaurantDetailMutableLiveData;
    private DetailRestaurantRepository detailRestaurantRepository;
    private MutableLiveData<ArrayList<BookingRestaurant>> bookingRestaurantMutableLiveData;
    private MutableLiveData<ArrayList<RatingRestaurant>> restaurantLikes;
    private MutableLiveData<ArrayList<User>> workmateBookingRestaurantMutableLiveData;
    public void init(){
                detailRestaurantRepository = DetailRestaurantRepository.getInstance();
    }

    public LiveData<Restaurant> getDetailRestaurant(String restaurantId){
        restaurantDetailMutableLiveData = detailRestaurantRepository.getRestaurantDetailNew(restaurantId);
        return restaurantDetailMutableLiveData;
    }

    
    public MutableLiveData<ArrayList<BookingRestaurant>> getBookingRestaurantMutableLiveData (String restaurantId){
        bookingRestaurantMutableLiveData = detailRestaurantRepository.bookingRestaurantRepo(restaurantId);
        return  bookingRestaurantMutableLiveData;
    }

    public MutableLiveData<ArrayList<RatingRestaurant>> getRestaurantLikes (String restaurantId){
        restaurantLikes = detailRestaurantRepository.restaurantLike(restaurantId);
        return restaurantLikes;
    }

    public MutableLiveData<ArrayList<User>> getWorkmateBookingRestaurantMutableLiveData (String restaurantId){
        workmateBookingRestaurantMutableLiveData = detailRestaurantRepository.workmatesBookingRestaurantRepo(restaurantId);
        return workmateBookingRestaurantMutableLiveData;
    }
}
