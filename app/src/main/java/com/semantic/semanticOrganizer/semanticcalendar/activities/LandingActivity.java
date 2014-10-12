package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.CheckListDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.HabitDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.TagDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.CheckList;
import com.semantic.semanticOrganizer.semanticcalendar.models.Habit;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.OrganizerItem;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;
import com.semantic.semanticOrganizer.semanticcalendar.utils.MyBroadcastReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class LandingActivity extends Activity {

    private Button tag_btn,checklist_btn,note_btn,alarm_btn,habit_btn ,add_item;
    private EditText addItemEditText;
    private LinearLayout homeLayout, horizontalLayout;
    private NoteDBHelper noteDBHelper;
    private HabitDBHelper habitDBHelper;
    private TagDBHelper tagDBHelper;
    private CheckListDBHelper checkListDBHelper;
    private List<OrganizerItem> mOrganizerItemList;
    private ActionMode mActionMode;
    private RelativeLayout parentLayout;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_archive:
                    archiveSelectedItems();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_delete:
                    deleteSelectedItems();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_refactor);

        initializeUi();
        setListeners();
        populateUi();
        //showNotif();

    }
    private void showNotif(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.abc_ic_clear)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setSubText("Sub text");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ListChecklistsActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ListNotesActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void populateUi(){

        new GetNotes().execute("");
        new GetTags().execute("");


    }

    private void populateTags(final List<Tag> tagList) {
        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        if(horizontalLayout!=null){

            mOrganizerItemList = new ArrayList<OrganizerItem>();

            for(final Tag tag : tagList){
                View view =layoutInflater.inflate(R.layout.list_layout, horizontalLayout, false);
                TextView tagTitle = (TextView) view.findViewById(R.id.heading);
                tagTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(), UpdateTagActivity.class);
//                        intent.putExtra(DBHelper.TASK_TITLE, tag.getTagText());
//                        intent.putExtra(DBHelper.COLUMN_ID, tag.getTagId());
//                        startActivity(intent);

                        String names[] ={"Edit Tag","Archive All Tag Items","Archive Tag"};
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LandingActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View convertView = (View) inflater.inflate(R.layout.edit_tag_dialog, null);
                        alertDialog.setView(convertView);
                        alertDialog.setTitle("Select Action");
                        alertDialog.setCancelable(true);
                        final AlertDialog alert = alertDialog.create();
                        final ListView lv = (ListView) convertView.findViewById(R.id.lv);
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                String str;
                                alert.cancel();
                                switch (position){
                                    case 0:
                                        str = "Edit Tag";
                                        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), UpdateTagActivity.class);
                                        intent.putExtra(DBHelper.TAG_TITLE, tag.getTagText());
                                        intent.putExtra(DBHelper.COLUMN_ID, tag.getTagId());
                                        intent.putExtra(DBHelper.TAG_DESCRIPTION, tag.getTagDescription());
                                        intent.putExtra(DBHelper.TAG_IS_ARCHIVED, tag.getIsArchived());
                                        startActivity(intent);

                                        break;
                                    case 1:
                                        str = "Archive Items";

                                        break;
                                    case 2:
                                        str = "Archive Tag";
                                        Tag.archiveTag(tag,getApplicationContext());
                                        Intent intent2 = new Intent(LandingActivity.this, LandingActivity.class);
                                        startActivity(intent2);
                                        break;
                                    default:
                                        str = "Error";
                                        break;
                                }
                                Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
                            }
                        });
                        lv.setAdapter(adapter);
                        alert.show();


                    }
                });
                tagTitle.setText(tag.getTagText());
                ListView lv = (ListView) view.findViewById(R.id.listView);

