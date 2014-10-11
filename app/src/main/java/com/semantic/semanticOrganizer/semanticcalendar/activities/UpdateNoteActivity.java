package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateNoteActivity extends FragmentActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private NoteDBHelper noteDBHelper;
    private EditText noteText;
    private Spinner tag;
    private Spinner dateSpinner, timeSpinner;

    private Button addReminderButton;
    private LinearLayout remainderContainer;
    private Boolean hasRemainder;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    int day, month, year, hour, minute, second;

    Integer noteId;
    Integer requestId;

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
                        if(noteId!=null){

                            updateNote(noteId);
                            Intent lIntent = new Intent(getApplicationContext(), LandingActivity.class);
                            startActivity(lIntent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_LONG).show();

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
        setContentView(R.layout.activity_add_note);
        initUi();
        setListeners();
        // Show the custom action bar view and hide the normal Home icon and title.
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
            String noteTextString = extras.getString(DBHelper.NOTE_DESCRIPTION);
            noteId = extras.getInt(DBHelper.COLUMN_ID);
            requestId = extras.getInt(DBHelper.NOTE_REQUEST_ID);
            noteText.setText(noteTextString);
            Integer tagId = extras.getInt(DBHelper.NOTE_TAG);
        }else{
            Toast.makeText(this, "Could not load note", Toast.LENGTH_LONG).show();
        }



        DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
        if (dpd != null) {
            dpd.setOnDateSetListener(this);
        }

        TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
        if (tpd != null) {
            tpd.setOnTimeSetListener(this);
        }

        updateUi();

    }


    private void updateUi() {

    }
        private void initUi() {
        noteText = (EditText) findViewById(R.id.noteText);
        addReminderButton =(Button) findViewById(R.id.addReminder);
        remainderContainer =(LinearLayout) findViewById(R.id.remainderContainer);
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        tag = (Spinner) findViewById(R.id.selectSpinner);
        List<Tag> tags = Tag.getAllTags(new ArrayList<Tag>(),getApplicationContext());
        tags.add(new Tag("No Tag"));
        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                android.R.layout.simple_spinner_item,tags );
        tag.setAdapter(adapter);
    }
    private void setListeners() {
        addReminderButton.setOnClickListener(new View.OnClickListener() {


            final Calendar calendar = Calendar.getInstance();
            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(UpdateNoteActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
            final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(UpdateNoteActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);


            @Override
            public void onClick(View view) {
                addReminderButton.setVisibility(View.GONE);
                final View add_reminder = getLayoutInflater().inflate(R.layout.add_reminder_non_repeating, remainderContainer, false);
                remainderContainer.addView(add_reminder);
                Button close = (Button) add_reminder.findViewById(R.id.close);
                dateSpinner = (Spinner) add_reminder.findViewById(R.id.date);
                timeSpinner = (Spinner) add_reminder.findViewById(R.id.time);

                final List <String> dates = new ArrayList<String>();
                dates.add("");
                dates.add("Today");
                dates.add("Yesterday");
                dates.add("Pick Date");
                final List <String> times = new ArrayList<String>();
                times.add("");
                times.add("Early Morning 5:00 AM");
                times.add("Morning 8:00 AM");
                times.add("Afternoon 12:00 PM");
                times.add("Evening 6:00 PM");
                times.add("Pick Time");

                ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,dates ){
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent)
                    {
                        View v = null;

                        // If this is the initial dummy entry, make it hidden
                        if (position == 0) {
                            TextView tv = new TextView(getContext());
                            tv.setHeight(0);
                            tv.setVisibility(View.GONE);
                            v = tv;
                        }
                        else {
                            // Pass convertView as null to prevent reuse of special case views
                            v = super.getDropDownView(position, null, parent);
                        }

                        // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                        parent.setVerticalScrollBarEnabled(false);
                        return v;
                    }
                };

                final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,times ){
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent)
                    {
                        View v = null;

                        // If this is the initial dummy entry, make it hidden
                        if (position == 0) {
                            TextView tv = new TextView(getContext());
                            tv.setHeight(0);
                            tv.setVisibility(View.GONE);
                            v = tv;
                        }
                        else {
                            // Pass convertView as null to prevent reuse of special case views
                            v = super.getDropDownView(position, null, parent);
                        }

                        // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                        parent.setVerticalScrollBarEnabled(false);
                        return v;
                    }
                };

                dateSpinner.setAdapter(dateAdapter);
                timeSpinner.setAdapter(timeAdapter);
                dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(dates.get(i).equals("Pick Date")){
                                datePickerDialog.setYearRange(1985, 2028);
                                datePickerDialog.setCloseOnSingleTapDay(false);
                                datePickerDialog.show(UpdateNoteActivity.this.getSupportFragmentManager(), DATEPICKER_TAG);


                            }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(times.get(i).equals("Pick Time")){
                            timePickerDialog.setCloseOnSingleTapMinute(false);
                            timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        remainderContainer.removeView(add_reminder);
                        addReminderButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });



    }




    private void updateNote(Integer noteId) {
        String noteTextString=noteText.getText().toString();
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        Note note = noteDBHelper.getNote(noteId);
        Tag noteTag = (Tag) tag.getSelectedItem();

        if(note!=null){
            if(noteTextString.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_LONG).show();
            }
            else{
                noteDBHelper.updateNote(note, noteTextString,noteTag.getTagId());
                noteDBHelper.close();
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }

        }
        else{
            noteDBHelper.close();
            Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
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
    protected void onPause()
    {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if(dateSpinner != null){
            dateSpinner.setSelection(0);
            EditText et = (EditText)dateSpinner.getChildAt(0);
            et.setText("Date Max");

        }
        Toast.makeText(UpdateNoteActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(UpdateNoteActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }
}
