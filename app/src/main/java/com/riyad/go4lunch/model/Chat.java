package com.riyad.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private String mName;
    private String mMessage;
    private String mUid;
    private Date mTimestamp;

    public Chat() { } // Needed for Firebase

    public Chat(String name, String message, String uid) {
        mName = name;
        mMessage = message;
        mUid = uid;
    }

    // --- GETTERS --
    public String getName() { return mName; }
    public String getMessage() { return mMessage; }
    public String getUid() { return mUid; }
    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    // --- SETTERS ---
    public void setMessage(String message) { mMessage = message; }
    public void setName(String name) { mName = name; }
    public void setUid(String uid) { mUid = uid; }
    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }
}
