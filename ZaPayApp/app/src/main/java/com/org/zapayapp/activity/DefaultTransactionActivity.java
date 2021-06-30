package com.org.zapayapp.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.DefaultTransactionAdapter;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.EndlessRecyclerViewScrollListener;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class DefaultTransactionActivity extends BaseActivity implements View.OnClickListener, APICallback {
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;
    private RecyclerView defaultTransactionRecView;
    private DefaultTransactionAdapter defaultTransactionAdapter;
    private int pageNo = 0;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ArrayList<TransactionModel> defaultArrayList;
    private TextView noDataTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        inIt();
        inItAction();
    }

    private void inIt() {
        defaultArrayList = new ArrayList<>();
        toolbar = findViewById(R.id.defaultScreenToolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.default_transaction));
        defaultTransactionRecView = findViewById(R.id.defaultTransactionRecView);
        noDataTv = findViewById(R.id.noDataTv);
        backArrowImageView.setOnClickListener(this);
    }

    private void inItAction() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        defaultTransactionRecView.setLayoutManager(linearLayoutManager);
        defaultTransactionRecView.setItemAnimator(new DefaultItemAnimator());
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pageNo = page;
                callAPIGetDefaultTransactions(pageNo);
            }
        };

        defaultTransactionRecView.addOnScrollListener(scrollListener);
        callAPIGetDefaultTransactions(pageNo);
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    private void setAdapter() {
        if (defaultTransactionAdapter == null) {
            defaultTransactionAdapter = new DefaultTransactionAdapter(this, defaultArrayList);
            defaultTransactionRecView.setAdapter(defaultTransactionAdapter);
        } else {
            defaultTransactionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrowImageView:
                finish();
                break;
        }
    }

    private void callAPIGetDefaultTransactions(int page) {
        if (page == 0 && scrollListener != null) {
            scrollListener.resetState();
        }

        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "page", page);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_default_transactions), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_default_transactions), titleTV);
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

            if (from.equals(getResources().getString(R.string.api_get_default_transactions))) {
                if (status == 200) {
                    if (pageNo == 0) {
                        defaultArrayList.clear();
                    }
                    List<TransactionModel> defaultList = apiCalling.getDataList(json, "data", TransactionModel.class);
                    if (defaultList.size() > 0) {
                        noDataTv.setVisibility(View.GONE);
                        defaultTransactionRecView.setVisibility(View.VISIBLE);
                        defaultArrayList.addAll(defaultList);
                        setAdapter();
                    } else {
                        if (pageNo == 0) {
                            noDataTv.setVisibility(View.VISIBLE);
                            defaultTransactionRecView.setVisibility(View.GONE);
                        }
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    //showSimpleAlert(msg, "");
                    if (pageNo == 0) {
                        noDataTv.setVisibility(View.VISIBLE);
                        defaultTransactionRecView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}