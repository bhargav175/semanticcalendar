package com.bhargav.smart.smartTasks.adapters;

/**
 * Created by Admin on 22-09-2014.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bhargav.smart.smartTasks.fragments.ArchivedCards;
import com.bhargav.smart.smartTasks.fragments.ArchivedLists;
import com.bhargav.smart.smartTasks.fragments.RepeatingTaskDates;
import com.bhargav.smart.smartTasks.fragments.RepeatingTaskInfo;

public class ViewRepeatingTaskPagerAdapter extends FragmentPagerAdapter {

    private String[] titles= {"Status", "Info"};

    public ViewRepeatingTaskPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new RepeatingTaskDates();
            case 1:
                return new RepeatingTaskInfo();



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