package com.semantic.semanticOrganizer.docket.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.activities.LandingActivity;


public class TutorialStepFourFragment extends Fragment {
    public static final String Tag = "CalendarPrint";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        setHasOptionsMenu(true);
        TextView heading = (TextView) rootView.findViewById(R.id.heading);
        TextView paragraph = (TextView) rootView.findViewById(R.id.paragraph);
        heading.setText("Lets Go!");
        paragraph.setText("We have created some lists for you to help you get started!");
        Button button = (Button) rootView.findViewById(R.id.tag);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), LandingActivity.class);
                startActivity(intent);
            }
        });

   return rootView;
    }
}
