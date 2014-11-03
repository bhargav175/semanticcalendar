package com.semantic.semanticOrganizer.semanticcalendar.helpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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