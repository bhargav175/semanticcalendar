package com.semantic.semanticOrganizer.docket.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.database.LabelDBHelper;
import com.semantic.semanticOrganizer.docket.models.Label;
import com.semantic.semanticOrganizer.docket.models.Tag;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 03-11-2014.
 */
public class AddLabelDialog extends AlertDialog.Builder {



    private View dialogLayout;
    private Integer tagId;
    private Integer itemId;
    //1 -> note, 2-> checklist , 3-> habit
    private Integer itemType;
    private List<Label> labels, selectedLabels;
    private List<Integer> selectedLabelIds;
    private Map<TextView,Label> labelMap;
    private Map<Integer,Label> idLabelMap;
    private Map<CheckBox,Label> checkBoxMap;
    private FragmentActivity fragmentActivity;
    private CheckBox violet, indigo, blue,green,yellow,orange,red,black;
    private TextView violetTv, indigoTv, blueTv, greenTv, yellowTv, orangeTv, redTv,blackTv;
    private FlowLayout labelLayout;



    public AddLabelDialog(final FragmentActivity a,Integer itemType, Integer itemId, Integer tagId,FlowLayout labelLayout) {
        super(a);
        // TODO Auto-generated constructor stub


        LayoutInflater inflater = a.getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.label_dialog, null);
        this.setTitle("Labels");
        this.setView(dialogLayout);
        this.itemType = itemType;
        this.itemId = itemId;
        this.tagId = tagId;
        this.fragmentActivity = a;
        this.labelLayout = labelLayout;
        initUi();
        setColorsForTextViews();
        setButtons();
        new GetLabels(a,this.tagId).execute("");


    }

    private void initUi() {
        labelMap = new LinkedHashMap<TextView, Label>();
        checkBoxMap = new LinkedHashMap<CheckBox, Label>();
        idLabelMap = new LinkedHashMap<Integer, Label>();
        violet=(CheckBox) dialogLayout.findViewById(R.id.violet);
        indigo=(CheckBox) dialogLayout.findViewById(R.id.indigo);
        blue=(CheckBox) dialogLayout.findViewById(R.id.blue);
        yellow=(CheckBox) dialogLayout.findViewById(R.id.yellow);
        orange=(CheckBox) dialogLayout.findViewById(R.id.orange);
        red=(CheckBox) dialogLayout.findViewById(R.id.red);
        black=(CheckBox) dialogLayout.findViewById(R.id.black);
        green=(CheckBox) dialogLayout.findViewById(R.id.green);
        violetTv = (TextView) dialogLayout.findViewById(R.id.violetEditText);
        indigoTv = (TextView) dialogLayout.findViewById(R.id.indigoEditText);
        blueTv = (TextView) dialogLayout.findViewById(R.id.blueEditText);
        greenTv = (TextView) dialogLayout.findViewById(R.id.greenEditText);
        yellowTv = (TextView) dialogLayout.findViewById(R.id.yellowEditText);
        orangeTv = (TextView) dialogLayout.findViewById(R.id.orangeEditText);
        redTv = (TextView) dialogLayout.findViewById(R.id.redEditText);
        blackTv = (TextView) dialogLayout.findViewById(R.id.blackEditText);
    }

    private void setButtons(){
        this.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        for(TextView tv: labelMap.keySet()){
                            Label label = labelMap.get(tv);
                            label.setName(tv.getText().toString());
                            new UpdateLabel(label).execute("");
                        }
                        // All those items which are checked.. //add them to the item and save em
                        for(CheckBox checkBox : checkBoxMap.keySet()){
                            Label label = checkBoxMap.get(checkBox);
                            if(checkBox.isChecked()){
                                if(selectedLabelIds.indexOf(label.getId())>-1){

                                }else{
                                    selectedLabelIds.add(label.getId());
                                }
                            }else{
                                if(selectedLabelIds.indexOf(label.getId())>-1){
                                    int index = selectedLabelIds.indexOf(label.getId());
                                    selectedLabelIds.remove(index);

                                }else{
                                }
                            }
                        }
                        updateLabelDisplay();

                    }
                }
        );
        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    private void setColorsForTextViews(){
        violetTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.violet_color));
        indigoTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.indigo_color));
        blueTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.blue_color));
        greenTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.green_color));
        yellowTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.yellow_color));
        orangeTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.orange_color));
        redTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.red_color));
        blackTv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.pitch_black));
    }
    private void populateDialog(){
        if(labels!=null){
            for(Label label: labels){
                idLabelMap.put(label.getId(),label);
                Integer color = label.getColor().getColorValue();
                TextView temp = null;
                CheckBox chimp = null;
                switch(color){
                    case 0:
                        temp = violetTv;
                        chimp = violet;
                        break;
                    case 1:
                        temp = indigoTv;
                        chimp = indigo;
                        break;
                    case 2:
                        temp = blueTv;
                        chimp = blue;
                        break;
                    case 3:
                        temp = greenTv;
                        chimp = green;
                        break;
                    case 4:
                        temp = yellowTv;
                        chimp = yellow;
                        break;
                    case 5:
                        temp = orangeTv;
                        chimp = orange;
                        break;
                    case 6:
                        temp = redTv;
                        chimp = red;
                        break;
                    case 7:
                        temp = blackTv;
                        chimp = black;
                        break;
                    default:
                        temp = violetTv;
                        chimp = violet;
                        break;

                }
                temp.setText(label.getName());
                labelMap.put(temp,label);
                checkBoxMap.put(chimp, label);

            }
        }
        new GetLabelsFromItem(this.fragmentActivity,this.itemId,this.itemType).execute("");

    }

    public void saveSelectedLabelsWithNote() {
        new SaveLabels(this.fragmentActivity,this.itemId,1).execute("");

    }

    public void saveSelectedLabelsWithCheckList() {
        new SaveLabels(this.fragmentActivity,this.itemId,2).execute("");

    }
    public void saveSelectedLabelsWithHabit() {
        new SaveLabels(this.fragmentActivity,this.itemId,3).execute("");

    }

    public class LabelHolder{
        public List<Integer> labelsList;
    }
    public LabelHolder getSelectedLabels(){
        LabelHolder labelHolder = new LabelHolder();
        labelHolder.labelsList = selectedLabelIds;
        return labelHolder;
    }
    private void updateLabelDisplay(){
        this.labelLayout.removeAllViews();
        for(Integer integer: selectedLabelIds){
            Label label = idLabelMap.get(integer);
            if(label!=null){
                TextView tv = new TextView(this.fragmentActivity);
                tv.setText(label.getName());
                tv.setPadding(8,8,8,8);
                tv.setBackgroundColor(Label.colorToDrawable(label.getColor()));
                tv.setTextColor(this.fragmentActivity.getResources().getColor(R.color.white));
                tv.setTextSize(this.fragmentActivity.getResources().getDimension(R.dimen.material_micro_text_size));
                this.labelLayout.addView(tv);
            }

        }

    }

    private void updateCheckBoxes() {
        for(CheckBox checkBox  : checkBoxMap.keySet()){
            if(selectedLabelIds.indexOf(checkBoxMap.get(checkBox).getId())>-1){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
        }
    }


    private class GetLabels extends AsyncTask<String, Void,Void> {

        private Context context;
        private Integer tagId;

        public GetLabels(Context context, Integer tagId){
            this.context=context; this.tagId= tagId;
        }

        @Override
        protected Void doInBackground(String... params) {
            LabelDBHelper labelDBHelper = new LabelDBHelper(context);
            labelDBHelper.open();
            labels =labelDBHelper.getLabelsByTag(tagId);
            labelDBHelper.close();
            //No Sandbox
            //tags.add(new Tag("SandBox"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
                populateDialog();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class UpdateLabel extends AsyncTask<String, Void,Void> {

        private Context context;
        private Label label;

        public UpdateLabel(Label label){
                this.label = label;
        }

        @Override
        protected Void doInBackground(String... params) {
            LabelDBHelper labelDBHelper = new LabelDBHelper(fragmentActivity);
            labelDBHelper.open();
            labelDBHelper.updateLabel(label);
            labelDBHelper.close();
            //No Sandbox
            //tags.add(new Tag("SandBox"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d("Add label Dialog", label.getName() +" Label Updated");
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class GetLabelsFromItem extends AsyncTask<String, Void,Void> {

        private Context context;
        private Integer id;
        private Integer type;

        public GetLabelsFromItem(Context context, Integer id, Integer type){
            this.id = id;
            this.context= context;
            this.type = type;
        }

        @Override
        protected Void doInBackground(String... params) {
            LabelDBHelper labelDBHelper = new LabelDBHelper(fragmentActivity);
            labelDBHelper.open();
            switch (type){
                case 1:
                    //note
                    selectedLabels = labelDBHelper.getLabelsByNote(id);
                    break;
                case 2:
                    //checklist
                    selectedLabels = labelDBHelper.getLabelsByCheckList(id);

                    break;
                case 3:
                    selectedLabels = labelDBHelper.getLabelsByHabit(id);
                    //habit
                    break;
                default:
                    //light
            }
            labelDBHelper.close();
            selectedLabelIds = new ArrayList<Integer>();
            for(Label label : selectedLabels){
                selectedLabelIds.add(label.getId());
            }

            //No Sandbox
            //tags.add(new Tag("SandBox"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    updateCheckBoxes();
                    updateLabelDisplay();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    private class SaveLabels extends AsyncTask<String, Void,Void> {

        private Context context;
        private Integer id;
        private Integer type;

        public SaveLabels(Context context, Integer id, Integer type){
            this.id = id;
            this.context= context;
            this.type = type;
        }

        @Override
        protected Void doInBackground(String... params) {
            LabelDBHelper labelDBHelper = new LabelDBHelper(fragmentActivity);
            labelDBHelper.open();
                switch (type){
                    case 1:
                        //note
                        labelDBHelper.saveLabelsWithNote(selectedLabelIds,id);

                        break;
                    case 2:
                        //checklist
                        labelDBHelper.saveLabelsWithCheckList(selectedLabelIds,id);

                        break;
                    case 3:
                        //habit
                        labelDBHelper.saveLabelsWithHabit(selectedLabelIds,id);

                        break;
                    default:
                        //light
                }


            labelDBHelper.close();
            selectedLabelIds = new ArrayList<Integer>();
            for(Label label : selectedLabels){
                selectedLabelIds.add(label.getId());
            }

            //No Sandbox
            //tags.add(new Tag("SandBox"));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    updateCheckBoxes();
                    updateLabelDisplay();
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }









}
