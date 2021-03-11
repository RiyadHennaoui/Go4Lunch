package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riyad.go4lunch.ChatActivity;
import com.riyad.go4lunch.DetailActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.User;

import java.util.List;

import static com.riyad.go4lunch.utils.Constants.PLACE_ID;
import static com.riyad.go4lunch.utils.Constants.WORKMATE_ID;
import static com.riyad.go4lunch.utils.Constants.WORKMATE_PICTURE_URL;
import static com.riyad.go4lunch.utils.Constants.WORKMATE_USERNAME;

public class WorkmateNewAdapter extends RecyclerView.Adapter<WorkmateNewAdapter.WorkmatesHolder> {

    private List<User> usersData;
    Context context;

    public WorkmateNewAdapter(List<User> usersData, Context context){
        this.usersData = usersData;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkmatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_worker, parent, false);
        return new WorkmatesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesHolder holder, int position) {
        holder.bind(usersData.get(position));
    }

    @Override
    public int getItemCount() {
        return usersData != null ? usersData.size() : 0;
    }

    public void setData(@NonNull List<User> users){
        usersData = users;
        notifyDataSetChanged();
    }

    public class WorkmatesHolder extends RecyclerView.ViewHolder {

        private CardView mMainCardView;
        private final ImageView mIvProfileCircle;
        private final TextView mFirstName;
        private final TextView mWhereLunch;
        private final Button mChat;


        public WorkmatesHolder(@NonNull View itemView) {
            super(itemView);

            mIvProfileCircle = itemView.findViewById(R.id.item_worker_iv_circle_picture_profile);
            mFirstName = itemView.findViewById(R.id.item_worker_tv_name);
            mWhereLunch = itemView.findViewById(R.id.item_worker_tv_where_he_lunch);
            mChat = itemView.findViewById(R.id.item_worker_bt_chat);
            mMainCardView = itemView.findViewById(R.id.item_worker_main_cardview);

        }

        public void bind(final User user) {


            mFirstName.setText(user.getmUsername());
            if(user.getBookingRestaurant().getRestaurantName() != null){
                mWhereLunch.setText(itemView.getContext().getString(R.string.workmates_adapter_is_eating_at, user.getBookingRestaurant().getRestaurantName()));
                mWhereLunch.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                mWhereLunch.setTypeface(mWhereLunch.getTypeface(), Typeface.BOLD);
                mMainCardView.setOnClickListener(v -> {
                    toDetailRestaurant(user.getBookingRestaurant().getRestaurantId(), v.getContext());
                });
            }else{
                mWhereLunch.setText(R.string.workmates_adapter_hasnt_decided_yet);
                mWhereLunch.setTypeface(mWhereLunch.getTypeface(), Typeface.ITALIC);
            }




            Glide.with(mIvProfileCircle).load(user.getmUrlPicture()).circleCrop().into(mIvProfileCircle);

            mChat.setOnClickListener(view -> toChat(user.getmUid(),
                    user.getmUrlPicture(),
                    user.getmUsername(),
                    view.getContext()));
        }
    }

    private void toChat(String workmateId, String workmatePhotoUrl, String workmateUsername, Context context){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(WORKMATE_ID, workmateId);
        intent.putExtra(WORKMATE_PICTURE_URL, workmatePhotoUrl);
        intent.putExtra(WORKMATE_USERNAME, workmateUsername);
        context.startActivity(intent);

    }

    private void toDetailRestaurant(String restaurantId, Context context){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(PLACE_ID, restaurantId);
        context.startActivity(intent);
    }
}
