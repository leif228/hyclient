package com.eyunda.third.adapters.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.third.activities.home.HomeCategoryActivity;
import com.eyunda.third.activities.home.HomeDynamicActivity;
import com.eyunda.third.activities.home.HomeSearchResultActivity;
import com.hy.client.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeGridViewAdapter extends BaseAdapter{
	private class gridViewHolder {
		TextView showText;//显示的文字
	}
	private Context mContext;
	private ArrayList<HashMap<String, Object>> mAppList;
	private LayoutInflater mInflater;

	int type;
	private gridViewHolder holder;
	public HomeGridViewAdapter(Context mContext,
			ArrayList<HashMap<String, Object>> setGridViewData, int type) {
		this.mContext=mContext;
		this.mAppList=setGridViewData;
		this.type =type;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.eyd_channel_gridview_item, null);
			holder = new gridViewHolder();
			holder.showText = (TextView) convertView.findViewById(R.id.channel_gridview_item);
			holder.showText.setOnClickListener(new lvButtonListener(position));
			convertView.setTag(holder);
		} else {
			holder = (gridViewHolder) convertView.getTag();

		}
		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			holder.showText.setText((String) appInfo.get("title"));
			holder.showText.setTag((String) appInfo.get("code"));
		}

		return convertView;
	}
	class lvButtonListener implements OnClickListener {
		private int position;

		lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			Map<String, Object> curMap = (Map<String, Object>)getItem(position);
			if(type==HomeCategoryActivity.MSG_DYNAMIC_LIST || type==HomeCategoryActivity.MSG_SHIP_LIST){
				Intent intent = new Intent(mContext,HomeDynamicActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("param", curMap.get("code").toString());
				bundle.putString("title", curMap.get("title").toString());
				bundle.putInt("type", type);
				intent.putExtras(bundle);
				mContext.startActivity(intent);

			}else{
				Intent intent = new Intent(mContext,HomeSearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("param", curMap.get("code").toString());
				bundle.putString("title", curMap.get("title").toString());
				bundle.putLong("type", type);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
				// Toast.makeText(mContext, curMap.toString(), 3000).show();
			}
		}
	}
}
