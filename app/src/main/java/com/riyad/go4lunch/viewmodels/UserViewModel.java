package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.DetailRestaurantRepository;
import com.riyad.go4lunch.networking.UserRepository;
import com.riyad.go4lunch.ui.Restaurant;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> userMutableLiveData;
    private UserRepository userRepository;
    public void init(String userId){

        if(userMutableLiveData != null){
            return;
        }

        userRepository = UserRepository.getInstance();
        userMutableLiveData = userRepository.getCurrentUser(userId);
    }

    public LiveData<User> getFirebaseUser(){
        return userMutableLiveData;
    }
}
