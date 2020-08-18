package com.org.zapayapp.activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.TimeStamp;
import com.org.zapayapp.webservices.APICallback;
import java.util.HashMap;
import retrofit2.Call;

public class LendingSummaryActivity extends BaseActivity implements APICallback, SimpleAlertFragment.AlertSimpleCallback {
    private TextView nameTV;
    private TextView amountTV;
    private TextView termTV;
    private TextView noOfPaymentTV;
    private TextView paymentDateTV;
    private TextView totalReceivedBackTV;
    private TextView viewAllTV;

    private TextView negotiateTV;
    private TextView acceptTV;
    private TextView declineTV;
    private  String transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lending_summary);
        inIt();
        inItAction();
    }

    private void inIt(){
        nameTV=findViewById(R.id.nameTV);
        amountTV=findViewById(R.id.amountTV);
        termTV=findViewById(R.id.termTV);
        noOfPaymentTV=findViewById(R.id.noOfPaymentTV);
        paymentDateTV=findViewById(R.id.paymentDateTV);
        totalReceivedBackTV=findViewById(R.id.totalReceivedBackTV);
        viewAllTV=findViewById(R.id.viewAllTV);

        negotiateTV=findViewById(R.id.negotiateTV);
        acceptTV=findViewById(R.id.acceptTV);
        declineTV=findViewById(R.id.declineTV);

        if (getIntent().getStringExtra("transactionId")!=null){
            transactionId=getIntent().getStringExtra("transactionId");
            callAPIGetTransactionRequestDetail(transactionId);
        }
    }

    private void inItAction(){
        negotiateTV.setOnClickListener(v -> {
       /*    Intent intent = new Intent(LendingSummaryActivity.this, LendBorrowActivity.class);
            intent.putExtra("isBorrow", false);
            startActivity(intent);*/
            callAPIUpdateTransactionRequestStatus("1");

        });

        acceptTV.setOnClickListener(v -> {
            callAPIUpdateTransactionRequestStatus("2");
        });

        declineTV.setOnClickListener(v -> {
            callAPIUpdateTransactionRequestStatus("3");
        });


    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }




    private void callAPIGetTransactionRequestDetail(String transaction_request_id) {
        String token= SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_request_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token,getString(R.string.api_get_transaction_request_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_request_details), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void callAPIUpdateTransactionRequestStatus(String status) {
        String token= SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId,
                "status", status);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token,getString(R.string.api_update_transaction_request_status), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_transaction_request_status), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        Log.e("json", "json======" + json);
        if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
                if (status==200){
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }else {
                    showSimpleAlert(msg, "");
                }
            }else if (from.equals(getResources().getString(R.string.api_get_transaction_request_details))){
                if (status==200){
                    if (json.get("data").getAsJsonObject()!=null){
                        setaData(json.get("data").getAsJsonObject());
                    }
                }else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }

            }

        }
    }

    private void setaData(JsonObject jsonObject){
        /*String id=jsonObject.get("id").getAsString();
        String from_id=jsonObject.get("from_id").getAsString();
        String to_id=jsonObject.get("to_id").getAsString();
        String amount=jsonObject.get("amount").getAsString();
        String total_amount=jsonObject.get("total_amount").getAsString();
        String status=jsonObject.get("status").getAsString();
        String terms_type=jsonObject.get("terms_type").getAsString();
        String terms_value=jsonObject.get("terms_value").getAsString();
        String no_of_payment=jsonObject.get("no_of_payment").getAsString();
        String pay_date=jsonObject.get("pay_date").getAsString();
        String updated_by=jsonObject.get("updated_by").getAsString();
        String request_by=jsonObject.get("request_by").getAsString();
        String created_at=jsonObject.get("created_at").getAsString();
        String updated_at=jsonObject.get("updated_at").getAsString();
        String first_name=jsonObject.get("first_name").getAsString();
        String last_name=jsonObject.get("last_name").getAsString();

        nameTV.setText(first_name+" "+ last_name);
        amountTV.setText("$"+amount);

        noOfPaymentTV.setText(no_of_payment);
        paymentDateTV.setText(TimeStamp.timeFun(created_at));

        totalReceivedBackTV.setText("$"+total_amount);

        if (terms_type.equalsIgnoreCase("1")) {
            termTV.setText(terms_value+" "+getString(R.string.percent));
        } else if (terms_type.equalsIgnoreCase("2")) {
            termTV.setText(terms_value+" "+getString(R.string.fee));
        } else if (terms_type.equalsIgnoreCase("3")) {
            termTV.setText(terms_value+" "+getString(R.string.discount));
        } else if (terms_type.equalsIgnoreCase("4")) {
            termTV.setText(terms_value+" "+getString(R.string.none));
        }*/





        if (jsonObject.get("first_name").getAsString()!=null&&jsonObject.get("first_name").getAsString().length()>0&&jsonObject.get("first_name").getAsString()!=null&&jsonObject.get("first_name").getAsString().length()>0){
            String first_name=jsonObject.get("first_name").getAsString();
            String last_name=jsonObject.get("last_name").getAsString();
            nameTV.setText(first_name+" "+ last_name);
        }

        if (jsonObject.get("amount").getAsString()!=null&&jsonObject.get("amount").getAsString().length()>0){
            String amount=jsonObject.get("amount").getAsString();
            amountTV.setText("$"+amount);
        }

        if (jsonObject.get("no_of_payment").getAsString()!=null&&jsonObject.get("no_of_payment").getAsString().length()>0){
            String no_of_payment=jsonObject.get("no_of_payment").getAsString();
            noOfPaymentTV.setText(no_of_payment);
        }

        if (jsonObject.get("created_at").getAsString()!=null&&jsonObject.get("created_at").getAsString().length()>0){
            String created_at=jsonObject.get("created_at").getAsString();
            paymentDateTV.setText(TimeStamp.timeFun(created_at));
        }

        if (jsonObject.get("total_amount").getAsString()!=null&&jsonObject.get("total_amount").getAsString().length()>0){
            String total_amount=jsonObject.get("total_amount").getAsString();
            totalReceivedBackTV.setText("$"+total_amount);
        }

        if (jsonObject.get("terms_type").getAsString()!=null&&jsonObject.get("terms_type").getAsString().length()>0&&jsonObject.get("terms_value").getAsString()!=null&&jsonObject.get("terms_value").getAsString().length()>0){
            String terms_type=jsonObject.get("terms_type").getAsString();
            String terms_value=jsonObject.get("terms_value").getAsString();
            if (terms_type.equalsIgnoreCase("1")) {
                termTV.setText(terms_value+" "+getString(R.string.percent));
            } else if (terms_type.equalsIgnoreCase("2")) {
                termTV.setText(terms_value+" "+getString(R.string.fee));
            } else if (terms_type.equalsIgnoreCase("3")) {
                termTV.setText(terms_value+" "+getString(R.string.discount));
            } else if (terms_type.equalsIgnoreCase("4")) {
                termTV.setText(terms_value+" "+getString(R.string.none));
            }
        }
    }

    public void showSimpleAlert(String message, String from) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("header", message);
            args.putString("textOk", getString(R.string.ok));
            args.putString("textCancel", getString(R.string.cancel));
            args.putString("from", from);
            SimpleAlertFragment alert = new SimpleAlertFragment();
            alert.setArguments(args);
            alert.show(fm, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSimpleCallback(String from) {
        if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
            finish();
        }
    }
}
