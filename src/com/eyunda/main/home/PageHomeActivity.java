package com.eyunda.main.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.hy.client.R;
import com.eyunda.main.Config;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.update.UpdateManager;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.main.view.MenuHorizontalScrollView;
import com.eyunda.main.view.ObjectEntity;
import com.eyunda.main.view.SlidAdapter;
import com.eyunda.main.view.callback.SizeCallBackForMenu;
import com.eyunda.part1.data.PartData_loader;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ygl.android.ui.BaseActivity;

public class PageHomeActivity extends BaseActivity {
	private MenuHorizontalScrollView scrollView;
	private LinearLayout menuList;
	protected View homeView;
	private Button menuBtn, top_right_but;
	private View[] children;
	private LayoutInflater inflater;
	private List<ObjectEntity> dataSource;
	private List<View> itemViews;
	protected PartData_loader data;
	protected Image_loader image_loader;
	// private TextView slid_text;
	private LinearLayout slid_lay;
	protected TextView login_type, login_name;
	protected ImageView userHead;
	ViewPager viewPager;
	protected TextView favor_count;
	protected LinearLayout root;
	protected ImageView showpoint,home_qa_point;
	public static boolean FLAG = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		dialogUtil = new DialogUtil(this);
		inflater = LayoutInflater.from(this);
		data = new PartData_loader();
		image_loader = new Image_loader(this, (TAApplication) getApplication());
		setContentView(inflater.inflate(R.layout.menu_scroll_view, null));
		showpoint = (ImageView) findViewById(R.id.showpoint);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuList = (LinearLayout) findViewById(R.id.menuList);

		this.homeView = inflater.inflate(R.layout.part_home, null);
		root = (LinearLayout) this.homeView.findViewById(R.id.root);
		this.menuBtn = (Button) this.homeView.findViewById(R.id.top_left_but);
		this.menuBtn.setOnClickListener(onClickListener);
		top_right_but = (Button) this.homeView.findViewById(R.id.top_right_but);
		top_right_but.setOnClickListener(onClickListener);
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		children = new View[] { leftView, homeView };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
				this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);

		try {
			String myString = "2114-09-15";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			Date d = sdf.parse(myString);
			boolean flag = d.before(new Date());
			
			if(flag){
				Toast.makeText(PageHomeActivity.this, "error:5001", Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
					
					new	DialogUtil(PageHomeActivity.this).sysExit();
					}
				},3000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		scrollView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					flag = 0;
//					Down_t = System.currentTimeMillis();
//					Down_x = event.getX();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					if (Math.abs(event.getX() - Down_x) > diff
//							|| (System.currentTimeMillis() - Down_t) > timeout) {
//						flag = 1;
//					}
//					break;
//				case MotionEvent.ACTION_UP:
//					flag = -1;
//					break;
//				case MotionEvent.ACTION_CANCEL:
//					if (flag == 0 || flag == 1) {
//						flag = -1;
//					}
//					break;
//				}
//				return false;
//			}
//
//		});
		switchTask.run();
		// slid_text = (TextView) homeView.findViewById(R.id.slid_text);
		slid_lay = (LinearLayout) homeView.findViewById(R.id.slid_lay);
		initItems();
		viewPager = (ViewPager) homeView.findViewById(R.id.mainViewPager);
		SlidAdapter viewPageAdapter = new SlidAdapter(this, dataSource,
				itemViews, (TAApplication) getApplication());

		viewPager.setAdapter(viewPageAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// slid_text.setText(dataSource.get(arg0).name);
				refreshDotsLayout(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		getVersion();
		if(!Config.USERID.equals(""))
		data.mobileUsergetNotify(new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String arg0) {
				if(arg0.equals("0")){
					
					home_qa_point.setVisibility(View.GONE);
				}else home_qa_point.setVisibility(View.VISIBLE);
				 
			}
		}, Config.USERID);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.top_left_but)
				scrollView.clickMenuBtn();
			else if (arg0.getId() == R.id.top_right_but)
				startActivity(new Intent(PageHomeActivity.this,
						CaptureActivity.class));
		}
	};

	private void initItems() {
		dataSource = new ArrayList<ObjectEntity>();
		itemViews = new ArrayList<View>();
		// for (int i = 0; i < 2; i++) {
		// ObjectEntity objectEntity = new ObjectEntity();
		// objectEntity.imgUrl = "http://www.hbksw.com/images/college/12_4.jpg";
		// objectEntity.name = "湖北大学";
		// dataSource.add(objectEntity);
		//
		// objectEntity = new ObjectEntity();
		// objectEntity.imgUrl = "http://www.hbksw.com/images/college/3_1.jpg";
		// objectEntity.name = "华中科技大学";
		// dataSource.add(objectEntity);
		// }

		data.mobileAdService(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);

				List<Map<String, String>> list = DataConvert.toArrayList(arg0);
				// {"id":1,"adtype":1,"pluginid":0,
				// "img":"http://localhost:9000/assets/ad/20140501221111322.png",
				// "value":"????","adorder":1}
				for (Map<String, String> m : list) {

					ObjectEntity objectEntity = new ObjectEntity();
					objectEntity.imgUrl = m.get("img");
					objectEntity.name = m.get("value");
					objectEntity.adtype = m.get("adtype");

					dataSource.add(objectEntity);

				}
				if (dataSource.size() > 0) {
					// slid_text.setText(dataSource.get(0).name);
					refreshDotsLayout(0);
				}

				for (int i = 0; i < dataSource.size(); i++) {

					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.slid_item, null);

					itemViews.add(view);
				}

			}

		}, "0");

	}

	/**
	 * 刷新标签元素布局，每次currentItemIndex值改变的时候都应该进行刷新。
	 */
	private void refreshDotsLayout(int currentItemIndex) {

		slid_lay.removeAllViews();
		for (int i = 0; i < dataSource.size(); i++) {
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
					0, LayoutParams.FILL_PARENT);
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
			dialogUtil.showQuitDialogFromConfig("提示", "是否退出？");
		return true;
	}

	private void getVersion() {

		data.getVersiom(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);

				// {"id":1,"appType":"android","version":"1.0","releaseDate":"2014-04-27 15:29:29",
				// "releaseNote":"湖北考试网Android手机客户端V1.0",
				// "url":"http://115.28.227.82:9000/assets/apps/android/eyunda-1.0.apk"}

				Map<String, String> tm = DataConvert.toMap(arg0);
				if (getVersionName().equals(tm.get("version"))) {

				} else {
					UpdateManager mUpdateManager = new UpdateManager(
							PageHomeActivity.this, tm.get("url"), tm
									.get("releaseNote"),"");
					mUpdateManager.checkUpdateInfo();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(PageHomeActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();

			}
		});
	}

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

	boolean cunhuan = false;
	int page_id = 1;
	private Runnable switchTask = new Runnable() {
		public void run() {
			if (cunhuan) {
				viewPager.setCurrentItem(page_id);
				page_id++;
				if (page_id >= dataSource.size())
					page_id = 0;
			}
			cunhuan = true;
			mHandler.postDelayed(switchTask, 5000);
		}
	};

	Handler mHandler = new Handler();

}
