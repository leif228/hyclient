package com.eyunda.third.activities.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.YydArea;
import com.eyunda.third.domain.YydCity;
import com.eyunda.third.domain.YydProvince;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
public class AgentApplyActivity extends CommonListActivity implements
OnClickListener 
{	
	private View newAgent;
	private Button basic_submit,basic_back;
	private Spinner agentType;
	private Spinner p, c, a;
	private EditText userNameEditText, true_name, basic_phone;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	private EditText c_address;
	private EditText com_name;
	private BasePopupWindow popupWindow;// 弹出菜单

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;
	
	private String typeSelect="";
	private String agentSel="";
	private String pSelect = "";
	List<String> pn = new ArrayList<String>();
	List<String> cn = new ArrayList<String>();
	List<String> an = new ArrayList<String>();
	OnItemSelectedListener agentTypel, pl, cl, al;
	int pposition = 0;
	int cposition = 0;
	String areaNo = "";// 广州市区
	List<YydProvince> plist = new ArrayList<YydProvince>();
	List<YydCity> clist = new ArrayList<YydCity>();
	List<YydArea> alist = new ArrayList<YydArea>();
	private String aSelect = "";
	private int selCitySize = -1;// 选择的城市下区级的个数
	private String selCityNo = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_agent_apply);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
		agentSel = getIntent().getStringExtra("agentSel");
		newAgent = (View) this.findViewById(R.id.newAgent);
		if("new".equals(agentSel))
			newAgent.setVisibility(View.VISIBLE);
		else
			newAgent.setVisibility(View.GONE);

		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_back = (Button) this.findViewById(R.id.basic_back);
		basic_submit.setOnClickListener(this);
		basic_back.setOnClickListener(this);
		true_name = (EditText) findViewById(R.id.true_name);
		basic_phone = (EditText) findViewById(R.id.basic_phone);
		userNameEditText = (EditText) findViewById(R.id.login_name);
		passwordEditText = (EditText) findViewById(R.id.pwd);
		confirmPwdEditText = (EditText) findViewById(R.id.pwd_again);
		c_address = (EditText) findViewById(R.id.c_address);
		com_name = (EditText) findViewById(R.id.com_name);
		agentType = (Spinner) findViewById(R.id.agentType);
		p = (Spinner) findViewById(R.id.p);
		c = (Spinner) findViewById(R.id.c);
		a = (Spinner) findViewById(R.id.a);
		agentTypel = new TypeSpinnerOnSelectedListener();
		pl = new PSpinnerOnSelectedListener();
		cl = new CSpinnerOnSelectedListener();
		al = new ASpinnerOnSelectedListener();
		agentType.setOnItemSelectedListener(agentTypel);
		p.setOnItemSelectedListener(pl);
		c.setOnItemSelectedListener(cl);
		a.setOnItemSelectedListener(al);
		initTypeSpinner();
		setview();
