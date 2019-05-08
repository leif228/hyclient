package com.eyunda.third.adapters.ship;

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
import com.eyunda.third.activities.ship.ShipGasOrderActivity;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShipGasOrderAdapter extends BaseAdapter {

	private class buttonViewHolder {
		
		Button  btnDel,btnRefund,btnPay;//删除按钮
		TextView  shipName , companyName , waresName , saleCount , price , tradeMoney ,
		 orderTime , stationName , gasTime , status ,id ;
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
	
	public ShipGasOrderAdapter(Context c,Handler h, ArrayList<Map<String, Object>> appList) {
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
			convertView = mInflater.inflate(R.layout.eyd_ship_gasorder_item, null);
			holder = new buttonViewHolder();
			holder.shipName = (TextView) convertView.findViewById(R.id.shipName);
			holder.companyName = (TextView) convertView.findViewById(R.id.companyName);
			holder.waresName = (TextView) convertView.findViewById(R.id.waresName);
			holder.saleCount = (TextView) convertView.findViewById(R.id.saleCount);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.tradeMoney = (TextView) convertView.findViewById(R.id.tradeMoney);
			holder.orderTime = (TextView) convertView.findViewById(R.id.orderTime);
			holder.status = (TextView) convertView.findViewById(R.id.status);
			holder.btnRefund = (Button) convertView.findViewById(R.id.btnRefund);
			holder.btnPay = (Button) convertView.findViewById(R.id.btnPay);
			holder.btnDel = (Button) convertView.findViewById(R.id.btnDel);
			holder.id = (TextView) convertView.findViewById(R.id.id);
			holder.logo = (ImageView) convertView.findViewById(R.id.logo);
			
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {

			//设置textview显示内容
//			holder.shipName.setText((String) appInfo.get("shipName"));
			holder.companyName.setText((String) appInfo.get("companyName"));
			holder.waresName.setText((String) appInfo.get("waresName"));
			holder.saleCount.setText((String) appInfo.get("saleCount"));
			holder.price.setText((String) appInfo.get("price"));
			holder.tradeMoney.setText((String) appInfo.get("tradeMoney"));
			holder.orderTime.setText((String) appInfo.get("orderTime"));
			holder.status.setText((String) appInfo.get("status"));
			holder.id.setText((String) appInfo.get("id"));
			
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("logo").toString(), 
					holder.logo,displayImageOptions);
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnRefund")) {
				  holder.btnRefund.setVisibility(View.VISIBLE);
				  holder.btnRefund.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnRefund.setVisibility(View.GONE);
			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnPay")) {
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
				message.what = ShipGasOrderActivity.MSG_Del_One_Item;//删除消息标志
				message.arg1 = position;
				message.obj = curMap.get("wid");//传递要删除的item对应的记录ID
				mHandler.sendMessage(message);
				//removeItem(position);
			}
			if(vid == holder.btnRefund.getId()){
				Message message = new Message();
				message.what = ShipGasOrderActivity.MSG_REFUND;//删除消息标志
				message.arg1 = position;
				message.obj = curMap.get("wid");//传递要删除的item对应的记录ID
				mHandler.sendMessage(message);
			}
			if(vid == holder.btnPay.getId()){
				Message message = new Message();
				message.what = ShipGasOrderActivity.MSG_PAY;//删除消息标志
				message.arg1 = position;
				message.obj = curMap.get("wid");//传递要删除的item对应的记录ID
				mHandler.sendMessage(message);
			}
		}
	}
}
