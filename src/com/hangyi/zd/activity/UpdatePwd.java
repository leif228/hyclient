package com.hangyi.zd.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.account.BankListActivity;
import com.eyunda.third.activities.account.BindBankCardActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UpdatePwd extends CommonActivity {
	Data_loader data;
	EditText pwd1, pwd2, pwd3;
	Button submit;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_user_updatepwd);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
		sp = getSharedPreferences( "UserInfoConfig", MODE_PRIVATE);

		pwd1 = (EditText) findViewById(R.id.pwd1);
		pwd2 = (EditText) findViewById(R.id.pwd2);
		pwd3 = (EditText) findViewById(R.id.pwd3);
		submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pwd1.getText().toString().equals("")) {

					Toast.makeText(UpdatePwd.this, "原密码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (pwd2.getText().toString().equals("")) {

					Toast.makeText(UpdatePwd.this, "新密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (pwd3.getText().toString().equals("")) {

					Toast.makeText(UpdatePwd.this, "确认密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!pwd3.getText().toString()
						.equals(pwd2.getText().toString())) {

					Toast.makeText(UpdatePwd.this, "两次密码不相同",
							Toast.LENGTH_SHORT).show();
					return;
				}
				loadData();
			}
		});
		
	}


	@Override
	protected void onStart() {
		super.onStart();
		setTitle("修改密码");
	}
	private void saveOptionInfo() {		
		SharedPreferences sp = this.getSharedPreferences("UserInfoConfig", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		String userPassword = pwd3.getText().toString().trim();
		editor.putString("UserPassword", userPassword);
		
		editor.commit();
	}
	private void loadData() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("username", sp.getString("UserName", ""));
		apiParams.put("oldpasswd", pwd1.getText().toString().trim());
		apiParams.put("newpasswd", pwd3.getText().toString().trim());
		
		data.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("提交中", "请稍候...");
			}
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				dialog.dismiss();
				
				if("0".equals(content)){
					saveOptionInfo();
					finish();
				}else if("-1".equals(content)) {
					Toast.makeText(UpdatePwd.this, "原密码错误",
							Toast.LENGTH_LONG).show();
				}else if("-3".equals(content)) {
					Toast.makeText(UpdatePwd.this, "找不到用户",
							Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(UpdatePwd.this, "未知错误",
							Toast.LENGTH_LONG).show();
				}
				
								
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(UpdatePwd.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(UpdatePwd.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(UpdatePwd.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(UpdatePwd.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.login, apiParams, "post");
	
	}
}
