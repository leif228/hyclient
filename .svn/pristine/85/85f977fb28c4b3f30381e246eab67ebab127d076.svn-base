package com.eyunda.third.activities.ship;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.map.ShipDynamicActivity;
import com.eyunda.third.adapters.ship.ShipLineAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.eyunda.tools.DateUtils;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶航线列表
 * 
 * @author
 *
 */
public class ShipLineListActivity extends CommonActivity implements
		OnItemClickListener,OnScrollListener  {
	Data_loader data;

	private ListView listView;
	private ShipLineAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;

	private String mmsi, shipId, shipName;

	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadMoreText;
	private int visibleLastIndex=0;//最后的可视项索引 ;
	public int page = 1;
	protected int totalPages;
	private int visibleItemCount;
	private TextView loadingText;
	
	private LinearLayout container ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_myship_moniter_list);
		Intent intent = getIntent();
		mmsi = intent.getStringExtra("mmsi");
		shipName = intent.getStringExtra("shipName");
		shipId = intent.getStringExtra("shipId");
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		this.listView = (ListView) findViewById(R.id.myship_list);
		dataList = new ArrayList<Map<String, Object>>();

		setTitle(getResources().getString(R.string.app_continue));
		
		container = (LinearLayout)findViewById(R.id.tab_select_container);
		
		//加载更多
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView);    //设置列表底部视图
		
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		loadMoreText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//点击查看更多，圈显示，文字显示加载更多，继续加载
				loadMoreButton.setVisibility(View.VISIBLE);
				if(page<totalPages){
					page = page+1;
					loadMoreText.setText("加载中...");

					getData(page);

				}else{
					listView.removeFooterView(loadMoreView);
					Toast.makeText(ShipLineListActivity.this, "数据全部加载完!", Toast.LENGTH_SHORT).show();
				}

			}
		});	
		
		smpAdapter = new ShipLineAdapter(ShipLineListActivity.this, dataList);
		listView.setAdapter(smpAdapter);
		listView.setOnItemClickListener(this);
		getData(page);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(shipName.equals("")){
			setTitle("船舶航次");
		}else{
			setTitle(shipName+"航次列表");
		}
		setRight("关闭", new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		container.setVisibility(View.GONE);

	}

	private void getData(int page) {
		final Map<String, Object> params = new HashMap<String, Object>();
		// 请求
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				SharedPreferencesUtils sp = new SharedPreferencesUtils("/mobile/monitor/historyRoutes", params);
				sp.setParam(arg0);
				
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					HashMap ma = (HashMap<String, String>) cd.getContent();
					List content = (ArrayList<String>) ma.get("shipArvlftDatas");
					totalPages =  ((Double) ma.get("total")).intValue();
					if(totalPages == 1){
						listView.removeFooterView(loadMoreView);
					}
					int size = content.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
								ShipStopData shipArvlftData = new ShipStopData((Map<String, Object>) content.get(i));
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("shipId", shipId); // shipId
								map.put("mmsi", mmsi);
								map.put("shipArvlftId", shipArvlftData.getId());
								map.put("startTime", shipArvlftData.getStartTime());
								map.put("endTime", shipArvlftData.getEndTime());
								map.put("startPort", shipArvlftData.getGoPortData().getFullName());
								map.put("endPort", shipArvlftData.getPortData().getFullName());
								map.put("useTime", "航行时长:"+shipArvlftData.getHours()+"小时"+shipArvlftData.getMinutes()+"分");
								
								String a =shipArvlftData.getJobStartTime();
								String b =shipArvlftData.getJobEndTime();
								String time =DateUtils.getDistanceTime2(a, b, "yyyy-MM-dd HH:mm");
								map.put("jobTime", "港作时长:"+time);
								map.put("jobDistance", "港作里程:"+shipArvlftData.getJobDistance()+"公里");
								DecimalFormat df=new DecimalFormat(".#");
								map.put("pathLong", "航行里程:"+df.format(shipArvlftData.getDistance())+"公里");
								dataList.add(map);

						}
					}
					smpAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ShipLineListActivity.this,cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipLineListActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipLineListActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(ShipLineListActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipLineListActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();
			}
		};
	
		params.put("id", shipId);
		params.put("pageNo", page);
		
		data.getApiResult(handler, "/mobile/monitor/historyRoutes", params, "get");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
		Intent intent = new Intent(ShipLineListActivity.this,ShipDynamicActivity.class);
		// 绑定传输的船舶数据
		intent.putExtra("id", map.get("shipId").toString());
		intent.putExtra("shipArvlftId", map.get("shipArvlftId").toString());
		intent.putExtra("mmsi", map.get("mmsi").toString());
		intent.putExtra("shipName", shipName);
		intent.putExtra("startTime", map.get("startTime").toString());
		intent.putExtra("endTime", map.get("endTime").toString());
		intent.putExtra("startPort", map.get("startPort").toString());
		intent.putExtra("endPort", map.get("endPort").toString());
		startActivity(intent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			//移动到最底部，文字变为查看更多
			if(page<totalPages)
				loadMoreText.setText("查看更多");
			else
				listView.removeFooterView(loadMoreView);

		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		//如果所有的记录选项等于数据集的条数，则移除列表底部视图
		//	        if(visibleItemCount == totalItemCount+1){
		//	            listView.removeFooterView(loadMoreView);
		//	            Toast.makeText(this, "数据全部加载完!", Toast.LENGTH_LONG).show();
		//	        }
	}

}
