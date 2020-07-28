package com.org.zapayapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.activity.BorrowSummaryActivity;
import com.org.zapayapp.activity.LendingSummaryActivity;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyHolder> {
    private Context context;
    private String data;

    public TransactionAdapter(Context context, String data) {
        this.context = context;
        this.data = data;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public TransactionAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_row, parent, false);

        return new TransactionAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.equalsIgnoreCase("pending")) {
                    Intent intent = new Intent(context, BorrowSummaryActivity.class);
                    context.startActivity(intent);
                } else if (data.equalsIgnoreCase("negotiation")) {
                    Intent intent = new Intent(context, LendingSummaryActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
