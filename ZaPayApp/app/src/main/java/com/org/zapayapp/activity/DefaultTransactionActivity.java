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

public class DefaultTransactionActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;

    private RecyclerView defaultTransactionRecView;
    private DefaultTransactionAdapter defaultTransactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        inIt();
        inItAction();
    }

    private void inIt() {
        toolbar = findViewById(R.id.toolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.default_transaction));
        defaultTransactionRecView = findViewById(R.id.defaultTransactionRecView);

        backArrowImageView.setOnClickListener(this);
    }

    private void inItAction() {
        setAdapter();
    }

    private void setAdapter() {
        defaultTransactionRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        defaultTransactionAdapter = new DefaultTransactionAdapter(this);
        defaultTransactionRecView.setAdapter(defaultTransactionAdapter);
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