package com.riyad.go4lunch.model;


import androidx.annotation.Nullable;

public class User {


    private String mUid;
    private String mUsername;
    private String mMail;
    @Nullable private String mUrlPicture;
    private BookingRestaurant bookingRestaurant = new BookingRestaurant();



    public User() { } // Needed for Firebase

    public User (String mUid, String mUsername, String mMail,@Nullable String mUrlPicture){

        this.mUid = mUid;
        this.mUsername = mUsername;
        this.mMail = mMail;
        this.mUrlPicture = mUrlPicture;


    }


    // --- GETTERS --
    @Nullable
    public String getUrlPicture() { return mUrlPicture; }
    public String getUid() { return mUid; }
    public String getUsername() { return mUsername; }
    public String getMail() { return mMail; }
    public BookingRestaurant getBookingRestaurant() { return bookingRestaurant; }


    // --- SETTERS ---

    public void setUid(String mUid) { this.mUid = mUid; }
    public void setUsername(String mUsername) { this.mUsername = mUsername; }
    public void setUrlPicture(@Nullable String mUrlPicture) { this.mUrlPicture = mUrlPicture; }
    public void setMail(String mMail) { this.mMail = mMail; }
    public void setBookingRestaurant(BookingRestaurant bookingRestaurant) { this.bookingRestaurant = bookingRestaurant; }
}
