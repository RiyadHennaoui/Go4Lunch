package com.riyad.go4lunch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.riyad.go4lunch.adapter.WorkmatesAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkmateFragment extends Fragment {

    private WorkmatesAdapter mWorkmatesAdapter;
    private List<User> mUser = new ArrayList<>();
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private RecyclerView mRecyclerView;
    private ViewGroup mEmptyView;

    public static WorkmateFragment newInstance(){
        return new WorkmateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_rv, container, false);

        mRecyclerView = myView.findViewById(R.id.main_rv);

        initFirestore();
        initRecyclerView();




        return myView;
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void initRecyclerView(){

        if (mQuery == null){
            Log.w("Workmate fragment", "No query, not initializing RV" );
        }

        mWorkmatesAdapter = new WorkmatesAdapter(mQuery, getContext()){

            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("Workmate Fragment", e.toString());
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mWorkmatesAdapter);

    }
}
