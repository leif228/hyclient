package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eyunda.third.activities.ship.ShipDuesActivity;
import com.hy.client.R;

public class ShipDuesAdapter extends BaseAdapter {

	private class buttonViewHolder {
		Button  btnDel,btnPay,refundBtn;//删除按钮
		TextView shipName,combo,money,createTime,startMonth,endMonth;
	}

	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keyString;
	private int[] valueViewID;
	private buttonViewHolder holder;
	private Handler mHandler;//与activity传递消息
	
	public ShipDuesAdapter(Context c,Handler h, ArrayList<Map<String, Object>> appList, int resource,
			String[] from, int[] to) {
		mAppList = appList;
		mContext = c;
		mHandler = h;
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
			convertView = mInflater.inflate(R.layout.eyd_ship_dues_item, null);
			holder = new buttonViewHolder();
			holder.shipName = (TextView) convertView.findViewById(valueViewID[0]);
			holder.combo = (TextView) convertView.findViewById(valueViewID[1]);
			holder.money = (TextView) convertView.findViewById(valueViewID[2]);
			holder.startMonth = (TextView) convertView.findViewById(valueViewID[3]);
			holder.endMonth = (TextView) convertView.findViewById(valueViewID[4]);
			holder.createTime = (TextView) convertView.findViewById(valueViewID[5]);
			holder.refundBtn = (Button) convertView.findViewById(valueViewID[6]);
			holder.btnPay = (Button) convertView.findViewById(valueViewID[7]);
			holder.btnDel = (Button) convertView.findViewById(R.id.btnDel);
			
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			//设置textview显示内容
			holder.shipName.setText((String) appInfo.get(keyString[0]));
			holder.combo.setText((String) appInfo.get(keyString[1]));
			holder.money.setText((String) appInfo.get(keyString[2]));
			holder.startMonth.setText((String) appInfo.get(keyString[3]));
			holder.endMonth.setText((String) appInfo.get(keyString[4]));
			holder.createTime.setText((String) appInfo.get(keyString[5]));
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get(keyString[6])) {
				  holder.refundBtn.setVisibility(View.VISIBLE);
				  holder.refundBtn.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.refundBtn.setVisibility(View.GONE);
			}
			if ((Boolean) appInfo.get(keyString[7])) {
				holder.btnPay.setVisibility(View.VISIBLE);
				holder.btnPay.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnPay.setVisibility(View.GONE);
			}
			
			holder.btnDel.setVisibility(View.VISIBLE);
			holder.btnDel.setOnClickListener(new lvButtonListener(position));
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
			if (vid == holder.btnPay.getId()){
				//Toast.makeText(mContext, "删除-position:"+position+"-orderID:"+curMap.get("orderNum"), Toast.LENGTH_SHORT).show();
				//发出个message到主要的activity
				Message message = new Message();
				message.what = ShipDuesActivity.MSG_PAY;//删除消息标志
				message.arg1 = position;
				message.obj = curMap.get("id");//传递要删除的item对应的记录ID
				mHandler.sendMessage(message);
				//removeItem(position);
			}
			if(vid == holder.refundBtn.getId()){
				Message message = new Message();
				message.what = ShipDuesActivity.MSG_REFUND;//删除消息标志
				message.arg1 = position;
				message.obj = curMap.get("id");//传递要删除的item对应的记录ID
				mHandler.sendMessage(message);
			}
		}
	}
}
