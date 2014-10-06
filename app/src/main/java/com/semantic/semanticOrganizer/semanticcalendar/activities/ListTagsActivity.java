package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.TagDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.Event;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class ListTagsActivity extends Activity {
    private TagDBHelper tagDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tags);

        final List<Tag> tagList = new ArrayList<Tag>();

        tagDBHelper = new TagDBHelper(this);
        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                R.layout.tag_list_item,R.id.text1, getAllTasks(tagList)){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);

                text1.setText(tagList.get(position).getTagText());
                text2.setText(tagList.get(position).getTagDescription());
                return view;
            }

        };
        ListView list = (ListView) findViewById(R.id.tagsListView);
        list.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_tags, menu);
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

    private List<Tag> getAllTasks(List<Tag> taskList) {
        tagDBHelper.open();
        Cursor cursor= tagDBHelper.fetchAllTags();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tag tag = tagDBHelper.cursorToTag(cursor);
            taskList.add(tag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        tagDBHelper.close();
        return taskList;
    }

}
