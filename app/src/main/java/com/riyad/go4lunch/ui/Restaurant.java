package com.riyad.go4lunch.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {

    private String name;
    private Double rating;
    private Double lat;
    private Double lng;
    private String restaurantImageUrl;
    private String restaurantStyle;

    public Restaurant(String name, Double rating, Double lat, Double lng, String restaurantImageUrl, String restaurantStyle) {
        this.name = name;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.restaurantImageUrl = restaurantImageUrl;
        this.restaurantStyle = restaurantStyle;
    }

    //GETTERS

    public String getName() { return name; }
    public Double getRating() { return rating; }
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }
    public String getRestaurantImageUrl() { return restaurantImageUrl; }
    public String getRestaurantStyle() { return restaurantStyle; }
    //SETTERS

    public void setName(String name) { this.name = name; }
    public void setRating(Double rating) { this.rating = rating; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
    public void setRestaurantImageUrl(String restaurantImageUrl) { this.restaurantImageUrl = restaurantImageUrl; }
    public void setRestaurantStyle(String restaurantStyle) { this.restaurantStyle = restaurantStyle; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.restaurantImageUrl);
        dest.writeString(this.restaurantStyle);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.rating);

    }

    Restaurant(Parcel in) {
        this.name = in.readString();
        this.rating = in.readDouble();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.restaurantImageUrl = in.readString();
        this.restaurantStyle = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) { return new Restaurant(in); }

        @Override
        public Restaurant[] newArray(int size) { return new Restaurant[size]; }
    };


}
