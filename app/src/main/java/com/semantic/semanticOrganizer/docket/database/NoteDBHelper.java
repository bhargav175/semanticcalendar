package com.semantic.semanticOrganizer.docket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Note;
import com.semantic.semanticOrganizer.docket.models.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public int updateNote(Note note) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.NOTE_TITLE, note.getNoteTitle());
        values.put(DBHelper.NOTE_DESCRIPTION, note.getNoteDescription());
        values.put(DBHelper.NOTE_TAG, note.getTag());
        values.put(DBHelper.NOTE_IS_ARCHIVED,note.getIsArchived());
        if(note.getDueTime()!=null){
            values.put(DBHelper.NOTE_REQUEST_ID,note.getRemainderId());
            values.put(DBHelper.COLUMN_DUE_TIME,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(note.getDueTime().getTime()));
        }else{

            values.putNull(DBHelper.NOTE_REQUEST_ID);
            values.putNull(DBHelper.COLUMN_DUE_TIME);
        }

        // updating row
        database.update(NOTES_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        Log.d( TAG,"Note updated" + note.getNoteTitle());


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
        note.setNoteDescription(cursor.getString(2));
        note.setIsArchived(cursor.getInt(3)>0);
        note.setCreatedTime(cursor.getString(4));
                if (!cursor.isNull(5)){
            note.setTag(cursor.getInt(5));
        }else{
                    note.setTag(null);
                }
        if (!cursor.isNull(6)) {
            note.setRemainderId(cursor.getInt(6));
        }
        else{
            note.setRemainderId(null);
        }
        if(cursor.getString(7)!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(7)));
                note.setDueTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

    public Note saveNoteWithTag(Note note, Tag currentTagInView) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevNoteId(NOTES_TABLE)) + 1;
        values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
        values.put(DBHelper.NOTE_TAG, currentTagInView.getTagId());
        values.put(DBHelper.NOTE_TITLE, note.getNoteTitle());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        database.insert(NOTES_TABLE, null, values);

        Log.d( TAG,"Note saved" + note.getNoteTitle());
        return getNote(id);
    }

    public Cursor fetchAllUnarchivedNotesWithDueDate(Calendar calendar) {
        return null;


    }

    public Cursor fetchAllArchivedNotes() {
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_IS_ARCHIVED +" = 1 " , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedNotesByDueDate(Calendar calendar) {
        Calendar startDate = (Calendar) calendar.clone();
        startDate.set(Calendar.HOUR_OF_DAY,0);
        startDate.set(Calendar.MINUTE,0);
        startDate.set(Calendar.SECOND,0);
        Calendar endDate = (Calendar) calendar.clone();
        endDate.set(Calendar.HOUR_OF_DAY,23);
        endDate.set(Calendar.MINUTE,59);
        endDate.set(Calendar.SECOND,59);
        return database.query(NOTES_TABLE, null, DBHelper.NOTE_IS_ARCHIVED +" = 0 AND " + DBHelper.COLUMN_DUE_TIME  + " between ? AND ?" , new String[]{new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate.getTime()),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate.getTime())}, null, null, null);

    }
}
