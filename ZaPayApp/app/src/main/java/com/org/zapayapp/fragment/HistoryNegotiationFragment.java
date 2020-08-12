package com.org.zapayapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.org.zapayapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryNegotiationFragment extends Fragment {

    public HistoryNegotiationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_negotiation, container, false);
    }
}
