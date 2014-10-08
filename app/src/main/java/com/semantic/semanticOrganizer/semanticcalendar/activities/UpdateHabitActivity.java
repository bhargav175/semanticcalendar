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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.HabitDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Habit;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class UpdateHabitActivity extends Activity {
    private HabitDBHelper habitDBHelper;
    private EditText habitText;
    private Spinner tag;
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
            String habitTextString = extras.getString(DBHelper.HABIT_TEXT);
            habitId = extras.getInt(DBHelper.COLUMN_ID);
            habitText.setText(habitTextString);
            Integer tagId = extras.getInt(DBHelper.HABIT_TAG);
        }else{
            Toast.makeText(this, "Could not load habit", Toast.LENGTH_LONG).show();
        }


    }

    private void initUi() {
        habitText = (EditText) findViewById(R.id.habitText);
        habitDBHelper = new HabitDBHelper(this);
        habitDBHelper.open();
        tag = (Spinner) findViewById(R.id.selectSpinner);
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
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_LONG).show();
            }
            else{
                habitDBHelper.updateHabit(habit, habitTextString,habitTag.getTagId());
                habitDBHelper.close();
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }

        }
        else{
            habitDBHelper.close();
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
