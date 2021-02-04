package com.org.zapayapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.AcceptActivity;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.TransactionModel;

public class MyDateUpdateDialog {
     private DateStatusUpdateListener dateStatusUpdateListener;

    public void changeDateRequestDialogFunc(Context context, DateStatusUpdateListener dateStatusUpdateListener1, DateModel dateModel, TransactionModel transactionModel){
        dateStatusUpdateListener=dateStatusUpdateListener1;
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.activity_change_date_request_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView previousDateTV = dialog.findViewById(R.id.previousDateTV);
        TextView requestedDateTV = dialog.findViewById(R.id.requestedDateTV);
        TextView acceptTV = dialog.findViewById(R.id.acceptTV);
        TextView declineTV = dialog.findViewById(R.id.declineTV);
        ImageView dateCloseIV = dialog.findViewById(R.id.dateCloseIV);

        if (dateModel != null) {
            previousDateTV.setText(dateModel.getPayDate());
            requestedDateTV.setText(dateModel.getNew_pay_date());
        }

        dateCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AcceptActivity.class);
                intent.putExtra("moveFrom", "ChangeDateRequestDialogActivity");
                intent.putExtra("status", new Gson().toJson(dateModel));
                intent.putExtra("transactionModel", new Gson().toJson(transactionModel));
                //intent.putExtra("transactionId", dateModel.getTransactionRequestId());
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        declineTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStatusUpdateListener.dateStatusUpdateResponse("decline");
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public interface DateStatusUpdateListener{
        public void dateStatusUpdateResponse(String data);
    }
}
