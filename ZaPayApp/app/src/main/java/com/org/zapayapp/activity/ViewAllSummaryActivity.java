package com.org.zapayapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.DatePickerFragmentDialogue;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;

import org.jetbrains.annotations.NotNull;
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
    private ImageView chatIV;
    private Intent intent;
    private CustomRatingBar viewRatingBar;
    private RecyclerView paybackDateRecycler;
    private ArrayList<DateModel> dateModelArrayList;
    private int dateSelectPos;
    private DateModel dateModel;
    private PaybackDateAdapter paybackDateAdapter;
    private String updated_by;
    private AgreementPdfDetailModel agreementPdfDetailModel;
    private LinearLayout agreementLL;
    private TextView commissionTitleTV, commissionValueTV;
    private String status;
    private String moveFrom;
    private String requestBy;
    private TextView allTransactionDetailTV;
    private View lendViewAllLine;
    private TextView afterCommissionAmountTV;
    private LinearLayout commissionLL;
    private TextView afterCommissionTitleTV;

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
        chatIV = findViewById(R.id.chatIV);
        viewRatingBar = findViewById(R.id.viewRatingBar);

        agreementLL = findViewById(R.id.agreementLL);
        commissionTitleTV = findViewById(R.id.commissionTitleTV);
        commissionValueTV = findViewById(R.id.commissionValueTV);
        allTransactionDetailTV = findViewById(R.id.allTransactionDetailTV);
        lendViewAllLine = findViewById(R.id.lendViewAllLine);
        afterCommissionAmountTV = findViewById(R.id.afterCommissionAmountTV);
        commissionLL = findViewById(R.id.commissionLL);
        afterCommissionTitleTV = findViewById(R.id.afterCommissionTitleTV);

        paybackDateRecycler = findViewById(R.id.paybackDateRecycler);
        paybackDateRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        paybackDateRecycler.setItemAnimator(new DefaultItemAnimator());

        negotiateTV.setVisibility(View.GONE);
        acceptTV.setVisibility(View.GONE);
        declineTV.setVisibility(View.GONE);
    }

    private void initAction() {
        dateModelArrayList = new ArrayList<>();
        negotiateTV.setOnClickListener(this);
        acceptTV.setOnClickListener(this);
        declineTV.setOnClickListener(this);
        backArrowImageView.setOnClickListener(this);
        chatIV.setOnClickListener(this);
        agreementLL.setOnClickListener(this);
        allTransactionDetailTV.setOnClickListener(this);

    }

    private void getIntentValues() {
        intent = getIntent();
        if (intent != null && intent.getStringExtra("transactionId") != null && intent.getStringExtra("status") != null) {
            transactionId = intent.getStringExtra("transactionId");
            //status = intent.getStringExtra("status");
            moveFrom = intent.getStringExtra("moveFrom");

            if (intent.getStringExtra("requestBy") != null) {
                requestBy = intent.getStringExtra("requestBy");

                if (Objects.requireNonNull(requestBy).equalsIgnoreCase("1")) {
                    titleTV.setText(getString(R.string.lending_summary));
                    viewAllNameType.setText(getString(R.string.lender));
                } else {
                    titleTV.setText(getString(R.string.borrow_summary));
                    viewAllNameType.setText(getString(R.string.borrower));
                }
            }

            callAPIGetTransactionRequestDetail(transactionId);
            /* if (getString(R.string.transaction).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                callAPIGetTransactionRequestDetail(transactionId);
            } else if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                callAPIGetHistoryRequestDetail(transactionId);
            }*/
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
                //callAPIUpdateTransactionRequestStatus("2");
                intent = new Intent(ViewAllSummaryActivity.this, AcceptActivity.class);
                intent.putExtra("moveFrom", moveFrom);
                intent.putExtra("status", "2");
                intent.putExtra("transactionModel", gson.toJson(transactionModel));
                startActivityForResult(intent, 200);
                break;
            case R.id.declineTV:
                callAPIUpdateTransactionRequestStatus("3");
                break;
            case R.id.backArrowImageView:
                finish();
                break;
            case R.id.chatIV:
                intent = new Intent(ViewAllSummaryActivity.this, ChatActivity.class);
                // intent.putExtra("transactionModel", transactionModel);
                intent.putExtra("transaction_id", transactionModel.getId());
                startActivity(intent);
                break;

            case R.id.agreementLL:
                if (agreementPdfDetailModel != null) {
                  /*  String url = agreementPdfDetailModel.getPdfUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);*/

                    Intent intent = new Intent(ViewAllSummaryActivity.this, PdfViewActivity.class);
                    intent.putExtra("pdf_url", agreementPdfDetailModel.getPdfUrl());
                    startActivity(intent);
                }

                break;

            case R.id.allTransactionDetailTV:
                Intent intent = new Intent(ViewAllSummaryActivity.this, ViewAllHistoryAndTransactionDetailsActivity.class);
                intent.putExtra("transactionId", transactionModel.getId());
                intent.putExtra("moveFrom", moveFrom);
                startActivity(intent);
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


    public void callAPIPayDateRequestStatusUpdateForCancel(DateModel dateModel, String requestBy) {
        //new_pay_date_status- 0=default, 1=pending, 2=accepted, 3=decline
        //cancel_from-  1=lender, 2=borrower
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId,
                "pay_date_id", dateModel.getId(),
                "new_pay_date_status", "1", //date accept
                "pdf_url", "",
                "cancel_from", requestBy);
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
                    //callAPIGetHistoryRequestDetail(transactionId);
                    callAPIGetTransactionRequestDetail(transactionId);
                    Intent intent = new Intent(ViewAllSummaryActivity.this, DateChangeRequestDialogActivity.class);
                    startActivity(intent);
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                }
            } else if (from.equals(getResources().getString(R.string.api_pay_date_request_status_update))) {
                if (status == 200) {
                    showSimpleAlert(getString(R.string.your_date_request_has_been_canceled), "");
                    callAPIGetTransactionRequestDetail(transactionId);
                    //paybackDateAdapter.notifyDataSetChanged();
                } else {
                    showSimpleAlert(msg, getResources().getString(R.string.api_pay_date_request_status_update));
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


        if (transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
            String pay_date = transactionModel.getPayDate();
            pay_date = pay_date.replaceAll("\\\\", "");
            try {
                JSONArray jsonArray = new JSONArray(pay_date);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String date = jsonObject1.getString("date");
                //paymentDateTV.setText(DateFormat.getDateFromEpoch(date));
                paymentDateTV.setText(DateFormat.dateFormatConvert(date));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (transactionModel.getTotalAmount() != null && transactionModel.getTotalAmount().length() > 0) {
            String total_amount = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + transactionModel.getTotalAmount();
            totalPayBackTV.setText(total_amount);
        }

        if (Const.isRequestByMe(transactionModel.getFromId())) {
            if (transactionModel.getReceiver_average_rating() != null && transactionModel.getReceiver_average_rating().length() > 0) {
                viewRatingBar.setScore(Float.parseFloat(transactionModel.getReceiver_average_rating()));
            }
        } else {
            if (transactionModel.getSender_average_rating() != null && transactionModel.getSender_average_rating().length() > 0) {
                viewRatingBar.setScore(Float.parseFloat(transactionModel.getSender_average_rating()));

            }
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
                // terms_value = terms_value + " " + getString(R.string.none);
                termTV.setText(getString(R.string.none));
            }
        }

        if (transactionModel.getUpdatedBy() != null && transactionModel.getUpdatedBy().length() > 0
                && transactionModel.getStatus() != null && transactionModel.getStatus().length() > 0) {

            updated_by = transactionModel.getUpdatedBy();
            status = transactionModel.getStatus();
            setButtonVisibility(forWhat);
        }

        if (jsonObject.get("commission_charges_detail").getAsString() != null && jsonObject.get("commission_charges_detail").getAsString().length() > 0) {
            String commission_charges_detail = jsonObject.get("commission_charges_detail").getAsString();
            commission_charges_detail.replace("\\", "/");
            CommissionModel commissionModel = gson.fromJson(commission_charges_detail, CommissionModel.class);

           /* if (moveFrom != null && moveFrom.equalsIgnoreCase(getString(R.string.transaction))) {
                if (requestBy != null && requestBy.length() > 0) {
                    if (requestBy.equalsIgnoreCase("1")) {     //lender
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                        commissionValueTV.setText(commissionModel.getLenderChargeValue());
                    } else if (requestBy.equalsIgnoreCase("2")) {   //borrower
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        commissionValueTV.setText(commissionModel.getBorrowerChargeValue());
                    }
                }
            }else if (moveFrom != null && moveFrom.equalsIgnoreCase(getString(R.string.history))){
                if (requestBy != null && requestBy.length() > 0) {
                    if (requestBy.equalsIgnoreCase("2")) {
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                        commissionValueTV.setText(commissionModel.getLenderChargeValue());
                    } else if (requestBy.equalsIgnoreCase("1")) {
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        commissionValueTV.setText(commissionModel.getBorrowerChargeValue());
                    }
                }
            */


            if (getString(R.string.transaction).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                    if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                        commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_lender());
                    }
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_borrower());
                    }
                }
            } else if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
                if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                    if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getLenderChargeValue() + ")" + commissionModel.getLenderChargeType());
                        commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_lender());
                    }
                    if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                        commissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + commissionModel.getBorrowerChargeValue() + ")" + commissionModel.getBorrowerChargeType());
                        commissionValueTV.setText(Const.getCurrency() + transactionModel.getAdmin_commission_from_borrower());
                    }
                }
            }
        }

        if (status.equalsIgnoreCase("2") || status.equalsIgnoreCase("4")) {
            agreementLL.setVisibility(View.VISIBLE);
        } else {
            agreementLL.setVisibility(View.GONE);
        }

        if (jsonObject.has("pdf_details") && jsonObject.get("pdf_details").getAsJsonArray() != null && jsonObject.get("pdf_details").getAsJsonArray().size() > 0) {
            List<AgreementPdfDetailModel> pdf_details = apiCalling.getDataList(jsonObject, "pdf_details", AgreementPdfDetailModel.class);
            agreementPdfDetailModel = pdf_details.get(0);
        }

        if (jsonObject.get("pay_dates_list").getAsJsonArray() != null && jsonObject.get("pay_dates_list").getAsJsonArray().size() > 0) {
            List<DateModel> list = apiCalling.getDataArrayList(jsonObject.get("pay_dates_list").getAsJsonArray(), "pay_dates_list", DateModel.class);
            dateModelArrayList.clear();
            dateModelArrayList.addAll(list);

            for (int i = 0; i < dateModelArrayList.size(); i++) {
               /* if (dateModelArrayList.get(i).getStatus().equalsIgnoreCase("remaining")) {
                    dateModelArrayList.get(i).setLatestRemaining(true);
                    break;
                }*/

               /* if (dateModelArrayList.get(i).getNew_pay_date_status().equalsIgnoreCase("0")) {
                    dateModelArrayList.get(i).setLatestRemaining(true);
                    break;
                }*/

                if (dateModelArrayList.get(i).getStatus().equals("remaining") && dateModelArrayList.get(i).getNew_pay_date_status().equalsIgnoreCase("0")) {
                    dateModelArrayList.get(i).setLatestRemaining(true);
                    break;
                }
            }

            setPaybackAdapter();
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

        //if (getString(R.string.transaction).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
        if (!Const.isRequestByMe(transactionModel.getFromId())) {
            if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                    titleTV.setText(getString(R.string.lending_summary));
                    viewAllNameType.setText(getString(R.string.lender));
                    totalPlayReceiveTV.setText(getString(R.string.total_to_receive_back));

                }
                if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                    titleTV.setText(getString(R.string.borrow_summary));
                    viewAllNameType.setText(getString(R.string.borrower));
                    totalPlayReceiveTV.setText(getString(R.string.total_to_pay_back));
                }
            }
        }
        //else if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) {
        else if (Const.isRequestByMe(transactionModel.getFromId())) {
            if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                    titleTV.setText(getString(R.string.lending_summary));
                    viewAllNameType.setText(getString(R.string.lender));
                    totalPlayReceiveTV.setText(getString(R.string.total_to_receive_back));
                }
                if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                    titleTV.setText(getString(R.string.borrow_summary));
                    viewAllNameType.setText(getString(R.string.borrower));
                    totalPlayReceiveTV.setText(getString(R.string.total_to_pay_back));
                }
            }
        }

        if (transactionModel.getIs_negotiate_after_accept() != null && transactionModel.getIs_negotiate_after_accept().length() > 0 && transactionModel.getIs_negotiate_after_accept().equals("2")) {
            allTransactionDetailTV.setVisibility(View.VISIBLE);
            lendViewAllLine.setVisibility(View.VISIBLE);
        } else {
            allTransactionDetailTV.setVisibility(View.GONE);
            lendViewAllLine.setVisibility(View.GONE);
        }


        //if (getString(R.string.transaction).equalsIgnoreCase(moveFrom)) {
        if (!Const.isRequestByMe(transactionModel.getFromId())) {
            if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                if (transactionModel.getRequestBy().equals("1")) {
                    if (transactionModel.getAdmin_commission_from_lender() != null && transactionModel.getAdmin_commission_from_lender().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_lender());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                } else if (transactionModel.getRequestBy().equals("2")) {
                    if (transactionModel.getAdmin_commission_from_borrower() != null && transactionModel.getAdmin_commission_from_borrower().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_borrower());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                }
            }

            //} else if (getString(R.string.history).equalsIgnoreCase(moveFrom)) {
        } else if (Const.isRequestByMe(transactionModel.getFromId())) {
            if (transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
                if (transactionModel.getRequestBy().equals("2")) {
                    if (transactionModel.getAdmin_commission_from_lender() != null && transactionModel.getAdmin_commission_from_lender().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_lender());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                } else if (transactionModel.getRequestBy().equals("1")) {
                    if (transactionModel.getAdmin_commission_from_borrower() != null && transactionModel.getAdmin_commission_from_borrower().length() > 0) {
                        float commission = Float.parseFloat(transactionModel.getAdmin_commission_from_borrower());
                        float totalAmount = Float.parseFloat(transactionModel.getTotalAmount());
                        float amount = totalAmount - commission;
                        afterCommissionAmountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
                    }
                }
            }
        }

      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);*/
        //setPaybackAdapter();

        //this will be invisible when after accept negotiate
        if (transactionModel.getIs_negotiate_after_accept() != null && transactionModel.getIs_negotiate_after_accept().length() > 0) {
            if (transactionModel.getIs_negotiate_after_accept().equals("2")) {
                commissionLL.setVisibility(View.GONE);
                afterCommissionTitleTV.setVisibility(View.GONE);
                afterCommissionAmountTV.setVisibility(View.GONE);
            }
        }
    }

    public void selectPaybackDate(int selectedPos, DateModel dateModel) {
        try {
            //if (isPreviousDateSelected(selectedPos)){
            dateSelectPos = selectedPos;
            this.dateModel = dateModel;
            DialogFragment newFragment1 = new DatePickerFragmentDialogue();
            Bundle args1 = new Bundle();
            args1.putString(getString(R.string.show), getString(R.string.current_month));
            args1.putString("DATE", dateModel.getPayDate());
            newFragment1.setArguments(args1);
            newFragment1.show(getSupportFragmentManager(), getString(R.string.date_picker));
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void datePickerCallback(String selectedDate, int year, int month, int day, String
            from) throws ParseException {
        String formattedDate = year + "-" + month + "-" + day;
        if (dateModel != null) {
            callAPIUpdatePayDate(formattedDate);
        }
    }

    private void setPaybackAdapter() {
        //  if (getString(R.string.history).equalsIgnoreCase(intent.getStringExtra("moveFrom"))) { }
        paybackDateAdapter = new PaybackDateAdapter(ViewAllSummaryActivity.this, dateModelArrayList, intent.getStringExtra("moveFrom"), requestBy, transactionModel);
        paybackDateRecycler.setAdapter(paybackDateAdapter);

      /*  if (paybackDateAdapter == null) {
            paybackDateAdapter = new PaybackDateAdapter(ViewAllSummaryActivity.this, dateModelArrayList, intent.getStringExtra("moveFrom"), requestBy, transactionModel);
            paybackDateRecycler.setAdapter(paybackDateAdapter);
        } else {
            paybackDateAdapter.notifyDataSetChanged();
        }*/
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


            // } else if (forWhat.equalsIgnoreCase(getString(R.string.history))) {
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

    private void setHistoryButtonVisibleFunc(@NotNull String status) {
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

            declineTV.setVisibility(View.VISIBLE);

        } else if (status.equalsIgnoreCase("2")) {

            if (transactionModel.getRequestBy().equalsIgnoreCase("2")) {
                if (transactionModel.getIs_negotiate_after_accept() != null && transactionModel.getIs_negotiate_after_accept().length() > 0 && transactionModel.getIs_negotiate_after_accept().equals("1")) {
                    negotiateTV.setVisibility(View.GONE);
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                } else {
                    if (transactionModel.getIs_negotiate_after_accept().equals("1")) {
                        negotiateTV.setVisibility(View.GONE);//re-negotiation apply only once
                    } else {
                        negotiateTV.setVisibility(View.VISIBLE);
                        negotiateTV.setText(R.string.accepted_renegotiate);
                    }

                    //negotiateTV.setVisibility(View.GONE); //this is Gone for Temprery (this manage after accept user can negotiate)
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                }
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
            declineTV.setVisibility(View.VISIBLE);

        } else if (status.equalsIgnoreCase("2")) {  //status=2 accepted,requestBy=2 Borrower
            if (transactionModel.getRequestBy().equalsIgnoreCase("1")) {
                if (transactionModel.getIs_negotiate_after_accept() != null && transactionModel.getIs_negotiate_after_accept().length() > 0 && transactionModel.getIs_negotiate_after_accept().equals("1")) {
                    negotiateTV.setVisibility(View.GONE);
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                } else {
                    if (transactionModel.getIs_negotiate_after_accept().equals("1")) {
                        negotiateTV.setVisibility(View.GONE);
                    } else {
                        negotiateTV.setVisibility(View.VISIBLE);
                        negotiateTV.setText(R.string.accepted_renegotiate);
                    }
                    //negotiateTV.setVisibility(View.GONE); //this is Gone for temporary (this manage after accept user can negotiate)
                    acceptTV.setVisibility(View.GONE);
                    declineTV.setVisibility(View.GONE);
                }
            }
        } else {
            negotiateTV.setVisibility(View.GONE);
            acceptTV.setVisibility(View.GONE);
            declineTV.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && data != null) {
            finish();
        }
    }
}


