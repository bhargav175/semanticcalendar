package com.example.admin.toorganize.models;

/**
 * Created by Admin on 16-09-2014.
 */
public class Task {
    private long id;
    private String taskText;
    private String taskDescription;
    private String taskQuestion;
    private long startMilliSeconds;

    public Task(){

    }
    public Task(int id, String taskText){
        this.id = id;
        this.taskText = taskText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return taskText;
    }

    public void setText(String taskText) {
        this.taskText = taskText;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return taskText;
    }
}
