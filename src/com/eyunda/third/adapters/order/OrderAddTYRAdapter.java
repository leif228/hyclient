package com.eyunda.third.adapters.order;

import com.hy.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class OrderAddTYRAdapter extends ArrayAdapter<String> implements Filterable {
    private int mLayoutResourceID;
    
    public OrderAddTYRAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutResourceID = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutResourceID, parent, false);
        }

        String item = getItem(position);
        ((TextView) (convertView.findViewById(R.id.userName))).setText(item);
        
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) filterResults.count = getCount();
            // 如果需要的话，在这里实现filter逻辑
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            }
        }
    };
    
}

