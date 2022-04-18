package com.org.zapayapp.webservices;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.utils.CommonMethods;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import static com.org.zapayapp.webservices.WNetworkCheck.TYPE_NOT_CONNECTED;
import static com.org.zapayapp.webservices.WNetworkCheck.getConnectivityStatus;

public class APICalling extends BaseRequestParser implements ServiceCallback<JsonElement> {
    private static final String BASE_URL = "https://developer.webvilleedemo.xyz/zapay/api/";
    public static final String CHAT_SERVER_URL = "http://mean.webvilleedemo.xyz:3005";
    private static final String IMAGE_URL = "https://developer.webvilleedemo.xyz/zapay/";

   /* private static final String BASE_URL = "https://zapay.io/api/";
    public static final String CHAT_SERVER_URL = "https://chat.zapay.io/";
    private static final String IMAGE_URL = "https://zapay.io/";*/

    private String TAG = "APICalling";
    private Activity activity;
    private Gson gson;
    private String result = "";
    private static Retrofit retrofit = null;

    /**
     * Instantiates a new Api calling.
     *
     * @param context the context
     */
    public APICalling(Context context) {
        activity = (Activity) context;
        mFileCache = new FileCache(activity.getResources().getString(R.string.app_name_folder), activity);
    }

    /**
     * Web service interface rest api.
     *
     * @return the rest api
     */
    public static RestAPI webServiceInterface() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
                 httpClient.addInterceptor(logging);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(RestAPI.class);
    }


 /**
     * Web service interface rest api.
     *
     * @return the rest api
     */
    public static RestAPI webServiceUploadInterface() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(RestAPI.class);
    }

    /**
     * Gets facebook image client.
     *
     * @return the facebook image client
     */
    public static RestAPI getFacebookImageClient() {
        // if (retrofit == null) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://graph.facebook.com/") // REMEMBER TO END with /
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //  }
        return retrofit.create(RestAPI.class);
    }

    /**
     * This method is used to fecth
     *
     * @return linked in image client
     */
    public static RestAPI getLinkedInImageClient() {
        //  if (retrofit == null) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://lh3.googleusercontent.com/") // REMEMBER TO END with /
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // }
        return retrofit.create(RestAPI.class);
    }

    /**
     * Gets image url.
     *
     * @param imageName the image name
     * @return the image url
     */
    public static String getImageUrl(String imageName) {
        return IMAGE_URL + imageName;
    }

    /**
     * Gets data object.
     *
     * @param <T>        the type parameter
     * @param jsonObject the json object
     * @param t          the t
     * @return the data object
     */
    public <T> Object getDataObject(JsonObject jsonObject, Class<T> t) {
        Gson gsm = new Gson();
        if (null != jsonObject && !TextUtils.isEmpty(jsonObject.toString())) {
            return gsm.fromJson(jsonObject.toString(), t);
        }
        return null;
    }

    /**
     * Gets data list.
     *
     * @param <T>        the type parameter
     * @param jsonObject the json object
     * @param key        the key
     * @param t          the t
     * @return the data list
     */
    public <T> List<T> getDataList(JsonObject jsonObject, String key, final Class<T> t) {
        Gson gsm = new Gson();
        List<T> list = null;
        list = new ArrayList<>();
        if (null != jsonObject) {
            if (jsonObject.get(key) != null && jsonObject.get(key).isJsonArray()) {
                JsonArray jsonArray = jsonObject.getAsJsonArray(key);
                if (null != jsonArray) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Object obj = gsm.fromJson(jsonArray.get(i), t);
                        if (obj != null && t.isAssignableFrom(obj.getClass()))
                            list.add(t.cast(obj));
                    }
                }
            }
        }
        return list;
    }

    /**
     * Gets data array list.
     *
     * @param <T>       the type parameter
     * @param jsonArray the json array
     * @param key       the key
     * @param t         the t
     * @return the data array list
     */
    public <T> List<T> getDataArrayList(JsonArray jsonArray, String key, final Class<T> t) {
        Gson gsm = new Gson();
        List<T> list = new ArrayList<>();
        if (null != jsonArray) {
            //JsonArray jsonArray = jsonObject.getAsJsonArray(key);
            for (int i = 0; i < jsonArray.size(); i++) {
                Object obj = gsm.fromJson(jsonArray.get(i), t);
                if (obj != null && t.isAssignableFrom(obj.getClass()))
                    list.add(t.cast(obj));
            }
        }
        return list;
    }

    /**
     * Cast collection list.
     *
     * @param <T>     the type parameter
     * @param srcList the src list
     * @param clas    the clas
     * @return the list
     */
    public <T> List<T> castCollection(List srcList, Class<T> clas) {
        List<T> list = new ArrayList<>();
        for (Object obj : srcList) {
            if (obj != null && clas.isAssignableFrom(obj.getClass()))
                list.add(clas.cast(obj));
        }
        return list;
    }

    /**
     * Call api.
     *
     * @param app  the app
     * @param call the call
     * @param from the from
     */
    public void callAPI(final ZapayApp app, Call<JsonElement> call, final String from, final View view) {
        gson = new Gson();
        final APICallback apiCallback = app.getApiCallback();
        if (from != null)
            showLoader(activity);
        if (hideKeyBoard) {
            try {
                CommonMethods.closeKeyboard(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mFileCache = new FileCache(from, activity);
        fileName = cacheEnabled ? mFileCache.getFile((from + result).hashCode() + ".req") : null;

        if (!TextUtils.isEmpty(FileCache.readFile(fileName)) && cacheEnabled && (TYPE_NOT_CONNECTED == getConnectivityStatus(activity))) {
            String lastResponse = FileCache.readFile(fileName);
            if (apiCallback != null) {
                hideLoader(activity);
                JsonElement element = gson.fromJson(lastResponse, JsonElement.class);
                JsonObject jsonObj = element.getAsJsonObject();
                apiCallback.apiCallback(jsonObj, from);
            }
            return;
        }

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                int statusCode = response.code();
                hideLoader(activity);


                try {
                    if (statusCode >= 200 && statusCode < 300 && response.isSuccessful()) {
                        JsonElement user1 = response.body();
                        result = gson.toJson(user1);
                        if (null != fileName) {
                            FileCache.writeFile(fileName, result.getBytes());
                        }
                        if (apiCallback != null) {
                            JsonElement element = gson.fromJson(result, JsonElement.class);
                            JsonObject jsonObj = element.getAsJsonObject();
                            apiCallback.apiCallback(jsonObj, from);
                        }
                    } else if (statusCode == 401) {
                        // unauthenticated(response, call, this, view);

                        //this code Write by
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("status","401");
                        jsonObject.addProperty("message","Unauthorized Access!");
                        apiCallback.apiCallback(jsonObject, from);

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
                hideLoader(activity);
                Log.e("statusCode","onFailure statusCode=="+call);
                if (t instanceof IOException) {
                    networkError((IOException) t, call, this, view);
                } else {
                    unexpectedError(t, call, this, view);
                }
            }
        });
    }


    public void callAPI2(final ZapayApp app, Call<JsonElement> call, final String from, final View view) {
        gson = new Gson();
        final APICallback apiCallback = app.getApiCallback();
        if (from != null)
            //showLoader(activity);
        if (hideKeyBoard) {
            try {
                CommonMethods.closeKeyboard(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mFileCache = new FileCache(from, activity);
        fileName = cacheEnabled ? mFileCache.getFile((from + result).hashCode() + ".req") : null;

        if (!TextUtils.isEmpty(FileCache.readFile(fileName)) && cacheEnabled && (TYPE_NOT_CONNECTED == getConnectivityStatus(activity))) {
            String lastResponse = FileCache.readFile(fileName);
            if (apiCallback != null) {
               // hideLoader(activity);
                JsonElement element = gson.fromJson(lastResponse, JsonElement.class);
                JsonObject jsonObj = element.getAsJsonObject();
                apiCallback.apiCallback(jsonObj, from);
            }
            return;
        }

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                int statusCode = response.code();
              //  hideLoader(activity);


                try {
                    if (statusCode >= 200 && statusCode < 300 && response.isSuccessful()) {
                        JsonElement user1 = response.body();
                        result = gson.toJson(user1);
                        if (null != fileName) {
                            FileCache.writeFile(fileName, result.getBytes());
                        }
                        if (apiCallback != null) {
                            JsonElement element = gson.fromJson(result, JsonElement.class);
                            JsonObject jsonObj = element.getAsJsonObject();
                            apiCallback.apiCallback(jsonObj, from);
                        }
                    } else if (statusCode == 401) {
                        // unauthenticated(response, call, this, view);

                        //this code Write by
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("status","401");
                        jsonObject.addProperty("message","Unauthorized Access!");
                        apiCallback.apiCallback(jsonObject, from);

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
                //hideLoader(activity);
                Log.e("statusCode","onFailure statusCode=="+call);
                if (t instanceof IOException) {
                    networkError((IOException) t, call, this, view);
                } else {
                    unexpectedError(t, call, this, view);
                }
            }
        });
    }

    /**
     * Call api.
     *
     * @param app      the app
     * @param call     the call
     * @param from     the from
     * @param isCancel the is cancel
     */
    public void callSearchAPI(final ZapayApp app, Call<JsonElement> call, final String from, final boolean isCancel) {
        gson = new Gson();
        final APICallback apiCallback = app.getApiCallback();
        if (from != null)
            showLoader(activity);
        if (hideKeyBoard) {
            try {
                CommonMethods.closeKeyboard(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mFileCache = new FileCache(from, activity);
        fileName = cacheEnabled ? mFileCache.getFile((from + result).hashCode() + ".req") : null;

        if (!TextUtils.isEmpty(FileCache.readFile(fileName)) && cacheEnabled && (TYPE_NOT_CONNECTED == getConnectivityStatus(
                activity))) {
            String lastResponse = FileCache.readFile(fileName);
            if (apiCallback != null) {
                hideLoader(activity);
                JsonElement element = gson.fromJson(lastResponse, JsonElement.class);
                JsonObject jsonObj = element.getAsJsonObject();
                apiCallback.apiCallback(jsonObj, from);
            }
            return;
        }

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                int statusCode = response.code();
                hideLoader(activity);
                try {
                    if (statusCode >= 200 && statusCode < 300 && response.isSuccessful()) {
                        JsonElement user1 = response.body();
                        result = gson.toJson(user1);
                        if (null != fileName) {
                            FileCache.writeFile(fileName, result.getBytes());
                        }
                        if (apiCallback != null) {
                            JsonElement element = gson.fromJson(result, JsonElement.class);
                            JsonObject jsonObj = element.getAsJsonObject();
                            apiCallback.apiCallback(jsonObj, from);
                        }
                    } else if (statusCode == 401) {
                        if (!isCancel) {
                            unauthenticated(response, null, null, null);
                        }
                    } else if (statusCode >= 400 && statusCode < 500) {
                        if (!isCancel) {
                            clientError(response, null, null, null);
                        }
                    } else if (statusCode >= 500 && statusCode < 600) {
                        if (!isCancel) {
                            serverError(response, null, null, null);
                        }
                    } else {
                        if (!isCancel) {
                            unexpectedError(new RuntimeException("Unexpected response " + response), null, null, null);
                        }
                    }
                } catch (Exception e) {
                    CommonMethods.showLogs(TAG, "api calling  " + e.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                hideLoader(activity);
                if (!isCancel) {
                    if (t instanceof IOException) {
                        // networkError((IOException) t);
                    } else {
                        //  unexpectedError(t);
                    }
                }
            }
        });
    }

    @Override
    public void unauthenticated(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            CommonMethods.showToast(activity, activity.getResources().getString(R.string.unable_auth));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void clientError(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            CommonMethods.showToast(activity, activity.getResources().getString(R.string.client_not_response));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void serverError(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            CommonMethods.showToast(activity, activity.getResources().getString(R.string.server_not_response));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void networkError(IOException e, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            CommonMethods.showToast(activity, activity.getResources().getString(R.string.network_conn));
        } else {
            retry(call, callback, view);
        }
    }

    @Override
    public void unexpectedError(Throwable t, Call<JsonElement> call, Callback<JsonElement> callback, View view) {
        if (call == null) {
            CommonMethods.showToast(activity, activity.getResources().getString(R.string.something_wrong));
        } else {
            retry(call, callback, view);
        }
    }

    public void retry(final Call<JsonElement> call, final Callback<JsonElement> callback, View view) {
        try {
            Snackbar.make(view, activity.getResources().getString(R.string.something_wrong_check_network), Snackbar.LENGTH_INDEFINITE).
                    setAction(activity.getResources().getString(R.string.retry), new View.OnClickListener() {
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
