package com.org.zapayapp.utils;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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





        if (object.get("created_at") != null && object.get("created_at").getAsString() != null && object.get("created_at").getAsString().length() > 0) {
            String created_at = object.get("created_at").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.CREATE_AT, created_at);
        }
        if (object.get("token") != null && object.get("token").getAsString() != null && object.get("token").getAsString().length() > 0) {
            String token = object.get("token").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.TOKEN, token);
        }

        /* if (object.get("country") != null && object.get("country").getAsString() != null && object.get("country").getAsString().length() > 0) {
            String country = object.get("country").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.COUNTRY, country);
        }
        if (object.get("_id") != null && object.get("_id").getAsString() != null && object.get("_id").getAsString().length() > 0) {
            String _id = object.get("_id").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var._ID, _id);
        }
        if (object.get("userName") != null && object.get("userName").getAsString() != null && object.get("userName").getAsString().length() > 0) {
            String userName = object.get("userName").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.USER_NAME, userName);
        }
        if (object.get("email") != null && object.get("email").getAsString() != null && object.get("email").getAsString().length() > 0) {
            String email = object.get("email").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.EMAIL, email);
        }
        if (object.get("contactNo") != null && object.get("contactNo").getAsString() != null && object.get("contactNo").getAsString().length() > 0) {
            String contactNo = object.get("contactNo").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.CONTACT_NO, contactNo);
        }
        if (object.get("referCode") != null && object.get("referCode").getAsString() != null && object.get("referCode").getAsString().length() > 0) {
            String referCode = object.get("referCode").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.REFERRAL_CODE, referCode);
        }
        if (object.get("token") != null && object.get("token").getAsString() != null && object.get("token").getAsString().length() > 0) {
            String token = object.get("token").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.TOKEN, token);
        }


//this add by ashok....
        if (object.get("radius") != null && object.get("radius").getAsString() != null && object.get("token").getAsString().length() > 0) {
            String radius = object.get("radius").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.RADIUS, radius);
        }

        if (object.get("productPrice") != null && object.get("productPrice").getAsString() != null && object.get("token").getAsString().length() > 0) {
            String productPrice = object.get("productPrice").getAsString();
            SharedPref.getPrefsHelper().savePref(Const.Var.PRODUCT_PRICE, productPrice);
        }*/



    }

/*
    public static void removeSession() {
        SharedPref.getPrefsHelper().savePref(Const.Var._ID, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.TOKEN, null);
        SharedPref.getPrefsHelper().savePref(Const.Var.IS_VERIFIED, false);
    }
*/







}
