package com.riyad.go4lunch.networking;

import com.riyad.go4lunch.data.Restaurants;
import com.riyad.go4lunch.datadetail.DetailRestaurant;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesAPI {

    @GET("maps/api/place/nearbysearch/json")
    Call<Restaurants> getRestaurant(@Query("location") String location,
                                    @Query("radius") String radius,
                                    @Query("type") String type,
                                    @Query("key") String key);


    @GET("maps/api/place/nearbysearch/json")
    Call<Restaurants> getNextPageRestaurant(@Query("key") String key,
                                            @Query("pagetoken") String pagetoken);


    @GET("maps/api/place/details/json")
    Call<DetailRestaurant> getRestaurantDetail(@Query("place_id") String place_id,
                                               @Query("fields") String fields,
                                               @Query("key") String key);
}
