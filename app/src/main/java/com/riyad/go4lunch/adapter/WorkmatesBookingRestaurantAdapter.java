package com.riyad.go4lunch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.User;
import java.util.ArrayList;

public class WorkmatesBookingRestaurantAdapter extends RecyclerView.Adapter<WorkmatesBookingRestaurantAdapter.WorkmatesBookingHolder> {

    private ArrayList<User> usersBooking;
    private String name;


    public WorkmatesBookingRestaurantAdapter(ArrayList<User> usersBooking) {
        this.usersBooking = usersBooking;
    }

    @NonNull
    @Override
    public WorkmatesBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        name = parent.getResources().getString(R.string.rv_adapter_workmat_book);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker_booking, parent, false);
        return new WorkmatesBookingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesBookingHolder holder, int position) {
        holder.bind(usersBooking.get(position));
    }

    @Override
    public int getItemCount() { return usersBooking != null ? usersBooking.size() : 0; }

    public void setData(ArrayList<User> users){
        usersBooking = users;
        notifyDataSetChanged();
    }

    public class WorkmatesBookingHolder extends RecyclerView.ViewHolder{

        private ImageView photoProfile;
        private TextView workmateName;

        public WorkmatesBookingHolder(@NonNull View itemView) {
            super(itemView);

            photoProfile = itemView.findViewById(R.id.item_worker_booking_iv_photo_profile);
            workmateName = itemView.findViewById(R.id.item_worker_booking_tv_worker_name);
        }

        public void bind(final User user){


            workmateName.setText(user.getUsername() + name);
            Glide.with(photoProfile).load(user.getUrlPicture()).circleCrop().into(photoProfile);
        }


    }


}
