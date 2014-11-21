package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.Habit;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 12-10-2014.
 */
public class MonthFragment extends Fragment {

    private Calendar mMonth;
    private TextView mMonthTitle;
    private List<HabitButton> mHabitButtons;
    private TableLayout mTableLayout;
    private List<TableRow> mTableRows;
    private TableRow datesRow;
    private Habit habit;
    private Calendar firstDayOfMonth;
    private ViewGroup view;
    private Context context;


    public static final MonthFragment newInstance(int year,int month, int habitId)
    {
        MonthFragment fragment = new MonthFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        bundle.putInt("habitId", habitId);

        fragment.setArguments(bundle);

        return fragment ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.month_table_layout, container, false);
        setHasOptionsMenu(true);
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        this.firstDayOfMonth = (Calendar) calendar.clone();
        this.habit = null;

        return rootView;
    }

    private void initWithDate(Context context, Calendar calendar){

        this.mMonthTitle = (TextView) view.findViewById(R.id.heading);
        this.mTableLayout = (TableLayout) view.findViewById(R.id.monthTableLayout);
        this.context = getActivity();
        this.mMonth = calendar;
        setMonth();
        this.mTableLayout.setStretchAllColumns(true);
        this.firstDayOfMonth = (Calendar) this.mMonth.clone();
        addDatesToTableRow();

    }
    private void setMonth(){
        this.mMonth.set(Calendar.DAY_OF_MONTH,1);
        this.mMonth.set(Calendar.HOUR_OF_DAY,0);
        this.mMonth.set(Calendar.MINUTE,0);
        this.mMonth.set(Calendar.SECOND,0);
        this.mMonthTitle.setText(this.mMonth.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault()));
        ;
    }
    private void addDatesToTableRow(){
        Calendar c = this.firstDayOfMonth;
        int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = c.get(Calendar.DAY_OF_WEEK);
        int buffer = getBufferDays(firstDay);
        createADateRow();
        for(int i =0 ; i<buffer ;i++){
            Button b =new Button(this.context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1f);
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
            HabitButton habitButton =  new HabitButton(this.context,temp,this.habit);
            if(this.datesRow.getChildCount()<7){
            }else{
                createADateRow();
            }
            this.datesRow.addView(habitButton,this.datesRow.getChildCount());
            c.add(Calendar.DATE, 1);
        }

    }

    private void createADateRow() {

        this.datesRow = new TableRow(this.context);
        this.datesRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        this.mTableLayout.addView(this.datesRow);


    }

    private int getBufferDays(int day){
        switch(day){
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

    private class GetHabit extends AsyncTask<String, Void, Void> {

        private int id;

        public GetHabit(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(String... params) {

            habit = Habit.getHabitById(id, getActivity());

            return null;
        }

        @Override
        protected void  onPostExecute(Void v) {

            initWithDate(context,firstDayOfMonth);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }





}
