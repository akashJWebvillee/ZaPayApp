package com.org.zapayapp.dialogs;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

public class ChangePassDialogActivity extends AppCompatActivity implements View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback {

    private TextView saveTV;
    private ImageView closeTV;
    private EditText editTextName, editTextEmail, editTextPhoneNo, editTextAddress;
    private String header = "";


    private CustomTextInputLayout oldPasswordInputLayout;
    private CustomTextInputLayout newPasswordInputLayout;
    private CustomTextInputLayout confirmPasswordInputLayout;

    private TextInputEditText oldPasswordUpEditText;
    private TextInputEditText newPasswordUpEditText;
    private TextInputEditText confirmPasswordUpEditText;
    public WValidationLib wValidationLib;


    /*Code for API calling*/
    protected ZapayApp zapayApp;
    protected Gson gson;
    protected APICalling apiCalling;
    protected RestAPI restAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getIntentValues();
        init();
        initAction();
        apicodeInit();
    }

    private void apicodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
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

    private void init() {
        wValidationLib = new WValidationLib(ChangePassDialogActivity.this);

        saveTV = findViewById(R.id.saveTV);
       /* editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNo = findViewById(R.id.editTextPhoneNo);
        editTextAddress = findViewById(R.id.editTextAddress);*/
        closeTV = findViewById(R.id.closeTV);


        oldPasswordInputLayout = findViewById(R.id.oldPasswordInputLayout);
        newPasswordInputLayout = findViewById(R.id.newPasswordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);

        oldPasswordUpEditText = findViewById(R.id.oldPasswordUpEditText);
        newPasswordUpEditText = findViewById(R.id.newPasswordUpEditText);
        confirmPasswordUpEditText = findViewById(R.id.confirmPasswordUpEditText);

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
                if (wValidationLib.isPassword(oldPasswordInputLayout, oldPasswordUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password), true)) {
                    if (wValidationLib.isPassword(newPasswordInputLayout, newPasswordUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password), true)) {
                        if (wValidationLib.isPassword(confirmPasswordInputLayout, confirmPasswordUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password), true)) {
                             /*  if (newPasswordUpEditText.getText().toString().trim().equals(confirmPasswordUpEditText.getText().toString().trim())){
                                   callAPIResetPassword();
                               }else {
                                   showSimpleAlert(getString(R.string.new_password_and_confirm_password_should_be_same), getString(R.string.new_password_and_confirm_password_should_be_same));
                               }*/
                            if (wValidationLib.isConfirmPasswordValidation(newPasswordInputLayout, newPasswordUpEditText, confirmPasswordInputLayout, confirmPasswordUpEditText, getString(R.string.important), getString(R.string.important), getString(R.string.please_enter_valid_password), getString(R.string.please_enter_valid_password_same), true)) {
                                callAPIResetPassword();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (v.equals(closeTV)) {
            finish();
        }
    }


    private void callAPIResetPassword() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();

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
        if (from.equals(getString(R.string.new_password_and_confirm_password_should_be_same))) {

        } else if (from.equals(getString(R.string.api_change_password))) {
            finish();
        }
    }
}
