package com.hangyi.zd.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.eyunda.main.CommonListActivity;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.update.UpdateManager1;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.part1.data.PartData_loader;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.chat.utils.LogUtil;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.UpdateInfoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.hangyi.tools.CloseActivityClass;
import com.hangyi.zd.ClearService;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AccountInfoActivity extends CommonListActivity {
	private static final String LOGTAG = LogUtil
			.makeLogTag(AccountInfoActivity.class);
	ImageView user_head;
	DialogUtil dialogUtil;
	RelativeLayout chang_user,modify_pw,user_logout,clear,pushSet,scship,hc,help;
	PartData_loader data;
	Image_loader loader;
	ProgressDialog dialog;
	ImageLoader mImageLoader;
	Data_loader data1;
	SynData_loader synDataLoader;
	private RelativeLayout version;
	static boolean active = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_account_information);
		help = (RelativeLayout) findViewById(R.id.help);
		version = (RelativeLayout) findViewById(R.id.version);
		hc = (RelativeLayout) findViewById(R.id.hc);
		chang_user = (RelativeLayout) findViewById(R.id.chang_user);
		scship = (RelativeLayout) findViewById(R.id.scship);
		modify_pw = (RelativeLayout) findViewById(R.id.modify_pw);
		user_logout = (RelativeLayout) findViewById(R.id.user_logout);
		clear = (RelativeLayout) findViewById(R.id.clear);
		pushSet = (RelativeLayout) findViewById(R.id.pushSet);
		
		synDataLoader = new SynData_loader();
		data1 = new Data_loader();
		dialogUtil = new DialogUtil(this);
		user_head = (ImageView) findViewById(R.id.user_head);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());

		loader = new Image_loader(this, (TAApplication) getApplication());
		
		chang_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AccountInfoActivity.this, ChangUserLoginActivity.class));
			}
		});
		modify_pw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AccountInfoActivity.this, UpdatePwd.class));
			}
		});

		user_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtil dialogAlert = new DialogUtil(AccountInfoActivity.this);
				dialogAlert.showDialogFromConfig("提示", "确认要退出登录吗?", new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						
						GlobalApplication.getInstance().stopPush();
						logout();
						
						//异步后台删除item
//						AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
//							@Override
//							public void onStart() {
//								dialog = dialogUtil.loading("通知", "请稍候...");
//							}
//
//							@Override
//							public void onSuccess(String arg0) {
//								dialog.dismiss();
//								ConvertData cd = new ConvertData(arg0);
//								if (cd.getReturnCode().equals("Success")) {
//									
//									adapter.removeItem(curPosition);
//									adapter.notifyDataSetChanged();
//								}else
//									Toast.makeText(AgentActivity.this, cd.getMessage(), Toast.LENGTH_LONG).show();
//							}
//						};
//						Map<String, Object> params = new HashMap<String, Object>();
//						params.put("id",id3);
//						data.getApiResult(handler, "/mobile/account/myAccount/delAgent", params, "get");	
//						
					}
				});
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtil dialogAlert = new DialogUtil(AccountInfoActivity.this);
				dialogAlert.showDialogFromConfig("提示", "确认要清空缓存吗?", new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						//TODO 清空
						Intent intent = new Intent(AccountInfoActivity.this,ClearService.class); 
						AccountInfoActivity.this.startService(intent);
						
						Toast.makeText(AccountInfoActivity.this, "后台进行清空缓存操作！", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		pushSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setunDistur() ;
			}
		});
		scship.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AccountInfoActivity.this,
						ShipSCActivity.class));
			}
		});
		hc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(AccountInfoActivity.this,
//						ShipHCActivity.class));
				startActivity(new Intent(AccountInfoActivity.this,
						GroupActivity.class));
			}
		});
		version.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				setDialog();
				getVersion();
			}
		});
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AccountInfoActivity.this, HelpActivity.class));
			}
		});
	}
	private String getVersionName()  
	{  
	        String version = "";
			try {
				// 获取packagemanager的实例  
				PackageManager packageManager = getPackageManager();  
				// getPackageName()是你当前类的包名，0代表是获取版本信息  
				PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);  
				version = packInfo.versionName;
			} catch (Exception e) {
			}  
	        return version;  
	} 
	public void setDialog() {
		if(active){
			String str = "当前版本："+getVersionName()+"\n本软件下载、安装免费，使用中的通信流量费由运营商收取。\n\n官方网站：http://www.zd.com\n客服电话：+86 020-88888888\nEmail：zd@zdserver.com\n\n对于在使用的过程中的任何问题和意见，欢迎发邮件给我们。\n\n";
			AlertDialog ad = new AlertDialog.Builder(AccountInfoActivity.this)
			.setTitle("珠电驳船调度系统").setMessage(str)
			.setPositiveButton("确定", null).create();
			ad.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			ad.show();
		}
	}
	private void getVersion() {
		if (NetworkUtils.isNetworkAvailable()) {
			data1.getzdVersion(new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					super.onStart();
					dialog = dialogUtil.loading("加载中", "请稍候...");
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					
					ConvertData cd = new ConvertData(arg0);
					if (cd.getReturnCode().equalsIgnoreCase("success")) {
						HashMap<String, Object> var = (HashMap<String, Object>) cd
								.getContent();
						UpdateInfoData up = new UpdateInfoData(var);
						//先读取配置文件，查看用户是否已经忽略当前升级版本
						//读取配置信息，查看用户是否已经忽略当前版本的升级
						SharedPreferences spNoUpdate = AccountInfoActivity.this.getSharedPreferences(
								ApplicationConstants.noUpdate_SharedPreferences, Activity.MODE_PRIVATE);
						String noUpdateVersion = spNoUpdate.getString("noUpdate", "");
						if ((getVersionName().equals(up.getVersion())) ) {
							setDialog();
						} else {
							UpdateManager1 mUpdateManager = new UpdateManager1(
									AccountInfoActivity.this, up.getUrl(),"最新版本为："+up.getVersion() +"\r\n"+up
											.getNote(),up.getVersion());
							mUpdateManager.checkUpdateInfo();
						}
					}else{
						setDialog();
					}
					dialog.dismiss();

				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					setDialog();
					dialog.dismiss();
				}
			});
		} else{
			setDialog();
		}
	}
	// 设置免打扰时段
    private void setunDistur() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.bpush_setundistur_time);

        final TimePicker start_picker = (TimePicker) window
                .findViewById(R.id.start_time_picker);
        final TimePicker end_picker = (TimePicker) window
                .findViewById(R.id.end_time_picker);
        start_picker.setIs24HourView(true);
        end_picker.setIs24HourView(true);
        start_picker
                .setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        end_picker
                .setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        Button set = (Button) window.findViewById(R.id.btn_set);
        set.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                int startHour = start_picker.getCurrentHour();
                int startMinute = start_picker.getCurrentMinute();
                int endHour = end_picker.getCurrentHour();
                int endMinute = end_picker.getCurrentMinute();

                if (startHour == 0 && startMinute == 0 && endHour == 0
                        && endMinute == 0) {
                    Toast.makeText(getApplicationContext(), "已取消 免打扰时段功能",
                            Toast.LENGTH_SHORT).show();
                } else if (startHour > endHour
                        || (startHour == endHour && startMinute > endMinute)) {
                    setToastText("第一天的" + startHour + ":" + startMinute, "第二天的"
                            + endHour + ":" + endMinute);
                } else {
                    setToastText(startHour + ":" + startMinute, endHour + ":"
                            + endMinute);
                }

                // Push: 设置免打扰时段
                // startHour startMinute：开始 时间 ，24小时制，取值范围 0~23 0~59
                // endHour endMinute：结束 时间 ，24小时制，取值范围 0~23 0~59
                PushManager.setNoDisturbMode(getApplicationContext(),
                        startHour, startMinute, endHour, endMinute);

                alertDialog.cancel();
            }

        });
        Button guide = (Button) window.findViewById(R.id.btn_guide);
        guide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str = "设置免打扰时段,在免打扰时段内,当用户收到通知时,会去除通知的提示音、振动以及提示灯闪烁.\n\n注意事项:\n1.如果开始时间小于结束时间，免打扰时段为当天的起始时间到结束时间.\n2.如果开始时间大于结束时间，免打扰时段为第一天起始时间到第二天结束时间.\n3.如果开始时间和结束时间的设置均为00:00时,取消免打扰时段功能.\n4.如果未调用接口设置开始时间和结束时间，免打扰时段默认为第一天的23:00到第二天的7:00.\n";
                new AlertDialog.Builder(AccountInfoActivity.this)
                        .setTitle("免打扰时段功能使用说明").setMessage(str)
                        .setPositiveButton("确定", null).show();
            }
        });

        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialog.cancel();
            }

        });

    }
        
        private void setToastText(String start, String end) {
            String text = getString(R.string.text_toast, start, end);
            Log.i("tangshi", text);
            int indexTotal = 13 + start.length();
            int indexPosition = indexTotal + 3 + end.length();
            SpannableString s = new SpannableString(text);
            s.setSpan(
                    new ForegroundColorSpan(getResources().getColor(R.color.red)),
                    13, indexTotal, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            s.setSpan(
                    new ForegroundColorSpan(getResources().getColor(R.color.red)),
                    indexTotal + 3, indexPosition,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

	public void logout() {
		SharedPreferences sp = getSharedPreferences("UserInfoConfig", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("AutoLogin", "false");
		editor.putString("SavePassword", "false");

		editor.putString("UserName", "");
		editor.putString("UserPassword", "");

		editor.commit();

		Intent intents = new Intent(this, com.eyunda.third.PushService.class);
		this.stopService(intents);
		
		CloseActivityClass.exitClient(AccountInfoActivity.this);
		
		AccountInfoActivity.this.finish();

//		Intent intent = new Intent(Intent.ACTION_MAIN);
//
//		intent.addCategory(Intent.CATEGORY_HOME);
//
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		AccountInfoActivity.this.startActivity(intent);
		

		android.os.Process.killProcess(android.os.Process.myPid());
		
	}

	/** http登出 */
//	private void loadData() {
//		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(String content) {
//				
//				Gson gson = new Gson();
//				HashMap<String, Object> map = gson.fromJson((String) content,
//						new TypeToken<Map<String, Object>>() {
//						}.getType());
//
//				if (map.get("returnCode").equals("Success")) {
//					logout();
//				} else {
//					UserInfoActivity.this.runOnUiThread(new Runnable() {
//						public void run() {
//							Toast.makeText(UserInfoActivity.this, "服务端解绑失败",
//									Toast.LENGTH_SHORT).show();
//							logout();
//						}
//					});
//				}
//			}
//
//			@Override
//			public void onFailure(Throwable error, String content) {
//				super.onFailure(error, content);
//				UserInfoActivity.this.runOnUiThread(new Runnable() {
//					public void run() {
//						logout();
//					}
//				});
//			}
//		};
//		data1.getApiResult(handler, "/mobile/login/logout");
//	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("账号设置");
		active = true;
	};
	@Override
	protected void onStop() {
		super.onStop();
		active = false;
	}


	/** 内存中还没有好友列表，先异步去加载，再发送状态信息 */
//	protected void loadDate() {
//
//		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onSuccess(String content) {
//				try {
//
//					Gson gson = new Gson();
//					HashMap<String, Object> map = gson.fromJson(
//							(String) content,
//							new TypeToken<Map<String, Object>>() {
//							}.getType());
//
//					if (map.get("returnCode").equals("Success")) {
//
//						HashMap<String, Object> contents = (HashMap<String, Object>) map
//								.get("content");
//						List<UserData> contacts = (List<UserData>) contents
//								.get("contacts");
//						if (contacts != null && !contacts.isEmpty()) {
//							for (int i = 0; i < contacts.size(); i++) {
//								UserData userData = new UserData(
//										(Map<String, Object>) contacts.get(i));
//								User user = new User();
//								user.setUserData(userData);
//								if (!userData
//										.getOnlineStatus()
//										.toString()
//										.equals(OnlineStatusCode.ofline
//												.toString()))
//									sendOnlineStatus(user);
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		data1.getApiResult(handler, "/mobile/contact/myContact");
//	}
	
	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}
}
