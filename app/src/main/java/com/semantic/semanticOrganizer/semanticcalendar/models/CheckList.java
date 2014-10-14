package com.semantic.semanticOrganizer.semanticcalendar.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.semanticcalendar.database.CheckListDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07-10-2014.
 */
public class CheckList {
    private Integer Tag;
    private Integer id;
    private String createdTime;
    private String checkListText;
    private Boolean isArchived;
    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    private int reminderId;

    public CheckList(Integer id) {
        this.id = id;
    }


    public CheckList(String checkListText) {
        this.checkListText = checkListText;
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

    public String getCheckListText() {
        return checkListText;
    }

    public void setCheckListText(String checkListText) {
        this.checkListText = checkListText;
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
}
