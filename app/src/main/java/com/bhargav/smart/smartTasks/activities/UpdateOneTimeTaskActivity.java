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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.models.OneTimeTask;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.database.OneTimeTaskDBHelper;
import com.bhargav.smart.smartTasks.database.ReminderDBHelper;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.helpers.DueDateDialog;
import com.bhargav.smart.smartTasks.helpers.OneTimeTaskReminderHelper;
import com.bhargav.smart.smartTasks.models.Reminder;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UpdateOneTimeTaskActivity extends ActionBarActivity implements View.OnClickListener {
    private OneTimeTaskDBHelper oneTimeTaskDBHelper;
    private ReminderDBHelper reminderDBHelper;
    private EditText noteText, noteDescription;
    private Spinner tag;
    private Switch isArchived, isReminded;
    private View add_reminder;
    private final List<String> dates = new ArrayList<String>();
    private Reminder currentReminder;
;
    private final List<String> times = new ArrayList<String>();
    private Long hadMilliSeconds = null, hasMIlliSeconds = null;
    private AdapterView.OnItemSelectedListener dateItemSelectedListener;
    private AdapterView.OnItemSelectedListener timeItemSelectedListener;
    private FloatingActionsMenu fam;
    private FloatingActionButton addDueDate;
    private OneTimeTask oneTimeTaskCurrent;
    private Boolean hadReminder, hasReminder;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    public static final String ACTIVITY_TAG = "UpdateOneTimeTaskActivity";
    private DueDateDialog mAlertBuilder;
    private AlertDialog mAlert;
    private List<TaskList> taskLists;
    private ArrayAdapter<TaskList> adapter;
    private TextView showDueDateTextView,stateTextView;
    public OneTimeTaskReminderHelper oneTimeTaskReminderHelper;
    private FlowLayout labelLayout;
    int day, month, year, hour, minute, second;

    Integer noteId;
    Integer requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        if (oneTimeTaskCurrent != null) {

                            if (noteText.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();

                            } else {
                                updateNote();
                                Intent intent = new Intent(UpdateOneTimeTaskActivity.this,TaskListActivity.class);
                                if(oneTimeTaskCurrent.getTag()!=null){
                                    intent.putExtra(DBHelper.COLUMN_ID, oneTimeTaskCurrent.getTag());

                                }
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_SHORT).show();

                        }

                        finish();
                    }
                }
        );
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Cancel"
                        finish();
                    }
                }
        );
        setContentView(R.layout.activity_add_note);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        new GetNote(extras.getInt(DBHelper.COLUMN_ID)).execute("");
        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE
        );
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );


    }



    private void initUi() {
        noteText = (EditText) findViewById(R.id.noteTitle);
        noteDescription =(EditText) findViewById(R.id.noteDescription);
        oneTimeTaskDBHelper = new OneTimeTaskDBHelper(this);
        isArchived = (Switch) findViewById(R.id.isArchivedSwitch);
        isReminded = (Switch) findViewById(R.id.hasReminderSwitch);
        tag = (Spinner) findViewById(R.id.categorySelectSpinner);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        showDueDateTextView = (TextView) findViewById(R.id.showDueDate);
        final Calendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = 9;
        minute = 0;
        second = 0;
        dates.add("Today");
        dates.add("Tomorrow");
        dates.add("Pick Date");
        times.add("Early Morning 5:00 AM");
        times.add("Morning 8:00 AM");
        times.add("Afternoon 12:00 PM");
        times.add("Evening 6:00 PM");
        times.add("Pick Time");
        new GetTags(this).execute("");


    }


    private void updateUi() {
        getActionBar().setTitle(utilFunctions.toCamelCase(oneTimeTaskCurrent.getNoteTitle()));
        noteId = oneTimeTaskCurrent.getId();
        noteText.setText(oneTimeTaskCurrent.getNoteTitle());
        noteDescription.setText(oneTimeTaskCurrent.getNoteDescription());
        isArchived.setChecked(oneTimeTaskCurrent.getIsArchived());
        isReminded.setChecked(oneTimeTaskCurrent.getIsReminded());
        showDueDateTextView.setText(OneTimeTask.getMetaText(oneTimeTaskCurrent));
        if (oneTimeTaskCurrent.getRemainderId() != null) {
            hadReminder = true;
            requestId = oneTimeTaskCurrent.getRemainderId();
        } else {
            hadReminder = false;
        }
        hasReminder = hadReminder;
        new GetReminder(requestId).execute("");
   }




    private void setFloatingActionListeners(){
//        mAlertBuilder=new DueDateDialog(UpdateNoteActivity.this,noteCurrent.getDueTime(),showDueDateTextView,year,month,day,hour,minute,hasReminder);
//        mAlert = mAlertBuilder.create();
        oneTimeTaskReminderHelper = new OneTimeTaskReminderHelper(this,UpdateOneTimeTaskActivity.this,requestId, oneTimeTaskCurrent.getDueTime(),showDueDateTextView, oneTimeTaskCurrent);
        showDueDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTimeTaskReminderHelper.show();
            }
        });


    }




    private void updateNote() {
        String noteTitleString = noteText.getText().toString();
        String noteDescriptionString = noteDescription.getText().toString();
        TaskList noteTaskList = (TaskList) tag.getSelectedItem();
        oneTimeTaskCurrent.setNoteTitle(noteTitleString);
        oneTimeTaskCurrent.setNoteDescription(noteDescriptionString);
        oneTimeTaskCurrent.setIsArchived(isArchived.isChecked());
        oneTimeTaskCurrent.setIsReminded(isReminded.isChecked());
        oneTimeTaskCurrent.setTag(noteTaskList.getTagId());
         new SaveNote(getApplicationContext()).execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_actvitiy_refactor, menu);
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
    protected void onPause() {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void onClick(View v) {

    }


    private class GetNote extends AsyncTask<String, Void, OneTimeTask> {

        private int id;

        public GetNote(int id) {
            this.id = id;
        }

        @Override
        protected OneTimeTask doInBackground(String... params) {

            oneTimeTaskCurrent = OneTimeTask.getNote(id, getApplicationContext());
            return oneTimeTaskCurrent;
        }

        @Override
        protected void onPostExecute(final OneTimeTask oneTimeTask) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (oneTimeTaskCurrent != null) {
                        initUi();
                        updateUi();
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


    private class GetTags extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetTags(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            taskLists = TaskList.getAllUnArchivedTags(context);
            taskLists.add(new TaskList("No Tag"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    taskLists = TaskList.getAllTags(new ArrayList<TaskList>(), context);
                    adapter = new ArrayAdapter<TaskList>(context,
                            android.R.layout.simple_spinner_item, taskLists);
                    tag.setAdapter(adapter);
                    Integer tagId;
                    if (oneTimeTaskCurrent.getTag() != null) {
                        tagId = oneTimeTaskCurrent.getTag();
                        for (TaskList taskListD : taskLists) {
                            if (taskListD.getTagId() == tagId) {
                                Integer pos = taskLists.indexOf(taskListD);
                                tag.setSelection(pos);
                                break;
                            }
                        }
                    } else {
                        tag.setSelection(taskLists.size() - 1);
                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    private class SaveNote extends AsyncTask<String, Void,Void> {

        private Context context;

        public SaveNote(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            DueDateDialog.Holder holder = oneTimeTaskReminderHelper.doSomethingAboutTheReminder(oneTimeTaskCurrent.getIsReminded());
            requestId = holder.remainderId;
            oneTimeTaskCurrent.setDueTime(holder.cal);
            oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
            oneTimeTaskDBHelper.open();
            oneTimeTaskCurrent.setRemainderId(requestId);
            oneTimeTaskDBHelper.updateNote(oneTimeTaskCurrent);
            oneTimeTaskDBHelper.close();
            return null;

        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



    @Override
    public void onBackPressed() {
        if(oneTimeTaskCurrent !=null){
            Intent intent = new Intent(this,TaskListActivity.class);
            intent.putExtra(DBHelper.COLUMN_ID, oneTimeTaskCurrent.getTag());
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,SuperMain.class);
            startActivity(intent);
        }
        return;
    }

}
