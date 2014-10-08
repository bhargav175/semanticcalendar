package com.semantic.semanticOrganizer.semanticcalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

/**
 * Created by Admin on 16-09-2014.
 */
public class NoteDBHelper {

    private final static String NOTES_TABLE = "notes";
    private final static String TAG = "Notesave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public NoteDBHelper(Context context) {
        this.context = context;

    }

    public NoteDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }


    public Cursor fetchAllNotes() {
        return database.query(NOTES_TABLE, null, null, null, null, null, null);
    }

    public Note getNote(int id) {
        Cursor cursor = database.query(NOTES_TABLE, null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note =cursorToNote(cursor);
        // return contact
        return note;
    }

    public int updateNote(Note note, String noteText ,Integer noteTag) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.NOTE_DESCRIPTION, noteText);
        values.put(DBHelper.NOTE_TAG, noteTag);



        // updating row
        database.update(NOTES_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        Toast.makeText(context,"Note "+ noteText+" updated", Toast.LENGTH_LONG).show();


        return 0;
    }


    public void saveNote(Note note) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevNoteId(NOTES_TABLE)) + 1)));
        values.put(DBHelper.NOTE_DESCRIPTION, note.getNoteText());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(NOTES_TABLE, null, values);
        Toast.makeText(context,"Note "+ note.getNoteText()+" saved", Toast.LENGTH_LONG).show();

    }
    private String getPrevNoteId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getInt(0));
        note.setNoteText(cursor.getString(1));
        note.setIsArchived(cursor.getInt(2)>0);
        note.setCreatedTime(cursor.getString(3));
                if (!cursor.isNull(4)){
            note.setTag(cursor.getInt(4));
        }

        return note;

    }

    public Cursor fetchAllNotesInTag(Tag tag) {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_TAG +  "=" + tag.getTagId(), null, null, null, null);
    }
    public Cursor fetchAllNotesSandbox() {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_TAG +  " is null" , null, null, null, null);
    }
}
