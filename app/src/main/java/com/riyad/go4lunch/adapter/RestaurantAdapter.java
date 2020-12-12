package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riyad.go4lunch.DetailActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.ui.Restaurant;

import java.util.List;

import static com.riyad.go4lunch.utils.Constants.PLACE_ID;

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
        private final TextView distance;
        private final TextView restaurantAdress;
        private final TextView opening_hour;
        private final TextView workmatesNumbreBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantPicture = itemView.findViewById(R.id.item_restaurant_iv_main_picture_profile);
            countryFood = itemView.findViewById(R.id.item_restaurant_iv_circle_picture_country_food);
            restaurantName = itemView.findViewById(R.id.item_restaurant_tv_name);
            ratingBar = itemView.findViewById(R.id.item_restaurant_rating_star);
            distance = itemView.findViewById(R.id.item_restaurant_tv_distance);
            restaurantAdress = itemView.findViewById(R.id.item_restaurant_tv_adresse);
            opening_hour = itemView.findViewById(R.id.item_restaurant_tv_opening);
            workmatesNumbreBook = itemView.findViewById(R.id.item_restaurant_tv_number_of_workmates);
        }

        public void bind(Restaurant restaurant) {

            Log.i("restaurant" + getAdapterPosition(), restaurant.getName() + "");
            restaurantName.setText(restaurant.getName());

            float ratingFloat = Float.parseFloat(restaurant.getRating());
            ratingBar.setRating(ratingFloat);

            distance.setText(restaurant.getDistanceAsString() + "m");

            restaurantAdress.setText(restaurant.getRestaurantAdress());

            opening_hour.setText(restaurant.getRestaurantDetail().getFormattedOpeningHour());

            workmatesNumbreBook.setText("(" + restaurant.getBookingRestaurant().size() + ")");
            Glide.with(countryFood).load(restaurant.getRestaurantImageUrl()).circleCrop().into(countryFood);

            itemView.setOnClickListener(view -> {

                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(PLACE_ID, restaurant.getId());
                        context.startActivity(intent);

                    }
            );


        }


    }
}
