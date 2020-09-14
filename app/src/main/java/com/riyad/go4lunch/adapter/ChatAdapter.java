package com.riyad.go4lunch.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;

public class ChatAdapter extends FirestoreRecyclerAdapter<Chat, ChatViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    //FOR DATA
    private final String idCurrentUser;

    //FOR COMMUNICATION
    private Listener callback;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Chat> options, Listener callback, String idCurrentUser) {
        super(options);
        this.callback = callback;
        this.idCurrentUser = idCurrentUser;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Chat model) {
        holder.updateMessage(model, this.idCurrentUser);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

}
