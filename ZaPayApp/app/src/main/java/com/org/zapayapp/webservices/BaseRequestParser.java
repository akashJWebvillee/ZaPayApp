package com.org.zapayapp.webservices;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Set;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BaseRequestParser {

    /**
     * The Run in background.
     */
    public boolean runInBackground = false;
    /**
     * The Hide key board.
     */
    public boolean hideKeyBoard = true;
    /**
     * The M file cache.
     */
    public FileCache mFileCache = null;
    /**
     * The File name.
     */
    public File fileName = null;
    /**
     * The Cache enabled.
     */
    public boolean cacheEnabled = false;
    /**
     * The Loader view.
     */
    public View loaderView = null;
    /**
     * The Server message.
     */
    public String serverMessage = "Something going wrong please try again later.";
    /**
     * The M response code.
     */
    public String mResponseCode = "0";
    /**
     * The App version.
     */
    public String app_version = "";
    private JSONObject mRespJSONObject = null;

    /**
     * Is run in background boolean.
     *
     * @return the boolean
     */
    public boolean isRunInBackground() {
        return runInBackground;
    }

    /**
     * Sets run in background.
     *
     * @param runInBackground the run in background
     */
    public void setRunInBackground(boolean runInBackground) {
        this.runInBackground = runInBackground;
    }

    /**
     * Is hide key board boolean.
     *
     * @return the boolean
     */
    public boolean isHideKeyBoard() {
        return hideKeyBoard;
    }

    /**
     * Sets hide key board.
     *
     * @param hideKeyBoard the hide key board
     */
    public void setHideKeyBoard(boolean hideKeyBoard) {
        this.hideKeyBoard = hideKeyBoard;
    }

    /**
     * Is cache enabled boolean.
     *
     * @return the boolean
     */
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    /**
     * Sets cache enabled.
     *
     * @param cacheEnabled the cache enabled
     */
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    /**
     * Show loader.
     *
     * @param activity the activity
     */
    public void showLoader(Activity activity) {
        if (activity != null && !(activity).isDestroyed()) {
            if (!runInBackground) {
                MyProgressDialog.getInstance().show(activity);
            }
        }
    }

    /**
     * Hide loader.
     *
     * @param activity the activity
     */
    public void hideLoader(Activity activity) {
        if (activity != null && !(activity).isDestroyed()) {
            if (!runInBackground) {
                MyProgressDialog.getInstance().dismiss();
            }
        }
    }

    /**
     * Gets main response in json.
     *
     * @return the main response in json
     */
    public JSONObject getMainResponseInJSON() {
        return mRespJSONObject;
    }

    /**
     * Parse json boolean.
     *
     * @param json     the json
     * @param mContext the m context
     * @return the boolean
     */
    public boolean parseJson(String json, final Context mContext) {
        if (!TextUtils.isEmpty(json)) {
            try {
                mRespJSONObject = new JSONObject(json);
                if (null != mRespJSONObject) {
                    mResponseCode = mRespJSONObject.optString("ResponseCode", "");
                    serverMessage = mRespJSONObject.optString("Message", serverMessage);
                    app_version = mRespJSONObject.optString("app_version", "");
                    if (TextUtils.isEmpty(mResponseCode)) {
                        mResponseCode = mRespJSONObject.optString("Status", "");
                    }
                    if (mResponseCode.equalsIgnoreCase("200") || mResponseCode.equalsIgnoreCase("201")) {
                        if (!TextUtils.isEmpty(app_version)) {
                            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext
                                    .getPackageName(), 0);
                            String currentVersionName = pInfo.versionName;
                            if (app_version.contains(",")) {
                                String[] versions = app_version.split(",");
                                boolean isAvail = false;
                                for (String serverVersion : versions) {
                                    if (serverVersion.equals(currentVersionName)) {
                                        isAvail = true;
                                        break;
                                    }
                                }
                                if (!isAvail) {
                                    // showUpdateDialog(mContext);
                                }
                            } else {
                                if (!app_version.equals(currentVersionName)) {
                                    //showUpdateDialog(mContext);
                                }
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Gets data array.
     *
     * @return the data array
     */
    public JSONArray getDataArray() {
        if (null == mRespJSONObject) {
            return null;
        }
        try {
            return mRespJSONObject.optJSONArray("Data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets data object.
     *
     * @return the data object
     */
    public Object getDataObject() {
        if (null == mRespJSONObject) {
            return null;
        }
        try {
            return mRespJSONObject.optJSONObject("Data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets hash map object.
     *
     * @param nameValuePair the name value pair
     * @return the hash map object
     */
    public HashMap<String, Object> getHashMapObject(Object... nameValuePair) {
        HashMap<String, Object> HashMap;
        if (null != nameValuePair && nameValuePair.length % 2 == 0) {
            HashMap = new HashMap<>();
            int i = 0;
            while (i < nameValuePair.length) {
                HashMap.put(nameValuePair[i].toString(), nameValuePair[i + 1]);
                i += 2;
            }
        } else {
            HashMap = new HashMap<>();
        }
        return HashMap;
    }

    /**
     * Gets hash map list object.
     *
     * @param nameValuePair the name value pair
     * @return the hash map list object
     */
    public HashMap<String, Object> getHashMapListObject(HashMap<String, Object> nameValuePair) {
        HashMap<String, Object> HashMap;
        if (null != nameValuePair && nameValuePair.size() > 0) {
            HashMap = new HashMap<>();
            int i = 0;
            Set<String> keys = nameValuePair.keySet();
            for (String key : keys) {
                Object value = nameValuePair.get(key);
                HashMap.put(key, value);
            }
        } else {
            HashMap = new HashMap<>();
        }
        return HashMap;
    }

    /**
     * Gets hash map object part.
     *
     * @param nameValuePair the name value pair
     * @return the hash map object part
     */
    public HashMap<String, RequestBody> getHashMapObjectPart(Object... nameValuePair) {
        HashMap<String, RequestBody> HashMap;
        if (null != nameValuePair && nameValuePair.length % 2 == 0) {
            HashMap = new HashMap<>();
            int i = 0;
            while (i < nameValuePair.length) {
                HashMap.put(nameValuePair[i].toString(), createPartFromString(nameValuePair[i + 1].toString()));
                i += 2;
            }
        } else {
            HashMap = new HashMap<>();
        }
        return HashMap;
    }

    private RequestBody createPartFromString(String partString) {
        return RequestBody.create(MultipartBody.FORM, partString);
    }
}
