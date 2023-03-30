package com.org.zapayapp.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.org.zapayapp.R;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;

import java.util.regex.Pattern;

public class WValidationLib {
    /*
     * Regular Expression
     */
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    // private static final String PASSWORD_REGEX = "^.{8,15}$";
    // private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[d$@$!%*?&#])[A-Za-z\\dd$@$!%*?&#]{12,}";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{12,}";

    private static final String USERNAME_REGEX = "^([-_A-Za-z0-9])*$";
    private static final String FULL_NAME = "^[\\p{L} .'-]+$";
    private static final String VALID_URL_REGEX = "(((f|ht){1}tp|tps:[//])[-a-zA-Z0-9@:%_\\+.~#?&//=]+)";
    private static final String ALPHANUMERIC = "[a-zA-Z0-9\\u00C0-\\u00FF \\\\./-\\\\?]*";
    private static final String ALPHA = "([a-zA-Z])\\w+";

    private Context wContext;

    public WValidationLib(Context context) {
        this.wContext = context;

    }

    public void removeError(final TextInputLayout inputLayout, final TextInputEditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inputLayout.setErrorEnabled(false);
                    editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
                editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputLayout.isErrorEnabled()) {
                    inputLayout.setError(null);
                    inputLayout.setErrorEnabled(false);
                    editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * @param editTextPassword        password edittext
     * @param editTextConfirmPassword confirm password edittext
     * @param required                pass boolean to check if the following validation is required
     * @return true when all validations are success else false
     */
    public boolean isConfirmPasswordValidation(CustomTextInputLayout editTextPasswordLay, TextInputEditText editTextPassword, CustomTextInputLayout editTextConfirmPasswordLay, TextInputEditText editTextConfirmPassword,
                                               String requireMsg, String requireConfMsg, String errorMsg, String notMatchMsg, boolean required) {
        if (isPassword22(editTextPasswordLay, editTextPassword, requireMsg, errorMsg, required)) {
            if (isPassword22(editTextConfirmPasswordLay, editTextConfirmPassword, requireConfMsg, errorMsg, required)) {
                if (isPasswordEqual(editTextPasswordLay, editTextConfirmPasswordLay, requireMsg, notMatchMsg)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if email id valid.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isEmailAddress(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValid(inputLayout, editText, EMAIL_REGEX, requireMsg, errorMsg, required);
    }

    /**
     * Check if user name is valid.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isUserName(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValid(inputLayout, editText, USERNAME_REGEX, requireMsg, errorMsg, required);
    }

    /**
     * Check if full name is valid.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isFullName(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValidFull(inputLayout, editText, FULL_NAME, requireMsg, errorMsg, required);
    }

    /**
     * Call this method when you need to check password validation for minimum length is 6 character with lowercase letter,uppercase letters,numbers and special characters
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isPassword(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValid(inputLayout, editText, PASSWORD_REGEX, requireMsg, errorMsg, required);
    }

    public boolean isPassword22(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValid22(inputLayout, editText, PASSWORD_REGEX, requireMsg, errorMsg, required);
    }

    /**
     * Check if only alpha numeric values are entered.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isAlphaNumeric(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValid(inputLayout, editText, ALPHANUMERIC, requireMsg, errorMsg, required);
    }

    /**
     * Check if only alphabetic values are entered.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isAlphabetic(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValid(inputLayout, editText, ALPHA, requireMsg, errorMsg, required);
    }

    /**
     * Check if Url is valid url
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isValidUrl(CustomTextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValid(inputLayout, editText, VALID_URL_REGEX, requireMsg, errorMsg, required);
    }

    /**
     * Check if edittext is empty
     *
     * @param editText edittext to check validation for
     * @param required pass boolean to check if the following validation is required
     * @return true when all validations are success else false
     */
    public boolean isEmpty(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isEmptyValid(inputLayout, editText, requireMsg, required);
    }

    /**
     * Check if edittext is empty
     *
     * @param editText edittext to check validation for
     * @param required pass boolean to check if the following validation is required
     * @return true when all validations are success else false
     */
    public boolean isEmptyPin(EditText editText, String requireMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isEmptyValidPin(editText, requireMsg, required);
    }

    /**
     * Method to check if password entered & confirm password is equal
     *
     * @param editText  editext of new password
     * @param editText1 edittext of confirm password
     * @return if equal return true else false
     */
    public boolean isPasswordEqual(TextInputLayout editText, TextInputLayout editText1, String requireMsg, String notMatchMsg) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isEqual(editText, editText1, requireMsg, notMatchMsg);
    }


    /**
     * Method to check if password entered & confirm password is equal
     *
     * @param editText  editext of new password
     * @param editText1 edittext of confirm password
     * @return if equal return true else false
     */
    public boolean isPinEqual(EditText editText, EditText editText1, String requireMsg, String notMatchMsg) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isEqualPin(editText, editText1, requireMsg, notMatchMsg);
    }


    /**
     * Check if only numeric values are entered.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isValidNumeric(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.isValidNumber(inputLayout, editText, requireMsg, errorMsg, required);
    }

    /**
     * Is valid boolean.
     *
     * @param editText the edit text
     * @param regex    the regex
     * @param errMsg   the err msg
     * @param required the required
     * @return the boolean
     */
    public boolean isValid(CustomTextInputLayout inputLayout, TextInputEditText editText, String regex, String requireMsg, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        //clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        if (text.length() <= 11) {
            inputLayout.requestFocus();
            //inputLayout.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, "Please enter 12 character."));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }


        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            inputLayout.requestFocus();
            //inputLayout.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValid22(CustomTextInputLayout inputLayout, TextInputEditText editText, String regex, String requireMsg, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        //clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }
        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            inputLayout.requestFocus();
            //inputLayout.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }

    /**
     * Is valid boolean.
     *
     * @param editText the edit text
     * @param regex    the regex
     * @param errMsg   the err msg
     * @param required the required
     * @return the boolean
     */
    public boolean isValidFull(CustomTextInputLayout inputLayout, TextInputEditText editText, String regex, String requireMsg, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        //clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }
        // pattern doesn't match so returning false
        if ((required && !Pattern.matches(regex, text)) || !editText.getText().toString().contains(" ")) {
            inputLayout.requestFocus();
            //inputLayout.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }

    /**
     * Has text boolean.
     *
     * @param editText the edit text
     * @return the boolean
     */
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public boolean hasText(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg) {

        String text = editText.getText().toString().trim();
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // length 0 means there is no text
        if (text.length() == 0) {
            inputLayout.requestFocus();
            //inputLayout.setError(requireMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, requireMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    private boolean hasTextRecangulerBg(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg) {
        String text = editText.getText().toString().trim();
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
        // length 0 means there is no text
        if (text.length() == 0) {
            inputLayout.requestFocus();
            //inputLayout.setError(requireMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, requireMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
            return false;
        }
        return true;
    }

    /**
     * Has text boolean.
     *
     * @param editText the edit text
     * @return the boolean
     */
    // check the input field has any text or not
    // return true if it contains text otherwise false
    private boolean hasTextPin(EditText editText, String requireMsg) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.requestFocus();
            editText.setError(requireMsg);
            return false;
        }
        return true;
    }

    /**
     * Has text boolean.
     *
     * @param editText the edit text
     * @return the boolean
     */
    // check the input field has any text or not
    // return true if it contains text otherwise false
    private boolean isEqual(TextInputLayout editText, TextInputLayout editText1, String requireMsg, String noMatchMsg) {
        String text = editText.getEditText().getText().toString().trim();
        editText.setError(null);
        editText.setErrorEnabled(false);
        String text1 = editText1.getEditText().getText().toString().trim();
        editText1.setError(null);
        editText1.setErrorEnabled(false);
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.requestFocus();
            editText.setError(requireMsg);
            editText.setErrorEnabled(true);
            return false;
        } else if (text1.length() == 0) {
            editText1.requestFocus();
            editText1.setError(requireMsg);
            editText1.setErrorEnabled(true);
            return false;
        } else if (!text.equals(text1)) {
            editText1.requestFocus();
            editText1.setError(noMatchMsg);
            editText1.setErrorEnabled(true);
            return false;
        }
        return true;
    }

    /**
     * Has text boolean.
     *
     * @param editText the edit text
     * @return the boolean
     */
    // check the input field has any text or not
    // return true if it contains text otherwise false
    private boolean isEqualPin(EditText editText, EditText editText1, String requireMsg, String noMatchMsg) {
        String text = editText.getText().toString().trim();
        editText.setError(null);
        String text1 = editText1.getText().toString().trim();
        editText.setError(null);
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.requestFocus();
            editText.setError(requireMsg);
            return false;
        } else if (text1.length() == 0) {
            editText1.requestFocus();
            editText1.setError(requireMsg);
            return false;
        } else if (!text.equals(text1)) {
            editText1.requestFocus();
            editText1.setError(noMatchMsg);
            return false;
        }
        return true;
    }

    /**
     * Is valid boolean.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isEmptyValid(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, boolean required) {

        //String text = editText.getEditText().getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            inputLayout.requestFocus();
            //inputLayout.setError(requireMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, requireMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }

    /**
     * Is valid boolean.
     *
     * @param editText the edit text
     * @param required the required
     * @return the boolean
     */
    public boolean isEmptyValidPin(EditText editText, String requireMsg, boolean required) {

        //String text = editText.getEditText().getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasTextPin(editText, requireMsg)) {
            return false;
        }
        return true;
    }

    /**
     * Is valid number boolean.
     *
     * @param et       the et
     * @param errMsg   the err msg
     * @param required the required
     * @return the boolean
     */
    // check the input field has any digits or not
    // return true if it contains digits otherwise false
    public boolean isValidNumber(TextInputLayout et, TextInputEditText editText, String requireMsg, String errMsg, boolean required) {
        String text = et.getEditText().getText().toString().trim();
        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        et.setError(null);
        et.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(et, editText, requireMsg)) {
            return false;
        }
        // pattern doesn't match so returning false
        if (required && !TextUtils.isDigitsOnly(text)) {
            et.requestFocus();
            // et.setError(errMsg);
            et.setErrorEnabled(true);
            et.setError(CustomTextInputLayout.setErrorMessage(wContext, errMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        if (text.length() < 10) {
            et.requestFocus();
            //et.setError(errMsg);
            et.setErrorEnabled(true);
            et.setError(CustomTextInputLayout.setErrorMessage(wContext, errMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidAmount1(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validAmount1(inputLayout, editText, requireMsg, errorMsg, required);
    }


    private boolean validAmount1(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        // pattern doesn't match so returning false
       /* if (required) {
            inputLayout.requestFocus();
            // et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }*/

        float amount1 = Float.parseFloat(text);

        //  if (amount1 > 0.11) {
        if (amount1 > 0.1 || amount1 < 0.0) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }

    public boolean isValidAmount2(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validAmount2(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validAmount2(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        float amount2 = Float.parseFloat(text);
      //  if (amount2 > 0.03) {
        if (amount2 > 0.1 || amount2 < 0.0) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidAccountNumber(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validAccountNumber(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validAccountNumber(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }


        if (text.length() < 4 || text.length() > 17) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidRoutingNumber(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validRoutingNumber(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validRoutingNumber(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        if (text.length() < 9) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidAddress1(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validAddress1(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validAddress1(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        if (text.length() > 51) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidAddress2(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validAddress2(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validAddress2(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        //                                if (address2EditText.getText().toString().trim().length() == 0 || address2EditText.getText().toString().trim().length() <= 51) {
        if (text.length() > 51) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidPostalCode(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validPostelCode(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validPostelCode(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        if (text.length() < 5) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidSSNcode(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validSSNcode(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validSSNcode(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_selector));
        // text required and editText is blank, so return false
        if (required && !hasText(inputLayout, editText, requireMsg)) {
            return false;
        }

        if (text.length() < 4) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.edt_bg_error));
            return false;
        }
        return true;
    }


    public boolean isValidAmount(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validAmount(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validAmount(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
        // text required and editText is blank, so return false
        if (required && !hasTextRecangulerBg(inputLayout, editText, requireMsg)) {
            return false;
        }
        float amount = Float.parseFloat(text);
        if (amount < 1) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
            return false;
        }

        return true;
    }


    public boolean isValidTerm(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validTerm(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validTerm(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
        // text required and editText is blank, so return false
        if (required && !hasTextRecangulerBg(inputLayout, editText, requireMsg)) {
            return false;
        }

        if (text != null && text.length() > 0) {
            float term = Float.parseFloat(text);
            if (term < 1) {
                inputLayout.requestFocus();
                //et.setError(errMsg);
                inputLayout.setErrorEnabled(true);
                inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
                editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
                return false;
            }
        }

        return true;
    }


    public boolean isValidNoOfPayment(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        WValidationLib v_lib = new WValidationLib(wContext);
        return v_lib.validNoOfPayment(inputLayout, editText, requireMsg, errorMsg, required);
    }

    private boolean validNoOfPayment(TextInputLayout inputLayout, TextInputEditText editText, String requireMsg, String errorMsg, boolean required) {
        String text = inputLayout.getEditText().getText().toString().trim();

        //CommonMethods.showLogs("text length ", "text length " + text.length());
        // clearing the error, if it was previously set by some other values
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);
        editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
        // text required and editText is blank, so return false
        if (required && !hasTextRecangulerBg(inputLayout, editText, requireMsg)) {
            return false;
        }
        float noOfPayment = Float.parseFloat(text);
        if (noOfPayment < 1) {
            inputLayout.requestFocus();
            //et.setError(errMsg);
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(CustomTextInputLayout.setErrorMessage(wContext, errorMsg));
            editText.setBackgroundDrawable(CommonMethods.getDrawableWrapper(wContext, R.drawable.rectanguler_borderr));
            return false;
        }
        return true;
    }


}
