package com.riyad.go4lunch.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {

    private String name;
    private String rating;
    private String lat;
    private String lng;
    private String restaurantImageUrl;


    public Restaurant(String name, String rating, String lat, String lng, String restaurantImageUrl) {
        this.name = name;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.restaurantImageUrl = restaurantImageUrl;

    }

    //GETTERS
    public String getName() { return name; }
    public String getRating() { return rating; }
    public String getLat() { return lat; }
    public String getLng() { return lng; }
    public String getRestaurantImageUrl() { return restaurantImageUrl; }

    //SETTERS
    public void setName(String name) { this.name = name; }
    public void setRating(String rating) { this.rating = rating; }
    public void setLat(String lat) { this.lat = lat; }
    public void setLng(String lng) { this.lng = lng; }
    public void setRestaurantImageUrl(String restaurantImageUrl) { this.restaurantImageUrl = restaurantImageUrl; }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.restaurantImageUrl);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.rating);

    }

    Restaurant(Parcel in) {
        this.name = in.readString();
        this.rating = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.restaurantImageUrl = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) { return new Restaurant(in); }

        @Override
        public Restaurant[] newArray(int size) { return new Restaurant[size]; }
    };


}
