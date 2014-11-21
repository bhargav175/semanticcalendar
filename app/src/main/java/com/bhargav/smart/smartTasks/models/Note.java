package com.bhargav.smart.smartTasks.models;

import android.content.Context;
import android.database.Cursor;

import com.bhargav.smart.smartTasks.database.NoteDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class Note {


    private Integer id;
    private String noteTitle;
    private String noteDescription;
    private String createdTime;
    private Integer remainderId;
    private Boolean isArchived;
    private Integer Tag;
    private Calendar dueTime;



    public Note(){

    }

    public Note(int id, String title, String createdTime){
        this.id = id;
        this.noteTitle = title;
        this.createdTime = createdTime;
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
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

    public static List<Note> getAllNotes(List<Note> noteList, Context context) {
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllNotes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDBHelper.cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;
    }

    public static List<Note> getAllNotesInTag(Tag tag, Context context) {
        List<Note> noteList = new ArrayList<Note>();
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllNotesInTag(tag);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDBHelper.cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;

    }

    public static List<Note> getAllNotesSandbox(Context context) {
        List<Note> noteList = new ArrayList<Note>();
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllNotesSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDBHelper.cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;

    }

    public static List<Note> getAllUnArchivedNotesSandbox(Context context) {
        List<Note> noteList = new ArrayList<Note>();
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllUnArchivedNotesSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDBHelper.cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;

    }

    public static Note getNote(int id, Context context) {
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
         Note note=  noteDBHelper.getNote(id);
        noteDBHelper.close();
        return note;
    }


    public static List<Note> getAllUnArchivedNotesInTag(Tag tag, Context context) {
        List<Note> noteList = new ArrayList<Note>();
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllUnArchivedNotesInTag(tag);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDBHelper.cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;
    }

    public static void archiveAllNotesInTag(Tag tag, Context context) {
        List<Note> noteList = getAllUnArchivedNotesInTag(tag,context);
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();

        for(Note note:noteList){
            noteDBHelper.archiveNote(note);
        }
        noteDBHelper.close();
    }

    public static List<Note> getAllArchivedNotes(Context context) {
        List<Note> noteList = new ArrayList<Note>();
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllArchivedNotes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDBHelper.cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;
    }

    public static List<Note> getAllUnArchivedNotesByDueDate(Context context,Calendar calendar){
        List<Note> noteList = new ArrayList<Note>();
        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
        noteDBHelper.open();
        Cursor cursor= noteDBHelper.fetchAllUnArchivedNotesByDueDate(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = noteDBHelper.cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        noteDBHelper.close();
        return noteList;
    }

    public Calendar getDueTime() {
        return dueTime;
    }

    public void setDueTime(Calendar dueTime) {
        this.dueTime = dueTime;
    }
}