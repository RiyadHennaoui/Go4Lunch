package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.RatingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_RESTAURANTS_NAME;
import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;
import static com.riyad.go4lunch.utils.Constants.FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT;
import static com.riyad.go4lunch.utils.Constants.FIELD_RATING_USER_FOR_RESTAURANT_DOCUMENT;

public class DetailRestaurantRepository {
    private static DetailRestaurantRepository detailRestaurant;
    private FirebaseFirestore restaurantDb = FirebaseFirestore.getInstance();
    private ArrayList<BookingRestaurant> bookinfRef = new ArrayList<>();
    private ArrayList<RatingRestaurant> ratingRestaurants = new ArrayList<>();
    private ArrayList<String> workmatesBookId = new ArrayList<>();


    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static DetailRestaurantRepository getInstance() {
        if (detailRestaurant == null) {
            detailRestaurant = new DetailRestaurantRepository();
        }
        return detailRestaurant;
    }
    

    public MutableLiveData<Restaurant> getRestaurantDetailNew(String id) {

        MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();
        restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(id)
                .get()
                .addOnCompleteListener(task -> {
                    Restaurant restaurant;
                    DocumentSnapshot currentDocument = task.getResult();
                    restaurant = currentDocument.toObject(Restaurant.class);
                    Log.e("RepoDetail", id + " ; " + restaurant.getId());
                    restaurantMutableLiveData.setValue(restaurant);
                });
        return restaurantMutableLiveData;
    }

    public MutableLiveData<ArrayList<RatingRestaurant>> restaurantLike(String restaurantId) {

        DocumentReference firestoreRestaurants = restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(restaurantId);
        MutableLiveData<ArrayList<RatingRestaurant>> isLiked = new MutableLiveData<>();
        RatingRestaurant newRatingRestaurant = new RatingRestaurant();
        ratingRestaurants = new ArrayList<>();

        firestoreRestaurants
                .get()
                .addOnCompleteListener(task -> {
                    Restaurant restaurant;
                    DocumentSnapshot documentSnapshot = task.getResult();
                    restaurant = documentSnapshot.toObject(Restaurant.class);
//                   Log.e("restaurant rateUser", restaurant.getRatedUser().get(0).getUserId());
                    if (restaurant.getRatingUser() != null) {
                        ratingRestaurants = restaurant.getRatingUser();
                        //                       Log.e("Maybe null", ratingRestaurants.size() + "" + ratingRestaurants.get(0).getUserId());
                    }

                    newRatingRestaurant.setRestaurantId(restaurantId);
                    newRatingRestaurant.setUserId(getCurrentUser().getUid());

                    if (ratingRestaurants.isEmpty()) {
                        Log.i("repo",  "test");
                        ratingRestaurants.add(newRatingRestaurant);
                    } else {
                        int currentCount = -1;
                        for (int i = 0; i < restaurant.getRatingUser().size(); i++) {
                            if (restaurant.getRatingUser().get(i).getUserId().equals(getCurrentUser().getUid())) {
                                currentCount = i;
                                break;
                            }
                        }
                        if (currentCount == -1) {
                            Log.i("repo current -1", currentCount + "");
                            ratingRestaurants.add(newRatingRestaurant);
                        } else {
                            Log.i("repo", currentCount + "");
                            ratingRestaurants.remove(currentCount);
                        }
                    }
                    firestoreRestaurants.update(FIELD_RATING_USER_FOR_RESTAURANT_DOCUMENT, ratingRestaurants);
                    isLiked.setValue(ratingRestaurants);
                });
        return isLiked;
    }

    //for book restaurant
    public MutableLiveData<ArrayList<BookingRestaurant>> bookingRestaurantRepo(String restaurantId) {

        DocumentReference firestoreRestaurants = restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(restaurantId);
        DocumentReference currentUserDocument = restaurantDb.collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid());

        MutableLiveData<ArrayList<BookingRestaurant>> bookingRestaurantMutableLiveData = new MutableLiveData<>();

