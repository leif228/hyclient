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
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.adapters.home.RecentCargoAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.CargoVolumnCode;
import com.eyunda.third.domain.enumeric.CargoWeightCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class CargoResultActivity extends CommonActivity  implements OnItemClickListener,OnScrollListener{

	private ListView mListView;
	private RecentCargoAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	private int totalPages;
	private int page=1;
	private Data_loader data = new Data_loader();
	private String seletedTpye;
	private String seletedTon;
	private String seletedArea;
	private String seletedArea2;
	private TextView loadMoreText;
	private TextView loadingText;
	private ProgressBar loadMoreButton;
	private View loadMoreView;
	private int visibleLastIndex=0;
	private int visibleItemCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cargo_result);
		Intent intent = getIntent();
		seletedTpye = intent.getStringExtra("cargoType");
		seletedTon = intent.getStringExtra("teu");
		seletedArea = intent.getStringExtra("upPort");
		seletedArea2 = intent.getStringExtra("downPort");
		mListView=(ListView)findViewById(R.id.xList);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		mListView.addFooterView(loadMoreView);    //设置列表底部视图
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		mListView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		smpAdapter = new RecentCargoAdapter(this,dataList);
		mListView.setAdapter(smpAdapter);
		mListView.setOnItemClickListener(this);
		getData();
		loadDate();
	}
	private void loadDate() {
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				if(page<totalPages){
					loadMoreText.setText("加载中...");
					page++;
					getData();
				}else{
					mListView.removeFooterView(loadMoreView);
				}
			}
		});	

	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setTitle("查找结果");
	}
	private ArrayList<Map<String, Object>> getData() {
		final Map<String, Object> apiParams = new HashMap<String, Object>();
		final String jzx =CargoBigTypeCode.container.getDescription();
		AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler(){


			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据查找中，请稍候...");
				loadMoreView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/home/cargoHome",apiParams);
				if(cd.getReturnCode().equals("Success")){

					HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
					totalPages = ((Double) var.get("totalPage")).intValue();
					List<Map<String, Object>> cargoList =(ArrayList<Map<String, Object>>)var.get("cargoPageData");
					for(Map<String, Object> mm : cargoList){
						CargoData cargoData = new CargoData(mm);

						Map<String, Object> maps = new HashMap<String, Object>();

						maps.put("cargoImg", cargoData.getCargoImage());
						maps.put("id", cargoData.getId().toString());
						if(cargoData.getCargoType().getCargoBigType().getDescription().equals(jzx)){
							maps.put("cargoName", cargoData.getCargoType().getCargoBigType().getDescription()+"(货号:"+cargoData.getId().toString()+")");
						}else
							maps.put("cargoName", cargoData.getCargoName()+"(货号:"+cargoData.getId().toString()+")");

						maps.put("cargoCode","货号:"+ cargoData.getId().toString());
						String shipper=cargoData.getOwner().getTrueName();
						if(shipper.equals("")){
							maps.put("shipper", "托运人:"+cargoData.getOwner().getLoginName());
						}else
							maps.put("shipper", "托运人:"+shipper);	
						maps.put("volume", "货量/箱量："+cargoData.getTonTeuDes());
						maps.put("place", cargoData.getStartFullName()
								+" 到 "   +cargoData.getEndFullName());

						maps.put("unitPrice","单价："+ cargoData.getPriceDes());
//						maps.put("totalPrice","总价:"+ cargoData.getTransFeeDes());
						maps.put("endTime", "截止:"+cargoData.getPeriodTime());
						maps.put("type", cargoData.getCargoType().toString());

						maps.put("chatName", cargoData.getOwner().getNickName());
						maps.put("userId", cargoData.getOwner().getId().toString());  
						maps.put("phone", cargoData.getOwner().getMobile()); 

						if(cargoData.getCargoType().getDescription().contains(jzx)){
							maps.put("volume", "箱量:"+cargoData.getTonTeuDes());
							maps.put("totalPrice","箱量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						}else{
							maps.put("volume", "货量:"+cargoData.getTonTeuDes()); 	
							maps.put("totalPrice","货量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						}	
						dataList.add(maps);
					}
					smpAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(CargoResultActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();

				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				mListView.removeFooterView(loadMoreView);
				if (content != null && content.equals("socket time out")) {
					Toast.makeText(CargoResultActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				}else if(content!=null){
					Toast.makeText(CargoResultActivity.this, content,Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(CargoResultActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();	
				}
			}
		};
		apiParams.put("pageNo", page);
		if(!seletedTpye.equals("")){
			CargoTypeCode ctc = CargoTypeCode.valueOf(seletedTpye);
			apiParams.put("cargoType",ctc);
			if(!seletedTon.equals("")){
				if(ctc.equals(CargoTypeCode.container20e)){
					apiParams.put("cargoVolumn",CargoVolumnCode.valueOf(seletedTon));
				}else{
					apiParams.put("cargoWeight",CargoWeightCode.valueOf(seletedTon));
				}
			}
		}
		if(!seletedArea.equals("") ){
			apiParams.put("upPort", PortCityCode.valueOf(seletedArea));
		}
		if( !seletedArea2.equals("")){
			apiParams.put("downPort", PortCityCode.valueOf(seletedArea2));

		}
		data.getApiResult(showHandler,"/mobile/home/cargoHome", apiParams,"post");
		return dataList;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> map=(HashMap<String, Object>) parent.getItemAtPosition(position);
		Intent intent = new Intent(this,CargoPreviewActivity.class);
		intent.putExtra("id", map.get("id").toString());
		startActivity(intent);

	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount()-1;  //数据集最后一项的索引 
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
		Log.d("visibleLastIndex", this.visibleItemCount+"");
	}
}
