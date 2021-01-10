package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.networking.ChatRepository;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<List<Chat>> getChatMutableLiveData;
    private ChatRepository chatRepository;
    String receiverId;


    public void init(String receiverId){
        if (getChatMutableLiveData != null){
            return;
        }

        this.receiverId = receiverId;
        chatRepository = ChatRepository.getInstance();
        getChatMutableLiveData = chatRepository.getChat(receiverId);

    }

    public LiveData<List<Chat>> getChat(){

        return getChatMutableLiveData;
    }

    public void sendMessage(String message){
        chatRepository.sendMessage(message, receiverId);
    }

}
