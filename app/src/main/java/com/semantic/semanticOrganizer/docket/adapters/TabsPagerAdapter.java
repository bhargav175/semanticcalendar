package com.semantic.semanticOrganizer.docket.adapters;

/**
 * Created by Admin on 22-09-2014.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

//        switch (index) {
//            case 0:
//                return new YesterdayFragment();
//            case 1:
//                return new TodayFragment();
//            case 2:
//                return new TomorrowFragment();
//            case 3:
//                return new NoteListFragment();
//
//        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}