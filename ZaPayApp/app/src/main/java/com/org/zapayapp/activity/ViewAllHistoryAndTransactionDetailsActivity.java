package com.org.zapayapp.activity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.ViewAllHistoryAndTransactionDetailsAdapter;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;

public class ViewAllHistoryAndTransactionDetailsActivity extends BaseActivity implements View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback {
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;
    private RecyclerView historyDetailRecView;
    private ViewAllHistoryAndTransactionDetailsAdapter viewAllHistoryAndTransactionDetailsAdapter;
    private List<TransactionModel> allTransactionArrayList;
    private String moveFrom;
    private TextView totalPayTV;

    private String transactionID;
    private String payDateIds="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        inIt();
        inItAction();
        getIntentFunc();
    }

    private void inIt() {
        toolbar = findViewById(R.id.viewAllDetailToolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.all_details));
        historyDetailRecView = findViewById(R.id.historyDetailRecView);
        totalPayTV = findViewById(R.id.totalPayTV);
        totalPayTV.setOnClickListener(this);
        backArrowImageView.setOnClickListener(this);
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    private void getIntentFunc() {
        if (getIntent().getStringExtra("transactionId") != null && getIntent().getStringExtra("moveFrom") != null) {
            String transactionId = getIntent().getStringExtra("transactionId");
            moveFrom = getIntent().getStringExtra("moveFrom");

         /*   if (getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
                callAPIGetAllTransactionDetail(transactionId);
                totalPayTV.setVisibility(View.GONE);
            } else if (getString(R.string.history).equalsIgnoreCase(moveFrom)) {
                callAPIGetAllTransactionHistoryDetail(transactionId);
                totalPayTV.setVisibility(View.GONE);
            } else if (getString(R.string.default_transaction).equalsIgnoreCase(moveFrom)) {
                callAPIGetAllDefaultTransactionDetail(transactionId);
                totalPayTV.setVisibility(View.VISIBLE);
            }*/

            if (moveFrom.equalsIgnoreCase(getString(R.string.default_transaction))){
                totalPayTV.setVisibility(View.VISIBLE);
            }else {
                totalPayTV.setVisibility(View.GONE);
            }
            callAPIGetAllDefaultTransactionDetail(transactionId);
        }
    }

    private void inItAction() {

    }

    private void setAdapter() {
        historyDetailRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        viewAllHistoryAndTransactionDetailsAdapter = new ViewAllHistoryAndTransactionDetailsAdapter(this, allTransactionArrayList, gson, moveFrom);
        historyDetailRecView.setAdapter(viewAllHistoryAndTransactionDetailsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrowImageView:
                finish();
                break;

            case R.id.totalPayTV:
                if (transactionID != null && transactionID.length() > 0 && payDateIds != null && payDateIds.length() > 0) {
                    callAPIPayDefaultAmount(transactionID, payDateIds);
                }
                break;
        }
    }

    private void callAPIGetAllTransactionDetail(String transaction_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_all_transaction_request_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_all_transaction_request_details), titleTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetAllTransactionHistoryDetail(String transaction_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_all_transaction_history_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_all_transaction_history_details), titleTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetAllDefaultTransactionDetail(String transaction_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        Log.e("transaction_id","transaction_id===="+transaction_id);
        Log.e("transaction_id","transaction_id===="+token);

        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_default_transaction_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_default_transaction_details), titleTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIPayDefaultAmount(String transaction_id,String payDateIds) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_id,
                "pay_date_ids", payDateIds);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_pay_default_amount), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_pay_default_amount), titleTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (from.equals(getResources().getString(R.string.api_get_all_transaction_request_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonArray() != null && json.get("data").getAsJsonArray().size() > 0) {
                        allTransactionArrayList = apiCalling.getDataList(json, "data", TransactionModel.class);
                        if (allTransactionArrayList != null && allTransactionArrayList.size() > 0) {
                            setAdapter();
                        }
                    }
                } else {
                    showSimpleAlert(msg, "");
                }

            } else if (from.equals(getResources().getString(R.string.api_get_all_transaction_history_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonArray() != null && json.get("data").getAsJsonArray().size() > 0) {
                        allTransactionArrayList = apiCalling.getDataList(json, "data", TransactionModel.class);
                        if (allTransactionArrayList != null && allTransactionArrayList.size() > 0) {
                            setAdapter();
                        }
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_default_transaction_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonArray() != null && json.get("data").getAsJsonArray().size() > 0) {
                        allTransactionArrayList = apiCalling.getDataList(json, "data", TransactionModel.class);
                        if (allTransactionArrayList != null && allTransactionArrayList.size() > 0) {
                            setTotalPayData();
                            setAdapter();
                        }
                    }
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            }else if (from.equals(getResources().getString(R.string.api_pay_default_amount))){
                if (status == 200) {
                    finish();
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }

    public void setTotalPayData() {
        /*if (allTransactionArrayList.size() > 0) {
            totalPayTV.setText(getString(R.string.total_pay) + " " + Const.getCurrency() + allTransactionArrayList.get(0).getTotalAmount());
        }*/
    }

    public void setTotalPayData11(List<DateModel> payDatesList, TransactionModel transactionModel) {
    /*   if (!Const.isRequestByMe(transactionModel.getFromId())){
           if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("1")){
               totalPayTV.setVisibility(View.GONE);
           }else if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("2")){
               totalPayTV.setVisibility(View.VISIBLE);
           }
       }else if (Const.isRequestByMe(transactionModel.getFromId())){
           if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("2")){
               totalPayTV.setVisibility(View.VISIBLE);
           }else if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("1")){
               totalPayTV.setVisibility(View.GONE);
           }
        }
*/




    /*   if (Const.isRequestByMe(transactionModel.getFromId())){
            if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("2")){
                totalPayTV.setVisibility(View.VISIBLE);
            }else if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("1")){
                totalPayTV.setVisibility(View.GONE);
            }
        }else if(!Const.isRequestByMe(transactionModel.getFromId())){
           if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("1")){
               totalPayTV.setVisibility(View.VISIBLE);
           }else if (transactionModel.getRequestBy()!=null&&transactionModel.getRequestBy().equals("2")){
               totalPayTV.setVisibility(View.GONE);
           }
       }*/

        totalPayTV.setVisibility(View.GONE);
        if (Const.isRequestByMe(transactionModel.getFromId())&&transactionModel.getRequestBy().equals("2")){
            totalPayTV.setVisibility(View.VISIBLE);
        }else if (!Const.isRequestByMe(transactionModel.getFromId())&&transactionModel.getRequestBy().equals("1")){
            totalPayTV.setVisibility(View.VISIBLE);

        }else {
            totalPayTV.setVisibility(View.GONE);
        }

        transactionID = transactionModel.getId();
        float defaultTotalAmount = 0;
        if (payDatesList.size() > 0) {
            for (int i = 0; i < payDatesList.size(); i++) {
                if (payDatesList.get(i).getIs_default_txn() != null && payDatesList.get(i).getIs_default_txn().equals("1")) {
                    // float amount=payDatesList.get(i).getEmi_amount();
                    float defaultFeeAmount = Float.parseFloat(payDatesList.get(i).getDefault_fee_amount());
                    float amount = Float.parseFloat(payDatesList.get(i).getEmi_amount());
                    float defaultPayAmount = amount + defaultFeeAmount;

                    defaultTotalAmount = defaultTotalAmount + defaultPayAmount;
                    payDateIds = payDateIds + "," + payDatesList.get(i).getId();
                }

                if (payDatesList.get(i).getTransactionId()!=null&&payDatesList.get(i).getTransactionId().length()>0){
                    if (payDatesList.get(i).getStatus()!=null&&payDatesList.get(i).getStatus().length()>0&&payDatesList.get(i).getStatus().equals("pending")){
                        totalPayTV.setVisibility(View.GONE);
                    }else {
                        totalPayTV.setVisibility(View.VISIBLE);
                    }
                }
            }
            payDateIds = payDateIds.substring(1);
            totalPayTV.setText(getString(R.string.total_pay) + " " + Const.getCurrency() + defaultTotalAmount);
        }
    }
}