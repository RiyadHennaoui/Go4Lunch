package com.riyad.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {
    private TextView textInputEditTextUsername;
    private TextView textViewEmail;
    private ImageView imageViewProfile;
    private ImageView addProfilePicture;
    private Button logoutProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_v2);

        textViewEmail = findViewById(R.id.profile_tv_mail);
        textInputEditTextUsername = findViewById(R.id.profile_tv_name);
        imageViewProfile = findViewById(R.id.profile_iv_profile);
        addProfilePicture = findViewById(R.id.profile_iv_add_picture);
        logoutProfile = findViewById(R.id.profile_btn_logout);



        this.isCurrentUserLogged();
        this.updateUIWhenCreating();
    }

    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    private Boolean isCurrentUserLogged(){ return (this.getCurrentUser() !=null); }

    private void updateUIWhenCreating(){



        if (this.getCurrentUser().getPhotoUrl() !=null){
            addProfilePicture.setVisibility(View.GONE);
            Glide.with(this)
                    .load(this.getCurrentUser().getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);
        }else{

            addProfilePicture.setOnClickListener((View v)-> {
               //TODO charger une photo.

            });
//            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
//                    .setPhotoUri(Uri.parse())

        }

        String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ?
               getString(R.string.info_no_email_found)  : this.getCurrentUser().getEmail();
        String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ?
                getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();

        this.textInputEditTextUsername.setText(username);
        this.textViewEmail.setText(email);

    }
}
