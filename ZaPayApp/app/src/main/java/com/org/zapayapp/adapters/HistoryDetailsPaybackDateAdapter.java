package com.org.zapayapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.model.TransactionModel;
import java.util.List;

public class HistoryDetailsPaybackDateAdapter extends RecyclerView.Adapter<HistoryDetailsPaybackDateAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<TransactionModel> transactionModelsList;

    public HistoryDetailsPaybackDateAdapter(Context context) {
        this.context = context;
        this.data = data;
        this.transactionModelsList = transactionModelsList;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.histry_detail_payback_date_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //TransactionModel transactionModel = transactionModelsList.get(position);




    }

    @Override
    public int getItemCount() {
        // return transactionModelsList.size();
        return 2;
    }
}
