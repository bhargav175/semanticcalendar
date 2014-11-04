package com.semantic.semanticOrganizer.docket.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.database.TagDBHelper;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Tag;

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
                R.layout.tag_list_item,R.id.text1, getAllTags(tagList)) {
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
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Tag tag = (Tag) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(getApplicationContext(), UpdateTagActivity.class);
                    intent.putExtra(DBHelper.TAG_TITLE, tag.getTagText());
                    intent.putExtra(DBHelper.COLUMN_ID, tag.getTagId());
                    intent.putExtra(DBHelper.TAG_DESCRIPTION, tag.getTagDescription());
                    intent.putExtra(DBHelper.TAG_IS_ARCHIVED, tag.getIsArchived());
                    startActivity(intent);
                }
            });
            list.setAdapter(adapter);



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
        return super.onOptionsItemSelected(item);
    }

    private List<Tag> getAllTags(List<Tag> tagList) {
        tagDBHelper.open();
        Cursor cursor= tagDBHelper.fetchAllTags();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tag tag = tagDBHelper.cursorToTag(cursor);
            tagList.add(tag);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        tagDBHelper.close();
        return tagList;
    }

}
