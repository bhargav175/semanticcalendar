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
import android.widget.ListView;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.Note;
import com.bhargav.smart.smartTasks.models.OrganizerItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TodayFragment extends Fragment {
    public static final String Tag = "CalendarPrint";
    private List<Note> notes;
    private List<OrganizerItem> organizerItems;
    private ListView lv;
    private ArrayAdapter<OrganizerItem> noteAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        lv = (ListView) rootView.findViewById(R.id.listView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        notes = new ArrayList<Note>();
        organizerItems = new ArrayList<OrganizerItem>();
        new GetDueNotes(getActivity(),Calendar.getInstance()).execute("");

    }

    private void afterGet(){
        noteAdapter = new ArrayAdapter<OrganizerItem>(getActivity(),R.layout.timeline_item,R.id.text1,organizerItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView mainText = (TextView) view.findViewById(R.id.text1);
                TextView description = (TextView) view.findViewById(R.id.text2);
                TextView dueDate = (TextView) view.findViewById(R.id.time);
                mainText.setText(organizerItems.get(position).getItemText());
                description.setText(organizerItems.get(position).getSecondaryText());
                dueDate.setText(new SimpleDateFormat("HH:mm").format(organizerItems.get(position).getDueTime().getTime()));
                return view;
            }
        };
        lv.setAdapter(noteAdapter);
    }

    private class GetDueNotes extends AsyncTask<String, Void,Void> {

        private Context context;
        private Calendar calendar;

        public GetDueNotes(Context context,Calendar calendar){
            this.context=context; this.calendar = (Calendar) calendar.clone();
        }

        @Override
        protected Void doInBackground(String... params) {
            notes =Note.getAllUnArchivedNotesByDueDate(context, calendar);
            organizerItems = OrganizerItem.getAllUnArchivedItemsByDueDate(context,calendar);
            //No Sandbox
            //tags.add(new Tag("SandBox"));
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
