package com.org.zapayapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

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
        String CURRENCY ="currency";
        String CREATE_AT = "created_at";
        String TOKEN = "token";

        //bankAccount detail....
        String BANK_ACCOUNT_ID = "bank_account_id";
        String ACCOUNT_NUMBER = "account_number";
        String ROUTING_NUMBER = "routing_number";
        String BANK_ACCOUNT_TYPE = "bank_account_type";
        String ACCOUNT_HOLDER_NAME = "name";
        String BANK_ACCOUNT_STATUS = "dwolla_bank_account_status";
        String FIREBASE_DEVICE_TOKEN = "fire_base_device_token";
    }

    public interface KEY {
        String DEVICE_TYPE = "1";
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
