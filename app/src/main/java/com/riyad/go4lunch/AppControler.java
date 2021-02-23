package com.riyad.go4lunch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.riyad.go4lunch.worker.WorkerDelateRestaurant;
import com.riyad.go4lunch.worker.WorkerRestaurantNotification;

import static com.riyad.go4lunch.utils.Constants.CHANNEL_DESCRIPTON;
import static com.riyad.go4lunch.utils.Constants.CHANNEL_ID;
import static com.riyad.go4lunch.utils.Constants.CHANNEL_NAME;

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
        WorkerRestaurantNotification.periodRequest(getApplicationContext());
        WorkerDelateRestaurant.deleteRestaurantsPeriodRequest(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
        MultiDex.install(this);
    }

    public Location getCurrentLocation() { return currentLocation; }

    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTON);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
