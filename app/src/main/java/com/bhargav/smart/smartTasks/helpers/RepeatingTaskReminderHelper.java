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

import static com.bhargav.smart.smartTasks.utils.utilFunctions.BLog;

/**
 * Created by Admin on 12-10-2014.
 */
public class RepeatingTaskReminderHelper {




        private Context context;
        private Boolean hadReminder, hasReminder, hadDueDate;
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
    private enum ACTION{
        SAVE_DATE_AND_REMINDER(0),SAVE_DUE_DATE_BUT_NO_REMINDER(1),UPDATE_DATE_AND_REMINDER(2),UPDATE_DATE_BUT_NO_REMINDER(3),DELETE_DATE_AND_REMINDER(4),DELETE_REMINDER(5),DO_NOTHING(6),RETURN_ALL_NULL(7),SAVE_DUE_DATE_BUT_DELETE_REMINDER(8),UPDATE_DUE_DATE_BUT_DELETE_REMINDER(9);
        private int actionValue;
        ACTION(int actionValue) {
            this.actionValue = actionValue;
        }

        public void setActionValue(int actionValue) {
            this.actionValue = actionValue;
        }

        public int getActionValue() {
            return actionValue;
        }
    }

    public RepeatingTaskReminderHelper(Context context, FragmentActivity f, Integer reminderId, Calendar dueDate, TextView textView, RepeatingTask repeatingTaskCurrent, Integer itemType) {
        this.context = context;
        this.itemId = repeatingTaskCurrent.getId();
        this.itemType = itemType;
        this.dueDate = dueDate;
        this.textView = textView;
        this.repeatingTaskCurrent = repeatingTaskCurrent;
        if(repeatingTaskCurrent.getDueTime()!=null){
            hadDueDate = true;
        }else{
            hadDueDate = false;
        }
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
    public DueHabitDialog.Holder doSomething(Boolean isReminded, Boolean hasDueDate){
        DueHabitDialog.Holder holder = returnUpdatedValues();
        DueHabitDialog.Holder initialHolder =this.mAlertBuilder.initialHolder();

        Calendar cal =(Calendar) holder.cal.clone();
        Boolean dueDateChanged =true;
        year= cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
            //there is a reminder

        //new code

        dueDateChanged = hasAnythingChanged(initialHolder, holder);
        Boolean wasReminded= hadReminder;

        holder = performActions(holder,isReminded, wasReminded, hasDueDate, dueDateChanged,year,month,day,hour,minute);

        return holder;
    }

    private DueHabitDialog.Holder performActions(DueHabitDialog.Holder holder,Boolean isReminded, Boolean wasReminded, Boolean hasDueDate, Boolean dueDateChanged, int YEAR, int MONTH,int DAY, int HOUR, int SECOND){
        ACTION a = dueDateReminderAction(isReminded, wasReminded, hasDueDate, dueDateChanged);
        switch (a){
            case SAVE_DATE_AND_REMINDER:{
                BLog(""+reminderId+"--" + year + "---"+month+"---"+day+"---"+hour+"---"+minute);
                new SaveReminder(context,holder,reminderId,hour,minute).execute("");
                break;
            }
            case SAVE_DUE_DATE_BUT_NO_REMINDER:{
                //BLog(""+reminderId+"--" + year + "---"+month+"---"+day+"---"+hour+"---"+minute);
                //new SaveReminder(context,reminderId,year,month,day,hour,minute).execute("");
                //equivalent to do nothing
                holder.remainderId=null;
                break;
            }
            case UPDATE_DATE_AND_REMINDER:{
                BLog(""+reminderId+"--" + year + "---"+month+"---"+day+"---"+hour+"---"+minute);
                new UpdateReminder(context,holder,reminderId,hour,minute).execute("");
                break;
            }
            case UPDATE_DATE_BUT_NO_REMINDER:{
                //quivalent to do nothing
                holder.remainderId=null;
                break;
            }
            case DELETE_DATE_AND_REMINDER:{
                //
                Integer remId = reminderId;
                BLog(""+reminderId+"--" + year + "---"+month+"---"+day+"---"+hour+"---"+minute);
                new DeleteReminder(context,remId,year,month,day,hour,minute).execute("");
                holder.cal = null;
                holder.startD = null;
                holder.endD = null;
                holder.remainderId = null;
                holder.curFrequency =null;
                holder.curHabitType = RepeatingTask.Type.FIXED;
                holder.daysCode = 0000000;

                break;
            }
            case DELETE_REMINDER:{
                Integer remId = reminderId;
                BLog(""+reminderId+"--" + year + "---"+month+"---"+day+"---"+hour+"---"+minute);
                new DeleteReminder(context,remId,year,month,day,hour,minute).execute("");
                holder.remainderId = null;

                break;
            }
            case DO_NOTHING:{

                break;
            }
            case RETURN_ALL_NULL:{
                holder.cal = null;
                holder.startD = null;
                holder.endD = null;
                holder.remainderId = null;
                holder.curFrequency =null;
                holder.curHabitType = RepeatingTask.Type.FIXED;
                holder.daysCode = 0000000;


                break;
            }
            case SAVE_DUE_DATE_BUT_DELETE_REMINDER:{
                Integer remId = reminderId;
                BLog(""+reminderId+"--" + year + "---"+month+"---"+day+"---"+hour+"---"+minute);
                new DeleteReminder(context,remId,year,month,day,hour,minute).execute("");
                holder.remainderId = null;
                break;
            }
            case UPDATE_DUE_DATE_BUT_DELETE_REMINDER:{
                Integer remId = reminderId;
                BLog(""+reminderId+"--" + year + "---"+month+"---"+day+"---"+hour+"---"+minute);
                new DeleteReminder(context,remId,year,month,day,hour,minute).execute("");
                holder.remainderId = null;
                break;
            }

        }

        return holder;
    }

    private Boolean hasAnythingChanged(DueHabitDialog.Holder initialHolder, DueHabitDialog.Holder newHolder){
        Integer initialFrequency,initialReminderId,initialDaysCode;
        RepeatingTask.Type initialHabitType;
        Calendar initialStartDate,initialEndDate,initialDueTime;
        initialFrequency = initialHolder.curFrequency;
        initialDaysCode = initialHolder.daysCode;
        initialReminderId = initialHolder.remainderId;
        initialHabitType = initialHolder.curHabitType;
        initialStartDate = (Calendar)initialHolder.startD.clone();
        initialEndDate = (Calendar)initialHolder.endD.clone();
        initialDueTime = (Calendar)initialHolder.cal.clone();
        Integer finalFrequency,finalReminderId,finalDaysCode;
        RepeatingTask.Type finalHabitType;
        Calendar finalStartDate,finalEndDate,finalDueTime;
        finalFrequency = newHolder.curFrequency;
        finalDaysCode = newHolder.daysCode;
        finalReminderId = newHolder.remainderId;
        finalHabitType = newHolder.curHabitType;
        finalStartDate = (Calendar)newHolder.startD.clone();
        finalEndDate = (Calendar)newHolder.endD.clone();
        finalDueTime = (Calendar)newHolder.cal.clone();
        if(initialFrequency == finalFrequency && initialDaysCode ==finalDaysCode && initialHabitType == finalHabitType && initialStartDate.compareTo(finalStartDate)==0 && initialEndDate.compareTo(finalEndDate) ==0 && initialDueTime.compareTo(finalDueTime)==0){
            return false;
        }else{
            return true;
        }


    }

    private ACTION dueDateReminderAction(Boolean isReminded, Boolean wasReminded, Boolean hasDueDate, Boolean dueDateChanged) {
        if (hadDueDate) {
            if (hasDueDate) {
                if (dueDateChanged) {
                    //update reminder
                    if (wasReminded) {
                        if (isReminded) {
                            //do nothing
                            return ACTION.UPDATE_DATE_AND_REMINDER;

                        } else {
                            //delete Reminder
                            //change reminderId to null
                            return ACTION.UPDATE_DUE_DATE_BUT_DELETE_REMINDER;


                        }
                    } else {
                        if (isReminded) {
                            //save Reminder
                            //save
                            return ACTION.SAVE_DATE_AND_REMINDER;

                        } else {
                            //do nothing
                            return ACTION.SAVE_DUE_DATE_BUT_NO_REMINDER;

                        }
                    }
                } else {
                    //do nothing
                    if (wasReminded) {
                        if (isReminded) {
                            //do nothing
                            return ACTION.DO_NOTHING;

                        } else {
                            //delete Reminder
                            //change reminderId to null
                            return ACTION.DELETE_REMINDER;


                        }
                    } else {
                        if (isReminded) {
                            //save Reminder
                            //save
                            return ACTION.SAVE_DATE_AND_REMINDER;

                        } else {
                            //do nothing
                            return ACTION.DO_NOTHING;

                        }
                    }

                }

            } else {

                //there is no due date
                // delete the reminder ob
                //change reminderId to null

                return ACTION.DELETE_DATE_AND_REMINDER;

                //return null as due date
            }

        } else {
            //return new due date as due date
            if (hasDueDate) {
                // due date change
                // no chance of wasReminded
                //return new due date as due date


                if (isReminded) {
                    //save reminder
                    return ACTION.SAVE_DATE_AND_REMINDER;

                } else {
                    //create duedate
                    return ACTION.SAVE_DUE_DATE_BUT_NO_REMINDER;

                    //no create reminder
                    //do nothing
                }


            } else {
                //did not have a due date
                //does not have a due date
                // delete the reminder ob
                //set due date to null
                //set isReminded to null
                //set reminder id to null
                return ACTION.RETURN_ALL_NULL;



            }
        }
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

