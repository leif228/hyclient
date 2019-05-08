package com.eyunda.third.activities.cargo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.user.CargoNotifyAdapter;
import com.eyunda.third.chat.event.NotifyEvent;
import com.eyunda.third.chat.mina.MessageSender;
import com.eyunda.third.chat.utils.MessageConstants;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.LoginSourceCode;
import com.eyunda.third.domain.enumeric.NotifyTypeCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.DateUtils;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class CargoSendNotifyActivity extends CommonActivity implements
		OnClickListener,OnItemClickListener {
	
	public static final String notifyType = "CargoNotify";
	private EditText title, to_name, content;
	private Button btnAll, btnDelete;
	public TextView tv_checknum;
	private Data_loader dataLoader;
	private String cargoId;
	private String notifyObj = "broker";
	private CargoData cargoData;
	private ListView mListView;
	private CargoNotifyAdapter adapter;
	List<List<ShipData>>  shipDatas;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	private int position=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().penaltyLog().build());
		setContentView(R.layout.eyd_cargo_sdnotify);
		dataLoader = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		mListView = (ListView) this.findViewById(R.id.listView1);
		title = (EditText) this.findViewById(R.id.title);
		to_name = (EditText) this.findViewById(R.id.to_name);
		content = (EditText) this.findViewById(R.id.content);
		btnAll = (Button) this.findViewById(R.id.btnAll);
		btnDelete = (Button) this.findViewById(R.id.btnDelete);
		tv_checknum = (TextView) this.findViewById(R.id.tv_checknum);

		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		cargoId = (String) bundle.getString("id");
		notifyObj = (String) bundle.getString("notifyObj");
		cargoData = (CargoData) bundle.getSerializable("cargoInfo");
		setView();

		shipDatas = new ArrayList<List<ShipData>>();
		
		adapter = new CargoNotifyAdapter(this, R.layout.eyd_cargo_notify,
				shipDatas, notifyObj);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(this);
		btnAll.setOnClickListener(new OnClickListener() {  
	            @Override  
	            public void onClick(View v) {  
	                for (int i = 0; i < shipDatas.size(); i++) {  
	                	CargoNotifyAdapter.getIsSelected().put(i, true);  
	                }  
	                dataChanged();  
	            }  
	        }); 
		btnDelete.setOnClickListener(new OnClickListener() {  
	            @Override  
	            public void onClick(View v) {  
	                for (int i = 0; i < shipDatas.size(); i++) {  
	                    CargoNotifyAdapter.getIsSelected().put(i, false);  
	                }  
	                dataChanged();  
	            }  
	        }); 
		loadData();
	}

