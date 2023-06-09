package com.org.zapayapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.activity.BorrowSummaryActivity;
import com.org.zapayapp.activity.LendingSummaryActivity;
import com.org.zapayapp.dialogs.RattingDialogActivity;
import com.org.zapayapp.model.AgreementPdfDetailModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.uihelpers.CustomRatingBar;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TransactionCompletedAdapter extends RecyclerView.Adapter<TransactionCompletedAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<TransactionModel> transactionModelsList;

    public TransactionCompletedAdapter(Context context, List<TransactionModel> transactionModelsList, String data) {
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
        private LinearLayout rattingLL;
        private CustomRatingBar viewRatingBar;
        private TextView requestByTV;
        private TextView transactionIdTV;
        private TextView agreementIdTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            noOfPaymentTV = itemView.findViewById(R.id.noOfPaymentTV);
            termTypeTV = itemView.findViewById(R.id.termTypeTV);
            borrowModeTitleTV = itemView.findViewById(R.id.borrowModeTitleTV);
            viewRatingBar = itemView.findViewById(R.id.viewRatingBar);
            viewRatingBar.setFocusableInTouchMode(false);
            rattingLL = itemView.findViewById(R.id.rattingLL);
            requestByTV = itemView.findViewById(R.id.requestByTV);
            transactionIdTV = itemView.findViewById(R.id.transactionIdTV);
            agreementIdTV = itemView.findViewById(R.id.agreementIdTV);
        }
    }

    @NonNull
    @Override
    public TransactionCompletedAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_completed_row, parent, false);
        return new TransactionCompletedAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionCompletedAdapter.MyHolder holder, int position) {
        TransactionModel transactionModel = transactionModelsList.get(position);

        if (transactionModel.getFromId() != null && transactionModel.getFromId().length() > 0) {
            if (Const.isRequestByMe(transactionModel.getFromId())) {
                if (transactionModel.getReceiver_first_name() != null && transactionModel.getReceiver_first_name().length() > 0 && transactionModel.getReceiver_last_name() != null && transactionModel.getReceiver_last_name().length() > 0) {
                    holder.nameTV.setText(transactionModel.getReceiver_first_name() + " " + transactionModel.getReceiver_last_name());
                    holder.requestByTV.setText(context.getString(R.string.self));

                }
            } else {
                if (transactionModel.getSender_first_name() != null && transactionModel.getSender_first_name().length() > 0 && transactionModel.getSender_last_name() != null && transactionModel.getSender_last_name().length() > 0) {
                    holder.nameTV.setText("" + transactionModel.getSender_first_name() + " " + transactionModel.getSender_last_name());
                    holder.requestByTV.setText(transactionModel.getSender_first_name());
                }
            }
        }

        /*if (transactionModel.getAverage_rating()!=null&&transactionModel.getAverage_rating().length()>0){
            holder.viewRatingBar.setScore(Float.parseFloat(transactionModel.getAverage_rating()));
        }


        if (transactionModel.getFromId() != null && transactionModel.getFromId().length() > 0) {
            if (Const.isRequestByMe(transactionModel.getFromId())) {
                if (transactionModel.getReceiver_average_rating()!=null&&transactionModel.getReceiver_average_rating().length()>0){
                    holder.viewRatingBar.setScore(Float.parseFloat(transactionModel.getReceiver_average_rating()));
                }
            } else {
                if (transactionModel.getSender_average_rating()!=null&&transactionModel.getSender_average_rating().length()>0){
                    holder.viewRatingBar.setScore(Float.parseFloat(transactionModel.getSender_average_rating()));
                }
            }
        }*/

        if (transactionModel.getRating_by_user() != null && transactionModel.getRating_by_user().length() > 0) {
            holder.viewRatingBar.setScore(Float.parseFloat(transactionModel.getRating_by_user()));
        }
        holder.viewRatingBar.setScrollToSelect(false);


        if (transactionModel.getCreatedAt() != null && transactionModel.getCreatedAt().length() > 0) {
            //holder.dateTV.setText(TimeStamp.timeFun(transactionModel.getCreatedAt()));
        }

        if (transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
            String pay_date = transactionModel.getPayDate();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                try {
                    // holder.dateTV.setText(DateFormat.getDateFromEpoch(date));
                    holder.dateTV.setText(date);
                } catch (Exception e) {
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
            holder.borrowModeTitleTV.setText(context.getString(R.string.lend_mode));
        } else if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
            holder.borrowModeTitleTV.setText(context.getString(R.string.borrow_mode));
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

        if (transactionModel.getAgreementPdfDetailModelList() != null && transactionModel.getAgreementPdfDetailModelList().size() > 0) {
            List<AgreementPdfDetailModel> AgreementPdfList = transactionModel.getAgreementPdfDetailModelList();
            AgreementPdfDetailModel pdfDetailModel = AgreementPdfList.get(0);
            holder.transactionIdTV.setText(pdfDetailModel.getTransactionRequestId());
            holder.agreementIdTV.setText(transactionModel.getAgreementId());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!transactionModel.getStatus().equalsIgnoreCase("2")) {
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        Intent intent = new Intent(context, BorrowSummaryActivity.class);
                        intent.putExtra("moveFrom", data);
                        intent.putExtra("status", transactionModel.getStatus());
                        intent.putExtra("transactionId", transactionModel.getId());
                        context.startActivity(intent);
                    } else if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, LendingSummaryActivity.class);
                        intent.putExtra("moveFrom", data);
                        intent.putExtra("status", transactionModel.getStatus());
                        intent.putExtra("transactionId", transactionModel.getId());
                        context.startActivity(intent);
                    }
                }
            }
        });

        holder.rattingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String averageRating = "";
                String is_already_rated = "";
                if (Const.isRequestByMe(transactionModel.getFromId())) {
                    averageRating = transactionModel.getReceiver_average_rating();
                    is_already_rated = transactionModel.getIs_already_rated();
                } else {
                    averageRating = transactionModel.getSender_average_rating();
                    is_already_rated = transactionModel.getIs_already_rated();
                }

                if (Const.isRequestByMe(transactionModel.getFromId())) {
                    if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                        if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                            Intent intent = new Intent(context, RattingDialogActivity.class);
                            intent.putExtra("requestBy", "2");
                            intent.putExtra("toId", transactionModel.getToId());
                            intent.putExtra("fromId", transactionModel.getFromId());
                            intent.putExtra("transactionRequestID", transactionModel.getId());
                            //intent.putExtra("averageRating", averageRating);
                            intent.putExtra("averageRating", transactionModel.getRating_by_user());
                            intent.putExtra("isAlreadyRated", is_already_rated);
                            context.startActivity(intent);
                        } else if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                            Intent intent = new Intent(context, RattingDialogActivity.class);
                            intent.putExtra("requestBy", "1");
                            intent.putExtra("toId", transactionModel.getToId());
                            intent.putExtra("fromId", transactionModel.getFromId());
                            intent.putExtra("transactionRequestID", transactionModel.getId());
                            // intent.putExtra("averageRating", averageRating);
                            intent.putExtra("averageRating", transactionModel.getRating_by_user());
                            intent.putExtra("isAlreadyRated", is_already_rated);
                            context.startActivity(intent);
                        }
                    }

                } else {
                    if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                        if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                            Intent intent = new Intent(context, RattingDialogActivity.class);
                            intent.putExtra("requestBy", "2");
                            intent.putExtra("toId", transactionModel.getFromId());
                            intent.putExtra("fromId", transactionModel.getToId());
                            intent.putExtra("transactionRequestID", transactionModel.getId());
                            intent.putExtra("averageRating", averageRating);
                            intent.putExtra("isAlreadyRated", is_already_rated);
                            context.startActivity(intent);
                        } else if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                            Intent intent = new Intent(context, RattingDialogActivity.class);
                            intent.putExtra("requestBy", "1");
                            intent.putExtra("toId", transactionModel.getFromId());
                            intent.putExtra("fromId", transactionModel.getToId());
                            intent.putExtra("transactionRequestID", transactionModel.getId());
                            intent.putExtra("averageRating", averageRating);
                            intent.putExtra("isAlreadyRated", is_already_rated);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionModelsList.size();
    }
}
