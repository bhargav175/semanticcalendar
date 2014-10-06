package com.semantic.semanticOrganizer.semanticcalendar.models;

/**
 * Created by Admin on 16-09-2014.
 */
public class Habit {



    private enum state {
        COMPLETED_SUCCESSFULLY,COMPLETED_UNSUCCESSFULLY, SKIPPED, INCOMPLETE;
    }
    private int id;
    private String habitText;
    private String habitQuestion;
    private String createdTime;
    private String dueTime;
    private Boolean isArchived;

    public Habit(){

    }

    public Habit(int id, String habitText){
        this.id = id;
        this.habitText = habitText;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String geTtodoText() {
        return habitText;
    }

    public void setTodoText(String todoText) {
        this.habitText = todoText;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }


    public String getTodoText() {
        return habitText;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getHabitQuestion() {
        return habitQuestion;
    }

    public void setHabitQuestion(String habitQuestion) {
        this.habitQuestion = habitQuestion;
    }


    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }


    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return habitText;
    }
}
