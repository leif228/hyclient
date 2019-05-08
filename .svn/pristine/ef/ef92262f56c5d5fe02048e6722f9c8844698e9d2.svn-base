package com.eyunda.third.activities.chat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.SplashActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.adapters.cargo.CargoAdapter;
import com.eyunda.third.adapters.chat.ChatAllHistoryAdapter;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Type;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.NotifyMessage;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ChatManager;
import com.eyunda.third.adapters.chat.manager.ChatRoomManager;
import com.eyunda.third.adapters.chat.manager.NotifyManager;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.chat.ChatRoomData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.eyunda.tools.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class NewChatAllHistoryActivity extends CommonListActivity implements
OnItemClickListener, OnClickListener{

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	
	private TextView tab1, tab2;
	private String current = "chat";
	private int selectedColor;
	private int unSelectedColor;
	private ImageView imageView;
	private int bmpW;// 图片宽度
	private int offset = 0;// 动图片偏移量
	private int currIndex = 0;// 当前页卡编号
	/** 页卡总数 **/
	private static final int pageSize = 2;
	
	private InputMethodManager inputMethodManager;
	private ListView listView;
	private Map<User, ChatRoom> chatRooms;
	private ChatAllHistoryAdapter adapter;
	private EditText query;
	private ImageButton clearSearch;
	public RelativeLayout errorItem;
	public TextView errorText;
	private NewMessageBroadcastReceiver msgReceiver;
	LinkedHashMap<User, ChatRoom> localTable = new LinkedHashMap<User, ChatRoom>();
	public static final int RESULT_CODE_CHATROOMSDEL = 23;
	public static final int RESULT_CODE_NOTIFYSDEL = 24;
	public static final int REQUEST_CODE_MOREDEL_MENU_CHAT = 25;
	public static final int REQUEST_CODE_MOREDEL_MENU_NOTIFY = 26;
	
	boolean notifyGoto = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_new_chatallhistory);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
		notifyGoto = getIntent().getBooleanExtra("notifyGoto", false);//判断是否为从android通知栏过来的
		if(notifyGoto){
			GlobalApplication.getInstance().setHaveOneChatMsgNotify(false);
			SharedPreferences sp = this.getSharedPreferences(
					"eyundaBindingCode", MODE_PRIVATE);
			String bindingCode = sp.getString("bindingCode", "");
			if("".equals(bindingCode)){
				startActivity(new Intent(
						this,
						com.eyunda.third.activities.user.LoginActivity.class));
				finish();
				return;
			}
			 if(SystemUtil.isAppAlive(this, "com.eyunda.main")){
				 if(GlobalApplication.getInstance().getLoginStatus()==LoginStatusCode.noLogin){
					 Intent mainIntent = new Intent(this, SplashActivity.class);
					 mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					 mainIntent.putExtra("gotoNewChatAllHistoryActivity", true);
					 startActivity(mainIntent);
					 finish();
					 return;
				 }
					
			 }else{
				 Intent launchIntent = getPackageManager(). getLaunchIntentForPackage("com.eyunda.main");
		            launchIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		            Bundle args = new Bundle();
		            args.putString("activity", "NewChatAllHistoryActivity");
	            	args.putString("selTab", "chat");
		            launchIntent.putExtra("NotifyGotoActivity", args);
		            startActivity(launchIntent);
		            finish();
		            return;
			 }
		
		} 

			String sel = getIntent().getStringExtra("selTab");
			if (sel != null)
				current = getIntent().getStringExtra("selTab");

			inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// errorItem = (RelativeLayout) findViewById(R.id.rl_error_item);
			// errorText = (TextView)
			// errorItem.findViewById(R.id.tv_connect_errormsg);

			// init tab
			InitImageView();
			initView();
			// contact list
			listView = (ListView) findViewById(R.id.list);
			adapter = new ChatAllHistoryAdapter(this, 1,
					loadConversationsWithRecentChat());

			// 设置adapter
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					onPause = true;
					ChatRoom conversation = adapter.getItem(position);
					if (conversation.getNotifyMsg() != null) {
						Intent intent = new Intent(
								NewChatAllHistoryActivity.this,
								NotifyDetailActivity.class)
								.putExtra("notifyId", conversation
										.getNotifyMsg().getId());
						startActivity(intent);
					} else {
						// 进入聊天页面
						Intent intent = new Intent(
								NewChatAllHistoryActivity.this,
								ChatActivity.class);
						intent.putExtra("toChatUser",
								conversation.getToChatUser());
						startActivity(intent);
					}
				}
			});
			// 注册上下文菜单
			registerForContextMenu(listView);

			listView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// 隐藏软键盘
					if (NewChatAllHistoryActivity.this.getWindow()
							.getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
						if (NewChatAllHistoryActivity.this.getCurrentFocus() != null)
							inputMethodManager
									.hideSoftInputFromWindow(
											NewChatAllHistoryActivity.this
													.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
					}
					return false;
				}
			});
			// // 搜索框
			// query = (EditText) findViewById(R.id.query);
			// // 搜索框中清除button
			// clearSearch = (ImageButton) findViewById(R.id.search_clear);
			// query.addTextChangedListener(new TextWatcher() {
			// public void onTextChanged(CharSequence s, int start, int before,
			// int count) {
			//
			// adapter.getFilter().filter(s);//
			// 根据ArrayAdapter<ChatRoom>、ChatRoom的toString（）来实现搜索功能的
			// // 。
			// if (s.length() > 0) {
			// clearSearch.setVisibility(View.VISIBLE);
			// } else {
			// clearSearch.setVisibility(View.INVISIBLE);
			// }
			// }
			//
			// public void beforeTextChanged(CharSequence s, int start, int
			// count,
			// int after) {
			// }
			//
			// public void afterTextChanged(Editable s) {
			// }
			// });
			// clearSearch.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// query.getText().clear();
			//
			// }
			// });

			// 注册一个接收消息的BroadcastReceiver
			msgReceiver = new NewMessageBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter(
					"android.intent.action.NewMessageBroadcast");
			intentFilter.setPriority(3);
			registerReceiver(msgReceiver, intentFilter);
			try {
				if ("chat".equals(current)) {
					// 聊天室信息
					localTable.clear();
					load_chatMsgs();
				} else {
					// 通知信息
					NotifyManager.getInstance().clearNotify();
					NotifyManager.getInstance().getServerNotifys(this);
				}
			} catch (Exception e) {
				Toast.makeText(NewChatAllHistoryActivity.this, "加载出错了",
						Toast.LENGTH_LONG).show();
			}
		
	}

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.iv_tab1);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
		// = 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void initView() {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);
		tab1 = (TextView) findViewById(R.id.tab_1);
		tab2 = (TextView) findViewById(R.id.tab_2);

		listView = (ListView) findViewById(R.id.mCargoList);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		if("chat".equals(current)){
			currIndex = 0;
			Animation animation = new TranslateAnimation(one * currIndex, 0, 0,
					0);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			
			tab1.setTextColor(selectedColor);
			tab2.setTextColor(unSelectedColor);
		}else{
			currIndex = 1;
			Animation animation2 = new TranslateAnimation(one * currIndex, one,
					0, 0);
			animation2.setFillAfter(true);// True:图片停在动画结束位置
			animation2.setDuration(300);
			imageView.startAnimation(animation2);
			
			tab2.setTextColor(selectedColor);
			tab1.setTextColor(unSelectedColor);
		}
	}

	public ChatAllHistoryAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(!notifyGoto)
			unregisterReceiver(msgReceiver);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.eyd_chat_delete_message, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_message) {
			ChatRoom tobeDeleteCons = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此会话
			ChatRoomManager.getInstance().deleteChatRoom(this, tobeDeleteCons);

			// 更新消息未读数
			// ((MainActivity) getActivity()).updateUnreadLabel();

			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			this.runOnUiThread(new Runnable() {
				public void run() {
					adapter = new ChatAllHistoryAdapter(
							NewChatAllHistoryActivity.this,
							R.layout.eyd_chat_row_chat_history,
							loadConversationsWithRecentChat());
//					System.err.println(chatRooms);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<ChatRoom> loadConversationsWithRecentChat() {
		List<ChatRoom> crs = new ArrayList<ChatRoom>();
		try {
			if("chat".equals(current)){
				// 获取所有会话   ,过滤掉messages seize为0的chatRoom
				chatRooms = ChatRoomManager.getInstance().getChatRooms();
				for (ChatRoom cr : chatRooms.values()) {
					if (cr.getRecentlyTime() != null)
						crs.add(cr);
				}
			}else{
				// 通知消息
				List<NotifyMessage> notifys = NotifyManager.getInstance()
						.getNotifys();
				if (notifys.size() > 0) {
					for (NotifyMessage nm : notifys) {
						ChatRoom cr = new ChatRoom();
						cr.setNotifyMsg(nm);
						crs.add(cr);
					}
				}
			}

			// 排序
			sortConversationByLastChatTime(crs);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return crs;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<ChatRoom> crs) {
		Collections.sort(crs, new Comparator<ChatRoom>() {
			@Override
			public int compare(final ChatRoom con1, final ChatRoom con2) {
				// con1，con2都不是通知
				if (con1.getNotifyMsg() == null && con2.getNotifyMsg() == null) {
					// ChatMessage con2LastMessage = con2.getLastMsg();
					// ChatMessage con1LastMessage = con1.getLastMsg();
					if (con1.getRecentlyTime() == con2.getRecentlyTime()) {
						return 0;
					} else if (con2.getRecentlyTime().getTime().getTime() > con1
							.getRecentlyTime().getTime().getTime()) {
						return 1;
					} else {
						return -1;
					}
					// con1不是通知，con2是通知
				} else if (con1.getNotifyMsg() == null
						&& con2.getNotifyMsg() != null) {
					NotifyMessage con2LastMessage = con2.getNotifyMsg();
					// ChatMessage con1LastMessage = con1.getLastMsg();
					if (con2LastMessage.getCreateTime() == con1
							.getRecentlyTime()) {
						return 0;
					} else if (con2LastMessage.getCreateTime().getTime()
							.getTime() > con1.getRecentlyTime().getTime()
							.getTime()) {
						return 1;
					} else {
						return -1;
					}
					// con1是通知，con2不是通知
				} else if (con1.getNotifyMsg() != null
						&& con2.getNotifyMsg() == null) {
					// ChatMessage con2LastMessage = con2.getLastMsg();
					NotifyMessage con1LastMessage = con1.getNotifyMsg();
					if (con2.getRecentlyTime() == con1LastMessage
							.getCreateTime()) {
						return 0;
					} else if (con2.getRecentlyTime().getTime().getTime() > con1LastMessage
							.getCreateTime().getTime().getTime()) {
						return 1;
					} else {
						return -1;
					}
				} else {
					// con1，con2都是通知
					NotifyMessage con2LastMessage = con2.getNotifyMsg();
					NotifyMessage con1LastMessage = con1.getNotifyMsg();
					if (con2LastMessage.getCreateTime() == con1LastMessage
							.getCreateTime()) {
						return 0;
					} else if (con2LastMessage.getCreateTime().getTime()
							.getTime() > con1LastMessage.getCreateTime()
							.getTime().getTime()) {
						return 1;
					} else {
						return -1;
					}
				}
			}

		});
	}
	boolean onPause = false;
	@Override
	public void onResume() {
		super.onResume();
		
		if(!onPause){
			// 设置只有一个聊天消息提醒
			if (getIntent().getBooleanExtra("notifyGoto", false))
				GlobalApplication.getInstance().setHaveOneChatMsgNotify(false);
		}
		onPause = false;
		
		refresh();
	}
	/**
	 * 覆盖手机返回键
	 */
	@Override
	public void onBackPressed() {
		startActivity(new Intent(NewChatAllHistoryActivity.this,
				com.eyunda.third.activities.MenuActivity.class));
		finish();
	}
	@Override
	public OnClickListener leftListener() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(NewChatAllHistoryActivity.this,
						com.eyunda.third.activities.MenuActivity.class));
				finish();
			}
		};
		return listener;
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("消息");
		setRight(R.drawable.eyd_chat_empty, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if("chat".equals(current)){
					startActivityForResult((new Intent(NewChatAllHistoryActivity.this,
							com.eyunda.third.activities.chat.ContextMenu.class))
							.putExtra("type", REQUEST_CODE_MOREDEL_MENU_CHAT),
							REQUEST_CODE_MOREDEL_MENU_CHAT);
				}else{
					startActivityForResult((new Intent(NewChatAllHistoryActivity.this,
							com.eyunda.third.activities.chat.ContextMenu.class))
							.putExtra("type", REQUEST_CODE_MOREDEL_MENU_NOTIFY),
							REQUEST_CODE_MOREDEL_MENU_NOTIFY);
				}
			}
		});

		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_MOREDEL_MENU_CHAT) {
			switch (resultCode) {
			case RESULT_CODE_CHATROOMSDEL:
				deleteManager(RESULT_CODE_CHATROOMSDEL);
				break;
			}
		}
		if (requestCode == REQUEST_CODE_MOREDEL_MENU_NOTIFY) {
			switch (resultCode) {
			case RESULT_CODE_NOTIFYSDEL:
				deleteManager(RESULT_CODE_NOTIFYSDEL);
				break;
			}
		}
	}

	private void deleteManager(final int resultCode) {
		// TODO Auto-generated method stub
		List<ChatRoom> list = loadConversationsWithRecentChat();
		final List<Long> crids = new ArrayList<Long>();
		final List<Long> nfids = new ArrayList<Long>();
		for(ChatRoom cr:list){
			if(cr.getNotifyMsg()!=null){
				nfids.add(cr.getNotifyMsg().getId());
			}else{
				crids.add(cr.getId());
			}
		}

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("消息删除中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				try {
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						if(resultCode==RESULT_CODE_CHATROOMSDEL){
							for(Long id:crids){
								ChatRoomManager.getInstance().delChatRoomById(id);
							}
							crids.clear();
						}else if(resultCode==RESULT_CODE_NOTIFYSDEL){
							for(Long id:nfids){
								ChatRoomManager.getInstance().delOnlyNotifyChatRoomById(id);
								NotifyManager.getInstance().delLocalNotifyById(id);
							}
							nfids.clear();
						}
						dialog.dismiss();
						refresh();
						
					} else {
						dialog.dismiss();
						Toast.makeText(NewChatAllHistoryActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(NewChatAllHistoryActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
			}
		};
		Gson gson = new Gson();
		Map<String, Object> params = new HashMap<String, Object>();
		if(resultCode==RESULT_CODE_CHATROOMSDEL){
			String cridsJsonStr = gson.toJson(crids);
			params.put("crids", cridsJsonStr);
			data.getApiResult(handler, "/mobile/chat/moredel", params, "post");
		}else if(resultCode==RESULT_CODE_NOTIFYSDEL){
			String nfidsJsonStr = gson.toJson(nfids);
			params.put("nfids", nfidsJsonStr);
			data.getApiResult(handler, "/mobile/message/moredel", params, "post");
		}
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {

	}

	/** 异步去加载当前用户的聊天信息 */
	private void load_chatMsgs() {
		final Map<String, Object> params = new HashMap<String, Object>();;
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("信息加载中", "请稍候...");
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
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
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
							boolean contentMD5Changed = (Boolean) map.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils("/mobile/chat/getAllCRS", params);
							if(contentMD5Changed&&NetworkUtils.isNetworkAvailable()){
								s.setParam(content);
							}else{
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion, new TypeToken<Map<String, Object>>() {
								}.getType());
							}
						}
						
						Map<String, Object> contents = (Map<String, Object>) map
								.get("content");
						LinkedHashMap<Long, Map<String, Object>> tableMap = (LinkedHashMap<Long, Map<String, Object>>) contents
								.get("table");
						List<Map<String, Object>> usersMap = (List<Map<String, Object>>) contents
								.get("users");

						List<UserData> users = new ArrayList<UserData>();
						for (int i = 0; i < usersMap.size(); i++) {
							UserData ud = new UserData(
									(Map<String, Object>) usersMap.get(i));
							users.add(ud);
						}
						// 本地Data
						LinkedHashMap<UserData, ChatRoomData> table = new LinkedHashMap<UserData, ChatRoomData>();
						Iterator iter = tableMap.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							Long userId = Long
									.valueOf(((String) entry.getKey()));
							ChatRoomData chatRoomData = new ChatRoomData(
									(Map<String, Object>) entry.getValue());
							for (UserData ud : users) {
								if (ud.getId().equals(userId)) {
									table.put(ud, chatRoomData);
								}
							}
						}
						// 本地化
						changToLocal(table);
						ChatRoomManager.getInstance().setChatRooms(localTable);
						refresh();// 刷新页面

					} else {
						// Toast.makeText(ChatAllHistoryActivity.this,
						// "服务端取不到数据",
						// Toast.LENGTH_SHORT).show();
						// 没有数据过来
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(NewChatAllHistoryActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
			}
		};
		 
		// params.put("userId", userId);
		data.getApiResult(handler, "/mobile/chat/getAllCRS", params, "get");
	}

	/** 本地化 */
	@SuppressWarnings("rawtypes")
	private void changToLocal(LinkedHashMap<UserData, ChatRoomData> table) {

		User user = null;
		ChatRoom chatRoom = null;
		Iterator iter2 = table.entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry entry = (Map.Entry) iter2.next();
			UserData userData = (UserData) entry.getKey();
			ChatRoomData chatRoomData = (ChatRoomData) entry.getValue();

			user = new User();
			user.setUserData(userData);
			chatRoom = new ChatRoom();
			chatRoom.setId(chatRoomData.getId());
			chatRoom.setRoomName(chatRoomData.getRoomName());
			chatRoom.setToChatUser(user);
			if (chatRoomData.getRecentlyTitle().startsWith(
					Type.IMAGE.toString()))
				chatRoom.setRecentlyTitle("[图片]");
			else if (chatRoomData.getRecentlyTitle().startsWith(
					Type.FILE.toString())) {
				chatRoom.setRecentlyTitle("[文件]");
			} else if (chatRoomData.getRecentlyTitle().startsWith(
					Type.VOICE.toString())) {
				chatRoom.setRecentlyTitle("[语音]");
			} else
				chatRoom.setRecentlyTitle(chatRoomData.getRecentlyTitle());
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(Long.valueOf(chatRoomData.getRecentlyTime()));
			chatRoom.setRecentlyTime(c);
			chatRoom.setUnreadMsgCount(chatRoomData.getNoReadCount());
			localTable.put(user, chatRoom);
		}

	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@SuppressWarnings("rawtypes")
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
			refresh();
			abortBroadcast();
		}
	}

	@Override
	public void onClick(View v) {
		current = "";
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		switch (v.getId()) {
		case R.id.tab_1: // 聊天
			currIndex = 0;
			Animation animation = new TranslateAnimation(one * currIndex, 0, 0,
					0);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			current = "chat";
			tab1.setTextColor(selectedColor);
			tab2.setTextColor(unSelectedColor);
			try {
				// 聊天室信息
				localTable.clear();
				load_chatMsgs();
			} catch (Exception e) {
				Toast.makeText(NewChatAllHistoryActivity.this, "加载聊天记录出错了", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.tab_2: // 通知
			currIndex = 1;
			Animation animation2 = new TranslateAnimation(one * currIndex, one,
					0, 0);
			animation2.setFillAfter(true);// True:图片停在动画结束位置
			animation2.setDuration(300);
			imageView.startAnimation(animation2);
			current = "notify";
			tab1.setTextColor(unSelectedColor);
			tab2.setTextColor(selectedColor);
			try {
				// 通知信息
				NotifyManager.getInstance().clearNotify();
				NotifyManager.getInstance().getServerNotifys(this);
			} catch (Exception e) {
				Toast.makeText(NewChatAllHistoryActivity.this, "加载通知记录出错了", Toast.LENGTH_LONG).show();
			}
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
