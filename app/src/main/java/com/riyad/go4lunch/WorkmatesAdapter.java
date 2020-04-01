package com.riyad.go4lunch;

import android.content.Context;
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

import java.util.List;


public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolder> {

    private List<User> mDataUsers;
    private Context context;


    public WorkmatesAdapter(Context con) {
        this.context = con;
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
        holder.bind(mDataUsers.get(position));
    }

    @Override
    public int getItemCount() { return mDataUsers != null ? mDataUsers.size() : 0; }

    public void setData(@NonNull List<User> users){
        mDataUsers = users;
        notifyDataSetChanged();
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

        public void bind(User user) {

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
