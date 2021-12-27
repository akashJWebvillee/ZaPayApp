package com.org.zapayapp.adapters;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.org.zapayapp.fragment.AcceptedFragment;
import com.org.zapayapp.fragment.CompletedFragment;
import com.org.zapayapp.fragment.DeclineFragment;
import com.org.zapayapp.fragment.PendingFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private Context context;

    //Constructor to the class
    public MyPagerAdapter(Context context, FragmentManager fm, int tabCount) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //Initializing tab count
        this.tabCount = tabCount;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PendingFragment tab1 = new PendingFragment();
                return tab1;
          /*  case 1:
                NegotiationFragment tab2 = new NegotiationFragment();
                return tab2;*/
            case 1:
                AcceptedFragment tab2 = new AcceptedFragment();
                return tab2;
            case 2:
                DeclineFragment tab3 = new DeclineFragment();
                return tab3;
            case 3:
                CompletedFragment tab4 = new CompletedFragment();
                return tab4;

            default:
                return null;

        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }
}
