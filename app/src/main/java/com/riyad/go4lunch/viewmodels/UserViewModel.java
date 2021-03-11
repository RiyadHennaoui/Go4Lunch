package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.UserRepository;

import java.util.List;


public class UserViewModel extends ViewModel {


    private UserRepository userRepository;


    public void init() {
        userRepository = UserRepository.getInstance();
    }

    public LiveData<User> getCurrentUser(){
        return  userRepository.getCurrentUser();
    }

    public LiveData<User> createUser() {
        return userRepository.creatUser();
    }

    public LiveData<User> getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public LiveData<User> setUserPhotoUrl(String userId, String photoUrl) {
        return userRepository.setPhotoProfileUser(userId, photoUrl);
    }

    public LiveData<User> setUserName(String userId, String name) {
        return userRepository.setNameProfileUser(userId, name);
    }

    public LiveData<List<User>> getUsers(){
        return  userRepository.getUsersList();
    }

}
