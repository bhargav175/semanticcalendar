package com.bhargav.smart.smartTasks.models;

import com.bhargav.smart.smartTasks.R;

/**
 * Created by Admin on 03-11-2014.
 */
public class Label {
    private Integer id;
    private String name;
    private Color color;
    private String createdTime;
    private Integer tagId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getTag() {
        return tagId;
    }

    public void setTag(Integer tagId) {
        this.tagId = tagId;
    }


    public enum  Color{
        VIOLET(0), INDIGO(1),BLUE(2),GREEN(3),YELLOW(4),ORANGE(5),RED(6),BLACK(7);

        private int colorValue;
        Color(int colorValue) {
            this.colorValue = colorValue;
        }

        public void setColorValue(int colorValue) {
            this.colorValue = colorValue;
        }

        public int getColorValue() {
            return colorValue;
        }
    }
    public static int colorToDrawable(Color color){
        if(color == Color.VIOLET){
            return R.color.violet_color;
        }
        if(color == Color.INDIGO){
            return R.color.indigo_color;
        }
        if(color == Color.BLUE){
            return R.color.blue_color;
        }
        if(color == Color.GREEN){
            return R.color.green_color;
        }
        if(color == Color.YELLOW){
            return R.color.yellow_color;
        }
        if(color == Color.ORANGE){
            return R.color.orange_color;
        }
        if(color == Color.RED){
            return R.color.red_color;
        }
        if(color == Color.BLACK){
            return R.color.pitch_black;
        }
        return R.color.pitch_black;
    }


}
