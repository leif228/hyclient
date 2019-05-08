package com.eyunda.third.adapters.cargo;

import java.util.ArrayList;
import java.util.Map;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.AddCargoResultActivity;
import com.eyunda.third.adapters.cargo.CargoAdapter.ViewHolder;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AgentAdapter extends BaseAdapter{
	private Activity context;
	private LayoutInflater listContainer;
	private ArrayList<Map<String, Object>> listItems;
	private ViewHolder  holder;
	private ImageLoader mImageLoader;
	public AgentAdapter(AddCargoResultActivity mContext,
			ArrayList<Map<String, Object>> data) {
		 this.context = mContext;  
         listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文    
         this.listItems = data;  
         mImageLoader = ImageLoader.getInstance();
         ImageLoader.getInstance().init(GlobalApplication.getInstance().getImageLoaderConfiguration());

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
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
             convertView = listContainer.inflate(R.layout.eyd_cargo_search_result, null);
             holder = new ViewHolder();
             //获取控件对象    
             holder.userLogo=(ImageView)convertView.findViewById(R.id.userLogo);
             holder.trueName=(TextView)convertView.findViewById(R.id.trueName);
             holder.cargoId=(TextView)convertView.findViewById(R.id.cargoId);
             holder.mobile=(TextView)convertView.findViewById(R.id.mobile);
             holder.email=(TextView)convertView.findViewById(R.id.email);
             holder.cargoTypeName=(TextView)convertView.findViewById(R.id.cargoTypeName);
             holder.cargoName=(TextView)convertView.findViewById(R.id.cargoName);

       
		 
	  }else {    
        	holder = (ViewHolder)convertView.getTag(); 
	  }
		    //设置控件集到convertView    
         convertView.setTag(holder); 
         mImageLoader.displayImage(ApplicationConstants.IMAGE_URL+listItems.get(position).get("logo"), holder.userLogo,MyshipAdapter.displayImageOptions);
         holder.trueName.setText((String) listItems.get(position).get("trueName"));  
         holder.cargoId.setText((String) listItems.get(position).get("cargoId"));  
         holder.mobile.setText((String) listItems.get(position).get("mobile"));  
         holder.email.setText((String) listItems.get(position).get("email"));  
         holder.cargoTypeName.setText((String) listItems.get(position).get("cargoTypeName"));  
         holder.cargoName.setText((String) listItems.get(position).get("cargoName"));  

		return convertView;
	}
	public  class ViewHolder{ 
		public ImageView userLogo;//用户头像
		public TextView trueName;  //真名
		public TextView cargoId;   //id
		public TextView mobile;    //电话
		public TextView email;     //邮件
		public TextView cargoTypeName; //货类
		public TextView cargoName; //货名

	}

}
