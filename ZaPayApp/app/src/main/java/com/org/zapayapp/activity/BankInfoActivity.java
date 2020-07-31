package com.org.zapayapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.org.zapayapp.R;
import com.org.zapayapp.dialogs.ChangeBankDialogActivity;

public class BankInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView changeTV,accountNumberTV,routingNumberTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);
        init();
        initAction();
    }

    private void init() {
        changeTV = findViewById(R.id.changeTV);
        accountNumberTV = findViewById(R.id.accountNumberTV);
        routingNumberTV = findViewById(R.id.routingNumberTV);
    }

    private void initAction() {
        changeTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.changeTV){
            Intent intent = new Intent(BankInfoActivity.this, ChangeBankDialogActivity.class);
            startActivity(intent);
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
