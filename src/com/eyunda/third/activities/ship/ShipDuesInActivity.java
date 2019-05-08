package com.eyunda.third.activities.ship;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.pay.ShipDuesPaymentActivity;
import com.eyunda.third.activities.ship.widget.DuesDatePickerFragment;
import com.eyunda.third.activities.ship.widget.DuesDatePickerFragment.DuesCallBack;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.enumeric.ComboCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.pay.PayEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipDuesInActivity extends CommonListActivity implements
		OnClickListener, DuesCallBack {
	private Spinner spinner_account_way = null;
	private Button account_submit, account_back;
	private TextView shipNametv, tv_start, tv_end, money;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	String selected;
	Button btnD;

	private String shipId = "";
	private String shipName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_ship_activity_duesin);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		spinner_account_way = (Spinner) findViewById(R.id.spinner_account_way);

		account_submit = (Button) this.findViewById(R.id.account_submit);
		account_back = (Button) this.findViewById(R.id.account_back);
		account_submit.setOnClickListener(this);
		account_back.setOnClickListener(this);

		shipNametv = (TextView) this.findViewById(R.id.shipName);
		tv_start = (TextView) this.findViewById(R.id.et_start);
		tv_end = (TextView) this.findViewById(R.id.et_end);
		money = (TextView) this.findViewById(R.id.money);

		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		shipId = (String) bundle.getString("shipId");
		shipName = (String) bundle.getString("shipName");

		setview();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setview() {
		shipNametv.setText(shipName);
		List<String> list_spinner_account_way = new ArrayList<String>();
		for (ComboCode p : ComboCode.values()) {
			list_spinner_account_way.add(p.getDescription());
		}
		ArrayAdapter adapter_spinner_account_way = new ArrayAdapter(this,
				R.layout.spinner_item, R.id.contentTextView,
				list_spinner_account_way);
		spinner_account_way.setAdapter(adapter_spinner_account_way);
		spinner_account_way
				.setOnItemSelectedListener(new SpinnerOnSelectedListener());
		
		AlertDialog();
	}

	private String getEndMonth(Calendar startTime, int months) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月",
				Locale.CHINA);
		Calendar c = startTime;
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + months;
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.set(year, month, day);
		String date = sDateFormat.format(c.getTime());
		return date;
	}

	@Override
	public void getStartTime(Calendar startTime) {
		setEndMonth(startTime,selected);
	}
	
	private void setEndMonth(Calendar startTime, String comboSelected){
		if ("一年".equals(comboSelected)) {
			tv_end.setText(getEndMonth(startTime, 12-1));
			money.setText("960.0 元");
		} else if ("半年".equals(comboSelected)) {
			tv_end.setText(getEndMonth(startTime, 6-1));
			money.setText("510.0 元");
		} else if ("一季度".equals(comboSelected)) {
			tv_end.setText(getEndMonth(startTime, 3-1));
			money.setText("270.0 元");
		} else if ("一个月".equals(comboSelected)) {
			tv_end.setText(getEndMonth(startTime, 1-1));
			money.setText("100.0 元");
		}
	}

	protected void AlertDialog() {
		
		DuesDatePickerFragment datePicker = new DuesDatePickerFragment(tv_start);
		DuesDatePickerFragment datePicker2 = new DuesDatePickerFragment(tv_end);
		tv_start.setText(datePicker.getCurrentTime());
		tv_end.setText(datePicker2.getNextYear());
		money.setText("960.0 元");
		
		tv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DuesDatePickerFragment datePicker = new DuesDatePickerFragment(
						tv_start);
				datePicker.setCallfuc(ShipDuesInActivity.this);
				datePicker.show(getFragmentManager(), "datePicker");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_submit:
			loadDate();
			break;
		case R.id.account_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("缴费");
	}

	class SpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			selected = adapterView.getItemAtPosition(position).toString();
			
			String startStr = tv_start.getText().toString();
			if(startStr!=null&&!"".equals(startStr)){
				String arr[] = startStr.split("年");
				if(arr.length==2){
					Calendar c = Calendar.getInstance();
					int year = Integer.valueOf(arr[0]);
					int month = Integer.valueOf(arr[1].substring(0, arr[1].length()-1))-1;
					int day = 1;
					c.set(year, month, day);
					setEndMonth(c,selected);
				}else
				   Toast.makeText(ShipDuesInActivity.this,"开始年月 字符串格式不对", 1).show();
			}else
				Toast.makeText(ShipDuesInActivity.this,"开始年月为空", 1).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}

	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					
					 Bundle bundle = new Bundle();
					 bundle.putString("shipId", shipId);
					 bundle.putString("shipName", shipName);
					 startActivity(new Intent(ShipDuesInActivity.this,ShipDuesActivity.class).putExtras(bundle));
					
					finish();
				} else {
					Toast.makeText(ShipDuesInActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipDuesInActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipDuesInActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(ShipDuesInActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipDuesInActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();

			}

		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shipId", Long.valueOf(shipId));
		params.put("startMonth", tv_start.getText().toString());
		params.put("endMonth", tv_end.getText().toString());
		params.put("money", Double.valueOf(money.getText().toString().replace("元", "")));
		if (selected != null) {
			for (ComboCode p : ComboCode.values()) {
				if (p.getDescription().equals(selected))
					params.put("combo", p);
			}
		} else {
			params.put("combo", ComboCode.year);
		}

		data.getApiResult(handler, "/mobile/ship/myShip/dues/inaccount",
				params);
	}

}
