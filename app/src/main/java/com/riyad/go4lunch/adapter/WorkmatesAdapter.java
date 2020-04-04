package com.riyad.go4lunch.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.riyad.go4lunch.R;
import com.riyad.go4lunch.User;

import java.util.List;


public class WorkmatesAdapter extends FirestoreAdapter<WorkmatesAdapter.ViewHolder> {


    private Context context;

    public WorkmatesAdapter(Query query, Context context) {
        super(query);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_worker, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout expandableCardView;
        private CardView mMainCardView;
        private final ImageView mIvProfileMain;
        private final ImageView mIvProfileCircle;
        private final TextView mFirstName;
        private final TextView mMailWorkmate;
        private final Button mExtandButton;
        private final Button mChat;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            mIvProfileMain = itemView.findViewById(R.id.item_worker_iv_main_picture_profile);
            mIvProfileCircle = itemView.findViewById(R.id.item_worker_iv_circle_picture_profile);
            mFirstName = itemView.findViewById(R.id.item_worker_tv_name);
            mMailWorkmate = itemView.findViewById(R.id.item_worker_tv_mail);
            mExtandButton = itemView.findViewById(R.id.item_worker_bt_expand);
            mChat = itemView.findViewById(R.id.item_worker_bt_chat);
            expandableCardView = itemView.findViewById(R.id.item_worker_expandableView);
            mMainCardView = itemView.findViewById(R.id.item_worker_main_cardview);
        }

        public void bind(final DocumentSnapshot snapshot) {


            User user = snapshot.toObject(User.class);
            Resources resources = itemView.getResources();

            mFirstName.setText(user.getmUsername());
            mMailWorkmate.setText(user.getmMail());

            Glide.with(mIvProfileMain).load(user.getmUrlPicture()).centerCrop().into(mIvProfileMain);
            Glide.with(mIvProfileCircle).load(user.getmUrlPicture()).circleCrop().into(mIvProfileCircle);

            mExtandButton.setOnClickListener(view -> {

                if (expandableCardView.getVisibility() == View.GONE){

                    TransitionManager.beginDelayedTransition(mMainCardView, new AutoTransition());
                    expandableCardView.setVisibility(View.VISIBLE);
                    mExtandButton.setBackgroundResource(R.drawable.ic_expand_less_black_24dp);

                }else{
                    TransitionManager.beginDelayedTransition(mMainCardView, new AutoTransition());
                    expandableCardView.setVisibility(View.GONE);
                    mExtandButton.setBackgroundResource(R.drawable.ic_expand_more_black_24dp);
                }

            });

            mChat.setOnClickListener(view -> {

                //TODO Crée un intent vers l'activité CHAT.

            });

        }
    }


}
