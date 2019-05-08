package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.activities.cargo.CargoActivity;
import com.eyunda.third.adapters.ship.DynamicAdapter;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.WrapStyleCode;
import com.eyunda.third.domain.ship.ArvlftShipData;
import com.eyunda.third.domain.ship.ShipArvlftData;
import com.eyunda.third.domain.ship.ShipUpdownData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipUpdownActivity extends CommonActivity implements OnClickListener {

	private LinearLayout relayout;
	private ImageView addGoods;
	private Button saveInfo;
	protected String ctUnitWeight;
	protected String ctWrapCount;
	protected String ctHeight;
	protected String ctWdith;
	protected String ctLong;
	boolean isSelected =false;
	private Data_loader data = new Data_loader();
	private long id;
	private String zhuagnxie;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.eyd_albumchoiceitem);
		relayout = (LinearLayout) findViewById(R.id.rl);
		addGoods = (ImageView) findViewById(R.id.iv_addgoods);
		saveInfo = (Button) findViewById(R.id.btn_save_report);

		initData();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		id = getIntent().getLongExtra("id", 0);
		zhuagnxie = getIntent().getStringExtra("updown");
		ArvlftShipData avf = (ArvlftShipData) getIntent().getSerializableExtra("data");
		addGoods.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
		ShipArvlftData arrive = avf.getArriveData();
		ShipArvlftData left = avf.getLeftData();
		if (arrive.getArvlft().getUpdown().equals(zhuagnxie)) {// 如果是卸货
			id = arrive.getId();
			for (ShipUpdownData up : arrive.getShipUpdownDatas()) {
				addGoods(up);
			}
		}

		if (left.getArvlft().getUpdown().equals(zhuagnxie)) {// 如果是装货
			id = left.getId();
			for (ShipUpdownData up : left.getShipUpdownDatas()) {
				addGoods(up);
			}
		}

	}

	private void addGoods(ShipUpdownData up) {
		LayoutInflater mInflater = LayoutInflater.from(this);
		final LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.list_item_goods, null);
		Spinner spinner = (Spinner) view.findViewById(R.id.spin_class); // 货类
		Spinner spinner_pack = (Spinner) view.findViewById(R.id.spin_pack);// 包装
		EditText ed_count = (EditText) view.findViewById(R.id.et_count); // 数量
		final TextView tv_cargos = (TextView) view.findViewById(R.id.tv_cargos);
		final EditText et_Name = (EditText) view.findViewById(R.id.et_goods);
		EditText unit_weight = (EditText) view.findViewById(R.id.et_unit);
		EditText total_weight = (EditText) view.findViewById(R.id.et_total);
		EditText et_long = (EditText) view.findViewById(R.id.et_long);
		EditText et_width = (EditText) view.findViewById(R.id.et_width);
		EditText et_height = (EditText) view.findViewById(R.id.et_height);
		EditText et_stere = (EditText) view.findViewById(R.id.et_stere);

		ImageView ivDel = (ImageView) view.findViewById(R.id.iv_del_item);// 删除按钮

		ed_count.setText(up.getWrapCount().toString());
		et_Name.setText(up.getCargoName());
		unit_weight.setText(up.getUnitWeight().toString());
		total_weight.setText(up.getFullWeight().toString());
		et_long.setText(up.getCtlLength().toString());
		et_height.setText(up.getCtlHeight().toString());
		et_width.setText(up.getCtlWidth().toString());
		et_stere.setText(up.getCtlVolume().toString());
		CargoTypeCode es[] = CargoTypeCode.values();

		ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(ShipUpdownActivity.this,
				R.layout.spinner_item, R.id.contentTextView, getTypes());
		spinner.setAdapter(adapter);

		ArrayAdapter<SpinnerItem> adapter2 = new ArrayAdapter<SpinnerItem>(ShipUpdownActivity.this,
				R.layout.spinner_item, R.id.contentTextView, getTypePack());
		spinner_pack.setAdapter(adapter2);
		spinner.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isSelected=true;
				return false;
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String cargo = parent.getItemAtPosition(position).toString();
				String jzx = CargoBigTypeCode.container.getDescription();
				String name[] = cargo.split("\\.");
				if(isSelected){
				if (cargo.contains(jzx)) { // 选中的是集装箱
					tv_cargos.setText("规格");
					et_Name.setText(name[1]);
				} else {
					et_Name.setText(name[1]);
					tv_cargos.setText("货名");
				}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		relayout.addView(view);
		autoCal(ed_count, unit_weight, total_weight, et_long, et_height, et_width, et_stere);
		ivDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				relayout.removeView(view);

			}
		});
		for (int j = 0; j < es.length; j++) {
			if (up.getCargoType().getDescription().equals(es[j].getDescription())) {
				spinner.setSelection(j);
			}
		}
		WrapStyleCode ws[] = WrapStyleCode.values();
		for (int j = 0; j < ws.length; j++) {
			if (up.getWrapStyle().getDescription().equals(ws[j].getDescription())) {
				spinner_pack.setSelection(j);
			}
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(zhuagnxie);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_addgoods:
			addGoodItem();
			break;

		case R.id.btn_save_report:
			onModifyReport();
			break;
		}

	}

	private void onModifyReport() {
		int goodsCount = relayout.getChildCount();
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> countList = new ArrayList<String>(); // 存放数量
		List<String> nameList = new ArrayList<String>(); // 存放货名
		List<String> unitList = new ArrayList<String>(); // 存放单重
		List<String> totalList = new ArrayList<String>(); // 存放总重
		List<String> lengthList = new ArrayList<String>(); // 存放长
		List<String> widthList = new ArrayList<String>(); // 存放宽
		List<String> heightList = new ArrayList<String>(); // 存放高
		List<String> volumeList = new ArrayList<String>(); // 存放体积
		final List<String> spinner_type = new ArrayList<String>(); // 存放货类
		final List<String> spinner_pack = new ArrayList<String>(); // 存放包装类

		for (int i = 0; i < goodsCount; i++) {
			LinearLayout linearlayout = (LinearLayout) relayout.getChildAt(i);
			EditText et_name = (EditText) linearlayout.findViewById(R.id.et_goods);
			EditText et_count = (EditText) linearlayout.findViewById(R.id.et_count);
			EditText unit_weight = (EditText) linearlayout.findViewById(R.id.et_unit);
			EditText total_weight = (EditText) linearlayout.findViewById(R.id.et_total); // 总重
			EditText et_long = (EditText) linearlayout.findViewById(R.id.et_long); // 长
			EditText et_height = (EditText) linearlayout.findViewById(R.id.et_width); // 高
			EditText et_width = (EditText) linearlayout.findViewById(R.id.et_height); // 宽
			EditText et_stere = (EditText) linearlayout.findViewById(R.id.et_stere); // 体积
			final Spinner sp = (Spinner) linearlayout.findViewById(R.id.spin_class);
			final Spinner sp2 = (Spinner) linearlayout.findViewById(R.id.spin_pack);
			String cag = ((SpinnerItem) sp.getSelectedItem()).getCargoType().toString();
			String cag2 = ((SpinnerItem) sp2.getSelectedItem()).getId().toString();
			spinner_type.add(cag);
			spinner_pack.add(cag2);
			// 添加到list中
			nameList.add(et_name.getText().toString());
			countList.add(et_count.getText().toString());
			unitList.add(unit_weight.getText().toString());
			totalList.add(total_weight.getText().toString());

			lengthList.add(et_long.getText().toString());
			widthList.add(et_height.getText().toString());
			heightList.add(et_width.getText().toString());
			volumeList.add(et_stere.getText().toString());
			if (TextUtils.isEmpty(et_count.getText().toString())) {
				Toast.makeText(this, "请输入货物数量", Toast.LENGTH_SHORT).show();
				return;
			} else if (TextUtils.isEmpty(et_name.getText().toString())) {
				Toast.makeText(this, "请输入货名", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		// params.put("cargoName", et_name.getText().toString());
		// params.put("cargoType",sp.getSelectedItem().toString());

		params.put("id", id);
		params.put("mmsi", DynamicAdapter.mmsi);
		params.put("wrapCount", countList.toString().substring(1, countList.toString().length() - 1));// 数量
		params.put("cargoName", nameList.toString().substring(1, nameList.toString().length() - 1));
		params.put("cargoType", spinner_type.toString().substring(1, spinner_type.toString().length() - 1));
		params.put("wrapStyle", spinner_pack.toString().substring(1, spinner_pack.toString().length() - 1));
		params.put("unitWeight", unitList.toString().substring(1, unitList.toString().length() - 1)); // 单重
		params.put("fullWeight", totalList.toString().substring(1, totalList.toString().length() - 1)); // 总重
		params.put("ctlLength", lengthList.toString().substring(1, lengthList.toString().length() - 1)); // 长
		params.put("ctlWidth", widthList.toString().substring(1, widthList.toString().length() - 1)); // 宽
		params.put("ctlHeight", heightList.toString().substring(1, heightList.toString().length() - 1)); // 高
		params.put("ctlVolume", volumeList.toString().substring(1, volumeList.toString().length() - 1)); // 体积
		params.put("tonTeu", totalList.toString().substring(1, totalList.toString().length() - 1)); // 总重/1000
		Log.v("params", params.toString());
		data.getApiResult(handler, "/mobile/state/saveShipApply", params, "post");
	}

	private void addGoodItem() {

		LayoutInflater mInflater = LayoutInflater.from(this);
		final LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.list_item_goods, null);
		Spinner spinner = (Spinner) view.findViewById(R.id.spin_class); // 货类
		Spinner spinner_pack = (Spinner) view.findViewById(R.id.spin_pack);// 包装
		EditText ed_count = (EditText) view.findViewById(R.id.et_count); // 数量
		final TextView tv_cargos = (TextView) view.findViewById(R.id.tv_cargos);
		final EditText et_Name = (EditText) view.findViewById(R.id.et_goods);
		EditText unit_weight = (EditText) view.findViewById(R.id.et_unit);
		EditText total_weight = (EditText) view.findViewById(R.id.et_total);
		EditText et_long = (EditText) view.findViewById(R.id.et_long);
		EditText et_height = (EditText) view.findViewById(R.id.et_width);
		EditText et_width = (EditText) view.findViewById(R.id.et_height);
		EditText et_stere = (EditText) view.findViewById(R.id.et_stere);

		ImageView ivDel = (ImageView) view.findViewById(R.id.iv_del_item);// 删除按钮

		ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(ShipUpdownActivity.this,
				R.layout.spinner_item, R.id.contentTextView, getTypes());
		spinner.setAdapter(adapter);

		ArrayAdapter<SpinnerItem> adapter2 = new ArrayAdapter<SpinnerItem>(ShipUpdownActivity.this,
				R.layout.spinner_item, R.id.contentTextView, getTypePack());
		spinner_pack.setAdapter(adapter2);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String cargo = parent.getItemAtPosition(position).toString();
				String jzx = CargoBigTypeCode.container.getDescription();
				String name[] = cargo.split("\\.");
				if (cargo.contains(jzx)) { // 选中的是集装箱
					tv_cargos.setText("规格");
					et_Name.setText(name[1]);
				} else {
					et_Name.setText(name[1]);
					tv_cargos.setText("货名");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		relayout.addView(view);
		autoCal(ed_count, unit_weight, total_weight, et_long, et_height, et_width, et_stere);

		ivDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				relayout.removeView(view);

			}
		});
	}

	private void autoCal(final EditText ed_count, final EditText unit_weight, final EditText total_weight,
			final EditText et_long, final EditText et_height, final EditText et_width, final EditText et_stere) {
		ed_count.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				ctUnitWeight = unit_weight.getText().toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				ctWrapCount = s.toString();
				if (!TextUtils.isEmpty(ctWrapCount) && !TextUtils.isEmpty(ctUnitWeight)) {
					Double total = Double.parseDouble(ctWrapCount) * Double.parseDouble(ctUnitWeight);
					double totals = CargoActivity.round(total, 3);
					total_weight.setText(String.valueOf(totals));
				}

			}
		});
		unit_weight.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ctUnitWeight = s.toString();
				if (!TextUtils.isEmpty(ctWrapCount) && !TextUtils.isEmpty(ctUnitWeight)) {
					Double total = Double.parseDouble(ctWrapCount) * Double.parseDouble(ctUnitWeight);
					double totals = CargoActivity.round(total, 3);
					total_weight.setText(String.valueOf(totals));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				ctWrapCount = ed_count.getText().toString();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		et_long.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				ctHeight = et_height.getText().toString();
				ctWdith = et_width.getText().toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				ctLong = s.toString();
				if (!TextUtils.isEmpty(ctLong) && !TextUtils.isEmpty(ctHeight) && !TextUtils.isEmpty(ctWdith)) {
					Double total = Double.parseDouble(ctLong) * Double.parseDouble(ctWdith)
							* Double.parseDouble(ctHeight);
					double totals = CargoActivity.round(total, 3);
					et_stere.setText(String.valueOf(totals));
				}

			}
		});
		et_height.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				ctHeight = et_height.getText().toString();
				ctWdith = et_width.getText().toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				ctHeight = s.toString();
				if (!TextUtils.isEmpty(ctLong) && !TextUtils.isEmpty(ctHeight) && !TextUtils.isEmpty(ctWdith)) {
					Double total = Double.parseDouble(ctLong) * Double.parseDouble(ctWdith)
							* Double.parseDouble(ctHeight);
					double totals = CargoActivity.round(total, 3);
					et_stere.setText(String.valueOf(totals));
				}
			}
		});
		et_width.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				ctLong = et_long.getText().toString();
				ctWdith = et_width.getText().toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				ctWdith = s.toString();
				if (!TextUtils.isEmpty(ctLong) && !TextUtils.isEmpty(ctHeight) && !TextUtils.isEmpty(ctWdith)) {
					Double total = Double.parseDouble(ctLong) * Double.parseDouble(ctWdith)
							* Double.parseDouble(ctHeight);
					double totals = CargoActivity.round(total, 3);
					et_stere.setText(String.valueOf(totals));
				}
			}
		});

	}

	// 货类
	private ArrayList<SpinnerItem> getTypes() {
		ArrayList<SpinnerItem> mTypes = new ArrayList<SpinnerItem>();

		for (CargoTypeCode e : CargoTypeCode.values()) {
			SpinnerItem sp = new SpinnerItem(e, e.getDescription());
			mTypes.add(sp);
		}
		return mTypes;
	}

	private ArrayList<SpinnerItem> getTypePack() {
		ArrayList<SpinnerItem> mTypes = new ArrayList<SpinnerItem>();

		for (WrapStyleCode e : WrapStyleCode.values()) {
			SpinnerItem sp = new SpinnerItem(e + "", e.getDescription());
			mTypes.add(sp);
		}

		return mTypes;
	}

	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			super.onStart();
			dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
		}

		@Override
		public void onSuccess(String arg0) {
			super.onSuccess(arg0);
			dialog.dismiss();
			Gson gson = new Gson();
			HashMap<String, Object> result = gson.fromJson((String) arg0, new TypeToken<Map<String, Object>>() {
			}.getType());
			if (result.get("returnCode").equals("Success")) {
				startActivity(new Intent(getApplicationContext(), ArvLftListActivity.class));
				finish();
				Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "提交失败！", Toast.LENGTH_SHORT).show();

			}
		}

		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			dialog.dismiss();
			if (content != null && content.equals("can't resolve host"))
				Toast.makeText(ShipUpdownActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
			else if (content != null && content.equals("socket time out")) {
				Toast.makeText(ShipUpdownActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
			} else if (content != null) {
				Log.e("error", content);
				Toast.makeText(ShipUpdownActivity.this, content, Toast.LENGTH_LONG).show();
			} else
				Toast.makeText(ShipUpdownActivity.this, "请求失败", Toast.LENGTH_LONG).show();

		}

	};
}
