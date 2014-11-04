package com.semantic.semanticOrganizer.docket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Habit;
import com.semantic.semanticOrganizer.docket.models.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 16-09-2014.
 */
public class HabitDBHelper {

    private final static String TABLE = DBHelper.HABITS_TABLE;
    private final static String TAG = "HabitSave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public HabitDBHelper(Context context) {
        this.context = context;

    }

    public HabitDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }

    public Cursor fetchAllUnArchivedCheckLists() {
        return database.query(TABLE, null, DBHelper.HABIT_IS_ARCHIVED + "=?",  new String[] { String.valueOf(false) }, null, null, null);
    }


    public Cursor fetchAllHabits() {
        return database.query(TABLE, null, null, null, null, null, null);
    }

    public Habit getHabit(int id) {
        Cursor cursor = database.query(TABLE,null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Habit habit =cursorToHabit(cursor);
        // return contact
        return habit;
    }



    public int updateHabit(Habit habit) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.HABIT_TITLE, habit.getHabitText());
        values.put(DBHelper.HABIT_DESCRIPTION, habit.getHabitDescription());
        values.put(DBHelper.HABIT_REQUEST_ID,habit.getRequestId());
        values.put(DBHelper.HABIT_IS_ARCHIVED,habit.getIsArchived());
        values.put(DBHelper.HABIT_TAG, habit.getTag());
        values.put(DBHelper.HABIT_TYPE,habit.getHabitType().getTypeValue());
        values.put(DBHelper.HABIT_DAYS_CODE,habit.getDaysCode());
        values.put(DBHelper.HABIT_DURATION,habit.getDuration());
        values.put(DBHelper.HABIT_FREQUENCY,habit.getFrequency());
        if(habit.getDueTime()!=null){
            values.put(DBHelper.COLUMN_DUE_TIME,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(habit.getDueTime().getTime()));
        }
                // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(habit.getId()) });
        Log.d( TAG,"Habit updated" + habit.getHabitText());


        return 0;
    }


    public void saveHabit(Habit habit) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevHabitId(TABLE)) + 1)));
        values.put(DBHelper.HABIT_TITLE, habit.getHabitText());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TABLE, null, values);
        Log.d( TAG,"Habit saved" + habit.getHabitText());

    }
    private String getPrevHabitId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public Habit cursorToHabit(Cursor cursor) {
//        + COLUMN_ID+ " integer primary key autoincrement, "
//                + HABIT_TITLE + " text not null, "
//                + HABIT_QUESTION+ " text , "
//                + HABIT_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
//                + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
//                + HABIT_TYPE + " integer DEFAULT 1, "
//                + HABIT_DAYS_CODE + " integer DEFAULT null, "
//                + HABIT_FREQUENCY + " integer DEFAULT null, "
//                + HABIT_DURATION + " integer DEFAULT null, "
//                + HABIT_TAG + " integer DEFAULT null, "
//                + HABIT_REQUEST_ID + " integer DEFAULT null"



        Habit habit = new Habit();
        habit.setId(cursor.getInt(0));
        habit.setHabitText(cursor.getString(1));
        habit.setHabitDescription(cursor.getString(2));
        habit.setIsArchived(cursor.getInt(3)>0);

        habit.setCreatedTime(cursor.getString(4));
        if (!cursor.isNull(5)){

            habit.setHabitType(Habit.Type.values()[cursor.getInt(5)]);
        }
        if (!cursor.isNull(6)){
            habit.setDaysCode(cursor.getInt(6));
        }
        if (!cursor.isNull(7)){

            habit.setFrequency(cursor.getInt(7));
        }
        if (!cursor.isNull(8)){
            habit.setDuration(cursor.getInt(8));
        }

        if (!cursor.isNull(9)){
            habit.setTag(cursor.getInt(9));
        }

        if (!cursor.isNull(10)){
            habit.setRequestId(cursor.getInt(10));
        }
        if(!cursor.isNull(11)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(11)));
                habit.setDueTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }



        return habit;

    }

    public Cursor fetchAllHabitsInTag(Tag tag) {
        return database.query(TABLE, null, DBHelper.HABIT_TAG +  "=" + tag.getTagId(), null, null, null, null);
    }

    public Cursor fetchAllHabitsSandbox() {
        return database.query(TABLE, null, DBHelper.HABIT_TAG +  " is null" , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedHabitsSandbox() {
        return database.query(TABLE, null, DBHelper.HABIT_TAG +  " is null AND " + DBHelper.HABIT_IS_ARCHIVED +" = 0 " , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedHabitsInTag(Tag tag) {
        return database.query(TABLE, null, DBHelper.HABIT_TAG +  "= ? AND "+DBHelper.HABIT_IS_ARCHIVED + " = 0" , new String[] { String.valueOf(tag.getTagId()) }, null, null, null);

    }

    public void archiveHabit(Habit habit) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.HABIT_IS_ARCHIVED, true);
        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(habit.getId()) });
        Log.d( TAG,"Habit archived" + habit.getHabitText());

    }

    public Habit saveHabitWithTag(Habit habit, Tag tag) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevHabitId(TABLE)) + 1;
        values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
        values.put(DBHelper.HABIT_TITLE, habit.getHabitText());
        values.put(DBHelper.HABIT_TAG, tag.getTagId());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TABLE, null, values);
        Log.d( TAG,"Habit saved" + habit.getHabitText());
        return getHabit(id);

    }

    public Cursor fetchAllArchivedHabits() {
        return database.query(TABLE, null, DBHelper.HABIT_IS_ARCHIVED +" = 1 " , null, null, null, null);
    }
}
