package com.eyunda.third.activities.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.eyunda.third.activities.account.BankListActivity;
import com.eyunda.third.activities.account.BindBankCardActivity;
import com.eyunda.third.activities.ship.ShipDuesActivity;
import com.eyunda.third.activities.ship.ShipGasOrderActivity;
import com.eyunda.third.adapters.chat.manager.UserSetManager;
import com.eyunda.third.adapters.ship.ShipDuesAdapter;
import com.eyunda.third.adapters.user.AccountAdapter;
import com.eyunda.third.adapters.user.UserAgentAdapter;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.AccountData;
import com.eyunda.third.domain.account.UserAgentData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.BuyOilStatusCode;
import com.eyunda.third.domain.ship.MyShipGasOrderData;
import com.eyunda.third.domain.ship.ShipNameData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AgentActivity extends CommonListActivity
{
	public static final int MSG_Del_One_Item = 1;
	private UserAgentAdapter adapter;
	private ListView listView;
	private Button btn_new,btn_old;
	private BasePopupWindow popupWindow;// 弹出菜单

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	private ArrayList<Map<String, Object>> dataList;
	private int curPosition;// 当前选择的item
	
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;
	private View btnH;
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_Del_One_Item: // 删除一个Item
				final Long id3 = (Long) msg.obj;
				curPosition = msg.arg1;// 要删除的记录
				DialogUtil dialogAlert = new DialogUtil(AgentActivity.this);
				dialogAlert.showDialogFromConfig("提示", "确认要删除该代理关系吗?", new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						//异步后台删除item
						AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
							@Override
							public void onStart() {
								dialog = dialogUtil.loading("通知", "请稍候...");
							}

							@Override
							public void onSuccess(String arg0) {
								dialog.dismiss();
								ConvertData cd = new ConvertData(arg0);
								if (cd.getReturnCode().equals("Success")) {
									
									adapter.removeItem(curPosition);
									adapter.notifyDataSetChanged();
								}else
									Toast.makeText(AgentActivity.this, cd.getMessage(), Toast.LENGTH_LONG).show();
							}
						};
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("id",id3);
						data.getApiResult(handler, "/mobile/account/myAccount/delAgent", params, "get");	
						
					}
				});
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_agent);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		dataList = new ArrayList<Map<String, Object>>();

		btn_new = (Button) this.findViewById(R.id.btn_new);
		btn_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AgentActivity.this,AgentApplyActivity.class).putExtra("agentSel", "new"));
			}
		});
		btn_old = (Button) this.findViewById(R.id.btn_old);
		btn_old.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AgentActivity.this,AgentApplyActivity.class).putExtra("agentSel", "old"));
			}
		});
		listView = (ListView) findViewById(R.id.list);
		setAdapter();
		setPopupWindow();
		
	}
	@Override
	protected BaseAdapter setAdapter() {
		
		// 设置adapter
		adapter = new UserAgentAdapter(this, mHandler,dataList);
		listView.setAdapter(adapter);
				
		return adapter;
	}
	private void setPopupWindow() {

		popupWindow = new BasePopupWindow(this);
		// 基本信息
		btnA = popupWindow.getBtnA();
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentActivity.this,
						BasicInfoActivity.class));
			}
		});
		btnB = popupWindow.getBtnB();
		btnB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentActivity.this,
						AccountListActivity.class));
			}
		});
		
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentActivity.this,
						ApplyAgentActivity.class));
			}
		});
		btnD = popupWindow.getBtnD();
		btnD.setVisibility(View.GONE);
		// 修改密码
		btnF = popupWindow.getBtnF();
		btnF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentActivity.this,
						UpdatePwd.class));
			}
		});
		// 管理子账号
		btnG = popupWindow.getBtnG();
		btnG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentActivity.this,
						UserChildListActivity.class));
			}
		});
		
		btnH = popupWindow.getBtnH();
		btnH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentActivity.this,BindBankCardActivity.class));
			}
		});
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			if(user.isChildUser()){
				btnA.setVisibility(View.VISIBLE);
				btnC.setVisibility(View.GONE);
				btnB.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnG.setVisibility(View.GONE);
				btnH.setVisibility(View.VISIBLE);
			}else {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnH.setVisibility(View.VISIBLE);
				if(user.isRealUser()){
					btnC.setVisibility(View.GONE);
					btnG.setVisibility(View.VISIBLE);
				}else{
					btnC.setVisibility(View.VISIBLE);
					btnG.setVisibility(View.GONE);
				}
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("代理关系");
		setRight(R.drawable.commen_top_right, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.togglePopWindow(v);
			}
		});
		dataList.clear();
		loadDate();
	}

	@Override
	protected void loadDate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}

			@SuppressWarnings({ "unchecked"})
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/account/myAccount/userAgent", params);
				if (cd.getReturnCode().equals("Success")) {
					List<Map<String, Object>> content = (ArrayList<Map<String, Object>>) cd.getContent();
					
					int size = content.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							UserAgentData data = new UserAgentData((Map<String, Object>) content.get(i));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", data.getId());
							map.put("roleCode", "代理类型:"+data.getRoleCode().getDescription());
							map.put("loginName", "登录名:"+data.getAgented().getLoginName());
							map.put("trueName", "真实姓名:"+data.getAgented().getTrueName());
							map.put("mobile", "手机:"+data.getAgented().getMobile());
							map.put("userLogo", data.getAgented().getUserLogo());
							map.put("unitName", "公司名称:"+data.getAgented().getUnitName());
							map.put("btnDel", true);
							dataList.add(map);
						}
					} else {
						// TODO 空记录处理
					}
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(AgentActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(AgentActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(AgentActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(AgentActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(AgentActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		data.getApiResult(handler, "/mobile/account/myAccount/userAgent", params, "get");
	}
}
