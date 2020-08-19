package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import static com.riyad.go4lunch.utils.Constants.COLLECTION_RESTAURANTS_NAME;
import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class DetailRestaurantRepository {
    private static DetailRestaurantRepository detailRestaurant;
    private FirebaseFirestore restaurantDb = FirebaseFirestore.getInstance();
    private ArrayList<BookingRestaurant> bookinfRef = new ArrayList<>();
    private ArrayList<RatingRestaurant> ratingRestaurants = new ArrayList<>();
    private ArrayList<String> workmatesBookId = new ArrayList<>();
    private ArrayList<User> workmatesRestaurantBooking= new ArrayList<>();
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

    public MutableLiveData<ArrayList<RatingRestaurant>> restaurantLike (String restaurantId){

        DocumentReference firestoreRestaurants = restaurantDb.collection(COLLECTION_RESTAURANTS_NAME).document(restaurantId);
        MutableLiveData<ArrayList<RatingRestaurant>> isLiked = new MutableLiveData<>();
        RatingRestaurant newRatingRestaurant = new RatingRestaurant();

        firestoreRestaurants
                .get()
                .addOnCompleteListener(task -> {
                   Restaurant restaurant;
                   DocumentSnapshot documentSnapshot = task.getResult();
                   restaurant = documentSnapshot.toObject(Restaurant.class);
                    if(restaurant.getBookingUser() != null){
                        ratingRestaurants = restaurant.getRatedUser();
                    }

                    newRatingRestaurant.setRestaurantId(restaurantId);
                    newRatingRestaurant.setUserId(getCurrentUser().getUid());

                    if (ratingRestaurants.isEmpty()) {
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
                        }
                        firestoreRestaurants.update("ratingUser", ratingRestaurants);
                        //TODO vérifier pourquoi le clique fonctionne seulement pour l'add mais pas pour le remove du like.
                        isLiked.setValue(ratingRestaurants);
                    }
                });
        return isLiked;
    }

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

                    newBookingRestaurant.setRestaurantId(restaurantId);
                    newBookingRestaurant.setTimestamp(currentTime);
                    newBookingRestaurant.setUserId(getCurrentUser().getUid());

                    if (bookinfRef.isEmpty()) {
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
                            bookinfRef.add(newBookingRestaurant);
                        } else {
                            bookinfRef.remove(currentCount);
                        }
                    }
                    Log.i("bookinfRef", bookinfRef.size() + "");
                    firestoreRestaurants.update("bookingUser", bookinfRef);
                    bookingRestaurantMutableLiveData.setValue(bookinfRef);
                });
        currentUserDocument.update("bookingUser", newBookingRestaurant);

        //TODO s'abonner à l'évenement sur la detailActivity.class
        return bookingRestaurantMutableLiveData;
    }

    public MutableLiveData<ArrayList<User>> workmatesBookingRestaurantRepo(String restaurantId){
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
                                            for (int j = 0; j < workmateRestaurantBook.size(); j++){
                                                if (workmateRestaurantBook.get(j).getmUid().equals(workmatesBookId.get(i)) ){
                                                    Log.i("ici", "ici " +  workmateRestaurantBook.get(j).getmUid());
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
