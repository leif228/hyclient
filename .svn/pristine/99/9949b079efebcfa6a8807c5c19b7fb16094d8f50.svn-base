package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.cargo.CargoActivity;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.order.AddOrderOneGSHActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderCargoAdapter extends BaseAdapter {
	private Activity mContext;
	private List<Map<String, Object>> listItems;
	private LayoutInflater listContainer;
	private Data_loader data;
	private ViewHolder holder;
	private ImageLoader mImageLoader;
	private String orderId;
	DialogUtil dialogUtil;
	ProgressDialog dialog;

	public OrderCargoAdapter(Activity context,
			ArrayList<Map<String, Object>> listItems, String oid) {
		this.mContext = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		this.orderId = oid;
		data = new Data_loader();
		dialogUtil = new DialogUtil(mContext);
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.eyd_cargo_item, null);
			holder = new ViewHolder();
			// 获取控件对象
			holder.cargoId = (TextView) convertView
					.findViewById(R.id.list_cargoId);
			holder.cargoImg = (ImageView) convertView
					.findViewById(R.id.list_img); // 图片
			holder.cargoName = (TextView) convertView
					.findViewById(R.id.list_cargoName); // 货名
			holder.cargoType = (TextView) convertView
					.findViewById(R.id.list_cargoType); // 货类
			holder.total = (TextView) convertView
					.findViewById(R.id.list_weight); // 总重
			holder.shipper = (TextView) convertView
					.findViewById(R.id.list_shipper); // 总重

			holder.volume = (TextView) convertView
					.findViewById(R.id.list_volume); // 重量/体积/数量
			holder.port = (TextView) convertView.findViewById(R.id.list_place); // 起止港
			holder.unitPrice = (TextView) convertView
					.findViewById(R.id.unit_price); // 单价
			holder.totalPrice = (TextView) convertView
					.findViewById(R.id.total_price); // 总价
			holder.endTime = (TextView) convertView
					.findViewById(R.id.list_endtime); // 截止日
			holder.status = (TextView) convertView
					.findViewById(R.id.list_status); // 发布状态

			holder.btnPublish = (Button) convertView
					.findViewById(R.id.btnPublish); // 发布
			holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit); // 编辑
			holder.btnDelete = (Button) convertView
					.findViewById(R.id.btnDelete); // 删除
			holder.btnCancel = (Button) convertView
					.findViewById(R.id.btnCancel); // 取消
			// 设置控件集到convertView
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
				+ listItems.get(position).get("cargoImage").toString(),
				holder.cargoImg, MyshipAdapter.displayImageOptions);
		holder.cargoId.setText((String) listItems.get(position).get("cargoId"));
		holder.cargoName.setText((String) listItems.get(position).get(
				"cargoName"));
		holder.cargoType.setText((String) listItems.get(position).get(
				"cargoType"));
		holder.shipper.setText((String) listItems.get(position).get("shipper"));
		holder.total.setText((String) listItems.get(position)
				.get("totalWeight"));
		holder.volume
				.setText((String) listItems.get(position).get("wrapCount"));
		holder.port.setText((String) listItems.get(position).get("port"));
		holder.unitPrice.setText((String) listItems.get(position).get(
				"unitPrice"));
		holder.totalPrice.setText((String) listItems.get(position).get(
				"totalPrice"));
		holder.endTime.setText((String) listItems.get(position).get("endTime"));
		holder.status.setText((String) listItems.get(position).get(
				"cargoStatus"));
		// 控制按钮的显示和隐藏
		// 发布按钮隐藏
		if ((Boolean) listItems.get(position).get("btnPublish")) {

			holder.btnPublish.setVisibility(View.VISIBLE);
			holder.btnPublish
					.setOnClickListener(new lvButtonListener(position));
		} else {
			holder.btnPublish.setVisibility(View.GONE);
		}
		// 编辑按钮隐藏
		if ((Boolean) listItems.get(position).get("btnEdit")) {
			holder.btnEdit.setVisibility(View.VISIBLE);
			holder.btnEdit.setOnClickListener(new lvButtonListener(position));
		} else {
			holder.btnEdit.setVisibility(View.GONE);
		}
		// 删除按钮隐藏
		if ((Boolean) listItems.get(position).get("btnDelete")) {
			holder.btnDelete.setVisibility(View.VISIBLE);
			holder.btnDelete.setOnClickListener(new lvButtonListener(position));
		} else {
			holder.btnDelete.setVisibility(View.GONE);
		}
		// 取消按钮隐藏
		if ((Boolean) listItems.get(position).get("btnCancel")) {
			holder.btnCancel.setVisibility(View.VISIBLE);
			holder.btnCancel.setOnClickListener(new lvButtonListener(position));
		} else {
			holder.btnCancel.setVisibility(View.GONE);
		}
		// 监听
		return convertView;
	}

	class lvButtonListener implements OnClickListener {
		private int position;

		public lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			// Map<Long, Object> curMap = (Map<Long, Object>)getItem(position);
			HashMap<String, Object> curMap = (HashMap<String, Object>) listItems
					.get(position);
			switch (v.getId()) {
			case R.id.btnEdit:

				Intent intent = new Intent(mContext, AddOrderOneGSHActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("cargoId", curMap.get("cargoId").toString());
				bundle.putString("orderId", orderId);
				bundle.putInt("request",2);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
				mContext.finish();
				break;

			case R.id.btnDelete:
				new AlertDialog.Builder(mContext)
						.setTitle("删除货运信息")
						.setMessage("确定删除吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										removeItem(position);
									}
								}).setNegativeButton("取消", null).show();
				break;

			}

		}

	}

	// 删除
	public void removeItem(final int position) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "正在联网删除...");
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					listItems.remove(position);
					notifyDataSetChanged();
					Toast.makeText(mContext, "删除" + cd.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				dialog.dismiss();
				Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
			}

		};
		HashMap<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> ida = listItems.get(position);
		params.put("id", ida.get("cargoId"));
		Log.v("params", params.toString());
		data.getApiResult(handler, "/mobile/cargo/myCargo/delete", params,
				"get");
	}



	public class ViewHolder {
		public TextView phone;
		// 自定义控件集合
		public ImageView cargoImg;
		public TextView cargoId;
		public TextView shipper;
		public TextView cargoType; // 货类
		public TextView cargoName; // 货名
		public TextView volume; // 箱（货）量
		public TextView total; // 总重
		public TextView port; // 港口
		public TextView unitPrice; // 单价
		public TextView totalPrice; // 总价
		public TextView endTime; // 截止
		public TextView status; // 状态

		public Button btnPublish;
		public Button btnEdit;
		public Button btnDelete;
		public Button btnCancel;
	}

}
