package com.eyunda.third.activities.order;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.order.MyTextWatcher;
import com.eyunda.third.adapters.order.OrderAddTYRAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 新增合同-查找托运人
 * 
 * @author guoqiang
 *
 */
public class AddOrderTYRActivity extends AddOrderActivity implements
		OnClickListener {
	SynData_loader synDataLoader;
	Data_loader dataLoader;
	Image_loader imgLoader;

	private AutoCompleteTextView autoCompleteTextView;
	private Button saveBtn;
	private RelativeLayout userInfo;
	private UserData curSeclectUser = null;
	private MyTextWatcher myTextWatcher;

	private RelativeLayout btnSaveTag;
	private LinearLayout autoCompTag;

	// 处理保存结果
	AsyncHttpResponseHandler resultHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			Toast.makeText(AddOrderTYRActivity.this, content,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStart() {
			dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
		}

		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			ConvertData cd = new ConvertData(res);
			dialog.dismiss();
			if (cd.getReturnCode().equalsIgnoreCase("Success")) {

				Intent intent = new Intent(AddOrderTYRActivity.this,
						AddOrderZXActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("orderId", orderId);// 合同ID
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(AddOrderTYRActivity.this, cd.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().penaltyLog().build());

		dataLoader = new Data_loader();
		imgLoader = new Image_loader(this, (GlobalApplication) getApplication());

		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		orderId = (Long) bundle.getLong("orderId");
		setContentView(R.layout.eyd_order_findtyr);

		saveBtn = (Button) findViewById(R.id.button_save);
		userInfo = (RelativeLayout) findViewById(R.id.userInfo);
		userInfo.setVisibility(View.INVISIBLE);

		btnSaveTag = (RelativeLayout) findViewById(R.id.btnSaveTag);
		autoCompTag = (LinearLayout) findViewById(R.id.autoCompTag);

		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTyr);
		OrderAddTYRAdapter autocompleteAdapter = new OrderAddTYRAdapter(this,
				R.layout.eyd_auto_user_item);

		myTextWatcher = new MyTextWatcher(autoCompleteTextView,
				autocompleteAdapter);

		autoCompleteTextView.setAdapter(autocompleteAdapter);

		autoCompleteTextView.addTextChangedListener(myTextWatcher);
		autoCompleteTextView.setThreshold(1);
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String info = ((OrderAddTYRAdapter) parent.getAdapter())
						.getItem(position);
				curSeclectUser = myTextWatcher.getSelectedUser(info);
				// Log.v("info", "选择了：" + info);
				if (null != curSeclectUser) {
					showUserInfo(curSeclectUser);
				}
			}
		});


		saveBtn.setOnClickListener(this);
	}


	@Override
	protected void onStart() {
		super.onStart();
		if (!orderId.equals(0L)) {
			setTitle("编辑合同-查找托运人");
			btnAddTyr.setOnClickListener(null);
			btnAddTyr.setBackgroundColor(0xFF6db7ff);
			loadDate();
		} else {
			setTitle("新增合同-查找托运人");
		}

	}

	@Override
	protected synchronized void loadDate() {
		// 异步获取当前用户，set到curSeclectUser中
		final Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("orderId", orderId);
		AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				Toast.makeText(AddOrderTYRActivity.this, content,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据读取中，请稍候...");
			}

			@Override
			public void onSuccess(String res) {
				super.onSuccess(res);
				ConvertData cd = new ConvertData(res,
						"/mobile/order/myOrder/edit", apiParams);

				HashMap<String, Object> var = (HashMap<String, Object>) cd
						.getContent();
				dialog.dismiss();
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> var2 = (HashMap<String, Object>) var
							.get("orderData");
					OrderData order = new OrderData(var2);
					curSeclectUser = order.getOwner();
					// curSeclectUser = new UserData((HashMap<String,
					// Object>)cd.getContent());
					if (null != GlobalApplication.getInstance().getUserData()) {// 当前用户是托运人，不能修改托运人信息
						if (GlobalApplication.getInstance().getUserData()
								.getId().equals(order.getOwnerId())) {
							// autoCompleteTextView.setVisibility(View.GONE);
							// saveBtn.setVisibility(View.GONE);
							// btnSaveTag.setVisibility(View.GONE);
							// autoCompTag.setVisibility(View.GONE);

						}
					}
					// 获取合同的托运人
					showUserInfo(curSeclectUser);

				} else {
					// 用户session已经丢失
					Toast.makeText(AddOrderTYRActivity.this, cd.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		};

		// 调用获取合同接口
		dataLoader.getApiResult(showHandler, "/mobile/order/myOrder/edit",
				apiParams, "get");

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_save) {
			Map<String, Object> apiParams = new HashMap<String, Object>();
			apiParams.put("orderId", orderId);
			apiParams.put("consignorId", curSeclectUser.getId());
			// 调用保存托运人接口
			UserData userData = GlobalApplication.getInstance().getUserData();
			if (userData != null
					&& userData.getId().longValue() == curSeclectUser.getId()
							.longValue()) {
				Toast.makeText(AddOrderTYRActivity.this, "保存失败，托运人不能是自己",
						Toast.LENGTH_SHORT).show();

			} else
				dataLoader.getApiResult(resultHandler,
						"/mobile/order/myOrder/saveConsignor", apiParams);
		}

	}

	/**
	 * 取用户信息，复制到视图，然后显示出来
	 */
	private void showUserInfo(UserData u) {
		ImageView user_head = (ImageView) findViewById(R.id.user_head); // 头像替换
		String uheadLogo = u.getUserLogo();
		if (!uheadLogo.equals("")) {
			String headString = ApplicationConstants.IMAGE_URL
					+ u.getUserLogo();
			imgLoader.load_normal_Img(headString, user_head);
		}

		Button userRealName = (Button) findViewById(R.id.userRealName);
		userRealName.setText(u.getTrueName());

		Button userLoginName = (Button) findViewById(R.id.userLoginName);
		userLoginName.setText(u.getLoginName());

		Button nickName = (Button) findViewById(R.id.nickName);
		nickName.setText(u.getNickName());

		Button phoneNum = (Button) findViewById(R.id.phoneNum);
		phoneNum.setText(u.getMobile());

		Button userEmail = (Button) findViewById(R.id.userEmail);
		userEmail.setText(u.getEmail());

		userInfo.setVisibility(View.VISIBLE);
	}

}
