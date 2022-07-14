package com.org.zapayapp.adapters;
import static com.org.zapayapp.utils.Const.Var.DEFAULT_SHARE_MSG;

import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.org.zapayapp.R;
import com.org.zapayapp.listener.ContactListener;
import com.org.zapayapp.model.ContactModel;
import com.org.zapayapp.utils.Const;

import java.util.List;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyHolder> {
    private Context context;
    private int selectedPos = -1;
    private List<ContactModel> contactList;
    private ContactListener contactListener;
    private String forWhat="";

    public ContactAdapter(Context context, ContactListener contactListener, List<ContactModel> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListener = contactListener;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView contactTxtName,btnInvite;
        private ImageView contactImgSelect;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            contactTxtName = itemView.findViewById(R.id.contactTxtName);
            contactImgSelect = itemView.findViewById(R.id.contactImgSelect);
            btnInvite = itemView.findViewById(R.id.btnInvite);
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
        final ContactModel model = contactList.get(position);

        if (model.getMobileContactName() != null && model.getMobileContactName().length() > 0) {
            holder.contactTxtName.setText(model.getMobileContactName());
        }

        if (selectedPos == holder.getAdapterPosition()) {
            holder.contactImgSelect.setImageResource(R.mipmap.ic_check_select);
            holder.contactTxtName.setSelected(true);
        } else {
            holder.contactImgSelect.setImageResource(R.drawable.checkbox_unselect);
            holder.contactTxtName.setSelected(false);
        }
        if (model.getIsInvite()==0){
            holder.btnInvite.setVisibility(View.GONE);
            holder.contactImgSelect.setVisibility(View.VISIBLE);
        }else {
            holder.btnInvite.setVisibility(View.VISIBLE);
            holder.contactImgSelect.setVisibility(View.GONE);
        }
        if (forWhat.equalsIgnoreCase(context.getString(R.string.negotiation))){
            holder.itemView.setClickable(false);
        }else {
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getIsInvite() == 0) {
                        if (selectedPos != holder.getAdapterPosition()) {
                            selectedPos = holder.getAdapterPosition();
                            contactListener.getContact(contactList.get(position));
                            notifyDataSetChanged();
                        }else {
                            selectedPos =-1;
                            notifyDataSetChanged();
                        }
                    }
                }
            });

            holder.btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BranchUniversalObject object = new BranchUniversalObject();
                    object.setTitle("ZaPay");
                    object.setContentDescription(DEFAULT_SHARE_MSG);

                    LinkProperties linkProperties = new LinkProperties();

                    ShareSheetStyle shareSheetStyle = new ShareSheetStyle(context, "ZaPay", DEFAULT_SHARE_MSG)
                           .setAsFullWidthStyle(true)
                            .setStyleResourceID(R.style.bottomSheetStyleWrapper)
                            .setSharingTitle("Invite Friends");

                    object.showShareSheet((Activity) context, linkProperties, shareSheetStyle, new Branch.BranchLinkShareListener() {
                        @Override
                        public void onShareLinkDialogLaunched() {

                        }

                        @Override
                        public void onShareLinkDialogDismissed() {

                        }

                        @Override
                        public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {

                        }

                        @Override
                        public void onChannelSelected(String channelName) {

                        }
                    });

                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public String getSelected() {
        if (selectedPos != -1) {
            return contactList.get(selectedPos).getMobileContactName();
        }
        return null;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelected(int position,String forWhat) {
        this.forWhat=forWhat;
        selectedPos = position;
        notifyItemChanged(position);
    }
}
