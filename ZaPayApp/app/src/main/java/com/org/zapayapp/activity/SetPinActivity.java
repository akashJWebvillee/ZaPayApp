package com.org.zapayapp.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.org.zapayapp.R;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;

public class SetPinActivity extends BaseActivity {

    private EditText et1, et2, et3, et4, et5, et6;
    private EditText[] editTexts;
    private String pin = "";

    private EditText cEt1, cEt2, cEt3, cEt4, cEt5, cEt6;
    private EditText[] cEditTexts;
    private String cPin = "";


    private String forWhat;
    private TextView confirmPassTitleTV;
    private TextView forgetPinTV;
    private LinearLayout confirmPassLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);
        inIt();
        getIntentFunc();
        initAction();
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    private void inIt() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);

        cEt1 = findViewById(R.id.cEt1);
        cEt2 = findViewById(R.id.cEt2);
        cEt3 = findViewById(R.id.cEt3);
        cEt4 = findViewById(R.id.cEt4);
        cEt5 = findViewById(R.id.cEt5);
        cEt6 = findViewById(R.id.cEt6);

        forgetPinTV = findViewById(R.id.forgetPinTV);
        confirmPassTitleTV = findViewById(R.id.confirmPassTitleTV);
        confirmPassLL = findViewById(R.id.confirmPassLL);

    }

    private void getIntentFunc() {
        if (getIntent().getStringExtra("forWhat") != null && getIntent().getStringExtra("forWhat").length() > 0) {
            forWhat = getIntent().getStringExtra("forWhat");
            confirmPassTitleTV.setVisibility(View.VISIBLE);
            confirmPassLL.setVisibility(View.VISIBLE);
        }

        if (forWhat.equalsIgnoreCase(getString(R.string.set_new_pin))){
            confirmPassTitleTV.setVisibility(View.VISIBLE);
            confirmPassLL.setVisibility(View.VISIBLE);
            forgetPinTV.setVisibility(View.GONE);
        }else if (forWhat.equalsIgnoreCase(getString(R.string.update_pin))){
            confirmPassTitleTV.setVisibility(View.VISIBLE);
            confirmPassLL.setVisibility(View.VISIBLE);
            forgetPinTV.setVisibility(View.GONE);
        }else if (forWhat.equalsIgnoreCase(getString(R.string.check_pin))){
            confirmPassTitleTV.setVisibility(View.GONE);
            confirmPassLL.setVisibility(View.GONE);
            forgetPinTV.setVisibility(View.VISIBLE);
        }
    }


    private void initAction() {
        editTexts = new EditText[]{et1, et2, et3, et4, et5, et6};
        et1.addTextChangedListener(new PinTextWatcher(0));
        et2.addTextChangedListener(new PinTextWatcher(1));
        et3.addTextChangedListener(new PinTextWatcher(2));
        et4.addTextChangedListener(new PinTextWatcher(3));
        et5.addTextChangedListener(new PinTextWatcher(4));
        et6.addTextChangedListener(new PinTextWatcher(5));

        et1.setOnKeyListener(new PinOnKeyListener(0));
        et2.setOnKeyListener(new PinOnKeyListener(1));
        et3.setOnKeyListener(new PinOnKeyListener(2));
        et4.setOnKeyListener(new PinOnKeyListener(3));
        et5.setOnKeyListener(new PinOnKeyListener(4));
        et6.setOnKeyListener(new PinOnKeyListener(5));

        //for confirm pin
        cEditTexts = new EditText[]{cEt1, cEt2, cEt3, cEt4, cEt5, cEt6};
        cEt1.addTextChangedListener(new cPinTextWatcher(0));
        cEt2.addTextChangedListener(new cPinTextWatcher(1));
        cEt3.addTextChangedListener(new cPinTextWatcher(2));
        cEt4.addTextChangedListener(new cPinTextWatcher(3));
        cEt5.addTextChangedListener(new cPinTextWatcher(4));
        cEt6.addTextChangedListener(new cPinTextWatcher(5));

        cEt1.setOnKeyListener(new cPinOnKeyListener(0));
        cEt2.setOnKeyListener(new cPinOnKeyListener(1));
        cEt3.setOnKeyListener(new cPinOnKeyListener(2));
        cEt4.setOnKeyListener(new cPinOnKeyListener(3));
        cEt5.setOnKeyListener(new cPinOnKeyListener(4));
        cEt6.setOnKeyListener(new cPinOnKeyListener(5));

        forgetPinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForceUpdate(getString(R.string.session_expired),getString(R.string.forget_pin_msg), false, getString(R.string.cancel), false);
            }
        });

    }


    /**
     * The type Pin on key listener.
     */

    class PinOnKeyListener implements View.OnKeyListener {
        private int currentIndex;

        /**
         * Instantiates a new Pin on key listener.
         *
         * @param currentIndex the current index
         */

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                //select all the text after the cursor
                int cursorPosition = editTexts[currentIndex].getSelectionStart();

                if (currentIndex != 0 && cursorPosition == 0) {
                    editTexts[currentIndex - 1].setText("");
                    editTexts[currentIndex - 1].requestFocus();
                }
            }
            return false;
        }
    }

    public class PinTextWatcher implements TextWatcher {
        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";
        private String beforeTypedString = "";
        private String aterTypedString = "";
        private int beforeCount = 0;
        private int afterCount = 0;
        private boolean isPaste;


        /**
         * Instantiates a new Pin text watcher.
         *
         * @param currentIndex the current index
         */

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeCount = count;
            beforeTypedString = s.toString().trim();
            showToast("beforeTextChanged " + currentIndex + " CharSequence " + s + " start " + start + " beforeCount " + beforeCount + " after " + after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            afterCount = count;
            showToast("onTextChanged " + currentIndex + " CharSequence " + s + " start " + start + " afterCount " + afterCount + " before " + before);
            aterTypedString = s.toString().trim();
            if (s.length() > 1) {
                isPaste = true;
            }

            if (aterTypedString.length() >= 2) {
                newTypedString = aterTypedString.charAt(0) + "";
            } else
                newTypedString = s.subSequence(start, start + count).toString().trim();

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = newTypedString;


            /* Detect paste event and set first char */

            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0 && beforeCount == 0) {
                moveToPrevious();
            }

            checkPin();

            int j = currentIndex + 1;
            for (int i = 1; i < aterTypedString.length(); i++) {
                if (j < editTexts.length) {
                    editTexts[j++].setText(aterTypedString.charAt(i) + "");
                }
            }
        }


        private void moveToNext() {
            if (!isLast) {
                editTexts[currentIndex + 1].requestFocus();
            }

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                moveToNextScreen();
            }
        }

        private void moveToPrevious() {
            if (!isFirst) {
                editTexts[currentIndex - 1].requestFocus();
            }
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }
    }


    private boolean checkPin() {
        boolean flag = false;
        pin = et1.getText().toString().trim() +
                et2.getText().toString().trim() +
                et3.getText().toString().trim() +
                et4.getText().toString().trim() +
                et5.getText().toString().trim() +
                et6.getText().toString().trim();

        if (pin.length() == 6) {
            flag = true;
        }
        return flag;
    }


    private void showToast(String msg) {
        Log.d("OTPRes-----", msg);
    }

    private void moveToNextScreen() {
        if (forWhat != null&&forWhat.equalsIgnoreCase(getString(R.string.check_pin))) {
            if (SharedPref.getPrefsHelper().getPref(Const.Var.PIN) != null && SharedPref.getPrefsHelper().getPref(Const.Var.PIN).toString().length() > 0) {
                String savePin = SharedPref.getPrefsHelper().getPref(Const.Var.PIN).toString();
                pin = et1.getText().toString().trim() + et2.getText().toString().trim() + et3.getText().toString().trim() + et4.getText().toString().trim() + et5.getText().toString().trim() + et6.getText().toString().trim();
                if (savePin.equals(pin)) {
                    Intent intent = new Intent(SetPinActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }else {
                    showSimpleAlert(getString(R.string.wrong_pin),"");
                    clearPin();
                }
            }
        }
    }


    //from here use confirm pin code
    class cPinOnKeyListener implements View.OnKeyListener {
        private int currentIndex;

        cPinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                //select all the text after the cursor
                int cursorPosition = cEditTexts[currentIndex].getSelectionStart();

                if (currentIndex != 0 && cursorPosition == 0) {
                    cEditTexts[currentIndex - 1].setText("");
                    cEditTexts[currentIndex - 1].requestFocus();
                }
            }
            return false;
        }
    }

    public class cPinTextWatcher implements TextWatcher {
        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";
        private String beforeTypedString = "";
        private String aterTypedString = "";
        private int beforeCount = 0;
        private int afterCount = 0;
        private boolean isPaste;

        cPinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == cEditTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeCount = count;
            beforeTypedString = s.toString().trim();
            showToast("beforeTextChanged " + currentIndex + " CharSequence " + s + " start " + start + " beforeCount " + beforeCount + " after " + after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            afterCount = count;
            showToast("onTextChanged " + currentIndex + " CharSequence " + s + " start " + start + " afterCount " + afterCount + " before " + before);
            aterTypedString = s.toString().trim();
            if (s.length() > 1) {
                isPaste = true;
            }

            if (aterTypedString.length() >= 2) {
                newTypedString = aterTypedString.charAt(0) + "";
            } else
                newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            cEditTexts[currentIndex].removeTextChangedListener(this);
            cEditTexts[currentIndex].setText(text);
            cEditTexts[currentIndex].setSelection(text.length());
            cEditTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0 && beforeCount == 0) {
                moveToPrevious();
            }

            checkConfirmPin();

            int j = currentIndex + 1;
            for (int i = 1; i < aterTypedString.length(); i++) {
                if (j < cEditTexts.length) {
                    cEditTexts[j++].setText(aterTypedString.charAt(i) + "");
                }
            }
        }


        private void moveToNext() {
            if (!isLast) {
                cEditTexts[currentIndex + 1].requestFocus();
            }

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                moveToNextScreenConfirmPin();
            }
        }

        private void moveToPrevious() {
            if (!isFirst) {
                cEditTexts[currentIndex - 1].requestFocus();
            }
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : cEditTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }
    }

    private boolean checkConfirmPin() {
        boolean flag = false;
        cPin = cEt1.getText().toString().trim() +
                cEt2.getText().toString().trim() +
                cEt3.getText().toString().trim() +
                cEt4.getText().toString().trim() +
                cEt5.getText().toString().trim() +
                cEt6.getText().toString().trim();

        if (cPin.length() == 6) {
            flag = true;
        }
        return flag;
    }

    private void moveToNextScreenConfirmPin() {
        pin = "";
        cPin = "";
        pin = et1.getText().toString().trim() + et2.getText().toString().trim() + et3.getText().toString().trim() + et4.getText().toString().trim() + et5.getText().toString().trim() + et6.getText().toString().trim();
        cPin = cEt1.getText().toString().trim() + cEt2.getText().toString().trim() + cEt3.getText().toString().trim() + cEt4.getText().toString().trim() + cEt5.getText().toString().trim() + cEt6.getText().toString().trim();

        if (!pin.equals("")) {
            if (pin.equals(cPin)) {
                SharedPref.getPrefsHelper().savePref(Const.Var.PIN, pin);
                if (forWhat!=null&&forWhat.equalsIgnoreCase(getString(R.string.set_new_pin))){
                    showSimpleAlert(getString(R.string.your_pin_has_been_set), getString(R.string.set_new_pin));
                }else {
                    showSimpleAlert(getString(R.string.your_pin_has_been_set), getString(R.string.update_pin));

                }
            } else {
                showSimpleAlert(getString(R.string.pin_and_confirm_pin_not_same), "");
                 clearConfirmPin();
            }
        } else {
            showSimpleAlert(getString(R.string.enter_pin), "");
        }
    }

    private void clearPin(){
        pin = "";
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
    }

    private void clearConfirmPin() {
        cPin = "";
        cEt1.setText("");
        cEt2.setText("");
        cEt3.setText("");
        cEt4.setText("");
        cEt5.setText("");
        cEt6.setText("");
    }
}
