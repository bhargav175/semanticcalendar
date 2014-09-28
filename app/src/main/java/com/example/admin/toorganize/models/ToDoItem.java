package com.example.admin.toorganize.models;

/**
 * Created by Admin on 16-09-2014.
 */
public class ToDoItem {

    public ToDoItem(){

    }

    public ToDoItem(int id, String description, Boolean isCompleted){
        this.id = id;
        this.todoText = description;
        this.isCompleted=isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String geTtodoText() {
        return todoText;
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    private int id;
    private String todoText;
    private String createdTime;

    public String getTodoText() {
        return todoText;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    private String dueTime;
    private Boolean isCompleted;




    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return todoText;
    }
}
