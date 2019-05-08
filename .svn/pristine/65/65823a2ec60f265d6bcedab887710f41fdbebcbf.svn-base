package com.eyunda.third.activities.home;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.oil.GasOrderActivity;
import com.eyunda.third.adapters.user.SpinnerAdapter;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.oil.GasWaresData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class GasOrderInActivity extends CommonListActivity implements
		OnClickListener {
	private Spinner spinner_account_way = null;
	private Button account_submit, account_back;
	private TextView wid, wname, price;
	private EditText count, tprice;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	String selected;
	Button btnD;

	private String wId = "";
//	private List<ShipNameData> list_ships = null;
//	private SpinnerAdapter adapter_spinner_account_way = null;
	private GasWaresData gwd = null;
//	private String shipId;
//	protected String shipName;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_gasorderin);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
//		spinner_account_way = (Spinner) findViewById(R.id.spinner_account_way);
//		list_ships = new ArrayList<ShipNameData>();
//		adapter_spinner_account_way = new SpinnerAdapter(this,
//				R.layout.spinner_item,R.id.contentTextView, list_ships);
//		spinner_account_way.setAdapter(adapter_spinner_account_way);
//		spinner_account_way
//				.setOnItemSelectedListener(new SpinnerOnSelectedListener());

		account_submit = (Button) this.findViewById(R.id.account_submit);
		account_back = (Button) this.findViewById(R.id.account_back);
		account_submit.setOnClickListener(this);
		account_back.setOnClickListener(this);

		wid = (TextView) this.findViewById(R.id.wid);
		wname = (TextView) this.findViewById(R.id.wname);
		price = (TextView) this.findViewById(R.id.price);
		count = (EditText) this.findViewById(R.id.count);
		tprice = (EditText) this.findViewById(R.id.tprice);
		
		count.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
					totalPrice();
			}
		});

		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		wId = (String) bundle.getString("id");

		loadGet();
	}

	protected void totalPrice() {
		if(!TextUtils.isEmpty(count.getText().toString()) && !TextUtils.isEmpty(price.getText().toString())){
			
			try {
				int num = Integer.parseInt(count.getText().toString());
				if (num > 0) {
					Double fee = Double.parseDouble(count.getText().toString()) * Double.parseDouble(price.getText().toString());
//			        tprice.setText(String.valueOf(DataConvert.getDataWithTwo(fee)));
					tprice.setText(String.valueOf(fee));
				}else
					throw new Exception();
			} catch (Exception e) {
				Toast.makeText(GasOrderInActivity.this, "请正确填写购买数量!",
						Toast.LENGTH_LONG).show();
				count.setText("");
				count.findFocus();
				return;
			}
			
		}
	}

	private void loadGet() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("通知", "请稍候...");
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
					HashMap<String, Object> var = (HashMap<String, Object>) map
							.get("content");
//					List<HashMap<String, Object>> mships = (List<HashMap<String, Object>>) var
//							.get("ships");
					HashMap<String, Object> mgasWaresData = (HashMap<String, Object>) var
							.get("gasWaresData");

//					list_ships.clear();
//					int i=0;
//					for (HashMap<String, Object> m : mships) {
//						ShipNameData snd = new ShipNameData(m);
//						if(i==0){
//							shipId = snd.getId().toString();
//							shipName = snd.getShipName();
//						}
//						i++;
//						list_ships.add(snd);
//					}
					gwd = new GasWaresData(mgasWaresData);
					refrush();
				} else {
					Toast.makeText(GasOrderInActivity.this,
							map.get("message").toString(), 1).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(GasOrderInActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(GasOrderInActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(GasOrderInActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(GasOrderInActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.valueOf(wId));
		data.getApiResult(handler, "/mobile/home/gasWares", params,"get");
	}

	protected void refrush() {
		GasOrderInActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				adapter_spinner_account_way.notifyDataSetChanged();
				if (gwd != null) {
					wid.setText(gwd.getId().toString());
					wname.setText(gwd.getWaresName());
					price.setText(gwd.getPrice().toString());
					count.setText("1");
					tprice.setText(gwd.getPrice().toString());
				}
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
		setTitle("购买页面");
	}

//	class SpinnerOnSelectedListener implements OnItemSelectedListener {
//
//		@Override
//		public void onItemSelected(AdapterView<?> adapterView, View view,
//				int position, long id) {
////			selected = adapterView.getItemAtPosition(position).toString();
//			ShipNameData s = adapter_spinner_account_way.getItem(position);
//			shipId = s.getId().toString();
//			shipName = s.getShipName();
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> adapterView) {
//		}
//
//	}

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
//					Bundle bundle = new Bundle();
//					bundle.putString("shipId", shipId);
//					bundle.putString("shipName", shipName);
					startActivity(new Intent(GasOrderInActivity.this,
							GasOrderActivity.class));

					finish();
				} else {
					Toast.makeText(GasOrderInActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(GasOrderInActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(GasOrderInActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(GasOrderInActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(GasOrderInActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();

			}

		};
		if(TextUtils.isEmpty(price.getText().toString()))
			return;
//		if(shipId==null){
//			Toast.makeText(GasOrderInActivity.this, "没有加油船舶不能购买!",
//					Toast.LENGTH_LONG).show();
//			return;
//		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (count.getText() == null || count.getText().toString().equals("")) {
			Toast.makeText(GasOrderInActivity.this, "请填写购买数量!",
					Toast.LENGTH_LONG).show();
			return;
		}
		String st = count.getText().toString();
		try {
			int num = Integer.parseInt(st);
			if (num > 0) {
				params.put("saleCount", num);
			}
		} catch (Exception e) {
			Toast.makeText(GasOrderInActivity.this, "请正确填写购买数量!",
					Toast.LENGTH_LONG).show();
			return;
		}
		params.put("waresId", wId);
//		params.put("shipId", shipId);

		data.getApiResult(handler, "/mobile/home/gasWaresBuy", params);
	}

}
