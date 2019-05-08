package com.eyunda.third.activities.cargo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.adapters.cargo.CargoAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoStatusCode;
import com.eyunda.third.domain.enumeric.SourceCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 我的货物
 * 
 * @author
 *
 */
public class CargoListActivity extends CommonActivity
		implements OnItemClickListener, OnClickListener, OnScrollListener {
	Data_loader data;
	Button bar_but1, bar_but2, bar_but3;
	private ListView listView;
	private CargoAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;
	private TextView tab1, tab2;
	private String current = "noData";
	private int selectedColor;
	private int unSelectedColor;
	private ImageView imageView;
	private int bmpW;// 图片宽度
	private int offset = 0;// 动图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	protected int totalPages;
	protected int page = 1;
	/** 页卡总数 **/
	private static final int pageSize = 2;
	private int visibleLastIndex = 0;// 最后的可视项索引 ;
	private int visibleItemCount;
	Boolean isLoad = false;
	Boolean isFirst = true;
	protected String seletedDate;
	protected String start = "";
	protected String end = "";
	UserData user = GlobalApplication.getInstance().getUserData();
	private LinearLayout erContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_cargo_widgit);
		data = new Data_loader();
		initView();
		InitImageView();
	}

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.iv_tab1);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
		// = 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void initView() {
		selectedColor = getResources().getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(R.color.tab_title_normal_color);
		tab1 = (TextView) findViewById(R.id.tab_1);
		tab2 = (TextView) findViewById(R.id.tab_2);

		listView = (ListView) findViewById(R.id.mCargoList);
		erContent = (LinearLayout) findViewById(R.id.testContent);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab1.setTextColor(selectedColor);
		tab2.setTextColor(unSelectedColor);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView); // 设置列表底部视图
		loadMoreButton = (ProgressBar) loadMoreView.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		listView.setOnItemClickListener(this);
		dataList = new ArrayList<Map<String, Object>>();
		smpAdapter = new CargoAdapter(CargoListActivity.this, dataList);
		listView.setAdapter(smpAdapter);
		getData();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("货物列表");
		// 是承运人和货主

		if (user != null) {
			String urs = user.getRoles();
			String code = Integer.toString(UserRoleCode.sailor.ordinal());
			int result = urs.indexOf(code);
			if(!(result >= 0)){//船员不能加货物
				setRight(R.drawable.zx_faver_top, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 新增
						Intent intent = new Intent(CargoListActivity.this, AddCargoActivity.class);
						intent.putExtra("source", SourceCode.fromAddCargo.ordinal());
						startActivity(intent);
						finish();
					}
				});
				setRightBtn(R.drawable.ic_action_search, new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog();
					}
				});
			}else{
				setRight(R.drawable.ic_action_search, new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog();
					}
				});
			}
			/*
			if (user.isRealUser()) {
				setRight(R.drawable.zx_faver_top, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 新增
						Intent intent = new Intent(CargoListActivity.this, AddCargoActivity.class);
						intent.putExtra("source", SourceCode.fromAddCargo.ordinal());
						startActivity(intent);
					}
				});
			} else if (user.isChildUser()) {
				checkParent(user.getId());
			}
			*/
		}

		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				isFirst = false;
				isLoad = true;
				if (page < totalPages) {
					loadMoreText.setText("加载中...");

					page++;
					getData();
				} else {
					listView.removeFooterView(loadMoreView);
				}
			}
		});

	}


	protected void AlertDialog() {

		LayoutInflater inflater = LayoutInflater.from(CargoListActivity.this);
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
		new AlertDialog.Builder(CargoListActivity.this).setTitle("查找货物").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info).setNegativeButton("取消", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetFooter();
						start = tv_start.getText().toString().trim();
						end = tv_end.getText().toString().trim();
						getData();
					}
				}).show();

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
			if (page < totalPages) {
				loadMoreText.setText("查看更多");
			} else
				// listView.removeFooterView(loadMoreView);
				loadMoreView.setVisibility(View.GONE);

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		Log.d("visibleItemCount", this.visibleItemCount + "");
	}

	private ArrayList<Map<String, Object>> getData() {
		// if(user!=null && !user.isConsigner()){
		final Map<String, Object> params = new HashMap<String, Object>();
		if (dataList.isEmpty())
			loadMoreView.setVisibility(View.GONE);
		// 请求
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				loadMoreButton.setVisibility(View.GONE);
				loadingText.setVisibility(View.GONE);
				ConvertData cd = new ConvertData(arg0, "/mobile/cargo/AllCargos", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> content = (List<Map<String, Object>>) ma.get("cargos");
					totalPages = ((Double) ma.get("totalPages")).intValue();
					int size = content.size();
					if (isFirst) {// 初始显示10条数据
						dataList.clear();
						for (int i = 0; i < size; i++)
							setData(content, i);
					} else {
						for (int i = 0; i < size; i++)
							setData(content, i);
					}

					smpAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(CargoListActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(CargoListActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(CargoListActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(CargoListActivity.this, content, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(CargoListActivity.this, "请求失败", Toast.LENGTH_LONG).show();
			}
		};
		params.put("overData", current);
		if (TextUtils.isEmpty(start)) {
			start = CalendarUtil.toYYYY_MM_DD_HH_MM(CalendarUtil.addMonths(CalendarUtil.now(), -1));
		}
		if (TextUtils.isEmpty(end)) {
			end = CalendarUtil.toYYYY_MM_DD(CalendarUtil.addDays(CalendarUtil.now(), 1));
		}
		params.put("startTime", start);
		params.put("endTime", end);
		params.put("pageNo", page);
		data.getApiResult(handler, "/mobile/cargo/AllCargos", params, "get");
		return dataList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
		Intent intent = new Intent(CargoListActivity.this, CargoPreviewActivity.class);
		intent.putExtra("id", map.get("cargoId").toString());
		intent.putExtra("userId", Long.parseLong(map.get("userId").toString()));
		intent.putExtra("phone", map.get("phone").toString());
		intent.putExtra("chatName", map.get("chatName").toString());
		intent.putExtra("name", map.get("cargoName").toString());
		startActivity(intent);
	}

	private void setData(List<Map<String, Object>> content, int i) {
		UserData user = GlobalApplication.getInstance().getUserData();
		CargoData cargo = new CargoData(content.get(i));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", cargo.getOwnerId().toString());
		map.put("cargoId", cargo.getId().toString());
		map.put("chatName", cargo.getOwner().getNickName());// 聊天页面

		map.put("cargoImage", cargo.getCargoImage());
		String name = cargo.getOwner().getTrueName();
		if (name.equals("")) {
			map.put("shipper", "托运人:" + cargo.getOwner().getLoginName());
		} else
			map.put("shipper", "托运人:" + name);

		// map.put("cargoName", "货名:" + cargo.getCargoName());
		if (cargo.getCargoType().getCargoBigType().getDescription()
				.equals(CargoBigTypeCode.container.getDescription())) {
			map.put("cargoName",
					cargo.getCargoType().getCargoBigType().getDescription() + "(货号:" + cargo.getId().toString() + ")");
		} else
			map.put("cargoName", cargo.getCargoName() + "(货号:" + cargo.getId().toString() + ")");

		// map.put("cargoType", "货类:" + cargo.getCargoType().getDescription());
		if (name.equals("")) {
			map.put("cargoType", "货主:" + cargo.getOwner().getLoginName());
		} else
			map.put("cargoType", "货主:" + name);

		map.put("number", "货类:" + cargo.getCargoType().getDescription());
		map.put("cargoTypeCode", cargo.getCargoType().toString());
		map.put("timelimit", cargo.getPeriodCode());
		map.put("port", cargo.getStartFullName() + " 到 " + cargo.getEndFullName());
		if (cargo.getCargoType().getDescription().contains(CargoBigTypeCode.container.getDescription())) {
			map.put("totalPrice",
					"箱量:" + cargo.getTonTeuDes() + ",运价:" + cargo.getPriceDes() + ",运费:" + cargo.getTransFeeDes());
		} else {
			map.put("totalPrice",
					"货量:" + cargo.getTonTeuDes() + ",运价:" + cargo.getPriceDes() + ",运费:" + cargo.getTransFeeDes());
		}

		map.put("unitPrice", "单价:" + cargo.getPriceDes());
		// map.put("totalPrice", "总价:" + cargo.getTransFeeDes());
		map.put("endTime", "截止:" + cargo.getPeriodTime());
		map.put("remark", cargo.getRemark());
		map.put("cargoStatus", cargo.getCargoStatus().getDescription());
		map.put("Status", cargo.getCargoStatus());

		map.put("cargoData", cargo);
		map.put("phone", cargo.getOwner().getMobile());

		map.put("btnPublish", false);
		map.put("btnEdit", true);
		map.put("btnDelete", true);
		map.put("btnDelOper", false);
		map.put("btnCancel", false);
		map.put("notity", true);

		// 滚动到最底部
		// mListView.setSelection(mListView.getAdapter().getCount()-1);
		dataList.add(map);

	}

	@Override
	public void onClick(View v) {
		current = "";
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		switch (v.getId()) {
		case R.id.tab_1: // 我的未过期
			currIndex = 0;
			Animation animation = new TranslateAnimation(one * currIndex, 0, 0, 0);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			current = "noData";
			tab1.setTextColor(selectedColor);
			tab2.setTextColor(unSelectedColor);
			resetFooter();
			getData();

			break;
		case R.id.tab_2: // 过期的
			currIndex = 1;
			Animation animation2 = new TranslateAnimation(one * currIndex, one, 0, 0);
			animation2.setFillAfter(true);// True:图片停在动画结束位置
			animation2.setDuration(300);
			imageView.startAnimation(animation2);
			current = "overData";
			tab1.setTextColor(unSelectedColor);
			tab2.setTextColor(selectedColor);
			resetFooter();
			getData();

			break;

		}
	}

	private void resetFooter() {
		page = 1;
		start = "";
		end = "";
		isLoad = false;
		isFirst = true;
		loadMoreView.setVisibility(View.VISIBLE);
		loadMoreButton.setVisibility(View.VISIBLE);
		loadMoreText.setText("");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		boolean isRefresh = true;
		if (isRefresh) {
			dataList.clear();
			getData();
			smpAdapter.notifyDataSetChanged();
		}
	}

	public void refresh() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					dataList.clear();
					page = 1;
					getData();
					// smpAdapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
