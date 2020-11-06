package com.org.zapayapp.utils;

import com.google.gson.JsonObject;

public class MySession {
    public static void MakeSession(JsonObject object) {
        if (object.get("id") != null && object.get("id").getAsString() != null) {
            String id = object.get("id").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.USER_ID, id);
        }
        if (object.get("first_name") != null && object.get("first_name").getAsString() != null) {
            String first_name = object.get("first_name").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.FIRST_NAME, first_name);
        }
        if (object.get("last_name") != null && object.get("last_name").getAsString() != null) {
            String last_name = object.get("last_name").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.LAST_NAME, last_name);
        }
        if (object.get("email") != null && object.get("email").getAsString() != null) {
            String email = object.get("email").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL, email);
        }
        if (object.get("mobile") != null && object.get("mobile").getAsString() != null) {
            String mobile = object.get("mobile").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.MOBILE, mobile);
        }
        if (object.get("address1") != null && object.get("address1").getAsString() != null) {
            String address1 = object.get("address1").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ADDRESS1, address1);
        }
        if (object.get("address2") != null && object.get("address2").getAsString() != null) {
            String address2 = object.get("address2").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ADDRESS2, address2);
        }
        if (object.get("city") != null && object.get("city").getAsString() != null) {
            String city = object.get("city").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.CITY, city);
        }
        if (object.get("state") != null && object.get("state").getAsString() != null) {
            String state = object.get("state").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.STATE, state);
        }
        if (object.get("postal_code") != null && object.get("postal_code").getAsString() != null) {
            String postal_code = object.get("postal_code").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.POSTAL_CODE, postal_code);
        }
        if (object.get("dob") != null && object.get("dob").getAsString() != null) {
            String dob = object.get("dob").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.DOB, dob);
        }
        if (object.get("ssn") != null && object.get("ssn").getAsString() != null) {
            String ssn = object.get("ssn").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.SSN, ssn);
        }
        if (object.get("user_status") != null && object.get("user_status").getAsString() != null) {
            String user_status = object.get("user_status").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.USER_STATUS, user_status);
        }
        if (object.get("profile_image") != null && object.get("profile_image").getAsString() != null) {
            String profile_image = object.get("profile_image").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.PROFILE_IMAGE, profile_image);
        }
        if (object.get("email_verify") != null && object.get("email_verify").getAsString() != null) {
            String email_verify = object.get("email_verify").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL_VERIFY, email_verify);
        }
        if (object.get("activity_status") != null && object.get("activity_status").getAsString() != null) {
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
        if (object.get("currency") != null && object.get("currency").getAsString() != null && object.get("currency").getAsString().length() > 0) {
            String currency = object.get("currency").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.CURRENCY, currency);
        }



        if (object.get("age") != null && object.get("age").getAsString() != null && object.get("age").getAsString().length() > 0) {
            String age = object.get("age").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.AGE, age);
        }
        if (object.get("sex") != null && object.get("sex").getAsString() != null && object.get("sex").getAsString().length() > 0) {
            String sex = object.get("sex").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.SEX, sex);
        }
        if (object.get("ethnicity") != null && object.get("ethnicity").getAsString() != null && object.get("ethnicity").getAsString().length() > 0) {
            String ethnicity = object.get("ethnicity").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ETHNICITY, ethnicity);
        }
        if (object.get("income") != null && object.get("income").getAsString() != null && object.get("income").getAsString().length() > 0) {
            String income = object.get("income").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.INCOME, income);
        }

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
        SharedPref.getPrefsHelper().savePref(Const.Var.POSTAL_CODE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.DOB, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.SSN, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.USER_STATUS, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.PROFILE_IMAGE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL_VERIFY, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ACTIVITY_STATUS, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.CREATE_AT, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.TOKEN, null);

        SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_ID, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_NUMBER, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ROUTING_NUMBER, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_TYPE, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_HOLDER_NAME, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_STATUS, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.PIN, null);
    }

    public static void saveBankData(JsonObject object) {
        if (object.get("id") != null && object.get("id").getAsString() != null) {
            String id = object.get("id").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_ID, id);
        }
        if (object.get("account_number") != null && object.get("account_number").getAsString() != null) {
            String account_number = object.get("account_number").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_NUMBER, account_number);
        }
        if (object.get("routing_number") != null && object.get("routing_number").getAsString() != null) {
            String routing_number = object.get("routing_number").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ROUTING_NUMBER, routing_number);
        }
        if (object.get("bank_account_type") != null && object.get("bank_account_type").getAsString() != null) {
            String bank_account_type = object.get("bank_account_type").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_TYPE, bank_account_type);
        }
        if (object.get("name") != null && object.get("name").getAsString() != null) {
            String name = object.get("name").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.ACCOUNT_HOLDER_NAME, name);
        }
        if (object.get("dwolla_bank_account_status") != null && object.get("dwolla_bank_account_status").getAsString() != null) {
            String dwolla_bank_account_status = object.get("dwolla_bank_account_status").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.BANK_ACCOUNT_STATUS, dwolla_bank_account_status);
        }
    }
}
