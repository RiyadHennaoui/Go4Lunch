package com.riyad.go4lunch.model;

import com.google.firebase.Timestamp;

public class BookingRestaurant {

    private String restaurantId;
    private Timestamp timestamp;

    public BookingRestaurant() { } // needed for firestore.

    public BookingRestaurant(String restaurantId, Timestamp timestamp) {

        this.restaurantId = restaurantId;
        this.timestamp = timestamp;
    }

    public String getRestaurantId() { return restaurantId; }
    public Timestamp getTimestamp() { return timestamp; }

    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
