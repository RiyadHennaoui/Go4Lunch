package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_LEFT;
import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_RIGHT;

public class MessageAdapter extends FirestoreRecyclerAdapter<Chat, MessageAdapter.ChatHolder> {

    private Context mContext;
    private ArrayList<Chat> mChat;
    //    private String mImageUrl;
    private FirebaseUser mCurrentUser;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Chat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


//        if (viewType == MSG_TYPE_RIGHT) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
//            return new ChatHolder(v);
//        } else {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
//            return new ChatHolder(v);
//        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
        return new ChatHolder(v);
    }

//    public interface Listner{

//        void onDataChanged();
//    }
//    private Listner callback;
    // public MessageAdapter(Context mContext, List<Chat> mChat, String mImageUrl) {
    // this.mContext = mContext;
    //   this.mChat = mChat;

    //     this.mImageUrl = mImageUrl;
    //   }
//    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Chat> options) {
//        super(options);

//

//    }


    //    @Override
//    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
//        holder.bind(mChat.get(position));

//    }
//    @Override
//    public int getItemViewType(int position) {
//
////        if (!mCurrentUser.getUid().equals(mChat.get(position).getSender().getmUid())) {
//        if (!mChat.get(position).getIsSender()){
//            return MSG_TYPE_LEFT;
//        } else {
//            return MSG_TYPE_RIGHT;
//        }
//

//    }


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
//            mUsername = itemView.findViewById(R.id.chat_item_tv_username_right);
            mTimestamp = itemView.findViewById(R.id.chat_item_tv_timestamp);
            receiverPhoto = itemView.findViewById(R.id.chat_activity_iv_photo_receiver);


        }


        public void bind(final Chat chat) {

            mMessage.setText(chat.getMessage());
            mTimestamp.setText(convertDateToHour(chat.getCreatedDate()));
            sortSenderOrReciever(chat);
            Glide.with(receiverPhoto).load(chat.getAuther().getmUrlPicture()).circleCrop().into(receiverPhoto);

        }

        private void sortSenderOrReciever(Chat chat) {
            if (chat.getIsSender()) {
                mUsername.setText(mCurrentUser.getDisplayName());
            } else {
                mUsername.setText(chat.getAuther().getmUsername());
            }
        }




        private String convertDateToHour(Date date) {
            DateFormat dfTime = new SimpleDateFormat("HH:mm");
            return dfTime.format(date);
        }
    }

}
