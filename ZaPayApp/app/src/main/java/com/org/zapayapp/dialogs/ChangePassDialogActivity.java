package com.org.zapayapp.dialogs;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.activity.BaseActivity;
import com.org.zapayapp.activity.SplashActivity;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.MySession;
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
    private CustomTextInputLayout oldPasswordInputLayout, newPasswordInputLayout, confirmPasswordInputLayout;
    private TextInputEditText oldPasswordUpEditText, newPasswordUpEditText, confirmPasswordUpEditText;
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
        init();
        initAction();
        apiCodeInit();
    }

    private void apiCodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
        wValidationLib = new WValidationLib(ChangePassDialogActivity.this);
    }

    private void init() {
        saveTV = findViewById(R.id.saveTV);
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
            try {
                if (wValidationLib.isPassword(oldPasswordInputLayout, oldPasswordUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password), true)) {
                    if (wValidationLib.isPassword(newPasswordInputLayout, newPasswordUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password), true)) {
                        if (wValidationLib.isPassword(confirmPasswordInputLayout, confirmPasswordUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password), true)) {
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
            }else if (from.equals(getResources().getString(R.string.api_logout))) {
                if (status == 200) {
                    clearLogout();
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
            callAPILogout();
        }
    }



    private void callAPILogout() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postApiToken(token, getString(R.string.api_logout));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_logout), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void clearLogout() {
        //disconnectSocket();
        MySession.removeSession();
        Intent intent = new Intent(ChangePassDialogActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
