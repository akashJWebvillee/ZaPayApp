package com.org.zapayapp.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.TransactionAdapter;
import com.org.zapayapp.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NegotiationFragment extends Fragment {
private RecyclerView negotiationRecyclerView;
    private List<TransactionModel> trasactionList;
    public NegotiationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_negotiation, container, false);
        inIt(view);
        initAction();
        return view;
    }

    private void inIt(View view){
        trasactionList=new ArrayList<>();
        negotiationRecyclerView=view.findViewById(R.id.negotiationRecyclerView);
    }

    private void initAction() {
        negotiationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        negotiationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity(),trasactionList,"negotiation");
        negotiationRecyclerView.setAdapter(transactionAdapter);
    }
}
