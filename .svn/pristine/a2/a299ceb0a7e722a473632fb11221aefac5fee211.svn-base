package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.pay.ShipGasOrderPaymentActivity;
import com.eyunda.third.activities.pay.ShipGasOrderRefundActivity;
import com.eyunda.third.activities.ship.widget.AlertSearchShipsDialog;
import com.eyunda.third.adapters.ship.ShipGasOrderAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.enumeric.BuyOilStatusCode;
import com.eyunda.third.domain.ship.MyShipGasOrderData;
import com.eyunda.third.domain.ship.ShipNameData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.pay.PayEntity;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipGasOrderActivity extends CommonListActivity implements
OnClickListener, OnItemClickListener,OnScrollListener{
	public static final int MSG_Del_One_Item = 1;
	public static final int MSG_REFUND = 2;
	public static final int MSG_PAY = 3;
	Data_loader data;

	private ListView listView;
	private ShipGasOrderAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;
	private int visibleLastIndex=0;//最后的可视项索引 ;
	private int visibleItemCount;
	private int curPosition;// 当前选择的item
	private int page=1;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	protected int totalPages;
	Boolean isLoad =false;
	Boolean isFirst=true;
	Boolean selectAllShip=false;
	
	private String shipId;
	private String shipName;
	private List<ShipNameData> shipNameDatas;
	// 删除账务处理
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_REFUND: 
				Long id = (Long) msg.obj;
				loadRefund(id);
//				startActivity(new Intent(ShipDuesActivity.this, ShipDuesRefundActivity.class));
//				Toast.makeText(ShipDuesActivity.this, "退款shipDuesId="+id, Toast.LENGTH_LONG).show();
				break;
			case MSG_PAY: 
				Long id2 = (Long) msg.obj;
				loadPay(id2);
//				startActivity(new Intent(ShipDuesActivity.this, ShipDuesRefundActivity.class));
//				Toast.makeText(ShipDuesActivity.this, "退款shipDuesId="+id, Toast.LENGTH_LONG).show();
				break;
			case MSG_Del_One_Item: // 删除一个Item
				final Long id3 = (Long) msg.obj;
				curPosition = msg.arg1;// 要删除的记录
				DialogUtil dialogAlert = new DialogUtil(ShipGasOrderActivity.this);
				dialogAlert.showDialogFromConfig("提示", "确认要删除该条记录么?", new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						//异步后台删除item
						AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
							@Override
							public void onStart() {
								dialog = dialogUtil.loading("通知", "请稍候...");
							}

							@Override
							public void onSuccess(String arg0) {
								dialog.dismiss();
								ConvertData cd = new ConvertData(arg0);
								if (cd.getReturnCode().equals("Success")) {
									
									smpAdapter.removeItem(curPosition);
									smpAdapter.notifyDataSetChanged();
								}else
									Toast.makeText(ShipGasOrderActivity.this, cd.getMessage(), Toast.LENGTH_LONG).show();
							}
						};
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("id",id3);
						data.getApiResult(handler, "/mobile/ship/deleteGasOrder", params, "get");	
						
					}
				});
				break;
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_baby_list);
		data = new Data_loader();
		this.listView = (ListView) findViewById(R.id.myship_list);
		dataList = new ArrayList<Map<String, Object>>();
		shipNameDatas = new ArrayList<ShipNameData>();
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView);    //设置列表底部视图
		listView.setFooterDividersEnabled(false);
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		shipId=(String)bundle.getString("shipId");
		shipName=(String)bundle.getString("shipName");
		
		setAdapter();
		loada();
		
	}

	protected void loadPay(Long id2) {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> var = (HashMap<String, Object>) cd.getContent();
					PayEntity payData = new PayEntity(var);
					
					 Bundle bundle = new Bundle();
					 bundle.putString("shipId", shipId);
					 bundle.putString("shipName", shipName);
					 bundle.putSerializable("payData",payData);
					 startActivity(new Intent(ShipGasOrderActivity.this,ShipGasOrderPaymentActivity.class).putExtras(bundle));
				} else {
					Toast.makeText(ShipGasOrderActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipGasOrderActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipGasOrderActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipGasOrderActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipGasOrderActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("id", id2 );
		data.getApiResult(handler, "/mobile/ship/myShip/gasOrderPay", params, "get");
	}

	protected void loadRefund(Long id) {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}

			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					startActivity(new Intent(ShipGasOrderActivity.this, ShipGasOrderRefundActivity.class));
				} else {
					Toast.makeText(ShipGasOrderActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipGasOrderActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipGasOrderActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipGasOrderActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipGasOrderActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("id", id );
		data.getApiResult(handler, "/mobile/ship/myShip/gasOrderRefund", params, "get");
	}

	private void loada() {
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				isFirst=false;
				isLoad=true;
				if(page<=totalPages){
					loadMoreText.setText("加载中...");
					page++;
					loadDate();
				}else{
					listView.removeFooterView(loadMoreView);
				}
			}
		});	
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶加油记录");
//			setRight("缴费", new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					//AlertDialog();
//					Bundle bundle = new Bundle();
//					bundle.putString("shipId", shipId);//shipId
//					bundle.putString("shipName", shipName);
//					startActivity(new Intent(ShipGasOrderActivity.this, ShipDuesInActivity.class).putExtras(bundle));
//				}
//			});
		setRight(R.drawable.ic_action_search,new OnClickListener() {

			@Override
			public void onClick(View v) {
				final AlertSearchShipsDialog ad = new AlertSearchShipsDialog(ShipGasOrderActivity.this,shipNameDatas,shipName,"gasOrder");
				ad.showAddDialog(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String selectShip = ad.getSelectShip();
						if(selectShip.equals("全部加油船舶")){
							selectAllShip=true;
							shipName=null;
							page=1;
							dataList.clear();
							shipNameDatas.clear();
							loadDate();
						}else{
							selectAllShip=false;
							for (ShipNameData p : shipNameDatas) {
								if(p.getShipName().equals(selectShip)){
									page=1;
									shipId = p.getId().toString();
									shipName = p.getShipName();
									dataList.clear();
									shipNameDatas.clear();
									loadDate();
									break;
								}
							}
						}
						refrushBtn();
					}
				});
			}
		});
		page=1;
		dataList.clear();
		shipNameDatas.clear();
		loadDate();
	}
	private void refrushBtn(){
		if(selectAllShip){
			top_commit_text.setVisibility(View.GONE);
		}else{
			top_commit_text.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected BaseAdapter setAdapter() {

		smpAdapter = new ShipGasOrderAdapter(ShipGasOrderActivity.this, mHandler,dataList);
		listView.setAdapter(smpAdapter);
		return smpAdapter;
	}

	@Override
	protected void loadDate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0,"/mobile/ship/myShip/gasOrder", params);
				if (cd.getReturnCode().equals("Success")) {
					Map r = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> content = (ArrayList<Map<String, Object>>) r.get("duesDatas");
					List<Map<String, Object>> content1 = (ArrayList<Map<String, Object>>) r.get("ships");
					totalPages =((Double)r.get("totalPages")).intValue();
					if(page<totalPages)
						loadMoreView.setVisibility(View.VISIBLE);
					else	
						loadMoreView.setVisibility(View.GONE);
					
					for(Map<String, Object> m:content1){
						ShipNameData snd = new ShipNameData(m);
						shipNameDatas.add(snd);
					}
					
					int size = content.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							MyShipGasOrderData data = new MyShipGasOrderData((Map<String, Object>) content.get(i));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("wid", data.getId());
							map.put("id", "订单号:"+data.getId());
							map.put("logo", data.getWaresLogo());
//							map.put("shipName", "船名:"+data.getShipName());
							map.put("companyName", "卖家:"+data.getCompanyName());
							map.put("waresName", "商品:"+data.getWaresName());
							map.put("saleCount", "购买数量:"+data.getSaleCount());
							map.put("price", "交易价格(元):"+data.getPrice());
							map.put("tradeMoney", "交易金额(元):"+data.getTradeMoney());
							map.put("orderTime", "购买时间:"+data.getOrderTime());
//							map.put("stationName", "加油站名称:"+data.getStationName());
//							map.put("gasTime", "加油时间:"+data.getGasTime());
							map.put("status", data.getStatus().getDescription());
							if(data.getStatus()==BuyOilStatusCode.edit)
								map.put("btnPay", true);
							else
								map.put("btnPay", false);
							if(data.getStatus()==BuyOilStatusCode.payment)
								map.put("btnRefund", true);
							else
								map.put("btnRefund", false);
							dataList.add(map);
						}
					} else {
						// TODO 空记录处理
					}
					smpAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ShipGasOrderActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipGasOrderActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipGasOrderActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipGasOrderActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipGasOrderActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("pageNo", page );
		params.put("shipId", shipId );
		if(selectAllShip)
			params.put("shipId", 0 );
		data.getApiResult(handler, "/mobile/ship/myShip/gasOrder", params, "get");
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
				//listView.removeFooterView(loadMoreView);
				loadMoreView.setVisibility(View.GONE);


		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onClick(View v) {
	}
}


