package com.riyad.go4lunch.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_RESTAURANTS_NAME;

public class WorkerDelateRestaurant extends Worker {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference userDb;
    private CollectionReference restaurantDb;
    private ArrayList<String> workmatesBookId = new ArrayList<>();
    private User user;
    private Restaurant restaurant;


    public WorkerDelateRestaurant(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("DoWorkDelate", "inDo work");
        //TODO revoir la modélisation suite au problème de BookingRestaurant et BoonkingUser;
        //TODO appel firestore pour récupérer l'utilisateur
        //TODO si le booking restaurant n'est pas null, prendre l'id du restaurant et récuperer ce restaurant dans firestore.
        //TODO récuperer la liste des users qui ont reservé sans oublier d'enlever le current user.
        //TODO appeler show notification.



        restaurantDb = firebaseFirestore.collection(COLLECTION_RESTAURANTS_NAME);

        List<Restaurant> restaurantList = setRestaurantsListInFirestore();

        suppressAllRestaurant(restaurantList);

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
                                Log.e("restauget", restaurant.getName());
                                restaurantsList.add(restaurant);
                            }
                        }
                    }
                });



        return restaurantsList;

    }

    private void suppressAllRestaurant(List<Restaurant> getRestaurant){
        for (int i= 0;i < getRestaurant.size() ; i++ ){
            Log.e("dans le supp", getRestaurant.get(i).getName());
            restaurantDb
                    .document(getRestaurant.get(i).getId())
                    .delete()
                    .addOnCompleteListener(task -> {
                        Log.e("dans la suppresion", "delate");
                    });
        }


    }

    public static void deleteRestaurantsPeriodRequest(Context context) {

        Long delay = getDelayofremoveRestaurants();

        OneTimeWorkRequest periodicWorkRequest = new OneTimeWorkRequest.Builder(WorkerDelateRestaurant.class)
//                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag("delete, worker")
                .build();


        WorkManager.getInstance(context).enqueue(periodicWorkRequest);

    }

    @NotNull
    private static Long getDelayofremoveRestaurants() {
        Calendar noonDate = Calendar.getInstance();
        noonDate.set(Calendar.HOUR_OF_DAY, 8);
        noonDate.set(Calendar.MINUTE, 38);
        noonDate.set(Calendar.SECOND, 30);
        Calendar actualDate = Calendar.getInstance();


        Long delay = noonDate.getTimeInMillis() - actualDate.getTimeInMillis();
        Log.e("delayde", (delay / 60000) + "");
        if (delay < 0){
            Calendar tomorrowDate = Calendar.getInstance();
            tomorrowDate.add(Calendar.HOUR_OF_DAY, 24);
            delay = tomorrowDate.getTimeInMillis() - actualDate.getTimeInMillis() - Math.abs(delay);
            Log.e("delayIf", (delay / 60000) + "");
        }
        return delay;
    }



}
