package com.org.zapayapp.dialogs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.activity.AcceptActivity;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import java.util.HashMap;

import retrofit2.Call;

public class ChangeDateRequestDialogActivity extends AppCompatActivity implements View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback {
    private TextView previousDateTV;
    private TextView requestedDateTV;
    private TextView acceptTV;
    private TextView declineTV;
    private ImageView dateCloseIV;
    public WValidationLib wValidationLib;
    private DateModel dateModel;

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
        apiCodeInit();
        getIntentFunc();
        initAction();
    }

    private void apiCodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
        wValidationLib = new WValidationLib(ChangeDateRequestDialogActivity.this);
    }

    private void getIntentFunc() {
        if (getIntent().getStringExtra("DateDetail") != null && getIntent().getStringExtra("DateDetail").length() > 0) {
            String dateDetail = getIntent().getStringExtra("DateDetail");
            dateModel = gson.fromJson(dateDetail, DateModel.class);
        }

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

        if (dateModel != null) {
            previousDateTV.setText(dateModel.getPayDate());
            requestedDateTV.setText(dateModel.getNew_pay_date());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptTV:
                Intent intent = new Intent(ChangeDateRequestDialogActivity.this, AcceptActivity.class);
                intent.putExtra("moveFrom", "ChangeDateRequestDialogActivity");
                intent.putExtra("status", gson.toJson(dateModel));
                intent.putExtra("transactionId", dateModel.getTransactionRequestId());
                startActivity(intent);
                finish();
                break;

            case R.id.declineTV:

                break;

            case R.id.dateCloseIV:
                Intent intent1=new Intent();
                intent1.putExtra("data","closeByButton");
                setResult(300,intent1);
                finish();//finishing activity
                break;

        }

    }


    private void callAPIPayDateRequestStatusUpdate() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", dateModel.getTransactionRequestId(),
                "pay_date_id", dateModel.getId(),
                "new_pay_date_status", "2"); //date accept
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_pay_date_request_status_update), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_pay_date_request_status_update), acceptTV);
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
            if (from.equals(getResources().getString(R.string.api_pay_date_request_status_update))) {
                if (status == 200) {
                    showSimpleAlert(msg, getString(R.string.api_pay_date_request_status_update));
                } else {
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
        if (from.equals(getString(R.string.api_change_password))) {
            // finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}