package com.org.zapayapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.ViewAllHistoryAndTransactionDetailsActivity;
import com.org.zapayapp.model.DefaultModel;
import com.org.zapayapp.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class DefaultTransactionAdapter extends RecyclerView.Adapter<DefaultTransactionAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<TransactionModel> transactionModelsList;

    public DefaultTransactionAdapter(Context context,ArrayList<TransactionModel>transactionModelsList) {
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
        private TextView borrowModeTitleTV;
        private ImageView dateUpdateIconIV;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            noOfPaymentTV = itemView.findViewById(R.id.noOfPaymentTV);
            termTypeTV = itemView.findViewById(R.id.termTypeTV);
            borrowModeTitleTV = itemView.findViewById(R.id.borrowModeTitleTV);
            dateUpdateIconIV = itemView.findViewById(R.id.dateUpdateIconIV);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.default_transaction_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        TransactionModel transactionModel = transactionModelsList.get(position);
        if (transactionModel.getFirstName()!=null&&transactionModel.getFirstName().length()>0&&transactionModel.getLastName()!=null&&transactionModel.getFirstName().length()>0){
            holder.nameTV.setText(transactionModel.getFirstName()+" "+transactionModel.getLastName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewAllHistoryAndTransactionDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
       // return transactionModelsList.size();
        return transactionModelsList.size();
    }
}
