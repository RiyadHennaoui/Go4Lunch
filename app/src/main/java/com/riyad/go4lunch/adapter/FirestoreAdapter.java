package com.riyad.go4lunch.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>{

    private Query mQuery;
    private ListenerRegistration mRegistration;
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    public FirestoreAdapter(Query query){ mQuery = query; }

    public void startListening(){

    }

    public void stopListening(){

        if(mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        mSnapshots.clear();
        notifyDataSetChanged();
    }

    public void setQuery (Query query){
        // Stop listening
        stopListening();

        // Clear existing data
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();

    }

    @Override
    public int getItemCount() { return mSnapshots.size(); }

    protected DocumentSnapshot getSnapshot(int index) { return mSnapshots.get(index); }

    protected void onError(FirebaseFirestoreException e) {}

    protected void onDataChanged() {}

}
