package com.riyad.go4lunch.networking;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.model.User;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class UserRepository {

    private FirebaseUser currentUserFirebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore userDb = FirebaseFirestore.getInstance();

    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public MutableLiveData<User> creatUserInFirebase() {

        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        User userToSave = new User();
        userToSave.setmUid(getCurrentUser().getUid());
        userToSave.setmUsername(getCurrentUser().getDisplayName());
        userToSave.setmMail(getCurrentUser().getEmail());
        if (getCurrentUser().getPhotoUrl() != null) {
            userToSave.setmUrlPicture(getCurrentUser().getPhotoUrl().toString());
        }

        userDb.collection(COLLECTION_USER_NAME)
                .document(userToSave.getmUid())
                .set(userToSave)
                .addOnCompleteListener(task -> userMutableLiveData.postValue(userToSave));

        return userMutableLiveData;
    }

    public MutableLiveData<User> getUserinfirestore(String userId) {

        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        userDb.collection(COLLECTION_USER_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User getUser;
                    getUser = documentSnapshot.toObject(User.class);
                    userMutableLiveData.setValue(getUser);
                });

        return userMutableLiveData;
    }

    public MutableLiveData<User> setPhotoProfileUserInFirestore(String userId, String userPhotoUrl) {

        MutableLiveData<User> userPhotoMutableLiveData = new MutableLiveData<>();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(userPhotoUrl))
                .build();


        userDb.collection(COLLECTION_USER_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User getUser;
                    getUser = documentSnapshot.toObject(User.class);
                    getUser.setmUrlPicture(userPhotoUrl);
                    currentUserFirebaseAuth.updateProfile(profileUpdates);

                    userPhotoMutableLiveData.setValue(getUser);
                });

        return userPhotoMutableLiveData;
    }

    public MutableLiveData<User> setNameProfileUserInFirestore(String userId, String userName) {

        MutableLiveData<User> userNameMutableLiveData = new MutableLiveData<>();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();


        userDb.collection(COLLECTION_USER_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User getUser;
                    getUser = documentSnapshot.toObject(User.class);
                    getUser.setmUsername(userName);
                    currentUserFirebaseAuth.updateProfile(profileUpdates);

                    userNameMutableLiveData.setValue(getUser);
                });

        return userNameMutableLiveData;
    }
}
