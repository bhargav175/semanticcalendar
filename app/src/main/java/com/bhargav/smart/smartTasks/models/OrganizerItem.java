package com.bhargav.smart.smartTasks.models;

import android.content.Context;

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
    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
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
    private String createdTime;
    private Integer id;

    public static List<OrganizerItem> getOrganizerItemsWithTag(Tag tag, Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();


        if(tag!=null){
            List<Note> noteList = Note.getAllNotesInTag(tag, context);
            List<CheckList> checkListList = CheckList.getAllCheckListsInTag(tag, context);
            List<Habit> habitList = Habit.getAllHabitsInTag(tag, context);
            organizerItems.addAll(castNotesToOrganizerItemList(noteList));
            organizerItems.addAll(castHabitsToOrganizerItemList(habitList));
            organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
            return organizerItems;
        }
        else{
            return organizerItems;
        }
    }


    public static List<OrganizerItem> getUnArchivedOrganizerItemsWithTag(Tag tag, Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();


        if(tag!=null){
            List<Note> noteList = Note.getAllUnArchivedNotesInTag(tag, context);
            List<CheckList> checkListList = CheckList.getAllUnArchivedCheckListsInTag(tag, context);
            List<Habit> habitList = Habit.getAllUnArchivedHabitsInTag(tag, context);
            organizerItems.addAll(castNotesToOrganizerItemList(noteList));
            organizerItems.addAll(castHabitsToOrganizerItemList(habitList));
            organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
            return organizerItems;
        }
        else{
            return organizerItems;
        }
    }

    public static List<OrganizerItem> getSandboxOrganizerItems(Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();



            List<Note> noteList = Note.getAllNotesSandbox(context);
            List<CheckList> checkListList = CheckList.getAllCheckListsSandbox(context);
            List<Habit> habitList = Habit.getAllHabitsSandbox(context);
            organizerItems.addAll(castNotesToOrganizerItemList(noteList));
            organizerItems.addAll(castHabitsToOrganizerItemList(habitList));
            organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
            return organizerItems;

    }

    public static List<OrganizerItem> getSandboxUnArchivedOrganizerItems(Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();



        List<Note> noteList = Note.getAllUnArchivedNotesSandbox(context);
        List<CheckList> checkListList = CheckList.getAllUnArchivedCheckListsSandbox(context);
        List<Habit> habitList = Habit.getAllUnArchivedHabitsSandbox(context);
        organizerItems.addAll(castNotesToOrganizerItemList(noteList));
        organizerItems.addAll(castHabitsToOrganizerItemList(habitList));
        organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
        return organizerItems;

    }


    public static List<OrganizerItem> castNotesToOrganizerItemList( List<Note> noteList){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(Note note:noteList){
            organizerItems.add(OrganizerItem.castNoteToOrganizerItem(note));
        }
        return organizerItems;
    }


    public static OrganizerItem castNoteToOrganizerItem( Note note){
            OrganizerItem organizerItem = new OrganizerItem();
            organizerItem.setItemText(note.getNoteTitle());
            organizerItem.setCreatedTime(note.getCreatedTime());
            organizerItem.setSecondaryText(note.getNoteDescription());
            if(note.getDueTime()!=null){
                organizerItem.setDueTime((Calendar)note.getDueTime().clone());
            }
            organizerItem.setId(note.getId());
            organizerItem.setType("NOTE");

        return organizerItem;
    }


    public static List<OrganizerItem> castHabitsToOrganizerItemList(List<Habit> habitList){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(Habit habit:habitList){
            organizerItems.add(castHabitToOrganizerItem(habit));
        }
        return organizerItems;
    }

    public static OrganizerItem castHabitToOrganizerItem(Habit habit){
        OrganizerItem organizerItem = new OrganizerItem();

            if(habit.getHabitDescription()!=null && habit.getHabitDescription().length()>0){
                organizerItem.setItemText(habit.getHabitDescription());

            }else{
                organizerItem.setItemText(habit.getHabitText());

            }
            organizerItem.setSecondaryText(habit.getCreatedTime());
            organizerItem.setCreatedTime(habit.getCreatedTime());
            organizerItem.setId(habit.getId());
            organizerItem.setType("HABIT");

        return organizerItem;
    }



    public static List<OrganizerItem> castCheckListsToOrganizerItemList( List<CheckList> checkListList){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(CheckList checkList:checkListList){
            organizerItems.add(castCheckListToOrganizerItem(checkList));
        }
        return organizerItems;
    }


    public static OrganizerItem castCheckListToOrganizerItem( CheckList checkList){

            OrganizerItem organizerItem = new OrganizerItem();
            organizerItem.setItemText(checkList.getCheckListTitle());
            organizerItem.setCreatedTime(checkList.getCreatedTime());
            organizerItem.setSecondaryText(checkList.getCheckListDescription());
            if(checkList.getDueTime()!=null){
                organizerItem.setDueTime((Calendar)checkList.getDueTime().clone());
            }
        organizerItem.setId(checkList.getId());
            organizerItem.setType("CHECKLIST");
        return organizerItem;
    }


    public static List<OrganizerItem> getAllArchivedItems(Context context) {
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();
        List<Note> noteList = Note.getAllArchivedNotes(context);
        List<CheckList> checkListList = CheckList.getAllArchivedCheckLists(context);
        List<Habit> habitList = Habit.getAllArchivedHabits(context);
        organizerItems.addAll(castNotesToOrganizerItemList(noteList));
        organizerItems.addAll(castHabitsToOrganizerItemList(habitList));
        organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
        return organizerItems;


    }


    public static List<OrganizerItem> getAllUnArchivedItemsByDueDate(Context context, Calendar calendar) {
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();
        List<Note> noteList = Note.getAllUnArchivedNotesByDueDate(context, calendar);
        List<CheckList> checkListList = CheckList.getAllUnArchivedCheckListsByDueDate(context, calendar);
        List<Habit> habitList = Habit.getAllUnArchivedHabitsByDueDate(context,calendar);
        organizerItems.addAll(castNotesToOrganizerItemList(noteList));
        organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
        organizerItems.addAll(castHabitsToOrganizerItemList(habitList));
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
}
