package com.riyad.go4lunch.networking;

import androidx.lifecycle.MutableLiveData;

import com.riyad.go4lunch.datadetail.DetailRestaurant;
import com.riyad.go4lunch.datadetail.Result;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.ui.RestaurantDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.BASE_PHOTO_URL;

public class DetailRestaurantRepository {
    private static DetailRestaurantRepository detailRestaurant;

    public static DetailRestaurantRepository getInstance(){
        if(detailRestaurant == null){
            detailRestaurant = new DetailRestaurantRepository();
        }
        return detailRestaurant;
    }

    private GooglePlacesAPI googlePlacesAPI;

    public DetailRestaurantRepository(){
        googlePlacesAPI = RetrofitService.createService(GooglePlacesAPI.class);
    }

    public MutableLiveData<RestaurantDetail> getRestaurantDetail(String id, String key){
        MutableLiveData<RestaurantDetail> restaurantDetailData = new MutableLiveData<>();
        googlePlacesAPI.getRestaurantDetail(id,"name,formatted_phone_number,opening_hours,photos,website,vicinity,formatted_address", key)
                .enqueue(new Callback<DetailRestaurant>() {
                    @Override
                    public void onResponse(Call<DetailRestaurant> call, Response<DetailRestaurant> response) {
                       if (response.isSuccessful()) {

                           restaurantDetailData.setValue(restaurantDetailMapResult(response.body()));
                       }
                   }

                    @Override
                    public void onFailure(Call<DetailRestaurant> call, Throwable t) {

                    }
                });


        return restaurantDetailData;
    }

    public RestaurantDetail restaurantDetailMapResult (DetailRestaurant restaurantDetail){

//        if (restaurantDetail.getResult().getPhotos() != null || !restaurantDetail.getResult().getPhotos().isEmpty()) {
            String photoReference = restaurantDetail.getResult().getPhotos().get(0).getPhotoReference();
            String photoUrlFormated = BASE_PHOTO_URL + photoReference + "&key=" + API_KEY_PLACES;
//        }

        return new RestaurantDetail(restaurantDetail.getResult().getName(),
         restaurantDetail.getResult().getVicinity(),
         restaurantDetail.getResult().getWebsite(),
         restaurantDetail.getResult().getFormattedPhoneNumber(),
         photoUrlFormated);
    }


}
