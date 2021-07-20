package com.org.zapayapp.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.TransactionActivity;
import com.org.zapayapp.adapters.TransactionCompletedAdapter;
import com.org.zapayapp.model.TransactionModel;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.EndlessRecyclerViewScrollListener;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.webservices.APICallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
public class CompletedFragment extends Fragment implements APICallback {
    private TransactionActivity activity;
    private List<TransactionModel> transactionList;
    private RecyclerView completedRecyclerView;
    private TransactionCompletedAdapter transactionAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int pageNo = 0;
    private TextView noDataTv;

    public CompletedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        inIt(view);
        initAction();
        return view;
    }

    private void inIt(View view) {
        transactionList = new ArrayList<>();
        activity = (TransactionActivity) getActivity();
        completedRecyclerView = view.findViewById(R.id.completedRecyclerView);
        noDataTv = view.findViewById(R.id.noDataTv);
    }

    private void initAction() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        completedRecyclerView.setLayoutManager(layoutManager);
        completedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pageNo = page;
                callAPIGetTransactionRequest(page);
            }
        };

        completedRecyclerView.addOnScrollListener(scrollListener);
        // pageNo = 0;
        // callAPIGetTransactionRequest(pageNo);
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 0;
        callAPIGetTransactionRequest(pageNo);
    }

    private void callAPIGetTransactionRequest(int page) {
        //  0=pending 1=negotiate, 2=accept
        if (pageNo == 0 && scrollListener != null) {
            scrollListener.resetState();
        }
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = activity.apiCalling.getHashMapObject(
                    "status", "4",
                    "page", page);

            activity.zapayApp.setApiCallback(this);
            Call<JsonElement> call = activity.restAPI.postWithTokenApi(token, getString(R.string.api_get_transaction_request), values);
            if (activity.apiCalling != null) {
                activity.apiCalling.callAPI(activity.zapayApp, call, getString(R.string.api_get_transaction_request), completedRecyclerView);
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

            if (from.equals(getResources().getString(R.string.api_get_transaction_request))) {
                if (status == 200) {
                    if (pageNo == 0) {
                        transactionList.clear();
                    }
                    List<TransactionModel> list = activity.apiCalling.getDataList(json, "data", TransactionModel.class);
                    if (list.size() > 0) {
                        noDataTv.setVisibility(View.GONE);
                        completedRecyclerView.setVisibility(View.VISIBLE);
                        transactionList.addAll(list);
                        setAdapter();
                    } else {
                        if (pageNo == 0) {
                            noDataTv.setVisibility(View.VISIBLE);
                            completedRecyclerView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (pageNo == 0) {
                        noDataTv.setVisibility(View.VISIBLE);
                        completedRecyclerView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void setAdapter() {
        //TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity(), transactionList, getString(R.string.transaction));
        // completedRecyclerView.setAdapter(transactionAdapter);
        if (transactionAdapter == null) {
            transactionAdapter = new TransactionCompletedAdapter(getActivity(), transactionList, getString(R.string.transaction));
            completedRecyclerView.setAdapter(transactionAdapter);
        } else {
            transactionAdapter.notifyDataSetChanged();
        }
    }
}

