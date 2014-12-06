package com.bhargav.smart.smartTasks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.OneTimeTask;
import com.bhargav.smart.smartTasks.models.Reminder;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.bhargav.smart.smartTasks.utils.utilFunctions.BLog;

/**
 * Created by Admin on 16-09-2014.
 */
public class OneTimeTaskDBHelper {

    private final static String TABLE = DBHelper.TASKS_TABLE;
    private final static String TAG = utilFunctions.SUPER_TAG;

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public OneTimeTaskDBHelper(Context context) {
        this.context = context;

    }

    public OneTimeTaskDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }

    public Cursor fetchAllUnArchivedNotes() {
        return database.query(TABLE, null, DBHelper.COLUMN_IS_ARCHIVED + "=?", new String[]{String.valueOf(false)}, null, null, null);
    }

    public Cursor fetchAllNotes() {
        return database.query(TABLE, null, null, null, null, null, null);
    }

    public OneTimeTask getNote(int id) {
        Cursor cursor = database.query(TABLE, null, DBHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        OneTimeTask oneTimeTask = cursorToNote(cursor);
        // return contact
        return oneTimeTask;
    }

    public int updateNote(OneTimeTask oneTimeTask) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TASK_TITLE, oneTimeTask.getNoteTitle());
        values.put(DBHelper.TASK_DESCRIPTION, oneTimeTask.getNoteDescription());
        values.put(DBHelper.COLUMN_PRIORITY, oneTimeTask.getPriority());
