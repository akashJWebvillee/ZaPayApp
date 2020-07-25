package com.org.zapayapp.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.adapter.HistoryAdapter;

public class HistoryActivity extends BaseActivity {

    private RecyclerView historyRecyclerView;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        initAction();
    }

    private void init() {
        imgBack = findViewById(R.id.imgBack);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);

    }

    private void initAction() {
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this);
        historyRecyclerView.setAdapter(historyAdapter);
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(HISTORY);
    }
}
