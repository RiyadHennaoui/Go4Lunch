package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.ui.Restaurant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_LEFT;
import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_RIGHT;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{

    private List<Chat> chatData;
    private FirebaseUser mCurrentUser;
    Context context;

    public FirebaseUser getmCurrentUser() {
        return mCurrentUser;
    }

    public ChatAdapter(List<Chat> chatData, Context context) {
        this.chatData = chatData;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //    View v = LayoutInflater.from(parent.getContext())
    //            .inflate(R.layout.chat_item_right, parent, false);
    //    return new ChatAdapter.ChatHolder(v);


        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.ChatHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.ChatHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatHolder holder, int position) {
        holder.bind(chatData.get(position));
    }

    @Override
    public int getItemCount() {
        return chatData != null ? chatData.size() : 0;
    }

    public void setData(@NonNull List<Chat> chat) {
        chatData = chat;
        Log.i("taille restaurant", chatData.size() + "");
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {

////        if (!mCurrentUser.getUid().equals(mChat.get(position).getSender().getmUid())) {
        if (!chatData.get(position).getIsSender()) {
            return MSG_TYPE_LEFT;
        } else {
            return MSG_TYPE_RIGHT;
        }
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        ImageView mProfilePicture;
        TextView mMessage;
        TextView mUsername;
        ImageView receiverPhoto;
        TextView mTimestamp;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            mProfilePicture = itemView.findViewById(R.id.chat_item_iv_circle_picture);
            mMessage = itemView.findViewById(R.id.chat_item_tv_message);
            mUsername = itemView.findViewById(R.id.chat_item_tv_username);
            mTimestamp = itemView.findViewById(R.id.chat_item_tv_timestamp);



        }


        public void bind(final Chat chat) {

            mMessage.setText(chat.getMessage());
            mTimestamp.setText(convertDateToHour(chat.getCreatedDate()));
            mUsername.setText(chat.getAuther().getmUsername());
            if (chat.getAuther().getmUrlPicture() != null) {
                Log.e(chat.getAuther().getmUsername(), chat.getAuther().getmUrlPicture());
                Glide.with(mProfilePicture).load(chat.getAuther().getmUrlPicture()).circleCrop().into(mProfilePicture);
            }

        }

        private String convertDateToHour(Date date) {
            DateFormat dfTime = new SimpleDateFormat("HH:mm");
            return dfTime.format(date);
        }
    }
}
