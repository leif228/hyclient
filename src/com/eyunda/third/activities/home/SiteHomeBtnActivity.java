package com.eyunda.third.activities.home;
import java.util.ArrayList;
import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.third.activities.home.AdvCargoSearchActivity;
import com.eyunda.third.activities.home.AdvShipSearchActivity;
import com.eyunda.third.activities.home.CargoFragment;
import com.eyunda.third.activities.home.ShipFragment;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.SearchRlsCode;
import com.eyunda.tools.PartEntity;
import com.hy.client.R;
import com.ygl.android.ui.BaseActivity;

public class SiteHomeBtnActivity extends BaseActivity{

	private int selectedColor, unSelectedColor;
	private TextView tv1,tv2;
	private EditText startPort;
	ShipFragment   mTab01;
	CargoFragment  mTab02;
	int plug_Interval = 1;//模块间间距
	PartEntity entity = null;
	private FragmentManager fragmentManager;
	private String TAG = "SiteHomeBtnActivity";
	int type = 1;//1船舶，2货物
	View homeView ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//      final float scale = getResources().getDisplayMetrics().density;
		//      plug_Interval = (int) (plug_Interval * scale);
		//      dialogUtil = new DialogUtil(this);
		//		entity = ListPlugUtil.setMainPageBoatList(this, data, image_loader, mainListHandler,1);
		//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//		params.topMargin = plug_Interval;
		//		root.addView(entity.getV(), params);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 1);
		initView();
		InitTextView();
		fragmentManager=getFragmentManager();
		setDefaultFragment(type);
		
	}
	private void setDefaultFragment(int type) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(type == 1){
			mTab01 = new ShipFragment();
			transaction.replace(R.id.id_content, mTab01);
		}else{
			mTab02 = new CargoFragment();
			transaction.replace(R.id.id_content, mTab02);
		}
		transaction.commit();
	}
	private void initView() {
		selectedColor = getResources().getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(R.color.tab_title_normal_color);

		homeView = LayoutInflater.from(this).inflate(R.layout.eyd_home_search_line, null);
		final EditText startPort = (EditText) homeView.findViewById(R.id.startPort);// 起始点
		ImageView search_btn = (ImageView) homeView.findViewById(R.id.search_btn);// 搜索按钮

		final Spinner spCate = (Spinner)homeView.findViewById(R.id.spCate);
		List<SpinnerItem> items = new ArrayList<SpinnerItem>();
		items.add(new SpinnerItem(SearchRlsCode.shipsearch.toString(), SearchRlsCode.shipsearch.getDescription()));
		items.add(new SpinnerItem(SearchRlsCode.cargosearch.toString(), SearchRlsCode.cargosearch.getDescription()));
		ArrayAdapter<SpinnerItem> spinnerAdapter = new ArrayAdapter<SpinnerItem>(SiteHomeBtnActivity.this,android.R.layout.simple_spinner_dropdown_item,items);
		spCate.setAdapter(spinnerAdapter);
		spCate.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {  


			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}

			@Override
			public void onItemSelected(AdapterView<?> adapter,View view,int position,long id) {
				if(1 == position){
					setTabSelection(1);
					startPort.setHint("货类/货名/起始港/到达港/托运人");
				}else{
					setTabSelection(0);
					startPort.setHint("船类/船名/MMSI/船东");
				}
			}  
        });  
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
						intent = new Intent(SiteHomeBtnActivity.this,AdvShipSearchActivity.class);
					}else{
						intent = new Intent(SiteHomeBtnActivity.this,AdvCargoSearchActivity.class);
					}
					intent.putExtra("key", key.trim());
					intent.putExtra("c", si.getId());
					startActivity(intent);
				}
			}
		});
		spCate.setVisibility(View.GONE);
	}
	private void InitTextView() {
		tv1 = (TextView)homeView.findViewById(R.id.tab_1);
		tv2 = (TextView)homeView.findViewById(R.id.tab_2);
		tv1.setVisibility(View.GONE);
		tv2.setVisibility(View.GONE);
//		tv1.setTextColor(selectedColor);
//		tv2.setTextColor(unSelectedColor);
//
//		tv1.setText("船舶");
//		tv2.setText("货物");
//
//		tv1.setOnClickListener(new MyOnClickListener(0));
//		tv2.setOnClickListener(new MyOnClickListener(1));
	}

	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {

			switch (index) {
			case 0:
				setTabSelection(0);
				break;
			case 1:
				setTabSelection(1);
				break;
			}
		}

	}
	private void setTabSelection(int index)
	{
		// 重置按钮
		resetBtn();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index){
		case 0:
			// 当点击了船舶tab时，改变控件的图片和文字颜色
			tv1.setTextColor(selectedColor);
			tv1.setVisibility(View.GONE);
			tv2.setVisibility(View.GONE);
			if (mTab01 == null) {
				// 如果ShipFragment为空，则创建一个并添加到界面上
				mTab01 = new ShipFragment();
				transaction.add(R.id.id_content, mTab01);
			} else {
				// 如果ShipFragment不为空，则直接将它显示出来
				transaction.show(mTab01);
			}
			break;
		case 1:
			// 当点击了货物tab时，改变控件的图片和文字颜色
			tv2.setTextColor(selectedColor);
			tv1.setVisibility(View.GONE);
			tv2.setVisibility(View.GONE);
			if (mTab02 == null) {
				// 如果CargoFragment为空，则创建一个并添加到界面上
				mTab02 = new CargoFragment();
				transaction.add(R.id.id_content, mTab02);
			} else {
				// 如果CargoFragment不为空，则直接将它显示出来
				transaction.show(mTab02);
			}
			break;

		}
		//transaction.addToBackStack(null);
		transaction.commit();
	}

	private void resetBtn() {
		tv1.setTextColor(unSelectedColor);
		tv2.setTextColor(unSelectedColor);

	}
	private void hideFragments(FragmentTransaction transaction) {
		if (mTab01 != null) {
			transaction.attach(mTab01);
			transaction.hide(mTab01);
		}
		if (mTab02 != null) {
			transaction.attach(mTab02);
			transaction.hide(mTab02);
		}

	}


}
