package com.semantic.semanticOrganizer.semanticcalendar.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 15-09-2014.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG="Data-base";


//Common Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_TIME = "createdTime";

    //Declare Tables
    public static final String TASKS_TABLE ="tasks";
    public static final String TAGS_TABLE ="tags";
    public static final String CHECKLISTS_TABLE ="checklists";
    public static final String CHECKLIST_ITEMS_TABLE ="checklistItems";
    public static final String HABITS_TABLE ="habits";

    public static final String NOTES_TABLE ="notes";
    public static final String TODO_TABLE ="todos";


    //TAsks Columns

    public static final String TASK_TITLE ="title";


//Note COlumns
    public static final String NOTE_DESCRIPTION ="description";
    public static final String NOTE_TAG ="tag_id";
    public static final String NOTE_IS_ARCHIVED ="isArchived";

//TODO COlumns
public static final String TODO_DESCRIPTION ="description";
public static final String TODO_IS_COMPLETED ="isCompleted";

//Tags Columns

    public static final String TAG_TITLE ="title";
    public static final String TAG_DESCRIPTION ="description";
    public static final String TAG_IS_ARCHIVED ="isArchived";

  //CheckList

    public static final String CHECKLIST_TITLE ="title";
    public static final String CHECKLIST_IS_ARCHIVED ="isArchived";
    public static final String CHECKLIST_TAG ="tag_id";


    //ChecklistItem
    public static final String CHECKLIST_ITEM_TEXT ="title";
    public static final String CHECKLIST_ITEM_STATE ="state";
    public static final String CHECKLIST_ITEM_CHECKLIST ="checklistId";


    //Habit

    public static final String HABIT_TEXT ="title";
    public static final String HABIT_QUESTION ="question";
    public static final String HABIT_STATE ="state";
    public static final String HABIT_IS_ARCHIVED ="isArchived";
    public static final String HABIT_TAG ="tag_id";


    private static final String DATABASE_NAME = "to_organize_db";
    private static final int DATABASE_VERSION = 15;






    private static final String CREATE_TABLE_NOTES = "create table if not exists "
            + NOTES_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + NOTE_DESCRIPTION+ " text not null, "
            + NOTE_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + NOTE_TAG + " integer DEFAULT null"
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
            + CHECKLIST_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
            + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + CHECKLIST_TAG + " integer DEFAULT null"

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
            + HABIT_TEXT + " text not null, "
         + HABIT_QUESTION+ " text , "
         + HABIT_STATE + " integer DEFAULT 0, "
         + HABIT_IS_ARCHIVED + " BOOLEAN DEFAULT 0, "
         + COLUMN_CREATED_TIME +" DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')), "
            + HABIT_TAG + " integer DEFAULT null"

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
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_TAGS);
        db.execSQL(CREATE_TABLE_CHECKLISTS);
        db.execSQL(CREATE_TABLE_CHECKLIST_ITEMS);
        db.execSQL(CREATE_TABLE_HABITS);

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
        onCreate(db);
    }
}
