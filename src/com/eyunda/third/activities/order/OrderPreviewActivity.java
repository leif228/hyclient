package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.chat.ChatUserData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 合同预览
 * 
 * @author guoqiang
 *
 */
public class OrderPreviewActivity extends CommonListActivity implements
		OnClickListener {
	Data_loader dataLoader;
	// private long timeout = 5000;
	// private Timer timer;
	protected Button call_but, answer_but;// 聊天和电话按钮
	protected LinearLayout liner_bg2, liner_bg1;// 聊天和电话背景
	private String tel = null;// 初始化电话号码
	private User boatDaiLi = null;
	private WebView mWebView;
	private ProgressDialog dialog;

	// private Handler mHandler = new Handler();
	final Activity activity = this;
	String orderId;
	String orderNum;
	String pdfFileName;
	String url = "";
	boolean btnEdit;
	private CommonAdapter<ChatUserData> mAdapter;
	private ArrayList<ChatUserData> mUsers;
	private ListView lv;
	AlertDialog aDialog;
	boolean currentOpt = false;// 当前操作 chat－>false,phone->true
	ArrayList<UserData> userSelect;
	boolean loadFinshed = false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_preview);
		Intent intent = getIntent();
		orderId = intent.getStringExtra("orderId");
		orderNum = intent.getStringExtra("orderNum");
		pdfFileName = intent.getStringExtra("pdfFileName");
		btnEdit = intent.getBooleanExtra("btnEdit",false);
		mUsers = new ArrayList<ChatUserData>();
		userSelect = new ArrayList<UserData>();

		dataLoader = new Data_loader();

		answer_but = (Button) findViewById(R.id.answer_but);
		call_but = (Button) findViewById(R.id.call_but);

		liner_bg1 = (LinearLayout) findViewById(R.id.liner_bg1);
		liner_bg2 = (LinearLayout) findViewById(R.id.liner_bg2);
		liner_bg1.setBackgroundColor(0xff5D77B4);
		liner_bg2.setBackgroundColor(0xff5D77B4);

		answer_but.setOnClickListener(this);
		call_but.setOnClickListener(this);

