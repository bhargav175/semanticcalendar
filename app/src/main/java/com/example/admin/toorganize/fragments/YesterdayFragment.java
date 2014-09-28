package com.example.admin.toorganize.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.toorganize.R;
import com.example.admin.toorganize.activities.HomeActivity;
import com.example.admin.toorganize.activities.ViewTaskActivity;
import com.example.admin.toorganize.helpers.Event;
import com.example.admin.toorganize.models.Task;
import com.example.admin.toorganize.utils.utilFunctions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class YesterdayFragment extends Fragment {

    public static final String Tag = "CalendarPrint";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_yesterday, container, false);



        CalendarView myCalendar = (CalendarView) rootView.findViewById(R.id.calendarView);

        myCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                Toast.makeText(getActivity().getApplicationContext(), date + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                Calendar tempCal = GregorianCalendar.getInstance();
                tempCal.set(year,month,date);
                uiUpdate(tempCal, rootView);
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar cal = GregorianCalendar.getInstance();

        System.out.println(dateFormat.format(cal.getTime())); //2014/08/06 16:00:22
        int month=cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hours= cal.get(Calendar.HOUR_OF_DAY);
        int mins= cal.get(Calendar.MINUTE);
        int sec= cal.get(Calendar.SECOND);
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day-1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);

        myCalendar.setDate(cal.getTimeInMillis());

        return rootView;
    }

    public  void uiUpdate(Calendar tempCal, View rootView){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar cal = GregorianCalendar.getInstance();
        Calendar cal2 = GregorianCalendar.getInstance();

        System.out.println(dateFormat.format(tempCal.getTime())); //2014/08/06 16:00:22
        int month=tempCal.get(Calendar.MONTH);
        int day = tempCal.get(Calendar.DAY_OF_MONTH);
        int year = tempCal.get(Calendar.YEAR);
        int hours= tempCal.get(Calendar.HOUR_OF_DAY);
        int mins= tempCal.get(Calendar.MINUTE);
        int sec= tempCal.get(Calendar.SECOND);
        cal.set(Calendar.YEAR,year);
        cal2.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal2.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal2.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal2.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,0);
        cal2.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,0);
        cal2.set(Calendar.SECOND,59);


        Log.d(Tag, cal.getTime().toString());
        Log.d(Tag,cal2.getTime().toString());

        Cursor cursor2 = getActivity().getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.TITLE,CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.DURATION, CalendarContract.Events.CALENDAR_DISPLAY_NAME},
                        CalendarContract.Events.DTSTART+" BETWEEN \""+cal.getTimeInMillis() +"\" AND \""+cal2.getTimeInMillis()+"\"",
                        null, CalendarContract.Events.DTSTART + " ASC");
        final List<Event> events = new ArrayList<Event>();



        if(cursor2 != null && cursor2.moveToFirst()) {
            cursor2.moveToFirst();
            // fetching calendars name
            String CNames[] = new String[cursor2.getCount()];
            String title,description,calendarDisplayName;
            String startTimeMillis = null,endTimeMillis =null;
            for (int i = 0; i < CNames.length; i++) {
                title= utilFunctions.getCursorEntity(cursor2.getString(1));
                description= utilFunctions.getCursorEntity(cursor2.getString(2));
                calendarDisplayName= utilFunctions.getCursorEntity(cursor2.getString(6));
                if(cursor2.getString(3)!=null){
                    startTimeMillis=utilFunctions.getDateFromString((cursor2.getString(3)));
                }
                if(cursor2.getString(4)!=null){
                    endTimeMillis=utilFunctions.getDateFromString((cursor2.getString(4)));
                }
                Event event = new Event(title,description,startTimeMillis,endTimeMillis,calendarDisplayName);

                Log.d(Tag,title);
                Log.d(Tag,description);
                Log.d(Tag,utilFunctions.getDateFromString((cursor2.getString(3))));
                Log.d(Tag,utilFunctions.getDateFromString((cursor2.getString(4))));
                Log.d(Tag,calendarDisplayName);
                cursor2.moveToNext();
                events.add(event);

            }

        }
        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(getActivity(),
                R.layout.event_list_item,R.id.el_text1, events){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.el_text1);
                TextView text2 = (TextView) view.findViewById(R.id.el_text2);
                text1.setText(events.get(position).getTitle());
                text2.setText(events.get(position).getCalendarDisplayName());
                return view;
            }

        };



        ListView listView =(ListView) rootView.findViewById(R.id.yesterdayEventList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.update_event_dialog);

                dialog.show();
//                Calendar cal = Calendar.getInstance();
//                Intent intent = new Intent(Intent.ACTION_EDIT);
//                intent.setType("vnd.android.cursor.item/event");
//                intent.putExtra("beginTime", cal.getTimeInMillis());
//                intent.putExtra("allDay", true);
//                intent.putExtra("rrule", "FREQ=YEARLY");
//                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
//                intent.putExtra("title", "A Test Event from android app");
//                startActivity(intent);


            }
        } );


    }

}
