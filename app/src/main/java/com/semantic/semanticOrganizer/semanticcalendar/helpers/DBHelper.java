package com.semantic.semanticOrganizer.semanticcalendar.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 15-09-2014.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG="Semantic Tables";


//Common Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_TIME = "createdTime";
    public static final String COLUMN_DUE_TIME = "dueTime";

    //Declare Tables
    public static final String TASKS_TABLE ="tasks";
    public static final String TAGS_TABLE ="tags";
    public static final String CHECKLISTS_TABLE ="checklists";
    public static final String CHECKLIST_ITEMS_TABLE ="checklistItems";
    public static final String HABITS_TABLE ="habits";

    public static final String NOTES_TABLE ="notes";
    public static final String LABEL_TABLE ="labels";
    public static final String REMINDER_TABLE ="reminders";
    public static final String HABIT_ITEMS_TABLE ="habitItems";
    public static final String NOTES_LABELS_TABLE ="noteLabels";
    public static final String CHECKLISTS_LABELS_TABLE ="checkListsLabels";
    public static final String HABITS_LABELS_TABLE ="habitsLabels";


    //TAsks Columns

    public static final String TASK_TITLE ="title";


//Note COlumns
    public static final String NOTE_TITLE ="title";
    public static final String NOTE_DESCRIPTION ="description";
    public static final String NOTE_TAG ="tagId";
    public static final String NOTE_IS_ARCHIVED ="isArchived";
    public static final String NOTE_REQUEST_ID ="requestId";


