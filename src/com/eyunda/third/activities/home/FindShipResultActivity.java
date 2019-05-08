package com.eyunda.third.activities.home;

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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.CargoVolumnCode;
import com.eyunda.third.domain.enumeric.CargoWeightCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class FindShipResultActivity extends CommonActivity implements OnItemClickListener,OnScrollListener{
	protected String seletedTpye,seletedTon,seletedArea;
	private Data_loader dataLoader;
	private ListView mListView;
	private CommonAdapter<ShipData> mAdapter;
	ArrayList<ShipData>  mShipDatas;
	protected int totalPages;
	private int page=1;


	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	private int visibleLastIndex;
	private int visibleItemCount;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_search_result_list);
		dataLoader = new Data_loader();
		//获取请求参数
		Intent intent = getIntent();
		seletedTpye = intent.getStringExtra("seletedTpye");
		seletedTon = intent.getStringExtra("seletedTon");
		seletedArea = intent.getStringExtra("seletedArea");

		initViews();

		loadData();

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("查找船舶结果");
	}
	private void initViews() {
		mShipDatas = new ArrayList<ShipData>();
		mListView = (ListView)findViewById(R.id.listview);
		mListView.setAdapter(mAdapter = new CommonAdapter<ShipData>(  
				FindShipResultActivity.this, mShipDatas, R.layout.eyd_home_one_item)  
				{  
			@Override  
			public void convert(ViewHolder viewHolder, ShipData item)  
			{  
				TextView shipName = viewHolder.getView(R.id.boat_name); 
				shipName.setText(item.getShipName()+"("+item.getMmsi()+")");
//				shipName.setText(item.getShipName());
				String logo = item.getShipLogo();
				String url = "";
				if(logo.equals("")){
					url = ApplicationConstants.SERVER_URL + "/img/shipImage/"+item.getShipType()+".jpg";
				}else{
					url = ApplicationConstants.IMAGE_URL+logo;
				}
				viewHolder.setImageByUrl(R.id.boat_logo,url);            //Log.d("test", ApplicationConstants.IMAGE_URL+item.getShipLogo());
				TextView shipId = viewHolder.getView(R.id.boat_id);
				shipId.setText(item.getId().toString());
				TextView mmsi = viewHolder.getView(R.id.boat_mmsi);
//				mmsi.setText(item.getMmsi());
				TextView totalTon = viewHolder.getView(R.id.boat_area);
				totalTon.setText("总吨:"+item.getSumTons().toString());
				TextView shipType = viewHolder.getView(R.id.boat_type);
				shipType.setText("类别:"+item.getTypeData().getTypeName());
				TextView carrier = viewHolder.getView(R.id.boat_cyr);
				String trueName = item.getMaster().getTrueName();
				if(null == item.getMaster().getTrueName() || "".equals(trueName)){
					mmsi.setText("承运人:"+item.getMaster().getLoginName());
				}else{
					mmsi.setText("承运人:"+item.getMaster().getTrueName());
				}
				TextView time = viewHolder.getView(R.id.boat_times);
				time.setText("发布日期:"+item.getReleaseTime());
				TextView dt = viewHolder.getView(R.id.boat_dt);
				dt.setText("动态:"+item.getArvlftDesc());

			}  

				});  
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		loadMoreView.setVisibility(View.VISIBLE);
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		loadMoreView.setOnClickListener(null);
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				if(page<totalPages){
					page++;
					loadData();
				}else{
					mListView.removeFooterView(loadMoreView);
				}
			}
		});
		mListView.addFooterView(loadMoreView);    //设置列表底部视图

	}
	private synchronized void loadData(){
		//读取最新船舶列表
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				loadMoreView.setVisibility(View.VISIBLE);
				loadMoreText.setVisibility(View.GONE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				super.onSuccess(arg0);
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>) cd
							.getContent();
					List<Map<String, Object>> list = (List<Map<String, Object>>) ma
							.get("shipDatas");
					totalPages = ((Double)ma.get("total")).intValue();
					if(totalPages == page || totalPages <= 1){//?移除footer？
						mListView.removeFooterView(loadMoreView);
					}else{
						loadMoreText.setText("查看更多");
						loadMoreText.setVisibility(View.VISIBLE);
					}
					if (list.size() > 0) {
						for (Map<String, Object> mm : list) {
							ShipData shipData = new ShipData(mm);
							mShipDatas.add(shipData);
						}
					}
					mAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(getApplication(),
							cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(getApplication(), "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(getApplication(), "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(getApplication(), content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(getApplication(), "未知异常",
							Toast.LENGTH_LONG).show();

			}
		};
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("pageNo",page);
		if(!seletedArea.equals("")){
			hashMap.put("recvCargoPort",PortCityCode.valueOf(seletedArea));
		}
		if(!seletedTpye.equals("")){
			CargoTypeCode ctc = CargoTypeCode.valueOf(seletedTpye);
			if(ctc !=null ){
				hashMap.put("cargoType",ctc);
				if(!seletedTon.equals("")){
					if(ctc.equals(CargoTypeCode.container20e)){
						hashMap.put("cargoVolumn",CargoVolumnCode.valueOf(seletedTon));
					}else{
						hashMap.put("cargoWeight",CargoWeightCode.valueOf(seletedTon));
					}
				}
			}
		}
		dataLoader.getApiResult(handler, "/mobile/home/shipHome", hashMap, "get");
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ShipData map = (ShipData) mListView.getItemAtPosition(position);

		Intent intent = new Intent(getApplicationContext(),
				ShipPreviewActivity.class);
		intent.putExtra("id", map.getId()+"");
		intent.putExtra("name", map.getShipName());
		intent.putExtra("type", 0);
		startActivity(intent);
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = mAdapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {

			if(page<totalPages){
				loadMoreText.setText("查看更多");
			}else{
				mListView.removeFooterView(loadMoreView);
			}
		}		
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}
}
