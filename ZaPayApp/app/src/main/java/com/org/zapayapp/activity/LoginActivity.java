package com.org.zapayapp.activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.MySession;
import com.org.zapayapp.webservices.APICallback;
import java.util.HashMap;
import retrofit2.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback {
    private TextView loginTV;
    private TextView signUpTV;
    private TextView mTextAgree;
    private CustomTextInputLayout etPasswordLayout, etEmailLayout;
    private TextInputEditText etPassword, editTextUsername;

    private RelativeLayout loginRelativeLayout;
    private RelativeLayout signUpRelativeLayout;
    private TextView loginButtonTV, signUpButtonTV;
    private TextView forgotPasswordTV;
    private Intent intent;
    //private WValidationLib wValidationLib;

    //signup data......
    private CustomTextInputLayout userNameSignUpInputLayout;
    private CustomTextInputLayout emailSignUpInputLayout;
    private CustomTextInputLayout mobileSignUpInputLayout;
    private CustomTextInputLayout passwordSignUpInputLayout;
    private CustomTextInputLayout conformPasswordSignUpInputLayout;

    private TextInputEditText userNameSignUpEditText;
    private TextInputEditText emailSignUpEditText;
    private TextInputEditText mobileSignUpEditText;
    private TextInputEditText passwordSignUpEditText;
    private TextInputEditText conformPasswordSignUpEditText;
    private CheckBox mChkAgree;
    private String firstName = "";
    private String lastName = "aaaa";


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

        //SignUp screen......
        userNameSignUpInputLayout = findViewById(R.id.userNameSignUpInputLayout);
        emailSignUpInputLayout = findViewById(R.id.emailSignUpInputLayout);
        mobileSignUpInputLayout = findViewById(R.id.mobileSignUpInputLayout);
        passwordSignUpInputLayout = findViewById(R.id.passwordSignUpInputLayout);
        conformPasswordSignUpInputLayout = findViewById(R.id.conformPasswordSignUpInputLayout);

        userNameSignUpEditText = findViewById(R.id.userNameSignUpEditText);
        emailSignUpEditText = findViewById(R.id.emailSignUpEditText);
        mobileSignUpEditText = findViewById(R.id.mobileSignUpEditText);
        passwordSignUpEditText = findViewById(R.id.passwordSignUpEditText);
        conformPasswordSignUpEditText = findViewById(R.id.conformPasswordSignUpEditText);

        mChkAgree = findViewById(R.id.mChkAgree);
        termCondition();
        // wValidationLib = new WValidationLib(this);

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

        wValidationLib.removeError(userNameSignUpInputLayout, userNameSignUpEditText);
        wValidationLib.removeError(emailSignUpInputLayout, emailSignUpEditText);
        wValidationLib.removeError(mobileSignUpInputLayout, mobileSignUpEditText);
        wValidationLib.removeError(passwordSignUpInputLayout, passwordSignUpEditText);
        wValidationLib.removeError(conformPasswordSignUpInputLayout, conformPasswordSignUpEditText);
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
                    if (wValidationLib.isEmailAddress(etEmailLayout, editTextUsername, getString(R.string.important), getString(R.string.please_enter_valid_email), true)) {
                        if (wValidationLib.isPassword(etPasswordLayout, etPassword, getString(R.string.important), getString(R.string.please_enter_valid_password), true)) {
                           /* intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();*/
                            callAPILogin();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.signUpButtonTV:
              /*  intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();*/
                try {
                    if (wValidationLib.isFullName(userNameSignUpInputLayout, userNameSignUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_full_name), true)) {
                        if (wValidationLib.isEmailAddress(emailSignUpInputLayout, emailSignUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_email), true)) {
                            if (wValidationLib.isValidNumeric(mobileSignUpInputLayout, mobileSignUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_mobile), true)) {
                                if (wValidationLib.isPassword(passwordSignUpInputLayout, passwordSignUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password),true)) {
                                    if (wValidationLib.isPassword(conformPasswordSignUpInputLayout, conformPasswordSignUpEditText, getString(R.string.important), getString(R.string.please_enter_valid_password),true)) {
                                        if (wValidationLib.isConfirmPasswordValidation(passwordSignUpInputLayout, passwordSignUpEditText, conformPasswordSignUpInputLayout, conformPasswordSignUpEditText, getString(R.string.important), getString(R.string.important), getString(R.string.please_enter_valid_password), getString(R.string.please_enter_valid_password_same), true)) {
                                            if (mChkAgree.isChecked()) {
                                                callAPISignUp();
                                            } else {
                                                showSimpleAlert(getString(R.string.term_condition), "");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void callAPISignUp() {
        try {
            String firstNameLastName = userNameSignUpEditText.getText().toString().trim();
            String[] firstNameLastName1 = firstNameLastName.split(" ");
            if (firstNameLastName1.length > 1) {
                firstName = firstNameLastName1[0];
                lastName = firstNameLastName1[1];
            }
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "first_name", firstName,
                    "last_name", lastName,
                    "email", emailSignUpEditText.getText().toString().trim(),
                    "mobile", mobileSignUpEditText.getText().toString().trim(),
                    "password", passwordSignUpEditText.getText().toString().trim(),
                    "device_type", Const.KEY.DEVICE_TYPE,
                    "device_token", "firebasetokenkey",
                    "device_id", Const.getDeviceId(LoginActivity.this));

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postApi(getString(R.string.api_signup), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_signup), loginButtonTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * API login
     */
    private void callAPILogin() {
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "email", editTextUsername.getText().toString().trim(),
                    "password", etPassword.getText().toString().trim(),
                    "device_type", Const.KEY.DEVICE_TYPE,
                    "device_token", "firebasetokenkey",
                    "device_id", Const.getDeviceId(LoginActivity.this));

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

            if (from.equals(getResources().getString(R.string.api_signin))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        JsonObject jsonObject = json.get("data").getAsJsonObject();
                        MySession.MakeSession(jsonObject);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_signup))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        JsonObject jsonObject = json.get("data").getAsJsonObject();
                       //MySession.MakeSession(jsonObject);
                        userNameSignUpEditText.setText("");
                        emailSignUpEditText.setText("");
                        mobileSignUpEditText.setText("");
                        passwordSignUpEditText.setText("");
                        conformPasswordSignUpEditText.setText("");
                        firstName = "";
                        lastName = "";
                        showSimpleAlert(msg, getString(R.string.api_signup));
                    }
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
        if (from.equals(getString(R.string.api_signup))){
            setSelectedView(1);
        }
    }
}
