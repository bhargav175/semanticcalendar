package com.bhargav.smart.smartTasks.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.activities.ArchivesActivity;
import com.bhargav.smart.smartTasks.activities.ViewRepeatingTaskActivity;
import com.bhargav.smart.smartTasks.database.TaskListDBHelper;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.ArrayList;
import java.util.List;


public class RepeatingTaskInfo extends Fragment {
    public static final String LOG_TAG = "CalendarPrint";
    private RepeatingTask repeatingTaskCurrent;
    private TextView title,description,category,due,state,reminder;
    private Typeface font;
    private final int UNARCHIVE=0;
    private View rootView;
    String taskListName;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_meta_repeating_task, container, false);
        setHasOptionsMenu(true);
        initUi();
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        taskListName = extras.getString(DBHelper.CATEGORY_TITLE);
        new GetNote(extras.getInt(DBHelper.COLUMN_ID)).execute("");
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.listView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            TaskList obj = (TaskList) lv.getItemAtPosition(acmi.position);
            menu.setHeaderTitle(obj.getTagText());
            menu.add(0,UNARCHIVE,0,"Unarchive");
        }

    }
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {


                switch (item.getItemId()) {
                    case UNARCHIVE:

                        return true;
                    default:

                        return true;
                }

    }



    private void initUi() {
        title = (TextView) rootView.findViewById(R.id.title);
        description =(TextView) rootView.findViewById(R.id.description);
        category = (TextView) rootView.findViewById(R.id.category);
        due =(TextView) rootView.findViewById(R.id.due);
        reminder =(TextView) rootView.findViewById(R.id.reminder);

    }


    private void updateUi() {
        title.setText(repeatingTaskCurrent.getRepeatingTaskText());
        description.setText(repeatingTaskCurrent.getRepeatingTaskDescription());
        due.setText(RepeatingTask.getMetaText(repeatingTaskCurrent));
        reminder.setText(repeatingTaskCurrent.getIsReminded().toString());
        if(taskListName == null){
            new GetTaskList(repeatingTaskCurrent.getId()).execute("");
        }else{
            setCategoryText();
        }
    }




    private void setCategoryText(){
        category.setText(taskListName);

    }


    private class GetNote extends AsyncTask<String, Void, RepeatingTask> {

        private int id;

        public GetNote(int id) {
            this.id = id;
        }

        @Override
        protected RepeatingTask doInBackground(String... params) {

            repeatingTaskCurrent = RepeatingTask.getRepeatingTaskById(id, getActivity());
            return repeatingTaskCurrent;
        }

        @Override
        protected void onPostExecute(final RepeatingTask oneTimeTask) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (repeatingTaskCurrent != null) {
                        updateUi();
                    } else {
                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
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

    private class GetTaskList extends AsyncTask<String, Void, Void> {

        private int id;

        public GetTaskList(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(String... params) {

            TaskList taskList = TaskList.getTagById(id, getActivity());
            taskListName = taskList.getTagText();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    setCategoryText();
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
