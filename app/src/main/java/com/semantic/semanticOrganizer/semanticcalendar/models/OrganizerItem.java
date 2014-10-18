package com.semantic.semanticOrganizer.semanticcalendar.models;

import android.content.Context;

import com.semantic.semanticOrganizer.semanticcalendar.database.HabitItemDBHelper;

import java.util.ArrayList;
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
            organizerItems.addAll(castHabitsToOrganizerItemList(habitList,context));
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
            organizerItems.addAll(castHabitsToOrganizerItemList(habitList,context));
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
            organizerItems.addAll(castHabitsToOrganizerItemList(habitList,context));
            organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
            return organizerItems;

    }

    public static List<OrganizerItem> getSandboxUnArchivedOrganizerItems(Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();



        List<Note> noteList = Note.getAllUnArchivedNotesSandbox(context);
        List<CheckList> checkListList = CheckList.getAllUnArchivedCheckListsSandbox(context);
        List<Habit> habitList = Habit.getAllUnArchivedHabitsSandbox(context);
        organizerItems.addAll(castNotesToOrganizerItemList(noteList));
        organizerItems.addAll(castHabitsToOrganizerItemList(habitList,context));
        organizerItems.addAll(castCheckListsToOrganizerItemList(checkListList));
        return organizerItems;

    }


    public static List<OrganizerItem> castNotesToOrganizerItemList( List<Note> noteList){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(Note note:noteList){
            OrganizerItem organizerItem = new OrganizerItem();
            organizerItem.setItemText(note.getNoteText());
            organizerItem.setCreatedTime(note.getCreatedTime());
            organizerItem.setId(note.getId());
            organizerItem.setType("NOTE");
            organizerItems.add(organizerItem);
        }
        return organizerItems;
    }


    public static List<OrganizerItem> castHabitsToOrganizerItemList(List<Habit> habitList ,Context context){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(Habit habit:habitList){
            OrganizerItem organizerItem = new OrganizerItem();
            if(habit.getHabitQuestion()!=null && habit.getHabitQuestion().length()>0){
                organizerItem.setItemText(habit.getHabitQuestion());

            }else{
                organizerItem.setItemText(habit.getHabitText());

            }
            HabitItemDBHelper habitItemDBHelper = new HabitItemDBHelper(context);
            habitItemDBHelper.open();
            if(habit.getHabitItemToday(context)!=null){
                organizerItem.setSecondaryText((String.valueOf( habit.getHabitItemToday(context).getHabitItemState().getStateValue())));

            }
            habitItemDBHelper.close();
            organizerItem.setCreatedTime(habit.getCreatedTime());
            organizerItem.setId(habit.getId());
            organizerItem.setType("HABIT");
            organizerItems.add(organizerItem);
        }
        return organizerItems;
    }

    public static List<OrganizerItem> castCheckListsToOrganizerItemList( List<CheckList> checkListList){
        List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();

        for(CheckList checkList:checkListList){
            OrganizerItem organizerItem = new OrganizerItem();
            organizerItem.setItemText(checkList.getCheckListText());
            organizerItem.setCreatedTime(checkList.getCreatedTime());
            organizerItem.setId(checkList.getId());
            organizerItem.setType("CHECKLIST");
            organizerItems.add(organizerItem);
        }
        return organizerItems;
    }


}
