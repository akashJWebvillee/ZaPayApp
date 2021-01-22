package com.org.zapayapp.adapters;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.BorrowSummaryActivity;
import com.org.zapayapp.activity.ViewAllSummaryActivity;
import com.org.zapayapp.model.AmendmentPdfDetailModel;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.MyDateUpdateDialog;

import java.util.ArrayList;
import java.util.List;

public class PaybackDateAdapter extends RecyclerView.Adapter<PaybackDateAdapter.MyHolder> {
    private Context context;
    private ArrayList<DateModel> dateModelArrayList;
    private String moveFrom;
    private String requestBy;
    private ViewAllSummaryActivity activity;

    public PaybackDateAdapter(Context context, ArrayList<DateModel> dateModelArrayList,String moveFrom,String requestBy) {
        this.context = context;
        this.dateModelArrayList = dateModelArrayList;
        this.moveFrom = moveFrom;
        this.requestBy = requestBy;
        activity= (ViewAllSummaryActivity) context;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView paymentNoTV, dateTV;
        private RelativeLayout editDateRL;
        private LinearLayout amendmentLL;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            paymentNoTV = itemView.findViewById(R.id.paymentNoTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            editDateRL = itemView.findViewById(R.id.editDateRL);
            amendmentLL = itemView.findViewById(R.id.amendmentLL);
        }
    }

