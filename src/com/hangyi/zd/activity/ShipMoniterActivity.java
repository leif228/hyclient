package com.hangyi.zd.activity;

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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.map.ShipDistributeActivity;
import com.eyunda.third.activities.ship.ShipLineListActivity;
import com.eyunda.third.activities.ship.widget.AlertSearchDialog;
import com.eyunda.third.adapters.ship.ShipMoniterAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.CollectCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶监控
 * 
 * @author
 *
 */
public class ShipMoniterActivity extends CommonActivity implements
OnItemClickListener,OnClickListener,OnScrollListener {
	Data_loader data;
	private ShipMoniterAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText, tab1, tab2;
	private TextView loadMoreText;
	private int visibleLastIndex=0;//最后的可视项索引 ;
	private int visibleItemCount;
	Boolean isLoad =false;
	Boolean isFirst=true;
	private ListView mlistView;
	protected int totalPages;
	private   int page=1;
	private String searchName ="";
	
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
		setContentView(R.layout.eyd_ship_monitor_list);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
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
		
		mlistView =  (ListView)findViewById(R.id.mlistView);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		mlistView.addFooterView(loadMoreView);    //设置列表底部视图
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		mlistView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		dataList =new ArrayList<Map<String,Object>>();
		smpAdapter = new ShipMoniterAdapter(this, dataList);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
		getData();
		loadDate();
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
				isFirst=false;
				isLoad=true;
				if(page<totalPages){
					loadMoreText.setText("加载中...");
					page++;
					getData();
				}
			}
		});	
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶监控");
		final UserData user = GlobalApplication.getInstance().getUserData();
	     if(user!=null){

			
			setRight("分布图", new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 分布
					Intent intent = new Intent(ShipMoniterActivity.this,
							ShipDistributeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("userId", user.getId()+"");// Ship ID
					bundle.putString("searchName", searchName );
					bundle.putInt("pageNo", page );
					bundle.putString("collectCode", current.name());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			setRight(R.drawable.ic_action_search, new OnClickListener() {

				@Override
				public void onClick(View v) {
					final AlertSearchDialog ad = new AlertSearchDialog(ShipMoniterActivity.this);
					ad.showAddDialog(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						View view =   ad.getView();
						 EditText editTextName = (EditText) view.findViewById(R.id.et_search);
						 resetFooter();
						 searchName =editTextName.getText().toString();
							getData();
							
						}
					});
				}
			});
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
				//mlistView.removeFooterView(loadMoreView);
				loadMoreView.setVisibility(View.GONE);
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		Log.d("visibleItemCount", this.visibleItemCount+"");
	}

	private void getData() {
		final Map<String, Object> params = new HashMap<String, Object>();

		 UserData user = GlobalApplication.getInstance().getUserData();

		// 请求
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0, "/mobile/monitor/myAllShip", params);
				if (cd.getReturnCode().equals("Success")) {

					HashMap ma = (HashMap<String, String>) cd.getContent();

					List content = (ArrayList<String>) ma.get("shipDatas");

					int size = content.size();
					totalPages =((Double)ma.get("totalPages")).intValue();
			
					if(isFirst){//初始显示10条数据
						dataList.clear();
						for(int i=0;i<size;i++)
						setData(content, i);
					}else{
						for(int i=0;i<size;i++)
							setData(content, i);
					}
					
					smpAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ShipMoniterActivity.this,cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				mlistView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipMoniterActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipMoniterActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(ShipMoniterActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipMoniterActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();
			}
		};

		params.put("selectCode", current.name());
		params.put("keyWords", searchName );
		params.put("pageNo", page );
		data.getApiResult(handler, "/mobile/monitor/myAllShip", params, "get");
	}

	private void setData(List<Map<String, Object>> content, int i) {
		ShipData shipData = new ShipData(
				(Map<String, Object>) content.get(i));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shipId", shipData.getId().toString()); // shipId
		map.put("shipLogo", shipData.getShipLogo());// Logo
		map.put("shipName", shipData.getShipName());// 船名
		map.put("shipType", "类别:"
				+ shipData.getTypeData().getTypeName()); // 类型
		map.put("TypeCode", shipData.getShipType()); // 编码
		map.put("place", "总吨:"
				+ shipData.getSumTons()+"吨");// 港口
		map.put("MMSI", "MMSI:" + shipData.getMmsi()); // MMsi
		String shipper=shipData.getMaster().getTrueName();
		if(shipper.equals("")){
			map.put("carrier", "承运人:"+shipData.getMaster().getLoginName());//承运人		
		}else
			map.put("carrier", "承运人:"+shipper);//承运人	

		if (shipData.getReleaseTime().equals("")) {
			map.put("endtime", "发布日期:无"); // 发布日期
		} else
			map.put("endtime",
					"发布日期:" + shipData.getReleaseTime()); // 发布日期
		// shipData.getReleaseStatus().toString();//发布状态
		
		map.put("dongtai", "动态:"+shipData.getArvlftDesc()); //动态
		map.put("btnPublish", true);// 实时动态按钮
		dataList.add(map);
		

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// String text = listView.getItemAtPosition(position)+"";
		HashMap<String, Object> map = (HashMap<String, Object>) mlistView.getItemAtPosition(position);
		Intent intent = new Intent(ShipMoniterActivity.this,ShipLineListActivity.class);
		// 绑定传输的船舶数据
		intent.putExtra("shipId", map.get("shipId").toString());
		intent.putExtra("shipName", map.get("shipName").toString());
		intent.putExtra("mmsi", map.get("MMSI").toString());
		startActivity(intent);
	}

	private void resetFooter() {
		isLoad =false;
		isFirst=true;
		loadMoreView.setVisibility(View.VISIBLE);
		loadMoreButton.setVisibility(View.VISIBLE);
		loadMoreText.setText("");
	}
	@Override
	public void onClick(View v) {
		current = CollectCode.mySelfShips;
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		switch (v.getId()) {
		case R.id.tab_1: // 我的未过期
			currIndex = 0;
			Animation animation = new TranslateAnimation(one * currIndex, 0, 0, 0);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			current = CollectCode.mySelfShips;
			tab1.setTextColor(selectedColor);
			tab2.setTextColor(unSelectedColor);
			resetFooter();
			getData();

			break;
		case R.id.tab_2: // 收藏的
			currIndex = 1;
			Animation animation2 = new TranslateAnimation(one * currIndex, one, 0, 0);
			animation2.setFillAfter(true);// True:图片停在动画结束位置
			animation2.setDuration(300);
			imageView.startAnimation(animation2);
			current = CollectCode.collectShips;
			tab1.setTextColor(unSelectedColor);
			tab2.setTextColor(selectedColor);
			resetFooter();
			getData();

			break;

		}
	}

}
