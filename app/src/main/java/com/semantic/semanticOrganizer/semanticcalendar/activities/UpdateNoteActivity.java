package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.ReminderDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.MySpinner;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.Reminder;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;
import com.semantic.semanticOrganizer.semanticcalendar.utils.MyBroadcastReceiver;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class UpdateNoteActivity extends FragmentActivity implements View.OnClickListener {
    private NoteDBHelper noteDBHelper;
    private ReminderDBHelper reminderDBHelper;
    private EditText noteText;
    private Spinner tag;
    private MySpinner dateSpinner, timeSpinner;
    private CheckBox isArchived;

    private Button addReminderButton;
    private LinearLayout remainderContainer;
    private Boolean hasReminder, remainderDirty;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";


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
                        if (noteId != null) {

                            updateNote(noteId);

                            Intent lIntent = new Intent(getApplicationContext(), LandingActivity.class);
                            startActivity(lIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_LONG).show();

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
        initUi();
        setListeners();
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
        // END_INCLUDE (inflate_set_custom_view)



        updateUi();

    }


    private void updateUi() {
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String noteTextString = extras.getString(DBHelper.NOTE_DESCRIPTION);
            noteId = extras.getInt(DBHelper.COLUMN_ID);
            noteText.setText(noteTextString);

            isArchived.setChecked(extras.getBoolean(DBHelper.NOTE_IS_ARCHIVED));
            Integer temp = null;
            if(extras.get(DBHelper.NOTE_REQUEST_ID)!=null){
              temp = extras.getInt(DBHelper.NOTE_REQUEST_ID);
            }

            if(temp !=null ){
                hasReminder = true;
                requestId = extras.getInt(DBHelper.NOTE_REQUEST_ID);
            }else{
                hasReminder = false;

            }
            ;

            List<Tag> tags = Tag.getAllTags(new ArrayList<Tag>(), getApplicationContext());
            tags.add(new Tag("No Tag"));

            ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                    android.R.layout.simple_spinner_item, tags);
            tag.setAdapter(adapter);
            Integer tagId = extras.getInt(DBHelper.NOTE_TAG);
            for(Tag tagD : tags){
                if(tagD.getTagId() == tagId){
                    tag.setSelection(tags.indexOf(tag));
                }
            }
            if(tag.isSelected()){
                tag.setSelection(tags.size());
            }

        } else {
            Toast.makeText(this, "Could not load note", Toast.LENGTH_LONG).show();
        }

        remainderDirty = false;
    }


    private void initUi() {
    noteText = (EditText) findViewById(R.id.noteText);
        addReminderButton = (Button) findViewById(R.id.addReminder);
        remainderContainer = (LinearLayout) findViewById(R.id.remainderContainer);
        noteDBHelper = new NoteDBHelper(this);
        reminderDBHelper = new ReminderDBHelper(this);
        noteDBHelper.open();
        reminderDBHelper.open();
        isArchived = (CheckBox) findViewById(R.id.isArchived);
        tag = (Spinner) findViewById(R.id.selectSpinner);

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = 9;
        minute = 0;
        second = 0;

    }


    private void setListeners() {
        addReminderButton.setOnClickListener(new View.OnClickListener() {


            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

                }
            }, year, month, day, false);
            final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                }
            }, hour, minute, false, false);


            @Override
            public void onClick(View view) {
                hasReminder = true;
                addReminderButton.setVisibility(View.GONE);
                final View add_reminder = getLayoutInflater().inflate(R.layout.add_reminder_non_repeating, remainderContainer, false);
                remainderContainer.addView(add_reminder);
                Button close = (Button) add_reminder.findViewById(R.id.close);
                dateSpinner = (MySpinner) add_reminder.findViewById(R.id.date);
                timeSpinner = (MySpinner) add_reminder.findViewById(R.id.time);
                final List<String> dates = new ArrayList<String>();
                dates.add("Today");
                dates.add("Yesterday");
                dates.add("Pick Date");
                final List<String> times = new ArrayList<String>();
                times.add("Early Morning 5:00 AM");
                times.add("Morning 8:00 AM");
                times.add("Afternoon 12:00 PM");
                times.add("Evening 6:00 PM");
                times.add("Pick Time");

                final ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, dates) {
                };
                final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, times) {
                };
                dateSpinner.setAdapter(dateAdapter);
                timeSpinner.setAdapter(timeAdapter);
               dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        remainderDirty = true;
                        if (dates.get(i).equals("Pick Date")) {

                            TextView dv = (TextView) dateSpinner.findViewById(android.R.id.text1);
                            dv.setText(day + "-" + month + "-" + year);


                            datePickerDialog.setYearRange(1985, 2028);
                            datePickerDialog.setCloseOnSingleTapDay(false);
                            datePickerDialog.show(UpdateNoteActivity.this.getSupportFragmentManager(), DATEPICKER_TAG);
                            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {
                                    TextView dv = (TextView) dateSpinner.findViewById(android.R.id.text1);
                                    year = y;
                                    month = m;
                                    day = d;
                                    dv.setText(day + "-" + month + "-" + year);
                                }
                            });
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        remainderDirty = true;

                        if (times.get(i).equals("Pick Time")) {
                            TextView tv = (TextView) timeSpinner.findViewById(android.R.id.text1);
                            tv.setText(hour + "::" + minute);

                            timePickerDialog.setCloseOnSingleTapMinute(false);
                            timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                            timePickerDialog.initialize(new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int h, int m) {
                                    TextView tv = (TextView) timeSpinner.findViewById(android.R.id.text1);
                                    hour = h;
                                    minute = m;
                                    tv.setText(hour + "::" + minute);
                                }
                            },hour,minute,false,false);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hasReminder = false;
                        remainderContainer.removeView(add_reminder);
                        addReminderButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


    }

    private void updateReminder(Integer requestId, int year , int month, int day, int hour,int minute){
            Intent intent = new Intent(this, MyBroadcastReceiver.class);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager!= null) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Reminder reminder = new Reminder(year,month,day,hour,minute,second,0,0,false);
                    reminderDBHelper.saveReminder(reminder);
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.set(year,month,day,hour,minute,0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Toast.makeText(this, "Alarm created " + calendar.getTime().toString() + " seconds",
                            Toast.LENGTH_SHORT).show();

            }
    }
