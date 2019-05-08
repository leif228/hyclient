package com.eyunda.third.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.chat.event.OnlineStatusCode;
import com.eyunda.third.domain.account.OperatorData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.tools.CircularImage;
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

	public UserData getChild(int groupPosition, int childPosition) {
		return (UserData) treeNodes.get(groupPosition).childs.get(childPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return treeNodes.get(groupPosition).childs.size();
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder holder;
			if (convertView == null) {
				holder = new ChildViewHolder();
				convertView = LayoutInflater.from(parentContext).inflate( R.layout.eyd_new_chat_row_contact, null);
				
				holder.avatar = (CircularImage) convertView.findViewById(R.id.avatar);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.handler_mobile = (TextView) convertView.findViewById(R.id.handler_mobile);
				holder.signature = (TextView) convertView.findViewById(R.id.signature);
				
				convertView.setTag(holder);
			} else {
				holder = (ChildViewHolder) convertView.getTag();
			}
		
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + getChild(groupPosition, childPosition).getUserLogo(), holder.avatar,
					displayImageOptions);
			holder.name.setText(getChild(groupPosition, childPosition).getTrueName());
			holder.handler_mobile.setText(getChild(groupPosition, childPosition).getMobile());
			
			OnlineStatusCode onlineStatus = getChild(groupPosition, childPosition).getOnlineStatus();
			if (onlineStatus == OnlineStatusCode.online) {
				holder.signature.setText("[在线]");
				holder.avatar.setAlpha(255);
			} 
//			else if (onlineStatus == OnlineStatusCode.busy) {
//				holder.signature.setText("[忙碌]");
//				holder.avatar.setAlpha(255);
//			} else if (onlineStatus == OnlineStatusCode.idle) {
//				holder.signature.setText("[空闲]");
//				holder.avatar.setAlpha(255);
//			}
			else {
				holder.signature.setText("[离线]");
				holder.avatar.setAlpha(80);// 这里设置透明度，当用户不在线时
			}
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
			holder.down_arrow_img = (ImageView) convertView.findViewById(R.id.down_arrow_img);
			
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		
		mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + getGroup(groupPosition).getUserData().getUserLogo(), holder.boat_logo,
				displayImageOptions);
		holder.loginName.setText(getGroup(groupPosition).getUserData().getTrueName());//姓名
		holder.name.setText( getGroup(groupPosition).getUserData().getMobile());//电话
		holder.companyName.setText("公司名称："+(String) getGroup(groupPosition).getUserData().getUnitName());//公司
		holder.boat_id.setText(getGroup(groupPosition).getId().toString());//用户ID
		holder.daiLi.setText("代理船舶数："+getGroup(groupPosition).getShipNumber());//成交合同数：
		holder.cjht.setText("成交合同数："+getGroup(groupPosition).getOrderNumber());//成交合同数：
		
		holder.down_arrow_img.setOnClickListener(new lvButtonListener(groupPosition,holder));
		
		return convertView;
	}
	
	class GroupViewHolder {
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

	public OperatorData getGroup(int groupPosition) {
		return (OperatorData) treeNodes.get(groupPosition).parent;
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