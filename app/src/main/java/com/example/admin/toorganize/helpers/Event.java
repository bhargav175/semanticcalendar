package com.example.admin.toorganize.helpers;

/**
 * Created by Admin on 23-09-2014.
 */
public class Event {
    private String title;
    private String description;
    String  startTimeMillis;
    String  endTimeMillis;
    private String calendarDisplayName;

    public Event(String title,String description,String startTimeMillis,String endTimeMillis,String calendarDisplayName){
        this.title=title;
        this.description=description;
        this.startTimeMillis=startTimeMillis;
        this.endTimeMillis=endTimeMillis;
        this.calendarDisplayName=calendarDisplayName;

    }
    @Override
    public String toString() {
        return title;
    }

    public String getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(String startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public String getEndTimeMillis() {
        return endTimeMillis;
    }

    public void setEndTimeMillis(String endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    public String getCalendarDisplayName() {
        return calendarDisplayName;
    }

    public void setCalendarDisplayName(String calendarDisplayName) {
        this.calendarDisplayName = calendarDisplayName;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
