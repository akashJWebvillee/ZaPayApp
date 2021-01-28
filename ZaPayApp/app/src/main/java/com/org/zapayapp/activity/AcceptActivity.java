package com.org.zapayapp.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.DateModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;

public class AcceptActivity extends BaseActivity implements APICallback, SimpleAlertFragment.AlertSimpleCallback, OnPageChangeListener, OnLoadCompleteListener {
    private TextView okTV, cancelTV;
    private CheckBox mChkAgree;
    private WebView webView;
    private String transactionId, moveFrom, status;
    private String pdfUrl = "";
    private DateModel dateModel;
    private ProgressBar progressBar;
    private PDFView pdfView;
    private int pageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        inIt();
        inItAction();
        getIntentValues();
    }

    private void inIt() {
        // discriptionTV = findViewById(R.id.discriptionTV);
        webView = findViewById(R.id.webView);
        okTV = findViewById(R.id.okTV);
        cancelTV = findViewById(R.id.cancelTV);
        mChkAgree = findViewById(R.id.mChkAgree);
        progressBar = findViewById(R.id.progressBar);
        pdfView = findViewById(R.id.pdfView);

    }

    private void getIntentValues() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("transactionId") != null && intent.getStringExtra("moveFrom") != null) {
            transactionId = intent.getStringExtra("transactionId");
            moveFrom = intent.getStringExtra("moveFrom");

            if (moveFrom.equalsIgnoreCase("ChangeDateRequestDialogActivity")) {
                dateModel = gson.fromJson(intent.getStringExtra("status"), DateModel.class);
                generateAmendmentPdf();
            } else {
                status = intent.getStringExtra("status");
                generateAgreementPdf();
            }
        }
    }

    private void inItAction() {
        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChkAgree.isChecked()) {
                    if (!pdfUrl.equalsIgnoreCase("")) {
                        if (moveFrom.equalsIgnoreCase("ChangeDateRequestDialogActivity")) {
                            if (dateModel != null) {
                                callAPIPayDateRequestStatusUpdate();
                            }
                        } else {
                            callAPIUpdateTransactionRequestStatus("2");
                        }
                    } else {
                        showSimpleAlert(getString(R.string.PDF_url_missing), "");
                    }

                } else if (moveFrom.equalsIgnoreCase("ChangeDateRequestDialogActivity")) {
                    showSimpleAlert(getString(R.string.please_accept_amendment_form), "");
                } else {
                    showSimpleAlert(getString(R.string.please_accept_agreement_form), "");
                }
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void generateAmendmentPdf() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_generate_amendment_pdf), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_generate_amendment_pdf), okTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void callAPIPayDateRequestStatusUpdate() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "transaction_request_id", transactionId,
                "pay_date_id", dateModel.getId(),
                "new_pay_date_status", "2", //date accept
                "pdf_url", pdfUrl);
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_pay_date_request_status_update), values);
            if (apiCalling != null) {
                apiCalling.setRunInBackground(false);
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_pay_date_request_status_update), okTV);
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

            } else if (from.equals(getResources().getString(R.string.api_generate_amendment_pdf))) {
                if (status == 200) {
                    JsonObject jsonObject = json.get("data").getAsJsonObject();
                    String pdf_url = jsonObject.get("pdf_url").getAsString();
                    pdfUrl = APICalling.getImageUrl(pdf_url);
                    loadPdfFile(APICalling.getImageUrl(pdf_url));
                } else {
                    showSimpleAlert(msg, "");
                }

            } else if (from.equals(getResources().getString(R.string.api_pay_date_request_status_update))) {
                if (status == 200) {
                    showSimpleAlert11(msg, getResources().getString(R.string.api_pay_date_request_status_update));

                } else {
                    showSimpleAlert(msg, "");
                }

            }


        }
    }

    private void loadPdfFile(String myPdfUrl) {

       /* WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new CustomWebViewClient());
        // webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
          // webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webSettings.setBuiltInZoomControls(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new Callback());
        String url = "https://docs.google.com/viewer?embedded=true&url=" + myPdfUrl;
        webView.loadUrl(url);*/

        progressBar.setVisibility(View.VISIBLE);
        new DownloadFile().execute(myPdfUrl);
    }

    private class DownloadFile extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            String fileUrl = strings[0];
            return downloadFile(fileUrl);
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            pdfView.fromStream(inputStream)
                    .onPageChange(AcceptActivity.this)
                    .enableAnnotationRendering(true)
                    .onLoad(AcceptActivity.this)
                    .scrollHandle(new DefaultScrollHandle(AcceptActivity.this))

                    .defaultPage(pageNumber)
                    .swipeHorizontal(false)
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .load();
            // progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {
        progressBar.setVisibility(View.GONE);
    }


    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            webview.setVisibility(webview.INVISIBLE);


        }

        @Override
        public void onPageFinished(WebView webview, String url) {
            webview.setVisibility(webview.VISIBLE);
            super.onPageFinished(webview, url);
            //apiCalling.setRunInBackground(true);

        }
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
            Intent intent = new Intent();
            setResult(200, intent);
            finish();//finishing activity
        } else if (from.equals(getResources().getString(R.string.api_pay_date_request_status_update))) {
            Intent intent = new Intent();
            setResult(200, intent);
            finish();//finishing activity
        }
    }

    public InputStream downloadFile(String fileUrl) {
        InputStream inputStream = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;

    }
}