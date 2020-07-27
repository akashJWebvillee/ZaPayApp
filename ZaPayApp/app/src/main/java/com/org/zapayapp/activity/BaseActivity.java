package com.org.zapayapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.NavigationAdapter;
import com.org.zapayapp.uihelpers.AdvanceDrawerLayout;
import com.org.zapayapp.utils.CommonMethods;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private NavigationView navView;
    private AdvanceDrawerLayout drawerLayout;
    protected FrameLayout activityContainer;
    protected Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private TextView navTextName, ownerTextName, ownerTextSubName;
    private RecyclerView navRecycler;
    private int currentScreen = 100;
    protected int MY_PROFILE = 0;
    protected int BANK_ACCOUNT = 1;
    protected int TRANSACTION = 2;
    protected int HISTORY = 3;
    protected int ABOUT_US = 4;
    protected int TERMS_CONDITION = 5;
    protected int HELP = 6;
    protected int LOGOUT = 8;//one value skip for view
    private Intent intent;
    private NavigationAdapter adapter;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        drawerLayout = (AdvanceDrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        activityContainer = drawerLayout.findViewById(R.id.activity_content);
        FrameLayout frameBase = drawerLayout.findViewById(R.id.frameBase);
        getLayoutInflater().inflate(layoutResID, frameBase, true);
        super.setContentView(drawerLayout);
        initialHeaderFooter();
    }

    private void initialHeaderFooter() {
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.navView);
        navTextName = findViewById(R.id.navTextName);
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
     * @return true/false according to required by default is false
     */
    protected boolean useDrawerToggle() {
        return false;
    }

    protected boolean useToolbar() {
        return false;
    }

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

    public void onNavigationItemSelected(int position) {
        closeDrawer();
        CommonMethods.closeKeyboard(BaseActivity.this, activityContainer);
        switch (position) {
            case 0:
                if (currentScreen != MY_PROFILE) {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                    //finish();
                }
                break;
            case 1:
                if (currentScreen != BANK_ACCOUNT) {
                    Intent intent = new Intent(this, BankInfoActivity.class);
                    startActivity(intent);
                    //finish();
                }
                break;
            case 2:
                if (currentScreen != TRANSACTION) {
                    Intent intent = new Intent(this, BorrowSummaryActivity.class);
                    startActivity(intent);
                    // finish();
                }
                break;
            case 3:
                if (currentScreen != HISTORY) {
                    Intent intent = new Intent(this, HistoryActivity.class);
                    startActivity(intent);
                    // finish();
                }
                break;
            case 4:
                if (currentScreen != ABOUT_US) {
                    Intent intent = new Intent(this, AboutUsActivity.class);
                    startActivity(intent);
                    //finish();
                }
                break;
            case 5:
                if (currentScreen != TERMS_CONDITION) {
                    Intent intent = new Intent(this, TermConditionActivity.class);
                    startActivity(intent);
                    // finish();
                }
                break;
            case 6:
                if (currentScreen != HELP) {
                    Intent intent = new Intent(this, HelpActivity.class);
                    startActivity(intent);
                    // finish();
                }
                break;
            case 8:
                if (currentScreen != LOGOUT) {
                    clearLogout();
                }
                break;
        }
    }

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

    private void clearLogout() {
        intent = new Intent(BaseActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
