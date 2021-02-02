package com.org.zapayapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.activity.BaseActivity;

import java.util.List;

public class NavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> navList;
    private int TYPE_VIEW = 1;
    private int TYPE_OPTION = 2;
    private int lastCheckedPos = -1;

    public NavigationAdapter(Context context, List<String> navList) {
        this.context = context;
        this.navList = navList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 7) {
            return TYPE_VIEW;
        } else {
            return TYPE_OPTION;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private View navView;
        private TextView navTextName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            navView = itemView.findViewById(R.id.navView);
            navTextName = itemView.findViewById(R.id.navTextName);
        }
    }

    class MyHolderView extends RecyclerView.ViewHolder {

        private View navView;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            navView = itemView.findViewById(R.id.navView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_VIEW) {
            view = LayoutInflater.from(context).inflate(R.layout.view_navigation_seperate, parent, false);
            return new MyHolderView(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.view_navigation, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        String model = navList.get(position);
        if (holder instanceof MyHolder) {
            ((MyHolder) holder).navTextName.setText(model);

            if (lastCheckedPos == holder.getAdapterPosition()) {
                ((MyHolder) holder).navView.setVisibility(View.VISIBLE);
                ((MyHolder) holder).navTextName.setSelected(true);
            } else {
                ((MyHolder) holder).navView.setVisibility(View.INVISIBLE);
                ((MyHolder) holder).navTextName.setSelected(false);
            }

            ((MyHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastCheckedPos != holder.getAdapterPosition()) {
                        lastCheckedPos = holder.getAdapterPosition();
                        notifyDataSetChanged();
                        ((BaseActivity)context).onNavigationItemSelected(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return navList.size();
    }

    public String getSelected() {
        if (lastCheckedPos != -1) {
            return navList.get(lastCheckedPos);
        }
        return null;
    }

    public void setSelected(int position) {
        lastCheckedPos = position;
        notifyDataSetChanged();
    }
}
