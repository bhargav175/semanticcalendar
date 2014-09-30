package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.adapters.NavDrawerListAdapter;
import com.semantic.semanticOrganizer.semanticcalendar.adapters.TabsPagerAdapter;
import com.semantic.semanticOrganizer.semanticcalendar.adapters.TutorialPagerAdapter;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.NoteListFragment;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.TodayFragment;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.TodoListFragment;
import com.semantic.semanticOrganizer.semanticcalendar.fragments.YesterdayFragment;
import com.semantic.semanticOrganizer.semanticcalendar.models.NavDrawerItem;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.Random;

public class TutorialActivity extends FragmentActivity  {

    private static final Random RANDOM = new Random();

    TutorialPagerAdapter mAdapter;
    ViewPager mPager;
    CirclePageIndicator mIndicator;

     public static final String TAG ="TutorialActivity";


    protected void onCreate(Bundle savedInstanceState) {

        final Context context = this;

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tutorial);
//        final Button b =(Button) findViewById(R.id.button);
//
//        b.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                Intent intent = new Intent(TutorialActivity.this,HomeActivity.class);
//                TutorialActivity.this.startActivity(intent);
//            }
//        });
        super.onCreate(savedInstanceState);

        //The look of this sample is set via a style in the manifest
        setContentView(R.layout.activity_tutorial_new);
        mAdapter = new TutorialPagerAdapter(getSupportFragmentManager());

        //Set the pager with an adapter
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(mAdapter);

//Bind the title indicator to the adapter
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);


    }





    /**
     * Fragment that appears in the "content_frame", shows a planet
     */

    /*Navigation DRawer*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tutorial, menu);
        return super.onCreateOptionsMenu(menu);
    }




    /*Navigation DRawer*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            //handleYourEvent();
            Log.d(TAG, "Search");
            return true;
        }

        if (id == R.id.action_new_event) {
            //handleYourEvent();
            Intent intent = new Intent(this, WriteEvent.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_new_note) {
            //handleYourEvent();
            Intent intent = new Intent(this, Write_Note_Activity.class);
            startActivity(intent);
            return true;
        }


        return false;
    }

}
