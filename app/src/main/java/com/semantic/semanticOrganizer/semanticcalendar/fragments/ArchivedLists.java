package com.semantic.semanticOrganizer.semanticcalendar.fragments;

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

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.activities.UpdateTagActivity;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;
import com.semantic.semanticOrganizer.semanticcalendar.utils.utilFunctions;

import java.util.ArrayList;
import java.util.List;


public class ArchivedLists extends Fragment {
    public static final String Tag = "CalendarPrint";
    private List<Tag> tags;
    private ListView listView;
    private ArrayAdapter<Tag> arrayAdapter;
    Typeface font;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.archives_list_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        font =Typeface.createFromAsset(getActivity().getAssets(),"fonts/RobotoCondensed-Light.ttf");
        setHasOptionsMenu(true);
        preInit();
        new GetTags(getActivity()).execute("");
        return rootView;
    }
    private void init(){
        arrayAdapter = new ArrayAdapter<com.semantic.semanticOrganizer.semanticcalendar.models.Tag>(getActivity(),R.layout.simple_list_item,R.id.text1,tags){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.text1);
                cardText1.setText(utilFunctions.toCamelCase(tags.get(position).getTagText()));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UpdateTagActivity.class);
                        intent.putExtra(DBHelper.COLUMN_ID, tags.get(position).getTagId());
                        startActivity(intent);
                    }
                });
                cardText1.setTypeface(font);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
    };
    private void preInit(){
        tags = new ArrayList<com.semantic.semanticOrganizer.semanticcalendar.models.Tag>();


    }


    private class GetTags extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetTags(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            tags = com.semantic.semanticOrganizer.semanticcalendar.models.Tag.getAllArchivedTags(context);
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
