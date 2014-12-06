package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.utils.utilFunctions;

/**
 * Created by Admin on 12-10-2014.
 */
public abstract class StateTextInterface extends TextView{
        protected Integer itemId;
        protected utilFunctions.State state;
        protected Context context;

    public StateTextInterface(Context context) {
        super(context);
        this.context = context;

    }

    public StateTextInterface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    protected StateTextInterface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
                c = R.color.green_color;
                break;
            default:
                c= R.color.yellow_color;
        }
        this.setTextColor(getResources().getColor(c));
        this.setBackgroundColor(getResources().getColor(R.color.white));
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
