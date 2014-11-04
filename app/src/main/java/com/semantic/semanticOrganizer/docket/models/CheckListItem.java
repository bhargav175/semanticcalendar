package com.semantic.semanticOrganizer.docket.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.docket.database.CheckListItemDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07-10-2014.
 */
public class CheckListItem {
    private int id;
    private String createdTime;

    private String checklistItemText;
    private Integer CheckList;
    private State checkListItemState;


    public State getCheckListItemState() {
        return checkListItemState;
    }

    public void setCheckListItemState(State checkListItemState) {
        this.checkListItemState = checkListItemState;
    }


    public Integer getCheckList() {
        return CheckList;
    }

    public void setCheckList(Integer checkList) {
        CheckList = checkList;
    }

    public String getCheckListItemText() {
        return checklistItemText;
    }

    public void setCheckListItemText(String chedklistItemText) {
        this.checklistItemText = chedklistItemText;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public enum  State{
        NOT_DONE(0), IN_PROGRESS(1), DONE(2);

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

    public static List<CheckListItem> getAllCheckListItemsInCheckList(CheckList checkList, Context context) {
        List<CheckListItem> checkListItems = new ArrayList<CheckListItem>();
        CheckListItemDBHelper checkListItemDBHelper = new CheckListItemDBHelper(context);
        checkListItemDBHelper.open();
        Cursor cursor= checkListItemDBHelper.fetchAllCheckListItemsInCheckList(checkList.getId());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CheckListItem checkListItem = checkListItemDBHelper.cursorToCheckListItem(cursor);
            checkListItems.add(checkListItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        checkListItemDBHelper.close();
        return checkListItems;

    }

}