        BookingRestaurant newBookingRestaurant = new BookingRestaurant();
        Timestamp currentTime = new Timestamp(new Date());
        bookinfRef = new ArrayList<>();

        firestoreRestaurants
                .get()
                .addOnCompleteListener(task -> {
                    Restaurant restaurant;
                    DocumentSnapshot documentSnapshot = task.getResult();
                    restaurant = documentSnapshot.toObject(Restaurant.class);
                    if (restaurant.getBookingUser() != null) {
                        bookinfRef = restaurant.getBookingUser();
                    }

                    if (bookinfRef.isEmpty()) {
                        newBookingRestaurant.setRestaurantId(restaurantId);
                        newBookingRestaurant.setTimestamp(currentTime);
                        newBookingRestaurant.setUserId(getCurrentUser().getUid());
                        bookinfRef.add(newBookingRestaurant);
                    } else {
                        int currentCount = -1;
                        for (int i = 0; i < restaurant.getBookingUser().size(); i++) {

                            if (restaurant.getBookingUser().get(i).getUserId().equals(getCurrentUser().getUid())) {
                                currentCount = i;
                                break;
                            }
                        }
                        if (currentCount == -1) {
                            newBookingRestaurant.setRestaurantId(restaurantId);
                            newBookingRestaurant.setTimestamp(currentTime);
                            newBookingRestaurant.setUserId(getCurrentUser().getUid());
                            bookinfRef.add(newBookingRestaurant);
                        } else {
                            bookinfRef.remove(currentCount);

                        }
                    }
                    Log.i("bookinfRef", bookinfRef.size() + "");
                    firestoreRestaurants.update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, bookinfRef);
                    currentUserDocument.update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, newBookingRestaurant);
                    bookingRestaurantMutableLiveData.setValue(bookinfRef);
                });


        //TODO s'abonner à l'évenement sur la detailActivity.class
        return bookingRestaurantMutableLiveData;
    }

    //get list of workmates book current restaurant.
    public MutableLiveData<ArrayList<User>> workmatesBookingRestaurantRepo(String restaurantId) {
        DocumentReference firestoreRestaurant = restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(restaurantId);
        CollectionReference workamatesCollection = restaurantDb.collection(COLLECTION_USER_NAME);

        ArrayList<User> userBooks = new ArrayList<>();
        workmatesBookId = new ArrayList<>();
        MutableLiveData<ArrayList<User>> workmatesListBookingRestaurant = new MutableLiveData<>();


        firestoreRestaurant
                .get()
                .addOnCompleteListener(task -> {
                    Restaurant currentRestaurant;
                    DocumentSnapshot currentDocument = task.getResult();
                    currentRestaurant = currentDocument.toObject(Restaurant.class);
                    if (currentRestaurant.getBookingUser() != null) {
                        for (int i = 0; i < currentRestaurant.getBookingUser().size(); i++) {
                            workmatesBookId.add(currentRestaurant.getBookingUser().get(i).getUserId());
                            Log.i("workmatesBookId", workmatesBookId.get(i));
                        }

                        workamatesCollection
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    List<User> workmateRestaurantBook;
                                    QuerySnapshot documentSnapshot = task2.getResult();
                                    workmateRestaurantBook = documentSnapshot.toObjects(User.class);
                                    if (!workmateRestaurantBook.isEmpty()) {
                                        for (int i = 0; i < workmatesBookId.size(); i++) {
                                            for (int j = 0; j < workmateRestaurantBook.size(); j++) {
                                                if (workmateRestaurantBook.get(j).getmUid().equals(workmatesBookId.get(i))) {
                                                    Log.i("ici", "ici " + workmateRestaurantBook.get(j).getmUid());
                                                    //TODO ajouter le if en dessous a la fin des tests.
                                                    // if (!workmateRestaurantBook.get(j).getmUid().equals(getCurrentUser().getUid())){
                                                    userBooks.add(workmateRestaurantBook.get(j));
                                                    //  }
                                                }
                                            }
                                        }
                                        workmatesListBookingRestaurant.setValue(userBooks);
                                    }
                                });
                    }
                });
        return workmatesListBookingRestaurant;
    }
}
