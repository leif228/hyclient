package com.eyunda.third.activities.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ContactManager;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.MassNotify;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class NewContactListActivity extends FragmentActivity implements
		OnClickListener {

	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView dlr, tyr;// 选项名称
	private Fragment[] fragments;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int selectedColor, unSelectedColor;
	/** 页卡总数 **/
	private static final int pageSize = 4;

	Button top_back;
	ImageView add;
	Data_loader data;
	DialogUtil dialogUtil;
	AllFriendsFragment allFriendsFragment;
	AllChildrenFragment allChildrenFragment;
	AllOwerFragment allOwerFragment;
	AllMasterFragment allMasterFragment;
	private ProgressDialog progressDialog;
	private TextView owertv;
	private TextView mastertv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtabwidget);

		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		initView();

	}

	private void initView() {
		top_back = (Button) findViewById(R.id.top_back);
		add = (ImageView) findViewById(R.id.add);
		top_back.setOnClickListener(this);
		add.setOnClickListener(this);

		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);

		InitImageView();
		InitTextView();
		InitViewPager();
	}

	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		allFriendsFragment = new AllFriendsFragment();
		allChildrenFragment = new AllChildrenFragment();
		allOwerFragment = new AllOwerFragment();
		allMasterFragment = new AllMasterFragment();
		fragments = new Fragment[] { allFriendsFragment,allOwerFragment,allMasterFragment ,allChildrenFragment };
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.vPager, allFriendsFragment)
				.add(R.id.vPager, allOwerFragment)
				.add(R.id.vPager, allMasterFragment)
				.add(R.id.vPager, allChildrenFragment)
				.hide(allChildrenFragment)
				.hide(allOwerFragment)
				.hide(allMasterFragment)
				.show(allFriendsFragment).commit();

	}

	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		dlr = (TextView) findViewById(R.id.tab_1);
		owertv = (TextView) findViewById(R.id.tab_2);
		mastertv = (TextView) findViewById(R.id.tab_3);
		tyr = (TextView) findViewById(R.id.tab_4);

		dlr.setTextColor(selectedColor);
		owertv.setTextColor(unSelectedColor);
		mastertv.setTextColor(unSelectedColor);
		tyr.setTextColor(unSelectedColor);

		dlr.setText("会话");
		owertv.setText("货主");
		mastertv.setText("船东");
		tyr.setText("子帐号");

		registerForContextMenu(dlr);
		dlr.setOnLongClickListener(new OnLongClickListenerImpl(0));
		owertv.setOnLongClickListener(new OnLongClickListenerImpl(1));
		mastertv.setOnLongClickListener(new OnLongClickListenerImpl(2));
		tyr.setOnLongClickListener(new OnLongClickListenerImpl(3));

		dlr.setOnClickListener(new MyOnClickListener(0));
		owertv.setOnClickListener(new MyOnClickListener(1));
		mastertv.setOnClickListener(new MyOnClickListener(2));
		tyr.setOnClickListener(new MyOnClickListener(3));
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

	private class OnLongClickListenerImpl implements OnLongClickListener {
		private int index = 0;

		public OnLongClickListenerImpl(int i) {
			index = i;
		}

		@Override
		public boolean onLongClick(View v) {

			switch (index) {
			case 0:
				Intent intent0 = new Intent(NewContactListActivity.this,
						SendNotifyActivity.class).putExtra("to", "dlr");
				startActivity(intent0);
				break;
			case 1:
				Intent intent1 = new Intent(NewContactListActivity.this,
						SendNotifyActivity.class).putExtra("to", "ower");
				startActivity(intent1);
				break;
			case 2:
				Intent intent2 = new Intent(NewContactListActivity.this,
						SendNotifyActivity.class).putExtra("to", "master");
				startActivity(intent2);
				break;
			case 3:
				Intent intent3 = new Intent(NewContactListActivity.this,
						SendNotifyActivity.class).putExtra("to", "tyr");
				startActivity(intent3);
				break;
			}
			return true;
		}
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
				dlr.setTextColor(selectedColor);
				owertv.setTextColor(unSelectedColor);
				mastertv.setTextColor(unSelectedColor);
				tyr.setTextColor(unSelectedColor);
				break;
			case 1:
				dlr.setTextColor(unSelectedColor);
				owertv.setTextColor(selectedColor);
				mastertv.setTextColor(unSelectedColor);
				tyr.setTextColor(unSelectedColor);
				break;
			case 2:
				dlr.setTextColor(unSelectedColor);
				owertv.setTextColor(unSelectedColor);
				mastertv.setTextColor(selectedColor);
				tyr.setTextColor(unSelectedColor);
				break;
			case 3:
				dlr.setTextColor(unSelectedColor);
				owertv.setTextColor(unSelectedColor);
				mastertv.setTextColor(unSelectedColor);
				tyr.setTextColor(selectedColor);
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
					* index, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

			ContactManager.getInstance().getContactList().clear();
			loadFragment();

			switch (index) {
			case 0:
				allFriendsFragment.refresh();
				loadDate();
				dlr.setTextColor(selectedColor);
				owertv.setTextColor(unSelectedColor);
				mastertv.setTextColor(unSelectedColor);
				tyr.setTextColor(unSelectedColor);
				break;
			case 1:
				allOwerFragment.refresh();
				loadDate();
				dlr.setTextColor(unSelectedColor);
				owertv.setTextColor(selectedColor);
				mastertv.setTextColor(unSelectedColor);
				tyr.setTextColor(unSelectedColor);
				break;
			case 2:
				allMasterFragment.refresh();
				loadDate();
				dlr.setTextColor(unSelectedColor);
				owertv.setTextColor(unSelectedColor);
				mastertv.setTextColor(selectedColor);
				tyr.setTextColor(unSelectedColor);
				break;
			case 3:
				allChildrenFragment.refresh();
				loadDate();
				dlr.setTextColor(unSelectedColor);
				owertv.setTextColor(unSelectedColor);
				mastertv.setTextColor(unSelectedColor);
				tyr.setTextColor(selectedColor);
				break;
			}

		}
	}

	/**
	 * 定义适配器
	 */
	class myPagerAdapter extends FragmentPagerAdapter {
		private Fragment[] fragment;

		public myPagerAdapter(FragmentManager fm, Fragment[] fragments) {
			super(fm);
			this.fragment = fragments;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragment == null || fragment.length == 0) ? null
					: fragment[arg0];
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
			return fragment == null ? 0 : fragment.length;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.top_back:
			finish();
			break;
		case R.id.add:
			startActivity(new Intent(this, AddContactActivity.class));
			break;
		default:
			break;
		}
	}

	// 加载碎片
	protected void replaceFragment(int viewResource, String fragmentName) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = Fragment.instantiate(this, fragmentName);
		ft.replace(viewResource, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
		getSupportFragmentManager().executePendingTransactions();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onStart() {
		super.onStart();
//		UserData user = GlobalApplication.getInstance().getUserData();
//		if (user != null) {
//			if (user.isCarrier()) {
//				// add.setVisibility(View.VISIBLE);
//			}
//		}
		ContactManager.getInstance().getContactList().clear();
		loadDate();
	}

	protected void loadDate() {
		final Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				progressDialog = dialogUtil.loading("正在初始化联系人列表!", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				try {
					progressDialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)) {
							boolean contentMD5Changed = (Boolean) map
									.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils(
									"/mobile/contact/myContact", params);
							if (contentMD5Changed
									&& NetworkUtils.isNetworkAvailable()) {
								s.setParam(content);
							} else {
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion,
										new TypeToken<Map<String, Object>>() {
										}.getType());
							}
						}
						// Toast.makeText(ContactListActivity.this,
						// (CharSequence) map.get("message"),
						// Toast.LENGTH_SHORT).show();
						HashMap<String, Object> contents = (HashMap<String, Object>) map
								.get("content");
						List<UserData> contacts = (List<UserData>) contents
								.get("contacts");
						if (contacts != null && !contacts.isEmpty()) {
							for (int i = 0; i < contacts.size(); i++) {
								UserData userData = new UserData(
										(Map<String, Object>) contacts.get(i));
								User user = new User();
								user.setUserData(userData);

								ContactManager.getInstance().addContact(user);
							}
							if (currIndex == 0)
								allFriendsFragment.refresh();
							else if (currIndex == 1)
								allOwerFragment.refresh();
							else if (currIndex == 2)
								allMasterFragment.refresh();
							else if (currIndex == 3)
								allChildrenFragment.refresh();
						}

					} else {
						Toast.makeText(NewContactListActivity.this,
								(CharSequence) map.get("message"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				progressDialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(NewContactListActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
			}
		};

		if (currIndex == 0)
			params.put("role", MassNotify.allfriends.toString());
		else if (currIndex == 1)
			params.put("role", MassNotify.allowner.toString());
		else if (currIndex == 2)
			params.put("role", MassNotify.allmaster.toString());
		else if (currIndex == 3)
			params.put("role", MassNotify.allchildren.toString());
		data.getApiResult(handler, "/mobile/contact/myContact", params, "get");

	}

	private void loadFragment() {
		FragmentTransaction trx = getSupportFragmentManager()
				.beginTransaction();
		if (!fragments[currIndex].isAdded()) {
			trx.add(R.id.vPager, fragments[currIndex]);
		}
		trx.show(fragments[currIndex]).commit();
	}

}
