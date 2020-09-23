package com.riyad.go4lunch;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riyad.go4lunch.adapter.ChatAdapter;
import com.riyad.go4lunch.viewmodels.ChatViewModel;

import static com.riyad.go4lunch.utils.Constants.WORKMATE_ID;

public class ChatActivity extends AppCompatActivity{

    ImageView chatpartnerPhoto;
    EditText currentMessage;
    ImageButton sendMessageButton;
    Toolbar toolbar;

    ChatViewModel chatViewModel;

    ChatAdapter chatAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        sendMessageButton = findViewById(R.id.chat_activity_ib_send);
        recyclerView= findViewById(R.id.main_rv);
        chatpartnerPhoto = findViewById(R.id.chat_activity_iv_photo_receiver);
        currentMessage = findViewById(R.id.chat_activity_et_message);
        toolbar = findViewById(R.id.chat_toolbar);

        initViewModel();

        configureToolbarChatActivity();

//        chatReceiver = displayOtherUser(recieverTemp);
//        Log.e("chatPartner Object", chatReceiver.getmUsername());
//        String photoUrl = chatReceiver.getmUrlPicture();
        Glide.with(chatpartnerPhoto).load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQilpWGZianQ41td4_sPkevbsQ8MXrEWERPUw&usqp=CAU").circleCrop().into(chatpartnerPhoto);

        readMessage();

        sendMessageButton.setOnClickListener(v -> {
            String message = currentMessage.getText().toString();
            if (!message.isEmpty()){
                chatViewModel.sendMessage(message);
            }
            currentMessage.setText("");
        });

    }

    private void initViewModel() {
        chatViewModel = ViewModelProviders.of(ChatActivity.this).get(ChatViewModel.class);
        String receiverTemp = getIntent().getStringExtra(WORKMATE_ID);
        chatViewModel.init(receiverTemp);
    }

    private void configureToolbarChatActivity() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void readMessage() {

        chatViewModel.getChat().observe(ChatActivity.this, chats -> {
            chatAdapter = new ChatAdapter(chats, ChatActivity.this);
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
