package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.networking.ChatRepository;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private ChatRepository chatRepository;
    public String receiverId;


    public void init(String receiverId){
        this.receiverId = receiverId;
        chatRepository = ChatRepository.getInstance();
    }

    public LiveData<List<Chat>> getChat(){

        return chatRepository.getChat(receiverId);
    }

    public void sendMessage(String message){
        chatRepository.sendMessage(message, receiverId);
    }

}
