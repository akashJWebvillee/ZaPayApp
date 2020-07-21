package com.org.zapayapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.org.zapayapp.R;
import com.org.zapayapp.utils.CommonMethods;

public class LoginActivity extends AppCompatActivity {
    private TextView loginTV;
    private TextView signUpTV;
    private TextView mTextAgree;
    private RelativeLayout loginRelativeLayout;
    private RelativeLayout signUpRelativeLayout;

    private EditText etPassword;
    private TextView loginButtonTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        inIt();
        inItAction();
    }

    private void inIt() {
        loginTV = findViewById(R.id.loginTV);
        signUpTV = findViewById(R.id.signUpTV);
        mTextAgree = findViewById(R.id.mTextAgree);
        loginRelativeLayout = findViewById(R.id.loginRelativeLayout);
        signUpRelativeLayout = findViewById(R.id.signUpRelativeLayout);
        etPassword = findViewById(R.id.etPassword);
        loginButtonTV = findViewById(R.id.loginButtonTV);
        termCondition();
    }

    private void inItAction() {
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTV.setTextColor(Color.parseColor("#ffffff"));
                signUpTV.setTextColor(Color.parseColor("#DDDDDD"));
                loginRelativeLayout.setVisibility(View.VISIBLE);
                signUpRelativeLayout.setVisibility(View.GONE);

            }
        });

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTV.setTextColor(Color.parseColor("#DDDDDD"));
                signUpTV.setTextColor(Color.parseColor("#ffffff"));
                loginRelativeLayout.setVisibility(View.GONE);
                signUpRelativeLayout.setVisibility(View.VISIBLE);
            }
        });


        loginButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPassword.getText().toString().trim().isEmpty()) {

                } else {
                    // etPassword.setFocusable(true);
                    //etPassword.setError("important");
                    etPassword.setError("Invalid Password");
                    etPassword.requestFocus();
                }
            }
        });

    }

    private void termCondition() {
        String textTerms = this.getString(R.string.term_and_condition);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(textTerms);
        ClickableSpan redClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                try {
                    String url = "https://www.javatpoint.com/";
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
                ds.setColor(CommonMethods.getColorWrapper(LoginActivity.this, R.color.colorPrimaryDark));
            }
        };

        ssBuilder.setSpan(
                redClickableSpan, // Span to add
                40, // Start of the span (inclusive)
                textTerms.length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        );

        mTextAgree.setText(ssBuilder);
        mTextAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
