package com.riyad.go4lunch.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;

public class MessageAdapter extends FirestoreRecyclerAdapter<Chat, MessageAdapter.ChatHolder > {






    public class ChatHolder extends RecyclerView.ViewHolder {

        // Sender Message (chat item left)
        private ImageView mProfileCircleSender;
        private TextView mSenderUsername;
        private TextView mSenderTimestamp;
        private TextView mSenderMessage;

        // Current user (chat item right)
        private TextView mCurrentMessageDisplay;
        private TextView mCurrentTimestamp;


        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            mProfileCircleSender = itemView.findViewById(R.id.chat_item_left_iv_circle_picture);
            mSenderUsername = itemView.findViewById(R.id.chat_item_left_tv_username);
            mSenderTimestamp = itemView.findViewById(R.id.chat_item_left_tv_timestamp);
            mSenderMessage = itemView.findViewById(R.id.chat_item_left_tv_message);

            mCurrentMessageDisplay = itemView.findViewById(R.id.chat_item_right_tv_message);
            mCurrentTimestamp = itemView.findViewById(R.id.)



        }
    }

}
