package com.bhargav.smart.smartTasks.adapters;

/**
 * Created by Admin on 22-09-2014.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bhargav.smart.smartTasks.fragments.TutorialStepFourFragment;
import com.bhargav.smart.smartTasks.fragments.TutorialStepOneFragment;
import com.bhargav.smart.smartTasks.fragments.TutorialStepThreeFragment;
import com.bhargav.smart.smartTasks.fragments.TutorialStepTwoFragment;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new TutorialStepOneFragment();
            case 1:
                return new TutorialStepTwoFragment();
            case 2:
                return new TutorialStepThreeFragment();
            case 3:
                return new TutorialStepFourFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}