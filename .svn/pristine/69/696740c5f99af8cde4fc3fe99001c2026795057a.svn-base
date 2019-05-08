package com.eyunda.third.activities.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ContactManager;
import com.eyunda.third.adapters.order.OrderAddTYRAdapter;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AddContactActivity extends CommonListActivity {
	private AutoCompleteTextView editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText;
	private Button addBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;
	Data_loader data;
	DialogUtil dialogUtil;
	boolean addFail = false;
	private AddContactTextWatcher myTextWatcher;
	private UserData curSeclectUser = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().penaltyLog().build());
		setContentView(R.layout.eyd_chat_activity_addcontact);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		editText = (AutoCompleteTextView) findViewById(R.id.ed_addcontact);
		OrderAddTYRAdapter autocompleteAdapter = new OrderAddTYRAdapter(this,
				R.layout.eyd_auto_user_item);

		myTextWatcher = new AddContactTextWatcher(editText, autocompleteAdapter);

		editText.setAdapter(autocompleteAdapter);

		editText.addTextChangedListener(myTextWatcher);
		editText.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String info = ((OrderAddTYRAdapter) parent.getAdapter())
						.getItem(position);
				curSeclectUser = myTextWatcher.getSelectedUser(info);
				// Log.v("info", "选择了：" + info);
				if (null != curSeclectUser) {
					addBtn.setVisibility(View.VISIBLE);
				}
			}
		});

		addBtn = (Button) findViewById(R.id.button_add);
		// avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					addContact();
					if (addFail) {
						editText.setText("");
						return;
					}
					if (curSeclectUser != null) {
						Long userId = curSeclectUser.getId();
						loadDate(userId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 查找contact
	 * 
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = addBtn.getText().toString();

		// if (getString(R.string.button_search).equals(saveText)) {
		// toAddUsername = name;
		// if(TextUtils.isEmpty(name)) {
		// startActivity(new Intent(this, AlertDialog.class).putExtra("msg",
		// "请输入用户名"));
		// return;
		// }
		//
		//
		// //服务器存在此用户，显示此用户和添加按钮
		// searchedUserLayout.setVisibility(View.VISIBLE);
		// nameText.setText(toAddUsername);
		//
		// }
	}

	/**
	 * 添加contact
	 * 
	 * @param view
	 */
	public void addContact() {
		addFail = false;
		if (editText.getText().toString().equals("")) {
			addFail = true;
			return;
		}
//		if (GlobalApplication.getInstance().getUserData().getLoginName()
//				.equals(editText.getText().toString())) {
//			// startActivity(new Intent(this, AlertDialog.class).putExtra("msg",
//			// "不能添加自己"));
//			addFail = true;
//			Toast.makeText(AddContactActivity.this, "不能添加自己",
//					Toast.LENGTH_SHORT).show();
//
//		}
		String addname = editText.getText().toString();
		List<User> users = new ArrayList<User>();
		Map<String, User> list = ContactManager.getInstance().getContactList();
		Iterator<Entry<String, User>> iterator = list.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			users.add(entry.getValue());
		}
		for (User u : users) {
			if (u.getUserData().getLoginName().equals(addname)
					|| u.getUserData().getMobile().equals(addname)
					|| u.getUserData().getEmail().equals(addname)) {
				addFail = true;
				Toast.makeText(AddContactActivity.this, "此用户已是你的好友",
						Toast.LENGTH_SHORT).show();

			}
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("新增承运人");

	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}

	protected void loadDate(Long userId) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				progressDialog = dialogUtil.loading("正在添加用户!", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				progressDialog.dismiss();
				Log.i("addContact", content);
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					// Toast.makeText(AddContactActivity.this,
					// (CharSequence) map.get("message"),
					// Toast.LENGTH_SHORT).show();
					// UserData userData = new UserData(
					// (Map<String, Object>) map.get("content"));
					// User user=new User();
					// user.setUserId(userData.getId());
					// user.setLoginName(userData.getLoginName());
					// user.setEmail(userData.getEmail());
					// user.setMobile(userData.getMobile());
					// user.setNickName(userData.getNickName());
					// user.setTrueName(userData.getTrueName());
					// ContactManager.getInstance().addContact(user);

					startActivity(new Intent(AddContactActivity.this,
							ContactListActivity.class)
							.putExtra("refresh", true));
					finish();
				} else {
					Toast.makeText(AddContactActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(AddContactActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		data.getApiResult(handler, "/mobile/contact/myContact/add", params);

	}

	@Override
	protected void loadDate() {

	}
}
