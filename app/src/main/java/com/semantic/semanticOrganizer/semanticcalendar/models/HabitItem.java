package com.semantic.semanticOrganizer.semanticcalendar.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.semanticcalendar.database.HabitDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class HabitItem {


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

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Integer getHabit() {
        return Habit;
    }

    public void setHabit(Integer habit) {
        Habit = habit;
    }

    public State getHabitItemState() {
        return habitItemState;
    }

    public void setHabitItemState(State habitItemState) {
        this.habitItemState = habitItemState;
    }

    private int id;
    private String createdTime;
    private String currentDate;
     private Integer Habit;
    private State habitItemState;



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


    public HabitItem(){

    }





    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return String.valueOf(habitItemState.getStateValue());
    }


}
