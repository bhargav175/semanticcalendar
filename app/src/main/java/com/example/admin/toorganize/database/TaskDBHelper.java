package com.example.admin.toorganize.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.toorganize.helpers.DBHelper;
import com.example.admin.toorganize.models.Note;
import com.example.admin.toorganize.models.Task;

/**
 * Created by Admin on 16-09-2014.
 */
public class TaskDBHelper {
    private final static String TASKS_TABLE = "tasks";
    private final static String NOTES_TABLE = "notes";
    private final static String TAG = "TAG-DBAdapter";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public TaskDBHelper(Context context) {
        this.context = context;

    }

    public TaskDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }

    public Cursor fetchAllTasks() {
        return database.query(TASKS_TABLE, null, null, null, null, null, null);
    }

    public Task getTask(int id) {
        Cursor cursor = database.query(TASKS_TABLE, new String[] {DBHelper.COLUMN_ID, DBHelper.TASK_TITLE }, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)),
               cursor.getString(1));
        // return contact
        return task;
    }

    public int updateTask(Task task, String taskText) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TASK_TITLE, taskText);

        // updating row
        return database.update(TASKS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }

    public Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getLong(0));
        task.setText(cursor.getString(1));
        return task;
    }

    public void saveTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("id", (Integer.toString(Integer.parseInt(getPrevTaskId(TASKS_TABLE)) + 1)));
        values.put("title", task.getText());
        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(TASKS_TABLE, null, values);

        Toast.makeText(context,task.getText(), Toast.LENGTH_LONG).show();
    }




    private String getPrevTaskId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }


}
