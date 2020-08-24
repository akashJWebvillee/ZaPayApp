package com.org.zapayapp.chat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {
    private RecyclerView msgRecyclerView;
    private SwipeRefreshLayout msgSwipeLayout;

    private ChatAdapter chatAdapter;
    private List<ChatMessageModel> msgList;

    private ImageView sendImageView;
    private EditText messageEditText;

    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
         inIt();
         inItAction();
    }

    private void inIt() {
        toolbar = findViewById(R.id.customToolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        titleTV.setText(getString(R.string.chat));

        msgRecyclerView = findViewById(R.id.msgRecyclerView);
        msgSwipeLayout = findViewById(R.id.msgSwipeLayout);

        sendImageView = findViewById(R.id.sendImageView);
        messageEditText = findViewById(R.id.messageEditText);

        msgList=new ArrayList<>();
        msgList.add(new ChatMessageModel("hii",0));
        msgList.add(new ChatMessageModel("helo",1));
        msgList.add(new ChatMessageModel("whre are u",0));
        msgList.add(new ChatMessageModel("i am here and uu",1));
        msgList.add(new ChatMessageModel("hay ",0));
    }

    private void inItAction(){
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false));
        msgRecyclerView.setItemAnimator(new DefaultItemAnimator());
         setMsgAdapter();
        scrollRecyclerViewToBottom(msgRecyclerView);

        msgSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // callConversationsMsgListAPI();
                msgSwipeLayout.setRefreshing(false);
            }
        });

        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageEditText.getText().toString().trim().isEmpty()){
                    String msg=messageEditText.getText().toString().trim();
                    msgList.add(new ChatMessageModel(msg,1));
                    messageEditText.setText("");
                    setMsgAdapter();
                    scrollRecyclerViewToBottom(msgRecyclerView);

                }else {
                    Toast.makeText(ChatActivity.this, getString(R.string.type_message), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void setMsgAdapter() {
        // hide initial chat UI
     /*   if (msgList.size() > 0 || chatAdapter!=null) {
            layoutNoData.setVisibility(View.GONE);
        }
        try {
            Collections.sort(msgList, ChatMessageModel.timeComparator);
            if (chatAdapter != null) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter = new ChatAdapter(ChatActivity.this, msgList);
                msgRecyclerView.setAdapter(chatAdapter);
            }
            if (pageNo == 0) {
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
        } else {
            chatAdapter = new ChatAdapter(ChatActivity.this, msgList);
            msgRecyclerView.setAdapter(chatAdapter);
        }

    }

    private void scrollRecyclerViewToBottom(RecyclerView recyclerView) {
        if (chatAdapter != null && chatAdapter.getItemCount() > 0) {
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }
}
