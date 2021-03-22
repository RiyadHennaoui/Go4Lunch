package com.riyad.go4lunch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riyad.go4lunch.MainActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.adapter.WorkmateNewAdapter;
import com.riyad.go4lunch.model.User;
import com.riyad.go4lunch.viewmodels.UserViewModel;


import java.util.ArrayList;


public class WorkmateFragment extends Fragment {


    private UserViewModel userViewModel;
    private WorkmateNewAdapter newAdapter;
    private ArrayList<User> usersList = new ArrayList<>();
    private Button search;


    public static WorkmateFragment newInstance() {
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
        userViewModel.getUsers().observe(this.getActivity(), users -> {
            newAdapter.setData(users);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setVisibiltysearch(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).setVisibiltysearch(true);
    }
}
