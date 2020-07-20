package com.org.zapayapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.org.zapayapp.R;
import com.org.zapayapp.uihelpers.AdvanceDrawerLayout;
import com.org.zapayapp.utils.CommonMethods;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navView;
    private AdvanceDrawerLayout drawerLayout;
    protected FrameLayout activityContainer;
    protected Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private TextView navTextName, ownerTextName, ownerTextSubName;
    private int currentScreen;
    protected int MY_PROFILE = 0;
    protected int BANK_ACCOUNT = 1;
    protected int TRANSACTION = 2;
    protected int HISTORY = 3;
    protected int ABOUT_US = 4;
    protected int TERMS_CONDITION = 5;
    protected int HELP = 6;
    protected int LOGOUT = 7;
    private Intent intent;
    private float scaleFactor = 6f;

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
        View headerView = navView.getHeaderView(0);
        navTextName = headerView.findViewById(R.id.navTextName);
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
                        this, drawerLayout, toolbar, R.string.nav_drawer_opened, R.string.nav_drawer_closed);/*{

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        super.onDrawerSlide(drawerView, slideOffset);
                            float slideX = drawerView.getWidth() * slideOffset;
                            //CommonMethods.showLogs("B", "" + slideOffset);
                            activityContainer.setTranslationX(slideX);
                            activityContainer.setScaleX(1 - (slideOffset / scaleFactor));
                            activityContainer.setScaleY(1 - (slideOffset / scaleFactor));
                    }
                };*/
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
                drawerLayout.setRadius(GravityCompat.START, 25); //set end container's corner radius (dimension)
                drawerToggle.syncState();
            } else if (useToolbar() && getSupportActionBar() != null) {
                // Use home/auth_back button instead
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(CommonMethods.getDrawableWrapper(this, R.mipmap.ic_back));
            }
            navView.setNavigationItemSelectedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean useDrawerToggle() {
        return true;
    }

    protected boolean useToolbar() {
        return true;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        switch (item.getItemId()) {
            case R.id.navMyProfile:
                if (currentScreen != MY_PROFILE) {
                    CommonMethods.closeKeyboard(BaseActivity.this, activityContainer);
                    finish();
                }
                return true;
            case R.id.navBankAccount:
                if (currentScreen != BANK_ACCOUNT) {

                }
                return true;
            case R.id.navTransaction:
                if (currentScreen != TRANSACTION) {

                }
                return true;
            case R.id.navHistory:
                if (currentScreen != HISTORY) {
                    CommonMethods.closeKeyboard(BaseActivity.this, activityContainer);
                }
                return true;
            case R.id.navAboutUs:
                if (currentScreen != ABOUT_US) {
                    CommonMethods.closeKeyboard(BaseActivity.this, activityContainer);
                }
                return true;
            case R.id.navTerms:
                if (currentScreen != TERMS_CONDITION) {
                    clearLogout();
                }
                return true;
            case R.id.navHelp:
                if (currentScreen != HELP) {
                    clearLogout();
                }
                return true;
            case R.id.navLogout:
                if (currentScreen != LOGOUT) {
                    clearLogout();
                }
                return true;
        }
        return false;
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
