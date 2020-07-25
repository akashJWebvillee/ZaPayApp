package com.org.zapayapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.adapters.IndicatorAdapter;

import java.util.ArrayList;
import java.util.List;

public class LendBorrowActivity extends BaseActivity {

    private RecyclerView indicatorRecycler;
    private List<String> listIndicator;
    private TextView nextButtonTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_borrow);
        init();
        initAction();
    }

    private void init() {
        indicatorRecycler = findViewById(R.id.indicatorRecycler);
        indicatorRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        indicatorRecycler.setItemAnimator(new DefaultItemAnimator());

        nextButtonTV = findViewById(R.id.nextButtonTV);

        listIndicator = new ArrayList<>();
    }

    private void initAction() {
        listIndicator.add("Amount");
        listIndicator.add("Terms");
        listIndicator.add("No of payment");
        listIndicator.add("Packback date");
        listIndicator.add("Borrow summary");
        listIndicator.add("Lending summary");
        listIndicator.add("Select contact");

        final IndicatorAdapter historyAdapter = new IndicatorAdapter(this, listIndicator);
        indicatorRecycler.setAdapter(historyAdapter);


        nextButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyAdapter.getSelectedPos() < listIndicator.size() -1)
                    historyAdapter.setSelected(historyAdapter.getSelectedPos() + 1);
            }
        });
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(100);
    }
}
