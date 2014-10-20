package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.adapters.ExpandableListAdapter;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.OrganizerItem;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends Activity {

    private Spinner tagSelector;
    private RelativeLayout noTagLayout,tagBannerContainer ,tagOrganizerMapView;
    private LinearLayout tagContainerLayout;
    private List<Tag> tags;
    private  TextView tagDescription ;
    private  TextView tagSecondaryText;
    private ImageView editTagImageView;
    private ListView cardsListView;

    private Map<Tag, List<OrganizerItem>> tagOrganizerMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

    }


    private void init(){
        noTagLayout = (RelativeLayout) findViewById(R.id.noTagsLayout);
        tagContainerLayout = (LinearLayout) findViewById(R.id.tagContainerLayout);
            new GetTags().execute("");
    }
    private void initializeUi(){
        //tags is already initialized and its size is greater than 0. Hence there definitely is a tag present
        tagBannerContainer = (RelativeLayout) findViewById(R.id.tagBannerContainer);
        tagOrganizerMapView = (RelativeLayout) findViewById(R.id.tagOrganizerMap);
        addTagBanner();

    }
    private void setListeners(){

    }
    private void addTagBanner(){
        final LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View tagBanner =layoutInflater.inflate(R.layout.tag_banner, tagOrganizerMapView, false);
        tagBannerContainer.addView(tagBanner);
        tagSelector = (Spinner) tagBanner.findViewById(R.id.spinner);
        int lastAddedTAg = 0;
        Tag lastAddedTagObject = tags.get(getLastAddedTagIndex());
        tags.add(new Tag("Untagged"));
        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                android.R.layout.simple_spinner_item, tags);
        tagSelector.setAdapter(adapter);
        tagDescription = (TextView) tagBanner.findViewById(R.id.description);
        tagSecondaryText = (TextView) tagBanner.findViewById(R.id.secondary_text);
        editTagImageView = (ImageView) tagBanner.findViewById(R.id.edit);
        cardsListView = (ListView) findViewById(R.id.cardsListView);
        tagSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final Tag currentTag = tags.get(i);
                tagDescription.setText(currentTag.getTagText());
                tagSecondaryText.setText(currentTag.getTagText());
                final Typeface font = Typeface.createFromAsset(
                        getApplicationContext().getAssets(),
                        "fonts/RobotoCondensed-Light.ttf");
                tagDescription.setTypeface(font);
                tagSecondaryText.setTypeface(font);
                final List<OrganizerItem> organizerItems;
                if(i == tags.size()-1){
                    //Sandbox
                    editTagImageView.setVisibility(View.GONE);
                    organizerItems = tagOrganizerMap.get(new Tag("Untagged"));

                }
                else{
                    //Normal tag
                    editTagImageView.setVisibility(View.VISIBLE);
                    editTagImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), UpdateTagActivity.class);
                            intent.putExtra(DBHelper.TAG_TITLE, currentTag.getTagText());
                            intent.putExtra(DBHelper.COLUMN_ID, currentTag.getTagId());
                            intent.putExtra(DBHelper.TAG_DESCRIPTION, currentTag.getTagDescription());
                            intent.putExtra(DBHelper.TAG_IS_ARCHIVED, currentTag.getIsArchived());
                            startActivity(intent);
                        }
                    });

                    organizerItems = tagOrganizerMap.get(currentTag);

                }

