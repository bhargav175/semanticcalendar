package com.semantic.semanticOrganizer.docket.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.database.TagDBHelper;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.Tag;
import com.semantic.semanticOrganizer.docket.utils.utilFunctions;

public class UpdateTagActivity extends Activity {
    private TagDBHelper tagDBHelper;
    private EditText tagText;
    private EditText tagDescription;
    private CheckBox tagIsArchived;
    Integer tagId;
    private Tag currentTagInView;
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
                        if(currentTagInView!=null){
                            if (tagText.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                updateTag();

                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_SHORT).show();
                            finish();

                        }
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
            if(tagId!=null){
                new GetTag(this,tagId).execute("");
            }
            else{
                Toast.makeText(this, "Could not load tag", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Could not load tag", Toast.LENGTH_SHORT).show();
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

    private void init(){
        getActionBar().setTitle(utilFunctions.toCamelCase(currentTagInView.getTagText()));
        tagText.setText(currentTagInView.getTagText());
        tagDescription.setText(currentTagInView.getTagDescription());
        tagIsArchived.setChecked(currentTagInView.getIsArchived());

    }

    private void updateTag() {
        String tagTextString=tagText.getText().toString();
        String tagDescriptionString=tagDescription.getText().toString();
        Boolean tagArchived=tagIsArchived.isChecked();

        currentTagInView.setTagText(tagTextString);
        currentTagInView.setTagDescription(tagDescriptionString);
        currentTagInView.setIsArchived(tagArchived);

            if(tagTextString.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show();
            }
            else{
                new UpdateTag(getApplicationContext(),currentTagInView).execute("");
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

    private class GetTag extends AsyncTask<String, Void,Void> {

        private Context context;
        private Integer tagId;

        public GetTag(Context context,Integer tagId){
            this.context=context;
            this.tagId = tagId;
        }

        @Override
        protected Void doInBackground(String... params) {
            currentTagInView =Tag.getTagById(tagId,context);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    init();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class UpdateTag extends AsyncTask<String, Void,Tag> {

        private Context context;
        private Tag tag;
        public UpdateTag(Context context, Tag tag){
            this.context=context;this.tag=tag;
        }

        @Override
        protected Tag doInBackground(String... params) {
            TagDBHelper  tagDBHelper = new TagDBHelper(context);
            try{
                tagDBHelper.open();
                tagDBHelper.updateTag(tag);
                tagDBHelper.close();
                return tag;
            }catch (Exception e){
                return null;
            }


        }

        @Override
        protected void onPostExecute(final Tag t) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(t!=null){
                        Toast.makeText(context, t.getTagText() +"is Updated",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "There was an error",Toast.LENGTH_SHORT).show();

                    }
                    if(t.getIsArchived()){
                        Intent intent = new Intent(UpdateTagActivity.this,SuperMain.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(UpdateTagActivity.this,TagActivity.class);
                        if(currentTagInView.getTagId()!=null) {
                            intent.putExtra(DBHelper.COLUMN_ID,currentTagInView.getTagId());
                        }
                        startActivity(intent);
                    }


                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
