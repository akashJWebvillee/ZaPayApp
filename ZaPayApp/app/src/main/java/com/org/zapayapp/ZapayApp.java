package com.org.zapayapp;

import android.app.Application;

import com.org.zapayapp.webservices.APICallback;

public class ZapayApp extends Application {

    private APICallback apiCallback;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public APICallback getApiCallback() {
        return apiCallback;
    }

    public void setApiCallback(APICallback apiCallback) {
        this.apiCallback = apiCallback;
    }
}
