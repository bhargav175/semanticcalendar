package com.bhargav.smart.smartTasks.activities;

import android.app.ActionBar;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.database.TaskListDBHelper;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.helpers.InlineEditable;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateTaskListActivity extends ActionBarActivity {
    private TaskListDBHelper taskListDBHelper;
    private InlineEditable tagText;
    private InlineEditable tagDescription;
    private Switch tagIsArchived;
    private Spinner colorSpinner;
    Integer tagId;
    private TaskList currentTaskListInView;
    private ArrayAdapter<String> colorAdapter;
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
                        if(currentTaskListInView !=null){
                            if (tagText.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                updateTag();

                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_SHORT).show();
                            finish();

                        }
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
        setContentView(R.layout.activity_add_tag);
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
            String tagTextString = extras.getString(DBHelper.CATEGORY_TITLE);
            tagId = extras.getInt(DBHelper.COLUMN_ID);
            if(tagId!=null){
                new GetTag(this,tagId).execute("");
            }
            else{
                Toast.makeText(this, "Could not load tag", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Could not load tag", Toast.LENGTH_SHORT).show();
        }

    }
    private void initUi() {
        tagText = (InlineEditable) findViewById(R.id.tagTitle);
        tagDescription = (InlineEditable) findViewById(R.id.descriptionText);
        tagIsArchived = (Switch) findViewById(R.id.isArchivedSwitch);
        taskListDBHelper = new TaskListDBHelper(this);
        colorSpinner =  (Spinner) findViewById(R.id.backgroundSpinner);




    }
    private void setListeners() {


    }

    private void init(TaskList t){

        currentTaskListInView = t;
        String[] a = {"None","Violet","Indigo","Blue","Green","Yellow","Orange","Red","Black"};
        final List<String> colorStrings = Arrays.asList(a);
        colorAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_list_item,R.id.text1 ,colorStrings){

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);


                //If convertView is null create a new view, else use convert view
                if(view==null){
                    view =((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_list_item,parent,false);

                }
                TextView tv = (TextView) view.findViewById(R.id.text1);
                if(tv!=null){
                    String s = colorStrings.get(position);
                    tv.setText(s);
                    if(position>0){

                        utilFunctions.Color c = utilFunctions.Color.values()[position];
                        int cr = utilFunctions.Color.getColorResource(c);
                        tv.setBackgroundColor(getResources().getColor(cr));
                        tv.setTextColor(getResources().getColor(android.R.color.white));
                    }
                    else if(position==0){
                        utilFunctions.Color c = utilFunctions.Color.values()[position];
                        tv.setBackgroundColor(getResources().getColor(R.color.light_gray_color));
                        tv.setTextColor(getResources().getColor(R.color.pitch_black));
                    }


                }
                return view;
            }
            //
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);


                //If convertView is null create a new view, else use convert view
                if(view==null){
                    view =((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_list_item,parent,false);

                }
                TextView tv = (TextView) view.findViewById(R.id.text1);
                if(tv!=null){
                    String s = colorStrings.get(position);
                    tv.setText(s);
                    if(position>0){

                        utilFunctions.Color c = utilFunctions.Color.values()[position];
                        int cr = utilFunctions.Color.getColorResource(c);
                        tv.setBackgroundColor(getResources().getColor(cr));
                        tv.setTextColor(getResources().getColor(android.R.color.white));
                    }
                    else if(position==0){
                        utilFunctions.Color c = utilFunctions.Color.values()[position];
                        tv.setBackgroundColor(getResources().getColor(R.color.light_gray_color));
                        tv.setTextColor(getResources().getColor(R.color.pitch_black));
                    }


                }
                return view;
            }
        };

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentTaskListInView.setTagColor(utilFunctions.Color.values()[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        colorSpinner.setAdapter(colorAdapter);

        getActionBar().setTitle(utilFunctions.toCamelCase(currentTaskListInView.getTagText()));
        tagText.setText(currentTaskListInView.getTagText());
        tagDescription.setText(currentTaskListInView.getTagDescription());
        tagIsArchived.setChecked(currentTaskListInView.getIsArchived());

        if(currentTaskListInView.getTagColor()==null){
            currentTaskListInView.setTagColor(utilFunctions.Color.values()[0]);
        }
        int s =currentTaskListInView.getTagColor().getColorValue();
        colorSpinner.setSelection(s,false);

    }

    private void updateTag() {
        String tagTextString=tagText.getText().toString();
        String tagDescriptionString=tagDescription.getText().toString();
        Boolean tagArchived=tagIsArchived.isChecked();

        currentTaskListInView.setTagText(tagTextString);
        currentTaskListInView.setTagDescription(tagDescriptionString);
        currentTaskListInView.setIsArchived(tagArchived);
        currentTaskListInView.setTagColor(utilFunctions.Color.values()[colorSpinner.getSelectedItemPosition()]);

            if(tagTextString.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show();
            }
            else{
                new UpdateTag(getApplicationContext(), currentTaskListInView).execute("");
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

    private class GetTag extends AsyncTask<String, Void,TaskList> {

        private Context context;
        private Integer tagId;

        public GetTag(Context context,Integer tagId){
            this.context=context;
            this.tagId = tagId;
        }

        @Override
        protected TaskList doInBackground(String... params) {
            TaskList t = TaskList.getTagById(tagId, context);
            return t;
        }

        @Override
        protected void onPostExecute(final TaskList t) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    init(t);
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class UpdateTag extends AsyncTask<String, Void,TaskList> {

        private Context context;
        private TaskList taskList;
        public UpdateTag(Context context, TaskList taskList){
            this.context=context;this.taskList = taskList;
        }

        @Override
        protected TaskList doInBackground(String... params) {
            TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
            try{
                taskListDBHelper.open();
                taskListDBHelper.updateTag(taskList);
                taskListDBHelper.close();
                return taskList;
            }catch (Exception e){
                return null;
            }


        }

        @Override
        protected void onPostExecute(final TaskList t) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(t!=null){
                        Toast.makeText(context, t.getTagText() +"is Updated",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "There was an error",Toast.LENGTH_SHORT).show();

                    }
                    if(t.getIsArchived()){
                        Intent intent = new Intent(UpdateTaskListActivity.this,SuperMain.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(UpdateTaskListActivity.this,TaskListActivity.class);
                        if(currentTaskListInView.getTagId()!=null) {
                            intent.putExtra(DBHelper.COLUMN_ID, currentTaskListInView.getTagId());
                        }
                        startActivity(intent);
                    }


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
        if(currentTaskListInView !=null){
            Intent intent = new Intent(this,TaskListActivity.class);
            intent.putExtra(DBHelper.COLUMN_ID, currentTaskListInView.getTagId());
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,SuperMain.class);
            startActivity(intent);
        }
    }
}
