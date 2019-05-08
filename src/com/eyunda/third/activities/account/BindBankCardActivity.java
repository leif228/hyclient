package com.eyunda.third.activities.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.user.AccountListActivity;
import com.eyunda.third.activities.user.AgentActivity;
import com.eyunda.third.activities.user.ApplyAgentActivity;
import com.eyunda.third.activities.user.BasePopupWindow;
import com.eyunda.third.activities.user.BasicInfoActivity;
import com.eyunda.third.activities.user.UpdatePwd;
import com.eyunda.third.activities.user.UserChildListActivity;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.AccountData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.eyunda.third.domain.enumeric.YesNoCode;
import com.eyunda.third.domain.wallet.PubPayCity;
import com.eyunda.third.domain.wallet.PubPayCnapsBank;
import com.eyunda.third.domain.wallet.PubPayNode;
import com.eyunda.third.domain.wallet.UserBankData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 绑定银行卡页面
 * @author
 */

public class BindBankCardActivity extends CommonListActivity implements
OnClickListener{
	TimeCount timer;
	private RelativeLayout settedpw_yes, settedpw_no;
	private Long userBindedBankCardId;
	
	private EditText idcard1, trueName1, accountNo1, mobile1, bankCode1;
	private RadioButton pinganbank1, otherbank1;
	private Button unbindcard;
	
	private Button next_btn,search_btn,btnSave,submit;
	private EditText idcard, trueName, accountNo, search, mobile, paypw, paypw2, tansNum;
	private TextView bankCode_btn, bankCode;
	private CheckBox onlyOne;

	private LinearLayout linear_po, linear_bs, ll_message;
	private Spinner p, c, openbank;
	private ArrayAdapter<PubPayCity> cAdapter;
	private ArrayAdapter<PubPayNode> pAdapter;
	private ArrayAdapter<PubPayCnapsBank> ppcbAdapter;
	
	private List<PubPayNode> listppn = new ArrayList<PubPayNode>();
	private List<PubPayCity> listppc = new ArrayList<PubPayCity>();
	private List<PubPayCnapsBank> listppcb = new ArrayList<PubPayCnapsBank>();
	protected PubPayCity currppc = null;
	protected PubPayNode currppn = null;
	protected PubPayCnapsBank currppcb = null;
	
	private RadioGroup poRadioGroup;
	private RadioButton pinganbank, otherbank;
	private String BankType = "pinganBank";//pinganBank, otherBank
	private RadioGroup sbRadioGroup;
	private RadioButton small5, big5;
	private String sbType = "small5";// small5,big5

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;
	private View btnH;
	private BasePopupWindow popupWindow;// 弹出菜单
	
	private boolean pfirst = false;
	String bankCodeStr = null;
	protected boolean receiveCard = false;
	
	private static final int REQUEST_CODE_BANK = 1;
	protected static final int RPC = 0;
	protected static final int RC = 2;
	protected static final int RPPCB = 3;
	protected static final int REQUEST_CODE_NEXT = 4;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case RPC:
				pAdapter.notifyDataSetChanged();
				cAdapter.notifyDataSetChanged();
				break;
			case RC:
				cAdapter.notifyDataSetChanged();
				break;
			case RPPCB:
				ppcbAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_bindbankcard);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
		setPopupWindow();
		setview();
		initDate();
	}
	
	private void setPopupWindow() {
		popupWindow = new BasePopupWindow(this);
		// 基本信息
		btnA = popupWindow.getBtnA();
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BindBankCardActivity.this,
						BasicInfoActivity.class));
			}
		});
		// 账户信息
		btnB = popupWindow.getBtnB();
		btnB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BindBankCardActivity.this,
						AccountListActivity.class));
			}
		});
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BindBankCardActivity.this,
						ApplyAgentActivity.class));
			}
		});
		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BindBankCardActivity.this,
						AgentActivity.class));
			}
		});
		// 修改密码
		btnF = popupWindow.getBtnF();
		btnF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BindBankCardActivity.this,
						UpdatePwd.class));
			}
		});
		// 管理子账号
		btnG = popupWindow.getBtnG();
		btnG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BindBankCardActivity.this,
						UserChildListActivity.class));
			}
		});
		btnH = popupWindow.getBtnH();
		btnH.setVisibility(View.GONE);
	}

	private RadioGroup.OnCheckedChangeListener poChangeRadio = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == pinganbank.getId()) {
				BankType = "pinganBank";
				linear_po.setVisibility(View.GONE);
			} else if (checkedId == otherbank.getId()) {
				BankType = "otherBank";
				linear_po.setVisibility(View.VISIBLE);
			}
		}
	};
	private RadioGroup.OnCheckedChangeListener sbChangeRadio = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == small5.getId()) {
				sbType = "small5";
				linear_bs.setVisibility(View.GONE);
			} else if (checkedId == big5.getId()) {
				sbType = "big5";
				linear_bs.setVisibility(View.VISIBLE);
			}
		}
	};
	
	private void setview() {
		
		settedpw_yes = (RelativeLayout) findViewById(R.id.settedpw_yes);
		settedpw_no = (RelativeLayout) findViewById(R.id.settedpw_no);
		
		trueName1 = (EditText) findViewById(R.id.trueName1);
		mobile1 = (EditText) findViewById(R.id.mobile1);
		idcard1 = (EditText) findViewById(R.id.idcard1);
		bankCode1 = (EditText) findViewById(R.id.bankCode1);
		accountNo1 = (EditText) findViewById(R.id.accountNo1);
		pinganbank1 = (RadioButton) findViewById(R.id.pinganbank1);
		otherbank1 = (RadioButton) findViewById(R.id.otherbank1);
		
		tansNum = (EditText) findViewById(R.id.tansNum);
		mobile = (EditText) findViewById(R.id.mobile);
		idcard = (EditText) findViewById(R.id.idcard);
		search = (EditText) findViewById(R.id.search);
		paypw = (EditText) findViewById(R.id.paypw);
		paypw2 = (EditText) findViewById(R.id.paypw2);
		ll_message = (LinearLayout) findViewById(R.id.ll_message);
		linear_po = (LinearLayout) findViewById(R.id.linear_po);
		linear_bs = (LinearLayout) findViewById(R.id.linear_bs);
		
		openbank = (Spinner) findViewById(R.id.openbank);
		ppcbAdapter = new ArrayAdapter<PubPayCnapsBank>(this, R.layout.spinner_item,R.id.contentTextView, listppcb);
		openbank.setAdapter(ppcbAdapter);
		openbank.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currppcb = listppcb.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		p = (Spinner) findViewById(R.id.p);
		pAdapter = new ArrayAdapter<PubPayNode>(this, R.layout.spinner_item,R.id.contentTextView, listppn);
		p.setAdapter(pAdapter);
		c = (Spinner) findViewById(R.id.c);
		cAdapter = new ArrayAdapter<PubPayCity>(this, R.layout.spinner_item,R.id.contentTextView, listppc);
		c.setAdapter(cAdapter);
		p.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currppn = listppn.get(position);
				if (pfirst) {
					data.getApiResult(new AsyncHttpResponseHandler() {

						@Override
						public void onStart() {
							super.onStart();
							dialog = dialogUtil.loading("正在加载!", "请稍候...");
						}

						@Override
						public void onSuccess(String content) {
							dialog.dismiss();
							Gson gson = new Gson();
							HashMap<String, Object> map = gson.fromJson(
									(String) content,
									new TypeToken<Map<String, Object>>() {
									}.getType());
							if (map.get("returnCode").equals("Success")) {
								List<Map<String, Object>> citysMap = (List<Map<String, Object>>) map.get("areas");
								listppc.clear();
								for (Map<String, Object> m : citysMap) {
									PubPayCity c = new PubPayCity(m);
									listppc.add(c);
								}
								if(!listppc.isEmpty())
									currppc = listppc.get(0);
								else
									currppc = null;
								mHandler.sendEmptyMessage(RC);
							} else {
								Toast.makeText(BindBankCardActivity.this,
										(CharSequence) map.get("message"),
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							if (content != null && content.equals("can't resolve host"))
								Toast.makeText(BindBankCardActivity.this, "网络连接异常",
										Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					}, "/mobile/wallet/myWallet/getAreas?cityCode=" + currppn.getNodeCode());
				}
				pfirst = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}});
		c.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currppc = listppc.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}});
		
		poRadioGroup = (RadioGroup) findViewById(R.id.poRadioGroup);
		pinganbank = (RadioButton) findViewById(R.id.pinganbank);
		otherbank = (RadioButton) findViewById(R.id.otherbank);
		poRadioGroup.setOnCheckedChangeListener(poChangeRadio);
		
		sbRadioGroup = (RadioGroup) findViewById(R.id.sbRadioGroup);
		small5 = (RadioButton) findViewById(R.id.small5);
		big5 = (RadioButton) findViewById(R.id.big5);
		sbRadioGroup.setOnCheckedChangeListener(sbChangeRadio);
		
		trueName = (EditText) findViewById(R.id.trueName);
		accountNo = (EditText) findViewById(R.id.accountNo);
		bankCode = (TextView) findViewById(R.id.bankCode);
		next_btn = (Button) findViewById(R.id.next_btn);
		search_btn = (Button) findViewById(R.id.search_btn);
		btnSave = (Button) findViewById(R.id.btnSave);
		unbindcard = (Button) findViewById(R.id.unbindcard);
		submit = (Button) findViewById(R.id.submit);
		bankCode_btn = (TextView) findViewById(R.id.bankCode_btn);
		unbindcard.setOnClickListener(this);
		submit.setOnClickListener(this);
		next_btn.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		bankCode_btn.setOnClickListener(this);
		onlyOne = (CheckBox) findViewById(R.id.onlyOne);
		onlyOne.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				receiveCard  = isChecked;
			}});
		
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			
			if (user.isChildUser()) {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnC.setVisibility(View.GONE);
				btnD.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnG.setVisibility(View.GONE);
			}else {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				if(user.isRealUser()){
					btnC.setVisibility(View.GONE);
					btnD.setVisibility(View.VISIBLE);
					btnG.setVisibility(View.VISIBLE);
				}else {
					btnC.setVisibility(View.VISIBLE);
					btnD.setVisibility(View.GONE);
					btnG.setVisibility(View.GONE);
				}
			}
		}
	}
	
	private void refresh(final Map<String,Object> content){
		final String settedpw = (String) content.get("settedPW");
		
		BindBankCardActivity.this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				AccountData accountData = new AccountData((Map<String, Object>) content.get("accountData"));
				if("yes".equals(settedpw)){
					
					settedpw_no.setVisibility(View.GONE);
					settedpw_yes.setVisibility(View.VISIBLE);
					
					UserBankData userBankData = new UserBankData((Map<String, Object>) content.get("userBankData"));
					userBindedBankCardId = userBankData.getId();
					idcard1.setText(accountData.getIdCode());
					trueName1.setText(userBankData.getAccountName());
					accountNo1.setText(userBankData.getCardNo());
					mobile1.setText(accountData.getMobile());
					bankCode1.setText(userBankData.getBankCode().getDescription());
					if(userBankData.getBankCode() == BankCode.SPABANK){
						pinganbank1.setChecked(true);
						otherbank1.setChecked(false);
					}else{
						pinganbank1.setChecked(false);
						otherbank1.setChecked(true);
					}
				}else{
					settedpw_no.setVisibility(View.VISIBLE);
					settedpw_yes.setVisibility(View.GONE);
					
					idcard.setText(accountData.getIdCode());
					
					List<Map<String,Object>> list = (List<Map<String, Object>>) content.get("allProvince");
					List<Map<String,Object>> list1 = (List<Map<String, Object>>) content.get("currc");
					listppn.clear();listppc.clear();
					for(Map<String,Object> map : list){
						PubPayNode ppn = new PubPayNode(map);
						listppn.add(ppn);
					}
					for(Map<String,Object> map : list1){
						PubPayCity ppc = new PubPayCity(map);
						listppc.add(ppc);
					}
					if(!listppn.isEmpty())
						currppn = listppn.get(0);
					if(!listppc.isEmpty())
						currppc = listppc.get(0);
					mHandler.sendEmptyMessage(RPC);
				}
			}});
	}
	
	private void initDate() {
		final Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("信息初始化中", "请稍候...");
			}

			@Override
			public void onSuccess(String json) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(json,"/mobile/wallet/myWallet/bindCardInit",params);

				if ("Success".equals(cd.getReturnCode())) {
					Map<String,Object> content = (Map<String, Object>) cd.getContent();
					refresh(content);
				} else {
					Toast.makeText(BindBankCardActivity.this, cd.getMessage(), Toast.LENGTH_LONG).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(BindBankCardActivity.this, "信息初始化失败", Toast.LENGTH_LONG).show();
				dialog.dismiss();

			}

		};
		data.getApiResult(handler, "/mobile/wallet/myWallet/bindCardInit", params, "get");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			loadDate();
			break;
		case R.id.bankCode_btn:
			startActivityForResult(new Intent(this, BankListActivity.class).putExtra(BankListActivity.INCLUDE_PINGAN, false),REQUEST_CODE_BANK);
			break;
		case R.id.search_btn:
			searchOpenBank();
			break;
		case R.id.btnSave:// 重新发送验证码
			loadDate();
			break;
		case R.id.unbindcard:// 
			DialogUtil dialogAlert = new DialogUtil(this);
			dialogAlert.showDialogFromConfig("提示", "你确认要解除绑定该银行卡吗？", new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					unBindCard(userBindedBankCardId);
					
				}
			});
			break;
		case R.id.submit:// 
			if(TextUtils.isEmpty(tansNum.getText().toString().trim())){
				Toast.makeText(this, "请输入短信验证码！", Toast.LENGTH_SHORT).show();
				return;
			}
			bindCard(tansNum.getText().toString().trim());
			break;
		default:
			break;
		}
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
					initDate();
				} else {
					Toast.makeText(BindBankCardActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(BindBankCardActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(BindBankCardActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(BindBankCardActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(BindBankCardActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("bindCardId", bindCardId);
		data.getApiResult(handler, "/mobile/wallet/myWallet/unBindCard", params, "post");
	}

	private void searchOpenBank() {

		final Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("查询中", "请稍候...");
			}

			@Override
			public void onSuccess(String json) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson(
						(String) json,
						new TypeToken<Map<String, Object>>() {
						}.getType());
				if (map.get("returnCode").equals("Success")) {
					List<Map<String, Object>> citysMap = (List<Map<String, Object>>) map.get("ppcbs");
					listppcb.clear();
					for (Map<String, Object> m : citysMap) {
						PubPayCnapsBank c = new PubPayCnapsBank(m);
						listppcb.add(c);
					}
					if(!listppcb.isEmpty())
						currppcb = listppcb.get(0);
					else
						currppcb = null;
						
					mHandler.sendEmptyMessage(RPPCB);
				} else {
					Toast.makeText(BindBankCardActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(BindBankCardActivity.this, "信息初始化失败", Toast.LENGTH_LONG).show();
				dialog.dismiss();

			}

		};
		if(!TextUtils.isEmpty(search.getText().toString().trim()))
			params.put("keyword", search.getText().toString().trim());
		if(bankCodeStr!=null)
			params.put("bank", bankCodeStr);
		else{
			Toast.makeText(BindBankCardActivity.this, "请选择银行", 1).show();
			return;
		}
		params.put("acode", currppc!=null?currppc.getOraAreaCode():"");
		data.getApiResult(handler, "/mobile/wallet/myWallet/searchOpenBank", params, "post");
	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_BANK) {
				bankCodeStr = data.getStringExtra("bankCode");
				
				BankCode bc = BankCode.valueOf(bankCodeStr);
				bankCode_btn.setText("〉");
				bankCode.setText(bc.getDescription());
			}else if(requestCode == REQUEST_CODE_NEXT){
				String MessageCode = data.getStringExtra("MessageCode");
				if(BindBankCardNextActivity.RESEND.equals(MessageCode)){
					loadDate();
				}else
					bindCard(MessageCode);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void bindCard(String messageCode) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					BindBankCardActivity.this.finish();
				} else {
					Toast.makeText(BindBankCardActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(BindBankCardActivity.this, "信息提交失败", 1).show();
				dialog.dismiss();

			}

		};
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("MessageCode", messageCode);
//		params.put("cardNo", accountNo.getText().toString().trim());
//		params.put("bankCode", bankCodeStr);
//		params.put("accountName", trueName.getText().toString().trim());
//		params.put("IdCode", idcard.getText().toString().trim());
//		params.put("MobilePhone", mobile.getText().toString().trim());
		
		if(!validate(params))
			return ;
		
		if(receiveCard)
			params.put("isRcvCard", YesNoCode.yes);
		else
			params.put("isRcvCard", YesNoCode.no);
		data.getApiResult(handler, "/mobile/wallet/myWallet/bindCard", params);
	}
	
	@Override
	protected void onDestroy() {
		if(timer!=null)
			timer.cancel();
		super.onDestroy();
	}
	
	@Override
	public void onStart() {
		super.onStart();
			setTitle("绑卡及支付密码");
			
			setRight(R.drawable.commen_top_right, new OnClickListener() {

				@Override
				public void onClick(View v) {
					popupWindow.togglePopWindow(v);
				}
			});
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
//					startActivityForResult(new Intent(BindBankCardActivity.this, BindBankCardNextActivity.class),REQUEST_CODE_NEXT);
					ll_message.setVisibility(View.VISIBLE);
					
					if(timer!=null)
						timer.cancel();
					
					timer = new TimeCount(120000, 1000);// 构造CountDownTimer对象
					timer.start();
					
					next_btn.setVisibility(View.GONE);
					submit.setVisibility(View.VISIBLE);
					
				} else {
					Toast.makeText(BindBankCardActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(BindBankCardActivity.this, "信息提交失败", 1).show();
				dialog.dismiss();

			}

		};
		Map<String, Object> params = new HashMap<String, Object>();
		if(!validate(params))
			return ;
		
		data.getApiResult(handler, "/mobile/wallet/myWallet/prebindCard", params);
	}
	
	private boolean validate(Map<String, Object> params){

		if(!TextUtils.isEmpty(idcard.getText().toString().trim()))
			params.put("IdCode", idcard.getText().toString().trim());
		else{
			Toast.makeText(BindBankCardActivity.this, "请填写身份证", 1).show();
			return false;
		}
		if(!TextUtils.isEmpty(trueName.getText().toString().trim()))
			params.put("accountName", trueName.getText().toString().trim());
		else{
			Toast.makeText(BindBankCardActivity.this, "请填写账户名称", 1).show();
			return false;
		}
		if(!TextUtils.isEmpty(accountNo.getText().toString().trim()))
			params.put("cardNo", accountNo.getText().toString().trim());
		else{
			Toast.makeText(BindBankCardActivity.this, "请填写银行账户", 1).show();
			return false;
		}	
		if(!TextUtils.isEmpty(mobile.getText().toString().trim()))
			params.put("MobilePhone", mobile.getText().toString().trim());
		else{
			Toast.makeText(BindBankCardActivity.this, "请填写银行预留手机号", 1).show();
			return false;
		}
		String phone = mobile.getText().toString().trim();
		if (phone != null && phone.length() != 11) {
			Toast.makeText(BindBankCardActivity.this, "手机号长度为11位！", 1).show();
			return false;
		} 
		if(!TextUtils.isEmpty(paypw.getText().toString().trim()))
			params.put("paypwd", paypw.getText().toString().trim());
		else{
			Toast.makeText(BindBankCardActivity.this, "请填写支付密码", 1).show();
			return false;
		}
		if(!TextUtils.isEmpty(paypw2.getText().toString().trim())){
			if(!(paypw.getText().toString().trim()).equals(paypw2.getText().toString().trim())){
				Toast.makeText(BindBankCardActivity.this, "两次支付密码不同！", 1).show();
				return false;
			}	
		}else{
			Toast.makeText(BindBankCardActivity.this, "请填写确认支付密码", 1).show();
			return false;
		}
		params.put("BankType", BankType);
		params.put("sbType", sbType);
		if(!BankType.equals("pinganBank")){
			if(bankCodeStr!=null)
				params.put("bankCode", bankCodeStr);
			else{
				Toast.makeText(BindBankCardActivity.this, "请选择银行", 1).show();
				return false;
			}
			
			if(!sbType.equals("small5")){
				if(currppcb==null){
					Toast.makeText(BindBankCardActivity.this, "请查找开户行", 1).show();
					return false;
				}else
					params.put("openBank", currppcb.getId().toString());
			}
		}else{
			bankCodeStr = BankCode.SPABANK.name();
			params.put("bankCode", bankCodeStr);
		}
		return true;
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnSave.setText("重新发送验证码");
			btnSave.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnSave.setClickable(false);
			btnSave.setText(millisUntilFinished / 1000 + "秒后需重新获取验证码");
		}
	}
}
