package com.org.zapayapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.adapters.ViewAllHistoryAndTransactionDetailsAdapter;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class ViewAllHistoryAndTransactionDetailsActivity extends BaseActivity implements View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback {
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;
    private RecyclerView historyDetailRecView;
    private ViewAllHistoryAndTransactionDetailsAdapter viewAllHistoryAndTransactionDetailsAdapter;
    private List<TransactionModel> allTransactionArrayList;
    private String moveFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        inIt();
        inItAction();
        getIntentFunc();
    }

    private void inIt() {
        toolbar = findViewById(R.id.viewAllDetailToolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.all_details));
        historyDetailRecView = findViewById(R.id.historyDetailRecView);
        backArrowImageView.setOnClickListener(this);
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    private void getIntentFunc() {
        if (getIntent().getStringExtra("transactionId") != null && getIntent().getStringExtra("moveFrom") != null) {
            String transactionId = getIntent().getStringExtra("transactionId");
            moveFrom = getIntent().getStringExtra("moveFrom");

            if (getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
                callAPIGetAllTransactionDetail(transactionId);
            } else if (getString(R.string.history).equalsIgnoreCase(moveFrom)) {
                callAPIGetAllTransactionHistoryDetail(transactionId);
            }
        }
    }

    private void inItAction() {

    }

    private void setAdapter() {
        historyDetailRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        viewAllHistoryAndTransactionDetailsAdapter = new ViewAllHistoryAndTransactionDetailsAdapter(this, allTransactionArrayList, gson, moveFrom);
        historyDetailRecView.setAdapter(viewAllHistoryAndTransactionDetailsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrowImageView:
                finish();
                break;
        }
    }

    private void callAPIGetAllTransactionDetail(String transaction_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_all_transaction_request_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_all_transaction_request_details), titleTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetAllTransactionHistoryDetail(String transaction_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_all_transaction_history_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_all_transaction_history_details), titleTV);
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

            if (from.equals(getResources().getString(R.string.api_get_all_transaction_request_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonArray() != null && json.get("data").getAsJsonArray().size() > 0) {
                        allTransactionArrayList = apiCalling.getDataList(json, "data", TransactionModel.class);
                        if (allTransactionArrayList != null && allTransactionArrayList.size() > 0) {
                            setAdapter();
                        }

                    }
                } else {
                    showSimpleAlert(msg, "");
                }

            } else if (from.equals(getResources().getString(R.string.api_get_all_transaction_history_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonArray() != null && json.get("data").getAsJsonArray().size() > 0) {
                        allTransactionArrayList = apiCalling.getDataList(json, "data", TransactionModel.class);
                        if (allTransactionArrayList != null && allTransactionArrayList.size() > 0) {
                            setAdapter();
                        }
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);

                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }
}