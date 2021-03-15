package com.riyad.go4lunch.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bolts.Task;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_RESTAURANTS_NAME;
import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;
import static com.riyad.go4lunch.utils.Constants.FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT;

public class WorkerDelateRestaurant extends Worker {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference restaurantDb;
    private CollectionReference usersDb;


    public WorkerDelateRestaurant(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        restaurantDb = firebaseFirestore.collection(COLLECTION_RESTAURANTS_NAME);
        usersDb = firebaseFirestore.collection(COLLECTION_USER_NAME);

        List<Restaurant> restaurantList = setRestaurantsListInFirestore();

        suppressAllRestaurant(restaurantList);
        suppressAllUsersBook();

        return Result.success();
    }

    private List<Restaurant> setRestaurantsListInFirestore(){

        ArrayList<Restaurant> restaurantsList = new ArrayList<>();
        restaurantDb
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (!task.getResult().isEmpty()){

                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                Restaurant restaurant = queryDocumentSnapshot.toObject(Restaurant.class);
                                restaurantsList.add(restaurant);
                                restaurantDb
                                        .document(restaurant.getId())
                                        .delete();
                            }
                        }
                    }
                });

        for (int i= 0;i < restaurantsList.size() ; i++ ){
            restaurantDb
                    .document(restaurantsList.get(i).getId())
                    .delete();
        }

        return restaurantsList;

    }

    private void suppressAllUsersBook(){

        ArrayList<User> users = new ArrayList<>();
        BookingRestaurant emptyBookingrestaurant = new BookingRestaurant();

        usersDb
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       if (!task.getResult().isEmpty()){

                           for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                               User user = queryDocumentSnapshot.toObject(User.class);
                               user.setBookingRestaurant(emptyBookingrestaurant);
                               users.add(user);
                               usersDb.document(user.getmUid())
                                       .set(user)
                                       .addOnSuccessListener(aVoid -> {} );
                           }
                       }
                   }
                });


    }

    private void suppressAllRestaurant(List<Restaurant> getRestaurant){
        for (int i= 0;i < getRestaurant.size() ; i++ ){
            restaurantDb
                    .document(getRestaurant.get(i).getId())
                    .delete();
        }
    }

    public static void deleteRestaurantsPeriodRequest(Context context) {

        Long delay = getDelayofremoveRestaurants();

        OneTimeWorkRequest periodicWorkRequest = new OneTimeWorkRequest.Builder(WorkerDelateRestaurant.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();


        WorkManager.getInstance(context).enqueue(periodicWorkRequest);

    }

    @NotNull
    private static Long getDelayofremoveRestaurants() {
        Calendar noonDate = Calendar.getInstance();
        noonDate.set(Calendar.HOUR_OF_DAY, 23);
        noonDate.set(Calendar.MINUTE, 59);
        noonDate.set(Calendar.SECOND, 59);
        Calendar actualDate = Calendar.getInstance();


        Long delay = noonDate.getTimeInMillis() - actualDate.getTimeInMillis();
        if (delay < 0){
            Calendar tomorrowDate = Calendar.getInstance();
            tomorrowDate.add(Calendar.HOUR_OF_DAY, 24);
            delay = tomorrowDate.getTimeInMillis() - actualDate.getTimeInMillis() - Math.abs(delay);
        }
        return delay;
    }



}
