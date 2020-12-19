package com.riyad.go4lunch.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.model.BookingRestaurant;

import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.DetailRestaurantRepository;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.ArrayList;

public class DetailRestaurantViewModel extends ViewModel {

    private MutableLiveData<Restaurant> restaurantDetailMutableLiveData;
    private DetailRestaurantRepository detailRestaurantRepository;
    private MutableLiveData<BookingRestaurant> bookRestaurantForUser;
    private MutableLiveData<ArrayList<User>> bookingRestaurantMutableLiveData;
    private MutableLiveData<ArrayList<User>> restaurantLikes;
    private MutableLiveData<ArrayList<User>> workmateBookingRestaurantMutableLiveData;
    public void init(){
                detailRestaurantRepository = DetailRestaurantRepository.getInstance();
    }

    public LiveData<Restaurant> getDetailRestaurant(String restaurantId){
        restaurantDetailMutableLiveData = detailRestaurantRepository.getRestaurantDetailNew(restaurantId);
        return restaurantDetailMutableLiveData;
    }

    public LiveData<BookingRestaurant> getUserBookingRestaurant(String restaurantId){

        bookRestaurantForUser = detailRestaurantRepository.userBookingRestaurant(restaurantId);

        return bookRestaurantForUser;
    }

    public LiveData<BookingRestaurant> clearUserBook(){
        bookRestaurantForUser = detailRestaurantRepository.clearUserBookingRestaurant();

        return  bookRestaurantForUser;
    }

    
    public MutableLiveData<ArrayList<User>> getBookingRestaurantMutableLiveData (String restaurantId){
        bookingRestaurantMutableLiveData = detailRestaurantRepository.bookingRestaurantRepo(restaurantId);
        return  bookingRestaurantMutableLiveData;
    }

    public MutableLiveData<ArrayList<User>> getRestaurantLikes (String restaurantId){
        restaurantLikes = detailRestaurantRepository.restaurantLike(restaurantId);
        return restaurantLikes;
    }

    public MutableLiveData<ArrayList<User>> getWorkmateBookingRestaurantMutableLiveData (String restaurantId){
        workmateBookingRestaurantMutableLiveData = detailRestaurantRepository.workmatesBookingRestaurantRepo(restaurantId);
        return workmateBookingRestaurantMutableLiveData;
    }
}
