package com.org.zapayapp.activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import java.util.HashMap;
import retrofit2.Call;

public class AboutUsActivity extends BaseActivity {
private TextView contentTV;
private TextView noDataTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        contentTV=findViewById(R.id.contentTV);
        noDataTv=findViewById(R.id.noDataTv);

        callAPIAboutUs();
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(ABOUT_US);
    }

    private void callAPIAboutUs() {
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "content_type", "about_us");

        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postApi(getString(R.string.api_get_content), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_content), contentTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        Log.e("json", "json======" + json);
        if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (from.equals(getResources().getString(R.string.api_get_content))) {
                if (status==200){
                    if (json.get("data").getAsJsonObject()!=null){
                        JsonObject jsonObject=  json.get("data").getAsJsonObject();
                        if (jsonObject.get("page_description").getAsString()!=null){
                            contentTV.setText(jsonObject.get("page_description").getAsString());
                        }

                    }
                }else if(status==401){
                    showForceUpdate(getString(R.string.session_expired), getString(R.string.your_session_expired), false, "", false);
                } else {
                    showSimpleAlert(msg, "");
                    noDataTv.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
