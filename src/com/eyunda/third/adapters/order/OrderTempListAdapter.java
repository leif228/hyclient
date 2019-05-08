package com.eyunda.third.adapters.order;

import java.util.List;
import java.util.Map;

import com.eyunda.third.activities.order.AddOrderTKActivity;
import com.hy.client.R;

import android.content.Context;
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

public class OrderTempListAdapter extends BaseAdapter {

	private class buttonViewHolder {

		
		TextView orderId;//合同内部ID
		TextView tempId;//采用的模板类型ID
		TextView tempDetailId;//单条类
		TextView tempName;//模板名

		Button btnEdit;//修改按钮
		ImageView btnDel;//删除按钮
		public TextView num;
	}

	private List<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;

	private String[] keyString;
	private int[] valueViewID;
	private buttonViewHolder holder;
	private Handler mHandler;//与activity传递消息

	public OrderTempListAdapter(Context c,Handler handle, List<Map<String, Object>> appList, int resource,
			String[] from, int[] to) {
		mAppList = appList;
		mContext = c;
		mHandler = handle;
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
			convertView = mInflater.inflate(R.layout.eyd_order_temp_item, null);
			holder = new buttonViewHolder();
			holder.num = (TextView) convertView.findViewById(valueViewID[0]);
			holder.orderId = (TextView) convertView.findViewById(valueViewID[1]);
			holder.tempId = (TextView) convertView.findViewById(valueViewID[2]);
			holder.tempDetailId = (TextView) convertView.findViewById(valueViewID[3]);
			holder.tempName = (TextView) convertView.findViewById(valueViewID[4]);
			holder.btnEdit = (Button) convertView.findViewById(valueViewID[5]);
			holder.btnDel = (ImageView) convertView.findViewById(valueViewID[6]);
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			holder.num.setText((String) appInfo.get(keyString[0]));
			holder.orderId.setText((String) appInfo.get(keyString[1]));
			holder.tempId.setText((String) appInfo.get(keyString[2]));
			holder.tempDetailId.setText((String) appInfo.get(keyString[3]));
			holder.tempName.setText((String) appInfo.get(keyString[4]));

			//控制按钮显示隐藏
			if ((Boolean) appInfo.get(keyString[5])) {
				holder.btnEdit.setVisibility(View.VISIBLE);
				holder.btnEdit.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnEdit.setVisibility(View.GONE);
			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get(keyString[6])) {
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
			Message message = new Message();
			if (vid == holder.btnEdit.getId()){
				//Toast.makeText(mContext, "-position:"+position+"-orderID:"+curMap.get("tempDetailId"), Toast.LENGTH_SHORT).show();
				//message.what = AddOrderTKActivity.MSG_Edit_One_Item;//删除消息标志
			}
			if (vid == holder.btnDel.getId()){
				//Toast.makeText(mContext, "删除-position:"+position+"-orderID:"+curMap.get("oId"), Toast.LENGTH_SHORT).show();
				//发出个message到主要的activity
			//	message.what = AddOrderTKActivity.MSG_Del_One_Item;//删除消息标志
				//removeItem(position);
			}
			message.arg1 = position;
			message.obj = curMap;//传递要修改的item对应的记录ID
			mHandler.sendMessage(message);
		}
	}
}
