package com.riyad.go4lunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class FirebaseAuthFragment extends Fragment {

    private static final int RC_SIGN_IN = 123;

    public static FirebaseAuthFragment newInstace(){
        return new FirebaseAuthFragment();
    }

    public FirebaseAuthFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.firebase_auth_fragment_blank, container, false);

    }


    public void creatSignInIntent(){

        // Google, Facebook and twitter authentification providers
        List<AuthUI.IdpConfig> proviaders = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
//                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        //Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(proviaders)
                .setIsSmartLockEnabled(false,true)
                .setLogo(R.drawable.icon1)
                .build(),RC_SIGN_IN
        );


    }





}
