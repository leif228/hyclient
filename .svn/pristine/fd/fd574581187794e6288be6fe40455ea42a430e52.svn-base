package com.eyunda.third.activities.chat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.ContactAdapter;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ChatRoomManager;
import com.eyunda.third.adapters.chat.manager.ContactManager;
import com.eyunda.third.adapters.chat.widget.Sidebar;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.chat.ChatRoomData;
import com.eyunda.third.domain.enumeric.ApplyStatusCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ContactRoleListActivity extends CommonListActivity
{
	private ContactPopupWindow popupWindow;// 弹出菜单

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	
	UserRoleCode role = UserRoleCode.broker;
	
	private ContactAdapter adapter;
	private List<User> contactList = new ArrayList<User>();
	private ListView listView;
	private Sidebar sidebar;
	private InputMethodManager inputMethodManager;
	private UserOnlineStatusBroadcast userOnlineStatusBroadcast;
	
	private static final int ITEM1 = Menu.FIRST;
	private static final int ITEM2 = Menu.FIRST + 1;
	
	private Thread thread = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_contact_role_list);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		setPopupWindow();
		
		inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) this.findViewById(R.id.list);
		sidebar = (Sidebar) this.findViewById(R.id.sidebar);
		(sidebar).setListView(listView);

		// 设置adapter
		adapter = new ContactAdapter(this,R.layout.eyd_chat_row_contact, contactList, sidebar);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserData currentUser = GlobalApplication.getInstance().getUserData();
				if(currentUser!=null){
//					User toChatUser = adapter.getItem(position);
//					
//					ChatRoom chatRoom = ChatRoomManager.getInstance().getLocalChatRoom(toChatUser);
//					if(chatRoom != null)
						startActivity(new Intent(ContactRoleListActivity.this, ChatActivity.class)
						.putExtra("toChatUser", adapter.getItem(position)));
//					else
//						addChatRoom(toChatUser);
				}
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		registerForContextMenu(listView);
		// 注册UserOnlineStatusBroadcast
		userOnlineStatusBroadcast = new UserOnlineStatusBroadcast();
		IntentFilter intentFilter1 = new IntentFilter(
				"android.intent.action.UserOnlineStatusBroadcast");
		intentFilter1.setPriority(5);
		this.registerReceiver(userOnlineStatusBroadcast, intentFilter1);

		ContactManager.getInstance().getContactList().clear();
		loadDate();
	}
	protected void addChatRoom(final User toChatUser) {
		// TODO Auto-generated method stub

		Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("正在加载聊天页面!", "请稍候...");
			}

			@SuppressWarnings("unchecked")
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
						Map<String, Object> cmap = (Map<String, Object>) map
								.get("content");
						ChatRoomData chatRoomData = new ChatRoomData(
								(Map<String, Object>) cmap.get("chatRoomData"));

						ChatRoom chatRoom = new ChatRoom();
						chatRoom.setId(chatRoomData.getId());
						chatRoom.setRoomName(chatRoomData.getRoomName());
						chatRoom.setToChatUser(toChatUser);
						chatRoom.setRecentlyTitle(chatRoomData.getRecentlyTitle());
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(Long.valueOf(chatRoomData.getRecentlyTime()));
						chatRoom.setRecentlyTime(c);
						ChatRoomManager.getInstance().getChatRooms().put(toChatUser, chatRoom);
						
						startActivity(new Intent(ContactRoleListActivity.this, ChatActivity.class)
						.putExtra("toChatUser", toChatUser));

					} else {
						Toast.makeText(ContactRoleListActivity.this,
								(CharSequence) map.get("message"),
								Toast.LENGTH_SHORT).show();
					}
					
				} catch (Exception e) {
					Toast.makeText(ContactRoleListActivity.this,
							e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ContactRoleListActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
			}
		};

		params.put("toUserId", toChatUser.getUserData().getId());
		data.getApiResult(handler, "/mobile/chat/toGetChatRoom", params, "get");
	}

	private void setPopupWindow() {
		popupWindow = new ContactPopupWindow(this);
		View broker = popupWindow.getBroker();
		broker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				role = UserRoleCode.broker;
				setTitle(role.getDescription());
				
				ContactManager.getInstance().getContactList().clear();
				loadDate();
			}
		});
		View handler = popupWindow.getHandler();
		handler.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				role = UserRoleCode.handler;
				setTitle(role.getDescription());
				
				ContactManager.getInstance().getContactList().clear();
				loadDate();
			}
		});
		View sailor = popupWindow.getSailor();
		sailor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				role = UserRoleCode.sailor;
				setTitle(role.getDescription());
				
				ContactManager.getInstance().getContactList().clear();
				loadDate();
			}
		});
		View master = popupWindow.getMaster();
		master.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				role = UserRoleCode.master;
				setTitle(role.getDescription());
				
				ContactManager.getInstance().getContactList().clear();
				loadDate();
			}
		});
		View owner = popupWindow.getOwner();
		owner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				role = UserRoleCode.owner;
				setTitle(role.getDescription());
				
				ContactManager.getInstance().getContactList().clear();
				loadDate();
			}
		});
		View member = popupWindow.getMember();
		member.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				role = UserRoleCode.member;
				setTitle(role.getDescription());
				
				ContactManager.getInstance().getContactList().clear();
				loadDate();
			}
		});
	}

	private class UserOnlineStatusBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 长按前1个不弹menu
		if (((AdapterContextMenuInfo) menuInfo).position > 0) {
			// 判断contextMenu显视
			User tobeDeleteUser = adapter
					.getItem(((AdapterContextMenuInfo) menuInfo).position);
			if (tobeDeleteUser.getUserData().getApplyStatus() == ApplyStatusCode.apply) {
				menu.add(0, ITEM1, 0, "同意");
				menu.add(0, ITEM2, 0, "删除");
			} else {
				this.getMenuInflater().inflate(
						R.menu.eyd_chat_context_contact_list, menu);
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM1:
			User friendUser = adapter.getItem(((AdapterContextMenuInfo) item
					.getMenuInfo()).position);

			friendContact(friendUser);
			break;
		case ITEM2:
			User tobeDeleteUser = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);

			// 删除此联系人
			deleteContact(tobeDeleteUser);
			break;
		case R.id.delete_contact:
			User tobeDeleteUser1 = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);

			// 删除此联系人
			deleteContact(tobeDeleteUser1);
			break;
