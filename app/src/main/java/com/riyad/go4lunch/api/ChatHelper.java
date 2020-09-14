package com.riyad.go4lunch.api;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.model.User;

public class ChatHelper {

    public static Query getAllMessageForChat(){
        return FirebaseFirestore.getInstance().collection("chat").orderBy("createdDate");
    }

    public static Task<DocumentReference> createMessageForChat(User reciever , String message, User userSender){
        Chat chat = new Chat(reciever, message, userSender);
        return FirebaseFirestore.getInstance().collection("chat").add(chat);
    }
}
