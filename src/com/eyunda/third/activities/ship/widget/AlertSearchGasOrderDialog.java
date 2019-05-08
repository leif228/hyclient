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

import com.eyunda.third.domain.enumeric.BuyOilStatusCode;
import com.eyunda.third.domain.ship.ShipNameData;
import com.hy.client.R;

public class AlertSearchGasOrderDialog {
	private Activity context;
	private View view;
	private Builder ad;
	private List<ShipNameData> shipNameDatas;
	private ShipNameData selected;

	public AlertSearchGasOrderDialog(Activity context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.search_ships4dues_item, null);
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		this.context = context;
		this.view = view;
		this.ad = ad;
		this.shipNameDatas = new ArrayList<ShipNameData>();
		
		ShipNameData s1 = new ShipNameData();
		s1.setMmsi("");
		s1.setShipName("全部订单记录");
		shipNameDatas.add(s1);
		selected = s1;
		
		for(BuyOilStatusCode c:BuyOilStatusCode.values()){
			ShipNameData s = new ShipNameData();
			s.setMmsi(c.toString());
			s.setShipName(c.getDescription());
			shipNameDatas.add(s);
		}
		
		setview(view);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setview(View view) {
		Spinner spinner_account_way = (Spinner) view.findViewById(R.id.spinner_account_way);
		ArrayAdapter adapter_spinner_account_way = new ArrayAdapter(context,
				R.layout.spinner_item, R.id.contentTextView,
				shipNameDatas);
		spinner_account_way.setAdapter(adapter_spinner_account_way);
		spinner_account_way.setOnItemSelectedListener(new SpinnerOnSelectedListener());
	}

	class SpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			selected = (ShipNameData) adapterView.getItemAtPosition(position);

		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}

	}

	public void showAddDialog(DialogInterface.OnClickListener lis) {

		ad.setTitle("选择类型").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setNegativeButton("取消", null).setPositiveButton("查找", lis)
				.show();

	}

	public ShipNameData getSelectShip() {
		return selected;
	}
	
}
