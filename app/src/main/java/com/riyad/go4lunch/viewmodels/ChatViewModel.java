package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.networking.ChatRepository;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<FirestoreRecyclerOptions<Chat>> firestoreRecyclerOptionsMutableLiveData;
    private ChatRepository chatRepository;

    public void init(String recieverId){
        if (firestoreRecyclerOptionsMutableLiveData != null){
            return;
        }

        chatRepository = ChatRepository.getInstance();
        firestoreRecyclerOptionsMutableLiveData = chatRepository.getChat(recieverId);
    }

    public LiveData<FirestoreRecyclerOptions<Chat>> getChat(){

        return  firestoreRecyclerOptionsMutableLiveData;
    }


}
