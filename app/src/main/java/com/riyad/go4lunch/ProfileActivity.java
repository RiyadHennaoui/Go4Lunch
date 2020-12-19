package com.riyad.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class ProfileActivity extends AppCompatActivity {
    private TextView textInputEditTextUsername;
    private TextView textViewEmail;
    private ImageView imageViewProfile;
    private ImageView addProfilePicture;
    private ImageView addProfileUsername;
    private Button logoutProfile;
    FirebaseUser user;
    String input;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_v2);

        textViewEmail = findViewById(R.id.profile_tv_mail);
        textInputEditTextUsername = findViewById(R.id.profile_tv_name);
        imageViewProfile = findViewById(R.id.profile_iv_profile);
        addProfilePicture = findViewById(R.id.profile_iv_add_picture);
        logoutProfile = findViewById(R.id.profile_btn_logout);
        addProfileUsername = findViewById(R.id.profile_iv_add_name);


        user = FirebaseAuth.getInstance().getCurrentUser();
        this.isCurrentUserLogged();
        this.updateUIWhenCreating();
    }

    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    private Boolean isCurrentUserLogged(){ return (this.getCurrentUser() !=null); }

    private void updateUIWhenCreating(){

        if(this.getCurrentUser().getDisplayName() != null){
            displayUsernameProfile();
            addProfileUsername.setVisibility(View.GONE);
        }else{
            addProfileUsername.setOnClickListener(v -> {
                AlertDialog.Builder alerteDiag = new AlertDialog.Builder(ProfileActivity.this);
                alerteDiag.setTitle(R.string.profileactivity_adiag_title_enter_your_name);
                final EditText editText = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(lp);
                alerteDiag.setView(editText);

                alerteDiag.setPositiveButton(R.string.profileactivity_adiag_btn_yes, (dialog, which) -> {
                   input = editText.getText().toString();
                    setUsernameProfile(input);
                });

                alerteDiag.setNegativeButton(R.string.profileactivity_adiag_btn_cancel, (dialog, which) -> dialog.cancel());
                alerteDiag.show();
                addProfileUsername.setVisibility(View.GONE);
                displayUsernameProfile();
            });
        }

        if (this.getCurrentUser().getPhotoUrl() !=null){
            displayPhotoProfile();
        }else{

            addProfilePicture.setOnClickListener((View v)-> {
               //TODO charger une photo.

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
                alertDialog.setTitle(R.string.profileactivity_adiag_title_picture_add);
                alertDialog.setMessage(R.string.profileactivity_adiag_message_picture_add);
                final EditText editText = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(lp);
                alertDialog.setView(editText);


                alertDialog.setPositiveButton(R.string.profileactivity_adiag_btn_yes, (dialog, which) -> {
                    input = editText.getText().toString();
                    setUserPhotoProfile(input);
                });
                alertDialog.setNegativeButton(R.string.profileactivity_adiag_btn_cancel, (dialog, which) -> dialog.cancel());

                alertDialog.show();
            });




        }
        displayUsernameProfile();
        displayEmailUser();

    }

    private void displayEmailUser() {
        String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ?
               getString(R.string.info_no_email_found)  : this.getCurrentUser().getEmail();
        this.textViewEmail.setText(email);
    }

    private void displayUsernameProfile() {
        String username = TextUtils.isEmpty(user.getDisplayName()) ?
                getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();
        this.textInputEditTextUsername.setText(username);
    }

    private void displayPhotoProfile() {
        addProfilePicture.setVisibility(View.GONE);
        if (getCurrentUser().getPhotoUrl() != null){
            Glide.with(this)
                    .load(this.getCurrentUser().getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);
        }

    }

    private void setUserPhotoProfile(String photoUrl) {

        //TODO Le faire via un Repo !!!!

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUrl))
                .build();

        User userForm = new User();
        userForm.setmUid(getCurrentUser().getUid());
        userForm.setmUsername(getCurrentUser().getDisplayName());
        userForm.setmMail(getCurrentUser().getEmail());
        userViewModel = ViewModelProviders.of(ProfileActivity.this).get(UserViewModel.class);
        userViewModel.init(userForm);
        userViewModel.getUserInFirestore()
                .observe(ProfileActivity.this, user1 -> {
                    user1.setmUrlPicture(photoUrl);

                });


        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, R.string.profileactivity_toast_update_profile_success, Toast.LENGTH_SHORT).show();
                            displayPhotoProfile();

                        }else{
                            Toast.makeText(ProfileActivity.this, R.string.profileactivity_toast_update_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setUsernameProfile(String usernameProfile){

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(usernameProfile)
                .build();

        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(task -> {
                    displayUsernameProfile();
                    addProfileUsername.setVisibility(View.GONE);
                });

    }


}
