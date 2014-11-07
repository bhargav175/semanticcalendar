package com.semantic.semanticOrganizer.docket.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.adapters.ArchivesPagerAdapter;
import com.semantic.semanticOrganizer.docket.adapters.SlidingTabLayout;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Tag;

import java.util.List;

public class ArchivesActivity extends FragmentActivity {

    ViewPager mPager;
    ArchivesPagerAdapter sAdapter;
    SlidingTabLayout slidingTabLayout;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ArrayAdapter<Tag> tagDrawerLayoutArrayAdapter;
    private List<Tag> tags;
    private Typeface font;
    private TextView archivesLink,homeLink;



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
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        slidingTabLayout=(SlidingTabLayout) findViewById(R.id.sliding_tabs);
        sAdapter = new ArchivesPagerAdapter(getSupportFragmentManager());
        //Set the pager with an adapter
        mPager  = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(sAdapter);
        slidingTabLayout.setViewPager(mPager);
        archivesLink = (TextView) findViewById(R.id.archiveLink);
        archivesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        homeLink = (TextView) findViewById(R.id.home);
        homeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArchivesActivity.this,SuperMain.class);
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
                cardText1.setTextSize(getResources().getDimension(R.dimen.material_micro_text_size));
                cardText1.setTypeface(font);
                return view;
            }
        };
        mDrawerListView.setAdapter(tagDrawerLayoutArrayAdapter);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tags.get(position).getTagId()!=null){
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(ArchivesActivity.this,TagActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,tags.get(position).getTagId());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ArchivesActivity.this,TagActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,tags.get(position).getTagId());
                    startActivity(intent);
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
}
