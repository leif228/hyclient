package com.eyunda.third.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

import com.ygl.android.view.YFListView;
import com.ygl.android.view.listview.OnRefreshListener;

/**
 * 列表基础类
 * 
 * @author user_ygl 初始化所有父类的抽象方法 可用的父类变量 SUPERPAGENUM 页数 从0开始 SURPERDATA
 *         listview dataset formatDate 需要将 STRINGLIST 转化为 SURPERDATA
 */
public abstract class BaseListActivity extends Activity {
	public YFListView listview;
	int lastItem = 0;
	protected String EMTYTEXT = "";
	ListViewUtil footbar;
	public int SUPERPAGENUM = 0;
	public List<Map<String, Object>> SURPERDATA = new ArrayList<Map<String, Object>>();
	public List<Map<String, String>> STRINGLIST;
	private boolean loading = false;
	private BaseAdapter super_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化
	 * 
	 * @param load
	 *            是否显示底部进度条
	 * @param showHead
	 *            是否下拉刷新
	 */
	public void intiListview(boolean load, boolean showHead) {
		// this.listview = (YFListView) findViewById(R.id.listview);

		int id = this.getResources().getIdentifier("listview", "id",
				this.getPackageName());
		this.listview = (YFListView) findViewById(id);
		listview.load = load;
		listview.showHead = showHead;
		listview.init(this);

		if (footbar == null)
			footbar = new ListViewUtil(this, listview, getResources());
		listview.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				SUPERPAGENUM = 0;
				loadDate();
				// new AsyncTask<Void, Void, Void>() {
				// @Override
				// protected Void doInBackground(Void... params) {
				// SUPERPAGENUM = 0;
				// STRINGLIST = setDataMethod();
				// return null;
				// }
				//
				// @Override
				// protected void onPostExecute(Void result) {
				// addData();
				// listview.onRefreshComplete();
				// }
				//
				// @Override
				// protected void onPreExecute() {
				// if (listview.load) {
				// footbar.removeFootBar();
				// footbar.addFootBar();
				// }
				//
				// }
				// }.execute();
			}

