package com.eyunda.third.activities.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.bool;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.pay.FetchMoneyActivity;
import com.eyunda.third.activities.pay.PayMoneyActivity;
import com.eyunda.third.activities.pay.TranslateActivity;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.adapters.account.WalletHomeAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.ApplyReplyCode;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.domain.enumeric.PayStatusCode;
import com.eyunda.third.domain.enumeric.SettleStyleCode;
import com.eyunda.third.domain.enumeric.WalletOptCode;
import com.eyunda.third.domain.enumeric.YesNoCode;
import com.eyunda.third.domain.wallet.WalletData;
import com.eyunda.third.domain.wallet.WalletSettleData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.pingan.bank.libs.fundverify.Common;
import com.pingan.bank.libs.fundverify.FundVerifyBack;
import com.pingan.bank.libs.fundverify.PAFundVerify;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 钱包首页
 * 
 * @author
 *
 */
public class UserSettlesActivity extends CommonActivity
		implements OnClickListener, OnItemClickListener, OnScrollListener {

	Data_loader dataLoader;
	private ProgressDialog mDialog;

	private ListView listView;
	private ArrayList<WalletSettleData> dataList;
	private CommonAdapter<WalletSettleData> smpAdapter;

	private int visibleLastIndex = 0;// 最后的可视项索引 ;
	private int visibleItemCount;
	private int curPosition;// 当前选择的item
	private int page = 1;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	private TextView balance, balanceTrans,noResult;

	protected int totalPages;
	Boolean isLoad = false;
	Boolean isFirst = true;
	protected String start = "";
	protected String end = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_wallet_settle);

		dataLoader = new Data_loader();

		this.balance = (TextView) findViewById(R.id.balance);//
		this.balanceTrans = (TextView) findViewById(R.id.balanceTrans);
		this.noResult = (TextView) findViewById(R.id.noResult);
		this.listView = (ListView) findViewById(R.id.list);
		
		setAdapter();
		initLoadMoreBtn();
	}

	private void setAdapter() {
		dataList = new ArrayList<WalletSettleData>();
		smpAdapter = new CommonAdapter<WalletSettleData>(getApplicationContext(), dataList, R.layout.eyd_settle_item) {

			@Override
			public void convert(ViewHolder helper, WalletSettleData item) {
				helper.setText(R.id.count, item.getMoney().toString());
				helper.setText(R.id.outCount, item.getFetchableMoney().toString());
				helper.setText(R.id.endtime, item.getCreateTime());
				helper.setText(R.id.descript, item.getSubject());
			}
		};
	}

	private void initLoadMoreBtn() {
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView); // 设置列表底部视图
		listView.setFooterDividersEnabled(false);
		loadMoreButton = (ProgressBar) loadMoreView.findViewById(R.id.progressBar);
		loadingText = (TextView) loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView) loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				isFirst = false;
				isLoad = true;
				if (page <= totalPages) {
					loadMoreText.setText("加载中...");
					page++;
					loadData();
				} else {
					listView.removeFooterView(loadMoreView);
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("我的账务");
		page = 1;
		dataList.clear();
		loadData();
		setRight(R.drawable.ic_action_search, new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog();

			}
		});
	}
	//按日期查找
	protected void AlertDialog() {
		LayoutInflater inflater = LayoutInflater.from(UserSettlesActivity.this);
		View view = inflater.inflate(R.layout.search_cargo_item, null);
		final TextView tv_start = (TextView) view.findViewById(R.id.et_start);
		final TextView tv_end = (TextView) view.findViewById(R.id.et_end);
		DatePickerFragment datePicker = new DatePickerFragment(tv_start);
		DatePickerFragment datePicker2 = new DatePickerFragment(tv_end);
		tv_start.setText(datePicker.getLastMonth());
		tv_end.setText(datePicker2.getCurrentTime());
		tv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePicker = new DatePickerFragment(tv_start);
				datePicker.show(getFragmentManager(), "datePicker");

			}
		});
		tv_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePicker = new DatePickerFragment(tv_end);
				datePicker.show(getFragmentManager(), "datePicker2");

			}
		});
		new AlertDialog.Builder(UserSettlesActivity.this).setTitle("查找账务").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info).setNegativeButton("取消", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						start = tv_start.getText().toString().trim();
						end = tv_end.getText().toString().trim();
						dataList.clear();
						loadData();
					}
				}).show();
	}

	protected void loadData() {
		final UserData ud = GlobalApplication.getInstance().getUserData();
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings({ "unchecked" })
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				loadMoreButton.setVisibility(View.GONE);
				loadingText.setVisibility(View.GONE);
				ConvertData cd = new ConvertData(arg0, "/mobile/wallet/getUserSettles", params);
				if (cd.getReturnCode().equals("Success")) {
					Map content = (HashMap<String, Object>) cd.getContent();
					List userSettles = (ArrayList<Map<String, Object>>) content.get("walletDatas");
					totalPages = ((Double) content.get("totalPages")).intValue();
					balance.setText("可用余额：" + ((Double) content.get("totalBalance")).toString());
					balanceTrans.setText("可提现余额：" + ((Double) content.get("totalTranOutAmount")).toString());

					int size = userSettles != null ? userSettles.size() : 0;
					if (size > 0) {
						toggleResult(true);
						for (int i = 0; i < size; i++) {
							WalletSettleData data = new WalletSettleData((Map<String, Object>) userSettles.get(i));
							dataList.add(data);
						}
					} else {
						// 显示无记录
						toggleResult(false);
					}
					smpAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(UserSettlesActivity.this, cd.getMessage(), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(UserSettlesActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(UserSettlesActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(UserSettlesActivity.this, content, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(UserSettlesActivity.this, "未知异常", Toast.LENGTH_LONG).show();
			}

		};
		params.put("startTime", start);
		params.put("endTime", end);
		params.put("pageNo", page);
		dataLoader.getApiResult(handler, "/mobile/wallet/getUserSettles", params, "get");
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.bindBank:
//			startActivity(new Intent(this, BankListActivity.class).putExtra("flag", BankListActivity.BINDED_BANK_CARD)
//					.putExtra(BankListActivity.BINDED_BANK_CARD_INCLUDE_EYUNDA, false));
//			break;

		default:
			break;
		}
	}
	private void toggleResult(boolean flag){
		if(flag){
			this.noResult.setVisibility(View.GONE);
			this.listView.setVisibility(View.VISIBLE);
			
		}else{
			this.noResult.setVisibility(View.VISIBLE);
			this.listView.setVisibility(View.GONE);
		}
	}
}
