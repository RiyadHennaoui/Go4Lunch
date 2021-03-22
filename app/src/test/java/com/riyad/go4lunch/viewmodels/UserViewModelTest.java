package com.riyad.go4lunch.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.networking.UserRepository;

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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();


    @Mock
    private UserRepository userRepository;

    private User user1;
    private List<User> userList1 = new ArrayList<>();
    private MutableLiveData<User> livedataUser;
    private MutableLiveData<List<User>> livedataUsersList;
    @InjectMocks
    UserViewModel userViewModel = new UserViewModel();

    @Before
    public void config(){
        MockitoAnnotations.initMocks(this);
        user1 = new User();
        user1.setUid("1");
        user1.setUsername("moi");
        userList1.add(user1);
        livedataUser = new MutableLiveData<>();
        livedataUser.postValue(user1);
        livedataUsersList = new MutableLiveData<>();
        livedataUsersList.postValue(userList1);

    }

    @Test
    public void getUserInFirestore() {

        //Given fresh viewModel and fake user id
        String fakeUserId = "fake user id ";
        //When get user in database
        when(userRepository.getUser(fakeUserId)).thenReturn(livedataUser);
        try {
            User user = JavaTestUtils.getOrAwaitValue(userViewModel.getUser(fakeUserId));
            assertEquals(user, user1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Then the user is find
    }

    @Test
    public void creatUserInFirestore(){
       when(userRepository.createUser()).thenReturn(livedataUser);

        try {
            User fakeUserLive = JavaTestUtils.getOrAwaitValue(userViewModel.createUser());
            assertEquals(fakeUserLive, user1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void setUserName(){
        String fakeUserId = "fake user id ";
        String fakeUserName = "moi";
        when(userRepository.setNameProfileUser(fakeUserId, fakeUserName)).thenReturn(livedataUser);

        try{
            User user = JavaTestUtils.getOrAwaitValue(userViewModel.setUserName(fakeUserId, fakeUserName));
            assertEquals(user.getUsername(), user1.getUsername());
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @Test
    public void getUsersList(){

        when(userRepository.getUsersList()).thenReturn(livedataUsersList);
        try{
            List<User> userList = JavaTestUtils.getOrAwaitValue(userViewModel.getUsers());
            assertEquals(userList, userList1);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}