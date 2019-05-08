package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.activities.ship.widget.DateTimePickDialogUtil;
import com.eyunda.third.adapters.ship.DynamicAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.AreaSelect;
import com.eyunda.tools.LocalFileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShipArvlftActivity extends CommonActivity implements
		OnClickListener {

	private TextView arrive;
	private TextView left;
	private TextView portData;
	private Button save;
	private RelativeLayout test_pop_layout;
	private Data_loader data = new Data_loader();
	private String id;
	private TextView goPortData;
	private AreaSelect asStart;
	private AreaSelect asEnd;
	private String arrived;
	private String leave;
	private EditText remark;
	private ArrayList<Map<String, Object>> all;
	private int curPosition;
	private String idNo;
	private Button addStart;
	private Button addEnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_ship_arvlft);
		initViews();

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("动态编辑");
	}

	private void initViews() {

		test_pop_layout = (RelativeLayout) findViewById(R.id.pop_layout);
		arrive = (TextView) findViewById(R.id.arrive_port);
		left = (TextView) findViewById(R.id.left_port);
		portData = (TextView) findViewById(R.id.ports);
		goPortData = (TextView) findViewById(R.id.goPort);
		remark = (EditText) findViewById(R.id.remark);
		save = (Button) findViewById(R.id.btn_save_report);
		addStart = (Button) findViewById(R.id.addStart);
		addEnd = (Button) findViewById(R.id.addEnd);
		arrive.setOnClickListener(this);
		left.setOnClickListener(this);
		addStart.setOnClickListener(this);
		addEnd.setOnClickListener(this);
		portData.setOnClickListener(this);
		goPortData.setOnClickListener(this);
		save.setOnClickListener(this);
		asStart = new AreaSelect(this, portData);
		asEnd = new AreaSelect(this, goPortData);
		curPosition = getIntent().getIntExtra("cur", -1);
		all = (ArrayList<Map<String, Object>>) getIntent()
				.getSerializableExtra("all");
		// 1.新增，2，修改最后一条
		if (all != null) {
			if (all.get(curPosition).equals(all.get(0))) {// 修改最后一条
				idNo = all.get(0).get("id").toString();
			} else
				// 非最后一条
				id = all.get(curPosition).get("id").toString();
		} else {// 新增
			idNo = getIntent().getStringExtra("id");

		}
		initData();
	}

	// if(avf!=null){
	// id =avf.getArriveData().getId();
	// }else
	// id =getIntent().getLongExtra("id", 0);

	private void initData() {

		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) cd
							.getContent();
					HashMap<String, Object> arv = (HashMap<String, Object>) content
							.get("arriveData");
					HashMap<String, Object> lef = (HashMap<String, Object>) content
							.get("leftData");
					HashMap<String, Object> add = (HashMap<String, Object>) content
							.get("addShipArvlft");

					ShipStopData arvData = new ShipStopData(arv);
					ShipStopData leftData = new ShipStopData(lef);
					ShipStopData addData = new ShipStopData(add);
					// DateTimePickDialogUtil du = new
					// DateTimePickDialogUtil(ShipArvlftActivity.this);
					// if(arvData.getArvlftTime().equals("")){
					// arrive.setText(du.getCurrentTime());
					// }else
					arrive.setText(arvData.getArvlftTime());
					left.setText(leftData.getArvlftTime());
					remark.setText(arvData.getRemark());
					if (arvData.getGoPortData() != null) {
						portData.setText(arvData.getPortData().getFullName());
						portData.setTag(arvData.getPortData().getPortNo());
					}
					if (leftData.getGoPortData() != null) {
						goPortData.setText(leftData.getGoPortData()
								.getFullName());
						goPortData.setTag(leftData.getGoPortData().getPortNo());
					}
					if (addData.getGoPortData() != null) {
						portData.setText(addData.getGoPortData().getFullName());
						portData.setTag(addData.getGoPortData().getPortNo());
					}
				} else {
					Toast.makeText(getApplicationContext(), cd.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_LONG)
						.show();

			}
		};
		if (id != null) { // 不是最后一条
			params.put("id", id);
			if (id.equals("0")) { // 新增
				params.put("id", idNo);
			}
		} else { // 最后一条
			params.put("id", idNo);
		}
		params.put("shipId", DynamicAdapter.shipId);
		params.put("mmsi", DynamicAdapter.mmsi);
		data.getApiResult(handler,
				"/mobile/state/myShip/shipApply/editShipArvlft", params, "get");

	}

	@Override
	public void onClick(View v) {
		DateTimePickDialogUtil dp = new DateTimePickDialogUtil(this, "");
		switch (v.getId()) {
		case R.id.arrive_port:
			dp.dateTimePicKDialog(arrive);
			break;

		case R.id.left_port:
			dp.dateTimePicKDialog(left);

			break;
		case R.id.ports:

//			PopupWindow popupWindow = asStart.makePopupWindow();
			int[] xy = new int[2];
			test_pop_layout.getLocationOnScreen(xy);
			int localHeight = asStart.getHeight();
//			popupWindow.showAtLocation(test_pop_layout, Gravity.CENTER
//					| Gravity.BOTTOM, 0, -localHeight);
			break;
		case R.id.goPort:

//			PopupWindow popupWindow2 = asEnd.makePopupWindow();
			int[] xy2 = new int[2];
			test_pop_layout.getLocationOnScreen(xy2);
			int localHeight2 = asEnd.getHeight();
//			popupWindow2.showAtLocation(test_pop_layout, Gravity.CENTER
//					| Gravity.BOTTOM, 0, -localHeight2);
			break;
		case R.id.btn_save_report:
			arrived = arrive.getText().toString();
			leave = left.getText().toString();
			if (TextUtils.isEmpty(arrived)) {
				Toast.makeText(this, "请输入到港时间", Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(portData.getText().toString())) {
				Toast.makeText(this, "请输入到港港口名", Toast.LENGTH_SHORT).show();
				return;
			}

			saveAfvLft();
			break;
		case R.id.addStart:
		case R.id.addEnd:
			LayoutInflater inflater = LayoutInflater
					.from(ShipArvlftActivity.this);
			View view = inflater.inflate(R.layout.college_item, null);
			final EditText et = (EditText) view.findViewById(R.id.port);
			final Spinner sp = (Spinner) view.findViewById(R.id.city);
			ArrayList<SpinnerItem> items = new ArrayList<SpinnerItem>();
			for (PortCityCode p : PortCityCode.values()) {
				SpinnerItem si = new SpinnerItem(p.getCode(),
						p.getDescription());
				items.add(si);
			}
			ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(
					this, android.R.layout.simple_spinner_item, items);
			sp.setAdapter(adapter);
			new AlertDialog.Builder(ShipArvlftActivity.this)
					.setTitle("新增港口")
					.setView(view)
					.setNegativeButton("取消", null)
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String port = et.getText().toString();
									String cityCode = ((SpinnerItem) sp
											.getSelectedItem()).getId();
									addNew(port, cityCode);

								}

								private void addNew(final String port,
										final String cityCode) {
									AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
										@Override
										public void onSuccess(String arg0) {
											super.onSuccess(arg0);
											Gson gson = new Gson();
											HashMap<String, Object> result = gson
													.fromJson(
															(String) arg0,
															new TypeToken<Map<String, Object>>() {
															}.getType());
											if (result.get("returnCode")
													.equals("Success")) {
												LocalFileUtil
														.getAreas(getApplicationContext());
												Toast.makeText(
														ShipArvlftActivity.this,
														(CharSequence) result
																.get("message"),
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(
														ShipArvlftActivity.this,
														(CharSequence) result
																.get("message"),
														Toast.LENGTH_SHORT)
														.show();
											}
										}

										@Override
										public void onFailure(Throwable arg0,
												String arg1) {
											// TODO Auto-generated method stub
											super.onFailure(arg0, arg1);
										}
									};
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("portCityCode", cityCode);
									params.put("portName", port);
									data.getApiResult(handler,
											"/mobile/ship/myShip/saveNewPort",
											params, "get");

								}
							}).show();
			break;

		}

	}

	private void saveAfvLft() {
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
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Toast.makeText(getApplicationContext(), cd.getMessage(),
							Toast.LENGTH_LONG).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), cd.getMessage(),
							Toast.LENGTH_LONG).show();

				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				dialog.dismiss();

			}
		};
		Map<String, Object> params = new HashMap<String, Object>();

		if (id != null && !id.equals("0")) {// 不是最后一条 和新增,编辑
			if (portData.getText().equals("")
					|| goPortData.getText().equals("")) {
				Toast.makeText(getApplicationContext(), "必须选择离港时间和将去港口!",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				params.put("id", id);
			}

		} else {
			params.put("id", idNo);
			params.put("mmsi", DynamicAdapter.mmsi);
			params.put("arriveTime", arrived);
			params.put("leftTime", leave);
			params.put("remark", remark.getText().toString());
			params.put("portNo", portData.getTag().toString());
			if (goPortData.getTag() != null)
				params.put("goPortNo", goPortData.getTag().toString());
			data.getApiResult(handler,
					"/mobile/state/myShip/shipApply/saveShipArvlft", params,
					"post");
		}
	}

}
