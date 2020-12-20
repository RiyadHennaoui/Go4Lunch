package com.riyad.go4lunch.networking;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.ProfileActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class UserRepository {

    private FirebaseUser currentUserFirebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore userDb = FirebaseFirestore.getInstance();

    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null){
            userRepository = new UserRepository();
        }
        return userRepository;
    }


    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public MutableLiveData<User> creatUserInFirebase (){

        MutableLiveData<User>  userMutableLiveData = new MutableLiveData<>();
        User userToSave = new User();
        userToSave.setmUid(getCurrentUser().getUid());
        userToSave.setmUsername(getCurrentUser().getDisplayName());
        userToSave.setmMail(getCurrentUser().getEmail());
        userDb.collection(COLLECTION_USER_NAME)
                .document(userToSave.getmUid())
                .set(userToSave)
                .addOnCompleteListener(task ->{
                    Log.e("useradd", "enfin ? ");

                        })
                .addOnFailureListener(e -> Log.e("userNotAdd", e.toString()))
        ;




        return userMutableLiveData;
    }

    public MutableLiveData<User> getUserinfirestore (String userId){

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

    public MutableLiveData<User> setPhotoProfileUserInFirestore (String userId, String userPhotoUrl){

        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

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
                    currentUserFirebaseAuth.updateProfile(profileUpdates)
                            .addOnCompleteListener(task1 -> {
                               if(task1.isSuccessful()){
                                Log.e("update Photoprofile", "ajouter au firebase auth avec success");
                               }
                            });
                    userMutableLiveData.setValue(getUser);
                });

        return userMutableLiveData;


    }

    public MutableLiveData<User> setNameProfileUserInFirestore (String userId, String userName){

        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        userDb.collection(COLLECTION_USER_NAME)
                .document(userId)

                .get()
                .addOnCompleteListener(task -> {
                    Log.e("userRepo", "lambda");
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User getUser;
                    getUser = documentSnapshot.toObject(User.class);
                    getUser.setmUsername(userName);
                    currentUserFirebaseAuth.updateProfile(profileUpdates);

                    userMutableLiveData.setValue(getUser);
                });

        return userMutableLiveData;


    }


}
