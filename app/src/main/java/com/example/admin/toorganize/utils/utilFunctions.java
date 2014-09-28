package com.example.admin.toorganize.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 23-09-2014.
 */
public class utilFunctions {
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


}
