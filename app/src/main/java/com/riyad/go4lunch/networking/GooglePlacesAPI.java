package com.riyad.go4lunch.networking;

import com.riyad.go4lunch.data.Restaurants;
import com.riyad.go4lunch.data.Result;
import com.riyad.go4lunch.datadetail.DetailRestaurant;
import com.riyad.go4lunch.ui.Restaurant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesAPI {

    @GET("maps/api/place/nearbysearch/json")
    Call<Restaurants> getRestaurant(@Query("location") String location,
                                    @Query("radius") String radius,
                                    @Query("type") String type,
                                    @Query("keyword") String keyword,
                                    @Query("key") String key);

    // TODO ajouter le pagetoken "getNextPageRestaurant".
    @GET("maps/api/place/nearbysearch/json")
    Call<Restaurants> getNextPageRestaurant(@Query("key") String key,
                                            @Query("pagetoken") String pagetoken);

//    @GET("maps/api/place/details/json")
//    Call<DetailRestaurant>
}
