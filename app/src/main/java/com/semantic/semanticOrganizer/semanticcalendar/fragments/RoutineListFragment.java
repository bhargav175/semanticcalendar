package com.semantic.semanticOrganizer.semanticcalendar.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semantic.semanticOrganizer.semanticcalendar.R;

public class RoutineListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_routines_list, container, false);

        return rootView;
    }


}
