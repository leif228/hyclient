package com.eyunda.third.activities.viewPage;

import java.util.ArrayList;
import java.util.List;









import com.hy.client.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
/**
 * ViewPager 引导
 */
public class ViewPagerActivity extends FragmentActivity {
	private ViewPager mVPActivity;
	private Fragment1 mFragment1;
	private Fragment2 mFragment2;
	private Fragment3 mFragment3;
	private Fragment4 mFragment4;
	private Fragment5 mFragment5;
	private List<Fragment> mListFragment = new ArrayList<Fragment>();
	private PagerAdapter mPgAdapter;
	private RadioGroup dotLayout;
	private ViewPager viewPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);
		initView();
	}

	private void initView() {
		dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
		viewPage = (ViewPager) findViewById(R.id.viewpager);
		mFragment1 = new Fragment1();
		mFragment2 = new Fragment2();
		mFragment3 = new Fragment3();
		mFragment4 = new Fragment4();
		mFragment5 = new Fragment5();
		mListFragment.add(mFragment1);
		mListFragment.add(mFragment2);
		mListFragment.add(mFragment3);
		mListFragment.add(mFragment4);
		mListFragment.add(mFragment5);
		mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				mListFragment);
		viewPage.setAdapter(mPgAdapter);
		viewPage.setOnPageChangeListener(new MyPagerChangeListener());
	}
	public class MyPagerChangeListener implements OnPageChangeListener {

		public void onPageSelected(int position) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
		}

	}
}
