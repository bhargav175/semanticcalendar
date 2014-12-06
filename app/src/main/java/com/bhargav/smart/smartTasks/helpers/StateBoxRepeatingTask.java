package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.RepeatingTaskItem;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bhargav.smart.smartTasks.utils.utilFunctions.BLog;

/**
 * Created by Admin on 12-10-2014.
 */
public class StateBoxRepeatingTask extends StateInterface {

    private RepeatingTaskItem repeatingTaskItem;
    private RepeatingTask repeatingTask;
    private Calendar calendar;
    private Integer repeatingTaskItemId;
    public StateBoxRepeatingTask(Context context) {
        super(context);
    }

    public StateBoxRepeatingTask(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void getStateFromDB() {

    }


    @Override
    public void init(Context context, Integer itemId) {


    }


    public void init(Context context, RepeatingTaskItem repeatingTaskItem, RepeatingTask repeatingTask, Calendar cal) {
        this.context = context;
        this.repeatingTask =repeatingTask;
        this.calendar = (Calendar) cal.clone();

        if(repeatingTaskItem == null){

            new SaveRepeatingTaskItem().execute("");
        }
        else{
            this.repeatingTaskItem = repeatingTaskItem;
           moreInit();
        }
    }
    public void moreInit(){
        this.repeatingTaskItemId = repeatingTaskItem.getId();
        this.state = repeatingTaskItem.getHabitItemState();
        updateText();
        updateColor();
        BLog(String.valueOf(repeatingTaskItem.getId()) + "" + new SimpleDateFormat(utilFunctions.dateFormat).format(repeatingTaskItem.getCurrentDate().getTime()));

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                changeState();
                updateText();
                updateColor();
                commitState();
            }
        });

    }

    protected void commitState(){
        if(repeatingTaskItem!=null){
            new SaveRepeatingTask().execute("");
        }

    }


    private class SaveRepeatingTask extends AsyncTask<String, Void, Void> {



        public SaveRepeatingTask() {

        }

        @Override
        protected Void doInBackground(String... params) {
            repeatingTaskItem.setHabitItemState(state);
            repeatingTaskItem.saveState(context,repeatingTaskItem);
            return null;
        }

        @Override
        protected void  onPostExecute(Void v) {



        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }



    private class SaveRepeatingTaskItem extends AsyncTask<String, Void, Void> {



        public SaveRepeatingTaskItem() {

        }

        @Override
        protected Void doInBackground(String... params) {
            repeatingTaskItem = RepeatingTaskItem.saveHabitItem(context,repeatingTask,calendar);

            return null;
        }

        @Override
        protected void  onPostExecute(Void v) {
            if(repeatingTaskItem!=null){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        moreInit();
                    }
                });

            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }



}
