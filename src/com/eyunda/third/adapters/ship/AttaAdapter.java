package com.eyunda.third.adapters.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.ship.UploadActivity;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AttaAdapter extends BaseAdapter{

	private Activity context;                        //运行上下文    
	private List<Map<String, Object>> listItems;    //信息集合    
	private LayoutInflater listContainer; 
	private ImageLoader mImageLoader;
	private Data_loader data;
	private ArrayList<Map<String,Object>> mInfos; 
	private DialogUtil dialogUtil;
	private ProgressDialog dialog;
	public  class ViewHolder{        //自定义控件集合      
		public TextView  id;
		public ImageView img;      
		public TextView name;      
		public ImageView delete; 

	}      

	public AttaAdapter(Activity context,ArrayList<Map<String, Object>> listItems) {
		this.context = context;             
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文    
		this.listItems = listItems;  
		mImageLoader = ImageLoader.getInstance();
		data = new Data_loader();
		dialogUtil = new DialogUtil(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder  holder = null;    
		if (convertView == null) {    
			holder = new ViewHolder();     
			//获取list_item布局文件的视图    
			convertView = listContainer.inflate(R.layout.eyd_upload_item, null);    
			//获取控件对象    
			holder.id = (TextView)convertView.findViewById(R.id.shipId);
			holder.img = (ImageView)convertView.findViewById(R.id.ItemImage);    
			holder.name = (TextView)convertView.findViewById(R.id.ItemText);    
			holder.delete =(ImageView)convertView.findViewById(R.id.delete_item);
			String content = listItems.get(position).get("img").toString();
			if(content.length()==0){
				holder.img.setVisibility(View.GONE);
				holder.name.setGravity(Gravity.LEFT);
			}
			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(context).setTitle("删除图文").setMessage("确定删除吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							showInfo(position);
						}
					})
					.setNegativeButton("取消", null)
					.show();
				}
			});
			//设置控件集到convertView    
			convertView.setTag(holder);   

			//设置图文显示
			//   holder.img.setBackgroundResource((Integer) listItems.get(position).get("img"));
			
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +listItems.get(position).get("img").toString(),
					holder.img,MyshipAdapter.displayImageOptions);
			holder.name.setText((String) listItems.get(position).get("describtion"));  
			holder.id.setText((String) listItems.get(position).get("id"));  
		}else {    
			holder = (ViewHolder)convertView.getTag();    
		}  
		return convertView; 
	} 
	// listview中点击按键弹出对话框  
	public void showInfo(final int position) {  
		AsyncHttpResponseHandler  handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "请求数据中...");
			}

			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> result= gson.fromJson((String) arg0,
						new TypeToken<Map<String, Object>>() {}.getType());
				if (result.get("returnCode").equals("Success")) {
					//异步后台删除item						
					Toast.makeText(context,
							(CharSequence) result.get("message"), Toast.LENGTH_SHORT).show();
					context.finish();
					Intent intent =new Intent(context,UploadActivity.class);
					intent.putExtra("shipId",  listItems.get(position).get("shipId").toString());
					context.startActivity(intent);
				}

			} 
			@Override
			public void onFailure(Throwable arg0,String content) {
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")){
					Toast.makeText(context, "网络连接异常",
							Toast.LENGTH_LONG).show();
				}	else if (content != null && content.equals("socket time out")) {
					Toast.makeText(context, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else{
					Toast.makeText(context, "删除失败",
							Toast.LENGTH_LONG).show();
				}
			}
		};

		HashMap<String, Object> map = new HashMap<String, Object>();
		String shipId =listItems.get(position).get("id").toString();
		map.put("attaId",shipId );
		data.getApiResult(handler ,"/mobile/ship/myShip/removeAtta",map,"get");


	} 
}
