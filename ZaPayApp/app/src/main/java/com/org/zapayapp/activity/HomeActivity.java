package com.org.zapayapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.org.zapayapp.R;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout homeLLLend, homeLLBorrow;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        initAction();
    }

    private void init() {
        homeLLLend = findViewById(R.id.homeLLLend);
        homeLLBorrow = findViewById(R.id.homeLLBorrow);

        AppCenter.start(getApplication(), "7c7f48b8-92b9-419a-842c-536b68581c02",
                Analytics.class, Crashes.class);// add this to trace the crashlytics



    }

    private void initAction() {
        homeLLLend.setOnClickListener(this);
        homeLLBorrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeLLLend:
                intent = new Intent(HomeActivity.this, LendBorrowActivity.class);
                intent.putExtra("isBorrow", false);
                startActivity(intent);
                break;
            case R.id.homeLLBorrow:
                intent = new Intent(HomeActivity.this, LendBorrowActivity.class);
                intent.putExtra("isBorrow", true);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(100);
    }



}
