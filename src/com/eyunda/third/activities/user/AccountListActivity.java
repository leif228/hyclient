package com.eyunda.third.activities.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.manager.UserSetManager;
import com.eyunda.third.adapters.user.AccountAdapter;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.account.AccountData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AccountListActivity extends CommonListActivity
{
	private AccountAdapter adapter;
	private List<AccountData> list;
	private ListView listView;
	private Button add_account;
	private BasePopupWindow popupWindow;// 弹出菜单

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_account_list);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		add_account = (Button) this.findViewById(R.id.button_add);
		add_account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AccountListActivity.this,AddAccountActivity.class));
			}
		});
		listView = (ListView) findViewById(R.id.list);
		list = UserSetManager.getInstance().getAccountDatas();

		// 设置adapter
		adapter = new AccountAdapter(this, R.layout.eyd_chat_row_contact,
				list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(AccountListActivity.this,
						AccountInfoActivity.class).putExtra("user",
						adapter.getItem(position)));
			}
		});

		registerForContextMenu(listView);
		setPopupWindow();
	}
	private void setPopupWindow() {
		popupWindow = new BasePopupWindow(this);
		// 基本信息
		btnA = popupWindow.getBtnA();
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AccountListActivity.this,
						BasicInfoActivity.class));
			}
		});
		btnB = popupWindow.getBtnB();
		btnB.setVisibility(View.GONE);
		
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AccountListActivity.this,
						ApplyAgentActivity.class));
			}
		});
		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AccountListActivity.this,
						AgentActivity.class));
			}
		});
		// 修改密码
		btnF = popupWindow.getBtnF();
		btnF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AccountListActivity.this,
						UpdatePwd.class));
			}
		});
		// 管理子账号
		btnG = popupWindow.getBtnG();
		btnG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AccountListActivity.this,
						UserChildListActivity.class));
			}
		});
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			if(user.isChildUser()){
				btnA.setVisibility(View.VISIBLE);
				btnC.setVisibility(View.GONE);
				btnD.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnG.setVisibility(View.GONE);
			}else {
				btnA.setVisibility(View.VISIBLE);
				btnF.setVisibility(View.VISIBLE);
				if(user.isRealUser()){
					btnC.setVisibility(View.GONE);
					btnG.setVisibility(View.VISIBLE);
					btnD.setVisibility(View.VISIBLE);
				}else {
					btnC.setVisibility(View.VISIBLE);
					btnG.setVisibility(View.GONE);
					btnD.setVisibility(View.GONE);
				}
			}
		}

	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		this.getMenuInflater().inflate(R.menu.eyd_user_context_account_list,
				menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			AccountData tobeDeleteUser = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除子账号
			loadDate(tobeDeleteUser);

			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("账户管理");
		setRight(R.drawable.commen_top_right, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.togglePopWindow(v);
			}
		});
		UserSetManager.getInstance().getAccountDatas().clear();
		loadDate();
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}
	public void refresh() {
		this.runOnUiThread(new Runnable() {
			public void run() {
				list = UserSetManager.getInstance().getAccountDatas();
				adapter.notifyDataSetChanged();
			}
		});
	}

	protected void loadDate(final AccountData a) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
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
					UserSetManager.getInstance().delAccount(a.getId());
					refresh();
				} else {
					Toast.makeText(AccountListActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
//				Toast.makeText(AccountInfoActivity.this, "信息提交失败", 1).show();
				dialog.dismiss();

			}

		};
		Map<String, Object> params = new HashMap<String, Object>();
		if (a != null) {
			params.put("id", a.getId());
		}

		data.getApiResult(handler, "/mobile/account/myAccount/delAccount",
				params);
	}
	@Override
	protected void loadDate() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				try {
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson((String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
							boolean contentMD5Changed = (Boolean) map.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils("/mobile/account/myAccount", null);
							if(contentMD5Changed&&NetworkUtils.isNetworkAvailable()){
								s.setParam(content);
							}else{
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion, new TypeToken<Map<String, Object>>() {
								}.getType());
							}
						} 
						
						Map<String, Object> contentMap=(Map<String, Object>) map.get("content");
						List<Map<String, Object>> accountDataMap=(List<Map<String, Object>>) contentMap.get("accountDatas");

						if(accountDataMap!=null&&accountDataMap.size()>0){
							for(Map<String,Object> m:accountDataMap){
								AccountData accountData=new AccountData(m);
								UserSetManager.getInstance().getAccountDatas().add(accountData);
							}
						}
						refresh();
					} else {
						Log.i("账户、申请为代理人信息", "异步加载失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		};
		data.getApiResult(handler, "/mobile/account/myAccount");
	
		
	}
}
