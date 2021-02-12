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
import com.org.zapayapp.activity.ViewAllSummaryActivity;
import com.org.zapayapp.model.DefaultModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DefaultTransactionAdapter extends RecyclerView.Adapter<DefaultTransactionAdapter.MyHolder> {
    private Context context;
    private List<TransactionModel> transactionModelsList;

    public DefaultTransactionAdapter(Context context, ArrayList<TransactionModel> transactionModelsList) {
        this.context = context;
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
        if (transactionModel.getFirstName() != null && transactionModel.getFirstName().length() > 0 && transactionModel.getLastName() != null && transactionModel.getFirstName().length() > 0) {
            holder.nameTV.setText(transactionModel.getFirstName() + " " + transactionModel.getLastName());
        }
        if (transactionModel.getTotalAmount() != null && transactionModel.getTotalAmount().length() > 0) {
            String total_amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + transactionModel.getTotalAmount();
            holder.amountTV.setText(total_amount);
        }
        if (transactionModel.getNoOfPayment() != null && transactionModel.getNoOfPayment().length() > 0) {
            holder.noOfPaymentTV.setText(transactionModel.getNoOfPayment());
        }

     /*   if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
            holder.borrowModeTitleTV.setText(context.getString(R.string.lend_mode));
        } else if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
            holder.borrowModeTitleTV.setText(context.getString(R.string.borrow_mode));
        }*/
        holder.borrowModeTitleTV.setText(context.getString(R.string.borrow_mode));
        if (transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
            String pay_date = transactionModel.getPayDate();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                holder.dateTV.setText(DateFormat.dateFormatConvert(date));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (transactionModel.getTermsType().equalsIgnoreCase("1")) {
            holder.termTypeTV.setText(context.getString(R.string.percent));
        } else if (transactionModel.getTermsType().equalsIgnoreCase("2")) {
            holder.termTypeTV.setText(context.getString(R.string.fee));
        } else if (transactionModel.getTermsType().equalsIgnoreCase("3")) {
            holder.termTypeTV.setText(context.getString(R.string.discount));
        } else if (transactionModel.getTermsType().equalsIgnoreCase("4")) {
            holder.termTypeTV.setText(context.getString(R.string.none));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewAllHistoryAndTransactionDetailsActivity.class);
            intent.putExtra("transactionId", transactionModel.getId());
            intent.putExtra("moveFrom", context.getString(R.string.default_transaction));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactionModelsList.size();
    }
}
