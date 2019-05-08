package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.adapters.ship.DynamicAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.ShipUpdownData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 新增或修改动态页
 * @author LiGuang
 *
 */
public class ShipDtListActivity extends CommonActivity implements OnItemClickListener,
OnItemLongClickListener,OnClickListener,OnScrollListener{

	private ListView mListView;
	private Data_loader data;
	private int nSelPos = -1;
	private Button btnAdd;
	private Button btnModify;
	private SimpleAdapter adapter;
	private View lastSelView = null; // 记录上一次选中的view对象
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	private int visibleItemCount;
	protected int totalPages;
	private ArrayList<Map<String, Object>> dataList= new ArrayList<Map<String,Object>>();
	ArrayList<Map<String,Object>>  mList = new ArrayList<Map<String,Object>>();

	public final static int MSG_DelDynamicInList = 1; //删除列表中的动态
	public final static int MSG_AddOneDynamic = 2; 
	public final static int MSG_AddOrderData = 3; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_ship_dtlist);
		setViews();

	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶到离列表");
		//承运人名等于当前用户名，则他是承运人
		//		UserData  currentUser =GlobalApplication.getInstance().getUserData();
		//		 if(currentUser!=null &&currentUser.getTrueName().equals(DynamicAdapter.carrierName)==false){
		//        	 btnModify.setVisibility(View.GONE);
		//        	 return;
		//		 }
		
		setRight(R.drawable.zx_faver_top,new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Message msg = new Message();
				msg.obj = 0;
				msg.what = MSG_AddOneDynamic;
				mHandler.sendMessage(msg);
			}
		});

	}

	private void setViews() {
		data = new Data_loader();
		mListView =(ListView)findViewById(R.id.dt_list);
		btnAdd =(Button)findViewById(R.id.btnAdd);
		btnModify =(Button)findViewById(R.id.btnModify);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		getData(page);
		adapter = new SimpleAdapter(
				this, 
				dataList,
				R.layout.eyd_dt_item,
				new String[] { "desc","xiezhuang","goods","cargoNames","number","weight","remark" },
				new int[] {R.id.ship_dt_time, R.id.ship_dt_cargo, R.id.dt_cargoType,R.id.dt_cargoName, R.id.dt_Num,R.id.dt_weight, R.id.ship_dt_bz});

		mListView.setAdapter(adapter);
		mListView.setFooterDividersEnabled(false);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		btnAdd.setOnClickListener(this);
		btnModify.setOnClickListener(this);
		mListView.addFooterView(loadMoreView);    //设置列表底部视图
		mListView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		loadDate();
		
	}
	
	private void loadDate() {

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
					mListView.removeFooterView(loadMoreView);
				}

			}
		});	

	}

	private ArrayList<Map<String,Object>> getData(int currentPage) { 
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
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
				Gson gson = new Gson();
				HashMap<String, Object> result= gson.fromJson((String) arg0,
						new TypeToken<Map<String, Object>>() {}.getType());
				if (result.get("returnCode").equals("Success")) {
					if(result.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
						boolean  contentMd5Changed =(Boolean)result.get(ApplicationConstants.CONTENTMD5CHANGED);
						SharedPreferencesUtils sp = new SharedPreferencesUtils("/mobile/state/shipApply/shipArvlft", params);
				        if(contentMd5Changed && NetworkUtils.isNetworkAvailable() ){
				        	sp.setParam(arg0);
				        }else{
							String localJson =sp.getParam();
							result = gson.fromJson(localJson, new TypeToken<Map<String, Object>>() {}.getType());
					}
					}
					HashMap<String, Object> content = (HashMap<String, Object>)result.get("content");
					Map<String, Object> maps =  (Map<String, Object>) content.get("pageData");
					Double  pa =(Double) content.get("totalPages");
					totalPages = (Double.valueOf(pa)).intValue();
					Map<String, Object> mapData =  (Map<String, Object>) content.get("shipData");
					ShipData  shipData = new ShipData(mapData);
					List<ShipStopData>  dt = (List<ShipStopData>) maps.get("result");

					for(int i=0;i<dt.size();i++){
						Map<String,Object> map = new HashMap<String, Object>();
						Map<String, Object> dtm = (Map<String, Object>) dt.get(i);
						ShipStopData avf = new ShipStopData(dtm);
						map.put("desc", avf.getArvlftDesc());
						map.put("remark", avf.getRemark());
						map.put("id", avf.getId());
						map.put("status", avf.getArvlft().getDescription());//到离状态
						map.put("startDate", avf.getArvlftTime().split(" ")[0]);  //日期
						map.put("startTime", avf.getArvlftTime().split(" ")[1]);  //时间
						map.put("port", avf.getPortData().getFullName()); // 到离港口						
						map.put("goPort", avf.getGoPortData().getFullName()); //将去港口全名						
						map.put("portNo", avf.getPortNo()); 					
						map.put("goPortNo", avf.getGoPortNo()); 				
						map.put("PortName", avf.getPortData().getPortName()); 				
						if(avf.getArvlft().getDescription().equals("离"))
							map.put("xiezhuang", "装货");
						else
							map.put("xiezhuang", "卸货");
						ArrayList<String> cargoList = new ArrayList<String>(); //显示的货类
						ArrayList<String> tonDesList = new ArrayList<String>();//显示的数量 
						ArrayList<String> cargoNameList = new ArrayList<String>();//显示的货名 
						ArrayList<String> wrapList = new ArrayList<String>();//显示的包装
						ArrayList<String> tonList = new ArrayList<String>();						
						ArrayList<String> teuList = new ArrayList<String>();						
						ArrayList<String> unitList = new ArrayList<String>();//显示的单重 
						ArrayList<String> totalList = new ArrayList<String>();//显示的总重 
						ArrayList<String> lengthList = new ArrayList<String>();//显示的长 
						ArrayList<String> weidthList = new ArrayList<String>();//显示的宽 
						ArrayList<String> heightList = new ArrayList<String>();//显示高 
						ArrayList<String> volumeList = new ArrayList<String>();//显示的体积
						for(ShipUpdownData updown : avf.getShipUpdownDatas()){	

							cargoList.add(updown.getCargoType().getDescription());
							tonDesList.add(updown.getWrapCount()+updown.getWrapStyle().getDescription());
							cargoNameList.add(updown.getCargoName());
							wrapList.add(updown.getWrapStyle().getDescription());
							tonList.add(updown.getWrapCount().toString());
							unitList.add(updown.getUnitWeight().toString());
							totalList.add(updown.getFullWeight().toString());
							lengthList.add(updown.getCtlLength().toString());
							weidthList.add(updown.getCtlWidth().toString());
							heightList.add(updown.getCtlHeight().toString());
							volumeList.add(updown.getCtlVolume().toString());
							teuList.add(updown.getFullWeight().toString()+"吨");

							String cargos = cargoList.toString().trim().substring(1,cargoList.toString().length()-1).replace(", ", "\n");
							String cargoNames = cargoNameList.toString().trim().substring(1,cargoNameList.toString().length()-1).replace(", ", "\n");
							String counts = tonDesList.toString().trim().substring(1,tonDesList.toString().length()-1).replace(", ", "\n");
							String weights = teuList.toString().trim().substring(1,teuList.toString().length()-1).replace(", ", "\n");
                             

							map.put("goods", cargos);
							map.put("cargoNames", cargoNames);
							map.put("number", counts);
							map.put("weight", weights);
							map.put("avfId", updown.getArvlftId());
							map.put("unitList", unitList);
							map.put("totalList", totalList);
							map.put("lengthList", lengthList);
							map.put("widthList", weidthList);
							map.put("heightList", heightList);
							map.put("volumeList", volumeList);
							map.put("nums", tonList);

						}
						map.put("names", cargoNameList);
						map.put("volume", cargoList);
						map.put("numDes", tonDesList);
						map.put("wrapStyle", wrapList);
						dataList.add(map);

					}
					if(adapter!=null)
					adapter.notifyDataSetChanged();
				}

			}
			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				mListView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipDtListActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipDtListActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(ShipDtListActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipDtListActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();			

			}

		};
	
		params.put("pageNo", currentPage);
		params.put("mmsi", DynamicAdapter.mmsi);
		data.getApiResult(handler, "/mobile/state/shipApply/shipArvlft",params,"get");
		return dataList;

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (lastSelView != null)
			lastSelView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		// 设置选项背景色		
		view.setBackgroundColor(Color.parseColor("#dcdcdc"));			
		lastSelView = view;
		// 记录被选中的位置
		nSelPos = position;
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// 记录被选中的位置
		nSelPos = position;

		//弹出对话框事件
		AlerDialog(nSelPos);


		return true;
	}
	private void AlerDialog(int cuurentPos) {
		if(cuurentPos != (dataList.size() - 1)){
			Toast.makeText(this, "请从最后一项开始删除", Toast.LENGTH_SHORT).show();
			return;
		}
		if(dataList.size()!=0){
			AlertDialog.Builder bulider = new Builder(this);
			bulider.setTitle("刪除提示");
			bulider.setMessage("是否刪除该动态?");
			bulider.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Message msg = new Message();
					msg.what = MSG_DelDynamicInList;
					msg.obj = nSelPos;
					mHandler.sendMessage(msg);
				}
			});

			bulider.setNegativeButton("取消", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}		
			});
			bulider.create().show();
		}
	}
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_DelDynamicInList:
				int nPos = (Integer) msg.obj;
				Map<String,Object> map = dataList.get(nPos);
				deleteLast(nPos);
				
				break;
			case MSG_AddOneDynamic:
				int nPos2 = (Integer) msg.obj; //第一条
				Intent intent = new Intent(ShipDtListActivity.this,MyshipActivity.class);
				if(dataList.size()!=0){
					
							startActivity(intent);
							finish();
					//新增船舶
				
				}else{
					intent.putExtra("id", 0);
					startActivity(intent);
					finish();

				}
			}

		}
	};
	private int visibleLastIndex=0;
	private int page=1;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	protected void deleteLast(final int nPos) {
		AsyncHttpResponseHandler  handler = new AsyncHttpResponseHandler(){
			        @Override
			        public void onSuccess(String arg0) {
			        	super.onSuccess(arg0);
			        	ConvertData cd = new ConvertData(arg0);
			        	if(cd.getReturnCode().equals("Success")){
			        		dataList.remove(nPos);
			        		adapter.notifyDataSetChanged();
			        		Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
			        	}else{
			        		Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
			        	}
			        	
			        }
			        @Override
			        public void onFailure(Throwable arg0) {
			        super.onFailure(arg0);
			        Toast.makeText(getApplicationContext(),"删除失败", Toast.LENGTH_SHORT).show();
			        }
		};
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mmsi", DynamicAdapter.mmsi);
		data.getApiResult(handler, "/mobile/state/myShip/shipApply/delLastShipArvlft",map,"get");
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			AlerDialog(nSelPos);
			break;

		case R.id.btnModify:
			if (nSelPos == -1) {
				Toast.makeText(this, "请在列表中选中一项", Toast.LENGTH_SHORT).show();

				return;
			}
			Intent intent = new Intent(this,MyshipActivity.class);
			HashMap<String, Object> map = (HashMap<String, Object>) mListView.getItemAtPosition(nSelPos);
			intent.putExtra("info", map); // 数据
			intent.putExtra("id", Long.parseLong(map.get("id").toString()));
			// intent.putExtra("index", nSelPos); // 操作项下标
			startActivity(intent);
			finish();
			break;

		}

	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			//移动到最底部，文字变为查看更多
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
            Log.v("visibleItemCount", visibleItemCount+"");
	}


}
