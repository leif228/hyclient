package com.eyunda.third.activities.ship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * Activity模版，供复制使用
 * @author
 *
 */
public class ActivityTemplate extends CommonActivity implements OnClickListener{
	private Data_loader dataLoader;
	private UserData user;//当前登录用户
	private int page=1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_activity_template);	
		user = GlobalApplication.getInstance().getUserData();
		dataLoader = new Data_loader();
		page = getIntent().getIntExtra("page", 1);
		initView();
		
	}

	private void initView() {

		
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("activity标题");
		loadData();
	}

	//请求服务端获取数据
	private void loadData(){
		final  Map<String,Object> params = new HashMap<String, Object>();
		//请求
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
				
			}


			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/ship/myAllShip", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>)cd.getContent();
					List<Map<String, Object>> content = (List<Map<String, Object>>)ma.get("shipDatas");
			
					int size = content.size();
					for(int i=0;i<size;i++){
						//ShipData shipData  = new ShipData((Map<String, Object>) content.get(i));
						Map<String, Object> map = new HashMap<String, Object>();					

					}
				}else{
					Toast.makeText(ActivityTemplate.this, cd.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ActivityTemplate.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ActivityTemplate.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(ActivityTemplate.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ActivityTemplate.this, "请求失败",
							Toast.LENGTH_LONG).show();							
			}			
		};		
		params.put("pageNo", page );
		dataLoader.getApiResult(handler , "/mobile/ship/myAllShip",params,"get");
			
	}
	//页面按钮点击事件处理
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_1: // 代理船舶

			break;
		case R.id.tab_2: // 收藏的

			break;

		}
	}

}
