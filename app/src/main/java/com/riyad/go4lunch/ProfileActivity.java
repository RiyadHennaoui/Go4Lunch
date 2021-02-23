package com.riyad.go4lunch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import java.util.Locale;

import static com.riyad.go4lunch.utils.Constants.SIGN_OUT_TASK;

public class ProfileActivity extends AppCompatActivity {
    private TextView textInputEditTextUsername;
    private TextView YourLunch;
    private TextView language;
    private ImageView editLanguage;
    private ImageView editLunch;
    private ImageView imageViewProfile;
    private ImageView addProfilePicture;
    private ImageView addProfileUsername;
    private Button logoutProfile;
    FirebaseUser firebaseUser;
    String input;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        YourLunch = findViewById(R.id.profile_tv_your_lunch);
        editLunch = findViewById(R.id.profile_edit_your_lunch);
        textInputEditTextUsername = findViewById(R.id.profile_tv_name);
        editLanguage = findViewById(R.id.profile_edit_language);
        language = findViewById(R.id.profile_tv_language);
        imageViewProfile = findViewById(R.id.profile_iv_profile);
        addProfilePicture = findViewById(R.id.profile_iv_add_picture);
        logoutProfile = findViewById(R.id.profile_btn_logout);
        addProfileUsername = findViewById(R.id.profile_edit_username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        initUserViewModel();

        this.isCurrentUserLogged();
        this.updateUIWhenCreating();
        this.logoutProfile();
        this.setEditLanguage();
    }

    private void initUserViewModel() {
        userViewModel = ViewModelProviders.of(ProfileActivity.this).get(UserViewModel.class);
        userViewModel.init();
    }

    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    private Boolean isCurrentUserLogged(){ return (this.getCurrentUser() !=null); }

    private void updateUIWhenCreating(){


            displayUsernameProfile(getCurrentUser().getDisplayName());

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
                    setUserNameProfile(input);
                });

                alerteDiag.setNegativeButton(R.string.profileactivity_adiag_btn_cancel, (dialog, which) -> dialog.cancel());
                alerteDiag.show();

            });


        if (this.getCurrentUser().getPhotoUrl() !=null){
            displayPhotoProfile();
        }else{

            addProfilePicture.setOnClickListener((View v)-> {

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
        displayUsernameProfile(getCurrentUser().getDisplayName());
        displayUserLunch();

    }

    private void displayUserLunch() {
        //TODO afficher le lunch si pas vide
        userViewModel.getUserInFirestore(getCurrentUser().getUid()).observe(ProfileActivity.this, user -> {
            if (user.getBookingRestaurant().getRestaurantName() != null) {
                String restaurantName = user.getBookingRestaurant().getRestaurantName();
                this.YourLunch.setText(restaurantName);
            }else{
                this.YourLunch.setText(R.string.navigation_drawer_toast_your_lunch_no_lunch);
            }

        });


    }

    private void displayUsernameProfile(String userName) {
        //TODO utiliser le repo et non le firebaseUser.

        this.textInputEditTextUsername.setText(userName);
    }

    private void displayPhotoProfile() {
        if (getCurrentUser().getPhotoUrl() != null){
            Glide.with(this)
                    .load(this.getCurrentUser().getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);
        }

    }

    private void setUserPhotoProfile(String photoUrl) {

        userViewModel.setUserPhotoUrl(firebaseUser.getUid(), photoUrl)
                .observe(this, user -> displayPhotoProfile());
    }


    private void setUserNameProfile(String usernameProfile){

            userViewModel.setUserName(firebaseUser.getUid(), usernameProfile)
            .observe(this, user -> displayUsernameProfile(user.getmUsername()));

    }

    private void logoutProfile(){

        logoutProfile.setOnClickListener(v -> {
            signOutUserFromFirebase();
        });


    }

    private void setEditLanguage(){
        editLanguage.setOnClickListener(v -> showChangeLanguageDialog());
    }

    private void signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return aVoid -> {
            switch (origin) {
                case SIGN_OUT_TASK:
                    finish();
                    break;
                default:
                    break;
            }
        };
    }

    private void showChangeLanguageDialog(){

        final String[] listItem = {"English", "French"};


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Choose language");
        alertDialog.setSingleChoiceItems(listItem, -1, (dialog, which) -> {
            if (which == 0){
//                setLocal("en");
                LocaleHelper.onAttach(this, "en");
            }else if(which == 1){
//                setLocal("fr");
                LocaleHelper.onAttach(this, "fr");
            }

        });
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);


        alertDialog.setPositiveButton(R.string.profileactivity_adiag_btn_yes, (dialog, which) -> {
            input = editText.getText().toString();
            language.setText(listItem.length);

        });
        alertDialog.setNegativeButton(R.string.profileactivity_adiag_btn_cancel, (dialog, which) -> dialog.cancel());
        alertDialog.show();


    }

//    private void setLocal(String language) {
//
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//        Configuration configuration = new Configuration();
//        configuration.locale = locale;
//        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
//    }


}
