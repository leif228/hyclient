package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.EydSearchListAdapter;
import com.eyunda.third.adapters.order.EydOrderListAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.SearchRlsCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.ShipPriceData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.AreaSelect;
import com.eyunda.tools.ListViewForScrollView;
import com.eyunda.tools.log.FilePathGenerator;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 搜索结果
 * 
 * @author guoqiang
 *
 */
public class AdvShipSearchActivity extends CommonListActivity implements
		OnClickListener,OnItemClickListener {
	Data_loader dataLoader;
	
	private View layout;//
	protected ListViewForScrollView listView; 
	private BaseAdapter adapter;
	private ArrayList<Map<String, Object>> dataList;

	EditText startPort;// 起始点
	//TextView endPort;// 终止点
	ImageView search_btn ;// 搜索按钮
	Spinner spCate;
	LinearLayout tabBtns;
		
	
	String keyWord="";
	String channel="";
	String area="";
	private EydSearchListAdapter smpAdapter;
	List<SpinnerItem> areaItems;
	ArrayAdapter<SpinnerItem> spinnerAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_adv_search);
		dataLoader = new Data_loader();
		//接收两个搜索值
		Intent intent=getIntent();
		keyWord = intent.getStringExtra("key");
		channel = intent.getStringExtra("c");
		area = intent.getStringExtra("area");
		ScrollView searchTabScroll = (ScrollView)findViewById(R.id.searchTabScroll);
		startPort = (EditText) findViewById(R.id.startPort);// 起始点
		search_btn = (ImageView)findViewById(R.id.search_btn);// 搜索按钮
		tabBtns = (LinearLayout)findViewById(R.id.tabBtns);// 隐藏tab
		tabBtns.setVisibility(View.GONE);
		//view视图.滚轮的背景视图
		final RelativeLayout homeLayout = (RelativeLayout)findViewById(R.id.homePage);

		spCate = (Spinner)findViewById(R.id.spCate);
		areaItems = new ArrayList<SpinnerItem>();
		spinnerAdapter = new ArrayAdapter<SpinnerItem>(this,android.R.layout.simple_spinner_dropdown_item,areaItems);
		spCate.setAdapter(spinnerAdapter);
		loadAreaAndSetSpinnerAdapter();
		initAreaSeleted();
		if(!keyWord.equals("")){
			startPort.setText(keyWord);
		}
		
		search_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String key = startPort.getText().toString();
				if(key.trim().equals("")){
					Toast.makeText(AdvShipSearchActivity.this, "请输入搜索词", Toast.LENGTH_SHORT).show();
				}else{
					SpinnerItem si = (SpinnerItem)spCate.getSelectedItem();
					Intent intent = null;
					if(si.getId().equalsIgnoreCase(SearchRlsCode.shipsearch.name())){
						intent = new Intent(AdvShipSearchActivity.this,AdvShipSearchActivity.class);
					}else{
						intent = new Intent(AdvShipSearchActivity.this,AdvCargoSearchActivity.class);
					}
					intent.putExtra("key", key.trim());
					intent.putExtra("c", si.getId());
					intent.putExtra("area", ((SpinnerItem)spCate.getSelectedItem()).getId());
					startActivity(intent);
					finish();
				}
			}
		});
		
		setAdapter();
		loadDate();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("搜索结果");
	}

	private void loadAreaAndSetSpinnerAdapter() {
		areaItems.add(new SpinnerItem("", "所在区域..."));
		for (PortCityCode e : PortCityCode.values()) {
			SpinnerItem sp = new SpinnerItem(e.name(), e.getFullName());
			areaItems.add(sp);
		}
		spinnerAdapter.notifyDataSetChanged();
	}
	private void initAreaSeleted(){
		int size = areaItems.size();
		if(size > 0){
			for(int i=0; i<size; i++){
				if(area.equals(areaItems.get(i).getId())){
					spCate.setSelection(i);
				}
			}
		}else{
			spCate.setSelection(0);
		}
	}
	@Override
	protected synchronized void loadDate() {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据查找中，请稍候...");
			}
			@Override
			public void onSuccess(String arg0) {
				
				ConvertData cd= new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Log.setLog2FileEnabled(true);
					Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda","log"));
					Log.d(arg0);
					HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
//					HashMap<String, Object> var2 = (HashMap<String, Object>)var.get("orderData");
					List<Map<String, Object>> shipDatas =(ArrayList<Map<String, Object>>)var.get("shipDatas");
//					//取出货物列表
					int size = shipDatas.size();
					//int size = 20;
					if(size > 0){
						for(int i=0; i<size; i++){
							ShipData sd = new ShipData(shipDatas.get(i));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("boat_logo", sd.getShipLogo());
							map.put("boat_id", sd.getId()+"");
							map.put("boat_name", sd.getShipName());
							map.put("boat_dis", "类别:"+sd.getTypeData().getTypeName());
							map.put("boat_hangxian","总吨:"+ sd.getSumTons()+"吨");//港口			
							map.put("boat_ton", "MMSI:"+sd.getMmsi()); //MMsi
							map.put("shipData", sd); //shipData
							String name=sd.getMaster().getTrueName();
							if(name.equals("")){
								map.put("boat_time", "承运人:"+sd.getMaster().getNickName());//承运人	
							}else
								map.put("boat_time", "承运人:"+name);//承运人			
							if(sd.getReleaseTime().equals("")){
								map.put("endtime", "发布日期:无"); //发布日期
							}else
								map.put("endtime", "发布日期:"+sd.getReleaseTime()); //发布日期	
						
							map.put("btnLeft", true);
							   List<ShipStopData> arvLftList = sd.getShipArvlftDatas();
							     for(ShipStopData arvLft : arvLftList){
							    	 map.put("boat_dt","动态:"+ arvLft.getArvlftDesc());
							
							     }		
							dataList.add(map);
						}
						smpAdapter.notifyDataSetChanged();
						dialog.dismiss();
					}else{
						dialog.dismiss();
						Toast.makeText(AdvShipSearchActivity.this, "没有查到结果", Toast.LENGTH_LONG).show();
					}
					
				}
			}
		};
		Map<String,Object> apiParams = new HashMap<String, Object>();
		apiParams.put("keyWords", keyWord);
		apiParams.put("searchRlsCode", channel);
		apiParams.put("area", area);
		dataLoader.getApiResult(handler , "/mobile/home/search",apiParams,"get");
	}


	@Override
	protected BaseAdapter setAdapter() {
		dataList = new ArrayList<Map<String, Object>>();
		smpAdapter = new EydSearchListAdapter(AdvShipSearchActivity.this, dataList);
		listView =  (ListViewForScrollView)findViewById(R.id.listview);
		listView.setAdapter(smpAdapter);
		listView.setOnItemClickListener(this);
		return smpAdapter;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent intent = new Intent(AdvShipSearchActivity.this,ShipPreviewActivity.class);
		intent.putExtra("id", dataList.get(position).get("boat_id").toString());
		intent.putExtra("name",dataList.get(position).get("boat_name").toString());
		startActivity(intent);
	}

}
