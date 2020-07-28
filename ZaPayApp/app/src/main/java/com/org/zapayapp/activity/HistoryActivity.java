package com.org.zapayapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.adapters.HistoryAdapter;

public class HistoryActivity extends BaseActivity {

    private RecyclerView historyRecyclerView;
    private ImageView backArrowImageView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        initAction();
    }

    private void init() {
        toolbar = findViewById(R.id.customToolbar);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
    }

    private void initAction() {
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this);
        historyRecyclerView.setAdapter(historyAdapter);

        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
