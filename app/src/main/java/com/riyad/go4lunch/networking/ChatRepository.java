package com.riyad.go4lunch.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    public MutableLiveData<FirestoreRecyclerOptions<Chat>> getChat(String chatPartnerId){

        MutableLiveData<FirestoreRecyclerOptions<Chat>> chatMutableLiveData = new MutableLiveData<>();
        //TODO J'etais ici
        Query query = chatDb.document(getCurrentUser().getUid()).collection(chatPartnerId).orderBy("createdDate", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();
        chatMutableLiveData.setValue(options);

        Log.e("chatRepo", chatMutableLiveData + " - " + chatPartnerId + " count : "+  options.getSnapshots().size() + options.getOwner());
//        chatDb.collection(COLLECTION_USER_NAME).document(userId).collection(chatPartnerId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    QueryDocumentSnapshot currentDocument;
//                    ArrayList<Chat> currentDiscussion = new ArrayList<>();
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                        Chat chat = new Chat();
//                        chat = documentSnapshot.toObject(Chat.class);
//                        currentChat
//                        currentDiscussion
//                    }
//                    currentChat = currentDocument.toObject(Chat.class);
//                    chatMutableLiveData.setValue(currentChat);
//                });
        return chatMutableLiveData;
    }
}
