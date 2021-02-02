package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.model.TransactionModel;

import java.util.List;

public class HistoryDetailsAdapter extends RecyclerView.Adapter<HistoryDetailsAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<TransactionModel> transactionModelsList;

    public HistoryDetailsAdapter(Context context) {
        this.context = context;
        this.data = data;
        this.transactionModelsList = transactionModelsList;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        /* private TextView nameTV;
         private TextView dateTV;
         private TextView amountTV;
         private TextView noOfPaymentTV;
         private TextView termTypeTV;
         private TextView borrowModeTitleTV;
         private ImageView dateUpdateIconIV;
 */
        private RecyclerView paybackDateRecycler;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            /*nameTV = itemView.findViewById(R.id.nameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            noOfPaymentTV = itemView.findViewById(R.id.noOfPaymentTV);
            termTypeTV = itemView.findViewById(R.id.termTypeTV);
            borrowModeTitleTV = itemView.findViewById(R.id.borrowModeTitleTV);
            dateUpdateIconIV = itemView.findViewById(R.id.dateUpdateIconIV);*/

            paybackDateRecycler = itemView.findViewById(R.id.paybackDateRecycler);
            paybackDateRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            HistoryDetailsPaybackDateAdapter paybackDateAdapter = new HistoryDetailsPaybackDateAdapter(context);
            paybackDateRecycler.setAdapter(paybackDateAdapter);

        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_details_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //TransactionModel transactionModel = transactionModelsList.get(position);


    }

    @Override
    public int getItemCount() {
        // return transactionModelsList.size();
        return 5;
    }

    private void setDateAdapter() {

    }
}
