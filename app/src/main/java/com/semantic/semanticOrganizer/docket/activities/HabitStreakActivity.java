package com.semantic.semanticOrganizer.docket.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.adapters.InfinitePagerAdapter;
import com.semantic.semanticOrganizer.docket.adapters.WrapContentHeightViewPager;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.helpers.HabitButton;
import com.semantic.semanticOrganizer.docket.helpers.MonthLayout;
import com.semantic.semanticOrganizer.docket.models.Habit;
import com.semantic.semanticOrganizer.docket.models.HabitItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HabitStreakActivity extends Activity {


    //Check if the habit is due today
    //Create habit item if it is due today
    //async update habit item
    //show next due date time
    //show progress of the user & success rate
    //if the habit tracks forever then it show progress goal for 1 month.. only a success rate
    // show habit items this week & disable dates which are ahead of time
    //show simple graph showing his overall graph
    //show button to switch to agenda view

    private Habit habitCurrent;
    private Boolean isHabitDueToday;
    private ImageView updateHabitButton;
    private CardView dueTodayCard, singleMonthCalendar;
    private TableRow weekHabitItemsTableRow;
    private TableLayout weekTableLayout;
    private List<HabitItem> habitItemList;
    private LinearLayout calendarMonthView;
    private TextView ViewFullHabitStats;
    private HorizontalScrollView calendarHorizontalScrollView;
    private WrapContentHeightViewPager habitStreakPager;
    private InfinitePagerAdapter habitStreakAdapter;
    private static final int PAGE_LEFT = 0;
    private static final int PAGE_MIDDLE = 1;
    private static final int PAGE_RIGHT = 2;
    private int mSelectedPageIndex = 1;
    private List<MonthLayout> monthLayouts = new ArrayList<MonthLayout>();
    private Calendar currMonth;
    private MonthLayout v0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_habit_streak);
        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        if(extras!=null){
           Integer  habitId = extras.getInt(DBHelper.COLUMN_ID);
            habitItemList = new ArrayList<HabitItem>();
            //checking habit itemss


            //checking habit items
            new GetHabit(habitId).execute("");

        }else{
            Toast.makeText(this, "Could not load habit", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.habit_streak, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    private Boolean getIsHabitDueToday(){
        //get todays date and day
        //are we withing duration?
        //is habit fixed?
                 // is today's day in the habit cycle? if yes okay.

                //else no
        //else
                //is today one of the days allotted
                        //was the habit finished already in the set of days? if yes no
                         //else okay
                //else no

        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Timestamp createdTime = Timestamp.valueOf(habitCurrent.getCreatedTime());
        Calendar createdDay = new GregorianCalendar();
        createdDay.setTimeInMillis( createdTime.getTime() );
        createdDay.set(Calendar.HOUR_OF_DAY,0);
        createdDay.set(Calendar.MINUTE,0);
        createdDay.set(Calendar.SECOND,0);
        Integer durationCount = 0;
        int habitDurationCountVal =Habit.getDurationCountFromDurationString(habitCurrent.getDuration());
        if(habitDurationCountVal<10000){
            durationCount = habitDurationCountVal- 1;
        }else{
            durationCount = null;
        }


        if(durationCount != null){
            Calendar lastDay =(Calendar) createdDay.clone();
            lastDay.add(Calendar.DATE,durationCount);

           if(today.compareTo(lastDay)<=0){
               if(habitCurrent.getHabitType()== Habit.Type.FIXED){
                   Calendar calendar = Calendar.getInstance();
                   int day = calendar.get(Calendar.DAY_OF_WEEK);
                   Boolean dayDue;
                   List<Boolean> dayBools = Habit.getBooleansFromDayCode(habitCurrent.getDaysCode());
                   switch(day){
                       case Calendar.SUNDAY:
                           dayDue = dayBools.get(0);
                           break;
                       case Calendar.MONDAY:
                           dayDue = dayBools.get(1);
                           break;
                       case Calendar.TUESDAY:
                           dayDue = dayBools.get(2);
                           break;
                       case Calendar.WEDNESDAY:
                           dayDue = dayBools.get(3);
                           break;
                       case Calendar.THURSDAY:
                           dayDue = dayBools.get(4);
                           break;
                       case Calendar.FRIDAY:
                           dayDue = dayBools.get(5);
                           break;
                       case Calendar.SATURDAY:
                           dayDue = dayBools.get(6);
                           break;
                       default:
                           dayDue = dayBools.get(0);
                           break;

                   }
                   return dayDue;

               }else{
                   //flexible
                   //was the activity finished specified number of times this interval?
                   //if n times per week
                   //show week info
                   //show number of times done

                   //if one per n weeks
                   //show week info
                   //show number/ n weeks


                   //if per n months
                   //show week info
                   //show number /n months


                   //if per year
                   //show week info /show number

                   return false;
               }
           }else{
               return false;
           }
        }else{
            return true;

        }


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void initUi(){
        singleMonthCalendar = (CardView) findViewById(R.id.singleMonthCalendar);
        if(habitCurrent.getDuration()==null){
            Intent intent = new Intent(getApplicationContext(), UpdateHabitActivity.class);
            intent.putExtra(DBHelper.HABIT_TITLE,  habitCurrent.getHabitText());
            intent.putExtra(DBHelper.COLUMN_ID, habitCurrent.getId());
            startActivity(intent);
        }else {
            final LayoutInflater layoutInflater = (LayoutInflater)
                    getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            TextView textView = (TextView) findViewById(R.id.info_habit_heading);
            dueTodayCard = (CardView) findViewById(R.id.dueTodayCard);
            ViewFullHabitStats = (TextView) findViewById(R.id.goToMonthView);

            textView.setText(habitCurrent.getHabitText());
            weekHabitItemsTableRow = new TableRow(this);
            weekTableLayout = (TableLayout) findViewById(R.id.weekTableLayout);
            weekTableLayout.setStretchAllColumns(true);
            weekHabitItemsTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            updateHabitButton = (ImageView) findViewById(R.id.updateHabitButton);
            updateHabitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UpdateHabitActivity.class);
                    intent.putExtra(DBHelper.HABIT_TITLE, habitCurrent.getHabitText());
                    intent.putExtra(DBHelper.COLUMN_ID, habitCurrent.getId());
                    startActivity(intent);
                }
            });
            if (getIsHabitDueToday()) {
                dueTodayCard.setVisibility(View.VISIBLE);
            } else {
                dueTodayCard.setVisibility(View.GONE);
            }

            //Create calendar
            currMonth = Calendar.getInstance();
            Calendar c = (Calendar) currMonth.clone();
            // Set the calendar to sunday of the current week
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            // Print dates of the current week starting on Sunday
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.add(Calendar.DAY_OF_WEEK,
                    c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK));
            for (int i = 0; i < 7; i++) {
                c = (Calendar) c.clone();
                HabitButton habitButton = new HabitButton(this, c, habitCurrent);
                weekHabitItemsTableRow.addView(habitButton, weekHabitItemsTableRow.getChildCount());
                c.add(Calendar.DATE, 1);
            }
            weekTableLayout.addView(weekHabitItemsTableRow);
            weekTableLayout.requestLayout();
            weekTableLayout.setVisibility(View.GONE);
            v0 = (MonthLayout) getLayoutInflater().inflate(R.layout.month_table_layout,singleMonthCalendar ,false);
            v0.initializeWith(getApplicationContext(),this.currMonth,habitCurrent);
            singleMonthCalendar.addView(v0);

        }

    }


    private void setContent(int index) {
        final MonthLayout monthLayout =  monthLayouts.get(index);
        Calendar month =(Calendar) currMonth.clone();
        month.add(Calendar.MONTH,index);
        new SetMonthLayoutMonth(monthLayout,month).execute("");
    }






    private class GetHabit extends AsyncTask<String, Void, Habit> {

        private int id;

        public GetHabit(int id) {
            this.id = id;
        }

        @Override
        protected Habit doInBackground(String... params) {

            habitCurrent = Habit.getHabitById(id, getApplicationContext());
            habitItemList.addAll(HabitItem.getAllHabitItemsInHabit(getApplicationContext(), habitCurrent));

            return habitCurrent;
        }

        @Override
        protected void onPostExecute(final Habit habit) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (habitCurrent != null) {
                     initUi();
                    } else {
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class SetMonthLayoutMonth extends AsyncTask<String, Void, Void> {

        private int id;
        private MonthLayout monthLayout;
        private Calendar calendar;

        public SetMonthLayoutMonth(MonthLayout monthLayout, Calendar calendar) {
            this.monthLayout = monthLayout;
            this.calendar = (Calendar) calendar.clone();
        }

        @Override
        protected Void doInBackground(String... params) {

            monthLayout.setMonth(calendar);
            return null;

        }

        @Override
        protected void onPostExecute(Void v){

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }









}

