package com.eyunda.third.activities.ship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的船舶或货物发布确认
 * 
 * @author
 *
 */
public class ConfirmPublicActivity extends CommonActivity implements OnClickListener {
	private Data_loader dataLoader;
	private UserData user;
	private boolean pubFlag = true;// true：发布 false:取消发布
	private int page;
	private Long id;//船舶或者货物Id
	private int source;//1船舶发布，2货物发布

	private TextView noticeText;
	private Button buttonSure, buttonCance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_myship_public);
		user = GlobalApplication.getInstance().getUserData();
		dataLoader = new Data_loader();
		dialogUtil = new DialogUtil(this);
		page = getIntent().getIntExtra("page", 1);
		pubFlag = getIntent().getBooleanExtra("pubFlag", true);
		id = getIntent().getLongExtra("id", 0L);
		source = getIntent().getIntExtra("source", 1);
		initView();

	}

	private void initView() {
		noticeText = (TextView)findViewById(R.id.noticeText);
		if(pubFlag){
			noticeText.setText("确认发布该"+(source==1?"船舶":"货物")+"信息么？");
		}else{
			noticeText.setText("确认取消发布该"+(source==1?"船舶":"货物")+"信息么？");
		}
		buttonCance = (Button)findViewById(R.id.buttonCance);
		buttonSure = (Button)findViewById(R.id.buttonSure);
		buttonCance.setOnClickListener(this);
		buttonSure.setOnClickListener(this);
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("操作确认");
		//loadData();
	}


	public void publish(boolean f) {
		if(f){
			AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					super.onStart();
					dialog = dialogUtil.loading("通知", "数据提交中，请稍候...");
				}
	
				@Override
				public void onSuccess(String arg0) {
					dialog.dismiss();
					ConvertData cd = new ConvertData(arg0);
					if (cd.getReturnCode().equals("Success")) {
						Intent intent = new Intent(ConfirmPublicActivity.this, MyshipActivity.class);
						intent.putExtra("page", page);
						startActivity(intent);
						finish();
						Toast.makeText(ConfirmPublicActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ConfirmPublicActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
	
					}
				}
	
				@Override
				public void onFailure(Throwable arg0) {
					super.onFailure(arg0);
					Toast.makeText(ConfirmPublicActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			};
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			String url = "";
			if(pubFlag){
				if(source==1){
					url = "/mobile/ship/myShip/publish";
				}else if(source == 2){
					url = "/mobile/cargo/myCargo/publish";
				}
			}else{
				if(source==1){
					url = "/mobile/ship/myShip/unpublish";
				}else if(source == 2){
					url = "/mobile/cargo/myCargo/unpublish";
				}
			}
			if(!"".equals(url)){
				dataLoader.getApiResult(handler, url, params, "post");
			}
		}else{
			if(source == 1){
				Intent intent = new Intent(ConfirmPublicActivity.this, MyshipActivity.class);
				intent.putExtra("page", page);
				startActivity(intent);
			}else if(source == 2){
				Intent intent = new Intent(ConfirmPublicActivity.this, CargoListActivity.class);
				intent.putExtra("page", page);
				startActivity(intent);
			}
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSure: // 确认
			publish(true);
			break;
		case R.id.buttonCance: // 取消
			publish(false);
			break;

		}
	}

}
