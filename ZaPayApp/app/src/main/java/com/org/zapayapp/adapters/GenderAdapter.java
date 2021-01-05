package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.org.zapayapp.R;
import com.org.zapayapp.model.CityModel;
import java.util.List;

public class GenderAdapter extends BaseAdapter {
    private List<String> genderList;
    private Context context;
    private LayoutInflater inflater;

    public GenderAdapter(Context context, List<String> genderList) {
        this.context = context;
        this.genderList = genderList;
    }

    @Override
    public int getCount() {
        return genderList.size();
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


        String gender = genderList.get(position);
        if (gender != null && gender.length() > 0) {
            String categoryName = gender;
            String catName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
            holder.tvname.setText(catName);
        }

        return vi;
    }

    class ViewHolder {
        private TextView tvname;
    }

}

