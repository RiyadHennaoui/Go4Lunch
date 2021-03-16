package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.riyad.go4lunch.utils.Constants.HOUR_FORMAT;
import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_LEFT;
import static com.riyad.go4lunch.utils.Constants.MSG_TYPE_RIGHT;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private List<Chat> chatData;
    Context context;


    public ChatAdapter(List<Chat> chatData, Context context) {
        this.chatData = chatData;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {

        if (!chatData.get(position).getIsSender()) {
            return MSG_TYPE_LEFT;
        } else {
            return MSG_TYPE_RIGHT;
        }
    }

    public class ChatHolder extends RecyclerView.ViewHolder {

        TextView mMessage;
        TextView mTimestamp;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.chat_item_tv_message);
            mTimestamp = itemView.findViewById(R.id.chat_item_tv_timestamp);
        }


        public void bind(final Chat chat) {

            mMessage.setText(chat.getMessage());
            mTimestamp.setText(convertDateToHour(chat.getCreatedDate()));
        }

        private String convertDateToHour(Date date) {
            DateFormat dfTime = new SimpleDateFormat(HOUR_FORMAT);
            return dfTime.format(date);
        }
    }
}
