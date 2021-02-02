package com.org.zapayapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.TransactionActivity;
import com.org.zapayapp.adapters.TransactionAdapter;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.EndlessRecyclerViewScrollListener;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class NegotiationFragment extends Fragment implements APICallback {

    private TransactionActivity activity;

    private RecyclerView negotiationRecyclerView;
    private List<TransactionModel> transactionList;
    private TransactionAdapter transactionAdapter;

    private EndlessRecyclerViewScrollListener scrollListener;
    private int pageNo = 0;
    private TextView noDataTv;


    public NegotiationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_negotiation, container, false);
        inIt(view);
        initAction();
        return view;
    }

    private void inIt(View view) {
        activity = (TransactionActivity) getActivity();
        transactionList = new ArrayList<>();
        negotiationRecyclerView = view.findViewById(R.id.negotiationRecyclerView);
        noDataTv = view.findViewById(R.id.noDataTv);
    }

    private void initAction() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        negotiationRecyclerView.setLayoutManager(layoutManager);
        negotiationRecyclerView.setItemAnimator(new DefaultItemAnimator());

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pageNo = page;
                callAPIGetTransactionRequest(pageNo);
            }
        };
        negotiationRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 0;
        callAPIGetTransactionRequest(pageNo);
    }

    private void callAPIGetTransactionRequest(int pageNo) {
        //  0=pending 1=negotiate, 2=accept
        if (pageNo == 0 && scrollListener != null) {
            scrollListener.resetState();
        }
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = activity.apiCalling.getHashMapObject(
                    "status", "1",
                    "page", pageNo);

            activity.zapayApp.setApiCallback(this);
            Call<JsonElement> call = activity.restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_request), values);
            if (activity.apiCalling != null) {
                activity.apiCalling.callAPI(activity.zapayApp, call, getString(R.string.api_get_transaction_request), negotiationRecyclerView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (from.equals(getResources().getString(R.string.api_get_transaction_request))) {
                if (status == 200) {
                    if (pageNo == 0) {
                        transactionList.clear();
                    }
                    List<TransactionModel> list = activity.apiCalling.getDataList(json, "data", TransactionModel.class);
                    if (list.size() > 0) {
                        noDataTv.setVisibility(View.GONE);
                        negotiationRecyclerView.setVisibility(View.VISIBLE);
                        transactionList.addAll(list);
                        setAdapter();

                    } else {
                        if (pageNo == 0) {
                            noDataTv.setVisibility(View.VISIBLE);
                            negotiationRecyclerView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (pageNo == 0) {
                        noDataTv.setVisibility(View.VISIBLE);
                        negotiationRecyclerView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void setAdapter() {
        if (transactionAdapter==null){
            transactionAdapter = new TransactionAdapter(getActivity(), transactionList, getString(R.string.transaction));
            negotiationRecyclerView.setAdapter(transactionAdapter);
        }else {
            transactionAdapter.notifyDataSetChanged();
        }

    }
}

