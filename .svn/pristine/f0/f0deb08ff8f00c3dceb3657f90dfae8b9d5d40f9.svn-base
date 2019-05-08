package com.eyunda.third.activities.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.home.WebSiteActivity;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.notice.NoticeData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.StringUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 通知列表
 * 
 * @author
 *
 */
public class NoticeListActivity extends CommonActivity implements OnItemClickListener, OnScrollListener {
	Data_loader data;

	private ListView listView;
	private ArrayList<NoticeData> dataList;
	private CommonAdapter<NoticeData> smpAdapter;	


	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	protected int totalPages;
	protected int page = 1;

	private int visibleLastIndex = 0;// 最后的可视项索引 ;
	private int visibleItemCount;
	Boolean isLoad = false;
	Boolean isFirst = true;
	protected String seletedDate;
	protected String start = "";
	protected String end = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_oil_lists);
		data = new Data_loader();
		initView();

	}

	private void initView() {

		listView = (ListView) findViewById(R.id.mOilList);

		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView); // 设置列表底部视图
		loadMoreButton = (ProgressBar) loadMoreView.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		listView.setOnItemClickListener(this);
		dataList = new ArrayList<NoticeData>();
		smpAdapter = new CommonAdapter<NoticeData>(NoticeListActivity.this,dataList,R.layout.eyd_notice_item) {
			
			@Override
			public void convert(ViewHolder helper, NoticeData item) {
				helper.setImageByUrl(R.id.newsImg,ApplicationConstants.IMAGE_URL+item.getSource() );
				helper.setText(R.id.newsTitle, StringUtil.shortStr(item.getTitle(),15));
				helper.setText(R.id.newsPubTime, item.getPublishTime());
				helper.setText(R.id.newsContent, StringUtil.shortStr(item.getContent(),42));
			}
		};
		listView.setAdapter(smpAdapter);
		getData();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("帮助");

		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				isFirst = false;
				isLoad = true;
				if (page < totalPages) {
					loadMoreText.setText("加载中...");

					page++;
					getData();
				} else {
					listView.removeFooterView(loadMoreView);
				}
			}
		});

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = smpAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
			if (page < totalPages) {
				loadMoreText.setText("查看更多");
			} else
				// listView.removeFooterView(loadMoreView);
				loadMoreView.setVisibility(View.GONE);

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	private ArrayList<NoticeData> getData() {
		// if(user!=null && !user.isConsigner()){
		final Map<String, Object> params = new HashMap<String, Object>();
		if (dataList.isEmpty())
			loadMoreView.setVisibility(View.GONE);
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
				ConvertData cd = new ConvertData(arg0, "/mobile/home/notice", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> content = (List<Map<String, Object>>) ma.get("noticeDatas");
					totalPages = ((Double) ma.get("totalPages")).intValue();
					int size = content.size();
					if (isFirst) {// 初始显示10条数据
						dataList.clear();
						for (int i = 0; i < size; i++)
							setData(content, i);
					} else {
						for (int i = 0; i < size; i++)
							setData(content, i);
					}

					smpAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(NoticeListActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(NoticeListActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(NoticeListActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(NoticeListActivity.this, content, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(NoticeListActivity.this, "请求失败", Toast.LENGTH_LONG).show();
			}
		};

		params.put("pageNo", page);
		data.getApiResult(handler, "/mobile/home/notice", params, "get");
		return dataList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		NoticeData nd =  (NoticeData)parent.getAdapter().getItem(position);
		Intent intent = new Intent(NoticeListActivity.this, WebSiteActivity.class);
		String url=	ApplicationConstants.SERVER_URL+"/mobile/home/noticeDetail?noticeId="+nd.getId();
		intent.putExtra("title", StringUtil.shortStr(nd.getTitle(),10));
		intent.putExtra("url",url);
		startActivity(intent);
	}


	private void setData(List<Map<String, Object>> content, int i) {

		NoticeData nd = new NoticeData((Map<String, Object>)content.get(i));
		// 滚动到最底部
		// mListView.setSelection(mListView.getAdapter().getCount()-1);
		dataList.add(nd);

	}

	private void resetFooter() {
		page = 1;
		start = "";
		end = "";
		isLoad = false;
		isFirst = true;
		loadMoreView.setVisibility(View.VISIBLE);
		loadMoreButton.setVisibility(View.VISIBLE);
		loadMoreText.setText("");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		boolean isRefresh = getIntent().getBooleanExtra("success", false);
		if (isRefresh) {
			dataList.clear();
			getData();
			smpAdapter.notifyDataSetChanged();
		}
	}

	public void refresh() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					dataList.clear();
					page = 1;
					getData();
					// smpAdapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
