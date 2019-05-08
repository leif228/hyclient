package com.eyunda.third.activities.home;
/**
 * 已经到
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.enumeric.ArvlftCode;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.ShipPriceData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ArriveFragment extends Fragment implements OnItemClickListener,OnScrollListener{

	private ListView mListView;
	private FragmentAdapter adapter;
	private ArrayList<Map<String, Object>> mInfos;
	Data_loader data;
	private Context mContext;
	private String param;
	private int page=1;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	protected int totalPages;
	private int visibleLastIndex=0;
	private int visibleItemCount;
	private int type;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fargment_one,container, false);
		mContext = getActivity();
		  /*通过getArgments()方法获取从Activity传过来的值*/  
         Bundle bundle = this.getArguments();  
         param = bundle.getString("param");
         type = bundle.getInt("type");
		return view;
	}
    
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.loadmore, null);
		mListView =(ListView)this.getView().findViewById(R.id.fragment_list1);
		mListView.addFooterView(loadMoreView);    //设置列表底部视图
		mListView.setFooterDividersEnabled(false);
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		mInfos= new ArrayList<Map<String,Object>>(); 
		 data = new Data_loader();
		adapter = new FragmentAdapter(mContext,mInfos);
		
		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		mListView.setOnItemClickListener(this);
		getAllData();
		loadDate();
	}
	private void getAllData() {
		switch (type) {
		case HomeCategoryActivity.MSG_DYNAMIC_LIST: //动态
			getData();
			
			break;

		case HomeCategoryActivity.MSG_SHIP_LIST:  //分类
			getTypes();
			break;
		}
		
	}
	private void loadDate() {
		loadMoreText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(page<totalPages){
					page = page+1;
					loadMoreButton.setVisibility(View.VISIBLE);
					loadMoreText.setText("加载中...");

					getAllData();

				}else{
					mListView.removeFooterView(loadMoreView);
				}

			}
		});	
	}
		
	
	private void getTypes() {

		final HashMap<String, Object>  params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				loadMoreView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0, "/mobile/home/sortList/show", params);
			
				if (cd.getReturnCode().equals("Success")) {
					
					Map<String, Object> var = (HashMap<String, Object>)cd.getContent();
					Double	num=  (Double) var.get("pageNo");
					totalPages = (Double.valueOf(num)).intValue();
					List<Map<String, Object>> shipList =(ArrayList<Map<String, Object>>)var.get("myShipDatas");
					for(Map<String, Object> mm : shipList){
						Map<String, Object> maps = new HashMap<String, Object>();
						//ShipDynamicData  dyt = new ShipDynamicData(mm);
						       ShipData shipDatas = new ShipData(mm);
						     List<ShipStopData> arvLftList = shipDatas.getShipArvlftDatas();
						     for(ShipStopData arvLft : arvLftList){
						    	 maps.put("port","动态:"+ arvLft.getArvlftDesc());
						
						     }					     

						     maps.put("name", shipDatas.getShipName());
						     maps.put("shipImg", shipDatas.getShipLogo());
						     maps.put("mmsi", "MMSI:"+shipDatas.getMmsi());
						     maps.put("reportType", "总吨:"+shipDatas.getSumTons()+"吨");
						     String trueName=shipDatas.getMaster().getTrueName();
						     if(trueName.equals("")){
						    	 
						    	 maps.put("shipper", "承运人:"+shipDatas.getMaster().getLoginName());
						     }else
						    	 maps.put("shipper", "承运人:"+trueName);
						 	if (shipDatas.getReleaseTime().equals("")) {
								maps.put("endtime", "发布日期:无"); // 发布日期
							} else
								maps.put("endtime",
										"发布日期:" + shipDatas.getReleaseTime()); // 发布日期
					         maps.put("shipType", "类别:"+shipDatas.getTypeData().getTypeName());

					         maps.put("id", shipDatas.getId().toString());
						     maps.put("shipName", shipDatas.getShipName()+"("+shipDatas.getMmsi()+")");
						     
						     maps.put("phone", shipDatas.getMaster().getMobile());
						     maps.put("chatName", shipDatas.getMaster().getNickName());
						mInfos.add(maps);
						adapter.notifyDataSetChanged();
					}
				}

			}
			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				mListView.removeFooterView(loadMoreView);
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
	
		
		
		String paramSplit2[]=param.split("_");
		params.put("attrValueCode", paramSplit2[1]);
		params.put("bigAreaCode", Long.parseLong(paramSplit2[0]));
		params.put("arvlft", ArvlftCode.arrive.toString());
		params.put("pageNo", page);
		data.getApiResult(handler, "/mobile/home/sortList/show",params,"get");
		
	}

	

	private ArrayList<Map<String, Object>> getData() {
		
		final HashMap<String, Object>  params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				loadMoreView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0, "/mobile/home/shipDynamic", params);
			
				if (cd.getReturnCode().equals("Success")) {
					
					Map<String, Object> var = (HashMap<String, Object>)cd.getContent();
					Double	num=  (Double) var.get("pageNo");
					totalPages = (Double.valueOf(num)).intValue();
					List<Map<String, Object>> shipList =(ArrayList<Map<String, Object>>)var.get("dynamicList");
					for(Map<String, Object> mm : shipList){
						Map<String, Object> maps = new HashMap<String, Object>();
						//ShipDynamicData  dyt = new ShipDynamicData(mm);
						       ShipData shipDatas = new ShipData(mm);
						     List<ShipStopData> arvLftList = shipDatas.getShipArvlftDatas();
						     for(ShipStopData arvLft : arvLftList){
						    	 maps.put("port","动态:"+ arvLft.getArvlftDesc());
						
						     }					     

						     maps.put("name", shipDatas.getShipName());
						     maps.put("shipImg", shipDatas.getShipLogo());
						     maps.put("mmsi", "MMSI:"+shipDatas.getMmsi());
						     maps.put("reportType", "总吨:"+shipDatas.getSumTons()+"吨");
						     String trueName=shipDatas.getMaster().getTrueName();
						     if(trueName.equals("")){
						    	 
						    	 maps.put("shipper", "承运人:"+shipDatas.getMaster().getLoginName());
						     }else
						    	 maps.put("shipper", "承运人:"+trueName);
						 	if (shipDatas.getReleaseTime().equals("")) {
								maps.put("endtime", "发布日期:无"); // 发布日期
							} else
								maps.put("endtime",
										"发布日期:" + shipDatas.getReleaseTime()); // 发布日期
					         maps.put("shipType", "类别:"+shipDatas.getTypeData().getTypeName());

					         maps.put("id", shipDatas.getId().toString());
						     maps.put("shipName", shipDatas.getShipName()+"("+shipDatas.getMmsi()+")");
						     
						     maps.put("phone", shipDatas.getMaster().getMobile());
						     maps.put("chatName", shipDatas.getMaster().getNickName());
						mInfos.add(maps);
						adapter.notifyDataSetChanged();
					}
				}

			}
			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				mListView.removeFooterView(loadMoreView);
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
	
			String[] paramSplit=param.split("_");
			params.put("cityPortNo", paramSplit[1]);
			params.put("pageNo", page);
			params.put("arvlft", ArvlftCode.arrive.toString());
			data.getApiResult(handler, "/mobile/home/shipDynamic",params,"get");
			
	
		return mInfos;

	}


	class FragmentAdapter extends BaseAdapter{
		private Context ac;
		private LayoutInflater listContainer;
		private ArrayList<Map<String, Object>> listItems;
		private ImageLoader mImageLoader;
		private lvHolder holder;

		public FragmentAdapter(Context activity,
				ArrayList<Map<String, Object>> data) {
			this.ac=activity;
			listContainer = LayoutInflater.from(ac);   //创建视图容器并设置上下文    
			this.listItems = data;
			mImageLoader = ImageLoader.getInstance();
		   
		}
		
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return  listItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {   
				convertView = listContainer.inflate(R.layout.activity_home_item, null);
				holder = new lvHolder();
				holder.logo =(ImageView)convertView.findViewById(R.id.shipLogo); //Logo
				holder.shiName=(TextView)convertView.findViewById(R.id.shipName);      //船名
				holder.mmsi=(TextView)convertView.findViewById(R.id.mmsi);             //MMSI
				holder.sailLine =(TextView)convertView.findViewById(R.id.sailLine); //地区
				holder.times =(TextView)convertView.findViewById(R.id.time);      //时间
				holder.types =(TextView)convertView.findViewById(R.id.shipType);      //时间
				holder.port =(TextView)convertView.findViewById(R.id.homePort);        //动态
				holder.shipper=(TextView)convertView.findViewById(R.id.cyr); //承运人
				convertView.setTag(holder); 

			}else {    
				holder = (lvHolder)convertView.getTag();

			}
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +(String)listItems.get(position).get("shipImg"), holder.logo,MyshipAdapter.displayImageOptions);
			holder.shiName.setText((String) listItems.get(position).get("shipName")); 
			holder.mmsi.setText((String) listItems.get(position).get("mmsi")); 
			holder.sailLine.setText((String) listItems.get(position).get("reportType"));
			
			holder.times.setText((String) listItems.get(position).get("time"));
			holder.types .setText((String) listItems.get(position).get("shipType"));
			holder.shipper.setText((String) listItems.get(position).get("shipper"));
			holder.port.setText((String) listItems.get(position).get("port")); 

			return convertView;
		}
	
		private class lvHolder{
			ImageView logo;
			TextView shiName ,times,shipper,types,mmsi,port,sailLine;
		}

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> map = (HashMap<String, Object>)mListView.getItemAtPosition(position);

		      Intent intent = new Intent(mContext,ShipPreviewActivity.class);
		      intent.putExtra("id", map.get("id").toString());
		      intent.putExtra("name", map.get("name").toString());
		      intent.putExtra("phone", map.get("phone").toString());
		      intent.putExtra("chatName", map.get("chatName").toString());
		      mContext.startActivity(intent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
		
			if(page<totalPages){
			loadMoreText.setText("查看更多");
			}else
				mListView.removeFooterView(loadMoreView);

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		Log.v("visibleLastIndex", this.visibleLastIndex+"");
	}

}