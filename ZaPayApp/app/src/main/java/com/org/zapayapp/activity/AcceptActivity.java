package com.org.zapayapp.activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import java.util.HashMap;
import retrofit2.Call;

public class AcceptActivity extends BaseActivity implements APICallback,SimpleAlertFragment.AlertSimpleCallback {
    private TextView okTV, cancelTV;
    private CheckBox mChkAgree;
    private WebView webView;
    private String transactionId, moveFrom, status;
    private String pdfUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        inIt();
        inItAction();
        getIntentValues();
        generateAgreementPdf();

    }

    private void inIt() {
        // discriptionTV = findViewById(R.id.discriptionTV);
        webView = findViewById(R.id.webView);
        okTV = findViewById(R.id.okTV);
        cancelTV = findViewById(R.id.cancelTV);
        mChkAgree = findViewById(R.id.mChkAgree);

    }

    private void getIntentValues() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("transactionId") != null && intent.getStringExtra("moveFrom") != null) {
            transactionId = intent.getStringExtra("transactionId");
            moveFrom = intent.getStringExtra("moveFrom");
            status = intent.getStringExtra("status");
            //setDataStatusFunc();
        }
    }

    private void inItAction() {
        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChkAgree.isChecked()) {
                    if (!pdfUrl.equalsIgnoreCase("")){
                        callAPIUpdateTransactionRequestStatus("2");
                    }else {
                        showSimpleAlert(getString(R.string.PDF_url_missing), "");
                    }
                } else {
                    showSimpleAlert(getString(R.string.please_accept_agreement_form), "");
                }
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void generateAgreementPdf() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_generate_agreement_pdf), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_generate_agreement_pdf), okTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callAPIUpdateTransactionRequestStatus(String status) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId,
                "status", status,
                "pdf_url", pdfUrl);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_update_transaction_request_status), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_transaction_request_status), okTV);
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

            if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
                if (status == 200) {
                    //showSimpleAlert(msg, getResources().getString(R.string.api_update_transaction_request_status));
                    showSimpleAlert11(msg, getResources().getString(R.string.api_update_transaction_request_status));
                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_generate_agreement_pdf))) {
                if (status == 200) {
                    JsonObject jsonObject = json.get("data").getAsJsonObject();
                    String pdf_url = jsonObject.get("pdf_url").getAsString();
                    pdfUrl = APICalling.getImageUrl(pdf_url);
                    loadPdfFile(APICalling.getImageUrl(pdf_url));
                } else {
                    showSimpleAlert(msg, "");
                }

            }
        }
    }

    private void loadPdfFile(String myPdfUrl) {
        //webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        // webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        // webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        String url = "https://docs.google.com/viewer?embedded=true&url=" + myPdfUrl;
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    //  progressDialog.show();
                    apiCalling.setRunInBackground(false);
                }
                if (progress == 100) {
                    // progressDialog.dismiss();
                    apiCalling.setRunInBackground(true);
                }
            }
        });
    }



    public void showSimpleAlert11(String message, String from) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("header", message);
            args.putString("textOk", getString(R.string.ok));
            args.putString("textCancel", getString(R.string.cancel));
            args.putString("from", from);
            SimpleAlertFragment alert = new SimpleAlertFragment();
            alert.setArguments(args);
            alert.show(fm, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSimpleCallback(String from) {
        if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
            Intent intent=new Intent();
            setResult(200,intent);
            finish();//finishing activity
        }
    }
}