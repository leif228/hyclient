package com.hangyi.zd.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.loaders.Data_loader;
import com.hangyi.tools.Util;
import com.hangyi.zd.ClearService;
import com.hangyi.zd.ReLoginListenerService;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.PersistentCookieStore;


public class LoginActivity extends Activity implements OnClickListener {
	private EditText etUserName = null;
	private EditText etPassword = null;
	private ImageView iv_LoginBtn = null;
	private ProgressBar pbLogin = null;
	private CheckBox cb_SavePassword = null;
	private CheckBox cb_AutoLogin = null;
	
	ProgressDialog dialog;
	Data_loader data1;
	DialogUtil dialogUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zd_activity_login);  
		iv_LoginBtn = (ImageView) findViewById(R.id.iv_Login);
//		pbLogin = (ProgressBar) findViewById(R.id.pb_login);
//		cb_SavePassword = (CheckBox) findViewById(R.id.cb_SavePassword);
//		cb_AutoLogin = (CheckBox) findViewById(R.id.cb_AutoLogin);
		etUserName = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_passeord);
		iv_LoginBtn.setOnClickListener(this);
		
		dialogUtil = new DialogUtil(this);
		data1 = new Data_loader();	
		
//		InitData();
	}
	
//	private void InitData() {
//			SharedPreferences sp = this.getSharedPreferences("UserInfoConfig", Context.MODE_PRIVATE);
//			String sUserName = sp.getString("UserName", "");
//			String sUserPwd = sp.getString("UserPassword", "");
//			String sSavePassword = sp.getString("SavePassword", "false");
//			String sAutlLogin = sp.getString("AutoLogin", "false");
//			
//			boolean bIsAutoLogin = Boolean.parseBoolean(sAutlLogin);
//			cb_AutoLogin.setChecked(bIsAutoLogin);
//			etUserName.setText(sUserName);
//			
//			boolean bIsSavePassword = Boolean.parseBoolean(sSavePassword);
//			cb_SavePassword.setChecked(bIsSavePassword);
//			if(bIsSavePassword)
//				etPassword.setText(sUserPwd);
//			
//			if(bIsAutoLogin)
//				onClick(iv_LoginBtn);
//			
//	}

	private void saveOptionInfo() {		
		SharedPreferences sp = this.getSharedPreferences("UserInfoConfig", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
//		if(cb_AutoLogin.isChecked())
			editor.putString("AutoLogin", "true");
//		else
//			editor.putString("AutoLogin", "false");
		
//		if(cb_SavePassword.isChecked())
			editor.putString("SavePassword", "true");
//		else
//			editor.putString("SavePassword", "false");
		
		String userName = etUserName.getText().toString();
		editor.putString("UserName", userName);
		String userPassword = etPassword.getText().toString();
		editor.putString("UserPassword", userPassword);
		
		editor.commit();
	}

	@Override
	public void onClick(View v) {
		int nID = v.getId();
		switch (nID) {
		case R.id.iv_Login: {
//			etUserName.clearFocus();
//			etPassword.clearFocus();
			
			KeyBoard(etUserName, "close");
			KeyBoard(etPassword, "close");
			login();
			break;
		}
		default:
			break;
		}
	}
	
	// 强制显示或者关闭系统键盘
	public static void KeyBoard(final EditText txtSearchKey, final String status) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager m = (InputMethodManager) txtSearchKey.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				if (status.equals("open")) {
					m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
				} else {
					m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
				}
			}
		}, 300);
	}

	private void login() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);    
		data1.asyncHttpClient.setCookieStore(myCookieStore); 

		Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("登录中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				//返回0登录成功，-1密码错误，-2未知错误，-3找不到用户
				if("0".equals(content)){
					saveOptionInfo();
					getCookieText();
					GlobalApplication.getInstance().startPush();
					
					Intent intent = new Intent(LoginActivity.this,ReLoginListenerService.class); 
					LoginActivity.this.startService(intent);
					
//					Toast.makeText(LoginActivity.this, "登录成功，cookie=" + getCookieText(), Toast.LENGTH_SHORT).show();
					startActivity(new Intent(LoginActivity.this,NewPageHomeMainActivity.class));
					finish();
				}else if("-1".equals(content)) {
					Toast.makeText(LoginActivity.this, "密码错误",
							Toast.LENGTH_LONG).show();
				}else if("-3".equals(content)) {
					Toast.makeText(LoginActivity.this, "找不到用户",
							Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(LoginActivity.this, "未知错误",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(LoginActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(LoginActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(LoginActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(LoginActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		};

		if (TextUtils.isEmpty(etUserName.getText().toString())) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			etUserName.requestFocus();
			return;
		} else if (TextUtils.isEmpty(etPassword.getText().toString())) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			etPassword.requestFocus();
			return;
		}

		params.put("username", etUserName.getText().toString().trim());
		params.put("passcode", etPassword.getText().toString().trim());
		params.put("clienttype", ApplicationConstants.clienttype);
		data1.getZd_ApiResult(handler, ApplicationUrls.login, params, "post");
	}
	
	public String getCookieText() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		List<Cookie> cookies = myCookieStore.getCookies();
		GlobalApplication.getInstance().setCookies(cookies);
		 StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cookies.size(); i++) {
			 Cookie cookie = cookies.get(i);
			 String cookieName = cookie.getName();
			 String cookieValue = cookie.getValue();
			if (!TextUtils.isEmpty(cookieName)
					&& !TextUtils.isEmpty(cookieValue)) {
				sb.append(cookieName + "=");
				sb.append(cookieValue + ";");
			}
		}
		return sb.toString();
	}

}
