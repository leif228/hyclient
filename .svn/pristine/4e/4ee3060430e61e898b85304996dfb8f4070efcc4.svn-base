package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.activities.account.GeneralPayActivity;
import com.eyunda.third.activities.map.ShipLatestDynamicActivity;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.activities.user.SignatureActivity;
import com.eyunda.third.adapters.order.EydOrderListAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.UserUtil;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 我的合同
 * 
 * @author guoqiang
 *
 */
public class MyOrderActivity extends CommonActivity implements
OnClickListener, OnItemClickListener,OnScrollListener {
	Data_loader data;

	Button bar_but1, bar_but2, bar_but3, bar_but4, tdxss, zhss;
	protected ScrollView head1;
	private ListView listView;
	private EydOrderListAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;
	//消息列表
	public static final int MSG_Edit_Order =  1; //修改
	public static final int MSG_Sign_Order =  2; //签名
	public static final int MSG_Pay_Order_payment  =  3; //已支付，担保中
	public static final int MSG_Pay_Order_confirmpay  =  4; //确认付款
	public static final int MSG_Pay_Order_refunapply  =  5; //退款申请
	public static final int MSG_Pay_Order_refund  =  6; //退款

	public static final int MSG_Del_Order  =  11; //删除
	public static final int MSG_Comment_Order  =  12; //评论
	public static final int MSG_Monitor  =  18; //监控

	protected static final int MSG_IFSIMPLE = 0;
	protected int totalPages;
	protected String start= "";;
	protected String end= "";;
	private int page=1;
	private int curPosition;//当前选择的item
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	private int visibleLastIndex;
	private int visibleItemCount;
	protected List<Map<String,Object>> cagoList;
	protected String orderCargoId;


	AsyncHttpResponseHandler saveHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart(){
			dialog = dialogUtil.loading("通知", "数据保存，请稍候...");
		}
		@Override
		public void onSuccess(String arg0) {
			dialog.dismiss();
			ConvertData cd = new ConvertData(arg0);
			Toast.makeText(MyOrderActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();
			if(cd.getReturnCode().equalsIgnoreCase("success")){
				dataList.remove(curPosition);
				smpAdapter.notifyDataSetChanged();
			}
		}
	};

	//消息处理
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			curPosition = msg.arg1;//要删除的记录
			final Map<String, Object> res = (Map<String, Object>)msg.obj;
			DialogUtil dialogOne = new DialogUtil(MyOrderActivity.this);
			switch (msg.what) {
			case MSG_Del_Order: //删除一个Item
				dialogOne.showDialogFromConfig("提示", "确认要删除该条合同么?", new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						//异步后台删除item
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("_method","delete");
						params.put("id",Long.parseLong(res.get("orderId").toString()));
						data.getApiResult(saveHandler, "/mobile/order/myOrder/deleteOrder", params, "post");
					}
				});
				break;
			case MSG_Edit_Order:
				//Intent moreIntent = new Intent(MyOrderActivity.this,AddOrderCCActivity.class);
				Intent simpleIntent = new Intent(MyOrderActivity.this,SimpleOrderActivity.class);
				//moreIntent.putExtra("orderId", res.get("orderId").toString());//合同ID
				simpleIntent.putExtra("orderId", Long.parseLong(res.get("orderId").toString()));
				startActivity(simpleIntent);
				
				break;

			case MSG_Sign_Order:
				Intent intent2 = new Intent(MyOrderActivity.this,SignatureActivity.class);
				Bundle bundle2 = new Bundle();
				bundle2.putString("orderId", res.get("orderId").toString());//合同ID
				intent2.putExtras(bundle2);
				startActivity(intent2);
				break;
			case MSG_Pay_Order_payment:
