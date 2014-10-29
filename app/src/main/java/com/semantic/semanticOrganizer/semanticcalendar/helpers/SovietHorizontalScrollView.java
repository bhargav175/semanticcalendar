package com.semantic.semanticOrganizer.semanticcalendar.helpers;

/**
 * Created by Admin on 28-10-2014.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class SovietHorizontalScrollView extends HorizontalScrollView
{
    public SovietHorizontalScrollView(Context context)
    {
        super(context);
    }

    public SovietHorizontalScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SovietHorizontalScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getChildCount() > 0 && getChildAt(0) instanceof ViewGroup)
        {
            ViewGroup view = (ViewGroup) getChildAt(0);
            int gravity = ((LayoutParams)view.getLayoutParams()).gravity;

            if(gravity == Gravity.CENTER || gravity == Gravity.CENTER_HORIZONTAL)
            {
                if(view.getMeasuredWidth() > getMeasuredWidth())
                {
                    LayoutParams params = (LayoutParams) view.getLayoutParams();
                    view.setLayoutParams(new LayoutParams(params.width, params.height, Gravity.NO_GRAVITY));
                }
            }
        }
    }
}