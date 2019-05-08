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
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.TreeViewExpandableListAdapter;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.home.EydShipSearchAdapter;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.OperatorData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.ShipPriceData;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.eyunda.tools.ExpandableListViewForScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 
 * @author
 *
 */
public class DlrListActivity extends CommonListActivity implements OnScrollListener {
	Data_loader dataLoader;

	private int page = 1;
	private String param;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadMoreText;
	private int visibleLastIndex = 0;// 最后的可视项索引 ;
	protected int totalPages;
	private int visibleItemCount;
	private TextView loadingText;
	Map<String, Object> apiParams = new HashMap<String, Object>();
	String url = "";
	
	private ExpandableListView operator_list;
	private List<OperatorData> mNews;
	TreeViewExpandableListAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_dlr_result_list);
		dataLoader = new Data_loader();
		
		this.operator_list = (ExpandableListView)this.findViewById(R.id.operator_list);
		operator_list.setGroupIndicator(null);
		adapter = new TreeViewExpandableListAdapter(this,operator_list);
		mNews = new ArrayList<OperatorData>();
		
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		operator_list.addFooterView(loadMoreView); // 设置列表底部视图
		loadMoreButton = (ProgressBar) loadMoreView
				.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		operator_list.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		setListView();
		
		loadDate();
		loadDate(page);
	}

	private void setListView(){
		
		initList();
		
		operator_list.setAdapter(adapter);

		operator_list.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				
				OperatorData nd = (OperatorData)adapter.getGroup(groupPosition);
				Intent intent = new Intent(DlrListActivity.this, UserSiteActivity.class);
				User user = new User();
				user.setUserData(nd.getUserData());
				intent.putExtra("toChatUser", user);
				startActivity(intent);
				return true;
			}
		});

		operator_list.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int parent, int children, long arg4) {
				UserData map = adapter.getChild(parent, children);
				
				if(GlobalApplication.getInstance().getUserData() != null && GlobalApplication.getInstance().getLoginStatus().equals(LoginStatusCode.logined)){
					User user = new User();
					user.setUserData(map);
					startActivity(new Intent(DlrListActivity.this,ChatActivity.class).putExtra("toChatUser", user));
//					Toast.makeText(NewPageHomeMainActivity.this, String.valueOf(map.getId()), Toast.LENGTH_SHORT).show();
					return false;
				}else{
					Toast.makeText(getApplicationContext(), "请先登陆！", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		});
	}
	public void initList() {
		adapter.RemoveAll();

		List<TreeViewExpandableListAdapter.TreeNode> treeNode = adapter.GetTreeNode();
		for (int i = 0; i < mNews.size(); i++) {
			TreeViewExpandableListAdapter.TreeNode node = new TreeViewExpandableListAdapter.TreeNode();
			node.parent = mNews.get(i);
			List child = ((ArrayList) mNews.get(i).getHandlerDatas());
			if (child != null) {
				for (int ii = 0; ii < child.size(); ii++) {
					node.childs.add(child.get(ii));
				}
			}
			treeNode.add(node);
		}

		adapter.UpdateTreeNode(treeNode);
		adapter.notifyDataSetChanged();
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
					operator_list.removeFooterView(loadMoreView);
					Toast.makeText(DlrListActivity.this, "数据全部加载完!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getGroupCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			// 移动到最底部，文字变为查看更多
			if (page < totalPages)
				loadMoreText.setText("查看更多");
			else
				operator_list.removeFooterView(loadMoreView);

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
			operator_list.removeFooterView(loadMoreView);
			if (content != null && content.equals("socket time out")) {
				Toast.makeText(DlrListActivity.this, "连接服务器超时",
						Toast.LENGTH_LONG).show();
			} else if (content != null) {
				Toast.makeText(DlrListActivity.this, content,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(DlrListActivity.this, "网络连接异常",
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
			HashMap<String, Object> var = (HashMap<String, Object>) cd
					.getContent();
			if (cd.getReturnCode().equals("Success")) {
					totalPages = ((Double) var.get("total")).intValue();
					List<Map<String, Object>> operatorList = (ArrayList<Map<String, Object>>) var
							.get("operatorList");
					for (Map<String, Object> mm : operatorList) {
						OperatorData nd = new OperatorData(mm);
						mNews.add(nd);
					}
					initList();
			} else {
				Toast.makeText(DlrListActivity.this, cd.getMessage(),
						Toast.LENGTH_SHORT).show();

			}
			// dialog.dismiss();
		}

	};

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("代理人");

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

		apiParams.put("roleCode", UserRoleCode.broker.name());
		apiParams.put("pageNo", page2);
		url = "/mobile/home/operatorList";

		// 调用接口
		dataLoader.getApiResult(showHandler, url, apiParams, "get");

	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

}
