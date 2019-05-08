package com.eyunda.third.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.main.view.MenuHorizontalScrollView;
import com.eyunda.main.view.callback.SizeCallBackForMenu;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.WalletHomeActivity;
import com.eyunda.third.activities.chat.ContactRoleListActivity;
import com.eyunda.third.activities.home.HomeCategoryActivity;
import com.eyunda.third.activities.home.HomeSearchResultActivity;
import com.eyunda.third.activities.home.UserSiteActivity;
import com.eyunda.third.activities.notice.NoticeListActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.ship.DynamicActivity;
import com.eyunda.third.activities.ship.ShipMoniterActivity;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.OperatorData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.eyunda.tools.ListViewForScrollView;
import com.google.zxing.client.android.CaptureActivity;
import com.hy.client.R;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ygl.android.ui.BaseActivity;

public class PageHomeMainActivity extends BaseActivity {
	protected MenuHorizontalScrollView scrollView;
	private LinearLayout menuList;
	protected View homeView;
	private Button menuBtn, top_right_but;
	private View[] children;
	private LayoutInflater inflater;

	protected Data_loader data;
	protected Image_loader image_loader;

	protected TextView login_type, login_name;
	protected ImageView userHead;

	protected ImageView showpoint;
	protected DialogUtil dialogUtil;
	
	private UserData currentUser;

	@Bind(R.id.imageview1) ImageView imageview1;
	@Bind(R.id.imageview2) ImageView imageview2;
//	@Bind(R.id.imageview3) ImageView imageview3;
//	@Bind(R.id.imageview4) ImageView imageview4;
//	@Bind(R.id.imageview5) ImageView imageview5;
	@Bind(R.id.imageview6) ImageView imageview6;
	@Bind(R.id.imageview7) ImageView imageview7;
	@Bind(R.id.imageview8) ImageView imageview8;
	
	@Bind(R.id.textView1) TextView textView1;
	@Bind(R.id.textView2) TextView textView2;
//	@Bind(R.id.textView3) TextView textView3;
//	@Bind(R.id.textView4) TextView textView4;
//	@Bind(R.id.textView5) TextView textView5;
	@Bind(R.id.textView6) TextView textView6;
	@Bind(R.id.textView7) TextView textView7;
	@Bind(R.id.textView8) TextView textView8;
	@Bind(R.id.mainContent) ScrollView mainContent; 
	private ListViewForScrollView newslist;
	private List<OperatorData> mNews;
	private List<UserData> mUsers;
	private CommonAdapter<OperatorData> mAdapter;	
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

		this.homeView = inflater.inflate(R.layout.part_home_main, null);

		this.menuBtn = (Button) this.homeView.findViewById(R.id.top_left_but);
		this.menuBtn.setOnClickListener(onClickListener);
		top_right_but = (Button) this.homeView.findViewById(R.id.top_right_but);

		top_right_but.setOnClickListener(onClickListener);
		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		// 左右布局
		children = new View[] { leftView, homeView };
		this.scrollView.initViews(children, new SizeCallBackForMenu(this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);
		this.newslist = (ListViewForScrollView)this.homeView.findViewById(R.id.newslist);
		mNews = new ArrayList<OperatorData>();
		mUsers = new ArrayList<UserData>();
		ButterKnife.bind(this);
		setListView();
	}

	@Override
	protected void onStart() {
		super.onStart();	
		
		loadData();
		scrollTop();
		currentUser = GlobalApplication.getInstance().getUserData();
		
		
	}
    private void scrollTop() {
    	mainContent = (ScrollView) findViewById(R.id.mainContent);
    	mainContent.smoothScrollTo(0, 0);
	}

