package com.org.zapayapp.chat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.BaseActivity;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;

public class ChatActivity extends BaseActivity implements View.OnClickListener, APICallback {
    private String TAG = ChatActivity.class.getSimpleName();
    private RecyclerView msgRecyclerView;
    private SwipeRefreshLayout msgSwipeLayout;
    private ChatAdapter chatAdapter;
    private List<ChatMessageModel> msgList;
    private ImageView sendImageView;
    private EditText messageEditText;
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;
    private Intent intent;
    private String senderId = "", receiverId = "", transactionId = "",receiverProfileImg="";
    private int pageNo = 0;
    private TextView noDataTv;
    TransactionModel transactionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        inIt();
        inItAction();
        getIntentValues();
    }

    private void inIt() {
        toolbar = findViewById(R.id.customToolbar);
        noDataTv = findViewById(R.id.noDataTv);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.chat));

        msgRecyclerView = findViewById(R.id.msgRecyclerView);
        msgSwipeLayout = findViewById(R.id.msgSwipeLayout);

        sendImageView = findViewById(R.id.sendImageView);
        messageEditText = findViewById(R.id.messageEditText);
        msgList = new ArrayList<>();
    }

    private void inItAction() {
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false));
        msgRecyclerView.setItemAnimator(new DefaultItemAnimator());
        scrollRecyclerViewToBottom(msgRecyclerView);

        msgSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callConversationsMsgListAPI();
                msgSwipeLayout.setRefreshing(false);
            }
        });

        backArrowImageView.setOnClickListener(this::onClick);
        sendImageView.setOnClickListener(this::onClick);
    }

    private void getIntentValues() {
       /* intent = getIntent();
        if (intent.getSerializableExtra("transactionModel") != null) {
            TransactionModel transactionModel = (TransactionModel) intent.getSerializableExtra("transactionModel");
            if (transactionModel != null) {
                if (transactionModel.getFromId() != null && transactionModel.getFromId().length() > 0 && transactionModel.getFromId().equals(SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID))) {
                    senderId = transactionModel.getFromId();
                    receiverId = transactionModel.getToId();
                    transactionId = transactionModel.getId();
                    receiverProfileImg = transactionModel.getProfileImage();

                    if (Const.isRequestByMe(transactionModel.getFromId())) {
                        titleTV.setText(transactionModel.getReceiver_first_name() + " " + transactionModel.getReceiver_last_name());
                    } else {
                        titleTV.setText("" + transactionModel.getSender_first_name() + " " + transactionModel.getSender_last_name());
                    }

                } else if (transactionModel.getToId() != null && transactionModel.getToId().length() > 0 && transactionModel.getToId().equals(SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID))) {
                    senderId = transactionModel.getToId();
                    receiverId = transactionModel.getFromId();
                    transactionId = transactionModel.getId();
                    receiverProfileImg = transactionModel.getProfileImage();
                    if (Const.isRequestByMe(transactionModel.getFromId())) {
                        titleTV.setText(transactionModel.getReceiver_first_name() + " " + transactionModel.getReceiver_last_name());
                    } else {
                        titleTV.setText("" + transactionModel.getSender_first_name() + " " + transactionModel.getSender_last_name());
                    }
                }
                callConversationsMsgListAPI();
            }
        }*/

        if (getIntent().getStringExtra("transaction_id")!=null&&getIntent().getStringExtra("transaction_id").length()>0){
            transactionId=getIntent().getStringExtra("transaction_id");
            callAPIGetTransactionRequestDetail(transactionId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendImageView:
                if (!messageEditText.getText().toString().trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        //receiver_id,sender_id,message,transaction_request_id
                        jsonObject.put("receiver_id", receiverId);
                        jsonObject.put("sender_id", senderId);
                        jsonObject.put("message", messageEditText.getText().toString());
                        jsonObject.put("transaction_request_id", transactionId);
                        jsonObject.put("profile_image", receiverProfileImg);
                        CommonMethods.showLogs("jsonobject ", "" + jsonObject);
                        mSocket.emit("send_msg", jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ChatActivity.this, getString(R.string.type_message), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backArrowImageView:
                finish();
                break;
        }
    }

    private void setMsgAdapter() {
        // hide initial chat UI
      /*  if (msgList.size() > 0 || chatAdapter!=null) {
            layoutNoData.setVisibility(View.GONE);
        }*/
        try {
            if (chatAdapter != null) {
                //msgList.addAll(0, list);
                Collections.sort(msgList, ChatMessageModel.timeComparator);
                chatAdapter.notifyDataSetChanged();
            } else {
               // msgList.addAll(list);
                Collections.sort(msgList, ChatMessageModel.timeComparator);
                chatAdapter = new ChatAdapter(this, msgList);
                msgRecyclerView.setAdapter(chatAdapter);
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
            }

            if(chatAdapter != null){
                chatAdapter.updateUserImg(receiverProfileImg);
            }
          /*  if (chatAdapter != null) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter = new ChatAdapter(ChatActivity.this, msgList);
                msgRecyclerView.setAdapter(chatAdapter);
            }*/
           // if (pageNo == 0) {
                //msgRecyclerView.scrollToPosition(msgList.size() - 1);
                scrollRecyclerViewToBottom(msgRecyclerView);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
        } else {
            chatAdapter = new ChatAdapter(ChatActivity.this, msgList);
            msgRecyclerView.setAdapter(chatAdapter);
        }*/


      if (msgList!=null&&msgList.size()>0){
          noDataTv.setVisibility(View.GONE);
      }else {
          noDataTv.setVisibility(View.VISIBLE);
      }
    }

    private void scrollRecyclerViewToBottom(RecyclerView recyclerView) {
        if (chatAdapter != null && chatAdapter.getItemCount() > 0) {
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callEventReadAll();
    }

    private void callAPIGetTransactionRequestDetail(String transaction_request_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_request_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_request_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_request_details), titleTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callConversationsMsgListAPI() {
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "transaction_request_id", transactionId,
                    "page", pageNo);
            if (pageNo > 0) {
                apiCalling.setRunInBackground(true);
            }
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString(), getString(R.string.api_get_chat_history), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_chat_history), sendImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        Log.e("json","chat json==="+json);
        if (from != null) {
            int status = json.get("status").getAsInt();
            String msg = json.get("message").getAsString();
            if (from.equals(getString(R.string.api_get_chat_history))) {
                if (status == 200) {
                    if (msgList != null && pageNo == 0)
                        msgList.clear();
                    List<ChatMessageModel> chattingListModels = apiCalling.getDataList(json, "data", ChatMessageModel.class);
                    if (chattingListModels.size() > 0) {
                        noDataTv.setVisibility(View.GONE);
                        msgList.addAll(chattingListModels);
                        setMsgAdapter();
                        pageNo = pageNo + 1;
                    } else {
                        //  if (pageNo == 0)
                        //showViewsForNoData();
                        // if (chatAdapter != null)
                        noDataTv.setVisibility(View.VISIBLE);
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else if (status==201){
                    if (msgList.size()>0) noDataTv.setVisibility(View.GONE);
                    else noDataTv.setVisibility(View.VISIBLE);
                }else {
                    //showPopup(getString(R.string.something_wrong));
                }
            } else if (from.equals(getResources().getString(R.string.api_get_transaction_request_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        transactionModel = (TransactionModel) apiCalling.getDataObject(json.get("data").getAsJsonObject(), TransactionModel.class);
                        setUserDetail(transactionModel);
                    }
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }
            }
        }
    }

    private void setUserDetail(TransactionModel transactionModel){
        if (transactionModel != null) {
                if (transactionModel.getFromId() != null && transactionModel.getFromId().length() > 0 && transactionModel.getFromId().equals(SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID))) {
                    senderId = transactionModel.getFromId();
                    receiverId = transactionModel.getToId();
                    transactionId = transactionModel.getId();

                    if (Const.isRequestByMe(transactionModel.getFromId())) {
                        titleTV.setText(transactionModel.getReceiver_first_name() + " " + transactionModel.getReceiver_last_name());
                        receiverProfileImg = transactionModel.getSender_profile_image();
                    } else {
                        titleTV.setText("" + transactionModel.getSender_first_name() + " " + transactionModel.getSender_last_name());
                        receiverProfileImg = transactionModel.getReceiver_profile_image();
                    }

                } else if (transactionModel.getToId() != null && transactionModel.getToId().length() > 0 && transactionModel.getToId().equals(SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID))) {
                    senderId = transactionModel.getToId();
                    receiverId = transactionModel.getFromId();
                    transactionId = transactionModel.getId();
                    receiverProfileImg = transactionModel.getReceiver_profile_image();
                    if (Const.isRequestByMe(transactionModel.getFromId())) {
                        titleTV.setText(transactionModel.getReceiver_first_name() + " " + transactionModel.getReceiver_last_name());
                        receiverProfileImg = transactionModel.getSender_profile_image();
                    } else {
                        titleTV.setText("" + transactionModel.getSender_first_name() + " " + transactionModel.getSender_last_name());
                        receiverProfileImg = transactionModel.getReceiver_profile_image();
                    }
                }
                callConversationsMsgListAPI();
            }
    }

    //----------------------------------------------------------------------------//
    private void callEventReadAll() {
        JSONObject jsonObject = new JSONObject();
        try {
            //transaction_request_id,message_id,sender_id
            jsonObject.put("sender_id", receiverId);
           // jsonObject.put("receiver_id", AppPreferences.getUserId(this));
            jsonObject.put("transaction_request_id", transactionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("read_message", jsonObject.toString());
        CommonMethods.showLogs("ChatActivity", "Calling read event for read all messages !!!");

    }

    public void onMsgSentReceived(JSONObject jsonObject, boolean isReceive) {
        super.onMsgSentReceived(jsonObject,isReceive);
        Log.e("jsonObject","jsonObject chat==="+jsonObject);
        try {
        if (!isReceive) {
            messageEditText.setText("");
        }
        //{"status":200,"message":"success","data":{"receiver_id":"53","sender_id":"52","message":"Hi","transaction_request_id":"103","message_id":55}}
            CommonMethods.showLogs(TAG, "" + "onMsgSentReceived ()");

            JSONObject msg_data = null;
            String msg_receiver_id = "", msg_sender_id = "", msg = "", msg_id = "",transaction_request_id="",status="0";
            long created_at = 0;
            String msg_sender_img = "", msg_receiver_img = "";

            try {
                // if i send msg to another
                msg_data = jsonObject.getJSONObject("data");
                msg_receiver_id = msg_data.get("receiver_id").toString();  // 74
                msg_sender_id = msg_data.get("sender_id").toString(); //73
                msg = msg_data.get("message").toString(); //73
                msg_id = msg_data.get("message_id").toString(); //73
                transaction_request_id = msg_data.get("transaction_request_id").toString(); //73
                status = msg_data.getString("status").toString(); //73
                created_at = msg_data.getLong("created_at"); //73
                if (msg_sender_id.equals(senderId)) {
                    msg_receiver_img = msg_data.getString("profile_image").toString();//receiverImg;
                } else {
                    msg_receiver_img = msg_data.getString("profile_image").toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ChatMessageModel messageModel = new ChatMessageModel();
            messageModel.setSenderId(msg_sender_id);
            messageModel.setReceiverId(msg_receiver_id);
            messageModel.setCreatedAt(created_at);
            messageModel.setMessage(msg);
            messageModel.setTransactionRequestId(transaction_request_id);
            messageModel.setStatus(status);
            messageModel.setId(msg_id);
            messageModel.setSenderFirstName("");
            messageModel.setSenderLastName("");
            messageModel.setReceiverFirstName("");
            messageModel.setReceiverLastName("");
            messageModel.setSenderProfileImage(msg_receiver_img);
            messageModel.setReceiverProfileImage(msg_receiver_img);
            msgList.add(messageModel);

            boolean exists = msgList.contains(messageModel);
            CommonMethods.showLogs(TAG, exists + "  -----------------------------");
            setMsgAdapter();
           // scrollRecyclerViewToBottom(msgRecyclerView);
            if (isReceive) {
                callEventReadAll();
                CommonMethods.showLogs("ChatActivity", "Message received :- " + jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMsgReceivedAck(JSONObject jsonObject) {
        super.onMsgReceivedAck(jsonObject);
        try {
            //{"status":200,"message":"success","data":{"sender_id":"5e54a668db07c1478d627b59",
            // "message_id":"5e7dc9b3acffa9404943fcb1",
            // "conversation_id":"5e722b2cf5822302f55f9ef0"}}
            CommonMethods.showLogs(TAG , jsonObject.toString());
            JSONObject msg_data = null;
            String transaction_request_id = "", msg_sender_id = "", msg_id = "",status="0";
            try {
                // if i send msg to another
                msg_data = jsonObject.getJSONObject("data");
                transaction_request_id = msg_data.get("transaction_request_id").toString();  // 74
                msg_sender_id = msg_data.get("sender_id").toString(); //73
                msg_id = msg_data.get("message_id").toString(); //73
                status = msg_data.get("status").toString(); //73
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (transactionId.equals(transaction_request_id)) {
                for (int i = 0; i < msgList.size(); i++) {
                    ChatMessageModel model = msgList.get(i);
                    if (model.getId().equals(msg_id) && model.getStatus().equals("0")) {
                        model.setStatus(status);
                    }
                }
                if (chatAdapter != null) {
                    chatAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMsgReadAck(JSONObject jsonObject) {
        super.onMsgReadAck(jsonObject);
        try {
            CommonMethods.showLogs(TAG ," onMsgReadAck-------------" +  jsonObject.toString());
            JSONObject msg_data = null;
            String transaction_request_id = "", msg_sender_id = "", status="0";
            try {
                // if i send msg to another
                msg_data = jsonObject.getJSONObject("data");
                transaction_request_id = msg_data.get("transaction_request_id").toString();  // 74
                msg_sender_id = msg_data.get("sender_id").toString(); //73
                status = msg_data.get("status").toString(); //73
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (transactionId.equals(transaction_request_id)) {
                for (int i = 0; i < msgList.size(); i++) {
                    ChatMessageModel model = msgList.get(i);
                    model.setStatus(status);
                }
                if (chatAdapter != null) {
                    chatAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
