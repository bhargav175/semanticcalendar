package com.example.admin.toorganize.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.toorganize.helpers.DBHelper;
import com.example.admin.toorganize.models.Note;

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
        Cursor cursor = database.query(NOTES_TABLE, new String[] {DBHelper.COLUMN_ID, DBHelper.NOTE_DESCRIPTION, DBHelper.COLUMN_CREATED_TIME, DBHelper.NOTE_IS_DASHBOARD_HEAD }, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note =cursorToNote(cursor);
        // return contact
        return note;
    }

    public int updateNote(Note note, String noteText, Boolean isDashboardHead) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.NOTE_DESCRIPTION, noteText);
        if(isDashboardHead){
            unsetDashboardHeadAllNotes();
        }
        int f = isDashboardHead?1:0;
        values.put(DBHelper.NOTE_IS_DASHBOARD_HEAD, f);

        // updating row
        database.update(NOTES_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        if(f>0){
            Toast.makeText(context,"Updated and Set as Dashboard Head", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(context,"Updated", Toast.LENGTH_LONG).show();

        }

        return 0;
    }

    private void unsetDashboardHeadAllNotes() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.NOTE_IS_DASHBOARD_HEAD, 0);
        database.update(NOTES_TABLE, cv, null,null);
    }

    public void saveNote(Note note) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevNoteId(NOTES_TABLE)) + 1)));
        values.put(DBHelper.NOTE_DESCRIPTION, note.getNoteText());

        int f =note.getIsDashboardHead()?1:0;

        values.put(DBHelper.NOTE_IS_DASHBOARD_HEAD,f );
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(NOTES_TABLE, null, values);
        if(f>0){
            Toast.makeText(context,"Saved and Set as Dashboard Head", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(context,"Saved", Toast.LENGTH_LONG).show();

        }
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
        note.setIsDashboardHead(cursor.getInt(3)>0);
        return note;

    }
}
