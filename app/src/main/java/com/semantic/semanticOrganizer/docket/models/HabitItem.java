package com.semantic.semanticOrganizer.docket.models;

import android.content.Context;

import com.semantic.semanticOrganizer.docket.database.HabitItemDBHelper;

import java.util.Calendar;
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

    public Calendar getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Calendar currentDate) {
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
    private Calendar currentDate;
     private Integer Habit;
    private State habitItemState;



    public enum  State{
        UNSET(0), SKIPPED(1), COMPLETED_UNSUCCESSFULLY(2),COMPLETED_SUCCESSFULLY(3);

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
    public static HabitItem getHabitItemByHabitAndDate(Context context, Habit habit, Calendar date){
        HabitItemDBHelper habitItemDBHelper = new HabitItemDBHelper(context);
        habitItemDBHelper.open();
        HabitItem habitItem = habitItemDBHelper.getHabitItemByDate(date.getTime(),habit);
        habitItemDBHelper.close();
        return habitItem;

    }
    public static  List<HabitItem> getAllHabitItemsInHabit(Context context, Habit habit){
        HabitItemDBHelper habitItemDBHelper = new HabitItemDBHelper(context);
        habitItemDBHelper.open();
        List<HabitItem> habitItemList = habitItemDBHelper.getHabitItemByHabits(habit);
        habitItemDBHelper.close();
        return habitItemList;

    }

    public static HabitItem saveHabitItem(Context context, Habit habit, Calendar date){
        HabitItemDBHelper habitItemDBHelper = new HabitItemDBHelper(context);
        habitItemDBHelper.open();
        HabitItem habitItem = habitItemDBHelper.saveHabitItem(date.getTime(),habit);
        habitItemDBHelper.close();
        return habitItem;

    }
    public static HabitItem updateHabitItem(Context context, HabitItem habitItem){
        HabitItemDBHelper habitItemDBHelper = new HabitItemDBHelper(context);
        habitItemDBHelper.open();
        habitItem = habitItemDBHelper.updateHabitItem(habitItem);
        habitItemDBHelper.close();
        return habitItem;

    }
    public static String getStateString(State state){
        if(state == State.UNSET){
            return "Unset";
        }else if(state == State.SKIPPED){
            return "Skipped";
        }else if (state == State.COMPLETED_UNSUCCESSFULLY){
            return "Completed Unsuccessfully";
        }else{
            return "Completed Successfully";
        }
    }





    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return String.valueOf(habitItemState.getStateValue());
    }


}
