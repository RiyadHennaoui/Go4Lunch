package com.riyad.go4lunch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.riyad.go4lunch.viewmodels.UserViewModel;
import com.yariksoffice.lingver.Lingver;

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
    private Switch notificationSwitch;
    String input;
    private SharedPreferences sharedPreferences;
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
        notificationSwitch = findViewById(R.id.profile_notification_switch);


        sharedPreferences = getSharedPreferences(getString(R.string.sharedpreference_notification_settings), MODE_PRIVATE);
        initUserViewModel();

        this.updateUIWhenCreating();
        this.logoutProfile();
        this.setEditLanguage();
        this.getSwitchState();
        this.setYourLunch();
        this.updatePhotoUrl();
        this.switchListner();


    }

    private void initUserViewModel() {
        userViewModel = ViewModelProviders.of(ProfileActivity.this).get(UserViewModel.class);
        userViewModel.init();
    }

    private void setYourLunch() {
        editLunch.setOnClickListener(v -> displayFragmentRestaurantList());

    }

    private void switchListner() {
        notificationSwitch.setOnClickListener(v -> setNotificationSwitchState());
    }

    private void setNotificationSwitchState() {

        if (notificationSwitch.isChecked()) {
            sharedPreferences.edit().putBoolean("isCheck", true).apply();
        } else {
            sharedPreferences.edit().putBoolean("isCheck", false).apply();
        }
    }

    private void getSwitchState() {
        boolean ifCheckedState = sharedPreferences.getBoolean("isCheck", true);
        if (ifCheckedState) {
            notificationSwitch.setChecked(true);
        } else {
            notificationSwitch.setChecked(false);
        }

    }


    private void updateUIWhenCreating() {

        displayUsernameProfile();
        updateUserName();

        displayPhotoProfile();

        displayUsernameProfile();
        displayUserLunch();

    }

    private void updateUserName() {
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
    }

    private void updatePhotoUrl() {
        addProfilePicture.setOnClickListener((View v) -> {

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

    private void displayUserLunch() {
        userViewModel.getCurrentUser().observe(ProfileActivity.this, user -> {
            if (user.getBookingRestaurant().getRestaurantName() != null) {
                String restaurantName = user.getBookingRestaurant().getRestaurantName();
                this.YourLunch.setText(restaurantName);
            } else {
                this.YourLunch.setText(R.string.navigation_drawer_toast_your_lunch_no_lunch);
            }

        });


    }

    private void displayUsernameProfile() {
        userViewModel.getCurrentUser().observe(ProfileActivity.this, user ->
                this.textInputEditTextUsername.setText(user.getmUsername()));

    }

    private void displayPhotoProfile() {
        userViewModel.getCurrentUser().observe(ProfileActivity.this, user -> {
            if (user.getmUrlPicture() != null) {
                Glide.with(this)
                        .load(user.getmUrlPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
            }
        });


    }

    private void setUserPhotoProfile(String photoUrl) {

        userViewModel.getCurrentUser().observe(ProfileActivity.this, user -> {
            userViewModel.setUserPhotoUrl(user.getmUid(), photoUrl)
                    .observe(this, user1 -> displayPhotoProfile());
        });

    }


    private void setUserNameProfile(String usernameProfile) {

        userViewModel.getCurrentUser().observe(this, user -> {
            userViewModel.setUserName(user.getmUid(), usernameProfile)
                    .observe(this, user1 -> displayUsernameProfile());
        });


    }

    private void logoutProfile() {

        logoutProfile.setOnClickListener(v -> {
            signOutUserFromFirebase();
        });


    }

    private void setEditLanguage() {
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
                    restartSplashScreen();
                    finishAffinity();
                    break;
                default:
                    break;
            }
        };
    }

    private void showChangeLanguageDialog() {

        final String[] languageChoise = getResources().getStringArray(R.array.listItem_array);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle(R.string.settings_activity_choose_language);
        alertDialog.setSingleChoiceItems(languageChoise, -1, (dialog, which) -> {
            if (which == 0) {
                setNewLocale("en", "US");
                language.setText(languageChoise[which]);
            } else if (which == 1) {
                setNewLocale("fr", "FR");
                language.setText(languageChoise[which]);
            }

        });
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setNegativeButton(R.string.profileactivity_adiag_btn_cancel, (dialog, which) -> dialog.cancel());
        alertDialog.show();


    }


    private void setNewLocale(String language, String country) {
        Lingver.getInstance().setLocale(this, language, country);
        restart();
    }

    private void restart() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private void displayFragmentRestaurantList() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragement value", 1);
        startActivity(intent);

    }

    private void restartSplashScreen() {
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }


}
