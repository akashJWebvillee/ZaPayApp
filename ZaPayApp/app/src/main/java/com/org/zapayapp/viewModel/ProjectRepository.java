package com.org.zapayapp.viewModel;
import android.content.Context;
import android.view.View;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;
import com.org.zapayapp.webservices.ServiceCallback;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository implements ServiceCallback<JsonElement> {

    private String TAG = "ProjectRepository";
    private RestAPI restAPI;
    private Context mContext;

    public ProjectRepository() {
        restAPI = APICalling.webServiceInterface();
    }

    public MutableLiveData<JsonObject> checkAppVersion(Context context, APICalling apiCalling, Gson gson, View view) {
        this.mContext = context;
        MutableLiveData<JsonObject> liveDataObj = new MutableLiveData<>();
      /*  Call<JsonElement> call = restAPI.getApi(context.getString(R.string.api_signup));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                int statusCode = response.code();
                try {
                    if (statusCode >= 200 && statusCode < 300 && response.isSuccessful()) {
                        JsonElement user1 = response.body();
                        String result = gson.toJson(user1);
                        JsonElement element = gson.fromJson(result, JsonElement.class);
                        JsonObject jsonObj = element.getAsJsonObject();
                        liveDataObj.setValue(jsonObj);
                    } else if (statusCode == 401) {
                        unauthenticated(response, call, this, view);
                    } else if (statusCode >= 400 && statusCode < 500) {
                        clientError(response, call, this, view);
                    } else if (statusCode >= 500 && statusCode < 600) {
                        serverError(response, call, this, view);
                    } else {
                        unexpectedError(new RuntimeException("Unexpected response " + response), call, this, view);
                    }
                } catch (Exception e) {
                    CommonMethods.showLogs(TAG, "api calling  " + e.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                if (t instanceof IOException) {
                    networkError((IOException) t, call, this, view);
                } else {
                    unexpectedError(t, call, this, view);
                }
            }
        });*/
        return liveDataObj;
    }

    @Override
    public void unauthenticated(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            if (mContext != null)
                CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.unable_auth));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void clientError(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            if (mContext != null)
                CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.client_not_response));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void serverError(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            if (mContext != null)
                CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.server_not_response));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void networkError(IOException e, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            if (mContext != null)
                CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.network_conn));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void unexpectedError(Throwable t, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            if (mContext != null)
                CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.something_wrong));
        } else {
            retry(call, callback, view);
        }
    }

    private void retry(final Call<JsonElement> call, final Callback<JsonElement> callback, View view) {
        try {
            Snackbar.make(view, mContext.getResources().getString(R.string.something_wrong_check_network), Snackbar.LENGTH_INDEFINITE).
                    setAction(mContext.getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            call.clone().enqueue(callback);
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
