package com.org.zapayapp;

import android.app.Application;
import android.provider.Settings;

import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;

public class ZapayApp extends Application {
    private APICallback apiCallback;


    @Override
    public void onCreate() {
        super.onCreate();
        SharedPref.init(this);

    }

    public APICallback getApiCallback() {
        return apiCallback;
    }

    public void setApiCallback(APICallback apiCallback) {
        this.apiCallback = apiCallback;
    }



}
