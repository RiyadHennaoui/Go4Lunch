package com.riyad.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private User mAuthor;
    private String mMessage;
    private Boolean isSender;
    @ServerTimestamp
    private Date mCreatedDate;

    public Chat() { } // Needed for Firebase

    public Chat(User author, String message) {
        mAuthor = author;
        mMessage = message;

    }

    // --- GETTERS --
    public User getAuthor() { return mAuthor; }
    public String getMessage() { return mMessage; }
    public Boolean getIsSender()  { return  isSender; }
    public Date getCreatedDate() { return mCreatedDate; }

    // --- SETTERS ---
    public void setMessage(String message) { mMessage = message; }
    public void setAuthor(User receiver) { mAuthor = receiver; }
    public void setIsSender(Boolean sender) { isSender = sender; }
    public void setCreatedDate(Date createdDate) { mCreatedDate = createdDate; }
}
