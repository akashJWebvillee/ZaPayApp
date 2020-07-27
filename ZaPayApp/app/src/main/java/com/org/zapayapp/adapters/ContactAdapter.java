package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyHolder> {

    private Context context;
    private List<String> contactList;
    private int selectedPos = -1;

    public ContactAdapter(Context context, List<String> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private TextView contactTxtName;
        private ImageView contactImgSelect;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            contactTxtName = itemView.findViewById(R.id.contactTxtName);
            contactImgSelect = itemView.findViewById(R.id.contactImgSelect);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_list, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        String model = contactList.get(position);

        if (selectedPos == holder.getAdapterPosition()) {
            holder.contactImgSelect.setImageResource(R.mipmap.ic_check_select);
            holder.contactTxtName.setSelected(true);
        } else {
            holder.contactImgSelect.setImageResource(R.drawable.checkbox_unselect);
            holder.contactTxtName.setSelected(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPos != holder.getAdapterPosition()) {
                    selectedPos = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public String getSelected() {
        if (selectedPos != -1) {
            return contactList.get(selectedPos);
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
