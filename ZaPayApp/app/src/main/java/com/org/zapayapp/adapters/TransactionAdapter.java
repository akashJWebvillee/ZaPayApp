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
import com.org.zapayapp.activity.BorrowSummaryActivity;
import com.org.zapayapp.activity.LendingSummaryActivity;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.SharedPref;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyHolder> {
    private Context context;
    private String data;
    private List<TransactionModel> transactionModelsList;

    public TransactionAdapter(Context context, List<TransactionModel> transactionModelsList, String data) {
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
        private TextView acceptedReNegotiateTV;
        private TextView requestByTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            noOfPaymentTV = itemView.findViewById(R.id.noOfPaymentTV);
            termTypeTV = itemView.findViewById(R.id.termTypeTV);
            borrowModeTitleTV = itemView.findViewById(R.id.borrowModeTitleTV);
            dateUpdateIconIV = itemView.findViewById(R.id.dateUpdateIconIV);
            acceptedReNegotiateTV = itemView.findViewById(R.id.acceptedReNegotiateTV);
            requestByTV = itemView.findViewById(R.id.requestByTV);
        }
    }

    @NonNull
    @Override
    public TransactionAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_row, parent, false);
        return new TransactionAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyHolder holder, int position) {
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





        if (transactionModel.getCreatedAt() != null && transactionModel.getCreatedAt().length() > 0) {
            //holder.dateTV.setText(TimeStamp.timeFun(transactionModel.getCreatedAt()));
        }

        if (transactionModel.getIs_negotiate_after_accept() != null && transactionModel.getIs_negotiate_after_accept().length() > 0 && transactionModel.getIs_negotiate_after_accept().equals("2")) {
            holder.acceptedReNegotiateTV.setVisibility(View.VISIBLE);
        } else {
            holder.acceptedReNegotiateTV.setVisibility(View.GONE);
        }

        if (transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
            String pay_date = transactionModel.getPayDate();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                try {
                    //holder.dateTV.setText(DateFormat.getDateFromEpoch(date));
                    holder.dateTV.setText(DateFormat.dateFormatConvert(date));
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


        if (!Const.isRequestByMe(transactionModel.getFromId())){
            if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                holder.borrowModeTitleTV.setText(context.getString(R.string.lend_mode));
            } else if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                holder.borrowModeTitleTV.setText(context.getString(R.string.borrow_mode));
            }
        }else if (Const.isRequestByMe(transactionModel.getFromId())){
            if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                holder.borrowModeTitleTV.setText(context.getString(R.string.borrow_mode));
            } else if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                holder.borrowModeTitleTV.setText(context.getString(R.string.lend_mode));
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

        if (transactionModel.getStatus() != null && transactionModel.getStatus().length() > 0 && transactionModel.getStatus().equalsIgnoreCase("2")) { //accepted
            if (transactionModel.getPay_date_update_status_is_pending() != null && transactionModel.getPay_date_update_status_is_pending().length() > 0 && transactionModel.getPay_date_update_status_is_pending().equalsIgnoreCase("1")) {
                if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                    holder.dateUpdateIconIV.setVisibility(View.VISIBLE);
                } else {
                    holder.dateUpdateIconIV.setVisibility(View.GONE);
                }
            } else {
                holder.dateUpdateIconIV.setVisibility(View.GONE);
            }
        } else {
            holder.dateUpdateIconIV.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Const.isRequestByMe(transactionModel.getFromId())){
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        Intent intent = new Intent(context, BorrowSummaryActivity.class);
                        intent.putExtra("moveFrom", context.getString(R.string.transaction));
                        intent.putExtra("status", transactionModel.getStatus());
                        intent.putExtra("transactionId", transactionModel.getId());
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                    } else if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, LendingSummaryActivity.class);
                        intent.putExtra("moveFrom", context.getString(R.string.transaction));
                        intent.putExtra("status", transactionModel.getStatus());
                        intent.putExtra("transactionId", transactionModel.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                    }


                }else if (Const.isRequestByMe(transactionModel.getFromId())){

                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        Intent intent = new Intent(context, LendingSummaryActivity.class);
                        intent.putExtra("moveFrom", context.getString(R.string.history));
                        intent.putExtra("status", transactionModel.getStatus());
                        intent.putExtra("transactionId", transactionModel.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                    } else if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, BorrowSummaryActivity.class);
                        intent.putExtra("moveFrom", context.getString(R.string.history));
                        intent.putExtra("status", transactionModel.getStatus());
                        intent.putExtra("transactionId", transactionModel.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
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
