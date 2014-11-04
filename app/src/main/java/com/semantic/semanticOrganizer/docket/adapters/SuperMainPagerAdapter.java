package com.semantic.semanticOrganizer.docket.adapters;

/**
 * Created by Admin on 22-09-2014.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.semantic.semanticOrganizer.docket.fragments.TimeLineFragment;
import com.semantic.semanticOrganizer.docket.fragments.TodayFragment;

public class SuperMainPagerAdapter extends FragmentPagerAdapter {

    private String[] titles= {"Today", "Timeline"};

    public SuperMainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new TodayFragment();
            case 1:
                return new TimeLineFragment();


        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}