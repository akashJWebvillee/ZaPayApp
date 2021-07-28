package com.org.zapayapp.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.PdfViewActivity;
import com.org.zapayapp.activity.ViewAllHistoryAndTransactionDetailsActivity;
import com.org.zapayapp.model.AgreementPdfDetailModel;
import com.org.zapayapp.model.CommissionModel;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewAllHistoryAndTransactionDetailsAdapter extends RecyclerView.Adapter<ViewAllHistoryAndTransactionDetailsAdapter.MyHolder> {
    private Context context;
    private List<TransactionModel> allTransactionArrayList;
    private ViewAllHistoryAndTransactionDetailsActivity activity;
    private Gson gson;
    private String moveFrom;

    public ViewAllHistoryAndTransactionDetailsAdapter(Context context, List<TransactionModel> allTransactionArrayList, Gson gson, String moveFrom) {
        this.activity = (ViewAllHistoryAndTransactionDetailsActivity) context;
        this.context = context;
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
        private TextView afterCommissionAmountTV;
        private LinearLayout agreementLL;
        private TextView defaultFeeAmountTV;
        private LinearLayout defaultFeeLL;
        private LinearLayout commissionLL;
        private LinearLayout afterZapayCommissionLL;

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
            afterCommissionAmountTV = itemView.findViewById(R.id.afterCommissionAmountTV);
            agreementLL = itemView.findViewById(R.id.agreementLL);
            defaultFeeAmountTV = itemView.findViewById(R.id.defaultFeeAmountTV);
            defaultFeeLL = itemView.findViewById(R.id.defaultFeeLL);
            commissionLL = itemView.findViewById(R.id.commissionLL);
            afterZapayCommissionLL = itemView.findViewById(R.id.afterZapayCommissionLL);

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

      /*  if (transactionModel.getFromId() != null && transactionModel.getFromId().length() > 0) {
            if (Const.isRequestByMe(transactionModel.getFromId())) {
                if (transactionModel.getReceiver_first_name() != null && transactionModel.getReceiver_first_name().length() > 0 && transactionModel.getReceiver_last_name() != null && transactionModel.getReceiver_last_name().length() > 0) {
                    holder.nameTV.setText(transactionModel.getReceiver_first_name() + " " + transactionModel.getReceiver_last_name());
                }
            } else {
                if (transactionModel.getSender_first_name() != null && transactionModel.getSender_first_name().length() > 0 && transactionModel.getSender_last_name() != null && transactionModel.getSender_last_name().length() > 0) {
                    holder.nameTV.setText("" + transactionModel.getSender_first_name() + " " + transactionModel.getSender_last_name());
                }
            }
        }
*/


        if (transactionModel.getAmount() != null && transactionModel.getAmount().length() > 0) {
            holder.amountTV.setText(Const.getCurrency() + transactionModel.getAmount());
        }
        if (transactionModel.getNoOfPayment() != null && transactionModel.getNoOfPayment().length() > 0) {
            holder.noOfPaymentTV.setText(transactionModel.getNoOfPayment());
        }
        if (transactionModel.getTotalAmount() != null && transactionModel.getTotalAmount().length() > 0) {
            holder.totalPayBackTV.setText(Const.getCurrency() + transactionModel.getTotalAmount());
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
                holder.termTV.setText(Const.getCurrency() + terms_value);
            } else if (terms_type.equalsIgnoreCase("3")) {
                terms_value = terms_value + " " + context.getString(R.string.discount);
                holder.termTV.setText(Const.getCurrency() + terms_value);
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
                        holder.commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_lender());
                    }
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        holder.commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_borrower());
                    }
                }
                holder.defaultFeeAmountTV.setText(Const.getCurrency() + commissionModel.getDefaultFeeValue());

            } else if (context.getString(R.string.history).equalsIgnoreCase(moveFrom)) {
                if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                        holder.commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_lender());
                    }
                    if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        holder.commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_borrower());
                    }
                }
                holder.defaultFeeAmountTV.setText(Const.getCurrency() + commissionModel.getDefaultFeeValue());

            } else if (context.getString(R.string.default_transaction).equalsIgnoreCase(moveFrom)) {
                if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                    holder.commissionTitleTV.setText(context.getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                    holder.commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_borrower());
                }
                holder.defaultFeeAmountTV.setText(Const.getCurrency() + commissionModel.getDefaultFeeValue());
            }
        }

        if (transactionModel.getPayDatesList() != null && transactionModel.getPayDatesList().size() > 0) {
            List<DateModel> payDatesList = transactionModel.getPayDatesList();
            if (moveFrom != null && moveFrom.length() > 0 && moveFrom.equalsIgnoreCase(context.getString(R.string.default_transaction))) {
                activity.setTotalPayData11(payDatesList, transactionModel);
                holder.defaultFeeLL.setVisibility(View.VISIBLE);
            } else {
                holder.defaultFeeLL.setVisibility(View.VISIBLE);
            }
            setAdapterFunc(holder.paybackDateRecycler, payDatesList, transactionModel);
        }

     /*   if (context.getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
            if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                holder.viewAllNameType.setText(context.getString(R.string.lender));
            } else {
                holder.viewAllNameType.setText(context.getString(R.string.borrower));
            }
        } else if (context.getString(R.string.history).equalsIgnoreCase(moveFrom)) {
            if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                holder.viewAllNameType.setText(context.getString(R.string.lender));
            } else {
                holdger.viewAllNameType.setText(context.getString(R.string.borrower));
            }
        }*/


        if (transactionModel.getAgreementPdfDetailModelList() != null && transactionModel.getAgreementPdfDetailModelList().size() > 0) {
            List<AgreementPdfDetailModel> pdf_details = transactionModel.getAgreementPdfDetailModelList();
            AgreementPdfDetailModel agreementPdfDetailModel = pdf_details.get(0);
            holder.agreementLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PdfViewActivity.class);
                    intent.putExtra("pdf_url", agreementPdfDetailModel.getPdfUrl());
                    context.startActivity(intent);
                }
            });
        }


        if (context.getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
            if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                if (transactionModel.getRequestBy().equals("1")) {
                    if (transactionModel.getAdmin_commission_from_lender() != null && transactionModel.getAdmin_commission_from_lender().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_lender());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        holder.afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                } else if (transactionModel.getRequestBy().equals("2")) {
                    if (transactionModel.getAdmin_commission_from_borrower() != null && transactionModel.getAdmin_commission_from_borrower().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_borrower());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        holder.afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                }
            }

        } else if (context.getString(R.string.history).equalsIgnoreCase(moveFrom)) {
            if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                if (transactionModel.getRequestBy().equals("2")) {
                    if (transactionModel.getAdmin_commission_from_lender() != null && transactionModel.getAdmin_commission_from_lender().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_lender());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        holder.afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                } else if (transactionModel.getRequestBy().equals("1")) {
                    if (transactionModel.getAdmin_commission_from_borrower() != null && transactionModel.getAdmin_commission_from_borrower().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_borrower());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        holder.afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                }
            }
        } else if (context.getString(R.string.default_transaction).equalsIgnoreCase(moveFrom)) {
            if (transactionModel.getAdmin_commission_from_borrower() != null && transactionModel.getAdmin_commission_from_borrower().length() > 0) {
                float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_borrower());
                float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                float amount = totalAmount - commission;
                holder.afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
            }
        }

        if (transactionModel.getIs_negotiate_after_accept() != null && transactionModel.getIs_negotiate_after_accept().length() > 0) {
            if (transactionModel.getIs_negotiate_after_accept().equals("2")) {
                holder.commissionLL.setVisibility(View.INVISIBLE);
                holder.afterZapayCommissionLL.setVisibility(View.GONE);
            } else {
                holder.commissionLL.setVisibility(View.VISIBLE);
                holder.afterZapayCommissionLL.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return allTransactionArrayList.size();
    }

    private void setAdapterFunc(RecyclerView paybackDateRecycler, List<DateModel> payDatesList, TransactionModel transactionModel) {
        ViewAllHistoryAndTransactionPaybackDateAdapter paybackDateAdapter = new ViewAllHistoryAndTransactionPaybackDateAdapter(context, payDatesList, transactionModel);
        paybackDateRecycler.setAdapter(paybackDateAdapter);
    }
}
