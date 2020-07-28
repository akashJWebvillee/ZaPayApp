package com.org.zapayapp.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import com.org.zapayapp.R;
import com.org.zapayapp.adapters.MyPaggerAdapter;
import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
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
        titleTV.setText(getString(R.string.transaction));

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.negotiation)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.completed)));

        tabLayout.setTabTextColors(getResources().getColor(R.color.tabTextColor), getResources().getColor(R.color.navTextColor));
        // setcustomLayout(tabLayout);

        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#04B804"));
         tabLayout.setSelectedTabIndicator(R.drawable.tab_indicator);


        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.pending));
        list.add(getString(R.string.negotiation));
        list.add(getString(R.string.completed));


        MyPaggerAdapter myPaggerAdapter = new MyPaggerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount(), list);
        viewPager.setAdapter(myPaggerAdapter);

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

    private void setcustomLayout(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(TransactionActivity.this).inflate(R.layout.custom_tab, null);
            //tv.setTextColor(customColor)
            tabLayout.getTabAt(i).setCustomView(tv);
        }
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
