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
import com.org.zapayapp.utils.CommonMethods;

import java.util.List;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.MyHolder> {

    private Context context;
    private List<String> indicatorList;
    private int selectedPos = 0;

    public IndicatorAdapter(Context context, List<String> indicatorList) {
        this.context = context;
        this.indicatorList = indicatorList;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private TextView indicatorText, indicatorTextValue;
        private LinearLayout indicatorLinearView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            indicatorText = itemView.findViewById(R.id.indicatorText);
            indicatorTextValue = itemView.findViewById(R.id.indicatorTextValue);
            indicatorLinearView = itemView.findViewById(R.id.indicatorLinearView);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_indicator, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String model = indicatorList.get(position);
        if (selectedPos == holder.getAdapterPosition()) {
            holder.indicatorText.setVisibility(View.VISIBLE);
            holder.indicatorText.setSelected(true);
            holder.indicatorTextValue.setTextColor(CommonMethods.getColorWrapper(context, R.color.textColor));
            holder.indicatorLinearView.setBackground(CommonMethods.getDrawableWrapper(context, R.drawable.circle_shape_white));
        } else if (selectedPos > holder.getAdapterPosition()) {
            holder.indicatorText.setVisibility(View.GONE);
            holder.indicatorText.setSelected(false);
            holder.indicatorTextValue.setTextColor(CommonMethods.getColorWrapper(context, R.color.colorWhite));
            holder.indicatorLinearView.setBackground(CommonMethods.getDrawableWrapper(context, R.drawable.circle_shape_green));
        } else {
            holder.indicatorText.setVisibility(View.GONE);
            holder.indicatorText.setSelected(false);
            holder.indicatorTextValue.setTextColor(CommonMethods.getColorWrapper(context, R.color.textColor));
            holder.indicatorLinearView.setBackground(CommonMethods.getDrawableWrapper(context, R.drawable.circle_shape_alpha_white));
        }
        holder.indicatorText.setText(model);
        holder.indicatorTextValue.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return indicatorList.size();
    }

    public String getSelected() {
        if (selectedPos != -1) {
            return indicatorList.get(selectedPos);
        }
        return null;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelected(int position) {
        selectedPos = position;
        notifyDataSetChanged();
    }
}
