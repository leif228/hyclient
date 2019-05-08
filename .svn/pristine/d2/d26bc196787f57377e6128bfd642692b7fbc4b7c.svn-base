package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.order.PortData;
import com.eyunda.third.domain.ship.ShipPortData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.LocalFileUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶接货
 *
 */
public class ShipDeliveryActivity extends ShipinfoActivity implements OnClickListener {

	protected String seletedDate;
	Data_loader data = new Data_loader();
	private String id;

	ArrayList<String> codeList = new ArrayList<String>();

	private LinearLayout lp;
	private TextView tv, rec;
	private RelativeLayout lrec;
	private TextView ttv;
	private ImageView ivdel;
	private Spinner spinner_city;

	private Spinner spinner_portName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ri_main_item_);
		id = getIntent().getStringExtra("shipId");
		setViews();
		getDatas();

	}

	private void getDatas() {
		final Map<String, Object> params = new HashMap<String, Object>();
		final StringBuffer sbType = new StringBuffer(100);
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			};

			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/ship/myShip/getShipDelivery", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>) cd.getContent();
					List<String> typeContent = (List<String>) ma.get("typList");
					List<Map<String, Object>> portContent = (List<Map<String, Object>>) ma.get("portList");
					for (String mc : typeContent) {
						// 把保存类别的编码用arrayList存放
						if (typeContent.size() > 1) {
							sbType.append(CargoBigTypeCode.valueOf(mc).getDescription() + ", ");
							tv.setText(sbType);
							codeList.add(mc);

						} else {
							sbType.append(CargoBigTypeCode.valueOf(mc).getDescription());
							tv.setText(sbType + ", ");
							codeList.add(mc);
						}
					}
					for (Map<String, Object> mp : portContent) {
						ShipPortData port = new ShipPortData(mp);

						if (port.getPortCity() != null)
							getPortInfo(port.getPortCity().getCode(), port.getCityName());
						else
							getPortInfo(port.getCargoPortData().getPortNo(), port.getCargoPortData().getFullName());
					}
				}

			};

			public void onFailure(Throwable arg0, String arg1) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();

			};
		};

		params.put("shipId", id);
		data.getApiResult(handler, "/mobile/ship/myShip/getShipDelivery", params, "get");

	}

	protected void getPortInfo(String portNo, String name) {
		View view = getLayoutInflater().inflate(R.layout.plug_item, null);
		TextView tvp = (TextView) view.findViewById(R.id.tv_cp);
		tvp.setText(name);
		tvp.setTag(portNo);
		lp.addView(view);
		ImageView iv = (ImageView) view.findViewById(R.id.clearPort);
		iv.setOnClickListener(new ClearPortOnclicListener(view));

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶接货");
		menu_report.setOnClickListener(null);
		menu_basic.setBackgroundColor(Color.parseColor("#3B79C4"));
		menu_report.setBackgroundColor(0xFF6db7ff);
	};

	private void setViews() {
		lrec = (RelativeLayout) findViewById(R.id.rec_ship);
		lp = (LinearLayout) findViewById(R.id.adPort);
		tv = (TextView) findViewById(R.id.tv_content);
		ivdel = (ImageView) findViewById(R.id.clear);
		Button save = (Button) findViewById(R.id.appSave);
		rec = (TextView) findViewById(R.id.rec_port);
		TextView bt = (TextView) findViewById(R.id.main);
		tv.setOnClickListener(this);
		rec.setOnClickListener(this);
		ivdel.setOnClickListener(this);
		save.setOnClickListener(this);
		bt.setOnClickListener(this);

	}

	private void dialog() {
		ArrayList<String> mList = new ArrayList<String>();
		ArrayList<String> mtypes = new ArrayList<String>();
		int num = 0;
		for (CargoBigTypeCode e : CargoBigTypeCode.values()) {
			mList.add(e.getDescription());
			mtypes.add(e.name());
			num++;
		}

		final String[] arrString = (String[]) mList.toArray(new String[0]);
		final String[] atyString = (String[]) mtypes.toArray(new String[0]);
		final StringBuffer sbCode = new StringBuffer(100);
		final StringBuffer sbStr = new StringBuffer(100);
		// 全部默认所有类非选
		ArrayList<Boolean> checked = new ArrayList<Boolean>();
		for (int i = 0; i < arrString.length; i++) {
			checked.add(false);
		}
		if (!TextUtils.isEmpty(tv.getText())) {
			String[] selected = tv.getText().toString().split(", ");

			String last = selected[selected.length - 1].substring(0, selected[selected.length - 1].length() - 1);
			for (int j = 0; j < selected.length; j++) { // 判断哪些是否已经选择了
				for (int i = 0; i < arrString.length; i++) {
					if (selected[j].equals(arrString[i])) {
						checked.set(i, true);
					}
					// 处理最后一项的逗号
					if (last.equals(arrString[i])) {
						checked.set(i, true);
					}
				}
			}
		}
		// 转化为Boolean数组
		Boolean[] checkedItems = (Boolean[]) checked.toArray(new Boolean[0]);
		boolean[] selectedItems = new boolean[num];
		for (int j = 0; j < num; j++) {
			try {
				selectedItems[j] = checkedItems[j];
			} catch (Exception e) {
				selectedItems[j] = false;
			}
		} // boolean[] checkedItems ={};

		new AlertDialog.Builder(ShipDeliveryActivity.this).setTitle("选择类别")
				.setMultiChoiceItems(arrString, selectedItems, new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						if (isChecked) {
							// sbCode.append(CargoTypeCode.getCargoType(arrString[which])
							// + ", ");
							sbCode.append(atyString[which] + ", ");
							sbStr.append(arrString[which] + ", ");
						}
					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv.append(sbStr);
						codeList.add(sbCode.toString());

					}
				}).setNegativeButton("取消", null).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear: // 右部按钮
			codeList.clear();
			tv.setText("");
			break;
		case R.id.main:
			dialog();
			break;

		case R.id.rec_port:
			// AreaSelect asStart = new AreaSelect(this, rec);
			addText();

			break;
		case R.id.appSave:
			StringBuffer sb = new StringBuffer(100);
			// 获取控件

			int count = lp.getChildCount(); // LinearLayout的个数
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					FrameLayout fram = (FrameLayout) lp.getChildAt(i); // FrameLayout包含TextView和imageView
					// 第i个FrameLayout中的TextView
					ttv = (TextView) fram.getChildAt(0);
					sb.append(ttv.getTag() + ", ");

				}
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("shipId", id);
			params.put("cargoType", codeList.toString().substring(1, codeList.toString().length() - 1));
			if (sb.length() == 0) {
				params.put("cityorport", sb);
			} else
				params.put("cityorport", sb.substring(0, sb.length() - 2));
			Log.v("ShipDeliveryActivity", params.toString());
			data.getApiResult(handler, "/mobile/ship/myShip/saveDelivery", params, "post");
			break;
		}
	}

	private void addText() {
		final View view = getLayoutInflater().inflate(R.layout.plug_item, null);
		final TextView tvp = (TextView) view.findViewById(R.id.tv_cp);
		ImageView iv = (ImageView) view.findViewById(R.id.clearPort);
		iv.setOnClickListener(new ClearPortOnclicListener(view));
		lp.addView(view);

		View views = getLayoutInflater().inflate(R.layout.dialog, null);
		spinner_city = (Spinner) views.findViewById(R.id.portCity);
		spinner_portName = (Spinner) views.findViewById(R.id.portName);
		ArrayList<SpinnerItem> cities = new ArrayList<SpinnerItem>();
		for (PortCityCode e : PortCityCode.values()) {
			SpinnerItem sp = new SpinnerItem(e.getCode(), e.getDescription());
			cities.add(sp);
		}
		// 获取港口数据,根据城市对应其港口名 AreaSelect
		// 选择的城市
		spinner_city.setOnItemSelectedListener(new autodropDownListener());
		// Map<PortCityCode, List<PortData>> portList =
		// LocalFileUtil.getAreaFile(getApplicationContext());
		// for(int i=0;i<PortCityCode.values().length; i++){
		// List<PortData> value = portList.get(PortCityCode.values()[i]);
		// for(PortData data : value){
		// SpinnerItem sp = new
		// SpinnerItem(data.getPortNo(),data.getPortName());
		// ports.add(sp);
		// }
		// }
		ArrayAdapter<SpinnerItem> adapter_city = new ArrayAdapter<SpinnerItem>(getApplicationContext(),
				R.layout.spinner_item, R.id.contentTextView, cities);
		// ArrayAdapter<SpinnerItem> adapter_portName = new
		// ArrayAdapter<SpinnerItem>(getApplicationContext(),
		// R.layout.spinner_item,R.id.contentTextView, ports);
		spinner_city.setAdapter(adapter_city);
		// spinner_portName.setAdapter(adapter_portName);
		new AlertDialog.Builder(ShipDeliveryActivity.this).setTitle("选择城市").setView(views)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						lp.removeView(view);

					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					StringBuilder sbValue = new StringBuilder(200);
					StringBuilder sbCode = new StringBuilder(200);

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String code = ((SpinnerItem) spinner_city.getSelectedItem()).getId();
						String value = ((SpinnerItem) spinner_city.getSelectedItem()).getValue();
						String codes2 = ((SpinnerItem) spinner_portName.getSelectedItem()).getId();
						String value2 = ((SpinnerItem) spinner_portName.getSelectedItem()).getValue();
						if (codes2.equals("")) {
							sbValue.append(value);
							sbCode.append(code);
						} else {
							sbValue.append(value).append(".").append(value2);
							sbCode.append(codes2);
						}
						tvp.setText(sbValue);

						tvp.setTag(sbCode);

					}
				}).show();

	}

	class autodropDownListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String value = ((SpinnerItem) parent.getItemAtPosition(position)).getId(); // 11
			Map<PortCityCode, List<PortData>> areas = LocalFileUtil.getAreaFile(getApplicationContext());
			ArrayList<SpinnerItem> portList = new ArrayList<SpinnerItem>();
			portList.add(new SpinnerItem("", "请选择港口"));
			for (int i = 0; i < PortCityCode.values().length; i++) {
				List<PortData> portDatas = areas.get(PortCityCode.values()[i]);
				for (PortData p : portDatas) {
					if (value.equals(p.getPortCity().getCode())) {
						SpinnerItem sp = new SpinnerItem(p.getPortNo(), p.getPortName());
						portList.add(sp);
					}
				}

				ArrayAdapter<SpinnerItem> adapter_portName = new ArrayAdapter<SpinnerItem>(context,
						R.layout.spinner_item, R.id.contentTextView, portList);
				spinner_portName.setAdapter(adapter_portName);
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	class ClearPortOnclicListener implements OnClickListener {
		private View view;

		public ClearPortOnclicListener(View view) {
			this.view = view;
		}

		@Override
		public void onClick(View v) {
			lp.removeView(view);

		}

	}

	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			dialog = dialogUtil.loading("保存中", "请稍候...");
		}

		@Override
		public void onSuccess(String arg0) {
			dialog.dismiss();
			ConvertData cd = new ConvertData(arg0);
			if (cd.getReturnCode().equals("Success")) {
				Toast.makeText(getApplicationContext(), "提交成功！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();

			}

		};

		public void onFailure(Throwable arg0, String arg1) {
			dialog.dismiss();
			Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();

		};
	};

}
