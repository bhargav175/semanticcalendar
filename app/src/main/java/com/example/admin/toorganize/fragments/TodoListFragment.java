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
import com.example.admin.toorganize.activities.ViewNoteActivity;
import com.example.admin.toorganize.database.NoteDBHelper;
import com.example.admin.toorganize.models.Note;

import java.util.ArrayList;
import java.util.List;


public class TodoListFragment extends Fragment {
    private NoteDBHelper noteDbHelper;
    public static final String Tag="NoteListActivity";
    public final static String EXTRA_MESSAGE = "com.example.admin.toorganize.NoteListActivity";
    public final static String NOTE_DESCRIPTION = "description";
    public final static String NOTE_IS_DASHBOARD_HEAD = "isHead";
    public final static String NOTE_ID = "id";



    public static TodoListFragment newInstance(String param1, String param2) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public TodoListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private List<Note> getAllNotes(List<Note> NoteList) {
        noteDbHelper.open();
        Cursor cursor= noteDbHelper.fetchAllNotes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDbHelper.cursorToNote(cursor);
            NoteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDbHelper.close();
        return NoteList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<Note> noteList = new ArrayList<Note>();
        noteDbHelper = new NoteDBHelper(getActivity());
        View view=inflater.inflate(R.layout.fragment_notes_list, container, false);
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getActivity(),
                android.R.layout.simple_list_item_1, getAllNotes(noteList));
        ListView list = (ListView) view.findViewById(R.id.noteListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Note note= (Note) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ViewNoteActivity.class);
                intent.putExtra(NOTE_DESCRIPTION, note.getNoteText());
                intent.putExtra(NOTE_IS_DASHBOARD_HEAD, note.getIsDashboardHead());
                intent.putExtra(NOTE_ID,note.getId());
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment


        return view;
    }

}
