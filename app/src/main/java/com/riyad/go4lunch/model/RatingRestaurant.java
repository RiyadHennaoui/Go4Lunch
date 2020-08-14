package com.riyad.go4lunch.model;

public class RatingRestaurant {
    String userId;
    String restaurantId;

    public RatingRestaurant() { } // needed for firebase.

    public RatingRestaurant(String userId, String restaurantId) {
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public String getUserId() { return userId; }
    public String getRestaurantId() { return restaurantId; }


    public void setUserId(String userId) { this.userId = userId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
}
