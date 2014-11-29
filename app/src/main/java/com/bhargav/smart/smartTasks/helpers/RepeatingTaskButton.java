package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.models.RepeatingTaskItem;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 12-10-2014.
 */
public class RepeatingTaskButton extends Button  implements View.OnClickListener {
    private static Boolean clickable = true;


    private Calendar buttonDate;
    private RepeatingTask repeatingTask;
    private RepeatingTaskItem repeatingTaskItem;
    private Context context;


    public RepeatingTaskButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RepeatingTaskButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RepeatingTaskButton(Context context) {
        super(context);
        init();
    }
    public RepeatingTaskButton(Context context, Calendar calendar, RepeatingTask repeatingTask) {
        super(context);
        initWithDate(context,calendar, repeatingTask);

    }

    private void init(){
            setOnClickListener(this);
    }

    private void initWithDate(Context context,Calendar cal, RepeatingTask repeatingTask){
        this.context = context;
        this.buttonDate = cal;
        this.setId((cal.get(Calendar.DAY_OF_MONTH))*1000000 + (cal.get(Calendar.MONTH)+1)*10000+ (cal.get(Calendar.YEAR)));
        this.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        this.repeatingTask = repeatingTask;
        init();
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1f);
        lp.gravity = Gravity.CENTER;
        lp.setMargins(2, 2, 2, 2);
        this.setLayoutParams(lp);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundColor(getResources().getColor(R.color.white));
        this.setTextColor(getResources().getColor(R.color.pitch_black));
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.material_micro_text_size));
        String buttonT =  new SimpleDateFormat(utilFunctions.reverseDateTimeFormat).format(cal.getTime());
        if(this.repeatingTask !=null && this.context!=null && this.buttonDate!=null){
            new GetHabitItem(this.context,this,this.repeatingTask,this.buttonDate).execute("");
        }

    }


    @Override
    public void onClick(View v) {
        // Do something
        if(clickable){
            clickable = false;
            Calendar now = Calendar.getInstance();
            if(this.buttonDate.compareTo(now)>0){
                Toast.makeText(this.context,"Cannot mark habit in future",Toast.LENGTH_SHORT).show();
                clickable= true;
            }else{
                new SaveHabitItem(this.context,this,this.repeatingTask,this.buttonDate).execute("");
            }


        }else{

        }

    }

    private class GetHabitItem extends AsyncTask<String, Void, RepeatingTaskItem> {


        private RepeatingTask repeatingTask;
        private Calendar date;
        private Context context;
        private RepeatingTaskButton repeatingTaskButton;

        public GetHabitItem(Context context, RepeatingTaskButton repeatingTaskButton,RepeatingTask repeatingTask,Calendar date) {
            this.repeatingTask = repeatingTask; this.context = context; this.date = date;this.repeatingTaskButton = repeatingTaskButton;

        }

        @Override
        protected RepeatingTaskItem doInBackground(String... params) {
            RepeatingTaskItem repeatingTaskItem = RepeatingTaskItem.getHabitItemByHabitAndDate(this.context, this.repeatingTask, this.date);
            return repeatingTaskItem;
        }

        @Override
        protected void onPostExecute(final RepeatingTaskItem repeatingTaskItem) {
            this.repeatingTaskButton.repeatingTaskItem = repeatingTaskItem;
            setColors(this.repeatingTaskButton,this.repeatingTaskButton.repeatingTaskItem);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class SaveHabitItem extends AsyncTask<String, Void, Void> {


        private RepeatingTask repeatingTask;
        private Calendar date;
        private Context context;
        private RepeatingTaskButton repeatingTaskButton;
        private String stateString;

        public SaveHabitItem(Context context, RepeatingTaskButton repeatingTaskButton,RepeatingTask repeatingTask,Calendar date) {
            this.repeatingTask = repeatingTask; this.context = context; this.date = date;this.repeatingTaskButton = repeatingTaskButton;

        }

        @Override
        protected Void doInBackground(String... params) {


            if(this.repeatingTaskButton.repeatingTaskItem == null){
                RepeatingTaskItem repeatingTaskItem = RepeatingTaskItem.saveHabitItem(this.context, this.repeatingTask, this.date);
                this.repeatingTaskButton.repeatingTaskItem = repeatingTaskItem;

            }
            else{

            }
                if(this.repeatingTaskButton.repeatingTaskItem.getHabitItemState()== utilFunctions.State.NOT_STARTED){
                    this.repeatingTaskButton.repeatingTaskItem.setHabitItemState(utilFunctions.State.STARTED);
                }
                else if(this.repeatingTaskButton.repeatingTaskItem.getHabitItemState()== utilFunctions.State.STARTED){
                    this.repeatingTaskButton.repeatingTaskItem.setHabitItemState(utilFunctions.State.COMPLETED);
                }
                else if(this.repeatingTaskButton.repeatingTaskItem.getHabitItemState()==utilFunctions.State.FAILED){
                    this.repeatingTaskButton.repeatingTaskItem.setHabitItemState(utilFunctions.State.COMPLETED);
                }
                else if(this.repeatingTaskButton.repeatingTaskItem.getHabitItemState()== utilFunctions.State.COMPLETED){
                    this.repeatingTaskButton.repeatingTaskItem.setHabitItemState(utilFunctions.State.NOT_STARTED);
                }
            RepeatingTaskItem.updateHabitItem(this.context, this.repeatingTaskButton.repeatingTaskItem);

            return null;
        }

        @Override
        protected void onPostExecute(Void v

        ) {

            setColors(repeatingTaskButton,this.repeatingTaskButton.repeatingTaskItem);
            this.stateString = RepeatingTaskItem.getStateString(this.repeatingTaskButton.repeatingTaskItem.getHabitItemState());

            final Handler h = new Handler(Looper.getMainLooper());

            final Runnable r =new Runnable() {
                @Override
                public void run() {

                    clickable = true;


                }
            };

            Runnable r1 = new Runnable() {

                @Override
                public void run() {
                    // do first thing
                    if(stateString==null){
                        Toast.makeText(context, "There was an error", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, stateString, Toast.LENGTH_SHORT).show();

                    }
                    h.postDelayed(r, 2000); // Length short second delay
                }
            };
            h.post(r1);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void setColors(RepeatingTaskButton repeatingTaskButton, RepeatingTaskItem repeatingTaskItem){
        if(repeatingTaskItem !=null){

            if(repeatingTaskItem.getHabitItemState() == utilFunctions.State.NOT_STARTED){
                repeatingTaskButton.setBackgroundColor(getResources().getColor(R.color.white));
                repeatingTaskButton.setTextColor(getResources().getColor(R.color.pitch_black));

            }
            if(repeatingTaskItem.getHabitItemState() == utilFunctions.State.STARTED){
                repeatingTaskButton.setBackgroundColor(getResources().getColor(R.color.white));
                repeatingTaskButton.setTextColor(getResources().getColor(R.color.light_blue_500));
                SpannableString content = new SpannableString(repeatingTaskButton.getText());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                repeatingTaskButton.setText(content);
            }
            if(repeatingTaskItem.getHabitItemState() == utilFunctions.State.FAILED){
                repeatingTaskButton.setBackgroundColor(getResources().getColor(R.color.white));
                repeatingTaskButton.setTextColor(getResources().getColor(R.color.red_color));
                SpannableString content = new SpannableString(repeatingTaskButton.getText());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                repeatingTaskButton.setText(content);
            }
            if(repeatingTaskItem.getHabitItemState() == utilFunctions.State.COMPLETED){
                repeatingTaskButton.setBackgroundColor(getResources().getColor(R.color.white));
                repeatingTaskButton.setTextColor(getResources().getColor(R.color.green_color));
                SpannableString content = new SpannableString(repeatingTaskButton.getText());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                repeatingTaskButton.setText(content);
            }
        }else{
            repeatingTaskButton.setBackgroundColor(getResources().getColor(R.color.white));
            repeatingTaskButton.setTextColor(getResources().getColor(R.color.pitch_black));

        }

    }



}
