package com.bhargav.smart.smartTasks.models;

import android.content.Context;
import android.database.Cursor;

import com.bhargav.smart.smartTasks.database.RepeatingTaskDBHelper;
import com.bhargav.smart.smartTasks.database.RepeatingTaskItemDBHelper;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 16-09-2014.
 */
public class RepeatingTask {


    private String repeatingTaskText;
    private String repeatingTaskDescription;
    private Calendar createdTime;
    private Boolean isArchived;
private Boolean isReminded;

    private Boolean hasStatistics;
    private Integer Tag;
    private Integer reminderId;
    private Type repeatingTaskType;
    private Integer frequency;
    private Calendar dueTime;
    private Calendar startDate;

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    private Integer priority;



    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    private Calendar endDate;

    private Integer daysCode;
    public static String[] frequencyStrings = {"4 times a week","3 times a week", "2 times a week","Once a week","Once every 2 weeks","Once a month","Once every 2 months", "Once every 6 months", "Once a year" };
    public static String[] durationStrings = {"1 week", "2 weeks","3 weeks" ,"1 month", "2 months", "3 months", "6 months", "1 year" ,"Forever" };
    private Integer id;

    public String getRepeatingTaskText() {
        return repeatingTaskText;
    }

    public void setRepeatingTaskText(String repeatingTaskText) {
        this.repeatingTaskText = repeatingTaskText;
    }

    public Integer getTag() {
        return Tag;
    }

    public void setTag(Integer tag) {
        Tag = tag;
    }

    public Boolean getHasStatistics() {
        return hasStatistics;
    }

    public void setHasStatistics(Boolean hasStatistics) {
        this.hasStatistics = hasStatistics;
    }



    public Type getRepeatingTaskType() {
        return repeatingTaskType;
    }

    public void setRepeatingTaskType(Type repeatingTaskType) {
        this.repeatingTaskType = repeatingTaskType;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }



    public Integer getDaysCode() {
        return daysCode;
    }

    public void setDaysCode(Integer daysCode) {
        this.daysCode = daysCode;
    }


    public RepeatingTaskItem getRepeatingTaskItemToday(Context context) {
        if(this.getId()!=null){
            RepeatingTaskItemDBHelper repeatingTaskItemDBHelper = new RepeatingTaskItemDBHelper(context);
            repeatingTaskItemDBHelper.open();
            RepeatingTaskItem repeatingTaskItem = repeatingTaskItemDBHelper.getHabitItemByDate(new Date(),this);
            repeatingTaskItemDBHelper.close();
            return repeatingTaskItem;
        }else{
            return null;
        }

    }

