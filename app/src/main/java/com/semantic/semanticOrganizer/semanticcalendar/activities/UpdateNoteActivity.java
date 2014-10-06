package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.NoteListFragment;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class UpdateNoteActivity extends Activity implements View.OnClickListener {
    private NoteDBHelper noteDBHelper;
    private EditText noteText;
    private Spinner tag;
    Integer noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        if(noteId!=null){

                            updateNote(noteId);
                            Intent lIntent = new Intent(getApplicationContext(), LandingActivity.class);
                            startActivity(lIntent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_LONG).show();

                        }

                        finish();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Cancel"
                        finish();
                    }
                });
        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        // END_INCLUDE (inflate_set_custom_view)
        setContentView(R.layout.activity_add_note);
        Intent intent = getIntent();

        Bundle extras= intent.getExtras();
        if(extras!=null){
            String noteTextString = extras.getString(DBHelper.NOTE_DESCRIPTION);
            noteId = extras.getInt(DBHelper.COLUMN_ID);
            noteText = (EditText) findViewById(R.id.noteText);
            noteText.setText(noteTextString);
        }else{
            Toast.makeText(this, "Could not load note", Toast.LENGTH_LONG).show();
        }


        initUi();
    }
    private void initUi() {
        noteText = (EditText) findViewById(R.id.noteText);
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        tag = (Spinner) findViewById(R.id.selectSpinner);
        List<Tag> tags = new ArrayList<Tag>();
        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                android.R.layout.simple_spinner_item, Tag.getAllTasks(getApplicationContext(),tags));
        tag.setAdapter(adapter);
    }
    private void setListeners() {


    }

    private void updateNote(Integer noteId) {
        String noteTextString=noteText.getText().toString();
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        Note note = noteDBHelper.getNote(noteId);
        if(note!=null){
            noteDBHelper.updateNote(note, noteTextString);
            noteDBHelper.close();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else{
            noteDBHelper.close();
            Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note, menu);
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
    @Override
    protected void onPause()
    {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onClick(View v) {

    }
}
