package com.org.zapayapp.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.tabs.TabLayout;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.adapters.MyPagerAdapter;
import com.org.zapayapp.uihelpers.CustomViewPager;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

public class TransactionActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;

    /*Code for API calling*/
    public ZapayApp zapayApp;
    public APICalling apiCalling;
    public RestAPI restAPI;
    private String transaction_request_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        inIt();
        inItAction();
    }

    private void inIt() {
        toolbar = findViewById(R.id.toolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.transaction));

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

         tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        //tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.negotiation)));
         tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.accepted)));
         tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.decline)));
         tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.history)));

        tabLayout.setTabTextColors(getResources().getColor(R.color.tabTextColor), getResources().getColor(R.color.navTextColor));
        tabLayout.setSelectedTabIndicator(R.drawable.tab_indicator);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setPagingEnabled(false);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void inItAction() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        apiCalling = new APICalling(this);
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
