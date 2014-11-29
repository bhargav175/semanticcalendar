package com.bhargav.smart.smartTasks.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.models.TaskList;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.database.RepeatingTaskDBHelper;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.helpers.DueHabitDialog;
import com.bhargav.smart.smartTasks.helpers.RepeatingTaskReminderHelper;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.Reminder;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UpdateRepeatingTaskActivity extends ActionBarActivity {
    private RepeatingTaskDBHelper repeatingTaskDBHelper;
    private EditText habitText, habitQuestion, habitDuration, habitFrequency;
    private Spinner tag;
    private Switch isArchived,isReminded;
    private RepeatingTask repeatingTaskCurrent;
    Integer habitId,currentHabitDuration,currentHabitFrequency;
    private TextView showDueDateTextView;
    public RepeatingTaskReminderHelper reminderHelper;
    private FlowLayout labelLayout;
    int day, month, year, hour, minute, second;
    private FloatingActionsMenu fam;
    private DueHabitDialog mAlertBuilder;
    private AlertDialog mAlert;
    private Reminder currentReminder;
    Integer requestId;
    private Boolean hadReminder,hasReminder;
    private Long hadMilliSeconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        if(repeatingTaskCurrent !=null){
                            if (habitText.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();

                            } else {
                                updateHabit();

                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_SHORT).show();

                        }

                        finish();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Cancel"
                        finish();
                    }
                });
        // Show the custom action bar view and hide the normal Home icon and title.
        setContentView(R.layout.activity_add_habit);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        // END_INCLUDE (inflate_set_custom_view)
        Intent intent = getIntent();

        Bundle extras= intent.getExtras();
        if(extras!=null){
            habitId = extras.getInt(DBHelper.COLUMN_ID);
            new GetHabit(habitId).execute("");

        }else{
            Toast.makeText(this, "Could not load habit", Toast.LENGTH_SHORT).show();
        }


    }

    private void initUi() {

        //4 edittexts
        habitText = (EditText) findViewById(R.id.repeatingTaskText);
        habitQuestion = (EditText) findViewById(R.id.habitQuestion);

        //2 spinners
        tag = (Spinner) findViewById(R.id.categorySelectSpinner);
        isArchived = (Switch) findViewById(R.id.isArchivedSwitch);
        isReminded = (Switch) findViewById(R.id.hasReminderSwitch);
        repeatingTaskDBHelper = new RepeatingTaskDBHelper(this);
        repeatingTaskDBHelper.open();
        showDueDateTextView = (TextView) findViewById(R.id.showDueDate);
        //Duration
        List<String> durationStrings = new ArrayList<String>();
        durationStrings.addAll(Arrays.asList(RepeatingTask.durationStrings));
 //habit Type
        new GetTags().execute("");
        updateUi();

    }


    private void setFloatingActionListeners(){
//        mAlertBuilder=new DueDateDialog(UpdateNoteActivity.this,noteCurrent.getDueTime(),showDueDateTextView,year,month,day,hour,minute,hasReminder);
//        mAlert = mAlertBuilder.create();
        reminderHelper = new RepeatingTaskReminderHelper(this,UpdateRepeatingTaskActivity.this,requestId, repeatingTaskCurrent.getDueTime(),showDueDateTextView, repeatingTaskCurrent,1);

        showDueDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderHelper.show();
            }
        });
    }

    private void updateUi(){
        //addFloatingActionButtons();

        getActionBar().setTitle(utilFunctions.toCamelCase(repeatingTaskCurrent.getRepeatingTaskText()));
        showDueDateTextView.setText(RepeatingTask.getMetaText(repeatingTaskCurrent));
        habitText.setText(repeatingTaskCurrent.getRepeatingTaskText());
        habitQuestion.setText(repeatingTaskCurrent.getRepeatingTaskDescription());

        if(repeatingTaskCurrent.getIsArchived()!=null){
            isArchived.setChecked(repeatingTaskCurrent.getIsArchived());
        }

        if (repeatingTaskCurrent.getReminderId() != null) {
            hadReminder = true;
            requestId = repeatingTaskCurrent.getReminderId() ;
        } else {
            hadReminder = false;
        }
        hasReminder = hadReminder;
        new GetReminder(requestId).execute("");

    }


    private void updateHabit() {

        new UpdateHabit(this).execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_actvitiy_refactor, menu);
        return true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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

    private class UpdateHabit extends AsyncTask<String, Void, Integer> {

        private Context context;

        public UpdateHabit(Context context){
            this.context=context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer res = 0;
            DueHabitDialog.Holder holder = reminderHelper.doSomething(isReminded.isChecked());
            repeatingTaskCurrent.setRepeatingTaskType(holder.curHabitType);
            repeatingTaskCurrent.setDueTime(holder.cal);
            currentHabitFrequency = holder.curFrequency;
            Integer daysCode = holder.daysCode;
            String habitTextString=habitText.getText().toString();
            String habitQuestionString=habitQuestion.getText().toString();
            repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
            repeatingTaskDBHelper.open();
            TaskList habitTaskList = (TaskList) tag.getSelectedItem();
            repeatingTaskCurrent.setRepeatingTaskText(habitTextString);
            repeatingTaskCurrent.setRepeatingTaskDescription(habitQuestionString);
            repeatingTaskCurrent.setReminderId(holder.remainderId);
            repeatingTaskCurrent.setTag(habitTaskList.getTagId());
            repeatingTaskCurrent.setStartDate(holder.startD);
            repeatingTaskCurrent.setEndDate(holder.endD);
            repeatingTaskCurrent.setIsArchived(isArchived.isChecked());
            if(repeatingTaskCurrent.getRepeatingTaskType()== RepeatingTask.Type.FIXED){
                repeatingTaskCurrent.setFrequency(null);
                repeatingTaskCurrent.setDaysCode(daysCode);
            }else{
                repeatingTaskCurrent.setDaysCode(null);
                repeatingTaskCurrent.setFrequency(currentHabitFrequency);
            }

            if(repeatingTaskCurrent !=null){
                if(habitTextString.length()==0){

                    res = -1;
                }
                else{
                    repeatingTaskDBHelper.updateRepeatingTask(repeatingTaskCurrent);
                    repeatingTaskDBHelper.close();
                    res = 1;
                }
            }
            else{
                res = 0;
                repeatingTaskDBHelper.close();
            }
            return res;

        }

        @Override
        protected void onPostExecute(final Integer res) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(res == 1){
                        Toast.makeText(context, repeatingTaskCurrent.getRepeatingTaskText() + " saved",Toast.LENGTH_SHORT).show();
                        if(repeatingTaskCurrent.getTag()!=null){

                            Intent intent = new Intent(UpdateRepeatingTaskActivity.this,TaskListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(DBHelper.COLUMN_ID, repeatingTaskCurrent.getTag());
                            startActivity(intent);

                        }
                    }
                    if(res == -1){
                        Toast.makeText(context,"Title cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                    if(res==0){
                        Toast.makeText(context,"Update Failed",Toast.LENGTH_SHORT).show();
                    }



                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



    private class GetHabit extends AsyncTask<String, Void, RepeatingTask> {

        private int id;

        public GetHabit(int id) {
            this.id = id;
        }

        @Override
        protected RepeatingTask doInBackground(String... params) {

            repeatingTaskCurrent = RepeatingTask.getRepeatingTaskById(id, getApplicationContext());
            return repeatingTaskCurrent;
        }

        @Override
        protected void onPostExecute(final RepeatingTask repeatingTask) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (repeatingTaskCurrent != null) {
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


    private class GetReminder extends AsyncTask<String, Void, Reminder> {

        private Integer id;

        public GetReminder(Integer id) {
            this.id = id;
        }

        @Override
        protected Reminder doInBackground(String... params) {
            if (id != null) {
                currentReminder = Reminder.getReminderById(id, getApplicationContext());
            } else {
                currentReminder = null;
            }
            return currentReminder;
        }

        @Override
        protected void onPostExecute(final Reminder reminder) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (currentReminder != null) {
                        year = reminder.getYear();
                        month = reminder.getMonthOfYear();
                        day = reminder.getDayOfMonth();
                        hour = reminder.getHourOfDay();
                        minute = reminder.getMinuteOfHour();
                        second = reminder.getSecond();
                        Calendar calendar = new GregorianCalendar();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        hadMilliSeconds = calendar.getTimeInMillis();

                    } else {

                    }
                    setFloatingActionListeners();

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


private class GetTags extends AsyncTask<String, Void, List<TaskList>> {



    public GetTags() {

    }

    @Override
    protected List<TaskList> doInBackground(String... params) {
        List<TaskList> taskLists = TaskList.getAllTags(new ArrayList<TaskList>(), getApplicationContext());
        return taskLists;

    }

    @Override
    protected void onPostExecute(final List<TaskList> taskLists) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<TaskList> adapter = new ArrayAdapter<TaskList>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, taskLists);
                tag.setAdapter(adapter);

                if(repeatingTaskCurrent.getTag()!=null){
                    for(TaskList taskListD : taskLists){
                        if(taskListD.getTagId() == repeatingTaskCurrent.getTag()){
                            Integer pos = taskLists.indexOf(taskListD);
                            tag.setSelection(pos);
                            break;
                        }
                    }
                }else{
                    tag.setSelection(taskLists.size()-1);
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


    @Override
    public void onBackPressed() {
        if(repeatingTaskCurrent !=null){
            Intent intent = new Intent(this,TaskListActivity.class);
            intent.putExtra(DBHelper.COLUMN_ID, repeatingTaskCurrent.getTag());
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,SuperMain.class);
            startActivity(intent);
        }

        return;
    }
}
