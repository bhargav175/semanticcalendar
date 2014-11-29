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
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.Reminder;
import com.bhargav.smart.smartTasks.utils.MyBroadcastReceiver;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Admin on 12-10-2014.
 */
public class RepeatingTaskReminderHelper {




        private Context context;
        private Boolean hadReminder, hasReminder;
        private Reminder currentReminder;
        private Integer reminderId;
        private Calendar dueDate;
        private RepeatingTask repeatingTaskCurrent;

        private int year,month,day,hour,minute,second;
        private TextView textView;
        private DueHabitDialog mAlertBuilder;
        private AlertDialog mAlert;
       private FragmentActivity f;
       private ReminderDBHelper reminderDBHelper;
        private Integer itemId,itemType;

    public RepeatingTaskReminderHelper(Context context, FragmentActivity f, Integer reminderId, Calendar dueDate, TextView textView, RepeatingTask repeatingTaskCurrent, Integer itemType) {
        this.context = context;
        this.itemId = repeatingTaskCurrent.getId();
        this.itemType = itemType;
        this.dueDate = dueDate;
        this.textView = textView;
        this.repeatingTaskCurrent = repeatingTaskCurrent;
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
    public DueHabitDialog.Holder returnUpdatedValues(){
        return this.mAlertBuilder.returnUpdatedValues();
    }
    public DueHabitDialog.Holder doSomething(Boolean isReminded){
        DueHabitDialog.Holder holder = returnUpdatedValues();
        Calendar cal =(Calendar) holder.cal.clone();
        year= cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
            //there is a reminder

        if(isReminded){
            if(hadReminder){
                //update
                new UpdateReminder(context,holder,reminderId,hour,minute).execute("");

            }
            else{
                //save
                new SaveReminder(context,holder,reminderId,hour,minute).execute("");

            }
        }else{

        }
            if(hadReminder){
                //update
                new UpdateReminder(context,holder,reminderId,hour,minute).execute("");

            }
            else{
                //save
                if(reminderId!=null){
                    deleteReminder(reminderId);
                }else{

                }


            }

        return holder;
    }





    private void updateReminder( DueHabitDialog.Holder holder,Integer requestId,int hour, int minute) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        intent.putExtra(DBHelper.COLUMN_ID,itemId);
        intent.putExtra("Type",itemType);
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Reminder reminder = new Reminder(0, 0, 0, hour, minute, second, 0, 0, false);
            reminder.setId(requestId);
            reminderDBHelper.open();
            reminderDBHelper.updateReminder(reminder);
            reminderDBHelper.close();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,pendingIntent);
          }
    }

    private void saveReminder( DueHabitDialog.Holder holder,Integer requestId, int hour, int minute) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        intent.putExtra(DBHelper.COLUMN_ID,itemId);
        intent.putExtra("Type",itemType);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Reminder reminder = new Reminder(0, 0, 0, hour, minute, second, 0, 0, false);
            reminder.setId(requestId);
            reminderDBHelper.open();
            reminderDBHelper.saveReminder(reminder);
            reminderDBHelper.close();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,pendingIntent);


        }
    }
    private void deleteReminder(Integer requestId) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
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
                    mAlertBuilder=new DueHabitDialog(f, repeatingTaskCurrent,dueDate,textView,year,month,day,hour,minute,hasReminder,reminderId);
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
        private int h,m;
        private DueHabitDialog.Holder holder;

        public SaveReminder(Context context,DueHabitDialog.Holder holder,Integer requestId,int hour, int minute) {

            this.holder = holder;
            this.h = hour;
            this.m = minute;
            this.context = context;
            this.requestId = requestId;
        }

        @Override
        protected Void doInBackground(String... params) {
            saveReminder(holder,reminderId,hour,minute);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, 0);
                    calendar.set(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 0);
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);
                    calendar.set(Calendar.SECOND, 0);
                    Toast.makeText(context, "Reminder set at" + new SimpleDateFormat(utilFunctions.timeFormat).format( calendar.getTime()) + "",
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
        private DueHabitDialog.Holder holder;


        public UpdateReminder(Context context,DueHabitDialog.Holder holder,Integer requestId,int hour, int minute) {

            this.holder = holder;
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
            updateReminder(holder,reminderId,hour, minute);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    Toast.makeText(context, "Alarm updated " + new SimpleDateFormat(utilFunctions.timeFormat).format( calendar.getTime()),
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
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    Toast.makeText(context, "Alarm deleted " +new SimpleDateFormat(utilFunctions.timeFormat).format( calendar.getTime()),
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

