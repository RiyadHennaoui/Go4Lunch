package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.data.Result;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> restaurantData;
    Context context;

    public RestaurantAdapter(List<Restaurant> restaurantData, Context context) {
        this.restaurantData = restaurantData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(restaurantData.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantData != null ? restaurantData.size() : 0;
    }

    public void setData(@NonNull List<Restaurant> restaurants) {
        restaurantData = restaurants;
        Log.i("taille restaurant", restaurantData.size() + "");
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView restaurantPicture;
        private final ImageView countryFood;
        private final TextView restaurantName;
        private final RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantPicture = itemView.findViewById(R.id.item_restaurant_iv_main_picture_profile);
            countryFood = itemView.findViewById(R.id.item_restaurant_iv_circle_picture_country_food);
            restaurantName = itemView.findViewById(R.id.item_restaurant_tv_name);
            ratingBar = itemView.findViewById(R.id.item_restaurant_rating_star);
        }

        public void bind(Restaurant restaurant) {

            Log.i("restaurant" + getAdapterPosition(), restaurant.getName() + "");
            restaurantName.setText(restaurant.getName());

            //TODO trouver comment implemanter les etoiles et une image pour le type de cuisine.


            Glide.with(countryFood).load(restaurant.getRestaurantImageUrl()).circleCrop().into(countryFood);

            itemView.setOnClickListener(view -> {
                        //TODO INTENT vers l'acitivtée détail Restaurant.
                    }
            );

        }
    }
}
