package com.eyunda.third.activities.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.pingan.bank.libs.fundverify.Common;
import com.pingan.bank.libs.fundverify.FundVerifyBack;
import com.pingan.bank.libs.fundverify.PAFundVerify;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 钱包首页
 * @author
 *
 */
public class WalletHomeActivity extends CommonActivity implements
OnClickListener, OnItemClickListener,OnScrollListener{
	
	public static final int MSG_Del_One_Item = 1;//删除
	public static final int MSG_REFUND = 2;//退款处理
	public static final int MSG_DIRECTREFUND = 3;//退款
	public static final int MSG_PAY = 4;//支付
	public static final int MSG_CONFIRMPAY = 5;//确认付款
	public static final int MSG_APPLYREFUND = 6;//申请退款
	public static final int MSG_SHOW_PW = 7;

	Data_loader data;
	private ProgressDialog mDialog;
	private ListView listView;
	private WalletHomeAdapter smpAdapter;
	private ArrayList<Map<String, Object>> dataList;
	private int visibleLastIndex=0;//最后的可视项索引 ;
	private int visibleItemCount;
	private int curPosition;// 当前选择的item
	private int page=1;
	private View loadMoreView;
	private ProgressBar loadMoreButton;
	private TextView loadingText;
	private TextView loadMoreText;
	private TextView balance,balanceTrans;
	private LinearLayout balanceContainer;
	protected int totalPages;
	Boolean isLoad =false;
	Boolean isFirst=true;
	protected String start="";
	protected String end="";
	private WalletHomePopupWindow popupWindow;// 弹出菜单
	private View bindBank,find,fill,fetch,turn,pay,receive;
	
	protected String settedPW = YesNoCode.no.toString();
	protected int setPWType = -1;
	
	protected Map<Long, FeeItemCode> feeTypes;//保存每个记录的支付类型
	// 删除账务处理
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SHOW_PW: 
				showPopupWindow();
				break;
			case MSG_Del_One_Item: // 删除一个Item
				curPosition = msg.arg1;// 要删除的记录
				final Long idToDel = (Long) msg.obj;
				final Map<String, Object> params = new HashMap<String, Object>();
				params.put("walletId",idToDel);
				DialogUtil dialogAlert = new DialogUtil(WalletHomeActivity.this);
				dialogAlert.showDialogFromConfig("提示", "确认要删除该条记录么?", new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						//异步后台删除item
						AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
							@Override
							public void onStart() {
								dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
							}

							@Override
							public void onSuccess(String arg0) {
								dialog.dismiss();
								ConvertData cd = new ConvertData(arg0);
								if (cd.getReturnCode().equals("Success")) {
									
									smpAdapter.removeItem(curPosition);
									smpAdapter.notifyDataSetChanged();
								}
							}
						};
						
						data.getApiResult(handler, "/mobile/wallet/delete", params, "get");	
						
					}
				});
				break;
			case MSG_CONFIRMPAY: //确认支付
				curPosition = msg.arg1;// 
				final Long id1 = (Long) msg.obj;
//				startActivity(new Intent(WalletHomeActivity.this, WebPayActivity.class)
//						.putExtra("url", ApplicationConstants.SERVER_URL+"/mobile/pinganpay/orderPay/?walletId="+id1+"&orderId=0&feeItem="+FeeItemCode.endfee)
//						.putExtra("title", "确认支付"));
//				startActivity(new Intent(WalletHomeActivity.this, GeneralPayActivity.class).putExtra("title", "确认支付").putExtra("valueId", id1.toString()).putExtra("value", "").putExtra("type", FeeItemCode.endfee.name()).putExtra("hasWalletId",true));
//
//				finish();
				//弹出确认支付窗口
				confirmDialog(id1,WalletOptCode.confirmPay,ApplyReplyCode.noapply, "确认付款么？", 1);
				
				break;
			case MSG_PAY: //支付
				curPosition = msg.arg1;// 
				final Long id2 = (Long) msg.obj;
