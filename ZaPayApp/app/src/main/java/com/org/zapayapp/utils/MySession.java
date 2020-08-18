package com.org.zapayapp.utils;
import com.google.gson.JsonObject;

public class MySession {
    public static void MakeSession(JsonObject object) {
        if (object.get("id") != null && object.get("id").getAsString() != null /*&& object.get("shopName").getAsString().length() > 0*/) {
            String id = object.get("id").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.USER_ID, id);
        }
        if (object.get("first_name") != null && object.get("first_name").getAsString() != null /*&& object.get("shopLogo").getAsString().length() > 0*/) {
            String first_name = object.get("first_name").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.FIRST_NAME, first_name);
        }
        if (object.get("last_name") != null && object.get("last_name").getAsString() != null /*&& object.get("ShopType").getAsString().length() > 0*/) {
            String last_name = object.get("last_name").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.LAST_NAME, last_name);
        }
        if (object.get("email") != null && object.get("email").getAsString() != null /*&& object.get("minDelivryTime").getAsString().length() > 0*/) {
            String email = object.get("email").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL, email);
        }

        if (object.get("mobile") != null && object.get("mobile").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String mobile = object.get("mobile").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.MOBILE, mobile);
        }

        if (object.get("address1") != null && object.get("address1").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String address1 = object.get("address1").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ADDRESS1, address1);
        }

        if (object.get("address2") != null && object.get("address2").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String address2 = object.get("address2").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ADDRESS2, address2);
        }

        if (object.get("city") != null && object.get("city").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String city = object.get("city").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.CITY, city);
        }

        if (object.get("state") != null && object.get("state").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String state = object.get("state").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.STATE, state);
        }

        if (object.get("postal_code") != null && object.get("postal_code").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String postal_code = object.get("postal_code").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.POSTEL_CODE, postal_code);
        }

        if (object.get("dob") != null && object.get("dob").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String dob = object.get("dob").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.DOB, dob);
        }

        if (object.get("ssn") != null && object.get("ssn").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String ssn = object.get("ssn").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.SSN, ssn);
        }

        if (object.get("user_status") != null && object.get("user_status").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String user_status = object.get("user_status").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.USER_STATUS, user_status);
        }

        if (object.get("profile_image") != null && object.get("profile_image").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String profile_image = object.get("profile_image").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.PROFILE_IMAGE, profile_image);
        }

        if (object.get("email_verify") != null && object.get("email_verify").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String email_verify = object.get("email_verify").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL_VERIFY, email_verify);
        }

        if (object.get("activity_status") != null && object.get("activity_status").getAsString() != null /*&& object.get("lat").getAsString().length() > 0*/) {
            String activity_status = object.get("activity_status").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ACTIVITY_STATUS, activity_status);
        }

        if (object.get("token") != null && object.get("token").getAsString() != null && object.get("token").getAsString().length() > 0) {
            String token = object.get("token").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.TOKEN, token);
        }

        if (object.get("created_at") != null && object.get("created_at").getAsString() != null && object.get("created_at").getAsString().length() > 0) {
            String created_at = object.get("created_at").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.CREATE_AT, created_at);
        }

        /* if (object.get("country") != null && object.get("country").getAsString() != null && object.get("country").getAsString().length() > 0) {
            String country = object.get("country").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.COUNTRY, country);
        }

        if (object.get("productPrice") != null && object.get("productPrice").getAsString() != null && object.get("token").getAsString().length() > 0) {
            String productPrice = object.get("productPrice").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.PRODUCT_PRICE, productPrice);
        }*/



    }

    public static void removeSession() {
        SharedPref.getPrefsHelper().savePref(Const.Var.USER_ID, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.FIRST_NAME, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.LAST_NAME, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.MOBILE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ADDRESS1, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ADDRESS2, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.CITY, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.STATE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.POSTEL_CODE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.DOB, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.SSN, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.USER_STATUS, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.PROFILE_IMAGE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL_VERIFY, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ACTIVITY_STATUS, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.CREATE_AT, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.TOKEN, null);



        SharedPref.getPrefsHelper().savePref(Const.Var.BANKACCOUNT_ID, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_NUMBER, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ROUTING_NUMBER, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.BANKACCOUNT_TYPE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_HOLDER_NAME, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_STATUS, null);



         //SharedPref.getPrefsHelper().savePref(Const.Var.USER_ID, null);
       // SharedPref.getPrefsHelper().savePref(Const.Var.TOKEN, null);
    }


    public static void saveBankData(JsonObject object) {
        if (object.get("id") != null && object.get("id").getAsString() != null /*&& object.get("shopName").getAsString().length() > 0*/) {
            String id = object.get("id").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.BANKACCOUNT_ID, id);
        }

        if (object.get("account_number") != null && object.get("account_number").getAsString() != null /*&& object.get("shopName").getAsString().length() > 0*/) {
            String account_number = object.get("account_number").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_NUMBER, account_number);
        }

        if (object.get("routing_number") != null && object.get("routing_number").getAsString() != null /*&& object.get("shopName").getAsString().length() > 0*/) {
            String routing_number = object.get("routing_number").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ROUTING_NUMBER, routing_number);
        }

        if (object.get("bank_account_type") != null && object.get("bank_account_type").getAsString() != null /*&& object.get("shopName").getAsString().length() > 0*/) {
            String bank_account_type = object.get("bank_account_type").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.BANKACCOUNT_TYPE, bank_account_type);
        }

        if (object.get("name") != null && object.get("name").getAsString() != null /*&& object.get("shopName").getAsString().length() > 0*/) {
            String name = object.get("name").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_HOLDER_NAME, name);
        }

        if (object.get("dwolla_bank_account_status") != null && object.get("dwolla_bank_account_status").getAsString() != null /*&& object.get("shopName").getAsString().length() > 0*/) {
            String dwolla_bank_account_status = object.get("dwolla_bank_account_status").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_STATUS, dwolla_bank_account_status);
        }


    }






}
