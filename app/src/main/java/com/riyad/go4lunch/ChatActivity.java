package com.riyad.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.adapter.MessageAdapter;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.riyad.go4lunch.utils.Constants.WORKMATE_ID;

public class ChatActivity extends AppCompatActivity {

    ImageView profilePhoto;
    TextView username;
    TextView message;
    TextView timestamp;

    String workmateId;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    CollectionReference firestoreChatCollection;

    EditText currentMessage;
    ImageButton sendMessageButton;

    UserViewModel userViewModel;

    MessageAdapter messageAdapter;
    //List<Chat> mchat;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        profilePhoto = findViewById(R.id.chat_item_iv_circle_picture);
        username = findViewById(R.id.chat_item_tv_username);
        message = findViewById(R.id.chat_item_tv_message);
        timestamp = findViewById(R.id.chat_item_tv_timestamp);
        currentMessage = findViewById(R.id.chat_activity_et_message);
        sendMessageButton = findViewById(R.id.chat_activity_ib_send);

        recyclerView = findViewById(R.id.main_rv);

         firestoreChatCollection = FirebaseFirestore.getInstance().collection("chat");

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        //TODO A remplir!

        workmateId = getIntent().getStringExtra(WORKMATE_ID);
//        Log.i("chatActivity", workmateId);
        sendMessageButton.setOnClickListener(v -> {
            String message = currentMessage.getText().toString();
            sendMessage(getCurrentUser().getUid(), workmateId, message);
            currentMessage.setText("");
            displayWorkmate(workmateId);
        });
        tryToGetMessage(getCurrentUser().getUid(), workmateId);


    }


    private void displayWorkmate(String workmateId){

        userViewModel = ViewModelProviders.of(ChatActivity.this).get(UserViewModel.class);
        userViewModel.init(workmateId);
        userViewModel.getFirebaseUser().observe(ChatActivity.this, workmate -> {
//            username.setText(workmate.getmUsername());
            readMessage(getCurrentUser().getUid(), workmateId, workmate.getmUrlPicture());
        });

    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void sendMessage(String sender, String receiver, String message){



        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        Chat chat = new Chat();

        chat.setRecieiver(receiver);
        chat.setSender(sender);
        chat.setMessage(message);

        firestoreChatCollection
                .add(chat)
                .addOnCompleteListener(task -> Log.e("chat", chat.getMessage()));


//        firestoreCollection
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()){
//                        if (task.getResult().isEmpty()){
//
//                        }else {
//                            ArrayList<Chat> chatArrayList = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Chat chat = document.toObject(Chat.class);
//                                firestoreCollection.add(chat);
////                                chatArrayList.add(chat);
//                            }
////                            firestoreCollection.add(chatArrayList);
//                        }
//
//                    }
//                });

    }

    private void tryToGetMessage(String myId, String userId){
        ArrayList<Chat> newChat = new ArrayList<>();
        firestoreChatCollection
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null){
                        Log.w("try show message", "Listen failed.", e);
                    }
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Chat chat =  doc.toObject(Chat.class);
                        newChat.add(chat);
                    }
                });
        Log.e("try show message", newChat.size() + "");
    }

    private void readMessage(String myId, String userId, String imageUrl){
        ArrayList<Chat> mchat = new ArrayList<>();
        ArrayList<Chat> chatChat = new ArrayList<>();

//        CollectionReference firestoreCollection = FirebaseFirestore.getInstance().collection("chat");

        firestoreChatCollection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        mchat.clear();

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Chat chat = queryDocumentSnapshot.toObject(Chat.class);
                            chatChat.add(chat);
                            if(myId.equals(chat.getSender()) && userId.equals(chat.getReceiver()) ||
                                    myId.equals(chat.getReceiver()) && userId.equals(chat.getSender())) {

                                Log.e("if chat", chat.getReceiver());
                                mchat.add(chat);

                            }
                            messageAdapter = new MessageAdapter(ChatActivity.this, mchat, imageUrl);
                            recyclerView.setAdapter(messageAdapter);
                        }
                    }else {
                        Log.e("if chat", task.getResult().toString());
                        Log.i("Chat Task", task.getException().toString());
                    }
                });

        Log.e("chat save", mchat.size() + "");
        Log.e("chat chat save", mchat.size() + "");
    }
}
