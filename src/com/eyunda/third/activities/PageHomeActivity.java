package com.eyunda.third.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.main.view.MenuHorizontalScrollView;
import com.eyunda.main.view.ObjectEntity;
import com.eyunda.main.view.SlidAdapter;
import com.eyunda.main.view.callback.SizeCallBackForMenu;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.advert.AdvertData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.hy.client.R;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ygl.android.ui.BaseActivity;

public class PageHomeActivity extends BaseActivity {
	protected MenuHorizontalScrollView scrollView;
	private LinearLayout menuList;
	protected View homeView;
	protected Button menuBtn, top_right_but;
	private View[] children;
	private LayoutInflater inflater;
	private List<ObjectEntity> dataSource;
	private List<View> itemViews;
	protected Data_loader data;
	protected Image_loader image_loader;
	// private TextView slid_text;
	private LinearLayout slid_lay;
	protected TextView login_type, login_name,slid_text,top_text;
	protected ImageView userHead;
	ViewPager viewPager;
	protected TextView favor_count;
	protected LinearLayout root;
	protected ImageView showpoint;

	public static boolean FLAG = false;// 坐标菜单滚动用到

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		dialogUtil = new DialogUtil(this);
		inflater = LayoutInflater.from(this);
		data = new Data_loader();
		image_loader = new Image_loader(this, (TAApplication) getApplication());
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		showpoint = (ImageView) findViewById(R.id.showpoint);

		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuList = (LinearLayout) findViewById(R.id.menuList);
		
		this.homeView = inflater.inflate(R.layout.part_home, null);
		root = (LinearLayout) this.homeView.findViewById(R.id.root);
		this.menuBtn = (Button) this.homeView.findViewById(R.id.top_left_but);
		this.menuBtn.setOnClickListener(onClickListener);
		top_right_but = (Button) this.homeView.findViewById(R.id.top_right_but);
		top_right_but.setVisibility(View.GONE);
		top_right_but.setOnClickListener(onClickListener);
		this.top_text = (TextView)this.homeView.findViewById(R.id.top_text);
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		// 左右布局
		children = new View[] { leftView, homeView };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
				this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		
//		// 切换广告图片
//		slid_text = (TextView) homeView.findViewById(R.id.slid_text);
//		slid_lay = (LinearLayout) homeView.findViewById(R.id.slid_lay);
//		switchTask.run();
//		// 初始化广告图片
//		initItems();
//		viewPager = (ViewPager) homeView.findViewById(R.id.mainViewPager);
//		SlidAdapter viewPageAdapter = new SlidAdapter(this, dataSource,
//				itemViews, (TAApplication) getApplication());
//
//		viewPager.setAdapter(viewPageAdapter);
//		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int arg0) {
//				slid_text.setText(dataSource.get(arg0).name);
//				refreshDotsLayout(arg0);
//
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//
//			}
//		});

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	// 顶部菜单点击
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.top_left_but)// 左边菜单
				scrollView.clickMenuBtn();
			else if (arg0.getId() == R.id.top_right_but)
				dialogUtil.showDialogFromConfig("提示", "暂未实现", new Handler() {

					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
					}
				});
		}
	};

	private void initItems() {
		dataSource = new ArrayList<ObjectEntity>();
		itemViews = new ArrayList<View>();
		
		final Map<String, Object> params = new HashMap<String, Object>();
		data.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ConvertData cd =new ConvertData(arg0,"/mobile/home/logoAdvert", params);
				if(cd.getReturnCode().endsWith("Success")){
					HashMap<String, Object> var = (HashMap<String, Object>) cd.getContent();
					List<HashMap<String, Object>> listDatas =(ArrayList<HashMap<String, Object>>)var.get("resDatas");//全部合同模板列表
					if(null != listDatas && listDatas.size() > 0){
						for (Map<String, Object> m : listDatas ) {
							//ShipData sd = new ShipData(m);
							AdvertData ad = new AdvertData(m);
							ObjectEntity objectEntity = new ObjectEntity();
							String logo = ad.getLogo();
		
							objectEntity.imgUrl = ApplicationConstants.IMAGE_URL+logo;

							objectEntity.cid = ad.getSid();
							//objectEntity.name = cargoData.getCargoName()+","+cargoData.getPeriodTime()+"从"+cargoData.getStartPortData().getFullName()+"到"+cargoData.getEndPortData().getFullName();
							objectEntity.name = ad.getTitle();
							objectEntity.ename = ad.getExt();
							objectEntity.adtype = ad.getAdtype().toString();
	
							dataSource.add(objectEntity);
	
						}
						if (dataSource.size() > 0) {
							//滚动点切换
							slid_text.setText(dataSource.get(0).name);
							refreshDotsLayout(0);
						}
	
						for (int i = 0; i < dataSource.size(); i++) {
	
							LayoutInflater inflater = getLayoutInflater();
							View view = inflater.inflate(R.layout.slid_item, null);
	
							itemViews.add(view);
						}
					}

				}else{
					
				}
				// {"id":1,"adtype":1,"pluginid":0,
				// "img":"http://localhost:9000/assets/ad/20140501221111322.png",
				// "value":"????","adorder":1}
				
			}

		}, "/mobile/home/logoAdvert", params, "get");

	}

	/**
	 * 刷新标签元素布局，每次currentItemIndex值改变的时候都应该进行刷新。
	 */
	private void refreshDotsLayout(int currentItemIndex) {

		slid_lay.removeAllViews();
		for (int i = 0; i < dataSource.size(); i++) {
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT);
			linearParams.weight = 1;
			RelativeLayout relativeLayout = new RelativeLayout(this);
			ImageView image = new ImageView(this);
			if (i == currentItemIndex) {
				image.setBackgroundResource(R.drawable.dot_selected);
			} else {
				image.setBackgroundResource(R.drawable.dot_unselected);
			}
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			relativeLayout.addView(image, relativeParams);
			slid_lay.addView(relativeLayout, linearParams);
		}
	}

	protected DialogUtil dialogUtil;

	@Override
	public boolean backKeyPressed() {
		if (scrollView.menuOut)
			scrollView.clickMenuBtn();
		else
			finish();
			//dialogUtil.showQuitDialogFromConfig("提示", "是否退出？");
		return true;
	}



	// 版本检查
	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String version = "";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	// 图片切换
	boolean cunhuan = false;
	int page_id = 1;
	private Runnable switchTask = new Runnable() {
		public void run() {
			if (cunhuan) {
				viewPager.setCurrentItem(page_id);
				page_id++;
				if (page_id >dataSource.size())
					page_id = 0;
				if(page_id == 0){
					if(dataSource.size() > 0 && dataSource.get(page_id) != null ){
						viewPager.setCurrentItem(page_id);
						slid_text.setText(dataSource.get(page_id).name);
					}
				}else{
					slid_text.setText(dataSource.get(page_id-1).name);
				}
			}
			cunhuan = true;
			mHandler.postDelayed(switchTask, 5000);
		}
	};

	Handler mHandler = new Handler();

}
