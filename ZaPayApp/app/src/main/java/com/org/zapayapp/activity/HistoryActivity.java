package com.org.zapayapp.activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.HistoryAdapter;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.ContactModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.EndlessRecyclerViewScrollListener;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;

public class HistoryActivity extends BaseActivity implements APICallback,SimpleAlertFragment.AlertSimpleCallback{
    private RecyclerView historyRecyclerView;
    private ImageView backArrowImageView;
    private Toolbar toolbar;


    private int pageNo=0;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        initAction();
    }

    private void init() {
        toolbar = findViewById(R.id.customToolbar);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
    }

    private void initAction() {
       LinearLayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pageNo = page;
                callAPIGetTransactionHistory(pageNo);
            }
        };
        historyRecyclerView.addOnScrollListener(scrollListener);

        pageNo = 0;
        callAPIGetTransactionHistory(pageNo);



        HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this);
        historyRecyclerView.setAdapter(historyAdapter);




        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(HISTORY);
    }

    private void callAPIGetTransactionHistory(int pageNo) {
        if (pageNo == 0 && scrollListener != null) {
            scrollListener.resetState();
        }

        String token= SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "page", pageNo);

            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token,getString(R.string.api_get_transaction_history), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_transaction_history), historyRecyclerView);
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

            if (from.equals(getResources().getString(R.string.api_get_transaction_history))) {

                if (status == 200) {
                    if (pageNo == 0) {
                       // contactNumberList.clear();
                    }

                    List<ContactModel> list = apiCalling.getDataList(json, "data", ContactModel.class);
                    if (list.size() > 0) {
                       // noDataTv.setVisibility(View.GONE);
                        historyRecyclerView.setVisibility(View.VISIBLE);
                       // contactNumberList.addAll(list);
                        //setAdapter();

                    } else {
                        if (pageNo == 0) {
                           // noDataTv.setVisibility(View.VISIBLE);
                            historyRecyclerView.setVisibility(View.GONE);
                        }
                    }

                } else {
                    showSimpleAlert(msg, "");
                }


                }
            }
        }



    private void setAdapter(){

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
        if (from.equals(getResources().getString(R.string.api_get_transaction_request))){ ;

        }
    }
}
