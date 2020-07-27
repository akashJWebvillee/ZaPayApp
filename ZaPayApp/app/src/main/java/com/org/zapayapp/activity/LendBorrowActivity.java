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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dd.ShadowLayout;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.ContactAdapter;
import com.org.zapayapp.adapters.IndicatorAdapter;
import com.org.zapayapp.adapters.PaybackAdapter;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.DatePickerFragmentDialogue;
import com.org.zapayapp.utils.WVDateLib;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LendBorrowActivity extends BaseActivity implements View.OnClickListener, DatePickerFragmentDialogue.DatePickerCallback {

    private RecyclerView indicatorRecycler;
    private List<String> listIndicator;
    private TextView nextButtonTV, backButtonTV;
    private ShadowLayout lendShadowBack, lendShadowNext;
    private int selectedPos = 0;
    private IndicatorAdapter indicatorAdapter;
    private TextView lendTxtHeader, lendTxtAmount, lendTermsTxtOption, lendTermsTxtPercent, lendTermsTxtFee, lendTermsTxtDiscount, lendTermsTxtNone;
    private EditText lendAmountEdtAmount, lendTermsEdtOption, lendPaymentEdtNo;
    private LinearLayout lendViewAmount, lendViewTerms, lendViewPayment, lendViewPayback, lendViewBorrow, lendViewLending, lendViewContact;
    private int amount, isTermsOption, isNoPayment, paybackPos;
    private float finalTotalAmount;
    private WVDateLib wvDateLib;
    private RecyclerView paybackRecycler, contactRecycler;
    private List<String> paybackList, contactList;
    private PaybackAdapter paybackAdapter;
    private ContactAdapter contactAdapter;
    private Intent intent;
    private boolean isBorrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_borrow);
        init();
        initAction();
        getIntentValues();
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
        listIndicator.add(getString(R.string.borrow_summary_));
        listIndicator.add(getString(R.string.lending_summary_));
        listIndicator.add(getString(R.string.select_contact));

        indicatorAdapter = new IndicatorAdapter(this, listIndicator);
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

    private void getIntentValues(){
        intent = getIntent();
        if(intent != null){
            isBorrow = intent.getBooleanExtra("isBorrow",false);
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
                amount = Integer.parseInt(s.toString());
                lendTxtAmount.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
    }

    private void initLendingView() {
        lendViewLending = findViewById(R.id.lendViewLending);
    }

    private void initContactView() {
        lendViewContact = findViewById(R.id.lendViewContact);
        contactRecycler = findViewById(R.id.contactRecycler);
        contactRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        contactRecycler.setItemAnimator(new DefaultItemAnimator());

        contactList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButtonTV:
                if (indicatorAdapter.getSelectedPos() < listIndicator.size() - 1) {
                    selectedPos = indicatorAdapter.getSelectedPos() + 1;
                    indicatorAdapter.setSelected(selectedPos);
                    setIndicatorView(selectedPos);
                } else if (selectedPos == 6) {
                    intent = new Intent(LendBorrowActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
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

    private void generatePaybackData() {
        lendTxtAmount.setText(wvDateLib.getCurrentDate());
        paybackList.clear();
        for (int i = 0; i < isNoPayment; i++) {
            paybackList.add("");
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
        paybackList.set(paybackPos, selectedDate);
        setPaybackAdapter();
    }

    private void setContactAdapter() {
        contactList.clear();
        for (int i = 0; i < 10; i++) {
            contactList.add("");
        }
        contactAdapter = new ContactAdapter(this, contactList);
        contactRecycler.setAdapter(contactAdapter);
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
            String s = lendPaymentEdtNo.getText().toString();
            if (s.length() == 0) {
                s = "1";
            }
            SpannableString span1 = new SpannableString(String.valueOf(finalTotalAmount));
            span1.setSpan(new ForegroundColorSpan(CommonMethods.getColorWrapper(this, R.color.navTextColor50Alpha)), 0, String.valueOf(finalTotalAmount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            isNoPayment = Integer.parseInt(s);

            float totalAmount = finalTotalAmount / isNoPayment;
            String symbol = " / ";

            finalTotalAmount = totalAmount;
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

        if (value == 0) {
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
            lendTxtAmount.setVisibility(View.GONE);
            lendViewBorrow.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.borrow_summary)));
        } else if (value == 5) {
            lendTxtAmount.setVisibility(View.GONE);
            lendViewLending.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.lending_summary)));
        } else if (value == 6) {
            lendTxtAmount.setVisibility(View.GONE);
            lendViewContact.setVisibility(View.VISIBLE);
            lendTxtHeader.setText(CommonMethods.capitalize(getString(R.string.select_contact)));
            setContactAdapter();
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
            lendTermsEdtOption.setHint(getString(R.string.enter_percent));
            lendTermsTxtOption.setText(getString(R.string.enter_percent));
            lendTermsTxtPercent.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            lendTermsTxtPercent.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.dark_grey_bg_rounded));
        } else if (isOption == 1) {
            lendTermsTxtFee.setTypeface(typefaceMed);
            lendTermsEdtOption.setHint(getString(R.string.enter_fee));
            lendTermsTxtOption.setText(getString(R.string.enter_fee));
            lendTermsTxtFee.setTextColor(CommonMethods.getColorWrapper(this, R.color.colorWhite));
            lendTermsTxtFee.setBackground(CommonMethods.getDrawableWrapper(this, R.drawable.dark_grey_bg_rounded));
        } else if (isOption == 2) {
            lendTermsTxtDiscount.setTypeface(typefaceMed);
            lendTermsEdtOption.setHint(getString(R.string.enter_discount));
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
}