//				startActivity(new Intent(WalletHomeActivity.this, WebPayActivity.class)
//						.putExtra("url", ApplicationConstants.SERVER_URL+"/mobile/pinganpay/orderPay/?walletId="+id2+"&orderId=0&feeItem="+FeeItemCode.prefee)
//						.putExtra("title", "支付"));
				FeeItemCode feetype = getFeeType(id2);
				String title = "资金托管";
				if(feetype.equals(FeeItemCode.inaccount)){
					title = "充值";
				}else if(feetype.equals(FeeItemCode.face)){
					title = "当面付";
				}else if(feetype.equals(FeeItemCode.outaccount)){
					title = "提现";
				}
				startActivity(new Intent(WalletHomeActivity.this, GeneralPayActivity.class).putExtra("title", title).putExtra("valueId", id2.toString()).putExtra("value", "").putExtra("type", feetype.name()).putExtra("hasWalletId",true));
				finish();

				
				break;
			case MSG_APPLYREFUND: // 
				curPosition = msg.arg1;// 
				final Long id4 = (Long) msg.obj;
				confirmDialog(id4, WalletOptCode.applyRefund,ApplyReplyCode.noapply, "确认申请退款么？",1);
				
				break;
			case MSG_REFUND: // 
				curPosition = msg.arg1;// 
				final Long id5 = (Long) msg.obj;
				confirmDialog(id5, WalletOptCode.refund,ApplyReplyCode.reply, "确认同意退款么？",2);
				break;
				
			}
		}
	};
	private LinearLayout btnA;
	private View btnB;
	private View btnC;
	private LinearLayout btnD;
	private View btnE;

	//获取对应walletData的支付类型
	protected FeeItemCode getFeeType(Long id2) {
		FeeItemCode res = FeeItemCode.inaccount;
		for(Map.Entry<Long, FeeItemCode> map : feeTypes.entrySet()){
			if(map.getKey().equals(id2)){
				res = map.getValue();
				break;
			}
		}
		return res;
	}

	protected void doRefund(Long id, ApplyReplyCode arc){
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Toast.makeText(getApplicationContext(), "操作成功！", Toast.LENGTH_SHORT).show();
					page=1;
					dataList.clear();
					loadDate();
				} else {
					Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
	
		params.put("walletId", id );
		params.put("applyReply", arc);
		data.getApiResult(handler, "/mobile/pinganpay/doRefund", params, "post");
	}
	protected void doOpt(Long id, WalletOptCode wpt) {

		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Toast.makeText(getApplicationContext(), "操作成功！", Toast.LENGTH_SHORT).show();
					page=1;
					dataList.clear();
					loadDate();
				} else {
					Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
	
		params.put("walletId", id );
		params.put("walletOptCode", wpt);
		data.getApiResult(handler, "/mobile/pinganpay/walletOpt", params, "post");
	}

	private void confirmDialog(final Long id, final WalletOptCode woc,final ApplyReplyCode arc,final String msg,final int type){
		new AlertDialog.Builder(WalletHomeActivity.this)
		.setTitle("通知")
		.setMessage(msg)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setNegativeButton("取消", null)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(type == 1){
					doOpt(id, woc);
				}else{
					doRefund(id, arc);
				}
			}
		}).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_wallet_home);
		this.bindBank = (View) findViewById(R.id.bindBank);
		this.find = (View) findViewById(R.id.find);//查找
		this.fill = (View) findViewById(R.id.fill);//充值
		this.fetch = (View) findViewById(R.id.fetch);//取现
		this.turn = (View) findViewById(R.id.turn);//转帐
		this.pay = (View) findViewById(R.id.pay);//付款
		this.receive = (View) findViewById(R.id.receive);//收款
		this.balance = (TextView) findViewById(R.id.balance);//
		this.balanceTrans = (TextView) findViewById(R.id.balanceTrans);
		this.balanceContainer = (LinearLayout)findViewById(R.id.balanceContainer);
		
		balanceContainer.setOnClickListener(this);
		bindBank.setOnClickListener(this);
		find.setOnClickListener(this);
		fill.setOnClickListener(this);
		fetch.setOnClickListener(this);
		turn.setOnClickListener(this);
		pay.setOnClickListener(this);
		receive.setOnClickListener(this);
		
		data = new Data_loader();
		this.listView = (ListView) findViewById(R.id.list);
		dataList = new ArrayList<Map<String, Object>>();
		feeTypes = new HashMap<Long, FeeItemCode>();
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		listView.addFooterView(loadMoreView);    //设置列表底部视图
		listView.setFooterDividersEnabled(false);
		loadMoreButton = (ProgressBar)loadMoreView.findViewById(R.id.progressBar);
		loadingText  =(TextView)loadMoreView.findViewById(R.id.loading_text);
		loadMoreText = (TextView)loadMoreView.findViewById(R.id.loadmore_text);
		listView.setOnScrollListener(this);
		loadMoreView.setOnClickListener(null);
		setAdapter();
		loada();
		
		setPopupWindow();
		showPopupWindow();
	}
	
	private void setPopupWindow() {
		popupWindow = new WalletHomePopupWindow(this);
		btnA = popupWindow.getBtnA();
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				
				final Map<String, Object> params = new HashMap<String, Object>();
				AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
					}
					@SuppressWarnings({ "unchecked"})
					@Override
					public void onSuccess(String arg0) {
						dialog.dismiss();
						ConvertData cd = new ConvertData(arg0);
						if (cd.getReturnCode().equals("Success")) {
							Map<String, String> map = (HashMap<String, String>) cd.getContent();
							showmDialog();
							start(PAFundVerify.TYPE_SET_PASSWORD,map);
							
						} else {
							Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
						}
					}
					@Override
					public void onFailure(Throwable arg0, String content) {
						super.onFailure(arg0, content);
						dialog.dismiss();
						if (content != null && content.equals("can't resolve host"))
							Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
						else if (content != null && content.equals("socket time out")) {
							Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
						} else if (content != null) {
							Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
						} else
							Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
					}
				};
				params.put("type", "S" );
				data.getApiResult(handler, "/mobile/wallet/myWallet/setpw", params, "get");
			}
		});		
		btnB = popupWindow.getBtnB();
		
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow

				final Map<String, Object> params = new HashMap<String, Object>();
				AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
					}
					@SuppressWarnings({ "unchecked"})
					@Override
					public void onSuccess(String arg0) {
						dialog.dismiss();
						ConvertData cd = new ConvertData(arg0);
						if (cd.getReturnCode().equals("Success")) {
							Map<String, String> map = (HashMap<String, String>) cd.getContent();
							showmDialog();
							setPWType = PAFundVerify.TYPE_MODIFY_PASSWORD;
							start(PAFundVerify.TYPE_MODIFY_PASSWORD,map);
							
						} else {
							Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
						}
					}
					@Override
					public void onFailure(Throwable arg0, String content) {
						super.onFailure(arg0, content);
						dialog.dismiss();
						if (content != null && content.equals("can't resolve host"))
							Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
						else if (content != null && content.equals("socket time out")) {
							Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
						} else if (content != null) {
							Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
						} else
							Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
					}
				};
				params.put("type", "C" );
				data.getApiResult(handler, "/mobile/wallet/myWallet/setpw", params, "get");
			}
		});		
		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow

				final Map<String, Object> params = new HashMap<String, Object>();
				AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
					}
					@SuppressWarnings({ "unchecked"})
					@Override
					public void onSuccess(String arg0) {
						dialog.dismiss();
						ConvertData cd = new ConvertData(arg0);
						if (cd.getReturnCode().equals("Success")) {
							Map<String, String> map = (HashMap<String, String>) cd.getContent();
							showmDialog();

							start(PAFundVerify.TYPE_FORGET_PASSWORD,map);
							
						} else {
							Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
						}
					}
					@Override
					public void onFailure(Throwable arg0, String content) {
						super.onFailure(arg0, content);
						dialog.dismiss();
						if (content != null && content.equals("can't resolve host"))
							Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
						else if (content != null && content.equals("socket time out")) {
							Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
						} else if (content != null) {
							Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
						} else
							Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
					}
				};
				params.put("type", "R" );
				data.getApiResult(handler, "/mobile/wallet/myWallet/setpw", params, "get");
			}
		});		
		btnE = popupWindow.getBtnE();
		btnE.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
			}
		});
		btnE.setVisibility(View.GONE);
	}
	private void showmDialog(){
		mDialog = new ProgressDialog(WalletHomeActivity.this);
		mDialog.setTitle("提示");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.setMessage("请稍候");
		try {
			mDialog.show();
		} catch (Exception e) {
		}
	}

	private void showPopupWindow() {
		if(YesNoCode.yes.toString().equals(settedPW)){
			btnA.setVisibility(View.GONE);
			btnB.setVisibility(View.VISIBLE);
		}else{
			btnA.setVisibility(View.VISIBLE);
			btnB.setVisibility(View.GONE);
		}
	}
	
	private void start(final int type, Map<String, String> map) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		PAFundVerify fundVerify = new PAFundVerify(WalletHomeActivity.this, ApplicationConstants.APP_KEY,
				false);
		fundVerify.start(new FundVerifyBack() {

			@Override
			public void startCheck() {
				Log.d("Test", "startCheck");
			}

			@Override
			public void processCheck() {
				Log.d("Test", "processCheck");
			}

			@Override
			public void finishCheck() {
				Log.d("Test", "finishCheck");
			}

			@Override
			public void failedCheck(String error) {
				Log.d("Test", "failedCheck->" + error);
			}
		}, getFormData(type,map), type);

	}
	
	private HashMap<String, String> getFormData(int type, Map<String, String> map) {
		if (type == PAFundVerify.TYPE_DEFAULT) {
			String orig = String.format("%s&%s&", map.get("P2PCode"), map.get("custAccId"));
			map.put("orig", orig);
		}
		return (HashMap<String, String>) map;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Common.CODE_Verify) {
			if (resultCode == Common.Result_OK) {
				if(setPWType == PAFundVerify.TYPE_MODIFY_PASSWORD){
					settedPW = YesNoCode.yes.toString();
					setPWSuccessTag();
					mHandler.sendEmptyMessage(MSG_SHOW_PW);
				}
				String str = data.getStringExtra(Common.PAY_RESULT_BACK);
				Toast.makeText(WalletHomeActivity.this, str,Toast.LENGTH_LONG).show();
			} else if (resultCode == Common.Result_Failed) {
				String str = data.getStringExtra(Common.PAY_RESULT_BACK);
				Toast.makeText(WalletHomeActivity.this, str,Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(WalletHomeActivity.this, "取消",Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void setPWSuccessTag() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
				} else {
					Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
					setPWSuccessTag();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
				setPWSuccessTag();
			}
		};
		data.getApiResult(handler, "/mobile/wallet/myWallet/setPWSuccess", params, "get");
	}


	private void loada() {
		loadMoreText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMoreButton.setVisibility(View.VISIBLE);
				isFirst=false;
				isLoad=true;
				if(page<=totalPages){
					loadMoreText.setText("加载中...");
					page++;
					loadDate();
				}else{
					listView.removeFooterView(loadMoreView);
				}
			}
		});	
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("我的钱包");
		page=1;
		dataList.clear();
		loadDate();
		loadBalance();
	}
	//加载余额
	private void loadBalance() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Map<String,Object> content =  (HashMap<String,Object>)cd.getContent();
					if(content != null){
						
						balance.setText("可用余额："+((Double)content.get("totalBalance")).toString());
						balanceTrans.setText("可提现余额："+ ((Double)content.get("totalTranOutAmount")).toString());

					}else{
						Toast.makeText(WalletHomeActivity.this, "获取余额信息失败", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
				setPWSuccessTag();
			}
		};
		data.getApiResult(handler, "/mobile/wallet/getAccoutNum", params, "get");
	}

	protected void AlertDialog() {
			LayoutInflater  inflater = LayoutInflater.from(WalletHomeActivity.this);
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
			new AlertDialog.Builder(WalletHomeActivity.this)
			.setTitle("查找账务")
			.setView(view)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setNegativeButton("取消", null)
			.setPositiveButton("查找", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					 start =tv_start.getText().toString().trim();
					 end =tv_end.getText().toString().trim();
					 dataList.clear();
					 loadDate();
				}
			}).show();
		
		
	}

	protected BaseAdapter setAdapter() {

		smpAdapter = new WalletHomeAdapter(WalletHomeActivity.this, mHandler,dataList, R.layout.eyd_wallet_home_item);
		listView.setAdapter(smpAdapter);
		return smpAdapter;
	}

	protected void loadDate() {
		final UserData ud = GlobalApplication.getInstance().getUserData();
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
				loadMoreView.setVisibility(View.VISIBLE);
			}

			@SuppressWarnings({ "unchecked"})
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				loadMoreButton.setVisibility(View.GONE); 
				loadingText.setVisibility(View.GONE); 
				ConvertData cd = new ConvertData(arg0,"/mobile/wallet/myWallet", params);
				if (cd.getReturnCode().equals("Success")) {
					Map r = (HashMap<String, Object>) cd.getContent();
					List content = (ArrayList<WalletData>) r.get("walletDatas");
					settedPW = (String) r.get("settedPW");
					totalPages =((Double)r.get("totalPages")).intValue();
					
					mHandler.sendEmptyMessage(MSG_SHOW_PW);

					int size = content!=null?content.size():0;
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							
							WalletData data = new WalletData((Map<String, Object>) content.get(i));
							Map<String, Object> map = new HashMap<String, Object>();
							SettleStyleCode ssc = data.getSettleStyle();
							PayStatusCode psc = data.getPaymentStatus();
							ApplyReplyCode rsc = data.getRefundStatus();

							feeTypes.put(data.getId(), data.getFeeItem());

							map.put("id", data.getId());
							map.put("settleNo", data.getPaymentNo());
							map.put("settleStyle",""+ssc.getDescription());
							map.put("totalFee",data.getTotalFee().toString());
							map.put("body",data.getBody());
							map.put("gmtPayment",data.getGmtPayment());
							map.put("feeItem",data.getFeeItem().getDescription());
							map.put("paymentStatus",psc.getDescription());
							map.put("refundStatus", rsc.getDescription());
							String inout = SettleStyleCode.fill.getDescription();
							Double totalFee = data.getTotalFee();
							
							if(ssc.equals(SettleStyleCode.fetch)){
								inout = SettleStyleCode.fetch.getDescription();
								totalFee = 0 - data.getTotalFee();
							}else if(ssc.equals(SettleStyleCode.pay)){
								if(ud.getId().equals(data.getBuyerId())){
									inout = "支出";
									totalFee = 0 - totalFee;
								}else if(!ud.getId().equals(data.getBuyerId())){
									inout = "收入";
									if(!ud.getId().equals(data.getBrokerId())){
										totalFee = data.getMiddleFee();
									}else if(ud.getId().equals(data.getBrokerId())){
										totalFee = data.getTotalFee() - data.getMiddleFee() - data.getServiceFee();
									}
								}
								
							}
							map.put("inout", inout);
							map.put("totalFee", totalFee.toString());
							
							//fetch=false, applyRefund=false, refund=false, delete=true, pay=true, directRefund=false, confirmPay=false
							if(data.getOps().get(WalletOptCode.pay.toString()))
								map.put("btnStartSurety", true);
							else
								map.put("btnStartSurety", false);	
							//检查合同状态，是否处于未申请退款状态，
							if(data.getOps().get(WalletOptCode.confirmPay.toString()) && (rsc.equals(ApplyReplyCode.noapply) || rsc.equals(ApplyReplyCode.noreply)))
								map.put("btnEndSurety", true);
							else
								map.put("btnEndSurety", false);	
							if(data.getOps().get(WalletOptCode.refund.toString()))
								map.put("btnRefund", true);
							else
								map.put("btnRefund", false);
							if(data.getOps().get(WalletOptCode.applyRefund.toString()))
								map.put("btnApplyRefund", true);
							else
								map.put("btnApplyRefund", false);
							if(data.getOps().get(WalletOptCode.delete.toString()))
								map.put("btnDel", true);
							else
								map.put("btnDel", false);
							dataList.add(map);
						}
					} else {
					}
					smpAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(WalletHomeActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				listView.removeFooterView(loadMoreView);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(WalletHomeActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(WalletHomeActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(WalletHomeActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(WalletHomeActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("startTime",start);
		params.put("endTime",end);
		params.put("pageNo", page );
		data.getApiResult(handler, "/mobile/wallet/myWallet", params, "get");
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
				//listView.removeFooterView(loadMoreView);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bindBank:
			startActivity(new Intent(this,BankListActivity.class).putExtra("flag", BankListActivity.BINDED_BANK_CARD).putExtra(BankListActivity.BINDED_BANK_CARD_INCLUDE_EYUNDA, false));
			break;
		case R.id.find:
			AlertDialog();
			break;
		case R.id.fill:
			startActivity(new Intent(this,FillMoneyActivity.class));
			
			break;
		case R.id.fetch:
			if(YesNoCode.no.toString().equals(settedPW)){
				Toast.makeText(this, "请先绑定提现银行卡！", Toast.LENGTH_LONG).show();
				return;
			}
			startActivity(new Intent(this,FillFetchActivity.class).putExtra("flag", "fetch"));
			
			break;
		case R.id.turn:
			startActivity(new Intent(this,TranslateActivity.class));
			break;
		case R.id.pay:
			startActivity(new Intent(this,PayMoneyActivity.class));
			break;
		case R.id.receive:
			startActivity(new Intent(this,FetchMoneyActivity.class));
			break;
		case R.id.balanceContainer:
			startActivity(new Intent(this, UserSettlesActivity.class));
		default:
			break;
		}
	}
}


