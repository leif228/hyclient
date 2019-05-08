package com.eyunda.third.adapters.order;

import java.util.List;






import com.eyunda.third.domain.order.TemplateData;
import com.hy.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderTemplateListAdapter extends BaseAdapter {

	
	private List<TemplateData> mList;
	private Context mContext;

	
	public OrderTemplateListAdapter(Context c,List<TemplateData> l) {
		this.mList = l;
		this.mContext = c;
		
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.eyd_order_template_item, null);
        if(convertView!=null)
        {
        	TextView orderTempId=(TextView)convertView.findViewById(R.id.orderTempId);
            TextView orderTempName=(TextView)convertView.findViewById(R.id.orderTempName);
            TextView orderTempUse=(TextView)convertView.findViewById(R.id.orderTempUse);
            orderTempId.setText(mList.get(position).getId().toString());
            orderTempName.setText(mList.get(position).getTitle());
            orderTempUse.setText(mList.get(position).getTitle());
        }
        return convertView;
	}

}
