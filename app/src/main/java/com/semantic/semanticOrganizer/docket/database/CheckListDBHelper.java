package com.semantic.semanticOrganizer.docket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.CheckList;
import com.semantic.semanticOrganizer.docket.models.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 16-09-2014.
 */
public class CheckListDBHelper {

    private final static String CHECKLISTS_TABLE = DBHelper.CHECKLISTS_TABLE;
    private final static String TAG = "CheckListsave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public CheckListDBHelper(Context context) {
        this.context = context;

    }

    public CheckListDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }


    public Cursor fetchAllCheckLists() {
        return database.query(CHECKLISTS_TABLE, null, null, null, null, null, null);
    }

    public Cursor fetchAllUnArchivedCheckLists() {
        return database.query(CHECKLISTS_TABLE, null, DBHelper.CHECKLIST_IS_ARCHIVED + "=?",  new String[] { String.valueOf(false) }, null, null, null);
    }

    public CheckList getCheckList(int id) {
        Cursor cursor = database.query(CHECKLISTS_TABLE, null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CheckList checkList =cursorToCheckList(cursor);
        // return contact
        return checkList;
    }

    public int updateCheckList(CheckList checkList) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CHECKLIST_TITLE, checkList.getCheckListTitle());
        values.put(DBHelper.CHECKLIST_DESCRIPTION, checkList.getCheckListDescription());
        values.put(DBHelper.CHECKLIST_IS_ARCHIVED, checkList.getIsArchived());
        values.put(DBHelper.CHECKLIST_TAG, checkList.getTag());
        values.put(DBHelper.CHECKLIST_REQUEST_ID, checkList.getReminderId());
        if(checkList.getDueTime()!=null){
            values.put(DBHelper.COLUMN_DUE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(checkList.getDueTime().getTime()));
        }

        // updating row
        database.update(CHECKLISTS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(checkList.getId()) });
        Log.d( TAG,"CheckList updated" + checkList.getCheckListTitle());


        return 0;
    }


    public void saveCheckList(CheckList checkList) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (Integer.toString(Integer.parseInt(getPrevCheckListId(CHECKLISTS_TABLE)) + 1)));
        values.put(DBHelper.CHECKLIST_TITLE, checkList.getCheckListTitle());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(CHECKLISTS_TABLE, null, values);
        Log.d( TAG,"CheckList saved" + checkList.getCheckListTitle());
        ;

    }
    private String getPrevCheckListId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public CheckList cursorToCheckList(Cursor cursor) {
        CheckList checkList = new CheckList();
        checkList.setId(cursor.getInt(0));
        checkList.setCheckListTitle(cursor.getString(1));
        checkList.setCheckListDescription(cursor.getString(2));
        checkList.setIsArchived(cursor.getInt(3)>0);
        checkList.setCreatedTime(cursor.getString(4));
        if (!cursor.isNull(5)){
            checkList.setTag(cursor.getInt(5));
        }
        if (!cursor.isNull(6)){
            checkList.setReminderId(cursor.getInt(6));
        }
        if(!cursor.isNull(7)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(cursor.getString(7)));
                checkList.setDueTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return checkList;

    }

    public Cursor fetchAllCheckListsInTag(Tag tag) {
        return database.query(CHECKLISTS_TABLE, null, DBHelper.CHECKLIST_TAG +  "=" + tag.getTagId(), null, null, null, null);
    }

    public Cursor fetchAllCheckListsSandbox() {
        return database.query(CHECKLISTS_TABLE, null, DBHelper.CHECKLIST_TAG +  " is null" , null, null, null, null);
    }

    public Cursor fetchAllUnArchivedCheckListsSandbox() {
        return database.query(CHECKLISTS_TABLE, null, DBHelper.CHECKLIST_TAG +  " is null AND "+DBHelper.CHECKLIST_IS_ARCHIVED+" = 0 "  , null, null, null, null);
    }


    public Cursor fetchAllUnArchivedCheckListsInTag(Tag tag) {

        return database.query(CHECKLISTS_TABLE, null, DBHelper.CHECKLIST_TAG +  "= ? AND "+DBHelper.CHECKLIST_IS_ARCHIVED + " = 0" , new String[] { String.valueOf(tag.getTagId()) }, null, null, null);

    }

    public void archiveCheckList(CheckList checkList) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CHECKLIST_IS_ARCHIVED, true);
        database.update(CHECKLISTS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(checkList.getId())});
        Log.d( TAG,"CheckList archived" + checkList.getCheckListTitle());
    }

    public CheckList saveCheckListWithTag(CheckList checkList, Tag tag) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevCheckListId(CHECKLISTS_TABLE))+ 1;
        values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
        values.put(DBHelper.CHECKLIST_TITLE, checkList.getCheckListTitle());
        values.put(DBHelper.CHECKLIST_TAG, tag.getTagId());

        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(CHECKLISTS_TABLE, null, values);
        Log.d( TAG,"CheckList saved" + checkList.getCheckListTitle());
        return getCheckList(id) ;
    }

    public Cursor fetchAllArchivedCheckLists() {
        return database.query(CHECKLISTS_TABLE, null, DBHelper.CHECKLIST_IS_ARCHIVED+" = 1"  , null, null, null, null);
    }
}
