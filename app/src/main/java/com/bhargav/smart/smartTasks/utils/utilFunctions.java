package com.bhargav.smart.smartTasks.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 23-09-2014.
 */
public class utilFunctions {

    public static String dateFormat = "dd:MM:yyyy";
    public static String timeFormat = "h::mm a";
    public static String dateTimeFormat = "dd:MM:yyyy h::mm a";
    public static String SUPER_TAG = "Semantic-Log";

    public static String getCursorEntity(String entity){
        if(entity!=null){
            return entity;
        }
        else{
            return "";
        }
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
