package com.semantic.semanticOrganizer.semanticcalendar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.activities.LandingActivity;


public class TodayFragment extends Fragment {
    public static final String Tag = "CalendarPrint";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        setHasOptionsMenu(true);


   return rootView;
    }
}
