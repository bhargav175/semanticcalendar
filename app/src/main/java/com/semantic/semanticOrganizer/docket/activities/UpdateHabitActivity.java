package com.semantic.semanticOrganizer.docket.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Toast;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.database.HabitDBHelper;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Habit;
import com.semantic.semanticOrganizer.docket.models.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateHabitActivity extends Activity {
    private HabitDBHelper habitDBHelper;
    private EditText habitText, habitQuestion, habitDuration, habitFrequency;
    private Spinner tag, frequencyBase, habitType,habitDurationSpinner;
    private LinearLayout fixedHabitLayout, flexibleHabitLayout;
    private CheckBox isArchived;
    private Boolean sun, mon,tue,wed,thu,fri,sat;
    private Habit habitCurrent;
    private Button sundayButton, mondayButton, tuesdayButton, wednesdayButton,thursdayButton, fridayButton, saturdayButton;
    Integer habitId,currentHabitDuration,currentHabitFrequency;

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
                        if(habitCurrent!=null){
                            if (habitText.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();

                            } else {
                                updateHabit();
                                Intent intent = new Intent(UpdateHabitActivity.this,TagActivity.class);
                                if(habitCurrent.getTag()!=null){
                                    intent.putExtra(DBHelper.COLUMN_ID,habitCurrent.getTag());

                                }
                                startActivity(intent);
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
        habitText = (EditText) findViewById(R.id.habitText);
        habitQuestion = (EditText) findViewById(R.id.habitQuestion);
        habitDurationSpinner = (Spinner) findViewById(R.id.durationSpinner);

        //2 spinners
        habitType = (Spinner) findViewById(R.id.selectHabitType);
        tag = (Spinner) findViewById(R.id.selectSpinner);
        frequencyBase = (Spinner) findViewById(R.id.frequencyBase);


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

        setButtonState(sundayButton,sun);
        setButtonState(mondayButton,mon);
        setButtonState(tuesdayButton,tue);
        setButtonState(wednesdayButton,wed);
        setButtonState(thursdayButton,thu);
        setButtonState(fridayButton,fri);
        setButtonState(saturdayButton,sat);



        habitDBHelper = new HabitDBHelper(this);
        habitDBHelper.open();
        flexibleHabitLayout = (LinearLayout) findViewById(R.id.linearLayoutFlexible);
        fixedHabitLayout = (LinearLayout) findViewById(R.id.linearLayoutFixed);

        //Duration
        List<String> durationStrings = new ArrayList<String>();
        durationStrings.addAll(Arrays.asList(Habit.durationStrings));


        ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,durationStrings);
        habitDurationSpinner.setAdapter(durationAdapter);
        habitDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               currentHabitDuration = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

 //habit Type
        List<String> habitTypeStrings = new ArrayList<String>();
        habitTypeStrings.add("Fixed");
        habitTypeStrings.add("Flexible");
        ArrayAdapter<String> habitTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,habitTypeStrings);
        habitType.setAdapter(habitTypeAdapter);
        habitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    habitCurrent.setHabitType(Habit.Type.FIXED);
                    fixedHabitLayout.setVisibility(View.VISIBLE);
                    flexibleHabitLayout.setVisibility(View.GONE);
                }else{
                    habitCurrent.setHabitType(Habit.Type.FLEXIBLE);
                    fixedHabitLayout.setVisibility(View.GONE);
                    flexibleHabitLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        habitType.setSelection(0);
        //Frequency
        List<String> habitFrequencyStrings = new ArrayList<String>();
        habitFrequencyStrings.addAll(Arrays.asList(Habit.frequencyStrings));
        ArrayAdapter<String> habitFrequencyAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,habitFrequencyStrings);
        frequencyBase.setAdapter(habitFrequencyAdapter);
        frequencyBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentHabitFrequency = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        frequencyBase.setSelection(0);
    }
    private void updateUi(){
        habitText.setText(habitCurrent.getHabitText());

        if(habitCurrent.getDuration()!=null){
            habitDurationSpinner.setSelection(habitCurrent.getDuration());
        }
        if(habitCurrent.getIsArchived()!=null){
           isArchived.setChecked(habitCurrent.getIsArchived());
        }
        if(habitCurrent.getHabitType()!=null){
            if(habitCurrent.getHabitType() == Habit.Type.FIXED){
                habitType.setSelection(0);

                int habitCurrentDaysCode = habitCurrent.getDaysCode();
                List<Boolean> daysBoolean = Habit.getBooleansFromDayCode(habitCurrentDaysCode);
                sun = daysBoolean.get(0);
                mon = daysBoolean.get(1);
                tue = daysBoolean.get(2);
                wed = daysBoolean.get(3);
                thu = daysBoolean.get(4);
                fri = daysBoolean.get(5);
                sat = daysBoolean.get(6);

                setButtonState(sundayButton,sun);
                setButtonState(mondayButton,mon);
                setButtonState(tuesdayButton,tue);
                setButtonState(wednesdayButton,wed);
                setButtonState(thursdayButton,thu);
                setButtonState(fridayButton,fri);
                setButtonState(saturdayButton,sat);

            }else{
                habitType.setSelection(1);
                if(habitCurrent.getFrequency()!=null){
                    frequencyBase.setSelection(habitCurrent.getFrequency());
                }else{
                    frequencyBase.setSelection(0);
                }

            }
        }
        new GetTags().execute("");

    }
    private void setListeners() {
        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sun = !sun;
                setButtonState(sundayButton,sun);
            }
        });
        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mon = !mon;
                setButtonState(mondayButton,mon);
            }
        });
        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tue = !tue;
                setButtonState(tuesdayButton,tue);
            }
        });
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wed = !wed;
                setButtonState(wednesdayButton,wed);
            }
        });
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thu = !thu;
                setButtonState(thursdayButton,thu);
            }
        });
        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fri = !fri;
                setButtonState(fridayButton,fri);
            }
        });
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sat = !sat;
                setButtonState(saturdayButton,sat);
            }
        });

    }

    private void updateHabit() {
        String habitTextString=habitText.getText().toString();
        habitDBHelper = new HabitDBHelper(this);
        habitDBHelper.open();
        Tag habitTag = (Tag) tag.getSelectedItem();
        habitCurrent.setHabitText(habitTextString);
        habitCurrent.setTag(habitTag.getTagId());
        habitCurrent.setDuration(currentHabitDuration);
        habitCurrent.setIsArchived(isArchived.isChecked());
        if(habitCurrent.getHabitType()== Habit.Type.FIXED){
            habitCurrent.setFrequency(null);
            habitCurrent.setDaysCode(Habit.toDaysCode(sun,mon,tue,wed,thu,fri,sat));
        }else{
            habitCurrent.setDaysCode(null);
            habitCurrent.setFrequency(currentHabitFrequency);
        }

        if(habitCurrent!=null){
            if(habitTextString.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show();
            }
            else{
                habitDBHelper.updateHabit(habitCurrent);
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

    private class GetHabit extends AsyncTask<String, Void, Habit> {

        private int id;

        public GetHabit(int id) {
            this.id = id;
        }

        @Override
        protected Habit doInBackground(String... params) {

            habitCurrent = Habit.getHabitById(id, getApplicationContext());
            return habitCurrent;
        }

        @Override
        protected void onPostExecute(final Habit habit) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (habitCurrent != null) {
                        initUi();
                        setListeners();

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




private class GetTags extends AsyncTask<String, Void, List<Tag>> {



    public GetTags() {

    }

    @Override
    protected List<Tag> doInBackground(String... params) {
        List<Tag> tags = Tag.getAllTags(new ArrayList<Tag>(),getApplicationContext());
        return tags;

    }

    @Override
    protected void onPostExecute(final List<Tag> tags) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                tags.add(new Tag("No Tag"));
                ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,tags );
                tag.setAdapter(adapter);

                if(habitCurrent.getTag()!=null){
                    for(Tag tagD : tags){
                        if(tagD.getTagId() == habitCurrent.getTag()){
                            Integer pos = tags.indexOf(tagD);
                            tag.setSelection(pos);
                            break;
                        }
                    }
                }else{
                    tag.setSelection(tags.size()-1);
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

    public void setButtonState(Button button, Boolean bool){
        if(bool){
            button.setBackgroundColor(getResources().getColor(R.color.light_blue_500));
            button.setTextColor(getResources().getColor(R.color.white));
        }
        else{
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setTextColor(getResources().getColor(R.color.light_blue_500));
        }

    }

}
