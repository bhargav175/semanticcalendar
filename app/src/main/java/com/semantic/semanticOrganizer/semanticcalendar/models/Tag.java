package com.semantic.semanticOrganizer.semanticcalendar.models;

import android.content.Context;
import android.database.Cursor;

import com.semantic.semanticOrganizer.semanticcalendar.database.TagDBHelper;

import java.util.List;

/**
 * Created by Admin on 28-09-2014.
 */
public class Tag {
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }


    private Integer tagId;
    private String tagText;

    public long getCreatedMillis() {
        return createdMillis;
    }

    public void setCreatedMillis(long createdMillis) {
        this.createdMillis = createdMillis;
    }

    private long createdMillis;

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    private String tagDescription;
    private Boolean isArchived;

    public Tag(String tagText){
        this.tagText=tagText;
    }
    public Tag(){

    }
    @Override
    public String toString(){
        return this.tagText;
    }

    public Tag(int id, String tagText,String tagDescription, Boolean isArchived, long createdMillis){
        this.tagId =id;
        this.tagText = tagText;
        this.tagDescription = tagDescription;
        this.isArchived = isArchived;
        this.createdMillis = createdMillis;

    }
    public void save(Context context){
        TagDBHelper tagDBHelper = new TagDBHelper(context);
        tagDBHelper.open();
        tagDBHelper.saveTag(this);
        tagDBHelper.close();
    }

    public static  List<Tag> getAllTags(List<Tag> taskList,Context context) {
        TagDBHelper tagDBHelper = new TagDBHelper(context);
        tagDBHelper.open();
        Cursor cursor= tagDBHelper.fetchAllTags();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tag tag = tagDBHelper.cursorToTag(cursor);
            taskList.add(tag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        tagDBHelper.close();
        return taskList;
    }


}