    public static List<RepeatingTask> getAllArchivedRepeatingTasks(Context context) {
        List<RepeatingTask> repeatingTaskList = new ArrayList<RepeatingTask>();
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        Cursor cursor= repeatingTaskDBHelper.fetchAllArchivedRepeatingTasks();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTask repeatingTask = repeatingTaskDBHelper.cursorToRepeatingTask(cursor);
            repeatingTaskList.add(repeatingTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        repeatingTaskDBHelper.close();
        return repeatingTaskList;
    }


    public Boolean getIsReminded() {
        return isReminded;
    }

    public void setIsReminded(Boolean isReminded) {
        this.isReminded = isReminded;
    }


    public Calendar getDueTime() {
        return dueTime;
    }

    public void setDueTime(Calendar dueTime) {
        this.dueTime = dueTime;
    }

    public String getRepeatingTaskDescription() {
        return repeatingTaskDescription;
    }

    public void setRepeatingTaskDescription(String repeatingTaskDescription) {
        this.repeatingTaskDescription = repeatingTaskDescription;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
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

    public RepeatingTask(){

    }

    public RepeatingTask(int id, String repeatingTaskText){
        this.id = id;
        this.repeatingTaskText = repeatingTaskText;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
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
        return repeatingTaskText;
    }


    public static RepeatingTask getRepeatingTaskById(Integer habitId, Context context) {
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        RepeatingTask repeatingTask = repeatingTaskDBHelper.getRepeatingTasks(habitId);
        repeatingTaskDBHelper.close();
        return repeatingTask;
    }



    public static List<RepeatingTask> getAllRepeatingTasks(List<RepeatingTask> repeatingTaskList, Context context) {
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        Cursor cursor= repeatingTaskDBHelper.fetchAllRepeatingTasks();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTask repeatingTask = repeatingTaskDBHelper.cursorToRepeatingTask(cursor);
            repeatingTaskList.add(repeatingTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        repeatingTaskDBHelper.close();
        return repeatingTaskList;
    }

    public static List<RepeatingTask> getAllRepeatingTasksInTag(TaskList taskList, Context context) {
        List<RepeatingTask> repeatingTaskList = new ArrayList<RepeatingTask>();
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        Cursor cursor= repeatingTaskDBHelper.fetchAllRepeatingTasksInTag(taskList);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTask repeatingTask = repeatingTaskDBHelper.cursorToRepeatingTask(cursor);
            repeatingTaskList.add(repeatingTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        repeatingTaskDBHelper.close();
        return repeatingTaskList;

    }
    public static List<RepeatingTask> getAllRepeatingTasksSandbox(Context context) {
        List<RepeatingTask> repeatingTaskList = new ArrayList<RepeatingTask>();
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        Cursor cursor= repeatingTaskDBHelper.fetchAllRepeatingTasksSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTask repeatingTask = repeatingTaskDBHelper.cursorToRepeatingTask(cursor);
            repeatingTaskList.add(repeatingTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        repeatingTaskDBHelper.close();
        return repeatingTaskList;

    }
public static List<RepeatingTask> getAllUnArchivedRepeatingTasksSandbox(Context context) {
        List<RepeatingTask> repeatingTaskList = new ArrayList<RepeatingTask>();
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        Cursor cursor= repeatingTaskDBHelper.fetchAllUnArchivedRepeatingTasksSandbox();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTask repeatingTask = repeatingTaskDBHelper.cursorToRepeatingTask(cursor);
            repeatingTaskList.add(repeatingTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        repeatingTaskDBHelper.close();
        return repeatingTaskList;

    }

    public static List<RepeatingTask> getAllUnArchivedRepeatingTasksInTag(TaskList taskList, Context context) {
        List<RepeatingTask> repeatingTaskList = new ArrayList<RepeatingTask>();
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        Cursor cursor= repeatingTaskDBHelper.fetchAllUnArchivedRepeatingTasksInTag(taskList);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTask repeatingTask = repeatingTaskDBHelper.cursorToRepeatingTask(cursor);
            repeatingTaskList.add(repeatingTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        repeatingTaskDBHelper.close();
        return repeatingTaskList;
    }
    public static List<RepeatingTask> getAllUnArchivedRepeatingTasksByDueDate(Context context, Calendar calendar) {
        List<RepeatingTask> repeatingTaskList = new ArrayList<RepeatingTask>();
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        Cursor cursor= repeatingTaskDBHelper.fetchAllUnArchivedRepeatingTasksByDueDate(calendar);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RepeatingTask repeatingTask = repeatingTaskDBHelper.cursorToRepeatingTask(cursor);
            repeatingTaskList.add(repeatingTask);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        repeatingTaskDBHelper.close();
        return repeatingTaskList;
    }



    public static void archiveAllRepeatingTasksInTag(TaskList taskList, Context context) {
        List<RepeatingTask> repeatingTaskList = getAllUnArchivedRepeatingTasksInTag(taskList, context);
        RepeatingTaskDBHelper repeatingTaskDBHelper = new RepeatingTaskDBHelper(context);
        repeatingTaskDBHelper.open();
        for(RepeatingTask repeatingTask : repeatingTaskList){
            repeatingTaskDBHelper.archiveRepeatingTask(repeatingTask);
        }
        repeatingTaskDBHelper.close();

    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
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
    public static List<String> getActiveDays(List<Boolean> daysBoolean){
        List<String> activeDays = new ArrayList<String>();
        if(daysBoolean.get(0)){
            activeDays.add("Sun");

        }
        if(daysBoolean.get(1)){
            activeDays.add("Mon");
        }
        if(daysBoolean.get(2)){
            activeDays.add("Tue");
        }
        if(daysBoolean.get(3)){
            activeDays.add("Wed");
        }
        if(daysBoolean.get(4)){
            activeDays.add("Thu");
        }
        if(daysBoolean.get(5)){
            activeDays.add("Fri");
        }
        if(daysBoolean.get(6)){
            activeDays.add("Sat");
        }
        return activeDays;
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
    public static String getMetaText(RepeatingTask repeatingTask){
        if(repeatingTask!=null){
            if(repeatingTask.getRepeatingTaskType()!=null && repeatingTask.getStartDate()!=null && repeatingTask.getEndDate()!=null && repeatingTask.getDueTime()!=null){
                String startD = new SimpleDateFormat(utilFunctions.dateFormat).format(repeatingTask.getStartDate().getTime());
                String endD = new SimpleDateFormat(utilFunctions.dateFormat).format(repeatingTask.getEndDate().getTime());
                String repeat = "";
                String dueTime = new SimpleDateFormat(utilFunctions.timeFormat).format(repeatingTask.getDueTime().getTime());
                if(repeatingTask.getRepeatingTaskType()==Type.FIXED){
                    List<String> activeDays = getActiveDays(getBooleansFromDayCode(repeatingTask.getDaysCode()));
                    for(int i =0; i<activeDays.size();i++){
                        repeat = repeat + activeDays.get(i);
                        if(i<(activeDays.size() - 1)){
                            repeat = repeat + " ";
                        }

                    }
                }else{
                    if(repeatingTask.getFrequency()!=null){
                        repeat = RepeatingTask.frequencyStrings[repeatingTask.getFrequency()];
                    }else{
                        return "None";
                    }

                }

                    //Starting from startDate Repeat once every week / every sunday at duetime till end date
                return "Starting from " + startD + " Repeat "+ repeat + " at " + dueTime + " till "+ endD;
            }else{
                return "None";
            }

        }else{
            return "None";
        }

    }

}
