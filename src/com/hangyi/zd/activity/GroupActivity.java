package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.reg.UpdateQA;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.part1.data.PartData_loader;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.activities.user.AgentActivity;
import com.eyunda.third.chat.utils.LogUtil;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.domain.GroupCode;
import com.hangyi.zd.domain.GroupData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;

public class GroupActivity extends CommonListActivity {
	private static final String LOGTAG = LogUtil
			.makeLogTag(GroupActivity.class);
	ImageView user_head;
	DialogUtil dialogUtil;
	RelativeLayout port,kh,cd,all;
	PartData_loader data;
	Image_loader loader;
	ProgressDialog dialog;
	ImageLoader mImageLoader;
	Data_loader data1;
	SynData_loader synDataLoader;
	List<GroupData> totalShipCooordDatas = new ArrayList<GroupData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_group_information);
		port = (RelativeLayout) findViewById(R.id.port);
		kh = (RelativeLayout) findViewById(R.id.kh);
		cd = (RelativeLayout) findViewById(R.id.cd);
		all = (RelativeLayout) findViewById(R.id.all);
		
		synDataLoader = new SynData_loader();
		data1 = new Data_loader();
		dialogUtil = new DialogUtil(this);
		user_head = (ImageView) findViewById(R.id.user_head);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());

		loader = new Image_loader(this, (TAApplication) getApplication());
		
		port.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GroupActivity.this, GroupListActivity.class).putExtra("gType", GroupCode.gport.getDescription()));
				finish();
			}
		});
		kh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GroupActivity.this, GroupListActivity.class).putExtra("gType", GroupCode.gkehu.getDescription()));
				finish();
			}
		});
		cd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GroupActivity.this, GroupListActivity.class).putExtra("gType", GroupCode.gship.getDescription()));
				finish();
			}
		});
		all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(totalShipCooordDatas.size()>0){
					postSC();
				}else{
					Toast.makeText(GroupActivity.this, "加载数据为空,请稍后再试！", Toast.LENGTH_SHORT).show();
				}
			}
		});

		getHx();
	}
	private void getHx() {
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            	break;
            }
        }

		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("PHPSESSID", PHPSESSID);
		
		data1.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				
				try {
					Gson gson = new Gson();
					final HashMap<String, Object> rmap = gson.fromJson(
							arg2, new TypeToken<Map<String, Object>>() {
							}.getType());
					if (rmap.get("returnCode").equals("Success")) {
						
						List<Map<String, Object>> map = (List<Map<String, Object>>) rmap.get("groupDatas");
						
						for(Map<String, Object> m:map){
							GroupData gd = new GroupData(m);
							totalShipCooordDatas.add(gd);
						}
					}
//					else
//						Toast.makeText(context, "加载数据失败！", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
//					Toast.makeText(context, "加载数据失败！", Toast.LENGTH_LONG).show();
				}
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(GroupActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(GroupActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(GroupActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(GroupActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.groups, apiParams, "get");	
	}
	
	GroupData map = null;
	private void postSC() {
		
		for(GroupData g:totalShipCooordDatas){
			if("所有".equals(g.getGroupName())){
				g.setFlag(true);
				map = g;
			}else{
				g.setFlag(false);
			}
		}
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		
		Gson s = new Gson();
		String json = s.toJson(totalShipCooordDatas);

		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("groupDatas", json);
		apiParams.put("PHPSESSID", PHPSESSID);
		
		data1.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				dialog.dismiss();
				
				GroupActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
//						groupListener.getResult(map);
//						startActivity(new Intent(GroupListActivity.this, NewPageHomeMainActivity.class)
//						.putExtra("groupData", map));
						if(map!=null){
							GlobalApplication.getInstance().setGroupData(map);
							startActivity(new Intent(GroupActivity.this, NewPageHomeMainActivity.class));
							finish();
						}
					}
				});
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(GroupActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(GroupActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(GroupActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(GroupActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.saveMobGroups, apiParams, "post");	
		
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("驳船分组");
	}

	
	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}
}
