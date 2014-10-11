package com.semantic.semanticOrganizer.semanticcalendar.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.semanticcalendar.activities.LandingActivity;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class Note {

    public Note(){

    }

    public Note(int id, String description, String createdTime){
        this.id = id;
        this.noteText = description;
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    private int id;
    private String noteText;
    private String createdTime;

    public int getRemainderId() {
        return remainderId;
    }

    public void setRemainderId(int remainderId) {
        this.remainderId = remainderId;
    }

    private int remainderId;




    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return noteText;
    }

    private Boolean isArchived;

    public Integer getTag() {
        return Tag;
    }

    public void setTag(Integer tag) {
        Tag = tag;
    }

    private Integer Tag;

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
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
}
