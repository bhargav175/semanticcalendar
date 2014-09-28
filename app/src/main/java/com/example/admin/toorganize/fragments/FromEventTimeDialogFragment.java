package com.example.admin.toorganize.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.example.admin.toorganize.activities.WriteEvent;

import java.util.Calendar;

public class FromEventTimeDialogFragment extends DialogFragment    implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ((WriteEvent)getActivity()).setFromTimeEditText(hourOfDay, minute);
        }
    }







