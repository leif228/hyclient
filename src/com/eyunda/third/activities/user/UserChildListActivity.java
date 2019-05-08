package com.eyunda.third.activities.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.BindBankCardActivity;
import com.eyunda.third.adapters.user.UserChildAdapter;
import com.eyunda.third.adapters.user.UserChildManager;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.ship.ShipNameData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UserChildListActivity extends CommonListActivity {
	private UserChildAdapter adapter;
	private List<UserData> list;
	private ListView listView;
	private BasePopupWindow popupWindow;// 弹出菜单
	private Button new_uc;
	private Button old_uc;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;
	private View btnH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_user_activity_userchild);

		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
		new_uc = (Button) this.findViewById(R.id.button_new);
		new_uc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UserChildListActivity.this,
						AddUserChildActivity.class).putExtra("agentSel", "new"));
			}
		});
		old_uc = (Button) this.findViewById(R.id.button_old);
		old_uc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UserChildListActivity.this,
						AddUserChildActivity.class).putExtra("agentSel", "old"));
			}
		});

		listView = (ListView) findViewById(R.id.list);
		list = UserChildManager.getInstance().getChilds();

		// 设置adapter
		adapter = new UserChildAdapter(this, R.layout.eyd_chat_row_contact,
				list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(UserChildListActivity.this,
						UserChildActivity.class).putExtra("user",
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
				startActivity(new Intent(UserChildListActivity.this,
						BasicInfoActivity.class));
			}
		});
		// 账号管理
		btnB = popupWindow.getBtnB();
		btnB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UserChildListActivity.this,
						AccountListActivity.class));
			}
		});
		// 申请成为代理人
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UserChildListActivity.this,
						ApplyAgentActivity.class));
			}
		});
		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UserChildListActivity.this,
						AgentActivity.class));
			}
		});
		// 修改密码
		btnF = popupWindow.getBtnF();
		btnF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UserChildListActivity.this,
						UpdatePwd.class));
			}
		});
		btnG = popupWindow.getBtnG();
		btnG.setVisibility(View.GONE);
		
		btnH = popupWindow.getBtnH();
		btnH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UserChildListActivity.this,BindBankCardActivity.class));
			}
		});
		
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			if (user.isChildUser()) {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnC.setVisibility(View.GONE);
				btnD.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnH.setVisibility(View.VISIBLE);
			}else if(!user.isChildUser()){
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnH.setVisibility(View.VISIBLE);
				if(user.isRealUser()){
					btnC.setVisibility(View.GONE);
					btnD.setVisibility(View.VISIBLE);
				}else if(!user.isRealUser()){
					btnC.setVisibility(View.VISIBLE);
					btnD.setVisibility(View.GONE);
				}
			}
		}
	
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		this.getMenuInflater().inflate(R.menu.eyd_user_context_userchild_list,
				menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			UserData tobeDeleteUser = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除子账号
			deleteChild(tobeDeleteUser);

			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("子账号管理");

		setRight(R.drawable.commen_top_right, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.togglePopWindow(v);
			}
		});
		list.clear();
		UserChildManager.getInstance().getChildMaps().clear();
		UserChildManager.getInstance().getShipList().clear();
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
				list = UserChildManager.getInstance().getChilds();
				adapter = new UserChildAdapter(UserChildListActivity.this, R.layout.eyd_chat_row_contact,
						list);
				listView.setAdapter(adapter);
			}
		});
	}

	private void deleteChild(final UserData tobeDeleteUser) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
			}

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
						UserChildManager.getInstance().delChild(UserChildManager.getInstance().getChildId(tobeDeleteUser.getId()));
						refresh();
					} else {
						Toast.makeText(UserChildListActivity.this,
								map.get("message").toString(), Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")) {
					Toast.makeText(UserChildListActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("childId", UserChildManager.getInstance().getChildId(tobeDeleteUser.getId()));
		data.getApiResult(handler, "/mobile/account/delete", params, "post");

	}

	@Override
	protected void loadDate() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("查询中", "请稍候...");
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
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
							boolean contentMD5Changed = (Boolean) map.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils("/mobile/account/myChild", null);
							if(contentMD5Changed&&NetworkUtils.isNetworkAvailable()){
								s.setParam(content);
							}else{
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion, new TypeToken<Map<String, Object>>() {
								}.getType());
							}
						} 
						Map<String, Object> cMaps = (Map<String, Object>) map
								.get("content");
						Map<String, Map<String, Object>> uMaps = (Map<String, Map<String, Object>>) cMaps
								.get("userDatas");
						List<Map<String, Object>> aMaps = (List<Map<String, Object>>) cMaps
								.get("allShips");
						/*Map<String, Map<String, Object>> userDataMaps = (Map<String, Map<String, Object>>) map
								.get("content");*/
						 Iterator<Entry<String, Map<String, Object>>> it = uMaps.entrySet().iterator();  
					        while(it.hasNext()){  
					            Entry<String, Map<String, Object>> entry=it.next();  
					            Long key=Long.valueOf(entry.getKey()); 
					            Map<String,Object> m = entry.getValue();
					            UserData userData = new UserData(m);
					            UserChildManager.getInstance().addChild(key, userData);
					        } 
					        for(Map<String,Object> m:aMaps){
					        	ShipNameData shipData = new ShipNameData(m);
					        	UserChildManager.getInstance().addChild(shipData);
					        }
						refresh();
					} else {
						Toast.makeText(UserChildListActivity.this,
								(String)map.get("message"), Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Toast.makeText(UserChildListActivity.this,
							e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")) {
					UserChildListActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(UserChildListActivity.this,
									"网络连接异常", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		data.getApiResult(handler, "/mobile/account/myChild", params, "get");
	}

}
