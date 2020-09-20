package com.riyad.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.networking.ChatRepository;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<List<Chat>> getChatMutableLiveData;
    private MutableLiveData<Chat> addMessageToSenderMutableLiveData;
    private MutableLiveData<Chat> addMessageToRecipientMutableLiveData;
    private ChatRepository chatRepository;

    public void init(String recieverId){
        if (getChatMutableLiveData != null){
            return;
        }

        chatRepository = ChatRepository.getInstance();
        getChatMutableLiveData = chatRepository.getChat(recieverId);
        //TODO voir avec Thie comment faier pour les paramètres de la méthode (récup le message et le currentUser (Objet User))
//        addMessageToSenderMutableLiveData = chatRepository.addMessageToRecipient()

    }

    public LiveData<List<Chat>> getChat(){

        return getChatMutableLiveData;
    }

    public LiveData<Chat> addMessageToSenderCollection(){
        return addMessageToSenderMutableLiveData;
    }

    public LiveData<Chat> addMessageToReceiverCollection(){
        return addMessageToRecipientMutableLiveData;
    }


}
