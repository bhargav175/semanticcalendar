package com.semantic.semanticOrganizer.docket.activities;

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
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.database.CheckListDBHelper;
import com.semantic.semanticOrganizer.docket.database.HabitDBHelper;
import com.semantic.semanticOrganizer.docket.database.NoteDBHelper;
import com.semantic.semanticOrganizer.docket.database.TagDBHelper;
import com.semantic.semanticOrganizer.docket.helpers.ComingUpDialog;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.CheckList;
import com.semantic.semanticOrganizer.docket.models.Habit;
import com.semantic.semanticOrganizer.docket.models.Note;
import com.semantic.semanticOrganizer.docket.models.Tag;
import com.semantic.semanticOrganizer.docket.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.List;

public class GoalsActivity extends FragmentActivity {
    private TextView archivesLink, timelineLink;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ArrayAdapter<Tag> tagDrawerLayoutArrayAdapter;
    private List<Tag> tags;
    private Typeface font;
    private Integer doSave;
    private FloatingActionsMenu fam;
    private ComingUpDialog wAlertBuilder;
    private AlertDialog wAlert;
    private ListView tagsListView;
    private ArrayAdapter<Tag> tagArrayAdapter;
    private LinearLayout mCustomHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        preInit();
        new GetTags(this).execute("");
    }
    private void preInit(){
        tagsListView = (ListView) findViewById(R.id.listView);
        font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        fam = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);

        archivesLink = (TextView) findViewById(R.id.archiveLink);
        timelineLink = (TextView) findViewById(R.id.timeline);
        archivesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalsActivity.this,ArchivesActivity.class);
                startActivity(intent);
            }
        });
        timelineLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalsActivity.this,SuperMain.class);
                startActivity(intent);
            }
        });

        wAlertBuilder = new ComingUpDialog(this);
        wAlert = wAlertBuilder.create();
        mCustomHeaders=new LinearLayout(getApplicationContext());
        mCustomHeaders.setOrientation(LinearLayout.VERTICAL);

    }


    private void afterGetTags(){
        tagArrayAdapter = new ArrayAdapter<Tag>(this,R.layout.tag_goal_item,R.id.text1,tags){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView mainText = (TextView) view.findViewById(R.id.text1);
                TextView description = (TextView) view.findViewById(R.id.text2);
                TextView dueDate = (TextView) view.findViewById(R.id.text3);
                mainText.setText(tags.get(position).getTagText());
                description.setText(tags.get(position).getTagDescription());
                if(tags.get(position).getTagDescription()==null){
                    description.setVisibility(View.GONE);
                }
                dueDate.setText(tags.get(position).getTagId() + " - Tag Id");
                return view;
            }
        };

        tagsListView.setAdapter(tagArrayAdapter);
        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),UpdateTagActivity.class);
                intent.putExtra(DBHelper.COLUMN_ID,tags.get(position).getTagId());
                startActivity(intent);
            }
        });
        tagDrawerLayoutArrayAdapter = new ArrayAdapter<Tag>(this,R.layout.drawer_list_item_tag,R.id.title,tags){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView cardText1 = (TextView) view.findViewById(R.id.title);
                cardText1.setText(utilFunctions.toCamelCase(tags.get(position).getTagText()));
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
                    Intent intent = new Intent(GoalsActivity.this,TagActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,tags.get(position).getTagId());
                    startActivity(intent);
                }else{
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(GoalsActivity.this,TagActivity.class);
                    startActivity(intent);
                }
            }
        });
        addFloatingActionButtons();

    }

    private void saveStuff(String str, Tag t){
        if(doSave==1){
            Note note = new Note();
            NoteDBHelper noteDbHelper = new NoteDBHelper(getApplicationContext());
            note.setNoteTitle(str);
            noteDbHelper.open();
            noteDbHelper.saveNoteWithTag(note, t);
            noteDbHelper.close();
        }else if(doSave ==2){
            CheckList checkList = new CheckList();
            CheckListDBHelper checkListDBHelper = new CheckListDBHelper(getApplicationContext());
            checkList.setCheckListTitle(str);
            checkListDBHelper.open();
            checkListDBHelper.saveCheckListWithTag(checkList, t);
            checkListDBHelper.close();
        }else if(doSave==3){
            Habit habit = new Habit();
            HabitDBHelper habitDBHelper = new HabitDBHelper(getApplicationContext());
            habit.setHabitText(str);
            habitDBHelper.open();
            habitDBHelper.saveHabitWithTag(habit,t);
            habitDBHelper.close();
        }else if(doSave==4){

            new SaveTag(getApplicationContext(),str).execute("");

        }
    }


    private void addFloatingActionButtons(){


        final LayoutInflater layoutInflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(GoalsActivity.this);
        // Setting Dialog Title
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        FloatingActionButton addList = (FloatingActionButton) findViewById(R.id.addListFabButton);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 4;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final Spinner spinner = (Spinner) editInputLayout.findViewById(R.id.spinner);
                spinner.setVisibility(View.GONE);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
                editInput.setCursorVisible(true);
                alertDialog.setTitle("Add New Goal").setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString(),null);
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.goals, menu);
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
            tags = Tag.getAllUnArchivedTags(context);
            //No Sandbox from now
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
    private class SaveTag extends AsyncTask<String, Void,Tag> {

        private Context context;
        private String str;

        public SaveTag(Context context, String str){
            this.context=context;
            this.str = str;
        }

        @Override
        protected Tag doInBackground(String... params) {

            Tag tag = new Tag();
            TagDBHelper tagDBHelper = new TagDBHelper(getApplicationContext());
            tag.setTagText(str);
            tagDBHelper.open();
            Tag tempTag = tagDBHelper.saveTag(tag);
            tagDBHelper.close();
            tags.add(tempTag);
            return tempTag;
        }

        @Override
        protected void onPostExecute(final Tag t) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tagDrawerLayoutArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),""+t.getTagText()+" goal created",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),UpdateTagActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,t.getTagId());
                    startActivity(intent);

                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
