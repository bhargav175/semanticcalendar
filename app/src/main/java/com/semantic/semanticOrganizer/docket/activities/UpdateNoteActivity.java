package com.semantic.semanticOrganizer.docket.activities;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.database.NoteDBHelper;
import com.semantic.semanticOrganizer.docket.database.ReminderDBHelper;
import com.semantic.semanticOrganizer.docket.helpers.AddLabelDialog;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.helpers.DueDateDialog;
import com.semantic.semanticOrganizer.docket.helpers.ReminderHelper;
import com.semantic.semanticOrganizer.docket.models.Note;
import com.semantic.semanticOrganizer.docket.models.Reminder;
import com.semantic.semanticOrganizer.docket.models.Tag;
import com.semantic.semanticOrganizer.docket.utils.MyBroadcastReceiver;
import com.semantic.semanticOrganizer.docket.utils.utilFunctions;

import org.apmem.tools.layouts.FlowLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class UpdateNoteActivity extends FragmentActivity implements View.OnClickListener {
    private NoteDBHelper noteDBHelper;
    private ReminderDBHelper reminderDBHelper;
    private EditText noteText, noteDescription;
    private Spinner tag;
    private CheckBox isArchived;
    private View add_reminder;
    private final List<String> dates = new ArrayList<String>();
    private Reminder currentReminder;
;
    private final List<String> times = new ArrayList<String>();
    private Long hadMilliSeconds = null, hasMIlliSeconds = null;
    private AdapterView.OnItemSelectedListener dateItemSelectedListener;
    private AdapterView.OnItemSelectedListener timeItemSelectedListener;
    private FloatingActionsMenu fam;
    private FloatingActionButton addDueDate, addLabel;
    private Note noteCurrent;
    private Boolean hadReminder, hasReminder;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    public static final String ACTIVITY_TAG = "UpdateNoteActivity";
    private DueDateDialog mAlertBuilder;
    private AddLabelDialog mAddLabelBuilder;
    private AlertDialog mAlert,mAddLabel;
    private List<Tag> tags;
    private ArrayAdapter<Tag> adapter;
    private TextView showDueDateTextView;
    public ReminderHelper reminderHelper;
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
                        if (noteCurrent != null) {

                            if (noteText.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();

                            } else {
                                updateNote();
                                Intent intent = new Intent(UpdateNoteActivity.this,TagActivity.class);
                                if(noteCurrent.getTag()!=null){
                                    intent.putExtra(DBHelper.COLUMN_ID,noteCurrent.getTag());

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
        noteDBHelper = new NoteDBHelper(this);
        isArchived = (CheckBox) findViewById(R.id.isArchived);
        labelLayout = (FlowLayout) findViewById(R.id.labelsLayout);
        tag = (Spinner) findViewById(R.id.selectSpinner);
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
        addFloatingActionButtons();


    }


    private void updateUi() {
        getActionBar().setTitle(utilFunctions.toCamelCase(noteCurrent.getNoteTitle()));
        noteId = noteCurrent.getId();
        noteText.setText(noteCurrent.getNoteTitle());
        noteDescription.setText(noteCurrent.getNoteDescription());
        isArchived.setChecked(noteCurrent.getIsArchived());
        if (noteCurrent.getRemainderId() != null) {
            hadReminder = true;
            requestId = noteCurrent.getRemainderId();
        } else {
            hadReminder = false;
        }
        hasReminder = hadReminder;
        new GetReminder(requestId).execute("");
   }



    private void addFloatingActionButtons(){
        fam = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        addDueDate = (FloatingActionButton) findViewById(R.id.addDueDate);
        addLabel = (FloatingActionButton) findViewById(R.id.addLabel);

    }
    private void setFloatingActionListeners(){
//        mAlertBuilder=new DueDateDialog(UpdateNoteActivity.this,noteCurrent.getDueTime(),showDueDateTextView,year,month,day,hour,minute,hasReminder);
//        mAlert = mAlertBuilder.create();

        reminderHelper = new ReminderHelper(this,UpdateNoteActivity.this,requestId,noteCurrent.getDueTime(),showDueDateTextView,noteCurrent.getId(),1);

        mAddLabelBuilder=new AddLabelDialog(UpdateNoteActivity.this,1,noteCurrent.getId(),noteCurrent.getTag(),labelLayout);
        mAddLabel = mAddLabelBuilder.create();
        addDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderHelper.show();
            }
        });
        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddLabel.show();
            }
        });
    }




    private void updateNote() {
        String noteTitleString = noteText.getText().toString();
        String noteDescriptionString = noteDescription.getText().toString();
        Tag noteTag = (Tag) tag.getSelectedItem();
        noteCurrent.setNoteTitle(noteTitleString);
        noteCurrent.setNoteDescription(noteDescriptionString);
        noteCurrent.setIsArchived(isArchived.isChecked());
        noteCurrent.setTag(noteTag.getTagId());
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


    private class GetNote extends AsyncTask<String, Void, Note> {

        private int id;

        public GetNote(int id) {
            this.id = id;
        }

        @Override
        protected Note doInBackground(String... params) {

            noteCurrent = Note.getNote(id, getApplicationContext());
            return noteCurrent;
        }

        @Override
        protected void onPostExecute(final Note note) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (noteCurrent != null) {
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
            tags =Tag.getAllUnArchivedTags(context);
            tags.add(new Tag("No Tag"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tags = Tag.getAllTags(new ArrayList<Tag>(), context);
                    tags.add(new Tag("No Tag"));
                    adapter = new ArrayAdapter<Tag>(context,
                            android.R.layout.simple_spinner_item, tags);
                    tag.setAdapter(adapter);
                    Integer tagId;
                    if (noteCurrent.getTag() != null) {
                        tagId = noteCurrent.getTag();
                        for (Tag tagD : tags) {
                            if (tagD.getTagId() == tagId) {
                                Integer pos = tags.indexOf(tagD);
                                tag.setSelection(pos);
                                break;
                            }
                        }
                    } else {
                        tag.setSelection(tags.size() - 1);
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
            mAddLabelBuilder.saveSelectedLabelsWithNote();

            DueDateDialog.Holder holder = reminderHelper.doSomethingAboutTheReminder();
            requestId = holder.remainderId;
            noteCurrent.setDueTime(holder.cal);
            noteDBHelper = new NoteDBHelper(context);
            noteDBHelper.open();
            noteCurrent.setRemainderId(requestId);
            noteDBHelper.updateNote(noteCurrent);
            noteDBHelper.close();
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
        if(noteCurrent!=null){
            Intent intent = new Intent(this,TagActivity.class);
            intent.putExtra(DBHelper.COLUMN_ID,noteCurrent.getTag());
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,SuperMain.class);
            startActivity(intent);
        }
        return;
    }

}
