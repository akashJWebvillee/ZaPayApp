package com.org.zapayapp.activity;
import android.os.Bundle;
import com.org.zapayapp.R;
import com.org.zapayapp.model.TransactionModel;

public class LendingSummaryActivity extends BaseActivity {
    private TransactionModel transactionModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lending_summary);
        inIt();
    }

    private void inIt(){
        if (getIntent().getSerializableExtra("transactionModel")!=null){
            transactionModel= (TransactionModel) getIntent().getSerializableExtra("transactionModel");
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
