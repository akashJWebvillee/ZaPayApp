package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.ViewAllHistoryAndTransactionDetailsActivity;
import com.org.zapayapp.model.CommissionModel;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class ViewAllHistoryAndTransactionDetailsAdapter extends RecyclerView.Adapter<ViewAllHistoryAndTransactionDetailsAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<TransactionModel> allTransactionArrayList;
    private ViewAllHistoryAndTransactionDetailsActivity activity;
    private Gson gson;
    private String moveFrom;

    public ViewAllHistoryAndTransactionDetailsAdapter(Context context, List<TransactionModel> allTransactionArrayList, Gson gson, String moveFrom) {
        this.activity = (ViewAllHistoryAndTransactionDetailsActivity) context;
        this.context = context;
        this.data = data;
        this.allTransactionArrayList = allTransactionArrayList;
        this.gson = gson;
        this.moveFrom = moveFrom;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView nameTV;
        private TextView paymentDateTV;
        private TextView amountTV;
        private TextView noOfPaymentTV;
        private TextView termTV;
        private RecyclerView paybackDateRecycler;
        private TextView navigateByTV;
        private TextView acceptByTV;
        private TextView totalPayBackTV;
        private TextView commissionTitleTV;
        private TextView commissionValueTV;
        private TextView viewAllNameType;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            paymentDateTV = itemView.findViewById(R.id.paymentDateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            noOfPaymentTV = itemView.findViewById(R.id.noOfPaymentTV);
            termTV = itemView.findViewById(R.id.termTV);

            navigateByTV = itemView.findViewById(R.id.navigateByTV);
            acceptByTV = itemView.findViewById(R.id.acceptByTV);
            totalPayBackTV = itemView.findViewById(R.id.totalPayBackTV);
            commissionTitleTV = itemView.findViewById(R.id.commissionTitleTV);
            commissionValueTV = itemView.findViewById(R.id.commissionValueTV);
            viewAllNameType = itemView.findViewById(R.id.viewAllNameType);

            paybackDateRecycler = itemView.findViewById(R.id.paybackDateRecycler);
            paybackDateRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
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
        TransactionModel transactionModel = allTransactionArrayList.get(position);
        if (transactionModel.getFirstName() != null && transactionModel.getFirstName().length() > 0) {
            holder.nameTV.setText(transactionModel.getFirstName());
        }

        if (transactionModel.getAmount() != null && transactionModel.getAmount().length() > 0) {
            holder.amountTV.setText(transactionModel.getAmount());
        }
        if (transactionModel.getNoOfPayment() != null && transactionModel.getNoOfPayment().length() > 0) {
            holder.noOfPaymentTV.setText(transactionModel.getNoOfPayment());
        }
        if (transactionModel.getTotalAmount() != null && transactionModel.getTotalAmount().length() > 0) {
            holder.totalPayBackTV.setText(transactionModel.getTotalAmount());
        }

        if (transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
            String pay_date = transactionModel.getPayDate();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                holder.paymentDateTV.setText(DateFormat.dateFormatConvert(date));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (transactionModel.getTermsType() != null && transactionModel.getTermsType().length() > 0) {
            String terms_type = transactionModel.getTermsType();
            String terms_value = transactionModel.getTermsValue();
            if (terms_type.equalsIgnoreCase("1")) {
                terms_value = terms_value + " " + context.getString(R.string.percent);
                holder.termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("2")) {
                terms_value = terms_value + " " + context.getString(R.string.fee);
                holder.termTV.setText("$" + terms_value);
            } else if (terms_type.equalsIgnoreCase("3")) {
                terms_value = terms_value + " " + context.getString(R.string.discount);
                holder.termTV.setText("$" + terms_value);
            } else if (terms_type.equalsIgnoreCase("4")) {
                holder.termTV.setText(activity.getResources().getString(R.string.none));
            }
        }

        if (transactionModel.getCommission_charges_detail() != null && transactionModel.getCommission_charges_detail().length() > 0) {
            String commission_charges_detail = transactionModel.getCommission_charges_detail();
            commission_charges_detail.replace("\\", "/");
            CommissionModel commissionModel = gson.fromJson(commission_charges_detail, CommissionModel.class);

            if (context.getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
                if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                    if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                        holder.commissionValueTV.setText("$" + transactionModel.getAdmin_commission_from_lender());
                    }
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        holder.commissionValueTV.setText("$" + transactionModel.getAdmin_commission_from_borrower());
                    }
                }
            } else if (context.getString(R.string.history).equalsIgnoreCase(moveFrom)) {
                if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                        holder.commissionValueTV.setText("$" + transactionModel.getAdmin_commission_from_lender());
                    }
                    if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        holder.commissionValueTV.setText("$" + transactionModel.getAdmin_commission_from_borrower());
                    }
                }
            }
        }

        if (transactionModel.getPayDatesList() != null && transactionModel.getPayDatesList().size() > 0) {
            List<DateModel> payDatesList = transactionModel.getPayDatesList();
        /*    dateModelArrayList.clear();
            dateModelArrayList.addAll(list);
            for (int i = 0; i < dateModelArrayList.size(); i++) {
                if (dateModelArrayList.get(i).getStatus().equalsIgnoreCase("remaining")) {
                    dateModelArrayList.get(i).setLatestRemaining(true);
                    break;
                }
            }*/
            setAdapterFunc(holder.paybackDateRecycler, payDatesList);
        }

        if (context.getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
            if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                holder.viewAllNameType.setText(context.getString(R.string.lender));
            } else {
                holder.viewAllNameType.setText(context.getString(R.string.borrower));
            }
        } else if (context.getString(R.string.history).equalsIgnoreCase(moveFrom)) {
            if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                holder.viewAllNameType.setText(context.getString(R.string.lender));
            } else {
                holder.viewAllNameType.setText(context.getString(R.string.borrower));
            }
        }
    }

    @Override
    public int getItemCount() {
        return allTransactionArrayList.size();
    }

    private void setAdapterFunc(RecyclerView paybackDateRecycler, List<DateModel> payDatesList) {
        ViewAllHistoryAndTransactionPaybackDateAdapter paybackDateAdapter = new ViewAllHistoryAndTransactionPaybackDateAdapter(context, payDatesList);
        paybackDateRecycler.setAdapter(paybackDateAdapter);
    }
}
