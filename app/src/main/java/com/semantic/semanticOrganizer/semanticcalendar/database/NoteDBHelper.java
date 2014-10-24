package com.semantic.semanticOrganizer.semanticcalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public Cursor fetchAllUnArchivedNotes() {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_IS_ARCHIVED + "=?",  new String[] { String.valueOf(false) }, null, null, null);
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

    public int updateNote(Note note, String noteText ,Boolean isArchived, Integer noteTag, Integer requestId) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.NOTE_DESCRIPTION, noteText);
        values.put(DBHelper.NOTE_TAG, noteTag);
        values.put(DBHelper.NOTE_IS_ARCHIVED,isArchived);
        values.put(DBHelper.NOTE_REQUEST_ID,requestId);



        // updating row
        database.update(NOTES_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        Log.d( TAG,"Note updated" + noteText);


        return 0;
    }


    public void saveNote(Note note) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevNoteId(NOTES_TABLE)) + 1)));
        values.put(DBHelper.NOTE_DESCRIPTION, note.getNoteTitle());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        database.insert(NOTES_TABLE, null, values);
        Log.d( TAG,"Note saved" + note.getNoteTitle());

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
        note.setNoteTitle(cursor.getString(1));
        note.setIsArchived(cursor.getInt(2)>0);
        note.setCreatedTime(cursor.getString(3));
                if (!cursor.isNull(4)){
            note.setTag(cursor.getInt(4));
        }else{
                    note.setTag(null);
                }
        if (!cursor.isNull(5)) {
            note.setRemainderId(cursor.getInt(5));
        }
        else{
            note.setRemainderId(null);
        }
        return note;

    }

    public Cursor fetchAllNotesInTag(Tag tag) {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_TAG +  "=" + tag.getTagId(), null, null, null, null);
    }
    public Cursor fetchAllNotesSandbox() {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_TAG +  " is null" , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedNotesSandbox() {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_TAG +  " is null AND "+DBHelper.NOTE_IS_ARCHIVED +" = 0 " , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedNotesInTag(Tag tag) {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_TAG +  "= ? AND "+DBHelper.NOTE_IS_ARCHIVED + " = 0" , new String[] { String.valueOf(tag.getTagId()) }, null, null, null);


    }

    public void archiveNote(Note note) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.NOTE_IS_ARCHIVED,true);

        // updating row
        database.update(NOTES_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        Log.d( TAG,"Note archived" + note.getNoteTitle());



    }

    public void saveNoteWithTag(Note note, Tag currentTagInView) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevNoteId(NOTES_TABLE)) + 1)));
        values.put(DBHelper.NOTE_TAG, currentTagInView.getTagId());
        values.put(DBHelper.NOTE_DESCRIPTION, note.getNoteTitle());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        database.insert(NOTES_TABLE, null, values);
        Log.d( TAG,"Note saved" + note.getNoteTitle());
    }
}
