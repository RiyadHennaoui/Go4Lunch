package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.UserRepository;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> userMutableLiveData;


    private static UserRepository userRepository;


    public void init(User user){
        if (userMutableLiveData != null){
            return;
        }
        userRepository = UserRepository.getInstance();
        userMutableLiveData = userRepository.creatUserInFirebase(user);

    }

    public LiveData<User> getUserRepository(){
        return userMutableLiveData;
    }


}
