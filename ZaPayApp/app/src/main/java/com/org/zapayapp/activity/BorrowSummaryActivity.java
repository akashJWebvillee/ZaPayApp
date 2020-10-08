package com.org.zapayapp.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.chat.ChatActivity;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.TimeStamp;
import com.org.zapayapp.webservices.APICallback;

import java.util.HashMap;

import retrofit2.Call;

public class BorrowSummaryActivity extends BaseActivity implements APICallback, View.OnClickListener {

    private TextView nameTV, amountTV, termTV, noOfPaymentTV, paymentDateTV, totalPayBackTV, viewAllTV, navigateTV, acceptTV, declineTV, chatTV;
    private String transactionId;
    private TransactionModel transactionModel;
    private String negotiationAcceptDeclineStatus = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_summary);
        init();
        initAction();
        getIntentValues();
    }

    private void init() {
        nameTV = findViewById(R.id.nameTV);
        amountTV = findViewById(R.id.amountTV);
        termTV = findViewById(R.id.termTV);
        noOfPaymentTV = findViewById(R.id.noOfPaymentTV);
        paymentDateTV = findViewById(R.id.paymentDateTV);
        totalPayBackTV = findViewById(R.id.totalPayBackTV);
        viewAllTV = findViewById(R.id.viewAllTV);

        navigateTV = findViewById(R.id.navigateTV);
        acceptTV = findViewById(R.id.acceptTV);
        declineTV = findViewById(R.id.declineTV);
        chatTV = findViewById(R.id.chatTV);
    }

    private void initAction() {
        navigateTV.setOnClickListener(this);
        acceptTV.setOnClickListener(this);
        declineTV.setOnClickListener(this);
        viewAllTV.setOnClickListener(this);
        chatTV.setOnClickListener(this);
    }

    private void getIntentValues() {
        intent = getIntent();
        if (intent != null && intent.getStringExtra("transactionId") != null) {
            transactionId = intent.getStringExtra("transactionId");
            if (intent.getStringExtra("moveFrom") != null) {
                if (getString(R.string.transaction).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    callAPIGetTransactionRequestDetail(transactionId);
                } else if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    callAPIGetHistoryRequestDetail(transactionId);
                }
            }
        }
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negotiateTV:
                negotiationAcceptDeclineStatus = "1";
                callAPIUpdateTransactionRequestStatus("1");
                break;
            case R.id.acceptTV:
                negotiationAcceptDeclineStatus = "2";
                callAPIUpdateTransactionRequestStatus("2");
                break;
            case R.id.declineTV:
                negotiationAcceptDeclineStatus = "3";
                callAPIUpdateTransactionRequestStatus("3");
                break;
            case R.id.viewAllTV:
                intent = new Intent(BorrowSummaryActivity.this, ViewAllSummaryActivity.class);
                intent.putExtra("transactionId", transactionId);
                startActivity(intent);
                break;
            case R.id.chatTV:
                intent = new Intent(BorrowSummaryActivity.this, ChatActivity.class);
                intent.putExtra("transactionModel", transactionModel);
                startActivity(intent);
                break;
        }
    }

    private void callAPIGetTransactionRequestDetail(String transaction_request_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_request_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_request_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_request_details), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetHistoryRequestDetail(String transaction_request_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_request_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_history_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_history_details), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIUpdateTransactionRequestStatus(String status) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId,
                "status", status);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_update_transaction_request_status), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_transaction_request_status), acceptTV);
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

            if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
                if (status == 200) {
                    if (!negotiationAcceptDeclineStatus.equalsIgnoreCase("1")) {
                        showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                    } else {
                        intent = new Intent(BorrowSummaryActivity.this, LendBorrowActivity.class);
                        intent.putExtra("isBorrow", true);
                        intent.putExtra("transactionModel", transactionModel);
                        startActivity(intent);
                    }
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_transaction_request_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        transactionModel = (TransactionModel) apiCalling.getDataObject(json.get("data").getAsJsonObject(), TransactionModel.class);
                        setData(json.get("data").getAsJsonObject());
                    }
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }
            } else if (from.equals(getResources().getString(R.string.api_get_transaction_history_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        transactionModel = (TransactionModel) apiCalling.getDataObject(json.get("data").getAsJsonObject(), TransactionModel.class);
                        setData(json.get("data").getAsJsonObject());
                    }
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }
            }
        }
    }

    private void setData(JsonObject jsonObject) {
        if (jsonObject.get("first_name").getAsString() != null && jsonObject.get("first_name").getAsString().length() > 0 && jsonObject.get("first_name").getAsString() != null && jsonObject.get("first_name").getAsString().length() > 0) {
            String first_name = jsonObject.get("first_name").getAsString();
            String last_name = jsonObject.get("last_name").getAsString();
            nameTV.setText(first_name + " " + last_name);
        }
        if (jsonObject.get("amount").getAsString() != null && jsonObject.get("amount").getAsString().length() > 0) {
            String amount = jsonObject.get("amount").getAsString();
            amountTV.setText("$" + amount);
        }
        if (jsonObject.get("no_of_payment").getAsString() != null && jsonObject.get("no_of_payment").getAsString().length() > 0) {
            String no_of_payment = jsonObject.get("no_of_payment").getAsString();
            noOfPaymentTV.setText(no_of_payment);
        }
        if (jsonObject.get("created_at").getAsString() != null && jsonObject.get("created_at").getAsString().length() > 0) {
            String created_at = jsonObject.get("created_at").getAsString();
            paymentDateTV.setText(TimeStamp.timeFun(created_at));
        }
        if (jsonObject.get("total_amount").getAsString() != null && jsonObject.get("total_amount").getAsString().length() > 0) {
            String total_amount = jsonObject.get("total_amount").getAsString();
            totalPayBackTV.setText("$" + total_amount);
        }
        if (jsonObject.get("terms_type").getAsString() != null && jsonObject.get("terms_type").getAsString().length() > 0 && jsonObject.get("terms_value").getAsString() != null && jsonObject.get("terms_value").getAsString().length() > 0) {
            String terms_type = jsonObject.get("terms_type").getAsString();
            String terms_value = jsonObject.get("terms_value").getAsString();
            if (terms_type.equalsIgnoreCase("1")) {
                termTV.setText(terms_value + " " + getString(R.string.percent));
            } else if (terms_type.equalsIgnoreCase("2")) {
                termTV.setText(terms_value + " " + getString(R.string.fee));
            } else if (terms_type.equalsIgnoreCase("3")) {
                termTV.setText(terms_value + " " + getString(R.string.discount));
            } else if (terms_type.equalsIgnoreCase("4")) {
                termTV.setText(terms_value + " " + getString(R.string.none));
            }
        }
    }

    public void showSimpleAlert(String message, String from) {
        try {
            FragmentManager fm = getSupportFragmentManager();
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
        if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
            finish();
        }
    }
}

