package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.RepeatingTask;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 12-10-2014.
 */
public abstract class StateInterface extends Button{
        protected Integer itemId;
        protected utilFunctions.State state;
        protected Context context;

    public StateInterface(Context context) {
        super(context);
        this.context = context;

    }

    public StateInterface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    protected StateInterface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    protected void changeState(){
        switch (this.state){
            case NOT_STARTED:
                this.state = utilFunctions.State.STARTED;
                break;
            case STARTED:
                this.state = utilFunctions.State.FAILED;
                break;
            case FAILED:
                this.state = utilFunctions.State.COMPLETED;
                break;
            case COMPLETED:
                this.state = utilFunctions.State.NOT_STARTED;
                break;
            default:
                this.state = utilFunctions.State.NOT_STARTED;
                break;
        }

    }




    protected void appearanceInit(){
        this.setText("Not Started");

    }

    public void updateColor(){
        int c ;
        if(this.state!=null){

        }else{
            this.state = utilFunctions.State.NOT_STARTED;
        }
        switch (this.state){
            case STARTED:
               c = R.color.indigo_color;
               break;
            case NOT_STARTED:
                c = R.color.orange_color;
                break;
            case FAILED:
                c = R.color.red_color;
                break;
            case COMPLETED:
                c = R.color.teal_color;
                break;
            default:
                c= R.color.yellow_color;
        }
        this.setBackgroundColor(getResources().getColor(c));
    }

    public void updateText(){
        String str;
        if(this.state!=null){

        }else{
            this.state = utilFunctions.State.NOT_STARTED;
        }
        switch (this.state){
            case STARTED:
                str = "Started";
                break;
            case NOT_STARTED:
                str = "Not Started";
                break;
            case FAILED:
                str = "Failed";
                break;
            case COMPLETED:
                str = "Completed";
                break;
            default:
                str = "Started";
        }
        this.setText(str);
    }


    abstract void getStateFromDB();
    public abstract void init(Context context, Integer itemId);
}