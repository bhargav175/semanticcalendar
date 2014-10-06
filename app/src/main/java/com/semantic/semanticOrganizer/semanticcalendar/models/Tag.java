package com.semantic.semanticOrganizer.semanticcalendar.models;

import android.content.Context;

import com.semantic.semanticOrganizer.semanticcalendar.database.TagDBHelper;

/**
 * Created by Admin on 28-09-2014.
 */
public class Tag {
    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }


    private long tagId;
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

}
