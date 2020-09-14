package com.riyad.go4lunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.riyad.go4lunch.adapter.ChatAdapter;
import com.riyad.go4lunch.adapter.MessageAdapter;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import java.util.ArrayList;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;
import static com.riyad.go4lunch.utils.Constants.WORKMATE_ID;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.Listener{

    ImageView profilePhoto;
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
    CollectionReference firestoreChatCollection;

    EditText currentMessage;
    ImageButton sendMessageButton;
    Button sendMessage;

    UserViewModel userViewModel;

    MessageAdapter messageAdapter;
//    ChatAdapter chatAdapter;
    //List<Chat> mchat;


    RecyclerView recyclerView;


    // FOR DESIGN
    TextView textViewRecyclerViewEmpty;
    EditText editTextMessage;
    ImageView imageViewPreview;

    // FOR DATA
    @Nullable
    private User modelCurrentUser;
    private String currentChatName;
    private Uri uriImageSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


//        textViewRecyclerViewEmpty = findViewById(R.id.activity_mentor_chat_text_view_recycler_view_empty);
//        editTextMessage = findViewById(R.id.activity_mentor_chat_message_edit_text);
//        imageViewPreview = findViewById(R.id.activity_mentor_chat_image_chosen_preview);
//        sendMessage =findViewById(R.id.activity_mentor_chat_send_button);
        profilePhoto = findViewById(R.id.chat_item_iv_circle_picture);
        usernameReceiver = findViewById(R.id.chat_item_tv_username_left);
        usernameCurrentUser = findViewById(R.id.chat_item_tv_username_right);
        message = findViewById(R.id.chat_item_tv_message);
        timestamp = findViewById(R.id.chat_item_tv_timestamp);
        currentMessage = findViewById(R.id.chat_activity_et_message);
        sendMessageButton = findViewById(R.id.chat_activity_ib_send);
        recyclerView= findViewById(R.id.main_rv);


        currentUser = displayUser(getCurrentUser().getUid());
        recieverTemp = getIntent().getStringExtra(WORKMATE_ID);

        //CollectionReference usersRef = FirebaseFirestore.getInstance().collection("user");
        firestoreChatCollection = FirebaseFirestore.getInstance().collection("chat");
        firestoreCurrentUserChatCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USER_NAME).document(getCurrentUser().getUid()).collection(recieverTemp);
        firestoreChatPartenerChatCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USER_NAME).document(recieverTemp).collection(getCurrentUser().getUid());
//        configureRecyclerView();
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        readMessage();


        chatReceiver = displayUser(recieverTemp);
//        tryToGetMessage();
        Log.i("chatActivity", chatReceiver + " " + getCurrentUser().getUid());
//        sendMessage.setOnClickListener(v -> {
//            onClickSendMessage();
//            Log.e("onClick", "v.");
//        });
        sendMessageButton.setOnClickListener(v -> {
            User currentUser = displayUser(getCurrentUser().getUid());
            User chatPartner = displayUser(recieverTemp);
            String message = currentMessage.getText().toString();
            sendMessage(currentUser, chatPartner, message);
            currentMessage.setText("");
        });



    }

//    public void onClickSendMessage() {
//       // if (!TextUtils.isEmpty(editTextMessage.getText()) && modelCurrentUser != null){
//            // Check if the ImageView is set
//            //if (this.imageViewPreview.getDrawable() == null) {
//                // SEND A TEXT MESSAGE
//                ChatHelper.createMessageForChat(chatReceiver, editTextMessage.getText().toString(),
//                        currentUser)
//                        .addOnFailureListener(e -> Log.e("onClickListner", e.toString()) );
//                this.editTextMessage.setText("");
//            //} else {
//                // SEND A IMAGE + TEXT IMAGE
//               // this.uploadPhotoInFirebaseAndSendMessage(editTextMessage.getText().toString());
//               // this.editTextMessage.setText("");
//              //  this.imageViewPreview.setImageDrawable(null);
//            //}
//       // }
//    }


    private User displayUser(String recieverId) {

        userViewModel = ViewModelProviders.of(ChatActivity.this).get(UserViewModel.class);
        userViewModel.init(recieverId);
        userViewModel.getFirebaseUser().observe(ChatActivity.this, workmate -> {
            chatReceiver = workmate;
        });
        return chatReceiver;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void sendMessage(User sender, User receiver, String message) {

        Chat chat = new Chat();

        chat.setSender(sender);
        chat.setRecieiver(receiver);
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


//    private void tryToGetMessage() {
//        ArrayList<Chat> newChat = new ArrayList<>();
//        firestoreChatCollection
//                .addSnapshotListener((queryDocumentSnapshots, e) -> {
//                    if (e != null) {
//                        Log.w("try show message", "Listen failed.", e);
//                    }
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        Chat chat = doc.toObject(Chat.class);
//                        newChat.add(chat);
//                    }
//                });
//        ArrayList<Chat> msortedChat = new ArrayList<>();
//        msortedChat = sortedChat(newChat, getCurrentUser().getUid(), recieverTemp);
//
////        messageAdapter = new MessageAdapter(msortedChat);
////        recyclerView.setAdapter(messageAdapter);
//
//        Log.e("chat try show message", newChat.size() + "");
//    }

    private void readMessage() {
     //   ArrayList<Chat> mchat = new ArrayList<>();

//        firestoreChatCollection
//                .addSnapshotListener((value, e) -> {
//                    List<Chat> queryChat = new ArrayList<>();
//                    int i = 0;
//                    for (QueryDocumentSnapshot doc : value) {
//                        queryChat.add(doc.toObject(Chat.class));
//                        mchat.add(queryChat.get(i));
//                        i++;
//                    }
//                });

        Query query = firestoreChatCollection.orderBy("createdDate");
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();


        //ArrayList<Chat> newChat = sortedChat(, myId, userId);
        messageAdapter = new MessageAdapter(options);
       // chatAdapter = new ChatAdapter(options, )
        recyclerView.setAdapter(messageAdapter);
        Log.e("adapter", "adapter??");
//                        }
//                    }else {
//                        Log.e("if chat", task.getResult().toString());
//                        Log.i("Chat Task", task.getException().toString());
//                    }
//                });

       // Log.e("chat save", mchat.size() + "");

    }

    private ArrayList<Chat> sortedChat(ArrayList<Chat> chats, String senderId, String recieverId) {
        ArrayList<Chat> currentChat = new ArrayList<>();
        for (int i = 0; i < chats.size(); i++) {
            if (senderId.equals(chats.get(i).getSender().getmUid()) && recieverId.equals(chats.get(i).getReceiver().getmUid()) ||
                    recieverId.equals(chats.get(i).getSender().getmUid()) && senderId.equals(chats.get(i).getReceiver().getmUid())) {
                currentChat.add(chats.get(i));
                return currentChat;
            }
        }
        return currentChat;
    }

    @Override
    public void onDataChanged() {

    }


//    @Override
//    public void onDataChanged() {
//
//    }

//    private void configureRecyclerView(){
//        //Track current chat name
//
//        //Configure Adapter & RecyclerView
//        chatAdapter = new ChatAdapter(generateOptionsForAdapter(ChatHelper.getAllMessageForChat()), this, currentUser.getmUid());
//        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()); // Scroll to bottom on new messages
//            }
//        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(this.chatAdapter);
//    }

//    private FirestoreRecyclerOptions<Chat> generateOptionsForAdapter(Query query){
//        return new FirestoreRecyclerOptions.Builder<Chat>()
//                .setQuery(query, Chat.class)
//                .setLifecycleOwner(this)
//                .build();
//    }
}
