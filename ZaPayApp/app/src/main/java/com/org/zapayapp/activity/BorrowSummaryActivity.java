package com.org.zapayapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.chat.ChatActivity;
import com.org.zapayapp.model.CommissionModel;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.AgreementPdfDetailModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.MyDateUpdateDialog;
import com.org.zapayapp.utils.MyDialog;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;

public class BorrowSummaryActivity extends BaseActivity implements APICallback, View.OnClickListener, MyDateUpdateDialog.DateStatusUpdateListener {
    private TextView nameTV, amountTV, termTV, noOfPaymentTV, paymentDateTV, totalReceivedBackTV, viewAllTV, negotiateTV, acceptTV, declineTV, commissionTitleTV, commissionValueTV;
    private ImageView chatTV;
    private String transactionId, moveFrom;
    private TransactionModel transactionModel;
    private String negotiationAcceptDeclineStatus = "";
    private Intent intent;
    private String status;
    private boolean isClickable = true;

    private String updated_by;
    private LinearLayout agreementFormLL;
    private AgreementPdfDetailModel agreementPdfDetailModel;
    private int changeDateRequestDialogActivityCODE = 300;
    private DateModel dateModel;
    private String request_by;
    private TextView afterCommissionTitleTV;
    private TextView afterCommissionAmountTV;
    private TextView vewAllDateTV;

