package com.eyunda.main.reg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.Config;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.localdata.LocalData;
import com.eyunda.main.localdata.UserInfo;
import com.eyunda.main.util.FavorUtil;
import com.eyunda.main.util.PlugUtil;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.part1.data.PartData_loader;
import com.hy.client.R;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;

public class LoginActivity extends CommonActivity {
	Button register, login_but,forgetpwd;
	DialogUtil dialogUtil;
	PartData_loader data;
	EditText username, pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_user_activity_login);
		dialogUtil = new DialogUtil(this);
		data = new PartData_loader();
		username = (EditText) findViewById(R.id.username);
		pwd = (EditText) findViewById(R.id.pwd);
		register = (Button) findViewById(R.id.register);
		forgetpwd= (Button) findViewById(R.id.forgetpwd);
		login_but = (Button) findViewById(R.id.login_but);
		logtag=0;
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				Intent intent = new Intent(LoginActivity.this,
//						Register_step2Activity.class);
//				intent.putExtra("phoneNum", "13815808796");
//				startActivity(intent);
			}
		});
		
		forgetpwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(LoginActivity.this,
						Forgetpwd.class));
			}
		});
		login_but.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Config.IFLOGIN = true;
				// Toast.makeText(LoginActivity.this, "登录成功。",
				// Toast.LENGTH_SHORT)
				// .show();
				logtag=0;
				
				KeyBoard(username, "close");
				KeyBoard(pwd, "close");
				login(username.getText().toString(), pwd.getText().toString());
			}
		});

	}

	ProgressDialog dialog;
int logtag=0;
private void startMain() {

		if (logtag > 2) {
			startActivity(new Intent(this,
					com.eyunda.third.activities.MenuActivity.class));
			// loadAllFavors(Config.USERID);
			finish();
		}
	}
	private void login(final String username,final String pwd) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			
				dialog = dialogUtil.loading("登录中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Log.i("userinfo",content);
				content = "{\"result\":0,\"content\":{\"mobileId\":112,\"mobileNo\":\"18795469670\",\"password\":\"FOG2ALH9V59HQzuI6NhSkQ==\",\"passwordQuestion\":null,\"passwordAnswer\":null,\"phoneModel\":\"Nexus 5\",\"registerDate\":\"2014-10-11 08:19:59\",\"lastLogin\":\"2014-11-17 09:06:17\",\"userId\":18795469670,\"headPortrait\":null,\"notify\":0,\"imei\":\"898600\",\"mid\":\"460027954608968\"},\"pluginShortcut\":\"5,18795469670\"}";
				Map<String, String> m = DataConvert.toMap(content);
				/*
 		{content={"passwordQuestion":null,
 		"lastLogin":"2014-04-17 14:41:32","mobileId":4,"imei":"89860312005140016588",
 		"userId":13888888888,"registerDate":"2014-04-13 20:50:05","mid":"460030163323537",
 		"headPortrait":"http:\/\/115.28.227.82:9000\/assets\/headPortait\/20140417142756688.octet-stream",
 		"passwordAnswer":null,"password":"KMjt3j1hoEEVEdOxhm8GNg==","phoneModel":"S8600","mobileNo":"13888888888"}, 
 		result=0}
				*/
				if(m==null) {
					Toast.makeText(LoginActivity.this,"未知信息："+content,
							Toast.LENGTH_SHORT).show();
					return ;}
				if (m.get("result").equals("0")) {
					loadAllFavors(username);
					Toast.makeText(LoginActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					UserInfo userInfo=new UserInfo();
					
					Map<String, String> user = DataConvert.toMap(DataConvert
							.toMap(content).get("content"));
					Config.USERMAP = user;
					Config.IFLOGIN = true;
					Config.USERID = user.get("userId");
					Config.LOGINNAME=username;
					userInfo.setUserId(Config.USERID);
					userInfo.setName(username);
					userInfo.setUserHead(user.get("headPortrait"));
					userInfo.setPwd(pwd);
					
					LocalData.get((TAApplication) getApplication()).addUser(userInfo);
					logtag++;
					 startMain();

					data.mySubscribe(new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String arg0) {
							super.onSuccess(arg0);
							List<Map<String, String>> list = DataConvert.toArrayList(arg0);
							if (list != null) {
								List<String> arg1=new ArrayList<String>();
							
								for (Map<String, String> tm : list) {
//									PushSetting.MYALLPUSH.put(tm.get("cid"), "");
									arg1.add(tm.get("cid"));
								}

							}
						 
						}
					}, username);
					
//					finish();
				} else {

					Toast.makeText(LoginActivity.this, m.get("content"),
							Toast.LENGTH_SHORT).show();
				}
			
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if(content!=null&&content.equals("can't resolve host"))
					Toast.makeText(LoginActivity.this,"网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialog.dismiss();

			}

		};

		data.login(handler, username, MD5Util.MD5(pwd));
	}

	
	private void loadAllFavors(String userid) {
		FavorUtil.clear();
		PlugUtil.ALLPLUG.clear();
		data.getAllFavors(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				List<Map<String, String>> list = DataConvert.toArrayList(arg0);
				if (list != null) {
					for (Map<String, String> tm : list) {
						FavorUtil.addFavor (tm.get("cid") );
					}
				}
			}
		}, userid);
		FavorUtil.clearInfo();
		data.getAllFavorsInfo(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				List<Map<String, String>> list = DataConvert.toArrayList(arg0);
				if (list != null) {
					for (Map<String, String> tm : list) {
						FavorUtil.addFavorInfo(tm.get("iid"));
					}
				}
			}
		}, userid);
	
		data.myPlugin(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				 
					List<Map<String, String>> list = DataConvert.toArrayList(arg0);
					if (list != null) {
						for (Map<String, String> tm : list) {
							PlugUtil.ALLPLUG.put(tm.get("id"), "");
							if (tm.get("isdefault").equals("1")) {
								// HomeActivity.PLUGS.add(tm.get("id"));

							}

						}
					}
					logtag++;
					 startMain();
			}
		}, userid);
	
		
		data.myShortcutList(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				 Map<String, String>  map = DataConvert.toMap(arg0);
				
				
				if (map != null) {
					String ids = map.get("pluginids");
					if (ids != null && !ids.equals("")) {
						for (String id : ids.split(",")) {
						}
					}
				}
				logtag++;
				 startMain();
			}
		}, userid);
	}
	
    //强制显示或者关闭系统键盘
    public static void KeyBoard(final EditText txtSearchKey,final String status)
    {
         
        Timer timer = new Timer();
      timer.schedule(new TimerTask(){
      @Override
      public void run()
      {
          InputMethodManager m = (InputMethodManager)
          txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
           if(status.equals("open"))
           {
               m.showSoftInput(txtSearchKey,InputMethodManager.SHOW_FORCED); 
           }
           else
           {
               m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0); 
           }
       }  
       }, 300);
    }
}
