package com.bhargav.smart.smartTasks.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagListView extends Activity {

    private ListView tagsListView;
    private List<Tag> tags;
    private Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tag_list_view);
        new GetTags().execute("");
    }
    private void initUi(){
        font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        tagsListView = (ListView) findViewById(R.id.tagsListView);
        ArrayAdapter<Tag> arrayAdapter = new ArrayAdapter<Tag>(this,R.layout.card_tag_material,R.id.info_text,tags){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.info_text);
                cardText1.setText(toCamelCase(tags.get(position).getTagText()));
                cardText1.setTextSize(getResources().getDimension(R.dimen.material_micro_text_size));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TagListView.this, TagActivity.class);
                        intent.putExtra(DBHelper.COLUMN_ID, tags.get(position).getTagId());
                        startActivity(intent);
                    }
                });
                cardText1.setTypeface(font);
                return view;
            }
        };
        tagsListView.setAdapter(arrayAdapter);
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
        getMenuInflater().inflate(R.menu.tag_list_view, menu);
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
    private class GetTags extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
             tags = new ArrayList<Tag>();
            tags = Tag.getAllUnArchivedTags(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            initUi();


        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}


