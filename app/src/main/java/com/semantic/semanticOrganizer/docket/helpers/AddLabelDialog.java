package com.semantic.semanticOrganizer.docket.helpers;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.semantic.semanticOrganizer.docket.R;

/**
 * Created by Admin on 03-11-2014.
 */
public class AddLabelDialog extends AlertDialog.Builder {



    private View dialogLayout;


    public AddLabelDialog(final FragmentActivity a) {
        super(a);
        // TODO Auto-generated constructor stub


        LayoutInflater inflater = a.getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.label_dialog, null);
        this.setView(dialogLayout);


    }





}