package com.bhargav.smart.smartTasks.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.adapters.ArchivesPagerAdapter;
import com.bhargav.smart.smartTasks.adapters.SlidingTabLayout;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.Tag;

import java.util.List;

public class ArchivesActivity extends ActionBarActivity {

    ViewPager mPager;
    ArchivesPagerAdapter sAdapter;
    SlidingTabLayout slidingTabLayout;
    private DrawerLayout mDrawerLayout;
    private LinearLayout sidebarHeader;
    private ListView mDrawerListView;
    private ArrayAdapter<Tag> tagDrawerLayoutArrayAdapter;
    private List<Tag> tags;
    private Typeface font;
    private TextView timelineLink,homeLink;



    @Override
    protected void onCreate(Bundle savedInstanceState)     {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);
        preInit();
        new GetTags(this).execute("");


    }
    private void preInit(){
        font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.right_drawer);
        sidebarHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.sidebar_right_header, null);
        mDrawerListView.addHeaderView(sidebarHeader);slidingTabLayout=(SlidingTabLayout) findViewById(R.id.sliding_tabs);
        sAdapter = new ArchivesPagerAdapter(getSupportFragmentManager());
        //Set the pager with an adapter
        mPager  = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(sAdapter);
        slidingTabLayout.setViewPager(mPager);
        timelineLink = (TextView) findViewById(R.id.home);
        timelineLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArchivesActivity.this,SuperMain.class);
                startActivity(intent);
            }
        });
        homeLink = (TextView) findViewById(R.id.lists);
        homeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArchivesActivity.this,GoalsActivity.class);
                startActivity(intent);
            }
        });


        //ActionMode


    }
    public void addTagToDrawer(Tag t){
        tags.add(t);
        tagDrawerLayoutArrayAdapter.notifyDataSetChanged();
    }

    private void afterGetTags(){
        tagDrawerLayoutArrayAdapter = new ArrayAdapter<Tag>(this,R.layout.drawer_list_item_tag,R.id.title,tags){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.title);
                cardText1.setText(toCamelCase(tags.get(position).getTagText()));
                return view;
            }
        };
        mDrawerListView.setAdapter(tagDrawerLayoutArrayAdapter);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= mDrawerListView.getHeaderViewsCount();
                if(position>-1){
                    if(tags.get(position).getTagId()!=null){
                        Intent intent = new Intent(ArchivesActivity.this,TagActivity.class);
                        intent.putExtra(DBHelper.COLUMN_ID,(tags.get(position).getTagId()));
                        startActivity(intent);
                    }else{

                    }
                }
            }
        });
    }


    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.archives, menu);
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

    private class GetTags extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetTags(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            tags =Tag.getAllUnArchivedTags(context);
            //No Sandbox
//            tags.add(new Tag("Untagged"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    afterGetTags();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,SuperMain.class);
        startActivity(intent);
        return;
    }
}