    @NonNull
    @Override
    public PaybackDateAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payback_date_list, parent, false);
        return new PaybackDateAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaybackDateAdapter.MyHolder holder, final int position) {
        holder.paymentNoTV.setText(String.valueOf(position + 1));
        if (dateModelArrayList.get(position).getPayDate() != null && dateModelArrayList.get(position).getPayDate().length() > 0) {
            holder.dateTV.setText(DateFormat.dateFormatConvert(dateModelArrayList.get(position).getPayDate()));
        }

        if (dateModelArrayList.get(position).getAmendmentPdfDetails()!=null&&dateModelArrayList.get(position).getAmendmentPdfDetails().size()>0){
            holder.amendmentLL.setVisibility(View.VISIBLE);
            List<AmendmentPdfDetailModel> amendmentPdfDetailModelList= dateModelArrayList.get(position).getAmendmentPdfDetails();
            AmendmentPdfDetailModel amendmentPdfDetailModel=amendmentPdfDetailModelList.get(0);
              if (amendmentPdfDetailModel!=null&&amendmentPdfDetailModel.getPdfUrl()!=null&&amendmentPdfDetailModel.getPdfUrl().length()>0){
                  holder.amendmentLL.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          redirectAmendmentForm(amendmentPdfDetailModel.getPdfUrl());
                      }
                  });
              }
        }else {
            holder.amendmentLL.setVisibility(View.GONE);
        }


        if (moveFrom.equalsIgnoreCase(context.getString(R.string.history))){
            if (dateModelArrayList.get(position).isEditable()){
                if (requestBy.equalsIgnoreCase("2")){
                    //holder.editDateRL.setVisibility(View.VISIBLE);
                    if (dateModelArrayList.get(position).getStatus() != null && dateModelArrayList.get(position).getStatus().length() > 0) {
                        if (dateModelArrayList.get(position).getStatus().equals("remaining")) {
                            if (dateModelArrayList.get(position).isLatestRemaining()&&dateModelArrayList.get(position).isEditable()){
                                holder.editDateRL.setVisibility(View.VISIBLE);
                            }else{
                                holder.editDateRL.setVisibility(View.GONE);
                            }

                            if (dateModelArrayList.get(position).getNew_pay_date_status()!=null&&dateModelArrayList.get(position).getNew_pay_date_status().equalsIgnoreCase("2")){ //2=date request accepted
                                holder.editDateRL.setVisibility(View.GONE);
                            }

                        } else if (dateModelArrayList.get(position).getStatus().equals("processed")) {
                            holder.editDateRL.setVisibility(View.GONE);
                        } else if (dateModelArrayList.get(position).getStatus().equals("pending")) {
                            holder.editDateRL.setVisibility(View.GONE);
                        } else if (dateModelArrayList.get(position).getStatus().equals("cancelled")) {
                            holder.editDateRL.setVisibility(View.GONE);
                        } else if (dateModelArrayList.get(position).getStatus().equals("failed")) {
                            holder.editDateRL.setVisibility(View.VISIBLE);
                        }
                    }


                }else if (requestBy.equalsIgnoreCase("1")){
                    holder.editDateRL.setVisibility(View.GONE);
                }
            }else {
                holder.editDateRL.setVisibility(View.GONE);
            }


        } else {
            if (dateModelArrayList.get(position).isEditable()){
                if (requestBy.equalsIgnoreCase("1")){
                    //holder.editDateRL.setVisibility(View.VISIBLE);
                    if (dateModelArrayList.get(position).getStatus() != null && dateModelArrayList.get(position).getStatus().length() > 0) {
                        if (dateModelArrayList.get(position).getStatus().equals("remaining")) {
                            if (dateModelArrayList.get(position).isLatestRemaining()&&dateModelArrayList.get(position).isEditable()){
                                holder.editDateRL.setVisibility(View.VISIBLE);
                            }else{
                                holder.editDateRL.setVisibility(View.GONE);
                            }

                            if (dateModelArrayList.get(position).getNew_pay_date_status()!=null&&dateModelArrayList.get(position).getNew_pay_date_status().equalsIgnoreCase("2")){ //2=date request accepted
                                holder.editDateRL.setVisibility(View.GONE);
                            }

                        } else if (dateModelArrayList.get(position).getStatus().equals("processed")) {
                            holder.editDateRL.setVisibility(View.GONE);
                        } else if (dateModelArrayList.get(position).getStatus().equals("pending")) {
                            holder.editDateRL.setVisibility(View.GONE);
                        } else if (dateModelArrayList.get(position).getStatus().equals("cancelled")) {
                            holder.editDateRL.setVisibility(View.GONE);
                        } else if (dateModelArrayList.get(position).getStatus().equals("failed")) {
                            holder.editDateRL.setVisibility(View.VISIBLE);
                        }
                    }


                }else if (requestBy.equalsIgnoreCase("2")){
                    holder.editDateRL.setVisibility(View.GONE);

                }
            }else {
                holder.editDateRL.setVisibility(View.GONE);
            }






            //status- remaining, processed, pending, cancelled, failed
           /* if (dateModelArrayList.get(position).getStatus() != null && dateModelArrayList.get(position).getStatus().length() > 0) {
                if (dateModelArrayList.get(position).getStatus().equals("remaining")) {
                    if (dateModelArrayList.get(position).isLatestRemaining()&&dateModelArrayList.get(position).isEditable()){
                        holder.editDateRL.setVisibility(View.VISIBLE);
                    }else{
                        holder.editDateRL.setVisibility(View.GONE);
                    }

                } else if (dateModelArrayList.get(position).getStatus().equals("processed")) {
                    holder.editDateRL.setVisibility(View.GONE);
                } else if (dateModelArrayList.get(position).getStatus().equals("pending")) {
                    holder.editDateRL.setVisibility(View.GONE);
                } else if (dateModelArrayList.get(position).getStatus().equals("cancelled")) {
                    holder.editDateRL.setVisibility(View.GONE);
                } else if (dateModelArrayList.get(position).getStatus().equals("failed")) {
                    holder.editDateRL.setVisibility(View.VISIBLE);
                }
            }*/
        }

        holder.editDateRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ViewAllSummaryActivity) {
                   /* if (dateModelArrayList.get(position).getIs_extended() != null && dateModelArrayList.get(position).getIs_extended().equals("0")) {
                        ((ViewAllSummaryActivity) context).selectPaybackDate(position, dateModelArrayList.get(position));
                    } else {
                        ((ViewAllSummaryActivity) context).showSimpleAlert(context.getString(R.string.date_already_extended), "");
                    }*/

                    if (dateModelArrayList.get(position).getNew_pay_date_status() != null && dateModelArrayList.get(position).getNew_pay_date_status().equals("0")) {
                        ((ViewAllSummaryActivity) context).selectPaybackDate(position, dateModelArrayList.get(position));
                    } else {
                        ((ViewAllSummaryActivity) context).showSimpleAlert(context.getString(R.string.date_already_extended), "");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateModelArrayList.size();
    }

    private void redirectAmendmentForm(String pdfUrl){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(pdfUrl));
        context.startActivity(i);
    }
}

