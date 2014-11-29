package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.models.RepeatingTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 12-10-2014.
 */
public class StateBoxRepeatingTask extends StateInterface {


    public StateBoxRepeatingTask(Context context) {
        super(context);
    }

    public StateBoxRepeatingTask(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void getStateFromDB() {

    }

    @Override
    public void init(Context context, Integer itemId) {

    }

    @Override
    public void onClick(View v) {

    }
}
