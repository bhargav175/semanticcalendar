package com.bhargav.smart.smartTasks.models;

import android.content.Context;
import android.database.Cursor;

import com.bhargav.smart.smartTasks.database.TaskListDBHelper;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 28-09-2014.
 */
public class TaskList {
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }


    private Integer tagId;
    private String tagText;

    public utilFunctions.Color getTagColor() {
        return tagColor;
    }

    public void setTagColor(utilFunctions.Color tagColor) {
        this.tagColor = tagColor;
    }

    private utilFunctions.Color tagColor;


    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }

    private Calendar createdTime;

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    private String tagDescription;
    private Boolean isArchived;

    public TaskList(String tagText){
        this.tagText=tagText;
    }
    public TaskList(){

    }
    @Override
    public String toString(){
        return this.tagText;
    }

    public TaskList(int id, String tagText, String tagDescription, Boolean isArchived, Calendar createdTime){
        this.tagId =id;
        this.tagText = tagText;
        this.tagDescription = tagDescription;
        this.isArchived = isArchived;
        this.createdTime = createdTime;

    }
    public void save(Context context){
        TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
        taskListDBHelper.open();
        taskListDBHelper.saveTag(this);
        taskListDBHelper.close();
    }


    public static TaskList getTagById(int tagId,Context context) {
        TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
        taskListDBHelper.open();
        TaskList taskList = taskListDBHelper.getTag(tagId);
        taskListDBHelper.close();
        return taskList;
    }




    public static  List<TaskList> getAllTags(List<TaskList> taskList,Context context) {
        TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
        taskListDBHelper.open();
        Cursor cursor= taskListDBHelper.fetchAllTags();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TaskList tag = taskListDBHelper.cursorToTag(cursor);
            taskList.add(tag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        taskListDBHelper.close();
        return taskList;
    }


    public static  List<TaskList> getAllUnArchivedTags(Context context) {
        List<TaskList> taskList = new ArrayList<TaskList>();
        TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
        taskListDBHelper.open();
        Cursor cursor= taskListDBHelper.fetchAllUnArchivedTags();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TaskList tag = taskListDBHelper.cursorToTag(cursor);
            taskList.add(tag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        taskListDBHelper.close();
        return taskList;
    }

    public static void archiveTag(TaskList taskList,Context context){
        TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
        taskListDBHelper.open();
        taskListDBHelper.archiveTag(taskList);
        taskListDBHelper.close();

    }

    public static void archiveAllTagItems(TaskList taskList, Context context){
        OneTimeTask.archiveAllNotesInTag(taskList, context);
        RepeatingTask.archiveAllRepeatingTasksInTag(taskList, context);
    }


    public static List<TaskList> getAllArchivedTags(Context context) {
        List<TaskList> taskList = new ArrayList<TaskList>();
        TaskListDBHelper taskListDBHelper = new TaskListDBHelper(context);
        taskListDBHelper.open();
        Cursor cursor= taskListDBHelper.fetchAllArchivedTags();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TaskList tag = taskListDBHelper.cursorToTag(cursor);
            taskList.add(tag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        taskListDBHelper.close();
        return taskList;
    }
}
