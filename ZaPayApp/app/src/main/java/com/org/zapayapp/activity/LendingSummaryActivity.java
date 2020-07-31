package com.org.zapayapp.activity;
import android.os.Bundle;
import com.org.zapayapp.R;

public class LendingSummaryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lending_summary);
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