//		synloadPCA();
		setPopupWindow();
	}
	private void synloadPCA() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				// dialog = dialogUtil.loading("正在注册!", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				// dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)) {
						boolean contentMD5Changed = (Boolean) map
								.get(ApplicationConstants.CONTENTMD5CHANGED);
						SharedPreferencesUtils s = new SharedPreferencesUtils(
								"/mobile/login/getregister", null);
						if (contentMD5Changed
								&& NetworkUtils.isNetworkAvailable()) {
							s.setParam(content);
						} else {
							String localJsion = s.getParam();
							map = gson.fromJson(localJsion,
									new TypeToken<Map<String, Object>>() {
									}.getType());
						}
					}
					List<Map<String, Object>> allProvinceMap = (List<Map<String, Object>>) map
							.get("content");
					for (Map<String, Object> m : allProvinceMap) {
						YydProvince p = new YydProvince(m);
						plist.add(p);
						pn.add(p.getProvinceName());
					}
					AgentApplyActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							refreshProvince();
						}
					});

				} else {

					Toast.makeText(AgentApplyActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(AgentApplyActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				// dialog.dismiss();
			}
		};
		// 传参请求服务
		data.getApiResult(handler, "/mobile/login/getregister");

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initTypeSpinner() {
		typeSelect=UserRoleCode.owner.toString();
		List<String> list = new ArrayList<String>();
		
		list.add(UserRoleCode.owner.getDescription());
		list.add(UserRoleCode.master.getDescription());
		ArrayAdapter t = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, list);
		agentType.setAdapter(t);
		agentType.setPrompt("代理类型");
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void refreshProvince() {
		ArrayAdapter paa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, pn);
		p.setAdapter(paa);
		p.setPrompt("省");
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void refreshCity() {
		ArrayAdapter caa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, cn);
		c.setAdapter(caa);
		c.setPrompt("市");

		alist.clear();
		an.clear();
		an.add("选择区域");
		refreshArea();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void refreshArea() {
		ArrayAdapter aaa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, an);
		a.setAdapter(aaa);
		a.setPrompt("区");
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setview() {
		pn.add("选择省份");
		ArrayAdapter paa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, pn);
		p.setAdapter(paa);
		p.setPrompt("省");

		cn.add("选择城市");
		ArrayAdapter caa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, cn);
		c.setAdapter(caa);
		c.setPrompt("市");

		an.add("选择区域");
		ArrayAdapter aaa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, an);
		a.setAdapter(aaa);
		a.setPrompt("区");
	}
	private void setPopupWindow() {
		popupWindow = new BasePopupWindow(this);
		// 基本信息
		btnA = popupWindow.getBtnA();
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentApplyActivity.this,
						BasicInfoActivity.class));
			}
		});
		btnB = popupWindow.getBtnB();
		btnB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentApplyActivity.this,
						AccountListActivity.class));
			}
		});
		
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentApplyActivity.this,
						ApplyAgentActivity.class));
			}
		});
		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentApplyActivity.this,
						AgentActivity.class));
			}
		});
		// 修改密码
		btnF = popupWindow.getBtnF();
		btnF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentApplyActivity.this,
						UpdatePwd.class));
			}
		});
		// 管理子账号
		btnG = popupWindow.getBtnG();
		btnG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(AgentApplyActivity.this,
						UserChildListActivity.class));
			}
		});
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			if(user.isChildUser()){
				btnA.setVisibility(View.VISIBLE);
				btnC.setVisibility(View.GONE);
				btnD.setVisibility(View.GONE);
				btnB.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnG.setVisibility(View.GONE);
			}else {
				btnA.setVisibility(View.VISIBLE);
				btnC.setVisibility(View.VISIBLE);
				btnD.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.VISIBLE);
				btnF.setVisibility(View.VISIBLE);
				btnG.setVisibility(View.VISIBLE);
				if(user.isRealUser()){
					btnD.setVisibility(View.VISIBLE);
					btnG.setVisibility(View.VISIBLE);
				}else {
					btnD.setVisibility(View.GONE);
					btnG.setVisibility(View.GONE);
				}
			}
		}
		
	}


	@Override
	public void onStart() {
		super.onStart();
		setTitle("添加代理关系");
//		setRight(R.drawable.commen_top_right, new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				popupWindow.togglePopWindow(v);
//			}
//		});
	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}
	protected void loadDate1(final String username, String pwd, 
			String address, String cname) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("正在提交!", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					finish();
				} else {

					Toast.makeText(AgentApplyActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(AgentApplyActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		};
		// 传参请求服务
		Map<String, Object> params = new HashMap<String, Object>();
		if(typeSelect.equals(UserRoleCode.owner.getDescription()))
			params.put("roleCode", UserRoleCode.owner);
		else
			params.put("roleCode", UserRoleCode.master);
			
		params.put("agentSel", agentSel);
		params.put("mobile", basic_phone.getText().toString().trim());
		if("new".equals(agentSel)){
			params.put("trueName", true_name.getText().toString().trim());
			params.put("password", pwd);
			params.put("address", address);
			params.put("unitName", cname);
//			if (areaNo.equals("")) {
//				if (pSelect.equals("澳门特别行政区"))
//					areaNo = "82";
//				else if (pSelect.equals("香港特别行政区"))
//					areaNo = "81";
//				else if (pSelect.equals("台湾省"))
//					areaNo = "71";
//				else if ("选择区域".equals(aSelect) && selCitySize == 0)
//					areaNo = selCityNo;
//				else if ("选择区域".equals(aSelect)||selCitySize == -1) {
//					Toast.makeText(AgentApplyActivity.this, "请选择所在地区",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//			}
			params.put("areaCode", areaNo);
		}
		data.getApiResult(handler, "/mobile/account/myAccount/saveUserAgent", params);
	}
	private void check(){
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		String address = c_address.getText().toString().trim();
		String cname = com_name.getText().toString().trim();
//		if ("old".equals(agentSel)&&TextUtils.isEmpty(username)) {
//			Toast.makeText(this, "登入名不能为空！", Toast.LENGTH_SHORT).show();
//			userNameEditText.requestFocus();
//			return;
//		} 
		if (TextUtils.isEmpty(basic_phone.getText())) {
			Toast.makeText(this, "手机不能为空！", Toast.LENGTH_SHORT).show();
			basic_phone.requestFocus();
			return;
		}
		if("new".equals(agentSel)){
			if(TextUtils.isEmpty(true_name.getText())){
				Toast.makeText(this, "真实姓名不能为空！", Toast.LENGTH_SHORT).show();
				true_name.requestFocus();
				return;
			}  else if (TextUtils.isEmpty(pwd)) {
				Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
				passwordEditText.requestFocus();
				return;
	
			} else if (pwd.length() < 6) {
				Toast.makeText(this, "密码至少6位！", Toast.LENGTH_SHORT).show();
				passwordEditText.requestFocus();
				return;
	
			} else if (TextUtils.isEmpty(confirm_pwd)) {
				Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
				confirmPwdEditText.requestFocus();
				return;
			} else if (!pwd.equals(confirm_pwd)) {
				Toast.makeText(this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
//			else if (TextUtils.isEmpty(address)) {
//				Toast.makeText(this, "请输入公司详细地址！", Toast.LENGTH_SHORT).show();
//				c_address.requestFocus();
//				return;
//	
//			} else if (TextUtils.isEmpty(cname)) {
//				Toast.makeText(this, "请输入公司名称！", Toast.LENGTH_SHORT).show();
//				com_name.requestFocus();
//				return;
//	
//			} 
		}
		loadDate1(username, pwd, address, cname);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.basic_submit:

			try {
				check();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.basic_back:
			finish();

			break;
		default:
			break;
		}

	}
	class PSpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			// refreshCA(position);
			pSelect = (String) adapterView.getItemAtPosition(position);
			if (position != 0) {
				String proCode = plist.get(position - 1).getProvinceNo();
				data.getApiResult(new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						dialog = dialogUtil.loading("正在加载!", "请稍候...");
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String content) {
						dialog.dismiss();
						Gson gson = new Gson();
						HashMap<String, Object> map = gson.fromJson(
								(String) content,
								new TypeToken<Map<String, Object>>() {
								}.getType());
						if (map.get("returnCode").equals("Success")) {
							clist.clear();
							cn.clear();
							cn.add("选择城市");
							List<Map<String, Object>> allMap = (List<Map<String, Object>>) map
									.get("content");
							for (Map<String, Object> m : allMap) {
								YydCity c = new YydCity(m);
								clist.add(c);
								cn.add(c.getCityName());
							}
							AgentApplyActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									refreshCity();
								}
							});
						} else {
							Toast.makeText(AgentApplyActivity.this,
									(CharSequence) map.get("message"),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if (content != null
								&& content.equals("can't resolve host"))
							Toast.makeText(AgentApplyActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}, "/mobile/login/getCitys?proCode=" + proCode);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}

	class CSpinnerOnSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			// refreshA(position);
			if (position != 0) {
				String cityCode = clist.get(position - 1).getCityNo();
				selCityNo = cityCode;
				data.getApiResult(new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						dialog = dialogUtil.loading("正在加载!", "请稍候...");
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String content) {
						dialog.dismiss();
						Gson gson = new Gson();
						HashMap<String, Object> map = gson.fromJson(
								(String) content,
								new TypeToken<Map<String, Object>>() {
								}.getType());
						if (map.get("returnCode").equals("Success")) {
							alist.clear();
							an.clear();
							an.add("选择区域");
							List<Map<String, Object>> allMap = (List<Map<String, Object>>) map
									.get("content");
							selCitySize = allMap.size();
							for (Map<String, Object> m : allMap) {
								YydArea a = new YydArea(m);
								alist.add(a);
								an.add(a.getAreaName());
							}
							AgentApplyActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									refreshArea();
								}
							});
						} else {
							Toast.makeText(AgentApplyActivity.this,
									(CharSequence) map.get("message"),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if (content != null
								&& content.equals("can't resolve host"))
							Toast.makeText(AgentApplyActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}, "/mobile/login/getAreas?cityCode=" + cityCode);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}

	class ASpinnerOnSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			// areaNo = three[pposition][cposition][position].getAreaNo();
			aSelect = (String) adapterView.getItemAtPosition(position);
			if (position != 0)
				areaNo = alist.get(position - 1).getAreaNo();
			else
				areaNo = "";
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}
	class TypeSpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			typeSelect = (String) adapterView.getItemAtPosition(position);
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}
	@Override
	protected void loadDate() {

	}
}
