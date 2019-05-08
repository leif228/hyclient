package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.ConfirmPublicActivity;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.ship.ShipDataSource;
import com.eyunda.third.activities.ship.ShipDuesActivity;
import com.eyunda.third.activities.ship.ShipGasOrderActivity;
import com.eyunda.third.activities.ship.ShipinfoActivity;
import com.eyunda.third.domain.enumeric.CollectCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class MyshipAdapter extends BaseAdapter {

	private class buttonViewHolder {
		ImageView shipLogo;// 船logo

		TextView shipId, shipName, TypeCode, shipType, code, place, keyWords, carrier, endtime, shipStatic;
		Button btnEdit;// 编辑按钮
		Button btnDelete;// 删除按钮
		Button btnPublish;// 发布按钮
		Button btnJudge;// 提交审核按钮
		Button btnCancle;// 取消按钮
		Button btnSetting;// 设置按钮
		Button btnPower;// 权限按钮
		Button btnDues;// 服务费按钮
		Button btnGasOrder;// 服务费按钮

		public TextView userId;

		public TextView mmsi;

	}

	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;
	private ImageLoader mImageLoader;
	public static String shipId;
	public static ShipData shipData;
	private ProgressDialog pd;
	public static DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.eyd_chat_ic_cycle)
			.showImageForEmptyUri(R.drawable.eyd_chat_search_clear_pressed).showImageOnFail(R.drawable.img_load_failed)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisc(true) // 缓存到sd卡
			.build();
	Data_loader data2 = new Data_loader();
	DialogUtil dialogUtil;
	private CollectCode current;

	public MyshipAdapter(Context c, ArrayList<Map<String, Object>> appList, CollectCode cur) {
		this.mAppList = appList;
		this.mContext = c;
		pd = new ProgressDialog(c);
		mImageLoader = ImageLoader.getInstance();
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		current = cur;
	}

	public void setCurrent(CollectCode cur) {
		current = cur;
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
			convertView = mInflater.inflate(R.layout.eyd_myship_item, null);
			holder = new buttonViewHolder();
			holder.shipId = (TextView) convertView.findViewById(R.id.shipId);
			holder.shipLogo = (ImageView) convertView.findViewById(R.id.shipLogo);
			holder.shipName = (TextView) convertView.findViewById(R.id.shipName);
			holder.shipType = (TextView) convertView.findViewById(R.id.shipType);
			holder.TypeCode = (TextView) convertView.findViewById(R.id.TypeCode);
			holder.mmsi = (TextView) convertView.findViewById(R.id.mmsi);
			holder.place = (TextView) convertView.findViewById(R.id.place);
			holder.carrier = (TextView) convertView.findViewById(R.id.carrier);
			holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
			holder.shipStatic = (TextView) convertView.findViewById(R.id.shipStatic);
			holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
			holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
			holder.btnPublish = (Button) convertView.findViewById(R.id.btnPublish);
			holder.btnJudge = (Button) convertView.findViewById(R.id.btnJudge);
			holder.btnCancle = (Button) convertView.findViewById(R.id.btnCancel);
			holder.btnSetting = (Button) convertView.findViewById(R.id.btnSetting);
			holder.btnPower = (Button) convertView.findViewById(R.id.btnPower);
			holder.btnDues = (Button) convertView.findViewById(R.id.btnDues);
			holder.btnGasOrder = (Button) convertView.findViewById(R.id.btnGasOrder);
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {

			// 设置imageView显示内容

			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + appInfo.get("shipLogo").toString(),
					holder.shipLogo, displayImageOptions);
			holder.shipId.setText((String) appInfo.get("shipId"));
			holder.shipName.setText((String) appInfo.get("shipName"));
			holder.shipType.setText((String) appInfo.get("shipType"));
			holder.TypeCode.setText((String) appInfo.get("TypeCode"));
			holder.mmsi.setText((String) appInfo.get("MMSI"));
			holder.carrier.setText((String) appInfo.get("carrier"));
			holder.place.setText((String) appInfo.get("place"));
			holder.endtime.setText((String) appInfo.get("endtime"));
			holder.shipStatic.setText((String) appInfo.get("shipStatic"));

			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnEdit")) {// 编辑
				holder.btnEdit.setVisibility(View.VISIBLE);
				holder.btnEdit.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnEdit.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnDelete")) {// 删除
				holder.btnDelete.setVisibility(View.VISIBLE);
				holder.btnDelete.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnDelete.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnPublish")) {// 发布
				holder.btnPublish.setVisibility(View.VISIBLE);
				holder.btnPublish.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnPublish.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnJudge")) {// 审核
				holder.btnJudge.setVisibility(View.VISIBLE);
				holder.btnJudge.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnJudge.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnCancel")) {// 取消
				holder.btnCancle.setVisibility(View.VISIBLE);
				holder.btnCancle.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnCancle.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnSetting")) {// 取消
				holder.btnSetting.setVisibility(View.VISIBLE);
				holder.btnSetting.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnSetting.setVisibility(View.GONE);
			}
			// 控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnPower")) {// 取消
				holder.btnPower.setVisibility(View.VISIBLE);
				holder.btnPower.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnPower.setVisibility(View.GONE);
			}
			// if ((Boolean) appInfo.get("btnDues")) {//取消
			// holder.btnDues.setVisibility(View.VISIBLE);
			// holder.btnDues.setOnClickListener(new
			// lvButtonListener(position));
			// } else {
			holder.btnDues.setVisibility(View.GONE);
			// }
			// if ((Boolean) appInfo.get("btnGasOrder")) {//取消
			// holder.btnGasOrder.setVisibility(View.VISIBLE);
			// holder.btnGasOrder.setOnClickListener(new
			// lvButtonListener(position));
			// } else {
			holder.btnGasOrder.setVisibility(View.GONE);
			// }

		}

		return convertView;

	}

	class lvButtonListener implements OnClickListener {
		private int position;
		Bitmap bitmap;

		lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			// Map<Long, Object> curMap = (Map<Long, Object>)getItem(position);
			HashMap<String, Object> curMap = (HashMap<String, Object>) mAppList.get(position);
			// shipId = curMap.get("shipId").toString();
			if (vid == holder.btnEdit.getId()) {
				Intent intent = new Intent(mContext, ShipinfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("shipId", curMap.get("shipId").toString());// shipId
				bundle.putSerializable("shipInfo", (ShipData) curMap.get("shipData"));
				intent.putExtras(bundle);
				// shipData = (ShipData) curMap.get("shipData");
				mContext.startActivity(intent);

			}

			if (vid == holder.btnDelete.getId()) {
				// Toast.makeText(mContext,
				// holder.btnDelete.getText()+"-position:"+position+"-shipID:"+curMap.get("shipName"),
				// Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(mContext).setTitle("删除船舶信息").setMessage("确定删除吗？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								removeItem(position, current);// 删除
							}
						}).setNegativeButton("取消", null).show();
			}
			if (vid == holder.btnPublish.getId()) {
				// Toast.makeText(mContext,
				// holder.btnPublish.getText()+"-position:"+position+"-shipID:"+curMap.get("shipName"),
				// Toast.LENGTH_SHORT).show();
				publish(position);// 发布
			}
			if (vid == holder.btnCancle.getId()) {
				// Toast.makeText(mContext,
				// holder.btnCancle.getText()+"-position:"+position+"-shipID:"+curMap.get("shipName"),
				// Toast.LENGTH_SHORT).show();
				unpublish(position);// 取消发布
			}
			if (vid == holder.btnSetting.getId()) {
				// 设置数据来源
				Intent intent = new Intent(mContext, ShipDataSource.class);
				Bundle bundle = new Bundle();
				bundle.putString("shipId", curMap.get("shipId").toString());// shipId
				bundle.putString("mmsi", curMap.get("MMSI").toString().substring(5));// shipId
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
			if (vid == holder.btnPower.getId()) {

			}
			if (vid == holder.btnDues.getId()) {
				// 权限
				Intent intent = new Intent(mContext, ShipDuesActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("shipId", curMap.get("shipId").toString());// shipId
				bundle.putString("shipName", curMap.get("shipName").toString());// shipId
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
			if (vid == holder.btnGasOrder.getId()) {
				// 权限
				Intent intent = new Intent(mContext, ShipGasOrderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("shipId", curMap.get("shipId").toString());// shipId
				bundle.putString("shipName", curMap.get("shipName").toString());// shipId
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
			if (vid == holder.btnJudge.getId()) {
				new AlertDialog.Builder(mContext).setTitle("提交船舶审核信息").setMessage("确定要提交审核吗？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								judgeItem(position);// 删除
							}
						}).setNegativeButton("取消", null).show();
			}

		}

	}

	// 删除
	public void removeItem(final int position, CollectCode current) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				pd.setMessage("删除中...");
				pd.setCanceledOnTouchOutside(false);
				pd.show();
			}

			@Override
			public void onSuccess(String arg0) {
				pd.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> result = gson.fromJson((String) arg0, new TypeToken<Map<String, Object>>() {
				}.getType());
				if (result.get("returnCode").equals("Success")) {
					mAppList.remove(position);
					notifyDataSetChanged();
					Toast.makeText(mContext, "删除" + result.get("message"), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "删除" + result.get("message") + ",请先删除关联的船舶动态或合同", Toast.LENGTH_LONG)
							.show();

				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				pd.dismiss();
				Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> ida = mAppList.get(position);

		params.put("id", ida.get("shipId"));
		if (current.equals(CollectCode.mySelfShips)) {
			data2.getApiResult(handler, "/mobile/ship/delete", params, "get");
		} else {
			data2.getApiResult(handler, "/mobile/ship/deleteFav", params, "get");
		}
	}

	// 提交审核
	public void judgeItem(final int position) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				pd.setMessage("提交中...");
				pd.setCanceledOnTouchOutside(false);
				pd.show();
			}

			@Override
			public void onSuccess(String arg0) {
				pd.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> result = gson.fromJson((String) arg0, new TypeToken<Map<String, Object>>() {
				}.getType());
				if (result.get("returnCode").equals("Success")) {
					Intent intent = new Intent(mContext, MyshipActivity.class);
					mContext.startActivity(intent);
				}
				Toast.makeText(mContext, result.get("message").toString(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				pd.dismiss();
				Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> ida = mAppList.get(position);

		params.put("id", ida.get("shipId"));
		data2.getApiResult(handler, "/mobile/ship/myShip/commit", params, "get");
	}

	// 取消发布
	public void unpublish(int position) {
		Map<String, Object> ida = mAppList.get(position);
		Long shipId = Long.parseLong((String) ida.get("shipId"));
		Intent intent = new Intent(mContext, ConfirmPublicActivity.class);
		intent.putExtra("id", shipId);
		intent.putExtra("page", 1);
		intent.putExtra("pubFlag", false);
		intent.putExtra("source", 1);
		mContext.startActivity(intent);
		((Activity) mContext).finish();

	}

	// 发布
	public void publish(final int position) {
		Map<String, Object> ida = mAppList.get(position);
		Long shipId = Long.parseLong((String) ida.get("shipId"));
		Intent intent = new Intent(mContext, ConfirmPublicActivity.class);
		intent.putExtra("id", shipId);
		intent.putExtra("page", 1);
		intent.putExtra("pubFlag", true);
		intent.putExtra("source", 1);
		mContext.startActivity(intent);
		((Activity) mContext).finish();
	}

	public void addItem(Map<String, Object> item) {
		mAppList.add(item);
	}
}
