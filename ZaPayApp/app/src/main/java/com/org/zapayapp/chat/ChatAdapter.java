package com.org.zapayapp.chat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.org.zapayapp.R;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessageModel> msgList;


    public ChatAdapter(Context context, List<ChatMessageModel> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*    switch (viewType){
                    case ChatMessageModel.RECEIVED_MSG:
                        View view1 = LayoutInflater.from(context).inflate(R.layout.received_msg_row, parent, false);
                        return new ReceivedMessageHolder(view1);

                        case ChatMessageModel.SEND_MSG:
                        View view2 = LayoutInflater.from(context).inflate(R.layout.send_msg_row, parent, false);
                        return new SendMessageHolder(view2);

            }*/



        if (viewType==ChatMessageModel.RECEIVED_MSG){
            View view1 = LayoutInflater.from(context).inflate(R.layout.received_msg_row, parent, false);
            return new ReceivedMessageHolder(view1);
        }else {
            View view2 = LayoutInflater.from(context).inflate(R.layout.send_msg_row, parent, false);
            return new SendMessageHolder(view2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageModel chatModel = msgList.get(position);
        if (holder instanceof ReceivedMessageHolder) {
            ((ReceivedMessageHolder) holder).receiveMsgTV.setText(chatModel.getMessage());

        }else  if (holder instanceof SendMessageHolder){
            ((SendMessageHolder) holder).sendMsgTV.setText(chatModel.getMessage());
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
            if (chatModel != null) {
                return chatModel.getMsgType();
            }
        }
        return 0;
    }


    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView receiveMsgTV;
        private TextView timeTextView;
        private TextView seenTextView;
        private RelativeLayout rlMyView, rlOtherUserView;
        private ImageView profile_image;
        private TextView msgMyTextView;
        private TextView timeMyTextView;


        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            receiveMsgTV = itemView.findViewById(R.id.receiveMsgTV);


        }
    }


    private class SendMessageHolder extends RecyclerView.ViewHolder {
        private TextView sendMsgTV;

        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            sendMsgTV=itemView.findViewById(R.id.sendMsgTV);
        }
    }
}
