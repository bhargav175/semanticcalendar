package com.semantic.semanticOrganizer.semanticcalendar.activities;

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

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.adapters.InfinitePagerAdapter;
import com.semantic.semanticOrganizer.semanticcalendar.adapters.WrapContentHeightViewPager;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.HabitButton;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.MonthLayout;
import com.semantic.semanticOrganizer.semanticcalendar.models.Habit;
import com.semantic.semanticOrganizer.semanticcalendar.models.HabitItem;

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
    private CardView dueTodayCard;
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
        if(habitCurrent.getDuration()==null){
            Intent intent = new Intent(getApplicationContext(), UpdateHabitActivity.class);
            intent.putExtra(DBHelper.HABIT_TITLE,  habitCurrent.getHabitText());
            intent.putExtra(DBHelper.COLUMN_ID, habitCurrent.getId());
            startActivity(intent);
        }else{
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
                    intent.putExtra(DBHelper.HABIT_TITLE,  habitCurrent.getHabitText());
                    intent.putExtra(DBHelper.COLUMN_ID, habitCurrent.getId());
                    startActivity(intent);
                }
            });
            if(getIsHabitDueToday()){
                dueTodayCard.setVisibility(View.VISIBLE);
            }else{
                dueTodayCard.setVisibility(View.GONE);
            }

            //Create calendar
            currMonth = Calendar.getInstance();
            Calendar c = (Calendar) currMonth.clone();
            // Set the calendar to sunday of the current week
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            // Print dates of the current week starting on Sunday
            c.set(Calendar.HOUR_OF_DAY,0);
            c.set(Calendar.MINUTE,0);
            c.set(Calendar.SECOND,0);
            c.add(Calendar.DAY_OF_WEEK,
                    c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK));
            for (int i = 0; i < 7; i++) {
                c = (Calendar) c.clone();
                HabitButton habitButton =  new HabitButton(this,c,habitCurrent);
                weekHabitItemsTableRow.addView(habitButton,weekHabitItemsTableRow.getChildCount());
                c.add(Calendar.DATE, 1);
            }
            weekTableLayout.addView(weekHabitItemsTableRow);
            weekTableLayout.requestLayout();
            habitStreakPager = (WrapContentHeightViewPager) findViewById(R.id.habitStreakPager);
            habitStreakAdapter = new InfinitePagerAdapter(habitCurrent,this,monthLayouts);
            weekTableLayout.setVisibility(View.GONE);

            initMonths();
            habitStreakPager.setCurrentItem(PAGE_MIDDLE,false);

            habitStreakPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                }

                @Override
                public void onPageSelected(int position) {
                    mSelectedPageIndex = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {

                        final MonthLayout leftPage = monthLayouts.get(PAGE_LEFT);
                        final MonthLayout middlePage = monthLayouts.get(PAGE_MIDDLE);
                        final MonthLayout rightPage = monthLayouts.get(PAGE_RIGHT);

                        final int oldLeftIndex = leftPage.getIndex();
                        final int oldMiddleIndex = middlePage.getIndex();
                        final int oldRightIndex = rightPage.getIndex();

                        // user swiped to right direction --> left page
                        if (mSelectedPageIndex == PAGE_LEFT) {

                            // moving each page content one page to the right
                            leftPage.setIndex(oldLeftIndex - 1);
                            middlePage.setIndex(oldLeftIndex);
                            rightPage.setIndex(oldMiddleIndex);

                            setContent(PAGE_RIGHT);
                            setContent(PAGE_MIDDLE);
                            setContent(PAGE_LEFT);

                            // user swiped to left direction --> right page
                        } else if (mSelectedPageIndex == PAGE_RIGHT) {

                            leftPage.setIndex(oldMiddleIndex);
                            middlePage.setIndex(oldRightIndex);
                            rightPage.setIndex(oldRightIndex + 1);

                            setContent(PAGE_LEFT);
                            setContent(PAGE_MIDDLE);


                            setContent(PAGE_RIGHT);
                        }
                        habitStreakPager.setCurrentItem(PAGE_MIDDLE, false);
                    }
                }
            });
        }

    }


    private void setContent(int index) {
        final MonthLayout monthLayout =  monthLayouts.get(index);
        Calendar month =(Calendar) currMonth.clone();
        month.add(Calendar.MONTH,index);
        new SetMonthLayoutMonth(monthLayout,month).execute("");
    }





    private void initMonths() {
        Calendar currMonth = Calendar.getInstance() ;
        MonthLayout v0 = (MonthLayout) getLayoutInflater().inflate(R.layout.month_table_layout, null);
        MonthLayout v1 = (MonthLayout) getLayoutInflater().inflate(R.layout.month_table_layout, null);
        MonthLayout v2 = (MonthLayout) getLayoutInflater().inflate(R.layout.month_table_layout, null);

        Calendar nextMonth = (Calendar) currMonth.clone();
        Calendar prevMonth = (Calendar) currMonth.clone();
        nextMonth.add(Calendar.MONTH,1);
        prevMonth.add(Calendar.MONTH,-1);
        v0.initializeWith(this,currMonth,habitCurrent);
        v1.initializeWith(this,nextMonth,habitCurrent);
        v2.initializeWith(this,prevMonth,habitCurrent);
        monthLayouts.add(v2);
        monthLayouts.add(v0);
        monthLayouts.add(v1);
        v2.setIndex(-1);
        v0.setIndex(0);
        v1.setIndex(1);
        habitStreakAdapter.addView(v2, 0);
        habitStreakAdapter.addView(v0, 0);
        habitStreakAdapter.addView(v1, 0);
        habitStreakPager.setAdapter(habitStreakAdapter);
        habitStreakAdapter.notifyDataSetChanged();
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

