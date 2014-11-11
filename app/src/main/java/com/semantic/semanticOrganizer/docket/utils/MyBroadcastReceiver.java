package com.semantic.semanticOrganizer.docket.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.activities.UpdateCheckListActivity;
import com.semantic.semanticOrganizer.docket.activities.UpdateNoteActivity;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.CheckList;
import com.semantic.semanticOrganizer.docket.models.Note;

/**
 * Created by Admin on 09-10-2014.
 */
public class MyBroadcastReceiver extends BroadcastReceiver
{
    private Note noteCurrent;
    private CheckList checkListCurrent;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Alarm executing",
//                Toast.LENGTH_LONG).show();
//        Log.d("ME", "Notification started");

        Bundle extras= intent.getExtras();
        if(extras!=null){
            Integer id = extras.getInt(DBHelper.COLUMN_ID);
            Integer type = extras.getInt("Type");
            if(type == 1){
                    new GetNote(id,context);
            }else if(type==2){
                new GetCheckList(id,context);
            }

        }




        // Vibrate the mobile phone
        /*Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);*/
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