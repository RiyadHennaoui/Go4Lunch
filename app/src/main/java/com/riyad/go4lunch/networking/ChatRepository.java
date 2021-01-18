package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.model.User;

import java.util.ArrayList;
import java.util.Date;
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

    public static ChatRepository getInstance() {
        if (chatRepository == null) {
            chatRepository = new ChatRepository();
        }
        return chatRepository;
    }

    public MutableLiveData<List<Chat>> getChat(String chatPartnerId) {

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
                            mchat.add(currentChat);
                        }
                    }
                    chatMutableLiveData.setValue(mchat);
                });

        return chatMutableLiveData;
    }


    public void sendMessage(String message, String chatPartenerId) {

        chatDb.document(getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    User currentUser;
                    if (task.isSuccessful()) {
                        DocumentSnapshot currentUserDocument = task.getResult();
                        currentUser = currentUserDocument.toObject(User.class);
                        Chat chat = new Chat();
                        chat.setAuthor(currentUser);
                        chat.setMessage(message);
                        chat.setCreatedDate(new Date());
                        //For save in Current User Collection.
                        chat.setIsSender(true);
                        chatDb.document(currentUser.getmUid()).collection(chatPartenerId)
                                .add(chat)
                                .addOnCompleteListener(task1 -> Log.e("in Lambda currentUser", chat.getMessage()));


                        //For save in chatPartener Collection.
                        chat.setIsSender(false);
                        chatDb.document(chatPartenerId).collection(currentUser.getmUid())
                                .add(chat)
                                //TODO demander à Thié s'il a besoin de garder les Logs dans les addOnCompleteListner
//                                .addOnCompleteListener(task2 -> Log.e("in Lambda for chat", chat.getMessage())
                                ;
                    }
                });

    }

}
