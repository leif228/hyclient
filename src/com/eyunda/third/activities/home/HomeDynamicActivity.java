package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.main.CommonActivity;
import com.hy.client.R;


public class HomeDynamicActivity extends FragmentActivity {
	private int selectedColor, unSelectedColor;
	private ViewPager viewPager;// 页卡内容
	private ArrayList<Fragment> fragments;// Tab页面列表
	private TextView tab1, tab2;// 选项名称
	private ImageView imageView;// 动画图片
	private int bmpW;// 动画图片宽度
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号

	private TextView tab3;
	/** 页卡总数 **/
	private static final int pageSize = 3;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.item_image_bucket);
		initView();
	}
	private void initView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);

        InitTop();
		InitImageView();
		InitTextView();
		InitViewPager();
	}
	private void InitTop() {
		Bundle	bundle = new Bundle();
		bundle = getIntent().getExtras();
     		Button btnTop = (Button)findViewById(R.id.top_back);
     		TextView tv = (TextView)findViewById(R.id.top_text);
     		tv.setText(bundle.getString("title"));
     		btnTop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
	}
	
	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();
		ArriveFragment  one = new ArriveFragment(); //已经到
		Left1Fragmnet  two = new Left1Fragmnet();  //将要到
		LeftFragment three = new LeftFragment(); //刚离开
		//传送参数
		Bundle	bundle = new Bundle();
		bundle = getIntent().getExtras();
		one.setArguments(bundle);
		two.setArguments(bundle);
		three.setArguments(bundle);
		fragments.add(one);
		fragments.add(two);
		fragments.add(three);
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOffscreenPageLimit(3); //一次性加载完三个页面，不需要再重复加载
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		tab1 = (TextView) findViewById(R.id.tab_1);
		tab2 = (TextView) findViewById(R.id.tab_2);
		tab3 = (TextView) findViewById(R.id.tab_3);

		tab1.setTextColor(selectedColor);
		tab2.setTextColor(unSelectedColor);
		tab3.setTextColor(unSelectedColor);

		tab1.setText("已经到");
		tab2.setText("将要到");
		tab3.setText("刚离开");

		tab1.setOnClickListener(new MyOnClickListener(0));
		tab2.setOnClickListener(new MyOnClickListener(1));
		tab3.setOnClickListener(new MyOnClickListener(2));
	}
	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */

	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
		// = 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {

			switch (index) {
			case 0:
				tab1.setTextColor(selectedColor);
				tab2.setTextColor(unSelectedColor);
				tab3.setTextColor(unSelectedColor);
				break;
			case 1:
				tab2.setTextColor(selectedColor);
				tab1.setTextColor(unSelectedColor);
				tab3.setTextColor(unSelectedColor);
				break;
			case 2:
				tab3.setTextColor(selectedColor);
				tab1.setTextColor(unSelectedColor);
				tab2.setTextColor(unSelectedColor);
				break;
			}
			viewPager.setCurrentItem(index);
		}

	}

	/**
	 * 为选项卡绑定监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* index, 0, 0);
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

			switch (index) {
			case 0:
				tab1.setTextColor(selectedColor);
				tab2.setTextColor(unSelectedColor);
				tab3.setTextColor(unSelectedColor);
				break;
			case 1:
				tab2.setTextColor(selectedColor);
				tab3.setTextColor(unSelectedColor);
				tab1.setTextColor(unSelectedColor);
				break;
			case 2:
				tab3.setTextColor(selectedColor);
				tab1.setTextColor(unSelectedColor);
				tab2.setTextColor(unSelectedColor);
				break;

			}
		}
	}

	/**
	 * 定义适配器
	 */
	class myPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentList;

		public myPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
			super(fragmentManager);
			this.fragmentList = fragmentList;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}

}
