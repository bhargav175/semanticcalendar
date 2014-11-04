package com.semantic.semanticOrganizer.docket.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.models.Habit;
import com.semantic.semanticOrganizer.docket.models.Reminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 12-10-2014.
 */
public class ReminderHelper {

        private Context context;
        private Boolean hadReminder, hasReminder;
        private Reminder currentReminder;
        private Integer reminderId;
        private Calendar dueDate;


}
