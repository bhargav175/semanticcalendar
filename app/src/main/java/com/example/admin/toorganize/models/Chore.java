package com.example.admin.toorganize.models;

/**
 * Created by Admin on 16-09-2014.
 */
public class Chore {
    private long id;
    private String noteText;
    private String noteTitle;

    public Chore(){

    }
    public Chore(int id, String taskText){
        this.id = id;
        this.noteText = taskText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return noteText;
    }

    public void setText(String taskText) {
        this.noteText = taskText;
    }
    public String getTitle() {
        return noteTitle;
    }

    public void setTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return noteText;
    }
}
