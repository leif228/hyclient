package com.hangyi.zd.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.third.GlobalApplication;
import com.eyunda.tools.CircularImage;
import com.hangyi.zd.domain.BaiduWeatherData;
import com.hangyi.zd.domain.HcNodeChildData;
import com.hangyi.zd.domain.HcNodeData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressWarnings({"rawtypes"})
public class TreeViewExpandableListAdapter extends BaseExpandableListAdapter {
	
	ImageLoader mImageLoader;
	ExpandableListView operator_list;
	
	DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.eyd_chat_ic_cycle)
	.showImageForEmptyUri(R.drawable.eyd_chat_search_clear_pressed).showImageOnFail(R.drawable.eyd_chat_default_avatar)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisc(true) // 缓存到sd卡
	.build();

	static public class TreeNode {
		public Object parent;
		public List<Object> childs = new ArrayList<Object>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public TreeViewExpandableListAdapter(Context view, ExpandableListView operator_list) {
		parentContext = view;
		this.operator_list = operator_list;
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());
	}

	public List<TreeNode> GetTreeNode() {
		return treeNodes;
	}

	public void UpdateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

	public void RemoveAll() {
		treeNodes.clear();
	}

	public HcNodeChildData getChild(int groupPosition, int childPosition) {
		return (HcNodeChildData) treeNodes.get(groupPosition).childs.get(childPosition);
	}

	public HcNodeData getGroup(int groupPosition) {
		return (HcNodeData) treeNodes.get(groupPosition).parent;
	}
	
	public int getChildrenCount(int groupPosition) {
		return treeNodes.get(groupPosition).childs.size();
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder holder;
			if (convertView == null) {
				holder = new ChildViewHolder();
				convertView = LayoutInflater.from(parentContext).inflate( R.layout.zd_new_chat_row_contact, null);
				
				holder.avatar = (CircularImage) convertView.findViewById(R.id.avatar);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.handler_mobile = (TextView) convertView.findViewById(R.id.handler_mobile);
				holder.signature = (TextView) convertView.findViewById(R.id.signature);
				holder.tq = (ImageView) convertView.findViewById(R.id.tq);
				holder.tqdis = (TextView) convertView.findViewById(R.id.tqdis);
				
				convertView.setTag(holder);
			} else {
				holder = (ChildViewHolder) convertView.getTag();
			}
			HcNodeChildData cd = getChild(groupPosition, childPosition);
			if(null != cd.getWeather()){
				if(disWeatherImg2(cd.getWeather())!=null)
					holder.tq.setImageBitmap(disWeatherImg2(cd.getWeather()));;
//				holder.tq.setText(disWeatherImg(cd.getWeather()));
				
				holder.tqdis.setText(cd.getWeather().getWeather());
			}else{
//				holder.tq.setText(""); 
				holder.tqdis.setText("");
			}
			
			holder.name.setText(cd.getPolice());
			return convertView;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder;
		if (convertView == null) {
			holder = new GroupViewHolder();
			convertView = LayoutInflater.from(parentContext).inflate( R.layout.eyd_new_operatorsearch_one_item, null);
			
			holder.boat_logo = (ImageView) convertView.findViewById(R.id.boat_logo);
			holder.loginName = (TextView) convertView.findViewById(R.id.loginName);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.companyName = (TextView) convertView.findViewById(R.id.companyName);
			holder.boat_id = (TextView) convertView.findViewById(R.id.boat_id);
			holder.daiLi = (TextView) convertView.findViewById(R.id.daiLi);
			holder.cjht = (TextView) convertView.findViewById(R.id.cjht);
			holder.tq = (ImageView) convertView.findViewById(R.id.tq);
			holder.tqdis = (TextView) convertView.findViewById(R.id.tqdis);
			holder.down_arrow_img = (ImageView) convertView.findViewById(R.id.down_arrow_img);
			
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		
		HcNodeData d = getGroup(groupPosition);
		
		if(null != d.getWeather()){
			if(disWeatherImg2(d.getWeather())!=null)
			holder.tq.setImageBitmap(disWeatherImg2(d.getWeather()));; 
			
			holder.tqdis.setText(d.getWeather().getWeather());
		}else{
//			holder.tq.setText(""); 
			holder.tqdis.setText("");
		}
		
		holder.loginName.setText(d.getStatus().getDescription());//姓名
		holder.name.setText(d.getOpTime());//姓名
		
		holder.down_arrow_img.setOnClickListener(new lvButtonListener(groupPosition,holder));
		
		return convertView;
	}
	private Bitmap disWeatherImg2(BaiduWeatherData bd){
//		String picName = "tq_26";
//		Resources res=parentContext.getResources();  
//		int i=res.getIdentifier(picName,"drawable",parentContext.getPackageName());  
//		Bitmap b = BitmapFactory.decodeStream(is);  
		
		Bitmap b = getImageFromAssetsFile("img/"+bd.getaPI());
		return b;
	}
	private SpannableString disWeatherImg(BaiduWeatherData bd){
//		String picName = "tq_26";
//		Resources res=parentContext.getResources();  
//		int i=res.getIdentifier(picName,"drawable",parentContext.getPackageName());  
//		Bitmap b = BitmapFactory.decodeStream(is);  
		
		Bitmap b = getImageFromAssetsFile("img/"+bd.getaPI());
		if(b == null)
			return new SpannableString("");
		
		ImageSpan imgSpan = new ImageSpan(parentContext,b);  
		SpannableString spanString = new SpannableString("icon");  
		spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		
		return spanString;
	}

	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = parentContext.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	
	class GroupViewHolder {
		public TextView tqdis;
//		public TextView tq;
		public ImageView tq;
		ImageView boat_logo;
		TextView loginName;
		TextView name;
		TextView companyName;
		TextView boat_id;
		TextView cjht;
		TextView daiLi;
		ImageView down_arrow_img;
	}
	class ChildViewHolder {
		public TextView tqdis;
		public ImageView tq;
//		public TextView tq;
		CircularImage avatar;
		TextView name;
		TextView handler_mobile;
		TextView signature;
	}
	
	class lvButtonListener implements OnClickListener {
		private int position;
		private GroupViewHolder holder;
		public lvButtonListener(int pos, GroupViewHolder h) {
			position = pos;
			holder = h;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.down_arrow_img:
				boolean expanded = operator_list.isGroupExpanded(position); 
				if(expanded){
					operator_list.collapseGroup(position);
					holder.down_arrow_img.setImageResource(R.drawable.categories_down_arrow);
				}else{
					operator_list.expandGroup(position);
					holder.down_arrow_img.setImageResource(R.drawable.categories_up_arrow);
				}
				break;
			}
		}
	}
	public void onGroupCollapsed(int groupPosition){
		
	} 
	public void onGroupExpanded(int groupPosition){
		
	} 

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getGroupCount() {
		return treeNodes.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}
}