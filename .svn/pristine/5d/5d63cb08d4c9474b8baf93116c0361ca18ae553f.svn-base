package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.activities.ship.widget.MyListView;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipFragment extends Fragment implements View.OnClickListener,OnItemClickListener,OnScrollListener {

	private MyListView mListView;
	private TextView findShip,shipType,dongtai,agent;  
	private CommonAdapter<ShipData> mAdapter;
	ArrayList<ShipData>  shipDatas;
	Data_loader dataLoader;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	private int visibleLastIndex;
	private int visibleItemCount;
	protected int totalPages;
	private int page=1;
	private int position=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ship, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mListView =(MyListView)this.getView().findViewById(R.id.listView1);
		findShip = (TextView)this.getView().findViewById(R.id.findShip);
		shipType = (TextView)this.getView().findViewById(R.id.shipType);
		dongtai = (TextView)this.getView().findViewById(R.id.dongtai);
		agent = (TextView)this.getView().findViewById(R.id.agent);

		findShip.setOnClickListener(this);
		shipType.setOnClickListener(this);
		dongtai.setOnClickListener(this);
		agent.setOnClickListener(this);
		
		dataLoader = new Data_loader();
		
		loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.loadmore, null);
		
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		mListView.addFooterView(loadMoreView);    //设置列表底部视图
		shipDatas = new ArrayList<ShipData>();

		mListView.setAdapter(mAdapter = new CommonAdapter<ShipData>(  
				getActivity(), shipDatas, R.layout.eyd_home_one_item)  
        {  
            @Override  
            public void convert(ViewHolder viewHolder, ShipData item)  
            {  
                TextView shipName = viewHolder.getView(R.id.boat_name);  
                shipName.setText(item.getShipName()+"("+item.getMmsi()+")");
                String logo = item.getShipLogo();
                String url = "";
                if(logo.equals("")){
                	url = ApplicationConstants.SERVER_URL + "/img/shipImage/"+item.getShipType()+".jpg";
                }else{
                	url = ApplicationConstants.IMAGE_URL+logo;
                }
                viewHolder.setImageByUrl(R.id.boat_logo,url);
                //Log.d("test", ApplicationConstants.IMAGE_URL+item.getShipLogo());
                TextView shipId = viewHolder.getView(R.id.boat_id);
                shipId.setText(item.getId().toString());
                TextView mmsi = viewHolder.getView(R.id.boat_mmsi);
                //mmsi.setText(item.getMmsi());
                
                TextView totalTon = viewHolder.getView(R.id.boat_area);
                totalTon.setText("总吨:"+item.getSumTons().toString());
                TextView shipType = viewHolder.getView(R.id.boat_type);
                shipType.setText("类别:"+item.getTypeData().getTypeName());
                TextView carrier = viewHolder.getView(R.id.boat_cyr);
                String trueName = item.getMaster().getTrueName();
                if(null == item.getMaster().getTrueName() || "".equals(trueName)){
//                	carrier.setText("承运人:"+item.getMaster().getLoginName());
                	mmsi.setText("承运人:"+item.getMaster().getLoginName());
                }else{
//                	carrier.setText("承运人:"+item.getMaster().getTrueName());
                	mmsi.setText("承运人:"+item.getMaster().getTrueName());
                }
                TextView time = viewHolder.getView(R.id.boat_times);
                time.setText("发布日期:"+item.getReleaseTime());
                TextView dt = viewHolder.getView(R.id.boat_dt);
                dt.setText("动态:"+item.getArvlftDesc());
                
            }  
  
        });  
		mListView.setOnItemClickListener(this);
		loadMoreView.setOnClickListener(null);
		mListView.setOnScrollListener(this);
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				if(page<totalPages){
					loadMoreText.setText("查看更多");
					page++;
					loadData();
				}else{
					mListView.removeFooterView(loadMoreView);
				}
			}
		});	
		loadData();
	}
	
	public synchronized void loadData(){
		final HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("pageNo",page);
//		hashMap.put("attrValueCode", );
//		hashMap.put("bigAreaCode", Long.parseLong(paramSplit2[0]));
//		hashMap.put("arvlft", ArvlftCode.arrive.toString());
		//读取最新船舶列表
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0,"/mobile/home/shipDynamic",hashMap) ;
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>) cd
							.getContent();
					List<Map<String, Object>> list = (List<Map<String, Object>>) ma
							.get("dynamicList");
					totalPages = ((Double)ma.get("pageNo")).intValue();
					if(totalPages <=1  || page == totalPages){
						mListView.removeFooterView(loadMoreView);
					}else{
						loadMoreText.setText("查看更多");
						loadMoreText.setVisibility(View.VISIBLE); 
					}
					if (list.size() > 0) {
						for (Map<String, Object> mm : list) {
							ShipData shipData = new ShipData(mm);
							shipDatas.add(shipData);
						}
					}
					mAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(getActivity(),
							cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(getActivity(), "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(getActivity(), "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(getActivity(), content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(getActivity(), "未知异常",
							Toast.LENGTH_LONG).show();

			}
		};

		dataLoader.getApiResult(handler, "/mobile/home/shipDynamic",hashMap,"get");
		
//		hashMap.put("recvCargoPort",null);
//		hashMap.put("cargoType",null);
//		hashMap.put("cargoWeight",null);
//		hashMap.put("cargoVolumn",null);
		//dataLoader.getApiResult(handler, "/mobile/home/shipHome", hashMap, "get");
		
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = mAdapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
	
			if(page<totalPages){
				loadMoreText.setText("查看更多");
			}else
				mListView.removeFooterView(loadMoreView);
				//loadMoreView.setVisibility(View.GONE);
		}

	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()){
		case R.id.findShip:
			Intent intent4 = new Intent(getActivity(),FindShipActivity.class);
			startActivity(intent4);
			break;
		case R.id.shipType:
			intent = new Intent(getActivity(),HomeCategoryActivity.class);
			intent.putExtra("type", HomeCategoryActivity.MSG_SHIP_LIST);
			getActivity().startActivity(intent);
			break;
		case R.id.dongtai:
			intent = new Intent(getActivity(),HomeCategoryActivity.class);
			intent.putExtra("type", HomeCategoryActivity.MSG_DYNAMIC_LIST);
			getActivity().startActivity(intent);

			break;
		case R.id.agent:

			intent = new Intent(getActivity(),HomeSearchResultActivity.class);
			Bundle bundleAgent = new Bundle();
			bundleAgent.putString("param", UserRoleCode.broker.name());
			bundleAgent.putString("title", "船代");
			bundleAgent.putLong("type",HomeCategoryActivity.MSG_DLR_LIST);
			intent.putExtras(bundleAgent);
			getActivity().startActivity(intent);
			break;
			
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.position =position;
		ShipData map = (ShipData) mListView.getItemAtPosition(position);

		Intent intent = new Intent(getActivity(),ShipPreviewActivity.class);
		intent.putExtra("id", map.getId()+"");
		intent.putExtra("name", map.getShipName());
		intent.putExtra("type", 0);
		startActivity(intent);
		
	}
	@Override
	public void onResume() {//恢复当前查看的船舶位置
		super.onResume();
		mListView.setSelection(position);
	}

}