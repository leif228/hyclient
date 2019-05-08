package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.ship.ShipinfoActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrderShipAdapter extends BaseAdapter {

	private class buttonViewHolder {
		ImageView shipLogo;//船logo
		
		TextView shipId,shipName,TypeCode,shipType,code,adv,carrier,endtime,shipStatic;
		Button btnEdit;//编辑按钮
		Button btnDelete;//删除按钮
		Button btnPublish;//发布按钮
		Button btnCancle;//取消按钮

		public TextView userId;
		
	}
	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;
	private ImageLoader mImageLoader;
	private ProgressDialog pd;
	public static DisplayImageOptions displayImageOptions =new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zd_icon) 
	.showImageForEmptyUri(R.drawable.eyd_chat_search_clear_pressed)
    .showImageOnFail(R.drawable.img_load_failed)  
    .bitmapConfig(Bitmap.Config.RGB_565)
    .cacheInMemory(true)    
    .cacheOnDisc(true)      //缓存到sd卡
    .build();    
	SynData_loader data =new SynData_loader();
    Data_loader data2 = new Data_loader();
    DialogUtil dialogUtil;
	public MyOrderShipAdapter(Context c, ArrayList<Map<String, Object>> appList) {
		this.mAppList = appList;
		this.mContext = c;
	    pd = new ProgressDialog(c);
		mImageLoader = ImageLoader.getInstance();
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (buttonViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.eyd_my_order_ship_item, null);
			holder = new buttonViewHolder();
			holder.userId =(TextView)convertView.findViewById(R.id.userId);
			holder.shipId =(TextView)convertView.findViewById(R.id.shipId);
			holder.shipLogo = (ImageView) convertView.findViewById(R.id.shipLogo);
			holder.shipName = (TextView) convertView.findViewById(R.id.shipName);
			holder.shipType =  (TextView) convertView.findViewById(R.id.shipType);
			holder.TypeCode =  (TextView) convertView.findViewById(R.id.TypeCode);
			holder.code = (TextView) convertView.findViewById(R.id.weight);
			holder.carrier=(TextView) convertView.findViewById(R.id.carrier);
			holder.adv = (TextView) convertView.findViewById(R.id.adv);
			holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
			holder.shipStatic = (TextView) convertView.findViewById(R.id.shipStatic);
			holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
			holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
			holder.btnPublish = (Button) convertView.findViewById(R.id.btnPublish);
			holder.btnCancle = (Button) convertView.findViewById(R.id.btnCancel);
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			
			//设置imageView显示内容
		
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("shipLogo").toString(), 
					holder.shipLogo,displayImageOptions);
			holder.userId.setText((String) appInfo.get("userId"));
			holder.shipId.setText((String) appInfo.get("shipId"));
			holder.shipName.setText((String) appInfo.get("shipName"));
			holder.shipType.setText((String) appInfo.get("shipType"));
			holder.TypeCode.setText((String) appInfo.get("TypeCode"));
			holder.code.setText((String) appInfo.get("code"));
			holder.carrier.setText((String)appInfo.get("carrier"));
			holder.adv.setText((String) appInfo.get("adv"));
			holder.endtime.setText((String) appInfo.get("endtime"));
			holder.shipStatic.setText((String) appInfo.get("shipStatic"));
			
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnEdit")) {//编辑
				holder.btnEdit.setVisibility(View.VISIBLE);
				holder.btnEdit.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnEdit.setVisibility(View.GONE);
			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnDelete")) {//删除
				holder.btnDelete.setVisibility(View.VISIBLE);
				holder.btnDelete.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnDelete.setVisibility(View.GONE);
			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnPublish")) {//发布
				holder.btnPublish.setVisibility(View.VISIBLE);
				holder.btnPublish.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnPublish.setVisibility(View.GONE);
			}
			//控制按钮显示隐藏
			if ((Boolean) appInfo.get("btnCancel")) {//取消
				holder.btnCancle.setVisibility(View.VISIBLE);
				holder.btnCancle.setOnClickListener(new lvButtonListener(position));
			} else {
				holder.btnCancle.setVisibility(View.GONE);
			}
			
			
		}
		
		return convertView;
		
	}



	class lvButtonListener implements OnClickListener {
		private int position;
		 Bitmap bitmap;


		lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			Map<Long, Object> curMap = (Map<Long, Object>)getItem(position);
			Map<String, Object> ida =mAppList.get(position);
			if (vid == holder.btnEdit.getId()){ 
				//Toast.makeText(mContext, holder.btnEdit.getText()+"-position:"+position+"-shipID:"+curMap.get("orderNum"), Toast.LENGTH_SHORT).show();
			    Intent intent = new Intent(mContext,ShipinfoActivity.class);
			    Bundle bundle = new Bundle();
			    
			   // bundle.putString("userId",MyshipActivity.userId);//船舶名
			    bundle.putString("myName",ida.get("shipName").toString());//船舶名
				bundle.putString("shipId", ida.get("shipId").toString());//shipId
				bundle.putString("logoUrl", ida.get("shipLogo").toString()); //imge uri
				bundle.putString("currentCyr", ida.get("carrier").toString()); //当前承运人
				bundle.putString("currentCity", ida.get("place").toString()); //当前地区
				bundle.putString("currentShip", ida.get("shipType").toString()); //当前船类
				//Log.d("MyshipAdapter--shipId", ida.get("shipId").toString());
				intent.putExtras(bundle);
			    mContext.startActivity(intent);
                ((Activity) mContext).finish();
			}
			
			if (vid == holder.btnDelete.getId()){
			//	Toast.makeText(mContext, holder.btnDelete.getText()+"-position:"+position+"-shipID:"+curMap.get("shipName"), Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(mContext).setTitle("删除船舶信息").setMessage("确定删除吗？")
				  .setPositiveButton("确定",new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int which) {   

					  removeItem(position);//删除
					  }
				  })  
				  .setNegativeButton("取消",null)
			      .show();
			}
			if (vid == holder.btnPublish.getId()){
			//	Toast.makeText(mContext, holder.btnPublish.getText()+"-position:"+position+"-shipID:"+curMap.get("shipName"), Toast.LENGTH_SHORT).show();
				publish(position);//发布
			}
			if (vid == holder.btnCancle.getId()){ 
			//	Toast.makeText(mContext, holder.btnCancle.getText()+"-position:"+position+"-shipID:"+curMap.get("shipName"), Toast.LENGTH_SHORT).show();
			    unpublish(position);//取消发布
			}
		
		}

	} 
	//删除
	public void removeItem(final int position) {
		
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				pd.setMessage("删除中...");
   				pd.setCanceledOnTouchOutside(false);
   				pd.show();
			}
			 @Override
			public void onSuccess(String arg0) {
				 pd.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> result= gson.fromJson(
							(String) arg0,
							new TypeToken<Map<String, Object>>() {}.getType());
					if (result.get("returnCode").equals("Success")) {
						mAppList.remove(position);
						notifyDataSetChanged();
                        Toast.makeText(mContext, "删除"+result.get("message"),Toast.LENGTH_SHORT).show();
					}
			}
			 @Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				pd.dismiss();
  				Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
			}
		};
		Map<String,Object> params = new HashMap<String, Object>();
		Map<String, Object> ida =mAppList.get(position);

		params.put("id",ida.get("shipId"));
	    data2.getApiResult(handler, "/mobile/ship/myShip/delete", params,"get");
	}
	//取消发布
       public void unpublish(int position) {
    	   AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
    		   @Override
    		   public void onStart() {
   				super.onStart();
   				pd.setMessage("取消中...");
   				pd.setCanceledOnTouchOutside(false);
   				pd.show();
   			}
  			 @Override
  			public void onSuccess(String arg0) {
  				   pd.dismiss();
  				   refreshPubish();
  				  ConvertData  cd = new ConvertData(arg0);
                   Toast.makeText(mContext, "取消发布"+cd.getMessage(), Toast.LENGTH_SHORT).show();
  				
  			 }
  			 @Override
  			public void onFailure(Throwable arg0) {
  				super.onFailure(arg0);
  				Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
  			}
  		};
  		Map<String,Object> params = new HashMap<String, Object>();
  		Map<String, Object> ida =mAppList.get(position);
          Log.v("ids", ida.toString());
  		params.put("id",ida.get("shipId"));
  	    data2.getApiResult(handler, "/mobile/ship/myShip/unpublish", params,"post");
  	
  }
	

	//发布
	public void publish(final int position) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				pd.setMessage("发布");
				pd.setCanceledOnTouchOutside(false);
				pd.show();
			}
			 @Override
			public void onSuccess(String arg0) {
                  pd.dismiss();
					ConvertData  cd = new ConvertData(arg0);
                     publish();
				    notifyDataSetChanged();
				Toast.makeText(mContext,"发布"+cd.getMessage(),Toast.LENGTH_SHORT).show();
					
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
  				Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
  				pd.dismiss();
			}
		};
		Map<String,Object> params = new HashMap<String, Object>();
		Map<String, Object> ida =mAppList.get(position);
        Log.v("ids", ida.toString());
		params.put("id",ida.get("shipId"));
	    data2.getApiResult(handler, "/mobile/ship/myShip/publish", params,"post");
		this.notifyDataSetChanged();
	}
	
    private void publish() {
        	((Activity) mContext).finish();
        mContext.startActivity(new Intent(mContext.getApplicationContext(),MyshipActivity.class));

		}
    public void refreshPubish(){
    	((Activity) mContext).finish();
    	mContext.startActivity(new Intent(mContext.getApplicationContext(),MyshipActivity.class));
    }
    public void addItem(Map<String, Object> item) {    
        mAppList.add(item);    
    }
}
