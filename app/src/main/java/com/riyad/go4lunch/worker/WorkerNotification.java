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
import com.riyad.go4lunch.MainActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_RESTAURANTS_NAME;
import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class WorkerNotification extends Worker {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference userDb;
    private CollectionReference restaurantDb;
    private ArrayList<String> workmatesBookId = new ArrayList<>();
    private User user;
    private Restaurant restaurant;


    public WorkerNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("DoWork", "inDo work");
        //TODO revoir la modélisation suite au problème de BookingRestaurant et BoonkingUser;
        //TODO appel firestore pour récupérer l'utilisateur
        //TODO si le booking restaurant n'est pas null, prendre l'id du restaurant et récuperer ce restaurant dans firestore.
        //TODO récuperer la liste des users qui ont reservé sans oublier d'enlever le current user.
        //TODO appeler show notification.



        restaurantDb = firebaseFirestore.collection(COLLECTION_RESTAURANTS_NAME);


        getCurrentUserInFirestore();


        return Result.success();
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //TODO appel firestore pour récupérer l'utilisateur
    private void getCurrentUserInFirestore() {

        DocumentReference userDocument = firebaseFirestore.collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid());

        userDocument.get()
                .addOnCompleteListener(task -> {

                    DocumentSnapshot userDocumentFind = task.getResult();
                    user = userDocumentFind.toObject(User.class);

                    getRestaurantBookByCurrentUser(user);

                });

    }
    //TODO si le booking restaurant n'est pas null, prendre l'id du restaurant et récuperer ce restaurant dans firestore.

    private void getRestaurantBookByCurrentUser(User currentUser) {

        String restaurantIdFound = currentUser.getBookingRestaurant().getRestaurantId();

        if (restaurantIdFound != null){
            restaurantDb.document(restaurantIdFound)
                    .get()
                    .addOnCompleteListener(task -> {

                        DocumentSnapshot documentSnapshot = task.getResult();
                        restaurant = documentSnapshot.toObject(Restaurant.class);
                        String usersNamebook = "";
                        Log.e("le resteau", restaurant.getName());
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
                usersNamebook += " et " + restaurant.getBookingRestaurant().get(i).getmUsername() + ".";
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

        Long delay = getDelayofSendNotificationRestaurantBook();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(WorkerNotification.class,
                24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();


        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
//        WorkManager.getInstance().enqueueUniquePeriodicWork("periodic", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    @NotNull
    private static Long getDelayofSendNotificationRestaurantBook() {
        Calendar noonDate = Calendar.getInstance();
        noonDate.set(Calendar.HOUR_OF_DAY, 12);
        noonDate.set(Calendar.MINUTE, 0);
        noonDate.set(Calendar.SECOND, 0);
        Calendar actualDate = Calendar.getInstance();


        Long delay = noonDate.getTimeInMillis() - actualDate.getTimeInMillis();
        Log.e("delay", (delay / 60000) + "");
        if (delay < 0){
            Calendar tomorrowDate = Calendar.getInstance();
            tomorrowDate.add(Calendar.HOUR_OF_DAY, 24);
            delay = tomorrowDate.getTimeInMillis() - actualDate.getTimeInMillis() - Math.abs(delay);
            Log.e("delayIf", (delay / 60000) + "");
        }
        return delay;
    }


    public void showNotification(String restaurantAdress, String restaurantName, String utilisateurs) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(getApplicationContext(), "4")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setContentTitle("Votre reservation au  " + restaurantName)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("avec vous : " + utilisateurs)
                        .addLine("adresse du restaurant : ")
                        .addLine(restaurantAdress)
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(4, notificationCompat.build());

    }



}
