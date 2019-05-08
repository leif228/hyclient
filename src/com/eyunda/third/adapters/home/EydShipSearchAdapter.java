package com.eyunda.third.adapters.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.home.HomeCategoryActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class EydShipSearchAdapter extends BaseAdapter{


	private ArrayList<Map<String, Object>> mAppList;
	private Activity mContext;
	private LayoutInflater mInflater;
	private ButtonViewHolder holder;
	private ImageLoader mImageLoader;
	private DialogUtil dialogUtil;
	private ProgressDialog dialog;
	private int type;
	Data_loader data = new Data_loader();
	public EydShipSearchAdapter(Activity context,
			ArrayList<Map<String, Object>> dataList,int type) {
		mImageLoader = ImageLoader.getInstance();
		dialogUtil = new DialogUtil(context);
		this.mAppList = dataList;
		this.mContext = context;
		this.type = type;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (ButtonViewHolder) convertView.getTag();
		} else {
			switch (type) {

			case HomeCategoryActivity.MSG_DLR_LIST://金牌代理列表
				convertView = mInflater.inflate(R.layout.eyd_operatorsearch_one_item, null);
				holder = new ButtonViewHolder();
				holder.logo =(ImageView) convertView.findViewById(R.id.boat_logo); //代理人头像
				holder.phoneNum =(TextView) convertView.findViewById(R.id.boat_ton);  //电话
				holder.id =(TextView)convertView.findViewById(R.id.boat_id); // id
				
				holder.loginName = (TextView)convertView.findViewById(R.id.loginName); // 登录名
				holder.name = (TextView)convertView.findViewById(R.id.name); // 姓名
				//holder.nickName = (TextView)convertView.findViewById(R.id.nickName); // 昵称
				holder.email =(TextView) convertView.findViewById(R.id.boat_dis);  //email
				holder.area  =(TextView) convertView.findViewById(R.id.areas);   //地区
				holder.companyName =(TextView) convertView.findViewById(R.id.companyName);//公司名
				holder.daiLi =(TextView) convertView.findViewById(R.id.daiLi); //代理船舶数
				holder.cjht =(TextView) convertView.findViewById(R.id.cjht); //代理船舶数
				holder.isCreate  =(TextView) convertView.findViewById(R.id.isCreate); //有没有网站
				
				convertView.setTag(holder);
				break;

			case HomeCategoryActivity.MSG_SHIP_LIST://船舶分类列表

			case HomeCategoryActivity.MSG_SHIP_LINE://船舶地区
				convertView = mInflater.inflate(R.layout.activity_home_item, null);
				holder = new ButtonViewHolder();
				holder.logo = (ImageView)convertView.findViewById(R.id.shipLogo); //logo
				holder.id =(TextView) convertView.findViewById(R.id.shipId);
				holder.shipName =(TextView) convertView.findViewById(R.id.shipName);//船名
				holder.MMSI =(TextView) convertView.findViewById(R.id.mmsi);//MMSI
				holder.sailLine =(TextView)convertView.findViewById(R.id.sailLine); //地区
				holder.times =(TextView)convertView.findViewById(R.id.time);      //时间
				holder.types =(TextView)convertView.findViewById(R.id.shipType);      //类别
				holder.port =(TextView)convertView.findViewById(R.id.homePort);        //动态
				holder.shipper=(TextView)convertView.findViewById(R.id.cyr); //承运人
				convertView.setTag(holder);
				break;
			case HomeCategoryActivity.MSG_DYNAMIC_LIST://船舶动态
			case HomeCategoryActivity.MSG_CARGRO_CATE_LIST://货运分类
			case HomeCategoryActivity.MSG_CARGRO_AREA_LIST://货物地区运输
				convertView = mInflater.inflate(R.layout.eyd_home_cargo_item, null);
				holder = new ButtonViewHolder();
				holder.logo = (ImageView) convertView.findViewById(R.id.boat_logo);
				holder.cargoName=(TextView)convertView.findViewById(R.id.list_cargoName); //货名
				holder.cargoPhone=(TextView)convertView.findViewById(R.id.list_phone); //电话
				holder.shipper=(TextView)convertView.findViewById(R.id.list_shipper); //货主
				holder.cargoCode=(TextView)convertView.findViewById(R.id.list_cargoCode); //货号
				holder.volume  =(TextView)convertView.findViewById(R.id.list_volume);  //重量/体积/数量
				holder.place =(TextView)convertView.findViewById(R.id.list_place);   //起止港
				holder.unitPrice =(TextView)convertView.findViewById(R.id.unit_price);   //单价
				holder.totalPrice =(TextView)convertView.findViewById(R.id.total_price);   //总价
				holder.endTime =(TextView)convertView.findViewById(R.id.list_endtime);   //截止日
				convertView.setTag(holder);
				break;

			}
		}
		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			switch (type) {


			case HomeCategoryActivity.MSG_SHIP_LIST://船舶分类


			case HomeCategoryActivity.MSG_SHIP_LINE://船舶地区
				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("shipLogo").toString(), 
						holder.logo,MyshipAdapter.displayImageOptions);
				holder.shipName.setText((String) appInfo.get("shipName"));
				holder.MMSI.setText((String) appInfo.get("MMSI")); 
				holder.id.setText((String) appInfo.get("id"));

				holder.sailLine.setText((String) appInfo.get("area"));
				holder.times.setText((String) appInfo.get("time"));
				holder.types.setText((String) appInfo.get("shipType"));
				holder.port.setText((String) appInfo.get("port"));
				holder.shipper.setText((String) appInfo.get("shipper"));

				break;
			case HomeCategoryActivity.MSG_DYNAMIC_LIST://船舶动态
				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("shipLogo").toString(), 
						holder.logo,MyshipAdapter.displayImageOptions);
				holder.shipName.setText((String) appInfo.get("shipName"));
				holder.MMSI.setText((String) appInfo.get("MMSI")); 
				holder.place .setText((String) appInfo.get("place")); 
				holder.mobile.setText((String) appInfo.get("mobile"));
				holder.dynamicInfo.setText((String) appInfo.get("dynamicInfo"));
				holder.receiveInfo.setText((String) appInfo.get("receiveInfo"));
				break;
			case HomeCategoryActivity.MSG_DLR_LIST://金牌代理列表
				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("agentLogo").toString(), 
						holder.logo,MyshipAdapter.displayImageOptions);
				
				holder.loginName.setText((String) appInfo.get("loginName")); 
				holder.name.setText((String) appInfo.get("name"));
				holder.email.setText((String) appInfo.get("email")); 
				holder.area.setText((String) appInfo.get("area"));  
				holder.companyName.setText((String) appInfo.get("companyName")); 
				holder.daiLi.setText((String) appInfo.get("daiLi")); 
				holder.cjht.setText((String) appInfo.get("cjht"));  
				holder.isCreate.setText((String) appInfo.get("isCreate").toString());  
			
				break;
			case HomeCategoryActivity.MSG_CARGRO_AREA_LIST://货物地区
			case  HomeCategoryActivity.MSG_CARGRO_CATE_LIST://货运分类列表
				if(appInfo.get("cargoImg").equals("")){
					mImageLoader.displayImage(ApplicationConstants.SERVER_URL+ "/img/cargoImage/" +appInfo.get("type")+".jpg", 
							holder.logo,MyshipAdapter.displayImageOptions);
				}else
				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("cargoImg").toString(), 
						holder.logo,MyshipAdapter.displayImageOptions);
				holder.cargoName.setText((String) appInfo.get("shipName"));
				holder.cargoCode.setText((String) appInfo.get("cargoCode")); 
				holder.cargoPhone .setText((String) appInfo.get("phone")); 
				holder.place .setText((String) appInfo.get("place")); 
				holder.shipper .setText((String) appInfo.get("shipper")); 
				holder.volume.setText((String) appInfo.get("volume")); 
				holder.endTime .setText((String) appInfo.get("endTime")); 
				holder.unitPrice .setText((String) appInfo.get("unitPrice")); 
				holder.totalPrice .setText((String) appInfo.get("totalPrice")); 

				break;
			}
		}
		return convertView;
	}

	protected void saveCargo(String id) {
		AsyncHttpResponseHandler ahr = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "正在联网获取数据...");
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd= new ConvertData(arg0);
				if(cd.getReturnCode().equals("Success")){
					Toast.makeText(mContext, "代理"+cd.getMessage() , Toast.LENGTH_SHORT).show();
					//					Intent intent = new Intent(mContext,CargoListActivity.class);
					//					mContext.startActivity(intent);
					//					mContext.finish();
				}else{
					Toast.makeText(mContext, cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				dialog.dismiss();
				Toast.makeText(mContext, "获取失败，请稍后再试", Toast.LENGTH_SHORT).show();

			}
		};
		HashMap<String, Object> map  = new HashMap<String, Object>();
		map.put("cargoId",id);
		data.getApiResult(ahr, "/mobile/cargo/myCargo/saveOperCargo",map,"post");

	}

	private class ButtonViewHolder {	
		public TextView cjht,isCreate;
		public TextView daiLi;
		public TextView companyName;
		public TextView area;
		public TextView nickName;
		public TextView name;
		public TextView loginName;
		public TextView port;
		public TextView types;
		public TextView times;
		public TextView sailLine;
		public TextView cargoCode;
		public TextView MMSI;
		public TextView cargoPhone;
		public TextView endTime;
		public TextView totalPrice;
		public TextView unitPrice;
		public TextView shipper;
		public TextView cargoName;
		public TextView phoneNum;
		public TextView volume;
		public TextView id;
		public TextView email;
		public TextView mobile;
		public TextView dynamicInfo;
		public TextView receiveInfo;
		ImageView logo;
		TextView shipName,place;

	}
}
