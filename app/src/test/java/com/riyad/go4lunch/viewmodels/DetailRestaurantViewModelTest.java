package com.riyad.go4lunch.viewmodels;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.riyad.go4lunch.data.Location;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.DetailRestaurantRepository;
import com.riyad.go4lunch.ui.Restaurant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class DetailRestaurantViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private DetailRestaurantRepository detailRestaurantRepository;
    private MutableLiveData<Restaurant> restaurantMutableLiveData;
    private Restaurant currentRestaurant;
    private LatLng latLng = new LatLng(44.0183, 1.3549);
    private Location location;
    private User user1;
    private String fakeRestaurantId;
    private ArrayList<User> usersList;
    private MutableLiveData<ArrayList<User>> usersListMutableLiveData;
    private BookingRestaurant bookingRestaurant1;
    private MutableLiveData<BookingRestaurant> bookingRestaurantMutableLiveData;

    @InjectMocks
    DetailRestaurantViewModel detailRestaurantViewModel;

    @Before
    public void config(){
        MockitoAnnotations.initMocks(this);
        fakeRestaurantId = "fake restaurant id";
        location = new Location();
        location.setLng(latLng.longitude);
        location.setLat(latLng.latitude);
        currentRestaurant = new Restaurant("123", "myRestaurant", "5", "www.imageurl.com", location, "Montauban");
        restaurantMutableLiveData = new MutableLiveData<>();
        restaurantMutableLiveData.postValue(currentRestaurant);
        user1 = new User("123", "user1", "user1@mail.com", "www.user1_picture.com");
        usersList = new ArrayList<>();
        usersList.add(user1);
        bookingRestaurant1 = new BookingRestaurant();
        bookingRestaurantMutableLiveData = new MutableLiveData<>();
        bookingRestaurantMutableLiveData.postValue(bookingRestaurant1);
        usersListMutableLiveData = new MutableLiveData<>();
        usersListMutableLiveData.postValue(usersList);
    }

    @Test
    public void getRestaurantDetail(){

        when(detailRestaurantRepository.getRestaurantDetailNew(fakeRestaurantId)).thenReturn(restaurantMutableLiveData);

        try{
            Restaurant restaurant = JavaTestUtils.getOrAwaitValue(detailRestaurantViewModel.getDetailRestaurant(fakeRestaurantId));
            assertEquals(restaurant, currentRestaurant);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @Test
    public void getUserBookingRestaurant(){

        when(detailRestaurantRepository.userBookingRestaurant(fakeRestaurantId)).thenReturn(bookingRestaurantMutableLiveData);
        try{
            BookingRestaurant bookingRestaurant = JavaTestUtils.getOrAwaitValue(detailRestaurantViewModel.getUserBookingRestaurant(fakeRestaurantId));
            assertEquals(bookingRestaurant, bookingRestaurant1);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @Test
    public void clearUserBook(){

        when(detailRestaurantRepository.clearUserBookingRestaurant()).thenReturn(bookingRestaurantMutableLiveData);

        try{

            BookingRestaurant bookingRestaurant = JavaTestUtils.getOrAwaitValue(detailRestaurantViewModel.clearUserBook());
            assertEquals(bookingRestaurant, bookingRestaurant1);

        }catch(InterruptedException e){
            e.printStackTrace();
        }


    }

    @Test
    public void usersBookingThisRestaurant(){


        when(detailRestaurantRepository.bookingRestaurant(fakeRestaurantId)).thenReturn(usersListMutableLiveData);

        try{
            ArrayList<User> users = JavaTestUtils.getOrAwaitValue(detailRestaurantViewModel.getBookingRestaurant(fakeRestaurantId));
            assertEquals(users, usersList);
        }catch (InterruptedException e){
            e.printStackTrace();
        }


    }

    @Test
    public void getRestaurantLike(){

    when(detailRestaurantRepository.restaurantLike(fakeRestaurantId)).thenReturn(usersListMutableLiveData);

    try{
        ArrayList<User> users = JavaTestUtils.getOrAwaitValue(detailRestaurantViewModel.getRestaurantLikes(fakeRestaurantId));
        assertEquals(users, usersList);

    }catch(InterruptedException e){
        e.printStackTrace();
    }


    }

    @Test
    public void getWorkmateBookingrestaurant(){

        when(detailRestaurantRepository.workmatesBookingRestaurant(fakeRestaurantId)).thenReturn(usersListMutableLiveData);

        try{
            ArrayList<User> users = JavaTestUtils.getOrAwaitValue(detailRestaurantViewModel.getWorkmateBookingRestaurant(fakeRestaurantId));
            assertEquals(users, usersList);

        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }



}