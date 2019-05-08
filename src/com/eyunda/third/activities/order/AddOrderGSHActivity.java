package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.adapters.order.OrderGshListAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 新增合同-新增干散货
 * 
 * @author guoqiang
 *
 */
public class AddOrderGSHActivity extends AddOrderActivity implements
		OnClickListener{
	
	// 返回码-->resultCode
	public static int RESULT_ERROR = 0; // 保存失败
	public static int RESULT_SUCCESS = 1; // 保存成功，返回
	public static int RESULT_BACK = 2; // 直接返回，未保存
	
	public static final int MSG_Del_One_Item = 2; //删除一个Item
	public static final int MSG_Edit_One_Item = 4; //修改一个Item
	
	private int curPosition;//保存当前选择的item值，供删除时使用
	private String curOid;//保存当前选择的item的Id，供删除时使用
	
	private Button btnAddOneGSH,btnAddNewGSH;//新增干散货按钮
	private OrderGshListAdapter ogAdapter;
	private ListView lv;
	
	private Button btnJZX;//顶部按钮
	
	private Data_loader dataLoader;
	
	private List<Map<String, Object>> lists;
	
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			curPosition = msg.arg1;//要删除的记录
			curOid = (String)msg.obj;
			switch (msg.what) {
				case MSG_Del_One_Item: {//删除一个Item

					DialogUtil dialogAlert = new DialogUtil(AddOrderGSHActivity.this);
					dialogAlert.showDialogFromConfig("提示", "确认要删除该条记录么?", new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							//异步后台删除item
							Map<String,Object> apiParams = new HashMap<String, Object>();
							//apiParams.put("id", Long.parseLong(orderId));
							apiParams.put("id", Long.parseLong(curOid));
							apiParams.put("_method", "delete");
							
							dataLoader.getApiResult( 
								new AsyncHttpResponseHandler(){
									@Override
									public void onStart(){
										dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
									}
									@Override
									public void onSuccess(String arg0) {
										ConvertData cd= new ConvertData(arg0);
										dialog.dismiss();
										if (cd.getReturnCode().equals("Success")) {
											
											ogAdapter.removeItem(curPosition);
											ogAdapter.notifyDataSetChanged();
										}else{
											Toast.makeText(AddOrderGSHActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
										}
									}
									@Override
									public void onFailure(Throwable arg0, String content) {
										super.onFailure(arg0, content);
										Toast.makeText(AddOrderGSHActivity.this, content,
												Toast.LENGTH_SHORT).show();
									}
								}
								, "/mobile/order/myOrder/removeOrderCargo",apiParams);
						}
					});
					break;
				}
				case MSG_Edit_One_Item: {//修改一个Ite
					Intent intent = new Intent(AddOrderGSHActivity.this, AddOrderOneGSHActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("cargoId", curOid);
					bundle.putLong("orderId", orderId);
					bundle.putInt("request", 2);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				}
			}

		}
	};
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_gsh_list);
		dataLoader = new Data_loader();
		Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
		orderId=(Long)bundle.getLong("orderId");
		lists = new ArrayList<Map<String,Object>>();
		
		btnJZX = (Button) findViewById(R.id.btnJZX);
		btnJZX.setOnClickListener(this);//点击集装箱
		btnAddOneGSH = (Button) findViewById(R.id.btnAddGSH);
		btnAddNewGSH = (Button) findViewById(R.id.btnAddNewGSH);
		btnAddNewGSH.setOnClickListener(this);
		btnAddOneGSH.setOnClickListener(this);//新增一条记录
		dialogUtil = new DialogUtil(this);
		
		String[] from = new String[]{
				"orderId",
				"oId",
				"goodsCate",
				"goodsName",
				"goodsWeight",
				"goodsPrice",
				"goodsFee",
				"goodsRemark",
				"btnEdit",
				"btnDel"
		};
		int[] to = new int[]{
				R.id.orderId,
				R.id.oId,
				R.id.goodsCate,
				R.id.goodsName,
				R.id.goodsWeight,
				R.id.goodsPrice,
				R.id.goodsFee,
				R.id.goodsRemark,
				R.id.btnEdit,
				R.id.btnDel
		};
		
		lv =  (ListView)findViewById(R.id.listview);
		ogAdapter = new OrderGshListAdapter(AddOrderGSHActivity.this, mHandler,lists, R.layout.eyd_order_gsh_item, from, to);
		lv.setAdapter(ogAdapter);
		loadDate();

		
	}



	@Override
	protected void onStart() {
		super.onStart();
		setTitle("编辑合同-货物列表");
		btnAddGSH.setOnClickListener(null);
		btnAddGSH.setBackgroundColor(0xFF6db7ff);
	}

	@Override
	protected synchronized void loadDate() {
		final Map<String,Object> apiParams = new HashMap<String, Object>();
		apiParams.put("orderId", orderId);
		//获取数据
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				lists.clear();
				ConvertData cd= new ConvertData(arg0,"/mobile/order/myOrder/edit",apiParams);
				if (cd.getReturnCode().equals("Success")) {
	
					HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
					HashMap<String, Object> var2 = (HashMap<String, Object>)var.get("orderData");
					int hasCargo = ((Double)var.get("hasCargos")).intValue();
					List<Map<String, Object>> orderCargoDatas =(ArrayList<Map<String, Object>>)var2.get("orderCargoDatas");
					if(hasCargo == 0){
						//结果为0
						btnAddOneGSH.setClickable(false);
						btnAddOneGSH.setBackgroundColor(Color.parseColor("#8B8B8B"));
					}
					//取出货物列表
					int size = orderCargoDatas.size();
					if(size > 0){
						for(int i=0; i<size; i++){
							CargoData ocd = new CargoData(orderCargoDatas.get(i));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("orderId",orderId);
							map.put("oId",ocd.getId()+"");
							
							if(ocd.getCargoType().getCargoBigType().equals(CargoBigTypeCode.container)){
								map.put("goodsCate","货类："+ocd.getCargoType().getDescription());
								map.put("goodsName","规格："+ocd.getCargoName());
								map.put("goodsWeight","货量(箱)："+ocd.getTonTeu().intValue());
								map.put("goodsPrice","运价(元/箱)："+ocd.getPrice());
							}else{
								map.put("goodsCate","货类："+ocd.getCargoType().getDescription());
								map.put("goodsName","货名："+ocd.getCargoName());
								map.put("goodsWeight","货重(吨)："+ocd.getTonTeu().intValue());
								map.put("goodsPrice","运价(元/吨)："+ocd.getPrice());
							}
							map.put("goodsFee","运费(元)："+ocd.getTransFee());
							map.put("goodsRemark","备注："+ocd.getRemark());
							map.put("btnEdit",true);
							map.put("btnDel",true);
							lists.add(map);
						}
						ogAdapter.notifyDataSetChanged();
					}else{

					}
					dialog.dismiss();
				}
			}
		};
		
		dataLoader.getApiResult(handler , "/mobile/order/myOrder/edit",apiParams,"get");
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		switch(v.getId()){
		case (R.id.btnAddGSH):
			intent = new Intent(AddOrderGSHActivity.this,
					OrderCargoListActivity.class);
			bundle = new Bundle();
			bundle.putLong("orderId", orderId);//合同ID
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case(R.id.btnAddNewGSH)://
			intent = new Intent(AddOrderGSHActivity.this,AddOrderOneGSHActivity.class);
			bundle = new Bundle();
			bundle.putString("cargoId", "0");
			bundle.putLong("orderId", orderId);
			bundle.putInt("request", 3);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}

	}
	@Override
	protected void onRestart() {
		super.onRestart();
		loadDate();
	}

}
