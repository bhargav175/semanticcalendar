package com.bhargav.smart.smartTasks.helpers;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.database.ReminderDBHelper;
import com.bhargav.smart.smartTasks.models.OneTimeTask;
import com.bhargav.smart.smartTasks.models.Reminder;
import com.bhargav.smart.smartTasks.utils.MyBroadcastReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Admin on 12-10-2014.
 */
public class OneTimeTaskReminderHelper {

       //Get reminder id
    //if there is a reminder .. show the textview and initialize it with the reminder
                //initialize DueDateDialog
                //when the user clicks on save.. // get the components of DueDateDialog holder
                        //if there is no reminder
                                                    //Delete the existing one
                        //else
                                                    //update this one


    //else
                //initialize DueDateDialog with today's date
                //when the user clicks on save item

                            //if there is no reminder
                                            //take lite
                            //else get next reminder it
                                                //save this reminder


        private Context context;
        private Boolean hadReminder, hasReminder;
        private Reminder currentReminder;
        private Integer reminderId;
        private OneTimeTask oneTimeTask;
        private Calendar dueDate;
        private int year,month,day,hour,minute,second;
        private TextView textView;
        private DueDateDialog mAlertBuilder;
        private AlertDialog mAlert;
       private FragmentActivity f;
       private ReminderDBHelper reminderDBHelper;

    public OneTimeTaskReminderHelper(Context context, FragmentActivity f, Integer reminderId, Calendar dueDate, TextView textView, OneTimeTask oneTimeTask) {
        this.context = context;
        this.oneTimeTask = oneTimeTask;
        this.dueDate = dueDate;
        this.textView = textView;
        this.reminderId = reminderId;
        this.f =f;
        Calendar cal = dueDate!=null ?(Calendar)dueDate.clone() : Calendar.getInstance();
        year= cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        second =0;
        reminderDBHelper = new ReminderDBHelper(this.context);
        new GetReminder(context).execute("");

    }

    public void show(){
        mAlert.show();
    }
    public void hide(){
        mAlert.dismiss();
    }
    public DueDateDialog.Holder returnUpdatedValues(){
        return this.mAlertBuilder.returnUpdatedValues();
    }
    public DueDateDialog.Holder doSomethingAboutTheReminder(Boolean isReminded){
        DueDateDialog.Holder holder = returnUpdatedValues();
        Calendar cal =(Calendar) holder.cal.clone();
        year= cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        if(isReminded){
            if(holder.bool){
                //there is a reminder

                if(hadReminder){
                    //update

                    new UpdateReminder(context,reminderId,year,month,day,hour,minute).execute("");

                }
                else{
                    //save
                    new SaveReminder(context,reminderId,year,month,day,hour,minute).execute("");

                }
            }else{
                if(hadReminder){
                    //delete Reminder
                    Integer remId = reminderId;
                    new DeleteReminder(context,remId,year,month,day,hour,minute).execute("");
                }else{
                    //lite
                }
                holder.cal = null;
                reminderId=null;

            }
        }else{
            if(reminderId!=null){
                Integer remId = reminderId;
                new DeleteReminder(context,remId,year,month,day,hour,minute).execute("");

            }else{

            }
            holder.cal = null;
            reminderId=null;

        }

        return holder;
    }





