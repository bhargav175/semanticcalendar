package com.example.admin.toorganize.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.toorganize.R;
import com.example.admin.toorganize.database.NoteDBHelper;
import com.example.admin.toorganize.fragments.NoteListFragment;
import com.example.admin.toorganize.models.Note;

public class ViewToDoActivity extends Activity {
    private NoteDBHelper noteDBHelper;
    public final static String NOTE_DESCRIPTION = "description";
    public final static String NOTE_ISHEAD = "isHead";
    public final static String NOTE_ID = "id";
    protected EditText noteTextInput;
    protected CheckBox noteIsDashboardHead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        if(extras!=null){
            String noteText = extras.getString(NoteListFragment.NOTE_DESCRIPTION);
            final int noteId = extras.getInt(NoteListFragment.NOTE_ID);
            noteTextInput = (EditText) findViewById(R.id.noteText);
            noteIsDashboardHead = (CheckBox) findViewById(R.id.isDashboardHead);
            noteTextInput.setText(noteText);
            noteIsDashboardHead.setChecked(extras.getBoolean(NoteListFragment.NOTE_IS_DASHBOARD_HEAD));


            Button noteUpdateButton = (Button) findViewById(R.id.update_note_btn);
            noteUpdateButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
                    updateNote(noteId);
                }
            });
        }else{
            Toast.makeText(this,"Could not load note",Toast.LENGTH_LONG).show();
        }

    }

    private void updateNote(Integer noteId) {
        String noteText=noteTextInput.getText().toString();
        Boolean isDashboardHead=noteIsDashboardHead.isChecked();
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        Note note = noteDBHelper.getNote(noteId);
        if(note!=null){
            noteDBHelper.updateNote(note, noteText, isDashboardHead);
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
        getMenuInflater().inflate(R.menu.write_todo, menu);
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
}
