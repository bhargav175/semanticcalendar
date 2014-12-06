package com.bhargav.smart.smartTasks.helpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.database.RepeatingTaskDBHelper;
import com.bhargav.smart.smartTasks.utils.utilFunctions;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 03-11-2014.
 */
public class DueHabitDialog extends AlertDialog.Builder {

    public FragmentActivity a;
    public Calendar c;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private TextView textView;
    private LinearLayout spinnerLayout;
    private View dialogLayout;
    private int year, month, day, hour, minute, second, tempYear, tempMonth, tempDay, tempHour, tempMinute;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog startDateDialog,endDateDialog;
    private List<String> dates, times;
    private Boolean hasReminder;
    private Calendar startDate,endDate,tempStartDate,tempEndDate;
    private Integer reminderId,currentHabitFrequency,tempHabitFrequency,tempDaysCode;
    private RepeatingTask repeatingTaskCurrent;
    private Button  timeButton, startDateButton,endDateButton;
    private RepeatingTaskDBHelper repeatingTaskDBHelper;
    private EditText habitText, habitQuestion, habitDuration, habitFrequency;
    private RepeatingTask.Type tempHabitType;
    private Spinner tag, frequencyBase, habitType;
    private LinearLayout fixedHabitLayout, flexibleHabitLayout;
    private Boolean sun, mon,tue,wed,thu,fri,sat;
    private Boolean tempSun, tempMon,tempTue,tempWed,tempThu,tempFri,tempSat;
    private TextView sundayButton, mondayButton, tuesdayButton, wednesdayButton,thursdayButton, fridayButton, saturdayButton;
    Integer habitId;

    ;


    public DueHabitDialog(final FragmentActivity a, final RepeatingTask repeatingTask, final Calendar c, final TextView textView, int y, int M, int d, int h, int m, final Boolean hasReminder, Integer reminderId) {
        super(a);
        // TODO Auto-generated constructor stub
        this.a = a;
        this.c = c;
        this.repeatingTaskCurrent = repeatingTask;
        this.textView = textView;
        LayoutInflater inflater = a.getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.due_habit_dialog, null);
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
        this.tempDaysCode = null;
        this.tempHabitFrequency = null;
        if(repeatingTask.getStartDate()!=null){
            this.startDate = repeatingTask.getStartDate();
        }
        else{
            this.startDate = Calendar.getInstance();
        }
            this.tempStartDate = (Calendar)this.startDate.clone();
        if(repeatingTask.getEndDate()!=null){
            this.endDate = repeatingTask.getEndDate();
        }
        else{
            this.endDate = Calendar.getInstance();
            this.endDate.add(Calendar.DAY_OF_YEAR,30);
        }
        this.tempEndDate = (Calendar)this.endDate.clone();

