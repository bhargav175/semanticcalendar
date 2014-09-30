package com.semantic.semanticOrganizer.semanticcalendar.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semantic.semanticOrganizer.semanticcalendar.R;


public class TutorialStepTwoFragment extends Fragment {
    public static final String Tag = "CalendarPrint";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        setHasOptionsMenu(true);
        TextView heading = (TextView) rootView.findViewById(R.id.heading);
        TextView paragraph = (TextView) rootView.findViewById(R.id.paragraph);
        heading.setText("Create Calendar Events!");
        paragraph.setText("These events are synced with Google Calendar");

   return rootView;
    }
}
