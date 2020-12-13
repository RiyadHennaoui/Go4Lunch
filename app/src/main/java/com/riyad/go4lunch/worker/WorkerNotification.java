package com.riyad.go4lunch.worker;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.riyad.go4lunch.MainActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
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


        CountDownLatch countDownLatch = new CountDownLatch(1);
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

        Log.e("getCurrentUserInFire", "Avant Lambda");
        userDocument.get()
                .addOnCompleteListener(task -> {

                    DocumentSnapshot userDocumentFind = task.getResult();
                    user = userDocumentFind.toObject(User.class);

                    getRestaurantBookByCurrentUser(user);

                    Log.e("getCurrentUserWorker", user.getmUsername() + " / " + user.getBookingRestaurant().getRestaurantId());
                });

    }
    //TODO si le booking restaurant n'est pas null, prendre l'id du restaurant et récuperer ce restaurant dans firestore.

    private void getRestaurantBookByCurrentUser(User currentUser) {

        String testFirestoreDataMapMail = currentUser.getmMail();
        String testFirestoreDataMapId = currentUser.getmUid();
        String testFirestoreDataMapUrlPicture = currentUser.getmUrlPicture();
        String testFirestoreDataMapObjectBook = currentUser.getBookingRestaurant().toString();
        String restaurantIdFound = currentUser.getBookingRestaurant().getRestaurantId();

        restaurantDb.document(restaurantIdFound)
                .get()
                .addOnCompleteListener(task -> {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    restaurant = documentSnapshot.toObject(Restaurant.class);

                    Log.e("le resteau", restaurant.getName());

                    showNotification(restaurant.getRestaurantAdress(), restaurant.getName(), getCurrentUser().getDisplayName());
                });

    }


//    private BookingRestaurant getBoonkingRestaurantForCurrentUser() {
//
//        BookingRestaurant bookingRestaurant = new BookingRestaurant();
//        userDb = firebaseFirestore.collection(COLLECTION_USER_NAME);
//        DocumentReference userDoc = userDb.document(getCurrentUser().getUid());
//
//        userDoc
//                .get()
//                .addOnCompleteListener(task -> {
//                    User currentUser;
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    currentUser = documentSnapshot.toObject(User.class);
//                    Gson gson = new Gson();
//                    Log.e("CurrentUser", gson.toJson(currentUser));
//                    bookingRestaurant.setRestaurantId(currentUser.getBookingRestaurant().getRestaurantId());
//                });
//
//        return bookingRestaurant;
//    }

    //TODO récuperer la liste des users qui ont reservé sans oublier d'enlever le current user.
    private ArrayList<User> getAllUsersBookRestaurant(String restaurantId) {


        ArrayList<String> usersNamesBookFormated = new ArrayList<>();
        ArrayList<User> bookingUserList = new ArrayList<>();
        workmatesBookId = new ArrayList<>();


        if (restaurantId != null) {
            restaurantDb = firebaseFirestore.collection(COLLECTION_RESTAURANTS_NAME);
            DocumentReference userIddocumentReference = restaurantDb.document(restaurantId);


            userIddocumentReference.get()
                    .addOnCompleteListener(task -> {
                        Restaurant currentRestaurantBook;
                        DocumentSnapshot documentSnapshot = task.getResult();
                        currentRestaurantBook = documentSnapshot.toObject(Restaurant.class);

                        for (int i = 0; i < currentRestaurantBook.getBookingRestaurant().size(); i++) {
                            workmatesBookId.add(currentRestaurantBook.getBookingRestaurant().get(i).getmUid());
                        }
                    });


            userDb.get()
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
                                        bookingUserList.add(workmateRestaurantBook.get(j));
                                        usersNamesBookFormated.add(workmateRestaurantBook.get(j).getmUsername());
                                        //  }
                                    }
                                }
                            }

                        }
                    });


        }

        return bookingUserList;
    }


    public static void periodRequest() {

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(WorkerNotification.class,
                1, TimeUnit.SECONDS)
                .setInitialDelay(15, TimeUnit.SECONDS)
//                .setConstraints(setConst())
                .build();


        WorkManager.getInstance().enqueueUniquePeriodicWork("periodic", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }


    public void showNotification(String restaurantAdress, String restaurantName, String utilisateurs) {

        Log.e("Notif", "here");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(getApplicationContext(), "4")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setContentTitle("Votre reservation au : " + restaurantName)
                .setContentText("avec vous : ")
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("avec vous : \n le restaurant est : " + restaurantAdress)
                        .addLine("2 eme ligne")
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(4, notificationCompat.build());

    }

    private String getUsersNames(ArrayList<User> utilisitaeurs) {

        String listUtilisateurs = "Moi ";

        for (int i = 0; i < utilisitaeurs.size(); i++) {
            listUtilisateurs += utilisitaeurs.get(i).getmUsername() + ", \n ";

        }

        return listUtilisateurs;
    }

}
