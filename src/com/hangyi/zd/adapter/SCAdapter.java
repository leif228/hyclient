package com.hangyi.zd.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.main.view.DialogUtil;
import com.hangyi.zd.domain.ShipSCData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SCAdapter extends BaseAdapter {

	private class buttonViewHolder {
		ImageView shipLogo;//船logo
		
		TextView shipId,shipName,TypeCode,shipType,place,carrier,endtime,dongtai;

		Button btnPublish;//实时轨迹

		public TextView mmsi;

		public CheckBox checkBox;
		
	}
	private ArrayList<ShipSCData> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private buttonViewHolder holder;
	private ImageLoader mImageLoader;
	public static DisplayImageOptions displayImageOptions =new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.eyd_chat_ic_cycle) 
	.showImageForEmptyUri(R.drawable.eyd_chat_search_clear_pressed)
    .showImageOnFail(R.drawable.zd_icon)  
    .bitmapConfig(Bitmap.Config.RGB_565)
    .cacheInMemory(true)    
    .cacheOnDisc(true)      //缓存到sd卡
    .build(); 


    DialogUtil dialogUtil;
	public SCAdapter(Context c, List<ShipSCData> shipSCDataList) {
		this.mAppList = (ArrayList<ShipSCData>) shipSCDataList;
		this.mContext = c;
//		File cacheDir = new File(Environment.getExternalStorageDirectory() + "/eyunda/img");
//		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(c)
//		.memoryCacheExtraOptions(200, 200)
//		.discCacheFileNameGenerator(new Md5FileNameGenerator())
//		.threadPoolSize(3)
//		.threadPriority(Thread.NORM_PRIORITY - 2)
//		.discCache(new UnlimitedDiscCache(cacheDir))
//		.tasksProcessingOrder(QueueProcessingType.FIFO)
//		.memoryCache(new LruMemoryCache(4 * 1024 * 1024))
//		.memoryCacheSizePercentage(10)
//		.imageDownloader(new BaseImageDownloader(c, 5 * 1000, 30 * 1000))
//		.imageDecoder(new BaseImageDecoder(true))
//		.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//		.writeDebugLogs().build();// 开始构建
//		
//		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(imageLoaderConfiguration);
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
			convertView = mInflater.inflate(R.layout.zd_sc_item, null);
			holder = new buttonViewHolder();
//			holder.shipId =(TextView)convertView.findViewById(R.id.shipId);
//			holder.shipLogo = (ImageView) convertView.findViewById(R.id.shipLogo);
			holder.shipName = (TextView) convertView.findViewById(R.id.shipName);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
//			holder.shipType =  (TextView) convertView.findViewById(R.id.shipType);
//			holder.TypeCode =  (TextView) convertView.findViewById(R.id.TypeCode);
//			holder.mmsi = (TextView) convertView.findViewById(R.id.mmsi);
//			holder.place = (TextView) convertView.findViewById(R.id.place);
//			holder.carrier=(TextView) convertView.findViewById(R.id.carrier);
//			holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
//			holder.dongtai = (TextView) convertView.findViewById(R.id.dongtai);
//			holder.btnPublish = (Button) convertView.findViewById(R.id.btnPublish);
			convertView.setTag(holder);
		}

		final ShipSCData appInfo = mAppList.get(position);
		if (appInfo != null) {
			
			//设置imageView显示内容
		
//			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +appInfo.get("shipLogo").toString(), 
//					holder.shipLogo,MyshipAdapter.displayImageOptions);
//			holder.shipId.setText((String) appInfo.get("shipId"));
			holder.shipName.setText((String) appInfo.getShipName());
//			holder.shipType.setText((String) appInfo.get("shipType"));
//			holder.TypeCode.setText((String) appInfo.get("TypeCode"));
//			holder.mmsi.setText((String) appInfo.get("MMSI"));
//			holder.carrier.setText((String)appInfo.get("carrier"));
//			holder.place.setText((String) appInfo.get("place"));
//			holder.endtime.setText((String) appInfo.get("endtime"));
//			holder.dongtai.setText((String) appInfo.get("dongtai"));
			holder.checkBox.setOnClickListener(new lvButtonListener(position));
			if("0".equals(appInfo.getFocus())){
				holder.checkBox.setChecked(false);
			}else{
				holder.checkBox.setChecked(true);
			}
//			holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					if(isChecked)
//						appInfo.setFocus("1");
//					else
//						appInfo.setFocus("0");
//				}
//			});

			
		}
		
		return convertView;
		
	}



	class lvButtonListener implements OnClickListener {
		private int position;


		lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			ShipSCData s =mAppList.get(position);
			CheckBox cb = (CheckBox) v;
			if(!cb.isChecked()){
				cb.setChecked(false);
				s.setFocus("0");
			}else{
				cb.setChecked(true);
				s.setFocus("1");
			}
//			notifyDataSetChanged();
		}

	} 


    public void addItem(ShipSCData item) {    
        mAppList.add(item);    
    }
}
