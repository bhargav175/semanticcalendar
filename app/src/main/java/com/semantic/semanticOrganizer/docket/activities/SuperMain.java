package com.semantic.semanticOrganizer.docket.activities;

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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.adapters.SlidingTabLayout;
import com.semantic.semanticOrganizer.docket.adapters.SuperMainPagerAdapter;
import com.semantic.semanticOrganizer.docket.database.CheckListDBHelper;
import com.semantic.semanticOrganizer.docket.database.HabitDBHelper;
import com.semantic.semanticOrganizer.docket.database.NoteDBHelper;
import com.semantic.semanticOrganizer.docket.database.TagDBHelper;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.models.CheckList;
import com.semantic.semanticOrganizer.docket.models.Habit;
import com.semantic.semanticOrganizer.docket.models.Note;
import com.semantic.semanticOrganizer.docket.models.Tag;

import java.util.List;

public class SuperMain extends FragmentActivity {

    ViewPager mPager;
    SuperMainPagerAdapter sAdapter;
    SlidingTabLayout slidingTabLayout;
    private TextView archivesLink;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ArrayAdapter<Tag> tagDrawerLayoutArrayAdapter;
    private List<Tag> tags;
    private Typeface font;
    private Integer doSave;
    private FloatingActionsMenu fam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_main);
        preInit();
        new GetTags(this).execute("");


    }
    private void preInit(){
        font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        fam = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        slidingTabLayout=(SlidingTabLayout) findViewById(R.id.sliding_tabs);
        sAdapter = new SuperMainPagerAdapter(getSupportFragmentManager());
        //Set the pager with an adapter
        mPager  = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(sAdapter);
        slidingTabLayout.setViewPager(mPager);
        archivesLink = (TextView) findViewById(R.id.archiveLink);
        archivesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperMain.this,ArchivesActivity.class);
                startActivity(intent);
            }
        });

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
                    Intent intent = new Intent(SuperMain.this,TagActivity.class);
                    intent.putExtra(DBHelper.COLUMN_ID,tags.get(position).getTagId());
                    startActivity(intent);
                }else{
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(SuperMain.this,TagActivity.class);
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
            Tag tag = new Tag();
            TagDBHelper tagDBHelper = new TagDBHelper(getApplicationContext());
            tag.setTagText(str);
            tagDBHelper.open();
            tagDBHelper.saveTag(tag);
            tagDBHelper.close();
        }
    }



    private void addFloatingActionButtons(){

        FloatingActionButton addNote = (FloatingActionButton) findViewById(R.id.addNoteFab);
        FloatingActionButton addCheckList = (FloatingActionButton) findViewById(R.id.addCheckListab);
        FloatingActionButton addHabit = (FloatingActionButton) findViewById(R.id.addHabitFab);
        FloatingActionButton addList = (FloatingActionButton) findViewById(R.id.addListFab);
        final LayoutInflater layoutInflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SuperMain.this);
        // Setting Dialog Title
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 4;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final Spinner spinner = (Spinner) editInputLayout.findViewById(R.id.spinner);
                spinner.setVisibility(View.GONE);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
                editInput.setCursorVisible(true);
                alertDialog.setTitle("Add List").setView(editInputLayout).setPositiveButton("YES",
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


        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 1;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final Spinner spinner = (Spinner) editInputLayout.findViewById(R.id.spinner);
                ArrayAdapter<Tag> tagArrayAdapter = new ArrayAdapter<Tag>(getApplicationContext(),R.layout.spinner_list_item,tags);
                spinner.setAdapter(tagArrayAdapter);
                spinner.setSelection(tags.size()-1);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
                editInput.setCursorVisible(true);

                alertDialog.setTitle("Add List").setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString(),(Tag) spinner.getSelectedItem());
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();
              }
        });
        addCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 2;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final Spinner spinner = (Spinner) editInputLayout.findViewById(R.id.spinner);
                ArrayAdapter<Tag> tagArrayAdapter = new ArrayAdapter<Tag>(getApplicationContext(),R.layout.spinner_list_item,tags);
                spinner.setAdapter(tagArrayAdapter);
                spinner.setSelection(tags.size()-1);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
                editInput.setCursorVisible(true);
                alertDialog.setTitle("Add List").setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString(),(Tag) spinner.getSelectedItem());
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();
            }
        });
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave = 3;
                View editInputLayout =layoutInflater.inflate(R.layout.add_something_to_list, null);
                final Spinner spinner = (Spinner) editInputLayout.findViewById(R.id.spinner);
                ArrayAdapter<Tag> tagArrayAdapter = new ArrayAdapter<Tag>(getApplicationContext(),R.layout.spinner_list_item,tags);
                spinner.setAdapter(tagArrayAdapter);
                spinner.setSelection(tags.size()-1);
                final EditText editInput = (EditText) editInputLayout.findViewById(R.id.noteTitle);
                editInput.setCursorVisible(true);

                alertDialog.setTitle("Add List").setView(editInputLayout).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                String str = editInput.getText().toString();
                                if(str.length()>0){
                                    saveStuff(editInput.getText().toString(),(Tag) spinner.getSelectedItem());
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Title Cannot Be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }}).create().show();
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
        getMenuInflater().inflate(R.menu.super_main, menu);
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
            tags.add(new Tag("Untagged"));
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