//Tags Columns

    public static final String TAG_TITLE ="title";
    public static final String TAG_DESCRIPTION ="description";
    public static final String TAG_IS_ARCHIVED ="isArchived";

  //CheckList

    public static final String CHECKLIST_TITLE ="title";
    public static final String CHECKLIST_DESCRIPTION ="description";
    public static final String CHECKLIST_IS_ARCHIVED ="isArchived";
    public static final String CHECKLIST_TAG ="tagId";
    public static final String CHECKLIST_REQUEST_ID ="requestId";


    //ChecklistItem
    public static final String CHECKLIST_ITEM_TEXT ="title";
    public static final String CHECKLIST_ITEM_STATE ="state";
    public static final String CHECKLIST_ITEM_CHECKLIST ="checklistId";


    //Habit

    public static final String HABIT_TITLE ="title";
    public static final String HABIT_DESCRIPTION ="description";
    public static final String HABIT_REQUEST_ID ="requestId";
    public static final String HABIT_IS_ARCHIVED ="isArchived";
    public static final String HABIT_TAG ="tagId";
    public static final String HABIT_TYPE="type";
    public static final String HABIT_DAYS_CODE="dayCode";
    public static final String HABIT_DURATION="numberOfDays";
    public static final String HABIT_FREQUENCY="frequency";


    //HabitItem
    public static final String HABIT_ITEM_DATE ="habitItemDate";
    public static final String HABIT_ITEM_STATE ="state";
    public static final String HABIT_ITEM_HABIT ="habitId";




    //Reminder

    public static final String REMINDER_DAY_OF_MONTH ="dayOfMonth";
    public static final String REMINDER_MONTH_OF_YEAR ="monthOfYear";
    public static final String REMINDER_YEAR ="Year";
    public static final String REMINDER_HOUR_OF_DAY ="hourOfDay";
    public static final String REMINDER_MINUTE_OF_HOUR ="minuteOfHour";
    public static final String REMINDER_SECONDS="seconds";
    public static final String REMINDER_IS_REPEATING="isRepeating";
    public static final String REMINDER_INTERVAL="interval";
    public static final String REMINDER_DURATION="duration";



    private static final String DATABASE_NAME = "to_organize_db";
    private static final int DATABASE_VERSION = 27;



    //Labels
    public static final String LABEL_NAME ="labelName";
    public static final String LABEL_TAG ="tagId";
    public static final String LABEL_COLOR ="labelColor";


    //Note labels
    public static final String NOTE_LABELS_NOTE_ID ="noteId";
    public static final String NOTE_LABELS_LABEL_ID ="labelId";


    //checklist labels
    public static final String CHECKLIST_LABELS_CHECKLIST_ID ="checkListId";
    public static final String CHECKLIST_LABELS_LABEL_ID ="labelId";

    //habit labels

    public static final String HABIT_LABELS_HABIT_ID ="habitId";
    public static final String HABIT_LABELS_LABEL_ID ="labelId";




    private static final String CREATE_TABLE_NOTES = "create table if not exists "
            + NOTES_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + NOTE_TITLE+ " text not null, "
            + NOTE_DESCRIPTION+ " text , "
            + NOTE_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + NOTE_TAG + " integer DEFAULT null, "
            + NOTE_REQUEST_ID + " integer DEFAULT null, "
            + COLUMN_DUE_TIME + " DATETIME DEFAULT null"
            +");";

    private static final String CREATE_TABLE_TAGS = "create table if not exists "
            + TAGS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + TAG_TITLE + " text not null, "
            + TAG_DESCRIPTION + " text, "
            + TAG_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime'))"

            +");";

    private static final String CREATE_TABLE_CHECKLISTS = "create table if not exists "
            + CHECKLISTS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + CHECKLIST_TITLE + " text not null, "
            + CHECKLIST_DESCRIPTION + " description , "
            + CHECKLIST_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + CHECKLIST_TAG + " integer DEFAULT null, "
            + CHECKLIST_REQUEST_ID + " integer DEFAULT null, "
            + COLUMN_DUE_TIME + " DATETIME DEFAULT null"


            +");";

    private static final String CREATE_TABLE_CHECKLIST_ITEMS = "create table if not exists "
            + CHECKLIST_ITEMS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + CHECKLIST_ITEM_TEXT + " text not null, "
            + CHECKLIST_ITEM_STATE + " integer DEFAULT 0, "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + CHECKLIST_ITEM_CHECKLIST + " integer not null"

            +");";
 private static final String CREATE_TABLE_HABITS = "create table if not exists "
            + HABITS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + HABIT_TITLE + " text not null, "
            + HABIT_DESCRIPTION + " text , "
            + HABIT_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + HABIT_TYPE + " integer DEFAULT 1, "
            + HABIT_DAYS_CODE + " integer DEFAULT null, "
            + HABIT_FREQUENCY + " integer DEFAULT null, "
            + HABIT_DURATION + " integer DEFAULT null, "
         + HABIT_TAG + " integer DEFAULT null, "
         + HABIT_REQUEST_ID + " integer DEFAULT null, "
         + COLUMN_DUE_TIME + " DATETIME DEFAULT null"


         +");";


    private static final String CREATE_TABLE_HABIT_ITEMS = "create table if not exists "
            + HABIT_ITEMS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + HABIT_ITEM_DATE + " DATETIME DEFAULT (DATETIME(current_date, 'localtime')), "
            + HABIT_ITEM_STATE+ " integer DEFAULT 0 , "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + HABIT_ITEM_HABIT+ " integer not null"
            +");";


    private static final String CREATE_TABLE_REMINDERS = "create table if not exists "
            + REMINDER_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + REMINDER_YEAR+ " integer DEFAULT 0 , "
            + REMINDER_MONTH_OF_YEAR+ " integer DEFAULT 0 , "
            + REMINDER_DAY_OF_MONTH+ " integer DEFAULT 0 , "
            + REMINDER_HOUR_OF_DAY+ " integer DEFAULT 0 , "
            + REMINDER_MINUTE_OF_HOUR+ " integer DEFAULT 0 , "
            + REMINDER_SECONDS+ " integer DEFAULT 0 , "
            + REMINDER_IS_REPEATING+ " BOOLEAN DEFAULT 0 , "
            + REMINDER_INTERVAL+ " integer DEFAULT 0 , "
            + REMINDER_DURATION+ " integer DEFAULT 0 , "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')) "
            +");";


    private static final String CREATE_TABLE_LABELS = "create table if not exists "
            + LABEL_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + LABEL_NAME + " text not null, "
            + LABEL_COLOR+ " text DEFAULT null , "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime'))"
            +");";


    private static final String CREATE_TABLE_NOTE_LABELS = "create table if not exists "
            + NOTES_LABELS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + NOTE_LABELS_NOTE_ID + " text not null, "
            + NOTE_LABELS_LABEL_ID+ " integer DEFAULT null , "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime'))"
            +");";


    private static final String CREATE_TABLE_CHECKLIST_LABELS = "create table if not exists "
            + CHECKLISTS_LABELS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + CHECKLIST_LABELS_CHECKLIST_ID + " text not null, "
            + CHECKLIST_LABELS_LABEL_ID+ " integer DEFAULT null , "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime'))"
            +");";


    private static final String CREATE_TABLE_HABIT_LABELS = "create table if not exists "
            + HABITS_LABELS_TABLE + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + HABIT_LABELS_HABIT_ID + " text not null, "
            + HABIT_LABELS_LABEL_ID+ " integer DEFAULT null , "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime'))"
            +");";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,CREATE_TABLE_NOTES);
        Log.d(TAG,CREATE_TABLE_TAGS);
        Log.d(TAG,CREATE_TABLE_CHECKLISTS);
        Log.d(TAG,CREATE_TABLE_CHECKLIST_ITEMS);
        Log.d(TAG,CREATE_TABLE_HABITS);
        Log.d(TAG,CREATE_TABLE_HABIT_ITEMS);
        Log.d(TAG,CREATE_TABLE_REMINDERS);
        Log.d(TAG,CREATE_TABLE_LABELS);
        Log.d(TAG,CREATE_TABLE_NOTE_LABELS);
        Log.d(TAG,CREATE_TABLE_CHECKLIST_LABELS);
        Log.d(TAG,CREATE_TABLE_HABIT_LABELS);
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_TAGS);
        db.execSQL(CREATE_TABLE_CHECKLISTS);
        db.execSQL(CREATE_TABLE_CHECKLIST_ITEMS);
        db.execSQL(CREATE_TABLE_HABITS);
        db.execSQL(CREATE_TABLE_HABIT_ITEMS);
        db.execSQL(CREATE_TABLE_REMINDERS);
        db.execSQL(CREATE_TABLE_LABELS);
        db.execSQL(CREATE_TABLE_NOTE_LABELS);
        db.execSQL(CREATE_TABLE_CHECKLIST_LABELS);
        db.execSQL(CREATE_TABLE_HABIT_LABELS);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKLISTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKLIST_ITEMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HABITS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HABIT_ITEMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REMINDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LABEL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_LABELS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKLISTS_LABELS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HABITS_LABELS_TABLE);
        onCreate(db);
    }
}
