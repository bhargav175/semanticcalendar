package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.HabitDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.CheckList;
import com.semantic.semanticOrganizer.semanticcalendar.models.Habit;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class UpdateHabitActivity extends Activity {
    private HabitDBHelper habitDBHelper;
    private EditText habitText, habitQuestion, habitDuration, habitFrequency;
    private Spinner tag, frequencyBase, habitType;
    private LinearLayout fixedHabitLayout, flexibleHabitLayout;
    private CheckBox isArchived;
    private Boolean sun, mon,tue,wed,thu,fri,sat;
    private Button sundayButton, mondayButton, tuesdayButton, wednesdayButton,thursdayButton, fridayButton, saturdayButton;
    Integer habitId;
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
                        if(habitId!=null){
                            updateHabit(habitId);
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
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
        initUi();

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
            String habitTextString = extras.getString(DBHelper.HABIT_TEXT);
            String habitQuestionString = extras.getString(DBHelper.HABIT_QUESTION);
            Integer habitDurationInt = extras.getInt(DBHelper.HABIT_DURATION);
            Integer habitFrequency = extras.getInt(DBHelper.HABIT_FREQUENCY);
            Boolean isArchived = extras.getInt(DBHelper.HABIT_IS_ARCHIVED)>0;
            Integer tagId = extras.getInt(DBHelper.HABIT_TAG);
            Integer habitTypeInt = extras.getInt(DBHelper.HABIT_TYPE);
            Integer habitDaysCode = extras.getInt(DBHelper.HABIT_DAYS_CODE);
            Integer requestId = extras.getInt(DBHelper.HABIT_REQUEST_ID);
            String habitCreatedTime = extras.getString(DBHelper.COLUMN_CREATED_TIME);

            habitText.setText(habitTextString);
            habitQuestion.setText(habitQuestionString);
            habitDuration.setText(habitDurationInt.toString());


        }else{
            Toast.makeText(this, "Could not load habit", Toast.LENGTH_SHORT).show();
        }


    }

    private void initUi() {

        //4 edittexts
        habitText = (EditText) findViewById(R.id.habitText);
        habitQuestion = (EditText) findViewById(R.id.habitQuestion);
        habitDuration = (EditText) findViewById(R.id.duration);
        habitFrequency = (EditText) findViewById(R.id.habitFrequency);

        //2 spinners
        habitType = (Spinner) findViewById(R.id.selectHabitType);
        tag = (Spinner) findViewById(R.id.selectSpinner);

        isArchived = (CheckBox) findViewById(R.id.isArchived);


        //7 Booleans

        sun = false;
        mon = false;
        tue = false;
        wed = false;
        thu = false;
        fri = false;
        sat = false;


        //8 Buttons
        sundayButton = (Button) findViewById(R.id.sundayButton);
        mondayButton = (Button) findViewById(R.id.mondayButton);
        tuesdayButton = (Button) findViewById(R.id.tuesdayButton);
        wednesdayButton = (Button) findViewById(R.id.wednesdayButton);
        thursdayButton = (Button) findViewById(R.id.thursdayButton);
        fridayButton = (Button) findViewById(R.id.fridayButton);
        saturdayButton = (Button) findViewById(R.id.saturdayButton);



        habitDBHelper = new HabitDBHelper(this);
        habitDBHelper.open();
        flexibleHabitLayout = (LinearLayout) findViewById(R.id.linearLayoutFlexible);
        fixedHabitLayout = (LinearLayout) findViewById(R.id.linearLayoutFixed);
        List<Tag> tags = Tag.getAllTags(new ArrayList<Tag>(),getApplicationContext());
        tags.add(new Tag("No Tag"));
        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                android.R.layout.simple_spinner_item,tags );
        tag.setAdapter(adapter);
    }
    private void setListeners() {


    }

    private void updateHabit(Integer habitId) {
        String habitTextString=habitText.getText().toString();
        habitDBHelper = new HabitDBHelper(this);
        habitDBHelper.open();
        Habit habit = habitDBHelper.getHabit(habitId);
        Tag habitTag = (Tag) tag.getSelectedItem();

        if(habit!=null){
            if(habitTextString.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show();
            }
            else{
                habitDBHelper.updateHabit(habit, habitTextString,habitTag.getTagId());
                habitDBHelper.close();

            }

        }
        else{
            habitDBHelper.close();
            Toast.makeText(this,"Update Failed",Toast.LENGTH_SHORT).show();
        }


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
}
