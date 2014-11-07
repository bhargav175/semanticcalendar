package com.semantic.semanticOrganizer.docket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class LabelDBHelper {

    private final static String TABLE = DBHelper.LABEL_TABLE;
    private final static String NOTES_LABELS_TABLE = DBHelper.NOTES_LABELS_TABLE;
    private final static String CHECKLISTS_LABELS_TABLE = DBHelper.CHECKLISTS_LABELS_TABLE;
    private final static String HABITS_LABELS_TABLE = DBHelper.HABITS_LABELS_TABLE;
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

    public List<Label> getLabelsByTag(int tagId) {
        Cursor cursor = database.query(TABLE, null, DBHelper.LABEL_TAG + "=?",
                new String[] { String.valueOf(tagId) }, null, null, null, null);
        List<Label> labels = new ArrayList<Label>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Label label =cursorToLabel(cursor);
                labels.add(label);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        // return contact
        return labels;
    }

    public int updateLabel(Label label) {

        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.LABEL_NAME, label.getName());
        values.put(DBHelper.LABEL_COLOR, label.getColor().getColorValue());
        // updating row
        database.update(TABLE, values, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(label.getId()) });
        Log.d( TAG,"Label updated" + label.getName());
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
        label.setColor(Label.Color.values()[cursor.getInt(2)]);
        label.setTag(cursor.getInt(3));
        label.setCreatedTime(cursor.getString(4));
        return label;

    }


    public void deleteLabel(Label label) {
        database = dbHelper.getWritableDatabase();
        // deleting label
        database.delete(TABLE, DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(label.getId()) });
        Log.d( TAG,"Label archived" + label.getName());



    }

    public Label saveLabel(Label label) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer id = Integer.parseInt(getPrevLabelId(TABLE)) + 1;
        values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
        values.put(DBHelper.LABEL_NAME, label.getName());
        values.put(DBHelper.LABEL_COLOR, label.getColor().getColorValue());
        values.put(DBHelper.LABEL_TAG, label.getTag());

        //values.put("image_path", draft.getDraftImagePath());
        //TODO Location Insertion
        database.insert(TABLE, null, values);

        Log.d( TAG,"Label saved" + label.getName());
        return getLabel(id);
    }

    public List<Integer> getLabelIdsByNote(Integer id){
        Cursor cursor = database.query(NOTES_LABELS_TABLE, null, DBHelper.NOTE_LABELS_NOTE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        List<Integer> labels = new ArrayList<Integer>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer labelId = cursor.getInt(2);
                labels.add(labelId);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        // return contact
        return labels;
    }

    public List<Integer> getLabelIdsByCheckList(Integer id){
        Cursor cursor = database.query(CHECKLISTS_LABELS_TABLE, null, DBHelper.CHECKLIST_LABELS_CHECKLIST_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        List<Integer> labels = new ArrayList<Integer>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer labelId = cursor.getInt(2);
                labels.add(labelId);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        // return contact
        return labels;
    }
    public List<Integer> getLabelIdsByHabit(Integer id){
        Cursor cursor = database.query(HABITS_LABELS_TABLE, null, DBHelper.HABIT_LABELS_HABIT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        List<Integer> labels = new ArrayList<Integer>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer labelId = cursor.getInt(2);
                labels.add(labelId);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        // return contact
        return labels;
    }



    public List<Label> getLabelsByNote(Integer id) {
        Cursor cursor = database.query(NOTES_LABELS_TABLE, null, DBHelper.NOTE_LABELS_NOTE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        List<Label> labels = new ArrayList<Label>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer labelId = cursor.getInt(2);
                Label label =getLabel(labelId);
                labels.add(label);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        // return contact
        return labels;

    }

    public List<Label> getLabelsByCheckList(Integer id) {
        Cursor cursor = database.query(CHECKLISTS_LABELS_TABLE, null, DBHelper.CHECKLIST_LABELS_CHECKLIST_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        List<Label> labels = new ArrayList<Label>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer labelId = cursor.getInt(2);
                Label label =getLabel(labelId);
                labels.add(label);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        // return contact
        return labels;

    }
    public List<Label> getLabelsByHabit(Integer id) {
        Cursor cursor = database.query(HABITS_LABELS_TABLE, null, DBHelper.HABIT_LABELS_HABIT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        List<Label> labels = new ArrayList<Label>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer labelId = cursor.getInt(2);
                Label label =getLabel(labelId);
                labels.add(label);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        // return contact
        return labels;

    }

    public void saveLabelWithNote(Integer labelId, Integer noteId) {
        database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            Integer id = Integer.parseInt(getPrevLabelId(NOTES_LABELS_TABLE)) + 1;
            values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
            values.put(DBHelper.NOTE_LABELS_NOTE_ID, noteId);
            values.put(DBHelper.NOTE_LABELS_LABEL_ID, labelId);

            //values.put("image_path", draft.getDraftImagePath());
            //TODO Location Insertion
            database.insert(NOTES_LABELS_TABLE, null, values);
            Log.d( TAG,"Label saved" + "Notelabel");


    }

    public void saveLabelWithCheckList(Integer labelId, Integer checkListId) {
        database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            Integer id = Integer.parseInt(getPrevLabelId(CHECKLISTS_LABELS_TABLE)) + 1;
            values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
            values.put(DBHelper.CHECKLIST_LABELS_CHECKLIST_ID, checkListId);
            values.put(DBHelper.CHECKLIST_LABELS_LABEL_ID, labelId);

            //values.put("image_path", draft.getDraftImagePath());
            //TODO Location Insertion
            database.insert(CHECKLISTS_LABELS_TABLE, null, values);
            Log.d( TAG,"Label saved" + "CheckListLabel");



    }
    public void saveLabelWithHabit(Integer labelId, Integer habitId) {
        database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            Integer id = Integer.parseInt(getPrevLabelId(HABITS_LABELS_TABLE)) + 1;
            values.put(DBHelper.COLUMN_ID, (Integer.toString(id)));
            values.put(DBHelper.HABIT_LABELS_HABIT_ID, habitId);
            values.put(DBHelper.HABIT_LABELS_LABEL_ID, labelId);

            //values.put("image_path", draft.getDraftImagePath());
            //TODO Location Insertion
            database.insert(HABITS_LABELS_TABLE, null, values);
            Log.d( TAG,"Label saved" + "HabitLabel");



    }



    public void saveLabelsWithNote(List<Integer> selectedLabelIds, Integer noteId) {
        List<Integer> labelsWithNoteEarlier = getLabelIdsByNote(noteId);
        for(Integer labelId : selectedLabelIds){
            if(labelsWithNoteEarlier.indexOf(labelId)>-1){
                //already exists
            }
            else{
                saveLabelWithNote(labelId,noteId);
            }
        }
        for(Integer labelId: labelsWithNoteEarlier){
            if(selectedLabelIds.indexOf(labelId)<0){
                //delete this labelId
                deleteLabelWithNote(labelId,noteId);
            }
        }
    }

    public void saveLabelsWithCheckList(List<Integer> selectedLabelIds, Integer checkListId) {
        List<Integer> labelsWithCheckListEarlier = getLabelIdsByCheckList(checkListId);
        for(Integer labelId : selectedLabelIds){
            if(labelsWithCheckListEarlier.indexOf(labelId)>-1){
                //already exists
            }
            else{
                saveLabelWithCheckList(labelId,checkListId);
            }
        }
        for(Integer labelId: labelsWithCheckListEarlier){
            if(selectedLabelIds.indexOf(labelId)<0){
                //delete this labelId
                deleteLabelWithCheckList(labelId,checkListId);
            }
        }
    }

    public void saveLabelsWithHabit(List<Integer> selectedLabelIds, Integer habitId) {
        List<Integer> labelsWithHabitEarlier = getLabelIdsByNote(habitId);
        for(Integer labelId : selectedLabelIds){
            if(labelsWithHabitEarlier.indexOf(labelId)>-1){
                //already exists
            }
            else{
                saveLabelWithHabit(labelId,habitId);
            }
        }
        for(Integer labelId: labelsWithHabitEarlier){
            if(selectedLabelIds.indexOf(labelId)<0){
                //delete this labelId
                deleteLabelWithHabit(labelId,habitId);
            }
        }
    }

    public void deleteLabelWithNote(Integer labelId, Integer NoteId) {
        database = dbHelper.getWritableDatabase();
        database.delete(NOTES_LABELS_TABLE,DBHelper.NOTE_LABELS_NOTE_ID + " = ? AND " + DBHelper.NOTE_LABELS_LABEL_ID + " = ?",
                new String[]{String.valueOf(NoteId),String.valueOf(labelId)});
    }
    public void deleteLabelWithCheckList(Integer labelId, Integer checkListId) {
        database = dbHelper.getWritableDatabase();
        database.delete(CHECKLISTS_LABELS_TABLE,DBHelper.CHECKLIST_LABELS_CHECKLIST_ID + " = ? AND " + DBHelper.CHECKLIST_LABELS_LABEL_ID + " = ?",
                new String[]{String.valueOf(checkListId),String.valueOf(labelId)});
    }
    public void deleteLabelWithHabit(Integer labelId, Integer habitId) {
        database = dbHelper.getWritableDatabase();
        database.delete(HABITS_LABELS_TABLE,DBHelper.HABIT_LABELS_HABIT_ID + " = ? AND " + DBHelper.HABIT_LABELS_LABEL_ID + " = ?",
                new String[]{String.valueOf(habitId),String.valueOf(labelId)});
    }
}
