package com.org.zapayapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.org.zapayapp.R;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.MySession;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import retrofit2.Call;

public class HomeActivity extends BaseActivity implements View.OnClickListener, APICallback {
    private LinearLayout homeLLLend, homeLLBorrow;
    private Intent intent;
    private TextView titleTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        initAction();
    }



    private void init() {
        homeLLLend = findViewById(R.id.homeLLLend);
        homeLLBorrow = findViewById(R.id.homeLLBorrow);
        titleTV = findViewById(R.id.titleTV);

        AppCenter.start(getApplication(), "7c7f48b8-92b9-419a-842c-536b68581c02",
                Analytics.class, Crashes.class);// add this to trace the crashlytics

    }

    private void initAction() {
        homeLLLend.setOnClickListener(this);
        homeLLBorrow.setOnClickListener(this);
       // callAPIGetUserDetail();
       // callAPIGetBankAccountDetail();


        if (SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME) != null && SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME).toString().length() > 0) {
            titleTV.setText(getString(R.string.hello) + " " + SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME).toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeLLLend:

                //activity_status=0  //signup
                //activity_status=1  //updated profile
                //activity_status=2   //added bank account
                //activity_status=3   //verifyed bank account(ready to send request)


                if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS)!=null&&SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().length()>0) {
                    if (!SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("0")){
                        intent = new Intent(HomeActivity.this, LendBorrowActivity.class);
                        intent.putExtra("isBorrow", false);
                        startActivity(intent);
                    }else {
                        showSimpleAlert(getString(R.string.update_your_profile), getString(R.string.update_your_profile));
                    }
                }

                break;

            case R.id.homeLLBorrow:
                if (SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS)!=null&&SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().length()>0) {
                    if (!SharedPref.getPrefsHelper().getPref(Const.Var.ACTIVITY_STATUS).toString().equals("0")){
                        intent = new Intent(HomeActivity.this, LendBorrowActivity.class);
                        intent.putExtra("isBorrow", true);
                        startActivity(intent);
                    }else {
                        showSimpleAlert(getString(R.string.update_your_profile), getString(R.string.update_your_profile));
                    }
                }

                break;
        }
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(100);
        callAPIGetUserDetail();
        callAPIGetBankAccountDetail();
    }

    private void callAPIGetUserDetail() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            //"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMzAiLCJmaXJzdF9uYW1lIjoiQXNob2twaXQiLCJsYXN0X25hbWUiOiJLdW1hciIsImVtYWlsIjoiYXNob2twaXRlY2guMTIzQGdtYWlsLmNvbSIsInJvbGUiOiIyIiwidGltZXN0YW1wIjoxNTk3NjQ1MDA2fQ.lkG8uyRzEcDN1gepzbPCASccoGWuVzg2zGeoIDIZvZk"
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.getApiToken(token, getString(R.string.api_get_user_details));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_user_details), homeLLBorrow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetBankAccountDetail() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.getApiToken(token, getString(R.string.api_get_bank_account_details));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_bank_account_details), homeLLBorrow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (from.equals(getResources().getString(R.string.api_get_user_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        JsonObject jsonObject = json.get("data").getAsJsonObject();
                        MySession.MakeSession(jsonObject);
                    }
                }else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_bank_account_details))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonObject() != null) {
                        MySession.saveBankData(json.get("data").getAsJsonObject());
                    }
                }else if (status == 401) {
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                }
            } else if (from.equals(getResources().getString(R.string.api_logout))) {
                if (status == 200) {
                    clearLogout();
                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }

    private void clearLogout() {
        MySession.removeSession();
        intent = new Intent(HomeActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
