package com.org.zapayapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;

import java.util.HashMap;

import retrofit2.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener, APICallback {

    private TextView loginTV;
    private TextView signUpTV;
    private TextView mTextAgree;
    private CustomTextInputLayout etPasswordLayout, etEmailLayout;
    private RelativeLayout loginRelativeLayout;
    private RelativeLayout signUpRelativeLayout;
    private TextInputEditText etPassword, editTextUsername;
    private TextView loginButtonTV, signUpButtonTV;
    private TextView forgotPasswordTV;
    private Intent intent;
    private WValidationLib wValidationLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inIt();
        inItAction();
    }

    private void inIt() {
        loginTV = findViewById(R.id.loginTV);
        signUpTV = findViewById(R.id.signUpTV);
        mTextAgree = findViewById(R.id.mTextAgree);
        etEmailLayout = findViewById(R.id.etEmailLayout);
        etPasswordLayout = findViewById(R.id.etPasswordLayout);
        loginRelativeLayout = findViewById(R.id.loginRelativeLayout);
        signUpRelativeLayout = findViewById(R.id.signUpRelativeLayout);
        editTextUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButtonTV = findViewById(R.id.loginButtonTV);
        signUpButtonTV = findViewById(R.id.signUpButtonTV);
        forgotPasswordTV = findViewById(R.id.forgotPasswordTV);
        termCondition();
        wValidationLib = new WValidationLib(this);
    }

    private void inItAction() {
        loginTV.setOnClickListener(this);
        signUpTV.setOnClickListener(this);
        forgotPasswordTV.setOnClickListener(this);
        loginButtonTV.setOnClickListener(this);
        signUpButtonTV.setOnClickListener(this);

        setSelectedView(1);
        wValidationLib.removeError(etEmailLayout, editTextUsername);
        wValidationLib.removeError(etPasswordLayout, etPassword);
    }

    private void setSelectedView(int position) {
        if (position == 1) {
            loginTV.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            signUpTV.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite70Alpha));
            loginRelativeLayout.setVisibility(View.VISIBLE);
            signUpRelativeLayout.setVisibility(View.GONE);

        } else if (position == 2) {
            loginTV.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite70Alpha));
            signUpTV.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            loginRelativeLayout.setVisibility(View.GONE);
            signUpRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void termCondition() {
        String textTerms = this.getString(R.string.term_and_condition_message);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(textTerms);
        ClickableSpan redClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                try {
                    String url = "https://www.google.com/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(CommonMethods.getColorWrapper(LoginActivity.this, R.color.textColor));
            }
        };

        ssBuilder.setSpan(
                new android.text.style.StyleSpan(Typeface.BOLD), // Span to add
                40, // Start of the span (inclusive)
                textTerms.length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        );

        ssBuilder.setSpan(
                redClickableSpan, // Span to add
                40, // Start of the span (inclusive)
                textTerms.length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        );
        mTextAgree.setText(ssBuilder);
        mTextAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginTV:
                setSelectedView(1);
                break;
            case R.id.signUpTV:
                setSelectedView(2);
                break;
            case R.id.forgotPasswordTV:
                intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.loginButtonTV:
                try {
                    if (wValidationLib.isEmailAddress(etEmailLayout, editTextUsername, getString(R.string.important), getString(R.string.important), true)) {
                        if (wValidationLib.isEmpty(etPasswordLayout, etPassword, getString(R.string.important), true)) {
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.signUpButtonTV:
                intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
        }
    }

    /**
     * API login
     */
    private void callAPILogin() {
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "email", editTextUsername.getText().toString(),
                    "password", etPassword.getText().toString());
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postApi(getString(R.string.api_signin), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_signin), loginButtonTV);
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
                status = json.get("code").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (from.equals(getResources().getString(R.string.api_signin))) {
                if (status == 200) {
                    if (json.get("data").isJsonObject()) {

                    }
                } else {
                   // showSimpleMsgAlert(msg, "", "");
                }
            }
        }
    }
}
