package com.bhargav.smart.smartTasks.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.database.RepeatingTaskDBHelper;
import com.bhargav.smart.smartTasks.database.OneTimeTaskDBHelper;
import com.bhargav.smart.smartTasks.database.TaskListDBHelper;
import com.bhargav.smart.smartTasks.helpers.ComingUpDialog;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.OneTimeTask;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.List;

public class TaskListsActivity extends ActionBarActivity {
    private TextView archivesLink, timelineLink,settingsLink;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ArrayAdapter<TaskList> tagDrawerLayoutArrayAdapter;
    private List<TaskList> taskLists;
    private Typeface font;
    private Integer doSave;
    private LinearLayout sidebarHeader;
    private FloatingActionsMenu fam;
    private ComingUpDialog wAlertBuilder;
    private AlertDialog wAlert;
    private ListView tagsListView;
    private ArrayAdapter<TaskList> tagArrayAdapter;
    private LinearLayout mCustomHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        preInit();
        new GetTags(this).execute("");
    }
    private void preInit(){
        tagsListView = (ListView) findViewById(R.id.listView);
        font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        fam = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.right_drawer);
        sidebarHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.sidebar_right_header, null);
        mDrawerListView.addHeaderView(sidebarHeader);
        archivesLink = (TextView) findViewById(R.id.archives);
        timelineLink = (TextView) findViewById(R.id.home);
        archivesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListsActivity.this,ArchivesActivity.class);
                startActivity(intent);
            }
        });
        timelineLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListsActivity.this,SuperMain.class);
                startActivity(intent);
            }
        });

        wAlertBuilder = new ComingUpDialog(this);
        wAlert = wAlertBuilder.create();
        mCustomHeaders=new LinearLayout(getApplicationContext());
        mCustomHeaders.setOrientation(LinearLayout.VERTICAL);
        settingsLink = (TextView) findViewById(R.id.settings);
        settingsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListsActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences tprefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean pref_show_statistics = tprefs.getBoolean("pref_show_statistics", false);
        Toast.makeText(this,pref_show_statistics.toString(),Toast.LENGTH_SHORT).show();
    }


    private void afterGetTags(){
        tagArrayAdapter = new ArrayAdapter<TaskList>(this,R.layout.tag_goal_item,R.id.text1, taskLists){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView mainText = (TextView) view.findViewById(R.id.text1);
                TextView description = (TextView) view.findViewById(R.id.text2);
                LinearLayout bgDiv = (LinearLayout) view.findViewById(R.id.bgDiv);
                mainText.setText(taskLists.get(position).getTagText());
                if(taskLists.get(position).getTagDescription()==null || taskLists.get(position).getTagDescription().length() ==0){
                    description.setVisibility(View.GONE);
                }
                else{
                    description.setText(taskLists.get(position).getTagDescription());

                }
                if(taskLists.get(position).getTagColor() == null || taskLists.get(position).getTagColor() == utilFunctions.Color.TRANSPARENT){

                }else{
                    bgDiv.setBackgroundColor(getResources().getColor(utilFunctions.Color.getColorResource(taskLists.get(position).getTagColor())));

                }
                return view;
            }
        };

        tagsListView.setAdapter(tagArrayAdapter);
        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
                intent.putExtra(DBHelper.COLUMN_ID, taskLists.get(position).getTagId());
                startActivity(intent);
            }
        });
        tagDrawerLayoutArrayAdapter = new ArrayAdapter<TaskList>(this,R.layout.drawer_list_item_tag,R.id.title, taskLists){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.title);
                cardText1.setText(utilFunctions.toCamelCase(taskLists.get(position).getTagText()));
                return view;
            }
        };
        mDrawerListView.setAdapter(tagDrawerLayoutArrayAdapter);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= mDrawerListView.getHeaderViewsCount();
                if(position>-1){
                    if(taskLists.get(position).getTagId()!=null){
                        Intent intent = new Intent(TaskListsActivity.this,TaskListActivity.class);
                        intent.putExtra(DBHelper.COLUMN_ID,(taskLists.get(position).getTagId()));
                        startActivity(intent);
                    }else{

                    }
                }

            }
        });
        addFloatingActionButtons();

    }

    private void saveStuff(String str, TaskList t){
        if(doSave==1){
            OneTimeTask oneTimeTask = new OneTimeTask();
            OneTimeTaskDBHelper oneTimeTaskDbHelper = new OneTimeTaskDBHelper(getApplicationContext());
            oneTimeTask.setNoteTitle(str);
            oneTimeTaskDbHelper.open();
            oneTimeTaskDbHelper.saveNoteWithTag(oneTimeTask, t);
            oneTimeTaskDbHelper.close();
        }else if(doSave ==2){

        }else if(doSave==3){
            RepeatingTask repeatingTask = new RepeatingTask();
            RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(getApplicationContext());
            repeatingTask.setRepeatingTaskText(str);
            repeatingTaskDBHelper.open();
            repeatingTaskDBHelper.saveRepeatingTaskWithTag(repeatingTask, t);
            repeatingTaskDBHelper.close();
        }else if(doSave==4){

            new SaveTag(getApplicationContext(),str).execute("");

        }
    }


    private void addFloatingActionButtons(){


        final LayoutInflater layoutInflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskListsActivity.this);
        // Setting Dialog Title
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        FloatingActionButton addList = (FloatingActionButton) findViewById(R.id.addListFabButton);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 4;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final Spinner spinner = (Spinner) editInputLayout.findViewById(R.id.spinner);
                spinner.setVisibility(View.GONE);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
                editInput.setCursorVisible(true);
                alertDialog.setTitle("Add New Goal").setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString(),null);
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.goals, menu);
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

    private class GetTags extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetTags(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            taskLists = TaskList.getAllUnArchivedTags(context);
            //No Sandbox from now
//            tags.add(new LOG_TAG("Untagged"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    afterGetTags();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    private class SaveTag extends AsyncTask<String, Void,TaskList> {

        private Context context;
        private String str;

        public SaveTag(Context context, String str){
            this.context=context;
            this.str = str;
        }

        @Override
        protected TaskList doInBackground(String... params) {

            TaskList taskList = new TaskList();
            TaskListDBHelper taskListDBHelper = new TaskListDBHelper(getApplicationContext());
            taskList.setTagText(str);
            taskListDBHelper.open();
            TaskList tempTaskList = taskListDBHelper.saveTag(taskList);
            taskListDBHelper.close();
            taskLists.add(tempTaskList);
            return tempTaskList;
        }

        @Override
        protected void onPostExecute(final TaskList t) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tagDrawerLayoutArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),""+t.getTagText()+" goal created",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,t.getTagId());
                    startActivity(intent);

                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
