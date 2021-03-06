package com.riyad.go4lunch.utils;

public class Constants {


    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 9003;

    // Default zoom to use when location permission is not granted
    public static final int DEFAULT_ZOOM = 15;

    // Key for storing activity state.
    public static final String KEY_CAMERA_POSITION = "camera_position";
    public static final String KEY_Location= "location";


    //for collections
    public static final String COLLECTION_USER_NAME = "user";
    public static final String COLLECTION_RESTAURANTS_NAME = "restaurants";
    public static final String FIELD_RATING_USER_FOR_RESTAURANT_DOCUMENT = "ratingUser";
    public static final String FIELD_BOOKING_USER_FOR_RESTAURANT_DOCUMENT = "bookingRestaurant";

    //for orderBy method fields
    public static final String ORDERBY_CREATED_DATE = "createdDate";
    public static final String ORDERBY_USERNAME = "username";
    public static final String ORDERBY_BOOKING_RESTAURANT = "bookingRestaurant";

    //for message type
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    //Hour format for chats
    public static final String HOUR_FORMAT = "HH:mm";

    //Places, Nearby Search requests
    public static final String RESTAURANT_TYPE = "restaurant";
    public static final String BASE_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public static final String RADIUS_FOR_RESTAURANT_SEARCH = "1500";

    //Shared Preferences Key
    public static final String CURRENT_DEVICE_LOCATION = "CURRENT_DEVICE_LOCATION";
    public static final String SHARED_NAME = "go4lunch";

    //ids
    public static final String PLACE_ID = "PLACE_ID";
    public static final String WORKMATE_ID = "workmateId";
    public static final String WORKMATE_PICTURE_URL = "workmatePictureUrl";
    public static final String WORKMATE_USERNAME = "workmateUsername";

    //for Permission to call
    public static final int  PERMISSION_TO_CALL = 123;

    //for Sign out Firebase Auth
    public static final int SIGN_OUT_TASK = 20;

    //for Place Auto Complete
    public static final int AUTOCOMPLETE_REQUEST_CODE = 300;

    //for detail restaurant field
    public static final String DETAIL_RESTAURANT_FIELD = "name,formatted_phone_number,opening_hours,photos,website,vicinity,formatted_address";

    //for notifications
    public static final String CHANNEL_NAME = "ChannelNameGo4Lunch";
    public static final String CHANNEL_DESCRIPTON = "ChannelDesciption";
    public static final String CHANNEL_ID = "4";




}
