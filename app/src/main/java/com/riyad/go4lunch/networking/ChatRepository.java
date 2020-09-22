package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;


import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class ChatRepository {

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    private static ChatRepository chatRepository;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();


    private CollectionReference chatDb;

    public ChatRepository() {
        firebaseFirestore.setFirestoreSettings(settings);
        chatDb = firebaseFirestore.collection(COLLECTION_USER_NAME);
    }

    public static ChatRepository getInstance(){
        if (chatRepository == null){
            chatRepository = new ChatRepository();
        }
        return chatRepository;
    }

    public MutableLiveData<List<Chat>> getChat(String chatPartnerId){

        MutableLiveData<List<Chat>> chatMutableLiveData = new MutableLiveData<>();

        List<Chat> mchat = new ArrayList<>();
        chatDb.document(getCurrentUser().getUid())
                .collection(chatPartnerId)
                .orderBy("createdDate", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        return;
                    }
                    mchat.clear();
                    String source = value != null && value.getMetadata().hasPendingWrites()
                            ? "Local" : "Server";

                    for (QueryDocumentSnapshot doc : value) {

                        if (doc != null) {
                            Chat currentChat;
                            currentChat = doc.toObject(Chat.class);
                            Log.e("repo Chat", currentChat.getMessage() + getCurrentUser().getUid() + ";" + chatPartnerId);
                            mchat.add(currentChat);
                        }
                    }
                   chatMutableLiveData.setValue(mchat);
                });

        return chatMutableLiveData;
    }


    public MutableLiveData<Chat> addMessagetoSender(String message, String chatPartenerId, User currentUser){
        MutableLiveData<Chat> messageToSenderMutableLiveData = new MutableLiveData<>();
        Chat chat = new Chat();
        chat.setAuthor(currentUser);
        chat.setMessage(message);
        //For save in Current User Collection.
        chatDb.document(currentUser.getmUid()).collection(chatPartenerId)
                .add(chat)
                .addOnCompleteListener(task -> Log.e("in Lambda currentUser", chat.getMessage()));


        //For save in chatPartener Collection.
        chatDb.document(chatPartenerId).collection(currentUser.getmUid())
                .add(chat)
                .addOnCompleteListener(task -> Log.e("in Lambda for chat", chat.getMessage()));




         messageToSenderMutableLiveData.setValue(chat);

        return messageToSenderMutableLiveData;
    }

    public MutableLiveData<Chat> addMessageToRecipient(String message, String chatPartenerId, User currentUser){
        MutableLiveData<Chat> messageToRecipientMutableLiveData = new MutableLiveData<>();
        Chat chat = new Chat();
        chat.setAuthor(currentUser);
        chat.setMessage(message);


        //For save in chatPartener Collection.
        chatDb.document(chatPartenerId).collection(currentUser.getmUid())
                .add(chat)
                .addOnCompleteListener(task -> Log.e("in Lambda for chat", chat.getMessage()));

        messageToRecipientMutableLiveData.setValue(chat);

        return messageToRecipientMutableLiveData;
    }
}
