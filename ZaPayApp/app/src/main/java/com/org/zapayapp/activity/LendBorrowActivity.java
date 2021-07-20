package com.org.zapayapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dd.ShadowLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.ContactAdapter;
import com.org.zapayapp.adapters.IndicatorAdapter;
import com.org.zapayapp.adapters.PaybackAdapter;
import com.org.zapayapp.listener.ContactListener;
import com.org.zapayapp.model.ContactModel;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.model.PabackModel;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.DatePickerFragmentDialogue;
import com.org.zapayapp.utils.EndlessRecyclerViewScrollListener;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WVDateLib;
import com.org.zapayapp.webservices.APICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class LendBorrowActivity extends BaseActivity implements View.OnClickListener, DatePickerFragmentDialogue.DatePickerCallback, APICallback, ContactListener {
    private RecyclerView indicatorRecycler;
    private List<String> listIndicator;
    private TextView nextButtonTV, backButtonTV;
    private ShadowLayout lendShadowBack, lendShadowNext;
    private int selectedPos = 0;
    private IndicatorAdapter indicatorAdapter;
    private TextView lendTxtHeader, lendTxtAmount, lendTermsTxtOption, lendTermsTxtPercent, lendTermsTxtFee, lendTermsTxtDiscount, lendTermsTxtNone;
    private TextInputEditText lendAmountEdtAmount, lendTermsEdtOption, lendPaymentEdtNo;

    private LinearLayout lendViewAmount, lendViewTerms, lendViewPayment, lendViewPayback, lendViewBorrow, lendViewLending, lendViewContact;
    private int isTermsOption, isNoPayment, paybackPos;

    private float finalTotalAmount;//instalment amount
    private double installmentAmount;//instalment amount
    private float amount; //enter amount
    private float finalTotalPayBackAmount;//after add term amount
    private String paymentDate;

    private WVDateLib wvDateLib;
    private RecyclerView paybackRecycler, contactRecycler;
    private List<PabackModel> paybackList;
    private PaybackAdapter paybackAdapter;
    private ContactAdapter contactAdapter;
    private Intent intent;
    private boolean isBorrow;
    private boolean isBack;

    private List<ContactModel> contactNumberList;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int pageNo = 0;
    private TextView noDataTv;
    private String toId = "";
    private int request_by;
    private String termValue = "";

    //borrow....
    private TextView amountTV, termTV, noOfPaymentTV, paymentDateTV, totalPayBackTV, zapayCommissionTitleTV, zapayCommissionTV, afterCommissionTV;
    private TextView afterCommissionTitleTV;

    //Lending....
    private TextView l_amountTV;
    private TextView lTermTV;
    private TextView lNoOfPaymentTV;
    private TextView lPaymentDateTV;
    private TextView totalReceivedBackTV, zapayCommissionTitleLenderTV, zapayCommissionLenderTV, afterCommissionLenderTV;
    private TextView afterCommissionLenderTitleTV;
    //this use for negotiation
    private TransactionModel transactionModel;
    private CustomTextInputLayout lendAmountEdtAmountInputLayout, lendTermsInputLayout, lendNoOfPaymentInputLayout;
    private ArrayList<String> dateListTitle;

    private double borrowerCommission;
    private double lenderCommission;
    private double defaultFeeAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_borrow);
        init();
        getIntentValues();
        initAction();
    }

    private void init() {
        indicatorRecycler = findViewById(R.id.indicatorRecycler);
        indicatorRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        indicatorRecycler.setItemAnimator(new DefaultItemAnimator());

        lendTxtHeader = findViewById(R.id.lendTxtHeader);
        lendTxtAmount = findViewById(R.id.lendTxtAmount);
        nextButtonTV = findViewById(R.id.nextButtonTV);
        backButtonTV = findViewById(R.id.backButtonTV);
        lendShadowBack = findViewById(R.id.lendShadowBack);
        lendShadowNext = findViewById(R.id.lendShadowNext);

        lendAmountEdtAmountInputLayout = findViewById(R.id.lendAmountEdtAmountInputLayout);
        lendTermsInputLayout = findViewById(R.id.lendTermsInputLayout);
        lendNoOfPaymentInputLayout = findViewById(R.id.lendNoOfPaymentInputLayout);

        listIndicator = new ArrayList<>();
        wvDateLib = new WVDateLib(this);

        dateListTitle = new ArrayList<>();
        dateListTitle.add(getString(R.string.first_select_date));
        dateListTitle.add(getString(R.string.second_select_date));
        dateListTitle.add(getString(R.string.third_select_date));
        dateListTitle.add(getString(R.string.fourth_select_date));
        dateListTitle.add(getString(R.string.fifth_select_date));
        dateListTitle.add(getString(R.string.sixth_select_date));
        dateListTitle.add(getString(R.string.seventh_select_date));
        dateListTitle.add(getString(R.string.eighth_select_date));
        dateListTitle.add(getString(R.string.ninth_select_date));
    }

    private void initAction() {
        listIndicator.add(getString(R.string.amount));
        listIndicator.add(getString(R.string.terms));
        listIndicator.add(getString(R.string.no_of_payments));
        listIndicator.add(getString(R.string.payback_date));

        if (isBorrow) {
            request_by = 2;
            listIndicator.add(getString(R.string.borrow_summary_));
        } else {
            request_by = 1;
            listIndicator.add(getString(R.string.lending_summary_));
        }

        if (transactionModel != null && transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0) {
            request_by = Integer.parseInt(transactionModel.getRequestBy());
        } else {
            listIndicator.add(getString(R.string.select_contact));
        }

        indicatorAdapter = new IndicatorAdapter(this, listIndicator, isBorrow);
        indicatorRecycler.setAdapter(indicatorAdapter);
        nextButtonTV.setOnClickListener(this);
        backButtonTV.setOnClickListener(this);

        initAmountView();
        initTermsView();
        initPaymentView();
        initPaybackView();
        initBorrowView();
        initLendingView();
        initContactView();
        setIndicatorView(0);

        if (transactionModel != null && transactionModel.getStatus() != null) {
            if (transactionModel.getStatus().equals("2") || transactionModel.getIs_negotiate_after_accept().equals("2")) {
                afterAcceptNegotiateFunc();
            }
        }
    }

    private void getIntentValues() {
        intent = getIntent();
        if (intent != null) {
            isBorrow = intent.getBooleanExtra("isBorrow", false);
        }

        //this code use for nagotiation......
        if (getIntent().getSerializableExtra("transactionModel") != null) {
            transactionModel = (TransactionModel) getIntent().getSerializableExtra("transactionModel");
        }
    }

    private void afterAcceptNegotiateFunc() {
        selectedPos = indicatorAdapter.getSelectedPos() + 1;
        indicatorAdapter.setSelected(selectedPos);
        setIndicatorView(selectedPos);
    }

    private void initAmountView() {
        lendViewAmount = findViewById(R.id.lendViewAmount);
        lendAmountEdtAmount = findViewById(R.id.lendAmountEdtAmount);
        lendAmountEdtAmount.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //lendAmountEdtAmount.setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        lendAmountEdtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                  amount = Float.parseFloat(s.toString());
                  lendTxtAmount.setText(s);
                */
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null & s.length() > 0) {
                    amount = Float.parseFloat(s.toString());
                    lendTxtAmount.setText(s);
                } else {
                    amount = Float.parseFloat("0");
                    lendTxtAmount.setText("");
                }
            }
        });

        //nigotiation
        if (transactionModel != null && transactionModel.getAmount() != null && transactionModel.getAmount().length() > 0) {
            //lendAmountEdtAmount.setText(transactionModel.getAmount());
            lendAmountEdtAmount.setText(transactionModel.getDue_amount());
        }
    }

    private void initTermsView() {
        lendViewTerms = findViewById(R.id.lendViewTerms);
        lendTermsEdtOption = findViewById(R.id.lendTermsEdtOption);
        lendTermsEdtOption.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //lendTermsEdtOption.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        lendTermsTxtOption = findViewById(R.id.lendTermsTxtOption);

        lendTermsTxtPercent = findViewById(R.id.lendTermsTxtPercent);
        lendTermsTxtFee = findViewById(R.id.lendTermsTxtFee);
        lendTermsTxtDiscount = findViewById(R.id.lendTermsTxtDiscount);
        lendTermsTxtNone = findViewById(R.id.lendTermsTxtNone);

        lendTermsTxtPercent.setOnClickListener(this);
        lendTermsTxtFee.setOnClickListener(this);
        lendTermsTxtDiscount.setOnClickListener(this);
        lendTermsTxtNone.setOnClickListener(this);

        lendTermsEdtOption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setOptionAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Negotiation....
        if (transactionModel != null && transactionModel.getTermsValue() != null && transactionModel.getTermsValue().length() > 0) {
            if (!transactionModel.getTermsValue().equals("0"))
                lendTermsEdtOption.setText(transactionModel.getTermsValue());
        }

        if (transactionModel != null && transactionModel.getTermsType() != null && transactionModel.getTermsType().length() > 0) {
            int termType = Integer.parseInt(transactionModel.getTermsType()) - 1; //term 1 jyada aa rhi h
            selectedTermsOption(termType);
        }
    }

    private void initPaymentView() {
        lendViewPayment = findViewById(R.id.lendViewPayment);
        lendPaymentEdtNo = findViewById(R.id.lendPaymentEdtNo);
        lendPaymentEdtNo.setImeOptions(EditorInfo.IME_ACTION_DONE);

        lendPaymentEdtNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setPaymentAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (transactionModel != null && transactionModel.getNoOfPayment() != null && transactionModel.getNoOfPayment().length() > 0) {
            // lendPaymentEdtNo.setText(transactionModel.getNoOfPayment());
            if (transactionModel.getStatus().equals("2")) {
                int remainingEmiCount = getRemainingEmiCount(transactionModel.getPayDatesList());
                isNoPayment=remainingEmiCount;
                lendPaymentEdtNo.setText("" + remainingEmiCount);
            } else {
                lendPaymentEdtNo.setText(transactionModel.getNoOfPayment());
            }
        }
    }

    private int getRemainingEmiCount(List<DateModel> dateModelList) {
        int count = 0;
        for (int i = 0; i < dateModelList.size(); i++) {
            if (dateModelList.get(i).getDefault_payment_pay_done() != null && dateModelList.get(i).getDefault_payment_pay_done().length() > 0) {
                if (dateModelList.get(i).getDefault_payment_pay_done().equals("0")) {
                    count = count + 1;
                }
            }
        }
        return count;
    }

    private void initPaybackView() {
        lendViewPayback = findViewById(R.id.lendViewPayback);
        paybackRecycler = findViewById(R.id.paybackRecycler);
        paybackRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        paybackRecycler.setItemAnimator(new DefaultItemAnimator());
        paybackList = new ArrayList<>();

        //for negotiation
        /*if (transactionModel.getPayDate()!=null&&transactionModel.getPayDate().length()>0){
            try {
                JSONArray jsonObject=new JSONArray(transactionModel.getPayDate());
                for (int i=0;i<jsonObject.length();i++){
                  JSONObject jsonObject1=jsonObject.getJSONObject(i);
                  String date= jsonObject1.getString("date");
                    paybackList.add(new PabackModel(date, true));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void initBorrowView() {
        lendViewBorrow = findViewById(R.id.lendViewBorrow);
        amountTV = findViewById(R.id.amountTV);
        termTV = findViewById(R.id.termTV);
        noOfPaymentTV = findViewById(R.id.noOfPaymentTV);
        paymentDateTV = findViewById(R.id.paymentDateTV);
        totalPayBackTV = findViewById(R.id.totalPayBackTV);

        zapayCommissionTitleTV = findViewById(R.id.zapayCommissionTitleTV);
        zapayCommissionTV = findViewById(R.id.zapayCommissionTV);
        afterCommissionTV = findViewById(R.id.afterCommissionTV);

        afterCommissionTitleTV = findViewById(R.id.afterCommissionTitleTV);
        afterCommissionLenderTitleTV = findViewById(R.id.afterCommissionLenderTitleTV);
    }

    private void initLendingView() {
        lendViewLending = findViewById(R.id.lendViewLending);
        l_amountTV = findViewById(R.id.l_amountTV);
        lTermTV = findViewById(R.id.lTermTV);
        lNoOfPaymentTV = findViewById(R.id.lNoOfPaymentTV);
        lPaymentDateTV = findViewById(R.id.lPaymentDateTV);
        totalReceivedBackTV = findViewById(R.id.totalReceivedBackTV);

        zapayCommissionTitleLenderTV = findViewById(R.id.zapayCommissionTitleLenderTV);
        zapayCommissionLenderTV = findViewById(R.id.zapayCommissionLenderTV);
        afterCommissionLenderTV = findViewById(R.id.afterCommissionLenderTV);
    }

    private void initContactView() {
        // contactList = new ArrayList<>();
        contactNumberList = new ArrayList<>();
        noDataTv = findViewById(R.id.noDataTv);

        lendViewContact = findViewById(R.id.lendViewContact);
        contactRecycler = findViewById(R.id.contactRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        contactRecycler.setLayoutManager(layoutManager);
        contactRecycler.setItemAnimator(new DefaultItemAnimator());

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pageNo = page;
                callAPIGetContactList(pageNo);
            }
        };

        contactRecycler.addOnScrollListener(scrollListener);
        pageNo = 0;
        callAPIGetContactList(pageNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButtonTV:
                if (indicatorAdapter.getSelectedPos() < listIndicator.size() - 1) {

                    if (selectedPos == 0) {
                        if (wValidationLib.isValidAmount(lendAmountEdtAmountInputLayout, lendAmountEdtAmount, getString(R.string.important), getString(R.string.enter_amount), true)) {
                            selectedPos = indicatorAdapter.getSelectedPos() + 1;
                            indicatorAdapter.setSelected(selectedPos);
                            setIndicatorView(selectedPos);
                        }
                    } else if (selectedPos == 1) {
                        if (isTermsOption == 3) {
                            selectedPos = indicatorAdapter.getSelectedPos() + 1;
                            indicatorAdapter.setSelected(selectedPos);
                            setIndicatorView(selectedPos);
                        } else {
                            if (wValidationLib.isValidTerm(lendTermsInputLayout, lendTermsEdtOption, getString(R.string.important), getString(R.string.enter_term), true)) {
                                selectedPos = indicatorAdapter.getSelectedPos() + 1;
                                indicatorAdapter.setSelected(selectedPos);
                                setIndicatorView(selectedPos);
                            }
                        }

                    } else if (selectedPos == 2) {
                        if (wValidationLib.isValidNoOfPayment(lendNoOfPaymentInputLayout, lendPaymentEdtNo, getString(R.string.important), getString(R.string.enter_no_of_payment), true)) {
                            //if (!lendPaymentEdtNo.getText().toString().trim().isEmpty()) {
                            selectedPos = indicatorAdapter.getSelectedPos() + 1;
                            indicatorAdapter.setSelected(selectedPos);
                            setIndicatorView(selectedPos);
                        } //else {
                        // showSimpleAlert(getString(R.string.enter_no_of_payment), "");
                        // }
                    } else if (selectedPos == 3) {
                        if (isSelectedAllDate()) {
                            selectedPos = indicatorAdapter.getSelectedPos() + 1;
                            indicatorAdapter.setSelected(selectedPos);
                            setIndicatorView(selectedPos);
                        }
                    } else {
                        selectedPos = indicatorAdapter.getSelectedPos() + 1;
                        indicatorAdapter.setSelected(selectedPos);
                        setIndicatorView(selectedPos);
                    }

                    // lendAmountEdtAmount
                    // lendTermsEdtOption
                    // lendPaymentEdtNo


                } else if (selectedPos == 4) {
                    transactionRequestFunc(); //this is calling negotiation
                } else if (selectedPos == 5) {
                    transactionRequestFunc();
                }
                break;
            case R.id.backButtonTV:
                if (indicatorAdapter.getSelectedPos() <= listIndicator.size() - 1) {
                    selectedPos = indicatorAdapter.getSelectedPos() - 1;
                    indicatorAdapter.setSelected(selectedPos);
                    setIndicatorView(selectedPos);
                }
                break;
            case R.id.lendTermsTxtPercent:
                selectedTermsOption(0);
                break;
            case R.id.lendTermsTxtFee:
                selectedTermsOption(1);
                break;
            case R.id.lendTermsTxtDiscount:
                selectedTermsOption(2);
                break;
            case R.id.lendTermsTxtNone:
                selectedTermsOption(3);
                break;
        }
    }


    private boolean isSelectedAllDate() {
        boolean dateSelect = false;
        for (int i = 0; i < paybackList.size(); i++) {
            if (paybackList.get(i).isAddDate()) {
                dateSelect = true;
            } else {
                dateSelect = false;
                showSimpleAlert(getString(R.string.select_date), "");
                break;
            }

        }
        return dateSelect;
    }


    private boolean isPreviousDateSelected(int position) {
        boolean dateSelect = false;
        if (position > 0) {
            int pos = 0;
            for (int i = 0; i < paybackList.size(); i++) {
                if (!paybackList.get(i).isAddDate()) {
                    pos = i;
                    break;
                }
            }

            if (paybackList.get(position - 1).isAddDate()) {
                dateSelect = true;
            } else {
                dateSelect = false;
                showSimpleAlert(getString(R.string.select) + " " + dateListTitle.get(pos), "");
            }
        } else {
            dateSelect = true;
        }
        return dateSelect;
    }

    private void transactionRequestFunc() {
        //activity_status=0  //signup
        //activity_status=1  //updated profile
        //activity_status=2   //added bank account
        //activity_status=3   //verifyed bank account(ready to send request)


        if (!toId.equalsIgnoreCase("")) {
            if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("1")) {
                showSimpleAlert(getString(R.string.please_add_bank_account), getString(R.string.please_add_bank_account));
            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("2")) {
                showSimpleAlert(getString(R.string.please_verify_bank_account), getString(R.string.please_verify_bank_account));
            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("3")) {
                //callAPITransactionRequest();

                if (transactionModel != null && transactionModel.getId() != null && transactionModel.getId().length() > 0) {
                    if (transactionModel.getStatus() != null && transactionModel.getStatus().equals("2") || transactionModel.getIs_negotiate_after_accept() != null && transactionModel.getIs_negotiate_after_accept().equals("2")) {  // is_negotiate_after_accept =2 after negotion
                        callAPInegotiateRunningTransactionRequest(); //negotiate after accept request
                    } else {
                        callAPITransactionRequest();  //negotiate before accept request
                    }
                } else {
                    callAPIGetContentDisclaimer();
                    // privacyPolicyDialog();
                }
            }
        } else {
            showSimpleAlert(getString(R.string.please_select_contact), "");
        }
    }

    private void generatePaybackData() {
        //paymentDate = DateFormat.dateFormatConvert11(wvDateLib.getCurrentDate());
        paymentDate = DateFormat.dateFormatConvert11(wvDateLib.incrementDateByOne());

        //Negotiation.....
       /* if (transactionModel != null && transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
            try {
                if (transactionModel.getNoOfPayment() != null && transactionModel.getNoOfPayment().equalsIgnoreCase(String.valueOf(isNoPayment))) {
                    paybackList.clear();
                    String dataArray = transactionModel.getPayDate().replace("\\", "");
                    JSONArray jsonObject = new JSONArray(dataArray);
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                        String date = jsonObject1.getString("date");
                        paybackList.add(new PabackModel(date, true, true));
                    }
                }
                try {
                    if (transactionModel != null && transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
                        try {
                            //lendTxtAmount.setText(DateFormat.getDateFromEpoch(paybackList.get(0).getPayDate()));
                            lendTxtAmount.setText(DateFormat.dateFormatConvert(paybackList.get(0).getPayDate()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        lendTxtAmount.setText(DateFormat.dateFormatConvert(paybackList.get(0).getPayDate()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (paybackList.size() != isNoPayment) {
                    paybackList.clear();
                    lendTxtAmount.setText(paymentDate);
                    for (int i = 0; i < isNoPayment; i++) {
                        paybackList.add(new PabackModel("", false, false));
                    }
                }
                setPaybackAdapter();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (paybackList.size() != isNoPayment) {
                paybackList.clear();
                lendTxtAmount.setText(paymentDate);
                for (int i = 0; i < isNoPayment; i++) {
                    paybackList.add(new PabackModel("", false, false));
                }
            }
            setPaybackAdapter();
        }*/

        //Negotiation.....
        if (transactionModel != null && transactionModel.getPayDatesList() != null && transactionModel.getPayDatesList().size() > 0) {
            try {
                paybackList.clear();
                List<DateModel> payDateList = transactionModel.getPayDatesList();
                for (int i = 0; i < payDateList.size(); i++) {
                    if (payDateList.get(i).getDefault_payment_pay_done().equals("0")) {
                        paybackList.add(new PabackModel(payDateList.get(i).getPayDate(), true, true));
                    }
                }
                try {
                    if (transactionModel != null && transactionModel.getPayDate() != null && transactionModel.getPayDate().length() > 0) {
                        try {
                            //lendTxtAmount.setText(DateFormat.getDateFromEpoch(paybackList.get(0).getPayDate()));
                            lendTxtAmount.setText(DateFormat.dateFormatConvert(paybackList.get(0).getPayDate()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        lendTxtAmount.setText(DateFormat.dateFormatConvert(paybackList.get(0).getPayDate()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (paybackList.size() != isNoPayment) {
                    paybackList.clear();
                    lendTxtAmount.setText(paymentDate);
                    for (int i = 0; i < isNoPayment; i++) {
                        paybackList.add(new PabackModel("", false, false));
                    }
                }
                setPaybackAdapter();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (paybackList.size() != isNoPayment) {
                paybackList.clear();
                lendTxtAmount.setText(paymentDate);
                for (int i = 0; i < isNoPayment; i++) {
                    paybackList.add(new PabackModel("", false, false));
                }
            }
            setPaybackAdapter();
        }
    }

    private void setPaybackAdapter() {
        paybackAdapter = new PaybackAdapter(this, paybackList);
        paybackRecycler.setAdapter(paybackAdapter);
    }

    public void selectPaybackDate(int selectedPos) {
        try {
            if (isPreviousDateSelected(selectedPos)) {
                paybackPos = selectedPos;
                DialogFragment newFragment1 = new DatePickerFragmentDialogue();
                Bundle args1 = new Bundle();
                args1.putString(getString(R.string.show), getString(R.string.min_current));
                newFragment1.setArguments(args1);
                newFragment1.show(getSupportFragmentManager(), getString(R.string.date_picker));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void datePickerCallback(String selectedDate, int year, int month, int day, String from) throws ParseException {
        //String formattedDate = day + "/" + month + "/" + year;
        String formattedDate = year + "-" + month + "-" + day;
        if (selectDateValidation(formattedDate)) {
            if (selectDateBackWordValidation(formattedDate)) {
                paybackList.set(paybackPos, new PabackModel(formattedDate, true, false));
                setPaybackAdapter();
                if (paybackPos == 0) {
                    paymentDate = formattedDate;
                    lendTxtAmount.setText(DateFormat.dateFormatConvert(paymentDate));
                }
            } else {
                showSimpleAlert(getString(R.string.Selected_Date_Should_Be_Less_Then) + " " + dateListTitle.get(paybackPos + 1), "");
            }
        } else {
            //showSimpleAlert(getString(R.string.do_not_select_past_date), "");
            showSimpleAlert(getString(R.string.Selected_Date_Should_Be_Greater_Then) + " " + dateListTitle.get(paybackPos - 1), "");
        }
    }

    private boolean selectDateValidation(String formattedDate) {
        boolean isSelect = false;
        if (paybackList.size() > 0 && paybackList.get(0).isAddDate() && paybackPos > 0) {
            if (paybackList.get(paybackPos - 1).isAddDate()) {
                long oldDate = DateFormat.getEpochFromDate(paybackList.get(paybackPos - 1).getPayDate());
                long selectDate = DateFormat.getEpochFromDate(formattedDate);
                if (selectDate > oldDate) {
                    isSelect = true;
                }
            }
        } else {
            isSelect = true;
        }

        return isSelect;
    }


    private boolean selectDateBackWordValidation(String formattedDate) {
        boolean isSelect = false;
        if (paybackList.size() > paybackPos + 1) {
            if (paybackList.get(paybackPos + 1).isAddDate()) {
                long oldDate = DateFormat.getEpochFromDate(paybackList.get(paybackPos + 1).getPayDate());
                long selectDate = DateFormat.getEpochFromDate(formattedDate);
                if (selectDate < oldDate) {
                    isSelect = true;
                } else {
                    isSelect = false;
                }
            } else {
                isSelect = true;
            }
        } else {
            isSelect = true;
        }
        return isSelect;
    }

    private void setContactAdapter() {
        if (contactAdapter != null) {
            contactAdapter.notifyDataSetChanged();
        } else {
            contactAdapter = new ContactAdapter(this, this, contactNumberList);
            contactRecycler.setAdapter(contactAdapter);
        }

        if (transactionModel != null && transactionModel.getId() != null && transactionModel.getId().length() > 0) {
            String userId = SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString();

            if (userId.equals(transactionModel.getToId())) {   //negotiation from transaction
                toId = transactionModel.getFromId();
                int selectPosition = 0;
                for (int i = 0; i < contactNumberList.size(); i++) {
                    //this work for transaction
                    if (transactionModel.getFromId().equals(contactNumberList.get(i).getId())) {
                        selectPosition = i;
                        contactAdapter.setSelected(selectPosition, getString(R.string.negotiation));
                        break;
                    }
                }

            } else if (userId.equals(transactionModel.getFromId())) { //negotiation from history
                toId = transactionModel.getToId();
                int selectPosition = 0;
                for (int i = 0; i < contactNumberList.size(); i++) {
                    //this work for history
                    if (transactionModel.getToId().equals(contactNumberList.get(i).getId())) {
                        selectPosition = i;
                        contactAdapter.setSelected(selectPosition, getString(R.string.negotiation));
                        break;
                    }
                }
            }
        }
    }

    private void setOptionAmount() {
        try {
            String s = lendTermsEdtOption.getText().toString();
            if (s.length() == 0) {
                s = "0";
            }
            SpannableString span1 = new SpannableString(String.valueOf(amount));
            span1.setSpan(new ForegroundColorSpan(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha)), 0, String.valueOf(amount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            String symbol = " ";
            float totalAmount = 0;
            if (isTermsOption == 0) {
                float percentAmount = (amount * Float.parseFloat(s)) / 100;
                totalAmount = amount + percentAmount;
                s = String.valueOf(percentAmount);
                symbol = " + ";
            } else if (isTermsOption == 1) {
                totalAmount = amount + Float.parseFloat(s);
                symbol = " + ";
            } else if (isTermsOption == 2) {
                totalAmount = amount - Float.parseFloat(s);
                symbol = " - ";
            } else if (isTermsOption == 3) {
                totalAmount = amount;
                s = "";
                symbol = "";
            }

            finalTotalAmount = totalAmount;
            finalTotalPayBackAmount = totalAmount;

            Spannable span2 = new SpannableString(s);
            span2.setSpan(new ForegroundColorSpan(CommonMethods.getColorWrapper(this, R.color.textColor)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString span3 = new SpannableString(String.valueOf(totalAmount));
            span3.setSpan(new ForegroundColorSpan(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha)), 0, String.valueOf(totalAmount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            lendTxtAmount.setText(TextUtils.concat(span1, symbol, span2, " = ", span3));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setPaymentAmount() {
        try {
            String s = lendPaymentEdtNo.getText().toString().trim();
            if (s.length() == 0 || s.equals("0")) {
                s = "1";
            }
            SpannableString span1 = new SpannableString(String.valueOf(finalTotalAmount));
            span1.setSpan(new ForegroundColorSpan(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha)), 0, String.valueOf(finalTotalAmount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            isNoPayment = Integer.parseInt(s);

            float totalAmount = finalTotalAmount / isNoPayment;
            String symbol = " / ";

            // finalTotalAmount = totalAmount;//this line comment by ashok
            installmentAmount = totalAmount;
            Spannable span2 = new SpannableString(s);
            span2.setSpan(new ForegroundColorSpan(CommonMethods.getColorWrapper(this, R.color.textColor)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString span3 = new SpannableString(String.valueOf(totalAmount));
            span3.setSpan(new ForegroundColorSpan(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha)), 0, String.valueOf(totalAmount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            lendTxtAmount.setText(TextUtils.concat(span1, symbol, span2, " = ", span3));
            //paybackList.clear();  //comment by ...
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setSelectedAmount() {
        lendTxtAmount.setText(String.valueOf(amount));
    }

    private void setIndicatorView(int value) {
        CommonMethods.closeKeyboard(LendBorrowActivity.this);
        lendShadowBack.setVisibility(View.VISIBLE);
        lendShadowNext.setVisibility(View.VISIBLE);

        lendTxtAmount.setVisibility(View.VISIBLE);

        lendViewAmount.setVisibility(View.GONE);
        lendViewTerms.setVisibility(View.GONE);
        lendViewPayment.setVisibility(View.GONE);
        lendViewPayback.setVisibility(View.GONE);
        lendViewBorrow.setVisibility(View.GONE);
        lendViewLending.setVisibility(View.GONE);
        lendViewContact.setVisibility(View.GONE);
        nextButtonTV.setText(getString(R.string.next));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (value == 0) {
            // lendAmountEdtAmount
            // lendTermsEdtOption
            // lendPaymentEdtNo
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            lendViewAmount.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.amount)));
            lendShadowBack.setVisibility(View.INVISIBLE);
            lendTxtAmount.setTextColor(CommonMethods.getColorWrapper(this, R.color.textColor));
            setSelectedAmount();


        } else if (value == 1) {
            lendViewTerms.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.terms)));
            lendTxtAmount.setTextColor(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha));
            selectedTermsOption(isTermsOption);
            afterAceptRequestNegitiateBackButtonManageFunc(value);

        } else if (value == 2) {
            lendViewPayment.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.no_of_payments)));
            lendTxtAmount.setTextColor(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha));
            setPaymentAmount();
            afterAceptRequestNegitiateBackButtonManageFunc(value);
        } else if (value == 3) {
            lendViewPayback.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(getString(R.string.start_date));
            lendTxtAmount.setTextColor(CommonMethods.getColorWrapper(this, R.color.textColor));
            generatePaybackData();
            afterAceptRequestNegitiateBackButtonManageFunc(value);
        } else if (value == 4) {
            if (isBorrow) {
                lendTxtAmount.setVisibility(View.GONE);
                lendViewBorrow.setVisibility(View.VISIBLE);
                lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.borrow_summary)));
                setBorrowData();

                if (transactionModel != null && transactionModel.getId() != null && transactionModel.getId().length() > 0) {
                    nextButtonTV.setText(getString(R.string.submit));
                }

            } else {
                lendTxtAmount.setVisibility(View.GONE);
                lendViewLending.setVisibility(View.VISIBLE);
                lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.lending_summary)));
                setLendData();
                if (transactionModel != null && transactionModel.getId() != null && transactionModel.getId().length() > 0) {
                    nextButtonTV.setText(getString(R.string.submit));
                }
            }
            afterAceptRequestNegitiateBackButtonManageFunc(value);
        } else if (value == 5) {
            lendTxtAmount.setVisibility(View.GONE);
            lendViewContact.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.select_contact)));
            //setContactAdapter();
            nextButtonTV.setText(getString(R.string.submit));
            afterAceptRequestNegitiateBackButtonManageFunc(value);
        }
    }

    private void afterAceptRequestNegitiateBackButtonManageFunc(int value) {
        if (value == 1) {
            if (transactionModel != null && transactionModel.getStatus() != null) {
                if (transactionModel.getStatus().equals("2") || transactionModel.getIs_negotiate_after_accept().equals("2")) {
                    backButtonTV.setVisibility(View.GONE);
                    lendShadowBack.setVisibility(View.INVISIBLE);
                }
            } else {
                backButtonTV.setVisibility(View.VISIBLE);
                lendShadowBack.setVisibility(View.VISIBLE);
            }
        } else if (value == 4) {
            if (transactionModel != null && transactionModel.getStatus() != null) {
                if (transactionModel.getStatus().equals("2") || transactionModel.getIs_negotiate_after_accept().equals("2")) {
                    zapayCommissionTitleTV.setVisibility(View.INVISIBLE);
                    zapayCommissionTV.setVisibility(View.INVISIBLE);
                    afterCommissionTitleTV.setVisibility(View.INVISIBLE);
                    afterCommissionTV.setVisibility(View.INVISIBLE);

                    zapayCommissionTitleLenderTV.setVisibility(View.INVISIBLE);
                    zapayCommissionLenderTV.setVisibility(View.INVISIBLE);
                    afterCommissionLenderTitleTV.setVisibility(View.INVISIBLE);
                    afterCommissionLenderTV.setVisibility(View.INVISIBLE);
                }
            } else {
                zapayCommissionTitleTV.setVisibility(View.VISIBLE);
                zapayCommissionTV.setVisibility(View.VISIBLE);
                afterCommissionTitleTV.setVisibility(View.VISIBLE);
                afterCommissionTV.setVisibility(View.VISIBLE);

                zapayCommissionTitleLenderTV.setVisibility(View.VISIBLE);
                zapayCommissionLenderTV.setVisibility(View.VISIBLE);
                afterCommissionLenderTitleTV.setVisibility(View.VISIBLE);
                afterCommissionLenderTV.setVisibility(View.VISIBLE);

            }

        } else {
            backButtonTV.setVisibility(View.VISIBLE);
            lendShadowBack.setVisibility(View.VISIBLE);
        }
    }

    private void selectedTermsOption(int isOption) {
        isTermsOption = isOption;
        setOptionAmount();
        lendTermsTxtPercent.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.grey_bg_rounded));
        lendTermsTxtFee.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.grey_bg_rounded));
        lendTermsTxtDiscount.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.grey_bg_rounded));
        lendTermsTxtNone.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.grey_bg_rounded));

        lendTermsTxtPercent.setTextColor(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha));
        lendTermsTxtFee.setTextColor(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha));
        lendTermsTxtDiscount.setTextColor(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha));
        lendTermsTxtNone.setTextColor(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha));
        lendTermsTxtOption.setVisibility(View.VISIBLE);
        lendTermsEdtOption.setVisibility(View.VISIBLE);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.gotha_pro_reg);
        lendTermsTxtPercent.setTypeface(typeface);
        lendTermsTxtFee.setTypeface(typeface);
        lendTermsTxtDiscount.setTypeface(typeface);
        lendTermsTxtNone.setTypeface(typeface);

        Typeface typefaceMed = ResourcesCompat.getFont(this, R.font.gotha_promed);
        if (isOption == 0) {
            lendTermsTxtPercent.setTypeface(typefaceMed);
            lendTermsEdtOption.setHint(getString(R.string.Enter_percent));
            lendTermsTxtOption.setText(getString(R.string.enter_percent));
            lendTermsTxtPercent.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            lendTermsTxtPercent.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.dark_grey_bg_rounded));
        } else if (isOption == 1) {
            lendTermsTxtFee.setTypeface(typefaceMed);
            lendTermsEdtOption.setHint(getString(R.string.Enter_fee));
            lendTermsTxtOption.setText(getString(R.string.enter_fee));
            lendTermsTxtFee.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            lendTermsTxtFee.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.dark_grey_bg_rounded));
        } else if (isOption == 2) {
            lendTermsTxtDiscount.setTypeface(typefaceMed);
            lendTermsEdtOption.setHint(getString(R.string.Enter_discount));
            lendTermsTxtOption.setText(getString(R.string.enter_discount));
            lendTermsTxtDiscount.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            lendTermsTxtDiscount.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.dark_grey_bg_rounded));

        } else if (isOption == 3) {
            lendTermsTxtNone.setTypeface(typefaceMed);
            lendTermsTxtOption.setVisibility(View.GONE);
            lendTermsEdtOption.setVisibility(View.GONE);
            lendTermsTxtNone.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            lendTermsTxtNone.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.dark_grey_bg_rounded));
            // boolean bb = wValidationLib.isValidTerm(lendTermsInputLayout, lendTermsEdtOption, getString(R.string.important), "", false);
        }
    }

    private void setBorrowData() {
        amountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));

        String termValue = lendTermsEdtOption.getText().toString().trim();
        if (isTermsOption + 1 == 1) {
            termTV.setText(termValue + " " + getString(R.string.percent));
        } else if (isTermsOption + 1 == 2) {
            termTV.setText(Const.getCurrency() + termValue + " " + getString(R.string.fee));
        } else if (isTermsOption + 1 == 3) {
            termTV.setText(Const.getCurrency() + termValue + " " + getString(R.string.discount));
        } else if (isTermsOption + 1 == 4) {
            termTV.setText(getString(R.string.none));
        }

        noOfPaymentTV.setText(String.valueOf(isNoPayment));
        paymentDateTV.setText(DateFormat.dateFormatConvert(paybackList.get(0).getPayDate()));
        totalPayBackTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(finalTotalPayBackAmount, 2));


        if (SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().length() > 0
                && SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_VALUE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_VALUE).toString().length() > 0
                && SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().length() > 0
                && SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_VALUE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_VALUE).toString().length() > 0) {

            float lenderChargeValue = Float.parseFloat(SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_VALUE).toString());
            float borrowerChargeValue = Float.parseFloat(SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_VALUE).toString());
            String borrowerChargeType = SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString();

            if (SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().equalsIgnoreCase("percent")) {
                if (SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().equalsIgnoreCase("flat")) {
                    lenderCommission = lenderChargeValue;
                } else if (SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().equalsIgnoreCase("percent")) {
                    lenderCommission = (finalTotalPayBackAmount * lenderChargeValue) / 100;
                }
                borrowerCommission = (finalTotalPayBackAmount * borrowerChargeValue) / 100;


                //*****this code use for temprary
             /*   float newAmount = finalTotalPayBackAmount;
                float previousAmount = Float.parseFloat(transactionModel.getTotalAmount());
                float previousCommission=Float.parseFloat(transactionModel.getAdmin_commission_from_borrower());
                if (newAmount > previousAmount) {
                    float increaseAmount = newAmount - previousAmount;
                    float newAmountCommission = (increaseAmount * borrowerChargeValue) / 100;
                    borrowerCommission = previousCommission + newAmountCommission;
                    Toast.makeText(LendBorrowActivity.this, "Amount increased" + borrowerCommission, Toast.LENGTH_SHORT).show();
                }else {
                    borrowerCommission = (finalTotalPayBackAmount * borrowerChargeValue) / 100;
                }*/

                //*****this code use for temprary

                double afterCommission = finalTotalPayBackAmount - borrowerCommission;
                zapayCommissionTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(borrowerCommission, 2));
                afterCommissionTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(afterCommission, 2));
                zapayCommissionTitleTV.setText(getString(R.string.zapay_commission) + "(" + borrowerChargeValue + ")" + borrowerChargeType);

            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().equalsIgnoreCase("flat")) {
                if (SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().equalsIgnoreCase("flat")) {
                    lenderCommission = lenderChargeValue;
                } else if (SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().equalsIgnoreCase("percent")) {
                    //lenderCommission = (amount * lenderChargeValue) / 100;
                    lenderCommission = (finalTotalPayBackAmount * lenderChargeValue) / 100;
                }

                //float afterCommission = amount - borrowerCommission;
                double afterCommission = finalTotalPayBackAmount - borrowerCommission;
                zapayCommissionTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(borrowerCommission, 2));
                afterCommissionTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(afterCommission, 2));
                zapayCommissionTitleTV.setText(getString(R.string.zapay_commission));
            }
        }
    }

    private void setLendData() {
        //l_amountTV.setText("$" + CommonMethods.roundedDoubleWithoutZero(amount));
        l_amountTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(amount, 2));
        String termValue = lendTermsEdtOption.getText().toString().trim();
        if (isTermsOption + 1 == 1) {
            lTermTV.setText(termValue + " " + getString(R.string.percent));
        } else if (isTermsOption + 1 == 2) {
            lTermTV.setText(Const.getCurrency() + termValue + " " + getString(R.string.fee));
        } else if (isTermsOption + 1 == 3) {
            lTermTV.setText(Const.getCurrency() + termValue + " " + getString(R.string.discount));
        } else if (isTermsOption + 1 == 4) {
            lTermTV.setText(getString(R.string.none));
        }

        lNoOfPaymentTV.setText(String.valueOf(isNoPayment));
        lPaymentDateTV.setText(DateFormat.dateFormatConvert(paybackList.get(0).getPayDate()));
        totalReceivedBackTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(finalTotalPayBackAmount, 2));

        if (SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().length() > 0
                && SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_VALUE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_VALUE).toString().length() > 0
                && SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().length() > 0
                && SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_VALUE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_VALUE).toString().length() > 0) {

            float borrowerChargeValue = Float.parseFloat(SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_VALUE).toString());
            float lenderChargeValue = Float.parseFloat(SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_VALUE).toString());
            String lenderChargeType = SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_VALUE).toString();

            if (SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().equalsIgnoreCase("percent")) {
                if (SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().equalsIgnoreCase("flat")) {
                    borrowerCommission = borrowerChargeValue;
                } else if (SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().equalsIgnoreCase("percent")) {
                    borrowerCommission = (finalTotalPayBackAmount * borrowerChargeValue) / 100;
                }

                lenderCommission = (finalTotalPayBackAmount * lenderChargeValue) / 100;
                double afterCommission = finalTotalPayBackAmount - lenderCommission;

                zapayCommissionLenderTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(lenderCommission, 2));
                afterCommissionLenderTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(afterCommission, 2));
                zapayCommissionTitleLenderTV.setText(getString(R.string.zapay_commission) + "(" + lenderChargeValue + ")" + lenderChargeType);


            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.LENDER_CHARGE_TYPE).toString().equalsIgnoreCase("flat")) {

                if (SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().equalsIgnoreCase("flat")) {
                    borrowerCommission = borrowerChargeValue;
                } else if (SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_TYPE).toString().equalsIgnoreCase("percent")) {
                    borrowerCommission = (finalTotalPayBackAmount * borrowerChargeValue) / 100;
                }
                lenderCommission = lenderChargeValue;

                double afterCommission = finalTotalPayBackAmount - lenderCommission;
                zapayCommissionLenderTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(lenderCommission, 2));
                afterCommissionLenderTV.setText(Const.getCurrency() + CommonMethods.setDigitAfterDecimalValue(afterCommission, 2));
                zapayCommissionTitleLenderTV.setText(getString(R.string.zapay_commission));
            }
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
    protected void onResume() {
        super.onResume();
        setCurrentScreen(100);
    }

    private void callAPIGetContactList(int pageNo) {
        if (pageNo == 0 && scrollListener != null) {
            scrollListener.resetState();
        }

        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "page", pageNo);

        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_contact_list), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(true);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_contact_list), contactRecycler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPITransactionRequest() {
        isTermsOption = isTermsOption + 1;
        //private int isTermsOption, isNoPayment, paybackPos;

        //request_type- 0=new, 1=update
        String transactionId = "";
        String request_type = "0";
        if (transactionModel != null && transactionModel.getId() != null && transactionModel.getId().length() > 0) {
            transactionId = transactionModel.getId();  //this condition for negotiation
            request_type = "1";
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < paybackList.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject();
              /*  if (paybackList.get(i).isDateEpockFormate()) {
                    jsonObject.put("date", paybackList.get(i).getPayDate());
                } else {
                    jsonObject.put("date", DateFormat.getEpochFromDate(paybackList.get(i).getPayDate()));
                }*/

                jsonObject.put("date", paybackList.get(i).getPayDate());
                jsonArray.put(i, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (isTermsOption == 4) {
            termValue = "0";
        } else {
            termValue = lendTermsEdtOption.getText().toString().trim();
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.DEFAULT_FEE_TYPE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.DEFAULT_FEE_TYPE).toString().length() > 0) {
            if (SharedPref.getPrefsHelper().getPref(Const.Var.DEFAULT_FEE_TYPE).toString().equals("flat")) {
                defaultFeeAmount = Float.parseFloat(SharedPref.getPrefsHelper().getPref(Const.Var.DEFAULT_FEE_VALUE).toString());
            } else if (SharedPref.getPrefsHelper().getPref(Const.Var.DEFAULT_FEE_TYPE).toString().equals("percent")) {
                float defaultFee = Float.parseFloat(SharedPref.getPrefsHelper().getPref(Const.Var.DEFAULT_FEE_VALUE).toString());
                float percentValue = (finalTotalPayBackAmount * defaultFee) / 100;
                defaultFeeAmount = finalTotalPayBackAmount + percentValue;
            }
        }

        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "to_id", toId,
                "amount", amount,
                "total_amount", finalTotalPayBackAmount,
                "terms_type", isTermsOption,
                "terms_value", termValue,
                "no_of_payment", isNoPayment,
                "pay_date", jsonArray.toString(),
                "request_by", String.valueOf(request_by),   //1=lender,2=borrower
                "request_type", request_type,
                "transaction_request_id", transactionId,
                "admin_commission_from_lender", CommonMethods.setDigitAfterDecimalValue(lenderCommission, 2),
                "admin_commission_from_borrower", CommonMethods.setDigitAfterDecimalValue(borrowerCommission, 2),
                "default_fee_amount", CommonMethods.setDigitAfterDecimalValue(defaultFeeAmount, 2));
        //Log.e("post", "post data======" + values.toString());

        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_transaction_request), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_transaction_request), contactRecycler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPInegotiateRunningTransactionRequest() {
        isTermsOption = isTermsOption + 1;
        //request_type- 0=new, 1=update
        //  String transactionId = "";

        String request_type = "0";
        String parentTransactionRequestId = "";
        String newTransactionRequestId = "";

        if (transactionModel != null && transactionModel.getId() != null && transactionModel.getId().length() > 0) {
            if (transactionModel.getParent_id() != null && transactionModel.getParent_id().equals("0")) {
                parentTransactionRequestId = transactionModel.getId();
                newTransactionRequestId = "";
                request_type = "0";
            } else if (!transactionModel.getParent_id().equals("0")) {
                parentTransactionRequestId = transactionModel.getParent_id();
                newTransactionRequestId = transactionModel.getId();
                request_type = "1";
            }
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < paybackList.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", paybackList.get(i).getPayDate());
                jsonArray.put(i, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (isTermsOption == 4) {
            termValue = "0";
        } else {
            termValue = lendTermsEdtOption.getText().toString().trim();
        }


        float childAmount = 0;
        float child_total_amount = 0;
        float childCommissionFromBorrower = 0;
        // if (isBorrow) {
        if (transactionModel != null && transactionModel.getRequestBy() != null && transactionModel.getRequestBy().length() > 0 && transactionModel.getRequestBy().equals("2")) {  //borrow mode
            float newTotalAmount = 0;
            if (finalTotalPayBackAmount > 0) {
                newTotalAmount = finalTotalPayBackAmount;
            } else {
                newTotalAmount = Float.parseFloat(transactionModel.getTotalAmount());
            }
            float previousTotalAmount = Float.parseFloat(transactionModel.getTotalAmount());
            float borrowerChargeValue = Float.parseFloat(SharedPref.getPrefsHelper().getPref(Const.Var.BORROWER_CHARGE_VALUE).toString());

            if (newTotalAmount > previousTotalAmount) {
                child_total_amount = newTotalAmount - previousTotalAmount;
                childCommissionFromBorrower = (child_total_amount * borrowerChargeValue) / 100;
                // Toast.makeText(LendBorrowActivity.this, "Amount increased" + childCommissionFromBorrower, Toast.LENGTH_SHORT).show();
            }

            float newAmount = amount;
            float previousAmount = Float.parseFloat(transactionModel.getAmount());
            if (newAmount > previousAmount) {
                childAmount = newAmount - previousAmount;
            }
        }

        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "to_id", toId,
                "amount", amount,
                "total_amount", finalTotalPayBackAmount,
                "terms_type", isTermsOption,
                "terms_value", termValue,
                "no_of_payment", isNoPayment,
                "pay_date", jsonArray.toString(),
                "request_by", String.valueOf(request_by),   //1=lender,2=borrower
                "request_type", request_type,
                "parent_transaction_request_id", parentTransactionRequestId,
                "new_transaction_request_id", newTransactionRequestId,

                "child_amount", childAmount,
                "child_total_amount", child_total_amount,
                "default_fee_amount", CommonMethods.setDigitAfterDecimalValue(defaultFeeAmount, 2));
        //"child_admin_commission_from_borrower", childCommissionFromBorrower

        Log.e("post", "negotiateRunningTransactionRequest post data======" + values.toString());
        Log.e("post", "negotiateRunningTransactionRequest getRequestBy======" + transactionModel.getRequestBy());

        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_negotiate_running_transaction_request), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_negotiate_running_transaction_request), contactRecycler);
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

            if (from.equals(getResources().getString(R.string.api_get_contact_list))) {
                if (status == 200) {
                    if (pageNo == 0) {
                        contactNumberList.clear();
                    }

                    List<ContactModel> list = apiCalling.getDataList(json, "data", ContactModel.class);
                    if (list.size() > 0) {
                        noDataTv.setVisibility(View.GONE);
                        contactRecycler.setVisibility(View.VISIBLE);
                        contactNumberList.addAll(list);
                        setContactAdapter();
                    } else {
                        if (pageNo == 0) {
                            noDataTv.setVisibility(View.VISIBLE);
                            contactRecycler.setVisibility(View.GONE);
                        }
                    }

                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    if (pageNo == 0) {
                        noDataTv.setVisibility(View.VISIBLE);
                        contactRecycler.setVisibility(View.GONE);
                        showSimpleAlert(msg, "");
                    }
                }

            } else if (from.equals(getResources().getString(R.string.api_transaction_request))) {
                if (status == 200) {
                    //showSimpleAlert(msg, getResources().getString(R.string.api_transaction_request));
                    Intent intent = new Intent(LendBorrowActivity.this, CongratulationActivity.class);
                    intent.putExtra("message", msg);
                    startActivity(intent);
                    finish();
                } else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_content))) {
                JsonObject jsonObject = json.get("data").getAsJsonObject();
                if (jsonObject != null && jsonObject.has("page_description") && jsonObject.get("page_description") != null && jsonObject.get("page_description").getAsString().length() > 0) {
                    privacyPolicyDialog(jsonObject.get("page_description").getAsString());
                }
            } else if (from.equals(getResources().getString(R.string.api_negotiate_running_transaction_request))) {
                Log.e("response", "api_negotiate_running_transaction_request==" + json);
                if (status == 200) {
                    Intent intent = new Intent(LendBorrowActivity.this, CongratulationActivity.class);
                    intent.putExtra("message", msg);
                    startActivity(intent);
                    finish();
                } else if (status == 402) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                }
            }
        }
    }

    @Override
    public void getContact(ContactModel contactModel) {
        toId = contactModel.getId();
    }

    private void callAPIGetContentDisclaimer() {
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "content_type", "disclaimer");
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_get_content), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_content), contactRecycler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void privacyPolicyDialog(String discription) {
        Dialog dialog = new Dialog(LendBorrowActivity.this);
        dialog.setContentView(R.layout.privacy_policy_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            int w = CommonMethods.getScreenWidth() - 100;
            dialog.getWindow().setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT);
            // dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
        TextView discriptionTV = dialog.findViewById(R.id.discriptionTV);
        discriptionTV.setText(discription);
        TextView okTV = dialog.findViewById(R.id.okTV);
        TextView cancelTV = dialog.findViewById(R.id.cancelTV);
        CheckBox mChkAgree = dialog.findViewById(R.id.mChkAgree);
        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChkAgree.isChecked()) {
                    callAPITransactionRequest();
                    dialog.dismiss();
                } else {
                    showSimpleAlert(getString(R.string.accept_disclaimer), "");
                }
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
