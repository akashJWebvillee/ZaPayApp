package com.org.zapayapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.org.zapayapp.R;
public class BorrowSummaryActivity extends BaseActivity {
private TextView navigateTV;
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


    private void init(){
        navigateTV=findViewById(R.id.navigateTV);
    }

    private void initAction(){
        navigateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BorrowSummaryActivity.this,LendingSummaryActivity.class));
            }
        });


    }
}
