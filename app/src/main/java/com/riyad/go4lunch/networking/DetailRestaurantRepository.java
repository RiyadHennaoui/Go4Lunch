package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.datadetail.DetailRestaurant;
import com.riyad.go4lunch.datadetail.Result;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.RatingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.ui.RestaurantDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.BASE_PHOTO_URL;

public class DetailRestaurantRepository {
    private static DetailRestaurantRepository detailRestaurant;
    private FirebaseFirestore restaurantDb = FirebaseFirestore.getInstance();
    private ArrayList<BookingRestaurant> bookinfRef = new ArrayList<>();
    private ArrayList<RatingRestaurant> ratingRestaurants = new ArrayList<>();
    private GooglePlacesAPI googlePlacesAPI;

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static DetailRestaurantRepository getInstance() {
        if (detailRestaurant == null) {
            detailRestaurant = new DetailRestaurantRepository();
        }
        return detailRestaurant;
    }

    public DetailRestaurantRepository() {
        googlePlacesAPI = RetrofitService.createService(GooglePlacesAPI.class);
    }

    public MutableLiveData<Restaurant> getRestaurantDetailNew(String id) {

        MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();
        restaurantDb.collection("restaurants").document(id)
                .get()
                .addOnCompleteListener(task -> {
                    Restaurant restaurant;
                    DocumentSnapshot currentDocument = task.getResult();
                    restaurant = currentDocument.toObject(Restaurant.class);
                    restaurantMutableLiveData.setValue(restaurant);
                });
        return restaurantMutableLiveData;
    }

    public MutableLiveData<ArrayList<RatingRestaurant>> restaurantLike (String restaurantId){

        DocumentReference firestoreRestaurants = restaurantDb.collection("restaurants").document(restaurantId);
        MutableLiveData<ArrayList<RatingRestaurant>> isLiked = new MutableLiveData<>();
        RatingRestaurant newRatingRestaurant = new RatingRestaurant();

        firestoreRestaurants
                .get()
                .addOnCompleteListener(task -> {
                   Restaurant restaurant;
                   Boolean shouldBeLiked = true;
                   DocumentSnapshot documentSnapshot = task.getResult();
                   restaurant = documentSnapshot.toObject(Restaurant.class);
                    if(restaurant.getBookingUser() != null){
                        ratingRestaurants = restaurant.getRatedUser();
                    }

                    newRatingRestaurant.setRestaurantId(restaurantId);
                    newRatingRestaurant.setUserId(getCurrentUser().getUid());

                    if (ratingRestaurants.isEmpty()) {
                        Log.e("if booking == null", "c'est null");
                        ratingRestaurants.add(newRatingRestaurant);
                        firestoreRestaurants.update("ratingUser", ratingRestaurants);
                    } else {
                        int currentCount = -1;
                        for (int i = 0; i < restaurant.getBookingUser().size(); i++) {


                            if (restaurant.getBookingUser().get(i).getUserId().equals(getCurrentUser().getUid())) {
                                currentCount = i;
                                break;
                            }
                        }
                        if (currentCount == -1){
                            ratingRestaurants.add(newRatingRestaurant);
                        }else{
                            ratingRestaurants.remove(currentCount);
                            shouldBeLiked = false;
                        }

                        Log.e("arrayBook", bookinfRef.toString());
                        firestoreRestaurants.update("ratingUser", ratingRestaurants);

                        isLiked.setValue(ratingRestaurants);

                    }
                });
        return isLiked;
    }

    public MutableLiveData<ArrayList<BookingRestaurant>> bookingRestaurantRepo(String restaurantId) {

        DocumentReference firestoreRestaurants = restaurantDb.collection("restaurants").document(restaurantId);
        DocumentReference currentUserDocument = restaurantDb.collection("user").document(getCurrentUser().getUid());

        MutableLiveData<ArrayList<BookingRestaurant>> bookingRestaurantMutableLiveData = new MutableLiveData<>();

        BookingRestaurant newBookingRestaurant = new BookingRestaurant();
        Timestamp currentTime = new Timestamp(new Date());


        firestoreRestaurants
                .get()
                .addOnCompleteListener(task -> {
                    Restaurant restaurant;
                    DocumentSnapshot documentSnapshot = task.getResult();
                    restaurant = documentSnapshot.toObject(Restaurant.class);
                    if (restaurant.getBookingUser() != null) {
                        bookinfRef = restaurant.getBookingUser();
                    }

                    newBookingRestaurant.setRestaurantId(restaurantId);
                    newBookingRestaurant.setTimestamp(currentTime);
                    newBookingRestaurant.setUserId(getCurrentUser().getUid());

                    if (bookinfRef.isEmpty()) {
                        Log.e("if booking == null", "c'est null");
                        bookinfRef.add(newBookingRestaurant);
                        firestoreRestaurants.update("bookingUser", bookinfRef);
                    } else {
                        int currentCount = -1;
                        for (int i = 0; i < restaurant.getBookingUser().size(); i++) {

                            if (restaurant.getBookingUser().get(i).getUserId().equals(getCurrentUser().getUid())) {
                                currentCount = i;
                                break;
                            }
                        }
                        if (currentCount == -1) {
                            bookinfRef.add(newBookingRestaurant);
                        } else {
                            bookinfRef.remove(currentCount);
                        }

                        Log.e("arrayBook", bookinfRef.toString());
                        firestoreRestaurants.update("bookingUser", bookinfRef);
                        bookingRestaurantMutableLiveData.setValue(bookinfRef);
                    }
                });
        currentUserDocument.update("bookingUser", newBookingRestaurant);

        //TODO s'abonner à l'évenement sur la detailActivity.class
        return bookingRestaurantMutableLiveData;
    }

}
