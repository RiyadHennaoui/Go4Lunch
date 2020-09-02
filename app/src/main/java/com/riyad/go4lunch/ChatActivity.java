package com.riyad.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.viewmodels.DetailRestaurantViewModel;
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

    EditText currentMessage;
    ImageButton sendMessageButton;

    UserViewModel userViewModel;

    Intent intent;
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


        //TODO A remplir!

        workmateId = getIntent().getStringExtra(WORKMATE_ID);
        sendMessageButton.setOnClickListener(v -> {
            String message = currentMessage.getText().toString();
            sendMessage(getCurrentUser().getUid(), workmateId, message);
        });

//        displayWormate(workmateId);
    }


    private void displayWormate(String workmateId){

        userViewModel = ViewModelProviders.of(ChatActivity.this).get(UserViewModel.class);
        userViewModel.init(workmateId);
        userViewModel.getFirebaseUser().observe(ChatActivity.this, wormate -> {
            displayWormate(workmateId);
            username.setText(wormate.getmUsername());
        });

    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void sendMessage(String sender, String receiver, String message){

        CollectionReference firestoreCollection = FirebaseFirestore.getInstance().collection("chat");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        firestoreCollection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult().isEmpty()){

                        }else {
                            ArrayList<Chat> chatArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Chat chat = document.toObject(Chat.class);
                                firestoreCollection.add(chat);
//                                chatArrayList.add(chat);
                            }
//                            firestoreCollection.add(chatArrayList);
                        }

                    }
                });

    }
}
