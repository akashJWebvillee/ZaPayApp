package com.org.zapayapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.chat.ChatActivity;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import retrofit2.Call;

public class LendingSummaryActivity extends BaseActivity implements APICallback, View.OnClickListener {
    private TextView nameTV, amountTV, termTV, noOfPaymentTV, paymentDateTV, totalReceivedBackTV, viewAllTV;
    private TextView negotiateTV, acceptTV, declineTV, chatTV;
    private String transactionId, moveFrom;
    private TransactionModel transactionModel;
    private String negotiationAcceptDeclineStatus = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lending_summary);
        inIt();
        inItAction();
        getIntentValues();
    }

    private void inIt() {
        nameTV = findViewById(R.id.nameTV);
        amountTV = findViewById(R.id.amountTV);
        termTV = findViewById(R.id.termTV);
        noOfPaymentTV = findViewById(R.id.noOfPaymentTV);
        paymentDateTV = findViewById(R.id.paymentDateTV);
        totalReceivedBackTV = findViewById(R.id.totalReceivedBackTV);
        viewAllTV = findViewById(R.id.viewAllTV);

        negotiateTV = findViewById(R.id.negotiateTV);
        acceptTV = findViewById(R.id.acceptTV);
        declineTV = findViewById(R.id.declineTV);
        chatTV = findViewById(R.id.chatTV);
    }

    private void inItAction() {
        negotiateTV.setOnClickListener(this);
        acceptTV.setOnClickListener(this);
        declineTV.setOnClickListener(this);
        viewAllTV.setOnClickListener(this);
        chatTV.setOnClickListener(this);
    }

    private void getIntentValues() {
        intent = getIntent();
        if (intent != null && intent.getStringExtra("transactionId") != null && intent.getStringExtra("moveFrom") != null) {
            transactionId = intent.getStringExtra("transactionId");
            moveFrom = intent.getStringExtra("moveFrom");
            if (intent.getStringExtra("moveFrom") != null) {
                if (getString(R.string.transaction).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    negotiateTV.setVisibility(View.VISIBLE);
                    acceptTV.setVisibility(View.VISIBLE);
                    declineTV.setVisibility(View.VISIBLE);
                    callAPIGetTransactionRequestDetail(transactionId);
                } else if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    negotiateTV.setVisibility(View.GONE);
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                    callAPIGetHistoryRequestDetail(transactionId);


                } else if (getString(R.string.negotiation).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    negotiateTV.setVisibility(View.VISIBLE);
                    acceptTV.setVisibility(View.VISIBLE);
                    declineTV.setVisibility(View.VISIBLE);
                    callAPIGetTransactionRequestDetail(transactionId);
                } else if (getString(R.string.accepted).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    negotiateTV.setVisibility(View.GONE);
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                    callAPIGetHistoryRequestDetail(transactionId);
                } else if (getString(R.string.completed).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    negotiateTV.setVisibility(View.GONE);
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                    callAPIGetHistoryRequestDetail(transactionId);
                } else if (getString(R.string.decline).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    negotiateTV.setVisibility(View.GONE);
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                    callAPIGetHistoryRequestDetail(transactionId);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negotiateTV:
                //negotiationAcceptDeclineStatus = "1";
                //callAPIUpdateTransactionRequestStatus("1");
                intent = new Intent(LendingSummaryActivity.this, LendBorrowActivity.class);
                intent.putExtra("isBorrow", false);
                intent.putExtra("transactionModel", transactionModel);
                startActivity(intent);
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
                intent = new Intent(LendingSummaryActivity.this, ViewAllSummaryActivity.class);
                intent.putExtra("transactionId", transactionId);
                intent.putExtra("moveFrom", moveFrom);
                intent.putExtra("requestBy", transactionModel.getRequestBy());
                startActivity(intent);
                break;
            case R.id.chatTV:
                intent = new Intent(LendingSummaryActivity.this, ChatActivity.class);
                intent.putExtra("transactionModel", transactionModel);
                startActivity(intent);
                break;
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
                        intent = new Intent(LendingSummaryActivity.this, LendBorrowActivity.class);
                        intent.putExtra("isBorrow", false);
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
            String name = jsonObject.get("first_name").getAsString() + " " + jsonObject.get("last_name").getAsString();
            nameTV.setText(name);
        }
        if (jsonObject.get("amount").getAsString() != null && jsonObject.get("amount").getAsString().length() > 0) {
            String amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + jsonObject.get("amount").getAsString();
            amountTV.setText(amount);
        }
        if (jsonObject.get("no_of_payment").getAsString() != null && jsonObject.get("no_of_payment").getAsString().length() > 0) {
            String no_of_payment = jsonObject.get("no_of_payment").getAsString();
            noOfPaymentTV.setText(no_of_payment);
        }
        if (jsonObject.get("created_at").getAsString() != null && jsonObject.get("created_at").getAsString().length() > 0) {
            String created_at = jsonObject.get("created_at").getAsString();
            // paymentDateTV.setText(TimeStamp.timeFun(created_at));
        }

        if (jsonObject.get("pay_date").getAsString() != null && jsonObject.get("pay_date").getAsString().length() > 0) {
            String pay_date = jsonObject.get("pay_date").getAsString();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                //paymentDateTV.setText(DateFormat.getDateFromEpoch(date));
                paymentDateTV.setText(date);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (jsonObject.get("total_amount").getAsString() != null && jsonObject.get("total_amount").getAsString().length() > 0) {
            String total_amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + jsonObject.get("total_amount").getAsString();
            totalReceivedBackTV.setText(total_amount);
        }
        if (jsonObject.get("terms_type").getAsString() != null && jsonObject.get("terms_type").getAsString().length() > 0 && jsonObject.get("terms_value").getAsString() != null && jsonObject.get("terms_value").getAsString().length() > 0) {
            String terms_type = jsonObject.get("terms_type").getAsString();
            String terms_value = jsonObject.get("terms_value").getAsString();
            if (terms_type.equalsIgnoreCase("1")) {
                terms_value = terms_value + " " + getString(R.string.percent);
                termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("2")) {
                terms_value = terms_value + " " + getString(R.string.fee);
                termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("3")) {
                terms_value = terms_value + " " + getString(R.string.discount);
                termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("4")) {
                // terms_value = terms_value + " " + getString(R.string.none);
                termTV.setText(getString(R.string.none));
            }
        }
    }
}
