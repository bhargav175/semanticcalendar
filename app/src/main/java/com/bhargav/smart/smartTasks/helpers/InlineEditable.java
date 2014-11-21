package com.bhargav.smart.smartTasks.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.bhargav.smart.smartTasks.R;

/**
 * Created by Admin on 01-11-2014.
 */
public class InlineEditable extends EditText {
    public InlineEditable(Context context) {
        super(context);
        init();
    }

    public InlineEditable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InlineEditable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        this.setBackgroundResource(R.drawable.inline_editable);
    }
}
