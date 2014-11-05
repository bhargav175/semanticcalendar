package com.semantic.semanticOrganizer.docket.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.database.CheckListDBHelper;
import com.semantic.semanticOrganizer.docket.database.CheckListItemDBHelper;
import com.semantic.semanticOrganizer.docket.helpers.AddLabelDialog;
import com.semantic.semanticOrganizer.docket.helpers.DBHelper;
import com.semantic.semanticOrganizer.docket.helpers.DueDateDialog;
import com.semantic.semanticOrganizer.docket.helpers.InlineEditable;
import com.semantic.semanticOrganizer.docket.helpers.ReminderHelper;
import com.semantic.semanticOrganizer.docket.models.CheckList;
import com.semantic.semanticOrganizer.docket.models.CheckListItem;
import com.semantic.semanticOrganizer.docket.models.Reminder;
import com.semantic.semanticOrganizer.docket.models.Tag;
import com.semantic.semanticOrganizer.docket.utils.utilFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UpdateCheckListActivity extends FragmentActivity {
    private CheckListDBHelper checkListDBHelper;
    private CheckListItemDBHelper checkListItemDBHelper;
    private InlineEditable checkListText,checkListDescription;
    private Spinner tag;
    private Button addCheckListItemButton;
    private LinearLayout checkListItemContainer;
    private List<CheckListItem> checkListItems;
    private List<Integer> checkListItemIds;
    private CheckBox isArchived;
    private FloatingActionsMenu fam;
    private FloatingActionButton addDueDate, addLabel;
    Integer checkListId;
    private CheckList currentCheckList;
    private TextView showDueDateTextView;
    public ReminderHelper reminderHelper;
    private AddLabelDialog mAddLabelBuilder;
    private AlertDialog mAlert,mAddLabel;
    Integer remainderId;
    private List<Tag> tags;
    private ArrayAdapter<Tag> adapter;
    private Reminder currentReminder;
    private Boolean hadReminder,hasReminder;

    int day, month, year, hour, minute, second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        if(currentCheckList!=null){
                            if (checkListText.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();

                            } else {
                                updateCheckList();
                                Intent intent = new Intent(UpdateCheckListActivity.this,TagActivity.class);
                                if(currentCheckList.getTag()!=null){
                                    intent.putExtra(DBHelper.COLUMN_ID,currentCheckList.getTag());

                                }
                                startActivity(intent);
                            }


                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_add_checklist);

        Intent intent = getIntent();

        Bundle extras= intent.getExtras();
        if(extras!=null){
            String checkListTextString = extras.getString(DBHelper.CHECKLIST_TITLE);
            checkListId = extras.getInt(DBHelper.COLUMN_ID);
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


            new GetCheckList(checkListId).execute("");

        }else{
            Toast.makeText(this, "Could not load checkList", Toast.LENGTH_SHORT).show();
        }



    }

    private void initUi() {
         checkListText = (InlineEditable) findViewById(R.id.checkListTitle);
        checkListDescription = (InlineEditable) findViewById(R.id.checkListDescription);
        tag = (Spinner) findViewById(R.id.selectSpinner);
        checkListItemContainer = (LinearLayout) findViewById(R.id.checkListItemContainer);
        addCheckListItemButton = (Button) findViewById(R.id.addCheckListItemButton);
        isArchived = (CheckBox) findViewById(R.id.isArchived);
        checkListDBHelper = new CheckListDBHelper(this);
        checkListItemDBHelper = new CheckListItemDBHelper(this);
        showDueDateTextView = (TextView) findViewById(R.id.showDueDate);
        new GetTags(this).execute("");
        addFloatingActionButtons();
    }

    private void addFloatingActionButtons(){
        fam = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        addDueDate = (FloatingActionButton) findViewById(R.id.addDueDate);
        addLabel = (FloatingActionButton) findViewById(R.id.addLabel);

    }
    private void setListeners() {
        addCheckListItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View add_checklist_item = getLayoutInflater().inflate(R.layout.add_checklist_item_view, checkListItemContainer, false);
                checkListItemContainer.addView(add_checklist_item);
                EditText checkListItemText =(EditText) add_checklist_item.findViewById(R.id.noteTitle);
                CheckBox checkListItemCheckBox =(CheckBox) add_checklist_item.findViewById(R.id.isArchived);
                TextView idStore =(TextView) add_checklist_item.findViewById(R.id.idStore);
                idStore.setText(null);
                Button close =(Button) add_checklist_item.findViewById(R.id.removeCheckListItemButton);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkListItemContainer.removeView(add_checklist_item);
                    }
                });

            }
        });


    }

    private void setFloatingActionListeners(){
//        mAlertBuilder=new DueDateDialog(UpdateNoteActivity.this,noteCurrent.getDueTime(),showDueDateTextView,year,month,day,hour,minute,hasReminder);
//        mAlert = mAlertBuilder.create();

        reminderHelper = new ReminderHelper(this,UpdateCheckListActivity.this,remainderId,currentCheckList.getDueTime(),showDueDateTextView);
        mAddLabelBuilder=new AddLabelDialog(UpdateCheckListActivity.this);
        mAddLabel = mAddLabelBuilder.create();
        addDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderHelper.show();
            }
        });
        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddLabel.show();
            }
        });
    }


    private void updateUi(){
        getActionBar().setTitle(utilFunctions.toCamelCase(currentCheckList.getCheckListTitle()));

        checkListText.setText(currentCheckList.getCheckListTitle());
        checkListDescription.setText(currentCheckList.getCheckListDescription());
        isArchived.setChecked(currentCheckList.getIsArchived());
        if (currentCheckList.getReminderId() != null) {
            hadReminder = true;
            remainderId = currentCheckList.getReminderId();
        } else {
            hadReminder = false;
        }
        hasReminder = hadReminder;
        new GetReminder(remainderId).execute("");
        for(CheckListItem checkListItem : checkListItems){
            final View add_checklist_item = getLayoutInflater().inflate(R.layout.add_checklist_item_view, checkListItemContainer, false);
            checkListItemContainer.addView(add_checklist_item);
            EditText checkListItemText =(EditText) add_checklist_item.findViewById(R.id.noteTitle);
            CheckBox checkListItemCheckBox =(CheckBox) add_checklist_item.findViewById(R.id.isArchived);
            TextView idStore =(TextView) add_checklist_item.findViewById(R.id.idStore);
            idStore.setText(String.valueOf(checkListItem.getId()));
            Button close =(Button) add_checklist_item.findViewById(R.id.removeCheckListItemButton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkListItemContainer.removeView(add_checklist_item);
                }
            });
            checkListItemText.setText(checkListItem.getCheckListItemText());
            if(checkListItem.getCheckListItemState() == CheckListItem.State.NOT_DONE){
                checkListItemCheckBox.setChecked(false);
            }else{
                checkListItemCheckBox.setChecked(true);
            }
        }

    }

    private void updateCheckList() {
        String checkListTextString=checkListText.getText().toString();
        String checkListDescriptionString=checkListDescription.getText().toString();
        Tag checkListTag = (Tag) tag.getSelectedItem();
        if(currentCheckList!=null){
            if(checkListTextString.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show();
            }
            else{
                currentCheckList.setCheckListTitle(checkListTextString);
                currentCheckList.setCheckListDescription(checkListDescriptionString);
                currentCheckList.setTag(checkListTag.getTagId());
                currentCheckList.setIsArchived(isArchived.isChecked());
                new UpdateCheckList(this).execute("");
            }
        }
        else{
            Toast.makeText(this,"Update Failed",Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onPause()
    {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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


    private class GetCheckList extends AsyncTask<String, Void,Void> {

        private int id;
        public GetCheckList(int id){
            this.id = id;
        }

        @Override
        protected Void doInBackground(String... params) {
            currentCheckList  = CheckList.getCheckListById(id,getApplicationContext());
            if(currentCheckList!=null){
                checkListItems = CheckListItem.getAllCheckListItemsInCheckList(currentCheckList, getApplicationContext());
                checkListItemIds = new ArrayList<Integer>();
                for(CheckListItem checkListItem : checkListItems){
                    checkListItemIds.add(checkListItem.getId());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                        if(currentCheckList!=null){
                            initUi();
                            setListeners();
                            updateUi();
                        }else{
                            Toast.makeText(getApplicationContext(),"There was an error",Toast.LENGTH_SHORT).show();
                        }


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
            tags.add(new Tag("No Tag"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tags = Tag.getAllTags(new ArrayList<Tag>(), context);
                    tags.add(new Tag("No Tag"));
                    adapter = new ArrayAdapter<Tag>(context,
                            android.R.layout.simple_spinner_item, tags);
                    tag.setAdapter(adapter);
                    Integer tagId;
                    if (currentCheckList.getTag() != null) {
                        tagId = currentCheckList.getTag();
                        for (Tag tagD : tags) {
                            if (tagD.getTagId() == tagId) {
                                Integer pos = tags.indexOf(tagD);
                                tag.setSelection(pos);
                                break;
                            }
                        }
                    } else {
                        tag.setSelection(tags.size() - 1);
                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



    private class UpdateCheckList extends AsyncTask<String, Void,Void> {

        private Context context;

        public UpdateCheckList(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... params) {
            DueDateDialog.Holder holder = reminderHelper.doSomethingAboutTheReminder();
            remainderId = holder.remainderId;
            currentCheckList.setDueTime(holder.cal);
            currentCheckList.setReminderId(remainderId);
            checkListDBHelper.open();
            checkListDBHelper.updateCheckList(currentCheckList);
            checkListDBHelper.close();
            checkListItemDBHelper.open();

            List<Integer> newCheckListItems =new ArrayList<Integer>();
            for(int i=0; i< checkListItemContainer.getChildCount();i++){
                View view = checkListItemContainer.getChildAt(i);
                TextView idStore =(TextView) view.findViewById(R.id.idStore);
                String idString = idStore.getText().toString().trim();
                Integer checkListItemId = null;
                if(idString.equals("")){

                }else{
                    checkListItemId= Integer.valueOf(idString);
                    newCheckListItems.add(checkListItemId);
                }

                EditText checkListItemText =(EditText) view.findViewById(R.id.noteTitle);
                CheckBox checkListItemCheckBox =(CheckBox) view.findViewById(R.id.isArchived);
                if(checkListItemId==null){
                    //create checklistitem in database
                    CheckListItem checkListItem = new CheckListItem();
                    checkListItem.setCheckListItemText(checkListItemText.getText().toString());
                    checkListItem.setCheckList(currentCheckList.getId());
                    if(checkListItemCheckBox.isChecked()){
                        checkListItem.setCheckListItemState(CheckListItem.State.DONE);
                    }else{
                        checkListItem.setCheckListItemState(CheckListItem.State.NOT_DONE);
                    }

                    checkListItemDBHelper.saveCheckListItem(checkListItem);

                }else{
                    //update checklistitem in database if it has been modified
                    CheckListItem checkListItem = checkListItemDBHelper.getCheckListItem(checkListItemId);
                    Integer currStateValue = null;
                    if(checkListItemCheckBox.isChecked()){
                        currStateValue=2;
                    }else{
                        currStateValue = 0;
                    }

                    if(checkListItem.getCheckListItemText().equals(checkListItemText.getText().toString()) && checkListItem.getCheckListItemState().getStateValue() == currStateValue )
                    {
                        //It was not modified
                    }else{
                        checkListItem.setCheckListItemText(checkListItemText.getText().toString());
                        checkListItem.setCheckList(currentCheckList.getId());
                        checkListItem.setCheckListItemState(CheckListItem.State.values()[currStateValue]);
                        checkListItemDBHelper.updateCheckListItem(checkListItem);
                    }
                }
            }

            for(Integer i: checkListItemIds){
                if(newCheckListItems.indexOf(i)<0){
                    //delete the checklistitem
                    checkListItemDBHelper.deleteCheckListItem(i);
                }
            }
            checkListItemDBHelper.open();

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

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

    private class GetReminder extends AsyncTask<String, Void, Reminder> {

        private Integer id;

        public GetReminder(Integer id) {
            this.id = id;
        }

        @Override
        protected Reminder doInBackground(String... params) {
            if (id != null) {
                currentReminder = Reminder.getReminderById(id, getApplicationContext());
            } else {
                currentReminder = null;
            }
            return currentReminder;
        }

        @Override
        protected void onPostExecute(final Reminder reminder) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (currentReminder != null) {
                        year = reminder.getYear();
                        month = reminder.getMonthOfYear();
                        day = reminder.getDayOfMonth();
                        hour = reminder.getHourOfDay();
                        minute = reminder.getMinuteOfHour();
                        second = reminder.getSecond();
                        Calendar calendar = new GregorianCalendar();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                    } else {

                    }
                    setFloatingActionListeners();

                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
