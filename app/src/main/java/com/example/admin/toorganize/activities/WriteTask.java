package com.example.admin.toorganize.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
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
import com.example.admin.toorganize.fragments.TimeDialogFragment;
import com.example.admin.toorganize.models.Task;

public class WriteTask extends Activity implements View.OnClickListener {
    private TaskDBHelper taskDbHelper;
    private EditText taskText,dateEditText;
    private Button saveTaskBtn,setDateBtn;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_task);
        initUi();
        setListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.write_task, menu);
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
    private void initUi() {
        taskText = (EditText) findViewById(R.id.taskText);
        setDateBtn=(Button)findViewById(R.id.set_date_button_task);
        dateEditText=(EditText)findViewById(R.id.task_edit_text_dt);
        taskDbHelper = new TaskDBHelper(this);
        taskDbHelper.open();
        saveTaskBtn = (Button) findViewById(R.id.save_task_btn);
    }
    private void setListeners() {
        saveTaskBtn.setOnClickListener(this);
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DialogFragment newFragment = new TimeDialogFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
    }

    public void setDateEditText(int hourOfDay,int minute){
        dateEditText.setText(hourOfDay+"::"+minute);
        Toast.makeText(this,hourOfDay+"::"+minute,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save_task_btn:
                Task task = new Task();
                task.setText(taskText.getText().toString());
                taskDbHelper.open();

                taskDbHelper.saveTask(task);
//                alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//                alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                        AlarmManager.INTERVAL_HALF_HOUR,
//                        AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
