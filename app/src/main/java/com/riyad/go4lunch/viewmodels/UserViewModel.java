package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.UserRepository;


public class UserViewModel extends ViewModel {


    private UserRepository userRepository;


    public void init() {
        userRepository = UserRepository.getInstance();
    }

    public LiveData<User> createUserinFirestoreRepository() {
        return userRepository.creatUserInFirebase();
    }

    public LiveData<User> getUserInFirestore(String userId) {
        return userRepository.getUserinfirestore(userId);
    }

    public LiveData<User> setUserPhotoUrl(String userId, String photoUrl) {
        return userRepository.setPhotoProfileUserInFirestore(userId, photoUrl);
    }

    public LiveData<User> setUserName(String userId, String name) {
        return userRepository.setNameProfileUserInFirestore(userId, name);
    }

}
