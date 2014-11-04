package com.semantic.semanticOrganizer.docket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Habit;
import com.semantic.semanticOrganizer.docket.models.HabitItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class HabitItemDBHelper {

    private final static String TABLE = DBHelper.HABIT_ITEMS_TABLE;
    private final static String TAG = "HabitSave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public HabitItemDBHelper(Context context) {
        this.context = context;

    }

    public HabitItemDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }


    public Cursor fetchAllHabitItems() {
        return database.query(TABLE, null, null, null, null, null, null);
    }

    public HabitItem getHabitItem(int id) {
        Cursor cursor = database.query(TABLE,null,null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        HabitItem habitItem =cursorToHabitItem(cursor);
        // return contact
        return habitItem;
    }

    public HabitItem getHabitItemByDate(Date date, Habit habit) {
        Cursor cursor = database.query(DBHelper.HABIT_ITEMS_TABLE,null, DBHelper.HABIT_ITEM_DATE + "= ? AND "+DBHelper.HABIT_ITEM_HABIT + " = ?",
                new String[] { new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) , habit.getId().toString()}, null, null, null, null);
        HabitItem habitItem = null;
        if ( cursor.moveToNext())
        {
            habitItem =cursorToHabitItem(cursor);
        }

        // return contact
        return habitItem;
    }

    public List<HabitItem> getHabitItemByHabits(Habit habit) {
        Cursor cursor = database.query(DBHelper.HABIT_ITEMS_TABLE,null,DBHelper.HABIT_ITEM_HABIT + " = ?",
                new String[] {  habit.getId().toString()}, null, null, null, null);
        List<HabitItem> habitItemList = new ArrayList<HabitItem>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HabitItem habitItem =cursorToHabitItem(cursor);
            habitItemList.add(habitItem);
            cursor.moveToNext();
        }
          // return contact
        return habitItemList;
    }



    public HabitItem updateHabitItem(HabitItem habitItem) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.HABIT_ITEM_DATE,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(habitItem.getCurrentDate().getTime()));
        values.put(DBHelper.HABIT_ITEM_HABIT, habitItem.getHabit());
        values.put(DBHelper.HABIT_ITEM_STATE, habitItem.getHabitItemState().getStateValue());
        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(habitItem.getId()) });
        HabitItem habitItem1 = getHabitItem(habitItem.getId());
        return habitItem1;
    }


    public HabitItem saveHabitItem(Date date,Habit habit) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevHabitItemId(TABLE)) + 1)));
        values.put(DBHelper.HABIT_ITEM_HABIT, habit.getId());
        values.put( DBHelper.HABIT_ITEM_DATE,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TABLE, null, values);
        HabitItem habitItem = getHabitItem(Integer.parseInt(getPrevHabitItemId(TABLE)) + 1);
        return habitItem;

    }
    private String getPrevHabitItemId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public HabitItem cursorToHabitItem(Cursor cursor) {
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



        HabitItem habitItem = new HabitItem();

        habitItem.setId(cursor.getInt(0));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            cal.setTime(sdf.parse(cursor.getString(1)));
            habitItem.setCurrentDate(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        habitItem.setHabitItemState(HabitItem.State.values()[(cursor.getInt(2))]);
        habitItem.setCreatedTime(cursor.getString(3));
        if(cursor.getString(4)!=null) {
            habitItem.setHabit(cursor.getInt(4));
        }
        return habitItem;

    }

    public Cursor fetchAllHabitsItemsInHabit(Habit habit) {
        return database.query(TABLE, null, DBHelper.HABIT_ITEM_HABIT +  "=" + habit.getId(), null, null, null, null);
    }


}
