package com.example.admin.toorganize.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.toorganize.R;
import com.example.admin.toorganize.models.EContact;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Admin on 26-09-2014.
 */
public class ContactAutoAdapter extends ArrayAdapter<EContact> {
    private LayoutInflater inflater ;


    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private ArrayList<EContact> items;
    private ArrayList<EContact> itemsAll;
    private ArrayList<EContact> suggestions;
    private int viewResourceId;

    public ContactAutoAdapter(Context context,int parentResourceId ,int viewResourceId, ArrayList<EContact> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<EContact>) items.clone();
        this.suggestions = new ArrayList<EContact>();
        this.viewResourceId = viewResourceId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null){
            view  =  inflater.inflate(R.layout.contact_display_item, null);
        }
        Log.d(MY_DEBUG_TAG,"view inflated");



        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView text2 = (TextView) view.findViewById(R.id.textView);
        EContact eContact = items.get(position);

        if(eContact.getPhoto_uri()!=null){
            Log.d(MY_DEBUG_TAG,eContact.getName()+"'s photo exists");
            imageView.setImageURI(Uri.parse(eContact.getPhoto_uri()));

        }else{
            Log.d(MY_DEBUG_TAG,eContact.getName()+"'s photo does not exists");

            imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.abc_ic_search));
        }
        text2.setText(eContact.getEmail());
        return view;
    }





    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((EContact)resultValue).getEmail();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (EContact eContact : itemsAll) {
                    if(eContact.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(eContact);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<EContact> filteredList = (ArrayList<EContact>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for ( Iterator<EContact> i = filteredList.iterator(); i.hasNext(); ) {
                    add(i.next());
                }
                notifyDataSetChanged();
            }
        }
    };

}
