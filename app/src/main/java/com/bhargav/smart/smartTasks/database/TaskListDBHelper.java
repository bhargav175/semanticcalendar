package com.bhargav.smart.smartTasks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.Label;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 16-09-2014.
 */
public class TaskListDBHelper {
    private final static String TAGS_TABLE = DBHelper.CATEGORIES_TABLE;
    private final static String TAG = "TAG-DBAdapter";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public TaskListDBHelper(Context context) {
        this.context = context;

    }

    public TaskListDBHelper open() throws SQLException {
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
        return database.query(TAGS_TABLE, null, DBHelper.COLUMN_IS_ARCHIVED + " = 0", null, null, null, null);
    }

    public TaskList getTag(int id) {
        Cursor cursor = database.query(TAGS_TABLE, null, DBHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TaskList taskList = cursorToTag(cursor);
        // return contact
        return taskList;
    }

    public int updateTag(TaskList taskList) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CATEGORY_TITLE, taskList.getTagText());
        values.put(DBHelper.CATEGORY_DESCRIPTION, taskList.getTagDescription());
        values.put(DBHelper.COLUMN_COLOR, taskList.getTagColor().getColorValue());
        values.put(DBHelper.COLUMN_IS_ARCHIVED, taskList.getIsArchived());

        // updating row
        return database.update(TAGS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskList.getTagId())});
    }


    public int archiveTag(TaskList taskList) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_IS_ARCHIVED, true);

        return database.update(TAGS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskList.getTagId())});
    }

    public TaskList cursorToTag(Cursor cursor) {
        TaskList taskList = new TaskList();
        taskList.setTagId(cursor.getInt(0));
        taskList.setTagText(cursor.getString(1));
        taskList.setTagDescription(cursor.getString(2));
        taskList.setTagColor(utilFunctions.Color.values()[cursor.getInt(3)]);
        taskList.setIsArchived(cursor.getInt(4) > 0);
        if (!cursor.isNull(5)) {
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(5)));
                taskList.setCreatedTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return taskList;
    }

    public TaskList saveTag(TaskList taskList) {
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevTagId(TAGS_TABLE)) + 1;
        values.put("id", (Integer.toString(id)));
        values.put("title", taskList.getTagText());
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TAGS_TABLE, null, values);
        Log.d(TAG, taskList.getTagText());
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
        return database.query(TAGS_TABLE, null, DBHelper.COLUMN_IS_ARCHIVED + " = 1", null, null, null, null);
    }
}
