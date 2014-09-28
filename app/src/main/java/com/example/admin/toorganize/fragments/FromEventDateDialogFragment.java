package com.example.admin.toorganize.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.admin.toorganize.activities.WriteEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FromEventDateDialogFragment extends DialogFragment    implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePicker and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);


        ((WriteEvent)getActivity()).setFromDateEditText(year,month,day);
    }
    }







