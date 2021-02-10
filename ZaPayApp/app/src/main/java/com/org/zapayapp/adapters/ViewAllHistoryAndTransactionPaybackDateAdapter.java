package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.model.DateModel;

import java.util.List;

public class ViewAllHistoryAndTransactionPaybackDateAdapter extends RecyclerView.Adapter<ViewAllHistoryAndTransactionPaybackDateAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<DateModel> payDatesList;

    public ViewAllHistoryAndTransactionPaybackDateAdapter(Context context, List<DateModel> payDatesList) {
        this.context = context;
        this.data = data;
        this.payDatesList = payDatesList;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView dateTV;
        private TextView paymentNoTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateTV);
            paymentNoTV = itemView.findViewById(R.id.paymentNoTV);

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
        DateModel dateModel = payDatesList.get(position);
        if (dateModel.getPayDate() != null && dateModel.getPayDate().length() > 0) {
            holder.dateTV.setText(dateModel.getPayDate());
        }
        if (dateModel.getPayDate() != null && dateModel.getPayDate().length() > 0) {
            holder.paymentNoTV.setText(String.valueOf(position + 1));
        }
    }

    @Override
    public int getItemCount() {
        return payDatesList.size();
    }
}
