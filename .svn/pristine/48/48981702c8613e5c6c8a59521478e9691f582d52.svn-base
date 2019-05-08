package com.eyunda.third.activities.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.enumeric.PayStyleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AddAccountActivity extends CommonListActivity implements
OnClickListener{
	private Spinner spinner_account_way = null;
	// private Spinner spinner_account_bank = null;
	private Button account_submit, account_back;
	private EditText et_account_name, et_account_num;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	String selected;
	Button btnD;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_account_info);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		spinner_account_way = (Spinner) findViewById(R.id.spinner_account_way);
		// spinner_account_bank = (Spinner)
		// findViewById(R.id.spinner_account_bank);

		account_submit = (Button) this.findViewById(R.id.account_submit);
		account_back = (Button) this.findViewById(R.id.account_back);
		account_submit.setOnClickListener(this);
		account_back.setOnClickListener(this);

		et_account_name = (EditText) this.findViewById(R.id.et_account_name);
		// et_account_subbank = (EditText)
		// this.findViewById(R.id.et_account_subbank);
		et_account_num = (EditText) this.findViewById(R.id.et_account_num);

		setview();
	}

	@SuppressWarnings("unchecked")
	private void setview() {
		List<String> list_spinner_account_way = new ArrayList<String>();
//		if (accountData != null) {
//			// spinner
//			list_spinner_account_way.add(accountData.getPayStyle()
//					.getDescription());
//			for (PayStyleCode p : PayStyleCode.values()) {
//				if (p.toString().equals(accountData.getPayStyle().toString()))
//					continue;
//				list_spinner_account_way.add(p.getDescription());
//			}
//			// others
//			et_account_name.setText(accountData.getAccounter());
//			et_account_num.setText(accountData.getAccountNo());
//		} else {
			for (PayStyleCode p : PayStyleCode.values()) {
				list_spinner_account_way.add(p.getDescription());
			}
//		}
		ArrayAdapter adapter_spinner_account_way = new ArrayAdapter(this,
				R.layout.spinner_item, R.id.contentTextView,
				list_spinner_account_way);
		spinner_account_way.setAdapter(adapter_spinner_account_way);
		spinner_account_way.setPrompt("支付方式");
		spinner_account_way
				.setOnItemSelectedListener(new SpinnerOnSelectedListener());
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
		setTitle("添加账户");
	}

	class SpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			selected = adapterView.getItemAtPosition(position).toString();
			System.out.println(selected);
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
			System.out.println("nothingSelected");
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

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Log.i("userinfo", content);
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					Toast.makeText(AddAccountActivity.this, "信息提交成功", 1)
							.show();
					finish();
					//UserSetManager.getInstance().synLoadDate();
				} else {
					Toast.makeText(AddAccountActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(AddAccountActivity.this, "信息提交失败", 1).show();
				dialog.dismiss();

			}

		};
		Map<String, Object> params = new HashMap<String, Object>();
//		if (accountData != null) {
//			params.put("id", accountData.getId());
//			params.put("payStyle", accountData.getPayStyle());
//		}
		params.put("userId", GlobalApplication.getInstance().getUserData()
				.getId());
		params.put("accounter", et_account_name.getText());
		params.put("accountNo", et_account_num.getText());
		if (selected != null) {
			for (PayStyleCode p : PayStyleCode.values()) {
				if (p.getDescription().equals(selected))
					params.put("payStyle", p);
			}
		}else{
			params.put("payStyle", PayStyleCode.alipay);
		}

		data.getApiResult(handler, "/mobile/account/myAccount/saveBankInfo",
				params);
	}
}
