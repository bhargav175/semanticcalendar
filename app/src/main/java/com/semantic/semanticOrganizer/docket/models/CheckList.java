package com.semantic.semanticOrganizer.docket.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.docket.database.CheckListDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 07-10-2014.
 */
public class CheckList {
    private Integer Tag;
    private Integer id;
    private Integer reminderId;
    private String createdTime;
    private String checkListTitle;
    private String checkListDescription;
    private Boolean isArchived;
    private Calendar dueTime;

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }


    public CheckList(Integer id) {
        this.id = id;
    }


    public CheckList(String checkListTitle) {
        this.checkListTitle = checkListTitle;
    }
    public CheckList() {

    }


    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getTag() {
        return Tag;
    }

    public void setTag(Integer tag) {
        Tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckListTitle() {
        return checkListTitle;
    }

    public void setCheckListTitle(String checkListTitle) {
        this.checkListTitle = checkListTitle;
    }

    public static List<CheckList> getAllCheckLists(List<CheckList> checkListList, Context context) {
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        Cursor cursor= checkListDBHelper.fetchAllCheckLists();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckList checkList = checkListDBHelper.cursorToCheckList(cursor);
            checkListList.add(checkList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListDBHelper.close();
        return checkListList;
    }

    public static List<CheckList> getAllCheckListsInTag(Tag tag, Context context) {
        List<CheckList> checkListList = new ArrayList<CheckList>();
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        Cursor cursor= checkListDBHelper.fetchAllCheckListsInTag(tag);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckList checkList = checkListDBHelper.cursorToCheckList(cursor);
            checkListList.add(checkList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListDBHelper.close();
        return checkListList;

    }

    public static List<CheckList> getAllCheckListsSandbox( Context context) {
        List<CheckList> checkListList = new ArrayList<CheckList>();
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        Cursor cursor= checkListDBHelper.fetchAllCheckListsSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckList checkList = checkListDBHelper.cursorToCheckList(cursor);
            checkListList.add(checkList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListDBHelper.close();
        return checkListList;

    }
    public static List<CheckList> getAllUnArchivedCheckListsSandbox( Context context) {
        List<CheckList> checkListList = new ArrayList<CheckList>();
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        Cursor cursor= checkListDBHelper.fetchAllUnArchivedCheckListsSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckList checkList = checkListDBHelper.cursorToCheckList(cursor);
            checkListList.add(checkList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListDBHelper.close();
        return checkListList;

    }

   public static CheckList getCheckListById(Integer id, Context context) {
       CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
       checkListDBHelper.open();
       CheckList checkList=  checkListDBHelper.getCheckList(id);
       checkListDBHelper.close();
       return checkList;
    }

    public static List<CheckList> getAllUnArchivedCheckListsInTag(Tag tag, Context context) {
        List<CheckList> checkListList = new ArrayList<CheckList>();
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        Cursor cursor= checkListDBHelper.fetchAllUnArchivedCheckListsInTag(tag);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckList checkList = checkListDBHelper.cursorToCheckList(cursor);
            checkListList.add(checkList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListDBHelper.close();
        return checkListList;
    }

    public static void archiveAllCheckListsInTag(Tag tag, Context context) {
        List<CheckList> checkListList = getAllUnArchivedCheckListsInTag(tag,context);
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        for(CheckList checkList : checkListList){
                checkListDBHelper.archiveCheckList(checkList);
        }
        checkListDBHelper.close();
    }

    public static List<CheckList> getAllArchivedCheckLists(Context context) {
        List<CheckList> checkListList = new ArrayList<CheckList>();
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        Cursor cursor= checkListDBHelper.fetchAllArchivedCheckLists();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckList checkList = checkListDBHelper.cursorToCheckList(cursor);
            checkListList.add(checkList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListDBHelper.close();
        return checkListList;
    }


    public static List<CheckList> getAllUnArchivedCheckListsByDueDate(Context context, Calendar calendar) {
        List<CheckList> checkListList = new ArrayList<CheckList>();
        CheckListDBHelper checkListDBHelper = new CheckListDBHelper(context);
        checkListDBHelper.open();
        Cursor cursor= checkListDBHelper.fetchAllUnArchivedCheckListsByDueDate(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckList checkList = checkListDBHelper.cursorToCheckList(cursor);
            checkListList.add(checkList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListDBHelper.close();
        return checkListList;
    }


    public Calendar getDueTime() {
        return dueTime;
    }

    public void setDueTime(Calendar dueTime) {
        this.dueTime = dueTime;
    }

    public String getCheckListDescription() {
        return checkListDescription;
    }

    public void setCheckListDescription(String checkListDescription) {
        this.checkListDescription = checkListDescription;
    }
}
