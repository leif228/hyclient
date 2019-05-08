package com.eyunda.third.activities.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.adapters.chat.ContactAdapter;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ContactManager;
import com.eyunda.third.adapters.chat.widget.Sidebar;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AllMasterFragment extends Fragment {
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
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.eyd_chat_activity_contact_cyr, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dialogUtil = new DialogUtil(getActivity());
		data = new Data_loader();

		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) view.findViewById(R.id.list);
		sidebar = (Sidebar) view.findViewById(R.id.sidebar);
		(sidebar).setListView(listView);
		contactList = getContactList();

		// 设置adapter
		adapter = new ContactAdapter(getActivity(),
				R.layout.eyd_chat_row_contact, contactList, sidebar);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(getActivity(), ChatActivity.class)
						.putExtra("toChatUser", adapter.getItem(position)));
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								getActivity().getCurrentFocus()
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
		getActivity().registerReceiver(userOnlineStatusBroadcast, intentFilter1);
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 长按前1个不弹menu
		if (((AdapterContextMenuInfo) menuInfo).position > 0) {
			getActivity().getMenuInflater().inflate(
					R.menu.eyd_chat_context_contact_list_allchild, menu);
		}
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.delete_contact) {
//			User tobeDeleteUser = adapter
//					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
//			// 删除此联系人
//			deleteContact(tobeDeleteUser);
//
//			return true;
//		}else
			if (item.getItemId() == R.id.send_notify) {
			User to = adapter.getItem(((AdapterContextMenuInfo) item
					.getMenuInfo()).position);

			getActivity().startActivity(
					new Intent(getActivity(), SendNotifyActivity.class)
							.putExtra("to", to.getUserData().getId().toString()).putExtra(
									"toUserName",
									to.getUserData().getLoginName()));
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
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().unregisterReceiver(userOnlineStatusBroadcast);
		
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
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getActivity(), "删除失败", 0)
									.show();
						}
					});
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host")) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getActivity(), "网络连接异常",
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
		try {
			// 可能会在子线程中调到这方法
			getActivity().runOnUiThread(new Runnable() {
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
			Iterator<Entry<String, User>> iterator = users.entrySet()
					.iterator();
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
}
