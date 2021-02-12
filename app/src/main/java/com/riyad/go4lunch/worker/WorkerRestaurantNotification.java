package com.riyad.go4lunch.worker;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.riyad.go4lunch.MainActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_RESTAURANTS_NAME;
import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class WorkerRestaurantNotification extends Worker {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference restaurantDb;
    private User user;
    private Restaurant restaurant;


    public WorkerRestaurantNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        restaurantDb = firebaseFirestore.collection(COLLECTION_RESTAURANTS_NAME);
        getCurrentUserInFirestore();
        return Result.success();
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void getCurrentUserInFirestore() {

        DocumentReference userDocument = firebaseFirestore.collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid());

        userDocument.get()
                .addOnCompleteListener(task -> {

                    DocumentSnapshot userDocumentFind = task.getResult();
                    user = userDocumentFind.toObject(User.class);

                    getRestaurantBookByCurrentUser(user);

                });

    }

    private void getRestaurantBookByCurrentUser(User currentUser) {

        String restaurantIdFound = currentUser.getBookingRestaurant().getRestaurantId();

        if (restaurantIdFound != null){
            restaurantDb.document(restaurantIdFound)
                    .get()
                    .addOnCompleteListener(task -> {

                        DocumentSnapshot documentSnapshot = task.getResult();
                        restaurant = documentSnapshot.toObject(Restaurant.class);
                        String usersNamebook = "";
                        for (int i = 0; i < restaurant.getBookingRestaurant().size(); i++){
                            usersNamebook = formatingWorkmateBookingListInNotification(usersNamebook, i);
                        }

                        showNotification(restaurant.getRestaurantAdress(), restaurant.getName(), usersNamebook);
                    });
        }


    }

    private String formatingWorkmateBookingListInNotification(String usersNamebook, int i) {
        if(!restaurant.getBookingRestaurant().get(i).getmUid().equals(getCurrentUser().getUid())){
            if (i == restaurant.getBookingRestaurant().size()- 1) {
                usersNamebook += " and " + restaurant.getBookingRestaurant().get(i).getmUsername() + ".";
            }else if (i == 0){
                usersNamebook += restaurant.getBookingRestaurant().get(i).getmUsername();
            }else{
                usersNamebook += " ," + restaurant.getBookingRestaurant().get(i).getmUsername();
            }
        }
        return usersNamebook;
    }


    //TODO récuperer la liste des users qui ont reservé sans oublier d'enlever le current user.
    public static void periodRequest(Context context) {


        Calendar noonDate = Calendar.getInstance();
        noonDate.set(Calendar.HOUR_OF_DAY, 12);
        noonDate.set(Calendar.MINUTE, 00);
        noonDate.set(Calendar.SECOND, 00);
        Calendar actualDate = Calendar.getInstance();


        Long delay = noonDate.getTimeInMillis() - actualDate.getTimeInMillis();
        Log.e("delay", (delay / 60000) + "");
        if (delay < 0){
            Calendar tomorrowDate = Calendar.getInstance();
            tomorrowDate.add(Calendar.HOUR_OF_DAY, 24);
            delay = tomorrowDate.getTimeInMillis() - actualDate.getTimeInMillis() - Math.abs(delay);
            Log.e("delayIf", (delay / 60000) + "");
        }
        //TODO trouver comment faire pour avoir la notification tous les jours à 12h.

        OneTimeWorkRequest periodicWorkRequest = new OneTimeWorkRequest.Builder(WorkerRestaurantNotification.class)
                .build();


        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
    }




    public void showNotification(String restaurantAdress, String restaurantName, String utilisateurs) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        //TODO trouver comment faire pour les extractions de ressources. 

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(getApplicationContext(), "4")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setContentTitle("Your reservation in  " + restaurantName)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("with you : " + utilisateurs)
                        .addLine("restaurant adress : ")
                        .addLine(restaurantAdress)
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(4, notificationCompat.build());

    }



}
