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
import com.google.gson.Gson;
import com.riyad.go4lunch.model.BookingRestaurant;
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
    private ArrayList<User> bookinfRef = new ArrayList<>();
    private ArrayList<User> ratingRestaurants = new ArrayList<>();
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
                    restaurantMutableLiveData.setValue(restaurant);
                });
        return restaurantMutableLiveData;
    }

    public MutableLiveData<ArrayList<User>> restaurantLike(String restaurantId) {

        DocumentReference firestoreRestaurants = restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(restaurantId);
        MutableLiveData<ArrayList<User>> isLiked = new MutableLiveData<>();
        User newRatingRestaurant = new User();
        ratingRestaurants = new ArrayList<>();

        firestoreRestaurants
                .get()
                .addOnCompleteListener(task -> {
                    Restaurant restaurant;
                    DocumentSnapshot documentSnapshot = task.getResult();
                    restaurant = documentSnapshot.toObject(Restaurant.class);
                    if (restaurant.getRatingUser() != null) {
                        ratingRestaurants = restaurant.getRatingUser();
                    }

                    newRatingRestaurant.setmUid(getCurrentUser().getUid());
                    newRatingRestaurant.setmUsername(getCurrentUser().getDisplayName());
                    newRatingRestaurant.setmMail(getCurrentUser().getEmail());

                    if (ratingRestaurants.isEmpty()) {
                        ratingRestaurants.add(newRatingRestaurant);
                    } else {
                        int currentCount = -1;
                        for (int i = 0; i < restaurant.getRatingUser().size(); i++) {
                            if (restaurant.getRatingUser().get(i).getmUid().equals(getCurrentUser().getUid())) {
                                currentCount = i;
                                break;
                            }
                        }
                        if (currentCount == -1) {
                            ratingRestaurants.add(newRatingRestaurant);
                        } else {
                            ratingRestaurants.remove(currentCount);
                        }
                    }
                    firestoreRestaurants.update(FIELD_RATING_USER_FOR_RESTAURANT_DOCUMENT, ratingRestaurants);
                    isLiked.setValue(ratingRestaurants);
                });
        return isLiked;
    }

    //for book restaurant
    public MutableLiveData<ArrayList<User>> bookingRestaurant(String restaurantId) {

        DocumentReference firestoreRestaurants = restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(restaurantId);
        MutableLiveData<ArrayList<User>> bookingRestaurantMutableLiveData = new MutableLiveData<>();
        bookinfRef = new ArrayList<>();
        restaurantDb.collection(COLLECTION_USER_NAME)
                .document(getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(taskUser -> {
                    User user;
                    DocumentSnapshot documentSnapshot = taskUser.getResult();
                    user = documentSnapshot.toObject(User.class);
                    Gson gson = new Gson();
                    Log.e("bookingRestaurant", gson.toJson(documentSnapshot.getData()));
                    removeBookedRestaurant(user, firestoreRestaurants, bookingRestaurantMutableLiveData);

                });
        return bookingRestaurantMutableLiveData;
    }

    private void bookRestaurant(Restaurant restaurant, DocumentReference firestoreRestaurants) {

        User newBookingRestaurant = new User();
        if (restaurant.getBookingRestaurant() != null) {
            bookinfRef = restaurant.getBookingRestaurant();
        }

        newBookingRestaurant.setmUid(getCurrentUser().getUid());
        newBookingRestaurant.setmUsername(getCurrentUser().getDisplayName());
        newBookingRestaurant.setUrlPicture(getCurrentUser().getPhotoUrl().toString());
        bookinfRef.add(newBookingRestaurant);

        firestoreRestaurants.update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, bookinfRef);

        DocumentReference currentUserDocument = restaurantDb.collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid());
        BookingRestaurant userBookingRestaurant = new BookingRestaurant();
        Timestamp currentTime = new Timestamp(new Date());
        userBookingRestaurant.setRestaurantName(restaurant.getName());
        userBookingRestaurant.setTimestamp(currentTime);
        userBookingRestaurant.setRestaurantId(restaurant.getId());

        currentUserDocument.update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, userBookingRestaurant);
    }

    private void removeBookedRestaurant(User user, DocumentReference firestoreRestaurants, MutableLiveData<ArrayList<User>> bookingRestaurantMutableLiveData) {

        Gson gson = new Gson();
        Log.e("repoResto", gson.toJson(user));
        if (user.getBookingRestaurant() != null && user.getBookingRestaurant().getRestaurantId() != null) {
            restaurantDb.collection(COLLECTION_RESTAURANTS_NAME)
                    .document(user.getBookingRestaurant().getRestaurantId())
                    .get()
                    .addOnCompleteListener(taskBookRestaurant -> {
                        Restaurant restaurant;
                        DocumentSnapshot documentSnapshot1 = taskBookRestaurant.getResult();
                        restaurant = documentSnapshot1.toObject(Restaurant.class);
                        bookinfRef = restaurant.getBookingRestaurant();
                        int currentCount = -1;
                        for (int i = 0; i < restaurant.getBookingRestaurant().size(); i++) {

                            if (restaurant.getBookingRestaurant().get(i).getmUid().equals(getCurrentUser().getUid())) {
                                currentCount = i;
                                break;
                            }
                        }
                        bookinfRef.remove(currentCount);
                        restaurantDb.collection(COLLECTION_RESTAURANTS_NAME)
                                .document(user.getBookingRestaurant().getRestaurantId())
                                .update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, bookinfRef);
                        firestoreRestaurants
                                .get()
                                .addOnCompleteListener(task -> {
                                    Restaurant restaurant1;
                                    DocumentSnapshot documentSnapshot2 = task.getResult();
                                    restaurant1 = documentSnapshot2.toObject(Restaurant.class);
                                    if (!restaurant.getId().equals(restaurant1.getId())) {
                                        bookRestaurant(restaurant1, firestoreRestaurants);
                                    }else {
                                        DocumentReference currentUserDocument = restaurantDb.collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid());
                                        BookingRestaurant userBookingRestaurant = new BookingRestaurant();
                                        currentUserDocument.update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, userBookingRestaurant);
                                    }
                                    bookingRestaurantMutableLiveData.setValue(bookinfRef);
                                });
                    });
        } else {
            firestoreRestaurants
                    .get()
                    .addOnCompleteListener(task -> {
                        Restaurant restaurant;
                        DocumentSnapshot documentSnapshot1 = task.getResult();
                        restaurant = documentSnapshot1.toObject(Restaurant.class);
                        bookRestaurant(restaurant, firestoreRestaurants);
                        bookingRestaurantMutableLiveData.setValue(bookinfRef);
                    });
        }

    }

    public MutableLiveData<BookingRestaurant> userBookingRestaurant(String restaurantId) {

        MutableLiveData<BookingRestaurant> bookingRestaurantMutableLiveData = new MutableLiveData<>();
        DocumentReference currentUserDocument = restaurantDb.collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid());
        DocumentReference currentRestaurant = restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(restaurantId);
        BookingRestaurant userBoonkingRestaurant = new BookingRestaurant();
        Timestamp currentTime = new Timestamp(new Date());

        currentRestaurant.get().addOnCompleteListener(task -> {
            Restaurant restaurant;
            DocumentSnapshot documentSnapshot = task.getResult();
            restaurant = documentSnapshot.toObject(Restaurant.class);
            userBoonkingRestaurant.setRestaurantName(restaurant.getName());
            userBoonkingRestaurant.setTimestamp(currentTime);
            userBoonkingRestaurant.setRestaurantId(restaurantId);

            currentUserDocument.update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, userBoonkingRestaurant);

        });

        bookingRestaurantMutableLiveData.setValue(userBoonkingRestaurant);

        return bookingRestaurantMutableLiveData;

    }

    public MutableLiveData<BookingRestaurant> clearUserBookingRestaurant() {

        MutableLiveData<BookingRestaurant> bookingRestaurantMutableLiveData = new MutableLiveData<>();
        DocumentReference currentUserDocument = restaurantDb.collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid());
        BookingRestaurant userBoonkingRestaurant = new BookingRestaurant();


        userBoonkingRestaurant.setTimestamp(null);
        userBoonkingRestaurant.setRestaurantId(null);

        currentUserDocument.update(FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT, userBoonkingRestaurant);

        bookingRestaurantMutableLiveData.setValue(userBoonkingRestaurant);

        return bookingRestaurantMutableLiveData;

    }

    //get list of workmates book current restaurant.
    public MutableLiveData<ArrayList<User>> workmatesBookingRestaurant(String restaurantId) {
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
                    if (currentRestaurant.getBookingRestaurant() != null) {
                        for (int i = 0; i < currentRestaurant.getBookingRestaurant().size(); i++) {
                            workmatesBookId.add(currentRestaurant.getBookingRestaurant().get(i).getmUid());
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
                                                    if (!workmateRestaurantBook.get(j).getmUid().equals(getCurrentUser().getUid())) {
                                                        userBooks.add(workmateRestaurantBook.get(j));
                                                    }
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
