package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;

import java.util.List;

import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_LEFT;
import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_RIGHT;

public class MessageAdapter extends FirestoreRecyclerAdapter<Chat, MessageAdapter.ChatHolder > {

    private Context mContext;
    private List<Chat> mChat;
    private String mImageUrl;
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
        if (viewType == MSG_TYPE_RIGHT){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ChatHolder(v);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ChatHolder(v);
        }
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
            mUsername = itemView.findViewById(R.id.chat_item_tv_username);
            mTimestamp = itemView.findViewById(R.id.chat_item_tv_timestamp);

        }

        public void bind(final Chat chat){


        mMessage.setText(chat.getMessage());
        mUsername.setText(chat.getSender());
//        mTimestamp.setText(chat.getTimestamp().toString());




        }
    }

    @Override
    public int getItemViewType(int position) {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(mCurrentUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
