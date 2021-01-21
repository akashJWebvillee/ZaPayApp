package com.org.zapayapp.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.adapters.NavigationAdapter;
import com.org.zapayapp.alert_dialog.AlertForcePopup;
import com.org.zapayapp.alert_dialog.AlertLogoutFragment;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.dialogs.DateChangeRequestDialogActivity;
import com.org.zapayapp.dialogs.EditProfileDialogActivity;
import com.org.zapayapp.uihelpers.AdvanceDrawerLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.MySession;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.viewModel.ProjectViewModel;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * The type Base activity.
 */
public class BaseActivity extends AppCompatActivity implements SimpleAlertFragment.AlertSimpleCallback, APICallback, AlertLogoutFragment.AlertLogoutCallback, AlertForcePopup.AlertForceCallback {

    private final String TAG = BaseActivity.class.getSimpleName();
    private NavigationView navView;
    private AdvanceDrawerLayout drawerLayout;
    /**
     * The Activity container.
     */
    protected FrameLayout activityContainer;
    /**
     * The Toolbar.
     */
    protected Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private TextView navTextName, ownerTextName, ownerTextSubName;
    private ImageView imgLogo;
    private RecyclerView navRecycler;
    private int currentScreen = 100;
    /**
     * The My profile.
     */
    protected int MY_PROFILE = 0;
    /**
     * The Bank account.
     */
    protected int BANK_ACCOUNT = 1;
    /**
     * The Transaction.
     */
    protected int TRANSACTION = 2;
    /**
     * The History.
     */
    protected int HISTORY = 3;
    /**
     * The About us.
     */
    protected int ABOUT_US = 4;
    /**
     * The Terms condition.
     */
    protected int TERMS_CONDITION = 5;
    /**
     * The Help.
     */
    protected int HELP = 6;
    /**
     * The Logout.
     */
    protected int LOGOUT = 8;//one value skip for view
    private Intent intent;
    private NavigationAdapter adapter;

    /*Code for API calling*/
    protected ZapayApp zapayApp;
    /**
     * The Gson.
     */
    protected Gson gson;
    /**
     * The Api calling.
     */
    protected APICalling apiCalling;
    /**
     * The Rest api.
     */
    protected RestAPI restAPI;
    /**
     * The Device token.
     */
    protected String deviceToken = "";

    public WValidationLib wValidationLib;

