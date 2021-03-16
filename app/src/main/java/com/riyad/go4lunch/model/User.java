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

    //TODO changer le nom des methodes
    // --- GETTERS --
    @Nullable
    public String getmUrlPicture() { return mUrlPicture; }
    public String getmUid() { return mUid; }
    public String getmUsername() { return mUsername; }
    public String getmMail() { return mMail; }
    public BookingRestaurant getBookingRestaurant() { return bookingRestaurant; }


    // --- SETTERS ---

    public void setmUid(String mUid) { this.mUid = mUid; }
    public void setmUsername(String mUsername) { this.mUsername = mUsername; }
    public void setUrlPicture(@Nullable String mUrlPicture) { this.mUrlPicture = mUrlPicture; }
    public void setmMail(String mMail) { this.mMail = mMail; }
    public void setBookingRestaurant(BookingRestaurant bookingRestaurant) { this.bookingRestaurant = bookingRestaurant; }
}