	//加载数据
	private void loadData() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				//TODO:add loading img
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {

				ConvertData cd = new ConvertData(arg0,"/mobile/home/operatorList", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> noticeDatas = (List<Map<String, Object>>) content.get("operatorList");

					int size = noticeDatas.size();
					mNews.clear();
					mUsers.clear();
					if(size > 0){
						for(int i=0; i<size;i++){
							OperatorData nd = new OperatorData((Map<String, Object>)noticeDatas.get(i));
							mNews.add(nd);
							mUsers.add(nd.getUserData());
							//把userData保存下来供后面使用
						}
					}
					mAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(PageHomeMainActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(PageHomeMainActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(PageHomeMainActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(PageHomeMainActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(PageHomeMainActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();
			}
		};
		params.put("pageNo", 1);
		params.put("roleCode", "");
		data.getApiResult(handler, "/mobile/home/operatorList", params, "get");
		
	}

	// 顶部菜单点击
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.top_left_but)// 左边菜单
				scrollView.clickMenuBtn();
			else if (arg0.getId() == R.id.top_right_but){
				if(GlobalApplication.getInstance().getUserData() != null && GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
					startActivity(new Intent(PageHomeMainActivity.this,CaptureActivity.class));
				}else{
					Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	private void setListView(){
		View listHead= inflater.inflate(R.layout.title_item, null);
		RelativeLayout more = (RelativeLayout) listHead.findViewById(R.id.more);
		TextView title = (TextView)listHead.findViewById(R.id.include_title);
		title.setText("代理人");
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(),HomeSearchResultActivity.class);
				Bundle bundleAgent = new Bundle();
				bundleAgent.putString("param", UserRoleCode.broker.name());
				bundleAgent.putString("title", "代理人");
				bundleAgent.putLong("type",HomeCategoryActivity.MSG_DLR_LIST);
				intent.putExtras(bundleAgent);
				startActivity(intent);
				
				//startActivity(new Intent(getApplicationContext(), NoticeListActivity.class));
			}
		});
		newslist.addHeaderView(listHead);

		newslist.setAdapter(mAdapter = new CommonAdapter<OperatorData>(context,mNews,R.layout.eyd_operatorsearch_one_item) {
			@Override
			public void convert(ViewHolder helper, OperatorData item) {
				helper.setImageByUrl(R.id.boat_logo,ApplicationConstants.IMAGE_URL+item.getUserData().getUserLogo() );//头像
				helper.setText(R.id.loginName, item.getUserData().getTrueName());//姓名
				helper.setText(R.id.name, item.getUserData().getMobile());//电话
				helper.setText(R.id.companyName, "公司名称："+item.getUserData().getUnitName());//公司
				helper.setText(R.id.boat_id, item.getUserData().getId().toString());//用户ID
				helper.setText(R.id.daiLi, "代理船舶数："+item.getShipNumber());//成交合同数：
				helper.setText(R.id.cjht, "成交合同数："+item.getOrderNumber());//成交合同数：
			}
		});
		newslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OperatorData nd = (OperatorData)parent.getAdapter().getItem(position);
				Intent intent = new Intent(PageHomeMainActivity.this, UserSiteActivity.class);
				User user = new User();
				user.setUserData(nd.getUserData());
				intent.putExtra("toChatUser", user);
				startActivity(intent);
			}
		});
	}
	private UserData findUser(Long uid){
		UserData current = null;
		for(UserData ud:mUsers){
			if(ud.getId().equals(uid)){
				current = ud;
				break;
			}
				
		}
		return current;
	}
	@Override
	public boolean backKeyPressed() {
		if (scrollView.menuOut)
			scrollView.clickMenuBtn();
		else
			finish();
		// dialogUtil.showQuitDialogFromConfig("提示", "是否退出？");
		return true;
	}


	
	//运货按钮
	@OnClick({R.id.imageview1, R.id.textView1}) 
	public void clickShipCargo(){
		Intent intent = new Intent(PageHomeMainActivity.this,SiteHomeActivity.class);
		intent.putExtra("type", 2);
		startActivity(intent);

	}
	//租船按钮
	@OnClick({R.id.imageview2, R.id.textView2}) 
	public void clickRentShip(){
		Intent intent = new Intent(PageHomeMainActivity.this,SiteHomeActivity.class);
		intent.putExtra("type", 1);
		startActivity(intent);
	}
	//动态按钮
	@OnClick({R.id.imageview3, R.id.textView3}) 
	void clickInsurance(){
		if(GlobalApplication.getInstance().getUserData() != null && GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
			Intent  intent= new Intent(getApplicationContext(), DynamicActivity.class);
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
		}
	}
	//联系人按钮
	@OnClick({R.id.imageview4, R.id.textView4}) 
	public void clickContect(){
		if(GlobalApplication.getInstance().getUserData() != null && GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
				startActivity(new Intent(PageHomeMainActivity.this,
						ContactRoleListActivity.class));
		}else{
			Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
		}
	}
	//合同按钮
	@OnClick({R.id.imageview5, R.id.textView5}) 
	public void clickOrder(){
		if(GlobalApplication.getInstance().getUserData() != null && GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
			Intent  intent= new Intent(getApplicationContext(), MyOrderActivity.class);
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
		}
	}

	//监控按钮
	@OnClick({R.id.imageview7, R.id.textView7}) 
	public void clickMonitor(){
		if(GlobalApplication.getInstance().getUserData() != null && GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
			startActivity(new Intent(getApplicationContext(),ShipMoniterActivity.class));
		}else{
			Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
		}
	}
	//钱包按钮
	@OnClick({R.id.imageview8, R.id.textView8}) 
	public void clickQianbao(){
		if(GlobalApplication.getInstance().getUserData() != null && GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
			Intent  intent= new Intent(getApplicationContext(), WalletHomeActivity.class);
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
		}

	}
	//代理人按钮
	@OnClick({R.id.imageview6, R.id.textView6}) 
	public void clickHelp(){
		Intent intent = new Intent(getApplicationContext(),HomeSearchResultActivity.class);
		Bundle bundleAgent = new Bundle();
		bundleAgent.putString("param", UserRoleCode.broker.name());
		bundleAgent.putString("title", "代理人");
		bundleAgent.putLong("type",HomeCategoryActivity.MSG_DLR_LIST);
		intent.putExtras(bundleAgent);
		startActivity(intent);
	}
}
