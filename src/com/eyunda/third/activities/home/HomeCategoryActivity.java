package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.order.AddOrderTYRActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.pay.PaymentActivity;
import com.eyunda.third.adapters.home.HomeCateExAdapter;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.LocalFileUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 分类搜索
 * 
 * @author
 *
 */
public class HomeCategoryActivity extends CommonListActivity implements
		OnClickListener, OnItemClickListener {
	Data_loader data;

	protected ListView listView;

	private RelativeLayout mainLayout;
	public static final int MSG_DLR_LIST =  1; //金牌代理
	public static final int MSG_SHIP_LIST =  2; //船舶分类
	public static final int MSG_SHIP_LINE =  3; //船舶航线
	public static final int MSG_CARGRO_CATE_LIST =  4; //货物类别
	public static final int MSG_CARGRO_AREA_LIST  =  5; //货物按地区查询
	public static final int MSG_DYNAMIC_LIST  =  6; //动态
	
	ExpandableListView mExpandableListView;
    HomeCateExAdapter mAdapter;
    Map<String,ArrayList<Map<String,Object>>> expandList;

    int currentSelect;


	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mainLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.eyd_category_search, null);
		setContentView(mainLayout);
		data = new Data_loader();
		
		
        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        //根据点击的按钮决定从哪个接口取数据
        Intent intent = getIntent();
        int num =intent.getIntExtra("type", -1);
        switch (num) {
        case MSG_SHIP_LIST://船舶分类
        	currentSelect = MSG_SHIP_LIST;

        	break;
		case MSG_DLR_LIST:
	        currentSelect = MSG_DLR_LIST;//金牌代理
	      
			break;
		//case MSG_SHIP_LINE://船舶航线
		case MSG_DYNAMIC_LIST://动态
			 //  currentSelect = MSG_SHIP_LINE;
			   currentSelect=MSG_DYNAMIC_LIST;
			  
			break;

		case MSG_CARGRO_CATE_LIST://货物类别
			    currentSelect =MSG_CARGRO_CATE_LIST;
			break;
		case MSG_CARGRO_AREA_LIST://货物按地区查询
			 currentSelect =MSG_CARGRO_AREA_LIST;
			break;
		}
       
        mAdapter = new HomeCateExAdapter(this,currentSelect);
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.expandGroup(0);

//      openAll() ;
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Log.e("hefeng", "ExpandableListView GroupClickListener groupPosition=" + groupPosition);
                return false;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Log.e("hefeng", "ExpandableListView ChildClickListener groupPosition=" + groupPosition + "||childPosition=" + childPosition);
            	return false;
            }
        });
        mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {  
            @Override  
            public void onGroupExpand(int groupPosition) {  
                for (int i = 0, count = mExpandableListView  
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {  
                    if (groupPosition != i) {// 关闭其他分组  
                    	mExpandableListView.collapseGroup(i);  
                    }  
                }  
            }  
        });
     
    }

    private void openAll() {
        int groupCount = mExpandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            mExpandableListView.expandGroup(i);
        }
    }


	@Override
	protected void onStart() {
		super.onStart();
		setTitle("选择分类");

	}

	@Override
	protected synchronized void loadDate() {

	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

}
