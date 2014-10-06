package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.semantic.semanticOrganizer.semanticcalendar.R;

public class LandingActivity extends Activity {

    private Button tag_btn,event_btn,checklist_btn,note_btn,alarm_btn,habit_btn;
//    private ActionMode.Callback tagActionModeCallback = new ActionMode.Callback() {
//
//        // Called when the action mode is created; startActionMode() was called
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            // Inflate a menu resource providing context menu items
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.context_menu, menu);
//            return true;
//        }
//
//        // Called each time the action mode is shown. Always called after onCreateActionMode, but
//        // may be called multiple times if the mode is invalidated.
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false; // Return false if nothing is done
//        }
//
//        // Called when the user selects a contextual menu item
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_cancel:
//                    tag_text.setText("");
//                    hideLayouts();
//                    Toast.makeText(getApplication(),"Cancel",Toast.LENGTH_LONG).show();
//                case R.id.action_okay:
//                    String text = tag_text.getText().toString();
//                    tag_text.setText("");
//                    hideLayouts();
//                    Tag tag= new Tag(text);
//                    tag.save(getApplicationContext());
//                    Toast.makeText(getApplication(),text,Toast.LENGTH_LONG).show();
//                    return true;
//                default:
//                    return false;
//            }
//        }
//
//        // Called when the user exits the action mode
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mode = null;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_refactor);

        initializeUi();
        setListeners();
        setActionModeListeners();
    }

    private void setListeners() {
        tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add TAg Activity
                Intent intent = new Intent(getApplicationContext(), AddTagActivity.class);
                startActivity(intent);

            }
        });
        event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(intent);
            }
        });
        checklist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddChecklistActivity.class);
                startActivity(intent);
            }
        });
        note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        habit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHabitActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setActionModeListeners(){


//
//        checklist_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                ActionMode mMode = null;
//                if(hasFocus){
//                    mMode = startActionMode(checklistActionModeCallback);
//                }else{
//                    if(mMode!=null){
//                        mMode.finish();
//                    }
//                }
//            }
//        });

    }


    private void initializeUi() {

        tag_btn= (Button) findViewById(R.id.tag_btn);
        event_btn= (Button) findViewById(R.id.event_btn);
        checklist_btn= (Button) findViewById(R.id.checklist_btn);
        note_btn= (Button) findViewById(R.id.note_btn);
        habit_btn= (Button) findViewById(R.id.habit_btn);


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
        if (id == R.id.action_view_tags) {
            Intent intent = new Intent(this,ListTagsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_notes) {
            Intent intent = new Intent(this,ListNotesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_view_events) {
            Intent intent = new Intent(this,ListEventsActivity.class);
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
}
