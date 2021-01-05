package com.org.zapayapp.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.org.zapayapp.fragment.AcceptedFragment;
import com.org.zapayapp.fragment.DeclineFragment;
import com.org.zapayapp.fragment.HistoryAcceptedFragment;
import com.org.zapayapp.fragment.HistoryCompletedFragment;
import com.org.zapayapp.fragment.HistoryDeclineFragment;
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
                HistoryAcceptedFragment tab3 = new HistoryAcceptedFragment();
                return tab3;

            case 3:
                HistoryCompletedFragment tab4 = new HistoryCompletedFragment();
                return tab4;
            case 4:
                HistoryDeclineFragment tab5 = new HistoryDeclineFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
