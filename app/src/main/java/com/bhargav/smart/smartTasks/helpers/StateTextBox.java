package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.models.OneTimeTask;

/**
 * Created by Admin on 12-10-2014.
 */
public class StateTextBox extends StateTextInterface {

    private OneTimeTask oneTimeTask;
    private Integer oneTimeTaskId;
    private Boolean updatedBool = false;

    public StateTextBox(Context context) {
        super(context);

    }

    public StateTextBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public StateTextBox(Context context, Integer oneTimeTaskId){
        super(context);

    }

    public StateTextBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    void getStateFromDB() {
        new GetOneTimeTask(oneTimeTaskId,context).execute("");
    }



    @Override
    public void init(Context context, Integer itemId) {
        this.oneTimeTaskId = itemId;
        this.setClickable(false);
        if(!updatedBool){
            getStateFromDB();
        }

    }



    private class SaveOneTypeTask extends AsyncTask<String, Void, Void> {

        private int id;

        public SaveOneTypeTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(String... params) {
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
                  updatedBool = true;
                  updateText();
                  updateColor();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
