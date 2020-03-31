package com.riyad.go4lunch;

import androidx.annotation.Nullable;

public class User {


    private String mUid;
    private String mUsername;
    private String mFirstname;
    @Nullable private String mUrlPicture;
    @Nullable private String mCurrentRestaurent;

    public User() { }

    public User ( String mUid, String mUsername, String mFirstname, String mUrlPicture, String mCurrentRestaurent){

        this.mUid = mUid;
        this.mUsername = mUsername;
        this.mFirstname = mFirstname;
        this.mUrlPicture = mUrlPicture;
        this.mCurrentRestaurent = mCurrentRestaurent;

    }


    // --- GETTERS --
    public String getmUid() { return mUid; }

    public String getmUsername() { return mUsername; }

    public String getmFirstname() { return mFirstname; }

    @Nullable
    public String getmUrlPicture() { return mUrlPicture; }

    @Nullable
    public String getmCurrentRestaurent() { return mCurrentRestaurent; }


    // --- SETTERS ---
    public void setmUid(String mUid) { this.mUid = mUid; }

    public void setmUsername(String mUsername) { this.mUsername = mUsername; }

    public void setmFirstname(String mFirstname) { this.mFirstname = mFirstname; }

    public void setmUrlPicture(@Nullable String mUrlPicture) { this.mUrlPicture = mUrlPicture; }

    public void setmCurrentRestaurent(@Nullable String mCurrentRestaurent) { this.mCurrentRestaurent = mCurrentRestaurent; }
}
