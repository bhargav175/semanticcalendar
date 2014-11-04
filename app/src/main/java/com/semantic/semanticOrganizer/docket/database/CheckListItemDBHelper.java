package com.semantic.semanticOrganizer.docket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.CheckListItem;

/**
 * Created by Admin on 16-09-2014.
 */
public class CheckListItemDBHelper {

    private final static String CHECKLIST_ITEMS_TABLE = DBHelper.CHECKLIST_ITEMS_TABLE;
    private final static String TAG = "CheckListItemsave";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public CheckListItemDBHelper(Context context) {
        this.context = context;

    }

    public CheckListItemDBHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();

    }


    public Cursor fetchAllCheckListItems() {
        return database.query(CHECKLIST_ITEMS_TABLE, null, null, null, null, null, null);
    }

    public CheckListItem getCheckListItem(int id) {
        Cursor cursor = database.query(CHECKLIST_ITEMS_TABLE, null, DBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CheckListItem checkListItem =cursorToCheckListItem(cursor);
        // return contact
        return checkListItem;
    }

    public int updateCheckListItem(CheckListItem checkListItem) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CHECKLIST_ITEM_TEXT, checkListItem.getCheckListItemText());
        values.put(DBHelper.CHECKLIST_ITEM_STATE, checkListItem.getCheckListItemState().getStateValue());



        // updating row
        database.update(CHECKLIST_ITEMS_TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(checkListItem.getId()) });
        Toast.makeText(context,"CheckListItem "+ checkListItem.getCheckListItemText()+" updated", Toast.LENGTH_SHORT).show();


        return 0;
    }


    public void saveCheckListItem(CheckListItem checkListItem) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CHECKLIST_ITEM_TEXT, checkListItem.getCheckListItemText());
        values.put(DBHelper.CHECKLIST_ITEM_STATE, checkListItem.getCheckListItemState().getStateValue());
        values.put(DBHelper.CHECKLIST_ITEM_CHECKLIST, checkListItem.getCheckList());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        Log.d(TAG, values.toString());
        database.insert(CHECKLIST_ITEMS_TABLE, null, values);
        Toast.makeText(context,"CheckListItem "+ checkListItem.getCheckListItemText()+" saved", Toast.LENGTH_SHORT).show();

    }
    private String getPrevCheckListItemId(String tableName) {
        try {
            Cursor cr = database.query(tableName, null, null, null, null, null, null);
            cr.moveToLast();
            return cr.getString(cr.getColumnIndex("id"));
        } catch (Exception e) {
            return "-1";
        }
    }

    public CheckListItem cursorToCheckListItem(Cursor cursor) {
        CheckListItem checkListItem = new CheckListItem();
        checkListItem.setId(cursor.getInt(0));
        checkListItem.setCheckListItemText(cursor.getString(1));
        checkListItem.setCheckListItemState(CheckListItem.State.values()[(cursor.getInt(2))]);
        checkListItem.setCreatedTime(cursor.getString(3));
        if (!cursor.isNull(4)){
            checkListItem.setCheckList(cursor.getInt(4));
        }

        return checkListItem;

    }

    public Cursor fetchAllCheckListItemsInCheckList(Integer checkListId) {
        return database.query(CHECKLIST_ITEMS_TABLE, null, DBHelper.CHECKLIST_ITEM_CHECKLIST +  "=" + checkListId, null, null, null, null);
    }

    public void deleteCheckListItem(Integer i) {
         database.delete(CHECKLIST_ITEMS_TABLE,  DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(i) });

    }
}
