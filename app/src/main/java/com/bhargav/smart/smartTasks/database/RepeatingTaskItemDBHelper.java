package com.bhargav.smart.smartTasks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.RepeatingTaskItem;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class RepeatingTaskItemDBHelper {

    private final static String TABLE = DBHelper.REPEATING_TASK_ITEMS_TABLE;
    private final static String TAG = "HabitSave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public RepeatingTaskItemDBHelper(Context context) {
        this.context = context;

    }

    public RepeatingTaskItemDBHelper open() throws SQLException {
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

    public RepeatingTaskItem getHabitItem(int id) {
        Cursor cursor = database.query(TABLE,null,null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RepeatingTaskItem repeatingTaskItem =cursorToHabitItem(cursor);
        // return contact
        return repeatingTaskItem;
    }

    public RepeatingTaskItem getHabitItemByDate(Date date, RepeatingTask repeatingTask) {
        Cursor cursor = database.query(DBHelper.REPEATING_TASK_ITEMS_TABLE,null, DBHelper.REPEATING_TASK_ITEM_REPEATING_TASK + "= ? AND "+DBHelper.REPEATING_TASK_ITEM_REPEATING_TASK + " = ?",
                new String[] { new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(date) , repeatingTask.getId().toString()}, null, null, null, null);
        RepeatingTaskItem repeatingTaskItem = null;
        if ( cursor.moveToNext())
        {
            repeatingTaskItem =cursorToHabitItem(cursor);
        }

        // return contact
        return repeatingTaskItem;
    }

    public List<RepeatingTaskItem> getHabitItemByHabits(RepeatingTask repeatingTask) {
        Cursor cursor = database.query(DBHelper.REPEATING_TASK_ITEMS_TABLE,null,DBHelper.REPEATING_TASK_ITEM_REPEATING_TASK + " = ?",
                new String[] {  repeatingTask.getId().toString()}, null, null, null, null);
        List<RepeatingTaskItem> repeatingTaskItemList = new ArrayList<RepeatingTaskItem>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTaskItem repeatingTaskItem =cursorToHabitItem(cursor);
            repeatingTaskItemList.add(repeatingTaskItem);
            cursor.moveToNext();
        }
          // return contact
        return repeatingTaskItemList;
    }



    public RepeatingTaskItem updateHabitItem(RepeatingTaskItem repeatingTaskItem) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.REPEATING_TASK_ITEM_DATE,
                new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(repeatingTaskItem.getCurrentDate().getTime()));
        values.put(DBHelper.REPEATING_TASK_ITEM_REPEATING_TASK, repeatingTaskItem.getHabit());
        values.put(DBHelper.COLUMN_STATE, repeatingTaskItem.getHabitItemState().getStateValue());
        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(repeatingTaskItem.getId()) });
        RepeatingTaskItem repeatingTaskItem1 = getHabitItem(repeatingTaskItem.getId());
        return repeatingTaskItem1;
    }


    public RepeatingTaskItem saveHabitItem(Date date,RepeatingTask repeatingTask) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevHabitItemId(TABLE)) + 1)));
        values.put(DBHelper.REPEATING_TASK_ITEM_REPEATING_TASK, repeatingTask.getId());
        values.put( DBHelper.REPEATING_TASK_ITEM_DATE,
                new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(date));
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TABLE, null, values);
        RepeatingTaskItem repeatingTaskItem = getHabitItem(Integer.parseInt(getPrevHabitItemId(TABLE)) + 1);
        return repeatingTaskItem;

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

    public RepeatingTaskItem cursorToHabitItem(Cursor cursor) {
        RepeatingTaskItem repeatingTaskItem = new RepeatingTaskItem();
        repeatingTaskItem.setId(cursor.getInt(0));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
        if (cursor.getString(1) != null) {
            try {
                cal.setTime(sdf.parse(cursor.getString(1)));
                repeatingTaskItem.setCurrentDate(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        repeatingTaskItem.setHabitItemState(utilFunctions.State.values()[(cursor.getInt(2))]);
        repeatingTaskItem.setIsCompleted(cursor.getInt(3) > 0);
        if (cursor.getString(4) != null) {
            try {
                cal.setTime(sdf.parse(cursor.getString(4)));
                repeatingTaskItem.setCompletedTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (cursor.getString(5) != null) {
            try {
                cal.setTime(sdf.parse(cursor.getString(5)));
                repeatingTaskItem.setCreatedTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(cursor.getString(6)!=null) {
            repeatingTaskItem.setHabit(cursor.getInt(6));

        }
        return repeatingTaskItem;

    }

    public Cursor fetchAllHabitsItemsInHabit(RepeatingTask repeatingTask) {
        return database.query(TABLE, null, DBHelper.REPEATING_TASK_ITEM_REPEATING_TASK +  "=" + repeatingTask.getId(), null, null, null, null);
    }


}
