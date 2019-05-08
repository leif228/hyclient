package com.eyunda.third.activities.oil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.oil.OilAdapter;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.SaleTypeCode;
import com.eyunda.third.domain.oil.GasWaresData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 *油品列表
 * 
 * @author
 *
 */
public class OilListActivity extends CommonActivity implements
		OnItemClickListener, OnScrollListener {
	Data_loader data;

	private ListView listView;
	private OilAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;


	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	protected int totalPages;
	protected int page = 1;

	private int visibleLastIndex = 0;// 最后的可视项索引 ;
	private int visibleItemCount;
	Boolean isLoad = false;
	Boolean isFirst = true;
	protected String seletedDate;
	protected String start = "";
	protected String end = "";
	UserData user = GlobalApplication.getInstance().getUserData();
	private String type;

	private SaleTypeCode saleType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_oil_lists);
		data = new Data_loader();
		
		Intent intent = getIntent();
		type  = intent.getStringExtra("saleType");
		if(type!=null&&!"".equals(type))
			saleType = SaleTypeCode.valueOf(type);
		
		initView();

	}



	private void initView() {
		
		listView = (ListView) findViewById(R.id.mOilList);
		
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView); // 设置列表底部视图
		loadMoreButton = (ProgressBar) loadMoreView
				.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		listView.setOnItemClickListener(this);
		dataList = new ArrayList<Map<String, Object>>();
		smpAdapter = new OilAdapter(OilListActivity.this, dataList);
		listView.setAdapter(smpAdapter);
		getData();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("商品列表");


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

	

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			if (page < totalPages) {
				loadMoreText.setText("查看更多");
			} else
				// listView.removeFooterView(loadMoreView);
				loadMoreView.setVisibility(View.GONE);

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
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
				ConvertData cd = new ConvertData(arg0,
						"/mobile/gas/gasWaresList", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>) cd
							.getContent();
					List<Map<String, Object>> content = (List<Map<String, Object>>) ma
							.get("oilDatas");
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
					Toast.makeText(OilListActivity.this, cd.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(OilListActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(OilListActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(OilListActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(OilListActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();
			}
		};
		params.put("saleType", saleType!=null?saleType.toString():"");
		params.put("pageNo", page);
		data.getApiResult(handler, "/mobile/gas/gasWaresList", params, "get");
		return dataList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		HashMap<String, Object> map = (HashMap<String, Object>) listView
				.getItemAtPosition(position);
		Intent intent = new Intent(OilListActivity.this,
				OilPreviewActivity.class);
		intent.putExtra("id", map.get("oilId").toString());
		intent.putExtra("name", map.get("oilName").toString());
		startActivity(intent);
	}
	final LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
	final UserData userData=GlobalApplication.getInstance().getUserData();

	private void setData(List<Map<String, Object>> content, int i) {
				
		GasWaresData gas = new GasWaresData(content.get(i));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("oilId", gas.getId().toString());
		map.put("oilName", gas.getWaresName());

		map.put("oilImage", gas.getWaresLogo());

		map.put("oilPrice", " 售价:" + gas.getPrice());
		map.put("oilDepartPrice", "市场价:" + gas.getStdPrice());
		map.put("oilCompay", "公司名称:" + gas.getCompanyData().getCompanyName());
		map.put("oilDescrip", "商品描述:" + gas.getDescription());
		map.put("ads", gas.getPriceSignal());
		map.put("btnBuy", false);
		if(userData!=null && isLogin.equals(LoginStatusCode.logined)){
			map.put("btnBuy", true);
		}
		
		// 滚动到最底部
		// mListView.setSelection(mListView.getAdapter().getCount()-1);
		dataList.add(map);

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
		boolean isRefresh = getIntent().getBooleanExtra("success", false);
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
