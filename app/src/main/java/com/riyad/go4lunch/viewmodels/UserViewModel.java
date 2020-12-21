package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> createUserMutableLiveData;

    private static UserRepository userRepository;


    public void init(){
        if (createUserMutableLiveData != null){
            return;
        }

        userRepository = UserRepository.getInstance();
        createUserMutableLiveData = userRepository.creatUserInFirebase();



    }

    public LiveData<User> createUserinFirestoreRepository(){
        return createUserMutableLiveData;
    }
    public void getUserInFirestore(String userId){userRepository.getUserinfirestore(userId);}
    public LiveData<User> setUserPhotoUrl(String userId, String photoUrl){return userRepository.setPhotoProfileUserInFirestore(userId, photoUrl);}
    public LiveData<User> setUserName(String userId, String name){return userRepository.setNameProfileUserInFirestore(userId, name);}

}
