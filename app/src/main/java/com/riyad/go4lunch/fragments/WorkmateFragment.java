package com.riyad.go4lunch.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.riyad.go4lunch.ProfileActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.adapter.WorkmateNewAdapter;
import com.riyad.go4lunch.adapter.WorkmatesAdapter;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.riyad.go4lunch.utils.Constants.COLLECTION_USER_NAME;
import static com.riyad.go4lunch.utils.Constants.ORDERBY_BOOKING_RESTAURANT;
import static com.riyad.go4lunch.utils.Constants.ORDERBY_USERNAME;

public class WorkmateFragment extends Fragment {


    private UserViewModel userViewModel;
    private WorkmateNewAdapter newAdapter;
    private ArrayList<User> usersList = new ArrayList<>();


    public static WorkmateFragment newInstance(){
        return new WorkmateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_rv, container, false);


        newAdapter = new WorkmateNewAdapter(usersList, getContext());
        RecyclerView myRecyclerView = myView.findViewById(R.id.main_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(newAdapter);




        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel.init();
        userViewModel.getUsers().observe(this.getActivity(), users ->{
            newAdapter.setData(users);
        });
    }



}
