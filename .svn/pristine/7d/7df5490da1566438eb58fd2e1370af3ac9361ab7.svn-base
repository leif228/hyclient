package com.eyunda.third.activities.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.chat.mina.MessageSender;
import com.eyunda.third.chat.utils.SIMCardInfo;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.eyunda.tools.MD5;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class LoginActivity extends CommonActivity {
	Button register, login_but, forgetpwd;
	Data_loader data;
	EditText username, pwd;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	int logtag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_user_activity_login);

		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		username = (EditText) findViewById(R.id.username);
		pwd = (EditText) findViewById(R.id.pwd);
		register = (Button) findViewById(R.id.register);
		forgetpwd = (Button) findViewById(R.id.forgetpwd);
		login_but = (Button) findViewById(R.id.login_but);
		logtag = 0;

		// 显示登入名
		String loginName = getIntent().getStringExtra("loginName");
		if (loginName != null) {
			username.setText(loginName);
		}
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));

				// Intent intent = new Intent(LoginActivity.this,
				// Register_step2Activity.class);
				// intent.putExtra("phoneNum", "13815808796");
				// startActivity(intent);
			}
		});

		forgetpwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent(LoginActivity.this,
						ForgetPasswdActivity.class));

			}
		});
		login_but.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				logtag = 0;

				KeyBoard(username, "close");
				KeyBoard(pwd, "close");

				login(username.getText().toString(), pwd.getText().toString());

			}
		});

	}

	private void startMain() {

		if (logtag > 0) {
			startActivity(new Intent(this,
					com.eyunda.third.activities.MenuActivity.class));
			finish();
			// loadAllFavors(Config.USERID);
		}
	}

	private void login(final String username, final String pwd) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("登录中", "请稍候...");
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
					// loadAllFavors(username);
					Toast.makeText(LoginActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
					UserData userData = new UserData(
							(Map<String, Object>) map.get("content"));

					// 写文件
					Context ctx = LoginActivity.this;
					SharedPreferences sp = ctx.getSharedPreferences(
							"eyundaBindingCode", MODE_PRIVATE);
					// 存入数据
					Editor editor = sp.edit();
					String bindingCode = userData.getBindingCode();
					editor.putString("bindingCode", bindingCode);
					editor.putString("loginName", userData.getLoginName());
					editor.putString("nickName", userData.getNickName());
					editor.putString("userLogo", userData.getUserLogo());
					editor.putString("roleDesc", userData.getShortRoleDesc());
					editor.putString("id", userData.getId().toString());
					editor.commit();
					
					cacheCurrUserJson(bindingCode,content);//缓存当前用户
					
					GlobalApplication.getInstance().setUserData(userData);
					GlobalApplication.getInstance().setLoginStatus(LoginStatusCode.logined);
					// 发送登录事件
					MessageSender.getInstance().sendLoginEvent();
					

					logtag++;
					startMain();
				} else {
					GlobalApplication.getInstance().setUserData(null);
					GlobalApplication.getInstance().setLoginStatus(LoginStatusCode.noLogin);
					
					Toast.makeText(LoginActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(LoginActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		};
		// 手动登入
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", username);
		params.put("password", pwd);
		String simNo = SIMCardInfo.getInstance(LoginActivity.this).getSimCardNumber();
		
		params.put("simCardNo", simNo);
		data.getApiResult(handler, "/mobile/login/login", params);
	}

	private void cacheCurrUserJson(String bindingCode,String json) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bindingCode", bindingCode);
		params.put("simCardNo", SIMCardInfo.getInstance(this).getSimCardNumber());
		SharedPreferencesUtils forAutoLogin = new SharedPreferencesUtils("/mobile/login/autoLogin",params);
		forAutoLogin.setParam(json);
		SharedPreferencesUtils currUser = new SharedPreferencesUtils("currUser",null);
		currUser.setParam(json);
	}

	// private void loadAllFavors(String userid) {
	// FavorUtil.clear();
	// PlugUtil.ALLPLUG.clear();
	// Plug_map.SHOWPLUGS.clear();
	// data.getAllFavors(new AsyncHttpResponseHandler() {
	// @Override
	// public void onSuccess(String arg0) {
	// super.onSuccess(arg0);
	// List<Map<String, String>> list = DataConvert.toArrayList(arg0);
	// if (list != null) {
	// for (Map<String, String> tm : list) {
	// FavorUtil.addFavor(tm.get("cid"));
	// }
	// }
	// }
	// }, userid);
	// FavorUtil.clearInfo();
	// data.getAllFavorsInfo(new AsyncHttpResponseHandler() {
	// @Override
	// public void onSuccess(String arg0) {
	// super.onSuccess(arg0);
	// List<Map<String, String>> list = DataConvert.toArrayList(arg0);
	// if (list != null) {
	// for (Map<String, String> tm : list) {
	// FavorUtil.addFavorInfo(tm.get("iid"));
	// }
	// }
	// }
	// }, userid);
	//
	// data.myPlugin(new AsyncHttpResponseHandler() {
	// @Override
	// public void onSuccess(String arg0) {
	// super.onSuccess(arg0);
	//
	// List<Map<String, String>> list = DataConvert.toArrayList(arg0);
	// if (list != null) {
	// for (Map<String, String> tm : list) {
	// PlugUtil.ALLPLUG.put(tm.get("id"), "");
	// if (tm.get("isdefault").equals("1")) {
	// // HomeActivity.PLUGS.add(tm.get("id"));
	// Map<String, Object> ttm = Plug_map.STATICMAP.get(tm
	// .get("id"));
	// if (ttm != null) {
	// ttm.put("isdefault", "1");
	// Plug_map.SHOWPLUGS.add(ttm);
	// }
	// }
	//
	// }
	// }
	// logtag++;
	// startMain();
	// }
	// }, userid);
	//
	// data.myShortcutList(new AsyncHttpResponseHandler() {
	// @Override
	// public void onSuccess(String arg0) {
	// super.onSuccess(arg0);
	// Map<String, String> map = DataConvert.toMap(arg0);
	//
	// if (map != null) {
	// String ids = map.get("pluginids");
	// if (ids != null && !ids.equals("")) {
	// for (String id : ids.split(",")) {
	// Map<String, Object> tm = Plug_map.STATICMAP.get(id);
	// if (tm != null)
	// Plug_map.SHOWPLUGS.add(tm);
	// }
	// }
	// }
	// logtag++;
	// startMain();
	// }
	// }, userid);
	// }

	// 强制显示或者关闭系统键盘
	public static void KeyBoard(final EditText txtSearchKey, final String status) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager m = (InputMethodManager) txtSearchKey
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				if (status.equals("open")) {
					m.showSoftInput(txtSearchKey,
							InputMethodManager.SHOW_FORCED);
				} else {
					m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
				}
			}
		}, 300);
	}

}
