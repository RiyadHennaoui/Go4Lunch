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
    private MutableLiveData<User> livedataUser;
    @InjectMocks
    UserViewModel userViewModel = new UserViewModel();

    @Before
    public void config(){
        MockitoAnnotations.initMocks(this);
        user1 = new User();
        user1.setmUid("1");
        user1.setmUsername("moi");
        livedataUser = new MutableLiveData<User>();
        livedataUser.postValue(user1);

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
       when(userRepository.creatUser()).thenReturn(livedataUser);

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
            assertEquals(user.getmUsername(), user1.getmUsername());
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}