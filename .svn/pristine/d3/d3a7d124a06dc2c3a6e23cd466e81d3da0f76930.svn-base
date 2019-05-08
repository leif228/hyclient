package com.eyunda.third.adapters.oil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.cargo.CargoActivity;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.cargo.CargoSendNotifyActivity;
import com.eyunda.third.activities.home.GasOrderInActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class OilAdapter extends BaseAdapter {
	private Activity mContext;   
	private List<Map<String, Object>> listItems; 
	private LayoutInflater listContainer; 
	private Data_loader data;
	private ViewHolder  holder;
	private ImageLoader mImageLoader;
	DialogUtil dialogUtil;
	ProgressDialog dialog;

	public OilAdapter(Activity context, ArrayList<Map<String, Object>> listItems) {
		this.mContext = context;  
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文    
		this.listItems = listItems;  
		data = new Data_loader();
		dialogUtil = new DialogUtil(mContext);
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return  listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {   
			//获取list_item布局文件的视图    
			convertView = listContainer.inflate(R.layout.eyd_oil_item, null);
			holder = new ViewHolder();
			//获取控件对象    
			holder.oilId =(TextView)convertView.findViewById(R.id.oilId);
			holder.oilImage =(ImageView)convertView.findViewById(R.id.oilImage);  
			holder.oilName=(TextView)convertView.findViewById(R.id.oilName); 
			holder.oilPrice=(TextView)convertView.findViewById(R.id.oilPrice); 
			holder.oilDepartPrice=(TextView)convertView.findViewById(R.id.oilDepartPrice); 
			holder.oilCompay=(TextView)convertView.findViewById(R.id.oilCompay); 
			holder.oilDescrip=(TextView)convertView.findViewById(R.id.oilDescrip); 

			holder.ads  =(TextView)convertView.findViewById(R.id.ads); 
			
			holder.btnBuy =(Button)convertView.findViewById(R.id.btnBuy);  
			//设置控件集到convertView    
			convertView.setTag(holder); 



		}else {    
			holder = (ViewHolder)convertView.getTag();    
		}
//		if(listItems.get(position).get("oilImage").equals("")){
//			mImageLoader.displayImage(ApplicationConstants.SERVER_URL+ "/img/cargoImage/" +listItems.get(position).get("cargoTypeCode")+".jpg", 
//					holder.oilImage,MyshipAdapter.displayImageOptions);
//		}else
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +listItems.get(position).get("oilImage").toString(), 
					holder.oilImage,MyshipAdapter.displayImageOptions);
		holder.oilId.setText((String) listItems.get(position).get("oilId"));  
		holder.oilName.setText((String) listItems.get(position).get("oilName"));  
		holder.oilPrice.setText((String) listItems.get(position).get("oilPrice"));  
		holder.oilDepartPrice.setText((String) listItems.get(position).get("oilDepartPrice"));  
		holder.oilCompay.setText((String) listItems.get(position).get("oilCompay"));  
		holder.oilDescrip.setText((String) listItems.get(position).get("oilDescrip"));
		holder.ads.setText((String) listItems.get(position).get("ads"));  
		
		if((Boolean) listItems.get(position).get("btnBuy")){

			holder.btnBuy.setVisibility(View.VISIBLE);
			holder.btnBuy.setOnClickListener(new lvButtonListener(position));
			
		}else{
			holder.btnBuy.setVisibility(View.GONE);
			
		}
		
		//监听
		return convertView;
	}
	class lvButtonListener implements OnClickListener {
		private int position;
		public lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			//	Map<Long, Object> curMap = (Map<Long, Object>)getItem(position);
			HashMap<String, Object> curMap =(HashMap<String, Object>) listItems.get(position);
			switch (v.getId()) {
			case R.id.btnBuy:

				Intent intent = new Intent(mContext,GasOrderInActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", curMap.get("oilId").toString());
			    intent.putExtras(bundle);
				mContext.startActivity(intent);
				break;

			}


		}

	}
	

	

	public  class ViewHolder{ 
		public Button btnBuy;
		//自定义控件集合     
		public ImageView oilImage;
		public TextView  oilId;
		public TextView  oilName;
		public TextView  oilPrice; //货类
		public TextView oilCompay;  //公司名称     
		public TextView oilDepartPrice;     //销售价格
		public TextView oilDescrip;     //商品描述 
		public TextView ads;       //价格标语
	}


}