			@Override
			public void addDate() {

				// doSetListView();
				if (!loading && System.currentTimeMillis() - passload > 100) {
					passload = System.currentTimeMillis();
					loadDate();
				}

			}

		});
		// doSetListView();
		loadDate();
	}

	long passload = 0;

	// protected synchronized void doSetListView() {
	// if (loading)
	// return;
	// loading = true;
	// if (SUPERPAGENUM == 0) {
	// SURPERDATA.clear();
	// if (listview.load)
	// footbar.addFootBar();
	// setAdapter();
	// }
	// new Thread(SUPERRUNBLE).start();
	// }

	/**
	 * 为listview 设置adapter
	 */
	protected abstract BaseAdapter setAdapter();

	// void addData() {
	// if (SUPERPAGENUM == 0)
	// SURPERDATA.clear();
	// if (STRINGLIST == null) {
	// footbar.ALERTTEXT = EMTYTEXT;
	// footbar.showListAddDataState(ListViewUtil.IS_EMPTY + "");
	// EMTYTEXT = "";
	//
	// myNotifyDataSetChanged();
	// return;
	// }
	// if (STRINGLIST.size() == 0) {
	//
	// footbar.ALERTTEXT = EMTYTEXT;
	// footbar.showListAddDataState(ListViewUtil.IS_EMPTY + "");
	// EMTYTEXT = "";
	// myNotifyDataSetChanged();
	// return;
	// }
	//
	// SUPERPAGENUM++;
	// String state = STRINGLIST.get(0).get("state");
	// String ifSuccess = STRINGLIST.get(0).get("success");
	//
	// if (ifSuccess != null && ifSuccess.equals("true")) {
	// if (STRINGLIST.size() < 10 && listview.load)
	// footbar.removeFootBar();
	// formatData();
	// STRINGLIST.clear();
	// EMTYTEXT = "";
	// myNotifyDataSetChanged();
	// return;
	// } else if (ifSuccess != null) {
	// // STRINGLIST.get(0).put("state", ListViewUtil.ERROR + "");
	// footbar.ALERTTEXT = STRINGLIST.get(0).get("msg");
	// footbar.showListAddDataState(ListViewUtil.ERROR+"");
	// EMTYTEXT = "";
	// myNotifyDataSetChanged();
	// return;
	// }
	//
	// if (STRINGLIST.get(0).get("state") != null
	// && STRINGLIST.get(0).get("state")
	// .equals(String.valueOf(ListViewUtil.LOGIN_SUCCESS))) {
	// if (STRINGLIST.size() < 10 && listview.load)
	// footbar.removeFootBar();
	// formatData();
	// STRINGLIST.clear();
	// EMTYTEXT = "";
	// } else if (STRINGLIST.get(0).get("state") != null) {
	// footbar.ALERTTEXT = EMTYTEXT;
	// footbar.showListAddDataState(state);
	// EMTYTEXT = "";
	// }
	// myNotifyDataSetChanged();
	//
	// }

	/**
	 * 格式化返回数据 将
	 */
	protected void formatData() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			for (String ts : tm.keySet()) {
				otm.put(ts,
						tm.get(ts).replaceAll("\r\t", "")
								.replaceAll("\r\n", ""));
			}
			SURPERDATA.add(otm);
		}
	}

	/**
	 * listview 刷新
	 */
	public void myNotifyDataSetChanged() {
	}

	public List<Map<String, String>> setDataMethod() {
		return STRINGLIST;
	};

	// Runnable SUPERRUNBLE = new Runnable() {
	// public void run() {
	// try {
	// Thread.sleep(100);
	// STRINGLIST = setDataMethod();
	// loading = false;
	// SUPERHANDLER.sendMessage(SUPERHANDLER.obtainMessage());
	// } catch (InterruptedException e) {
	// }
	// }
	// };
	// Handler SUPERHANDLER = new Handler() {
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// addData();
	// }
	// };

	protected abstract void loadDate();

	public void onListviewStart() {
		if (loading)
			return;
		loading = true;
		if (SUPERPAGENUM == 0) {
			SURPERDATA.clear();
			if (listview.load) {
				footbar.removeFootBar();
				footbar.addFootBar();
			}
			super_adapter = setAdapter();
			listview.setAdapter(super_adapter);
		}

	}

	public void onListviewonFailure() {
		// SUPERPAGENUM=0;
		footbar.removeFootBar();
		listview.onRefreshComplete();
	}

	public void onListviewSuccess() {

		if (SUPERPAGENUM == 0)
			SURPERDATA.clear();
		if (STRINGLIST == null) {
			footbar.ALERTTEXT = EMTYTEXT;
			footbar.showListAddDataState(ListViewUtil.IS_EMPTY + "");
			EMTYTEXT = "";
			myNotifyDataSetChanged();
			super_adapter.notifyDataSetChanged();
			listview.onRefreshComplete();
			return;
		}
		if (STRINGLIST.size() == 0) {
			footbar.ALERTTEXT = EMTYTEXT;
			footbar.showListAddDataState(ListViewUtil.IS_EMPTY + "");
			EMTYTEXT = "";
			myNotifyDataSetChanged();
			super_adapter.notifyDataSetChanged();
			listview.onRefreshComplete();
			loading = false;
			return;
		}

		SUPERPAGENUM++;
		// String state = STRINGLIST.get(0).get("state");
		// String ifSuccess = STRINGLIST.get(0).get("success");

		// if (ifSuccess != null && ifSuccess.equals("true")) {
		if (STRINGLIST.size() < 10 && listview.load)
			footbar.removeFootBar();
		formatData();
		STRINGLIST.clear();
		EMTYTEXT = "";
		// } else if (ifSuccess != null&& ifSuccess.equals("false")) {
		// footbar.ALERTTEXT = STRINGLIST.get(0).get("msg");
		// footbar.showListAddDataState(ListViewUtil.ERROR+"");
		// EMTYTEXT = "";
		// }

		try {
			myNotifyDataSetChanged();
			super_adapter.notifyDataSetChanged();
			listview.onRefreshComplete();
			loading = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
