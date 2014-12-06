package com.bhargav.smart.smartTasks.models;

import android.content.Context;

import com.bhargav.smart.smartTasks.database.ReminderDBHelper;

/**
 * Created by Admin on 16-09-2014.
 */
public class Reminder {




    private int id;


    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private int hourOfDay;
    private int minuteOfHour;
    private int second;
    private int duration;
    private int interval;
    private Boolean isRepeating;
    private String createdTime;

    public Reminder(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int second, int duration, int interval, Boolean isRepeating) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minuteOfHour = minuteOfHour;
        this.second = second;
        this.duration = duration;
        this.interval = interval;
        this.isRepeating = isRepeating;
    }

    public Reminder() {
    }


    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinuteOfHour() {
        return minuteOfHour;
    }

    public void setMinuteOfHour(int minuteOfHour) {
        this.minuteOfHour = minuteOfHour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Boolean getIsRepeating() {
        return isRepeating;
    }

    public void setIsRepeating(Boolean isRepeating) {
        this.isRepeating = isRepeating;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }



    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return createdTime;
    }


    public static Reminder getReminderById(Integer reminderId, Context context) {
        ReminderDBHelper reminderDBHelper = new ReminderDBHelper(context);
        reminderDBHelper.open();
        Reminder reminder= reminderDBHelper.getReminder(reminderId);
        reminderDBHelper.close();
        return reminder;
    }

    public static Integer getNextReminderId(Context context){
        ReminderDBHelper reminderDBHelper = new ReminderDBHelper(context);
        reminderDBHelper.open();
        Integer i= reminderDBHelper.getLastReminderId() +1;
        reminderDBHelper.close();
        return i;
    }


    public static void deleteReminder(Context context,Integer remainderId) {
        ReminderDBHelper reminderDBHelper = new ReminderDBHelper(context);
        reminderDBHelper.open();
        reminderDBHelper.deleteReminder(remainderId);
        reminderDBHelper.close();
    }
}
