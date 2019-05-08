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
import android.view.LayoutInflater;
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
import com.eyunda.third.activities.pay.ShipDuesPaymentActivity;
import com.eyunda.third.activities.pay.ShipDuesRefundActivity;
import com.eyunda.third.activities.pay.ShipGasOrderPaymentActivity;
import com.eyunda.third.activities.ship.widget.AlertSearchShipsDialog;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.adapters.ship.ShipDuesAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.ship.MyShipDuesData;
import com.eyunda.third.domain.ship.ShipNameData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.pay.PayEntity;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipDuesActivity extends CommonListActivity implements
OnClickListener, OnItemClickListener,OnScrollListener{
	public static final int MSG_Del_One_Item = 1;
	public static final int MSG_REFUND = 2;
	public static final int MSG_PAY = 3;
	Data_loader data;

	private ListView listView;
	private ShipDuesAdapter smpAdapter;
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
				DialogUtil dialogAlert = new DialogUtil(ShipDuesActivity.this);
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
									Toast.makeText(ShipDuesActivity.this, cd.getMessage(), Toast.LENGTH_LONG).show();
							}
						};
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("id",id3);
						data.getApiResult(handler, "/mobile/ship/deleteDues", params, "get");	
						
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
					startActivity(new Intent(ShipDuesActivity.this, ShipDuesRefundActivity.class));
				} else {
					Toast.makeText(ShipDuesActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipDuesActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipDuesActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipDuesActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipDuesActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("id", id );
		data.getApiResult(handler, "/mobile/ship/myShip/duesRefund", params, "get");
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
					 startActivity(new Intent(ShipDuesActivity.this,ShipDuesPaymentActivity.class).putExtras(bundle));
				} else {
					Toast.makeText(ShipDuesActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipDuesActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipDuesActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipDuesActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipDuesActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("id", id2 );
		data.getApiResult(handler, "/mobile/ship/myShip/duesPay", params, "get");
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
		setTitle("缴费记录");
			setRight("缴费", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//AlertDialog();
					Bundle bundle = new Bundle();
					bundle.putString("shipId", shipId);//shipId
					bundle.putString("shipName", shipName);
					startActivity(new Intent(ShipDuesActivity.this, ShipDuesInActivity.class).putExtras(bundle));
				}
			});
		setRight(R.drawable.ic_action_search,new OnClickListener() {

			@Override
			public void onClick(View v) {
				final AlertSearchShipsDialog ad = new AlertSearchShipsDialog(ShipDuesActivity.this,shipNameDatas,shipName,"dues");
				ad.showAddDialog(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String selectShip = ad.getSelectShip();
						if(selectShip.equals("全部缴费船舶")){
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
	protected void AlertDialog() {
			LayoutInflater  inflater = LayoutInflater.from(ShipDuesActivity.this);
			View view = inflater.inflate(R.layout.search_cargo_item, null);
			final TextView tv_start = (TextView) view.findViewById(R.id.et_start);
			final TextView tv_end = (TextView) view.findViewById(R.id.et_end);
			DatePickerFragment datePicker = new DatePickerFragment(tv_start);  
			DatePickerFragment datePicker2 = new DatePickerFragment(tv_end);  
			tv_start.setText(datePicker.getLastMonth());
			tv_end.setText(datePicker2.getCurrentTime());
			tv_start.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DatePickerFragment datePicker = new DatePickerFragment(tv_start); 
					 datePicker.show(getFragmentManager(), "datePicker");  
					
				}
			});
			tv_end.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DatePickerFragment datePicker = new DatePickerFragment(tv_end); 
					 datePicker.show(getFragmentManager(), "datePicker2");
					
				}
			});
	}

	@Override
	protected BaseAdapter setAdapter() {
		String[] from = new String[] { "shipName","combo","money","startMonth","endMonth","createTime","refundBtn","btnPay"};
		int[] to = new int[] { 
				
				R.id.shipname,// 
				R.id.combo,// 
				R.id.money,// 
				R.id.startMonth,// 
				R.id.endMonth,// 
				R.id.createTime,
				R.id.refundBtn,
				R.id.btnPay
		};

		smpAdapter = new ShipDuesAdapter(ShipDuesActivity.this, mHandler,dataList, R.layout.eyd_ship_dues_item, from, to);
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
				ConvertData cd = new ConvertData(arg0,"/mobile/ship/myShip/dues", params);
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
							MyShipDuesData settleData = new MyShipDuesData((Map<String, Object>) content.get(i));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", settleData.getId());
							map.put("shipName", "船名:"+settleData.getShipName());
							map.put("combo", "套餐:"+settleData.getCombo().getDescription());
							map.put("money", "金额:"+settleData.getMoney()+"元");
							map.put("createTime", "缴费时间:"+settleData.getCreateTime());
							map.put("startMonth", "开始年月:"+settleData.getStartMonth());
							map.put("endMonth", "结束年月:"+settleData.getEndMonth());
							if(settleData.getRefundAction().equals("yes"))
								map.put("refundBtn", true);
							else
								map.put("refundBtn", false);
							if(settleData.getRefundAction().equals("no"))
								map.put("btnPay", true);
							else
								map.put("btnPay", false);
							dataList.add(map);
						}
					} else {
						// TODO 空记录处理
					}
					smpAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ShipDuesActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipDuesActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipDuesActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipDuesActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipDuesActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("pageNo", page );
		params.put("shipId", shipId );
		if(selectAllShip)
			params.put("shipId", 0 );
		data.getApiResult(handler, "/mobile/ship/myShip/dues", params, "get");
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