//		case R.id.send_notify:
//			User to = adapter
//			.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
//			
//			this.startActivity(new Intent(this,SendNotifyActivity.class)
//							.putExtra("to", to.getUserData().getId().toString())
//							.putExtra("toUserName", to.getUserData().getLoginName()));
//			break;
		}
		return true;
	}
	
	private void friendContact(final User friendUser) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				try {
					Gson gson = new Gson();
					final HashMap<String, Object> rmap = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());
					if (rmap.get("returnCode").equals("Success")) {
						ContactManager.getInstance().friendContact(friendUser);
						refresh();
					} else {
						ContactRoleListActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(ContactRoleListActivity.this, "失败", 0).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host")) {
					ContactRoleListActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(ContactRoleListActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		};
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("contactId", friendUser.getUserData().getId());
		data.getApiResult(handler, "/mobile/contact/myContact/friend", map,
				"post");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(userOnlineStatusBroadcast);

	}

	/**
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact(final User tobeDeleteUser) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				try {
					Gson gson = new Gson();
					final HashMap<String, Object> rmap = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());
					if (rmap.get("returnCode").equals("Success")) {
//						ContactManager.getInstance().deleteContact(
//								tobeDeleteUser);
//						refresh();
						ContactRoleListActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(ContactRoleListActivity.this, "删除成功", 0).show();
							}
						});
					} else {
						ContactRoleListActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(ContactRoleListActivity.this, "删除失败", 0).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host")) {
					ContactRoleListActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(ContactRoleListActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						}
					});
				}

			}

		};
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("contactId", tobeDeleteUser.getUserData().getId());
		data.getApiResult(handler, "/mobile/contact/myContact/delete", map,
				"get");

	}

	// 刷新ui
	public void refresh() {
			// 可能会在子线程中调到这方法
			this.runOnUiThread(new Runnable() {
				public void run() {
					getContactList();
					adapter.notifyDataSetChanged();

				}
			});
	}

	private List<User> getContactList() {
		contactList.clear();
		Map<String, User> users = ContactManager.getInstance().getContactList();
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			if(entry != null)
				contactList.add(entry.getValue());
		}
		// 排序
		Collections.sort(contactList, new Comparator<User>() {

			@Override
			public int compare(User lhs, User rhs) {
				return lhs.getHeader().compareTo(rhs.getHeader());
			}
		});
		return contactList;
	}

	
	@Override
	public void onStart() {
		super.onStart();
		setTitle(role.getDescription());
		
		setRight(R.drawable.commen_top_right, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.togglePopWindow(v);
			}
		});
		
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {

		final Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("正在加载列表!", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				
					dialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)) {
							boolean contentMD5Changed = (Boolean) map
									.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils(
									"/mobile/contact/myContact", params);
							if (contentMD5Changed
									&& NetworkUtils.isNetworkAvailable()) {
								s.setParam(content);
							} else {
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion,
										new TypeToken<Map<String, Object>>() {
										}.getType());
							}
						}
						HashMap<String, Object> contents = (HashMap<String, Object>) map
								.get("content");
						List<UserData> contacts = (List<UserData>) contents
								.get("contacts");
						if (contacts != null && !contacts.isEmpty()) {
							for (int i = 0; i < contacts.size(); i++) {
								if(contacts.get(i) != null) {
									UserData userData = new UserData(
											(Map<String, Object>) contacts.get(i));
									User user = new User();
									user.setUserData(userData);
									
									ContactManager.getInstance().addContact(user);
								}
							}
						}

					} else {
						Toast.makeText(ContactRoleListActivity.this,
								(CharSequence) map.get("message"),
								Toast.LENGTH_SHORT).show();
					}
					
					refresh();
				
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ContactRoleListActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				
				refresh();
			}
		};

		params.put("role", role.name());
		data.getApiResult(handler, "/mobile/contact/myContact", params, "get");

	
	}
}
