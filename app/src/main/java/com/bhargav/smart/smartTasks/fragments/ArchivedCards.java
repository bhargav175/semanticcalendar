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
import android.widget.ListView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.docket.R;
import com.bhargav.smart.smartTasks.activities.UpdateCheckListActivity;
import com.bhargav.smart.smartTasks.activities.UpdateHabitActivity;
import com.bhargav.smart.smartTasks.activities.UpdateNoteActivity;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.OrganizerItem;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.ArrayList;
import java.util.List;


public class ArchivedCards extends Fragment {
    public static final String Tag = "CalendarPrint";
    private List<OrganizerItem> organizerItems;
    private ListView listView;
    private ArrayAdapter<OrganizerItem> arrayAdapter;
    Typeface font;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.archives_list_layout, container, false);
        setHasOptionsMenu(true);
        listView = (ListView) rootView.findViewById(R.id.listView);
        font =Typeface.createFromAsset(getActivity().getAssets(),"fonts/RobotoCondensed-Light.ttf");
        setHasOptionsMenu(true);
        preInit();
        new GetOrganizerItems(getActivity()).execute("");
       return rootView;
    }

    private void init(){
        arrayAdapter = new ArrayAdapter<OrganizerItem>(getActivity(),R.layout.simple_list_item,R.id.text1,organizerItems){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.text1);
                cardText1.setText(utilFunctions.toCamelCase(organizerItems.get(position).getItemText()));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrganizerItem organizerItem = organizerItems.get(position);
                        if(organizerItem.getType().equals("NOTE")){
                            Intent intent = new Intent(getActivity(), UpdateNoteActivity.class);
                            intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                            startActivity(intent);
                        }else if(organizerItem.getType().equals("HABIT")){
                            Intent intent = new Intent(getActivity(), UpdateHabitActivity.class);
                            intent.putExtra(DBHelper.COLUMN_ID,organizerItem.getId());
                            startActivity(intent);
                        }else if(organizerItem.getType().equals("CHECKLIST")){
                            Intent intent = new Intent(getActivity(), UpdateCheckListActivity.class);
                            intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                            startActivity(intent);
                        }
                    }
                });
                cardText1.setTypeface(font);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
    };
    private void preInit(){
        organizerItems = new ArrayList<OrganizerItem>();


    }


    private class GetOrganizerItems extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetOrganizerItems(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            organizerItems = OrganizerItem.getAllArchivedItems(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    init();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
