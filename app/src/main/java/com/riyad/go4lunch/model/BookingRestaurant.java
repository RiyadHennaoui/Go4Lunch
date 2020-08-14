package com.riyad.go4lunch.model;

import com.google.firebase.Timestamp;

public class BookingRestaurant {
    private String userId;
    private String restaurantId;
    private Timestamp timestamp;

    public BookingRestaurant() { } // needed for firestore.

    public BookingRestaurant(String userId, String restaurantId, Timestamp timestamp) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public String getRestaurantId() { return restaurantId; }
    public Timestamp getTimestamp() { return timestamp; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
