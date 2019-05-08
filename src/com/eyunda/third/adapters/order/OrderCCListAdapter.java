package com.eyunda.third.adapters.order;

import java.util.List;
import java.util.Map;

import com.eyunda.third.activities.order.AddOrderGSHActivity;
import com.hy.client.R;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Toast;
/**
 * 运输工具列表
 * @author guoqiang
 *
 */
public class OrderCCListAdapter extends BaseAdapter {

	private int selectedPosition = -1;// 选中的位置
	
	private class buttonViewHolder {

		TextView boat_name;//船名称
		TextView boat_id;//船的ID
		TextView boat_ton;//载货量
		TextView boat_dis;//船舶描述
		TextView boat_hangxian;//航线
		TextView boat_price;//报价
		TextView boat_time;//截止时间
		ImageView boat_favor;//详情按钮
	}

	private List<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keyString;
	private int[] valueViewID;
	private buttonViewHolder holder;

	public OrderCCListAdapter(Context c, List<Map<String, Object>> appList, int resource,
			String[] from, int[] to) {
		mAppList = appList;
		mContext = c;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keyString = new String[from.length];
		valueViewID = new int[to.length];
		System.arraycopy(from, 0, keyString, 0, from.length);
		System.arraycopy(to, 0, valueViewID, 0, to.length);
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
			convertView = mInflater.inflate(R.layout.eyd_home_one_item, null);
			holder = new buttonViewHolder();
	
			holder.boat_name = (TextView) convertView.findViewById(valueViewID[0]);
			holder.boat_id = (TextView) convertView.findViewById(valueViewID[1]);
			holder.boat_ton = (TextView) convertView.findViewById(valueViewID[2]);
			holder.boat_dis = (TextView) convertView.findViewById(valueViewID[3]);
			holder.boat_hangxian = (TextView) convertView.findViewById(valueViewID[4]);
			holder.boat_price = (TextView) convertView.findViewById(valueViewID[5]);
			holder.boat_time = (TextView) convertView.findViewById(valueViewID[6]);
			holder.boat_favor = (ImageView) convertView.findViewById(valueViewID[7]);
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			holder.boat_name.setText((String) appInfo.get(keyString[0]));
			holder.boat_id.setText((String) appInfo.get(keyString[1]));
			holder.boat_ton.setText((String) appInfo.get(keyString[2]));
			holder.boat_dis.setText((String) appInfo.get(keyString[3]));
			holder.boat_hangxian.setText((String) appInfo.get(keyString[4]));
			holder.boat_price.setText((String) appInfo.get(keyString[5]));
			holder.boat_time.setText((String) appInfo.get(keyString[6]));

			holder.boat_favor.setOnClickListener(new lvButtonListener(position));


		}
	
//
//		convertView.setBackgroundColor(Color.WHITE);
//        if(position == curPosition){
//        	convertView.setBackgroundColor(Color.BLACK);
//        }

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
			if (vid == holder.boat_favor.getId()){
				Toast.makeText(mContext, "导向船舶详情-position:"+position+"-orderID:"+curMap.get("boat_id"), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
