package com.eyunda.third.adapters.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.order.AddOrderTKActivity;
import com.eyunda.third.activities.order.AddOrderTYRActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.pay.PaymentActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.TAApplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

public class EydOrderListAdapter extends BaseAdapter {

	private class buttonViewHolder {
		ImageView boatLogo;// 船logo

		TextView orderId;// 合同内部ID
		TextView orderNum;// 合同编号
		TextView orderCount;// 合同金额

		TextView daLiRen;// 代理人
		TextView orderTime;// 合同时间

		TextView hangXian;// 航线

		TextView chengYunRen;// 承运人
		TextView tuoYunRen;// 托运人

		TextView orderStatic;// 合同状态

		Button btnEdit;// 编辑按钮
		Button btnSign;// 签名
		Button btnPay;// 支付
		Button btnDel;// 删除按钮
		Button btnMonitor;// 监控按钮
		Button btnRefund;// 退款按钮
		TextView shipName;// 船名

		public TextView shipId;

		public TextView orderCargoId;

		public TextView account;
		public TextView cargoSize;
	}

	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keyString;
	private int[] valueViewID;
	private buttonViewHolder holder;
	private static DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zd_icon)
			.showImageForEmptyUri(R.drawable.zd_icon)
			.showImageOnFail(R.drawable.zd_icon).cacheInMemory(true)
			.cacheOnDisc(true).delayBeforeLoading(1000)
			.bitmapConfig(Bitmap.Config.RGB_565) // default
			.build();
	ImageLoader im;
	private Handler mHandler;// 与activity传递消息

	public EydOrderListAdapter(Context c, Handler handle,
			ArrayList<Map<String, Object>> appList) {
		mAppList = appList;
		mContext = c;
		mHandler = handle;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keyString = new String[] { "boatLogo", "orderId", "orderNum",
				"orderCount", "daLiRen", "orderTime", "hangXian",
				"chengYunRen", "tuoYunRen", "orderStatic", "btnEdit",
				"btnSign", "btnSignText", "btnPay", "btnDel", "btnPayText",
				"btnPayAction", "shipName","pdfFileName","btnMonitor","shipId","cargoSize","btnRefund"};
		valueViewID = new int[] { R.id.boatLogo, R.id.orderId, R.id.orderNum,
				R.id.orderCount, R.id.daLiRen, R.id.orderTime, R.id.hangXian,
				R.id.chenYunRen, R.id.tuoYunRen, R.id.orderStatic,
				R.id.btnEdit, R.id.btnSign, R.id.btnPay, R.id.btnDel,R.id.btnMonitor, 
				R.id.shipName,R.id.shipId,R.id.cargoSize,R.id.btnRefund};

		im = ImageLoader.getInstance();
		im.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
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
			convertView = mInflater.inflate(R.layout.eyd_boat_item, null);
			holder = new buttonViewHolder();
			holder.boatLogo = (ImageView) convertView
					.findViewById(valueViewID[0]);

			holder.orderId = (TextView) convertView
					.findViewById(valueViewID[1]);
			holder.orderNum = (TextView) convertView
					.findViewById(valueViewID[2]);
			holder.orderCount = (TextView) convertView
					.findViewById(valueViewID[3]);
			holder.daLiRen = (TextView) convertView
					.findViewById(valueViewID[4]);
			holder.orderTime = (TextView) convertView
					.findViewById(valueViewID[5]);
			holder.hangXian = (TextView) convertView
					.findViewById(valueViewID[6]);
			holder.chengYunRen = (TextView) convertView
					.findViewById(valueViewID[7]);
			holder.tuoYunRen = (TextView) convertView
					.findViewById(valueViewID[8]);
			holder.orderStatic = (TextView) convertView
					.findViewById(valueViewID[9]);
			holder.btnEdit = (Button) convertView.findViewById(valueViewID[10]);
			holder.btnSign = (Button) convertView.findViewById(valueViewID[11]);
			holder.btnPay = (Button) convertView.findViewById(valueViewID[12]);
			holder.btnDel = (Button) convertView.findViewById(valueViewID[13]);
			holder.btnMonitor = (Button) convertView.findViewById(valueViewID[14]);
			holder.shipName = (TextView) convertView.findViewById(valueViewID[15]);
			holder.shipId = (TextView) convertView.findViewById(valueViewID[16]);
			holder.cargoSize = (TextView) convertView.findViewById(valueViewID[17]);
			holder.btnRefund = (Button) convertView.findViewById(valueViewID[18]);
			
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {

			im.displayImage(
					ApplicationConstants.IMAGE_URL
							+ appInfo.get(keyString[0]).toString(),
					holder.boatLogo, MyshipAdapter.displayImageOptions);
			holder.orderId.setText((String) appInfo.get(keyString[1]));
			holder.orderNum.setText((String) appInfo.get(keyString[2]));
			holder.orderCount.setText((String) appInfo.get(keyString[3]));
			holder.daLiRen.setText((String) appInfo.get(keyString[4]));
			holder.orderTime.setText((String) appInfo.get(keyString[5]));
			holder.hangXian.setText((String) appInfo.get(keyString[6]));
			holder.chengYunRen.setText((String) appInfo.get(keyString[7]));
			holder.tuoYunRen.setText((String) appInfo.get(keyString[8]));
			holder.orderStatic.setText((String) appInfo.get(keyString[9]));

			// 控制按钮显示隐藏 编辑按钮
			if ((Boolean) appInfo.get(keyString[10])) {
				holder.btnEdit.setVisibility(View.VISIBLE);
				holder.btnEdit
						.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnEdit.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get(keyString[11])) {
				holder.btnSign.setVisibility(View.VISIBLE);
				holder.btnSign.setText((String) appInfo.get(keyString[12]));
				holder.btnSign
						.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnSign.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get(keyString[13])) {
				holder.btnPay.setVisibility(View.VISIBLE);
				holder.btnPay
						.setOnClickListener(new lvButtonListener(position));
				holder.btnPay.setText((String) appInfo.get(keyString[15]));
				holder.btnPay.setTag((String) appInfo.get(keyString[16]));
			} else {
				holder.btnPay.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get(keyString[14])) {
				holder.btnDel.setVisibility(View.VISIBLE);
				holder.btnDel
						.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnDel.setVisibility(View.GONE);
			}
			
			holder.shipName.setText((String) appInfo.get(keyString[17]));
			if((Boolean) appInfo.get("btnMonitor")){
				holder.btnMonitor.setVisibility(View.VISIBLE);
				holder.btnMonitor
				.setOnClickListener(new lvButtonListener(position));
			}else{
				holder.btnMonitor.setVisibility(View.GONE);
			}
			holder.cargoSize.setText((String) appInfo.get(keyString[21]));
			if ((Boolean) appInfo.get("btnRefund")) {
				holder.btnRefund.setVisibility(View.VISIBLE);
				holder.btnRefund
						.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnRefund.setVisibility(View.GONE);
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
			Map<String, Object> curMap = (Map<String, Object>) getItem(position);
			Message message = new Message();
			message.arg1 = position;
			message.obj = curMap;// 传递要修改的item对应的记录ID
			if (vid == holder.btnEdit.getId()) {// 编辑

				message.what = MyOrderActivity.MSG_Edit_Order;// 编辑消息标志

			}
			if (vid == holder.btnSign.getId()) {// 签名
				message.what = MyOrderActivity.MSG_Sign_Order;// 删除消息标志
			}
			if (vid == holder.btnPay.getId()) {// 支付和评论
				// 根据Tag区别
				String tag = (String) curMap.get("btnPayAction");
				if (tag != null) {
					if (tag.equalsIgnoreCase("payment")) {
						message.what = MyOrderActivity.MSG_Pay_Order_payment;
					}
					if (tag.equalsIgnoreCase("confirmpay")) {
						message.what = MyOrderActivity.MSG_Pay_Order_confirmpay;
					}
					if (tag.equalsIgnoreCase("refunapply")) {
						message.what = MyOrderActivity.MSG_Pay_Order_refunapply;
					}
					if (tag.equalsIgnoreCase("refund")) {
						message.what = MyOrderActivity.MSG_Pay_Order_refund;
					}
					if (tag.equalsIgnoreCase("approval")) {
						message.what = MyOrderActivity.MSG_Comment_Order;
					}
					
				}

				// Toast.makeText(mContext,
				// holder.btnPay.getText()+"-position:"+position+"-orderID:"+curMap.get("orderNum"),
				// Toast.LENGTH_SHORT).show();
			}
			if (vid == holder.btnDel.getId()) {// 删除
				message.what = MyOrderActivity.MSG_Del_Order;// 删除消息标志

			}
			if (vid == holder.btnMonitor.getId()) {// 监控
				message.what = MyOrderActivity.MSG_Monitor;// 

			}
			if (vid == holder.btnRefund.getId()) {// 退款
				message.what = MyOrderActivity.MSG_Pay_Order_refund;// 删除消息标志

			}
			mHandler.sendMessage(message);
			// removeItem(position);
		}
	}
}
