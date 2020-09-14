package com.riyad.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private User mReceiver;
    private String mMessage;
    private User mTheSender;
    private Boolean isSender;
    private Date mCreatedDate;

    public Chat() { } // Needed for Firebase

    public Chat(User receiver, String message, User sender) {
        mReceiver = receiver;
        mMessage = message;
        mTheSender = sender;
    }

    // --- GETTERS --
    public User getReceiver() { return mReceiver; }
    public String getMessage() { return mMessage; }
    public User getSender() { return mTheSender; }
    public Boolean getIsSender()  { return  isSender; }
    @ServerTimestamp
    public Date getCreatedDate() { return mCreatedDate; }

    // --- SETTERS ---
    public void setMessage(String message) { mMessage = message; }
    public void setRecieiver(User receiver) { mReceiver = receiver; }
    public void setSender(User uid) { mTheSender = uid; }
    public void setIsSender(Boolean sender) { isSender = sender; }
    public void setCreatedDate(Date createdDate) { mCreatedDate = createdDate; }
}
