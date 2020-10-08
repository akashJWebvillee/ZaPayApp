package com.org.zapayapp.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.org.zapayapp.R;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WVDateLib;
import com.org.zapayapp.webservices.APICalling;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatMessageModel> msgList;
    private WVDateLib wvDateLib;
    private String receiverProfileImg="";

    public ChatAdapter(Context context, List<ChatMessageModel> msgList) {
        this.context = context;
        this.msgList = msgList;
        this.wvDateLib = new WVDateLib(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChatMessageModel.SEND_MSG) {
            View view2 = LayoutInflater.from(context).inflate(R.layout.send_msg_row, parent, false);
            return new SendMessageHolder(view2);
        } else {
            View view1 = LayoutInflater.from(context).inflate(R.layout.received_msg_row, parent, false);
            return new ReceivedMessageHolder(view1);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            ChatMessageModel chatModel = msgList.get(position);
            if (holder instanceof ReceivedMessageHolder) {
                ReceivedMessageHolder receivedMessageHolder = ((ReceivedMessageHolder) holder);
                if(chatModel.getMessage() != null && chatModel.getMessage().length()>0) {
                    receivedMessageHolder.receiveMsgTV.setText(chatModel.getMessage());
                }
                if(chatModel.getCreatedAt() != 0 ) {
                    receivedMessageHolder.timeTextView.setText(wvDateLib.milliSecToDate(chatModel.getCreatedAt()));
                }
                if(receiverProfileImg != null && receiverProfileImg.length()>0){
                    Glide.with(context).load(APICalling.getImageUrl(receiverProfileImg)).placeholder(R.mipmap.ic_user).circleCrop().into(receivedMessageHolder.imageViewProfile);
                }
            } else if (holder instanceof SendMessageHolder) {
                SendMessageHolder sendMessageHolder =  ((SendMessageHolder) holder);
                sendMessageHolder.sendMsgTV.setText(chatModel.getMessage());
                sendMessageHolder.timeMyTextView.setText(wvDateLib.milliSecToDate(chatModel.getCreatedAt()));
                if (chatModel.getStatus().equals("0")) {
                    sendMessageHolder.seenTextView.setText(context.getString(R.string.chat_send));
                } else if (chatModel.getStatus().equals("1")) {
                    sendMessageHolder.seenTextView.setText(context.getString(R.string.chat_received));
                } else if (chatModel.getStatus().equals("2")) {
                    sendMessageHolder.seenTextView.setText(context.getString(R.string.chat_seen));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (msgList != null) {
            ChatMessageModel chatModel = msgList.get(position);
            if (chatModel != null ) {
                if(chatModel.getSenderId().equals(SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID))) {
                    return ChatMessageModel.SEND_MSG;
                }else if(chatModel.getReceiverId().equals(SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID))) {
                    return ChatMessageModel.RECEIVED_MSG;
                }
            }
        }
        return ChatMessageModel.RECEIVED_MSG;
    }


    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView receiveMsgTV,timeTextView;
        private CircularImageView imageViewProfile;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            receiveMsgTV = itemView.findViewById(R.id.receiveMsgTV);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
        }
    }


    private class SendMessageHolder extends RecyclerView.ViewHolder {
        private TextView sendMsgTV,seenTextView,timeMyTextView;

        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            sendMsgTV = itemView.findViewById(R.id.sendMsgTV);
            seenTextView = itemView.findViewById(R.id.seenTextView);
            timeMyTextView = itemView.findViewById(R.id.timeMyTextView);
        }
    }

    public void updateUserImg(String image){
        receiverProfileImg = image;
    }
}
