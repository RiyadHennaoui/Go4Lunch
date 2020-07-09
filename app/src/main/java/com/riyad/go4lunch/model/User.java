package com.riyad.go4lunch.model;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.riyad.go4lunch.ui.Restaurant;

import java.sql.Timestamp;
import java.util.ArrayList;

public class User {


    private String mUid;
    private String mUsername;
    private String mMail;
    @Nullable private String mUrlPicture;
    private ArrayList<Pair<Restaurant, com.google.firebase.Timestamp>> bookingRestaurant = new ArrayList<>();
    private ArrayList<Pair<Restaurant, Integer>> ratedRestaurant = new ArrayList<>();


    public User() { } // Needed for Firebase

    public User (String mUid, String mUsername, String mMail, String mUrlPicture){

        this.mUid = mUid;
        this.mUsername = mUsername;
        this.mMail = mMail;
        this.mUrlPicture = mUrlPicture;


    }

    // --- GETTERS --
    @Nullable
    public String getmUrlPicture() { return mUrlPicture; }
    public String getmUid() { return mUid; }
    public String getmUsername() { return mUsername; }
    public String getmMail() { return mMail; }


    // --- SETTERS ---

    public void setmUid(String mUid) { this.mUid = mUid; }
    public void setmUsername(String mUsername) { this.mUsername = mUsername; }
    public void setmUrlPicture(@Nullable String mUrlPicture) { this.mUrlPicture = mUrlPicture; }
    public void setmMail(String mMail) { this.mMail = mMail; }
}
