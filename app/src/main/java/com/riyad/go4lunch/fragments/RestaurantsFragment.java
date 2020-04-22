package com.riyad.go4lunch.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riyad.go4lunch.R;
import com.riyad.go4lunch.adapter.RestaurantAdapter;
import com.riyad.go4lunch.data.Restaurants;
import com.riyad.go4lunch.data.Result;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.viewmodels.RestaurantsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RestaurantsFragment extends Fragment {

    private RestaurantAdapter restaurantAdapter;
    private ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
    private RestaurantsViewModel restaurantsViewModel;


    public RestaurantsFragment() { }

    public static RestaurantsFragment newInstance(){ return new RestaurantsFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init ViewModel
        restaurantsViewModel = ViewModelProviders.of(requireActivity()).get(RestaurantsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_rv, container, false);

        restaurantAdapter = new RestaurantAdapter(restaurantArrayList, getContext());

        RecyclerView myRecyclerView = myView.findViewById(R.id.main_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(restaurantAdapter);

        return myView ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantsViewModel.init();
        restaurantsViewModel.getRestaurantRepository().observe(this, restaurants ->  {
            restaurantAdapter.setData(restaurants);

        });
    }

}
