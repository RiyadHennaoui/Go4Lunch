package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.riyad.go4lunch.ChatActivity;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.User;

import static com.riyad.go4lunch.utils.Constants.WORKMATE_ID;
import static com.riyad.go4lunch.utils.Constants.WORKMATE_PICTURE_URL;
import static com.riyad.go4lunch.utils.Constants.WORKMATE_USERNAME;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesAdapter.UsersHolder> {

    Context context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersHolder holder, int position, @NonNull User user) {
        holder.bind(user);
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker, parent, false);
        return new UsersHolder(v);
    }

    public class UsersHolder extends RecyclerView.ViewHolder {

        private CardView mMainCardView;
        private final ImageView mIvProfileCircle;
        private final TextView mFirstName;
        private final TextView mWhereLunch;
        private final Button mChat;


        public UsersHolder(@NonNull View itemView) {
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
}
