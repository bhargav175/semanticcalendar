package com.semantic.semanticOrganizer.semanticcalendar.models;

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




    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return noteText;
    }

    private Boolean isArchived;

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }
}
