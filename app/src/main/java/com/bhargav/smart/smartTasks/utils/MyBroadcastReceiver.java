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

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.activities.UpdateRepeatingTaskActivity;
import com.bhargav.smart.smartTasks.activities.UpdateOneTimeTaskActivity;
import com.bhargav.smart.smartTasks.activities.ViewOneTimeTaskActivity;
import com.bhargav.smart.smartTasks.activities.ViewRepeatingTaskActivity;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.OneTimeTask;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 09-10-2014.
 */
public class MyBroadcastReceiver extends BroadcastReceiver
{
    private OneTimeTask oneTimeTaskCurrent;
    private RepeatingTask repeatingTaskCurrent;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm executing",
                Toast.LENGTH_LONG).show();


        Bundle extras= intent.getExtras();
        if(extras!=null){
            Integer id = extras.getInt(DBHelper.COLUMN_ID);
            Integer type = extras.getInt("Type");
            if(type == 1){
                oneTimeTaskCurrent = OneTimeTask.getNote(id, context);
                if (oneTimeTaskCurrent != null) {
                    Intent notificationIntent = new Intent(context, ViewOneTimeTaskActivity.class);
                    notificationIntent.putExtra(DBHelper.COLUMN_ID, oneTimeTaskCurrent.getId());
                    PendingIntent contentIntent = PendingIntent.getActivity(context,
                            0, notificationIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setContentIntent(contentIntent)
                                    .setSmallIcon(R.drawable.docket_logo)
                                    .setContentTitle(oneTimeTaskCurrent.getNoteTitle())
                                    .setContentText(oneTimeTaskCurrent.getCreatedTime().getTime().toString());

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());

                } else {

                }
            }if(type==2){


            }if(type==3){
                repeatingTaskCurrent = RepeatingTask.getRepeatingTaskById(id, context);
                if (repeatingTaskCurrent != null) {
                    Boolean triggerNotification = false;
                    String notificationTitle= "";
                    String notificationText ="";
                    Intent notificationIntent = new Intent(context, ViewRepeatingTaskActivity.class);
                    notificationIntent.putExtra(DBHelper.COLUMN_ID, repeatingTaskCurrent.getId());
                    PendingIntent contentIntent = PendingIntent.getActivity(context,
                            0, notificationIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    if(repeatingTaskCurrent.getRepeatingTaskType() == RepeatingTask.Type.FIXED){
                            Integer daysCode = repeatingTaskCurrent.getDaysCode();

                        if(daysCode!=null){


                            List<Boolean> daysBoolean = RepeatingTask.getBooleansFromDayCode(daysCode);
                            triggerNotification= isFixedDayToRemind(daysBoolean);
                            notificationTitle = repeatingTaskCurrent.getRepeatingTaskText();
                            notificationText = repeatingTaskCurrent.getDaysCode().toString();
                            }else{

                            }

                    }
                    else if(repeatingTaskCurrent.getRepeatingTaskType() == RepeatingTask.Type.FLEXIBLE){

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

    private class GetNote extends AsyncTask<String, Void, OneTimeTask> {

        private int id;
        private Context context;

        public GetNote(int id, Context context) {
            this.id = id; this.context = context;
        }

        @Override
        protected OneTimeTask doInBackground(String... params) {

            oneTimeTaskCurrent = OneTimeTask.getNote(id, context);
            return oneTimeTaskCurrent;
        }

        @Override
        protected void onPostExecute(final OneTimeTask oneTimeTask) {

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



}