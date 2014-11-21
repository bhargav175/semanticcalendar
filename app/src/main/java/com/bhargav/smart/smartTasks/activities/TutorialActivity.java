package com.bhargav.smart.smartTasks.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.semantic.semanticOrganizer.docket.R;
import com.bhargav.smart.smartTasks.adapters.TutorialPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

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

        if (id == R.id.action_new_event) {
            //handleYourEvent();

            return true;
        }
        if (id == R.id.action_new_note) {
            //handleYourEvent();

            return true;
        }


        return false;
    }

}