//                final List<Note> noteList = Note.getAllNotesInTag(tag, this);
//                mNoteList.addAll(noteList);
//                final ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this,
//                        R.layout.card_simple_note,R.id.cardText1, noteList){
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        View view = super.getView(position, convertView, parent);
//                        TextView cardText1 = (TextView) view.findViewById(R.id.cardText1);
//                        cardText1.setText(noteList.get(position).getNoteText());
//                        return view;
//                    }
//
//                };

                final List<OrganizerItem> organizerItems = OrganizerItem.getOrganizerItemsWithTag(tag, this);
                mOrganizerItemList.addAll(organizerItems);
                final ArrayAdapter<OrganizerItem> adapter = new ArrayAdapter<OrganizerItem>(this,
                        R.layout.card_simple_note,R.id.cardText1, organizerItems){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView cardText1 = (TextView) view.findViewById(R.id.cardText1);
                        TextView cardText2 = (TextView) view.findViewById(R.id.cardText2);
                        cardText1.setText(organizerItems.get(position).getItemText());
                        cardText2.setText(organizerItems.get(position).getCreatedTime());
                        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                        if(organizerItems.get(position).getType().equals("NOTE")){
                            imageView.setImageResource(R.drawable.ic_action_new);

                        }else if(organizerItems.get(position).getType().equals("HABIT")){
                            imageView.setImageResource(R.drawable.ic_action_time);

                        }else if(organizerItems.get(position).getType().equals("CHECKLIST")){
                            imageView.setImageResource(R.drawable.abc_ic_go);

                        }else{
                            imageView.setImageResource(R.drawable.ic_action_play);
                        }


                        return view;
                    }

                };




                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        OrganizerItem organizerItem = (OrganizerItem) parent.getAdapter().getItem(position);

                        if(organizerItem.getType().equals("NOTE")){
                            new GetNote(organizerItem.getId()).execute("");


                        }else if(organizerItem.getType().equals("HABIT")){
                            Intent intent = new Intent(getApplicationContext(), UpdateHabitActivity.class);
                            Habit habit = Habit.getHabitById(organizerItem.getId(),getApplicationContext());
                            intent.putExtra(DBHelper.HABIT_TEXT, habit.getHabitText());
                            intent.putExtra(DBHelper.HABIT_QUESTION, habit.getHabitQuestion());
                            intent.putExtra(DBHelper.HABIT_DURATION, habit.getDuration());
                            intent.putExtra(DBHelper.HABIT_DAYS_CODE, habit.getDaysCode());
                            intent.putExtra(DBHelper.HABIT_FREQUENCY, habit.getFrequency());
                            intent.putExtra(DBHelper.HABIT_IS_ARCHIVED, habit.getIsArchived());
                            intent.putExtra(DBHelper.HABIT_TYPE, habit.getHabitType());
                            intent.putExtra(DBHelper.COLUMN_CREATED_TIME, habit.getCreatedTime());
                            intent.putExtra(DBHelper.HABIT_REQUEST_ID, habit.getRequestId());
                            intent.putExtra(DBHelper.COLUMN_ID,habit.getId());
                            startActivity(intent);
                        }else if(organizerItem.getType().equals("CHECKLIST")){
                            Intent intent = new Intent(getApplicationContext(), UpdateCheckListActivity.class);
                            intent.putExtra(DBHelper.CHECKLIST_TITLE,  organizerItem.getItemText());
                            intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                            startActivity(intent);
                        }else{

                        }



                    }
                });
                lv.setAdapter(adapter);




                    horizontalLayout.addView(view,1);

                }
            }
    }

    private void deleteSelectedItems(){
        Toast.makeText(this,"Delete",Toast.LENGTH_SHORT).show();

    }
    private void archiveSelectedItems(){
        Toast.makeText(this,"Archive",Toast.LENGTH_SHORT).show();
    }

    public void startAlert() {
        int i =10;
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pI = PendingIntent.getBroadcast(this.getApplicationContext(), 17, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        if (alarmManager!= null) {
            if(pI==null) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 17, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + i *1000, pendingIntent);
                Toast.makeText(this, "Alarm did not exist and is now set in " + i + " seconds",
                        Toast.LENGTH_SHORT).show();

            }
        }




    }
    public void cancelAlert() {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 17, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager!= null) {
            if(pendingIntent!=null){
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Alarm cancelled ",
                        Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Alarm does not exist ",
                        Toast.LENGTH_SHORT).show();

            }


        }


    }

    private void populateSandbox(final List<OrganizerItem> organizerItems) {
        if(homeLayout!=null){


           ArrayAdapter<OrganizerItem> adapter = new ArrayAdapter<OrganizerItem>(this,
                   R.layout.card_simple_note,R.id.cardText1,organizerItems ){
               @Override
               public View getView(int position, View convertView, ViewGroup parent) {
                   View view = super.getView(position, convertView, parent);
                   TextView cardText1 = (TextView) view.findViewById(R.id.cardText1);
                   cardText1.setText(organizerItems.get(position).getItemText());
                   return view;
               }

           };
           ListView list = (ListView) findViewById(R.id.sandboxCards);
           list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position,
                                       long id) {
                   OrganizerItem organizerItem = (OrganizerItem) parent.getAdapter().getItem(position);

                   if(organizerItem.getType().equals("NOTE")){
                       Intent intent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
                       intent.putExtra(DBHelper.NOTE_DESCRIPTION, organizerItem.getItemText());
                       intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                       startActivity(intent);
                   }else if(organizerItem.getType().equals("HABIT")){
                       Intent intent = new Intent(getApplicationContext(), UpdateHabitActivity.class);
                       intent.putExtra(DBHelper.HABIT_TEXT,  organizerItem.getItemText());
                       intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                       startActivity(intent);
                   }else if(organizerItem.getType().equals("CHECKLIST")){
                       Intent intent = new Intent(getApplicationContext(), UpdateCheckListActivity.class);
                       intent.putExtra(DBHelper.CHECKLIST_TITLE,  organizerItem.getItemText());
                       intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                       startActivity(intent);
                   }else{

                   }

               }
           });
           list.setAdapter(adapter);

       }
    }


    private void setListeners() {

        add_item.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                String names[] ={"Simple Note","CheckList","Habit","Tag"};
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LandingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.select_note_type_dialog, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("List");
                alertDialog.setCancelable(true);
                final AlertDialog alert = alertDialog.create();
                ListView lv = (ListView) convertView.findViewById(R.id.lv);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                       String str;
                       switch (position){
                           case 0:
                               str = "Simple Note";

                               Note note = new Note();
                               note.setNoteText(addItemEditText.getText().toString());
                               noteDBHelper.open();
                               noteDBHelper.saveNote(note);
                               noteDBHelper.close();
                               break;
                           case 1:
                               str = "CheckList";
                               CheckList checkList = new CheckList();
                               checkList.setCheckListText(addItemEditText.getText().toString());
                               checkListDBHelper.open();
                               checkListDBHelper.saveCheckList(checkList);
                               checkListDBHelper.close();
                               break;
                           case 2:
                               str = "Habit";
                               Habit habit = new Habit();
                               habit.setHabitText(addItemEditText.getText().toString());
                               habitDBHelper.open();
                               habitDBHelper.saveHabit(habit);
                               habitDBHelper.close();
                               break;
                           case 3:
                               Tag tag = new Tag();
                               tag.setTagText(addItemEditText.getText().toString());
                               tagDBHelper.open();
                               tagDBHelper.saveTag(tag);
                               tagDBHelper.close();
                               parentLayout.invalidate();

                               str = "Tag";
                               break;
                           default:
                               str = "Error";
                               break;
                       }
                        alert.cancel();
                        Toast.makeText(getApplicationContext(),addItemEditText.getText()+" "+str+" created",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),LandingActivity.class);
                        startActivity(intent);
                    }
                });
                lv.setAdapter(adapter);
                alert.show();
            }});

        tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add TAg Activity
                Intent intent = new Intent(getApplicationContext(), AddTagActivity.class);
                startActivity(intent);

            }
        });

        checklist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddChecklistActivity.class);
                startActivity(intent);
            }
        });
        note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        habit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHabitActivity.class);
                startActivity(intent);
            }
        });


    }




    private void initializeUi() {
        noteDBHelper = new NoteDBHelper(this);
        tagDBHelper = new TagDBHelper(this);
        habitDBHelper = new HabitDBHelper(this);
        checkListDBHelper = new CheckListDBHelper(this);
        add_item= (Button) findViewById(R.id.addButton);
        addItemEditText =(EditText) findViewById(R.id.addText);
        tag_btn= (Button) findViewById(R.id.tag_btn);
        checklist_btn= (Button) findViewById(R.id.checklist_btn);
        note_btn= (Button) findViewById(R.id.note_btn);
        habit_btn= (Button) findViewById(R.id.habit_btn);
        homeLayout = (LinearLayout) findViewById(R.id.homeLayout);
        parentLayout =(RelativeLayout) findViewById(R.id.parentLayout);
        horizontalLayout = (LinearLayout) findViewById(R.id.horizontalLayout);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_actvitiy_refactor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_view_tags) {
            Intent intent = new Intent(this,ListTagsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_notes) {
            Intent intent = new Intent(this,ListNotesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_checklists) {
            Intent intent = new Intent(this,ListChecklistsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_habits) {
            Intent intent = new Intent(this,ListHabitsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private class GetNotes extends AsyncTask<String, Void, List<OrganizerItem>> {

        @Override
        protected List<OrganizerItem> doInBackground(String... params) {
            List<OrganizerItem> organizerItems = OrganizerItem.getSandboxOrganizerItems(getApplicationContext());
            return organizerItems;
        }

        @Override
        protected void onPostExecute(final List<OrganizerItem> organizerItems) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    populateSandbox(organizerItems);

                    Toast.makeText(getApplicationContext(),"Executed",Toast.LENGTH_SHORT).show();
                }
            });



        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class GetTags extends AsyncTask<String, Void, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(String... params) {
            List<Tag> tagList = new ArrayList<Tag>();
            tagList = Tag.getAllUnArchivedTags(getApplicationContext());
            return tagList;
        }

        @Override
        protected void onPostExecute(final List<Tag> tagList) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    populateTags(tagList);
                    Toast.makeText(getApplicationContext(),"Executed",Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class GetNote extends AsyncTask<String, Void,Note> {

        private int id;
        public GetNote(int id){
            this.id = id;
        }

        @Override
        protected Note doInBackground(String... params) {
            Note note = new Note();
            note = Note.getNote(id,getApplicationContext());
            return note;
        }

        @Override
        protected void onPostExecute(final Note note) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(note!=null){
                        Intent intent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
                        intent.putExtra(DBHelper.NOTE_DESCRIPTION, note.getNoteText());
                        intent.putExtra(DBHelper.COLUMN_ID,note.getId());
                        intent.putExtra(DBHelper.NOTE_REQUEST_ID,note.getRemainderId());
                        intent.putExtra(DBHelper.NOTE_IS_ARCHIVED,note.getIsArchived());
                        intent.putExtra(DBHelper.NOTE_TAG,note.getTag());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }





}

