package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.org.zapayapp.R;

import java.util.List;

public class IncomeAdapter extends BaseAdapter {
    private List<String> incomeList;
    private Context context;
    private LayoutInflater inflater;

    public IncomeAdapter(Context context, List<String> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @Override
    public int getCount() {
        return incomeList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Below code is used to hide first item.
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowView(convertView, position, parent, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowView(convertView, position, parent, false);
    }

    private View rowView(View convertView, int position, ViewGroup parent, boolean isDropView) {
        View vi = convertView;             //trying to reuse a recycled view
        ViewHolder holder = null;
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (vi == null) {
            vi = mLayoutInflater.inflate(R.layout.item_spinner, parent, false);
            holder = new ViewHolder();
            holder.tvname = vi.findViewById(R.id.textSpinner);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        if (isDropView) {
            holder.tvname.setPadding(30, 20, 20, 20);
        } else {
            holder.tvname.setPadding(0, 20, 20, 20);
        }


        String income = incomeList.get(position);
        if (income != null && income.length() > 0) {
            String categoryName = income;
            String catName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
            holder.tvname.setText(catName);
        }

        return vi;
    }

    class ViewHolder {
        private TextView tvname;
    }

}
