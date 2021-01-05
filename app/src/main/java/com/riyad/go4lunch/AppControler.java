package com.riyad.go4lunch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.riyad.go4lunch.worker.WorkerNotification;

public class AppControler extends Application {

    Location currentLocation;


    private static AppControler instance;
    public static synchronized AppControler getInstance()
    {
        // Return the instance
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createNotificationChannel();
        //TODO a remettre après avoir finaliser la creation du user dans firestore dans splashScnreen activity
        Log.e("dans app", "pour pas de dowork");
        WorkerNotification.periodRequest(getApplicationContext());
//        WorkerCleaListOfRestaurant.deleteRestaurantsPeriodRequest(getApplicationContext());
        WorkerNotification.deleteRestaurantsPeriodRequest(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Location getCurrentLocation() { return currentLocation; }

    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ChannelNameGo4Lunch";
            String description = "ChannelDesciption";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("4", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
