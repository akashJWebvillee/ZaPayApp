package com.org.zapayapp.dialogs;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.uihelpers.CustomRatingBar;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;
import java.util.HashMap;
import retrofit2.Call;

public class RattingDialogActivity extends AppCompatActivity implements APICallback , SimpleAlertFragment.AlertSimpleCallback {
    private String requestBy;
    private String toId="";
    private String fromId="";
    private String transactionRequestID="";
    private String averageRating="";
    private String isAlreadyRated="";

    private TextView titleTV;
    private TextView saveTV;
    private ImageView closeTV;
    private CustomRatingBar viewRatingBar;

    /*Code for API calling*/
    protected ZapayApp zapayApp;
    protected Gson gson;
    protected APICalling apiCalling;
    protected RestAPI restAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ratting_dialog);

        getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ratting_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        apiCodeInit();
        getIntentFunc();
        inIt();
        inItAction();
    }
    private void apiCodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
        //wValidationLib = new WValidationLib(ChangePassDialogActivity.this);
    }
    private void inIt(){
        titleTV=findViewById(R.id.titleTV);
        viewRatingBar=findViewById(R.id.viewRatingBar);
        saveTV=findViewById(R.id.saveTV);
        closeTV=findViewById(R.id.closeTV);
    }

    private void getIntentFunc(){
        if (getIntent().getStringExtra("requestBy")!=null&&getIntent().getStringExtra("toId")!=null&&getIntent().getStringExtra("transactionRequestID")!=null){
            requestBy= getIntent().getStringExtra("requestBy");
            toId= getIntent().getStringExtra("toId");
            fromId= getIntent().getStringExtra("fromId");
            transactionRequestID= getIntent().getStringExtra("transactionRequestID");
            averageRating= getIntent().getStringExtra("averageRating");
            isAlreadyRated= getIntent().getStringExtra("isAlreadyRated");
        }
    }

    private void inItAction(){
        if (requestBy!=null&&requestBy.equals("1")){
            titleTV.setText(getString(R.string.rate_lender));

        }else if (requestBy!=null&&requestBy.equals("2")){
            titleTV.setText(getString(R.string.rate_borrower));
        }

     /*   if (averageRating!=null&&averageRating.length()>0){
            viewRatingBar.setScore(Float.parseFloat(averageRating));
        }*/

        if (isAlreadyRated!=null&&isAlreadyRated.length()>0){
            if (isAlreadyRated.equals("0")){
                saveTV.setVisibility(View.VISIBLE);
                viewRatingBar.setScore(0);
            }if (isAlreadyRated.equals("1")){    //already rated
                viewRatingBar.setScrollToSelect(false);
                saveTV.setVisibility(View.GONE);
                viewRatingBar.setScore(Float.parseFloat(averageRating));
            }
        }

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Float rating=viewRatingBar.getScore();
             callAPIRating(String.valueOf(rating));
            }
        });

        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void callAPIRating(String ratingValue) {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    //"to_id", fromId,
                    "to_id", toId,
                    "rating", ratingValue,
                    "transaction_request_id", transactionRequestID);

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_rating), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_rating), saveTV);
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
            if (from.equals(getResources().getString(R.string.api_rating))) {
                if (status == 200) {
                    showSimpleAlert(msg, getString(R.string.api_rating));
                } if (status==401){
                    showSimpleAlert(msg, "");
                }else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }

    public void showSimpleAlert(String message, String from) {
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
       finish();
    }
}
