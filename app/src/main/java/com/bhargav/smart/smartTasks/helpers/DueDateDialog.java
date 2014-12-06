package com.bhargav.smart.smartTasks.helpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.models.OneTimeTask;
import com.bhargav.smart.smartTasks.utils.utilFunctions;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.bhargav.smart.smartTasks.R;
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
    private Boolean hasReminder, hasDueDate,tempHasDueDate;
    private Integer reminderId;
    private OneTimeTask oneTimeTask;
    private Button addDueDateButton, dateButton, timeButton, closeButton;
    ;


    public DueDateDialog(final FragmentActivity a, final OneTimeTask oneTimeTask, final Calendar c, final TextView textView,int y, int M , int d, int h, int m,final Boolean hasReminder,final Boolean hasDueDate,Integer reminderId) {
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
        this.oneTimeTask = oneTimeTask;
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
        this.hasDueDate = hasDueDate;
        this.tempHasDueDate = hasDueDate;
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
                tempHasDueDate=false;
                setTimeAndDateTexts();
            }
        });

        addDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDueLayout.setVisibility(View.GONE);
                dueLayout.setVisibility(View.VISIBLE);
                tempHasDueDate= true;
                setTimeAndDateTexts();

            }
        });
        this.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        setNewTextViewText(true);

                    }
                }
        );
        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setNewTextViewText(false);

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
        if(hasDueDate){
            noDueLayout.setVisibility(View.GONE);
            dueLayout.setVisibility(View.VISIBLE);
            setDueDate();
        }else{
            noDueLayout.setVisibility(View.VISIBLE);
            dueLayout.setVisibility(View.GONE);
            setTextViewNone();
            }

    }

    private void setTempDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, tempHour);
        cal.set(Calendar.MINUTE, tempMinute);
        cal.set(Calendar.YEAR, tempYear);
        cal.set(Calendar.MONTH, tempMonth);
        cal.set(Calendar.DAY_OF_MONTH, tempDay);
        dateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(cal.getTime()));
        timeButton.setText(new SimpleDateFormat(utilFunctions.timeFormat).format(cal.getTime()));
    }

    private void setTimeAndDateTexts(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, tempHour);
        cal.set(Calendar.MINUTE, tempMinute);
        cal.set(Calendar.YEAR, tempYear);
        cal.set(Calendar.MONTH, tempMonth);
        cal.set(Calendar.DAY_OF_MONTH, tempDay);
        dateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(cal.getTime()));
        timeButton.setText(new SimpleDateFormat(utilFunctions.timeFormat).format(cal.getTime()));
    }


    private void setDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.SECOND, 0);
        setConfirmTextViewText();
        dateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(cal.getTime()));
        timeButton.setText(new SimpleDateFormat(utilFunctions.timeFormat).format(cal.getTime()));
    }
    private void setTextViewText(){
        textView.setText(OneTimeTask.getMetaText(oneTimeTask));

    }
    private void setConfirmTextViewText(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.SECOND, 0);
        oneTimeTask.setDueTime(cal);
        textView.setText(OneTimeTask.getMetaText(oneTimeTask));

    }
    private void setTextViewNone(){
        textView.setText("None");

    }

    private void setNewTextViewText(Boolean isEffected){
            if(isEffected){
                hasDueDate = tempHasDueDate;
                year=tempYear;
                month = tempMonth;
                day = tempDay;
                hour = tempHour;
                minute = tempMinute;
                second = 0;
            }else{

            }
            if(hasDueDate){
                setDueDate();
            }else{
                setTextViewNone();
            }
    }
    public Holder returnUpdatedValues(){
        Calendar cal = null;
        if(hasDueDate == false){
            cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        else{
            cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        return new Holder(cal, hasDueDate,reminderId);

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


}