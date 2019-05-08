package com.eyunda.third.activities.user;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.main.SplashActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ForgetPasswdActivity extends Activity {
	EditText emailet;
	Data_loader data;
	DialogUtil dialogUtil;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_forgetpwd);
		
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
		// username = (EditText) findViewById(R.id.number);
		emailet = (EditText) findViewById(R.id.forgetpwd_email);
	}

	public void getUserPasswd(View view) {
		String email = emailet.getText().toString();
		if(email!=null)
			todo(email);
	}

	public void todo(String email) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("邮件发送中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				try {
					dialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson((String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						alert();
					} else {
						Toast.makeText(ForgetPasswdActivity.this, map.get("message").toString(),
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ForgetPasswdActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		data.getApiResult(handler, "/portal/login/email", params);
	}
	private void alert() {
		Dialog dialog = new android.app.AlertDialog.Builder(this)
				.setTitle("提示").setMessage("重置密码邮件已经发送，请登陆邮箱进行重置！")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						Intent intent = new Intent(ForgetPasswdActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
					}
				}).create();
		dialog.show();
	}

	public void back(View view) {
		finish();
	}
}