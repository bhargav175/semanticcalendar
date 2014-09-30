package com.semantic.semanticOrganizer.semanticcalendar.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.Event;
import com.semantic.semanticOrganizer.semanticcalendar.utils.utilFunctions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class TutorialStepOneFragment extends Fragment {
    public static final String Tag = "CalendarPrint";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        setHasOptionsMenu(true);
        TextView heading = (TextView) rootView.findViewById(R.id.heading);
        TextView paragraph = (TextView) rootView.findViewById(R.id.paragraph);
        heading.setText("Plan!");
        paragraph.setText("Plan your day like never before");

   return rootView;
    }
}
