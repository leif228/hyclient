package com.eyunda.third.adapters.user;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.ShipDuesActivity;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserAgentAdapter extends BaseAdapter {

	private class buttonViewHolder {
		Button  btnDel;//删除按钮
		TextView type,loginName,trueName,mobile,unitName;
		ImageView logo;
	}

	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;
	private Handler mHandler;//与activity传递消息
	
	private ImageLoader mImageLoader;
	public static DisplayImageOptions displayImageOptions =new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.eyd_chat_ic_cycle) 
	.showImageForEmptyUri(R.drawable.eyd_chat_search_clear_pressed)
    .showImageOnFail(R.drawable.img_load_failed)  
    .bitmapConfig(Bitmap.Config.RGB_565)
    .cacheInMemory(true)    
    .cacheOnDisc(true)      //缓存到sd卡
    .build(); 
	
	public UserAgentAdapter(Context c,Handler h, ArrayList<Map<String, Object>> appList) {
		mAppList = appList;
		mContext = c;
		mHandler = h;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader = ImageLoader.getInstance();
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
			convertView = mInflater.inflate(R.layout.eyd_useragent_item, null);
			holder = new buttonViewHolder();
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.loginName = (TextView) convertView.findViewById(R.id.loginName);
			holder.trueName = (TextView) convertView.findViewById(R.id.trueName);
			holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
			holder.unitName = (TextView) convertView.findViewById(R.id.unitName);
			holder.logo = (ImageView) convertView.findViewById(R.id.logo);
			holder.btnDel = (Button) convertView.findViewById(R.id.btnDel);
			
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			//设置textview显示内容
			holder.type.setText((String) appInfo.get("roleCode"));
			holder.loginName.setText((String) appInfo.get("loginName"));
			holder.trueName.setText((String) appInfo.get("trueName"));
			holder.mobile.setText((String) appInfo.get("mobile"));
			holder.unitName.setText((String) appInfo.get("unitName"));
			
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("userLogo").toString(), 
					holder.logo,displayImageOptions);
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnDel")) {
				  holder.btnDel.setVisibility(View.VISIBLE);
				  holder.btnDel.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnDel.setVisibility(View.GONE);
			}
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

			if (vid == holder.btnDel.getId()){
				//Toast.makeText(mContext, "删除-position:"+position+"-orderID:"+curMap.get("orderNum"), Toast.LENGTH_SHORT).show();
				//发出个message到主要的activity
				Message message = new Message();
				message.what = ShipDuesActivity.MSG_Del_One_Item;//删除消息标志
				message.arg1 = position;
				message.obj = curMap.get("id");//传递要删除的item对应的记录ID
				mHandler.sendMessage(message);
				//removeItem(position);
			}
		}
	}
}
