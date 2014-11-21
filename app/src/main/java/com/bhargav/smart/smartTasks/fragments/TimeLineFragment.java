package com.bhargav.smart.smartTasks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhargav.smart.smartTasks.R;


public class TimeLineFragment extends Fragment {
    public static final String Tag = "CalendarPrint";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_time, container, false);
        setHasOptionsMenu(true);


   return rootView;
    }
}
