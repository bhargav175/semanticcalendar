package com.bhargav.smart.smartTasks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;


public class TutorialStepThreeFragment extends Fragment {
    public static final String Tag = "CalendarPrint";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        setHasOptionsMenu(true);
        TextView heading = (TextView) rootView.findViewById(R.id.heading);
        TextView paragraph = (TextView) rootView.findViewById(R.id.paragraph);
        heading.setText("Notes, Todos and Habits!");
        paragraph.setText("Create reminders in all of them");

   return rootView;
    }
}
