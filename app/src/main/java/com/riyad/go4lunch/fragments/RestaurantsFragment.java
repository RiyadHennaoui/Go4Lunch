package com.riyad.go4lunch.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riyad.go4lunch.R;
import com.riyad.go4lunch.adapter.RestaurantAdapter;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.viewmodels.DetailRestaurantViewModel;
import com.riyad.go4lunch.viewmodels.RestaurantsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.riyad.go4lunch.utils.Constants.CURRENT_DEVICE_LOCATION;
import static com.riyad.go4lunch.utils.Constants.SHARED_NAME;

public class RestaurantsFragment extends Fragment {

    private RestaurantAdapter restaurantAdapter;
    private ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
    private RestaurantsViewModel restaurantsViewModel;
    private SharedPreferences sharedPreferences;


    public RestaurantsFragment() {
    }

    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

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

        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(SHARED_NAME, MODE_PRIVATE);

        String currentLocation = sharedPreferences.getString(CURRENT_DEVICE_LOCATION, "");
        restaurantsViewModel.init(currentLocation);
        restaurantsViewModel.getRestaurants().observe(this.getActivity(), restaurants -> {
            restaurantAdapter.setData(restaurants);

        });


    }

    @Override
    public void onResume() {
        super.onResume();
        restaurantsViewModel.getRestaurants().observe(this.getActivity(), restaurants -> {
            restaurantAdapter.setData(restaurants);

        });
    }

    public void displayAutocompleteRestaurant(String restaurantId) {

        DetailRestaurantViewModel detailRestaurantViewModel;
        detailRestaurantViewModel = ViewModelProviders.of(this.getActivity()).get(DetailRestaurantViewModel.class);
        detailRestaurantViewModel.init();
        detailRestaurantViewModel.getDetailRestaurant(restaurantId).observe(this.getActivity(), restaurant -> {

            if (restaurant == null) {
                Toast.makeText(this.getActivity(), R.string.restaurant_fragment_restaurant_is_not_in_bounds, Toast.LENGTH_SHORT).show();
            } else {
                restaurantAdapter.setData(Arrays.asList(restaurant));
            }
        });


    }

}
