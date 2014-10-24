package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.CheckListDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.HabitDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.NoteDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.TagDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.CheckList;
import com.semantic.semanticOrganizer.semanticcalendar.models.Habit;
import com.semantic.semanticOrganizer.semanticcalendar.models.Note;
import com.semantic.semanticOrganizer.semanticcalendar.models.OrganizerItem;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import org.apmem.tools.layouts.FlowLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends Activity {

    private RelativeLayout noTagLayout,tagBannerContainer ,tagOrganizerMapView;
    private ScrollView flowLayout;
    private LinearLayout tagContainerLayout,tagDetail,addingTag;
    private FlowLayout tagsAsTags;
    private List<Tag> tags;
    private  TextView tagDescription ;
    private  TextView tagSecondaryText;
    private ImageView editTagImageView, tagsLableView, closeFlowLayoutView,addTagDonebutton, addTagCancelButton;
    private ListView cardsListView;
    private Button addTagButton;
    private EditText addTagEditText;
    private Tag currentTagInView;
    private Map<Tag, List<OrganizerItem>> tagOrganizerMap;
    private Map<Tag, TextView> tagTextViewMap;
    private ArrayAdapter<OrganizerItem> listAdapter;
    private Typeface font;
    private Integer doSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
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
        tagOrganizerMapView = (RelativeLayout) findViewById(R.id.tagOrganizerMap);
        addTagBanner();
        addFloatingActionButtons();

    }
    private void setListeners(){

    }

    private void saveStuff(String str){
        if(doSave==1){
            Note note = new Note();
            NoteDBHelper noteDbHelper = new NoteDBHelper(getApplicationContext());
            note.setNoteTitle(str);
            noteDbHelper.open();
            noteDbHelper.saveNoteWithTag(note, currentTagInView);
            noteDbHelper.close();
        }else if(doSave ==2){
            CheckList checkList = new CheckList();
            CheckListDBHelper checkListDBHelper = new CheckListDBHelper(getApplicationContext());
            checkList.setCheckListText(str);
            checkListDBHelper.open();
            checkListDBHelper.saveCheckListWithTag(checkList, currentTagInView);
            checkListDBHelper.close();
        }else if(doSave==3){
            Habit habit = new Habit();
            HabitDBHelper habitDBHelper = new HabitDBHelper(getApplicationContext());
            habit.setHabitText(str);
            habitDBHelper.open();
            habitDBHelper.saveHabitWithTag(habit,currentTagInView);
            habitDBHelper.close();
        }
    }

    private void addFloatingActionButtons(){

        FloatingActionButton addNote = (FloatingActionButton) findViewById(R.id.addNoteFab);
        FloatingActionButton addCheckList = (FloatingActionButton) findViewById(R.id.addCheckListab);
        FloatingActionButton addHabit = (FloatingActionButton) findViewById(R.id.addHabitFab);
        final LayoutInflater layoutInflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        // Setting Dialog Title
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // closed
        // Showing Alert Message
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 1;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.editText);
                alertDialog.setTitle("Add Note To "+toCamelCase(currentTagInView.getTagText())).setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString());
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();            }
        });
        addCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 2;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.editText);
                alertDialog.setTitle("Add CheckList To "+toCamelCase(currentTagInView.getTagText())).setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString());
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();
            }
        });
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave =3;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.editText);
                alertDialog.setTitle("Add Habit To "+toCamelCase(currentTagInView.getTagText())).setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString());
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();
            }
        });
    }

    private void toggleTagDetail(){
        if(flowLayout.getVisibility()==View.GONE){
            flowLayout.setVisibility(View.VISIBLE);
            tagDetail.setVisibility(View.GONE);
            cardsListView.setVisibility(View.GONE);
        }else{
            flowLayout.setVisibility(View.GONE);
            tagDetail.setVisibility(View.VISIBLE);
            cardsListView.setVisibility(View.VISIBLE);
        }

    }
    private void addTagBanner(){
        final LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        cardsListView = (ListView) findViewById(R.id.cardsListView);
        cardsListView.removeAllViewsInLayout();
        final View tagBanner =layoutInflater.inflate(R.layout.tag_banner, null);
       font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        LinearLayout mCustomHeaders=new LinearLayout(getApplicationContext());
        mCustomHeaders.setOrientation(LinearLayout.VERTICAL);
        //tagBannerContainer.addView(tagBanner);
        //if cardsListView has a header then remove it
        //flush cardslistview
        mCustomHeaders.removeAllViews();
        mCustomHeaders.addView(tagBanner);
        cardsListView.addHeaderView(mCustomHeaders,null,false);
        tagDetail = (LinearLayout) tagBanner.findViewById(R.id.tagDetail);
        flowLayout = (ScrollView) findViewById(R.id.flowLayout);
        tagsLableView = (ImageView) tagBanner.findViewById(R.id.tagLabelsImage);
        closeFlowLayoutView = (ImageView) findViewById(R.id.clearFlowLayout);
        addTagButton = (Button) findViewById(R.id.addTagButton);
        addTagDonebutton = (ImageView) findViewById(R.id.doneButton);
        addTagCancelButton = (ImageView) findViewById(R.id.delete);
        addingTag = (LinearLayout) findViewById(R.id.addingTag);
        addTagEditText = (EditText) findViewById(R.id.addTagEditText);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingTag.setVisibility(View.VISIBLE);
                addTagButton.setVisibility(View.GONE);
            }
        });

        addTagCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTagEditText.setText("");
                addingTag.setVisibility(View.GONE);
                addTagButton.setVisibility(View.VISIBLE);
            }
        });

        addTagDonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTagEditText.getText().toString().length()>0){
                    TagDBHelper tagDBHelper = new TagDBHelper(getApplicationContext());
                    Tag tag = new Tag();
                    tag.setTagText(addTagEditText.getText().toString());
                    tagDBHelper.open();
                    Tag tagItem =tagDBHelper.saveTag(tag);
                    tags.add(tags.size()-2,tagItem);
                    tagOrganizerMap.put(tagItem,new ArrayList<OrganizerItem>());
                    addTagToTagsAsTags(tagItem);

                    tagDBHelper.close();
                    addTagEditText.setText("");
                    addingTag.setVisibility(View.GONE);
                    addTagButton.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(getApplicationContext(),"Tag Title Cannot Be Empty",Toast.LENGTH_SHORT).show();

                }

            }
        });

        flowLayout.setVisibility(View.VISIBLE);
        View.OnClickListener toggleTagDetailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTagDetail();
            }
        };
        tagsLableView.setOnClickListener(toggleTagDetailClickListener);
        closeFlowLayoutView.setOnClickListener(toggleTagDetailClickListener);
        int lastAddedTAg = 0;
        Tag lastAddedTagObject = tags.get(getLastAddedTagIndex());
        tagDescription = (TextView) tagBanner.findViewById(R.id.description);
        tagSecondaryText = (TextView) tagBanner.findViewById(R.id.secondary_text);
        editTagImageView = (ImageView) tagBanner.findViewById(R.id.edit);
        final List<OrganizerItem> organizerItems = new ArrayList<OrganizerItem>();
        listAdapter = new ArrayAdapter<OrganizerItem>(getApplicationContext(),
                R.layout.card_organizer_item,R.id.info_text, organizerItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.info_text);
                TextView cardText2 = (TextView) view.findViewById(R.id.secondary_text);
                cardText1.setText(toCamelCase(organizerItems.get(position).getItemText()));
                cardText2.setText(toCamelCase(organizerItems.get(position).getCreatedTime()));
                cardText1.setTextSize(getResources().getDimension(R.dimen.material_micro_text_size));
                cardText2.setTextSize(getResources().getDimension(R.dimen.material_micro_text_size));
                cardText1.setTypeface(font);
                cardText2.setTypeface(font);
                cardText2.setVisibility(View.GONE);
               return view;
            }
        };


        cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                OrganizerItem organizerItem = (OrganizerItem) parent.getAdapter().getItem(position);

                if(organizerItem.getType().equals("NOTE")){
                    new GetNote(organizerItem.getId()).execute("");

                }else if(organizerItem.getType().equals("HABIT")){
                    Intent intent = new Intent(getApplicationContext(), UpdateHabitActivity.class);
                    intent.putExtra(DBHelper.HABIT_TEXT,  organizerItem.getItemText());
                    intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                    startActivity(intent);
                }else if(organizerItem.getType().equals("CHECKLIST")){
                    Intent intent = new Intent(getApplicationContext(), UpdateCheckListActivity.class);
                    intent.putExtra(DBHelper.CHECKLIST_TITLE,  organizerItem.getItemText());
                    intent.putExtra(DBHelper.COLUMN_ID, organizerItem.getId());
                    startActivity(intent);
                }else{

                }

            }
        });

        cardsListView.setAdapter(listAdapter);
        tagsAsTags = (FlowLayout) findViewById(R.id.tagsAsTags);
        tagsAsTags.setOrientation(FlowLayout.HORIZONTAL);

        //Flush tagsAsTags

        tagsAsTags.removeAllViews();
        tagTextViewMap = new LinkedHashMap<Tag, TextView>();
        for(final Tag tagItem:tags){
            addTagToTagsAsTags( tagItem);
        }
        Tag lastAddedTagObj = tags.get(getLastAddedTagIndex());
        TextView t = tagTextViewMap.get(lastAddedTagObj);
        t.performClick();
    }

    private void addTagToTagsAsTags(final Tag tagItem) {
        final TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        if(tags.indexOf(tagItem)==tags.size()-1){
            tv.setTextColor(getResources().getColor(R.color.light_black));
            tv.setBackgroundColor(getResources().getColor(R.color.light_gray_color));
        }else{
            tv.setTextColor(getResources().getColor(R.color.light_black));
            tv.setBackgroundColor(getResources().getColor(R.color.white));
        }
        tv.setLayoutParams(params);
        int dp = (int) getResources().getDimension(R.dimen.material_left_padding);
        tv.setPadding(dp,dp,dp,dp);
        tv.setText(toCamelCase(tagItem.getTagText()));
        tv.setTextSize(getResources().getDimension(R.dimen.material_micro_text_size));
        tv.setTypeface(font);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Tag currentTag = tagItem;
                currentTagInView = tagItem;
                for(int i =0; i<tagTextViewMap.size(); i++){
                    TextView tk = tagTextViewMap.get(tags.get(i));
                    if(i==tagTextViewMap.size()-1){
                        tk.setTextColor(getResources().getColor(R.color.light_black));
                        tk.setBackgroundColor(getResources().getColor(R.color.light_gray_color));
                    }else{
                        tk.setTextColor(getResources().getColor(R.color.light_black));
                        tk.setBackgroundColor(getResources().getColor(R.color.white));
                    }

                }
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setBackgroundColor(getResources().getColor(R.color.blue_color));
                tagDescription.setText(toCamelCase(currentTag.getTagText()));
                tagSecondaryText.setText(toCamelCase(currentTag.getTagDescription()));
                tagDescription.setTypeface(font);
                tagSecondaryText.setTypeface(font);
                tagDescription.setTextSize(getResources().getDimension(R.dimen.material_medium_text_size));
                tagSecondaryText.setTextSize(getResources().getDimension(R.dimen.material_micro_text_size));
                final List<OrganizerItem> organizerItems;
                if (tags.indexOf(tagItem) == tags.size() - 1) {
                    //Sandbox
                    editTagImageView.setVisibility(View.GONE);
                    organizerItems = tagOrganizerMap.get(currentTag);
                } else {
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
                listAdapter.clear();
                listAdapter.addAll(organizerItems);
                listAdapter.notifyDataSetChanged();
                toggleTagDetail();
            }
        });
        tagsAsTags.addView(tv,tagsAsTags.getChildCount()-1);
        tagTextViewMap.put(tagItem,tv);
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
                    tags.add(new Tag("Untagged"));
                    tagOrganizerMap = new LinkedHashMap<Tag, List<OrganizerItem>>();
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
                        intent.putExtra(DBHelper.NOTE_DESCRIPTION, note.getNoteTitle());
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



