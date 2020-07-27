package com.org.zapayapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.zapayapp.R;
import com.org.zapayapp.activity.HistoryActivity;
import com.org.zapayapp.adapter.HistoryAdapter;
import com.org.zapayapp.adapters.TransactionAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment {
    private RecyclerView pendingRecyclerView;

    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pending, container, false);

        inIt(view);
        initAction();
        return view;
    }

    private void inIt(View view){
        pendingRecyclerView=view.findViewById(R.id.pendingRecyclerView);
    }

    private void initAction() {
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        pendingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity());
        pendingRecyclerView.setAdapter(transactionAdapter);
    }
}
