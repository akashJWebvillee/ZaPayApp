package com.org.zapayapp.dialogs;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import java.util.HashMap;

import retrofit2.Call;

public class VerifyBankDialogActivity extends AppCompatActivity implements View.OnClickListener , APICallback,SimpleAlertFragment.AlertSimpleCallback{
    private TextView saveTV;
    private ImageView closeTV;
    private String header = "";

    private Spinner bankAccountTypeSpinner;
    private String bankAccountType="";

    public WValidationLib wValidationLib;
    /*Code for API calling*/
    protected ZapayApp zapayApp;
    protected Gson gson;
    protected APICalling apiCalling;
    protected RestAPI restAPI;

    private CustomTextInputLayout amount1InputLayout;
    private CustomTextInputLayout amount2InputLayout;

    private TextInputEditText amount1EditText;
    private TextInputEditText amount2EditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verify_bank_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getIntentValues();
        init();
        initAction();
        apicodeInit();
    }
    private void getIntentValues() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.getStringExtra("header") != null) {
                    header = intent.getStringExtra("header");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apicodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
    }

    private void init() {
        wValidationLib=new WValidationLib(VerifyBankDialogActivity.this);

        saveTV = findViewById(R.id.saveTV);
        //accountNumberTV = findViewById(R.id.accountNumberTV);
        //routNumberTV = findViewById(R.id.routNumberTV);
        closeTV = findViewById(R.id.closeTV);


        amount1InputLayout = findViewById(R.id.amount1InputLayout);
        amount2InputLayout = findViewById(R.id.amount2InputLayout);

        amount1EditText = findViewById(R.id.amount1EditText);
        amount2EditText = findViewById(R.id.amount2EditText);

    }

    private void initAction() {
        saveTV.setOnClickListener(this);
        closeTV.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(saveTV)) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            // finish();

            try {

             /*   if (wValidationLib.isEmpty(amount1InputLayout, amount1EditText, getString(R.string.important),true)) {
                    if (wValidationLib.isEmpty(amount2InputLayout, amount2EditText, getString(R.string.important), true)) {
                        callAPIVerifyBankAccount();
                    }
                }*/


             if (wValidationLib.isValidAmount1(amount1InputLayout, amount1EditText, getString(R.string.important),getString(R.string.amount1ValidationMsg), true)){
                 if (wValidationLib.isValidAmount2(amount2InputLayout, amount2EditText, getString(R.string.important),getString(R.string.amount2ValidationMsg), true)){
                     callAPIVerifyBankAccount();
                 }
             }




            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (v.equals(closeTV)){
            finish();
        }
    }

    private void callAPIVerifyBankAccount() {
        String token= SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "id", SharedPref.getPrefsHelper().getPref(Const.Var.BANKACCOUNT_ID).toString(),
                    "amount1", amount1EditText.getText().toString().trim(),
                    "amount2", amount2EditText.getText().toString().trim());

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token,getString(R.string.api_verify_bank_account_by_micro_deposits), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_verify_bank_account_by_micro_deposits), saveTV);
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

            if (from.equals(getResources().getString(R.string.api_verify_bank_account_by_micro_deposits))) {
                if (status==200){
                    bankAccountType="";
                    amount1EditText.setText("");
                    amount2EditText.setText("");
                    showSimpleAlert(msg, getResources().getString(R.string.api_verify_bank_account_by_micro_deposits));
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
        if (from.equals(getResources().getString(R.string.api_verify_bank_account_by_micro_deposits))){ ;
            Intent intent=new Intent();
            //intent.putExtra("MESSAGE",message);
            setResult(3,intent);
            finish();//finishing activity
        }
    }
}


