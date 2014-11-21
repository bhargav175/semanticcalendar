package com.bhargav.smart.smartTasks.helpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.semantic.semanticOrganizer.docket.R;

/**
 * Created by Admin on 03-11-2014.
 */
public class ComingUpDialog extends AlertDialog.Builder {

    public FragmentActivity a;

    private View dialogLayout;



    public ComingUpDialog(final FragmentActivity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.a = a;

        LayoutInflater inflater = a.getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.work_in_progress, null);
        this.setView(dialogLayout);
        this.setTitle("Coming Up Soon!");

        initUi();
        setListeners(a);

    }

    private void setListeners(final FragmentActivity a) {

        this.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                    }
                }
        );
        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });



    }


    private void initUi() {

    }



}