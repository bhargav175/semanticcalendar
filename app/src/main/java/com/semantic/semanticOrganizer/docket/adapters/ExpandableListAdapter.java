package com.semantic.semanticOrganizer.docket.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.semantic.semanticOrganizer.docket.R;
import com.semantic.semanticOrganizer.docket.models.OrganizerItem;
import com.semantic.semanticOrganizer.docket.models.Tag;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 19-10-2014.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<Tag, List<OrganizerItem>> organizerCollections;
    private List<Tag> tags;

    public ExpandableListAdapter(Activity context, List<Tag> tags,
                                 Map<Tag, List<OrganizerItem>> organizerCollections) {
        this.context = context;
        this.organizerCollections = organizerCollections;
        this.tags = tags;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return organizerCollections.get(tags.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final OrganizerItem organizerItem = (OrganizerItem) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_organizer_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.info_text);
        TextView secondaryText = (TextView) convertView.findViewById(R.id.secondary_text);

        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<OrganizerItem> child =
                                        organizerCollections.get(tags.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Typeface font = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/RobotoCondensed-Light.ttf");

        item.setText(toCamelCase(organizerItem.getItemText()));
        secondaryText.setText(toCamelCase(organizerItem.getItemText()));
        item.setTypeface(font);
        secondaryText.setTypeface(font);

        if (childPosition == getChildrenCount(groupPosition) - 1) {
            convertView.setPadding(0, 0, 0, 20);
        } else
            convertView.setPadding(0, 0, 0, 0);

        return convertView;
    }


    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }

    public int getChildrenCount(int groupPosition) {
        return organizerCollections.get(tags.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return tags.get(groupPosition);
    }

    public int getGroupCount() {
        return tags.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Tag tagName = (Tag) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflaInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaInflater.inflate(R.layout.card_tag,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.info_text);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(toCamelCase(tagName.getTagText()));

        //convertView.setVisibility(View.GONE);



        if (isExpanded){
            convertView.setPadding(0, 0, 0, 0);

        } else{
            convertView.setPadding(0, 0, 0, 20);
        }



        Typeface font = Typeface.createFromAsset(
               context.getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        item.setTypeface(font);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}