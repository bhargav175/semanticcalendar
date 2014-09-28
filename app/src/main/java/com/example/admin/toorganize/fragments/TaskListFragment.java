package com.example.admin.toorganize.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.admin.toorganize.R;
import com.example.admin.toorganize.activities.ViewTaskActivity;
import com.example.admin.toorganize.database.TaskDBHelper;
import com.example.admin.toorganize.models.Task;

import java.util.ArrayList;
import java.util.List;


public class TaskListFragment extends Fragment {

    private TaskDBHelper taskDbHelper;
    public static final String Tag="TaskManager";
    public final static String EXTRA_MESSAGE = "com.example.admin.toorganize.TaskManager";
    public final static String TASK_TEXT = "task_text";
    public final static String TASK_ID = "task_id";



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskListFragment newInstance(String param1, String param2) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private List<Task> getAllTasks(List<Task> taskList) {
        taskDbHelper.open();
        Cursor cursor= taskDbHelper.fetchAllTasks();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = taskDbHelper.cursorToTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        taskDbHelper.close();
        return taskList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<Task> taskList = new ArrayList<Task>();
        taskDbHelper = new TaskDBHelper(getActivity());
        View view=inflater.inflate(R.layout.fragment_task_list, container, false);
        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(getActivity(),
                android.R.layout.simple_list_item_1, getAllTasks(taskList));
        ListView list = (ListView) view.findViewById(R.id.taskListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Task task= (Task) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ViewTaskActivity.class);
                intent.putExtra(TASK_TEXT, task.getText());
                intent.putExtra(TASK_ID,(int) task.getId());
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment


        return view;
    }

}
