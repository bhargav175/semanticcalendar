package com.semantic.semanticOrganizer.docket.helpers;

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
import com.semantic.semanticOrganizer.docket.R;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 03-11-2014.
 */
public class DueDateDialog extends AlertDialog.Builder {

    public FragmentActivity a;
    public Calendar c;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private TextView textView;
    private LinearLayout spinnerLayout;
    private RelativeLayout noDueLayout,dueLayout;

    private View dialogLayout;
    private int year, month, day, hour, minute, second, tempYear,tempMonth,tempDay,tempHour,tempMinute;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private List<String> dates, times;
    private Boolean hasReminder;
    private Integer reminderId;
    private Button addDueDateButton, dateButton, timeButton, closeButton;
    ;


    public DueDateDialog(final FragmentActivity a, final Calendar c, final TextView textView,int y, int M , int d, int h, int m,final Boolean hasReminder,Integer reminderId) {
        super(a);
        // TODO Auto-generated constructor stub
        this.a = a;
        this.c = c;
        this.textView = textView;
        LayoutInflater inflater = a.getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.custom_dialog, null);
        this.setView(dialogLayout);
        this.setTitle("Add Due Time");
        this.year = y;
        this.month = M;
        this.day = d;
        this.hour = h;
        this.minute = m;
        this.tempYear = y;
        this.tempMonth = M;
        this.tempDay = d;
        this.tempHour = h;
        this.tempMinute = m;
        this.hasReminder = hasReminder;
        this.reminderId = reminderId;
        initUi();
        setListeners(a);

    }

    private void setListeners(final FragmentActivity a) {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDueLayout.setVisibility(View.VISIBLE);
                dueLayout.setVisibility(View.GONE);
                hasReminder=false;
                hideTextView();
            }
        });

        addDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDueLayout.setVisibility(View.GONE);
                dueLayout.setVisibility(View.VISIBLE);
                showTextView();
                hasReminder= true;
            }
        });
        this.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        year=tempYear;
                        month = tempMonth;
                        day = tempDay;
                        hour = tempHour;
                        minute = tempMinute;
                        setDueDate();

                    }
                }
        );
        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setDueDate();

            }
        });


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(a.getSupportFragmentManager(), DATEPICKER_TAG);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {
                        tempYear = y;
                        tempMonth = m;
                        tempDay = d;
                        setTempDueDate();
                    }

                });
            }
        });


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(a.getSupportFragmentManager(), TIMEPICKER_TAG);
                timePickerDialog.initialize(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int h, int m) {
                        tempHour = h;
                        tempMinute = m;
                        setTempDueDate();

                    }
                }, hour, minute, false, false);

            }


        });

    }


    private void initUi() {
        noDueLayout = (RelativeLayout) dialogLayout.findViewById(R.id.noDueLayout);
        dueLayout = (RelativeLayout) dialogLayout.findViewById(R.id.dueLayout);
        spinnerLayout = (LinearLayout) dialogLayout.findViewById(R.id.spinnerLayout);
        dateButton = (Button) dialogLayout.findViewById(R.id.dateButton);
        timeButton = (Button) dialogLayout.findViewById(R.id.timeButton);
        closeButton = (Button) dialogLayout.findViewById(R.id.close);
        addDueDateButton = (Button) dialogLayout.findViewById(R.id.addDueDateButton);


        datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

            }
        }, year, month, day, false);
        timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

            }
        }, hour, minute, false, false);

        initializeReminder();

    }
    private void initializeReminder(){
        if(hasReminder){
            noDueLayout.setVisibility(View.GONE);
            dueLayout.setVisibility(View.VISIBLE);
            setDueDate();
        }else{
            noDueLayout.setVisibility(View.VISIBLE);
            dueLayout.setVisibility(View.GONE);
            setDueDate();
            hideTextView();
            }

    }

    private void setTempDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, tempHour);
        cal.set(Calendar.MINUTE, tempMinute);
        cal.set(Calendar.YEAR, tempYear);
        cal.set(Calendar.MONTH, tempMonth);
        cal.set(Calendar.DAY_OF_MONTH, tempDay);
        textView.setText("Due Date - " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(cal.getTime()));
        showTextView();
        dateButton.setText(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()));
        timeButton.setText(new SimpleDateFormat("HH:mm").format(cal.getTime()));
    }


    private void setDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        textView.setText("Due Date - " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(cal.getTime()));
        dateButton.setText(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()));
        timeButton.setText(new SimpleDateFormat("HH:mm").format(cal.getTime()));
    }
    public Holder returnUpdatedValues(){
        Calendar cal = null;
        if(hasReminder == false){
            cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
        }
        else{
            cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
        }
        return new Holder(cal, hasReminder,reminderId);

    }
    public class Holder{
        public Calendar cal;
        public Boolean bool;
        public Integer remainderId;
        public Holder(Calendar cal, Boolean bool, Integer reminderId){
            if(cal == null){
                this.cal = null;
            }else{
                this.cal=(Calendar) cal.clone();
            }
            this.bool=bool;
            this.remainderId = reminderId;
        }
        public Holder(){

        }
    }
    private void hideTextView(){
        textView.setVisibility(View.GONE);
    }
    private void showTextView(){
        textView.setVisibility(View.VISIBLE);
    }




}