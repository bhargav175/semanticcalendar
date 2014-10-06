package com.semantic.semanticOrganizer.semanticcalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

/**
 * Created by Admin on 16-09-2014.
 */
public class TagDBHelper {
    private final static String TAGS_TABLE = "tags";
    private final static String TAG = "TAG-DBAdapter";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public TagDBHelper(Context context) {
        this.context = context;

    }

    public TagDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }

    public Cursor fetchAllTags() {
        return database.query(TAGS_TABLE, null, null, null, null, null, null);
    }

    public Tag getTag(int id) {
        Cursor cursor = database.query(TAGS_TABLE, new String[] {DBHelper.COLUMN_ID, DBHelper.TAGS_TABLE }, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Tag tag = new Tag(Integer.parseInt(cursor.getString(0)),
               cursor.getString(1),cursor.getString(2),cursor.getInt(3)>0,cursor.getLong(4));
        // return contact
        return tag;
    }

    public int updateTag(Tag tag, String tagText) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TAG_TITLE, tagText);

        // updating row
        return database.update(TAGS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(tag.getTagId()) });
    }

    public Tag cursorToTag(Cursor cursor) {
        Tag tag = new Tag();
        tag.setTagId(cursor.getLong(0));
        tag.setTagText(cursor.getString(1));
        return tag;
    }

    public void saveTag(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("id", (Integer.toString(Integer.parseInt(getPrevTagId(TAGS_TABLE)) + 1)));
        values.put("title", tag.getTagText());
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TAGS_TABLE, null, values);

        Toast.makeText(context,tag.getTagText(), Toast.LENGTH_LONG).show();
    }




    private String getPrevTagId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }


}
