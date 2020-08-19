package com.riyad.go4lunch.utils;

public class Constants {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSION_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 9003;

    // Default zoom to use when location permission is not granted
    public static final int DEFAULT_ZOOM = 15;

    // Key for storing activity state.
    public static final String KEY_CAMERA_POSITION = "camera_position";
    public static final String KEY_Location= "location";

    // Used for selecting the current place.
    public static final int M_MAX_ENTRIES = 10;

    // Used for saved instance state
    public final String SAVED_INSTANCE_STATE_KEY = "SAVED_INSTANCE_STATE_KEY";

    //for collections
    public static final String COLLECTION_USER_NAME = "user";
    public static final String COLLECTION_RESTAURANTS_NAME = "restaurants";

    //for message type
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    //Places, Nearby Search requests
    public static final String API_KEY_PLACES = "AIzaSyA55BEn4vL47MvxLsWLR_Nvlcd0uawEVdQ";
    public static final String RESTAURANT_TYPE = "restaurant";
    public static final String BASE_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

    //Shared Preferences Key
    public static final String CURRENT_DEVICE_LOCATION = "CURRENT_DEVICE_LOCATION";

    //Place id
    public static final String PLACE_ID = "PLACE_ID";

    //for Permission to call
    public static final int  PERMISSION_TO_CALL = 123;




}
