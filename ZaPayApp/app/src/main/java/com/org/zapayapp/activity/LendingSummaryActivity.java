package com.org.zapayapp.activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.TimeStamp;
import com.org.zapayapp.webservices.APICallback;

import java.util.HashMap;
import retrofit2.Call;

public class LendingSummaryActivity extends BaseActivity implements APICallback, SimpleAlertFragment.AlertSimpleCallback {
    private TransactionModel transactionModel;
    private TextView nameTV;
    private TextView amountTV;
    private TextView termTV;
    private TextView noOfPaymentTV;
    private TextView paymentDateTV;
    private TextView totalReceivedBackTV;
    private TextView viewAllTV;

    private TextView negotiateTV;
    private TextView acceptTV;
    private TextView declineTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lending_summary);
        inIt();
        inItAction();
    }

    private void inIt(){
        if (getIntent().getSerializableExtra("transactionModel")!=null){
            transactionModel= (TransactionModel) getIntent().getSerializableExtra("transactionModel");
        }

        nameTV=findViewById(R.id.nameTV);
        amountTV=findViewById(R.id.amountTV);
        termTV=findViewById(R.id.termTV);
        noOfPaymentTV=findViewById(R.id.noOfPaymentTV);
        paymentDateTV=findViewById(R.id.paymentDateTV);
        totalReceivedBackTV=findViewById(R.id.totalReceivedBackTV);
        viewAllTV=findViewById(R.id.viewAllTV);

        negotiateTV=findViewById(R.id.negotiateTV);
        acceptTV=findViewById(R.id.acceptTV);
        declineTV=findViewById(R.id.declineTV);


    }

    private void inItAction(){
        nameTV.setText(transactionModel.getLastName());
        amountTV.setText("$"+transactionModel.getAmount());

        noOfPaymentTV.setText(transactionModel.getNoOfPayment());
        paymentDateTV.setText(TimeStamp.timeFun(transactionModel.getCreatedAt()));
        totalReceivedBackTV.setText("$"+transactionModel.getTotalAmount());

        if (transactionModel.getTermsType().equalsIgnoreCase("1")) {
            termTV.setText(transactionModel.getTermsValue()+" %");
        } else if (transactionModel.getTermsType().equalsIgnoreCase("2")) {
            termTV.setText(transactionModel.getTermsValue()+" Fee");
        } else if (transactionModel.getTermsType().equalsIgnoreCase("3")) {
            termTV.setText(transactionModel.getTermsValue()+" Discount");
        } else if (transactionModel.getTermsType().equalsIgnoreCase("4")) {
            termTV.setText(transactionModel.getTermsValue()+" None");
        }


        negotiateTV.setOnClickListener(v -> {
       /*    Intent intent = new Intent(LendingSummaryActivity.this, LendBorrowActivity.class);
            intent.putExtra("isBorrow", false);
            startActivity(intent);*/
            callAPIUpdateTransactionRequestStatus("1");

        });

        acceptTV.setOnClickListener(v -> {
            callAPIUpdateTransactionRequestStatus("2");
        });

        declineTV.setOnClickListener(v -> {
            callAPIUpdateTransactionRequestStatus("3");
        });
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    private void callAPIUpdateTransactionRequestStatus(String status) {
        String token= SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionModel.getId(),
                "status", status);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token,getString(R.string.api_update_transaction_request_status), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_transaction_request_status), acceptTV);
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

            if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
                if (status==200){
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }else {
                    showSimpleAlert(msg, "");
                }
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
