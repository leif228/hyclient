//package com.eyunda.part1.plug;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.eyunda.main.CommonListActivity;
//import com.eyunda.main.Config;
//import com.eyunda.main.R;
//import com.eyunda.main.json.DataConvert;
//import com.eyunda.main.util.PlugUtil;
//import com.eyunda.main.view.DialogUtil;
//import com.eyunda.main.view.MyBaseAdapter;
//import com.eyunda.part1.data.PartData_loader;
//import com.ta.util.http.AsyncHttpResponseHandler;
//
///**
// * 插件列表
// * 
// * @author user_ygl
// * 
// */
//public class CustomPlug extends CommonListActivity implements OnClickListener {
//	PartData_loader data;
//	Button bar_but1, bar_but2, bar_but3;
//	int tab_index = 1;
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.edit_plug);
//		data = new PartData_loader();
//		bar_but1 = (Button) findViewById(R.id.bar_but1);
//		bar_but2 = (Button) findViewById(R.id.bar_but2);
//		bar_but3 = (Button) findViewById(R.id.bar_but3);
//
//		bar_but1.setOnClickListener(this);
//		bar_but2.setOnClickListener(this);
//		bar_but3.setOnClickListener(this);
//
//		intiListview(false, true);
//
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Map<String, Object> map = SURPERDATA.get(arg2 - 1);
//				// {id=2, but1=2130837558, charge=0, isdefault=0, chargeremark=,
//				// description=,
//				// name=高招院校插件, favor=0, expirydate=, recommend=0}
//
//				Intent intent = new Intent(CustomPlug.this, PlugInfo.class);
//				intent.putExtra("description", (String) map.get("description"));
////				intent.putExtra("favor", (String) map.get("favor"));
//				intent.putExtra("name", (String) map.get("name"));
//				intent.putExtra("id", (String) map.get("id"));
//				startActivity(intent);
//			}
//		});
//		dialogUtil = new DialogUtil(this);
//	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//		setTitle("插件市场");
//		if (adapter != null)
//			adapter.notifyDataSetChanged();
//		
////		top_back.setVisibility(View.GONE);s
//		top_commit.setText("确定");
//		top_commit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(CustomPlug.this,
//						com.eyunda.part1.plughome.LoginActivity.class));
//
//				finish();
//
//			}
//		});
//	}
//
//	@Override
//	protected void loadDate() {
//		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
//			@Override
//			public void onStart() {
//				super.onStart();
//				onListviewStart();
//			}
//
//			@Override
//			public void onSuccess(String content) {
//				STRINGLIST = DataConvert.toArrayList(content);
//				onListviewSuccess();
//			}
//
//			@Override
//			public void onFailure(Throwable error, String content) {
//				super.onFailure(error, content);
//				onListviewonFailure();
//			}
//		};
//		if (tab_index == 3) {
//			data.myPlugin(handler, Config.USERID);
//		}else if(tab_index==1){
//			data.recommendedPlugins(handler);
//		} else
//			data.pluginService(handler);
//
//	}
//
//	MyBaseAdapter adapter;
//
//	@Override
//	protected BaseAdapter setAdapter() {
//		adapter = new MyBaseAdapter(this, SURPERDATA, R.layout.plug_item,
//				new String[] { "name", "description", "but1", }, new int[] {
//						R.id.plug_name, R.id.plug_dis, R.id.but1 }) {
//			@Override
//			public void iniview(View arg0, final int arg1,
//					List<? extends Map<String, ?>> arg2) {
//
//				ImageView but1 = (ImageView) arg0.findViewById(R.id.but1);
//				ImageView but2 = (ImageView) arg0.findViewById(R.id.but2);
//				ImageView college_logo = (ImageView) arg0
//						.findViewById(R.id.college_logo);
//
//				String id = (String) SURPERDATA.get(arg1).get("id");
//				Map<String, Object> tm = Plug_map.STATICMAP.get(id);
//
//				if (tm != null) {
//
//					college_logo.setImageResource((Integer) tm.get("logo"));
//				} else
//					college_logo.setImageResource(R.drawable.ic_launcher);
//
//				but1.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (!PlugUtil.isFavor((String) SURPERDATA.get(arg1)
//								.get("id"))) {
//						
//							addPlug((String) SURPERDATA.get(arg1).get("id"));
//
//						} else {
//							delPlug((String) SURPERDATA.get(arg1).get("id"));
//						
//						}
//
//						//adapter.notifyDataSetChanged();
//					}
//				});
//				but2.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (!PlugUtil.isFavor((String) SURPERDATA.get(arg1)
//								.get("id"))) {
//							 
//							addPlug((String) SURPERDATA.get(arg1).get("id"));
//						} else{
//							delPlug((String) SURPERDATA.get(arg1).get("id"));
//						}
//							 
//					}
//				});
//
//				if (PlugUtil.isFavor((String) SURPERDATA.get(arg1).get("id"))) {
//					but2.setImageResource(R.drawable.plug_checked);
//				} else
//					but2.setImageResource(0x00ffffff);
//			}
//		};
//		adapter.setViewBinder();
//		return adapter;
//	}
//
//	DialogUtil dialogUtil;
//	ProgressDialog dialog;
//
//	private void delPlug(final String plugId) {
//		if (!Config.USERID.equals("")) {
//
//			data.unchoosePlugin(new AsyncHttpResponseHandler() {
//				@Override
//				public void onStart() {
//					super.onStart();
//					dialog = dialogUtil.loading("提示", "取消插件...");
//				}
//
//				@Override
//				public void onSuccess(String content) {
//					super.onSuccess(content);
//					dialog.dismiss();
//					Map<String, String> tm = DataConvert.toMap(content);
//					if (tm.get("result").equals("0")) {
//						Toast.makeText(CustomPlug.this, "取消插件成功",
//								Toast.LENGTH_SHORT).show();
//						PlugUtil.removeFavor(plugId);
//						PlugUtil.removeFavor((String) plugId);
//						adapter.notifyDataSetChanged();
//					} else {
//
//						Toast.makeText(CustomPlug.this, tm.get("content"),
//								Toast.LENGTH_SHORT).show();
//					}
//
//				}
//
//				@Override
//				public void onFailure(Throwable error) {
//					super.onFailure(error);
//					dialog.dismiss();
//				}
//			}, Config.USERID, plugId);
//
//		}
//
//	}
//
//	private void addPlug(final String plugId) {
//
//		if (!Config.USERID.equals("")) {
//
//			data.choosePlugin(new AsyncHttpResponseHandler() {
//				@Override
//				public void onStart() {
//					super.onStart();
//					dialog = dialogUtil.loading("提示", "选择插件...");
//				}
//
//				@Override
//				public void onSuccess(String content) {
//					super.onSuccess(content);
//					dialog.dismiss();
//					Map<String, String> tm = DataConvert.toMap(content);
//					if (tm.get("result").equals("0")) {
//						Toast.makeText(CustomPlug.this, "选择插件成功",
//								Toast.LENGTH_SHORT).show();
//						PlugUtil.addFavor(plugId);
//						adapter.notifyDataSetChanged();
//					} else {
//
//						Toast.makeText(CustomPlug.this, tm.get("content"),
//								Toast.LENGTH_SHORT).show();
//					}
//
//				}
//
//				@Override
//				public void onFailure(Throwable error) {
//					super.onFailure(error);
//					dialog.dismiss();
//				}
//			}, Config.USERID, plugId);
//
//		}
//
//	}
//
//	@Override
//	protected void formatData() {
//		for (Map<String, String> tm : STRINGLIST) {
//			Map<String, Object> otm = new HashMap<String, Object>();
//
//			for (String ts : tm.keySet()) {
//				otm.put(ts,
//						tm.get(ts).replaceAll("\r\t", "")
//								.replaceAll("\r\n", ""));
//				if (ts.equals("charge")) {
//					if (tm.get(ts).equals("0")) {
//						otm.put("but1", R.drawable.plug_free);
//					} else {
//						otm.put("but1", R.drawable.plug_notfree);
//					}
//				}
//
//			}
//			SURPERDATA.add(otm);
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//
//		case R.id.bar_but1:
//			bar_but1.setTextColor(0xff4B7DD2);
//			bar_but2.setTextColor(0xffB0B0B0);
//			bar_but3.setTextColor(0xffB0B0B0);
//			tab_index = 1;
//			break;
//		case R.id.bar_but2:
//			bar_but1.setTextColor(0xffB0B0B0);
//			bar_but2.setTextColor(0xff4B7DD2);
//			bar_but3.setTextColor(0xffB0B0B0);
//			tab_index = 2;
//			break;
//		case R.id.bar_but3:
//			if (Config.USERID.equals("")) {
//				dialogUtil.showDialogFromConfig("提醒",
//						"\u3000\u3000查看我的插件需要先登录<br>你是否现在登录？", new Handler() {
//
//							@Override
//							public void handleMessage(Message msg) {
//								super.handleMessage(msg);
//
//								startActivity(new Intent(CustomPlug.this,
//										com.eyunda.main.reg.LoginActivity.class));
//							}
//						});
//				return;
//			}
//			bar_but1.setTextColor(0xffB0B0B0);
//			bar_but2.setTextColor(0xffB0B0B0);
//			bar_but3.setTextColor(0xff4B7DD2);
//			tab_index = 3;
//			break;
//
//		default:
//			break;
//		}
//
//		SUPERPAGENUM = 0;
//		loadDate();
//	}
//
//}
