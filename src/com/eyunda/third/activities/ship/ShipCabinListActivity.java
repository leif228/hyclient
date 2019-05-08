package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.ship.ShipCabinData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶接货
 *
 */
public class ShipCabinListActivity extends CommonActivity implements OnItemClickListener,OnScrollListener {
	Data_loader dataLoader = new Data_loader();
	CommonAdapter<ShipCabinData> cabinListAdapter;
	List<ShipCabinData> cabinList;
	UserData currentUser;
	
	int pageNo = 1, totalPages = 1, page = 1;;
	String startTime;
	String endTime;
	
	ListView cabinListView;
	
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	private int visibleLastIndex = 0;// 最后的可视项索引 ;
	private int visibleItemCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_carbin_list);
		currentUser = GlobalApplication.getInstance().getUserData();
		cabinList = new ArrayList<ShipCabinData>();
		pageNo = getIntent().getIntExtra("pageNo", 1);
		startTime = getIntent().getStringExtra("startTime");
		endTime = getIntent().getStringExtra("endTime");
		setViews();
		addLoadMore();
		

	}
	private void setViews() {
		
		cabinListView = (ListView)findViewById(R.id.carbinList); 
		cabinListAdapter = new CommonAdapter<ShipCabinData>(getApplicationContext(),cabinList,R.layout.eyd_carbin_item) {
			
			@Override
			public void convert(ViewHolder helper, ShipCabinData item) {
				helper.setImageByUrl(R.id.shipLogo, ApplicationConstants.IMAGE_URL+item.getShipData().getShipLogo());
				helper.setText(R.id.shipName, item.getShipData().getShipName());
				helper.setText(R.id.shipMaster, item.getShipData().getMaster().getTrueName());
				helper.setText(R.id.etaTime, item.getArrivePortTime());
				helper.setText(R.id.endPort, item.getPortData().getFullName());
				helper.setText(R.id.remark, item.getRemark());
			}
		};
		cabinListView.setOnScrollListener(this);
		cabinListView.setOnItemClickListener(this);
	}
	private void addLoadMore() {
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		//cabinListView.addFooterView(loadMoreView); // 设置列表底部视图
		loadMoreButton = (ProgressBar) loadMoreView.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		loadMoreView.setOnClickListener(null);
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
					page++;
					getDatas();
			}
		});
	}

	private void getDatas() {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageNo", pageNo);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			};

			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/cabin/AllCabins", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> cabins = (List<Map<String, Object>>) content.get("cabins");
					totalPages = ((Double) content.get("totalPages")).intValue();
					for(Map<String, Object> m:cabins){
						ShipCabinData scd = new ShipCabinData(m);
						cabinList.add(scd);
					}
					cabinListAdapter.notifyDataSetChanged();
				}
				
			};

			public void onFailure(Throwable arg0, String arg1) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();

			};
		};
		dataLoader.getApiResult(handler, "/mobile/cabin/AllCabins", params, "get");

	}

	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶接货");
		if(currentUser != null){
			if(canPublic()){
				setRight(R.drawable.zx_faver_top, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(getApplicationContext(),ShipCabinActivity.class));
						finish();
					}
				});
			}
		}
		cabinList.clear();
		getDatas();
	};
	//当前用户是否具有上报权限
	private boolean canPublic() {
		return true;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = cabinListAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
			int footerSize = cabinListView.getFooterViewsCount();
			if (page < totalPages) {
				if(footerSize == 0){
					cabinListView.addFooterView(loadMoreView); 
				}
				loadMoreText.setText("查看更多");
				loadMoreView.setVisibility(View.VISIBLE);
			} else{
				loadMoreView.setVisibility(View.GONE);
				if(footerSize > 0){
					cabinListView.removeFooterView(loadMoreView);
				}
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(canPublic()){
			HashMap<String, Object> map = (HashMap<String, Object>) cabinListView.getItemAtPosition(position);
			Intent intent = new Intent(getApplicationContext(), ShipCabinActivity.class);
			intent.putExtra("cabinId", (Long)map.get("cabinId"));
			startActivity(intent);
		}
	}


}
