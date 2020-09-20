package com.riyad.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private User mAuther;
    private String mMessage;
    private Boolean isSender;
    private Date mCreatedDate;

    public Chat() { } // Needed for Firebase

    public Chat(User auther, String message) {
        mAuther = auther;
        mMessage = message;

    }

    // --- GETTERS --
    public User getAuther() { return mAuther; }
    public String getMessage() { return mMessage; }

    public Boolean getIsSender()  { return  isSender; }
    @ServerTimestamp
    public Date getCreatedDate() { return mCreatedDate; }

    // --- SETTERS ---
    public void setMessage(String message) { mMessage = message; }
    public void setAuther(User receiver) { mAuther = receiver; }
    public void setIsSender(Boolean sender) { isSender = sender; }
    public void setCreatedDate(Date createdDate) { mCreatedDate = createdDate; }
}
