package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;

public class Write_Note_Activity extends Activity implements View.OnClickListener{
    private NoteDBHelper noteDBHelper;
    private EditText noteText;
    private Button saveNoteBTn;
    protected CheckBox noteIsDashboardHead;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        initUi();
        setListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.write_note, menu);
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
    private void initUi() {
        noteText = (EditText) findViewById(R.id.noteText);
        noteDBHelper = new NoteDBHelper(this);
        noteDBHelper.open();
        noteIsDashboardHead = (CheckBox) findViewById(R.id.isDashboardHead);

        saveNoteBTn = (Button) findViewById(R.id.save_note_btn);
    }
    private void setListeners() {
        saveNoteBTn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save_note_btn:
                Note note = new Note();
                note.setNoteText(noteText.getText().toString());
                noteDBHelper.open();
                noteDBHelper.saveNote(note);
                Intent intent = new Intent(this, HomeActivity.class);
                noteDBHelper.close();

                startActivity(intent);
                break;
        }
    }
}
