package com.org.zapayapp.dialogs;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import java.util.HashMap;

import retrofit2.Call;

public class ChangeDateRequestDialogActivity extends AppCompatActivity implements View.OnClickListener , APICallback, SimpleAlertFragment.AlertSimpleCallback{
    private TextView previousDateTV;
    private TextView requestedDateTV;
    private TextView acceptTV;
    private TextView declineTV;
    private ImageView dateCloseIV;
    public WValidationLib wValidationLib;

    /*Code for API calling*/
    protected ZapayApp zapayApp;
    protected Gson gson;
    protected APICalling apiCalling;
    protected RestAPI restAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_date_request_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        init();
        initAction();
        apiCodeInit();
    }

    private void apiCodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
        wValidationLib = new WValidationLib(ChangeDateRequestDialogActivity.this);
    }

    private void init() {
        previousDateTV = findViewById(R.id.previousDateTV);
        requestedDateTV = findViewById(R.id.requestedDateTV);
        acceptTV = findViewById(R.id.acceptTV);
        declineTV = findViewById(R.id.declineTV);
        dateCloseIV = findViewById(R.id.dateCloseIV);


    }

    private void initAction() {
        acceptTV.setOnClickListener(this);
        declineTV.setOnClickListener(this);
        dateCloseIV.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptTV:

                break;

            case R.id.declineTV:

                break;

            case R.id.dateCloseIV:
                finish();
                break;

        }

    }


    private void callAPIResetPassword() {
      /*  String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();

        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "old_password", oldPasswordUpEditText.getText().toString().trim(),
                    "new_password", newPasswordUpEditText.getText().toString().trim());

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_change_password), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_change_password), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
       /* if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (from.equals(getResources().getString(R.string.api_change_password))) {
                if (status == 200) {
                    oldPasswordUpEditText.setText("");
                    newPasswordUpEditText.setText("");
                    confirmPasswordUpEditText.setText("");

                    showSimpleAlert(msg, getString(R.string.api_change_password));
                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }*/
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
        if (from.equals(getString(R.string.api_change_password))) {
            // finish();

        }
    }

}