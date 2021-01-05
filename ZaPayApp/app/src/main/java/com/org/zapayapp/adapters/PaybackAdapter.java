package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.activity.LendBorrowActivity;
import com.org.zapayapp.model.PabackModel;
import com.org.zapayapp.utils.DateFormat;

import java.util.List;

public class PaybackAdapter extends RecyclerView.Adapter<PaybackAdapter.MyHolder> {
    private Context context;
    private List<PabackModel> indicatorList;

    public PaybackAdapter(Context context, List<PabackModel> indicatorList) {
        this.context = context;
        this.indicatorList = indicatorList;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private TextView paybackTxtDateNo, paybackTxtDate;
        private LinearLayout paybackTxtSelect;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            paybackTxtDateNo = itemView.findViewById(R.id.paybackTxtDateNo);
            paybackTxtDate = itemView.findViewById(R.id.paybackTxtDate);
            paybackTxtSelect = itemView.findViewById(R.id.paybackTxtSelect);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payback_list, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        PabackModel model = indicatorList.get(position);
        if (position == 0) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.first_select_date));
        } else if (position == 1) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.second_select_date));
        } else if (position == 2) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.third_select_date));
        } else if (position == 3) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.fourth_select_date));
        } else if (position == 4) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.fifth_select_date));
        } else if (position == 5) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.sixth_select_date));
        } else if (position == 6) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.seventh_select_date));
        } else if (position == 7) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.eighth_select_date));
        } else if (position == 8) {
            holder.paybackTxtDateNo.setText(context.getString(R.string.ninth_select_date));
        }

        if (model != null && model.getPayDate().length() > 0) {
           /* if (model.isDateEpockFormate()) {
                try {
                    holder.paybackTxtDate.setText(DateFormat.getDateFromEpoch(model.getPayDate()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder.paybackTxtDate.setText(model.getPayDate());
            }*/
            holder.paybackTxtDate.setText(DateFormat.dateFormatConvert(model.getPayDate()));
        }

        holder.paybackTxtSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof LendBorrowActivity) {
                    ((LendBorrowActivity) context).selectPaybackDate(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return indicatorList.size();
    }
}
