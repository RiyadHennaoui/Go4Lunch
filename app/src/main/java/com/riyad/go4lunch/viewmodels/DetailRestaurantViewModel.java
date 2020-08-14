package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.datadetail.DetailRestaurant;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.RatingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.DetailRestaurantRepository;
import com.riyad.go4lunch.networking.UserRepository;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.ui.RestaurantDetail;

import java.util.ArrayList;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;

public class DetailRestaurantViewModel extends ViewModel {

    private MutableLiveData<Restaurant> restaurantDetailMutableLiveData;
    private DetailRestaurantRepository detailRestaurantRepository;
    private MutableLiveData<ArrayList<BookingRestaurant>> bookingRestaurantMutableLiveData;
    private MutableLiveData<ArrayList<RatingRestaurant>> restaurantLikes;
    public void init(String restaurantId){

        if(restaurantDetailMutableLiveData != null){
            return;
        }


        detailRestaurantRepository = DetailRestaurantRepository.getInstance();
        restaurantDetailMutableLiveData = detailRestaurantRepository.getRestaurantDetailNew(restaurantId);

    }

    public LiveData<Restaurant> getDetailRestaurant(){
        return restaurantDetailMutableLiveData;
    }



    //TODO sur un click de l'utilisateur appeler le repo pour faire le booking.
    public MutableLiveData<ArrayList<BookingRestaurant>> getBookingRestaurantMutableLiveData (String restaurantId){
        bookingRestaurantMutableLiveData = detailRestaurantRepository.bookingRestaurantRepo(restaurantId);
        return  bookingRestaurantMutableLiveData;
    }

    public MutableLiveData<ArrayList<RatingRestaurant>> getRestaurantLikes (String restaurantId){
        restaurantLikes = detailRestaurantRepository.restaurantLike(restaurantId);
        return restaurantLikes;
    }
}
