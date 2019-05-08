package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.GeneralPayActivity;
import com.eyunda.third.activities.account.WebPayActivity;
import com.eyunda.third.activities.cargo.AddCargoActivity;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.activities.order.SimpleOrderActivity;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.tools.CustomAlertDialog;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶预览
 * 
 * @author Liguang
 *
 */
public class ShipPreviewActivity extends CommonActivity implements OnClickListener {
	Data_loader dataLoader;
	// private long timeout = 5000;

	protected Button call_but, answer_but;
	protected LinearLayout liner_bg, liner_bg1,liner_bg2;// 聊天和电话背景
	private WebView mWebView;
	private ProgressDialog progressDialog;
	Dialog dialog = null;

	String shipId;
	private int type;
	Data_loader load = new Data_loader();
	private String shipName;

	List<UserData> operators;
	CustomAlertDialog.Builder customBuilder = null;
	private CommonAdapter<SpinnerItem> smpAdapter;
	private ArrayList<SpinnerItem> dataList;
	private int select = 1;// 1,聊天，2电话

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_ship_preview);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0); // 默认值为0
		shipId = intent.getStringExtra("id");
		shipName = intent.getStringExtra("name");
		dataLoader = new Data_loader();
		customBuilder = new CustomAlertDialog.Builder(ShipPreviewActivity.this);
		operators = new ArrayList<UserData>();
		dataList = new ArrayList<SpinnerItem>();
		init();
	}

	private void init() {
		answer_but = (Button) findViewById(R.id.answer_but);
		call_but = (Button) findViewById(R.id.call_but);
		answer_but.setOnClickListener(this);
		call_but.setOnClickListener(this);
		liner_bg2 = (LinearLayout) findViewById(R.id.liner_bg2);
		liner_bg1 = (LinearLayout) findViewById(R.id.liner_bg1);
		liner_bg = (LinearLayout) findViewById(R.id.liner_bg);
		final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
		final ProgressBar lpb = (ProgressBar) findViewById(R.id.loadingPb);
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setBlockNetworkImage(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setUseWideViewPort(true);

		// 缓存webView
		mWebView.getSettings().setAppCacheEnabled(false);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.getSettings().setAppCachePath(Environment.getExternalStorageDirectory() + "/eyunda/cache");
		if (!NetworkUtils.isNetworkAvailable()) {
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		// 页面加载提示及超时处理
		mWebView.setWebViewClient(new WebViewClient() {

			// 创建一个WebViewClient,重写onPageStarted和onPageFinished
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				getOperInfo();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.getSettings().setBlockNetworkImage(false);
				lpb.setVisibility(View.GONE);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			// 进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.GONE);
				} else {
					if (View.GONE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);

			}

		});

		mWebView.loadUrl(ApplicationConstants.SERVER_URL + "/mobile/common/shipShow?id=" + shipId);

		smpAdapter = new CommonAdapter<SpinnerItem>(ShipPreviewActivity.this, dataList, R.layout.eyd_popup_item) {
			@Override
			public void convert(ViewHolder helper, SpinnerItem item) {
				helper.setText(R.id.spinnerValue, item.getValue());
				helper.setText(R.id.spinnerId, item.getId());
				helper.setText(R.id.spinnerCid, item.getData());
			}
		};

	}

	// 获取船舶代理人列表
	private void getOperInfo() {
		final HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("shipId", shipId);
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ConvertData cd = new ConvertData(arg0, "/mobile/home/shipOperatorList", params);
				if (cd.getReturnCode().equals("Success")) {
					Map<String, Object> content = (Map<String, Object>) cd.getContent();
					List<Map<String, Object>> operatorList = (ArrayList<Map<String, Object>>) content.get("operatorDatas");
					int size = operatorList.size();
					for (int i = 0; i < size; i++) {
						UserData user = new UserData((Map<String, Object>) operatorList.get(i));
						operators.add(i, user);
						SpinnerItem si = new SpinnerItem(i + "", user.getTrueName(), user.getId().toString());
						dataList.add(si);
					}
					smpAdapter.notifyDataSetChanged();
					if (operators.size() > 0) {
						enableBtns();
					} else {
						disableBtns();
					}
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
			}
		};
		load.getApiResult(handler, "/mobile/home/shipOperatorList", params, "get");
	}

	// 设置按钮不可点击
	protected void disableBtns() {
		liner_bg1.setBackgroundColor(0xff696969);
		answer_but.setClickable(false);	
		liner_bg2.setBackgroundColor(0xff696969);
		call_but.setClickable(false);

	}

	// 设置按钮可点击状态
	protected void enableBtns() {
		liner_bg1.setBackgroundColor(0xff5b87d5);
		answer_but.setClickable(true);
		liner_bg2.setBackgroundColor(0xff5b87d5);
		call_but.setClickable(true);
	}

	// 显示备选用户
	private void showOperators() {
		dialog = null;
		customBuilder.setTitle("选择联系人").setAdapter(smpAdapter, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SpinnerItem si = smpAdapter.getItem(position);
				dialog.dismiss();
				User chatUser = getSelectUser(si);
				if (select == 1) {
					startActivity(
							new Intent(ShipPreviewActivity.this, ChatActivity.class).putExtra("toChatUser", chatUser));
				} else if (select == 2) {
					Intent phoneIntent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + chatUser.getUserData().getMobile()));
					startActivity(phoneIntent);
				}
			}
		});
		dialog = customBuilder.create();
		dialog.show();

	}

	// 从备选列表中找出当前选择的用户
	protected User getSelectUser(SpinnerItem si) {
		UserData user = operators.get(Integer.parseInt(si.getId()));
		User chatUser = new User();
		// chatUser.getUserData().setId(124L);
		// chatUser.getUserData().setMobile("1371001101");
		// chatUser.getUserData().setNickName("张山");
		chatUser.getUserData().setId(user.getId());
		chatUser.getUserData().setMobile(user.getMobile());
		chatUser.getUserData().setNickName(user.getNickName());
		return chatUser;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (shipName != null) {
			setTitle(shipName);
		}
		final LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
		final UserData userData = GlobalApplication.getInstance().getUserData();
		if (isLogin.equals(LoginStatusCode.noLogin)) {// 登录状态：未登录,无法聊天
			liner_bg1.setBackgroundColor(0xff696969);
			answer_but.setOnClickListener(null);
		}

		setRight(R.drawable.ico_souc, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLogin.equals(LoginStatusCode.logined)) {
					if (userData != null && !userData.isChildUser()) {
						doFavorite(userData);
					} else {
						Toast.makeText(getApplicationContext(), "子帐号无权进行此项操作!", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "未登录无权进行此项操作!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// 收藏
	protected void doFavorite(UserData userData) {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				progressDialog = dialogUtil.loading("通知", "收藏中，请稍后...");
			}

			@Override
			public void onSuccess(String arg0) {
				progressDialog.dismiss();

				ConvertData cd = new ConvertData(arg0);
				String ma = cd.getMessage();
				Toast.makeText(getApplicationContext(), ma, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				progressDialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipPreviewActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipPreviewActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipPreviewActivity.this, content, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipPreviewActivity.this, "请求失败", Toast.LENGTH_LONG).show();
			}
		};
		params.put("shipId", Long.valueOf(shipId));
		dataLoader.getApiResult(handler, "/mobile/ship/favorite", params, "get");
	}

	@Override
	public void onClick(View v) {
		UserData ud = GlobalApplication.getInstance().getUserData();
		if (ud != null && ud.isChildUser()) {
			Toast.makeText(getApplicationContext(), "子帐号不能操作!", Toast.LENGTH_SHORT).show();
			return;
		}

		switch (v.getId()) {
		case R.id.call_but:// 打电话
			select = 2;
			showOperators();
			// Intent phoneIntent = new Intent(Intent.ACTION_CALL,
			// Uri.parse("tel:"+ chatUser.getUserData().getMobile()));
			// startActivity(phoneIntent);
			break;

		case R.id.answer_but: // 聊天
			select = 1;
			showOperators();
			// LoginStatusCode isLogin =
			// GlobalApplication.getInstance().getLoginStatus();

			// if (isLogin.equals(LoginStatusCode.logined)){
			// if(ud!=null&&!ud.getId().equals(chatUser.getUserData().getId()))
			// startActivity(new
			// Intent(this,ChatActivity.class).putExtra("toChatUser",
			// chatUser));
			// else
			// Toast.makeText(getApplicationContext(), "不能与自己聊天",
			// Toast.LENGTH_LONG).show();
			// }else
			// Toast.makeText(getApplicationContext(),
			// "请登录后再试",Toast.LENGTH_LONG).show();

			break;
		}
	}

}
