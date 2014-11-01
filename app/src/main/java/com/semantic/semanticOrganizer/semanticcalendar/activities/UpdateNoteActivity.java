package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import android.widget.RelativeLayout;
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
    private EditText noteText;
    private Spinner tag;
    private MySpinner dateSpinner, timeSpinner;
    private CheckBox isArchived;
    private View add_reminder;
    private final List<String> dates = new ArrayList<String>();
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private final List<String> times = new ArrayList<String>();
    private Long hadMilliSeconds = null, hasMIlliSeconds = null;
    private AdapterView.OnItemSelectedListener dateItemSelectedListener;
    private AdapterView.OnItemSelectedListener timeItemSelectedListener;


    private Note noteCurrent;
    private Button addReminderButton, close;
    private RelativeLayout remainderContainer;
    private Boolean hadReminder, hasReminder;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    public static final String ACTIVITY_TAG = "UpdateNoteActivity";


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
                                updateNote(noteId);
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


    private void updateUi(Note note) {

        if (note != null) {

            noteId = note.getId();
            noteText.setText(note.getNoteTitle());

            isArchived.setChecked(note.getIsArchived());

            //Get Reminder


            if (note.getRemainderId() != null) {
                hadReminder = true;
                requestId = note.getRemainderId();
            } else {
                hadReminder = false;
            }
            ;
            hasReminder = hadReminder;

            //Update Note TAg

            List<Tag> tags = Tag.getAllTags(new ArrayList<Tag>(), getApplicationContext());
            tags.add(new Tag("No Tag"));

            ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                    android.R.layout.simple_spinner_item, tags);
            tag.setAdapter(adapter);

            Integer tagId;
            if (note.getTag() != null) {
                tagId = note.getTag();
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


        } else {
            Toast.makeText(this, "Could not load note", Toast.LENGTH_SHORT).show();
        }


    }


    private void initUi() {
        noteText = (EditText) findViewById(R.id.noteTitle );
        addReminderButton = (Button) findViewById(R.id.addReminder);
        remainderContainer = (RelativeLayout) findViewById(R.id.remainderContainer);
        noteDBHelper = new NoteDBHelper(this);
        reminderDBHelper = new ReminderDBHelper(this);
        noteDBHelper.open();
        reminderDBHelper.open();
        isArchived = (CheckBox) findViewById(R.id.isArchived);
        tag = (Spinner) findViewById(R.id.selectSpinner);

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


        add_reminder = getLayoutInflater().inflate(R.layout.add_reminder_non_repeating, remainderContainer, false);
        remainderContainer.addView(add_reminder);
        close = (Button) add_reminder.findViewById(R.id.close);
        dateSpinner = (MySpinner) add_reminder.findViewById(R.id.date);
        timeSpinner = (MySpinner) add_reminder.findViewById(R.id.time);

        hideReminder();
        configureDialogs();
        configureSpinners();


    }


    private void setListeners() {


        if (hadReminder) {
            Log.d(ACTIVITY_TAG,requestId.toString());
            Reminder reminder = Reminder.getReminderById(requestId, this);
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

            yesReminderUI();
            showReminder();

            dateSpinner.post(new Runnable() {
                @Override
                public void run() {
                    TextView dv = (TextView) dateSpinner.findViewById(android.R.id.text1);
                    dv.setText(day + "-" + month + "-" + year);
                }
            });
            timeSpinner.post(new Runnable() {
                @Override
                public void run() {
                    TextView tv = (TextView) timeSpinner.findViewById(android.R.id.text1);
                    tv.setText(hour + "::" + minute);

                }
            });


        } else {
            noReminderUI();
        }


    }


    private void configureSpinners() {

        final ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dates) {
        };
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, times) {
        };
        dateSpinner.setAdapter(dateAdapter);
        timeSpinner.setAdapter(timeAdapter);


        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                }else if(dates.get(i).equals("Today")){
                    Calendar calendar = new GregorianCalendar();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day= calendar.get(Calendar.DAY_OF_MONTH);
                }else if(dates.get(i).equals("Tomorrow")){
                    Calendar calendar = new GregorianCalendar();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day= calendar.get(Calendar.DAY_OF_MONTH) + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                    }, hour, minute, false, false);

                }else if(times.get(i).equals("Early Morning 5:00 AM")){
                    hour = 5;
                    minute = 0;

                }else if(times.get(i).equals("Morning 8:00 AM")){
                    hour = 8;
                    minute = 0;
                }
                else if(times.get(i).equals("Afternoon 12:00 PM")){
                    hour = 12;
                    minute = 0;
                }
                 else if(times.get(i).equals("Evening 6:00 PM")){
                    hour = 18;
                    minute = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void configureDialogs() {

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
    }

    private void noReminderUI() {


        addReminderButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                hasReminder = true;
                showReminder();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hasReminder = false;
                        hideReminder();
                    }
                });
            }
        });
    }


    private void yesReminderUI() {

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasReminder = true;
                showReminder();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hasReminder = false;
                        hideReminder();
                    }
                });
            }
        });
    }

    private void showReminder() {
        addReminderButton.setVisibility(View.GONE);
        add_reminder.setVisibility(View.VISIBLE);

    }

    private void hideReminder() {
        add_reminder.setVisibility(View.GONE);
        addReminderButton.setVisibility(View.VISIBLE);

    }

    private void updateReminder(Integer requestId, int year, int month, int day, int hour, int minute) {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Reminder reminder = new Reminder(year, month, day, hour, minute, second, 0, 0, false);
            reminder.setId(requestId);
            reminderDBHelper.updateReminder(reminder);
            Calendar calendar = new GregorianCalendar();
            calendar.set(year, month, day, hour, minute, 0);
            Date currentLocalTime = calendar.getTime();
            DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
            date.setTimeZone(TimeZone.getTimeZone("GMT"));
            String localTime = date.format(currentLocalTime);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm created " + localTime + " seconds",
                    Toast.LENGTH_SHORT).show();

        }
    }

    private void saveReminder(Integer requestId, int year, int month, int day, int hour, int minute) {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Reminder reminder = new Reminder(year, month, day, hour, minute, second, 0, 0, false);
            reminder.setId(requestId);

            reminderDBHelper.saveReminder(reminder);
            Calendar calendar = new GregorianCalendar();
            calendar.set(year, month, day, hour, minute, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm created " + calendar.getTime().toString() + " seconds",
                    Toast.LENGTH_SHORT).show();

        }
    }


    private void deleteReminder(int requestId) {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestId, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Alarm cancelled ",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Alarm does not exist ",
                        Toast.LENGTH_SHORT).show();

            }


        }
    }


    private void updateNote(Integer noteId) {
        String noteTextString = noteText.getText().toString();
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        Tag noteTag = (Tag) tag.getSelectedItem();
        if(hasReminder){
            if (requestId != null) {
                Toast.makeText(this, "Note already has reminder", Toast.LENGTH_SHORT).show();
            } else {
                requestId = Reminder.getNextReminderId(this);
                Toast.makeText(this, "Note has no reminder", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestId!=null){
            Toast.makeText(this, requestId.toString() + hasReminder.toString() + "-" + year + "-" + month + "-" + day + "-" + hour + "-" + minute, Toast.LENGTH_SHORT).show();
        }

        if (hadReminder) {
            if (hasReminder) {
                Calendar calendar = new GregorianCalendar();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                hasMIlliSeconds = calendar.getTimeInMillis();
                if (hasMIlliSeconds == hadMilliSeconds) {
                    //do nothing
                } else {
                    //update
                    updateReminder(requestId, year, month, day, hour, minute);
                }
            } else {
                //delete current reminder
                deleteReminder(requestId);
            }

        } else {
            if (hasReminder) {
                //add reminder
                saveReminder(requestId, year, month, day, hour, second);
            } else {
                //do nothing
            }
        }
        if (noteCurrent != null) {

            noteDBHelper.updateNote(noteCurrent, noteTextString, isArchived.isChecked(), noteTag.getTagId(), requestId);
            noteDBHelper.close();


        } else {

            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
        noteDBHelper.close();


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

                        updateUi(noteCurrent);

                        setListeners();
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


}
