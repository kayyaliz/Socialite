package edu.wit.mobileapp.socialite.GUI.Testing_Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TestingFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Data", "NLU", "Tone" };
    private Context context;

    public TestingFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
//        return PageFragment.newInstance(position + 1);
        Fragment fragment = null;
        if (position == 0) {
            fragment = new Test_DataEntryFragment();
        } else if (position == 1) {
            fragment = new Test_NLU_Fragment();
        } else if (position == 2) {
            fragment = new Test_TA_Fragment();
        }
        return fragment;
}

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}