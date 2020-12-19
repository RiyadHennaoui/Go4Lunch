package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.UserRepository;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> creatUserMutableLiveData;
    private MutableLiveData<User> getUserMutableLiveData;
    private MutableLiveData<User> setUserPhotoUrlMutableLiveData;
    private MutableLiveData<User> setUserNameMutableLiveData;


    private static UserRepository userRepository;


    public void init(User user){
        if (creatUserMutableLiveData != null){
            return;
        }
        userRepository = UserRepository.getInstance();
        creatUserMutableLiveData = userRepository.creatUserInFirebase(user);
        getUserMutableLiveData = userRepository.getUserinfirestore(user.getmUid());
        setUserNameMutableLiveData = userRepository.setNameProfileUserInFirestore(user.getmUid(), user.getmUsername());
        setUserPhotoUrlMutableLiveData = userRepository.setPhotoProfileUserInFirestore(user.getmUid(), user.getmUrlPicture());

    }

    public LiveData<User> getUserRepository(){
        return creatUserMutableLiveData;
    }
    public LiveData<User> getUserInFirestore(){return getUserMutableLiveData;}

}
