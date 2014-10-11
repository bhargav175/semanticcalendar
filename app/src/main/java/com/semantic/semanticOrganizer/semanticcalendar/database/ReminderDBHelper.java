package com.semantic.semanticOrganizer.semanticcalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Reminder;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

/**
 * Created by Admin on 16-09-2014.
 */
public class ReminderDBHelper {

    private final static String TABLE = DBHelper.REMINDER_TABLE;
    private final static String TAG = "ReminderSave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public ReminderDBHelper(Context context) {
        this.context = context;

    }

    public ReminderDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }


    public Cursor fetchAllReminders() {
        return database.query(TABLE, null, null, null, null, null, null);
    }

    public Reminder getReminder(int id) {
        Cursor cursor = database.query(TABLE,null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Reminder reminder =cursorToReminder(cursor);
        // return contact
        return reminder;
    }



    public int updateReminder(Reminder reminder) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.REMINDER_YEAR, reminder.getYear());
        values.put(DBHelper.REMINDER_MONTH_OF_YEAR, reminder.getMinuteOfHour());
        values.put(DBHelper.REMINDER_DAY_OF_MONTH, reminder.getDayOfMonth());
        values.put(DBHelper.REMINDER_HOUR_OF_DAY, reminder.getHourOfDay());
        values.put(DBHelper.REMINDER_MINUTE_OF_HOUR, reminder.getMinuteOfHour());
        values.put(DBHelper.REMINDER_SECONDS, reminder.getSecond());
        values.put(DBHelper.REMINDER_IS_REPEATING, reminder.getIsRepeating());
        values.put(DBHelper.REMINDER_INTERVAL, reminder.getInterval());
        values.put(DBHelper.REMINDER_DURATION, reminder.getDuration());



        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(reminder.getId()) });
        Toast.makeText(context,"Reminder "+ reminder.getId()+" updated", Toast.LENGTH_SHORT).show();


        return 0;
    }


    public void saveReminder(Reminder reminder) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevReminderId(TABLE)) + 1)));
        values.put(DBHelper.REMINDER_YEAR, reminder.getYear());
        values.put(DBHelper.REMINDER_MONTH_OF_YEAR, reminder.getMinuteOfHour());
        values.put(DBHelper.REMINDER_DAY_OF_MONTH, reminder.getDayOfMonth());
        values.put(DBHelper.REMINDER_HOUR_OF_DAY, reminder.getHourOfDay());
        values.put(DBHelper.REMINDER_MINUTE_OF_HOUR, reminder.getMinuteOfHour());
        values.put(DBHelper.REMINDER_SECONDS, reminder.getSecond());
        values.put(DBHelper.REMINDER_IS_REPEATING, reminder.getIsRepeating());
        values.put(DBHelper.REMINDER_INTERVAL, reminder.getInterval());
        values.put(DBHelper.REMINDER_DURATION, reminder.getDuration());
        Log.d(TAG, values.toString());
        database.insert(TABLE, null, values);
        Toast.makeText(context,"Reminder "+ reminder.getId()+" saved", Toast.LENGTH_SHORT).show();

    }
    private String getPrevReminderId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public Integer getLastReminderId(){
        try {
            Cursor cr = database.query(DBHelper.REMINDER_TABLE, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getInt(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return -1;
        }
    }

    public Reminder cursorToReminder(Cursor cursor) {


        Reminder reminder = new Reminder();
        reminder.setId(cursor.getInt(0));

        if (!cursor.isNull(1)){
            reminder.setYear(cursor.getInt(1));
        }
        if (!cursor.isNull(2)){
            reminder.setMonthOfYear(cursor.getInt(2));
        }
        if (!cursor.isNull(3)){
            reminder.setDayOfMonth(cursor.getInt(3));
        }
        if (!cursor.isNull(4)){
            reminder.setHourOfDay(cursor.getInt(4));
        }
        if (!cursor.isNull(5)){
            reminder.setMinuteOfHour(cursor.getInt(5));
        }
        if (!cursor.isNull(6)){
            reminder.setSecond(cursor.getInt(6));
        }
        if (!cursor.isNull(7)){
            reminder.setIsRepeating(cursor.getInt(7)>0);
        }
        if (!cursor.isNull(8)){
            reminder.setInterval(cursor.getInt(8));
        }
        if (!cursor.isNull(9)){
            reminder.setDuration(cursor.getInt(9));
        }

        reminder.setCreatedTime(cursor.getString(10));

        return reminder;

    }


}
