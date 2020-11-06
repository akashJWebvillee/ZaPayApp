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
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.TimeStamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            nameTV = itemView.findViewById(R.id.nameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            noOfPaymentTV = itemView.findViewById(R.id.noOfPaymentTV);
            termTypeTV = itemView.findViewById(R.id.termTypeTV);
            borroModeTitleTV = itemView.findViewById(R.id.borrowModeTitleTV);
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
        TransactionModel transactionModel = transactionModelsList.get(position);


        if (transactionModel.getFirstName() != null && transactionModel.getFirstName().length() > 0 && transactionModel.getFirstName() != null && transactionModel.getFirstName().length() > 0) {
            String name = transactionModel.getFirstName() + " " + transactionModel.getLastName();
            holder.nameTV.setText(name);
        }

        if (transactionModel.getCreatedAt() != null && transactionModel.getCreatedAt().length() > 0) {
           // holder.dateTV.setText(TimeStamp.timeFun(transactionModel.getCreatedAt()));
        }

        if (transactionModel.getPayDate()!=null&&transactionModel.getPayDate().length()>0){
            String pay_date=transactionModel.getPayDate();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1=  jsonArray.getJSONObject(0);
                String date= jsonObject1.getString("date");
                try {
                    //holder.dateTV.setText(DateFormat.getDateFromEpoch(date));
                    holder.dateTV.setText(DateFormat.dateFormatConvert(date));
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (transactionModel.getNoOfPayment() != null && transactionModel.getNoOfPayment().length() > 0) {
            holder.noOfPaymentTV.setText(transactionModel.getNoOfPayment());
        }

        if (transactionModel.getAmount() != null && transactionModel.getAmount().length() > 0) {
            String total_amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + transactionModel.getTotalAmount();
            holder.amountTV.setText(total_amount);
        }

        if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
            holder.borroModeTitleTV.setText(context.getString(R.string.lend_mode));
        } else if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
            holder.borroModeTitleTV.setText(context.getString(R.string.borrow_mode));
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (!transactionModel.getStatus().equalsIgnoreCase("2")) {
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        Intent intent = new Intent(context, BorrowSummaryActivity.class);
                        intent.putExtra("moveFrom", data);
                        intent.putExtra("transactionId", transactionModel.getId());
                        context.startActivity(intent);
                    } else if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, LendingSummaryActivity.class);
                        intent.putExtra("moveFrom", data);
                        intent.putExtra("transactionId", transactionModel.getId());
                        context.startActivity(intent);
                    }
                //}
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionModelsList.size();
    }
}
