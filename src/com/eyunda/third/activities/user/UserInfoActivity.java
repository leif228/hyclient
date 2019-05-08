package com.eyunda.third.activities.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.entity.FormFile;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.Config;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.localdata.LocalData;
import com.eyunda.main.reg.EditImage;
import com.eyunda.main.reg.UpdateQA;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.part1.data.PartData_loader;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ContactManager;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.chat.event.OnlineStatusCode;
import com.eyunda.third.chat.event.StatusEvent;
import com.eyunda.third.chat.mina.MessageSender;
import com.eyunda.third.chat.utils.LogUtil;
import com.eyunda.third.chat.utils.MessageConstants;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.LoginSourceCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.eyunda.third.locatedb.EydLocalDB;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UserInfoActivity extends CommonListActivity {
	private static final String LOGTAG = LogUtil
			.makeLogTag(UserInfoActivity.class);
	Button logout;
	TextView email, phone, nick;
	ImageView user_head;
	DialogUtil dialogUtil;
	RelativeLayout updateqa;
	PartData_loader data;
	Image_loader loader;
	ProgressDialog dialog;
	ImageLoader mImageLoader;
	Data_loader data1;
	SynData_loader synDataLoader;
	Spinner online_states;
	String selected;
	boolean nothingSelected = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_user_information);
		// 4.0为http请求在主线程中
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads().detectDiskWrites().detectNetwork()
		// .penaltyLog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
		// .penaltyLog().penaltyDeath().build());
		email = (TextView) findViewById(R.id.email);
		phone = (TextView) findViewById(R.id.phone);
		nick = (TextView) findViewById(R.id.nick);
		logout = (Button) findViewById(R.id.logout);
		synDataLoader = new SynData_loader();
		data1 = new Data_loader();
		user_head = (ImageView) findViewById(R.id.user_head);
		online_states = (Spinner) findViewById(R.id.online_states);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());
		setLeft(leftListener());// 重写顶部返回

		updateqa = (RelativeLayout) findViewById(R.id.updateqa);
		loader = new Image_loader(this, (TAApplication) getApplication());
		// String headString = LocalData.get((TAApplication) getApplication())
		// .getUserHead();
		// if (headString != null)
		// loader.load_normal_Img(headString, user_head);
		updateqa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UserInfoActivity.this, UpdateQA.class));
			}
		});
		user_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// dialogUtil.showDialogForFile(getPath());
			}
		});

		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				MessageSender.getInstance().sendLogoutEvent();//mina发登出事件
				// 写文件
				Context ctx = UserInfoActivity.this;
				SharedPreferences sp = ctx.getSharedPreferences(
						"eyundaBindingCode", MODE_PRIVATE);
				// 存入数据
				Editor editor = sp.edit();
				editor.putString("bindingCode", "");
				editor.putString("loginName", "");
				editor.putString("nickName", "");
				editor.putString("userLogo", "");
				editor.putString("roleDesc", "");
				editor.putString("id", "");
				editor.commit();
				
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
				}
				
				logout();

			}
		});
		
		setview();
	}

	@SuppressWarnings("unchecked")
	private void setview() {
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			phone.setText(user.getMobile());
			email.setText(user.getEmail());
			nick.setText(user.getNickName());
			mImageLoader.displayImage(
					ApplicationConstants.IMAGE_URL + user.getUserLogo(),
					user_head, MyshipAdapter.displayImageOptions);

			// spinner
			List<String> list_spinner_account_way = new ArrayList<String>();
			if (user.getOnlineStatus() != null) {
				for (OnlineStatusCode p : OnlineStatusCode.values()) {
					if (p.equals(user.getOnlineStatus()))
						list_spinner_account_way.add(p.getDescription());
				}
				for (OnlineStatusCode p : OnlineStatusCode.values()) {
					if (p.equals(user.getOnlineStatus()))
						continue;
					list_spinner_account_way.add(p.getDescription());
				}

			} else {
				for (OnlineStatusCode p : OnlineStatusCode.values()) {
					list_spinner_account_way.add(p.getDescription());
				}
			}
			ArrayAdapter adapter_spinner_account_way = new ArrayAdapter(this,
					R.layout.spinner_item, R.id.contentTextView,
					list_spinner_account_way);
			online_states.setAdapter(adapter_spinner_account_way);
			online_states.setPrompt("在线状态");
			online_states
					.setOnItemSelectedListener(new SpinnerOnSelectedListener());
		}
	}

	class SpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			selected = adapterView.getItemAtPosition(position).toString();
			if (selected != null) {
				for (OnlineStatusCode p : OnlineStatusCode.values()) {
					if (p.getDescription().equals(selected))
						if (p.toString().equals(GlobalApplication.getInstance().getUserData().getOnlineStatus()))
							nothingSelected = true;
						else
							nothingSelected = false;
				}
			}
			Log.e(LOGTAG, selected);
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
			System.out.println("nothingSelected");
		}

	}

	// 覆写左键按钮，添加消息发送
	@Override
	protected OnClickListener leftListener() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		};
		return listener;
	}

	public void logout() {
		Intent intents = new Intent(this,com.eyunda.third.PushService.class);  
		this.stopService(intents);  
		
		Intent intent = new Intent(Intent.ACTION_MAIN);

		intent.addCategory(Intent.CATEGORY_HOME);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		UserInfoActivity.this.startActivity(intent);

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
		setTitle("个人中心");

	};

	@SuppressWarnings("rawtypes")
	@Override
	public void onBackPressed() {
		if (nothingSelected) {
			finish();
		} else {
			if (selected != null) {
				for (OnlineStatusCode p : OnlineStatusCode.values()) {
					if (p.getDescription().equals(selected)) {
						// 1更新内存用户在线状态
						GlobalApplication.getInstance().getUserData().setOnlineStatus(p);
						// 2通知好友状态改变
						MessageSender.getInstance().sendStatusEvent(p);
					}
				}
			}
			finish();
			
//			Map<String, User> map = ContactManager.getInstance()
//					.getContactList();
//			if (map.size() > 0) {
//				Iterator iter = map.entrySet().iterator();
//				while (iter.hasNext()) {
//					Map.Entry entry = (Map.Entry) iter.next();
//					User toUser = (User) entry.getValue();
//					if (!toUser.getUserData().getOnlineStatus()
//							.equals(OnlineStatusCode.ofline))
//						sendOnlineStatus(toUser);
//				}
//			} else {
//				loadDate();
//			}
//			finish();
		}
	}

	String pathString;

	// private String getPath() {
	//
	// String dir = Utils.getDiskCacheDir(context, "afinalCache")
	// .getAbsolutePath();
	//
	// if (!new File(dir).exists())
	// new File(dir).mkdirs();
	// pathString = dir + File.separator + System.currentTimeMillis() + ".jpg";
	//
	// return pathString;
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == 1 || requestCode == 2) {
		// System.out.println(data);
		//
		// }

		if (resultCode == 44) {
			imgpath = pathString;
			if (pathString != null)
				dialogUtil.showDialogFromConfig("提示", "是否上传头像？", handler1);

		} else if (resultCode == RESULT_OK) {
			if (requestCode == 1) {

				if (pathString != null) {
					Intent intent = new Intent(this, EditImage.class);
					intent.putExtra("path", pathString);
					startActivityForResult(intent, requestCode);

				}
			} else if (requestCode == 2) {
				if (data != null) {

					retrunData = data;
					Uri contactData = retrunData.getData();

					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = UserInfoActivity.this
							.managedQuery(contactData, proj, null, null, null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();

					String img_path = actualimagecursor
							.getString(actual_image_column_index);
					if (img_path.endsWith(".jpg") || img_path.endsWith(".png")
							|| img_path.endsWith(".bmp")) {
						Intent intent = new Intent(this, EditImage.class);
						intent.putExtra("path", img_path);
						pathString = img_path;
						startActivityForResult(intent, requestCode);

					}

				}
			}
		} else {
			pathString = null;
		}

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

	Intent retrunData;
	String imgpath;
	Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if (imgpath != null) {

					data.uploadHead(new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable arg0) {
							super.onFailure(arg0);
							dialog.dismiss();
						}

						@Override
						public void onStart() {
							super.onStart();
							dialog = dialogUtil.loading("头像上传中", "请稍候");
						}

						@Override
						public void onSuccess(String arg0) {
							super.onSuccess(arg0);
							Map<String, String> tm = DataConvert.toMap(arg0);
							// {"result":0,"content":"头像上传成功",
							// "imgUrl":"http://115.28.227.82:9000/assets/headPortait/20140417142756688.octet-stream"}
							dialog.dismiss();
							if (imgpath.endsWith(".jpg")
									|| imgpath.endsWith(".png")
									|| imgpath.endsWith(".bmp")) {
								BitmapFactory.Options option = new BitmapFactory.Options();
								option.inSampleSize = 4;
								Bitmap bm = BitmapFactory.decodeFile(imgpath,
										option);
								user_head.setImageBitmap(bm);
								LocalData.get((TAApplication) getApplication())
										.setUserHead(tm.get("imgUrl"));
							}
							imgpath = null;
						}

					}, Config.USERID, imgpath);

				} else {
					// new PhotoUtil()
					// .createNewBitmapAndCompressByFile(pathString);
					FormFile f = new FormFile(pathString, pathString,
							"filedata", null);
					List<FormFile> list = new ArrayList<FormFile>();
					list.add(f);
					pathString = null;

				}
			} catch (Exception e) {
				Toast.makeText(UserInfoActivity.this, "上传头像失败",
						Toast.LENGTH_LONG).show();
				imgpath = null;
				e.printStackTrace();
			}
		}
	};

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
