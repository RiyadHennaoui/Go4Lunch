package com.riyad.go4lunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.riyad.go4lunch.adapter.ChatAdapter;
import com.riyad.go4lunch.adapter.MessageAdapter;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.ChatViewModel;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;
import static com.riyad.go4lunch.utils.Constants.WORKMATE_ID;

public class ChatActivity extends AppCompatActivity{



    TextView usernameReceiver;
    TextView usernameCurrentUser;
    TextView message;
    TextView timestamp;

    String recieverTemp;
    User chatReceiver;
    User currentUser;
    FirebaseUser firebaseUser;
    CollectionReference firestoreCurrentUserChatCollection;
    CollectionReference firestoreChatPartenerChatCollection;
    CollectionReference userCollection;

    ImageView chatpartnerPhoto;
    EditText currentMessage;
    ImageButton sendMessageButton;

    UserViewModel userViewModel;


    ChatAdapter chatAdapter;


    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        sendMessageButton = findViewById(R.id.chat_activity_ib_send);
        recyclerView= findViewById(R.id.main_rv);
        chatpartnerPhoto = findViewById(R.id.chat_activity_iv_photo_receiver);



        currentUser = displayCurrentUser(getCurrentUser().getUid());
        recieverTemp = getIntent().getStringExtra(WORKMATE_ID);
//        chatReceiver = displayOtherUser(recieverTemp);
//        Glide.with(chatpartnerPhoto).load(displayOtherUser(recieverTemp).getmUrlPicture()).circleCrop().into(chatpartnerPhoto);



        firestoreCurrentUserChatCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USER_NAME)
                .document(getCurrentUser().getUid())
                .collection(recieverTemp);

        firestoreChatPartenerChatCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USER_NAME)
                .document(recieverTemp)
                .collection(getCurrentUser().getUid());

        userCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USER_NAME);

        readMessage();


        sendMessageButton.setOnClickListener(v -> {
            String message = currentMessage.getText().toString();
            sendMessage(message);
            currentMessage.setText("");
        });

    }



    private User displayCurrentUser(String userId) {

        userViewModel = ViewModelProviders.of(ChatActivity.this).get(UserViewModel.class);
        userViewModel.init(userId);
        userViewModel.getFirebaseCurrentUser().observe(ChatActivity.this, workmate -> {
            currentUser = workmate;
        });
        return currentUser;
    }

    private User displayOtherUser(String otherUserId){

        userViewModel = ViewModelProviders.of(ChatActivity.this).get(UserViewModel.class);
        userViewModel.init(otherUserId);
        userViewModel.getOtherFirebaseUser().observe(ChatActivity.this, otherUser -> chatReceiver = otherUser);
        return chatReceiver;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void sendMessage(String message) {

        Chat chat = new Chat();
        chat.setAuther(displayCurrentUser(getCurrentUser().getUid()));
        chat.setMessage(message);

        if (!chat.getMessage().equals("")) {
            chat.setIsSender(true);
            firestoreCurrentUserChatCollection
                    .add(chat)
                    .addOnCompleteListener(task -> Log.e("chatSender", chat.getMessage()));

            chat.setIsSender(false);
            firestoreChatPartenerChatCollection
                    .add(chat)
                    .addOnCompleteListener(task -> Log.e("chatRieciever", chat.getMessage()));

        }

    }

    private void readMessage() {
        List<Chat> mchat = new ArrayList<>();

        userCollection.document(getCurrentUser().getUid()).collection(recieverTemp)
                .orderBy("createdDate", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {

                        return;
                    }


                    for (QueryDocumentSnapshot doc : value) {
                        Chat currentDoc;
                        currentDoc = doc.toObject(Chat.class);
                            mchat.add(currentDoc);
                    }

                    chatAdapter = new ChatAdapter(mchat, ChatActivity.this);
                    configureRecyclerView(chatAdapter);
                });
    }


    private void configureRecyclerView(ChatAdapter thisChatAdapter){

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(thisChatAdapter);

    }
}
