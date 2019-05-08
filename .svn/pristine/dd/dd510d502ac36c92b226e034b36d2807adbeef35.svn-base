package com.eyunda.third.activities.order;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.FindTYRActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 新增合同-查找运输工具
 * @author guoqiang
 *
 */
public class AddOrderCCActivity extends AddOrderActivity implements
OnClickListener{
	Data_loader dataLoader;
	private TextView consignor;
	private TextView carrier;
	private TextView company1;
	private TextView company2;
	private TextView shipType;
	private TextView shipName;
	private ImageLoader mImageLoader;
	private ImageView consignorImg;
	private ImageView carrierImg;
	private ImageView shipImg;
	private Long consignorId;
	private Long shipId;
	private Long carrierId;
	private Button delShip, btnSave;
	Long userId ;
	//private String orderId;
	private static final int SEARCH_CONSIGNER = 0;
	private static final int REQUEST_CODE_SHIP = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		orderId=(Long)bundle.getLong("orderId");
		setContentView(R.layout.eyd_order_findcc);
		dataLoader = new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		consignor =(TextView)findViewById(R.id.tuoyunren);
		carrier =(TextView)findViewById(R.id.chengyunren);
		company1=(TextView)findViewById(R.id.company1);
		company2=(TextView)findViewById(R.id.company2);
		shipType=(TextView)findViewById(R.id.shipType);
		shipName=(TextView)findViewById(R.id.shipName);
		consignorImg =(ImageView)findViewById(R.id.consignorLogo);
		carrierImg =(ImageView)findViewById(R.id.carrierLogo);
		shipImg =(ImageView)findViewById(R.id.shipLogo);
		delShip =(Button)findViewById(R.id.del);
		btnSave =(Button)findViewById(R.id.btnSave);
		consignorImg.setOnClickListener(this);
		delShip.setOnClickListener(this);
		shipImg.setOnClickListener(this);
		if(!orderId.equals(0L))
		getDatas();
		userId = GlobalApplication.getInstance().getUserData().getId();
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶");
	}
     @Override
    public void onClick(View v) {
    	super.onClick(v);
    	
    	switch (v.getId()) {
		case R.id.consignorLogo:
			 if(userId.equals(consignorId)){ //当前用户在合同中角色是托运人，不能选择。
					return;
				}
			Intent intent = new Intent(this,FindTYRActivity.class);
			intent.putExtra("id", orderId);
			startActivityForResult(intent, SEARCH_CONSIGNER);
			break;

		case R.id.shipLogo:
			if(userId.equals(consignorId)){ //当前用户在合同中角色是托运人，不能选择。
				return;
			}
			Intent i = new Intent(this,FindShipActivity.class);
			i.putExtra("orderId", orderId);
			startActivityForResult(i, REQUEST_CODE_SHIP);
			break;
		case R.id.del:
			new AlertDialog.Builder(this)
			.setTitle("删除船舶?")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					shipId=0L;
					shipType.setText("");
					shipName.setText("");
					shipImg.setImageResource(R.drawable.img_load_failed);
					delShip.setVisibility(View.GONE);
				}
			})
			.setNegativeButton("取消", null)
			.show();
			break;
		}
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        	super.onActivityResult(requestCode, resultCode, data);
        	if(resultCode==RESULT_OK && requestCode==SEARCH_CONSIGNER){
    			UserData user =(UserData) data.getSerializableExtra("userData");
    			String trueName = user.getTrueName();
    			if(trueName.equals("")){
    				consignor.setText(user.getNickName());
    			}else
    				consignor.setText(trueName);
    			company1.setText(user.getUnitName());
    			consignorId = user.getId();
    			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL+ user.getUserLogo(), consignorImg, MyshipAdapter.displayImageOptions);
    		}
        	if(resultCode==RESULT_OK && requestCode==REQUEST_CODE_SHIP){
    			ShipData ship =(ShipData) data.getSerializableExtra("orderData");
    				String shipImage = ship.getShipLogo();
    				shipType.setText(ship.getTypeData().getTypeName());
    				shipName.setText(ship.getShipName());
    				shipId = ship.getId();
    				carrierId = ship.getMasterId();
    				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL+shipImage, shipImg, MyshipAdapter.displayImageOptions);
    				delShip.setVisibility(View.VISIBLE);
    		}
        	
        }
	public void saveShip(View v){
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ConvertData cd = new ConvertData(arg0);
				if(cd.getReturnCode().equals("Success")){
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
				}else
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				Toast.makeText(getApplicationContext(),arg1, Toast.LENGTH_SHORT).show();
			}
		};
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", orderId);
		params.put("userId", consignorId);
		params.put("carrierId", carrierId);
		params.put("shipId", shipId);
		Log.v("cc", params.toString());
		dataLoader.getApiResult(handler, "/mobile/order/myOrder/saveUsers",params,"get");

	}
	private void getDatas() {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/order/myOrder/edit", params);
				if(cd.getReturnCode().equals("Success")){
					Map<String,Object> map = (Map<String, Object>) cd.getContent();
					Map<String,Object> orderMap =(HashMap<String, Object>) map.get("orderData");
					OrderData  orderData = new OrderData(orderMap);
					consignorId =orderData.getOwnerId();
					shipId =orderData.getShipId();
					carrierId =orderData.getMasterId();
					consignor.setText(orderData.getOwner().getNickName());
					carrier.setText(orderData.getMaster().getNickName());
					company1.setText(orderData.getOwner().getUnitName());
					company2.setText(orderData.getMaster().getUnitName());
					mImageLoader.displayImage(ApplicationConstants.IMAGE_URL+ orderData.getOwner().getUserLogo(),
							consignorImg, MyshipAdapter.displayImageOptions);
					mImageLoader.displayImage(ApplicationConstants.IMAGE_URL+ orderData.getMaster().getUserLogo(),
							carrierImg, MyshipAdapter.displayImageOptions);
					
					String shipTypeStr = "";
					String shipNameStr = "";
					String shipLogoStr = "";
					if(orderData.getShipData().getId() > 0){
						shipTypeStr  = orderData.getShipData().getTypeData().getTypeName();
						shipNameStr = orderData.getShipData().getShipName();
						shipLogoStr = orderData.getShipData().getShipLogo();
						shipType.setVisibility(View.VISIBLE);
						shipName.setVisibility(View.VISIBLE);
					}else{
						shipType.setVisibility(View.GONE);
						shipName.setVisibility(View.GONE);
					}
					shipType.setText(shipTypeStr);
					shipName.setText(shipNameStr);
					mImageLoader.displayImage(ApplicationConstants.IMAGE_URL+ shipLogoStr,
							shipImg, MyshipAdapter.displayImageOptions);
					
					if(userId.equals(consignorId) || (orderData.getShipData().getId() == 0)){
						//托运人不显示删除船舶按钮
						delShip.setVisibility(View.GONE);
					}
					if(userId.equals(consignorId)){
						btnSave.setVisibility(View.GONE);
					}
				}else{
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();

				}
			}
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();

			}
		};
		dataLoader.getApiResult(handler, "/mobile/order/myOrder/edit",params,"get");
	}
}
