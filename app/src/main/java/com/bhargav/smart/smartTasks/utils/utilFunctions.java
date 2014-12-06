package com.bhargav.smart.smartTasks.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import com.bhargav.smart.smartTasks.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 23-09-2014.
 */
public class utilFunctions {


    public enum  State{
        NOT_STARTED(0), STARTED(1), FAILED(2),COMPLETED(3);

        private int stateValue;
        State(int stateValue) {
            this.stateValue = stateValue;
        }

        public void setStateValue(int stateValue) {
            this.stateValue = stateValue;
        }

        public String getStateValueString(){
            switch(this.getStateValue()){
                case 0:
                    return "Not Started";
                case 1:
                    return "Started";
                case 2:
                    return "Failed";
                case 3:
                    return "Completed";
                default:
                    return "Not Started";

            }
        }

        public int getStateValue() {
            return stateValue;
        }
    }

    public enum  Color{
        TRANSPARENT(0),VIOLET(1), INDIGO(2), BLUE(3),GREEN(4),YELLOW(5),ORANGE(6),RED(7),BLACK(8);

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

        public static int getColorResource(Color color){

            int cr;
            switch (color){
                case TRANSPARENT:
                    cr = android.R.color.transparent;
                    break;
                case VIOLET:
                    cr =  R.color.violet_color;
                    break;
                case INDIGO:
                    cr =  R.color.indigo_color;
                    break;
                case BLUE:
                    cr =  R.color.blue_color;
                    break;
                case GREEN:
                    cr =  R.color.green_color;
                    break;
                case YELLOW:
                    cr =  R.color.yellow_color;
                    break;
                case ORANGE:
                    cr =  R.color.orange_color;
                    break;
                case RED:
                    cr =  R.color.red_color;
                    break;
                case BLACK:
                    cr =  R.color.pitch_black;
                    break;
                default:
                    cr =  android.R.color.transparent;


            }
            return cr;
        }

        public String asString(){

            String cr;
            switch (this){
                case TRANSPARENT:
                    cr = "Transparent";
                    break;
                case VIOLET:
                    cr =  "Violet";
                    break;
                case INDIGO:
                    cr =  "Indigo";
                    break;
                case BLUE:
                    cr =  "Blue";
                    break;
                case GREEN:
                    cr =  "Green";
                    break;
                case YELLOW:
                    cr = "Yellow";
                    break;
                case ORANGE:
                    cr =  "Orange";
                    break;
                case RED:
                    cr =  "Red";
                    break;
                case BLACK:
                    cr =  "Black";
                    break;
                default:
                    cr =  "Transparent";


            }
            return cr;
        }

    }







    public static String dateFormat = "dd-MM-yyyy";
    public static String dayFormat = "dd";
    public static String monthYearFormat = "MMM, yyyy";
    public static String weekDayMonthFormat = "EEE, dd MM yyyy";
    public static String timeFormat = "HH:mm";
    public static String dateTimeFormat = "dd-MM-yyyy HH:mm";
    public static String reverseDateTimeFormat =  "yyyy-MM-dd HH:mm:ss";

    public static String SUPER_TAG = "SmartDo";

    public static String getCursorEntity(String entity){
        if(entity!=null){
            return entity;
        }
        else{
            return "";
        }
    }
    public static void BLog(String str){
        Log.d(SUPER_TAG,str);
    }

    public static String getDate(Long milliSeconds) {
        if(milliSeconds!=null){
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());

        }
        else{
            return "--";
        }



    }
    public static String getDateFromString(String milliSeconds) {
        if(milliSeconds!=null){
            Long ms =Long.valueOf(milliSeconds);
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ms);
            return formatter.format(calendar.getTime());

        }
        else{
            return "--";
        }

    }
    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }



}
