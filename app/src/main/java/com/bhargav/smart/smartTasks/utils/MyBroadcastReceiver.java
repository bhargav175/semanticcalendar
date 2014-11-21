package com.bhargav.smart.smartTasks.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.semantic.semanticOrganizer.docket.R;
import com.bhargav.smart.smartTasks.activities.UpdateCheckListActivity;
import com.bhargav.smart.smartTasks.activities.UpdateHabitActivity;
import com.bhargav.smart.smartTasks.activities.UpdateNoteActivity;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.CheckList;
import com.bhargav.smart.smartTasks.models.Habit;
import com.bhargav.smart.smartTasks.models.Note;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 09-10-2014.
 */
public class MyBroadcastReceiver extends BroadcastReceiver
{
    private Note noteCurrent;
    private CheckList checkListCurrent;
    private Habit habitCurrent;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm executing",
                Toast.LENGTH_LONG).show();


        Bundle extras= intent.getExtras();
        if(extras!=null){
            Integer id = extras.getInt(DBHelper.COLUMN_ID);
            Integer type = extras.getInt("Type");
            if(type == 1){
                noteCurrent = Note.getNote(id,context );
                if (noteCurrent != null) {
                    Intent notificationIntent = new Intent(context, UpdateNoteActivity.class);
                    notificationIntent.putExtra(DBHelper.COLUMN_ID,noteCurrent.getId());
                    PendingIntent contentIntent = PendingIntent.getActivity(context,
                            0, notificationIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setContentIntent(contentIntent)
                                    .setSmallIcon(R.drawable.docket_logo)
                                    .setContentTitle(noteCurrent.getNoteTitle())
                                    .setContentText(noteCurrent.getCreatedTime());

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());

                } else {

                }
            }if(type==2){
                checkListCurrent = CheckList.getCheckListById(id, context);
                if (checkListCurrent != null) {
                    Intent notificationIntent = new Intent(context, UpdateCheckListActivity.class);
                    notificationIntent.putExtra(DBHelper.COLUMN_ID,checkListCurrent.getId());
                    PendingIntent contentIntent = PendingIntent.getActivity(context,
                            0, notificationIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setContentIntent(contentIntent)
                                    .setSmallIcon(R.drawable.docket_logo)
                                    .setContentTitle(checkListCurrent.getCheckListTitle())
                                    .setContentText(checkListCurrent.getCreatedTime());

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());
                } else {

                }

            }if(type==3){
                habitCurrent = Habit.getHabitById(id, context);
                if (habitCurrent != null) {
                    Boolean triggerNotification = false;
                    String notificationTitle= "";
                    String notificationText ="";
                    Intent notificationIntent = new Intent(context, UpdateHabitActivity.class);
                    notificationIntent.putExtra(DBHelper.COLUMN_ID,habitCurrent.getId());
                    PendingIntent contentIntent = PendingIntent.getActivity(context,
                            0, notificationIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    if(habitCurrent.getHabitType() == Habit.Type.FIXED){
                            Integer daysCode = habitCurrent.getDaysCode();

                        if(daysCode!=null){


                            List<Boolean> daysBoolean = Habit.getBooleansFromDayCode(daysCode);
                            triggerNotification= isFixedDayToRemind(daysBoolean);
                            notificationTitle = habitCurrent.getHabitText();
                            notificationText = habitCurrent.getDaysCode().toString();
                            }else{

                            }

                    }
                    else if(habitCurrent.getHabitType() == Habit.Type.FLEXIBLE){

                    }
                    if(triggerNotification){
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setContentIntent(contentIntent)
                                        .setSmallIcon(R.drawable.docket_logo)
                                        .setContentTitle(notificationTitle)
                                        .setContentText(notificationText);
                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());
                    }

                } else {

                }
            }

        }




        // Vibrate the mobile phone
        /*Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);*/
    }

    private Boolean isFixedDayToRemind(List<Boolean> daysBoolean){
        Boolean sun,mon,tue,wed,thu,fri,sat;
        sun = daysBoolean.get(0);
        mon = daysBoolean.get(1);
        tue = daysBoolean.get(2);
        wed = daysBoolean.get(3);
        thu = daysBoolean.get(4);
        fri = daysBoolean.get(5);
        sat = daysBoolean.get(6);

        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY && sun){
            return true;
        }
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY && mon){
            return true;
        }
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY && tue){
            return true;
        }
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY && wed){
            return true;
        }
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY && thu){
            return true;
        }
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY && fri){
            return true;
        }
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY && sat){
            return true;
        }else{
            return false;
        }
    }

    private class GetNote extends AsyncTask<String, Void, Note> {

        private int id;
        private Context context;

        public GetNote(int id, Context context) {
            this.id = id; this.context = context;
        }

        @Override
        protected Note doInBackground(String... params) {

            noteCurrent = Note.getNote(id,context );
            return noteCurrent;
        }

        @Override
        protected void onPostExecute(final Note note) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

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

    private class GetCheckList extends AsyncTask<String, Void, CheckList> {

        private int id;
        private Context context;

        public GetCheckList(int id, Context context) {
            this.id = id; this.context = context;
        }

        @Override
        protected CheckList doInBackground(String... params) {

            checkListCurrent = CheckList.getCheckListById(id, context);
            return checkListCurrent;
        }

        @Override
        protected void onPostExecute(final CheckList checkListCurrent) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (checkListCurrent != null) {
                        Intent notificationIntent = new Intent(context, UpdateCheckListActivity.class);
                        notificationIntent.putExtra(DBHelper.COLUMN_ID,checkListCurrent.getId());
                        PendingIntent contentIntent = PendingIntent.getActivity(context,
                                0, notificationIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setContentIntent(contentIntent)
                                        .setSmallIcon(R.drawable.docket_logo)
                                        .setContentTitle(checkListCurrent.getCheckListTitle())
                                        .setContentText(checkListCurrent.getCreatedTime());

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());
                    } else {

                    }
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