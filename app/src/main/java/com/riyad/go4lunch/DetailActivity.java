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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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
            fbBookingRestaurant.setOnClickListener(v -> bookRestaurant(restaurantId));
            restaurantLike.setOnClickListener(v -> likeThisRestaurant(restaurantId));
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

    public void bookRestaurant(String restaurantId){
        detailRestaurantViewModel.getBookingRestaurantMutableLiveData(restaurantId)
                .observe(DetailActivity.this, bookingRestaurants -> {

                    for (int i = 0; i < bookingRestaurants.size(); i++) {
                        if (bookingRestaurants.get(i).getUserId().equals(getCurrentUser().getUid())) {
                            //TODO mettre le fba en vert.
                            Log.i("detailActivity", "oui il est la bordel");
                            fbBookingRestaurant.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_clear_24));

                        } else {
                            //TODO mettre en gris.
                            Log.i("detailActivity", "non il est pas la bordel");
                            fbBookingRestaurant.setImageResource(R.drawable.ic_baseline_check_24);
                        }
                    }

        });
    }

    public void likeThisRestaurant(String restaurantId){
        detailRestaurantViewModel.getRestaurantLikes(restaurantId)
                .observe(DetailActivity.this, like -> {

                    for (int i = 0; i < like.size(); i++){
                        if (like.get(i).getUserId().equals(getCurrentUser().getUid())){
                            restaurantLike.setBackground(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                        }else{
                            restaurantLike.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_border_24));
                        }
                    }

                });
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
