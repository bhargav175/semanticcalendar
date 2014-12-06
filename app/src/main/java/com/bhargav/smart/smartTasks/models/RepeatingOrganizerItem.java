package com.bhargav.smart.smartTasks.models;

import android.content.Context;

import com.bhargav.smart.smartTasks.utils.utilFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bhargav.smart.smartTasks.utils.utilFunctions.BLog;

/**
 * Created by Admin on 07-10-2014.
 */
public class RepeatingOrganizerItem extends OrganizerItem {

    public Map<Calendar,RepeatingTaskItem> calendarRepeatingTaskItemMap = new LinkedHashMap<Calendar, RepeatingTaskItem>();

    public static List<RepeatingOrganizerItem> getAllReUnArchivedOverdueRepeatingItems(Context context, Calendar calendar) {

        List<RepeatingOrganizerItem> repeatingOrganizerItems = new ArrayList<RepeatingOrganizerItem>();
        List<RepeatingTask> repeatingTaskList = RepeatingTask.getAllUnArchivedRepeatingTasksWithStartDateBeforeToday(context, calendar);
        repeatingOrganizerItems.addAll(castHabitsToRepeatingOrganizerItemList(context, repeatingTaskList));
        Collections.sort(repeatingOrganizerItems, new Comparator<RepeatingOrganizerItem>() {
            @Override
            public int compare(RepeatingOrganizerItem rorg1, RepeatingOrganizerItem rorg2) {
                return rorg1.getDueTime().compareTo(rorg2.getDueTime());
            }
        });


        return repeatingOrganizerItems;

    }

    public static List<RepeatingOrganizerItem> castHabitsToRepeatingOrganizerItemList(Context context,List<RepeatingTask> repeatingTaskList){
        List<RepeatingOrganizerItem> repeatingOrganizerItems = new ArrayList<RepeatingOrganizerItem>();

        for(RepeatingTask repeatingTask : repeatingTaskList){
            repeatingOrganizerItems.add(castToRepeatingOrganizerItem(context, repeatingTask));
        }
        return repeatingOrganizerItems;
    }

    public static RepeatingOrganizerItem castToRepeatingOrganizerItem(Context context,RepeatingTask repeatingTask){
        RepeatingOrganizerItem repeatingOrganizerItem = new RepeatingOrganizerItem();
        if(repeatingTask.getRepeatingTaskDescription()!=null && repeatingTask.getRepeatingTaskDescription().length()>0){
            repeatingOrganizerItem.setItemText(repeatingTask.getRepeatingTaskDescription());
        }else{
            repeatingOrganizerItem.setItemText(repeatingTask.getRepeatingTaskText());
        }
        if(repeatingTask.getDueTime()!=null){
            Calendar today = Calendar.getInstance();
            Calendar c =(Calendar) repeatingTask.getDueTime().clone();
            c.set(Calendar.YEAR,today.get(Calendar.YEAR));
            c.set(Calendar.MONTH,today.get(Calendar.MONTH));
            c.set(Calendar.DAY_OF_MONTH,today.get(Calendar.DAY_OF_MONTH));
            repeatingOrganizerItem.setDueTime(c);
        }
        repeatingOrganizerItem.setCreatedTime(repeatingTask.getCreatedTime());
        repeatingOrganizerItem.setId(repeatingTask.getId());
        repeatingOrganizerItem.setType("HABIT");

        return repeatingOrganizerItem;
    }


    public static RepeatingOrganizerItem getRepeatingTaskItemsInToRepeatingOrganizerItem(Context context,RepeatingOrganizerItem repeatingOrganizerItem){
      RepeatingTask repeatingTask = RepeatingTask.getRepeatingTaskById(repeatingOrganizerItem.getId(),context);
        if(repeatingTask.getStartDate()!=null &&repeatingTask.getEndDate()!=null){
            int hits = 0;int misses =0;
            Calendar startCalendar = (Calendar) repeatingTask.getStartDate().clone();
            Calendar endCalendar = (Calendar) repeatingTask.getEndDate().clone();
            Calendar loopThrough = (Calendar) startCalendar.clone();

            while(loopThrough.compareTo(endCalendar)<=0){
                if(repeatingTask.isDayDueDate(loopThrough)){
                    RepeatingTaskItem repeatingTaskItem = RepeatingTaskItem.getHabitItemByHabitAndDate(context,repeatingTask,loopThrough);
                    if(repeatingTaskItem!=null){

                        if(repeatingTaskItem.getHabitItemState()!=null){
                            if(repeatingTaskItem.getHabitItemState() == utilFunctions.State.COMPLETED){
                                hits +=1;
                            }
                            else{
                                misses +=1;
                            }
                            BLog(new SimpleDateFormat(utilFunctions.dateFormat).format(loopThrough.getTime()) + "  Repeating Task Item Exists And is Not Null");
                            repeatingOrganizerItem.calendarRepeatingTaskItemMap.put((Calendar)loopThrough.clone(), repeatingTaskItem);
                            BLog("Key size" + (String.valueOf(repeatingOrganizerItem.calendarRepeatingTaskItemMap.keySet().size())));
                        }else{
                            misses+=1;
                            BLog(new SimpleDateFormat(utilFunctions.dateFormat).format(loopThrough.getTime())+ "  Repeating Task Item Exists And is Null");
                            repeatingOrganizerItem.calendarRepeatingTaskItemMap.put((Calendar)loopThrough.clone(), repeatingTaskItem);
                            BLog("Key size" + (String.valueOf(repeatingOrganizerItem.calendarRepeatingTaskItemMap.keySet().size())));
                        }

                    }
                    else{
                        BLog(new SimpleDateFormat(utilFunctions.dateFormat).format(loopThrough.getTime()) + " Repeating Task Item Does Not Exist"  );
                        repeatingOrganizerItem.calendarRepeatingTaskItemMap.put((Calendar)loopThrough.clone(),null);
                        if(repeatingOrganizerItem.calendarRepeatingTaskItemMap.containsKey(loopThrough)){
                            BLog("Key exists");

                        }else{

                        }

                        BLog("Key size" + (String.valueOf(repeatingOrganizerItem.calendarRepeatingTaskItemMap.keySet().size())));
                        misses +=1;
                    }
                }
                else{
                    //do nothing
                    BLog(new SimpleDateFormat(utilFunctions.dateFormat).format(loopThrough.getTime()) + " Not A Due Date"  );

                }
                loopThrough.add(Calendar.DATE,1);




            }
            repeatingOrganizerItem.setSecondaryText("Hits/Misses = "+ String.valueOf(hits) +"/"+String.valueOf(misses));
        }else{
            repeatingOrganizerItem.setSecondaryText(repeatingTask.getRepeatingTaskDescription());
        }
        return repeatingOrganizerItem;
    }

}
