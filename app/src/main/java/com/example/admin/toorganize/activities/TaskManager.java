package com.example.admin.toorganize.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.admin.toorganize.R;
import com.example.admin.toorganize.database.TaskDBHelper;
import com.example.admin.toorganize.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager extends  ActionBarActivity {
    private TaskDBHelper taskDbHelper;
    public static final String Tag="TaskManager";
    public final static String EXTRA_MESSAGE = "com.example.admin.toorganize.TaskManager";
    public final static String TASK_TEXT = "task_text";
    public final static String TASK_ID = "task_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        List<Task> taskList = new ArrayList<Task>();
        taskDbHelper = new TaskDBHelper(this);

        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
                android.R.layout.simple_list_item_1, getAllTasks(taskList));
        ListView list = (ListView) findViewById(R.id.taskListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Task task= (Task) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), ViewTaskActivity.class);
                intent.putExtra(TASK_TEXT, task.getText());
                intent.putExtra(TASK_ID,(int) task.getId());
                startActivity(intent);
            }
        });



    }

    private List<Task> getAllTasks(List<Task> taskList) {
        taskDbHelper.open();
        Cursor cursor= taskDbHelper.fetchAllTasks();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = taskDbHelper.cursorToTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        taskDbHelper.close();
        return taskList;
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_manager, menu);
        return super.onCreateOptionsMenu(menu);
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
        if (id == R.id.action_search) {
            //handleYourEvent();
            Log.d(Tag,"Seach");
            return true;
        }
        if (id == R.id.action_new) {
            //handleYourEvent();
            Intent intent = new Intent(this, WriteTask.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

}
