package com.bhargav.smart.smartTasks.models;

import android.content.Context;

import com.bhargav.smart.smartTasks.database.RepeatingTaskItemDBHelper;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class RepeatingTaskItem {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
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

    public utilFunctions.State getHabitItemState() {
        return habitItemState;
    }

    public void setHabitItemState(utilFunctions.State habitItemState) {
        this.habitItemState = habitItemState;
    }

    private int id;
    private Calendar createdTime;
    private Calendar currentDate;
     private Integer Habit;
    private utilFunctions.State habitItemState;

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    private Boolean isCompleted;

    public Calendar getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Calendar completedTime) {
        this.completedTime = completedTime;
    }

    private Calendar completedTime;





    public RepeatingTaskItem(){

    }
    public static RepeatingTaskItem getHabitItemByHabitAndDate(Context context, RepeatingTask repeatingTask, Calendar date){
        RepeatingTaskItemDBHelper repeatingTaskItemDBHelper = new RepeatingTaskItemDBHelper(context);
        repeatingTaskItemDBHelper.open();
        RepeatingTaskItem repeatingTaskItem = repeatingTaskItemDBHelper.getHabitItemByDate(date.getTime(), repeatingTask);
        repeatingTaskItemDBHelper.close();
        return repeatingTaskItem;

    }
    public static  List<RepeatingTaskItem> getAllHabitItemsInHabit(Context context, RepeatingTask repeatingTask){
        RepeatingTaskItemDBHelper repeatingTaskItemDBHelper = new RepeatingTaskItemDBHelper(context);
        repeatingTaskItemDBHelper.open();
        List<RepeatingTaskItem> repeatingTaskItemList = repeatingTaskItemDBHelper.getHabitItemByHabits(repeatingTask);
        repeatingTaskItemDBHelper.close();
        return repeatingTaskItemList;

    }

    public static RepeatingTaskItem saveHabitItem(Context context, RepeatingTask repeatingTask, Calendar date){
        RepeatingTaskItemDBHelper repeatingTaskItemDBHelper = new RepeatingTaskItemDBHelper(context);
        repeatingTaskItemDBHelper.open();
        RepeatingTaskItem repeatingTaskItem = repeatingTaskItemDBHelper.saveHabitItem(date.getTime(), repeatingTask);
        repeatingTaskItemDBHelper.close();
        return repeatingTaskItem;

    }
    public static RepeatingTaskItem updateHabitItem(Context context, RepeatingTaskItem repeatingTaskItem){
        RepeatingTaskItemDBHelper repeatingTaskItemDBHelper = new RepeatingTaskItemDBHelper(context);
        repeatingTaskItemDBHelper.open();
        repeatingTaskItem = repeatingTaskItemDBHelper.updateHabitItem(repeatingTaskItem);
        repeatingTaskItemDBHelper.close();
        return repeatingTaskItem;

    }
    public static String getStateString(utilFunctions.State state){
        if(state == utilFunctions.State.NOT_STARTED){
            return "Unset";
        }else if(state == utilFunctions.State.STARTED){
            return "Skipped";
        }else if (state == utilFunctions.State.FAILED){
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
