package com.org.zapayapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.PaybackDateAdapter;
import com.org.zapayapp.chat.ChatActivity;
import com.org.zapayapp.dialogs.DateChangeRequestDialogActivity;
import com.org.zapayapp.model.CommissionModel;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.AgreementPdfDetailModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.uihelpers.CustomRatingBar;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.DatePickerFragmentDialogue;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ViewAllSummaryActivity extends BaseActivity implements APICallback, View.OnClickListener, DatePickerFragmentDialogue.DatePickerCallback {
    private TextView viewAllNameType, nameTV, amountTV, termTV, noOfPaymentTV, paymentDateTV, totalPayBackTV, negotiateTV, acceptTV, declineTV, totalPlayReceiveTV;
    private String transactionId;
    private TransactionModel transactionModel;
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;
    private TextView chatTV;
    private Intent intent;
    private String status;
    private CustomRatingBar viewRatingBar;

    private RecyclerView paybackDateRecycler;
    private ArrayList<DateModel> dateModelArrayList;
    private int dateSelectPos;
    private DateModel dateModel;
    private PaybackDateAdapter paybackDateAdapter;
    private String requestBy;

    private String updated_by;
    private AgreementPdfDetailModel agreementPdfDetailModel;
    private LinearLayout agreementLL;
    private TextView commissionTitleTV, commissionValueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_summary);
        init();
        initAction();
        getIntentValues();
    }

    private void init() {
        toolbar = findViewById(R.id.customToolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);

        viewAllNameType = findViewById(R.id.viewAllNameType);
        nameTV = findViewById(R.id.nameTV);
        amountTV = findViewById(R.id.amountTV);
        termTV = findViewById(R.id.termTV);
        noOfPaymentTV = findViewById(R.id.noOfPaymentTV);
        paymentDateTV = findViewById(R.id.paymentDateTV);
        totalPayBackTV = findViewById(R.id.totalPayBackTV);
        totalPlayReceiveTV = findViewById(R.id.totalPlayReceiveTV);

        negotiateTV = findViewById(R.id.negotiateTV);
        acceptTV = findViewById(R.id.acceptTV);
        declineTV = findViewById(R.id.declineTV);
        chatTV = findViewById(R.id.chatTV);
        viewRatingBar = findViewById(R.id.viewRatingBar);

        agreementLL = findViewById(R.id.agreementLL);
        commissionTitleTV = findViewById(R.id.commissionTitleTV);
        commissionValueTV = findViewById(R.id.commissionValueTV);


        paybackDateRecycler = findViewById(R.id.paybackDateRecycler);
        paybackDateRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        paybackDateRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void initAction() {
        dateModelArrayList = new ArrayList<>();
        negotiateTV.setOnClickListener(this);
        acceptTV.setOnClickListener(this);
        declineTV.setOnClickListener(this);
        backArrowImageView.setOnClickListener(this);
        chatTV.setOnClickListener(this);
        agreementLL.setOnClickListener(this);
    }

    private void getIntentValues() {
        intent = getIntent();
        if (intent != null && intent.getStringExtra("transactionId") != null && intent.getStringExtra("status") != null) {
            transactionId = intent.getStringExtra("transactionId");
            status = intent.getStringExtra("status");

            if (intent.getStringExtra("requestBy") != null) {
                requestBy = intent.getStringExtra("requestBy");
              /*  if (Objects.requireNonNull(requestBy).equalsIgnoreCase("1")) {
                    titleTV.setText(getString(R.string.lending_summary));
                    viewAllNameType.setText(getString(R.string.lender));
                } else {
                    titleTV.setText(getString(R.string.borrow_summary));
                    viewAllNameType.setText(getString(R.string.borrower));
                }*/



                if (getString(R.string.transaction).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    if (Objects.requireNonNull(requestBy).equalsIgnoreCase("1")) {
                        titleTV.setText(getString(R.string.lending_summary));
                        viewAllNameType.setText(getString(R.string.lender));
                    } else {
                        titleTV.setText(getString(R.string.borrow_summary));
                        viewAllNameType.setText(getString(R.string.borrower));
                    }
                } else if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                    if (Objects.requireNonNull(requestBy).equalsIgnoreCase("2")) {
                        titleTV.setText(getString(R.string.lending_summary));
                        viewAllNameType.setText(getString(R.string.lender));
                    } else {
                        titleTV.setText(getString(R.string.borrow_summary));
                        viewAllNameType.setText(getString(R.string.borrower));
                    }
                }


            }

            if (getString(R.string.transaction).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                callAPIGetTransactionRequestDetail(transactionId);
            } else if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                callAPIGetHistoryRequestDetail(transactionId);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negotiateTV:
                intent = new Intent(ViewAllSummaryActivity.this, LendBorrowActivity.class);
                intent.putExtra("isBorrow", true);
                intent.putExtra("transactionModel", transactionModel);
                startActivity(intent);
                finish();
                break;
            case R.id.acceptTV:
                callAPIUpdateTransactionRequestStatus("2");
                break;
            case R.id.declineTV:
                callAPIUpdateTransactionRequestStatus("3");
                break;
            case R.id.backArrowImageView:
                finish();
                break;
            case R.id.chatTV:
                intent = new Intent(ViewAllSummaryActivity.this, ChatActivity.class);
                intent.putExtra("transactionModel", transactionModel);
                startActivity(intent);
                break;

            case R.id.agreementLL:
                if (agreementPdfDetailModel != null) {
                    String url = agreementPdfDetailModel.getPdfUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
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
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transaction_request_id);
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


    private void callAPIUpdatePayDate(String pay_date) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "pay_date_id", dateModel.getId(),
                "transaction_request_id", dateModel.getTransactionRequestId(),
                "pay_date", pay_date);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_update_pay_date), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_pay_date), acceptTV);
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
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", getResources().getString(R.string.api_update_transaction_request_status));
                    setResult(2, intent);
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_transaction_request_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        transactionModel = (TransactionModel) apiCalling.getDataObject(json.get("data").getAsJsonObject(), TransactionModel.class);
                        setData(json.get("data").getAsJsonObject(), getString(R.string.transaction));
                    }

                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);

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
            } else if (from.equals(getResources().getString(R.string.api_update_pay_date))) {
                if (status == 200) {
                    callAPIGetHistoryRequestDetail(transactionId);
                    Intent intent = new Intent(ViewAllSummaryActivity.this, DateChangeRequestDialogActivity.class);
                    startActivity(intent);
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }
            }
        }
    }

    private void setData(JsonObject jsonObject, String forWhat) {
        if (jsonObject.get("first_name").getAsString() != null && jsonObject.get("first_name").getAsString().length() > 0 && jsonObject.get("first_name").getAsString() != null && jsonObject.get("first_name").getAsString().length() > 0) {
            String name = jsonObject.get("first_name").getAsString() + " " + jsonObject.get("last_name").getAsString();
            nameTV.setText(name);
        }
        if (jsonObject.get("amount").getAsString() != null && jsonObject.get("amount").getAsString().length() > 0) {
            String amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + jsonObject.get("amount").getAsString();
            amountTV.setText(amount);
        }
        if (jsonObject.get("no_of_payment").getAsString() != null && jsonObject.get("no_of_payment").getAsString().length() > 0) {
            String no_of_payment = jsonObject.get("no_of_payment").getAsString();
            noOfPaymentTV.setText(no_of_payment);
        }
        if (jsonObject.get("created_at").getAsString() != null && jsonObject.get("created_at").getAsString().length() > 0) {
            String created_at = jsonObject.get("created_at").getAsString();
            // paymentDateTV.setText(TimeStamp.timeFun(created_at));
        }

        if (jsonObject.get("pay_date").getAsString() != null && jsonObject.get("pay_date").getAsString().length() > 0) {
            String pay_date = jsonObject.get("pay_date").getAsString();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                //  paymentDateTV.setText(DateFormat.getDateFromEpoch(date));
                paymentDateTV.setText(DateFormat.dateFormatConvert(date));

                /*dateModelArrayList.clear();
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object=  jsonArray.getJSONObject(i);
                    String date1= object.getString("date");
                    dateModelArrayList.add(new DateModel(date1,true));
                }*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.get("total_amount").getAsString() != null && jsonObject.get("total_amount").getAsString().length() > 0) {
            String total_amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + jsonObject.get("total_amount").getAsString();
            totalPayBackTV.setText(total_amount);
        }

        if (jsonObject.get("average_rating").getAsString() != null && jsonObject.get("average_rating").getAsString().length() > 0) {
            viewRatingBar.setScore(Float.parseFloat(jsonObject.get("average_rating").getAsString()));
        }

        if (jsonObject.get("terms_type").getAsString() != null && jsonObject.get("terms_type").getAsString().length() > 0 && jsonObject.get("terms_value").getAsString() != null && jsonObject.get("terms_value").getAsString().length() > 0) {
            String terms_type = jsonObject.get("terms_type").getAsString();
            String terms_value = jsonObject.get("terms_value").getAsString();
            if (terms_type.equalsIgnoreCase("1")) {
                terms_value = terms_value + " " + getString(R.string.percent);
                termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("2")) {
                terms_value = terms_value + " " + getString(R.string.fee);
                termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("3")) {
                terms_value = terms_value + " " + getString(R.string.discount);
                termTV.setText(terms_value);
            } else if (terms_type.equalsIgnoreCase("4")) {
                // terms_value = terms_value + " " + getString(R.string.none);
                termTV.setText(getString(R.string.none));
            }
        }


        if (jsonObject.has("updated_by") && jsonObject.get("updated_by").getAsString() != null && jsonObject.get("updated_by").getAsString().length() > 0
                && jsonObject.has("status") && jsonObject.get("status").getAsString() != null && jsonObject.get("status").getAsString().length() > 0) {
            updated_by = jsonObject.get("updated_by").getAsString();
            status = jsonObject.get("status").getAsString();
            setButtonVisibility(forWhat);
        }

        if (jsonObject.has("commission_charges_detail") && jsonObject.get("commission_charges_detail").getAsString() != null && jsonObject.get("commission_charges_detail").getAsString().length() > 0) {
            String commission_charges_detail = jsonObject.get("commission_charges_detail").getAsString();
            commission_charges_detail.replace("\\", "/");
            CommissionModel commissionModel = gson.fromJson(commission_charges_detail, CommissionModel.class);


            if (requestBy != null && requestBy.length() > 0) {
                if (requestBy.equalsIgnoreCase("1")) {     //lender
                    commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                    commissionValueTV.setText(commissionModel.getLenderChargeValue());
                } else if (requestBy.equalsIgnoreCase("2")) {   //borrower
                    commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                    commissionValueTV.setText(commissionModel.getBorrowerChargeValue());
                }
            }
        }

        if (status.equalsIgnoreCase("2") || status.equalsIgnoreCase("4")) {
            agreementLL.setVisibility(View.VISIBLE);
        } else {
            agreementLL.setVisibility(View.GONE);
        }

        if (jsonObject.has("pdf_details") && jsonObject.get("pdf_details").getAsJsonArray() != null) {
            List<AgreementPdfDetailModel> pdf_details = apiCalling.getDataList(jsonObject, "pdf_details", AgreementPdfDetailModel.class);
            agreementPdfDetailModel = pdf_details.get(0);
        }







        if (jsonObject.get("pay_dates_list").getAsJsonArray() != null && jsonObject.get("pay_dates_list").getAsJsonArray().size() > 0) {
            // List<DateModel> list = apiCalling.getDataList(json, "data", DateModel.class);
            List<DateModel> list = apiCalling.getDataArrayList(jsonObject.get("pay_dates_list").getAsJsonArray(), "pay_dates_list", DateModel.class);
            dateModelArrayList.clear();
            dateModelArrayList.addAll(list);

            for (int i = 0; i < dateModelArrayList.size(); i++) {
                if (dateModelArrayList.get(i).getStatus().equalsIgnoreCase("remaining")) {
                    dateModelArrayList.get(i).setLatestRemaining(true);
                    break;
                }
            }
        }

        if (transactionModel.getStatus() != null && transactionModel.getStatus().length() > 0) {
            String transactionStatus = transactionModel.getStatus();
            for (int i = 0; i < dateModelArrayList.size(); i++) {

                if (transactionStatus.equals("0")) {
                    dateModelArrayList.get(i).setEditable(false);
                } else if (transactionStatus.equals("1")) {
                    dateModelArrayList.get(i).setEditable(false);
                } else if (transactionStatus.equals("2")) {
                    dateModelArrayList.get(i).setEditable(true);
                } else if (transactionStatus.equals("3")) {
                    dateModelArrayList.get(i).setEditable(false);
                } else if (transactionStatus.equals("4")) {
                    dateModelArrayList.get(i).setEditable(false);
                }

            }
        }


        if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
            if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                titleTV.setText(getString(R.string.lending_summary));
                totalPlayReceiveTV.setText(getString(R.string.total_to_receive_back));
            }
            if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                titleTV.setText(getString(R.string.borrow_summary));
                totalPlayReceiveTV.setText(getString(R.string.total_to_pay_back));
            }
        }
        setPaybackAdapter();
    }


    public void selectPaybackDate(int selectedPos, DateModel dateModel) {
        try {
            //if (isPreviousDateSelected(selectedPos)){
            dateSelectPos = selectedPos;
            this.dateModel = dateModel;
            DialogFragment newFragment1 = new DatePickerFragmentDialogue();
            Bundle args1 = new Bundle();
            args1.putString(getString(R.string.show), getString(R.string.current_month));
            newFragment1.setArguments(args1);
            newFragment1.show(getSupportFragmentManager(), getString(R.string.date_picker));
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void datePickerCallback(String selectedDate, int year, int month, int day, String from) throws ParseException {
        String formattedDate = year + "-" + month + "-" + day;
        if (dateModel != null) {
            callAPIUpdatePayDate(formattedDate);
        }

    }


    private void setPaybackAdapter() {
        if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {

        }

        if (paybackDateAdapter == null) {
            paybackDateAdapter = new PaybackDateAdapter(ViewAllSummaryActivity.this, dateModelArrayList, intent.getStringExtra("moveFrom"), requestBy);
            paybackDateRecycler.setAdapter(paybackDateAdapter);
        } else {
            paybackDateAdapter.notifyDataSetChanged();
        }
    }

    private void setButtonVisibility(String forWhat) {
        if (forWhat.equalsIgnoreCase(getString(R.string.transaction))) {

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


        } else if (forWhat.equalsIgnoreCase(getString(R.string.history))) {

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


    private void setHistoryButtonVisibleFunc(String status) {
        if (status.equalsIgnoreCase("1")) {
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
}


