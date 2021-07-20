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
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import java.util.List;

public class ViewAllHistoryAndTransactionPaybackDateAdapter extends RecyclerView.Adapter<ViewAllHistoryAndTransactionPaybackDateAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<DateModel> payDatesList;
    private TransactionModel transactionModel;

    public ViewAllHistoryAndTransactionPaybackDateAdapter(Context context, List<DateModel> payDatesList,TransactionModel transactionModel) {
        this.context = context;
        this.data = data;
        this.payDatesList = payDatesList;
        this.transactionModel = transactionModel;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView dateTV;
        private TextView paymentNoTV;
        private TextView amountTV;
        private RelativeLayout parentRL;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateTV);
            paymentNoTV = itemView.findViewById(R.id.paymentNoTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            parentRL = itemView.findViewById(R.id.parentRL);

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

        /*if (dateModel.getPayDate() != null && dateModel.getPayDate().length() > 0) {
            holder.amountTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY).toString()+ CommonMethods.setDigitAfterDecimalValue(Float.parseFloat(dateModel.getEmi_amount()),2));
        }*/

        if (dateModel.getPayDate() != null && dateModel.getPayDate().length() > 0) {
            holder.paymentNoTV.setText(String.valueOf(position + 1));
        }


      /*  if (dateModel.getIs_default_txn() != null && dateModel.getIs_default_txn().length() > 0) {
            if (dateModel.getIs_default_txn().equals("1")) { //is_default_txn==1 defaulter hai
                if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().length()>0&&transactionModel.getRequestBy().equals("1")){
                    holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_red_4redius));
               } else {
                    holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_4redius));
                }

                float defaultFeeAmount=Float.parseFloat(dateModel.getDefault_fee_amount());
                float amount=Float.parseFloat(dateModel.getEmi_amount());
                float defaultPayAmount=amount+defaultFeeAmount;
                holder.amountTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY).toString()+ CommonMethods.setDigitAfterDecimalValue(defaultPayAmount,2));

            }else if (dateModel.getIs_default_txn().equals("0")){//is_default_txn==1 not defaulter
                holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_4redius));
                holder.amountTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY).toString()+ CommonMethods.setDigitAfterDecimalValue(Float.parseFloat(dateModel.getEmi_amount()),2));
            }
        }*/





        if (!Const.isRequestByMe(transactionModel.getFromId())){
            if (dateModel.getIs_default_txn() != null && dateModel.getIs_default_txn().length() > 0) {
                if (dateModel.getIs_default_txn().equals("1")) { //is_default_txn==1 defaulter hai
                    if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().length()>0&&transactionModel.getRequestBy().equals("1")){
                        holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_red_4redius));
                    }else {
                        holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_4redius));
                    }

                    float defaultFeeAmount=Float.parseFloat(dateModel.getDefault_fee_amount());
                    float amount=Float.parseFloat(dateModel.getEmi_amount());
                    float defaultPayAmount=amount+defaultFeeAmount;
                    holder.amountTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY).toString()+ CommonMethods.setDigitAfterDecimalValue(defaultPayAmount,2));

                }else if (dateModel.getIs_default_txn().equals("0")){//is_default_txn==1 not defaulter
                    holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_4redius));
                    holder.amountTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY).toString()+ CommonMethods.setDigitAfterDecimalValue(Float.parseFloat(dateModel.getEmi_amount()),2));

                }
            }

        }else if (Const.isRequestByMe(transactionModel.getFromId())){
            if (dateModel.getIs_default_txn() != null && dateModel.getIs_default_txn().length() > 0) {
                if (dateModel.getIs_default_txn().equals("1")) { //is_default_txn==1 defaulter hai
                    if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().length()>0&&transactionModel.getRequestBy().equals("2")){
                        holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_red_4redius));
                    }else {
                        holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_4redius));
                    }

                    float defaultFeeAmount=Float.parseFloat(dateModel.getDefault_fee_amount());
                    float amount=Float.parseFloat(dateModel.getEmi_amount());
                    float defaultPayAmount=amount+defaultFeeAmount;
                    holder.amountTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY).toString()+ CommonMethods.setDigitAfterDecimalValue(defaultPayAmount,2));

                }else if (dateModel.getIs_default_txn().equals("0")){//is_default_txn==1 not defaulter
                    holder.parentRL.setBackground(CommonMethods.getDrawableWrapper(context,R.drawable.rectanguler_4redius));
                    holder.amountTV.setText(SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY).toString()+ CommonMethods.setDigitAfterDecimalValue(Float.parseFloat(dateModel.getEmi_amount()),2));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return payDatesList.size();
    }
}
