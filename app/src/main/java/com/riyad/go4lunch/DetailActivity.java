package com.riyad.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.adapter.WorkmatesBookingRestaurantAdapter;
import com.riyad.go4lunch.model.BookingRestaurant;
import com.riyad.go4lunch.model.RatingRestaurant;
import com.riyad.go4lunch.viewmodels.DetailRestaurantViewModel;

import java.util.ArrayList;

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
    private DetailRestaurantViewModel detailRestaurantViewModel;
    private RecyclerView rvDetailActivity;
    private WorkmatesBookingRestaurantAdapter workmatesBookingRestaurantAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        rvDetailActivity = findViewById(R.id.main_rv);

        restaurantID = getIntent().getStringExtra(PLACE_ID);

        detailRestaurantViewModel = ViewModelProviders.of(DetailActivity.this).get(DetailRestaurantViewModel.class);
        detailRestaurantViewModel.init();
        displayDetailRestaurant();
        initRv();
        displayWorkmatesBoonkingThisRestaurant();
    }


    private void displayDetailRestaurant() {

        detailRestaurantViewModel.getDetailRestaurant(restaurantID).observe(DetailActivity.this, restaurantDetail -> {
            Glide.with(restaurantPicture).load(restaurantDetail.getRestaurantImageUrl()).centerCrop().into(restaurantPicture);
            setBookingIcon(restaurantDetail.getBookingRestaurant());
            setRatingIcon(restaurantDetail.getRatingUser());
            fbBookingRestaurant.setOnClickListener(v -> bookRestaurant());
            restaurantLike.setOnClickListener(v -> likeThisRestaurant());
            restaurantName.setText(restaurantDetail.getName());
            restaurantCall.setOnClickListener(v -> callPhoneNumber(restaurantDetail.getRestaurantDetail().getFormattedNumber()));
            restaurantWebsite.setOnClickListener(v -> displayWebsite(restaurantDetail.getRestaurantDetail().getWebsite()));
            restaurantAdress.setText(restaurantDetail.getRestaurantAdress());
        });
    }

    private void displayWorkmatesBoonkingThisRestaurant() {
        //TODO n'affiche pas la liste mais seulement le premier user qui book! a voir
        detailRestaurantViewModel.getWorkmateBookingRestaurantMutableLiveData(restaurantID).observe(DetailActivity.this,
                workmates -> {
                    workmatesBookingRestaurantAdapter = new WorkmatesBookingRestaurantAdapter(workmates);
                    rvDetailActivity.setAdapter(workmatesBookingRestaurantAdapter);
                });
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void callPhoneNumber(String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    public void displayWebsite(String websiteUrl) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(websiteUrl));
        startActivity(intent);

    }

    public void bookRestaurant() {
        detailRestaurantViewModel.getBookingRestaurantMutableLiveData(restaurantID)
                .observe(DetailActivity.this, bookingRestaurants -> {
                    setBookingIcon(bookingRestaurants);
                    displayWorkmatesBoonkingThisRestaurant();
                });

    }

    private void setBookingIcon(ArrayList<BookingRestaurant> bookingRestaurants) {
        boolean isBook = false;
        for (int i = 0; i < bookingRestaurants.size(); i++) {
            if (bookingRestaurants.get(i).getUserId().equals(getCurrentUser().getUid())) {
                isBook = true;
            }
        }
        if (isBook) {
            fbBookingRestaurant.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_clear_24));
        } else {
            fbBookingRestaurant.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_24));
        }
    }

    public void likeThisRestaurant() {
        detailRestaurantViewModel.getRestaurantLikes(restaurantID)
                .observe(DetailActivity.this, this::setRatingIcon);
    }

    private void setRatingIcon(ArrayList<RatingRestaurant> like) {
        boolean isLike = false;
        Log.e("rating", "click");

        for (int i = 0; i < like.size(); i++) {
            if (like.get(i).getUserId().equals(getCurrentUser().getUid())) {
                    isLike = true;
            }
        }
        if (isLike) {
            restaurantLike.setBackground(getResources().getDrawable(R.drawable.ic_star_black_24dp));
        } else {
            restaurantLike.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_border_24));
        }
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

    private void initRv() {
        rvDetailActivity.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(DetailActivity.this);
        rvDetailActivity.setLayoutManager(layoutManager);

    }


}
