package com.bhargav.smart.smartTasks.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.activities.UpdateOneTimeTaskActivity;
import com.bhargav.smart.smartTasks.activities.UpdateRepeatingTaskActivity;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.helpers.StateBoxRepeatingTask;
import com.bhargav.smart.smartTasks.models.OrganizerItem;
import com.bhargav.smart.smartTasks.models.RepeatingOrganizerItem;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.RepeatingTaskItem;
import com.bhargav.smart.smartTasks.utils.utilFunctions;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RepeatingTaskDates extends Fragment {
    public static final String Tag = "CalendarPrint";
    private FloatingActionButton editFam;
    private RepeatingTask repeatingTaskCurrent;
    private LinearLayout repeatingItemHolder;
    private View rootView;
    Typeface font;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_repeating_task_dates, container, false);
        setHasOptionsMenu(true);
        font =Typeface.createFromAsset(getActivity().getAssets(),"fonts/RobotoCondensed-Light.ttf");
        setHasOptionsMenu(true);
        preInit();
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        new GetNote(extras.getInt(DBHelper.COLUMN_ID)).execute("");

        return rootView;
    }

    private void preInit(){
         repeatingItemHolder =(LinearLayout) rootView.findViewById(R.id.repeatingItemHolder);


    }


    private class GetRepeatingTaskItems extends AsyncTask<String, Void,Void> {

        private Context context;
        private RepeatingOrganizerItem repeatingOrganizerItem;


        public GetRepeatingTaskItems(Context context,RepeatingOrganizerItem repeatingOrganizerItem){
            this.context=context; this.repeatingOrganizerItem = repeatingOrganizerItem;
        }

        @Override
        protected Void doInBackground(String... params) {

            this.repeatingOrganizerItem = RepeatingOrganizerItem.getRepeatingTaskItemsInToRepeatingOrganizerItem(context, repeatingOrganizerItem);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Calendar start =(Calendar) repeatingTaskCurrent.getStartDate().clone();
                    Calendar end = (Calendar) repeatingTaskCurrent.getEndDate().clone();
                    Calendar temp = (Calendar) start.clone();

                    while(temp.compareTo(end)<=0){

                        if(repeatingOrganizerItem.calendarRepeatingTaskItemMap.containsKey(temp)){
                            View view = View.inflate(getActivity(),R.layout.simple_reminder_state_box, null);
                            TextView tv= (TextView) view.findViewById(R.id.date);
                            StateBoxRepeatingTask state= (StateBoxRepeatingTask) view.findViewById(R.id.state);
                            RepeatingTaskItem repeatingTaskItem = repeatingOrganizerItem.calendarRepeatingTaskItemMap.get(temp);
                            Calendar c =(Calendar) temp.clone();
                            state.init(context,repeatingTaskItem,repeatingTaskCurrent,c);
                            tv.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(temp.getTime()));
                            repeatingItemHolder.addView(view);
                        }
                        else{
                            View view = View.inflate(getActivity(),R.layout.simple_reminder_nodue_box, null);
                            TextView tv= (TextView) view.findViewById(R.id.date);
                            tv.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(temp.getTime()));
                            repeatingItemHolder.addView(view);
                        }


                        temp.add(Calendar.DATE,1);




                    }

//
//                    for(Calendar cal :repeatingOrganizerItem.calendarRepeatingTaskItemMap.keySet()){
//                        View view = View.inflate(getActivity(),R.layout.simple_reminder_state_box, null);
//                        TextView tv= (TextView) view.findViewById(R.id.date);
//                        StateBoxRepeatingTask state= (StateBoxRepeatingTask) view.findViewById(R.id.state);
//                        RepeatingTaskItem repeatingTaskItem = repeatingOrganizerItem.calendarRepeatingTaskItemMap.get(cal);
//                        Calendar c =(Calendar) cal.clone();
//                        state.init(context,repeatingTaskItem,repeatingTaskCurrent,c);
//                        tv.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(cal.getTime()));
//                        repeatingItemHolder.addView(view);
//                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
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


                    if (repeatingTaskCurrent != null) {
                        RepeatingOrganizerItem repeatingOrganizerItem = RepeatingOrganizerItem.castToRepeatingOrganizerItem(getActivity(),repeatingTaskCurrent);
                        if(repeatingTaskCurrent.getStartDate()!=null &&repeatingTaskCurrent.getEndDate()!=null && repeatingTaskCurrent.getDueTime()!=null){
                            new GetRepeatingTaskItems(getActivity(),repeatingOrganizerItem).execute("");
                        }else{
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    View view = View.inflate(getActivity(),R.layout.no_due_dates_repeating, null);
                                    repeatingItemHolder.addView(view);
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
