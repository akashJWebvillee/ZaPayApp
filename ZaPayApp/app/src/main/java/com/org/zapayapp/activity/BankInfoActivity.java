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
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;

import java.util.HashMap;

import retrofit2.Call;

public class BankInfoActivity extends BaseActivity implements View.OnClickListener, APICallback {
    private TextView changeTV,accountNumberTV,routingNumberTV;
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
    }

    private void initAction() {
        changeTV.setOnClickListener(this);
        addTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.changeTV){
            Intent intent = new Intent(BankInfoActivity.this, ChangeBankDialogActivity.class);
            startActivity(intent);
        }else if (v.getId()==R.id.addTV){

            Intent intent = new Intent(BankInfoActivity.this, AdddBankDialogActivity.class);
            startActivity(intent);
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



  /*  private void callAPIAddBankAccount() {
        String token= SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "account_number", accountNumberEditText.getText().toString().trim(),
                    "routing_number", routNumberEditText.getText().toString().trim(),
                    "bank_account_type", bankAccountType,
                    "name", nameEditText.getText().toString().trim());

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token,getString(R.string.api_add_bank_account), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_add_bank_account), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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

            if (from.equals(getResources().getString(R.string.api_add_bank_account))) {
                if (status==200){



                    showSimpleAlert(msg, "");
                }else {
                    showSimpleAlert(msg, "");
                }
            }

        }
    }
}
