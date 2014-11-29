package com.bhargav.smart.smartTasks.models;

import android.content.Context;

import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Admin on 07-10-2014.
 */
public class OrganizerItem {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
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
    private String type;
    private String itemText;
    private Calendar dueTime;

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    private String secondaryText;
    private Calendar createdTime;
    private Integer id;
    private utilFunctions.State state;

    public static List<OrganizerItem> getOrganizerItemsWithTag(TaskList taskList, Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();


        if(taskList !=null){
            List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllNotesInTag(taskList, context);
            List<RepeatingTask> repeatingTaskList = RepeatingTask.getAllRepeatingTasksInTag(taskList, context);
            organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
            organizerItems.addAll(castHabitsToOrganizerItemList(repeatingTaskList));
            return organizerItems;
        }
        else{
            return organizerItems;
        }
    }


    public static List<OrganizerItem> getUnArchivedOrganizerItemsWithTag(TaskList taskList, Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();


        if(taskList !=null){
            List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllUnArchivedNotesInTag(taskList, context);
            List<RepeatingTask> repeatingTaskList = RepeatingTask.getAllUnArchivedRepeatingTasksInTag(taskList, context);
            organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
            organizerItems.addAll(castHabitsToOrganizerItemList(repeatingTaskList));
            return organizerItems;
        }
        else{
            return organizerItems;
        }
    }

    public static List<OrganizerItem> getSandboxOrganizerItems(Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();



            List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllNotesSandbox(context);
            List<RepeatingTask> repeatingTaskList = RepeatingTask.getAllRepeatingTasksSandbox(context);
            organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
            organizerItems.addAll(castHabitsToOrganizerItemList(repeatingTaskList));
            return organizerItems;

    }

    public static List<OrganizerItem> getSandboxUnArchivedOrganizerItems(Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();



        List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllUnArchivedNotesSandbox(context);
        List<RepeatingTask> repeatingTaskList = RepeatingTask.getAllUnArchivedRepeatingTasksSandbox(context);
        organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
        organizerItems.addAll(castHabitsToOrganizerItemList(repeatingTaskList));
        return organizerItems;

    }


    public static List<OrganizerItem> castNotesToOrganizerItemList( List<OneTimeTask> oneTimeTaskList){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(OneTimeTask oneTimeTask : oneTimeTaskList){
            organizerItems.add(OrganizerItem.castNoteToOrganizerItem(oneTimeTask));
        }
        return organizerItems;
    }


    public static OrganizerItem castNoteToOrganizerItem( OneTimeTask oneTimeTask){
            OrganizerItem organizerItem = new OrganizerItem();
            organizerItem.setItemText(oneTimeTask.getNoteTitle());
            organizerItem.setCreatedTime(oneTimeTask.getCreatedTime());
            organizerItem.setSecondaryText(oneTimeTask.getNoteDescription());
            if(oneTimeTask.getDueTime()!=null){
                organizerItem.setDueTime((Calendar) oneTimeTask.getDueTime().clone());
            }
           if(oneTimeTask.getTaskItemState()!=null){
             organizerItem.setState(oneTimeTask.getTaskItemState());
            }
            organizerItem.setId(oneTimeTask.getId());
            organizerItem.setType("NOTE");

        return organizerItem;
    }


    public static List<OrganizerItem> castHabitsToOrganizerItemList(List<RepeatingTask> repeatingTaskList){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(RepeatingTask repeatingTask : repeatingTaskList){
            organizerItems.add(castHabitToOrganizerItem(repeatingTask));
        }
        return organizerItems;
    }

    public static OrganizerItem castHabitToOrganizerItem(RepeatingTask repeatingTask){
        OrganizerItem organizerItem = new OrganizerItem();
            if(repeatingTask.getRepeatingTaskDescription()!=null && repeatingTask.getRepeatingTaskDescription().length()>0){
                organizerItem.setItemText(repeatingTask.getRepeatingTaskDescription());
            }else{
                organizerItem.setItemText(repeatingTask.getRepeatingTaskText());
            }
            if(repeatingTask.getDueTime()!=null){
                Calendar today = Calendar.getInstance();
                Calendar c =(Calendar) repeatingTask.getDueTime().clone();
                c.set(Calendar.YEAR,today.get(Calendar.YEAR));
                c.set(Calendar.MONTH,today.get(Calendar.MONTH));
                c.set(Calendar.DAY_OF_MONTH,today.get(Calendar.DAY_OF_MONTH));
            organizerItem.setDueTime(c);
            }
            organizerItem.setSecondaryText(repeatingTask.getRepeatingTaskDescription());
            organizerItem.setCreatedTime(repeatingTask.getCreatedTime());
            organizerItem.setId(repeatingTask.getId());
            organizerItem.setType("HABIT");

        return organizerItem;
    }


    public static List<OrganizerItem> getAllArchivedItems(Context context) {
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();
        List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllArchivedNotes(context);
        List<RepeatingTask> repeatingTaskList = RepeatingTask.getAllArchivedRepeatingTasks(context);
        organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
        organizerItems.addAll(castHabitsToOrganizerItemList(repeatingTaskList));
        return organizerItems;
    }


    public static List<OrganizerItem> getAllUnArchivedItemsByDueDate(Context context, Calendar calendar) {
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();
        List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllUnArchivedNotesByDueDate(context, calendar);
        List<RepeatingTask> repeatingTaskList = RepeatingTask.getAllUnArchivedRepeatingTasksByDueDate(context, calendar);
        organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
        organizerItems.addAll(castHabitsToOrganizerItemList(repeatingTaskList));
        Collections.sort(organizerItems, new Comparator<OrganizerItem>() {
            @Override
            public int compare(OrganizerItem org1, OrganizerItem org2) {
                return org1.getDueTime().compareTo(org2.getDueTime());
            }
        });

        return organizerItems;
    }

    public Calendar getDueTime() {
        return dueTime;
    }

    public void setDueTime(Calendar dueTime) {
        this.dueTime = dueTime;
    }

    public utilFunctions.State getState() {
        return state;
    }

    public void setState(utilFunctions.State state) {
        this.state = state;
    }

    public static List<OrganizerItem> getAllUnArchivedItemsByWeek(Context context, Calendar calendar) {
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();
        List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllUnArchivedNotesByWeek(context, calendar);
       organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
        Collections.sort(organizerItems, new Comparator<OrganizerItem>() {
            @Override
            public int compare(OrganizerItem org1, OrganizerItem org2) {
                return org1.getDueTime().compareTo(org2.getDueTime());
            }
        });

        return organizerItems;

    }

    public static List<OrganizerItem> getAllUnArchivedItemsByMonth(Context context, Calendar calendar) {
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();
        List<OneTimeTask> oneTimeTaskList = OneTimeTask.getAllUnArchivedNotesByMonth(context, calendar);
        organizerItems.addAll(castNotesToOrganizerItemList(oneTimeTaskList));
        Collections.sort(organizerItems, new Comparator<OrganizerItem>() {
            @Override
            public int compare(OrganizerItem org1, OrganizerItem org2) {
                return org1.getDueTime().compareTo(org2.getDueTime());
            }
        });

        return organizerItems;
    }
}
