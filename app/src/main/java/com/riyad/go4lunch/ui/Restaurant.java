package com.riyad.go4lunch.ui;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.riyad.go4lunch.AppControler;
import com.riyad.go4lunch.data.Location;
import com.riyad.go4lunch.model.User;

import java.util.ArrayList;

public class Restaurant implements Parcelable {

    private String id;
    private String documentId;
    private String name;
    private String rating;
    private String restaurantImageUrl;
    private Location restaurantLocation;
    private String restaurantAdress;
    private RestaurantDetail restaurantDetail;
    private ArrayList<Pair<User, Timestamp>> bookingUser = new ArrayList<>();
    private ArrayList<Pair<User, Integer>> ratedUser = new ArrayList<>();


    public Restaurant() {
    }

    public Restaurant(String id, String name, String rating, String restaurantImageUrl, Location restaurantLocation, String restaurantAdress) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.restaurantImageUrl = restaurantImageUrl;
        this.restaurantLocation = restaurantLocation;
        this.restaurantAdress = restaurantAdress;

    }

    //GETTERS
    public String  getId() { return id; }
    @Exclude
    public String getDocumentId() { return documentId; }
    public String getName() { return name; }
    public String getRating() { return rating; }
    public String getRestaurantImageUrl() { return restaurantImageUrl; }
    public Location getRestaurantLocation() { return restaurantLocation; }

    public void setBookingUser(ArrayList<Pair<User, Timestamp>> bookingUser) {
        this.bookingUser = bookingUser;
    }

    public String getRestaurantAdress() { return restaurantAdress; }
    public RestaurantDetail getRestaurantDetail() {
        return restaurantDetail;
    }

    //SETTERS
    public void setId(String id) { this.id = id; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public void setName(String name) { this.name = name; }
    public void setRating(String rating) { this.rating = rating; }
    public void setRestaurantImageUrl(String restaurantImageUrl) { this.restaurantImageUrl = restaurantImageUrl; }
    public void setRestaurantAdress(String restaurantAdress) { this.restaurantAdress = restaurantAdress; }

    public void setRestaurantDetail(RestaurantDetail restaurantDetail) {
        this.restaurantDetail = restaurantDetail;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.restaurantImageUrl);
        dest.writeString(this.rating);

    }

    Restaurant(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.rating = in.readString();
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
        placeLocation.setLatitude(this.getRestaurantLocation().getLat());
        placeLocation.setLongitude(this.getRestaurantLocation().getLng());
        int distance = (int) AppControler.getInstance().getCurrentLocation().distanceTo(placeLocation);

        return distance;
    }

    public String getDistanceAsString(){
        return getDistance() + "";
    }

}
