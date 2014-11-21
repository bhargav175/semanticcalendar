package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.Habit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 12-10-2014.
 */
public class MonthLayout extends LinearLayout {

    private Calendar mMonth, currMonthSelected;
    private TextView mMonthTitle;
    private List<HabitButton> mHabitButtons;
    private TableLayout mTableLayout;
    private List<TableRow> mTableRows;
    private TableRow datesRow;
    private Habit habit;
    private Context context;
    private Calendar firstDayOfMonth;
    private ViewGroup view;
    private int index;

    private Spinner monthSpinner, yearSpinner;
    private Integer currYear;
    private String[] mMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private List<Integer> mYears;


    public MonthLayout(Context context) {
        super(context);
    }

    public MonthLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MonthLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public MonthLayout(Context context, Calendar calendar, Habit habit) {
        super(context);

    }

    private void clearTableDateRows() {
        for (int i = 0; i < this.mTableRows.size(); i++) {
            this.mTableLayout.removeView(this.mTableRows.get(i));
        }
    }

    public void setMonth(Calendar calendar) {
        clearTableDateRows();
        this.mMonth = (Calendar) calendar.clone();
        this.mMonth.set(Calendar.DAY_OF_MONTH, 0);
        this.mMonth.set(Calendar.HOUR_OF_DAY, 0);
        this.mMonth.set(Calendar.MINUTE, 0);
        this.mMonth.set(Calendar.SECOND, 1);
        initializeCalendarView();

    }

    public void setSelectedMonth() {
        clearTableDateRows();
        this.mMonth = (Calendar) currMonthSelected.clone();
        this.mMonth.set(Calendar.DAY_OF_MONTH, 0);
        this.mMonth.set(Calendar.HOUR_OF_DAY, 0);
        this.mMonth.set(Calendar.MINUTE, 0);
        this.mMonth.set(Calendar.SECOND, 1);
        initializeCalendarView();

    }


    public Calendar getMonth() {
        return this.mMonth;
    }


    private void init() {

        inflate(context, R.layout.month_table_layout, this);
        this.monthSpinner = (Spinner) view.findViewById(R.id.monthSpinner);
        ArrayAdapter<String> monthSpinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mMonths);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, position);
                setMonth(calendar);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monthSpinner.setAdapter(monthSpinnerAdapter);
        this.yearSpinner = (Spinner) view.findViewById(R.id.yearSpinner);
        this.mTableLayout = (TableLayout) view.findViewById(R.id.monthTableLayout);
        setExactMonth();


    }

    private void initWithDate(Context context, Calendar calendar, Habit habit) {
        this.mYears = new ArrayList<Integer>();
        this.currYear = calendar.get(Calendar.YEAR);
        int i;
        for (i = 0; i < 10; i++)
        {
            mYears.add(0, currYear - i);
        }
        for (i = 1; i < 10; i++) {
            mYears.add(currYear + i);
        }
        this.mTableLayout = (TableLayout) findViewById(R.id.monthTableLayout);
        this.mTableLayout.setStretchAllColumns(true);
        this.monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        this.currMonthSelected =(Calendar) calendar.clone();
        ArrayAdapter<String> monthSpinnerAdapter = new ArrayAdapter<String>(context,  R.layout.spinner_list_item, mMonths);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currMonthSelected.set(Calendar.MONTH, position);
                setSelectedMonth();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monthSpinner.setAdapter(monthSpinnerAdapter);
        this.yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        ArrayAdapter<Integer> yearSpinnerAdapter = new ArrayAdapter<Integer>(context, R.layout.spinner_list_item, mYears);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currMonthSelected.set(Calendar.YEAR, mYears.get(position));
                setSelectedMonth();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearSpinner.setAdapter(yearSpinnerAdapter);
        this.mTableRows = new ArrayList<TableRow>();
        this.habit = habit;
        this.context = context;
        this.mMonth = calendar;
        initializeCalendarView();

    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    private void initializeCalendarView() {
        this.setId(Calendar.getInstance().getActualMaximum(Calendar.YEAR) * 1000 + Calendar.getInstance().getActualMaximum(Calendar.MONTH));
        setExactMonth();
        this.firstDayOfMonth = (Calendar) this.mMonth.clone();
        addDatesToTableRow();
    }

    private void setExactMonth() {
        this.mMonth.set(Calendar.DAY_OF_MONTH, 1);
        this.mMonth.set(Calendar.HOUR_OF_DAY, 0);
        this.mMonth.set(Calendar.MINUTE, 0);
        this.mMonth.set(Calendar.SECOND, 0);
        this.monthSpinner.setSelection(this.mMonth.get(Calendar.MONTH));
        this.yearSpinner.setSelection(getYearIndex(this.mMonth.get(Calendar.YEAR)));
        ;
    }

    private void addDatesToTableRow() {
        Calendar c = this.firstDayOfMonth;
        int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = c.get(Calendar.DAY_OF_WEEK);
        int buffer = getBufferDays(firstDay);
        createADateRow();
        for (int i = 0; i < buffer; i++) {
            Button b = new Button(this.context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(2, 2, 2, 2);
            b.setLayoutParams(lp);
            b.setGravity(Gravity.CENTER);
            b.setBackgroundColor(getResources().getColor(R.color.white));
            b.setTextColor(getResources().getColor(R.color.white));
            b.setText("-");
            b.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.material_micro_text_size));
            this.datesRow.addView(b);
        }
        for (int i = 0; i < numberOfDays; i++) {
            Calendar temp = (Calendar) c.clone();
            HabitButton habitButton = new HabitButton(this.context, temp, this.habit);
            if (this.datesRow.getChildCount() < 7) {
            } else {
                createADateRow();
            }
            this.datesRow.addView(habitButton, this.datesRow.getChildCount());
            c.add(Calendar.DATE, 1);
        }
        c.add(Calendar.DATE,-1);
        int remainingDays = getRemDays(c.get(Calendar.DAY_OF_WEEK));
        for (int i = 0; i < remainingDays; i++) {
            Button b = new Button(this.context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(2, 2, 2, 2);
            b.setLayoutParams(lp);
            b.setGravity(Gravity.CENTER);
            b.setBackgroundColor(getResources().getColor(R.color.white));
            b.setTextColor(getResources().getColor(R.color.white));
            b.setText("-");
            b.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.material_micro_text_size));
            this.datesRow.addView(b);
        }

    }

    private void createADateRow() {

        this.datesRow = new TableRow(this.context);
        this.datesRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        this.mTableRows.add(this.datesRow);
        this.mTableLayout.addView(this.datesRow);


    }
    private int getYearIndex(int y){
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        return 9+ y - thisYear;
    }

    private int getRemDays(int lastDay){
        switch (lastDay) {
            case Calendar.MONDAY:
                return 5;
            case Calendar.TUESDAY:
                return 4;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 2;
            case Calendar.FRIDAY:
                return 1;
            case Calendar.SATURDAY:
                return 0;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 1;
        }
    }

    private int getBufferDays(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            case Calendar.SUNDAY:
                return 0;
            default:
                return 1;
        }
    }


    public void initializeWith(Context context, Calendar currMonth, Habit habitCurrent) {
        initWithDate(context, currMonth, habitCurrent);


    }
}
