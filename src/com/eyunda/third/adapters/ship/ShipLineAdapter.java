package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.Map;

import com.hy.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShipLineAdapter extends BaseAdapter {

	private class buttonViewHolder {
		public TextView shipId,startPort,endPort,startTime,endTime,mmsi,shipArvlftId,
		jobTime,useTime,pathLong,jobDistance;
	}
	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;

	public ShipLineAdapter(Context c, ArrayList<Map<String, Object>> appList) {
		this.mAppList = appList;
		this.mContext = c;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (buttonViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.eyd_ship_line_item, null);
			holder = new buttonViewHolder();
			holder.shipId =(TextView)convertView.findViewById(R.id.shipId);
			holder.shipArvlftId =(TextView)convertView.findViewById(R.id.shipArvlftId);
			holder.mmsi = (TextView) convertView.findViewById(R.id.mmsi);
			holder.startPort = (TextView) convertView.findViewById(R.id.startPort);
			holder.endPort=(TextView) convertView.findViewById(R.id.endPort);
			holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.endTime = (TextView) convertView.findViewById(R.id.endTime);
			holder.useTime = (TextView) convertView.findViewById(R.id.useTime);
			holder.pathLong = (TextView) convertView.findViewById(R.id.pathLong);
			holder.jobTime = (TextView) convertView.findViewById(R.id.jobTime);
			holder.jobDistance = (TextView) convertView.findViewById(R.id.jobDistance);

			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			holder.shipId.setText((String) appInfo.get("shipId"));
			holder.shipArvlftId.setText(((Long) appInfo.get("shipArvlftId")).toString());
			holder.mmsi.setText((String) appInfo.get("mmsi"));
			holder.startPort.setText((String) appInfo.get("startPort")+" 到 "+(String) appInfo.get("endPort"));
			holder.endPort.setText((String) appInfo.get("endPort"));
			holder.startTime.setText((String) appInfo.get("startTime")+" 到 "+(String) appInfo.get("endTime"));
			holder.endTime.setText((String) appInfo.get("endTime"));
			holder.useTime.setText((String) appInfo.get("useTime"));
			holder.pathLong.setText((String) appInfo.get("pathLong"));
			holder.jobTime.setText((String) appInfo.get("jobTime"));
			holder.jobDistance.setText((String) appInfo.get("jobDistance"));
		}
		
		return convertView;
		
	}

    public void addItem(Map<String, Object> item) {    
        mAppList.add(item);    
    }
}
