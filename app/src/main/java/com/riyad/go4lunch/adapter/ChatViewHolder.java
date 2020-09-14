package com.riyad.go4lunch.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.riyad.go4lunch.R;
import com.riyad.go4lunch.model.Chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    //ROOT VIEW
    RelativeLayout rootView;

    //PROFILE CONTAINER
    LinearLayout profileContainer;
    ImageView imageViewProfile;
    ImageView imageViewIsMentor;

    //MESSAGE CONTAINER
    RelativeLayout messageContainer;
    //IMAGE SENDED CONTAINER
    CardView cardViewImageSent;
    ImageView imageViewSent;
    //TEXT MESSAGE CONTAINER
    LinearLayout textMessageContainer;
    TextView textViewMessage;
    RecyclerView recyclerView;
    //DATE TEXT
    TextView textViewDate;

    //FOR DATA
    private final int colorCurrentUser;
    private final int colorRemoteUser;


    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        rootView = itemView.findViewById(R.id.activity_mentor_chat_item_root_view);
        profileContainer = itemView.findViewById((R.id.activity_mentor_chat_item_profile_container));
        imageViewProfile = itemView.findViewById(R.id.activity_mentor_chat_item_profile_container_profile_image);
        imageViewIsMentor = itemView.findViewById(R.id.activity_mentor_chat_item_profile_container_is_mentor_image);
        messageContainer = itemView.findViewById(R.id.activity_mentor_chat_item_message_container);
        cardViewImageSent = itemView.findViewById(R.id.activity_mentor_chat_item_message_container_image_sent_cardview);
        imageViewSent = itemView.findViewById((R.id.activity_mentor_chat_item_message_container_image_sent_cardview_image));
        textMessageContainer = itemView.findViewById(R.id.activity_mentor_chat_item_message_container_text_message_container);
        textViewMessage = itemView.findViewById(R.id.activity_mentor_chat_item_message_container_text_message_container_text_view);
        textViewDate = itemView.findViewById(R.id.activity_mentor_chat_item_message_container_text_view_date);
        recyclerView = itemView.findViewById(R.id.activity_mentor_chat_recycler_view_not_container);


        colorCurrentUser = ContextCompat.getColor(itemView.getContext(), R.color.colorAccent);
        colorRemoteUser = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);

    }

    public void updateMessage(Chat chat, String currentUserId){

        // Check if current user is the sender
        Boolean isCurrentUser = currentUserId.equals(chat.getSender().getmUid());

        // Update message TextView
        this.textViewMessage.setText(chat.getMessage());
        this.textViewMessage.setTextAlignment(isCurrentUser ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

        // Update date TextView
        if (chat.getCreatedDate() != null) this.textViewDate.setText(this.convertDateToHour(chat.getCreatedDate()));

        // Update isMentor ImageView
        //this.imageViewIsMentor.setVisibility(message.getUserSender().getIsMentor() ? View.VISIBLE : View.INVISIBLE);

        // Update profile picture ImageView
        //if (message.getUserSender().getUrlPicture() != null)
        //    glide.load(message.getUserSender().getUrlPicture())
        //            .apply(RequestOptions.circleCropTransform())
        //            .into(imageViewProfile);

        // Update image sent ImageView
        //if (message.getUrlImage() != null){
        //    glide.load(message.getUrlImage())
        //            .into(imageViewSent);
        //    this.imageViewSent.setVisibility(View.VISIBLE);
        //} else {
            this.imageViewSent.setVisibility(View.GONE);
        //}

        //Update Message Bubble Color Background
        ((GradientDrawable) textMessageContainer.getBackground()).setColor(isCurrentUser ? colorCurrentUser : colorRemoteUser);

        // Update all views alignment depending is current user or not
        this.updateDesignDependingUser(isCurrentUser);
    }

    private void updateDesignDependingUser(Boolean isSender){

        // PROFILE CONTAINER
        RelativeLayout.LayoutParams paramsLayoutHeader = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutHeader.addRule(isSender ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        this.profileContainer.setLayoutParams(paramsLayoutHeader);

        // MESSAGE CONTAINER
        RelativeLayout.LayoutParams paramsLayoutContent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutContent.addRule(isSender ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.activity_mentor_chat_item_profile_container);
        this.messageContainer.setLayoutParams(paramsLayoutContent);

        // CARDVIEW IMAGE SEND
        RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsImageView.addRule(isSender ? RelativeLayout.ALIGN_LEFT : RelativeLayout.ALIGN_RIGHT, R.id.activity_mentor_chat_item_message_container_text_message_container);
        this.cardViewImageSent.setLayoutParams(paramsImageView);

        this.rootView.requestLayout();
    }

    // ---

    private String convertDateToHour(Date date){
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }

}
