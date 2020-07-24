package com.org.zapayapp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.org.zapayapp.R;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener{

    private TextView textViewSpannable;
    private TextView buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        initAction();
    }

    private void init(){
        textViewSpannable = findViewById(R.id.textViewSpannable);
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
            finish();
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
}
