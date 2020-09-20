package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.riyad.go4lunch.model.Chat;

import java.util.ArrayList;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_CHAT;
import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;

public class ChatRepository {
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    private static ChatRepository chatRepository;

    private CollectionReference chatDb = FirebaseFirestore.getInstance().collection(COLLECTION_USER_NAME);



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
}
