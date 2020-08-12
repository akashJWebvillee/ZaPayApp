package com.org.zapayapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.dialogs.AdddBankDialogActivity;
import com.org.zapayapp.dialogs.ChangeBankDialogActivity;
import com.org.zapayapp.dialogs.VerifyBankDialogActivity;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.MySession;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;

import java.util.HashMap;

import retrofit2.Call;

public class BankInfoActivity extends BaseActivity implements View.OnClickListener, APICallback {
    private TextView changeTV, accountNumberTV, routingNumberTV;
    private TextView addTV;
    //get_bank_account_details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);
        init();
        initAction();
    }

    private void init() {
        changeTV = findViewById(R.id.changeTV);
        accountNumberTV = findViewById(R.id.accountNumberTV);
        routingNumberTV = findViewById(R.id.routingNumberTV);
        addTV = findViewById(R.id.addTV);


        setData();
    }

    private void initAction() {
        changeTV.setOnClickListener(this);
        addTV.setOnClickListener(this);

        callAPIGetBankAccountDetail();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.changeTV) {
            Intent intent = new Intent(BankInfoActivity.this, ChangeBankDialogActivity.class);
            //startActivity(intent);
            startActivityForResult(intent, 2);

        } else if (v.getId() == R.id.addTV) {
            if (addTV.getText().toString().trim().equals(getString(R.string.add))) {
                Intent intent = new Intent(BankInfoActivity.this, AdddBankDialogActivity.class);
                // startActivity(intent);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(BankInfoActivity.this, VerifyBankDialogActivity.class);
                // startActivity(intent);
                startActivityForResult(intent, 3);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            // String message=data.getStringExtra("MESSAGE");
            callAPIGetBankAccountDetail();
        } else if (requestCode == 2) {
            callAPIGetBankAccountDetail();
        } else if (requestCode == 3) {
            callAPIGetBankAccountDetail();

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


    private void callAPIGetBankAccountDetail() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.getApiToken(token, getString(R.string.api_get_bank_account_details));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_bank_account_details), addTV);
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

            if (from.equals(getResources().getString(R.string.api_get_bank_account_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        MySession.saveBankData(json.get("data").getAsJsonObject());
                        setData();

                    }
                } else {
                    //showSimpleAlert(msg, "");
                }
            }

        }
    }


    private void setData() {
        if (SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER).toString().length() > 0) {
            accountNumberTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER, ""));
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.ROUTING_NUMBER) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ROUTING_NUMBER).toString().length() > 0) {
            routingNumberTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ROUTING_NUMBER, ""));
        }



        if (SharedPref.getPrefsHelper().getPref(Const.Var.BANKACCOUNT_ID) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BANKACCOUNT_ID).toString().length() > 0) {
            addTV.setText(getString(R.string.verify_account));
        } else {
            addTV.setText(getString(R.string.add));
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS).toString().length() > 0 &&
                SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS).toString().equalsIgnoreCase("verified")) {
            addTV.setVisibility(View.GONE);
        } else {
            addTV.setVisibility(View.VISIBLE);
        }


        if (SharedPref.getPrefsHelper().getPref(Const.Var.BANKACCOUNT_ID) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BANKACCOUNT_ID).toString().length() > 0) {
            changeTV.setVisibility(View.VISIBLE);
        } else {
            changeTV.setVisibility(View.GONE);
        }

    }
}
