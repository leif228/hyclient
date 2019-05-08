package com.eyunda.third.adapters.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.third.activities.home.HomeCategoryActivity;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.EnumConst.BigAreaCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.tools.LocalFileUtil;
import com.hy.client.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeCateExAdapter extends BaseExpandableListAdapter {
	int type;
	Context mContext;
	LayoutInflater mInflater;
	private static final String G_TEXT = "g_text";
	private static final String C_TEXT1 = "c_text1";
	private static final String C_TEXT2 = "c_text2";
	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	public HomeCateExAdapter(HomeCategoryActivity homeCategoryActivity,
			int currentSelect) {
		mInflater = LayoutInflater.from(homeCategoryActivity);
		this.type=currentSelect;
		this.mContext =homeCategoryActivity;
		setData();
	}
	private void setData() {
		for(BigAreaCode e:BigAreaCode.values()){
			String bigArea =e.getDescription();
            Map<String, String> curGroupMap = new HashMap<String, String>();
                 curGroupMap.put(G_TEXT, bigArea);  //显示ExpandableListView的item
                 curGroupMap.put(C_TEXT2, e.getCode()); //隐藏设置id
                 groupData.add(curGroupMap);
                 
 	            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
 	            for (int j = 0; j < 2; j++) {
 	                Map<String, String> curChildMap = new HashMap<String, String>();
 	                children.add(curChildMap);
 	                curChildMap.put(C_TEXT1, "Child " + j);
 	            }
 	            childData.add(children);     
		}
		
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childData.get(groupPosition).get(childPosition).get(C_TEXT1).toString();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewChild = new ViewChild();
			convertView = mInflater.inflate(R.layout.channel_expandablelistview_item, null);
			mViewChild.gridView = (GridView) convertView.findViewById(R.id.channel_item_child_gridView);
			convertView.setTag(mViewChild);
		} else {
			mViewChild = (ViewChild) convertView.getTag();
		}
           
		HomeGridViewAdapter mSimpleAdapter = new HomeGridViewAdapter(mContext, setGridViewData(groupPosition),type);
		mViewChild.gridView.setAdapter(mSimpleAdapter);
		//setGridViewListener(mViewChild.gridView);
		mViewChild.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		//        hashMap.put(groupPosition + "", mViewChild.gridView);
		return convertView;
	}

	/**
	 * 设置gridview点击事件监听
	 * 
	 * @param gridView
	 */
	private void setGridViewListener(final GridView gridView) {
		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(view instanceof TextView){
					//如果想要获取到哪一行，则自定义gridview的adapter，item设置2个textview一个隐藏设置id，显示哪一行
					TextView tv = (TextView) view ;
					Toast.makeText(mContext, "position=" + position+"||"+tv.getText(), Toast.LENGTH_SHORT).show();
					Log.e("hefeng", "gridView listaner position=" + position + "||text="+tv.getText());
				}
			}
		});
	}


	/**
	 * 设置GridView数据
	 * @param groupPosition
	 * @return gridItem
	 */
	private ArrayList<HashMap<String, Object>> setGridViewData(int groupPosition) {
		ArrayList<HashMap<String, Object>> gridItem = new ArrayList<HashMap<String, Object>>();
		switch (type) {
		
			// gridItem =LocalFileUtil.getHomeCateSearchFile(mContext,groupPosition,groupData);
	
		 case HomeCategoryActivity.MSG_SHIP_LIST://船舶分类
			 gridItem= LocalFileUtil.getShipSortList(mContext,groupPosition,groupData);
			 break;
		 case HomeCategoryActivity.MSG_DYNAMIC_LIST: //船舶动态
		// case HomeCategoryActivity.MSG_SHIP_LINE://航线
				//gridItem =LocalFileUtil.getSailLineList(mContext,groupPosition,groupData);
				 String bigArea = groupData.get(groupPosition).get(C_TEXT2); //大区域
				 List<PortCityCode> areas = PortCityCode.getPortCities(BigAreaCode.getByCode(bigArea)); 
				 for(int i=0;i<areas.size();i++){
					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
	                   hashMap.put("code", bigArea+"_"+areas.get(i).getCode());
	                   hashMap.put("title", areas.get(i).getDescription());
	                   gridItem.add(hashMap);
				 }
				
				break;

		 case HomeCategoryActivity.MSG_DLR_LIST: //金牌代理			
		 case HomeCategoryActivity.MSG_CARGRO_AREA_LIST://货物按地区查询
			 //TODO 货运地区查询，点击某一个城市查询含该城市的货物运输信息
			 String bigAreas = groupData.get(groupPosition).get(C_TEXT2); 
			 List<PortCityCode> areas2 = PortCityCode.getPortCities(BigAreaCode.getByCode(bigAreas));
			 for(int i=0;i<areas2.size();i++){
			 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			     hashMap.put("code",  groupData.get(groupPosition).get(C_TEXT2)+"_"+areas2.get(i).getCode());
                 hashMap.put("title", areas2.get(i).getDescription());
                 gridItem.add(hashMap);
			 }
			 break;
		 case HomeCategoryActivity.MSG_CARGRO_CATE_LIST://货物类别
				
			 for (CargoTypeCode e : CargoTypeCode.getCargoTypeCodes()) {
				 HashMap<String, Object> hashMap = new HashMap<String, Object>();
				 hashMap.put("code", groupData.get(groupPosition).get(C_TEXT2)+"_"+e.name().toString()); //编码
				 hashMap.put("title",e.getDescription().contains("集装箱")? "集装箱" : e.getDescription().substring(4, e.getDescription().length()));   //item名
				 gridItem.add(hashMap);
			 }
			break;

			 

		}
		return gridItem;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(childData.size()>0){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupData.get(groupPosition).get(G_TEXT).toString();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			mViewChild = new ViewChild();
			convertView = mInflater.inflate(R.layout.channel_expandablelistview, null);
			mViewChild.textView = (TextView) convertView.findViewById(R.id.channel_group_name);
			mViewChild.imageView = (ImageView) convertView.findViewById(R.id.channel_imageview_orientation);
			convertView.setTag(mViewChild);
		} else {
			mViewChild = (ViewChild) convertView.getTag();
		}

		if (isExpanded) {
			mViewChild.imageView.setImageResource(R.drawable.channel_expandablelistview_top_icon);
		} else {
			mViewChild.imageView.setImageResource(R.drawable.channel_expandablelistview_bottom_icon);
		}
		mViewChild.textView.setText(getGroup(groupPosition).toString());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	ViewChild mViewChild;

	static class ViewChild {
		ImageView imageView;
		TextView textView;
		GridView gridView;
	}
}