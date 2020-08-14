package com.riyad.go4lunch.networking;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.model.User;

public class UserRepository {

    private static UserRepository userRepository;
    private FirebaseFirestore restaurantDb = FirebaseFirestore.getInstance();

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    public MutableLiveData<User> getCurrentUser(String userId){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        restaurantDb.collection("user").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    User currentUser;
                    DocumentSnapshot currentUserDocument = task.getResult();
                    currentUser = currentUserDocument.toObject(User.class);
                    userMutableLiveData.setValue(currentUser);
                });

        return userMutableLiveData;
    }
}