        initUi();
        setListeners(a);

    }

    private void setListeners(final FragmentActivity a) {

        this.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        year = tempYear;
                        month = tempMonth;
                        day = tempDay;
                        hour = tempHour;
                        minute = tempMinute;
                        confirmChanges();
                        setDueDate();

                    }
                }
        );
        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setDueDate();

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
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDateDialog.setYearRange(1985, 2028);
                startDateDialog.setCloseOnSingleTapDay(false);
                startDateDialog.show(a.getSupportFragmentManager(), DATEPICKER_TAG);
                startDateDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {

                        setTempStartDate(y, m, d);
                    }

                });
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDateDialog.setYearRange(1985, 2028);
                endDateDialog.setCloseOnSingleTapDay(false);
                endDateDialog.show(a.getSupportFragmentManager(), DATEPICKER_TAG);
                endDateDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {

                        setTempEndDate(y, m, d);
                    }

                });
            }
        });

    }


    private void initHabitParamsUi(){


        //2 spinners
        habitType = (Spinner) dialogLayout.findViewById(R.id.selectHabitType);
        frequencyBase = (Spinner) dialogLayout.findViewById(R.id.frequencyBase);

        sun = false;
        mon = false;
        tue = false;
        wed = false;
        thu = false;
        fri = false;
        sat = false;
        tempSun = false;
        tempMon = false;
        tempTue = false;
        tempWed = false;
        tempThu = false;
        tempFri = false;
        tempSat = false;


        //8 Buttons
        sundayButton = (TextView) dialogLayout.findViewById(R.id.sundayButton);
        mondayButton = (TextView) dialogLayout.findViewById(R.id.mondayButton);
        tuesdayButton = (TextView) dialogLayout.findViewById(R.id.tuesdayButton);
        wednesdayButton = (TextView) dialogLayout.findViewById(R.id.wednesdayButton);
        thursdayButton = (TextView) dialogLayout.findViewById(R.id.thursdayButton);
        fridayButton = (TextView) dialogLayout.findViewById(R.id.fridayButton);
        saturdayButton = (TextView) dialogLayout.findViewById(R.id.saturdayButton);
        repeatingTaskDBHelper = new RepeatingTaskDBHelper(a);
        repeatingTaskDBHelper.open();
        flexibleHabitLayout = (LinearLayout) dialogLayout.findViewById(R.id.linearLayoutFlexible);
        fixedHabitLayout = (LinearLayout) dialogLayout.findViewById(R.id.linearLayoutFixed);
        initializeSpinners();
        setListeners();

    }

    private void initUi() {
        initHabitParamsUi();
        spinnerLayout = (LinearLayout) dialogLayout.findViewById(R.id.spinnerLayout);
        timeButton = (Button) dialogLayout.findViewById(R.id.selectHabitTime);
        startDateButton = (Button) dialogLayout.findViewById(R.id.selectStartDate);
        endDateButton = (Button) dialogLayout.findViewById(R.id.selectEndDate);
        timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

            }
        }, hour, minute, false, false);

        startDateDialog  = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

            }
        }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH), false);


        endDateDialog  = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

            }
        }, endDate.get(Calendar.YEAR),  endDate.get(Calendar.MONTH),  endDate.get(Calendar.DAY_OF_MONTH), false);

        startDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(startDate.getTime()));
        endDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(endDate.getTime()));
        initializeReminder();

    }

    private void initializeSpinners(){
        List<String> durationStrings = new ArrayList<String>();
        durationStrings.addAll(Arrays.asList(RepeatingTask.durationStrings));


        ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(a,android.R.layout.simple_spinner_item,durationStrings);

        //habit Type
        List<String> habitTypeStrings = new ArrayList<String>();
        habitTypeStrings.add("Fixed");
        habitTypeStrings.add("Flexible");
        ArrayAdapter<String> habitTypeAdapter = new ArrayAdapter<String>(a,android.R.layout.simple_spinner_item,habitTypeStrings);
        habitType.setAdapter(habitTypeAdapter);
        habitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    tempHabitType = RepeatingTask.Type.FIXED;
                    if(tempDaysCode==null){
                        tempDaysCode = 1111111;
                    }
                    fixedHabitLayout.setVisibility(View.VISIBLE);
                    flexibleHabitLayout.setVisibility(View.GONE);
                }else{
                    tempHabitType = RepeatingTask.Type.FLEXIBLE;
                    if(tempHabitFrequency==null){
                        tempHabitFrequency = 0;
                    }
                    fixedHabitLayout.setVisibility(View.GONE);
                    flexibleHabitLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        habitType.setSelection(0);
        //Frequency
        List<String> habitFrequencyStrings = new ArrayList<String>();
        habitFrequencyStrings.addAll(Arrays.asList(RepeatingTask.frequencyStrings));
        ArrayAdapter<String> habitFrequencyAdapter = new ArrayAdapter<String>(a,android.R.layout.simple_spinner_item,habitFrequencyStrings);
        frequencyBase.setAdapter(habitFrequencyAdapter);
        frequencyBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempHabitFrequency = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        frequencyBase.setSelection(0);
    }
    private void initializeReminder() {


        if(repeatingTaskCurrent.getRepeatingTaskType()!=null){
            if(repeatingTaskCurrent.getRepeatingTaskType() == RepeatingTask.Type.FIXED){
                habitType.setSelection(0);

                int habitCurrentDaysCode = repeatingTaskCurrent.getDaysCode();
                List<Boolean> daysBoolean = RepeatingTask.getBooleansFromDayCode(habitCurrentDaysCode);
                sun = daysBoolean.get(0);
                mon = daysBoolean.get(1);
                tue = daysBoolean.get(2);
                wed = daysBoolean.get(3);
                thu = daysBoolean.get(4);
                fri = daysBoolean.get(5);
                sat = daysBoolean.get(6);
                tempSun = sun;
                tempMon = mon;
                tempTue = tue;
                tempWed = wed;
                tempThu = thu;
                tempFri = fri;
                tempSat = sat;

                setButtonState(sundayButton,tempSun);
                setButtonState(mondayButton,tempMon);
                setButtonState(tuesdayButton,tempTue);
                setButtonState(wednesdayButton,tempWed);
                setButtonState(thursdayButton,tempThu);
                setButtonState(fridayButton,tempFri);
                setButtonState(saturdayButton,tempSat);

            }else{
                habitType.setSelection(1);
                if(repeatingTaskCurrent.getFrequency()!=null){
                    frequencyBase.setSelection(repeatingTaskCurrent.getFrequency());
                }else{
                    frequencyBase.setSelection(0);
                }

            }
        }

       setDueDate();

    }

    private void setTempDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, tempHour);
        cal.set(Calendar.MINUTE, tempMinute);
        cal.set(Calendar.YEAR, tempYear);
        cal.set(Calendar.MONTH, tempMonth);
        cal.set(Calendar.DAY_OF_MONTH, tempDay);
        textView.setText("Due at - " + new SimpleDateFormat(utilFunctions.timeFormat).format(cal.getTime()));
        showTextView();
        timeButton.setText(new SimpleDateFormat(utilFunctions.timeFormat).format(cal.getTime()));
    }


    private void confirmChanges() {
        sun =tempSun;
        mon =tempMon;
        tue =tempTue;
        wed =tempWed;
        thu =tempThu;
        fri =tempFri;
        sat =tempSat;
        startDate = (Calendar) tempStartDate.clone();
        endDate = (Calendar) tempEndDate.clone();
        startDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(startDate.getTime()));
        endDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(endDate.getTime()));
        repeatingTaskCurrent.setStartDate((Calendar) startDate.clone());
        repeatingTaskCurrent.setEndDate((Calendar) endDate.clone());
        repeatingTaskCurrent.setRepeatingTaskType(tempHabitType);
        if(tempHabitType == RepeatingTask.Type.FIXED){
            repeatingTaskCurrent.setDaysCode(tempDaysCode);
            repeatingTaskCurrent.setFrequency(null);
        }
        else{
            repeatingTaskCurrent.setDaysCode(null);
            currentHabitFrequency = tempHabitFrequency;
            repeatingTaskCurrent.setFrequency(currentHabitFrequency);
        }
        setTempMetaText();
        hour = tempHour;
        minute = tempMinute;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        repeatingTaskCurrent.setDueTime((Calendar) cal.clone());


    }

    private void setStartDate(Integer y, Integer m, Integer d) {

        startDate.set(Calendar.YEAR,y);
        startDate.set(Calendar.MONTH,m);
        startDate.set(Calendar.DAY_OF_MONTH,d);
        if(endDate.compareTo(startDate)<=0){
            endDate = (Calendar) startDate.clone();
            endDate.add(Calendar.DAY_OF_YEAR,1);
        }
        repeatingTaskCurrent.setStartDate((Calendar) startDate.clone());
        setTempMetaText();
        startDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(startDate.getTime()));
    }

    private void setTempStartDate(Integer y, Integer m, Integer d) {

        tempStartDate.set(Calendar.YEAR,y);
        tempStartDate.set(Calendar.MONTH,m);
        tempStartDate.set(Calendar.DAY_OF_MONTH,d);
        if(tempEndDate.compareTo(tempStartDate)<=0){
            tempEndDate = (Calendar) tempStartDate.clone();
            tempEndDate.add(Calendar.DAY_OF_YEAR,1);
        }

        startDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(tempStartDate.getTime()));
        endDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(tempEndDate.getTime()));
    }

    private void setEndDate(Integer y, Integer m, Integer d) {

        endDate.set(Calendar.YEAR,y);
        endDate.set(Calendar.MONTH,m);
        endDate.set(Calendar.DAY_OF_MONTH,d);
        if(endDate.compareTo(startDate)<=0){
            endDate = (Calendar) startDate.clone();
            endDate.add(Calendar.DAY_OF_YEAR,1);
        }
        repeatingTaskCurrent.setEndDate((Calendar) endDate.clone());
        setTempMetaText();
        endDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(endDate.getTime()));
    }

    private void setTempEndDate(Integer y, Integer m, Integer d) {

        tempEndDate.set(Calendar.YEAR,y);
        tempEndDate.set(Calendar.MONTH,m);
        tempEndDate.set(Calendar.DAY_OF_MONTH,d);
        if(tempEndDate.compareTo(tempStartDate)<=0){
            tempEndDate = (Calendar) tempStartDate.clone();
            tempEndDate.add(Calendar.DAY_OF_YEAR,1);
        }
        endDateButton.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(tempEndDate.getTime()));
    }


    private void setTempMetaText(){
       textView.setText(RepeatingTask.getMetaText(repeatingTaskCurrent));
    }



    private void setDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        textView.setText("Due Date - " + new SimpleDateFormat(utilFunctions.dateTimeFormat).format(cal.getTime()));
        timeButton.setText(new SimpleDateFormat(utilFunctions.timeFormat).format(cal.getTime()));
        repeatingTaskCurrent.setDueTime((Calendar) cal.clone());
        setTempMetaText();
    }

    public Holder initialHolder(){
        return new Holder(repeatingTaskCurrent.getDueTime(),  reminderId);
    }

    public Holder returnUpdatedValues() {
            Calendar cal = null;
            cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.YEAR,2000);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MONTH,0);
            cal.set(Calendar.DAY_OF_MONTH, 1);


        return new Holder(cal,  reminderId);

    }

    private void setListeners() {
        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempSun = !tempSun;
                setButtonState(sundayButton,tempSun);
            }
        });
        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempMon = !tempMon;
                setButtonState(mondayButton,tempMon);
            }
        });
        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempTue = !tempTue;
                setButtonState(tuesdayButton,tempTue);
            }
        });
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempWed = !tempWed;
                setButtonState(wednesdayButton,tempWed);
            }
        });
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempThu = !tempThu;
                setButtonState(thursdayButton,tempThu);
            }
        });
        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempFri = !tempFri;
                setButtonState(fridayButton,tempFri);
            }
        });
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempSat = !tempSat;
                setButtonState(saturdayButton,tempSat);
            }
        });

    }

    public void setButtonState(TextView button, Boolean bool){
        if(bool){
            button.setBackgroundColor(a.getResources().getColor(R.color.light_blue_500));
            button.setTextColor(a.getResources().getColor(R.color.white));
        }
        else{
            button.setBackgroundColor(a.getResources().getColor(R.color.white));
            button.setTextColor(a.getResources().getColor(R.color.light_blue_500));
        }
        tempDaysCode = RepeatingTask.toDaysCode(sun, mon, tue, wed, thu, fri, sat);
    }
    public class Holder {
        public Calendar cal, startD, endD;
        public Integer remainderId;
        public Integer curFrequency;
        public Integer daysCode;
        public RepeatingTask.Type curHabitType;


        public Holder(Calendar cal, Integer reminderId) {
            if (cal == null) {
                this.cal = null;
            } else {
                this.cal = (Calendar) cal.clone();
            }
            this.remainderId = reminderId;
            this.startD =(Calendar) startDate.clone();
            this.endD =(Calendar) endDate.clone();
            this.curFrequency = currentHabitFrequency;
            this.curHabitType = repeatingTaskCurrent.getRepeatingTaskType();
            this.daysCode = RepeatingTask.toDaysCode(sun, mon, tue, wed, thu, fri, sat);

        }

        public Holder() {

        }
    }

    private void hideTextView() {
        textView.setVisibility(View.GONE);
    }

    private void showTextView() {
        textView.setVisibility(View.VISIBLE);

    }


}




