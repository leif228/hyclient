package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.order.CCTextWatcher;
import com.eyunda.third.adapters.order.OrderAddTYRAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 查找运输工具--返回结果
 * @author liguang
 *
 */
public class FindShipActivity extends AddOrderActivity implements
OnClickListener{
	SynData_loader synDataLoader;
	Image_loader imgLoader;
	Data_loader dataLoader;
	private CCTextWatcher ccTextWatcher;
	private AutoCompleteTextView autoCompleteTextView;
	private Button saveBtn ;
	private RelativeLayout userInfo;
	private ShipData curSeclectShip=null;
	protected boolean isSimple=false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
		dataLoader = new Data_loader();
		imgLoader = new Image_loader(this, (GlobalApplication) getApplication());


		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		orderId=(Long)bundle.getLong("orderId");
		setContentView(R.layout.eyd_order_findship);

		saveBtn = (Button)findViewById(R.id.button_save);
		userInfo=(RelativeLayout)findViewById(R.id.userInfo);
		userInfo.setVisibility(View.INVISIBLE);
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteCC);
		OrderAddTYRAdapter autocompleteAdapter = new OrderAddTYRAdapter(this,R.layout.eyd_auto_user_item);
		ccTextWatcher = new CCTextWatcher(autoCompleteTextView, autocompleteAdapter);
		autoCompleteTextView.setAdapter(autocompleteAdapter);
		autoCompleteTextView.addTextChangedListener(ccTextWatcher);
		autoCompleteTextView.setThreshold(1);
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//				String info = ((OrderAddTYRAdapter) parent.getAdapter()).getItem(position);
				//				curSeclectShip = ccTextWatcher.getSelectedShip(info);
				//				showBoatInfo(curSeclectShip);
				CharSequence keyWords = (CharSequence) parent.getItemAtPosition(position);
				findShipByKeyWords(keyWords);
			}
		});
		saveBtn.setOnClickListener(this);
	}

	protected void findShipByKeyWords(CharSequence keyWords) {
		final Map<String,Object> apiParams = new HashMap<String, Object>();
		apiParams.put("shipKeyWords", keyWords);
		if(keyWords.length() >= 0){
			AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					ConvertData cd= new ConvertData(arg0);
					if(cd.getReturnCode().equals("Success")){
						List shipList = (ArrayList<ShipData>)cd.getContent();
						for(int i=0; i<shipList.size();i++){
							Map<String,Object> tmp =  (HashMap<String,Object>)shipList.get(i);
							curSeclectShip = new ShipData(tmp);
							showBoatInfo(curSeclectShip);
						}
					}
				}
				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
				}
			};
			dataLoader.getApiResult(handler,"/mobile/order/findShip", apiParams,"get");
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		btnAddCC.setOnClickListener(null);
		btnAddCC.setBackgroundColor(0xFF6db7ff);
		if(!orderId.equals(0L)){
			setTitle("查找运输工具"); 
			loadDate();
		}

	}

	@Override
	protected synchronized void loadDate() {
		final Map<String,Object> apiParams = new HashMap<String, Object>();
		apiParams.put("orderId", orderId);
		AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				Toast.makeText(FindShipActivity.this, content,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据读取中，请稍候...");
			}
			@Override
			public void onSuccess(String res) {
				super.onSuccess(res);
				ConvertData cd = new ConvertData(res,"/mobile/order/myOrder/edit",apiParams);
				HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
				HashMap<String, Object> var2 = (HashMap<String, Object>)var.get("orderData");
				OrderData order = new OrderData(var2); 
				curSeclectShip = order.getShipData();
				String cid= curSeclectShip.getId().toString();
				//当前用户在合同中角色为托运人时，不显示车船选择框
				if(!cid.equals("0")){
					showBoatInfo(curSeclectShip);
				}
				dialog.dismiss();
			}

		};
		//同步获取当前用户，set到curSeclectUser中

		//调用获取合同接口
		dataLoader.getApiResult(showHandler,"/mobile/order/myOrder/edit", apiParams,"get");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_save){

			Intent simpleIntent = new Intent();
			simpleIntent.putExtra("orderData", curSeclectShip);
			setResult(RESULT_OK, simpleIntent);
			finish();
		}

	}
	/**
	 * 异步取船舶信息，复制到视图，然后显示出来
	 */
	private void showBoatInfo(ShipData cur){
		ImageView user_head = (ImageView)findViewById(R.id.user_head);		//头像替换
		String uheadLogo = cur.getShipLogo();
		if(!uheadLogo.equals("")){
			String headString = ApplicationConstants.IMAGE_URL+uheadLogo;
			imgLoader.load_normal_Img(headString, user_head);
		}

		Button shipName=(Button)findViewById(R.id.shipName);
		shipName.setText(cur.getShipName());
		Button shipMMSI = (Button)findViewById(R.id.shipMMSI);
		shipMMSI.setText(cur.getMmsi());

		Button carrierName=(Button)findViewById(R.id.carrierName);
		if(cur.getMaster().getTrueName().equalsIgnoreCase("")){
			carrierName.setText(cur.getMaster().getLoginName());
		}else{
			carrierName.setText(cur.getMaster().getTrueName());

		}

		//shipCate 类别
		Button shipCate=(Button)findViewById(R.id.shipCate);
		shipCate.setText(cur.getTypeData().getTypeName());
		//航线
		Button phoneNum=(Button)findViewById(R.id.phoneNum);
		phoneNum.setText(cur.getKeyWords());
		//shipTon 总吨
		Button shipTon=(Button)findViewById(R.id.shipTon);
		shipTon.setText(cur.getSumTons()+"吨");

		//动态
		TextView shipKeywords=(TextView)findViewById(R.id.shipKeywords);
		shipKeywords.setText(cur.getArvlftDesc());
		userInfo.setVisibility(View.VISIBLE);
	}





}
