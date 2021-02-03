package com.riyad.go4lunch.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.riyad.go4lunch.model.Chat;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.ChatRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class ChatViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    ChatRepository chatRepository;
    private User author;
    private List<Chat> chatList;
    Chat chat;
    private MutableLiveData<List<Chat>> liveDataListOfChats;

    @InjectMocks
    ChatViewModel chatViewModel = new ChatViewModel();


    @Before
    public void config(){
        MockitoAnnotations.initMocks(this);

        author = new User();
        author.setmUid("1");
        author.setmUsername("author");

        chat = new Chat();
        chat.setMessage("new Message");
        chat.setAuthor(author);
        chat.setIsSender(true);

        chatList = new ArrayList<>();
        chatList.add(chat);

        liveDataListOfChats = new MutableLiveData<>();
        liveDataListOfChats.postValue(chatList);
        chatViewModel.receiverId = author.getmUid();
    }


    @Test
    public void getChat(){

        when(chatRepository.getChat(author.getmUid())).thenReturn(liveDataListOfChats);
        try {
            List<Chat> fakeChatList = JavaTestUtils.getOrAwaitValue(chatViewModel.getChat());
            assertEquals(fakeChatList, chatList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}