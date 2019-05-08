package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.activities.home.view.XListView;
import com.eyunda.third.activities.home.view.XListView.IXListViewListener;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.adapters.ship.ShipMoniterAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoStatusCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 代理船舶 和代理货物
 * @author LiGuang
 *
 */
public class AgentInfoActivity extends CommonActivity implements OnClickListener,IXListViewListener{
	private int bmpW;// 图片宽度
	private int offset = 0;// 动图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int selectedColor;
	private int unSelectedColor;
	private ImageView imageView;
	private TextView tab1;
	private TextView tab2;
	private XListView listView;
	private Data_loader data;
	private ArrayList<Map<String, Object>> dataList;
	private ArrayList<Map<String, Object>> mAppList;
	private ShipMoniterAdapter smpAdapter;
	private String id;
	private AgentCargoAdapter cargoAdapter;
	private Handler mHandler;
	private static final int pageSize = 2;
	private int page=1; //初始页数
	private int totalPages;
	private boolean isFirst=false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_boat_info);
		data = new Data_loader();
		mHandler = new Handler();
		initView();
		InitImageView();
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle(getIntent().getStringExtra("name"));

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
	private void initView() {
		selectedColor = getResources().getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(R.color.tab_title_normal_color);
		tab1 = (TextView) findViewById(R.id.tab_1);
		tab2 = (TextView) findViewById(R.id.tab_2);

		listView = (XListView) findViewById(R.id.operatorDetails);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab1.setTextColor(selectedColor);
		tab2.setTextColor(unSelectedColor);
		dataList = new ArrayList<Map<String, Object>>();
		mAppList = new ArrayList<Map<String, Object>>();
		smpAdapter = new ShipMoniterAdapter(this, dataList);
		listView.setAdapter(smpAdapter);
		id=getIntent().getStringExtra("id");
		getData();
	}
	private ArrayList<Map<String, Object>> getCargoData() {
		final  Map<String,Object> params = new HashMap<String, Object>();
		// 请求
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				mAppList.clear();
				ConvertData cd = new ConvertData(arg0, "/mobile/home/operDetail", params);
				if(cd.getReturnCode().equals("Success")){
					//dataList.clear();
					HashMap<String, Object> ma = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> content = (List<Map<String, Object>>)ma.get("pageCargoData");
					totalPages =((Double)ma.get("pageCargoNo")).intValue();
					//当前页数已经是最后一页了
					if(page>=totalPages) 
						listView.setPullLoadEnable(false);
					int size = content.size();
					for (int i = 0; i < size; i++){
						CargoData cargo = new CargoData(content.get(i));
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("userId", cargo.getOwner().toString());
						map.put("cargoId", cargo.getId().toString());
						map.put("chatName", cargo.getOwner().getNickName());

						map.put("cargoImage", cargo.getCargoImage());
						String name = cargo.getOwner().getTrueName();
						if (name.equals("")) {
							map.put("shipper", "托运人:" + cargo.getOwner().getLoginName());
						} else
							map.put("shipper", "托运人:" + name);
						map.put("cargoName", "货名:" + cargo.getCargoName());
						map.put("cargoType", "货类:" + cargo.getCargoType().getDescription());
						map.put("wrapCount", "数量:" + cargo.getTonTeu());
						map.put("totalWeight", "总重:" + cargo.getTonTeu() + "吨");
						map.put("number", "货类:" + cargo.getCargoType().getDescription());
						map.put("timelimit", cargo.getPeriodCode());
						map.put("port", cargo.getStartFullName() + " 到  " + cargo.getEndFullName());
						map.put("unitPrice", "单价:" + cargo.getPriceDes());
						map.put("totalPrice", "总价:" + cargo.getTransFeeDes());
						map.put("endTime", "截止:" + cargo.getPeriodTime());
						map.put("remark", cargo.getRemark());
						map.put("type", cargo.getCargoType().toString());
						map.put("cargoStatus", cargo.getCargoStatus().getDescription());

						map.put("cargoData", cargo);
						map.put("phone", cargo.getOwner().getMobile());

						mAppList.add(map);


					}
					cargoAdapter.notifyDataSetChanged();


				} else {
					Toast.makeText(AgentInfoActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(AgentInfoActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(AgentInfoActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(AgentInfoActivity.this, content, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(AgentInfoActivity.this, "请求失败", Toast.LENGTH_LONG).show();
			}
		};
		params.put("id", id);
		params.put("pageNo", page);
		data.getApiResult(handler, "/mobile/home/operDetail", params, "get");
		return mAppList;

	}

	class AgentCargoAdapter extends  BaseAdapter{
		private Activity mContext;   
		private List<Map<String, Object>> listItems; 
		private LayoutInflater listContainer; 
		private ViewHolder  holder;
		private ImageLoader mImageLoader;
		DialogUtil dialogUtil;
		ProgressDialog dialog;

		public AgentCargoAdapter(Activity context, ArrayList<Map<String, Object>> listItems) {
			this.mContext = context;  
			listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文    
			this.listItems = listItems;  
			data = new Data_loader();
			dialogUtil = new DialogUtil(mContext);
			mImageLoader = ImageLoader.getInstance();
		}
		@Override
		public int getCount() {
			return  listItems.size();
		}
		@Override
		public Object getItem(int position) {
			return listItems.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {   
				//获取list_item布局文件的视图    
				convertView = listContainer.inflate(R.layout.cargo_agetnt_adapter, null);
				holder = new ViewHolder();
				//获取控件对象    
				holder.cargoId =(TextView)convertView.findViewById(R.id.list_cargoId);
				holder.cargoImg =(ImageView)convertView.findViewById(R.id.list_img);      //图片
				holder.cargoName=(TextView)convertView.findViewById(R.id.list_cargoName); //货名
				holder.cargoType=(TextView)convertView.findViewById(R.id.list_cargoType); //货类
				holder.total=(TextView)convertView.findViewById(R.id.list_weight); //总重
				holder.shipper=(TextView)convertView.findViewById(R.id.list_shipper); //总重

				holder.volume  =(TextView)convertView.findViewById(R.id.list_volume);  //重量/体积/数量
				holder.port =(TextView)convertView.findViewById(R.id.list_place);   //起止港
				holder.unitPrice =(TextView)convertView.findViewById(R.id.unit_price);   //单价
				holder.totalPrice =(TextView)convertView.findViewById(R.id.total_price);   //总价
				holder.endTime =(TextView)convertView.findViewById(R.id.list_endtime);   //截止日

				//设置控件集到convertView    
				convertView.setTag(holder); 


			}else {    
				holder = (ViewHolder)convertView.getTag();    
			}
			if(listItems.get(position).get("cargoImage").equals("")){
				mImageLoader.displayImage(ApplicationConstants.SERVER_URL+ "/img/cargoImage/" +listItems.get(position).get("type")+".jpg", 
						holder.cargoImg,MyshipAdapter.displayImageOptions);
			}else
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +listItems.get(position).get("cargoImage").toString(), 
					holder.cargoImg,MyshipAdapter.displayImageOptions);
			holder.cargoId.setText((String) listItems.get(position).get("cargoId"));  
			holder.cargoName.setText((String) listItems.get(position).get("cargoName"));  
			holder.cargoType.setText((String) listItems.get(position).get("cargoType"));  
			holder.shipper.setText((String) listItems.get(position).get("shipper"));  
			holder.total.setText((String) listItems.get(position).get("totalWeight"));
			holder.volume.setText((String) listItems.get(position).get("wrapCount"));  
			holder.port.setText((String) listItems.get(position).get("port"));  
			holder.unitPrice.setText((String) listItems.get(position).get("unitPrice"));  
			holder.totalPrice.setText((String) listItems.get(position).get("totalPrice"));  
			holder.endTime.setText((String) listItems.get(position).get("endTime"));  
			// holder.status.setText((String) listItems.get(position).get("cargoStatus"));
			//监听
			return convertView;
		}

		public  class ViewHolder{ 
			public Button btnDelOper;
			public Button btnDeleteOper;
			public TextView phone;
			//自定义控件集合     
			public ImageView cargoImg;
			public TextView  cargoId;
			public TextView  shipper;
			public TextView  cargoType; //货类
			public TextView cargoName;  //货名     
			public TextView volume;     //箱（货）量 
			public TextView total;     //总重 
			public TextView port;       //港口  
			public TextView unitPrice;   //单价
			public TextView totalPrice;  //总价 
			public TextView endTime;    //截止
			public TextView status;     //状态
		}


	}



	private  ArrayList<Map<String, Object>> getData(){
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
				ConvertData cd = new ConvertData(arg0, "/mobile/home/operDetail", params);
				if (cd.getReturnCode().equals("Success")) {
					dialog.dismiss();
					if(isFirst) dataList.clear();
					HashMap<String, Object> ma = (HashMap<String, Object>)cd.getContent();
					List<Map<String, Object>> content = (List<Map<String, Object>>)ma.get("pageShipData");
					totalPages =((Double)ma.get("pageShipNo")).intValue();
					//当前页数已经是最后一页了
					if(page>=totalPages) 
						listView.setPullLoadEnable(false);
					int size = content.size();
					for(int i=0;i<size;i++){
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
						map.put("btnPublish", false);// 实时动态按钮

						dataList.add(map);
					}
					smpAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(AgentInfoActivity.this, cd.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0,String content) {
				super.onFailure(arg0,content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(AgentInfoActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(AgentInfoActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(AgentInfoActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(AgentInfoActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();							
			}			
		};		

		params.put("id", id);
		params.put("pageNo", page);
		data.getApiResult(handler , "/mobile/home/operDetail",params,"get");
		return dataList;		
	}


	@Override
	public void onClick(View v) {
		isFirst=true;
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		switch (v.getId()) {
		case R.id.tab_1: 
			currIndex = 0;
			Animation animation = new TranslateAnimation(one * currIndex, 0, 0, 0);
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			tab1.setTextColor(selectedColor);
			tab2.setTextColor(unSelectedColor);
			getData();
			smpAdapter = new ShipMoniterAdapter(this, dataList);
			listView.setAdapter(smpAdapter);
			listView.setOnItemClickListener(lis);
			break;
		case R.id.tab_2: 
			currIndex = 1;
			Animation animation2 = new TranslateAnimation(one * currIndex, one, 0, 0);
			animation2.setFillAfter(true);
			animation2.setDuration(300);
			imageView.startAnimation(animation2);
			tab1.setTextColor(unSelectedColor);
			tab2.setTextColor(selectedColor);
			getCargoData();
			cargoAdapter = new AgentCargoAdapter(this, mAppList);
			listView.setAdapter(cargoAdapter);
			listView.setOnItemClickListener(los);
			break;

		}

	}
	OnItemClickListener los = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
			Intent intent = new Intent(AgentInfoActivity.this, CargoPreviewActivity.class);
			intent.putExtra("id", map.get("cargoId").toString());
//			intent.putExtra("userId", Long.parseLong(map.get("userId").toString()));
//			intent.putExtra("phone", map.get("phone").toString());
//			intent.putExtra("chatName", map.get("chatName").toString());
			intent.putExtra("name", map.get("cargoName").toString());
			startActivity(intent);
			
		}
	};
	
	OnItemClickListener lis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			HashMap<String, Object> map = (HashMap<String, Object>)listView.getItemAtPosition(position);
			Intent intent=new Intent(AgentInfoActivity.this,ShipPreviewActivity.class);	
			intent.putExtra("id", map.get("shipId").toString());    //船id
			intent.putExtra("name",map.get("shipName").toString());   
			startActivity(intent);
			
		}
	};
	
	/** 停止刷新， */
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}
	// 下拉刷新
		@Override
		public void onRefresh() {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(page<totalPages){
						page++;
						getData();

					}
					listView.setAdapter(smpAdapter);
					smpAdapter.notifyDataSetChanged();
					onLoad();
				}
			}, 2000);
		}
		// 加载更多
		@Override
		public void onLoadMore() {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(page<totalPages){
						page++;
						getData();

					}
					onLoad();
				}
			}, 2000);
		}
	

}
