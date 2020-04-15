package com.riyad.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private String mReceiver;
    private String mMessage;
    private String mSender;
    private Date mTimestamp;

    public Chat() { } // Needed for Firebase

    public Chat(String receiver, String message, String sender) {
        mReceiver = receiver;
        mMessage = message;
        mSender = sender;
    }

    // --- GETTERS --
    public String getReceiver() { return mReceiver; }
    public String getMessage() { return mMessage; }
    public String getSender() { return mSender; }
    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    // --- SETTERS ---
    public void setMessage(String message) { mMessage = message; }
    public void setRecieiver(String name) { mReceiver = name; }
    public void setSender(String uid) { mSender = uid; }
    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }
}
