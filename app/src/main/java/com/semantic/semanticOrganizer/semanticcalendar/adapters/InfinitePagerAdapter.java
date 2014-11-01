package com.semantic.semanticOrganizer.semanticcalendar.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.semantic.semanticOrganizer.semanticcalendar.R;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.MonthFragment;
import com.semantic.semanticOrganizer.semanticcalendar.helpers.MonthLayout;
import com.semantic.semanticOrganizer.semanticcalendar.models.Habit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by Admin on 29-10-2014.
 */
public class InfinitePagerAdapter extends PagerAdapter {

    private List<MonthLayout> monthLayouts;
    private Context context;
    private Habit habitCurrent;
    private Calendar currMonth;

    public InfinitePagerAdapter(Habit habitCurrent, Context context, List<MonthLayout> monthLayouts) {
        super();
        this.habitCurrent = habitCurrent;
        this.context= context;
        this.monthLayouts = monthLayouts;


        Calendar nextMonth = Calendar.getInstance();
        this.currMonth =(Calendar) nextMonth.clone();
        nextMonth.set(Calendar.MONTH,0);

//        for(int i = 0 ; i <12;i++){
//            nextMonth = (Calendar)nextMonth.clone();
//            nextMonth.add(Calendar.MONTH,1);
//
//            MonthFragment next = MonthFragment.newInstance(nextMonth.get(Calendar.YEAR), nextMonth.get(Calendar.MONTH), habitCurrent.getId());
//            this.monthLayouts.add(next);
//        }
    }

    @Override
    public Object instantiateItem (ViewGroup container, int position){
//        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View mPage = inflater.inflate(R.layout.month_table_layout,null);
//        LayoutInflater inflater = (LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        MonthLayout monthLayout1 =(MonthLayout) inflater.inflate(R.layout.month_table_layout,null);
//        monthLayout1.initializeWith(context,currMonth,habitCurrent);
//        this.monthLayouts.add(monthLayout1);
        View v =this.monthLayouts.get(position);
        container.addView(v);
        return v;

    }

    @Override
    public int getItemPosition (Object object)
    {
//        //change
////
//        int
//            return POSITION_NONE;
//        else index = monthLayouts.indexOf (object);
//        if (index == -1)
            return POSITION_NONE;
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
    // to add the page to the container, which is normally the ViewPager itself.  Since
    // all our pages are persistent, we simply retrieve it from our "views" ArrayList.


    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
    // is our job to remove the page from the container, which is normally the
    // ViewPager itself.  Since all our pages are persistent, we do nothing to the
    // contents of our "views" ArrayList.
    @Override
    public void destroyItem (ViewGroup container, int position, Object object)
    {
        container.removeView ((View) object);
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager; can be used by app as well.
    // Returns the total number of pages that the ViewPage can display.  This must
    // never be 0.
    @Override
    public int getCount ()
    {
        return 3;
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.
    @Override
    public boolean isViewFromObject (View view, Object object)
    {
        return view == object;
    }

    //-----------------------------------------------------------------------------
    // Add "view" to right end of "views".
    // Returns the position of the new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView (MonthLayout v)
    {
        return addView (v, monthLayouts.size());
    }

    //-----------------------------------------------------------------------------
    // Add "view" at "position" to "views".
    // Returns position of new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView (MonthLayout v, int position)
    {
        monthLayouts.add (position, v);
        return position;
    }

    //-----------------------------------------------------------------------------
    // Removes "view" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView (ViewPager pager, View v)
    {
        return removeView (pager, monthLayouts.indexOf (v));
    }

    //-----------------------------------------------------------------------------
    // Removes the "view" at "position" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView (ViewPager pager, int position)
    {
        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        pager.setAdapter (null);
        monthLayouts.remove (position);
        pager.setAdapter (this);

        return position;
    }

    //-----------------------------------------------------------------------------
    // Returns the "view" at "position".
    // The app should call this to retrieve a view; not used by ViewPager.

    // Other relevant methods:

    // finishUpdate - called by the ViewPager - we don't care about what pages the
    // pager is displaying so we don't use this method.
}
