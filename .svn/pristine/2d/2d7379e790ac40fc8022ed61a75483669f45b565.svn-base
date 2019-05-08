package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.AddCargoActivity;
import com.eyunda.third.activities.cargo.CargoActivity;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.ship.widget.AlertSearchDialog;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.CollectCode;
import com.eyunda.third.domain.enumeric.ShipStatusCode;
import com.eyunda.third.domain.enumeric.SourceCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 我的船舶
 * @author
 *
 */
public class MyshipActivity extends CommonActivity implements OnItemClickListener, OnClickListener,OnScrollListener{
	Data_loader data;
	Button menu_basic,menu_class,menu_upload,menu_report;
	private ListView listView;
	private MyshipAdapter smpAdapter;
	private  ArrayList<Map<String, Object>> dataList= new ArrayList<Map<String, Object>>();
	UserData user = GlobalApplication.getInstance().getUserData();
	protected int size;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText,tab1, tab2;
	private int visibleLastIndex=0;//最后的可视项索引 ;
	private int visibleItemCount;
	private TextView loadMoreText;
	protected int totalPages;
	protected int page=1;
	private String searchName ="";
	private LinearLayout erContent;
	
	private int selectedColor;
	private int unSelectedColor;
	private ImageView imageView;
	private int bmpW;// 图片宽度
	private int offset = 0;// 动图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private CollectCode current = CollectCode.mySelfShips;//my代理的船舶，fav收藏的
	/** 页卡总数 **/
	private static final int pageSize = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_myship_list);	
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		page = getIntent().getIntExtra("page", 1);
		initView();
		InitImageView();
	}

	private void initView() {

		selectedColor = getResources().getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(R.color.tab_title_normal_color);
		tab1 = (TextView) findViewById(R.id.tab_1);
		tab2 = (TextView) findViewById(R.id.tab_2);


		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab1.setTextColor(selectedColor);
		tab2.setTextColor(unSelectedColor);
		
		listView =  (ListView)findViewById(R.id.myship_list);
		erContent =(LinearLayout)findViewById(R.id.testContent);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView);    //设置列表底部视图
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		loadMoreView.setOnClickListener(null);
		smpAdapter = new MyshipAdapter(MyshipActivity.this, dataList,current);
		listView.setAdapter(smpAdapter);
		if (user != null) {
			String urs = user.getRoles();
			String code = Integer.toString(UserRoleCode.sailor.ordinal());
			int result = urs.indexOf(code);
			if(!(result >= 0)){//船员不能加货物
				getData();
				loadDate();
			}
		}
	}
	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.iv_tab1);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
		// = 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void loadDate() {
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				if(page<totalPages){
					loadMoreText.setText("加载中...");
					page++;
					getData();
				}else{
					listView.removeFooterView(loadMoreView);
				}
			}
		});	

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("我的船舶");
		if (user != null) {
			String urs = user.getRoles();
			String codeBroker = Integer.toString(UserRoleCode.broker.ordinal());
			String codeHandler = Integer.toString(UserRoleCode.handler.ordinal());
			String codeMaster = Integer.toString(UserRoleCode.master.ordinal());
			boolean result = urs.contains(codeBroker) || urs.contains(codeHandler) || urs.contains(codeMaster);
			if(result){//只有船东、代理人和业务员可添加船舶
				setRight(R.drawable.zx_faver_top, new OnClickListener() {
					@Override
					public void onClick(View v) {
						//新增船舶
						Intent intent = new Intent(MyshipActivity.this,ShipinfoActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("shipId", "0");//Ship ID
						bundle.putLong("id", 0);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				});
				setRightBtn(R.drawable.ic_action_search, new OnClickListener() {
					@Override
					public void onClick(View v) {
						final AlertSearchDialog ad = new AlertSearchDialog(MyshipActivity.this);
						ad.showAddDialog(new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								View view =   ad.getView();
								EditText editTextName = (EditText) view.findViewById(R.id.et_search);
								resetFooter();
								searchName =editTextName.getText().toString();
								dataList.clear();
								getData();

							}
						});
					}
				});
			}else{
				setRight(R.drawable.ic_action_search, new OnClickListener() {
					@Override
					public void onClick(View v) {
						final AlertSearchDialog ad = new AlertSearchDialog(MyshipActivity.this);
						ad.showAddDialog(new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								View view =   ad.getView();
								EditText editTextName = (EditText) view.findViewById(R.id.et_search);
								resetFooter();
								searchName =editTextName.getText().toString();
								dataList.clear();
								getData();

							}
						});
					}
				});
			}
		
		}


	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount()-1;  //数据集最后一项的索引 
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {

			if(page<totalPages){
				loadMoreText.setText("查看更多");
			}else
				listView.removeFooterView(loadMoreView);
			//loadMoreView.setVisibility(View.GONE);
		}

	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	private  ArrayList<Map<String, Object>> getData(){
		final  Map<String,Object> params = new HashMap<String, Object>();

		//请求
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){


			@Override
			public void onStart() {
				super.onStart();
				//dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
				loadMoreView.setVisibility(View.VISIBLE);
			}


			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				//dialog.dismiss();

				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0, "/mobile/ship/myAllShip", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>)cd.getContent();
					List<Map<String, Object>> content = (List<Map<String, Object>>)ma.get("shipDatas");
					totalPages =((Double)ma.get("totalPages")).intValue();
					int size = content.size();
					for(int i=0;i<size;i++){
						ShipData shipData  = new ShipData((Map<String, Object>) content.get(i));
						Map<String, Object> map = new HashMap<String, Object>();					
						//	map.put("id", shipData.getUserId()); //发布人id
						map.put("shipId", shipData.getId().toString()); //shipId
						map.put("shipLogo", shipData.getShipLogo());//Logo

						map.put("shipName", shipData.getShipName());//船名
						map.put("shipType", "类别:"+shipData.getTypeData().getTypeName());  //类型
						map.put("place","总吨:"+ shipData.getSumTons()+"吨");//港口			
						map.put("MMSI", "MMSI:"+shipData.getMmsi()); //MMsi
						map.put("mmsi", shipData.getMmsi()); //MMsi
						map.put("shipData", shipData); //shipData
						String name=shipData.getMaster().getTrueName();
						if(name.equals("")){
							map.put("carrier", "承运人:"+shipData.getMaster().getNickName());//承运人	
						}else
							map.put("carrier", "承运人:"+name);//承运人			
						if(shipData.getReleaseTime().equals("")){
							map.put("endtime", "发布日期:无"); //发布日期
						}else
							map.put("endtime", "发布日期:"+shipData.getReleaseTime()); //发布日期					
						map.put("shipStatic", shipData.getShipStatus().toString());
						map.put("btnEdit", true);//编辑
						map.put("btnDelete", true);//删除
						map.put("btnPublish", true);//发布
						map.put("btnJudge", false);//提交审核
						map.put("btnCancel", false);//取消
						map.put("btnSetting", false);//设置
						map.put("btnPower", false);//权限
						map.put("shipStatic",shipData.getShipStatus().getDescription());
						if(current == CollectCode.mySelfShips){
							if(shipData.getShipStatus()==ShipStatusCode.edit){
								map.put("btnEdit", true);
								map.put("btnDelete", true);
								map.put("btnPublish", false);
								map.put("btnJudge", true);
								map.put("btnSetting", false);
							}else if(shipData.getShipStatus()==ShipStatusCode.commit){
								map.put("btnEdit", true);
								map.put("btnDelete", true);
								map.put("btnPublish", false);
								map.put("btnSetting", false);
								map.put("btnJudge", false);
							}else if(shipData.getShipStatus()==ShipStatusCode.audit){
								map.put("btnEdit", true);
								map.put("btnDelete", true);
								map.put("btnPublish", true);
								map.put("btnSetting", false);
								map.put("btnJudge", false);
							}else if(shipData.getShipStatus()==ShipStatusCode.publish){
								map.put("btnEdit", false);
								map.put("btnDelete", false);
								map.put("btnPublish", false);
								map.put("btnCancel", true);
								map.put("btnSetting", true);
								map.put("btnJudge", false);
								map.put("btnPower", false);   //不用对方同意直接收藏成功
							}
							map.put("btnDues", true);
							map.put("btnGasOrder", true);
							
						}else{
							map.put("btnEdit", false);//编辑
							map.put("btnDelete", true);//删除
							map.put("btnPublish", false);//发布
							map.put("btnJudge", false);
							map.put("btnCancel", false);//取消
							map.put("btnSetting", false);
							map.put("btnPower", false);//权限
							map.put("btnDues", false);//权限
							map.put("btnGasOrder", false);//权限
						}
						//					if(user.getRole().equals(RoleCode.SHIPPROXY)){
						//					    if(!user.getId().equals(shipData.getUserId())){
						//						   map.put("btnEdit", false);
						//						   map.put("btnDelete", false);
						//						   map.put("btnPublish", false);
						//						   map.put("btnCancel", false);
						//					 } 
						//					}
						dataList.add(map);
					}
					smpAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(MyshipActivity.this, cd.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(MyshipActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(MyshipActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(MyshipActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(MyshipActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();							
			}			
		};		

		params.put("selectCode", current.name() );
		params.put("keyWords", searchName );
		params.put("pageNo", page );
		data.getApiResult(handler , "/mobile/ship/myAllShip",params,"get");
		return dataList;		
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		//String text = listView.getItemAtPosition(position)+"";
		HashMap<String, Object> map = (HashMap<String, Object>)listView.getItemAtPosition(position);
		Intent intent=new Intent(MyshipActivity.this,ShipPreviewActivity.class);	
		//绑定传输的船舶数据   
		intent.putExtra("id", map.get("shipId").toString());    //船id
		intent.putExtra("name",map.get("shipName").toString());   

		startActivity(intent);	
	}


	/**重置视图**/
	private void resetFooter() {
		dataList.clear();
		page=1;
		searchName="";
		loadMoreButton.setVisibility(View.VISIBLE);
		loadMoreText.setText("");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		boolean isRefresh = true;
		if(isRefresh){
			dataList.clear();
			refresh();
			smpAdapter.notifyDataSetChanged();
		}
	}

	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			runOnUiThread(new Runnable() {
				public void run() {
					dataList.clear();
					page=1;
					getData();
					//smpAdapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		current = CollectCode.mySelfShips;
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		switch (v.getId()) {
		case R.id.tab_1: // 代理船舶
			currIndex = 0;
			Animation animation = new TranslateAnimation(one * currIndex, 0, 0, 0);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			current = CollectCode.mySelfShips;
			smpAdapter.setCurrent(current);
			tab1.setTextColor(selectedColor);
			tab2.setTextColor(unSelectedColor);
			resetFooter();
			dataList.clear();
			getData();
			top_commit.setVisibility(View.VISIBLE);
			break;
		case R.id.tab_2: // 收藏的
			currIndex = 1;
			Animation animation2 = new TranslateAnimation(one * currIndex, one, 0, 0);
			animation2.setFillAfter(true);// True:图片停在动画结束位置
			animation2.setDuration(300);
			imageView.startAnimation(animation2);
			current = CollectCode.collectShips;
			smpAdapter.setCurrent(current);
			tab1.setTextColor(unSelectedColor);
			tab2.setTextColor(selectedColor);
			resetFooter();
			dataList.clear();
			getData();
			top_commit.setVisibility(View.GONE);
			break;

		}
	}

}
