package com.riyad.go4lunch;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import androidx.multidex.MultiDex;

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
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Location getCurrentLocation() { return currentLocation; }

    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }


}
