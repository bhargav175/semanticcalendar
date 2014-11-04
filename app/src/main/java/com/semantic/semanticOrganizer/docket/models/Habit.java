package com.semantic.semanticOrganizer.docket.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.docket.database.HabitDBHelper;
import com.semantic.semanticOrganizer.docket.database.HabitItemDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class Habit {


    private String habitText;
    private String habitDescription;
    private String createdTime;
    private Boolean isArchived;
    private Integer Tag;
    private Integer requestId;
    private Type habitType;
    private Integer frequency;
    private Calendar dueTime;

    public static String[] frequencyStrings = {"4 times a week","3 times a week", "2 times a week","Once a week","Once every 2 weeks","Once a month","Once every 2 months", "Once every 6 months", "Once a year" };
    public static String[] durationStrings = {"1 week", "2 weeks","3 weeks" ,"1 month", "2 months", "3 months", "6 months", "1 year" ,"Forever" };
    private Integer id;

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

    public HabitItem getHabitItemToday(Context context) {
        if(this.getId()!=null){
            HabitItemDBHelper habitItemDBHelper = new HabitItemDBHelper(context);
            habitItemDBHelper.open();
            HabitItem habitItem = habitItemDBHelper.getHabitItemByDate(new Date(),this);
            habitItemDBHelper.close();
            return habitItem;
        }else{
            return null;
        }

    }

    public static List<Habit> getAllArchivedHabits(Context context) {
        List<Habit> habitList = new ArrayList<Habit>();
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        Cursor cursor= habitDBHelper.fetchAllArchivedHabits();
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

    public Calendar getDueTime() {
        return dueTime;
    }

    public void setDueTime(Calendar dueTime) {
        this.dueTime = dueTime;
    }

    public String getHabitDescription() {
        return habitDescription;
    }

    public void setHabitDescription(String habitDescription) {
        this.habitDescription = habitDescription;
    }


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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
public static List<Habit> getAllUnArchivedHabitsSandbox(Context context) {
        List<Habit> habitList = new ArrayList<Habit>();
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        Cursor cursor= habitDBHelper.fetchAllUnArchivedHabitsSandbox();
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

    public static List<Habit> getAllUnArchivedHabitsInTag(Tag tag, Context context) {
        List<Habit> habitList = new ArrayList<Habit>();
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        Cursor cursor= habitDBHelper.fetchAllUnArchivedHabitsInTag(tag);
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


    public static void archiveAllHabitsInTag(Tag tag, Context context) {
        List<Habit> habitList = getAllUnArchivedHabitsInTag(tag,context);
        HabitDBHelper habitDBHelper = new HabitDBHelper(context);
        habitDBHelper.open();
        for(Habit habit : habitList){
            habitDBHelper.archiveHabit(habit);
        }
        habitDBHelper.close();

    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public static int toDaysCode(Boolean sun,Boolean mon,Boolean tue,Boolean wed,Boolean thu,Boolean fri,Boolean sat){
        int suns = sun?1:0;
        int muns = mon?1:0;
        int tues = tue?1:0;
        int weds = wed?1:0;
        int thus = thu?1:0;
        int fris = fri?1:0;
        int sats = sat?1:0;
        return suns*1000000+muns*100000+tues*10000+weds*1000+thus*100+fris*10+sats;
    }

    public static List<Boolean> getBooleansFromDayCode(int daysCode){
        int rem = daysCode;
        int suns = rem/1000000;
        rem = rem % 1000000;
        int mons = rem/100000;
        rem = rem % 100000;
        int tues = rem/10000;
        rem = rem%10000;
        int weds = rem/1000;
        rem = rem%1000;
        int thus = rem/100;
        rem = rem %100;
        int fris = rem/10;
        rem = rem%10;
        int sats = rem;
        List<Boolean> daysBoolean = new ArrayList<Boolean>();
        daysBoolean.add(suns>0);
        daysBoolean.add(mons>0);
        daysBoolean.add(tues>0);
        daysBoolean.add(weds>0);
        daysBoolean.add(thus>0);
        daysBoolean.add(fris>0);
        daysBoolean.add(sats>0);
        return daysBoolean;
    }
    public static int getDurationCountFromDurationString(int durationIndex){
        switch(durationIndex){
            case 0:
                return 7;
            case 1:
                return 14;
            case 2:
                return 21;
            case 3:
                return 30;
            case 4:
                return 60;
            case 5:
                return 90;
            case 6:
                return 180;
            case 7:
                return 360;
            case 8:
                return 10000;
            default:
                return 10000;
        }
    }
}
