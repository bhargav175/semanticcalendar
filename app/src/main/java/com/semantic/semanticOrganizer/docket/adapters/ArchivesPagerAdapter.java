package com.semantic.semanticOrganizer.docket.adapters;

/**
 * Created by Admin on 22-09-2014.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.semantic.semanticOrganizer.docket.fragments.ArchivedCards;
import com.semantic.semanticOrganizer.docket.fragments.ArchivedLists;

public class ArchivesPagerAdapter extends FragmentPagerAdapter {

    private String[] titles= {"Lists", "Notes", "CheckLists","Habits"};

    public ArchivesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new ArchivedLists();
            case 1:
                return new ArchivedCards();



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