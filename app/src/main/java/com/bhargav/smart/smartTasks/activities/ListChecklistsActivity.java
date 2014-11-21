package com.bhargav.smart.smartTasks.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.docket.R;
import com.bhargav.smart.smartTasks.database.CheckListDBHelper;
import com.bhargav.smart.smartTasks.helpers.DBHelper;
import com.bhargav.smart.smartTasks.models.CheckList;

import java.util.ArrayList;
import java.util.List;

public class ListChecklistsActivity extends Activity {

    private CheckListDBHelper checkListDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_checklists);

        final List<CheckList> checkListList = new ArrayList<CheckList>();

        checkListDBHelper = new CheckListDBHelper(this);
        ArrayAdapter<CheckList> adapter = new ArrayAdapter<CheckList>(this,
                R.layout.tag_list_item,R.id.text1, CheckList.getAllCheckLists(checkListList,this)){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);

                text1.setText(checkListList.get(position).getCheckListTitle());
                text2.setText(checkListList.get(position).getCreatedTime());
                return view;
            }

        };
        ListView list = (ListView) findViewById(R.id.checkListsListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CheckList checkList= (CheckList) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), UpdateCheckListActivity.class);
                intent.putExtra(DBHelper.CHECKLIST_TITLE, checkList.getCheckListTitle());
                intent.putExtra(DBHelper.COLUMN_ID,checkList.getId());
                startActivity(intent);
            }
        });
        list.setAdapter(adapter);
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
}
