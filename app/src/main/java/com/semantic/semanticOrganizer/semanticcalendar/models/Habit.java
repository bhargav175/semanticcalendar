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
    private Boolean isArchived;
    private Integer Tag;
    private Integer requestId;
    private Type habitType;
    private Integer frequency;

    public Type getHabitType() {
        return habitType;
    }

    public void setHabitType(Type habitType) {
        this.habitType = habitType;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDaysCode() {
        return daysCode;
    }

    public void setDaysCode(Integer daysCode) {
        this.daysCode = daysCode;
    }

    private Integer duration;
    private Integer daysCode;


    public enum  Type{
        FIXED(0), FLEXIBLE(1);

        private int typeValue;
        Type(int typeValue) {
            this.typeValue = typeValue;
        }

        public void setTypeValue(int typeValue) {
            this.typeValue = typeValue;
        }

        public int getTypeValue() {
            return typeValue;
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


    public static Habit getHabitById(Integer habitId, Context context) {
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        Habit habit= habitDBHelper.getHabit(habitId);
        habitDBHelper.close();
        return habit;
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

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }
}
