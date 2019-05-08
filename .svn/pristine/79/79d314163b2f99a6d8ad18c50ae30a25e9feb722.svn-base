package com.hangyi.zd.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.tools.CircularImage;
import com.hangyi.zd.domain.MapPortData;
import com.hangyi.zd.domain.ShipGroupNodeData;
import com.hy.client.R;

public class ShipGroupExpandableListAdapter extends BaseExpandableListAdapter {
	
	ExpandableListView operator_list;

	static public class TreeNode {
		public Object parent;
		public List<Object> childs = new ArrayList<Object>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public ShipGroupExpandableListAdapter(Context view, ExpandableListView operator_list) {
		parentContext = view;
		this.operator_list = operator_list;
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

	public MapPortData getChild(int groupPosition, int childPosition) {
		return (MapPortData) treeNodes.get(groupPosition).childs.get(childPosition);
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
				holder.signature = (TextView) convertView.findViewById(R.id.signature);
				
				convertView.setTag(holder);
			} else {
				holder = (ChildViewHolder) convertView.getTag();
			}
		
			holder.name.setText(getChild(groupPosition, childPosition).getPort_name());
			return convertView;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if (convertView == null) {
			holder = new ChildViewHolder();
			convertView = LayoutInflater.from(parentContext).inflate( R.layout.eyd_new_chat_row_contact2, null);
			
			holder.loginName = (TextView) convertView.findViewById(R.id.loginName);
			holder.down_arrow_img = (ImageView) convertView.findViewById(R.id.down_arrow_img);
			
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		
		holder.loginName.setText( getGroup(groupPosition).getShipName());//电话
		holder.down_arrow_img.setOnClickListener(new lvButtonListener(groupPosition,holder));
		if(isExpanded){
			holder.down_arrow_img.setImageResource(R.drawable.categories_down_arrow);
		}else{
			holder.down_arrow_img.setImageResource(R.drawable.categories_up_arrow);
		}
		
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
		TextView loginName;
		TextView signature;
		ImageView down_arrow_img;
	}
	
	class lvButtonListener implements OnClickListener {
		private int position;
		private ChildViewHolder holder;
		public lvButtonListener(int pos, ChildViewHolder h) {
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
//					holder.down_arrow_img.setImageResource(R.drawable.categories_down_arrow);
				}else{
					operator_list.expandGroup(position);
//					holder.down_arrow_img.setImageResource(R.drawable.categories_up_arrow);
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

	public ShipGroupNodeData getGroup(int groupPosition) {
		return (ShipGroupNodeData) treeNodes.get(groupPosition).parent;
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