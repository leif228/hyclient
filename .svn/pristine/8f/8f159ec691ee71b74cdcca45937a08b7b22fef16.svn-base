package com.eyunda.third.activities.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.user.AgentActivity;
import com.eyunda.third.adapters.account.BankListAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.eyunda.third.domain.wallet.UserBankData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class BankListActivity extends CommonListActivity {
	private BankListAdapter adapter;
	private List<SpinnerItem> list;
	private ListView listView;
	
	private String flag = null; 
	public static final String BINDED_BANK_CARD = "bindedBankCard";
	public static final String BINDED_BANK_CARD_INCLUDE_EYUNDA = "includeEyunda";
	public static final String INCLUDE_PINGAN = "includePingan";
	
	Data_loader data;
	private boolean includeEyunda = true;
	private boolean includePingan = true;
	int source = 1;//读取绑定列表1见证宝取现银行卡列表，2跨行支付已绑定银行卡列表
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_bank_list);
		data = new Data_loader();
		list = new ArrayList<SpinnerItem>();
		flag = getIntent().getStringExtra("flag");
		source = getIntent().getIntExtra("source", 1);
		includeEyunda = getIntent().getBooleanExtra(BINDED_BANK_CARD_INCLUDE_EYUNDA,true);
		includePingan = getIntent().getBooleanExtra(INCLUDE_PINGAN,true);

		listView = (ListView) findViewById(R.id.list);
		
		// 设置adapter
		adapter = new BankListAdapter(this, R.layout.eyd_chat_row_contact, list, flag);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				if(source == 2){
					intent.putExtra("bankCode", adapter.getItem(position).getData())
					.putExtra("codeNo", adapter.getItem(position).getFlag())
					.putExtra("userBankId", adapter.getItem(position).getCid());
				}else{
					if(BINDED_BANK_CARD.equals(flag))
						intent.putExtra("bankCode", adapter.getItem(position).getId())
								.putExtra("codeNo", adapter.getItem(position).getFlag())
								.putExtra("userBankId", adapter.getItem(position).getCid());
					else
						intent.putExtra("bankCode", adapter.getItem(position).getId());
				}
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		
		if (BINDED_BANK_CARD.equals(flag)) //设置可长按取消绑定
			registerForContextMenu(listView);
	}

	private void initList() {
		list.clear();
		if(source == 2){
			loadBindList();
		}else{
			if (BINDED_BANK_CARD.equals(flag)) {   //绑定过的银行卡
				loadDate();
			} else{
				if(!includePingan){
					BankCode[] banks = BankCode.values();
					banks = Arrays.copyOfRange(banks, 1, BankCode.values().length);
					for(BankCode bc : banks){
						if(!bc.name().equals(BankCode.SPABANK.name())){
							SpinnerItem s = new SpinnerItem();
							s.setId(bc.toString());
							s.setData(bc.getDescription());
							s.setValue(bc.getIcon());
							
							list.add(s);
						}
					}
				}else{
					BankCode[] banks = BankCode.values();
					banks = Arrays.copyOfRange(banks, 1, BankCode.values().length);
					for (BankCode b : banks) {
						SpinnerItem s = new SpinnerItem();
						s.setId(b.toString());
						s.setData(b.getDescription());
						s.setValue(b.getIcon());
						
						list.add(s);
					}
				}
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (BINDED_BANK_CARD.equals(flag)) {
			setTitle("我的银行卡");
//			setRight(R.drawable.zx_faver_top, new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					startActivity(new Intent(BankListActivity.this,BindBankCardActivity.class));
//				}
//			});
		}else
			setTitle("选择开户银行");
		
		initList();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		this.getMenuInflater().inflate(R.menu.eyd_wallet_bind_bankcard, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			final SpinnerItem card = adapter .getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 解除绑定
			
			DialogUtil dialogAlert = new DialogUtil(BankListActivity.this);
			dialogAlert.showDialogFromConfig("提示", "你确认要解除绑定该银行卡吗？", new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					unBindCard(Long.valueOf(card.getCid()));
					
				}
			});

			return true;
		}else if(item.getItemId() == R.id.set_rcv){
			SpinnerItem card = adapter .getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			setRcvBank(Long.valueOf(card.getCid()));
		}
		return super.onContextItemSelected(item);
	}

	private void setRcvBank(Long valueOf) {
		final Map<String, Object> params = new HashMap<String, Object>();
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
					Toast.makeText(BankListActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(BankListActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(BankListActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(BankListActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(BankListActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(BankListActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("cardId", valueOf);
		data.getApiResult(handler, "/mobile/wallet/myWallet/setRcvCard", params, "post");
	}

	private void unBindCard(Long bindCardId) {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}

			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/wallet/myWallet/getBindCards", params);
				if (cd.getReturnCode().equals("Success")) {
					initList();
				} else {
					Toast.makeText(BankListActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(BankListActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(BankListActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(BankListActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(BankListActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("bindCardId", bindCardId);
		data.getApiResult(handler, "/mobile/wallet/myWallet/unBindCard", params, "post");
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/wallet/myWallet/getBindCards", params);
				if (cd.getReturnCode().equals("Success")) {
					List content = (ArrayList<UserBankData>) cd.getContent();

					int size = content!=null?content.size():0;
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							UserBankData data = new UserBankData((Map<String, Object>) content.get(i));
							SpinnerItem s = new SpinnerItem();
							s.setId(data.getBankCode().toString());
							s.setData(data.getBankCode().getDescription());
							s.setValue(data.getBankCode().getIcon());
							s.setCid(data.getId().toString());
							s.setFlag(data.getCardNo());
							
							list.add(s);
						}
					} else {
						// TODO 空记录处理
					}
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(BankListActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(BankListActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(BankListActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(BankListActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(BankListActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("includeEyunda", includeEyunda);
		data.getApiResult(handler, "/mobile/wallet/myWallet/getBindCards", params, "get");
	}
	private void loadBindList() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/pinganpay/getMyBindCards", params);
				if (cd.getReturnCode().equals("Success")) {
					List content = (ArrayList<UserBankData>) cd.getContent();

					int size = content!=null?content.size():0;
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							UserBankData data = new UserBankData((Map<String, Object>) content.get(i));
							SpinnerItem s = new SpinnerItem();
							s.setId(data.getBindId());
							s.setData(data.getBankName());
							s.setValue(data.getBankCode().getIcon());
							s.setCid(data.getBindId().toString());
							s.setFlag("账号后四位("+data.getCardNo()+")");
							
							list.add(s);
						}
					} else {
						// TODO 空记录处理
					}
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(BankListActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(BankListActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(BankListActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(BankListActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(BankListActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		
		data.getApiResult(handler, "/mobile/pinganpay/getMyBindCards", params, "get");
	}
}
