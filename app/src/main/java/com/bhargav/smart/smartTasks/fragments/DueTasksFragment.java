package com.bhargav.smart.smartTasks.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.activities.ViewOneTimeTaskActivity;
import com.bhargav.smart.smartTasks.activities.ViewRepeatingTaskActivity;
import com.bhargav.smart.smartTasks.adapters.SeparatedListAdapter;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.helpers.StateBox;
import com.bhargav.smart.smartTasks.helpers.StateTextBox;
import com.bhargav.smart.smartTasks.models.OrganizerItem;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bhargav.smart.smartTasks.utils.utilFunctions.BLog;


public class DueTasksFragment extends Fragment {
    public static final String Tag = "CalendarPrint";
    private List<OrganizerItem> organizerItemsToday, organizerItemsFlex, organizerItemsMonth;
    private ListView lv;
    private ArrayAdapter<OrganizerItem> todayTaskAdapter,flexTaskAdapter,monthTaskAdapter;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_due_tasks, container, false);
        lv = (ListView) rootView.findViewById(R.id.todayListView);
        lv.setScrollingCacheEnabled(false);
        this.inflater = inflater;
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        organizerItemsToday = new ArrayList<OrganizerItem>();
        organizerItemsFlex = new ArrayList<OrganizerItem>();
        new GetDueNotes(getActivity(),Calendar.getInstance()).execute("");

    }
    static class ViewHolderItem {

        TextView textViewItem;

    }


    private void afterGet(){

        // create our list and custom adapter
       BLog((String.valueOf(organizerItemsToday.size())));
        todayTaskAdapter = new ArrayAdapter<OrganizerItem>(getActivity(),R.layout.timeline_item_plain_text,R.id.text1, organizerItemsToday){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null) {
                    convertView = inflater.inflate(R.layout.timeline_item_plain_text, parent, false);

                }else{

                }

                 View view = super.getView(position, convertView, parent);
                TextView mainText = (TextView) view.findViewById(R.id.text1);
                TextView description = (TextView) view.findViewById(R.id.text2);
                TextView dueTime = (TextView) view.findViewById(R.id.time);
                TextView dueDate = (TextView) view.findViewById(R.id.date);
                mainText.setText(organizerItemsToday.get(position).getItemText());
                description.setText(organizerItemsToday.get(position).getSecondaryText());
                dueTime.setText(new SimpleDateFormat(utilFunctions.timeFormat).format(organizerItemsToday.get(position).getDueTime().getTime()));
                StateTextBox stateView = (StateTextBox) view.findViewById(R.id.state);

                if(organizerItemsToday.get(position).getType().equals("NOTE")){
                        stateView.init(getActivity(), organizerItemsToday.get(position).getId());
                        dueDate.setText(new SimpleDateFormat(utilFunctions.weekDayMonthFormat).format(organizerItemsToday.get(position).getDueTime().getTime()));
                }

                if(organizerItemsToday.get(position).getType().equals("HABIT")) {
                    dueDate.setVisibility(View.GONE);
                }
                else{
                    dueDate.setVisibility(View.VISIBLE);
                }
                return view;
            }
        };
        flexTaskAdapter = new ArrayAdapter<OrganizerItem>(getActivity(),R.layout.timeline_item_plain_text,R.id.text1, organizerItemsFlex){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null) {
                    convertView = inflater.inflate(R.layout.timeline_item_plain_text, parent, false);

                }else{

                }
                View view = super.getView(position, convertView, parent);
                TextView mainText = (TextView) view.findViewById(R.id.text1);
                TextView description = (TextView) view.findViewById(R.id.text2);
                TextView dueTime = (TextView) view.findViewById(R.id.time);
                TextView dueDate = (TextView) view.findViewById(R.id.date);
                mainText.setText(organizerItemsFlex.get(position).getItemText());
                description.setText(organizerItemsFlex.get(position).getSecondaryText());
                dueTime.setText(new SimpleDateFormat(utilFunctions.timeFormat).format(organizerItemsFlex.get(position).getDueTime().getTime()));
                StateTextBox stateView = (StateTextBox) view.findViewById(R.id.state);
                if(organizerItemsFlex.get(position).getType().equals("NOTE")){
                    stateView.init(getActivity(), organizerItemsFlex.get(position).getId());
                    dueDate.setText(new SimpleDateFormat(utilFunctions.weekDayMonthFormat).format(organizerItemsFlex.get(position).getDueTime().getTime()));
         }

                if(organizerItemsFlex.get(position).getType().equals("HABIT")) {
                    dueDate.setVisibility(View.GONE);
                }
                else{
                    dueDate.setVisibility(View.VISIBLE);
                }
                return view;
            }
        };


        SeparatedListAdapter adapter = new SeparatedListAdapter(getActivity());
        adapter.addSection("Today - Fixed Tasks", todayTaskAdapter);
        adapter.addSection("Flexible Tasks", flexTaskAdapter);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((OrganizerItem) lv.getItemAtPosition(position)).getType().equals("NOTE")){
                    Intent intent = new Intent(getActivity(),ViewOneTimeTaskActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,(((OrganizerItem) lv.getItemAtPosition(position)).getId()));
                    startActivity(intent);
                }
                else if(((OrganizerItem) lv.getItemAtPosition(position)).getType().equals("HABIT")){
                    Intent intent = new Intent(getActivity(),ViewRepeatingTaskActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,(((OrganizerItem) lv.getItemAtPosition(position)).getId()));
                    startActivity(intent);
                }
            }
        });
    }

    private class GetDueNotes extends AsyncTask<String, Void,Void> {

        private Context context;
        private Calendar calendar;

        public GetDueNotes(Context context,Calendar calendar){
            this.context=context; this.calendar = (Calendar) calendar.clone();
        }

        @Override
        protected Void doInBackground(String... params) {
            organizerItemsToday = OrganizerItem.getAllUnArchivedItemsByDueDate(context,calendar);
            organizerItemsFlex = OrganizerItem.getAllUnArchivedFlexibleRepeatingItems(context, calendar);
            //No Sandbox
            //tags.add(new LOG_TAG("SandBox"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    afterGet();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



}
