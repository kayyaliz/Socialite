package edu.wit.mobileapp.socialite.GUI.NLU_Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_DataEntryFragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_NLU_Fragment;
import edu.wit.mobileapp.socialite.GUI.Testing_Fragments.Test_TA_Fragment;

public class NLUFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Graph", "List", "Info" };
    private Context context;

    public NLUFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new NLU_Graph_Fragment();
        } else if (position == 1) {
            fragment = new NLU_List_Fragment();
        } else if (position == 2) {
            fragment = new NLU_Info_Fragment();
        }
        return fragment;
    }

    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
