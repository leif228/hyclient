package com.eyunda.third.activities.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.user.UserChildManager;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AddUserChildActivity extends CommonListActivity implements
		OnClickListener {
	private EditText login_name, true_name, password,
			sedpassword, basic_phone, ships;
	private Button basic_submit, basic_back, select_ship;
	private Spinner spinner_type = null;
	private RelativeLayout old_ll = null;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	private String mmsis = "";
	private String shipNames = "";
	String selected = "";

	// private String loginname="";
	// private String truename="";
	// private String nickname="";
	// private String basicemail="";
	// private String basicphone="";
	// private String spassword="";
	
	private String agentSel="";
	private String userRole=UserRoleCode.sailor.name();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_adduserchild);
		
		agentSel = getIntent().getStringExtra("agentSel");
		old_ll = (RelativeLayout) findViewById(R.id.old_ll);
		if("new".equals(agentSel))
			old_ll.setVisibility(View.VISIBLE);
		else
			old_ll.setVisibility(View.GONE);
		
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		spinner_type = (Spinner) findViewById(R.id.spinner_type);
		initial();

		List<String> list = new ArrayList<String>();
		UserData userData = GlobalApplication.getInstance().getUserData();
		if(userData!=null){
//			if(userData.isConsigner()&&!userData.isCarrier()){
//				list.add(UserRoleCode.handler.getDescription());
//				select_ship.setVisibility(View.INVISIBLE);
//				ships.setVisibility(View.INVISIBLE);
//			}else{
//				list.add(UserRoleCode.sailor.getDescription());
//				list.add(UserRoleCode.handler.getDescription());
//			}
			if(userData.isRealUser()){
				list.add(UserRoleCode.sailor.getDescription());
				list.add(UserRoleCode.handler.getDescription());
			}
		}
		ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, list);
		spinner_type.setAdapter(adapter);
		spinner_type.setOnItemSelectedListener(new SpinnerOnSelectedListener());

	}

	class SpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			selected = adapterView.getItemAtPosition(position).toString();
			if (selected.equals(UserRoleCode.sailor.getDescription())) {
				AddUserChildActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						userRole=UserRoleCode.sailor.name();
						select_ship.setVisibility(View.VISIBLE);
						ships.setVisibility(View.VISIBLE);
					}
				});
			} else if (selected.equals(UserRoleCode.handler.getDescription())) {
				AddUserChildActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						userRole=UserRoleCode.handler.name();
						select_ship.setVisibility(View.INVISIBLE);
						ships.setVisibility(View.INVISIBLE);
					}
				});
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}

	}

	private void initial() {
		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_back = (Button) this.findViewById(R.id.basic_back);
		select_ship = (Button) this.findViewById(R.id.select_ship);
		basic_submit.setOnClickListener(this);
		select_ship.setOnClickListener(this);
		basic_back.setOnClickListener(this);

		login_name = (EditText) this.findViewById(R.id.login_name);
		true_name = (EditText) this.findViewById(R.id.true_name);
		basic_phone = (EditText) this.findViewById(R.id.basic_phone);
		password = (EditText) this.findViewById(R.id.password);
		sedpassword = (EditText) this.findViewById(R.id.sedpassword);
		ships = (EditText) this.findViewById(R.id.ships);
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("增加子账号");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.basic_submit:
			try {
				check();

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.basic_back:
			finish();
			break;
		case R.id.select_ship:
			if (UserChildManager.getInstance().getShipList().isEmpty()) {
				Toast.makeText(this, "没有加载到船舶数据!", Toast.LENGTH_LONG).show();
				break;
			}
			Intent intent = new Intent(this, UserChildSelShipActivity.class);
			if(!"".equals(mmsis)){
				intent.putExtra("selmmsis", mmsis);
			}
			startActivityForResult(intent,
					UserChildSelShipActivity.REQUEST_CODE_SELECT);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			// 重写签名
			if (requestCode == UserChildSelShipActivity.REQUEST_CODE_SELECT) {
				mmsis = data.getStringExtra("mmsis");
				shipNames = data.getStringExtra("shipNames");
				ships.setText(shipNames);
			}
		}
	}

	private void check() {
//		if ("old".equals(agentSel)&&TextUtils.isEmpty(login_name.getText())) {
//			Toast.makeText(this, "登入名不能为空！", Toast.LENGTH_SHORT).show();
//			login_name.requestFocus();
//			return;
//		} 

		if (TextUtils.isEmpty(basic_phone.getText())) {
			Toast.makeText(this, "手机不能为空！", Toast.LENGTH_SHORT).show();
			basic_phone.requestFocus();
			return;
		}
		if("new".equals(agentSel)&&TextUtils.isEmpty(true_name.getText())){
			Toast.makeText(this, "真实姓名不能为空！", Toast.LENGTH_SHORT).show();
			true_name.requestFocus();
			return;
		} else if ("new".equals(agentSel)&&TextUtils.isEmpty(password.getText())) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			password.requestFocus();
			return;

		} else if ("new".equals(agentSel)&&TextUtils.isEmpty(sedpassword.getText())) {
			Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
			sedpassword.requestFocus();
			return;
		} else if ("new".equals(agentSel)&&!password.getText().toString()
				.equals(sedpassword.getText().toString())) {
			Toast.makeText(this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		loadDate();
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
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						Toast.makeText(AddUserChildActivity.this, "子账号创建成功", 1)
								.show();
						Intent intent = new Intent(AddUserChildActivity.this,
								UserChildListActivity.class);
						startActivity(intent);
						finish();

					} else {
						Toast.makeText(AddUserChildActivity.this,
								map.get("message").toString(), 1).show();
					}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")) {
					Toast.makeText(AddUserChildActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("childSel", agentSel);
		params.put("userRole", userRole);//sailor handler
		params.put("mobile", basic_phone.getText());
		if("new".equals(agentSel)){
			params.put("trueName", true_name.getText());
			params.put("password", sedpassword.getText());
		}
		if (selected.equals(UserRoleCode.sailor.getDescription()) && !mmsis.equals(""))
			params.put("mmsis", mmsis);
		else if (selected.equals(UserRoleCode.sailor.getDescription()) && mmsis.equals("")) {
			Toast.makeText(this, "请选择上报的船舶", Toast.LENGTH_SHORT).show();
			return;
		}
		data.getApiResult(handler, "/mobile/account/save", params);

	}
}
