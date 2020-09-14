package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_LEFT;
import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_RIGHT;

public class MessageAdapter extends FirestoreRecyclerAdapter<Chat, MessageAdapter.ChatHolder> {

    private Context mContext;
    private ArrayList<Chat> mChat;
    //    private String mImageUrl;
    private FirebaseUser mCurrentUser;



    public interface Listner{
        void onDataChanged();
    }

    private Listner callback;

    // public MessageAdapter(Context mContext, List<Chat> mChat, String mImageUrl) {
    // this.mContext = mContext;
    //   this.mChat = mChat;
    //     this.mImageUrl = mImageUrl;
    //   }

    public MessageAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);

    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ChatHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ChatHolder(v);
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    //    @Override
//    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
//        holder.bind(mChat.get(position));
//    }

    @Override
    public int getItemViewType(int position) {

        if (!mCurrentUser.getUid().equals(mChat.get(position).getSender().getmUid())) {
            return MSG_TYPE_LEFT;
        } else {
            return MSG_TYPE_RIGHT;
        }

    }


    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
        holder.bind(mChat.get(position));
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        ImageView mProfilePicture;
        TextView mMessage;
        TextView mUsername;

        TextView mTimestamp;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            mProfilePicture = itemView.findViewById(R.id.chat_item_iv_circle_picture);
            mMessage = itemView.findViewById(R.id.chat_item_tv_message);
            mUsername = itemView.findViewById(R.id.chat_item_tv_username_right);
            mTimestamp = itemView.findViewById(R.id.chat_item_tv_timestamp);


        }


        public void bind(final Chat chat) {


            mMessage.setText(chat.getMessage());
            mUsername.setText(chat.getSender().getmUsername());
            mTimestamp.setText(convertDateToHour(chat.getCreatedDate()));

        }

        public void updateMessage(Chat chat, String currentUserId) {
            Boolean isSender = currentUserId.equals(chat.getSender().getmUid());

            mMessage.setText(chat.getMessage());
            //mMessage.setTextAlignment(isSender ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);
            if (chat.getCreatedDate() != null)
                mTimestamp.setText(convertDateToHour(chat.getCreatedDate()));
        }


        private String convertDateToHour(Date date) {
            DateFormat dfTime = new SimpleDateFormat("HH:mm");
            return dfTime.format(date);
        }
    }

}
