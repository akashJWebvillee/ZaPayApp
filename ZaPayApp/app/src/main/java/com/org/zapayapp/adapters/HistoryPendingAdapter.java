package com.org.zapayapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.activity.BorrowSummaryActivity;
import com.org.zapayapp.activity.LendingSummaryActivity;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.TimeStamp;

import java.util.List;

public class HistoryPendingAdapter extends RecyclerView.Adapter<HistoryPendingAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<TransactionModel> transactionModelsList;

    public HistoryPendingAdapter(Context context, List<TransactionModel> transactionModelsList, String data) {
        this.context = context;
        this.data = data;
        this.transactionModelsList = transactionModelsList;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView nameTV;
        private TextView dateTV;
        private TextView amountTV;
        private TextView noOfPaymentTV;
        private TextView termTypeTV;
        private TextView borroModeTitleTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.nameTV);
            dateTV=itemView.findViewById(R.id.dateTV);
            amountTV=itemView.findViewById(R.id.amountTV);
            noOfPaymentTV=itemView.findViewById(R.id.noOfPaymentTV);
            termTypeTV=itemView.findViewById(R.id.termTypeTV);
            borroModeTitleTV=itemView.findViewById(R.id.borroModeTitleTV);
        }
    }

    @NonNull
    @Override
    public HistoryPendingAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_row, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPendingAdapter.MyHolder holder, int position) {
        TransactionModel transactionModel= transactionModelsList.get(position);

        holder.nameTV.setText(transactionModel.getFirstName()+" "+transactionModel.getLastName());
        holder.dateTV.setText(TimeStamp.timeFun(transactionModel.getCreatedAt()));
        holder.noOfPaymentTV.setText(transactionModel.getNoOfPayment());
        holder.amountTV.setText("$"+transactionModel.getAmount());

        if (transactionModel.getRequestBy().equalsIgnoreCase("1")){
            holder.borroModeTitleTV.setText("Lend Mode:");
        }else if (transactionModel.getRequestBy().equalsIgnoreCase("2")){
            holder.borroModeTitleTV.setText("Borrow Mode:");
        }

        if (transactionModel.getTermsType().equalsIgnoreCase("1")){
            holder.termTypeTV.setText(transactionModel.getTermsValue()+" %");
        }else if (transactionModel.getTermsType().equalsIgnoreCase("2")){
            holder.termTypeTV.setText(transactionModel.getTermsValue()+" Fee");
        }else if (transactionModel.getTermsType().equalsIgnoreCase("3")){
            holder.termTypeTV.setText(transactionModel.getTermsValue()+" Discount");
        }else if (transactionModel.getTermsType().equalsIgnoreCase("4")){
            holder.termTypeTV.setText(transactionModel.getTermsValue()+" None");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                    Intent intent = new Intent(context, BorrowSummaryActivity.class);
                    intent.putExtra("transactionModel",transactionModel);
                    context.startActivity(intent);
                } else if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                    Intent intent = new Intent(context, LendingSummaryActivity.class);
                    intent.putExtra("transactionModel",transactionModel);
                    context.startActivity(intent);
                }
            }
        });



     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    @Override
    public int getItemCount() {
        return transactionModelsList.size();
    }
}