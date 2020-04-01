package com.riyad.go4lunch;

import androidx.annotation.Nullable;

public class User {


    private String mUid;
    private String mUsername;
    private String mFirstname;
    private String mMail;
    @Nullable private String mUrlPicture;
    @Nullable private String mCurrentRestaurant;

    public User() { }

    public User (String mUid, String mUsername, String mMail, String mFirstname, String mUrlPicture, String mCurrentRestaurant){

        this.mUid = mUid;
        this.mUsername = mUsername;
        this.mFirstname = mFirstname;
        this.mMail = mMail;
        this.mUrlPicture = mUrlPicture;
        this.mCurrentRestaurant = mCurrentRestaurant;

    }



    // --- GETTERS --
    public String getmUid() { return mUid; }

    public String getmUsername() { return mUsername; }
    public String getmFirstname() { return mFirstname; }

    @Nullable
    public String getmUrlPicture() { return mUrlPicture; }

    public String getmMail() { return mMail; }

    @Nullable
    public String getmCurrentRestaurant() { return mCurrentRestaurant; }


    // --- SETTERS ---

    public void setmUid(String mUid) { this.mUid = mUid; }
    public void setmUsername(String mUsername) { this.mUsername = mUsername; }

    public void setmFirstname(String mFirstname) { this.mFirstname = mFirstname; }

    public void setmUrlPicture(@Nullable String mUrlPicture) { this.mUrlPicture = mUrlPicture; }

    public void setmCurrentRestaurant(@Nullable String mCurrentRestaurant) { this.mCurrentRestaurant = mCurrentRestaurant; }

    public void setmMail(String mMail) { this.mMail = mMail; }
}
