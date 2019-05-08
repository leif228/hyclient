package com.eyunda.third.adapters.home;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.home.view.XListView;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RecentCargoAdapter extends BaseAdapter{

	private ImageLoader mImageLoader;
	private ArrayList<Map<String, Object>> mAppList;
	private Activity mContext;
	private LayoutInflater mInflater;
	private FtViewHolder holder;
	private DialogUtil dialogUtil;
	ArrayList<Integer> times = new ArrayList<Integer>();
	int time=0;

	public RecentCargoAdapter(Activity rencentFragment,ArrayList<Map<String, Object>> dataList) {
		mImageLoader = ImageLoader.getInstance();
		dialogUtil = new DialogUtil(rencentFragment);
		this.mAppList = dataList;
		this.mContext = rencentFragment;
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
			holder = (FtViewHolder) convertView.getTag();
			if(parent instanceof XListView){
				if(((XListView) parent).isOnMeasure()){
					return convertView;
				}
			}
		} else {
			convertView = mInflater.inflate(R.layout.eyd_home_cargo_item, null);
			holder = new FtViewHolder();
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
			
		}
		Map<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			if(appInfo.get("cargoImg").equals("")){
				mImageLoader.displayImage(ApplicationConstants.SERVER_URL+ "/img/cargoImage/" +appInfo.get("type")+".jpg", 
						holder.logo,MyshipAdapter.displayImageOptions);
			}else
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("cargoImg").toString(), 
					holder.logo,MyshipAdapter.displayImageOptions);
			holder.cargoName.setText((String) appInfo.get("cargoName"));
			holder.cargoCode.setText((String) appInfo.get("cargoCode")); 
			holder.cargoPhone .setText((String) appInfo.get("phone")); 
			holder.place .setText((String) appInfo.get("place")); 
			holder.shipper .setText((String) appInfo.get("shipper")); 
			holder.volume.setText((String) appInfo.get("volume")); 
			holder.endTime .setText((String) appInfo.get("endTime")); 
			holder.unitPrice .setText((String) appInfo.get("unitPrice")); 
			holder.totalPrice .setText((String) appInfo.get("totalPrice")); 
		}
		
		
		return convertView;
	}
	
}
   class FtViewHolder {	
	public TextView cargoCode;
	public TextView cargoPhone;
	public TextView endTime;
	public TextView totalPrice;
	public TextView unitPrice;
	public TextView shipper;
	public TextView cargoName;
	public TextView volume;
	public ImageView logo;
	public TextView place;

}

