package com.org.zapayapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.dd.ShadowLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.dialogs.AddBankDialogActivity;
import com.org.zapayapp.dialogs.ChangeBankDialogActivity;
import com.org.zapayapp.dialogs.EditProfileDialogActivity;
import com.org.zapayapp.dialogs.VerifyBankDialogActivity;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.MySession;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.FileCache;
import com.org.zapayapp.webservices.MyProgressDialog;
import com.org.zapayapp.webservices.RestAPI;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankInfoActivity extends BaseActivity implements View.OnClickListener, APICallback {

    private TextView changeTV, accountNumberTV, routingNumberTV;
    private TextView addTV;
    private ShadowLayout addShadowLayout, addShadowChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);
        init();
        initAction();
    }

    private void init() {

        addShadowChange = findViewById(R.id.addShadowChange);
        accountNumberTV = findViewById(R.id.accountNumberTV);
        routingNumberTV = findViewById(R.id.routingNumberTV);
        addTV = findViewById(R.id.addTV);
        addShadowLayout = findViewById(R.id.addShadowLayout);
//
//      Intent intent = new Intent(BankInfoActivity.this, VerifyBankDialogActivity.class);
//      startActivityForResult(intent, 3);

    }

    private void initAction() {
        addShadowChange.setOnClickListener(this);
        addTV.setOnClickListener(this);
        callAPIGetBankAccountDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        callAPIGetUserDetail();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addShadowChange) {
            Intent intent = new Intent(BankInfoActivity.this, ChangeBankDialogActivity.class);
            startActivityForResult(intent, 2);
        } else if (v.getId() == R.id.addTV) {
            if (addTV.getText().toString().trim().equals(getString(R.string.add))) {
                Intent intent = new Intent(BankInfoActivity.this, AddBankDialogActivity.class);
                startActivityForResult(intent, 1);
            } else {
                callAPIInitiateMicroDeposit();
//                Intent intent = new Intent(BankInfoActivity.this, VerifyBankDialogActivity.class);
//                startActivityForResult(intent, 3);
            }
        }
    }

    public void showLoader() {
        MyProgressDialog.getInstance().show(BankInfoActivity.this);
    }

    public void hideLoader() {
        MyProgressDialog.getInstance().dismiss();
    }

    private void callAPIInitiateMicroDeposit() {
        try {

            showLoader();

            String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();

//            AndroidNetworking.post("https://developer.webvilleedemo.xyz/zapay/api/initiatemicro")
//                    .addHeaders("Authorization", token)
//                    .addBodyParameter("id", SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_ID).toString())
//                    .build()
//                    .getAsString(new StringRequestListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            hideLoader();
//                            CommonMethods.showLogs("onResponseonResponse", "onResponse  " + response);
//                        }
//
//                        @Override
//                        public void onError(ANError anError) {
//                            hideLoader();
//                            CommonMethods.showLogs("onResponseonResponse", "ANError  " + anError.getErrorBody());
//                        }
//                    });

            HashMap<String, Object> values = apiCalling.getHashMapObject("id", SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_ID).toString());

            RestAPI restAPI = APICalling.getDwollaRetrofitRestApi();

            Call<ResponseBody> call = restAPI.checkInitiateMicroDeposit(token, values);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    int statusCode = response.code();

                    hideLoader();

                    try {
                        CommonMethods.showLogs("gdfddfdfdfdfdfdf", "gdfddfdfdfdfdfdf  " + statusCode);
                       // CommonMethods.showLogs("gdfddfdfdfdfdfdf", "gdfddfdfdfdfdfdf  " + response.body().string());

                        if ((statusCode == 201 && response.isSuccessful())) {
                            Intent intent = new Intent(BankInfoActivity.this, VerifyBankDialogActivity.class);
                            startActivityForResult(intent, 3);
                        } else if(statusCode == 400) {
                            Intent intent = new Intent(BankInfoActivity.this, VerifyBankDialogActivity.class);
                            startActivityForResult(intent, 3);
                        } else {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("status", statusCode + "");
                            jsonObject.addProperty("message", getResources().getString(R.string.something_wrong_check_network));
                            apiCallback(jsonObject, getResources().getString(R.string.api_initiatemicro));
                        }
                    } catch (Exception e) {
                        CommonMethods.showLogs("Failuredddd", "api calling  " + e.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    hideLoader();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("status", 500 + "");
                    jsonObject.addProperty("message", getResources().getString(R.string.something_wrong_check_network));
                    apiCallback(jsonObject, getResources().getString(R.string.api_initiatemicro));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
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

    private void callAPIGetUserDetail() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.getApiToken(token, getString(R.string.api_get_user_details));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_user_details), addTV);
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
                CommonMethods.showLogs("statusAAAAAAA", "status = " + status);
                msg = json.get("message").getAsString();
                CommonMethods.showLogs("statusAAAAAAA", "message = " + msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (from.equals(getResources().getString(R.string.api_get_bank_account_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {

                        // Log.e("jsonjsonjkkkkson", json.getAsJsonObject().toString());

                        //  bankId = json.get("id").getAsString();

                        //  Log.e("jsonjsonjkkkkson", "bankId = " + bankId);

                        MySession.saveBankData(json.get("data").getAsJsonObject());
                        setData();
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                }
            } else if (from.equals(getResources().getString(R.string.api_get_user_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        JsonObject jsonObject = json.get("data").getAsJsonObject();
                        CommonMethods.showLogs(BankInfoActivity.class.getSimpleName(), "jsonObject.toString() :- " + jsonObject.toString());
                        MySession.MakeSession(jsonObject);
                        setData();
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_initiatemicro))) {
                if (status == 201 || status == 400) {
                    CommonMethods.showLogs("api_initiatemicro", "STATUS == " + status);
                    CommonMethods.showLogs("api_initiatemicro", "MESSAGE == " + msg);
                    Intent intent = new Intent(BankInfoActivity.this, VerifyBankDialogActivity.class);
                    startActivityForResult(intent, 3);
                } else {
                    showSimpleAlert(msg, "");
                }
            } else {
               // showSimpleAlert(msg, "");
            }

        }

    }

    private void setData() {

        if (SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER).toString().length() > 0) {
            // accountNumberTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER, ""));

            Log.e("setDatasetDatssssa", "BANK_ACCOUNT_ID = " + SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_ID).toString());

            String acNumber = SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER, "");
            int acLength = SharedPref.getPrefsHelper().getPref(Const.Var.ACCOUNT_NUMBER).toString().length();

            if (acLength > 3) {
                String lastFourDigit = acNumber.substring(acNumber.length() - 4);
                String acNumberStr = "";
                for (int i = 0; i < acNumber.length() - 4; i++) {
                    acNumberStr = acNumberStr + "*";
                }
                accountNumberTV.setText(acNumberStr + lastFourDigit);
            }
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.ROUTING_NUMBER) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ROUTING_NUMBER).toString().length() > 0) {
            routingNumberTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ROUTING_NUMBER, ""));
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_ID) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_ID).toString().length() > 0) {
            addTV.setText(getString(R.string.verify_account));
        } else {
            addTV.setText(getString(R.string.add));
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS).toString().length() > 0 &&
                SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS).toString().equalsIgnoreCase("verified")) {
            addTV.setVisibility(View.INVISIBLE);
            addShadowLayout.setVisibility(View.INVISIBLE);
            // addShadowChange.setVisibility(View.VISIBLE);
        } else {
            addTV.setVisibility(View.VISIBLE);
            addShadowLayout.setVisibility(View.VISIBLE);
            // addShadowChange.setVisibility(View.INVISIBLE);
        }


        if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().length() > 0) {
            if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("1")) { // 1=bank account add pending
                addShadowChange.setVisibility(View.INVISIBLE);
            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("2")) { //bank verify pending
                addShadowChange.setVisibility(View.VISIBLE);
            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("3")) {//bank verify complete
                addShadowChange.setVisibility(View.INVISIBLE);
            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("0")) {//bank verify complete
                showSimpleAlert(getString(R.string.complete_profile_alert), getString(R.string.alert_from_profile));
            }
        }

    }

    @Override
    public void onSimpleCallback(String from) {
        super.onSimpleCallback(from);
        if (from.equals(getResources().getString(R.string.alert_from_profile))) {
            Intent intent = new Intent(BankInfoActivity.this, EditProfileDialogActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }
}
