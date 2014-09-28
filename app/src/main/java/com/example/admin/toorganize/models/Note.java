package com.example.admin.toorganize.models;

/**
 * Created by Admin on 16-09-2014.
 */
public class Note {

    public Note(){

    }

    public Note(int id, String description, Boolean isDashboardHead){
        this.id = id;
        this.noteText = description;
        this.isDashboardHead=isDashboardHead;
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

    public Boolean getIsDashboardHead() {
        return isDashboardHead;
    }

    public void setIsDashboardHead(Boolean isDashboardHead) {
        this.isDashboardHead = isDashboardHead;
    }

    private Boolean isDashboardHead;



    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return noteText;
    }
}
