package com.org.zapayapp.activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.webservices.APICallback;
import java.util.HashMap;
import retrofit2.Call;

/**
 * The type Help activity.
 */
public class HelpActivity extends BaseActivity implements APICallback {
    private TextView contentTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        contentTV=findViewById(R.id.contentTV);
        callAPIHelp();
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
        setCurrentScreen(HELP);
    }

    private void callAPIHelp() {
        HashMap<String, Object> values = apiCalling.getHashMapObject(
                "content_type", "help");

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
                       // JsonObject jsonObject=  json.get("data").getAsJsonObject();

                    }
                }else {
                    //showSimpleAlert(msg, "");
                }
            }
        }
    }
}
