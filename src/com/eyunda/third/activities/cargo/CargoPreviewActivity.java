package com.eyunda.third.activities.cargo;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.activities.order.OrderPreviewActivity;
import com.eyunda.third.activities.order.SimpleOrderActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.tools.CustomAlertDialog;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class CargoPreviewActivity extends CommonListActivity implements OnClickListener {
	Data_loader dataLoader;
	protected Button call_but, answer_but;
	protected LinearLayout liner_bg2, liner_bg1;
	private String tel;
	private WebView mWebView;
	String cargoId;
	String shipName;
	private User user;
	private LinearLayout liner_bg;
	private Data_loader data = new Data_loader();

	List<UserData> operators = new ArrayList<UserData>();
	CustomAlertDialog.Builder customBuilder = null;
	private CommonAdapter<SpinnerItem> smpAdapter;
	private ArrayList<SpinnerItem> dataList = new ArrayList<SpinnerItem>();
	private int select = 1;// 1,聊天，2电话
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_ship_preview);
		Intent intent = getIntent();
		cargoId = intent.getStringExtra("id");
		shipName = intent.getStringExtra("name");
		dataLoader = new Data_loader();
		answer_but = (Button) findViewById(R.id.answer_but);
		call_but = (Button) findViewById(R.id.call_but);
		answer_but.setOnClickListener(this);
		call_but.setOnClickListener(this);

		liner_bg = (LinearLayout) findViewById(R.id.liner_bg);
		liner_bg1 = (LinearLayout) findViewById(R.id.liner_bg1);
		liner_bg2 = (LinearLayout) findViewById(R.id.liner_bg2);
		final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
		final ProgressBar lpb = (ProgressBar) findViewById(R.id.loadingPb);
		customBuilder = new CustomAlertDialog.Builder(CargoPreviewActivity.this);
		
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
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				getCarrierData();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.getSettings().setBlockNetworkImage(false);
				lpb.setVisibility(View.GONE);
			}

		});
		mWebView.setWebChromeClient(new WebChromeClient() {

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

		mWebView.loadUrl(ApplicationConstants.SERVER_URL + "/mobile/common/cargoShow?id=" + cargoId);
		smpAdapter = new CommonAdapter<SpinnerItem>(getApplicationContext(), dataList, R.layout.eyd_popup_item) {
			@Override
			public void convert(ViewHolder helper, SpinnerItem item) {
				helper.setText(R.id.spinnerValue, item.getValue());
				helper.setText(R.id.spinnerId, item.getId());
				helper.setText(R.id.spinnerCid, item.getData());
			}
		};
	}

	protected void getCarrierData() {
		final HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", cargoId);
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ConvertData cd = new ConvertData(arg0, "/mobile/home/cargoAgent", params);
				if (cd.getReturnCode().equals("Success")) {
					Map<String, Object> content = (Map<String, Object>) cd.getContent();
					List<Map<String, Object>> cargoAgents = (ArrayList<Map<String,Object>>)content.get("cargoAgents");
					int size = cargoAgents.size();
					for (int i = 0; i < size; i++) {
						UserData user = new UserData((Map<String, Object>) cargoAgents.get(i));
						operators.add(i, user);
						SpinnerItem si = new SpinnerItem(i+"", user.getTrueName(), user.getId().toString());
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

		data.getApiResult(handler, "/mobile/home/cargoAgent", params, "get");

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
							new Intent(CargoPreviewActivity.this, ChatActivity.class).putExtra("toChatUser", chatUser));
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

		if (isLogin.equals(LoginStatusCode.noLogin)) {// 登录状态：未登录
			liner_bg1.setBackgroundColor(0xff696969);
			answer_but.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					
				}
			});
			liner_bg1.setBackgroundColor(0xff696969);
			call_but.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					
				}
			});
		}
	}

	
	
	@Override
	public void onClick(View v) {
		UserData ud = GlobalApplication.getInstance().getUserData();
		LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
		switch (v.getId()) {
		case R.id.call_but:
			select = 2;
			if (isLogin.equals(LoginStatusCode.logined) && ((ud.getRoleDesc().indexOf("1") >=0) ||(ud.getRoleDesc().indexOf("0") >=0))) {
				showOperators();
			} else
				Toast.makeText(getApplicationContext(), "请登录后再试", Toast.LENGTH_LONG).show();

			break;

		case R.id.answer_but:
			select = 1;

			if (isLogin.equals(LoginStatusCode.logined) && ((ud.getRoleDesc().indexOf("1") >=0) ||(ud.getRoleDesc().indexOf("0") >=0))) {
				showOperators();
			} else
				Toast.makeText(getApplicationContext(), "请登录后再试", Toast.LENGTH_LONG).show();

			break;
		}

	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}

	@Override
	protected void loadDate() {
		
	}
}
