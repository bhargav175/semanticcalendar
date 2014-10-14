package com.semantic.semanticOrganizer.semanticcalendar.activities;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.database.CheckListDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.database.CheckListItemDBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.DBHelper;
import com.semantic.semanticOrganizer.semanticcalendar.models.CheckList;
import com.semantic.semanticOrganizer.semanticcalendar.models.CheckListItem;
import com.semantic.semanticOrganizer.semanticcalendar.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class UpdateCheckListActivity extends Activity {
    private CheckListDBHelper checkListDBHelper;
    private CheckListItemDBHelper checkListItemDBHelper;
    private EditText checkListText;
    private Spinner tag;
    private Button addCheckListItemButton;
    private LinearLayout checkListItemContainer;
    private List<CheckListItem> checkListItems;
    private List<Integer> checkListItemIds;
    private CheckBox isArchived;
    Integer checkListId;
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
                        if(checkListId!=null){

                            updateCheckList(checkListId);
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
            Toast.makeText(this, "Could not load checkList", Toast.LENGTH_LONG).show();
        }



    }

    private void initUi() {


        checkListText = (EditText) findViewById(R.id.checkListText);

        tag = (Spinner) findViewById(R.id.selectSpinner);
        checkListItemContainer = (LinearLayout) findViewById(R.id.checkListItemsContainer);
        addCheckListItemButton = (Button) findViewById(R.id.addCheckListItemButton);
        isArchived = (CheckBox) findViewById(R.id.isArchived);


    }
    private void setListeners() {
        addCheckListItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View add_checklist_item = getLayoutInflater().inflate(R.layout.add_checklist_item_view, checkListItemContainer, false);
                checkListItemContainer.addView(add_checklist_item);
                EditText checkListItemText =(EditText) add_checklist_item.findViewById(R.id.editText);
                CheckBox checkListItemCheckBox =(CheckBox) add_checklist_item.findViewById(R.id.checkBox);
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

    private void updateUi(CheckList checkList){

        checkListDBHelper = new CheckListDBHelper(this);
        checkListItemDBHelper = new CheckListItemDBHelper(this);
        checkListDBHelper.open();

        checkListItemDBHelper.open();

        checkListText.setText(checkList.getCheckListText());
        isArchived.setChecked(checkList.getIsArchived());


        for(CheckListItem checkListItem : checkListItems){
            final View add_checklist_item = getLayoutInflater().inflate(R.layout.add_checklist_item_view, checkListItemContainer, false);
            checkListItemContainer.addView(add_checklist_item);
            EditText checkListItemText =(EditText) add_checklist_item.findViewById(R.id.editText);
            CheckBox checkListItemCheckBox =(CheckBox) add_checklist_item.findViewById(R.id.checkBox);
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


        List<Tag> tags = Tag.getAllTags(new ArrayList<Tag>(),getApplicationContext());
        tags.add(new Tag("No Tag"));
        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(this,
                android.R.layout.simple_spinner_item,tags );
        tag.setAdapter(adapter);


        Integer tagId = checkList.getTag() ;
        if(tagId!=null){

            for(Tag tagD : tags){
                if(tagD.getTagId() == tagId){
                    Integer pos = tags.indexOf(tagD);
                    tag.setSelection(pos);
                    break;
                }
            }
        }else{
            tag.setSelection(tags.size()-1);
        }



        checkListItemDBHelper.close();

        checkListDBHelper.close();


    }

    private void updateCheckList(Integer checkListId) {
        String checkListTextString=checkListText.getText().toString();
        checkListDBHelper.open();
        CheckList checkList = checkListDBHelper.getCheckList(checkListId);
        Tag checkListTag = (Tag) tag.getSelectedItem();

        if(checkList!=null){
            if(checkListTextString.length()==0){
                Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_LONG).show();
            }
            else{
                checkListItemDBHelper.open();


                checkListDBHelper.updateCheckList(checkList, checkListTextString,checkListTag.getTagId(), isArchived.isChecked());
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

                    EditText checkListItemText =(EditText) view.findViewById(R.id.editText);
                    CheckBox checkListItemCheckBox =(CheckBox) view.findViewById(R.id.checkBox);
                    if(checkListItemId==null){
                        //create checklistitem in database
                        CheckListItem checkListItem = new CheckListItem();
                        checkListItem.setCheckListItemText(checkListItemText.getText().toString());
                        checkListItem.setCheckList(checkList.getId());
                        if(checkListItemCheckBox.isChecked()){
                            checkListItem.setCheckListItemState(CheckListItem.State.DONE);
                        }else{
                            checkListItem.setCheckListItemState(CheckListItem.State.NOT_DONE);
                        }

                        checkListItemDBHelper.saveCheckListItem(checkListItem);



                    }else{
                        //update checklistitem in database
                       CheckListItem checkListItem = new CheckListItem();
                        checkListItem.setCheckListItemText(checkListItemText.getText().toString());
                        checkListItem.setCheckList(checkList.getId());
                        if(checkListItemCheckBox.isChecked()){
                            checkListItem.setCheckListItemState(CheckListItem.State.DONE);
                        }else{
                            checkListItem.setCheckListItemState(CheckListItem.State.NOT_DONE);
                        }

                        checkListItemDBHelper.updateCheckListItem(checkListItem,checkListItemText.getText().toString(), CheckListItem.State.NOT_DONE);


                    }



                }

                for(Integer i: checkListItemIds){
                    if(newCheckListItems.indexOf(i)<0){
                        //delete the checklistitem
                        checkListItemDBHelper.deleteCheckListItem(i);

                    }
                }


                checkListDBHelper.close();

                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
            }

        }
        else{
            checkListDBHelper.close();
            Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
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


    private class GetCheckList extends AsyncTask<String, Void,CheckList> {

        private int id;
        public GetCheckList(int id){
            this.id = id;
        }

        @Override
        protected CheckList doInBackground(String... params) {
            CheckList checkList  = CheckList.getCheckListById(id,getApplicationContext());
            return checkList;
        }

        @Override
        protected void onPostExecute(final CheckList checkListTemp) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(checkListTemp!=null){
                        checkListItems = CheckListItem.getAllCheckListItemsInCheckList(checkListTemp, getApplicationContext());
                        checkListItemIds = new ArrayList<Integer>();
                        for(CheckListItem checkListItem : checkListItems){
                            checkListItemIds.add(checkListItem.getId());
                        }
                        initUi();
                        setListeners();
                        updateUi(checkListTemp);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
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
