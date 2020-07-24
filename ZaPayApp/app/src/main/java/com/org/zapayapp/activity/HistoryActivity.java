package com.org.zapayapp.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.org.zapayapp.R;
import com.org.zapayapp.adapter.HistoryAdapter;

public class HistoryActivity extends AppCompatActivity {
private RecyclerView historyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        initAction();
    }

    private void init(){
        historyRecyclerView=findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        HistoryAdapter historyAdapter=new HistoryAdapter(HistoryActivity.this);
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void initAction(){


    }




}
