package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.TagDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class UpdateTagActivity extends Activity {
    private TagDBHelper tagDBHelper;
    private EditText tagText;
    private EditText tagDescription;
    private CheckBox tagIsArchived;
    Integer tagId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        if(tagId!=null){

                            updateTag(tagId);
                            Intent lIntent = new Intent(getApplicationContext(), LandingActivity.class);
                            startActivity(lIntent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_LONG).show();

                        }

                        finish();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Cancel"
                        finish();
                    }
                });
        // Show the custom action bar view and hide the normal Home icon and title.
        setContentView(R.layout.activity_add_tag);
        initUi();

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        // END_INCLUDE (inflate_set_custom_view)
        Intent intent = getIntent();

        Bundle extras= intent.getExtras();
        if(extras!=null){
            String tagTextString = extras.getString(DBHelper.TAG_TITLE);
            tagId = extras.getInt(DBHelper.COLUMN_ID);
            tagText.setText(tagTextString);
            tagDescription.setText(extras.getString(DBHelper.TAG_DESCRIPTION));
            tagIsArchived.setChecked(extras.getBoolean(DBHelper.TAG_IS_ARCHIVED));
        }else{
            Toast.makeText(this, "Could not load tag", Toast.LENGTH_LONG).show();
        }

    }
    private void initUi() {
        tagText = (EditText) findViewById(R.id.tagTitle);
        tagDescription = (EditText) findViewById(R.id.descriptionText);
        tagIsArchived = (CheckBox) findViewById(R.id.isArchived);
        tagDBHelper = new TagDBHelper(this);

    }
    private void setListeners() {


    }

    private void updateTag(Integer tagId) {
        String tagTextString=tagText.getText().toString();
        String tagDescriptionString=tagDescription.getText().toString();
        Boolean tagArchived=tagIsArchived.isChecked();
        tagDBHelper = new TagDBHelper(this);
        tagDBHelper.open();
        Tag tag = tagDBHelper.getTag(tagId);

        if(tag!=null){
            if(tagTextString.length()==0){
                tagDBHelper.close();
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_LONG).show();
            }
            else{
                tagDBHelper.updateTag(tag, tagTextString,tagDescriptionString,tagArchived);
                tagDBHelper.close();
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }

        }
        else{
            tagDBHelper.close();
            Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_actvitiy_refactor, menu);
        return true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
}
