package com.riyad.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.riyad.go4lunch.viewmodels.DetailRestaurantViewModel;

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

    private void displayDetailRestaurant (String restaurantId){

        DetailRestaurantViewModel detailRestaurantViewModel;
        detailRestaurantViewModel = ViewModelProviders.of(DetailActivity.this).get(DetailRestaurantViewModel.class);
        detailRestaurantViewModel.init(restaurantId);
        detailRestaurantViewModel.getDetailRestaurant().observe(DetailActivity.this, restaurantDetail -> {
            restaurantName.setText(restaurantDetail.getName());
            restaurantAdress.setText(restaurantDetail.getFormatedAdress());
        });

    }


}
