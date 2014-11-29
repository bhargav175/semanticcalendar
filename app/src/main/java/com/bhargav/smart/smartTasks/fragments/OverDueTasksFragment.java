package com.bhargav.smart.smartTasks.fragments;

import android.content.Context;
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

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.helpers.StateBox;
import com.bhargav.smart.smartTasks.models.OrganizerItem;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bhargav.smart.smartTasks.utils.utilFunctions.BLog;


public class OverDueTasksFragment extends Fragment {
    public static final String Tag = "CalendarPrint";
    private List<OrganizerItem> organizerItemsToday, organizerItemsWeek, organizerItemsMonth;
    private ListView lv;
    private ArrayAdapter<OrganizerItem> todayTaskAdapter,weekTaskAdapter,monthTaskAdapter;
    private LayoutInflater inflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_due_tasks, container, false);
        lv = (ListView) rootView.findViewById(R.id.todayListView);
        this.inflater = inflater;
        return rootView;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        organizerItemsMonth = new ArrayList<OrganizerItem>();

        new GetDueNotes(getActivity(), Calendar.getInstance()).execute("");

    }

    private void afterGet(){
        BLog((String.valueOf(organizerItemsToday.size())));
        todayTaskAdapter = new ArrayAdapter<OrganizerItem>(getActivity(),R.layout.timeline_item,R.id.text1, organizerItemsMonth){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView mainText = (TextView) view.findViewById(R.id.text1);
                TextView description = (TextView) view.findViewById(R.id.text2);
                TextView dueDate = (TextView) view.findViewById(R.id.time);
                LinearLayout stateLayout = (LinearLayout) view.findViewById(R.id.stateBoxLayout);
                mainText.setText(organizerItemsMonth.get(position).getItemText());
                description.setText(organizerItemsMonth.get(position).getSecondaryText());
                dueDate.setText(new SimpleDateFormat(utilFunctions.dateFormat).format(organizerItemsMonth.get(position).getDueTime().getTime()));

                if(organizerItemsMonth.get(position).getType().equals("NOTE")){
                    StateBox stateView = (StateBox) inflater.inflate(R.layout.simple_state_item, stateLayout, false);
                    stateView.init(getActivity(), organizerItemsMonth.get(position).getId());
                    stateLayout.addView(stateView);

                }




                return view;
            }
        };
        lv.setAdapter(todayTaskAdapter);
    }

    private class GetDueNotes extends AsyncTask<String, Void,Void> {

        private Context context;
        private Calendar calendar;

        public GetDueNotes(Context context,Calendar calendar){
            this.context=context; this.calendar = (Calendar) calendar.clone();
        }

        @Override
        protected Void doInBackground(String... params) {
            organizerItemsMonth = OrganizerItem.getAllUnArchivedItemsByMonth(context, calendar);


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
