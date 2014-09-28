package com.example.admin.toorganize.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.toorganize.R;
import com.example.admin.toorganize.database.TaskDBHelper;
import com.example.admin.toorganize.models.Task;

public class ViewTaskActivity extends Activity {
    private TaskDBHelper taskDbHelper;

    protected EditText taskTextInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        if(extras!=null){
            String taskText = extras.getString(TaskManager.TASK_TEXT);
            final int taskId = extras.getInt(TaskManager.TASK_ID);
            taskTextInput = (EditText) findViewById(R.id.single_task_text);
            taskTextInput.setText(taskText);
            Button taskUpdateButton = (Button) findViewById(R.id.update_single_task);
            taskUpdateButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
                    updateTask(taskId);
                }
            });
        }else{
            Toast.makeText(this,"Could not load Task",Toast.LENGTH_LONG).show();
        }

    }

    private void updateTask(Integer taskId) {
        String taskText=taskTextInput.getText().toString();
        taskDbHelper = new TaskDBHelper(this);
        taskDbHelper.open();
        Task task = taskDbHelper.getTask(taskId);
        if(task!=null){
            taskDbHelper.updateTask(task,taskText);
            taskDbHelper.close();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else{
            taskDbHelper.close();
            Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_task, menu);
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
}