//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//	}

	private void setView() {
		title.setText("货物发布通知");
		title.setEnabled(false);
		to_name.setText("下面查看过的所有船舶");
		to_name.setEnabled(false);
		String ct = "";
		if(cargoData!=null){
			ct += "货号" + cargoData.getId() + ",";
			if (cargoData.getCargoType().getCargoBigType().equals(CargoBigTypeCode.container)) {
				ct += cargoData.getCargoType().getDescription() + "，";
			} else {
				ct += cargoData.getCargoName() + "，";
			}
			ct += cargoData.getTonTeuDes() + "，";
			ct += "从 " + cargoData.getStartFullName() + " 到 " + cargoData.getEndFullName() + "。";
			ct += "点击可查看到货物详情.如有意向请点击货物详情页面下方与我联系，期待与您合作！(" + DateUtils.getTime("yyyy-MM-dd HH:mm") + ")";
		}
		content.setText(ct);
		content.setEnabled(false);
	}
	
	 private void dataChanged() {  
		 adapter.setPositionTag(-1);
		 adapter.notifyDataSetChanged();  
	 } 
	
	private void sendNotify(Long userId, String userName, String shipNames) {
		Map<String, String> map = new HashMap<String, String>();
		NotifyEvent ne = new NotifyEvent(map);
		ne.setContent(content.getText().toString()+"与货物相匹配的船舶有：" + shipNames + "。");
		ne.setLoginSource(LoginSourceCode.mobile);
		ne.setTitle(title.getText().toString());
		ne.setMessageType(MessageConstants.NOTIFY_EVENT);
		
		if (GlobalApplication.getInstance().getUserData() != null) {
			ne.setFromUserId(GlobalApplication.getInstance().getUserData().getId());
			
			if(!"".equals(GlobalApplication.getInstance().getUserData().getNickName()))
				ne.setFromUserName(GlobalApplication.getInstance().getUserData().getNickName());
			else if(!"".equals(GlobalApplication.getInstance().getUserData().getTrueName()))
				ne.setFromUserName(GlobalApplication.getInstance().getUserData().getTrueName());
			else if(!"".equals(GlobalApplication.getInstance().getUserData().getLoginName()))
				ne.setFromUserName(GlobalApplication.getInstance().getUserData().getLoginName());
		}
	
		ne.setMsgType(NotifyTypeCode.cargonotify.toString());
		ne.setToUserId(userId);
		ne.setToUserName(userName);
		
		MessageSender.getInstance().sendNotifyEvent(ne);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("群发通知");
		setRight("发送", new OnClickListener() {

			@SuppressWarnings({ "rawtypes"})
			@Override
			public void onClick(View v) {
				if(shipDatas!=null&&shipDatas.size()>0){
						HashMap<Integer, Boolean> map = CargoNotifyAdapter.getIsSelected();
						Iterator iter = map.entrySet().iterator();
						boolean sel = false;
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							boolean val = (Boolean)entry.getValue();
							if(val){
								sel = true;
								int key = (Integer)entry.getKey();
								List<ShipData> list = shipDatas.get(key);
								if(!list.isEmpty()){
									String shipNames = "";
									for(ShipData sd:list){
										shipNames += sd.getShipName() + "，";
									}
									if(!shipNames.equals(""))
										shipNames = shipNames.substring(0, shipNames.length()-1);
									
									if("broker".equals(notifyObj)){
										sendNotify(list.get(0).getBroker().getId(),list.get(0).getBroker().getTrueName(),shipNames);
									}else{
										sendNotify(list.get(0).getMaster().getId(),list.get(0).getMaster().getTrueName(),shipNames);
									}
								}
							}
						}
					if(!sel){
						Toast.makeText(CargoSendNotifyActivity.this, "请选择通知的船舶", Toast.LENGTH_SHORT).show();
						return;
					}else{	
						Toast.makeText(CargoSendNotifyActivity.this, "通知发送成功", Toast.LENGTH_LONG).show();
						finish();
					}
				}else{
					Toast.makeText(CargoSendNotifyActivity.this, "没有找到该货物匹配的船舶", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public synchronized void loadData(){
		//读取最新船舶列表
		final HashMap<String, Object> hashMap = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("正在查找船舶", "请稍候...");
//				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/cargo/getShips4Notify", hashMap);
				if (cd.getReturnCode().equals("Success")) {
					
					Map<String, List<HashMap<String, Object>>> map = (Map<String, List<HashMap<String, Object>>>) cd.getContent();
					for (String key : map.keySet()) {
						List<ShipData> shipList = new ArrayList<ShipData>();
						List<HashMap<String, Object>>  mlist = map.get(key);
						for(HashMap<String, Object> hm : mlist){
							ShipData shipData = new ShipData(hm);
							shipList.add(shipData);
						}
						shipDatas.add(shipList);
					}
					adapter.initDate();
					adapter.setPositionTag(-1);
					adapter.notifyDataSetChanged();

				} else {
					Toast.makeText(CargoSendNotifyActivity.this,
							cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(CargoSendNotifyActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(CargoSendNotifyActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(CargoSendNotifyActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(CargoSendNotifyActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		};
		if(cargoData==null){
			Toast.makeText(CargoSendNotifyActivity.this, "读取货物数据失败",
					Toast.LENGTH_LONG).show();
			return;
		}
		hashMap.put("id",cargoId);
		hashMap.put("notifyObj",notifyObj);
		dataLoader.getApiResult(handler, "/mobile/cargo/getShips4Notify", hashMap, "get");
	}
	
	@Override
	public void onClick(View v) {}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.position =position;
//		ShipNameData map = (ShipNameData) mListView.getItemAtPosition(position);
//
//		Intent intent = new Intent(this,ShipPreviewActivity.class);
//		intent.putExtra("id", map.getId()+"");
//		intent.putExtra("name", map.getShipName());
//		intent.putExtra("type", 0);
//		startActivity(intent);
		
	}
	@Override
	public void onResume() {//恢复当前查看的船舶位置
		super.onResume();
		mListView.setSelection(position);
	}
}
