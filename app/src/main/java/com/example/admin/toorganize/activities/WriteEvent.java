package com.example.admin.toorganize.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.toorganize.R;
import com.example.admin.toorganize.adapters.ContactAutoAdapter;
import com.example.admin.toorganize.database.TaskDBHelper;
import com.example.admin.toorganize.fragments.FromEventDateDialogFragment;
import com.example.admin.toorganize.fragments.FromEventTimeDialogFragment;
import com.example.admin.toorganize.fragments.ToEventDateDialogFragment;
import com.example.admin.toorganize.fragments.ToEventTimeDialogFragment;
import com.example.admin.toorganize.models.EContact;
import com.example.admin.toorganize.models.ReminderCl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WriteEvent extends Activity implements View.OnClickListener {
    public static final String TAG ="EventPhoto";
    private TaskDBHelper taskDbHelper;
    private EditText eventText,eventDescription,eventLocation ,setFromDateBtn,setToDateBtn,setFromTimeBtn,setToTimeBtn;
    private MultiAutoCompleteTextView guestAdd;
    private ArrayList<EContact> contacts;
    private Button saveEventBtn, addReminderButton, cancelEventBtn;
    private LinearLayout reminderContainer;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    String[] remStrings={"0 minutes","1 minute" ,"5 minutes","10 minutes","15 minutes", "20 minute" , "30 minutes", "45 minutes","1 hour", " 2 hours","3 hours","12 hours","24 hours","2 days", "1 week"};
    Integer[] remInts ={0,60,300,600,900,1200,1800,2700,3600,7200,10800,43200,86400,172800,604800};
    String[] typeStrings={"Notification","Email"};
    Integer[] typeInts={CalendarContract.Reminders.METHOD_ALARM, CalendarContract.Reminders.METHOD_EMAIL};
    Calendar fromDate,toDate;
    public Boolean hasAlarm = false;
    public Boolean needsMailService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_event);
        initUi();
        setListeners();
        asyncContact();

    }

    private void asyncContact() {
        new connection_test().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.write_event, menu);
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
        return super.onOptionsItemSelected(item);
    }

    private class connection_test extends AsyncTask<Void, Object, ArrayList<EContact>> {

        @Override
        protected void onPreExecute()
        {
            guestAdd.setFocusable(false);
            WriteEvent.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WriteEvent.this, "Fetching Contacts", Toast.LENGTH_SHORT).show();
                }
            });
        }

        protected void onPostExecute(ArrayList<EContact> contactss)
        {
            setAutoComplete(contactss);
            contacts = (ArrayList<EContact>) contactss.clone();
            guestAdd.setFocusableInTouchMode(true);
            WriteEvent.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WriteEvent.this, "Fetched Contacts", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        protected ArrayList<EContact> doInBackground(Void... params) {
            try {
                return addAutoCompleteAttendees();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<EContact>();

        }
    }
    private void initUi() {
        guestAdd = (MultiAutoCompleteTextView)findViewById(R.id.guest_view);
        fromDate = Calendar.getInstance();
        toDate =  Calendar.getInstance();
        eventText = (EditText) findViewById(R.id.eventTitle);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        setFromDateBtn=(EditText)findViewById(R.id.editFromDate);
        setToDateBtn=(EditText)findViewById(R.id.editToDate);
        setFromTimeBtn=(EditText)findViewById(R.id.editFromTime);
        setToTimeBtn=(EditText)findViewById(R.id.editToTime);
        taskDbHelper = new TaskDBHelper(this);
        taskDbHelper.open();
        saveEventBtn = (Button) findViewById(R.id.save_event_btn);
        addReminderButton = (Button) findViewById(R.id.add_reminder_button);
        cancelEventBtn = (Button) findViewById(R.id.cancel_event_button);
        reminderContainer = (LinearLayout) findViewById(R.id.reminder_container);
        fromDate.set(Calendar.HOUR_OF_DAY, 21);
        fromDate.set(Calendar.MINUTE,0);
        toDate.set(Calendar.HOUR_OF_DAY,22);
        toDate.set(Calendar.MINUTE,0);
        setFromDateEditText(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));
        setToDateEditText(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));
        setFromTimeEditText(fromDate.get(Calendar.HOUR_OF_DAY), fromDate.get(Calendar.MINUTE));
        setToTimeEditText(toDate.get(Calendar.HOUR_OF_DAY), toDate.get(Calendar.MINUTE));

    }

    private void setAutoComplete(ArrayList<EContact> contactss){
        ArrayAdapter<EContact> adapter = new ContactAutoAdapter(this,
                R.layout.contact_display_item,R.id.textView, contactss);
        guestAdd.setAdapter(adapter);
        guestAdd.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private ArrayList<EContact> addAutoCompleteAttendees() throws IOException {
        ArrayList<EContact> contactss = new ArrayList<EContact>();

        ContentResolver cr = getContentResolver();

        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);

        while (emailCur.moveToNext())
        {
            EContact eContact = new EContact();
            eContact.setContact_id(Long.valueOf(emailCur.getString(emailCur.getColumnIndex(ContactsContract.Profile._ID))));
            eContact.setName(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)));
            eContact.setEmail(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
            eContact.setPhoto_uri(emailCur.getString(emailCur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)));
            contactss.add(eContact);
        }
        emailCur.close();

        return contactss;
    }

    private Uri getPhotoUriFromID(String id) {
        try {
            Cursor cur = getContentResolver()
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + id
                                    + " AND "
                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }




    private void setListeners() {
        saveEventBtn.setOnClickListener(this);

        setFromDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DialogFragment newFragment = new FromEventDateDialogFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
        setToDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DialogFragment newFragment = new ToEventDateDialogFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
        setFromTimeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DialogFragment newFragment = new FromEventTimeDialogFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
        setToTimeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DialogFragment newFragment = new ToEventTimeDialogFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                addReminder();
            }
        });

    }
    public void addReminder(){
        if(hasAlarm==false){
            hasAlarm = true;
        }
        LinearLayout li = new LinearLayout(this);
        li.setOrientation(LinearLayout.HORIZONTAL);

        Spinner rem=new Spinner(this);
        rem.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,remStrings));
        rem.setSelection(3);
        Spinner type=new Spinner(this);
        type.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,typeStrings));
        type.setSelection(0);
        Button b=new Button(this);
        b.setText("X");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout =(LinearLayout) v.getParent();
                reminderContainer.removeView(linearLayout);
                if(reminderContainer.getChildCount()>0){
                    hasAlarm = true;
                }else{
                    hasAlarm = false;
                }
            }
        });
        LinearLayout.LayoutParams remParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        remParams.setMargins(5,5,5,5);
        remParams.weight=4;
        rem.setLayoutParams(remParams);
        LinearLayout.LayoutParams typeParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        typeParams.setMargins(5,5,5,5);
        typeParams.weight=4;
        type.setLayoutParams(typeParams);
        LinearLayout.LayoutParams bParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bParams.weight=1;
        b.setLayoutParams(bParams);
        li.addView(rem);
        li.addView(type);
        li.addView(b);
        reminderContainer.addView(li);
    }






        public void setFromDateEditText(int year,int month, int day){
        fromDate.set(Calendar.YEAR,year);
        fromDate.set(Calendar.MONTH,month);
        fromDate.set(Calendar.DAY_OF_MONTH,day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = sdf.format(fromDate.getTime());
        setFromDateBtn.setText(formattedTime);
        validateDate();

    }

    public void setToDateEditText(int year,int month, int day){
        toDate.set(Calendar.YEAR,year);
        toDate.set(Calendar.MONTH,month);
        toDate.set(Calendar.DAY_OF_MONTH,day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = sdf.format(toDate.getTime());
        setToDateBtn.setText(formattedTime);
        validateDate();

    }
    public void validateDate(){
        if(toDate.getTimeInMillis()<fromDate.getTimeInMillis()){
            toDate.set(Calendar.YEAR,fromDate.get(Calendar.YEAR));
            toDate.set(Calendar.MONTH,fromDate.get(Calendar.MONTH));
            toDate.set(Calendar.DAY_OF_MONTH,fromDate.get(Calendar.DAY_OF_MONTH));
            toDate.set(Calendar.HOUR_OF_DAY,fromDate.get(Calendar.HOUR_OF_DAY));
            toDate.set(Calendar.MINUTE,fromDate.get(Calendar.MINUTE));
            setToDateEditText(toDate.get(Calendar.YEAR),toDate.get(Calendar.MONTH),toDate.get(Calendar.DAY_OF_MONTH));
            setToTimeEditText(toDate.get(Calendar.HOUR_OF_DAY),toDate.get(Calendar.MINUTE));
        }else{

        }

    }


    public void setFromTimeEditText(int hourOfDay,int minute){
        fromDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
        fromDate.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("HH::mm");
        String formattedTime = sdf.format(fromDate.getTime());
        setFromTimeBtn.setText(formattedTime);
        validateDate();

    }
    public void setToTimeEditText(int hourOfDay,int minute){
        toDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
        toDate.set(Calendar.MINUTE,minute);
        while(toDate.getTimeInMillis()<fromDate.getTimeInMillis()) {
            toDate.set(Calendar.DAY_OF_MONTH,toDate.get(Calendar.DAY_OF_MONTH)+1);
            setToDateEditText(toDate.get(Calendar.YEAR),toDate.get(Calendar.MONTH),toDate.get(Calendar.DAY_OF_MONTH));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH::mm");
        String formattedTime = sdf.format(toDate.getTime());
        setToTimeBtn.setText(formattedTime);
        validateDate();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save_event_btn:
                String title =eventText.getText().toString();
                String description =eventDescription.getText().toString();
                Long startMillis = fromDate.getTimeInMillis();
                Long endMillis = toDate.getTimeInMillis();
                String[] emailStrings=guestAdd.getText().toString().split(", ");

//                for(int i=0; i<emailStrings.length; i++) {
//
//
//                }
//
//
//                for(EContact eContact : contacts){
//
//                }
//



                ContentResolver contentResolver = getContentResolver();
                ContentValues calEvent = new ContentValues();
                calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
                calEvent.put(CalendarContract.Events.TITLE, title);
                calEvent.put(CalendarContract.Events.DESCRIPTION, description);
                calEvent.put(CalendarContract.Events.DTSTART, startMillis);
                calEvent.put(CalendarContract.Events.DTEND, endMillis);
                calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "India");



                Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, calEvent);

                // The returned Uri contains the content-retriever URI for
                // the newly-inserted event, including its id


                int id = Integer.parseInt(uri.getLastPathSegment());
                Toast.makeText(this, "Created Calendar Event " + id,
                        Toast.LENGTH_SHORT).show();


                //Add reminders here

                if(hasAlarm ){
                    List<ReminderCl> reminderCl = new ArrayList<ReminderCl>();
                    int childCount = reminderContainer.getChildCount();

                    for (int i=0; i < childCount; i++){
                        LinearLayout ll = (LinearLayout) reminderContainer.getChildAt(i);
                        Spinner e1 = (Spinner) ll.getChildAt(0);
                        Spinner e2 = (Spinner) ll.getChildAt(1);
                        Integer i1 = e1.getSelectedItemPosition();
                        Integer i2 = e2.getSelectedItemPosition();

                        ContentValues reminderValues = new ContentValues();

                        reminderValues.put("event_id", id);
                        reminderValues.put("minutes", remInts[i1]); // Default value of the
                        // system. Minutes is a
                        // integer


                        reminderValues.put("method",typeInts[i2]); // Alert Methods: Default(0),
                        // Alert(1), Email(2),
                        // SMS(3)
                        Uri remUri = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues);

                    }
                    needsMailService=true;
                    if (needsMailService) {
                        String attendeuesesUriString = "content://com.android.calendar/attendees";

                        /********
                         * To add multiple attendees need to insert ContentValues multiple
                         * times
                         ***********/
                        ContentValues attendeesValues = new ContentValues();

                        attendeesValues.put("event_id", id);
                        attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
                        attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
                        // E
                        // mail
                        // id
                        attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
                        // Relationship_None(0),
                        // Organizer(2),
                        // Performer(3),
                        // Speaker(4)
                        attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
                        // Required(2), Resource(3)
                        attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
                        // Decline(2),
                        // Invited(3),
                        // Tentative(4)

                        Uri attendeuesesUri = getContentResolver().insert(CalendarContract.Attendees.CONTENT_URI, attendeesValues);
                    }
                }

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