//		answer_but.setClickable(false);
//		call_but.setClickable(false);

		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBlockNetworkImage(true);
    	//缓存webView
		mWebView.getSettings().setAppCacheEnabled(false); 
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.getSettings().setAppCachePath(Environment.getExternalStorageDirectory()+"/eyunda/cache");
		if(!NetworkUtils.isNetworkAvailable()){
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		}
		
		// 页面加载提示及超时处理
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				super.onPageStarted(view, url, favicon);
				dialog = ProgressDialog.show(OrderPreviewActivity.this, null,
						"页面加载中，请稍候..");
			}

			/**
			 * onPageFinished指页面加载完成,完成后取消计时器
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dialog.dismiss();
				mWebView.getSettings().setBlockNetworkImage(false);
			}
		});

		loadDate();

		String sessionId = "";
		if (GlobalApplication.getInstance() != null) {
			sessionId = GlobalApplication.getInstance().getUserData()
					.getSessionId();
		}

		mWebView.loadUrl(ApplicationConstants.SERVER_URL
				+ "/mobile/order/show?id=" + orderId + "&"
				+ ApplicationConstants.SESSIONID + "=" + sessionId);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("合同预览");
		// Toast.makeText(this, orderId+orderNum, Toast.LENGTH_LONG).show();
		setRight(R.drawable.download1, new OnClickListener() {

			@Override
			public void onClick(View v) {
				download();
			}

		});
		if(!btnEdit)
			top_commit_text.setVisibility(View.GONE);
		setRightBtn(R.drawable.editor, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserData ud = GlobalApplication.getInstance().getUserData();
				if(ud!=null&&ud.isChildUser()){
					Toast.makeText(getApplicationContext(), "子帐号不能操作!", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				Intent simpleIntent = new Intent(OrderPreviewActivity.this,SimpleOrderActivity.class);
				simpleIntent.putExtra("orderId", orderId);
				OrderPreviewActivity.this.startActivity(simpleIntent);
			}
			
		});
	}

	private void download() {
		Intent intent = new Intent(OrderPreviewActivity.this,
				OrderDownloadActivity.class);
		// 绑定传输的合同编号数据
		intent.putExtra("orderId", orderId);
		intent.putExtra("orderNum", orderNum);
		intent.putExtra("pdfFileName",pdfFileName);
		startActivity(intent);
	}

	// 异步读取order信息，决定显示电话和聊天对象
	@Override
	protected synchronized void loadDate() {
		final Map<String, Object> apiParams = new HashMap<String, Object>();
		
		// 获取数据
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {

				ConvertData cd = new ConvertData(arg0,"/mobile/order/myOrder/edit",apiParams);
				
				if (cd.getReturnCode().equals("Success")) {

					HashMap<String, Object> var = (HashMap<String, Object>) cd.getContent();
					HashMap<String, Object> var2 = (HashMap<String, Object>) var.get("orderData");
					OrderData orderData = new OrderData(var2);
		
					if (GlobalApplication.getInstance().getUserData() != null) {	
						Long uid = GlobalApplication.getInstance().getUserData().getId();
						Long ownerId = orderData.getOwnerId();//货主
						Long agentId = orderData.getAgentId();//货代
						Long brokerId = orderData.getBrokerId();//船代
						Long masterId = orderData.getMasterId();//船东
						Long handerId = orderData.getHandlerId();//操作员
						ArrayList<String> phones = new ArrayList<String>(); 
						ArrayList<UserData> users = new ArrayList<UserData>(); 
						phones.add(0, orderData.getOwner().getMobile());
						users.add(0,orderData.getOwner());
						phones.add(1,orderData.getAgent().getMobile());
						users.add(1,orderData.getAgent());
						phones.add(2,orderData.getBroker().getMobile());
						users.add(2,orderData.getBroker());
						phones.add(3, orderData.getHandler().getMobile());
						users.add(3,orderData.getHandler());
						phones.add(4,orderData.getMaster().getMobile());
						users.add(4,orderData.getMaster());
						userSelect.clear();
						if(uid.equals(ownerId)){//找后一个聊天
							if(!uid.equals(handerId)){
								userSelect.add(orderData.getHandler());
							}else if(!uid.equals(masterId)){
								userSelect.add(orderData.getMaster());
							}
							
						}else if(uid.equals(masterId)){//往前找一个
							if(!uid.equals(handerId)){
								userSelect.add(orderData.getHandler());
							}else if(!uid.equals(ownerId)){
								userSelect.add(orderData.getOwner());
							}
						}else if(uid.equals(handerId)){//最多两个
							if(!uid.equals(ownerId)){
								userSelect.add(orderData.getOwner());
							}
							if(!uid.equals(masterId)){
								userSelect.add(orderData.getMaster());
							}
						}
						
						int size = userSelect.size();
						if(size == 2){//显示两个用户
							UserData userA= userSelect.get(0);
							ChatUserData cud = new ChatUserData();
							cud.setUserId(userA.getId());
							cud.setUserName(userA.getTrueName());
							cud.setUserRole("托运人");
							cud.setUserLogo(userA.getUserLogo());
							cud.setUserPhone(userA.getMobile());
							mUsers.add(cud);
							UserData userB= userSelect.get(1);
							ChatUserData cudB = new ChatUserData();
							cudB.setUserId(userB.getId());
							cudB.setUserName(userB.getTrueName());
							cudB.setUserRole("承运人");
							cudB.setUserLogo(userB.getUserLogo());
							cudB.setUserPhone(userB.getMobile());
							mUsers.add(cudB);
							
						}else if(size == 0){
							disableAllBtn();
						}
						loadFinshed = true;

					}
				}else{
					Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
				}
				
			}
		};
		
		apiParams.put("orderId", Long.parseLong(orderId));
		dataLoader.getApiResult(handler, "/mobile/order/myOrder/edit",apiParams, "get");
	}
	//禁用聊天按钮
	protected void disableAllBtn() {
		liner_bg1.setBackgroundColor(0xff696969);
		liner_bg2.setBackgroundColor(0xff696969);
		answer_but.setClickable(false);
		call_but.setClickable(false);
		
	}

	@Override
	public void onClick(View v) {
		UserData ud = GlobalApplication.getInstance().getUserData();
		
		if (v.getId() == R.id.call_but) {// 打电话
			if(loadFinshed){
				if(userSelect.size() == 2){//显示弹框
					createDialog(1);
				}else if(userSelect.size() == 1){//直接打电话
					startCall(userSelect.get(0).getMobile());
				}
			}
		} else if (v.getId() == R.id.answer_but) {
			if(loadFinshed){
				if(userSelect.size() == 2){
					createDialog(2);
				}else if(userSelect.size() == 1){
					ChatUserData cud = new ChatUserData();
					cud.setUserId(userSelect.get(0).getId());
					cud.setUserName(userSelect.get(0).getTrueName());
					cud.setUserRole("");
					cud.setUserLogo(userSelect.get(0).getUserLogo());
					startChat(cud);
				}
			}
		}

	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}
	/**
	 * 
	 * @param type 1打电话，2，聊天
	 */
	private void createDialog(int type){
		if(type == 1){
			currentOpt = true;
		}else{
			currentOpt = false;
		}
		Context context = getApplicationContext();
		Builder dial = new Builder(OrderPreviewActivity.this);
		LayoutInflater localinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = localinflater.inflate(R.layout.alert_dialog_menu_layout, null);
		lv = (ListView) layout.findViewById(R.id.content_list);
		lv.setAdapter(mAdapter = new CommonAdapter<ChatUserData>(context,mUsers,R.layout.alert_chat_user_item) {

			@Override
			public void convert(ViewHolder helper, ChatUserData item) {

				helper.setImageByUrl(R.id.userLogo,ApplicationConstants.IMAGE_URL+item.getUserLogo() );
				helper.setText(R.id.userName, item.getUserName());
				helper.setText(R.id.userRole, item.getUserRole());
				helper.setText(R.id.userId, item.getUserId().toString());
				helper.setText(R.id.userPhone, item.getUserPhone());
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ChatUserData cd = (ChatUserData)lv.getItemAtPosition(position);
				aDialog.dismiss();
				if(currentOpt){
					//打电话
					startCall(cd.getUserPhone());
				}else{
					//聊天
					startChat(cd);
				}
				
			}
		});
		aDialog=dial.setView(layout).create();  
        aDialog.show();
        
		
	}
	
	private void startCall(String telphone){
		if (!TextUtils.isEmpty(telphone)) {
			Intent phoneIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + telphone));
			startActivity(phoneIntent);

		} else {
			Toast.makeText(OrderPreviewActivity.this, "用户未提供电话号码", Toast.LENGTH_SHORT).show();
		}
	}
	private void startChat(ChatUserData cud){
		if (cud != null) {
			LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
			if(isLogin.equals(LoginStatusCode.logined)){
				boatDaiLi = new User();
				for(int i=0; i<userSelect.size();i++){
					if(cud.getUserId() == userSelect.get(i).getId()){
						boatDaiLi.setUserData(userSelect.get(i));
						break;
					}
				}
				startActivity(new Intent(this,ChatActivity.class).putExtra("toChatUser", boatDaiLi));
			}else{
				Toast.makeText(OrderPreviewActivity.this, "请登陆后再操作！",Toast.LENGTH_SHORT).show();
			}
		} else {
			 Toast.makeText(OrderPreviewActivity.this, "获取用户信息出错！",Toast.LENGTH_SHORT).show();
		}	
	}
	/**
	 * 设置打电话按钮状态
	 * @param flag true,可打电话；false,不可打电话
	 */
	private void setCallBtnStatus(boolean flag){
		if(flag){
			call_but.setClickable(true);
			liner_bg2.setBackgroundColor(0xff5D77B4);
		}else{
			call_but.setClickable(false);
			liner_bg2.setBackgroundColor(0xff696969);
		}
	}
}
