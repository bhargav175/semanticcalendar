package com.example.admin.toorganize.helpers;

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
    public static final String NOTES_TABLE ="notes";
    public static final String TODO_TABLE ="todos";
    public static final String HABIT_TABLE ="habits";


    //TAsks Columns

    public static final String TASK_TITLE ="title";


//Note COlumns
    public static final String NOTE_DESCRIPTION ="description";
    public static final String NOTE_IS_DASHBOARD_HEAD ="isHead";

//TODO COlumns
public static final String TODO_DESCRIPTION ="description";
public static final String TODO_IS_COMPLETED ="isCompleted";



    private static final String DATABASE_NAME = "to_organize_db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_TASKS = "create table if not exists "
            + TASKS_TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + TASK_TITLE + " text not null, "
            + COLUMN_CREATED_TIME +"TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            +");";

    private static final String CREATE_TABLE_NOTES = "create table if not exists "
            + NOTES_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + NOTE_DESCRIPTION+ " text not null, "
            + COLUMN_CREATED_TIME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + NOTE_IS_DASHBOARD_HEAD + " BOOLEAN DEFAULT FALSE"
            +");";

 private static final String CREATE_TABLE_TODOS = "create table if not exists "
            + TODO_TABLE + "("
         + COLUMN_ID+ " integer primary key autoincrement, "
         + TODO_DESCRIPTION + " text not null, "
         + TODO_IS_COMPLETED + " BOOLEAN DEFAULT FALSE, "
         + COLUMN_CREATED_TIME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"

         +");";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,CREATE_TABLE_TASKS);
        Log.d(TAG,CREATE_TABLE_NOTES);
        Log.d(TAG,CREATE_TABLE_TODOS);
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_TODOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }
}
