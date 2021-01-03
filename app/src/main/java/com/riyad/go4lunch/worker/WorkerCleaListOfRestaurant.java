package com.riyad.go4lunch.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.ui.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_RESTAURANTS_NAME;
import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class WorkerCleaListOfRestaurant extends Worker {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference restaurantDb;

    public WorkerCleaListOfRestaurant(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("doWorkdelete", "in");
        restaurantDb = firestore.collection(COLLECTION_RESTAURANTS_NAME);
        setRestaurantsListInFirestore();
        return Result.success();
    }


    private void setRestaurantsListInFirestore(){

        ArrayList<Restaurant> restaurantsList = new ArrayList<>();
        restaurantDb
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       if (!task.getResult().isEmpty()){
                           for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                               Restaurant restaurant = queryDocumentSnapshot.toObject(Restaurant.class);
                               restaurantsList.add(restaurant);
                           }
                       }
                   }
                });

        for (int i= 0;i < restaurantsList.size() ; i++ ){
            restaurantDb
                    .document(restaurantsList.get(i).getId())
                    .delete()
                    .addOnCompleteListener(task -> {
                        Log.e("dans la suppresion", "delate");
                    });
        }

    }

    public static void deleteRestaurantsPeriodRequest(Context context) {

        Long delay = getDelayofSendNotificationRestaurantBook();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(WorkerNotification.class,
                24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();


        WorkManager.getInstance(context).enqueue(periodicWorkRequest);

    }

    @NotNull
    private static Long getDelayofSendNotificationRestaurantBook() {
        Calendar noonDate = Calendar.getInstance();
        noonDate.set(Calendar.HOUR_OF_DAY, 23);
        noonDate.set(Calendar.MINUTE, 59);
        noonDate.set(Calendar.SECOND, 59);
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
