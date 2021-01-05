package com.org.zapayapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.org.zapayapp.R;

public class CongratulationActivity extends AppCompatActivity {
    private TextView closeButtonTV;
    private TextView setMsgTV;
    private String message="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);
        inIt();
        getIntentFunc();
        initAction();
    }


    private void inIt() {
        setMsgTV = findViewById(R.id.setMsgTV);
        closeButtonTV = findViewById(R.id.closeButtonTV);
    }

    private void getIntentFunc() {
        if (getIntent().getStringExtra("message") != null) {
             message = getIntent().getStringExtra("message");
        }
    }

    private void initAction() {
        setMsgTV.setText(message);
        closeButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
