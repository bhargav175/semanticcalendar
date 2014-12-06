package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 15-09-2014.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "Semantic Tables";


    //Common Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_TIME = "createdTime";
    public static final String COLUMN_DUE_TIME = "dueTime";
    public static final String COLUMN_COMPLETED_TIME = "completedTime";
    public static final String COLUMN_HAS_REMINDER = "hasReminder";
    public static final String COLUMN_IS_COMPLETED = "isCompleted";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_IS_ARCHIVED = "isArchived";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_PRIORITY = "priority";

    //Declare Tables
    public static final String CATEGORIES_TABLE = "categories";
    public static final String REPEATING_TASKS_TABLE = "repeatingTasks";

    public static final String TASKS_TABLE = "tasks";
    public static final String LABEL_TABLE = "labels";
    public static final String REMINDER_TABLE = "reminders";
    public static final String REPEATING_TASK_ITEMS_TABLE = "repeatingTaskItems";
    public static final String TASKS_LABELS_TABLE = "taskLabels";
    public static final String REPEATING_TASKS_LABELS_TABLE = "repeatingTaskLabels";


    //Note COlumns
    public static final String TASK_TITLE = "title";
    public static final String TASK_DESCRIPTION = "description";
    public static final String TASK_CATEGORY = "categoryId";
    public static final String TASK_REMINDER_ID = "requestId";


