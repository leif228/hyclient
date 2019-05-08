package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.ship.MyOrderShipAdapter;
import com.eyunda.third.domain.enumeric.ReleaseStatusCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 备选船舶
 * @author
 *
 */
public class AddOrderCCSuggestActivity extends CommonListActivity implements OnItemClickListener{
	Data_loader data;
	Button menu_basic,menu_class,menu_upload,menu_report;
	protected ScrollView head1;


	private ListView listView;
	private MyOrderShipAdapter smpAdapter;
	private  ArrayList<Map<String, Object>> dataList;
	DialogUtil dialogUtil;
	ProgressDialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_myship_list);	
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		this.listView =  (ListView)findViewById(R.id.myship_list);
		smpAdapter = new MyOrderShipAdapter(AddOrderCCSuggestActivity.this, getData());
		listView.setAdapter(smpAdapter);
		listView.setOnItemClickListener(this);
	      
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶列表"); 
		setRight("关闭",new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private  ArrayList<Map<String, Object>> getData(){
		dataList= new ArrayList<Map<String, Object>>();
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
				Gson gson = new Gson();
				HashMap<String, Object> result= gson.fromJson((String) arg0,
						new TypeToken<Map<String, Object>>() {}.getType());
				if (result.get("returnCode").equals("Success")) {
					HashMap ma = (HashMap<String, String>)result.get("content");
				
					List content = (ArrayList<String>)ma.get("shipDatas");
					
					int size = content.size();
					if(size > 0){
						for(int i = 0; i <size; i++){
						
					ShipData shipData  = new ShipData((Map<String, Object>) content.get(i));
					Map<String, Object> map = new HashMap<String, Object>();
					//map.put("operatorId", shipData.getOperatorId()); //operatorId
					map.put("userId", "0"); //用户id
					map.put("shipId", shipData.getId().toString()); //shipId
					map.put("shipLogo", shipData.getShipLogo());//Logo
					map.put("shipName", shipData.getShipName());//船名
					map.put("shipType", "类别:"+shipData.getTypeData().getTypeName());  //类型
					map.put("TypeCode", shipData.getShipType());  //编码
					map.put("code", "船号:"+shipData.getShipCode());//船号			
					map.put("adv", shipData.getKeyWords()); //广告词
					map.put("carrier", "承运人:"+shipData.getMaster().getTrueName());//承运人
					map.put("endtime", "发布时间："+shipData.getReleaseTime()); //截止日期
					
					//shipData.getReleaseStatus().toString();//发布状态
					map.put("shipStatic", shipData.getShipStatus().toString());
					map.put("btnEdit", true);//编辑
					map.put("btnDelete", true);//删除
					map.put("btnPublish", true);//发布
					map.put("btnCancel", false);//取消
					
				
					dataList.add(map);
                
					
				}
			}
					smpAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(AddOrderCCSuggestActivity.this, (String)result.get("message"),
							Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(AddOrderCCSuggestActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(AddOrderCCSuggestActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(AddOrderCCSuggestActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(AddOrderCCSuggestActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();			
				
			}
			
		};
		
		Map<String,Object> params = new HashMap<String, Object>();
		data.getApiResult(handler , "/mobile/ship/myShip",params,"get");
		
		return dataList;

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		//String text = listView.getItemAtPosition(position)+"";
		HashMap map = (HashMap)listView.getItemAtPosition(position);
		Intent intent=new Intent(AddOrderCCSuggestActivity.this,ShipPreviewActivity.class);	
		//绑定传输的船舶数据   
		intent.putExtra("id", map.get("shipId").toString());
		intent.putExtra("userId", map.get("userId").toString());
		intent.putExtra("name",map.get("shipName").toString());   
		startActivity(intent);	
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