private void deleteReminder(int requestId){
    Intent intent = new Intent(this, MyBroadcastReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestId, intent, PendingIntent.FLAG_NO_CREATE);
    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    if (alarmManager!= null) {
        if(pendingIntent!=null){
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Alarm cancelled ",
                    Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Alarm does not exist ",
                    Toast.LENGTH_SHORT).show();

        }


    }
    }


    private void updateNote(Integer noteId) {
        String noteTextString = noteText.getText().toString();
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        Note note = noteDBHelper.getNote(noteId);
        Tag noteTag = (Tag) tag.getSelectedItem();
        if(requestId!=null){
            Toast.makeText(this, "Note already has reminder", Toast.LENGTH_LONG).show();


        }else{
            requestId = Reminder.getNextReminderId(this);
            Toast.makeText(this, "Note has no reminder", Toast.LENGTH_LONG).show();

        }
        Toast.makeText(this, requestId.toString() + hasReminder.toString() + "-"+year+ "-"+month+ "-"+day+ "-"+hour+ "-"+minute, Toast.LENGTH_SHORT).show();
//        if(remainderDirty){
//            updateReminder(requestId, year, month , day, hour ,second);
//        }
//        if(!hasReminder && requestId !=null){
//            deleteReminder(requestId);
//        }
        if(note.hasReminder() && !remainderDirty){
            //Do nothing
        }
        if(note.hasReminder() && remainderDirty){
            updateReminder(requestId, year, month , day, hour ,second);
        }
        if(!note.hasReminder() && !remainderDirty){
            //Do nothing
        }
        if(!note.hasReminder() && remainderDirty){
            deleteReminder(requestId);
        }


        if (note != null) {
            if (noteTextString.length() == 0) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_LONG).show();
            } else {
                noteDBHelper.updateNote(note, noteTextString,isArchived.isChecked(), noteTag.getTagId(),requestId);
                noteDBHelper.close();
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }

        } else {
            noteDBHelper.close();
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();


        }


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
        noteDBHelper.close();
        reminderDBHelper.close();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void onClick(View v) {

    }


}
