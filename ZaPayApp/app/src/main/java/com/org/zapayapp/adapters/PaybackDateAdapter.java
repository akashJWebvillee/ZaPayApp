package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.activity.ViewAllSummaryActivity;
import com.org.zapayapp.model.DateModel;

import java.util.ArrayList;

public class PaybackDateAdapter extends RecyclerView.Adapter<PaybackDateAdapter.MyHolder> {
    private Context context;
    private ArrayList<DateModel> dateModelArrayList;

    public PaybackDateAdapter(Context context, ArrayList<DateModel> dateModelArrayList) {
        this.context = context;
        this.dateModelArrayList = dateModelArrayList;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView paymentNoTV, dateTV;
        private RelativeLayout editDateRL;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            paymentNoTV = itemView.findViewById(R.id.paymentNoTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            editDateRL = itemView.findViewById(R.id.editDateRL);
        }
    }

    @NonNull
    @Override
    public PaybackDateAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payback_date_list, parent, false);
        return new PaybackDateAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaybackDateAdapter.MyHolder holder, final int position) {
        holder.paymentNoTV.setText(String.valueOf(position + 1));
        if (dateModelArrayList.get(position).getPayDate() != null && dateModelArrayList.get(position).getPayDate().length() > 0) {
            holder.dateTV.setText(dateModelArrayList.get(position).getPayDate());
        }

        if (dateModelArrayList.get(position).isEditable()) {
            holder.editDateRL.setVisibility(View.VISIBLE);
        } else {
            holder.editDateRL.setVisibility(View.GONE);
        }


        //status- remaining, processed, pending, cancelled, failed
        if (dateModelArrayList.get(position).getStatus() != null && dateModelArrayList.get(position).getStatus().length() > 0) {
            if (dateModelArrayList.get(position).getStatus().equals("remaining")) {

            } else if (dateModelArrayList.get(position).getStatus().equals("processed")) {

            } else if (dateModelArrayList.get(position).getStatus().equals("pending")) {

            } else if (dateModelArrayList.get(position).getStatus().equals("cancelled")) {

            } else if (dateModelArrayList.get(position).getStatus().equals("failed")) {

            }
        }

        holder.editDateRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ViewAllSummaryActivity) {
                    if (dateModelArrayList.get(position).getIs_extended() != null && dateModelArrayList.get(position).getIs_extended().equals("0")) {
                        ((ViewAllSummaryActivity) context).selectPaybackDate(position, dateModelArrayList.get(position));

                    } else {
                        ((ViewAllSummaryActivity) context).showSimpleAlert(context.getString(R.string.date_already_extended), "");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateModelArrayList.size();
    }
}

