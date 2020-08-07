package com.org.zapayapp.activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.webservices.APICallback;
import java.util.HashMap;
import retrofit2.Call;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener, APICallback {
    private TextView textViewSpannable;
    private TextView buttonSend;

    private CustomTextInputLayout passwordInputLayout;
    private TextInputEditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        initAction();
    }

    private void init(){
        textViewSpannable = findViewById(R.id.textViewSpannable);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        buttonSend = findViewById(R.id.buttonSend);

    }

    private void initAction(){
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(getResources().getString(R.string.enter_email_address_with_account));
        ssBuilder.setSpan(
                new android.text.style.StyleSpan(Typeface.BOLD), // Span to add
                11, // Start of the span (inclusive)
                24, // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        );
        textViewSpannable.setText(ssBuilder);
        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSend) {

            try {
                if (wValidationLib.isEmpty(passwordInputLayout, passwordEditText, getString(R.string.important), true)) {
                    if (wValidationLib.isEmailAddress(passwordInputLayout, passwordEditText, getString(R.string.important), getString(R.string.important), true)) {
                        callAPIResetPassword();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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


    private void callAPIResetPassword() {
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "email", passwordEditText.getText().toString().trim());

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postApi(getString(R.string.api_reset_password), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_reset_password), buttonSend);
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

            if (from.equals(getResources().getString(R.string.api_reset_password))) {
                if (status==200){
                    passwordEditText.setText("");
                    showSimpleAlert(msg, "");

                }else {
                    showSimpleAlert(msg, "");
                }
            }

        }
    }

}
