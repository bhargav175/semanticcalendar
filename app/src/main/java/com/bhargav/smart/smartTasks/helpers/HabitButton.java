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
import com.bhargav.smart.smartTasks.models.Habit;
import com.bhargav.smart.smartTasks.models.HabitItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 12-10-2014.
 */
public class HabitButton extends Button  implements View.OnClickListener {
    private static Boolean clickable = true;


    private Calendar buttonDate;
    private Habit habit;
    private HabitItem habitItem;
    private Context context;


    public HabitButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public HabitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HabitButton(Context context) {
        super(context);
        init();
    }
    public HabitButton(Context context, Calendar calendar, Habit habit) {
        super(context);
        initWithDate(context,calendar,habit);

    }

    private void init(){
            setOnClickListener(this);
    }

    private void initWithDate(Context context,Calendar cal, Habit habit){
        this.context = context;
        this.buttonDate = cal;
        this.setId((cal.get(Calendar.DAY_OF_MONTH))*1000000 + (cal.get(Calendar.MONTH)+1)*10000+ (cal.get(Calendar.YEAR)));
        this.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        this.habit = habit;
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
        String buttonT =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        if(this.habit!=null && this.context!=null && this.buttonDate!=null){
            new GetHabitItem(this.context,this,this.habit,this.buttonDate).execute("");
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
                new SaveHabitItem(this.context,this,this.habit,this.buttonDate).execute("");
            }


        }else{

        }

    }

    private class GetHabitItem extends AsyncTask<String, Void, HabitItem> {


        private Habit habit;
        private Calendar date;
        private Context context;
        private HabitButton habitButton;

        public GetHabitItem(Context context,HabitButton habitButton,Habit habit,Calendar date) {
            this.habit = habit; this.context = context; this.date = date;this.habitButton = habitButton;

        }

        @Override
        protected HabitItem doInBackground(String... params) {
            HabitItem habitItem  = HabitItem.getHabitItemByHabitAndDate(this.context,this.habit,this.date);
            return habitItem;
        }

        @Override
        protected void onPostExecute(final HabitItem habitItem) {
            this.habitButton.habitItem = habitItem;
            setColors(this.habitButton,this.habitButton.habitItem);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class SaveHabitItem extends AsyncTask<String, Void, Void> {


        private Habit habit;
        private Calendar date;
        private Context context;
        private HabitButton habitButton;
        private String stateString;

        public SaveHabitItem(Context context,HabitButton habitButton,Habit habit,Calendar date) {
            this.habit = habit; this.context = context; this.date = date;this.habitButton = habitButton;

        }

        @Override
        protected Void doInBackground(String... params) {


            if(this.habitButton.habitItem == null){
                HabitItem habitItem  = HabitItem.saveHabitItem(this.context,this.habit,this.date);
                this.habitButton.habitItem = habitItem;

            }
            else{

            }
                if(this.habitButton.habitItem.getHabitItemState()== HabitItem.State.UNSET){
                    this.habitButton.habitItem.setHabitItemState(HabitItem.State.SKIPPED);
                }
                else if(this.habitButton.habitItem.getHabitItemState()== HabitItem.State.SKIPPED){
                    this.habitButton.habitItem.setHabitItemState(HabitItem.State.COMPLETED_UNSUCCESSFULLY);
                }
                else if(this.habitButton.habitItem.getHabitItemState()== HabitItem.State.COMPLETED_UNSUCCESSFULLY){
                    this.habitButton.habitItem.setHabitItemState(HabitItem.State.COMPLETED_SUCCESSFULLY);
                }
                else if(this.habitButton.habitItem.getHabitItemState()== HabitItem.State.COMPLETED_SUCCESSFULLY){
                    this.habitButton.habitItem.setHabitItemState(HabitItem.State.UNSET);
                }
            HabitItem.updateHabitItem(this.context,this.habitButton.habitItem);

            return null;
        }

        @Override
        protected void onPostExecute(Void v

        ) {

            setColors(habitButton,this.habitButton.habitItem);
            this.stateString =HabitItem.getStateString(this.habitButton.habitItem.getHabitItemState());

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

    public void setColors(HabitButton habitButton, HabitItem habitItem){
        if(habitItem!=null){

            if(habitItem.getHabitItemState() == HabitItem.State.UNSET){
                habitButton.setBackgroundColor(getResources().getColor(R.color.white));
                habitButton.setTextColor(getResources().getColor(R.color.pitch_black));

            }
            if(habitItem.getHabitItemState() == HabitItem.State.SKIPPED){
                habitButton.setBackgroundColor(getResources().getColor(R.color.white));
                habitButton.setTextColor(getResources().getColor(R.color.light_blue_500));
                SpannableString content = new SpannableString(habitButton.getText());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                habitButton.setText(content);
            }
            if(habitItem.getHabitItemState() == HabitItem.State.COMPLETED_UNSUCCESSFULLY){
                habitButton.setBackgroundColor(getResources().getColor(R.color.white));
                habitButton.setTextColor(getResources().getColor(R.color.red_color));
                SpannableString content = new SpannableString(habitButton.getText());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                habitButton.setText(content);
            }
            if(habitItem.getHabitItemState() == HabitItem.State.COMPLETED_SUCCESSFULLY){
                habitButton.setBackgroundColor(getResources().getColor(R.color.white));
                habitButton.setTextColor(getResources().getColor(R.color.green_color));
                SpannableString content = new SpannableString(habitButton.getText());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                habitButton.setText(content);
            }
        }else{
            habitButton.setBackgroundColor(getResources().getColor(R.color.white));
            habitButton.setTextColor(getResources().getColor(R.color.pitch_black));

        }

    }



}
