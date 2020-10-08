package com.org.zapayapp.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.org.zapayapp.fragment.HistoryCompletedFragment;
import com.org.zapayapp.fragment.HistoryNegotiationFragment;
import com.org.zapayapp.fragment.HistoryPendingFragment;

import java.util.ArrayList;

public class MyHistoryPaggerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private Context context;

    //Constructor to the class
    public MyHistoryPaggerAdapter(Context context, FragmentManager fm, int tabCount) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //Initializing tab count
        this.tabCount = tabCount;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs

        switch (position) {
            case 0:
                HistoryPendingFragment tab1 = new HistoryPendingFragment();
                return tab1;
            case 1:
                HistoryNegotiationFragment tab2 = new HistoryNegotiationFragment();
                return tab2;
            case 2:
                HistoryCompletedFragment tab3 = new HistoryCompletedFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