    private void updateReminder(Integer requestId, int year, int month, int day, int hour, int minute) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        intent.putExtra(DBHelper.COLUMN_ID,oneTimeTask.getId());
        intent.putExtra("Type",1);
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Reminder reminder = new Reminder(year, month, day, hour, minute, second, 0, 0, false);
            reminder.setId(requestId);
            reminderDBHelper.open();
            reminderDBHelper.updateReminder(reminder);
            reminderDBHelper.close();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(year, month, day, hour, minute, 0);
            Date currentLocalTime = calendar.getTime();
            DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
            date.setTimeZone(TimeZone.getTimeZone("GMT"));
            String localTime = date.format(currentLocalTime);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
          }
    }

    private void saveReminder(Integer requestId, int year, int month, int day, int hour, int minute) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        intent.putExtra(DBHelper.COLUMN_ID,oneTimeTask.getId());
        intent.putExtra("Type",1);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Reminder reminder = new Reminder(year, month, day, hour, minute, second, 0, 0, false);
            reminder.setId(requestId);
            reminderDBHelper.open();
            reminderDBHelper.saveReminder(reminder);
            reminderDBHelper.close();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(year, month, day, hour, minute, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);


        }
    }
    private void deleteReminder(Integer requestId) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(reminderId!=null){
            reminderDBHelper.open();
            reminderDBHelper.deleteReminder(reminderId);
            reminderDBHelper.close();
        }
        if (alarmManager != null) {
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    private class GetReminder extends AsyncTask<String, Void, Void> {

        private Integer id;
        private Context context;

        public GetReminder(Context context) {

            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            if (reminderId != null) {
                hadReminder = true;
                hasReminder = true;
                currentReminder = Reminder.getReminderById(reminderId, context);
            } else {
                reminderId = Reminder.getNextReminderId(context);
                hadReminder = false;
                hasReminder = false;
                currentReminder = new Reminder(year, month, day, hour, minute, second, 0, 0, false);
                currentReminder.setId(reminderId);
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    year = currentReminder.getYear();
                    month = currentReminder.getMonthOfYear();
                    day = currentReminder.getDayOfMonth();
                    hour = currentReminder.getHourOfDay();
                    minute = currentReminder.getMinuteOfHour();
                    second = currentReminder.getSecond();
                    Calendar calendar = new GregorianCalendar();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    mAlertBuilder=new DueDateDialog(f,oneTimeTask,dueDate,textView,year,month,day,hour,minute,hasReminder,reminderId);
                    mAlert = mAlertBuilder.create();
                    reminderDBHelper = new ReminderDBHelper(context);
                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class SaveReminder extends AsyncTask<String, Void, Void> {

        private Integer requestId;
        private Context context;
        private int y,M,d,h,m;


        public SaveReminder(Context context,Integer requestId, int year, int month, int day, int hour, int minute) {

            this.y = year;
            this.M = month;
            this.d= day;
            this.h = hour;
            this.m = minute;
            this.context = context;
            this.requestId = requestId;
        }

        @Override
        protected Void doInBackground(String... params) {
            saveReminder(reminderId,year,month,day,hour,minute);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, M);
                    calendar.set(Calendar.DAY_OF_MONTH, d);
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);
                    calendar.set(Calendar.SECOND, 0);
                    Toast.makeText(context, "Alarm created " + calendar.getTime().toString() + " seconds",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class UpdateReminder extends AsyncTask<String, Void, Void> {

        private Integer requestId;
        private Context context;
        private int y,M,d,h,m;


        public UpdateReminder(Context context,Integer requestId, int year, int month, int day, int hour, int minute) {

            this.y = year;
            this.M = month;
            this.d= day;
            this.h = hour;
            this.m = minute;
            this.context = context;
            this.requestId = requestId;
        }

        @Override
        protected Void doInBackground(String... params) {
            updateReminder(reminderId, year, month, day, hour, minute);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, M);
                    calendar.set(Calendar.DAY_OF_MONTH, d);
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);
                    calendar.set(Calendar.SECOND, 0);
                    Toast.makeText(context, "Alarm updated " + calendar.getTime().toString() + " seconds",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class DeleteReminder extends AsyncTask<String, Void, Void> {

        private Integer requestId;
        private Context context;
        private int y,M,d,h,m;


        public DeleteReminder(Context context,Integer requestId, int year, int month, int day, int hour, int minute) {

            this.y = year;
            this.M = month;
            this.d= day;
            this.h = hour;
            this.m = minute;
            this.context = context;
            this.requestId = requestId;
        }

        @Override
        protected Void doInBackground(String... params) {
            deleteReminder(requestId);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, M);
                    calendar.set(Calendar.DAY_OF_MONTH, d);
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);
                    calendar.set(Calendar.SECOND, 0);
                    Toast.makeText(context, "Alarm deleted " + calendar.getTime().toString() + " seconds",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }





}


//
//DueDateDialog.Holder holder = reminderHelper.returnUpdatedValues();
//hasReminder = holder.bool;
//        if(hasReminder){
//        Calendar calendar1 = (Calendar) holder.cal.clone();
//        noteCurrent.setDueTime((Calendar) calendar1.clone());
//        year= calendar1.get(Calendar.YEAR);
//        month = calendar1.get(Calendar.MONTH);
//        day = calendar1.get(Calendar.DAY_OF_MONTH);
//        hour = calendar1.get(Calendar.HOUR_OF_DAY);
//        minute = calendar1.get(Calendar.MINUTE);
//        }
//
//
//        if (hadReminder) {
//        if (hasReminder) {
//        Calendar calendar = new GregorianCalendar();
//        calendar.set(Calendar.YEAR, year);
//        calendar.set(Calendar.MONTH, month);
//        calendar.set(Calendar.DAY_OF_MONTH, day);
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);
//        hasMIlliSeconds = calendar.getTimeInMillis();
//        if (hasMIlliSeconds == hadMilliSeconds) {
//        //do nothing
//        } else {
//        //update
//        updateReminder(requestId, year, month, day, hour, minute);
//        }
//        } else {
//        //delete current reminder
//        deleteReminder(requestId);
//        }
//
//        } else {
//        if (hasReminder) {
//        //add reminder
//        saveReminder(requestId, year, month, day, hour, second);
//        } else {
//        //do nothing
//        }
//        }