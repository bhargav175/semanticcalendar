package com.semantic.semanticOrganizer.semanticcalendar.models;

/**
 * Created by Admin on 07-10-2014.
 */
public class CheckListItem {
    private int id;
    private String createdTime;

    public Integer getCheckList() {
        return CheckList;
    }

    public void setCheckList(Integer checkList) {
        CheckList = checkList;
    }

    public String getChedklistItemText() {
        return chedklistItemText;
    }

    public void setChedklistItemText(String chedklistItemText) {
        this.chedklistItemText = chedklistItemText;
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

    private String chedklistItemText;
    private Integer CheckList;

}
