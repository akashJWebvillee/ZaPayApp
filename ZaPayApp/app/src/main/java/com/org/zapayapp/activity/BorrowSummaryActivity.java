package com.org.zapayapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.org.zapayapp.R;
import com.org.zapayapp.model.TransactionModel;

public class BorrowSummaryActivity extends BaseActivity {

    private TextView navigateTV;
private TransactionModel transactionModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_summary);
        init();
        initAction();
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }


    private void init() {
        if (getIntent().getSerializableExtra("transactionModel")!=null){
             transactionModel= (TransactionModel) getIntent().getSerializableExtra("transactionModel");
        }

        navigateTV = findViewById(R.id.navigateTV);
    }

    private void initAction() {
        navigateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(BorrowSummaryActivity.this, LendingSummaryActivity.class));
            }
        });


    }
}
