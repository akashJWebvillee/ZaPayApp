package com.org.zapayapp;

import android.app.Application;
import android.provider.Settings;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;

import java.net.URISyntaxException;

public class ZapayApp extends Application {

    private APICallback apiCallback;
    public Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPref.init(this);
        try {
            mSocket = IO.socket(APICalling.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public APICallback getApiCallback() {
        return apiCallback;
    }

    public void setApiCallback(APICallback apiCallback) {
        this.apiCallback = apiCallback;
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void setmSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }
}
