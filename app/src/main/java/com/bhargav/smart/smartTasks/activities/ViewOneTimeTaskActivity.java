package com.bhargav.smart.smartTasks.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.adapters.ArchivesPagerAdapter;
import com.bhargav.smart.smartTasks.database.OneTimeTaskDBHelper;
import com.bhargav.smart.smartTasks.database.ReminderDBHelper;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.helpers.DueDateDialog;
import com.bhargav.smart.smartTasks.helpers.OneTimeTaskReminderHelper;
import com.bhargav.smart.smartTasks.helpers.StateBox;
import com.bhargav.smart.smartTasks.models.OneTimeTask;
import com.bhargav.smart.smartTasks.models.OrganizerItem;
import com.bhargav.smart.smartTasks.models.Reminder;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ViewOneTimeTaskActivity extends ActionBarActivity  {

    private FloatingActionButton editFam;
    private OneTimeTask oneTimeTaskCurrent;

    private ArrayAdapter<TaskList> adapter;
    private TextView title,description,category,due,reminder;
    private StateBox state;


    ViewPager mPager;
   private DrawerLayout mDrawerLayout;
    private LinearLayout sidebarHeader;
    private ListView mDrawerListView;
    private ArrayAdapter<TaskList> tagDrawerLayoutArrayAdapter;
    private List<TaskList> taskLists;
    private Typeface font;
    private TextView timelineLink,homeLink,settingsLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        setContentView(R.layout.activity_view_note);
        preInit();
        new GetTags(this).execute("");


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        new GetNote(extras.getInt(DBHelper.COLUMN_ID)).execute("");
        // Show the custom action bar view and hide the normal Home icon and title.



    }

    private void preInit() {
        font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.right_drawer);
        sidebarHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.sidebar_right_header, null);
        mDrawerListView.addHeaderView(sidebarHeader);
        timelineLink = (TextView) findViewById(R.id.home);
        settingsLink = (TextView) findViewById(R.id.settings);
        settingsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneTimeTaskActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        timelineLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneTimeTaskActivity.this, SuperMain.class);
                startActivity(intent);
            }
        });
        homeLink = (TextView) findViewById(R.id.lists);
        homeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneTimeTaskActivity.this, TaskListsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void afterGetTags(){
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
                        Intent intent = new Intent(ViewOneTimeTaskActivity.this,TaskListActivity.class);
                        intent.putExtra(DBHelper.COLUMN_ID,(taskLists.get(position).getTagId()));
                        startActivity(intent);
                    }else{

                    }
                }
            }
        });


    }




    private void initUi() {
        title = (TextView) findViewById(R.id.title);
        description =(TextView) findViewById(R.id.description);
        category = (TextView) findViewById(R.id.category);
        due =(TextView) findViewById(R.id.due);
        state = (StateBox) findViewById(R.id.state);
        reminder =(TextView) findViewById(R.id.reminder);
        new GetTags(this).execute("");
    }


    private void updateUi() {
        title.setText(oneTimeTaskCurrent.getNoteTitle());
        description.setText(oneTimeTaskCurrent.getNoteDescription());
        due.setText(OneTimeTask.getMetaText(oneTimeTaskCurrent));
        state.init(this,oneTimeTaskCurrent.getId());
        reminder.setText(oneTimeTaskCurrent.getIsReminded().toString());
        setFloatingActionListeners();
        updateCategoryName();
   }

    private void updateCategoryName(){

        for(TaskList taskList :taskLists){
            if(oneTimeTaskCurrent.getTag() == taskList.getTagId()){
                category.setText(utilFunctions.toCamelCase(taskList.getTagText()));
                break;
            }
        }


    }




    private void setFloatingActionListeners(){
        editFam = (FloatingActionButton) findViewById(R.id.edit);
        editFam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneTimeTaskActivity.this,UpdateOneTimeTaskActivity.class);
                intent.putExtra(DBHelper.COLUMN_ID,oneTimeTaskCurrent.getId());
                startActivity(intent);
            }
        });

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



    private class GetTags extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetTags(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            taskLists = TaskList.getAllUnArchivedTags(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //updateCategoryName();
                    afterGetTags();

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
        finish();
    }

}
