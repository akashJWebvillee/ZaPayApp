package com.org.zapayapp.adapters;
import android.app.PendingIntent;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.org.zapayapp.fragment.CompletedFragment;
import com.org.zapayapp.fragment.NegotiationFragment;
import com.org.zapayapp.fragment.PendingFragment;

import java.util.ArrayList;

public class MyPaggerAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    private Context context;
    private ArrayList<String> list;

    //Constructor to the class
    public MyPaggerAdapter(Context context, FragmentManager fm, int tabCount, ArrayList<String> list) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs

        switch (position) {
            case 0:
                PendingFragment tab1 = new PendingFragment();
                return tab1;
            case 1:
                NegotiationFragment tab2 = new NegotiationFragment();
                return tab2;
            case 2:
                CompletedFragment tab3 = new CompletedFragment();
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
