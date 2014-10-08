package com.semantic.semanticOrganizer.semanticcalendar.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.semanticcalendar.database.HabitDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class Habit {




    private int id;

    public String getHabitText() {
        return habitText;
    }

    public void setHabitText(String habitText) {
        this.habitText = habitText;
    }

    public Integer getTag() {
        return Tag;
    }

    public void setTag(Integer tag) {
        Tag = tag;
    }

    private String habitText;
    private String habitQuestion;
    private String createdTime;
    private String dueTime;
    private Boolean isArchived;
    private Integer Tag;
    private State habitState;

    public State getHabitState() {
        return habitState;
    }

    public void setHabitState(State habitState) {
        this.habitState = habitState;
    }

    public enum  State{
        NOT_STARTED(0), STARTED(1), INCOMPLETE(2),COMPLETED_UNSUCCESSFULLY(3),COMPLETED_SUCCESSFULLY(4);

        private int stateValue;
        State(int stateValue) {
            this.stateValue = stateValue;
        }

        public void setStateValue(int stateValue) {
            this.stateValue = stateValue;
        }

        public int getStateValue() {
            return stateValue;
        }
    }


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


    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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


    public static List<Habit> getAllHabits(List<Habit> habitList, Context context) {
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        Cursor cursor= habitDBHelper.fetchAllHabits();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Habit habit = habitDBHelper.cursorToHabit(cursor);
            habitList.add(habit);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        habitDBHelper.close();
        return habitList;
    }

    public static List<Habit> getAllHabitsInTag(Tag tag, Context context) {
        List<Habit> habitList = new ArrayList<Habit>();
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        Cursor cursor= habitDBHelper.fetchAllHabitsInTag(tag);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Habit habit = habitDBHelper.cursorToHabit(cursor);
            habitList.add(habit);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        habitDBHelper.close();
        return habitList;

    }
    public static List<Habit> getAllHabitsSandbox(Context context) {
        List<Habit> habitList = new ArrayList<Habit>();
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        Cursor cursor= habitDBHelper.fetchAllHabitsSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Habit habit = habitDBHelper.cursorToHabit(cursor);
            habitList.add(habit);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        habitDBHelper.close();
        return habitList;

    }
}
