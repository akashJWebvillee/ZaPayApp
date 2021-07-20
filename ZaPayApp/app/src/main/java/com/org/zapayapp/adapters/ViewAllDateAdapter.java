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
import com.org.zapayapp.utils.DateFormat;

import java.util.ArrayList;
import java.util.List;

public class ViewAllDateAdapter extends RecyclerView.Adapter<ViewAllDateAdapter.MyHolder> {
    private Context context;
    private List<DateModel> dateModelArrayList;


    public ViewAllDateAdapter(Context context, List<DateModel> dateModelArrayList) {
        this.context = context;
        this.dateModelArrayList = dateModelArrayList;

    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView paymentNoTV, dateTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            paymentNoTV = itemView.findViewById(R.id.paymentNoTV);
            dateTV = itemView.findViewById(R.id.dateTV);

        }
    }

    @NonNull
    @Override
    public ViewAllDateAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_all_date_row, parent, false);
        return new ViewAllDateAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllDateAdapter.MyHolder holder, final int position) {
        int pos = position + 1;
        holder.paymentNoTV.setText("" + pos);
        DateModel dateModel = dateModelArrayList.get(position);
        if (dateModel.getPayDate() != null && dateModel.getPayDate().length() > 0) {
            holder.dateTV.setText(DateFormat.dateFormatConvert(dateModelArrayList.get(position).getPayDate()));

          //  holder.dateTV.setText(dateModel.getPayDate());
        }

    }

    @Override
    public int getItemCount() {
        return dateModelArrayList.size();
    }


}

