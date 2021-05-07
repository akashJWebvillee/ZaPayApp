package com.org.zapayapp.utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class Const {
    public interface Var {
        //Login data..
        String USER_ID = "user_id";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String EMAIL = "email";
        String MOBILE = "mobile";

        String ADDRESS1 = "address1";
        String ADDRESS2 = "address2";
        String CITY = "city";
        String STATE = "state";
        String POSTAL_CODE = "postal_code";
        String DOB = "dob";
        String SSN = "ssn";
        String USER_STATUS = "user_status";
        String PROFILE_IMAGE = "profile_image";
        String EMAIL_VERIFY = "email_verify";
        String ACTIVITY_STATUS = "activity_status";
        String CURRENCY = "currency";
        String CREATE_AT = "created_at";
        String TOKEN = "token";

        String AGE = "age";
        String SEX = "sex";
        String ETHNICITY = "ethnicity";
        String INCOME = "income";
        String SIGNATURE = "signature";

        //bankAccount detail....
        String BANK_ACCOUNT_ID = "bank_account_id";
        String ACCOUNT_NUMBER = "account_number";
        String ROUTING_NUMBER = "routing_number";
        String BANK_ACCOUNT_TYPE = "bank_account_type";
        String ACCOUNT_HOLDER_NAME = "name";
        String BANK_ACCOUNT_STATUS = "dwolla_bank_account_status";
        String FIREBASE_DEVICE_TOKEN = "fire_base_device_token";
        String PIN = "pin";
        String DEVICE_TOKEN = "device_token";

        //charges_detail.....
        String BORROWER_CHARGE_VALUE = "borrower_charge_value";
        String BORROWER_CHARGE_TYPE = "borrower_charge_type";
        String LENDER_CHARGE_VALUE = "lender_charge_value";
        String LENDER_CHARGE_TYPE = "lender_charge_type";
        String DEFAULT_FEE_VALUE = "default_fee_value";
        String DEFAULT_FEE_TYPE = "default_fee_type";

        String IsDEFAULTER = "is_defaulter";
    }

    public interface KEY {
        String DEVICE_TYPE = "1";
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void logMsg(String msg) {
        Log.e("response:", "response===" + msg);

    }

    public static String getCurrency(){
       String currency=SharedPref.getPrefsHelper().getPref(Var.CURRENCY,"$");
       if (currency==null&&currency.length()==0){
           currency="$";
       }
      return currency;
    }


    public static boolean isRequestByMe(String fromId){
        boolean flag = false;
        if (fromId != null && fromId.length() > 0 && SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID) != null && SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString().length() > 0) {
            if (fromId.equalsIgnoreCase(SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString())) {
                flag=true;
            } else {
                flag=false;
            }
        }

        return flag;
    }


    public static boolean isUserDefaulter(){
        //is_defaulter==1 defaulter
        //is_defaulter==0 not defaulter

        boolean flag = false;
            if (SharedPref.getPrefsHelper().getPref(Var.IsDEFAULTER)!=null&&SharedPref.getPrefsHelper().getPref(Var.IsDEFAULTER).toString().length()>0) {
                if (SharedPref.getPrefsHelper().getPref(Var.IsDEFAULTER).toString().equals("1")){
                    flag=true;
                }else {
                    flag=false;
                }
            }
            return flag;
       // return false;

            }



}


