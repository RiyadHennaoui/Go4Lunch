package com.riyad.go4lunch.viewmodels;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.maps.model.LatLng;
import com.riyad.go4lunch.data.Location;
import com.riyad.go4lunch.networking.RestaurantRepository;
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
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.RESTAURANT_TYPE;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RestaurantsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantRepository restaurantRepository;
    private MutableLiveData<List<Restaurant>> liveDataRestaurantList;
    private LatLng latLng = new LatLng(44.0183, 1.3549);
    private Location location = new Location();
    private Restaurant restaurant;
    private List<Restaurant> restaurantList1;



    @InjectMocks
    RestaurantsViewModel restaurantsViewModel = new RestaurantsViewModel();


    @Before
    public void config() {
        MockitoAnnotations.initMocks(this);
        location.setLat(latLng.latitude);
        location.setLng(latLng.longitude);
        restaurant = new Restaurant("123", "myRestaurant", "5", "www.imageurl.com", location, "Montauban");
        liveDataRestaurantList = new MutableLiveData<>();
        restaurantList1 = new ArrayList<>();
        restaurantList1.add(restaurant);
        liveDataRestaurantList.postValue(restaurantList1);
        restaurantsViewModel.currentLocation = location.toString();
    }


    @Test
    public void getRestaurant() {
        location.setLat(latLng.latitude);
        location.setLng(latLng.longitude);

        when(restaurantRepository.getRestaurants(location.toString(), "1500", RESTAURANT_TYPE, API_KEY_PLACES)).thenReturn(liveDataRestaurantList);

        try {
            List<Restaurant> restaurantList = JavaTestUtils.getOrAwaitValue(restaurantsViewModel.getRestaurantRepository());
            assertEquals(restaurantList, restaurantList1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}