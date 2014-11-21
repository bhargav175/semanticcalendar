package com.bhargav.smart.smartTasks.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.database.CheckListDBHelper;
import com.bhargav.smart.smartTasks.database.HabitDBHelper;
import com.bhargav.smart.smartTasks.database.NoteDBHelper;
import com.bhargav.smart.smartTasks.helpers.ComingUpDialog;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.CheckList;
import com.bhargav.smart.smartTasks.models.Habit;
import com.bhargav.smart.smartTasks.models.Note;
import com.bhargav.smart.smartTasks.models.OrganizerItem;
import com.bhargav.smart.smartTasks.models.Tag;

import java.text.SimpleDateFormat;
import java.util.List;

public class TagActivity extends ActionBarActivity {


    private RelativeLayout tagOrganizerMapView, loadingLayout;
    private LinearLayout tagDetail,addingTag;
    private TextView tagDescription ;
    private  TextView tagSecondaryText;
    private ImageView tagsLableView, closeFlowLayoutView,addTagDonebutton, addTagCancelButton;
    private ListView cardsListView;
    private Button addTagButton;
    private EditText addTagEditText;
    private Tag currentTagInView;
    private ArrayAdapter<OrganizerItem> listAdapter;
    private Typeface font;
    private Integer doSave, tagId;
    private FloatingActionsMenu fam;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private LinearLayout sidebarHeader;
    private List<OrganizerItem> organizerItems;
    private List<Tag> tags;
    private LinearLayout mCustomHeaders;
    private ArrayAdapter<Tag> tagDrawerLayoutArrayAdapter;
    private ComingUpDialog wAlertBuilder;
    private AlertDialog wAlert;
    private TextView archivesLink,homeLink,listsLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tag);
        preInit();
        getExtras();

    }
    private void preInit(){
        font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        fam = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.right_drawer);
        sidebarHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.sidebar_right_header, null);
        mDrawerListView.addHeaderView(sidebarHeader);
        mCustomHeaders=new LinearLayout(getApplicationContext());
        mCustomHeaders.setOrientation(LinearLayout.VERTICAL);
        tagOrganizerMapView = (RelativeLayout) findViewById(R.id.tagOrganizerMap);
        loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        cardsListView = (ListView) findViewById(R.id.cardsListView);

        wAlertBuilder = new ComingUpDialog(this);
        wAlert = wAlertBuilder.create();
        archivesLink = (TextView) findViewById(R.id.archives);
        archivesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagActivity.this,ArchivesActivity.class);
                startActivity(intent);
            }
        });
        homeLink = (TextView) findViewById(R.id.home);
        homeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagActivity.this,GoalsActivity.class);
                startActivity(intent);
            }
        });
        listsLink = (TextView) findViewById(R.id.lists);
        listsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagActivity.this,SuperMain.class);
                startActivity(intent);
            }
        });


    }
    private void getExtras(){
        Intent intent = getIntent();

        Bundle extras= intent.getExtras();
        if(extras!=null){
            Integer t = extras.getInt(DBHelper.COLUMN_ID);
            getTag(t);
        }else{
            getTag(null);
        }
    }
    private void getTag(Integer t){
        if(t!=null){
            cardsListView.removeAllViewsInLayout();
            if(cardsListView.getHeaderViewsCount()==0){
                cardsListView.addHeaderView(mCustomHeaders, null, false);
            }
            mCustomHeaders.removeAllViews();
            tagId = t;
            new GetTag(this,tagId).execute("");
        }
        else{
            cardsListView.removeAllViewsInLayout();
            if(cardsListView.getHeaderViewsCount()==0){
                cardsListView.addHeaderView(mCustomHeaders, null, false);
            }
            mCustomHeaders.removeAllViews();

            new GetTag(this,null).execute("");
        }

    }




    private void init(){

        //Get Tags and add them to navigation drawer
        getActionBar().setTitle(toCamelCase(currentTagInView.getTagText()));
        new GetTags(this).execute("");

        if(currentTagInView.getTagId()!=null){
            new GetAllUnArchivedOrganizerItems(currentTagInView,this).execute("");
        }else{
           new GetAllUnArchivedOrganizerItems(currentTagInView,this).execute("");
        }



    }

    private void initializeUi(){
        //tags is already initialized and its size is greater than 0. Hence there definitely is a tag present
        tagOrganizerMapView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        fam.setVisibility(View.VISIBLE);
        addTagBanner();
        addFloatingActionButtons();


    }
    private void initDrawerLayout(){
        //tags is already initialized and its size is greater than 0. Hence there definitely is a tag present
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
                        mDrawerLayout.closeDrawers();
                        getTag(tags.get(position).getTagId());

                    }else{

                    }
                }


            }
        });

    }
    private void setListeners(){

    }

    private void saveStuff(String str){
        OrganizerItem organizerItem = null;
        if(doSave==1){
            Note note = new Note();
            NoteDBHelper noteDbHelper = new NoteDBHelper(getApplicationContext());
            note.setNoteTitle(str);
            noteDbHelper.open();
            Note n =noteDbHelper.saveNoteWithTag(note, currentTagInView);
            organizerItem = OrganizerItem.castNoteToOrganizerItem(n);
            noteDbHelper.close();
        }else if(doSave ==2){
            CheckList checkList = new CheckList();
            CheckListDBHelper checkListDBHelper = new CheckListDBHelper(getApplicationContext());
            checkList.setCheckListTitle(str);
            checkListDBHelper.open();
            CheckList c = checkListDBHelper.saveCheckListWithTag(checkList, currentTagInView);
            organizerItem = OrganizerItem.castCheckListToOrganizerItem(c);

            checkListDBHelper.close();
        }else if(doSave==3){
            Habit habit = new Habit();
            HabitDBHelper habitDBHelper = new HabitDBHelper(getApplicationContext());
            habit.setHabitText(str);
            habitDBHelper.open();
            Habit h = habitDBHelper.saveHabitWithTag(habit,currentTagInView);
            organizerItem = OrganizerItem.castHabitToOrganizerItem(h);

            habitDBHelper.close();
        }
        if(organizerItem!=null){
            listAdapter.add(organizerItem);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void addFloatingActionButtons(){

        FloatingActionButton addNote = (FloatingActionButton) findViewById(R.id.addNoteFab);
        FloatingActionButton addCheckList = (FloatingActionButton) findViewById(R.id.addCheckListab);
        FloatingActionButton addHabit = (FloatingActionButton) findViewById(R.id.addHabitFab);
        final LayoutInflater layoutInflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TagActivity.this);
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
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
                alertDialog.setTitle("Add Note To "+toCamelCase(currentTagInView.getTagText())).setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString());
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();            }
        });
        addCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 2;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
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

                //Replace this code
                doSave =3;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
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


                //end here

                // wAlert.show();
            }
        });
    }


    private void addTagBanner(){
        final LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View tagBanner =layoutInflater.inflate(R.layout.tag_banner, null);
        mCustomHeaders.addView(tagBanner);
        tagDetail = (LinearLayout) tagBanner.findViewById(R.id.tagDetail);
        tagDescription = (TextView) tagBanner.findViewById(R.id.description);
        tagSecondaryText = (TextView) tagBanner.findViewById(R.id.secondary_text);
       if(currentTagInView.getTagId()!=null){

            tagDescription.setText(currentTagInView.getTagDescription());
            tagSecondaryText.setText(currentTagInView.getTagId().toString());

        }
        else{
            tagDescription.setText("All Items which are not in any particular list");
            tagSecondaryText.setText(currentTagInView.getTagId().toString());

        }
        tagSecondaryText.setVisibility(View.GONE);

        listAdapter = new ArrayAdapter<OrganizerItem>(getApplicationContext(),
                R.layout.card_organizer_item,R.id.info_text, organizerItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.info_text);
                TextView cardText2 = (TextView) view.findViewById(R.id.secondary_text);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                if(organizerItems.get(position).getType().equals("NOTE")){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_note_add_black_24dp));
                }else if(organizerItems.get(position).getType().equals("CHECKLIST")){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                }else{
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_black_24dp));
                }
                cardText1.setText(toCamelCase(organizerItems.get(position).getItemText()));
                if(organizerItems.get(position).getDueTime()!=null){
                    cardText2.setText("Due - "+ new SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa").format(organizerItems.get(position).getDueTime().getTime()));
                    cardText2.setVisibility(View.VISIBLE);
                }else{
                    cardText2.setVisibility(View.GONE);
                }
//                cardText1.setTextSize(getResources().getDimension(R.dimen.material_micro_text_size));
//                cardText1.setTypeface(font);
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
                    intent.putExtra(DBHelper.HABIT_TITLE,  organizerItem.getItemText());
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
        getMenuInflater().inflate(R.menu.tag_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.edit) {
            Intent intent = new Intent(getApplicationContext(), UpdateTagActivity.class);
            intent.putExtra(DBHelper.COLUMN_ID, currentTagInView.getTagId());
            startActivity(intent);
            return true;
        }
        if (id == R.id.archive) {
            Tag.archiveTag(currentTagInView, getApplicationContext());
            Toast.makeText(getApplicationContext(),currentTagInView.getTagText()+" is Archived",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,GoalsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.archiveAll) {
            Tag.archiveAllTagItems(currentTagInView,getApplicationContext());
            Toast.makeText(getApplicationContext(),"All Items in "+ currentTagInView.getTagText() +"are Archived",Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listAdapter.clear();
                    listAdapter.notifyDataSetChanged();
                }
            });
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


        private Tag tag;
        private Context context;

        public GetAllUnArchivedOrganizerItems(Tag tag,Context context){
            this.tag = tag; this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {

                if(tag.getTagId() == null){
                    organizerItems =OrganizerItem.getSandboxUnArchivedOrganizerItems(context);
                }else{
                    organizerItems =OrganizerItem.getUnArchivedOrganizerItemsWithTag(tag,context);
                }



            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    initializeUi();
                }
            });
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
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
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
             if(tagId==null){
                 currentTagInView = new Tag("SandBox");
             }else{
                 currentTagInView =Tag.getTagById(tagId,context);
             }

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


    private class GetTags extends AsyncTask<String, Void,Void> {

        private Context context;

        public GetTags(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            tags =Tag.getAllUnArchivedTags(context);
            //No Sandbox
            //tags.add(new Tag("SandBox"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    initDrawerLayout();
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
