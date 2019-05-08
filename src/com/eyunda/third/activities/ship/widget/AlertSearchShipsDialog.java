package com.eyunda.third.activities.ship.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.eyunda.third.domain.ship.ShipNameData;
import com.hy.client.R;

public class AlertSearchShipsDialog {
	private Activity context;
	private View view;
	private Builder ad;
	private String selectShip = "";
	private List<ShipNameData> shipNameDatas;
	private String currShip;
	private String selected;
	private String flag;

	public AlertSearchShipsDialog(Activity context,List<ShipNameData> shipNameDatas, String currShip, String flag) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.search_ships4dues_item, null);
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		this.context = context;
		this.view = view;
		this.ad = ad;
		this.shipNameDatas = shipNameDatas;
		this.currShip = currShip;
		this.flag = flag;
		setview(view);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setview(View view) {
		Spinner spinner_account_way = (Spinner) view.findViewById(R.id.spinner_account_way);
		List<String> list_spinner_account_way = new ArrayList<String>();
		if (currShip != null) {
			selected=currShip;
			list_spinner_account_way.add(currShip);
		} else {
			if("dues".equals(flag))
				selected="全部缴费船舶";
			else
				selected="全部加油船舶";
		}
		if("dues".equals(flag))
			list_spinner_account_way.add("全部缴费船舶");
		else
			list_spinner_account_way.add("全部加油船舶");

		for (ShipNameData p : shipNameDatas) {
			if(currShip != null&&p.getShipName().equals(currShip)){
				continue;
			}
			list_spinner_account_way.add(p.getShipName());
		}
		ArrayAdapter adapter_spinner_account_way = new ArrayAdapter(context,
				R.layout.spinner_item, R.id.contentTextView,
				list_spinner_account_way);
		spinner_account_way.setAdapter(adapter_spinner_account_way);
		spinner_account_way.setOnItemSelectedListener(new SpinnerOnSelectedListener());
	}

	class SpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			selected = adapterView.getItemAtPosition(position).toString();

		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}

	}

	public void showAddDialog(DialogInterface.OnClickListener lis) {

		ad.setTitle("选择船舶").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setNegativeButton("取消", null).setPositiveButton("查找", lis)
				.show();

	}

	public String getSelectShip() {
		return selected;
	}
	
}
