package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.cargo.CargoListActivity;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.activities.home.view.XListView;
import com.eyunda.third.activities.home.view.XListView.IXListViewListener;
import com.eyunda.third.activities.ship.widget.MyListView;
import com.eyunda.third.adapters.home.RecentCargoAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.ListPlugUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 首页货物
 * @author LiGuang
 *
 */
public class CargoFragment  extends Fragment  implements OnItemClickListener,OnScrollListener {

	private TextView tab01,tab02,tab03,tab04;
	private int selectedColor;
	private int unSelectedColor;
	protected DialogUtil dialogUtil;
	protected ProgressDialog dialog;
	public Map<String, Object> map;
	Boolean isFirst = true;
	AgentInfoActivity mTab01;
	FindCargoActivity mTab04;
	private MyListView mListView;
	private RecentCargoAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	private Data_loader data = new Data_loader();
	private Handler mHandler;
	private int position=0;
	private int page=1;
	private int totalPages;
	private ScrollView scrollView;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadMoreText;
	private TextView loadingText;
	private int visibleLastIndex;
	private int visibleItemCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_cargo, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		getData();
	}
	private void initView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);
		dialogUtil = new DialogUtil(getActivity());
        //默认最新货物
        mListView=(MyListView)this.getView().findViewById(R.id.findCargo);
       loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.loadmore, null);
		
         mListView.addFooterView(loadMoreView);    //设置列表底部视图,需在setAdapter之前调用
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
        smpAdapter = new RecentCargoAdapter(getActivity(),dataList);
		mListView.setAdapter(smpAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
//      设置让它上拉，FALSE为不让上拉，便不加载更多数据
		tab01=(TextView)this.getView().findViewById(R.id.tab01);
		tab02=(TextView)this.getView().findViewById(R.id.tab02);
		tab03=(TextView)this.getView().findViewById(R.id.tab03);
		tab04=(TextView)this.getView().findViewById(R.id.tab04);
		tab01.setTextColor(unSelectedColor);
		tab02.setTextColor(unSelectedColor);
		tab03.setTextColor(unSelectedColor);
		tab04.setTextColor(unSelectedColor);
		
		tab01.setOnClickListener(new MyOnClickListener(0));
		tab02.setOnClickListener(new MyOnClickListener(1));
		tab03.setOnClickListener(new MyOnClickListener(2));
		tab04.setOnClickListener(new MyOnClickListener(3));
        loadDate();
	}
	
	private void loadDate() {
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				if(page<totalPages){
					loadMoreText.setText("查看更多");
					page++;
					getData();
				}else{
					mListView.removeFooterView(loadMoreView);
				}
			}
		});	
		
	}
	private ArrayList<Map<String, Object>> getData() {
		final Map<String, Object> apiParams = new HashMap<String, Object>();
		final String jzx =CargoBigTypeCode.container.getDescription();
		AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler(){
			
			@Override
			public void onStart(){
				loadMoreView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0,"/mobile/home/cargoHome",apiParams);
				if(cd.getReturnCode().equals("Success")){
					HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
					totalPages = ((Double) var.get("totalPage")).intValue();
					if(totalPages <=1  || page == totalPages){
						mListView.removeFooterView(loadMoreView);
					}else{
						loadMoreText.setText("查看更多");
						loadMoreText.setVisibility(View.VISIBLE); 
					}
					List<Map<String, Object>> cargoList =(ArrayList<Map<String, Object>>)var.get("cargoPageData");
					for(Map<String, Object> mm : cargoList){
						CargoData cargoData = new CargoData(mm);

						Map<String, Object> maps = new HashMap<String, Object>();

						maps.put("cargoImg", cargoData.getCargoImage());
						maps.put("id", cargoData.getId().toString());
						if(cargoData.getCargoType().getCargoBigType().getDescription().equals(jzx)){
							maps.put("cargoName", cargoData.getCargoType().getCargoBigType().getDescription()+"(货号:"+cargoData.getId().toString()+")");
						}else
							maps.put("cargoName", cargoData.getCargoName()+"(货号:"+cargoData.getId().toString()+")");

						maps.put("cargoCode","货号:"+ cargoData.getId().toString());
						String shipper=cargoData.getOwner().getTrueName();
						if(shipper.equals("")){
							maps.put("shipper", "托运人:"+cargoData.getOwner().getLoginName());
						}else
							maps.put("shipper", "托运人:"+shipper);	
						maps.put("volume", "货量/箱量："+cargoData.getTonTeuDes());
						maps.put("place", cargoData.getStartFullName()
								+" 到 "   +cargoData.getEndFullName());

						maps.put("unitPrice","单价:"+ cargoData.getPriceDes());
//						maps.put("totalPrice","总价:"+ cargoData.getTransFeeDes());
						maps.put("endTime", "截止:"+cargoData.getPeriodTime());
						maps.put("type", cargoData.getCargoType().toString());

						maps.put("chatName", cargoData.getOwner().getNickName());
						maps.put("userId", cargoData.getOwner().getId().toString());  
						maps.put("phone", cargoData.getOwner().getMobile()); 

						if(cargoData.getCargoType().getDescription().contains(jzx)){
							maps.put("volume", "箱量:"+cargoData.getTonTeuDes());
							maps.put("totalPrice","箱量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						}else{
							maps.put("volume", "货量:"+cargoData.getTonTeuDes()); 	
							maps.put("totalPrice","货量:"+cargoData.getTonTeuDes()+",运价:"+cargoData.getPriceDes()+",运费:"+ cargoData.getTransFeeDes());
						}	
						dataList.add(maps);
					}
					smpAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getActivity(), cd.getMessage(),Toast.LENGTH_SHORT).show();

				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				loadingText.setVisibility(View.GONE);
				if (content != null && content.equals("socket time out")) {
					Toast.makeText(getActivity(), "连接服务器超时",
							Toast.LENGTH_LONG).show();
				}else if(content!=null){
					Toast.makeText(getActivity(), content,Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), "网络连接异常",Toast.LENGTH_LONG).show();	
				}
			}
		};
		apiParams.put("pageNo", page);
		data.getApiResult(showHandler,"/mobile/home/cargoHome", apiParams,"get");
		return dataList;
		
	}
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			resetBtn();
			switch (index) {
			case 0: //默认最新货物 。//找货：按货类、货量、装港、卸港等条件查询
				tab01.setTextColor(selectedColor);
				Intent intent4 = new Intent(getActivity(),FindCargoActivity.class);
				startActivity(intent4);
				//popupwindow.showAsDropDown(v, Gravity.CENTER, 0);
				break;
			case 1: //分类
				tab02.setTextColor(selectedColor);
				Intent intent= new Intent(getActivity(),HomeCategoryActivity.class);
				intent.putExtra("type", HomeCategoryActivity.MSG_CARGRO_CATE_LIST);
				startActivity(intent);
				break;
			case 2: //地区
				tab03.setTextColor(selectedColor);
				Intent intent2 = new Intent(getActivity(),HomeCategoryActivity.class);
				intent2.putExtra("type", HomeCategoryActivity.MSG_CARGRO_AREA_LIST);
				startActivity(intent2);
				break;
			case 3://代理
				tab04.setTextColor(selectedColor);

				Intent intent3 = new Intent(getActivity(),HomeSearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("param", UserRoleCode.broker.name());
				bundle.putString("title", "货代");
				bundle.putLong("type",HomeCategoryActivity.MSG_DLR_LIST);
				intent3.putExtras(bundle);
				getActivity().startActivity(intent3);
				
				break;
			}
		}

	}

	private void resetBtn() {
		tab01.setTextColor(unSelectedColor);
		tab02.setTextColor(unSelectedColor);
		tab03.setTextColor(unSelectedColor);
		tab04.setTextColor(unSelectedColor);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.position = position;
		HashMap<String, Object> map = (HashMap<String, Object>) parent.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), CargoPreviewActivity.class);
		intent.putExtra("id", map.get("id").toString());
		intent.putExtra("name", map.get("cargoName").toString());
		startActivity(intent);
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
				mListView.removeFooterView(loadMoreView);
				//loadMoreView.setVisibility(View.GONE);
		}

	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}
	@Override
	public void onResume() {//恢复当前查看的货物位置
		super.onResume();
		mListView.setSelection(position);
	}
	
}
