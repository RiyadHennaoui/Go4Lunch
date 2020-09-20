package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.UserRepository;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> currentUserMutableLiveData;
    private MutableLiveData<User> otherUserMutableLiveData;
    private UserRepository userRepository;
    public void init(String userId){

        if(currentUserMutableLiveData != null){
            return;
        }

        userRepository = UserRepository.getInstance();
        currentUserMutableLiveData = userRepository.getCurrentUser(userId);
        otherUserMutableLiveData = userRepository.getUser(userId);

    }

    public LiveData<User> getFirebaseCurrentUser(){
        return currentUserMutableLiveData;
    }

    public LiveData<User> getOtherFirebaseUser(){
        return otherUserMutableLiveData;
    }
}
