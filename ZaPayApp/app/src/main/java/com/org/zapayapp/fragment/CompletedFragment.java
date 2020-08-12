package com.org.zapayapp.fragment;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.activity.TransactionActivity;
import com.org.zapayapp.adapters.TransactionAdapter;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.EndlessRecyclerViewScrollListener;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedFragment extends Fragment implements APICallback, SimpleAlertFragment.AlertSimpleCallback{
    /*Code for API calling*/
    protected ZapayApp zapayApp;
    protected Gson gson;
    protected APICalling apiCalling;
    protected RestAPI restAPI;


    TransactionActivity activity;

    private List<TransactionModel> trasactionList;
    private RecyclerView completedRecyclerView;


    private EndlessRecyclerViewScrollListener scrollListener;
    private int pageNo=0;
    private TextView noDataTv;

    public CompletedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_completed, container, false);
        apicodeInit();
        inIt(view);
        initAction();
        return view;
    }

    private void apicodeInit() {
        zapayApp = (ZapayApp) getActivity().getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(getContext());
    }

    private void inIt(View view){
        trasactionList=new ArrayList<>();
        activity = (TransactionActivity) getActivity();
        completedRecyclerView=view.findViewById(R.id.completedRecyclerView);
        noDataTv=view.findViewById(R.id.noDataTv);
    }

    private void initAction() {
       LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        completedRecyclerView.setLayoutManager(layoutManager);
        completedRecyclerView.setItemAnimator(new DefaultItemAnimator());


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pageNo = page;
                callAPIGetTransactionRequest(pageNo);
            }
        };

        completedRecyclerView.addOnScrollListener(scrollListener);
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
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "status", "2",
                    "page", pageNo);

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_request), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_request), completedRecyclerView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        Log.e("json", "json======" + json);
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
                        trasactionList.clear();
                    }

                    List<TransactionModel> list = apiCalling.getDataList(json, "data", TransactionModel.class);
                    if (list.size() > 0) {
                        noDataTv.setVisibility(View.GONE);
                        completedRecyclerView.setVisibility(View.VISIBLE);
                        trasactionList.addAll(list);
                        setAdapter();

                    } else {
                        if (pageNo == 0) {
                            noDataTv.setVisibility(View.VISIBLE);
                            completedRecyclerView.setVisibility(View.GONE);
                        }
                    }

                }else {
                    if (pageNo == 0) {
                        noDataTv.setVisibility(View.VISIBLE);
                        completedRecyclerView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }


    private void setAdapter() {
        TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity(), trasactionList, "pending");
        completedRecyclerView.setAdapter(transactionAdapter);
    }

    public void showSimpleAlert(String message, String from) {
        try {
            FragmentManager fm = activity.getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("header", message);
            args.putString("textOk", getString(R.string.ok));
            args.putString("textCancel", getString(R.string.cancel));
            args.putString("from", from);
            SimpleAlertFragment alert = new SimpleAlertFragment();
            alert.setArguments(args);
            alert.show(fm, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSimpleCallback(String from) {
        if (from.equals(getResources().getString(R.string.api_get_transaction_request))) {
            ;

        }
    }
}