//        values.put(DBHelper.COLUMN_STATE, oneTimeTask.getTaskItemState().getStateValue());
        if (oneTimeTask.getDueTime() != null) {
            values.put(DBHelper.COLUMN_DUE_TIME, new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(oneTimeTask.getDueTime().getTime()));
        } else {

            values.putNull(DBHelper.COLUMN_DUE_TIME);
        }
        values.put(DBHelper.COLUMN_HAS_REMINDER, oneTimeTask.getIsReminded());
        values.put(DBHelper.COLUMN_IS_COMPLETED, oneTimeTask.getIsCompleted());
        values.put(DBHelper.COLUMN_IS_ARCHIVED, oneTimeTask.getIsArchived());
        if (oneTimeTask.getCompletedTime() != null) {
            values.put(DBHelper.COLUMN_COMPLETED_TIME, new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(oneTimeTask.getCompletedTime().getTime()));
        } else {

            values.putNull(DBHelper.COLUMN_COMPLETED_TIME);
        }
        if (oneTimeTask.getCreatedTime() != null) {
            values.put(DBHelper.COLUMN_CREATED_TIME, new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(oneTimeTask.getCreatedTime().getTime()));
        } else {

            values.putNull(DBHelper.COLUMN_CREATED_TIME);
        }
        if (oneTimeTask.getRemainderId() != null) {
            values.put(DBHelper.TASK_REMINDER_ID, oneTimeTask.getRemainderId());
        } else {

            values.putNull(DBHelper.TASK_REMINDER_ID);
        }
        values.put(DBHelper.TASK_CATEGORY, oneTimeTask.getTag());

        // updating row
        BLog(values.toString());
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(oneTimeTask.getId())});
        Log.d(TAG, "Note updated" + oneTimeTask.getNoteTitle());


        return 0;
    }


    public void saveNote(OneTimeTask oneTimeTask) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevNoteId(TABLE)) + 1)));
        values.put(DBHelper.TASK_DESCRIPTION, oneTimeTask.getNoteTitle());
        values.put(DBHelper.COLUMN_CREATED_TIME, new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(Calendar.getInstance().getTime()));

        //values.put("image_path", draft.getDraftImagePath());
        BLog(values.toString());
        //TODO Location Insertion
        database.insert(TABLE, null, values);
        Log.d(TAG, "Note saved" + oneTimeTask.getNoteTitle());

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

    public OneTimeTask cursorToNote(Cursor cursor) {
        OneTimeTask oneTimeTask = new OneTimeTask();
         oneTimeTask.setId(cursor.getInt(0));
        oneTimeTask.setNoteTitle(cursor.getString(1));
        oneTimeTask.setNoteDescription(cursor.getString(2));
        oneTimeTask.setPriority(cursor.getInt(3));
        oneTimeTask.setTaskItemState(utilFunctions.State.values()[(cursor.getInt(4))]);
        if (cursor.getString(5) != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(5)));
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                oneTimeTask.setDueTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        oneTimeTask.setIsReminded(cursor.getInt(6) > 0);
        oneTimeTask.setIsCompleted(cursor.getInt(7) > 0);
        oneTimeTask.setIsArchived(cursor.getInt(8) > 0);
        if (cursor.getString(9) != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(9)));
                oneTimeTask.setCompletedTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (cursor.getString(10) != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(10)));
                oneTimeTask.setCreatedTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!cursor.isNull(11)) {
            oneTimeTask.setTag(cursor.getInt(11));
        } else {
            oneTimeTask.setTag(null);
        }


        if (!cursor.isNull(12)) {
            oneTimeTask.setRemainderId(cursor.getInt(12));
        } else {
            oneTimeTask.setRemainderId(null);
        }


        return oneTimeTask;

    }

    public Cursor fetchAllNotesInTag(TaskList taskList) {
        return database.query(TABLE, null, DBHelper.TASK_CATEGORY + "=" + taskList.getTagId(), null, null, null, null);
    }

    public Cursor fetchAllNotesSandbox() {
        return database.query(TABLE, null, DBHelper.TASK_CATEGORY + " is null", null, null, null, null);
    }

    public Cursor fetchAllUnArchivedNotesSandbox() {
        return database.query(TABLE, null, DBHelper.TASK_CATEGORY + " is null AND " + DBHelper.COLUMN_IS_ARCHIVED + " = 0 ", null, null, null, null);
    }

    public Cursor fetchAllUnArchivedNotesInTag(TaskList taskList) {
        return database.query(TABLE, null, DBHelper.TASK_CATEGORY + "= ? AND " + DBHelper.COLUMN_IS_ARCHIVED + " = 0", new String[]{String.valueOf(taskList.getTagId())}, null, null, null);


    }

    public void archiveNote(OneTimeTask oneTimeTask) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_IS_ARCHIVED, true);

        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(oneTimeTask.getId())});
        Log.d(TAG, "Note archived" + oneTimeTask.getNoteTitle());


    }

    public OneTimeTask saveNoteWithTag(OneTimeTask oneTimeTask, TaskList currentTaskListInView) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevNoteId(TABLE)) + 1;
        values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
        values.put(DBHelper.TASK_CATEGORY, currentTaskListInView.getTagId());
        values.put(DBHelper.TASK_TITLE, oneTimeTask.getNoteTitle());

        //values.put("image_path", draft.getDraftImagePath());
        BLog(values.toString());
        database.insert(TABLE, null, values);

        Log.d(TAG, "Note saved" + oneTimeTask.getNoteTitle());
        OneTimeTask oneTimeTask1 = getNote(id);
        oneTimeTask1.setDefaultProperties();
        updateNote(oneTimeTask1);
        Log.d(TAG, "Note updated" + oneTimeTask1.getNoteTitle());
        return oneTimeTask1;
    }

    public Cursor fetchAllUnarchivedNotesWithDueDate(Calendar calendar) {
        return null;


    }

    public Cursor fetchAllArchivedNotes() {
        return database.query(TABLE, null, DBHelper.COLUMN_IS_ARCHIVED + " = 1 ", null, null, null, null);
    }

    public Cursor fetchAllUnArchivedNotesByDueDate(Calendar calendar) {
        Calendar startDate = (Calendar) calendar.clone();
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        Calendar endDate = (Calendar) calendar.clone();
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        BLog(new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()));
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND " + DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"')",null, null, null, null);

    }

    public Cursor fetchAllUnArchivedNotesByWeek(Calendar calendar) {
        Calendar d = (Calendar) calendar.clone();
        Calendar e = (Calendar) d.clone();
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);
        int day = d.get(Calendar.DAY_OF_WEEK);
        int s,l;
        switch (day){
            case Calendar.SUNDAY:
                s =0;
                l =6;
                break;
            case Calendar.MONDAY:
                s =1;
                l =5;
                break;
            case Calendar.TUESDAY:
                s =2;
                l =4;
                break;
            case Calendar.WEDNESDAY:
                s =3;
                l =3;
                break;
            case Calendar.THURSDAY:
                s =4;
                l =2;
                break;
            case Calendar.FRIDAY:
                s =5;
                l =1;
                break;
            case Calendar.SATURDAY:
                s =6;
                l =0;
                break;
            default:
                s =0;
                l =6;
                break;

        }
        d.add(Calendar.DAY_OF_YEAR, -s);
        e.set(Calendar.HOUR_OF_DAY, 23);
        e.set(Calendar.MINUTE, 59);
        e.set(Calendar.SECOND, 59);
        e.add(Calendar.DAY_OF_MONTH, l);
        BLog(new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()));
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND " + DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(e.getTime())+"')",null, null, null, null);

    }

    public Cursor fetchAllUnArchivedNotesByMonth(Calendar calendar) {
        Calendar d = (Calendar) calendar.clone();
        d.set(Calendar.DAY_OF_MONTH,1);
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);
        Calendar e = (Calendar) d.clone();
        e.set(Calendar.MONTH,d.get(Calendar.MONTH)+1);
       e.add(Calendar.DAY_OF_MONTH,-1);
        e.set(Calendar.HOUR_OF_DAY, 23);
        e.set(Calendar.MINUTE, 59);
        e.set(Calendar.SECOND, 59);
        BLog(new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()));
        BLog(new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(e.getTime()));
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND " + DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(e.getTime())+"')",null, null, null, null);

    }

    public Cursor fetchAllUnArchivedUpComingNotes(Calendar calendar) {
        Calendar d = (Calendar) calendar.clone();
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);
        d.add(Calendar.DAY_OF_MONTH, 1);
        BLog(new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()));
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND " + DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()) +"')",null, null, null, null);
    }

    public Cursor fetchAllUnArchivedOverdueNotes(Calendar calendar) {
        Calendar d = (Calendar) calendar.clone();
        d.set(Calendar.HOUR_OF_DAY, 23);
        d.set(Calendar.MINUTE, 59);
        d.set(Calendar.SECOND, 59);
        d.add(Calendar.DAY_OF_MONTH, -1);
        BLog(new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()));
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND " + DBHelper.COLUMN_IS_COMPLETED + " = 0 AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(d.getTime()) +"')",null, null, null, null);
    }

    public void updateState(OneTimeTask oneTimeTask) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_STATE, oneTimeTask.getTaskItemState().getStateValue());
        // updating row
        BLog(values.toString());
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(oneTimeTask.getId())});
        BLog(oneTimeTask.getTaskItemState().toString());
    }

    public void removeReminder(OneTimeTask oneTimeTask, Context context) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_HAS_REMINDER, false);
        values.putNull(DBHelper.TASK_REMINDER_ID);
        if(oneTimeTask.getRemainderId()!=null){
            Reminder.deleteReminder(context,oneTimeTask.getRemainderId());
        }
        // updating row
        BLog(values.toString());
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(oneTimeTask.getId())});

    }
}
