package com.bhargav.smart.smartTasks.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhargav.smart.smartTasks.R;
import com.bhargav.smart.smartTasks.helpers.MonthLayout;
import com.bhargav.smart.smartTasks.models.RepeatingTask;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 29-10-2014.
 */
public class InfinitePagerAdapter extends PagerAdapter {

    private List<MonthLayout> monthLayouts;
    private Context context;
    private RepeatingTask repeatingTaskCurrent;
    private Calendar currMonth;
    private MonthLayout firstLayout;

    public InfinitePagerAdapter(RepeatingTask repeatingTaskCurrent, Context context, List<MonthLayout> monthLayouts) {
        super();
        this.repeatingTaskCurrent = repeatingTaskCurrent;
        this.context= context;
        this.monthLayouts = monthLayouts;


        Calendar nextMonth = Calendar.getInstance();
        this.currMonth =(Calendar) nextMonth.clone();
        nextMonth.set(Calendar.MONTH,0);

    }



    @Override
    public int getCount() {
        return (Integer.MAX_VALUE);
    }

    public int getRealCount(){
        return monthLayouts.size();
    }

    public int addView (MonthLayout v)
    {
        return addView (v, monthLayouts.size());
    }

    public int addView (MonthLayout v, int position)
    {
        if(monthLayouts.size() ==0){
            firstLayout = v;
        }
        monthLayouts.add (position, v);
        return position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int virtualPosition = position % getRealCount();
        return instantiateVirtualItem(container, virtualPosition);
    }

    public Object instantiateVirtualItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mPage = inflater.inflate(R.layout.tag_list_item,null);
        TextView tv= (TextView) mPage.findViewById(R.id.text1);
        tv.setText(String.valueOf(position));
        container.addView(mPage);
        return mPage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int virtualPosition = position % getRealCount();
        destroyVirtualItem(container, virtualPosition, object);
    }


    public void destroyVirtualItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }



    @Override
    public boolean isViewFromObject (View view, Object object)
    {
        return view == object;
    }



}