//                final ArrayAdapter<OrganizerItem> adapter = new ArrayAdapter<OrganizerItem>(getApplicationContext(),
//                        R.layout.card_organizer_item,R.id.info_text, organizerItems){
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        View view = super.getView(position, convertView, parent);
//                        TextView cardText1 = (TextView) view.findViewById(R.id.info_text);
//                        TextView cardText2 = (TextView) view.findViewById(R.id.secondary_text);
//                        cardText1.setText(organizerItems.get(position).getItemText());
//                        cardText2.setText(organizerItems.get(position).getCreatedTime());
//                        cardText1.setTypeface(font);
//                        cardText2.setTypeface(font);
//
////                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
////                            view.startAnimation(animation);
//                        return view;
//                    }
//                };

                final ArrayAdapter<OrganizerItem> adapter = new ArrayAdapter<OrganizerItem>(getApplicationContext(),
                        R.layout.card_organizer_item,R.id.info_text, organizerItems){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView cardText1 = (TextView) view.findViewById(R.id.info_text);
                        TextView cardText2 = (TextView) view.findViewById(R.id.secondary_text);
                        cardText1.setText(organizerItems.get(position).getItemText());
                        cardText2.setText(organizerItems.get(position).getCreatedTime());
                        cardText1.setTypeface(font);
                        cardText2.setTypeface(font);

                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                            view.startAnimation(animation);
                        return view;
                    }
                };

                cardsListView.addHeaderView(tagBanner);
                cardsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                for(OrganizerItem organizerItem : organizerItems){
//                    final View organizerCard =layoutInflater.inflate(R.layout.card_organizer_item, cardsLayout, false);
//                    cardsLayout.addView(organizerCard);
//                    TextView infoText = (TextView)  organizerCard.findViewById(R.id.info_text);
//                    TextView secondaryText = (TextView)  organizerCard.findViewById(R.id.secondary_text);
//                    infoText.setText(organizerItem.getItemText());
//                    secondaryText.setText(organizerItem.getCreatedTime());
//                    infoText.setTypeface(font);
//                    secondaryText.setTypeface(font);
//
//                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tagSelector.setSelection(lastAddedTAg);

    }

    private int getLastAddedTagIndex(){
        int lastAddedTAg =0;
        for(Tag tag:tags){
            if(tags.indexOf(tag)>0 && tags.indexOf(tag)<tags.size()){
                if(tags.get(0).getCreatedMillis()> tag.getCreatedMillis()){
                    lastAddedTAg = tags.indexOf(tag);
                }
            }

        }
        return lastAddedTAg;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
        if (id == R.id.action_view_tags) {
            Intent intent = new Intent(this,ListTagsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_notes) {
            Intent intent = new Intent(this,ListNotesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_checklists) {
            Intent intent = new Intent(this,ListChecklistsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_habits) {
            Intent intent = new Intent(this,ListHabitsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    private class GetNotes extends AsyncTask<String, Void, List<OrganizerItem>> {

        @Override
        protected List<OrganizerItem> doInBackground(String... params) {
            List<OrganizerItem> organizerItems = OrganizerItem.getSandboxUnArchivedOrganizerItems(getApplicationContext());
            return organizerItems;
        }

        @Override
        protected void onPostExecute(final List<OrganizerItem> organizerItems) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                }
            });



        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class GetTags extends AsyncTask<String, Void, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(String... params) {
            List<Tag> tagList = new ArrayList<Tag>();
            tagList = Tag.getAllUnArchivedTags(getApplicationContext());
            return tagList;
        }

        @Override
        protected void onPostExecute(final List<Tag> tagList) {

        if(tagList.size()>0){



            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tags = new ArrayList<Tag>();
                    tags.addAll(tagList);
                    if(tagOrganizerMap ==null){
                        tagOrganizerMap = new LinkedHashMap<Tag, List<OrganizerItem>>();
                    }
                    new GetAllUnArchivedOrganizerItems(tags,getApplicationContext()).execute("");
                    noTagLayout.setVisibility(View.GONE);
                    tagContainerLayout.setVisibility(View.VISIBLE);
                }
            });


        }else{

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    noTagLayout.setVisibility(View.VISIBLE);
                    tagContainerLayout.setVisibility(View.GONE);
                }
            });


        }

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class GetNote extends AsyncTask<String, Void,Note> {

        private int id;
        public GetNote(int id){
            this.id = id;
        }

        @Override
        protected Note doInBackground(String... params) {
            Note note = new Note();
            note = Note.getNote(id,getApplicationContext());
            return note;
        }

        @Override
        protected void onPostExecute(final Note note) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(note!=null){
                        Intent intent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
                        intent.putExtra(DBHelper.NOTE_DESCRIPTION, note.getNoteText());
                        intent.putExtra(DBHelper.COLUMN_ID,note.getId());
                        intent.putExtra(DBHelper.NOTE_REQUEST_ID,note.getRemainderId());
                        intent.putExtra(DBHelper.NOTE_IS_ARCHIVED,note.getIsArchived());
                        intent.putExtra(DBHelper.NOTE_TAG,note.getTag());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    private class GetAllUnArchivedOrganizerItems extends AsyncTask<String, Void, Void> {


        private List<Tag> tags;
        private Context context;

        public GetAllUnArchivedOrganizerItems(List<Tag> tags,Context context){
            this.tags = tags; this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            for(Tag tag:tags){
                List<OrganizerItem> organizerItems;
                if(tag.getTagId() == null){
                    organizerItems =OrganizerItem.getSandboxUnArchivedOrganizerItems(context);
                }else{
                    organizerItems =OrganizerItem.getUnArchivedOrganizerItemsWithTag(tag,context);
                }
                tagOrganizerMap.put(tag,organizerItems);

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
            initializeUi();

        }




    }


    private class GetUnArchivedOrganizerItems extends AsyncTask<String, Void,List<OrganizerItem>> {

        private Tag tag;
        private Context context;

        public GetUnArchivedOrganizerItems(Tag tag,Context context){
            this.tag = tag; this.context=context;
        }

        @Override
        protected List<OrganizerItem> doInBackground(String... params) {
            List<OrganizerItem> organizerItems =OrganizerItem.getUnArchivedOrganizerItemsWithTag(tag,context);
            return organizerItems;
        }

        @Override
        protected void onPostExecute(final List<OrganizerItem> organizerItems) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tagOrganizerMap.put(tag,organizerItems);
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



    private class GetUnArchivedSandBoxItems extends AsyncTask<String, Void,List<OrganizerItem>> {

        private Context context;

        public GetUnArchivedSandBoxItems(Context context){
           this.context=context;
        }

        @Override
        protected List<OrganizerItem> doInBackground(String... params) {
            List<OrganizerItem> organizerItems =OrganizerItem.getSandboxUnArchivedOrganizerItems(context);
            return organizerItems;
        }

        @Override
        protected void onPostExecute(final List<OrganizerItem> organizerItems) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Tag sandBox = new Tag("Untagged");
                    tagOrganizerMap.put(sandBox,organizerItems);
                    initializeUi();

                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }







}



