package com.eyunda.third.activities.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.adapters.chat.ContactAdapter;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ContactManager;
import com.eyunda.third.adapters.chat.widget.Sidebar;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.MassNotify;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SingleAllFriendsActivity extends FragmentActivity implements
		OnClickListener {
	private ContactAdapter adapter;
	private List<User> contactList = new ArrayList<User>();
	private ListView listView;
	private boolean hidden;
	private Sidebar sidebar;
	private InputMethodManager inputMethodManager;
	private UserOnlineStatusBroadcast userOnlineStatusBroadcast;

	// private ProgressDialog progressDialog;
	Data_loader data;
	DialogUtil dialogUtil;

	Button top_back;
	ImageView add;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_chat_activity_contact_allfriends);

		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		top_back = (Button) findViewById(R.id.top_back);
		add = (ImageView) findViewById(R.id.add);
		top_back.setOnClickListener(this);
		add.setOnClickListener(this);

		inputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) findViewById(R.id.list);
		sidebar = (Sidebar) findViewById(R.id.sidebar);
		(sidebar).setListView(listView);
		contactList = getContactList();

		// 设置adapter
		adapter = new ContactAdapter(this, R.layout.eyd_chat_row_contact,
				contactList, sidebar);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(SingleAllFriendsActivity.this,
						ChatActivity.class).putExtra("toChatUser",
						adapter.getItem(position)));
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (SingleAllFriendsActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (SingleAllFriendsActivity.this.getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								SingleAllFriendsActivity.this.getCurrentFocus()
										.getWindowToken(),
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

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 长按前1个不弹menu
		if (((AdapterContextMenuInfo) menuInfo).position > 0) {
			this.getMenuInflater().inflate(
					R.menu.eyd_chat_context_contact_list, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			User tobeDeleteUser = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此联系人
			deleteContact(tobeDeleteUser);

			return true;
		} else if (item.getItemId() == R.id.send_notify) {
			User to = adapter.getItem(((AdapterContextMenuInfo) item
					.getMenuInfo()).position);

			this.startActivity(new Intent(this, SendNotifyActivity.class)
					.putExtra("to", to.getUserData().getId().toString())
					.putExtra("toUserName", to.getUserData().getLoginName()));
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
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
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						(String) content, new TypeToken<Map<String, Object>>() {
						}.getType());
				if (rmap.get("returnCode").equals("Success")) {
					ContactManager.getInstance().deleteContact(tobeDeleteUser);
					refresh();
				} else {
					SingleAllFriendsActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SingleAllFriendsActivity.this,
									"删除失败", 0).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host")) {
					SingleAllFriendsActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SingleAllFriendsActivity.this,
									"网络连接异常", Toast.LENGTH_SHORT).show();
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
		try {
			// 可能会在子线程中调到这方法
			this.runOnUiThread(new Runnable() {
				public void run() {
					getContactList();
					adapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<User> getContactList() {
		contactList.clear();
		Map<String, User> users = ContactManager.getInstance().getContactList();
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
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

	private class UserOnlineStatusBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
//		UserData user = GlobalApplication.getInstance().getUserData();
//		if (user != null) {
//			if (user.isCarrier()) {
//				//add.setVisibility(View.VISIBLE);
//			}
//		}
		ContactManager.getInstance().getContactList().clear();
		loadDate();
	}

	protected void loadDate() {
		final Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				progressDialog = dialogUtil.loading("正在初始化联系人列表!", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				try {
					progressDialog.dismiss();
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
						// Toast.makeText(ContactListActivity.this,
						// (CharSequence) map.get("message"),
						// Toast.LENGTH_SHORT).show();
						HashMap<String, Object> contents = (HashMap<String, Object>) map
								.get("content");
						List<UserData> contacts = (List<UserData>) contents
								.get("contacts");
						if (contacts != null && !contacts.isEmpty()) {
							for (int i = 0; i < contacts.size(); i++) {
								UserData userData = new UserData(
										(Map<String, Object>) contacts.get(i));
								User user = new User();
								user.setUserData(userData);

								ContactManager.getInstance().addContact(user);
							}
							refresh();
						}

					} else {
						Toast.makeText(SingleAllFriendsActivity.this,
								(CharSequence) map.get("message"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				progressDialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(SingleAllFriendsActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
			}
		};

		params.put("role", MassNotify.allfriends.toString());
		data.getApiResult(handler, "/mobile/contact/myContact", params, "get");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.top_back:
			finish();
			break;
		case R.id.add:
			startActivity(new Intent(this, AddContactActivity.class));
			break;
		default:
			break;
		}

	}
}
