package com.riyad.go4lunch.networking;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public MutableLiveData<User> getCurrentUser(){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        userDb.collection(COLLECTION_USER_NAME)
                .document(getCurrentFirebaseUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                   User currentUserFirestore;
                   currentUserFirestore = documentSnapshot.toObject(User.class);
                    userMutableLiveData.postValue(currentUserFirestore);
                });

        return  userMutableLiveData;
    }


    public MutableLiveData<User> creatUser() {

        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        User userToSave = new User();
        userToSave.setmUid(getCurrentFirebaseUser().getUid());
        userToSave.setmUsername(getCurrentFirebaseUser().getDisplayName());
        userToSave.setmMail(getCurrentFirebaseUser().getEmail());
        if (getCurrentFirebaseUser().getPhotoUrl() != null) {
            userToSave.setmUrlPicture(getCurrentFirebaseUser().getPhotoUrl().toString());
        }

        userDb.collection(COLLECTION_USER_NAME)
                .document(userToSave.getmUid())
                .set(userToSave)
                .addOnCompleteListener(task -> userMutableLiveData.postValue(userToSave));

        return userMutableLiveData;
    }

    public MutableLiveData<User> getUser(String userId) {

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

    public MutableLiveData<User> setPhotoProfileUser(String userId, String userPhotoUrl) {

        MutableLiveData<User> userPhotoMutableLiveData = new MutableLiveData<>();

        Log.e("userreposetPhoto", userPhotoUrl);



        userDb.collection(COLLECTION_USER_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User getUser;
                    getUser = documentSnapshot.toObject(User.class);
                    getUser.setmUrlPicture(userPhotoUrl);
                    UserProfileChangeRequest profileUpdates = getUserPhotoUrlProfileChangeRequest(userPhotoUrl);
                    currentUserFirebaseAuth.updateProfile(profileUpdates);
                    userDb.collection(COLLECTION_USER_NAME).document(userId).set(getUser);

                    userPhotoMutableLiveData.setValue(getUser);
                });

        return userPhotoMutableLiveData;
    }

    @NotNull
    private UserProfileChangeRequest getUserPhotoUrlProfileChangeRequest(String userPhotoUrl) {
        return new UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(userPhotoUrl))
                                .build();
    }

    public MutableLiveData<User> setNameProfileUser(String userId, String userName) {

        MutableLiveData<User> userNameMutableLiveData = new MutableLiveData<>();




        userDb.collection(COLLECTION_USER_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User getUser;
                    getUser = documentSnapshot.toObject(User.class);
                    getUser.setmUsername(userName);
                    UserProfileChangeRequest profileUpdates = getUserNameProfileChangeRequest(userName);
                    currentUserFirebaseAuth.updateProfile(profileUpdates);
                    userDb.collection(COLLECTION_USER_NAME).document(userId).set(getUser);

                    userNameMutableLiveData.setValue(getUser);
                });

        return userNameMutableLiveData;
    }

    @NotNull
    private UserProfileChangeRequest getUserNameProfileChangeRequest(String userName) {
        return new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();
    }

    public MutableLiveData<List<User>> getUsersList(){
        MutableLiveData<List<User>> usersData = new MutableLiveData<>();

        userDb.collection(COLLECTION_USER_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<User> usersList = new ArrayList<>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            User user = documentSnapshot.toObject(User.class);
                            if(!user.getmUid().equals(currentUserFirebaseAuth.getUid())){

                                    usersList.add(user);


                            }

                        }
                        Collections.sort(usersList, new Comparator<User>(){

                            @Override
                            public int compare(User user1, User user2) {
                                return ComparisonChain.start()
                                        .compare(user1.getBookingRestaurant().getRestaurantName(), user2.getBookingRestaurant().getRestaurantName(), Ordering.natural().nullsLast())
                                        .compare(user1.getmUsername(), user2.getmUsername(), Ordering.natural())
                                        .result();
                            }
                        });

                        usersData.setValue(usersList);
                    }

                });
            return usersData;
    }


}
