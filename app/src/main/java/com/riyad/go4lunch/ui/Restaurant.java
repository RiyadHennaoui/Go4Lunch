package com.riyad.go4lunch.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.riyad.go4lunch.AppControler;
import com.riyad.go4lunch.data.Location;

public class Restaurant implements Parcelable {

    private String id;
    private String name;
    private String rating;
    private Double lat;
    private Double lng;
    private String restaurantImageUrl;
    private Location restaurantLocation;



    public Restaurant(String id, String name, String rating, Double lat, Double lng, String restaurantImageUrl, Location restaurantLocation) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.restaurantImageUrl = restaurantImageUrl;
        this.restaurantLocation = restaurantLocation;

    }

    //GETTERS
    public String getId() { return id; }
    public String getName() { return name; }
    public String getRating() { return rating; }
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }
    public String getRestaurantImageUrl() { return restaurantImageUrl; }
    public Location getRestaurantLocation() { return restaurantLocation; }

    //SETTERS
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRating(String rating) { this.rating = rating; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
    public void setRestaurantImageUrl(String restaurantImageUrl) { this.restaurantImageUrl = restaurantImageUrl; }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.restaurantImageUrl);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.rating);

    }

    Restaurant(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.rating = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.restaurantImageUrl = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) { return new Restaurant(in); }

        @Override
        public Restaurant[] newArray(int size) { return new Restaurant[size]; }
    };

    public int getDistance(){

        android.location.Location placeLocation = new android.location.Location("");
        placeLocation.setLatitude(this.lat);
        placeLocation.setLongitude(this.lng);
        int distance = (int) AppControler.getInstance().getCurrentLocation().distanceTo(placeLocation);

        return distance;
    }

    public String getDistanceAsString(){
        return getDistance() + "";
    }

}