//				startActivity(new Intent(MyOrderActivity.this, WebPayActivity.class)
//						.putExtra("url", ApplicationConstants.SERVER_URL+"/mobile/pinganpay/orderPay/?orderId="+res.get("orderId").toString()+"&feeItem="+FeeItemCode.prefee)
//						.putExtra("title", "合同支付"));
				startActivity(new Intent(MyOrderActivity.this, GeneralPayActivity.class).putExtra("title", "合同支付").putExtra("valueId", res.get("orderId").toString()).putExtra("value", "").putExtra("type", FeeItemCode.prefee.name()));
				
				finish();
				break;
			case MSG_Pay_Order_confirmpay:
			
				break;
			
			
			case MSG_Pay_Order_refund:

				break;	
			case MSG_Comment_Order://评论
				Intent intentComm = new Intent(MyOrderActivity.this,OrderCommentActivity.class);
				Bundle bundleComm = new Bundle();
				bundleComm.putString("orderId", res.get("orderId").toString());//合同ID
				intentComm .putExtras(bundleComm);
				startActivity(intentComm);
				break;
			case MSG_Monitor:
				Intent intentM = new Intent(MyOrderActivity.this,ShipLatestDynamicActivity.class);
				// 绑定传输的船舶数据
				intentM.putExtra("id", res.get("orderId").toString());
				intentM.putExtra("shipName","");
				intentM.putExtra("mmsi", "");
				intentM.putExtra("type", "2");//区别请求来源
				startActivity(intentM);
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_add_order);
		data = new Data_loader();
		this.listView = (ListView) findViewById(R.id.listview);

		dataList = new ArrayList<Map<String, Object>>();
		smpAdapter = (EydOrderListAdapter)setAdapter();
		listView.setAdapter(smpAdapter);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView);    //设置列表底部视图
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		loadData();
	}

	private void loadData() {
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				if(page<totalPages){
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
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {

			if(page<totalPages){
				loadMoreText.setText("查看更多");
			}else
				listView.removeFooterView(loadMoreView);
			//loadMoreView.setVisibility(View.GONE);
		}

	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		Log.d("visibleLastIndex", this.visibleItemCount+"");
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("我的合同");
		UserData currentUser= GlobalApplication.getInstance().getUserData();
		if(currentUser != null){
			setRight(R.drawable.ic_action_search, new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog();

				}
			});
			//只有代理人才能新增合同
			if(UserUtil.isRole(currentUser, UserRoleCode.handler)||UserUtil.isRole(currentUser, UserRoleCode.broker)){
				setRightBtn(R.drawable.zx_faver_top, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//新增合同
						startActivity(new Intent(getApplicationContext(),SimpleOrderActivity.class).putExtra("orderId", 0L));
					}
				});
			}

		}
		dataList.clear();
		loadDate();
		

	}
	protected void AlertDialog() {
		LayoutInflater  inflater = LayoutInflater.from(MyOrderActivity.this);
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
		new AlertDialog.Builder(MyOrderActivity.this)
		.setTitle("查找合同")
		.setView(view)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setNegativeButton("取消", null)
		.setPositiveButton("查找", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				start="";
				end ="";
				dataList.clear();
				start =tv_start.getText().toString().trim();
				end =tv_end.getText().toString().trim();
				loadDate();
			}
		}).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {


		HashMap map = (HashMap) listView.getItemAtPosition(position);

		Intent intent = new Intent(MyOrderActivity.this,
				com.eyunda.third.activities.order.OrderPreviewActivity.class);
		// 绑定传输的合同编号数据
		intent.putExtra("orderId", map.get("orderId").toString());
		intent.putExtra("orderNum", map.get("orderNum").toString());
		intent.putExtra("pdfFileName",map.get("pdfFileName").toString());
		intent.putExtra("btnEdit",(Boolean)map.get("btnEdit"));
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
	}

	protected BaseAdapter setAdapter() {
		return new EydOrderListAdapter(MyOrderActivity.this, mHandler,dataList);
	}

	protected void loadDate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		// 获取数据
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {


			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
				loadMoreView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(String arg0) {
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/order/myOrder", params);
				if(cd.getReturnCode().equalsIgnoreCase("Success")){
					Map r = (HashMap<String, String>)cd.getContent();
					totalPages =((Double)r.get("pageNo")).intValue();
					List content = (ArrayList<String>) r.get("orderDatas");
					cagoList = (List<Map<String, Object>>) r.get("cargos");
					int size = content.size();
					Log.i(size+"-----");
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							OrderData orderData = new OrderData((Map<String, Object>) content.get(i));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("boatLogo", orderData.getShipData().getShipLogo());
							map.put("orderId", orderData.getId().toString());
							map.put("cargoSize", "");
							map.put("orderNum", "合同号:"+ orderData.getId().toString());
							map.put("orderCount", "交易金额:"+ orderData.getTransFee().toString()+"元");
							map.put("orderTime", "交易日期:"+orderData.getCreateTime());
							map.put("shipId", orderData.getShipData().getId().toString());
							String hangxian = orderData.getStartPort().getFullName()+" 到 "+orderData.getEndPort().getFullName();
							map.put("hangXian", hangxian);
							if(orderData.getMaster().getTrueName().equals("")){
								if(orderData.getMaster().getNickName().equals("")){
									map.put("chengYunRen", "承运人:无");
								}else{
									map.put("chengYunRen", "承运人:"+ orderData.getMaster().getNickName());
								}
							}else{
								map.put("chengYunRen", "承运人:"+ orderData.getMaster().getTrueName());
							}
							if(orderData.getOwner().getTrueName().equals("")){
								map.put("tuoYunRen", "托运人:"+ orderData.getOwner().getNickName());
							}else{
								map.put("tuoYunRen", "托运人:"+ orderData.getOwner().getTrueName());
							}

							map.put("orderStatic", orderData.getStatus().getDescription());
							if(orderData.getShipData().getShipName().equals("")){
								map.put("shipName", "批次运输合同");
							}else{
								map.put("shipName", orderData.getShipData().getShipName());
							}
							map.put("pdfFileName",orderData.getPdfFileName());
							map.put("shipImage",orderData.getShipData().getShipLogo());
							map.put("btnEdit", false);//编辑
							map.put("btnDel", false);//删除
							map.put("btnSign", false);//签名
							map.put("btnSignText", "签名");
							map.put("btnPay", false);//支付
							map.put("btnRefund", false);//支付
							map.put("btnPayText", "支付");
							map.put("btnPayAction", "");
							Map<String,Boolean> ops = orderData.getOps();
							//order.ops.edit  
							//order.ops.startsign
							//order.ops.presign 
							//order.ops.endsign
							//order.ops.payment支付预付款
							//order.ops.confirmpay结算付款
							//order.ops.refundapply 已申请退款
							//order.ops.refund 退款,交易关闭
							//order.ops.approval评价

							//合同状态及用户身份控制按钮显示

							if(ops.get("edit")){
								map.put("btnEdit", true);//编辑
								map.put("btnDel", true);//删除
							}
							if(ops.get("startsign")){
								map.put("btnSign", true);//签名
								map.put("btnSignText", "承运人签字");
							}

							if(ops.get("endsign")){
								map.put("btnSign", true);//签名
								map.put("btnSignText", "托运人签字");
							}
							if(ops.get("payment")){
								map.put("btnPay", true);//支付
								map.put("btnPayText", "支付");
								map.put("btnPayAction", "payment");
							}
							if(ops.get("confirmpay")){
								map.put("btnPay", true);//支付
								map.put("btnPayText", "确认付款");
								map.put("btnPayAction", "confirmpay");
							}
							if(ops.get("refundapply")){
								map.put("btnRefund", true);//支付
								map.put("btnRefundText", "退款申请");
								map.put("btnRefundAction", "refundapply");
							}
							if(ops.get("refund")){
								map.put("btnRefund", true);//支付
								map.put("btnRefundText", "退款处理");
								map.put("btnRefundAction", "refund");
							}
							if(ops.get("approval")){
								map.put("btnPay", true);//支付
								map.put("btnPayText", "评价");
								map.put("btnPayAction", "approval");
								map.put("btnRefund", false);//支付
							}
							

							if((null !=ops.get("monitor")) && ops.get("monitor")){
								map.put("btnMonitor", true);//支付
							}else{
								map.put("btnMonitor", false);//支付
							}
							dataList.add(map);
						}
						smpAdapter.notifyDataSetChanged();
					}
				}else{
					Toast.makeText(MyOrderActivity.this, cd.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(MyOrderActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(MyOrderActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(MyOrderActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(MyOrderActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();			
			}

		};
		params.put("pageNo", page);
		params.put("startTime", start);
		params.put("endTime", end);
		data.getApiResult(handler, "/mobile/order/myOrder", params, "get");
	}
	@Override
	protected void onRestart(){ //数据改变后进行刷新
		super.onRestart();
//		Intent intent =getIntent();
//		boolean isRefresh = intent.getBooleanExtra("success", false);
//		if(isRefresh){
//			dataList.clear();
//			loadDate();
//		}
	}
}
