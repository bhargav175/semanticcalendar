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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public Cursor fetchAllUnArchivedTags() {
        return database.query(TAGS_TABLE, null,DBHelper.TAG_IS_ARCHIVED +  " = 0",  null, null, null, null);
    }

    public Tag getTag(int id) {
        Cursor cursor = database.query(TAGS_TABLE, null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Tag tag = new Tag(Integer.parseInt(cursor.getString(0)),
               cursor.getString(1),cursor.getString(2),cursor.getInt(3)>0,cursor.getLong(4));
        // return contact
        return tag;
    }

    public int updateTag(Tag tag, String tagTitle,String tagDescription, Boolean isArchived) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TAG_TITLE, tagTitle);
        values.put(DBHelper.TAG_DESCRIPTION, tagDescription);
        values.put(DBHelper.TAG_IS_ARCHIVED, isArchived);

        // updating row
        return database.update(TAGS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(tag.getTagId()) });
    }


    public int archiveTag(Tag tag) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TAG_IS_ARCHIVED, true);

        return database.update(TAGS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(tag.getTagId()) });
    }

        public Tag cursorToTag(Cursor cursor) {
        Tag tag = new Tag();
        tag.setTagId(cursor.getInt(0));
        tag.setTagText(cursor.getString(1));
        tag.setTagDescription(cursor.getString(2));
        tag.setIsArchived(cursor.getInt(3) > 0);
        if(cursor.getString(4)!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try
            {
                Date date = simpleDateFormat.parse(cursor.getString(4));
                tag.setCreatedMillis(date.getTime());
                System.out.println("date : "+simpleDateFormat.format(date));
            }
            catch (ParseException ex)
            {
                System.out.println("Exception "+ex);
            }
        }

        return tag;
    }

    public Tag saveTag(Tag tag) {
        ContentValues values = new ContentValues();
        Integer id =Integer.parseInt(getPrevTagId(TAGS_TABLE)) + 1;
        values.put("id", (Integer.toString(id)));
        values.put("title", tag.getTagText());
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TAGS_TABLE, null, values);

        Toast.makeText(context,tag.getTagText(), Toast.LENGTH_LONG).show();
        return getTag(id);
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


    public Cursor fetchAllArchivedTags() {
        return database.query(TAGS_TABLE, null,DBHelper.TAG_IS_ARCHIVED +  " = 1",  null, null, null, null);
    }
}
