package com.riyad.go4lunch.model;

import com.google.firebase.Timestamp;

public class BookingRestaurant {

    private String restaurantId;
    private Timestamp timestamp;
    private String restaurantName;

    public BookingRestaurant() { } // needed for firestore.

    public BookingRestaurant(String restaurantId, Timestamp timestamp, String restaurantName) {

        this.restaurantId = restaurantId;
        this.timestamp = timestamp;
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() { return restaurantId; }
    public Timestamp getTimestamp() { return timestamp; }
    public String getRestaurantName() { return restaurantName; }

    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
}
