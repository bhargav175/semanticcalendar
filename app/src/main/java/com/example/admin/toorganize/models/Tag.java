package com.example.admin.toorganize.models;

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

    public String getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(String startMillis) {
        this.startMillis = startMillis;
    }
    private long tagId;
    private String tagText;
    private String startMillis;

}
