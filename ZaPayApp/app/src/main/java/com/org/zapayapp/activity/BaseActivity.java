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
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.adapters.NavigationAdapter;
import com.org.zapayapp.alert_dialog.AlertLogoutFragment;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.uihelpers.AdvanceDrawerLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.MySession;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * The type Base activity.
 */
public class BaseActivity extends AppCompatActivity implements SimpleAlertFragment.AlertSimpleCallback, APICallback, AlertLogoutFragment.AlertLogoutCallback {

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
    protected void onResume() {
        super.onResume();
        setHeaderData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivityInit();
        wValidationLib = new WValidationLib(this);
    }

    private void baseActivityInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
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
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_logout),activityContainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        Log.e("json", "json======1111==" + json);
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
        if (from.equals(getResources().getString(R.string.api_transaction_request))){
            intent = new Intent(BaseActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }else if (from.equals(getResources(). getString(R.string.update_your_profile))){
            intent = new Intent(BaseActivity.this, ProfileActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            //finish();
        }

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




    private void setHeaderData(){
       /* View headerView = navView.getHeaderView(0);
        imgLogo = headerView.findViewById(R.id.imgLogo);
        navTextName = headerView.findViewById(R.id.navTextName);*/

       if (SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE).toString().length() > 0) {
            Glide.with(BaseActivity.this)
                    .load(apiCalling.getImageUrl(SharedPref.getPrefsHelper().getPref(Const.Var.PROFILE_IMAGE).toString())).placeholder(R.mipmap.ic_user)
                    .into(imgLogo);
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME) != null && SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME).toString().length() > 0) {
            navTextName.setText(SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME, "") + " " + SharedPref.getPrefsHelper().getPref(Const.Var.LAST_NAME, ""));

        }
    }
}
