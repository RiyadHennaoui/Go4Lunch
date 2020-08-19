package com.riyad.go4lunch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.User;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesAdapter.UsersHolder> {


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

        private ConstraintLayout expandableCardView;
        private CardView mMainCardView;
        private final ImageView mIvProfileMain;
        private final ImageView mIvProfileCircle;
        private final TextView mFirstName;
        private final TextView mMailWorkmate;
        private final Button mExtandButton;
        private final Button mChat;


        public UsersHolder(@NonNull View itemView) {
            super(itemView);


            mIvProfileMain = itemView.findViewById(R.id.item_worker_iv_main_picture_profile);
            mIvProfileCircle = itemView.findViewById(R.id.item_worker_iv_circle_picture_profile);
            mFirstName = itemView.findViewById(R.id.item_worker_tv_name);
            mMailWorkmate = itemView.findViewById(R.id.item_worker_tv_mail);
            mExtandButton = itemView.findViewById(R.id.item_worker_bt_expand);
            mChat = itemView.findViewById(R.id.item_worker_bt_chat);
            expandableCardView = itemView.findViewById(R.id.item_worker_expandableView);
            mMainCardView = itemView.findViewById(R.id.item_worker_main_cardview);
            mIvProfileMain.setVisibility(View.VISIBLE);
        }

        public void bind(final User user) {


            mFirstName.setText(user.getmUsername());
            mMailWorkmate.setText(user.getmMail());

            Glide.with(mIvProfileMain).load(user.getmUrlPicture()).centerCrop().into(mIvProfileMain);
            Glide.with(mIvProfileCircle).load(user.getmUrlPicture()).circleCrop().into(mIvProfileCircle);

            mExtandButton.setOnClickListener(view -> {

                if (expandableCardView.getVisibility() == View.GONE) {

                    TransitionManager.beginDelayedTransition(mMainCardView, new AutoTransition());
                    expandableCardView.setVisibility(View.VISIBLE);
                    mExtandButton.setBackgroundResource(R.drawable.ic_expand_less_black_24dp);

                } else {
                    TransitionManager.beginDelayedTransition(mMainCardView, new AutoTransition());
                    expandableCardView.setVisibility(View.GONE);
                    mExtandButton.setBackgroundResource(R.drawable.ic_expand_more_black_24dp);
                }

            });

            mChat.setOnClickListener(view -> {

                //TODO Crée un intent vers l'activitée CHAT.

            });

        }
    }
}
