package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.adapters.ship.ArvLftAdapter;
import com.eyunda.third.adapters.ship.DynamicAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.ArvlftCode;
import com.eyunda.third.domain.ship.ArvlftShipData;
import com.eyunda.third.domain.ship.ShipArvlftData;
import com.eyunda.third.domain.ship.ShipUpdownData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ArvLftListActivity extends CommonActivity implements OnScrollListener {

	private ListView mListView;
	private ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	private ArvLftAdapter dtAdapter;
	protected View loadMoreView;
	protected View loadMoreButton;
	protected View loadingText;
	private TextView loadMoreText;
	protected int totalPages;
	private int visibleLastIndex = 0;
	private int page = 1;
	private Data_loader data;
	private int visibleItemCount;
	protected String start = "";
	protected String end = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_arrive_left);
		initViews();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(DynamicAdapter.shipName + "船舶动态");
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user.isChildUser() && !user.getShips().equals("")) { // 如果是子账号并且有操作船的权限

			setRight(R.drawable.zx_faver_top, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent addIntent = new Intent(ArvLftListActivity.this, ShipArvlftActivity.class);
					addIntent.putExtra("id", "0");
					startActivity(addIntent);
				}
			});
		}
		setRightBtn(R.drawable.ic_action_search, new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog();
			}
		});
	}

	protected void AlertDialog() {

		LayoutInflater inflater = LayoutInflater.from(ArvLftListActivity.this);
		View view = inflater.inflate(R.layout.search_cargo_item, null);
		final TextView tv_start = (TextView) view.findViewById(R.id.et_start);
		final TextView tv_end = (TextView) view.findViewById(R.id.et_end);

		DatePickerFragment datePicker = new DatePickerFragment(tv_start);
		DatePickerFragment datePicker2 = new DatePickerFragment(tv_end);
		tv_start.setText(datePicker.getLastMonth());
		tv_end.setText(datePicker2.getCurrentTime());
		tv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePicker = new DatePickerFragment(tv_start);
				datePicker.show(getFragmentManager(), "datePicker");

			}
		});
		tv_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePicker = new DatePickerFragment(tv_end);
				datePicker.show(getFragmentManager(), "datePicker2");

			}
		});
		new AlertDialog.Builder(ArvLftListActivity.this).setTitle("查找货物").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info).setNegativeButton("取消", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						start = "";
						end = "";
						dataList.clear();
						page = 1;
						start = tv_start.getText().toString().trim();
						end = tv_end.getText().toString().trim();
						getData();
					}
				}).show();

	}

	private void initViews() {
		data = new Data_loader();
		mListView = (ListView) findViewById(R.id.dt_list);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		loadMoreButton = (ProgressBar) loadMoreView.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		dtAdapter = new ArvLftAdapter(this, dataList);
		mListView.setAdapter(dtAdapter);
		// mListView.setFooterDividersEnabled(false);
		mListView.addFooterView(loadMoreView); // 设置列表底部视图
		mListView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		getData();
		load();
	}

	private void load() {
		loadMoreText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击查看更多，圈显示，文字显示加载更多，继续加载
				loadMoreButton.setVisibility(View.VISIBLE);
				if (page < totalPages) {
					loadMoreText.setText("加载中...");
					page = page + 1;

					getData();

				} else {
					mListView.removeFooterView(loadMoreView);
				}

			}
		});

	}

	private ArrayList<Map<String, Object>> getData() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				loadMoreView.setVisibility(View.VISIBLE);
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");

			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				loadMoreButton.setVisibility(View.GONE);
				loadingText.setVisibility(View.GONE);
				ConvertData cd = new ConvertData(arg0, "/mobile/state/shipApply/shipArvlft", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> maps = (List<Map<String, Object>>) content.get("pageData");
					totalPages = ((Double) content.get("totalPages")).intValue();

					for (Map<String, Object> mm : maps) {

						ArvlftShipData avf = new ArvlftShipData(mm);
						ShipArvlftData arrive = avf.getArriveData();
						ShipArvlftData left = avf.getLeftData();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("data", avf);
						map.put("remark", "备注:" + arrive.getRemark()); // 到港备注
						map.put("id", arrive.getId().toString());
						map.put("port", "港口：" + arrive.getPortData().getFullName()); // 到港口
						map.put("goPort", "下一港口:" + left.getGoPortData().getFullName()); // 下一港口
						// map.put("portNo", avf.getPortNo());
						// map.put("goPortNo", avf.getGoPortNo());
						// map.put("PortName", avf.getPortData().getPortName());
						map.put("time1", "到港时间:" + arrive.getArvlftTime());
						map.put("time2", "离港时间:" + left.getArvlftTime());
						if (arrive.getArvlft().equals(ArvlftCode.left)) {
							map.put("zhuanghuo", "装货:");
						} else {
							map.put("zhuanghuo", "卸货:");
						}
						List<ShipUpdownData> upList = avf.getLeftData().getShipUpdownDatas();
						List<ShipUpdownData> downList = avf.getArriveData().getShipUpdownDatas();
						if (upList != null) {
							map.put("up",
									upList.toString().substring(1, upList.toString().length() - 1).replace(", ", "\n"));
						} else
							map.put("up", "无");
						if (downList != null) {
							map.put("down",
									downList.toString().substring(1, downList.toString().length() - 1)
											.replace(", ", "\n"));
						} else
							map.put("down", "无");
						dataList.add(map);
					}
					dtAdapter.notifyDataSetChanged();
				}

			}

			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				mListView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ArvLftListActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ArvLftListActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(ArvLftListActivity.this, content, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ArvLftListActivity.this, "请求失败", Toast.LENGTH_LONG).show();

			};

		};
		params.put("pageNo", page);
		params.put("mmsi", DynamicAdapter.mmsi);
		params.put("startTime", start);
		params.put("endTime", end);
		data.getApiResult(handler, "/mobile/state/shipApply/shipArvlft", params, "get");

		return dataList;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = dtAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
			// 移动到最底部，文字变为查看更多
			if (page < totalPages) {

				loadMoreText.setText("查看更多");
			} else
				mListView.removeFooterView(loadMoreView);

		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		Log.v("visibleItemCount", this.visibleItemCount + "");
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		dataList.clear();
		refresh();
		dtAdapter.notifyDataSetChanged();
	}

	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			runOnUiThread(new Runnable() {
				public void run() {
					getData();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
