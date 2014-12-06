package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.OneTimeTask;
import com.bhargav.smart.smartTasks.models.RepeatingTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 12-10-2014.
 */
public class StateBox extends StateInterface {

    private OneTimeTask oneTimeTask;
    private Integer oneTimeTaskId;
    private Boolean updatedBool = false;

    public StateBox(Context context) {
        super(context);

    }

    public StateBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public StateBox(Context context,Integer oneTimeTaskId){
        super(context);

    }

    public StateBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    void getStateFromDB() {
        new GetOneTimeTask(oneTimeTaskId,context).execute("");
    }



    @Override
    public void init(Context context, Integer itemId) {
        this.oneTimeTaskId = itemId;
        if(!updatedBool){
            getStateFromDB();
        }
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
        if(oneTimeTask!=null){
            new SaveOneTypeTask().execute("");
        }

    }



    private class SaveOneTypeTask extends AsyncTask<String, Void, Void> {



        public SaveOneTypeTask() {

        }

        @Override
        protected Void doInBackground(String... params) {
            oneTimeTask.setTaskItemState(state);
            OneTimeTask.saveState(context,oneTimeTask);
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

    private class GetOneTimeTask extends AsyncTask<String, Void, Void> {

        private int id;
        private Context context;

        public GetOneTimeTask(int id, Context context) {
            this.id = id; this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {

            oneTimeTask = OneTimeTask.getNote(id, context);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if(oneTimeTask!=null){
                updatedBool = true;
                state = oneTimeTask.getTaskItemState();
                updateText();
                updateColor();
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
