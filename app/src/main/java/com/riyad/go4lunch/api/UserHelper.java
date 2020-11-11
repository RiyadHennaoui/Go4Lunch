package com.riyad.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.model.User;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class UserHelper {

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USER_NAME);
    }

    public static Task<Void> createUser(String mUid, String mUsername,  String mMail, String mUrlPicture) {
        User userToCreat = new User(mUid, mUsername, mMail, mUrlPicture);
        //TODO vérifier que le user n'existe pas déjà dans firestore, si c'est cas ne rien faire sinon le créer.
        return UserHelper.getUsersCollection().document(mUid).set(userToCreat);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getUser(String mUid) {
        return UserHelper.getUsersCollection().document(mUid).get();
    }

    // --- UPDATE ---
    public static Task<Void> updateUsername(String mUsername, String mUid) {
        return UserHelper.getUsersCollection().document(mUid).update("mUsername", mUsername);
    }

    public static Task<Void> updateUrlPicture(String mUrlPicture, String mUid) {
        return UserHelper.getUsersCollection().document(mUid).update("mUrlPicture", mUrlPicture);
    }

    public static Task<Void> updateMail(String mMail, String mUid) {
        return UserHelper.getUsersCollection().document(mUid).update("mMail", mMail);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String mUid) {
        return UserHelper.getUsersCollection().document(mUid).delete();
    }




}

