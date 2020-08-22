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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class AdddBankDialogActivity extends AppCompatActivity implements View.OnClickListener , APICallback,SimpleAlertFragment.AlertSimpleCallback{
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

    private CustomTextInputLayout accountNumberInputLayout;
    private CustomTextInputLayout routNumberInputLayout;
    private CustomTextInputLayout nameInputLayout;

    private TextInputEditText accountNumberEditText;
    private TextInputEditText routNumberEditText;
    private TextInputEditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addd_bank_dialog);

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
        wValidationLib=new WValidationLib(AdddBankDialogActivity.this);

        saveTV = findViewById(R.id.saveTV);
        //accountNumberTV = findViewById(R.id.accountNumberTV);
        //routNumberTV = findViewById(R.id.routNumberTV);
        closeTV = findViewById(R.id.closeTV);


        accountNumberInputLayout = findViewById(R.id.accountNumberInputLayout);
        routNumberInputLayout = findViewById(R.id.routNumberInputLayout);
        nameInputLayout = findViewById(R.id.nameInputLayout);

        accountNumberEditText = findViewById(R.id.accountNumberEditText);
        routNumberEditText = findViewById(R.id.routNumberEditText);
        nameEditText = findViewById(R.id.nameEditText);
    }

    private void initAction() {
        saveTV.setOnClickListener(this);
        closeTV.setOnClickListener(this);

        String[] aacountType = getResources().getStringArray(R.array.accountTypeArray);

        bankAccountTypeSpinner = findViewById(R.id.bankAccountTypeSpinner);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,aacountType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankAccountTypeSpinner.setAdapter(aa);
        bankAccountTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //bankAccountType= (String) parent.getItemAtPosition(position);
                bankAccountType = aacountType[position];
                Log.e("bankAccountType","bankAccountType======="+bankAccountType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(saveTV)) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
           // finish();

           /* try {
                if (wValidationLib.isEmpty(accountNumberInputLayout, accountNumberEditText, getString(R.string.important),true)) {
                if (accountNumberEditText.getText().toString().trim().length()>=4&&accountNumberEditText.getText().toString().trim().length()<=17){
                    if (wValidationLib.isEmpty(routNumberInputLayout, routNumberEditText, getString(R.string.important), true)) {
                        if (routNumberEditText.getText().toString().trim().length()>=9){
                            if (wValidationLib.isEmpty(nameInputLayout, nameEditText, getString(R.string.important), true)) {
                                callAPIAddBankAccount();
                            }

                        }else {
                            showSimpleAlert(getString(R.string.enter_valid_routing_number),"");
                        }
                    }
                }else {
                    showSimpleAlert(getString(R.string.enter_valid_account_number),"");
                }
            }*/


            try {
                if (wValidationLib.isValidAccountNumber(accountNumberInputLayout, accountNumberEditText, getString(R.string.important),getString(R.string.enter_valid_account_number),true)) {
                        if (wValidationLib.isValidRoutingNumber(routNumberInputLayout, routNumberEditText, getString(R.string.important), getString(R.string.enter_valid_routing_number),true)) {
                            if (wValidationLib.isEmpty(nameInputLayout, nameEditText, getString(R.string.important), true)) {
                                    callAPIAddBankAccount();
                                }
                        }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }





        }else if (v.equals(closeTV)){
            finish();
        }
    }

    private void callAPIAddBankAccount() {
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

            if (from.equals(getResources().getString(R.string.api_add_bank_account))) {
                if (status==200){
                    bankAccountType="";
                    accountNumberEditText.setText("");
                    routNumberEditText.setText("");
                    nameEditText.setText("");

                    showSimpleAlert(msg, getResources().getString(R.string.api_add_bank_account));
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
        if (from.equals(getResources().getString(R.string.api_add_bank_account))){ ;
            Intent intent=new Intent();
            //intent.putExtra("MESSAGE",message);
            setResult(1,intent);
            finish();//finishing activity
        }
    }
}

