package com.riyad.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.DetailRestaurantViewModel;
import com.riyad.go4lunch.viewmodels.RestaurantsViewModel;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.Date;

import static com.riyad.go4lunch.utils.Constants.PERMISSION_TO_CALL;
import static com.riyad.go4lunch.utils.Constants.PLACE_ID;

public class DetailActivity extends AppCompatActivity {

    private TextView restaurantName;
    private ImageView restaurantPicture;
    private TextView restaurantAdress;
    private Button restaurantCall;
    private Button restaurantLike;
    private Button restaurantWebsite;
    private FloatingActionButton fbBookingRestaurant;
    private String restaurantID;
    private String phoneNumber;
    private FirebaseFirestore restaurantDb = FirebaseFirestore.getInstance();
    private DetailRestaurantViewModel detailRestaurantViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        restaurantName = findViewById(R.id.detail_activity_tv_name_restaurant);
        restaurantPicture = findViewById(R.id.detail_activity_iv_restaurant_picture);
        restaurantAdress = findViewById(R.id.detail_activity_tv_restaurant_adress);
        restaurantCall = findViewById(R.id.detail_activity_btn_call);
        restaurantLike = findViewById(R.id.detail_activity_btn_like);
        restaurantWebsite = findViewById(R.id.detail_activity_btn_website);
        fbBookingRestaurant = findViewById(R.id.detail_activity_fbtn_booking_restaurant);

        restaurantID = getIntent().getStringExtra(PLACE_ID);


        displayDetailRestaurant(restaurantID);

    }


    private void displayDetailRestaurant(String restaurantId) {


        detailRestaurantViewModel = ViewModelProviders.of(DetailActivity.this).get(DetailRestaurantViewModel.class);
        detailRestaurantViewModel.init(restaurantId);
        detailRestaurantViewModel.getDetailRestaurant().observe(DetailActivity.this, restaurantDetail -> {
            restaurantName.setText(restaurantDetail.getName());
            restaurantAdress.setText(restaurantDetail.getRestaurantAdress());
            Glide.with(restaurantPicture).load(restaurantDetail.getRestaurantImageUrl()).centerCrop().into(restaurantPicture);


            restaurantCall.setOnClickListener(v -> callPhoneNumber(restaurantDetail.getRestaurantDetail().getFormattedNumber()));
            restaurantWebsite.setOnClickListener(v -> displayWebsite(restaurantDetail.getRestaurantDetail().getWebsite()));
            fbBookingRestaurant.setOnClickListener(v ->
                    bookRestaurantNew(restaurantId)
            //        bookingRestaurant(restaurantID, getCurrentUser().getUid())
            );
        });



    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void callPhoneNumber(String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            this.phoneNumber = phoneNumber;
            ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_TO_CALL);
        } else {

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

    }

    public void displayWebsite(String websiteUrl) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(websiteUrl));
        startActivity(intent);

    }

    public void bookRestaurantNew(String restaurantId){
        detailRestaurantViewModel.getBookingRestaurantMutableLiveData(restaurantId)
                .observe(DetailActivity.this, bookingRestaurants -> {
            if() {
                //TODO mettre le fba en vert.

            }else{
                //TODO mettre en gris.
            }

        });
    }

    public void bookingRestaurant (String restaurantID, String currentUser){

        DocumentReference firestoreRestaurants = restaurantDb.collection("restaurants").document(restaurantID);
        DocumentReference currentUserDocument = restaurantDb.collection("user").document(getCurrentUser().getUid());

        BookingRestaurant newBookingRestaurant = new BookingRestaurant();
        ArrayList<BookingRestaurant> bookinfRef = new ArrayList<>();
        Timestamp currentTime = new Timestamp(new Date());
        newBookingRestaurant.setUserId(currentUser);
        newBookingRestaurant.setRestaurantId(restaurantID);
        newBookingRestaurant.setTimestamp(currentTime);


        bookinfRef.add(newBookingRestaurant);


        firestoreRestaurants.update("bookingUser", bookinfRef);
        currentUserDocument.update("bookingUser", bookinfRef);
//        firestoreRestaurants.set()
//        firestoreRestaurants.update()
//        firestoreRestaurants.update(restaurant.getBookingUser(), new Timestamp(new Date() ).




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {

            case PERMISSION_TO_CALL:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    callPhoneNumber(phoneNumber);

                } else {
                    Toast.makeText(this, "Cannot make call without your permission", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


}
