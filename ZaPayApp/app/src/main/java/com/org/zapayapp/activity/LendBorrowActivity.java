package com.org.zapayapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.ContactAdapter;
import com.org.zapayapp.adapters.IndicatorAdapter;
import com.org.zapayapp.adapters.PaybackAdapter;
import com.org.zapayapp.listener.ContactListener;
import com.org.zapayapp.model.ContactModel;
import com.org.zapayapp.model.PabackModel;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
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
    private EditText lendAmountEdtAmount, lendTermsEdtOption, lendPaymentEdtNo;
    private LinearLayout lendViewAmount, lendViewTerms, lendViewPayment, lendViewPayback, lendViewBorrow, lendViewLending, lendViewContact;

    private int isTermsOption, isNoPayment, paybackPos;

    private float finalTotalAmount;//instalment amount
    private float installmentAmount;//instalment amount
    private float amount;   //enter amount
    private float finalTotalPayBackAmount;//after add term amount
    private String paymenetDate;


    private WVDateLib wvDateLib;
    private RecyclerView paybackRecycler, contactRecycler;
    // private List<String> paybackList;
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
    private TextView amountTV;
    private TextView termTV;
    private TextView noOfPaymentTV;
    private TextView paymentDateTV;
    private TextView totalPayBackTV;

    //Lending....
    TextView l_amountTV;
    TextView lTermTV;
    TextView lNoOfPaymentTV;
    TextView lPaymentDateTV;
    TextView totalReceivedBackTV;


    // lendAmountEdtAmount
    // lendTermsEdtOption
    // lendPaymentEdtNo

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

        listIndicator = new ArrayList<>();
        wvDateLib = new WVDateLib(this);
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
        listIndicator.add(getString(R.string.select_contact));

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
    }

    private void getIntentValues() {
        intent = getIntent();
        if (intent != null) {
            isBorrow = intent.getBooleanExtra("isBorrow", false);
        }
    }

    private void initAmountView() {
        lendViewAmount = findViewById(R.id.lendViewAmount);
        lendAmountEdtAmount = findViewById(R.id.lendAmountEdtAmount);
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
                Log.e("CharSequence", "CharSequence==========" + s);
                if (s != null & s.length() > 0) {
                    amount = Float.parseFloat(s.toString());
                    lendTxtAmount.setText(s);
                } else {
                    amount = Float.parseFloat("0");
                    lendTxtAmount.setText("");
                }
            }
        });
    }

    private void initTermsView() {
        lendViewTerms = findViewById(R.id.lendViewTerms);
        lendTermsEdtOption = findViewById(R.id.lendTermsEdtOption);
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
    }

    private void initPaymentView() {
        lendViewPayment = findViewById(R.id.lendViewPayment);
        lendPaymentEdtNo = findViewById(R.id.lendPaymentEdtNo);
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
    }

    private void initPaybackView() {
        lendViewPayback = findViewById(R.id.lendViewPayback);
        paybackRecycler = findViewById(R.id.paybackRecycler);
        paybackRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        paybackRecycler.setItemAnimator(new DefaultItemAnimator());

        paybackList = new ArrayList<>();
    }


    private void initBorrowView() {
        lendViewBorrow = findViewById(R.id.lendViewBorrow);
        amountTV = findViewById(R.id.amountTV);
        termTV = findViewById(R.id.termTV);
        noOfPaymentTV = findViewById(R.id.noOfPaymentTV);
        paymentDateTV = findViewById(R.id.paymentDateTV);
        totalPayBackTV = findViewById(R.id.totalPayBackTV);

       /* amountTV.setText(String.valueOf(amount));
        String termValue= lendTermsEdtOption.getText().toString().trim();
        if (isTermsOption + 1==1){
           // termTV.setText("@14% or $7.00");
            termTV.setText("@"+termValue+"% or &"+finalTotalAmount);
        }else if (isTermsOption + 1==2){
            termTV.setText("@"+termValue+"Fee or &"+finalTotalAmount);
        }else if (isTermsOption + 1==3){
            termTV.setText("@"+termValue+"Discount or &"+finalTotalAmount);
        }else if (isTermsOption + 1==4){
            termTV.setText("@"+termValue+"None or &"+finalTotalAmount);
        }

        noOfPaymentTV.setText(String.valueOf(isNoPayment));
        paymentDateTV.setText(paymenetDate);
        totalPayBackTV.setText(String.valueOf(finalTotalPayBackAmount));*/
    }

    private void initLendingView() {
        lendViewLending = findViewById(R.id.lendViewLending);
        l_amountTV = findViewById(R.id.l_amountTV);
        lTermTV = findViewById(R.id.lTermTV);
        lNoOfPaymentTV = findViewById(R.id.lNoOfPaymentTV);
        lPaymentDateTV = findViewById(R.id.lPaymentDateTV);
        totalReceivedBackTV = findViewById(R.id.totalReceivedBackTV);
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
                    //selectedPos = indicatorAdapter.getSelectedPos() + 1;
                    //indicatorAdapter.setSelected(selectedPos);
                    //setIndicatorView(selectedPos);

                    if (selectedPos == 0) {
                        if (!lendAmountEdtAmount.getText().toString().trim().isEmpty()) {
                            selectedPos = indicatorAdapter.getSelectedPos() + 1;
                            indicatorAdapter.setSelected(selectedPos);
                            setIndicatorView(selectedPos);
                        } else {
                            //Toast.makeText(LendBorrowActivity.this,"eneter amount",Toast.LENGTH_SHORT).show();
                            showSimpleAlert(getString(R.string.enter_amount), "");
                        }
                    } else if (selectedPos == 1) {
                        if (!lendTermsEdtOption.getText().toString().trim().isEmpty() || isTermsOption == 3) {
                            selectedPos = indicatorAdapter.getSelectedPos() + 1;
                            indicatorAdapter.setSelected(selectedPos);
                            setIndicatorView(selectedPos);
                        } else {
                            //Toast.makeText(LendBorrowActivity.this,"eneter term",Toast.LENGTH_SHORT).show();
                            showSimpleAlert(getString(R.string.enter_term), "");
                        }
                    } else if (selectedPos == 2) {
                        if (!lendPaymentEdtNo.getText().toString().trim().isEmpty()) {
                            selectedPos = indicatorAdapter.getSelectedPos() + 1;
                            indicatorAdapter.setSelected(selectedPos);
                            setIndicatorView(selectedPos);
                        } else {
                            showSimpleAlert(getString(R.string.enter_no_of_payment), "");
                        }
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


                } else if (selectedPos == 5) {
                   /* intent = new Intent(LendBorrowActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();*/
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


    private void transactionRequestFunc() {
       /* if (!toId.equalsIgnoreCase("")) {
            if (SharedPref.getPrefsHelper().getPref(Const.Var.BANKACCOUNT_ID) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BANKACCOUNT_ID).toString().length() > 0) {
                if (SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS) != null && SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS).toString().length() > 0
                        && SharedPref.getPrefsHelper().getPref(Const.Var.BANK_ACCOUNT_STATUS).toString().equalsIgnoreCase("verified")) {
                    callAPITransactionRequest();
                } else {
                    showSimpleAlert(getString(R.string.please_verify_bank_account), "");
                }
            } else {
                showSimpleAlert(getString(R.string.please_add_bank_account), "");
            }

        } else {
            showSimpleAlert(getString(R.string.please_select_contact), "");
        }
*/


        //activity_status=0  //signup
        //activity_status=1  //updated profile
        //activity_status=2   //added bank account
        //activity_status=3   //verifyed bank account(ready to send request)


        if (!toId.equalsIgnoreCase("")) {
            if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("2")) {
                if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("3")) {
                    callAPITransactionRequest();
                } else {
                    showSimpleAlert(getString(R.string.please_verify_bank_account), "");
                }
            } else {
                showSimpleAlert(getString(R.string.please_add_bank_account), "");
            }
        } else {
            showSimpleAlert(getString(R.string.please_select_contact), "");
        }
    }

    private void generatePaybackData() {
        paymenetDate = wvDateLib.getCurrentDate();
        lendTxtAmount.setText(paymenetDate);

        paybackList.clear();
        for (int i = 0; i < isNoPayment; i++) {
            paybackList.add(new PabackModel("", false));
        }
        setPaybackAdapter();
    }

    private void setPaybackAdapter() {
        paybackAdapter = new PaybackAdapter(this, paybackList);
        paybackRecycler.setAdapter(paybackAdapter);
    }

    public void selectPaybackDate(int selectedPos) {
        try {
            paybackPos = selectedPos;
            DialogFragment newFragment1 = new DatePickerFragmentDialogue();
            Bundle args1 = new Bundle();
            args1.putString(getString(R.string.show), getString(R.string.min_current));
            newFragment1.setArguments(args1);
            newFragment1.show(getSupportFragmentManager(), getString(R.string.date_picker));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void datePickerCallback(String selectedDate, int year, int month, int day, String from) throws ParseException {
        // String formattedDate = year+"-"+month+"-"+day;
        String formattedDate = day + "/" + month + "/" + year;
        Log.e("formattedDate", "formattedDate===" + formattedDate);
        // paybackList.set(paybackPos, selectedDate);
        paybackList.set(paybackPos, new PabackModel(formattedDate, true));
        setPaybackAdapter();
        if (paybackPos == 0) {
            paymenetDate = formattedDate;
            lendTxtAmount.setText(paymenetDate);


        }
    }

    private void setContactAdapter() {
        if (contactAdapter != null) {
            contactAdapter.notifyDataSetChanged();
        } else {
            contactAdapter = new ContactAdapter(this, this, contactNumberList);
            contactRecycler.setAdapter(contactAdapter);
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
            paybackList.clear();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setSelectedAmount() {
        lendTxtAmount.setText(String.valueOf(amount));

    }

    private void setIndicatorView(int value) {
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

        } else if (value == 2) {
            lendViewPayment.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.no_of_payments)));
            lendTxtAmount.setTextColor(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha));
            setPaymentAmount();
        } else if (value == 3) {
            lendViewPayback.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(getString(R.string.start_date));
            lendTxtAmount.setTextColor(CommonMethods.getColorWrapper(this, R.color.textColor));
            generatePaybackData();
        } else if (value == 4) {
            if (isBorrow) {
                lendTxtAmount.setVisibility(View.GONE);
                lendViewBorrow.setVisibility(View.VISIBLE);
                lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.borrow_summary)));
                setBorrowData();
            } else {
                lendTxtAmount.setVisibility(View.GONE);
                lendViewLending.setVisibility(View.VISIBLE);
                lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.lending_summary)));
                setLendData();
            }
        } else if (value == 5) {
            lendTxtAmount.setVisibility(View.GONE);
            lendViewContact.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.select_contact)));
            //setContactAdapter();
            nextButtonTV.setText(getString(R.string.submit));
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
        }
    }

    private void setBorrowData() {
        amountTV.setText(String.valueOf(amount));
        String termValue = lendTermsEdtOption.getText().toString().trim();
        if (isTermsOption + 1 == 1) {
            // termTV.setText("@14% or $7.00");
            // termTV.setText("@"+termValue+"% or &"+finalTotalAmount);
            termTV.setText(termValue + " %");
        } else if (isTermsOption + 1 == 2) {
            termTV.setText(termValue + " Fee");
        } else if (isTermsOption + 1 == 3) {
            termTV.setText(termValue + " Discount");
        } else if (isTermsOption + 1 == 4) {
            termTV.setText(termValue + " None");
        }

        noOfPaymentTV.setText(String.valueOf(isNoPayment));
        paymentDateTV.setText(paymenetDate);
        totalPayBackTV.setText(String.valueOf(finalTotalPayBackAmount));
    }

    private void setLendData() {
        l_amountTV.setText(String.valueOf(amount));
        String termValue = lendTermsEdtOption.getText().toString().trim();
        if (isTermsOption + 1 == 1) {
            // termTV.setText("@14% or $7.00");
            // termTV.setText("@"+termValue+"% or &"+finalTotalAmount);
            lTermTV.setText(termValue + " %");
        } else if (isTermsOption + 1 == 2) {
            lTermTV.setText(termValue + " Fee");
        } else if (isTermsOption + 1 == 3) {
            lTermTV.setText(termValue + " Discount");
        } else if (isTermsOption + 1 == 4) {
            lTermTV.setText(termValue + " None");
        }

        lNoOfPaymentTV.setText(String.valueOf(isNoPayment));
        lPaymentDateTV.setText(paymenetDate);
        totalReceivedBackTV.setText(String.valueOf(finalTotalPayBackAmount));
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
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_contact_list), contactRecycler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callAPITransactionRequest() {
        isTermsOption = isTermsOption + 1;
        //private int isTermsOption, isNoPayment, paybackPos;//ashok


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


        Log.e("transactionRequest", "to_id===" + toId);
        Log.e("transactionRequest", "amount===" + amount);
        Log.e("transactionRequest", "total_amount===" + finalTotalAmount);
        Log.e("transactionRequest", "terms_type===" + isTermsOption);
        Log.e("transactionRequest", "terms_value===" + lendTermsEdtOption.getText().toString().trim());
        Log.e("transactionRequest", "no_of_payment===" + isNoPayment);
        Log.e("transactionRequest", "pay_date===" + jsonArray);
        Log.e("transactionRequest", "request_by===" + String.valueOf(request_by));
        Log.e("transactionRequest", "request_type===" + 0);
        Log.e("transactionRequest", "transaction_request_id===");


        if (isTermsOption == 4) {
            termValue = "0";
        } else {
            termValue = lendTermsEdtOption.getText().toString().trim();
        }

        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "to_id", toId,
                "amount", amount,
                "total_amount", finalTotalPayBackAmount,
                "terms_type", isTermsOption,
                "terms_value", termValue,
                "no_of_payment", isNoPayment,
                "pay_date", jsonArray.toString(),
                "request_by", String.valueOf(request_by),
                "request_type", "0",
                "transaction_request_id", "");

        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_transaction_request), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_transaction_request), contactRecycler);
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

                } else {
                    if (pageNo == 0) {
                        noDataTv.setVisibility(View.VISIBLE);
                        contactRecycler.setVisibility(View.GONE);
                        showSimpleAlert(msg, "");
                    }

                }
            } else if (from.equals(getResources().getString(R.string.api_transaction_request))) {
                if (status == 200) {

                    showSimpleAlert(msg, getResources().getString(R.string.api_transaction_request));
                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }

    @Override
    public void getContact(ContactModel contactModel) {
        toId = contactModel.getId();

    }
}
