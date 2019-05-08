package com.eyunda.third.activities.cargo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.order.AddOrderCCActivity;
import com.eyunda.third.adapters.order.MyTextWatcher;
import com.eyunda.third.adapters.order.OrderAddTYRAdapter;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoStatusCode;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.hy.client.R;
import com.hy.client.R.id;
import com.hy.client.R.layout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FindTYRActivity extends CommonActivity implements OnClickListener {

	private Button saveBtn;
	private RelativeLayout userInfo;
	private AutoCompleteTextView autoCompleteTextView;
	private MyTextWatcher myTextWatcher;
	protected UserData curSeclectUser;
	private Data_loader dataLoader;
	private String userId;
	protected String tyrName;
	protected CargoStatusCode curStatus;
	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
		setContentView(R.layout.eyd_order_findtyr);
		 dataLoader = new Data_loader();
		 mImageLoader = ImageLoader.getInstance();
		 Intent intent =getIntent();
		 userId=intent.getStringExtra("id");
		saveBtn = (Button)findViewById(R.id.button_save);
		userInfo=(RelativeLayout)findViewById(R.id.userInfo);
		userInfo.setVisibility(View.INVISIBLE);
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTyr); 
		OrderAddTYRAdapter autocompleteAdapter = new OrderAddTYRAdapter(this,R.layout.eyd_auto_user_item);
		myTextWatcher = new MyTextWatcher(autoCompleteTextView, autocompleteAdapter);
	    autoCompleteTextView.setAdapter(autocompleteAdapter);
		
		autoCompleteTextView.addTextChangedListener(myTextWatcher);
		autoCompleteTextView.setThreshold(1);
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String info = ((OrderAddTYRAdapter) parent.getAdapter()).getItem(position);
				curSeclectUser = myTextWatcher.getSelectedUser(info);
                //Log.v("info", "选择了：" + info);  
                if(null != curSeclectUser){
                	showUserInfo(curSeclectUser);
                }
			}
		});
		saveBtn.setOnClickListener(this);
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("查找托运人");
		if(!userId.equalsIgnoreCase("0")){
			loadDate();
		}
	}

	private void loadDate() {
		//异步获取当前用户，set到curSeclectUser中
		Map<String,Object> apiParams = new HashMap<String, Object>();
		//apiParams.put("cargoKeyWords",autoCompleteTextView.getText().toString());
		//调用获取合同接口
		dataLoader.getApiResult(showHandler,"/mobile/cargo/AllCargos", apiParams,"get");

		
	}
	AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			Toast.makeText(FindTYRActivity.this, content,
					Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onStart(){
			dialog = dialogUtil.loading("通知", "数据读取中，请稍候...");
		}
		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			ConvertData cd = new ConvertData(res);
			
			HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
			dialog.dismiss();
			if(cd.getReturnCode().equals("Success")){
				List<Map<String, Object>> list = (List<Map<String,Object>>)var.get("cargos");
				if(list.size()>0){
					for(Map<String,Object> mm :list){
						CargoData cargo = new CargoData(mm); 
						curStatus =cargo.getCargoStatus();
						curSeclectUser = cargo.getOwner();
						showUserInfo(curSeclectUser);
						
					}
				}
			}
		}

	};
	/**
	 * 取用户信息，复制到视图，然后显示出来
	 */
	protected void showUserInfo(UserData u) {
		    
			ImageView user_head = (ImageView)findViewById(R.id.user_head);		//头像替换

				String headString = ApplicationConstants.IMAGE_URL+u.getUserLogo();
				mImageLoader.displayImage(headString, user_head,
						MyshipAdapter.displayImageOptions);	
			
			
			Button userRealName=(Button)findViewById(R.id.userRealName);
			userRealName.setText(u.getTrueName());
			
			Button userLoginName=(Button)findViewById(R.id.userLoginName);
			userLoginName.setText(u.getLoginName());
			
			Button nickName=(Button)findViewById(R.id.nickName);
			nickName.setText(u.getNickName());
			
			Button phoneNum=(Button)findViewById(R.id.phoneNum);
			phoneNum.setText(u.getMobile());
			
			Button userEmail=(Button)findViewById(R.id.userEmail);
			userEmail.setText(u.getEmail());
			
			userInfo.setVisibility(View.VISIBLE);
		


		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_save){
			Map<String,Object> apiParams = new HashMap<String, Object>();
			Intent intent = new Intent();
			//intent.putExtra("userImg", curSeclectUser);
			intent.putExtra("userData", curSeclectUser);
			if(curStatus!=null)
			intent.putExtra("cargoStatus", curStatus.toString());
			setResult(RESULT_OK, intent);
			finish();
		
		}
	}
}
