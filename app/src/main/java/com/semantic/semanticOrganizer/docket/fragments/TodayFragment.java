package com.semantic.semanticOrganizer.docket.fragments;

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

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.models.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TodayFragment extends Fragment {
    public static final String Tag = "CalendarPrint";
    private List<Note> notes;
    private ListView lv;
    private ArrayAdapter<Note> noteAdapter;


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
        new GetDueNotes(getActivity(),Calendar.getInstance()).execute("");

    }

    private void afterGet(){
        noteAdapter = new ArrayAdapter<Note>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,notes){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(notes.get(position).getNoteTitle());
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
