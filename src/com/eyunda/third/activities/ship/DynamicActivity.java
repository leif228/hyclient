package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.widget.AlertSearchDialog;
import com.eyunda.third.adapters.ship.DynamicAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.ship.ArvlftShipData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.ShipUpdownData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DynamicActivity extends CommonActivity implements OnItemClickListener,OnScrollListener{

	private ListView mlistView;
	private DynamicAdapter smpAdapter;
	ArrayList<Map<String, Object>>  dataList = new ArrayList<Map<String,Object>>();
	Data_loader data = new Data_loader();
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadMoreText;
	private TextView loadingText;
	private int visibleLastIndex=0;//最后的可视项索引 ;
	private int visibleItemCount;
	protected int totalPages;
	protected int page=1;
	private String searchName ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_ship_dynamic);
		initView();

	}
	private void initView() {

		mlistView =  (ListView)findViewById(R.id.mlistView);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		mlistView.addFooterView(loadMoreView);    //设置列表底部视图
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		mlistView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		smpAdapter = new DynamicAdapter(this, dataList);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
		getData();
		loadDate();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setTitle("动态上报");
		UserData user = GlobalApplication.getInstance().getUserData();
	     if(user!=null){

		setRight(R.drawable.ic_action_search, new OnClickListener() {

			@Override
			public void onClick(View v) {
				final AlertSearchDialog ad = new AlertSearchDialog(DynamicActivity.this);
				ad.showAddDialog(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					View view =   ad.getView();
					 EditText editTextName = (EditText) view.findViewById(R.id.et_search);
					 resetFooter();
					 searchName =editTextName.getText().toString();
						getData();
					
						
					}
				});
			}
		});
	     }
	}
	

	private void loadDate() {
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击查看更多，圈显示，文字显示加载更多，继续加载
				loadMoreButton.setVisibility(View.VISIBLE);

				if(page<totalPages){
					loadMoreText.setText("加载中...");
					page++;
					getData();
				}else{
					mlistView.removeFooterView(loadMoreView);
				}
			}
		});	

	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			//移动到最底部，文字变为查看更多
             if(page<totalPages){
				
				loadMoreText.setText("查看更多");
			}else
				//mlistView.removeFooterView(loadMoreView);
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
		final Map<String,Object> params = new HashMap<String, Object>();
        UserData user = GlobalApplication.getInstance().getUserData();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
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
				
				ConvertData cd = new ConvertData(arg0, "/mobile/state/myShip", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>)cd.getContent();
					List<Map<String,Object>> content = (List<Map<String, Object>>) ma.get("arvlf");
					 totalPages =((Double)ma.get("totalPages")).intValue();
					int size = content.size();
						for(int i=0;i<size;i++){
							ArvlftShipData avf  = new ArvlftShipData((Map<String, Object>) content.get(i));
							ShipData  shipData = avf.getShipData();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userId", shipData.getMasterId());
							map.put("shipId", shipData.getId().toString());
							map.put("shipLogo", shipData.getShipLogo());
							map.put("shipName", shipData.getShipName());//船名
							if(avf.getArriveData().getPortData()!=null){
							map.put("port","港口:"+ avf.getArriveData().getPortData().getFullName());
							}else
							map.put("port","港口:");	
							if(avf.getLeftData().getGoPortData()!=null){
							map.put("nextPort","下一港口:"+avf.getLeftData().getGoPortData().getFullName());
							}else
							map.put("nextPort","下一港口:");
							map.put("startTime", "到港时间:"+ avf.getArriveData().getArvlftTime());
							map.put("endTime", "离港时间:"+ avf.getLeftData().getArvlftTime());
							map.put("MMSI", shipData.getMmsi()); //MMsi
							List<ShipUpdownData> upList = avf.getLeftData().getShipUpdownDatas();
							List<ShipUpdownData> downList = avf.getArriveData().getShipUpdownDatas();
							if(upList!=null){
							map.put("up", upList.toString().substring(1,upList.toString().length()-1).replace(", ", "\n"));
							}else
								map.put("up","无");
							if(downList!=null){
							map.put("down", downList.toString().substring(1,downList.toString().length()-1).replace(", ", "\n"));
							}else
								map.put("down","无");
							map.put("remark", "备注:"+avf.getArriveData().getRemark());
							
							String name =shipData.getMaster().getTrueName();
							if(name.equals("")){
								map.put("carrier", "承运人:"+shipData.getMaster().getNickName());//船东
							}else
								map.put("carrier", "承运人:"+name);//船东
						
							dataList.add(map);
						}
					
					
					smpAdapter.notifyDataSetChanged();				
				}else{
					Toast.makeText(DynamicActivity.this, cd.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				dialog.dismiss();
				mlistView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(DynamicActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(DynamicActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(DynamicActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(DynamicActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();			

			}

		};

	
		params.put("keyWords", searchName );
		params.put("pageNo", page );
		data.getApiResult(handler , "/mobile/state/myShip",params,"get");

		return dataList;




	}
		

	




	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, Object> map = (HashMap<String, Object>)mlistView.getItemAtPosition(position);
		Intent intent=new Intent(this,ShipPreviewActivity.class);	
		intent.putExtra("id", map.get("shipId").toString());
		intent.putExtra("name",map.get("shipName").toString());   

		startActivity(intent);	
	}
	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			runOnUiThread(new Runnable() {
				public void run() {
					getData();
					smpAdapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void resetFooter() {
	
		loadMoreView.setVisibility(View.VISIBLE);
		loadMoreButton.setVisibility(View.VISIBLE);
		loadMoreText.setText("");
		dataList.clear();
		searchName="";
		page=1;
	}
}
