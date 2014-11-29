package com.bhargav.smart.smartTasks.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.activities.ArchivesActivity;
import com.bhargav.smart.smartTasks.database.TaskListDBHelper;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.ArrayList;
import java.util.List;


public class ArchivedLists extends Fragment {
    public static final String LOG_TAG = "CalendarPrint";
    private List<TaskList> taskLists;
    private ListView listView;
    private ArrayAdapter<TaskList> arrayAdapter;
    Typeface font;
    private final int UNARCHIVE=0;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.archives_list_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        font =Typeface.createFromAsset(getActivity().getAssets(),"fonts/RobotoCondensed-Light.ttf");
        setHasOptionsMenu(true);
        registerForContextMenu(listView);
        listView.setOnCreateContextMenuListener(this);

        preInit();
        new GetTags(getActivity()).execute("");
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.listView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            TaskList obj = (TaskList) lv.getItemAtPosition(acmi.position);
            menu.setHeaderTitle(obj.getTagText());
            menu.add(0,UNARCHIVE,0,"Unarchive");
        }

    }
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {


                switch (item.getItemId()) {
                    case UNARCHIVE:
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        Log.d("ArchivedList", "removing item pos=" + info.position);
                        TaskList obj = (TaskList) listView.getItemAtPosition(info.position);
                        obj.setIsArchived(false);
                        new UnArchiveTag(getActivity(),obj).execute("");
                        taskLists.remove((TaskList) listView.getItemAtPosition(info.position));
                        arrayAdapter.notifyDataSetChanged();
                        return true;
                    default:

                        return true;
                }

    }




    private void init(){
        arrayAdapter = new ArrayAdapter<TaskList>(getActivity(),R.layout.simple_list_item,R.id.text1, taskLists){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.text1);
                cardText1.setText(utilFunctions.toCamelCase(taskLists.get(position).getTagText()));
                cardText1.setTypeface(font);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
    };
    private void preInit(){
        taskLists = new ArrayList<TaskList>();


    }


    private class GetTags extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetTags(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            taskLists = TaskList.getAllArchivedTags(context);
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


    private class UnArchiveTag extends AsyncTask<String, Void,TaskList> {

        private Context context;
        private TaskList taskList;
        public UnArchiveTag(Context context, TaskList taskList){
            this.context=context;this.taskList = taskList;
        }

        @Override
        protected TaskList doInBackground(String... params) {
            TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
            try{
                taskListDBHelper.open();
                taskListDBHelper.updateTag(taskList);
                taskListDBHelper.close();
                return taskList;
            }catch (Exception e){
                return null;
            }


        }

        @Override
        protected void onPostExecute(final TaskList t) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                        if(t!=null){
                            ((ArchivesActivity)getActivity()).addTagToDrawer(t);

                            Toast.makeText(context, t.getTagText() +"is nArchived",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "There was an error",Toast.LENGTH_SHORT).show();

                        }

                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}
