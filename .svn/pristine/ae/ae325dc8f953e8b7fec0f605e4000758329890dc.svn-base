package com.eyunda.third.adapters.user;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyunda.third.activities.cargo.AutoNextLineLinearlayout;
import com.eyunda.third.activities.cargo.CargoSendNotifyActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.ship.ShipData;
import com.hy.client.R;

public class CargoNotifyAdapter extends BaseAdapter {

	private int res;
	private LayoutInflater layoutInflater;
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;
	private List<List<ShipData>> list;
	private CargoSendNotifyActivity activity;
	private String notifyObj;
	/** 标签之间的间距 px */
	int itemMargins = 5;
	/** 标签的行间距 px */
	int lineMargins = 3;
	private int positionTag = -1;

	public CargoNotifyAdapter(Context context, int resource,
			List<List<ShipData>> list, String notifyObj) {
		this.res = resource;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.activity = (CargoSendNotifyActivity) context;
		this.notifyObj = notifyObj;
		isSelected = new HashMap<Integer, Boolean>();
	}

	public int getPositionTag() {
		return positionTag;
	}

	public void setPositionTag(int positionTag) {
		this.positionTag = positionTag;
	}

	public void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public List<ShipData> getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(res, null);
			holder.nameTextview = (TextView) convertView
					.findViewById(R.id.name);
			holder.signature = (AutoNextLineLinearlayout) convertView
					.findViewById(R.id.signature);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		List<ShipData> sds = getItem(position);
		UserData user = new UserData();
		if (!sds.isEmpty()) {
			if ("broker".equals(notifyObj)) {
				user = sds.get(0).getBroker();
			} else {
				user = sds.get(0).getMaster();
			}
		}
		if (!user.getTrueName().equals(""))
			holder.nameTextview.setText(user.getTrueName());
		else if (!user.getNickName().equals(""))
			holder.nameTextview.setText(user.getNickName());
		else
			holder.nameTextview.setText(user.getLoginName());

		this.refrush(holder.signature, sds, position);

		holder.cb.setChecked(getIsSelected().get(position));
		holder.cb.setOnClickListener(new lvButtonListener(holder.cb, position));

		return convertView;

	}

	public void refrush(LinearLayout container, List<ShipData> sds, int position) {
		container.removeAllViews();
		LayoutInflater inflater = activity.getLayoutInflater();
		LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tvParams.setMargins(0, 0, itemMargins, 0);

		for (int i = 0; i < sds.size(); ++i) {
			String text = "";
			if (i == sds.size() - 1)
				text = sds.get(i).getShipName();
			else
				text = sds.get(i).getShipName() + "，";

			addItemView(inflater, container, tvParams, text, sds.get(i).getId());
		}
	}

	private void addItemView(LayoutInflater inflater, ViewGroup viewGroup,
			LayoutParams params, final String text, final Long shipId) {
		TextView tvItem = (TextView) inflater.inflate(
				R.layout.eyd_cargo_notify_signature, null);

		tvItem.setText(text);
		tvItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, ShipPreviewActivity.class);
				intent.putExtra("id", shipId.toString());

				String newText = text;
				if (newText.contains("，"))
					newText = newText.replace("，", "").trim();
				intent.putExtra("name", newText);
				activity.startActivity(intent);
			}
		});
		viewGroup.addView(tvItem, params);
	}

	class lvButtonListener implements OnClickListener {
		private int position;
		private CheckBox holder;

		public lvButtonListener(CheckBox cb, int pos) {
			position = pos;
			holder = cb;
		}

		@Override
		public void onClick(View v) {
			holder.toggle();
			// 将CheckBox的选中状况记录下来
			boolean isChecked = holder.isChecked();
			if (isChecked) {
				holder.setChecked(false);
				getIsSelected().put(position, false);
			} else {
				holder.setChecked(true);
				getIsSelected().put(position, true);
			}
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		CargoNotifyAdapter.isSelected = isSelected;
	}

	public class ViewHolder {
		public TextView nameTextview;
		public AutoNextLineLinearlayout signature;
		public CheckBox cb;
	}
}
