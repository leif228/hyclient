package com.eyunda.third.adapters.account;

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

import com.eyunda.third.activities.account.WalletHomeActivity;
import com.hy.client.R;

public class WalletHomeAdapter extends BaseAdapter {

	private class buttonViewHolder {
		TextView settleNo, settleStyle,feeItem, inout, paymentStatus, refundStatus, totalFee,body,gmtPayment;
		Button btnStartSurety, btnEndSurety, btnDirectRefund, btnApplyRefund, btnRefund,btnDel;
	}

	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;
	private Handler mHandler;//与activity传递消息
	
	public WalletHomeAdapter(Context c,Handler h, ArrayList<Map<String, Object>> appList, int resource) {
		mAppList = appList;
		mContext = c;
		mHandler = h;
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
			convertView = mInflater.inflate(R.layout.eyd_wallet_home_item, null);
			holder = new buttonViewHolder();
			holder.settleNo = (TextView) convertView.findViewById(R.id.settleNo);
			holder.settleStyle = (TextView) convertView.findViewById(R.id.settleStyle);
			holder.totalFee = (TextView) convertView.findViewById(R.id.totalFee);
			holder.body = (TextView) convertView.findViewById(R.id.body);
			holder.gmtPayment = (TextView) convertView.findViewById(R.id.gmtPayment);
			holder.feeItem = (TextView) convertView.findViewById(R.id.feeItem);
			holder.inout = (TextView) convertView.findViewById(R.id.inout);
			holder.paymentStatus = (TextView) convertView.findViewById(R.id.paymentStatus);
			holder.refundStatus = (TextView) convertView.findViewById(R.id.refundStatus);


			
			holder.btnStartSurety = (Button) convertView.findViewById(R.id.btnStartSurety);//支付
			holder.btnEndSurety = (Button) convertView.findViewById(R.id.btnEndSurety);//确认付款
			holder.btnDirectRefund = (Button) convertView.findViewById(R.id.btnDirectRefund);
			holder.btnApplyRefund = (Button) convertView.findViewById(R.id.btnApplyRefund);
			holder.btnRefund = (Button) convertView.findViewById(R.id.btnRefund);
			holder.btnDel = (Button) convertView.findViewById(R.id.btnDel);
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			//设置textview显示内容
			holder.settleNo.setText((String) appInfo.get("settleNo"));
			holder.settleStyle.setText((String) appInfo.get("settleStyle"));
			holder.totalFee.setText((String) appInfo.get("totalFee"));
			holder.body.setText((String) appInfo.get("body"));
			holder.gmtPayment.setText((String) appInfo.get("gmtPayment"));
			holder.feeItem.setText((String) appInfo.get("feeItem"));
			holder.inout.setText((String) appInfo.get("inout"));
			holder.paymentStatus.setText((String) appInfo.get("paymentStatus"));
			holder.refundStatus.setText((String) appInfo.get("refundStatus"));
			
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnStartSurety")) {
				  holder.btnStartSurety.setVisibility(View.VISIBLE);
				  holder.btnStartSurety.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnStartSurety.setVisibility(View.GONE);
			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnEndSurety")) {
				holder.btnEndSurety.setVisibility(View.VISIBLE);
				holder.btnEndSurety.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnEndSurety.setVisibility(View.GONE);
			}
//			//控制按钮显示隐藏
//			if ((Boolean) appInfo.get("btnDirectRefund")) {
//				holder.btnDirectRefund.setVisibility(View.VISIBLE);
//				holder.btnDirectRefund.setOnClickListener(new lvButtonListener(position));
//			} else {
//				holder.btnDirectRefund.setVisibility(View.GONE);
//			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnApplyRefund")) {
				holder.btnApplyRefund.setVisibility(View.VISIBLE);
				holder.btnApplyRefund.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnApplyRefund.setVisibility(View.GONE);
			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnRefund")) {
				holder.btnRefund.setVisibility(View.VISIBLE);
				holder.btnRefund.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnRefund.setVisibility(View.GONE);
			}
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
			if(vid == holder.btnStartSurety.getId()){
				Message message = new Message();
				message.what = WalletHomeActivity.MSG_PAY;
				message.arg1 = position;
				message.obj = curMap.get("id");
				mHandler.sendMessage(message);
			}
			if(vid == holder.btnEndSurety.getId()){
				Message message = new Message();
				message.what = WalletHomeActivity.MSG_CONFIRMPAY;
				message.arg1 = position;
				message.obj = curMap.get("id");
				mHandler.sendMessage(message);
			}
			if(vid == holder.btnDirectRefund.getId()){
				Message message = new Message();
				message.what = WalletHomeActivity.MSG_DIRECTREFUND;
				message.arg1 = position;
				message.obj = curMap.get("id");
				mHandler.sendMessage(message);
			}
			if(vid == holder.btnApplyRefund.getId()){
				Message message = new Message();
				message.what = WalletHomeActivity.MSG_APPLYREFUND;
				message.arg1 = position;
				message.obj = curMap.get("id");
				mHandler.sendMessage(message);
			}
			if(vid == holder.btnRefund.getId()){
				Message message = new Message();
				message.what = WalletHomeActivity.MSG_REFUND;
				message.arg1 = position;
				message.obj = curMap.get("id");
				mHandler.sendMessage(message);
			}
			if(vid == holder.btnDel.getId()){
				Message message = new Message();
				message.what = WalletHomeActivity.MSG_Del_One_Item;
				message.arg1 = position;
				message.obj = curMap.get("id");
				mHandler.sendMessage(message);
			}
		}
	}
}