//Tags Columns

    public static final String CATEGORY_TITLE = "title";
    public static final String CATEGORY_DESCRIPTION = "description";
    public static final String CATEGORY_IS_ARCHIVED = "isArchived";


    //RepeatingTask

    public static final String REPEATING_TASK_TITLE = "title";
    public static final String REPEATING_TASK_DESCRIPTION = "description";
    public static final String REPEATING_TASK_REMINDER_ID = "requestId";
    public static final String REPEATING_TASK_CATEGORY = "categoryId";
    public static final String REPEATING_TASK_TYPE = "type";
    public static final String REPEATING_TASK_DAYS_CODE = "dayCode";
    public static final String REPEATING_TASK_FREQUENCY = "frequency";
    public static final String REPEATING_TASK_START_DATE = "startDate";
    public static final String REPEATING_TASK_END_DATE = "endDate";
    public static final String REPEATING_TASK_HITS = "hits";
    public static final String REPEATING_TASK_MISSES = "misses";
    public static final String REPEATING_TASK_SUCCESS_PERCENTAGE = "successPercentage";
    public static final String REPEATING_TASK_HAS_STATISTICS = "hasStatistics";


    //RepeatingTaskItem
    public static final String REPEATING_TASK_ITEM_DATE = "repeatingTaskItemDate";
    public static final String REPEATING_TASK_ITEM_REPEATING_TASK = "repeatingTaskId";


    //Reminder

    public static final String REMINDER_DAY_OF_MONTH = "dayOfMonth";
    public static final String REMINDER_MONTH_OF_YEAR = "monthOfYear";
    public static final String REMINDER_YEAR = "Year";
    public static final String REMINDER_HOUR_OF_DAY = "hourOfDay";
    public static final String REMINDER_MINUTE_OF_HOUR = "minuteOfHour";
    public static final String REMINDER_SECONDS = "seconds";
    public static final String REMINDER_IS_REPEATING = "isRepeating";
    public static final String REMINDER_INTERVAL = "interval";
    public static final String REMINDER_DURATION = "duration";


    private static final String DATABASE_NAME = "to_organize_db";
    private static final int DATABASE_VERSION = 43;




    private static final String CREATE_TABLE_TASKS = "create table if not exists "
            + TASKS_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + TASK_TITLE + " text not null, "
            + TASK_DESCRIPTION + " text , "
            + COLUMN_PRIORITY + " integer DEFAULT 70, "
            + COLUMN_STATE + " integer DEFAULT 0 , "
            + COLUMN_DUE_TIME + " DATETIME DEFAULT null, "
            + COLUMN_HAS_REMINDER + " BOOLEAN DEFAULT 0, "
            + COLUMN_IS_COMPLETED + " BOOLEAN DEFAULT 0, "
            + COLUMN_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_COMPLETED_TIME + " DATETIME DEFAULT null, "
            + COLUMN_CREATED_TIME + " DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + TASK_CATEGORY + " integer DEFAULT null, "
            + TASK_REMINDER_ID + " integer DEFAULT null "

            + ");";

    private static final String CREATE_TABLE_CATEGORIES = "create table if not exists "
            + CATEGORIES_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + CATEGORY_TITLE + " text not null, "
            + CATEGORY_DESCRIPTION + " text, "
            + COLUMN_COLOR + " integer DEFAULT 0, "
            + COLUMN_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME + " DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime'))"

            + ");";


    private static final String CREATE_TABLE_REPEATING_TASKS = "create table if not exists "
            + REPEATING_TASKS_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + REPEATING_TASK_TITLE + " text not null, "
            + REPEATING_TASK_DESCRIPTION + " text , "
            + COLUMN_PRIORITY + " integer DEFAULT 70, "
            + REPEATING_TASK_TYPE + " integer DEFAULT 1, "
            + REPEATING_TASK_DAYS_CODE + " integer DEFAULT null, "
            + REPEATING_TASK_FREQUENCY + " integer DEFAULT null, "
            + REPEATING_TASK_START_DATE + " DATETIME DEFAULT null, "
            + REPEATING_TASK_END_DATE + " DATETIME DEFAULT null, "
            + REPEATING_TASK_HITS + " integer DEFAULT 0, "
            + REPEATING_TASK_MISSES + " integer DEFAULT 0, "
            + REPEATING_TASK_SUCCESS_PERCENTAGE + " double DEFAULT 0, "
            + REPEATING_TASK_HAS_STATISTICS + " BOOLEAN DEFAULT 0, "
            + COLUMN_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME + " DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + COLUMN_DUE_TIME + " DATETIME DEFAULT null, "
            + COLUMN_HAS_REMINDER + " BOOLEAN DEFAULT 0, "
            + REPEATING_TASK_CATEGORY + " integer DEFAULT null, "
            + REPEATING_TASK_REMINDER_ID + " integer DEFAULT null "
            + ");";


    private static final String CREATE_TABLE_REPEATING_TASK_ITEMS = "create table if not exists "
            + REPEATING_TASK_ITEMS_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + REPEATING_TASK_ITEM_DATE + " DATETIME DEFAULT (DATETIME(current_date, 'localtime')), "
            + COLUMN_STATE + " integer DEFAULT 0 , "
            + COLUMN_IS_COMPLETED + " BOOLEAN DEFAULT 0, "
            + COLUMN_COMPLETED_TIME + " DATETIME DEFAULT null, "
            + COLUMN_CREATED_TIME + " DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + REPEATING_TASK_ITEM_REPEATING_TASK + " integer not null "

            + ");";


    private static final String CREATE_TABLE_REMINDERS = "create table if not exists "
            + REMINDER_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + REMINDER_YEAR + " integer DEFAULT 0 , "
            + REMINDER_MONTH_OF_YEAR + " integer DEFAULT 0 , "
            + REMINDER_DAY_OF_MONTH + " integer DEFAULT 0 , "
            + REMINDER_HOUR_OF_DAY + " integer DEFAULT 0 , "
            + REMINDER_MINUTE_OF_HOUR + " integer DEFAULT 0 , "
            + REMINDER_SECONDS + " integer DEFAULT 0 , "
            + REMINDER_IS_REPEATING + " BOOLEAN DEFAULT 0 , "
            + REMINDER_INTERVAL + " integer DEFAULT 0 , "
            + REMINDER_DURATION + " integer DEFAULT 0 , "
            + COLUMN_CREATED_TIME + " DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')) "
            + ");";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, CREATE_TABLE_TASKS);
        Log.d(TAG, CREATE_TABLE_CATEGORIES);
        Log.d(TAG, CREATE_TABLE_REPEATING_TASKS);
        Log.d(TAG, CREATE_TABLE_REPEATING_TASK_ITEMS);
        Log.d(TAG, CREATE_TABLE_REMINDERS);
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_REPEATING_TASKS);
        db.execSQL(CREATE_TABLE_REPEATING_TASK_ITEMS);
        db.execSQL(CREATE_TABLE_REMINDERS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REPEATING_TASKS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REPEATING_TASK_ITEMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REMINDER_TABLE);
        onCreate(db);
    }
}
