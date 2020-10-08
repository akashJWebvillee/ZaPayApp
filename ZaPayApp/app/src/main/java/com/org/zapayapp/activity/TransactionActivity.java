package com.org.zapayapp.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.tabs.TabLayout;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.MyPagerAdapter;
import com.org.zapayapp.uihelpers.CustomViewPager;
import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;

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
        backArrowImageView = (ImageView) toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.transaction));

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.negotiation)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.completed)));

        tabLayout.setTabTextColors(getResources().getColor(R.color.tabTextColor), getResources().getColor(R.color.navTextColor));
        tabLayout.setSelectedTabIndicator(R.drawable.tab_indicator);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setPagingEnabled(false);
        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void inItAction() {
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
