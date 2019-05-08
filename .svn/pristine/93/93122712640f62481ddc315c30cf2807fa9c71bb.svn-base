package com.eyunda.third.adapters.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.home.HomeCategoryActivity;
import com.eyunda.third.activities.order.AddOrderTYRActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

public class EydCargoSearchListAdapter extends BaseAdapter {

	private class buttonViewHolder {
		ImageView boat_logo;//托运人头像
		
		TextView boat_id;//货物ID
		TextView boat_name;//货名
		TextView boat_ton;//货重
		
		TextView boat_dis;//货类
		TextView boat_price;//总价格
		
		TextView boat_hangxian;//航线
		
		TextView boat_time;//截止时间
		TextView btnLeft;
		TextView boat_phone;//截止时间

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
    .showImageOnFail(R.drawable.zd_icon)    
    .cacheInMemory(true)    
    .cacheOnDisc(true)    
    .build();    
	private int mResource;//
	public EydCargoSearchListAdapter(Context c, ArrayList<Map<String, Object>> appList) {
		mImageLoader = ImageLoader.getInstance();
		keyString = new String[]{
				"boat_logo",//托运人头像
				"list_cargoName",//货名
				"boat_phone",// 电话
				"list_shipper",//货主
				"list_cargoCode",//货类
				"unit_price"   , //单价
				"boat_price",//总价格
				"boat_time",//截止时间
				"list_volume",//数量
				"list_place",//起止港
				"type",
		};
		valueViewID = new int[]{
				R.id.boat_logo,
				R.id.list_cargoName,
				R.id.list_phone,
				R.id.list_shipper,
				R.id.list_cargoCode,
				R.id.unit_price,
				R.id.total_price,
				R.id.list_endtime,
				R.id.list_volume,
				R.id.list_place
				
		};
		mAppList = appList;
		mContext = c;
		mResource = R.layout.eyd_home_cargo_item;
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
			holder.btnLeft = (TextView) convertView.findViewById(valueViewID[8]);
			holder.boat_phone = (TextView) convertView.findViewById(valueViewID[9]);
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			
//			int mid = (Integer) appInfo.get(keyString[0]);//取第一值
//			holder.boat_logo.setImageDrawable(holder.boat_logo.getResources().getDrawable(mid));
			if(appInfo.get(keyString[0]).equals("")){
				mImageLoader.displayImage(ApplicationConstants.SERVER_URL+ "/img/cargoImage/" +appInfo.get(keyString[10])+".jpg", 
						holder.boat_logo,MyshipAdapter.displayImageOptions);
			}else
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get(keyString[0]).toString(), holder.boat_logo,
					displayImageOptions);
			holder.boat_id.setText((String) appInfo.get(keyString[1]));
			holder.boat_name.setText((String) appInfo.get(keyString[2]));
			holder.boat_ton.setText((String) appInfo.get(keyString[3]));
			holder.boat_dis.setText((String) appInfo.get(keyString[4]));
			holder.boat_price.setText((String) appInfo.get(keyString[5]));
			holder.boat_hangxian.setText((String) appInfo.get(keyString[6]));
			holder.boat_time.setText((String) appInfo.get(keyString[7]));
			holder.btnLeft.setText((String) appInfo.get(keyString[8]));
			holder.boat_phone.setText((String) appInfo.get(keyString[9]));
			
			
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
//			Map<String, Object> curMap = (Map<String, Object>)getItem(position);
//			Intent intent = new Intent(mContext,ShipPreviewActivity.class);
//			intent.putExtra("id", curMap.get("boat_id").toString());
//			intent.putExtra("type", HomeCategoryActivity.MSG_CARGRO_CATE_LIST);
//			intent.putExtra("name",curMap.get("boat_name").toString());
//			intent.putExtra("phone",curMap.get("boat_phone").toString());
//			mContext.startActivity(intent);
//			if (vid == holder.btnLeft.getId()){
//				Intent intent = new Intent(mContext,ShipPreviewActivity.class);
//				intent.putExtra("id", curMap.get("boat_id").toString());
//				intent.putExtra("type", HomeCategoryActivity.MSG_CARGRO_CATE_LIST);
//				intent.putExtra("name",curMap.get("boat_name").toString());
//				intent.putExtra("phone",curMap.get("boat_phone").toString());
//				mContext.startActivity(intent);
//				Toast.makeText(mContext, "跳转货物详情页面", Toast.LENGTH_SHORT).show();
//			}
		}
	}
}
