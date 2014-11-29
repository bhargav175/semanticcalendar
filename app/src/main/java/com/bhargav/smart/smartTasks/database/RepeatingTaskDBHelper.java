package com.bhargav.smart.smartTasks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.TaskList;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 16-09-2014.
 */
public class RepeatingTaskDBHelper {

    private final static String TABLE = DBHelper.REPEATING_TASKS_TABLE;
    private final static String TAG = "HabitSave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public RepeatingTaskDBHelper(Context context) {
        this.context = context;

    }

    public RepeatingTaskDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }


    public Cursor fetchAllRepeatingTasks() {
        return database.query(TABLE, null, null, null, null, null, null);
    }

    public RepeatingTask getRepeatingTasks(int id) {
        Cursor cursor = database.query(TABLE,null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RepeatingTask repeatingTask = cursorToRepeatingTask(cursor);
        // return contact
        return repeatingTask;
    }



    public int updateRepeatingTask(RepeatingTask repeatingTask) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.REPEATING_TASK_TITLE, repeatingTask.getRepeatingTaskText());
        values.put(DBHelper.REPEATING_TASK_DESCRIPTION, repeatingTask.getRepeatingTaskDescription());

        if(repeatingTask.getPriority()!=null){
            values.put(DBHelper.COLUMN_PRIORITY, repeatingTask.getPriority());
        }else{
            values.putNull(DBHelper.COLUMN_PRIORITY);

        }

        if(repeatingTask.getRepeatingTaskType()!=null){
            values.put(DBHelper.REPEATING_TASK_TYPE, repeatingTask.getRepeatingTaskType().getTypeValue());
        }else{
            values.putNull(DBHelper.REPEATING_TASK_TYPE);

        }
        if(repeatingTask.getDaysCode()!=null){
            values.put(DBHelper.REPEATING_TASK_DAYS_CODE, repeatingTask.getDaysCode());
        }else{
            values.putNull(DBHelper.REPEATING_TASK_DAYS_CODE);

        }
        if(repeatingTask.getFrequency()!=null){
            values.put(DBHelper.REPEATING_TASK_FREQUENCY, repeatingTask.getFrequency());
        }else{
            values.putNull(DBHelper.REPEATING_TASK_FREQUENCY);

        }
        if(repeatingTask.getStartDate()!=null){
            Calendar c = (Calendar) repeatingTask.getStartDate().clone();
            c.set(Calendar.HOUR_OF_DAY,0);
            c.set(Calendar.MINUTE,0);
            c.set(Calendar.SECOND,0);
            values.put(DBHelper.REPEATING_TASK_START_DATE,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(c.getTime()));
        }else{
            values.putNull(DBHelper.REPEATING_TASK_START_DATE);

        }
        if(repeatingTask.getEndDate()!=null){
            Calendar e = (Calendar) repeatingTask.getEndDate().clone();
            e.set(Calendar.HOUR_OF_DAY,23);
            e.set(Calendar.MINUTE,59);
            e.set(Calendar.SECOND,59);
            values.put(DBHelper.REPEATING_TASK_END_DATE,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(e.getTime()));
        }else{
            values.putNull(DBHelper.REPEATING_TASK_END_DATE);

        }
        values.put(DBHelper.REPEATING_TASK_HAS_STATISTICS, repeatingTask.getHasStatistics());
        values.put(DBHelper.COLUMN_IS_ARCHIVED, repeatingTask.getIsArchived());

        if(repeatingTask.getDueTime()!=null){
            values.put(DBHelper.COLUMN_DUE_TIME,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(repeatingTask.getDueTime().getTime()));
        }
        values.put(DBHelper.COLUMN_HAS_REMINDER, repeatingTask.getIsReminded());
        values.put(DBHelper.REPEATING_TASK_CATEGORY, repeatingTask.getTag());

        if(repeatingTask.getReminderId()!=null){
            values.put(DBHelper.REPEATING_TASK_REMINDER_ID, repeatingTask.getReminderId());
        }else{
            values.putNull(DBHelper.REPEATING_TASK_REMINDER_ID);

        }


                // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(repeatingTask.getId()) });
        Log.d( utilFunctions.SUPER_TAG+TAG,"Repeating Task updated" + repeatingTask.getRepeatingTaskText());


        return 0;
    }


    public void saveRepeatingTask(RepeatingTask repeatingTask) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevRepeatingTaskId(TABLE)) + 1)));
        values.put(DBHelper.REPEATING_TASK_TITLE, repeatingTask.getRepeatingTaskText());
        Integer d = RepeatingTask.durationStrings.length - 1;
        values.put(DBHelper.REPEATING_TASK_START_DATE,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(repeatingTask.getStartDate().getTime()));
        values.put(DBHelper.REPEATING_TASK_END_DATE,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(repeatingTask.getEndDate().getTime()));
        values.put(DBHelper.REPEATING_TASK_TYPE, RepeatingTask.Type.FIXED.getTypeValue());
        values.put(DBHelper.REPEATING_TASK_DAYS_CODE,1111111);
        values.putNull(DBHelper.REPEATING_TASK_FREQUENCY);
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(utilFunctions.SUPER_TAG+TAG, values.toString());
        database.insert(TABLE, null, values);
        Log.d( utilFunctions.SUPER_TAG+TAG,"Repeating Task saved" + repeatingTask.getRepeatingTaskText());

    }
    private String getPrevRepeatingTaskId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public RepeatingTask cursorToRepeatingTask(Cursor cursor) {
        RepeatingTask repeatingTask = new RepeatingTask();
        repeatingTask.setId(cursor.getInt(0));
        repeatingTask.setRepeatingTaskText(cursor.getString(1));
        repeatingTask.setRepeatingTaskDescription(cursor.getString(2));
        repeatingTask.setPriority(cursor.getInt(3));
        if (!cursor.isNull(4)){
            repeatingTask.setRepeatingTaskType(RepeatingTask.Type.values()[cursor.getInt(4)]);
        }
        if (!cursor.isNull(5)){
            repeatingTask.setDaysCode(cursor.getInt(5));
        }
        if (!cursor.isNull(6)){

            repeatingTask.setFrequency(cursor.getInt(6));
        }
        if(!cursor.isNull(7)){
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(7)));
                repeatingTask.setStartDate(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(!cursor.isNull(8)){
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(8)));
                repeatingTask.setEndDate(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        repeatingTask.setHasStatistics(cursor.getInt(9)>0);
        repeatingTask.setIsArchived(cursor.getInt(10)>0);
        if(!cursor.isNull(11)){
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(11)));
                repeatingTask.setCreatedTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(!cursor.isNull(12)){
            SimpleDateFormat sdf = new SimpleDateFormat(utilFunctions.reverseDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(12)));
                repeatingTask.setDueTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        repeatingTask.setIsReminded(cursor.getInt(13)>0);

        if (!cursor.isNull(14)){
            repeatingTask.setTag(cursor.getInt(14));
        }

        if (!cursor.isNull(15)){
            repeatingTask.setReminderId(cursor.getInt(15));
        }




        return repeatingTask;

    }

    public Cursor fetchAllRepeatingTasksInTag(TaskList taskList) {
        return database.query(TABLE, null, DBHelper.REPEATING_TASK_CATEGORY +  "=" + taskList.getTagId(), null, null, null, null);
    }

    public Cursor fetchAllRepeatingTasksSandbox() {
        return database.query(TABLE, null, DBHelper.REPEATING_TASK_CATEGORY +  " is null" , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedRepeatingTasksSandbox() {
        return database.query(TABLE, null, DBHelper.REPEATING_TASK_CATEGORY +  " is null AND " + DBHelper.COLUMN_IS_ARCHIVED +" = 0 " , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedRepeatingTasksInTag(TaskList taskList) {
        return database.query(TABLE, null, DBHelper.REPEATING_TASK_CATEGORY +  "= ? AND "+DBHelper.COLUMN_IS_ARCHIVED + " = 0" , new String[] { String.valueOf(taskList.getTagId()) }, null, null, null);

    }

    public void archiveRepeatingTask(RepeatingTask repeatingTask) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_IS_ARCHIVED, true);
        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(repeatingTask.getId()) });
        Log.d( utilFunctions.SUPER_TAG+TAG,"Repeating Task archived" + repeatingTask.getRepeatingTaskText());

    }

    public RepeatingTask saveRepeatingTaskWithTag(RepeatingTask repeatingTask, TaskList taskList) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevRepeatingTaskId(TABLE)) + 1;
        values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
        values.put(DBHelper.REPEATING_TASK_TITLE, repeatingTask.getRepeatingTaskText());
        values.put(DBHelper.REPEATING_TASK_CATEGORY, taskList.getTagId());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        Calendar e = (Calendar)c.clone();
        e.add(Calendar.DATE,30);
        e.set(Calendar.HOUR_OF_DAY,23);
        e.set(Calendar.MINUTE,59);
        e.set(Calendar.SECOND,59);
        values.put(DBHelper.REPEATING_TASK_START_DATE,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(c.getTime()));
        values.put(DBHelper.REPEATING_TASK_END_DATE,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(e.getTime()));
        Calendar d = (Calendar) c.clone();
        d.set(Calendar.YEAR,2000);
        d.set(Calendar.MONTH,0);
        d.set(Calendar.DAY_OF_MONTH,1);
        d.set(Calendar.HOUR_OF_DAY,9);
        d.set(Calendar.MINUTE,0);
        d.set(Calendar.SECOND,0);
        values.put(DBHelper.COLUMN_DUE_TIME,new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(c.getTime()));
        values.put(DBHelper.REPEATING_TASK_TYPE, RepeatingTask.Type.FIXED.getTypeValue());
        values.put(DBHelper.REPEATING_TASK_DAYS_CODE,1111111);
        values.put(DBHelper.COLUMN_HAS_REMINDER,false);
        values.put(DBHelper.COLUMN_IS_ARCHIVED,false);
        values.putNull(DBHelper.REPEATING_TASK_FREQUENCY);
        values.putNull(DBHelper.REPEATING_TASK_REMINDER_ID);
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(utilFunctions.SUPER_TAG+TAG, values.toString());
        database.insert(TABLE, null, values);
        Log.d( utilFunctions.SUPER_TAG+TAG,"Repeating Task saved" + repeatingTask.getRepeatingTaskText());
        return getRepeatingTasks(id);

    }

    public Cursor fetchAllArchivedRepeatingTasks() {
        return database.query(TABLE, null, DBHelper.COLUMN_IS_ARCHIVED +" = 1 " , null, null, null, null);
    }
    public Cursor fetchAllUnArchivedRepeatingTasksByDueDate(Calendar calendar) {
        Calendar startDate = (Calendar) calendar.clone();
        startDate.set(Calendar.HOUR_OF_DAY,0);
        startDate.set(Calendar.MINUTE,0);
        startDate.set(Calendar.SECOND,0);
        Calendar endDate = (Calendar) calendar.clone();
        endDate.set(Calendar.HOUR_OF_DAY,23);
        endDate.set(Calendar.MINUTE,59);
        endDate.set(Calendar.SECOND,59);
        //if it is fixed then is the daycode digit matching
        //if it is flexible is it needed/possible to do it today
        int dayOfWeek =startDate.get(Calendar.DAY_OF_MONTH);
        switch (dayOfWeek){
            case Calendar.SUNDAY:
                return fetchAllUnArchivedRepeatingTasksOnSunday(startDate,endDate);
            case Calendar.MONDAY:
                return fetchAllUnArchivedRepeatingTasksOnMonday(startDate,endDate);
            case Calendar.TUESDAY:
                return fetchAllUnArchivedRepeatingTasksOnTuesday(startDate,endDate);
            case Calendar.WEDNESDAY:
                return fetchAllUnArchivedRepeatingTasksOnWednesday(startDate,endDate);
            case Calendar.THURSDAY:
                return fetchAllUnArchivedRepeatingTasksOnThursday(startDate,endDate);
            case Calendar.FRIDAY:
                return fetchAllUnArchivedRepeatingTasksOnFriday(startDate,endDate);
            case Calendar.SATURDAY:
                return fetchAllUnArchivedRepeatingTasksOnSaturday(startDate,endDate);
            default:
                return fetchAllUnArchivedRepeatingTasksOnSunday(startDate,endDate);

        }

    }

    public Cursor fetchAllUnArchivedRepeatingTasksOnSunday(Calendar startDate, Calendar endDate){
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND " + DBHelper.REPEATING_TASK_TYPE  +" = 0 AND " +  DBHelper.REPEATING_TASK_START_DATE + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.REPEATING_TASK_END_DATE + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"') AND ("+DBHelper.REPEATING_TASK_DAYS_CODE+ "/1000000)  >= 1 ",null, null, null, null);
    }

    public Cursor fetchAllUnArchivedRepeatingTasksOnMonday(Calendar startDate, Calendar endDate){
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND "  + DBHelper.REPEATING_TASK_TYPE  +" = 0 AND " +  DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"') AND (("+DBHelper.REPEATING_TASK_DAYS_CODE+ "%1000000)/100000)  >= 1 ",null, null, null, null);
    }
    public Cursor fetchAllUnArchivedRepeatingTasksOnTuesday(Calendar startDate, Calendar endDate){
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND "  + DBHelper.REPEATING_TASK_TYPE  +" = 0 AND " +  DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"') AND (("+DBHelper.REPEATING_TASK_DAYS_CODE+ "%100000)/10000)  >= 1 ",null, null, null, null);
    }
    public Cursor fetchAllUnArchivedRepeatingTasksOnWednesday(Calendar startDate, Calendar endDate){
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND "  + DBHelper.REPEATING_TASK_TYPE  +" = 0 AND " +  DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"') AND (("+DBHelper.REPEATING_TASK_DAYS_CODE+ "%10000)/1000)  >= 1 ",null, null, null, null);
    }
    public Cursor fetchAllUnArchivedRepeatingTasksOnThursday(Calendar startDate, Calendar endDate){
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND "  + DBHelper.REPEATING_TASK_TYPE  +" = 0 AND " +  DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"') AND (("+DBHelper.REPEATING_TASK_DAYS_CODE+ "%1000)/100)  >= 1 ",null, null, null, null);
    }
    public Cursor fetchAllUnArchivedRepeatingTasksOnFriday(Calendar startDate, Calendar endDate){
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND "  + DBHelper.REPEATING_TASK_TYPE  +" = 0 AND " +  DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"') AND (("+DBHelper.REPEATING_TASK_DAYS_CODE+ "%100)/10)  >= 1 ",null, null, null, null);
    }
    public Cursor fetchAllUnArchivedRepeatingTasksOnSaturday(Calendar startDate, Calendar endDate){
        return database.query(TABLE, null,DBHelper.COLUMN_IS_ARCHIVED + " = 0 AND "  + DBHelper.REPEATING_TASK_TYPE  +" = 0 AND " +  DBHelper.COLUMN_DUE_TIME + " >= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(startDate.getTime()) +"') AND " + DBHelper.COLUMN_DUE_TIME + " <= Datetime('"+new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(endDate.getTime())+"') AND "+DBHelper.REPEATING_TASK_DAYS_CODE+ "%10  >= 1 ",null, null, null, null);
    }
}