    /**
     * Socket connection
     */
    public Socket mSocket;
    private boolean isConnected;
    /*Chat sockets*/
    private String JOIN = "join_chat_ack";
    private String SEND_MSG_ACK = "send_message_ack";
    private String RECEIVE_MSG = "receive_message_ack";
    private String RECEIVE_MSG_ACK = "receive_message_success_ack";
    private String READ_MSG_ACK = "read_message_ack";


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // if (!isConnected) {
            isConnected = true;
            CommonMethods.showLogs("NETWORK_CONNECTION", "Emitter.Listener onConnect :- " + " Network Connected !!!");
            CommonMethods.showLogs("exception", isConnected + "isConnected");
        }
    };

    private Emitter.Listener onReConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // if (!isConnected) {
            isConnected = true;
            CommonMethods.showLogs("NETWORK_CONNECTION", "Emitter.Listener onReConnect :- " + " Network ReConnected !!!");
            CommonMethods.showLogs("exception", isConnected + "isConnected");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
            CommonMethods.showLogs("NETWORK_CONNECTION", "Emitter.Listener onDisconnect :- " + " Network disconnected !!!");
            CommonMethods.showLogs("exception", isConnected + " ");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            CommonMethods.showLogs("NETWORK_CONNECTION", "Emitter.Listener onConnectError :- " + " Network Connect Error !!!");

            CommonMethods.showLogs("exception", isConnected + " ");
        }
    };

    private Emitter.Listener onJoinEmtListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // final JSONObject jsonObject = (JSONObject) args[0];
            CommonMethods.showLogs("onJoin", "onJoin :- " + "Joined successfully !!" + args);
            CommonMethods.showLogs("SOCKET CONNECTION", "onJoinEmtListener called");
        }
    };

    private Emitter.Listener onSendMsgAckEmtListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject jsonObject = (JSONObject) args[0];
                CommonMethods.showLogs("onSendMsgAck", "onSendMsgAck :- " + jsonObject);
                if (jsonObject.getInt("status") == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onMsgSentReceived(jsonObject, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReceiveMsgEmtListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject jsonObject = (JSONObject) args[0];
                CommonMethods.showLogs(BaseActivity.class.getSimpleName(), "onReceiveMsgEmtListener :- " + jsonObject);
                if (jsonObject.getInt("status") == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onMsgSentReceived(jsonObject, true);
                                sendReceiveAck(jsonObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReceiveMsgAckEmtListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject jsonObject = (JSONObject) args[0];
                CommonMethods.showLogs(BaseActivity.class.getSimpleName(), "onReceiveMsgAck :- " + jsonObject);
                if (jsonObject.getInt("status") == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onMsgReceivedAck(jsonObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReadMsgAckEmtListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject jsonObject = (JSONObject) args[0];
                CommonMethods.showLogs(BaseActivity.class.getSimpleName(), "onReadMsgAck :- " + jsonObject);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onMsgReadAck(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        drawerLayout = (AdvanceDrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        activityContainer = drawerLayout.findViewById(R.id.activity_content);
        FrameLayout frameBase = drawerLayout.findViewById(R.id.frameBase);
        getLayoutInflater().inflate(layoutResID, frameBase, true);
        super.setContentView(drawerLayout);
        initialHeaderFooter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivityInit();
    }

    private void baseActivityInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
        wValidationLib = new WValidationLib(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            CommonMethods.showLogs(TAG, "getInstanceId failed " + task.getException());
                            return;
                        }

                        if (task.getResult() != null) {
                            deviceToken = task.getResult().getToken();
                            SharedPref.getPrefsHelper().savePref(Const.Var.DEVICE_TOKEN, deviceToken);
                            CommonMethods.showLogs(TAG, deviceToken);
                        }
                    }
                });
    }

    private void initialHeaderFooter() {
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.navView);
        // navTextName = findViewById(R.id.navTextName);
        imgLogo = navView.findViewById(R.id.imgLogo);
        navTextName = navView.findViewById(R.id.navTextName);
        //  setHeaderData(navView);
        navRecycler = findViewById(R.id.navRecycler);
        if (useToolbar()) {
            try {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }
                toolbar.setTitle("");
                getSupportActionBar().setTitle("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            toolbar.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        setUpNavView();
        fireBaseToken();
    }

    private void setUpNavView() {
        try {
            if (hideSlider()) {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else if (useDrawerToggle()) { // use the hamburger menu
                drawerToggle = new ActionBarDrawerToggle(
                        this, drawerLayout, toolbar, R.string.nav_drawer_opened, R.string.nav_drawer_closed);
                drawerLayout.addDrawerListener(drawerToggle);
                drawerToggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
                drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonMethods.closeKeyboard(BaseActivity.this);
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                });
                drawerToggle.setHomeAsUpIndicator(R.mipmap.menu);
                drawerLayout.setScrimColor(Color.TRANSPARENT);
                drawerLayout.setDrawerElevation(0f);
                drawerLayout.setViewScale(GravityCompat.START, 0.9f); //set height scale for main view (0f to 1f)
                drawerLayout.setViewElevation(GravityCompat.START, 20); //set main view elevation when drawer open (dimension)
                drawerLayout.setViewScrimColor(GravityCompat.START, Color.TRANSPARENT); //set drawer overlay coloe (color)
                drawerLayout.setDrawerElevation(20); //set drawer elevation (dimension)
                drawerLayout.setContrastThreshold(3); //set maximum of contrast ratio between white text and background color.
                drawerLayout.setRadius(GravityCompat.START, 40); //set end container's corner radius (dimension)
                drawerToggle.syncState();
            } else if (useToolbar() && getSupportActionBar() != null) {
                // Use home/auth_back button instead
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(CommonMethods.getDrawableWrapper(this, R.mipmap.ic_back));
            }
            setNavDrawerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to set the custom navigation drawer for selection
     */
    private void setNavDrawerView() {
        navRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        navRecycler.setItemAnimator(new DefaultItemAnimator());
        List<String> navList = new ArrayList<>();
        navList.add(getString(R.string.my_profile));
        navList.add(getString(R.string.bank_account));
        navList.add(getString(R.string.transaction));
        navList.add(getString(R.string.history));
        navList.add(getString(R.string.about_us));
        navList.add(getString(R.string.terms_and_conditions));
        navList.add(getString(R.string.help));
        navList.add("");
        navList.add(getString(R.string.logout));
        adapter = new NavigationAdapter(this, navList);
        navRecycler.setAdapter(adapter);
    }

    /**
     * Method use to show the drawer if required
     *
     * @return true /false according to required by default is false
     */
    protected boolean useDrawerToggle() {
        return false;
    }

    /**
     * Use toolbar boolean.
     *
     * @return the boolean
     */
    protected boolean useToolbar() {
        return false;
    }

    /**
     * Hide slider boolean.
     *
     * @return the boolean
     */
    protected boolean hideSlider() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            CommonMethods.closeKeyboard(BaseActivity.this, drawerLayout);
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On navigation item selected.
     *
     * @param position the position
     */
    public void onNavigationItemSelected(int position) {
        closeDrawer();
        CommonMethods.closeKeyboard(BaseActivity.this, activityContainer);
        switch (position) {
            case 0:
                if (currentScreen != MY_PROFILE) {
                    intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                }
                break;
            case 1:
                if (currentScreen != BANK_ACCOUNT) {
                    intent = new Intent(this, BankInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case 2:
                if (currentScreen != TRANSACTION) {
                    intent = new Intent(this, TransactionActivity.class);
                    startActivity(intent);
                }
                break;
            case 3:
                if (currentScreen != HISTORY) {
                    //intent = new Intent(this, HistoryActivity.class);
                    intent = new Intent(this, MyHistoryActivity.class);
                    startActivity(intent);
                }
                break;
            case 4:
                if (currentScreen != ABOUT_US) {
                    intent = new Intent(this, AboutUsActivity.class);
                    startActivity(intent);
                }
                break;
            case 5:
                if (currentScreen != TERMS_CONDITION) {
                    intent = new Intent(this, TermConditionActivity.class);
                    startActivity(intent);
                }
                break;
            case 6:
                if (currentScreen != HELP) {
                    intent = new Intent(this, HelpActivity.class);
                    startActivity(intent);
                }
                break;
            case 8:
                if (currentScreen != LOGOUT) {
                    alertLogOut();

                }
                break;
        }
    }


    /**
     * Sets current screen.
     *
     * @param currentScreen the current screen
     */
    protected void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
        if (adapter != null) {
            adapter.setSelected(currentScreen);
        }
    }

    private void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    private void callAPILogout() {
        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postApiToken(token, getString(R.string.api_logout));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_logout), activityContainer);
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

            if (from.equals(getResources().getString(R.string.api_logout))) {
                if (status == 200) {
                    clearLogout();
                } else {
                    showSimpleAlert(msg, "");
                }
            }

        }
    }

    private void clearLogout() {
        disconnectSocket();
        MySession.removeSession();
        intent = new Intent(BaseActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
        if (from.equals(getResources().getString(R.string.api_transaction_request))) {
            intent = new Intent(BaseActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else if (from.equals(getResources().getString(R.string.update_your_profile))) {
            //intent = new Intent(BaseActivity.this, ProfileActivity.class);
            intent = new Intent(BaseActivity.this, EditProfileDialogActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (from.equals(getResources().getString(R.string.please_add_bank_account))) {
            intent = new Intent(BaseActivity.this, BankInfoActivity.class);
            startActivity(intent);
        } else if (from.equals(getResources().getString(R.string.please_verify_bank_account))) {
            intent = new Intent(BaseActivity.this, BankInfoActivity.class);
            startActivity(intent);
        } else if (from.equals(getResources().getString(R.string.api_update_transaction_request_status))) {
            finish();
        } else if (from.equalsIgnoreCase(getString(R.string.api_signup))) {
            moveToLogin();
        } else if (from.equalsIgnoreCase(getString(R.string.update_pin))) {
            finish();
        } else if (from.equalsIgnoreCase(getString(R.string.set_new_pin))) {
            Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else if (from.equalsIgnoreCase(getResources().getString(R.string.api_pay_date_request_status_update))) {
           // finish();
        }
    }

    protected void moveToLogin() {

    }

    private void alertLogOut() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("header", getString(R.string.are_you_sure_do_you_want_to_logout));
            args.putString("textOk", getString(R.string.ok));
            args.putString("textCancel", getString(R.string.cancel));
            AlertLogoutFragment alert = new AlertLogoutFragment();
            alert.setArguments(args);
            alert.show(fm, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLogoutCallback(boolean value) {
        if (value) {
            callAPILogout();
        }
    }

    public void showForceUpdate(String from, String headerMsg, boolean isAddress, String btnCancel, boolean isCancel) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("header", headerMsg);
            args.putString("textOk", getString(R.string.ok));
            args.putString("textCancel", btnCancel);
            args.putString("from", from);
            args.putBoolean("isAddress", isAddress);
            AlertForcePopup alert = new AlertForcePopup();
            alert.setCancelable(isCancel);
            alert.setArguments(args);
            alert.show(fm, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onForceCallback(String from, boolean isAddress) {
        if (from.equals(getString(R.string.session_expired))) {
            clearLogout();
        } else if (from.equalsIgnoreCase(getString(R.string.do_you_want_to_close_the_application))) {
            finish();
        }
    }


    private void setHeaderData() {
        if (SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE).toString().length() > 0) {
            Glide.with(BaseActivity.this)
                    .load(apiCalling.getImageUrl(SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE).toString()))
                    .placeholder(R.mipmap.default_profile)
                    .into(imgLogo);
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME) != null && SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME).toString().length() > 0) {
            navTextName.setText(SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME, "") + " " + SharedPref.getPrefsHelper().getPref(Const.Var.LAST_NAME, ""));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID) != null && SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString().length() > 0) {
            disconnectSocket(); // 2
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID) != null && SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString().length() > 0) {
            setHeaderData();
            disconnectSocket(); // 2
            connectSocket();
        }
    }

    public void connectSocket() {
        CommonMethods.showLogs("SOCKET CONNECTION", "connectSocket() called");
        mSocket = zapayApp.getmSocket();
        try {
            if (mSocket != null/* && !mSocket.connected()*/) {
                mSocket.on(Socket.EVENT_CONNECT, onConnect);
                mSocket.on(Socket.EVENT_RECONNECT, onReConnect);
                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                mSocket.on(JOIN, onJoinEmtListener);
                mSocket.on(SEND_MSG_ACK, onSendMsgAckEmtListener);
                mSocket.on(RECEIVE_MSG, onReceiveMsgEmtListener);
                mSocket.on(RECEIVE_MSG_ACK, onReceiveMsgAckEmtListener);
                mSocket.on(READ_MSG_ACK, onReadMsgAckEmtListener);
                mSocket.connect();
                CommonMethods.showLogs(BaseActivity.class.getSimpleName(), "mSocket.connected() :- " + true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String user_id = SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID, "");
        if (user_id != null) {
            if (!user_id.equals("")) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("uid", user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("join_chat", user_id);
                CommonMethods.showLogs("uid", user_id + " ");
            }
        }
    }

    public void disconnectSocket() {
        CommonMethods.showLogs("SOCKET CONNECTION", "disconnectSocket() called --- " + 2);
        mSocket = zapayApp.getmSocket();
        try {
            if (mSocket != null /*&& mSocket.connected()*/) {
                mSocket.disconnect();
                mSocket.off(Socket.EVENT_CONNECT, onConnect);
                mSocket.off(Socket.EVENT_RECONNECT, onReConnect);
                mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                mSocket.off(JOIN, onJoinEmtListener);
                mSocket.off(SEND_MSG_ACK, onSendMsgAckEmtListener);
                mSocket.off(RECEIVE_MSG, onReceiveMsgEmtListener);
                mSocket.off(RECEIVE_MSG_ACK, onReceiveMsgAckEmtListener);
                mSocket.off(READ_MSG_ACK, onReadMsgAckEmtListener);
                CommonMethods.showLogs(BaseActivity.class.getSimpleName(), "mSocket.connected() :- " + false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMsgSentReceived(JSONObject jsonObject, boolean isReceive) {
        CommonMethods.showLogs(BaseActivity.class.getSimpleName(), "onMsgSentReceived :-" + jsonObject);
        CommonMethods.showLogs(BaseActivity.class.getSimpleName(), "isReceive :-" + isReceive);
    }

    public void onMsgReceivedAck(JSONObject jsonObject) {
        CommonMethods.showLogs("msg in class", jsonObject.toString());
        CommonMethods.showLogs("BaseActivity", "onMsgReceivedAck -- MSG Received Listener  :-" + jsonObject);
    }

    public void onMsgReadAck(JSONObject jsonObject) {
        CommonMethods.showLogs("msg in class", jsonObject.toString());
        CommonMethods.showLogs("BaseActivity", "onMsgReadAck -- MSG Read Listener  :-" + jsonObject);
    }

    private void sendReceiveAck(final JSONObject object) {
        try {

            ProjectViewModel projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);

            //{"status":200,"message":"success","data":{"receiver_id":"52","sender_id":"53","message":"Hello","transaction_request_id":"103","message_id":108,"status":0,"created_at":1598699350222}}
            JSONObject msg_data = null;
            String transaction_request_id = "", msg_sender_id = "", msg_id = "";
            msg_data = object.getJSONObject("data");
            transaction_request_id = msg_data.get("transaction_request_id").toString();  // 74
            msg_sender_id = msg_data.get("sender_id").toString(); //73
            msg_id = msg_data.get("message_id").toString(); //73

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sender_id", msg_sender_id);
            jsonObject.put("message_id", msg_id);
            jsonObject.put("transaction_request_id", transaction_request_id);
            mSocket.emit("receive_message_success", jsonObject.toString());
            CommonMethods.showLogs("ChatActivity", "receive_message_ack !!!" + jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fireBaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tag", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        if (task.getResult() != null && task.getResult().getToken().length() > 0) {
                            String newToken = task.getResult().getToken();
                            SharedPref.getPrefsHelper().savePref(Const.Var.FIREBASE_DEVICE_TOKEN, newToken);
                        }

                        Log.e("Firebase token", "Firebase token=====" + task.getResult().getToken());
                    }
                });

    }
}
