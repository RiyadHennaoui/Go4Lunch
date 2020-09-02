package com.riyad.go4lunch.networking;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riyad.go4lunch.model.Chat;

import java.util.ArrayList;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_CHAT;

public class ChatRepository {
    private static ChatRepository chatRepository;
    private FirebaseFirestore chatDb = FirebaseFirestore.getInstance();
    private Chat currentChat;


    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static ChatRepository getInstance(){
        if (chatRepository == null){
            chatRepository = new ChatRepository();
        }
        return chatRepository;
    }

    public MutableLiveData<Chat> getChat(String id){

        MutableLiveData<Chat> chatMutableLiveData = new MutableLiveData<>();
        chatDb.collection(COLLECTION_CHAT).document(id)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot currentDocument = task.getResult();
                    currentChat = currentDocument.toObject(Chat.class);
                    chatMutableLiveData.setValue(currentChat);
                });
        return chatMutableLiveData;
    }
}
