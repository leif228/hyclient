package com.eyunda.third.activities;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.AddCargoActivity;
import com.eyunda.third.activities.home.AdvCargoSearchActivity;
import com.eyunda.third.activities.home.AdvShipSearchActivity;
import com.eyunda.third.activities.home.CargoFragment;
import com.eyunda.third.activities.home.ShipFragment;
import com.eyunda.third.activities.ship.ShipinfoActivity;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.SearchRlsCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.tools.PartEntity;
import com.hy.client.R;

public class SiteHomeActivity extends PageHomeActivity{

	private int selectedColor, unSelectedColor;
	private TextView tv1,tv2;
	private EditText startPort;
	ShipFragment   mTab01;
	CargoFragment  mTab02;
	int plug_Interval = 1;//模块间间距
	PartEntity entity = null;
	private FragmentManager fragmentManager;
	private String TAG = "SiteHomeActivity";
	int type = 1;//1船舶，2货物
	UserData user;
	List<SpinnerItem> areaItems;
	ArrayAdapter<SpinnerItem> spinnerAdapter;
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
		
		loadAreaAndSetSpinnerAdapter();
		
	}
	private void setDefaultFragment(int type) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(type == 1){
			mTab01 = new ShipFragment();
			transaction.replace(R.id.id_content, mTab01);
			startPort.setHint("船类/船名/MMSI/船东");
		}else{
			mTab02 = new CargoFragment();
			transaction.replace(R.id.id_content, mTab02);
			startPort.setHint("货类/货名/起始港/到达港/托运人");
		}
		transaction.commit();
	}
	private void initView() {
		selectedColor = getResources().getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(R.color.tab_title_normal_color);
		startPort = (EditText)findViewById(R.id.startPort); 
		ImageView search_btn = (ImageView) homeView.findViewById(R.id.search_btn);// 搜索按钮

		final Spinner spCate = (Spinner)homeView.findViewById(R.id.spCate);
		areaItems = new ArrayList<SpinnerItem>();
		
		spinnerAdapter = new ArrayAdapter<SpinnerItem>(SiteHomeActivity.this,android.R.layout.simple_spinner_dropdown_item,areaItems);
		spCate.setAdapter(spinnerAdapter);
		spCate.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}  
        });  
		


		search_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String key = startPort.getText().toString();
				if(key.trim().equals("")){
					
					Toast.makeText(context, "请输入搜索词", Toast.LENGTH_SHORT).show();
				}else{

					Intent intent = null;
					String si = SearchRlsCode.shipsearch.toString();
					if(type == 1){
						intent = new Intent(SiteHomeActivity.this,AdvShipSearchActivity.class);
						si = SearchRlsCode.shipsearch.toString();
					}else{
						intent = new Intent(SiteHomeActivity.this,AdvCargoSearchActivity.class);
						si = SearchRlsCode.cargosearch.toString();
					}
					intent.putExtra("area", ((SpinnerItem)spCate.getSelectedItem()).getId());
					intent.putExtra("key", key.trim());
					intent.putExtra("c", si);
					startActivity(intent);
				}
			}
		});
		menuBtn.setBackgroundResource(R.drawable.base_back);
		menuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});

		if(type == 1){
			top_text.setText("船盘");
		}else{
			top_text.setText("货盘");
		}
		if(GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
			user = GlobalApplication.getInstance().getUserData();
			if(user!=null){
				if(type==1){
					top_right_but.setBackgroundResource(R.drawable.zx_faver_top);
					top_right_but.setVisibility(View.VISIBLE);
				}else{
					if(user.isRealUser()){
						top_right_but.setBackgroundResource(R.drawable.zx_faver_top);
						top_right_but.setVisibility(View.VISIBLE);
					}
					
				}
			}
		}
		top_right_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if(type == 1){
					intent.setClass(SiteHomeActivity.this, ShipinfoActivity.class);
					intent.putExtra("shipId", "0");
				}else{
					intent.setClass(SiteHomeActivity.this,  AddCargoActivity.class);
					intent.putExtra("type", 3L);
					intent.putExtra("cargoId", 0L);
				}
				startActivity(intent);
			}
		});
		
	}
	
	private void loadAreaAndSetSpinnerAdapter() {
		areaItems.add(new SpinnerItem("", "所在区域..."));
		for (PortCityCode e : PortCityCode.values()) {
			SpinnerItem sp = new SpinnerItem(e.name(), e.getFullName());
			areaItems.add(sp);
		}
		spinnerAdapter.notifyDataSetChanged();
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
