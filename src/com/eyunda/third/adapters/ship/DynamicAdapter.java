package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.Map;

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

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.ArvLftListActivity;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DynamicAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Map<String, Object>> dataList;
	private LayoutInflater mInflater;
	private DtViewHolder holder;
	public static String shipId;
	public static String carrierName;
	public static String mmsi;
	public static String shipName;
	private Data_loader data = new Data_loader();
	private ImageLoader mImageLoader;

	public DynamicAdapter(Context context, ArrayList<Map<String, Object>> data) {
		this.mContext = context;
		this.dataList = data;
		mInflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (DtViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.eyd_dynamic_item, null);
			holder = new DtViewHolder();
			holder.shipLogo = (ImageView) convertView.findViewById(R.id.shipLogo);
			holder.shipName = (TextView) convertView.findViewById(R.id.shipName);
			holder.portCity = (TextView) convertView.findViewById(R.id.portCity);
			holder.nextPort = (TextView) convertView.findViewById(R.id.nextPort);
			holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.endTime = (TextView) convertView.findViewById(R.id.endTime);

			holder.up = (TextView) convertView.findViewById(R.id.up);
			holder.down = (TextView) convertView.findViewById(R.id.down);
			holder.remark = (TextView) convertView.findViewById(R.id.remark);
			holder.report = (Button) convertView.findViewById(R.id.btnEdit);
			convertView.setTag(holder);
		}
		Map<String, Object> appInfo = dataList.get(position);
		mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + appInfo.get("shipLogo").toString(), holder.shipLogo,
				MyshipAdapter.displayImageOptions);
		holder.shipName.setText((String) appInfo.get("shipName"));
		holder.startTime.setText((String) appInfo.get("startTime"));
		holder.endTime.setText((String) appInfo.get("endTime"));
		holder.portCity.setText((String) appInfo.get("port"));
		holder.nextPort.setText((String) appInfo.get("nextPort"));

		holder.up.setText((String) appInfo.get("up"));
		holder.down.setText((String) appInfo.get("down"));
		holder.remark.setText((String) appInfo.get("remark"));
		holder.report.setOnClickListener(new BtOnclickListener(position));

		return convertView;
	}

	class BtOnclickListener implements OnClickListener {
		private int position;

		public BtOnclickListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			final Map<String, Object> items = dataList.get(position);
			mmsi = items.get("MMSI").toString();
			shipName = items.get("shipName").toString();
			String name = items.get("carrier").toString();
			shipId = items.get("shipId").toString();

			Bundle bundle = new Bundle();
			switch (v.getId()) {
			case R.id.btnEdit:
				Intent intent = new Intent(mContext, ArvLftListActivity.class);
				bundle.putString("mmsi", mmsi);
				String[] names = name.split(":");
				if (names.length == 2)
					carrierName = names[1];
				intent.putExtras(bundle);
				intent.putExtra("shipName", items.get("shipName").toString());
				mContext.startActivity(intent);
				break;

			}

		}

	}

	private class DtViewHolder {

		public TextView nextPort;
		public TextView remark;
		public TextView down;
		public TextView up;
		public Button report;
		public TextView shipName;
		public TextView endTime;
		public TextView startTime;
		public TextView portCity;
		public ImageView shipLogo;

	}

}
