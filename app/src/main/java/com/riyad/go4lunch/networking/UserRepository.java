package com.riyad.go4lunch.networking;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class UserRepository {

    private FirebaseFirestore userDb = FirebaseFirestore.getInstance();

    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null){
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    private GooglePlacesAPI googlePlacesAPI;

    public UserRepository(){
        googlePlacesAPI = RetrofitService.createService(GooglePlacesAPI.class);
    }

    public MutableLiveData<User> creatUserInFirebase (User currentUser){

        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        userDb.collection(COLLECTION_USER_NAME)
                .document(currentUser.getmUid())
                .get()
                .addOnCompleteListener(task -> {

                    User userToSave = new User();
                    userToSave.setmUsername(currentUser.getmUsername());
                    userToSave.setmUid(currentUser.getmUid());
                    userToSave.setmMail(currentUser.getmMail());

                userMutableLiveData.setValue(userToSave);
                });
        return userMutableLiveData;
    }


}
