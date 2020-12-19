package com.riyad.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.api.UserHelper;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import java.util.Arrays;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);




        if (!userCurrentLogged()) {

            createSignInIntent();

        } else {

            intentToMainActivity();

        }


    }

    private void intentToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private Boolean userCurrentLogged() {
        return (this.getCurrentUser() != null);
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),

//TODO Revoir l'autenthification de FB !
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

// TODO finir l'activité SplashScreen après le lancement de l'autentification Firebase.
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_logo_main)
                        .build(), RC_SIGN_IN
        );

    }

    private void creatUserInFirestore(){

        User userForm = new User();
        userForm.setmUid(getCurrentUser().getUid());
        userForm.setmUsername(getCurrentUser().getDisplayName());
        userForm.setmMail(getCurrentUser().getEmail());

        userViewModel = ViewModelProviders.of(SplashScreen.this).get(UserViewModel.class);
        userViewModel.init(userForm);
        userViewModel.getUserRepository()
                .observe(SplashScreen.this, user -> {
                    Log.e("UserCreated ?", "Vérifie dans Firestore");

    });
        //TODO Crée un Repo Pour les Users, supprimer la class UserHelper, sans oublier de faire le TODO de vérification si le compte existe dèja!!!
        if (userCurrentLogged()){




//            String mUid = this.getCurrentUser().getUid();
//            String mUsername = this.getCurrentUser().getDisplayName();
//            String mUrlPicture = (this.getCurrentUser().getPhotoUrl() != null) ?
//                    this.getCurrentUser().getPhotoUrl().toString() : null;
//            String mMail = this.getCurrentUser().getEmail();
//
//            UserHelper.createUser(mUid, mUsername, mMail, mUrlPicture).addOnFailureListener(e ->
//
//                    Toast.makeText(getApplicationContext(), "UserHelper not created error server" + e, Toast.LENGTH_LONG).show());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {

//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                this.creatUserInFirestore();
                intentToMainActivity();
                finish();
            } else {

                Log.e("Dans le requestSplash", resultCode + "");
                //TODO something

            }

        }
    }
}
