package com.org.zapayapp.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.zapayapp.R;
import com.org.zapayapp.adapters.DefaultTransactionAdapter;
import com.org.zapayapp.adapters.HistoryDetailsAdapter;

public class HistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;

    private RecyclerView historyDetailRecView;
    private HistoryDetailsAdapter historyDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        inIt();
        inItAction();
    }

    private void inIt() {
        toolbar = findViewById(R.id.toolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.all_details));
        historyDetailRecView = findViewById(R.id.historyDetailRecView);

        backArrowImageView.setOnClickListener(this);
    }

    private void inItAction() {
        setAdapter();
    }

    private void setAdapter() {
        historyDetailRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        historyDetailsAdapter = new HistoryDetailsAdapter(this);
        historyDetailRecView.setAdapter(historyDetailsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backArrowImageView:
                finish();
                break;
        }
    }
}