    private ArrayList<String> payDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_summary);
        init();
        initAction();
        getIntentValues();
    }

    private void init() {
        payDateList=new ArrayList<>();
        nameTV = findViewById(R.id.nameTV);
        amountTV = findViewById(R.id.amountTV);
        termTV = findViewById(R.id.termTV);
        noOfPaymentTV = findViewById(R.id.noOfPaymentTV);
        paymentDateTV = findViewById(R.id.paymentDateTV);
        totalReceivedBackTV = findViewById(R.id.totalReceivedBackTV);
        viewAllTV = findViewById(R.id.viewAllTV);

        negotiateTV = findViewById(R.id.negotiateTV);
        acceptTV = findViewById(R.id.acceptTV);
        declineTV = findViewById(R.id.declineTV);
        chatTV = findViewById(R.id.chatTV);

        commissionTitleTV = findViewById(R.id.commissionTitleTV);
        commissionValueTV = findViewById(R.id.commissionValueTV);
        agreementFormLL = findViewById(R.id.agreementFormLL);

        afterCommissionTitleTV = findViewById(R.id.afterCommissionTitleTV);
        afterCommissionAmountTV = findViewById(R.id.afterCommissionAmountTV);
        vewAllDateTV = findViewById(R.id.vewAllDateTV);
    }

    private void initAction() {
        negotiateTV.setOnClickListener(this);
        acceptTV.setOnClickListener(this);
        declineTV.setOnClickListener(this);
        viewAllTV.setOnClickListener(this);
        chatTV.setOnClickListener(this);
        agreementFormLL.setOnClickListener(this);
        vewAllDateTV.setOnClickListener(this);
    }

    private void getIntentValues() {
        intent = getIntent();
        if (intent != null && intent.getStringExtra("transactionId") != null && intent.getStringExtra("moveFrom") != null) {
            transactionId = intent.getStringExtra("transactionId");
            moveFrom = intent.getStringExtra("moveFrom");
            status = intent.getStringExtra("status");
            //setDataStatusFunc();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClickable = true;
        setDataStatusFunc();
    }

    private void setDataStatusFunc() {
      /*  if (getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
            callAPIGetTransactionRequestDetail(transactionId);
        } else if (getString(R.string.history).equalsIgnoreCase(moveFrom)) {
            callAPIGetHistoryRequestDetail(transactionId);
        }*/
        callAPIGetTransactionRequestDetail(transactionId);
    }

    private void setHistoryButtonVisibleFunc(String status) {
        if (status.equalsIgnoreCase("0")) {
            negotiateTV.setVisibility(View.GONE);
            acceptTV.setVisibility(View.GONE);
            declineTV.setVisibility(View.VISIBLE);
        } else if (status.equalsIgnoreCase("1")) {
            if (updated_by != null && SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString().equalsIgnoreCase(updated_by)) {
                negotiateTV.setVisibility(View.GONE);
                acceptTV.setVisibility(View.GONE);
                declineTV.setVisibility(View.GONE);
            } else {
                negotiateTV.setVisibility(View.VISIBLE);
                acceptTV.setVisibility(View.VISIBLE);
                declineTV.setVisibility(View.VISIBLE);
            }

        } else {
            negotiateTV.setVisibility(View.GONE);
            acceptTV.setVisibility(View.GONE);
            declineTV.setVisibility(View.GONE);
        }
    }

    private void setTransactionButtonVisibleFunc(String status) {
        if (status.equalsIgnoreCase("0")) {   //0==pending
            negotiateTV.setVisibility(View.VISIBLE);
            acceptTV.setVisibility(View.VISIBLE);
            declineTV.setVisibility(View.VISIBLE);
        } else if (status.equalsIgnoreCase("1")) {
            if (updated_by != null && SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString().equalsIgnoreCase(updated_by)) {
                negotiateTV.setVisibility(View.GONE);
                acceptTV.setVisibility(View.GONE);
                declineTV.setVisibility(View.GONE);
            } else {
                negotiateTV.setVisibility(View.VISIBLE);
                acceptTV.setVisibility(View.VISIBLE);
                declineTV.setVisibility(View.VISIBLE);
            }
        } else {
            negotiateTV.setVisibility(View.GONE);
            acceptTV.setVisibility(View.GONE);
            declineTV.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negotiateTV:
                intent = new Intent(BorrowSummaryActivity.this, LendBorrowActivity.class);
                intent.putExtra("isBorrow", true);
                intent.putExtra("transactionModel", transactionModel);
                startActivity(intent);
                break;
            case R.id.acceptTV:
                intent = new Intent(BorrowSummaryActivity.this, AcceptActivity.class);
                intent.putExtra("moveFrom", moveFrom);
                intent.putExtra("status", "2");
                intent.putExtra("transactionModel", gson.toJson(transactionModel));
                startActivityForResult(intent, 200);
                break;

            case R.id.declineTV:
                if (transactionModel != null && transactionModel.getStatus().equals("2")) { //accept
                    callAPIUpdateRunningTransactionRequestStatus("3");
                } else {
                    negotiationAcceptDeclineStatus = "3";
                    callAPIUpdateTransactionRequestStatus("3");
                }
                break;

            case R.id.viewAllTV:
                if (isClickable) {
                    isClickable = false;
                    intent = new Intent(BorrowSummaryActivity.this, ViewAllSummaryActivity.class);
                    intent.putExtra("transactionId", transactionId);
                    intent.putExtra("moveFrom", moveFrom);
                    intent.putExtra("status", status);
                    intent.putExtra("requestBy", transactionModel.getRequestBy());
                    startActivityForResult(intent, 2);// Activity is started with requestCode 2
                }
                break;
            case R.id.chatTV:
                intent = new Intent(BorrowSummaryActivity.this, ChatActivity.class);
                //intent.putExtra("transactionModel", transactionModel);
                intent.putExtra("transaction_id", transactionModel.getId());
                startActivity(intent);
                break;

            case R.id.agreementFormLL:
                if (agreementPdfDetailModel != null) {
                 /*   String url = agreementPdfDetailModel.getPdfUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);*/

                    Intent intent = new Intent(BorrowSummaryActivity.this, PdfViewActivity.class);
                    intent.putExtra("pdf_url", agreementPdfDetailModel.getPdfUrl());
                    startActivity(intent);
                }
                break;

            case R.id.vewAllDateTV:
                MyDialog.viewAllDateFunc(BorrowSummaryActivity.this,payDateList);
                break;
        }
    }

    private void callAPIGetTransactionRequestDetail(String transaction_request_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_request_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_request_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_request_details), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetHistoryRequestDetail(String transaction_request_id) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject("transaction_request_id", transaction_request_id);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_history_details), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_history_details), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIUpdateTransactionRequestStatus(String status) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId,
                "status", status);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_update_transaction_request_status), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_transaction_request_status), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIUpdateRunningTransactionRequestStatus(String status) { //1=negotiate,2=accept,3=decline
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "parent_transaction_request_id", transactionModel.getParent_id(),
                "new_transaction_request_id", transactionModel.getId(),
                "status", status);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_update_running_transaction_request_status), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_running_transaction_request_status), acceptTV);
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

            if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
                if (status == 200) {
                    if (!negotiationAcceptDeclineStatus.equalsIgnoreCase("1")) {
                        showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                    } else {
                        intent = new Intent(BorrowSummaryActivity.this, LendBorrowActivity.class);
                        intent.putExtra("isBorrow", true);
                        intent.putExtra("transactionModel", transactionModel);
                        startActivity(intent);
                    }
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_transaction_request_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        transactionModel = (TransactionModel) apiCalling.getDataObject(json.get("data").getAsJsonObject(), TransactionModel.class);
                        setData(json.get("data").getAsJsonObject(), getString(R.string.transaction));
                    }
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }
            } else if (from.equals(getResources().getString(R.string.api_get_transaction_history_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        transactionModel = (TransactionModel) apiCalling.getDataObject(json.get("data").getAsJsonObject(), TransactionModel.class);
                        setData(json.get("data").getAsJsonObject(), getString(R.string.history));
                    }
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }
            } else if (from.equals(getResources().getString(R.string.api_pay_date_request_status_update))) {
                if (status == 200) {
                    showSimpleAlert(msg, getString(R.string.api_pay_date_request_status_update));
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_update_running_transaction_request_status))) {
                if (status == 200) {
                    showSimpleAlert(msg, getString(R.string.api_update_running_transaction_request_status));
                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }

    private void setData(JsonObject jsonObject, String forWhat) {
        if (transactionModel.getFromId() != null && transactionModel.getFromId().length() > 0) {
            if (Const.isRequestByMe(transactionModel.getFromId())) {
                if (transactionModel.getReceiver_first_name() != null && transactionModel.getReceiver_first_name().length() > 0 && transactionModel.getReceiver_last_name() != null && transactionModel.getReceiver_last_name().length() > 0) {
                  nameTV.setText(transactionModel.getReceiver_first_name() + " " + transactionModel.getReceiver_last_name());
                }
            } else {
                if (transactionModel.getSender_first_name() != null && transactionModel.getSender_first_name().length() > 0 && transactionModel.getSender_last_name() != null && transactionModel.getSender_last_name().length() > 0) {
                   nameTV.setText("" + transactionModel.getSender_first_name() + " " + transactionModel.getSender_last_name());
                }
            }
        }


        if (transactionModel.getAmount() != null && transactionModel.getAmount().length() > 0) {
            String amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + transactionModel.getAmount();
            amountTV.setText(amount);
        }
        if (transactionModel.getNoOfPayment() != null && transactionModel.getNoOfPayment().length() > 0) {
            noOfPaymentTV.setText(transactionModel.getNoOfPayment());
        }
        if (transactionModel.getCreatedAt() != null && transactionModel.getCreatedAt().length() > 0) {
            //paymentDateTV.setText(TimeStamp.timeFun(transactionModel.getCreatedAt()));
        }

        if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
            request_by = transactionModel.getRequestBy();
        }


        if (transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
            String pay_date = transactionModel.getPayDate();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                // paymentDateTV.setText(DateFormat.getDateFromEpoch(date));
                paymentDateTV.setText(DateFormat.dateFormatConvert(date));

                payDateList.clear();
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonOb = jsonArray.getJSONObject(i);
                    payDateList.add(jsonOb.getString("date"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (transactionModel.getTotalAmount() != null && transactionModel.getTotalAmount().length() > 0) {
            String total_amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + transactionModel.getTotalAmount();
            totalReceivedBackTV.setText(total_amount);

        }


        if (transactionModel.getTermsType() != null && transactionModel.getTermsType().length() > 0 && transactionModel.getTermsValue() != null && transactionModel.getTermsValue().length() > 0) {
            String terms_type = transactionModel.getTermsType();
            String terms_value = transactionModel.getTermsValue();

            if (terms_type.equalsIgnoreCase("1")) {
                terms_value = terms_value + " " + getString(R.string.percent);
                termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("2")) {
                terms_value = terms_value + " " + getString(R.string.fee);
                termTV.setText(Const.getCurrency() + terms_value);
            } else if (terms_type.equalsIgnoreCase("3")) {
                terms_value = terms_value + " " + getString(R.string.discount);
                termTV.setText(Const.getCurrency() + terms_value);
            } else if (terms_type.equalsIgnoreCase("4")) {
                //terms_value = terms_value + " " + getString(R.string.none);
                termTV.setText(getString(R.string.none));
            }
        }

        if (transactionModel.getUpdatedBy() != null && transactionModel.getUpdatedBy().length() > 0
                &&  transactionModel.getStatus() != null && transactionModel.getStatus().length() > 0) {
            updated_by = transactionModel.getUpdatedBy();
            status = transactionModel.getStatus();
            setButtonVisibility(forWhat);
        }

        if (transactionModel.getCommission_charges_detail() != null && transactionModel.getCommission_charges_detail().length() > 0) {
            String commission_charges_detail = transactionModel.getCommission_charges_detail();
            commission_charges_detail.replace("\\", "/");
            CommissionModel commissionModel = gson.fromJson(commission_charges_detail, CommissionModel.class);
            commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
            commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_borrower());
        }

        if (status.equalsIgnoreCase("2") || status.equalsIgnoreCase("4")) {
            agreementFormLL.setVisibility(View.VISIBLE);
        } else {
            agreementFormLL.setVisibility(View.GONE);
        }

        if (jsonObject.has("pdf_details") && jsonObject.get("pdf_details").getAsJsonArray() != null) {
            List<AgreementPdfDetailModel> pdf_details = apiCalling.getDataList(jsonObject, "pdf_details", AgreementPdfDetailModel.class);
            if (pdf_details.size() > 0)
                agreementPdfDetailModel = pdf_details.get(0);
        }

        if (jsonObject.has("pay_date_pending_request_details") && jsonObject.get("pay_date_pending_request_details").getAsJsonArray() != null) {
            List<DateModel> pdf_details = apiCalling.getDataList(jsonObject, "pay_date_pending_request_details", DateModel.class);
            if (pdf_details.size() > 0) {
                dateModel = pdf_details.get(0);


                if (status != null && status.length() > 0 && status.equalsIgnoreCase("2")) { //2=accepted
                    if (moveFrom.equalsIgnoreCase(getString(R.string.transaction))) {
                        if (request_by.equalsIgnoreCase("2")) {
                            if (dateModel != null && dateModel.getStatus().equalsIgnoreCase("remaining"))
                                new MyDateUpdateDialog().changeDateRequestDialogFunc(BorrowSummaryActivity.this, this, dateModel, transactionModel);
                        }
                    } else if (moveFrom.equalsIgnoreCase(getString(R.string.history))) {
                        if (request_by.equalsIgnoreCase("1")) {
                            if (dateModel != null && dateModel.getStatus().equalsIgnoreCase("remaining"))
                                new MyDateUpdateDialog().changeDateRequestDialogFunc(BorrowSummaryActivity.this, this, dateModel, transactionModel);
                        }
                    }
                }
            }
        }

        if (transactionModel.getAdmin_commission_from_borrower() != null && transactionModel.getAdmin_commission_from_borrower().length() > 0) {
            float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_borrower());
            float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
            float amount = totalAmount - commission;
            afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
        }
    }

    private void setButtonVisibility(String forWhat) {
       // if (forWhat.equalsIgnoreCase(getString(R.string.transaction))) {
        if (!Const.isRequestByMe(transactionModel.getFromId())) {

            if (status != null && status.equalsIgnoreCase("0")) { //PENDING
                setTransactionButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("1")) {//negotioation
                setTransactionButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("2")) {//accepted
                setTransactionButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("3")) {//decline
                setTransactionButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("4")) {//completed
                setTransactionButtonVisibleFunc(status);
            }


        //} else if (forWhat.equalsIgnoreCase(getString(R.string.history))) {
        } else if (Const.isRequestByMe(transactionModel.getFromId())) {

            if (status != null && status.equalsIgnoreCase("0")) { //PENDING
                setHistoryButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("1")) {//negotioation
                setHistoryButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("2")) {//accepted
                setHistoryButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("3")) {//decline
                setHistoryButtonVisibleFunc(status);
            } else if (status != null && status.equalsIgnoreCase("4")) {//completed
                setHistoryButtonVisibleFunc(status);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && data != null) {
            String message = data.getStringExtra("MESSAGE");
            hideThreeButton();
            finish();
        } else if (requestCode == 200 && data != null) {
            hideThreeButton();
            finish();
        }
    }

    private void hideThreeButton() {
        negotiateTV.setVisibility(View.GONE);
        acceptTV.setVisibility(View.GONE);
        declineTV.setVisibility(View.GONE);
    }

    private void showThreeButton() {
        negotiateTV.setVisibility(View.VISIBLE);
        acceptTV.setVisibility(View.VISIBLE);
        declineTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void dateStatusUpdateResponse(String data) {
        if (data.equalsIgnoreCase("decline")) {
            if (dateModel != null) {
                callAPIPayDateRequestStatusUpdate();
            }
        }
    }

    private void callAPIPayDateRequestStatusUpdate() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", dateModel.getTransactionRequestId(),
                "pay_date_id", dateModel.getId(),
                "pdf_url", "",
                "new_pay_date_status", "3",
                "cancel_from", transactionModel.getRequestBy()); //date update decline
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_pay_date_request_status_update), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_pay_date_request_status_update), acceptTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

