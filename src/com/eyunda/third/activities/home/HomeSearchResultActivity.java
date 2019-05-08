package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.home.EydShipSearchAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.OperatorData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.ShipPriceData;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 分类搜索结果
 * 
 * @author
 *
 */
public class HomeSearchResultActivity extends CommonListActivity implements
		OnItemClickListener, OnScrollListener {
	Data_loader dataLoader;

	protected ListView listView;
	private BaseAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;
	private int page = 1;
	private int type;
	private String param;
	private String title;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadMoreText;
	private int visibleLastIndex = 0;// 最后的可视项索引 ;
	protected int totalPages;
	private int visibleItemCount;
	private TextView loadingText;
	Map<String, Object> apiParams = new HashMap<String, Object>();
	String url = "";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_search_result_list);
		dataList = new ArrayList<Map<String, Object>>();
		dataLoader = new Data_loader();
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		type = (int) bundle.getLong("type");// 根据type决定adapter
		param = (String) bundle.getString("param");// 区域Code_当前选择的ID
		title = (String) bundle.getString("title");// 当前选择的标题名
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView = (ListView) findViewById(R.id.listview);
		listView.addFooterView(loadMoreView); // 设置列表底部视图
		loadMoreButton = (ProgressBar) loadMoreView
				.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		setAdapter(type);
		loadDate();
		loadDate(page);
	}

	@Override
	protected void loadDate() {
		loadMoreText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击查看更多，圈显示，文字显示加载更多，继续加载
				loadMoreButton.setVisibility(View.VISIBLE);
				if (page < totalPages) {
					page = page + 1;
					loadMoreText.setText("加载中...");

					loadDate(page);

				} else {
					listView.removeFooterView(loadMoreView);
					Toast.makeText(HomeSearchResultActivity.this, "数据全部加载完!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			// 移动到最底部，文字变为查看更多
			if (page < totalPages)
				loadMoreText.setText("查看更多");
			else
				listView.removeFooterView(loadMoreView);

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;

		Log.v("========================= ", "========================");
		Log.v("firstVisibleItem = ", firstVisibleItem + "");
		Log.v("visibleItemCount = ", visibleItemCount + "");
		Log.v("totalItemCount = ", totalItemCount + "");
		Log.v("========================= ", "========================");

		// 如果所有的记录选项等于数据集的条数，则移除列表底部视图
		// if(visibleItemCount == totalItemCount+1){
		// listView.removeFooterView(loadMoreView);
		// Toast.makeText(this, "数据全部加载完!", Toast.LENGTH_LONG).show();
		// }

	}

	AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			loadMoreView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			// dialog.dismiss();
			listView.removeFooterView(loadMoreView);
			if (content != null && content.equals("socket time out")) {
				Toast.makeText(HomeSearchResultActivity.this, "连接服务器超时",
						Toast.LENGTH_LONG).show();
			} else if (content != null) {
				Toast.makeText(HomeSearchResultActivity.this, content,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(HomeSearchResultActivity.this, "网络连接异常",
						Toast.LENGTH_LONG).show();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			loadMoreButton.setVisibility(View.GONE);
			loadingText.setVisibility(View.GONE);
			ConvertData cd = new ConvertData(res, url, apiParams);
			String jzx = CargoBigTypeCode.container.getDescription();
			HashMap<String, Object> var = (HashMap<String, Object>) cd
					.getContent();
			if (cd.getReturnCode().equals("Success")) {
				// 根据type决定处理方式
				switch (type) {
				case HomeCategoryActivity.MSG_DLR_LIST:// 金牌代理列表
					totalPages = ((Double) var.get("total")).intValue();
					List<Map<String, Object>> operatorList = (ArrayList<Map<String, Object>>) var
							.get("operatorList");
					for (Map<String, Object> mm : operatorList) {
						OperatorData od = new OperatorData(mm);

						Map<String, Object> map = new HashMap<String, Object>();
						map.put("agentLogo", od.getUserData().getUserLogo());
						map.put("id", od.getUserId().toString());
						map.put("name", od.getUserData().getMobile());

						map.put("daiLi", "代理船舶数:" + od.getShipNumber());
						map.put("cjht", "成交合同数:" + od.getOrderNumber());
						map.put("area", "地区:" + od.getUserData().getUnitAddr());
						map.put("loginName", od.getUserData().getTrueName());
						map.put("loginName4site", od.getUserData().getLoginName());
						map.put("userData", od.getUserData());
						map.put("nickName", "昵称:"
								+ od.getUserData().getNickName());
						map.put("companyName", "公司名称:"
								+ od.getUserData().getUnitName());
						map.put("email", "邮箱:" + od.getUserData().getEmail());
						map.put("isCreate", od.getUserData().isCreatSite());
						String trueName = od.getUserData().getTrueName();
						if (trueName.equals("")) {

							map.put("chatName", od.getUserData().getLoginName());
						} else
							map.put("chatName", trueName);
						map.put("userId", od.getUserId().toString());
						map.put("phone", od.getUserData().getNickName());
						dataList.add(map);
					}
					smpAdapter.notifyDataSetChanged();
					break;
				case HomeCategoryActivity.MSG_SHIP_LIST: // 船舶分类
					Double num2 = (Double) var.get("pageNo");
					totalPages = (Double.valueOf(num2)).intValue();
					List<Map<String, Object>> shipList = (ArrayList<Map<String, Object>>) var
							.get("myShipDatas");
					// List<Map<String, Object>> priceList =(List<Map<String,
					// Object>>) var.get("myShipPriceDatas");
					for (Map<String, Object> mm : shipList) {
						Map<String, Object> map = new HashMap<String, Object>();
						ShipData shipData = new ShipData(mm);
						List<ShipStopData> arvLftList = shipData
								.getShipArvlftDatas();
						for (ShipStopData arvLft : arvLftList) {
							map.put("port", "动态:" + arvLft.getArvlftDesc());
						}

						map.put("area", "总吨:" + shipData.getSumTons() + "吨");
						String trueName = shipData.getMaster()
								.getTrueName();
						if (trueName.equals("")) {

							map.put("shipper", "承运人:"
									+ shipData.getMaster().getLoginName());
						} else
							map.put("shipper", "承运人:" + trueName);
						if (shipData.getReleaseTime().equals("")) {
							map.put("endtime", "发布日期:无"); // 发布日期
						} else
							map.put("endtime",
									"发布日期:" + shipData.getReleaseTime()); // 发布日期
						map.put("shipType", "类别:"
								+ shipData.getTypeData().getTypeName());

						map.put("shipLogo", shipData.getShipLogo());
						map.put("shipName", shipData.getShipName());
						map.put("MMSI", "MMSI:" + shipData.getMmsi());
						map.put("id", shipData.getId().toString());
						map.put("endTime", "截止:" + shipData.getReleaseTime());

						dataList.add(map);
					}

					smpAdapter.notifyDataSetChanged();
					break;

				case HomeCategoryActivity.MSG_SHIP_LINE: // 船舶地区
					Double num4 = (Double) var.get("pageNo");
					totalPages = (Double.valueOf(num4)).intValue();
					List<Map<String, Object>> sailList = (ArrayList<Map<String, Object>>) var
							.get("sailList");
					for (int i = 0; i < sailList.size(); i++) {
						Map<String, Object> price = sailList.get(i);
						ShipPriceData priceData = new ShipPriceData(
								(Map<String, Object>) price
										.get("shipPriceData"));
						ShipData shipData = new ShipData(
								(Map<String, Object>) price.get("shipData"));
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("id", shipData.getId().toString());
						map.put("shipLogo", shipData.getShipLogo());
						map.put("shipTitle", shipData.getKeyWords());
						map.put("shipName", shipData.getShipName());
						map.put("MMSI", "MMSI:" + shipData.getMmsi());
						map.put("unitprice", "单价:" + priceData.getPriceDes());
						map.put("totalprice",
								"总报价:" + priceData.getIntTransFeeDes());
						map.put("endTime", "截止:" + shipData.getReleaseTime());
						map.put("phoneNum", "电话:"
								+ shipData.getMaster().getMobile());
						map.put("place", priceData.getStartPortData()
								.getFullName()
								+ " 到 "
								+ priceData.getEndPortData().getFullName());

						if (priceData.getCargoType().getDescription()
								.contains(jzx))
							map.put("toalWeight",
									"箱量:" + priceData.getTonTeuDes());
						else
							map.put("toalWeight",
									"货量:" + priceData.getTonTeuDes());

						dataList.add(map);
					}
					listView.setSelection(listView.getAdapter().getCount() - 1);
					smpAdapter.notifyDataSetChanged();

					break;
				case HomeCategoryActivity.MSG_CARGRO_CATE_LIST:// 货物分类
					Double num3 = (Double) var.get("pageNo");
					totalPages = (Double.valueOf(num3)).intValue();
					List<Map<String, Object>> cargoList = (ArrayList<Map<String, Object>>) var
							.get("cargoList");
					for (Map<String, Object> mm : cargoList) {
						CargoData cargoData = new CargoData(mm);

						Map<String, Object> maps = new HashMap<String, Object>();

						maps.put("cargoImg", cargoData.getCargoImage());
						maps.put("id", cargoData.getId().toString());
						if (cargoData.getCargoType().getCargoBigType()
								.getDescription().equals(jzx)) {
							maps.put("shipName", cargoData.getCargoType().getCargoBigType()
									.getDescription()+"(货号:"+cargoData.getId().toString()+")");
						} else
							maps.put("shipName", cargoData.getCargoName()+"(货号:"+cargoData.getId().toString()+")");

						maps.put("cargoCode", "货号:"
								+ cargoData.getId().toString());
						String shipper = cargoData.getOwner().getTrueName();
						if (shipper.equals("")) {
							maps.put("shipper", "托运人:"
									+ cargoData.getOwner().getLoginName());
						} else
							maps.put("shipper", "托运人:" + shipper);
						maps.put("volume", "货量/箱量：" + cargoData.getTonTeuDes());
						maps.put("place", cargoData.getStartFullName() + " 到 "
								+ cargoData.getEndFullName());
						maps.put("type", cargoData.getCargoType().toString());
						maps.put("unitPrice", "单价：" + cargoData.getPriceDes());
//						maps.put("totalPrice",
//								"总价:" + cargoData.getTransFeeDes());
						maps.put("endTime", "截止:" + cargoData.getPeriodTime());

						maps.put("chatName", cargoData.getOwner().getNickName());
						maps.put("userId", cargoData.getOwner().getId()
								.toString());
						maps.put("phone", cargoData.getOwner().getMobile());

						if (cargoData.getCargoType().getDescription()
								.contains(jzx)) {
							maps.put("volume", "箱量:" + cargoData.getTonTeuDes());
							maps.put("totalPrice","箱量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						} else{
							maps.put("volume", "货量:" + cargoData.getTonTeuDes());
							maps.put("totalPrice","货量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						}	
						dataList.add(maps);
					}
					smpAdapter.notifyDataSetChanged();
					break;
				case HomeCategoryActivity.MSG_CARGRO_AREA_LIST:// 货物地区
					Double num5 = (Double) var.get("pageNo");
					totalPages = (Double.valueOf(num5)).intValue();
					List<Map<String, Object>> cargolList = (ArrayList<Map<String, Object>>) var
							.get("cargoList");
					for (Map<String, Object> mm : cargolList) {
						Map<String, Object> map = new HashMap<String, Object>();
						CargoData cargoData = new CargoData(mm);

						map.put("phone", cargoData.getOwner().getMobile()); // 电话
						map.put("userId", cargoData.getOwnerId()); // Id,聊天对象用
						map.put("chatName", cargoData.getOwner().getNickName()); // 人名

						map.put("id", cargoData.getId().toString());
						map.put("cargoImg", cargoData.getCargoImage());
						map.put("cargoCode", "货号:"
								+ cargoData.getId().toString());
						String shipper = cargoData.getOwner().getTrueName();
						if (shipper.equals("")) {
							map.put("shipper", "托运人:"
									+ cargoData.getOwner().getLoginName());
						} else
							map.put("shipper", "托运人:" + shipper);

						map.put("place", cargoData.getStartFullName() + " 到 "
								+ cargoData.getEndFullName());
						if (cargoData.getCargoType().getCargoBigType()
								.getDescription().equals(jzx)) {
							map.put("shipName", cargoData.getCargoType().getCargoBigType()
									.getDescription()+"(货号:"+cargoData.getId().toString()+")");
						} else
							map.put("shipName", cargoData.getCargoName()+"(货号:"+cargoData.getId().toString()+")");
						map.put("type", cargoData.getCargoType().toString());
						map.put("unitPrice", "单价:" + cargoData.getPriceDes());
//						map.put("totalPrice",
//								"总价:" + cargoData.getTransFeeDes());
						map.put("endTime", "截止:" + cargoData.getPeriodTime());

						if (cargoData.getCargoType().getDescription()
								.contains(jzx)){
							map.put("volume", "箱量:" + cargoData.getTonTeuDes());
							map.put("totalPrice","箱量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						}else{
							map.put("volume", "货量:" + cargoData.getTonTeuDes());
							map.put("totalPrice","货量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						}
						dataList.add(map);
					}
					smpAdapter.notifyDataSetChanged();
					break;
				case HomeCategoryActivity.MSG_DYNAMIC_LIST: // 动态
					Double num6 = (Double) var.get("pageNo");
					totalPages = (Double.valueOf(num6)).intValue();
					break;
				}
			} else {
				Toast.makeText(HomeSearchResultActivity.this, cd.getMessage(),
						Toast.LENGTH_SHORT).show();

			}
			// dialog.dismiss();
		}

	};

	@Override
	protected void onStart() {
		super.onStart();
		if ("".equals(title)) {
			setTitle("查找结果");
		} else {
			setTitle(title);
		}

	}

	protected void setParams(HashMap<String, Object> result, String arg0) {
		Gson gson = new Gson();
		if (result.containsKey(ApplicationConstants.CONTENTMD5CHANGED)) {
			boolean contentMd5Changed = (Boolean) result
					.get(ApplicationConstants.CONTENTMD5CHANGED);
			SharedPreferencesUtils sp = new SharedPreferencesUtils(url,
					apiParams);
			if (contentMd5Changed && NetworkUtils.isNetworkAvailable()) {
				sp.setParam(arg0);
			} else {
				String localJson = sp.getParam();
				result = gson.fromJson(localJson,
						new TypeToken<Map<String, Object>>() {
						}.getType());
			}
		}
	}

	private void loadDate(int page2) {

		switch (type) {
		case HomeCategoryActivity.MSG_DLR_LIST:// 金牌代理列表
			apiParams.put("roleCode", param);
			apiParams.put("pageNo", page2);
			url = "/mobile/home/operatorList";

			break;
		case HomeCategoryActivity.MSG_SHIP_LIST:// 船舶分类列表
			String paramSplit2[] = param.split("_");
			apiParams.put("attrValueCode", paramSplit2[1]);
			apiParams.put("bigAreaCode", Long.parseLong(paramSplit2[0]));
			apiParams.put("shipSort", 1);
			apiParams.put("pageNo", page2);
			url = "/mobile/home/sortList/show";

			break;
		case HomeCategoryActivity.MSG_CARGRO_CATE_LIST:// 货运分类
			String paramSplit3[] = param.split("_");
			apiParams.put("cargoTypeCode", paramSplit3[1]);
			apiParams.put("bigAreaCode", Long.parseLong(paramSplit3[0]));
			apiParams.put("pageNo", page2);
			url = "/mobile/home/sortCargoList";

			break;
		case HomeCategoryActivity.MSG_CARGRO_AREA_LIST:// 货物地区
			String paramSplit4[] = param.split("_");
			apiParams.put("cityPortNo", paramSplit4[1]);
			apiParams.put("pageNo", page2);
			url = "/mobile/home/cargoList";

			break;
		case HomeCategoryActivity.MSG_SHIP_LINE:// 船舶地区
			String paramSplit5[] = param.split("_");
			apiParams.put("cityPortNo", paramSplit5[1]);
			apiParams.put("pageNo", page2);
			url = "/mobile/home/shipSailLineList";

			break;

		}
		// 调用接口
		dataLoader.getApiResult(showHandler, url, apiParams, "get");

	}

	private void setAdapter(int type) {

		smpAdapter = new EydShipSearchAdapter(HomeSearchResultActivity.this,
				dataList, type);
		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(smpAdapter);

		listView.setOnItemClickListener(this);

	}

	/**
	 * 跳转到各个分类详情页
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> map = (HashMap<String, Object>) listView
				.getItemAtPosition(position);

		if (type == HomeCategoryActivity.MSG_CARGRO_CATE_LIST
				|| type == HomeCategoryActivity.MSG_CARGRO_AREA_LIST) {
			Intent intent = new Intent(this, CargoPreviewActivity.class);
			intent.putExtra("id", map.get("id").toString()); // 传输的船、货id
			intent.putExtra("name", map.get("shipName").toString()); // 船、货名称
			intent.putExtra("userId",
					Long.parseLong(map.get("userId").toString()));
			intent.putExtra("chatName", map.get("chatName").toString());
			if (map.get("phone") != null)
				intent.putExtra("phone", map.get("phone").toString());
			startActivity(intent);
		} else if (type == HomeCategoryActivity.MSG_DLR_LIST) {
			// 判断用户是否有网站，有网站则跳转到网站
//			String isCreateSite = (String)map.get("isCreate").toString();
			Intent intent = null;
//			if (isCreateSite.equals("true")) {
				//加载webView
				intent = new Intent(this, UserSiteActivity.class);
				User user = new User();
				user.setUserData((UserData) map.get("userData"));
				intent.putExtra("toChatUser", user);
//			} else {
//				intent = new Intent(this, AgentInfoActivity.class);
//				intent.putExtra("name", map.get("chatName").toString());
//				intent.putExtra("id", map.get("id").toString());
//			}
			startActivity(intent);

		} else {
			Intent intent = new Intent(this, ShipPreviewActivity.class);
			intent.putExtra("id", map.get("id").toString()); // 传输的船、货id
			intent.putExtra("name", map.get("shipName").toString()); // 船、货名称
			startActivity(intent);
		}
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

}
