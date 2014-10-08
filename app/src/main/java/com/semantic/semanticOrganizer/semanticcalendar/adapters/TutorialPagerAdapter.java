package com.semantic.semanticOrganizer.semanticcalendar.adapters;

/**
 * Created by Admin on 22-09-2014.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.semantic.semanticOrganizer.semanticcalendar.fragments.TutorialStepFourFragment;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.TutorialStepOneFragment;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.TutorialStepThreeFragment;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.TutorialStepTwoFragment;

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