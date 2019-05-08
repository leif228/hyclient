package com.eyunda.third.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.order.AddOrderTYRActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EydSearchListAdapter extends BaseAdapter {

	private class buttonViewHolder {
		ImageView boat_logo;//船logo
		
		TextView boat_id;//合同内部ID
		TextView boat_name;//船名
		TextView boat_ton;//载货量
		
		TextView boat_dis;//描述
		TextView boat_price;//航线报价
		
		TextView boat_hangxian;//航线
		
		TextView boat_time;//船舶时间
		TextView unitPrice;
		TextView boat_volume;//联系电话

	}

	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keyString;
	private int[] valueViewID;
	private buttonViewHolder holder;

	private ImageLoader mImageLoader;
	public static DisplayImageOptions displayImageOptions =new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zd_icon)    
    .showImageOnFail(R.drawable.img_load_failed)    
    .cacheInMemory(true)    
    .cacheOnDisc(true)    
    .build();    
	
	
	private int mResource;//
	public EydSearchListAdapter(Context c, ArrayList<Map<String, Object>> appList) {
		
		keyString = new String[]{
				"boat_logo",//logo
				"boat_id", 
				"boat_name", //船名
				"boat_ton",  //mmsi
				"boat_dis",  //类别
				"boat_hangxian",//总吨
				"boat_time", //承运人
				"endtime", //时间
				"boat_dt", //时间
				
			
		};
		valueViewID = new int[]{
				R.id.ship_logo, 
				R.id.shipId,   
				R.id.shipName,
				R.id.shipNum,
				R.id.shipTitle,
				R.id.place,
				R.id.unit_price,
				R.id.volume,
				R.id.boat_dt,
				
				
		};
		mImageLoader = ImageLoader.getInstance();
		mAppList = appList;
		mContext = c;
		mResource = R.layout.eyd_home_sail_item;
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
			convertView = mInflater.inflate(mResource, null);
			holder = new buttonViewHolder();
			holder.boat_logo = (ImageView) convertView.findViewById(valueViewID[0]);
			holder.boat_id = (TextView) convertView.findViewById(valueViewID[1]);
			holder.boat_name = (TextView) convertView.findViewById(valueViewID[2]);
			holder.boat_ton = (TextView) convertView.findViewById(valueViewID[3]);
			holder.boat_dis = (TextView) convertView.findViewById(valueViewID[4]);
			holder.boat_price = (TextView) convertView.findViewById(valueViewID[5]);
			holder.boat_hangxian = (TextView) convertView.findViewById(valueViewID[6]);
			holder.boat_time = (TextView) convertView.findViewById(valueViewID[7]);
			holder.boat_volume = (TextView) convertView.findViewById(valueViewID[8]);
			
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			
//			int mid = (Integer) appInfo.get(keyString[0]);//取第一值
//			holder.boat_logo.setImageDrawable(holder.boat_logo.getResources().getDrawable(mid));
			
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get(keyString[0]).toString(), holder.boat_logo,
					displayImageOptions);
			holder.boat_id.setText((String) appInfo.get(keyString[1]));
			holder.boat_name.setText((String) appInfo.get(keyString[2]));
			holder.boat_ton.setText((String) appInfo.get(keyString[3]));
			holder.boat_dis.setText((String) appInfo.get(keyString[4]));
			holder.boat_price.setText((String) appInfo.get(keyString[5]));
			holder.boat_hangxian.setText((String) appInfo.get(keyString[6]));
			holder.boat_time.setText((String) appInfo.get(keyString[7]));
			holder.boat_volume.setText((String) appInfo.get(keyString[8]));
		}
		
		return convertView;
	}

	public void removeItem(int position) {
		mAppList.remove(position);
		this.notifyDataSetChanged();
	}

	class lvButtonListener implements OnClickListener {
		private int position;

		lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			Map<String, Object> curMap = (Map<String, Object>)getItem(position);
			if (vid == holder.unitPrice.getId()){
				Intent intent = new Intent(mContext,ShipPreviewActivity.class);
				intent.putExtra("id", curMap.get("boat_id").toString());
				intent.putExtra("name",curMap.get("boat_name").toString());
				intent.putExtra("phone",curMap.get("boat_phone").toString());
				mContext.startActivity(intent);
			}
		}
	}
}
