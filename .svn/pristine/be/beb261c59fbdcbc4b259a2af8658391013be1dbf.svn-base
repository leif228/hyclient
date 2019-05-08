package com.eyunda.third.activities.user;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.user.UserChildManager;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UserChildActivity extends CommonListActivity implements
		OnClickListener {
	private EditText login_name, true_name, nick_name, basic_email,
			basic_phone,password,sedpassword,ships;
	private Button basic_submit, basic_back ,select_ship,type;
	private TextView textView6,textView7;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	UserData user;
	protected String mmsis = "";//服务端过来的
	private String  ms="";//本地选择的
	private String shipNames="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_modifyuserchild);

		user = (UserData) getIntent().getSerializableExtra("user");

		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		initial();
		setUpView();
		getSelectShips(user);
	}

	private void getSelectShips(UserData user) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("查询中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				try {
					dialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						mmsis  = (String) map.get("content");

					} else {
						Toast.makeText(UserChildActivity.this,
								map.get("message").toString(), 1).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")) {
					UserChildActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(UserChildActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		};
		// 手动登入
		Map<String, Object> params = new HashMap<String, Object>();
		if(user==null)
			return;
		params.put("childId", UserChildManager.getInstance().getChildId(user.getId()));
		data.getApiResult(handler, "/mobile/account/getChild",
				params);

			
	}

	private void initial() {
		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_back = (Button) this.findViewById(R.id.basic_back);
		select_ship = (Button) this.findViewById(R.id.select_ship);
		type = (Button) this.findViewById(R.id.type);
		select_ship.setOnClickListener(this);
		basic_submit.setOnClickListener(this);
		basic_back.setOnClickListener(this);

		login_name = (EditText) this.findViewById(R.id.login_name);
		true_name = (EditText) this.findViewById(R.id.true_name);
		nick_name = (EditText) this.findViewById(R.id.nick_name);
		basic_email = (EditText) this.findViewById(R.id.basic_email);
		basic_phone = (EditText) this.findViewById(R.id.basic_phone);
		password = (EditText) this.findViewById(R.id.password);
		sedpassword = (EditText) this.findViewById(R.id.sedpassword);
		ships = (EditText) this.findViewById(R.id.ships);
		password.setVisibility(View.GONE);
		sedpassword.setVisibility(View.GONE);
		textView6 = (TextView) this.findViewById(R.id.textView6);
		textView7 = (TextView) this.findViewById(R.id.textView7);
		textView6.setVisibility(View.GONE);
		textView7.setVisibility(View.GONE);
	}

	private void setUpView() {
		if(user!=null){
			login_name.setText(user.getLoginName());
			true_name.setText(user.getTrueName());
			nick_name.setText(user.getNickName());
			basic_email.setText(user.getEmail());
			basic_phone.setText(user.getMobile());
			ships.setText(user.getShips());
			if(!user.getShips().equals("")){
				type.setText("船员");
				select_ship.setVisibility(View.VISIBLE);
				ships.setVisibility(View.VISIBLE);
			}else{
				type.setText("业务员");
				select_ship.setVisibility(View.INVISIBLE);
				ships.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("子账号详情");
	}
	private void check() {
		if (TextUtils.isEmpty(login_name.getText())) {
			Toast.makeText(this, "登入名不能为空！", Toast.LENGTH_SHORT).show();
			login_name.requestFocus();
			return;
		} else if (TextUtils.isEmpty(basic_phone.getText())) {
			Toast.makeText(this, "手机不能为空！", Toast.LENGTH_SHORT).show();
			basic_phone.requestFocus();
			return;
		} else if (basic_phone.getText()!=null&&basic_phone.getText().toString().length()!=11) {
			Toast.makeText(this, "手机号长度为11位！", Toast.LENGTH_SHORT).show();
			basic_phone.requestFocus();
			return;
		}
		loadDate();
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
			if(UserChildManager.getInstance().getShipList().isEmpty()){
				Toast.makeText(this, "没有加载到船舶数据!", Toast.LENGTH_LONG).show();
				break;
			}
			Intent intent =new Intent(this, UserChildSelShipActivity.class);
			if(!"".equals(ms)){
				intent.putExtra("selmmsis", ms);
				intent.putExtra("mmsis", mmsis);
			}else{
				intent.putExtra("mmsis", mmsis);
			}
			startActivityForResult(intent,UserChildSelShipActivity.REQUEST_CODE_SELECT);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			// 重写签名
			if (requestCode == UserChildSelShipActivity.REQUEST_CODE_SELECT ) {
				ms = data.getStringExtra("mmsis");
				 shipNames = data.getStringExtra("shipNames");
				 ships.setText(shipNames);
			}
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
				try {
					dialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						Toast.makeText(UserChildActivity.this, "子账号修改成功", 1)
								.show();
						Intent intent = new Intent(UserChildActivity.this,
								UserChildListActivity.class);
						startActivity(intent);
						finish();

					} else {
						Toast.makeText(UserChildActivity.this,
								map.get("message").toString(), 1).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")) {
					UserChildActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(UserChildActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		};
		// 手动登入
		Map<String, Object> params = new HashMap<String, Object>();
		if(user==null)
			return;
		params.put("childId", UserChildManager.getInstance().getChildId(user.getId()));
		params.put("id", GlobalApplication.getInstance().getUserData().getId());
		params.put("loginName", login_name.getText());
		params.put("trueName", true_name.getText());
		params.put("nickName", nick_name.getText());
		params.put("email", basic_email.getText());
		params.put("mobile", basic_phone.getText());
		if(ms.equals("")&&mmsis.length()>0)
		  params.put("mmsis", mmsis);
		else if(!ms.equals(""))
			params.put("mmsis", ms);
		data.getApiResult(handler, "/mobile/account/save",
				params);

	}
}
