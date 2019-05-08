package com.eyunda.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.part1.plug.ViewContents;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.home.AdvCargoSearchActivity;
import com.eyunda.third.activities.home.AdvShipSearchActivity;
import com.eyunda.third.activities.home.HomeCategoryActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.home.HomeCateExAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.SearchRlsCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.ShipPriceData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.eyunda.tools.log.FilePathGenerator;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ListPlugUtil {

	private static LinearLayout yxLinearLayout;

	private static ViewContents boatList;//船舶列表


	/**
	 * 设置首页推荐船舶模块
	 * 
	 */
	public static PartEntity setMainPageBoatList(final Context context,
			Data_loader data, final Image_loader image_loader,
			final Handler handler, int current) {
		PartEntity returnEntity = new PartEntity();
		if (yxLinearLayout == null) {
			returnEntity.setNeedload(true);
			//搜索框
			yxLinearLayout = (LinearLayout) View.inflate(context,R.layout.eyd_home_search_line, null);
		} else{
			returnEntity.setNeedload(false);
		}
		returnEntity.setV(yxLinearLayout);

		final EditText startPort = (EditText) yxLinearLayout.findViewById(R.id.startPort);// 起始点
		//TextView endPort = (TextView) yxLinearLayout.findViewById(R.id.endPort);// 终止点
		ImageView search_btn = (ImageView) yxLinearLayout.findViewById(R.id.search_btn);// 搜索按钮
		//startPort.setText("起始港:");

		final Spinner spCate = (Spinner)yxLinearLayout.findViewById(R.id.spCate);
		List<SpinnerItem> items = new ArrayList<SpinnerItem>();
		items.add(new SpinnerItem(SearchRlsCode.shipsearch.toString(), "搜索"+SearchRlsCode.shipsearch.getDescription()));
		items.add(new SpinnerItem(SearchRlsCode.cargosearch.toString(), "搜索"+SearchRlsCode.cargosearch.getDescription()));
		ArrayAdapter<SpinnerItem> spinnerAdapter = new ArrayAdapter<SpinnerItem>(context,android.R.layout.simple_spinner_dropdown_item,items);
		spCate.setAdapter(spinnerAdapter);

		search_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String key = startPort.getText().toString();
				if(key.trim().equals("")){
					Toast.makeText(context, "请输入搜索词", Toast.LENGTH_SHORT).show();
				}else{
					SpinnerItem si = (SpinnerItem)spCate.getSelectedItem();
					Intent intent = null;
					if(si.getId().equalsIgnoreCase(SearchRlsCode.shipsearch.name())){
						intent = new Intent(context,AdvShipSearchActivity.class);
					}else{
						intent = new Intent(context,AdvCargoSearchActivity.class);
					}
					intent.putExtra("key", key.trim());
					intent.putExtra("c", si.getId());
					context.startActivity(intent);
				}
			}
		});
		

		return returnEntity;

	}
	 public static void setListViewHeightBasedOnChildren(ListView listView) {  
	        ListAdapter listAdapter = listView.getAdapter();   
	        if (listAdapter == null) {  
	            // pre-condition  
	            return;  
	        }  
	  
	        int totalHeight = 0;  
	        for (int i = 0; i < listAdapter.getCount(); i++) {  
	            View listItem = listAdapter.getView(i, null, listView);  
	            listItem.measure(0, 0);  
	            totalHeight += listItem.getMeasuredHeight();  
	        }  
	  
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	        listView.setLayoutParams(params);  
	    }  
}
