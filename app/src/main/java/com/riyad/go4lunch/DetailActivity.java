package com.riyad.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.riyad.go4lunch.viewmodels.DetailRestaurantViewModel;

import static com.riyad.go4lunch.utils.Constants.PERMISSION_TO_CALL;
import static com.riyad.go4lunch.utils.Constants.PLACE_ID;

public class DetailActivity extends AppCompatActivity {

    private TextView restaurantName;
    private ImageView restaurantPicture;
    private TextView restaurantAdress;
    private Button restaurantCall;
    private Button restaurantLike;
    private Button restaurantWebsite;
    private FloatingActionButton bookingRestaurant;
    private String restaurantID;
    private String phoneNumber;

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
        bookingRestaurant = findViewById(R.id.detail_activity_fbtn_booking_restaurant);

        restaurantID = getIntent().getStringExtra(PLACE_ID);
        displayDetailRestaurant(restaurantID);

    }

    private void displayDetailRestaurant(String restaurantId) {

        DetailRestaurantViewModel detailRestaurantViewModel;
        detailRestaurantViewModel = ViewModelProviders.of(DetailActivity.this).get(DetailRestaurantViewModel.class);
        detailRestaurantViewModel.init(restaurantId);
        detailRestaurantViewModel.getDetailRestaurant().observe(DetailActivity.this, restaurantDetail -> {
            restaurantName.setText(restaurantDetail.getName());
            restaurantAdress.setText(restaurantDetail.getFormatedAdress());
            Glide.with(restaurantPicture).load(restaurantDetail.getUrlPicture()).centerCrop().into(restaurantPicture);


            restaurantCall.setOnClickListener(v -> callPhoneNumber(restaurantDetail.getFormattedNumber()));
            restaurantWebsite.setOnClickListener(v -> displayWebsite(restaurantDetail.getWebsite()));
        });


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
