package com.bhargav.smart.smartTasks.models;

import android.content.Context;
import android.database.Cursor;

import com.bhargav.smart.smartTasks.database.OneTimeTaskDBHelper;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class OneTimeTask {


    private Integer id;
    private String noteTitle;
    private String noteDescription;
    private Calendar createdTime;
    private Integer remainderId;
    private Boolean isArchived;
    private Boolean isReminded;
    private Boolean isCompleted;
    private Integer Tag;
    private Calendar dueTime;
 private Calendar completedTime;



    private utilFunctions.State taskItemState;



    public OneTimeTask(){

    }

    public OneTimeTask(int id, String title, Calendar createdTime){
        this.id = id;
        this.noteTitle = title;
        this.createdTime = createdTime;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public Calendar getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Calendar completedTime) {
        this.completedTime = completedTime;
    }

    public Boolean getIsReminded() {
        return isReminded;
    }

    public void setIsReminded(Boolean isReminded) {
        this.isReminded = isReminded;
    }
    public utilFunctions.State getTaskItemState() {
        return taskItemState;
    }

    public void setTaskItemState(utilFunctions.State taskItemState) {
        this.taskItemState = taskItemState;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getRemainderId() {
        return remainderId;
    }

    public void setRemainderId(Integer remainderId) {
        this.remainderId = remainderId;
    }

    public Boolean hasReminder(){
        if(this.getRemainderId() ==null){
            return false;
        }else{
            return true;
        }
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    private Integer priority;


    public void setDefaultProperties(){
        this.setNoteDescription("");
        this.setTaskItemState(utilFunctions.State.NOT_STARTED);
        this.setRemainderId(null);
        this.setPriority(70);
        this.setIsCompleted(false);
        this.setCompletedTime(null);
        this.setIsArchived(false);
        this.setDueTime(null);
        this.setIsReminded(false);

    }



    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return noteTitle;
    }


    public Integer getTag() {
        return Tag;
    }

    public void setTag(Integer tag) {
        Tag = tag;
    }


    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }



    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public static List<OneTimeTask> getAllNotes(List<OneTimeTask> oneTimeTaskList, Context context) {
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllNotes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;
    }

    public static List<OneTimeTask> getAllNotesInTag(TaskList taskList, Context context) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllNotesInTag(taskList);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;

    }

    public static List<OneTimeTask> getAllNotesSandbox(Context context) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllNotesSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;

    }

    public static List<OneTimeTask> getAllUnArchivedNotesSandbox(Context context) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllUnArchivedNotesSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;

    }

    public static OneTimeTask getNote(int id, Context context) {
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
         OneTimeTask oneTimeTask =  oneTimeTaskDBHelper.getNote(id);
        oneTimeTaskDBHelper.close();
        return oneTimeTask;
    }


    public static List<OneTimeTask> getAllUnArchivedNotesInTag(TaskList taskList, Context context) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllUnArchivedNotesInTag(taskList);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;
    }

    public static void archiveAllNotesInTag(TaskList taskList, Context context) {
        List<OneTimeTask> oneTimeTaskList = getAllUnArchivedNotesInTag(taskList,context);
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();

        for(OneTimeTask oneTimeTask : oneTimeTaskList){
            oneTimeTaskDBHelper.archiveNote(oneTimeTask);
        }
        oneTimeTaskDBHelper.close();
    }

    public static List<OneTimeTask> getAllArchivedNotes(Context context) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllArchivedNotes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;
    }

    public static List<OneTimeTask> getAllUnArchivedNotesByDueDate(Context context,Calendar calendar){
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllUnArchivedNotesByDueDate(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;
    }

    public Calendar getDueTime() {
        return dueTime;
    }

    public void setDueTime(Calendar dueTime) {
        this.dueTime = dueTime;
    }
    public static String getMetaText(OneTimeTask oneTimeTask) {
        if(oneTimeTask.getDueTime()!=null){
            return new SimpleDateFormat(utilFunctions.dateTimeFormat).format(oneTimeTask.getDueTime().getTime());
        }else{
            return "None";
        }
    }

    public static List<OneTimeTask> getAllUnArchivedNotesByWeek(Context context, Calendar calendar) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllUnArchivedNotesByWeek(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;
    }

    public static void saveState(Context context,OneTimeTask oneTimeTask) {
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        oneTimeTaskDBHelper.updateState(oneTimeTask);
        oneTimeTaskDBHelper.close();
    }

    public static List<OneTimeTask> getAllUnArchivedNotesByMonth(Context context, Calendar calendar) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllUnArchivedNotesByMonth(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;
    }

    public static List<OneTimeTask> getAllUnArchivedUpComingNotes(Context context, Calendar calendar) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllUnArchivedUpComingNotes(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;

    }

    public static List<OneTimeTask> getAllUnArchivedOverDueNotes(Context context, Calendar calendar) {
        List<OneTimeTask> oneTimeTaskList = new ArrayList<OneTimeTask>();
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();
        Cursor cursor= oneTimeTaskDBHelper.fetchAllUnArchivedOverdueNotes(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OneTimeTask oneTimeTask = oneTimeTaskDBHelper.cursorToNote(cursor);
            oneTimeTaskList.add(oneTimeTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        oneTimeTaskDBHelper.close();
        return oneTimeTaskList;
    }


    public static void removeReminders(TaskList taskList, Context context) {
        List<OneTimeTask> oneTimeTaskList = getAllUnArchivedNotesInTag(taskList,context);
        OneTimeTaskDBHelper oneTimeTaskDBHelper = new OneTimeTaskDBHelper(context);
        oneTimeTaskDBHelper.open();

        for(OneTimeTask oneTimeTask : oneTimeTaskList){
            oneTimeTaskDBHelper.removeReminder(oneTimeTask,context);
        }
        oneTimeTaskDBHelper.close();
    }
}
