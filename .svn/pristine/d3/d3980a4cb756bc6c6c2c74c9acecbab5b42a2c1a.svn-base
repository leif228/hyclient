package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipMonitorApplyAdapter extends BaseAdapter {

	private class buttonViewHolder {
		ImageView shipLogo;//船logo
		
		TextView shipName,TypeCode,shipType,code,place,keyWords,carrier,endtime,shipStatic;
		Button btnEdit;//编辑按钮
		Button btnCancle;//取消按钮

		public TextView userId;

	}
	private ArrayList<Map<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;
	private ImageLoader mImageLoader;
	public static String shipId;
	public static ShipData shipData;
	private ProgressDialog pd;
	public static DisplayImageOptions displayImageOptions =new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.eyd_chat_ic_cycle) 
	.showImageForEmptyUri(R.drawable.eyd_chat_search_clear_pressed)
    .showImageOnFail(R.drawable.img_load_failed)  
    .bitmapConfig(Bitmap.Config.RGB_565)
    .cacheInMemory(true)    
    .cacheOnDisc(true)      //缓存到sd卡
    .build();    
    Data_loader data2 = new Data_loader();
    DialogUtil dialogUtil;
	private ListView listView;
	public ShipMonitorApplyAdapter(Context c, ArrayList<Map<String, Object>> appList, ListView listView) {
		this.mAppList = appList;
		this.mContext = c;
		this.listView = listView;
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
			convertView = mInflater.inflate(R.layout.eyd_shipmonitorapply_item, null);
			holder = new buttonViewHolder();
			holder.userId =(TextView)convertView.findViewById(R.id.userId);
			holder.shipLogo = (ImageView) convertView.findViewById(R.id.shipLogo);
			holder.shipName = (TextView) convertView.findViewById(R.id.shipName);
			holder.shipType =  (TextView) convertView.findViewById(R.id.shipType);
			holder.TypeCode =  (TextView) convertView.findViewById(R.id.TypeCode);
			holder.place = (TextView) convertView.findViewById(R.id.place);
			holder.carrier=(TextView) convertView.findViewById(R.id.carrier);
			holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
			holder.shipStatic = (TextView) convertView.findViewById(R.id.shipStatic);
			holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
			holder.btnCancle = (Button) convertView.findViewById(R.id.btnCancel);
			convertView.setTag(holder);
		}

		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			
			//设置imageView显示内容
		
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("shipLogo").toString(), 
					holder.shipLogo,displayImageOptions);
			holder.userId.setText((String) appInfo.get("userId"));
			holder.shipName.setText((String) appInfo.get("shipName"));
			holder.shipType.setText((String) appInfo.get("shipType"));
			holder.TypeCode.setText((String) appInfo.get("TypeCode"));
			holder.carrier.setText((String)appInfo.get("carrier"));
			holder.place.setText((String) appInfo.get("place"));
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
			//Map<Long, Object> curMap = (Map<Long, Object>)getItem(position);
			HashMap<String, Object> curMap =(HashMap<String, Object>) mAppList.get(position);
			shipId =  curMap.get("shipId").toString();
			if (vid == holder.btnEdit.getId()){ 
				new AlertDialog.Builder(mContext).setTitle("同意监控").setMessage("确定同意监控其船舶吗？")
				  .setPositiveButton("确定",new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int which) {   

						  agree(position);//
					  }
				  })  
				  .setNegativeButton("取消", null)
			      .show();
			}
			if (vid == holder.btnCancle.getId()){ 
				new AlertDialog.Builder(mContext).setTitle("取消监控").setMessage("确定取消监控吗？")
				  .setPositiveButton("确定",new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int which) {   
						  unagree(position);//取消
					  }
				  })  
				  .setNegativeButton("取消", null)
			      .show();
			}
		}

	} 
	public void agree(final int position) {
		
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				pd.setMessage("处理中...");
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
//						mAppList.remove(position);
						Map<String, Object> appInfo = mAppList.get(position);
						if (appInfo != null) {
							appInfo.put("btnEdit",false);
							notifyDataSetChanged();
							listView.setSelection(position);
						}
					}else{
                        Toast.makeText(mContext, (CharSequence) result.get("message"),Toast.LENGTH_LONG).show();
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

		params.put("shipId",ida.get("shipId"));
		params.put("userId",ida.get("userId"));
		data2.getApiResult(handler, "/mobile/ship/agree", params,"post");
	}
	public void unagree(final int position) {
		
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				pd.setMessage("处理中...");
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
						listView.setSelection(position);
				}else{
					Toast.makeText(mContext, (CharSequence) result.get("message"),Toast.LENGTH_LONG).show();
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
		
		params.put("shipId",ida.get("shipId"));
		params.put("userId",ida.get("userId"));
		data2.getApiResult(handler, "/mobile/ship/unagree", params,"post");
	}
    public void addItem(Map<String, Object> item) {    
        mAppList.add(item);    
    }
}
