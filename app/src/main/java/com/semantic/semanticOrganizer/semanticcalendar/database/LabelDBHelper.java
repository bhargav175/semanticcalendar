package com.semantic.semanticOrganizer.semanticcalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Label;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 16-09-2014.
 */
public class LabelDBHelper {

    private final static String TABLE = DBHelper.LABEL_TABLE;
    private final static String TAG = "Labels";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public LabelDBHelper(Context context) {
        this.context = context;

    }

    public LabelDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }

    public Cursor fetchAllLabels() {
        return database.query(TABLE, null, null,null, null, null, null);
    }



    public Label getLabel(int id) {
        Cursor cursor = database.query(TABLE, null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Label label =cursorToLabel(cursor);
        // return contact
        return label;
    }

    public int updateLabel(Label label) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.LABEL_NAME, label.getName());
        values.put(DBHelper.LABEL_COLOR, label.getColor());
        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(label.getId()) });
        Log.d( TAG,"Note updated" + label.getName());
        return 0;
    }


    private String getPrevLabelId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public Label cursorToLabel(Cursor cursor) {
        Label label = new Label();
        label.setId(cursor.getInt(0));
        label.setName(cursor.getString(1));
        label.setColor(cursor.getString(2));
        label.setCreatedTime(cursor.getString(3));
        return label;

    }


    public void deleteLabel(Label label) {
        database = dbHelper.getWritableDatabase();
        // deleting label
        database.delete(TABLE, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(label.getId()) });
        Log.d( TAG,"Note archived" + label.getName());



    }

    public Label saveLabel(Label label) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevLabelId(TABLE)) + 1;
        values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
        values.put(DBHelper.LABEL_NAME, label.getName());
        values.put(DBHelper.LABEL_COLOR, label.getColor());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        database.insert(TABLE, null, values);

        Log.d( TAG,"Note saved" + label.getName());
        return getLabel(id);
    }

}
