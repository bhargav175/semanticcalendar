package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;

import java.util.ArrayList;
import java.util.List;

public class ListNotesActivity extends Activity {
    private NoteDBHelper noteDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        final List<Note> noteList = new ArrayList<Note>();

        noteDBHelper = new NoteDBHelper(this);
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this,
                R.layout.tag_list_item,R.id.text1, getAllNotes(noteList)){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);

                text1.setText(noteList.get(position).getNoteText());
                text2.setText(noteList.get(position).getCreatedTime());
                return view;
            }

        };
        ListView list = (ListView) findViewById(R.id.noteListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Note note= (Note) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
                intent.putExtra(DBHelper.NOTE_DESCRIPTION, note.getNoteText());
                intent.putExtra(DBHelper.COLUMN_ID,note.getId());
                startActivity(intent);
            }
        });
        list.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Note> getAllNotes(List<Note> noteList) {
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllNotes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note tag = noteDBHelper.cursorToNote(cursor);
            noteList.add(tag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;
    }
}
