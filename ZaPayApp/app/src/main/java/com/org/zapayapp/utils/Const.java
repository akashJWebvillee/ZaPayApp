package com.org.zapayapp.utils;
import android.content.Context;
import android.provider.Settings;

public class Const {
    public interface Var {
        //Login data..
        String USER_ID= "user_id";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String EMAIL = "email";
        String MOBILE = "mobile";


      String ADDRESS1 = "address1";
      String ADDRESS2 = "address2";
      String CITY = "city";
      String STATE = "state";
      String POSTEL_CODE = "postal_code";
      String DOB = "dob";
      String SSN = "ssn";
      String USER_STATUS = "user_status";
      String PROFILE_IMAGE = "profile_image";
      String EMAIL_VERIFY = "email_verify";
      String ACTIVITY_STATUS = "activity_status";


        String CREATE_AT = "created_at";
        String TOKEN = "token";



        //bankAccount detail....
        String BANKACCOUNT_ID = "bank_account_id";
        String ACCOUNT_NUMBER = "account_number";
        String ROUTING_NUMBER = "routing_number";
        String BANKACCOUNT_TYPE = "bank_account_type";
        String ACCOUNT_HOLDER_NAME = "name";
        String BANK_ACCOUNT_STATUS = "dwolla_bank_account_status";


    }

    public interface MytvApi {
        String BASE_URL = "http://3.14.202.215/api/";
        String LANGUAGE_API = BASE_URL + "base_url";
    }

    public interface KEY {
        String DEVICE_TYPE = "1";
    }


    public static String getDeviceId(Context context){
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }





}
