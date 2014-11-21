package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by Admin on 12-10-2014.
 */
public class MySpinner extends Spinner {

    OnItemSelectedListener listener;

    public MySpinner(Context context) {
        super(context);
    }

    public MySpinner(Context context, int mode, OnItemSelectedListener listener) {
        super(context, mode);
        this.listener = listener;
    }

    public MySpinner(Context context, AttributeSet attrs, OnItemSelectedListener listener) {
        super(context, attrs);
        this.listener = listener;
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, OnItemSelectedListener listener) {
        super(context, attrs, defStyleAttr);
        this.listener = listener;
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, OnItemSelectedListener listener) {
        super(context, attrs, defStyleAttr, mode);
        this.listener = listener;
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setSelection(int position)
    {
        super.setSelection(position);

        if (position == getSelectedItemPosition())
        {
            listener.onItemSelected(null, null, position, 0);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        this.listener = listener;
    }
}
