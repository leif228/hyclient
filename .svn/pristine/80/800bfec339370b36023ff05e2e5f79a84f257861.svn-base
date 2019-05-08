package com.eyunda.third.activities.user;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.chat.utils.SIMCardInfo;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.YydArea;
import com.eyunda.third.domain.YydCity;
import com.eyunda.third.domain.YydProvince;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.domain.enumeric.UserTypeCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class RegisterActivity extends CommonListActivity implements
		OnClickListener {
	protected static final int RECAPTCHA = 0;

	private String uuid = "";

	//图片资源  
	private String url = ApplicationConstants.SERVER_URL+"/mobile/login/captcha";
	
	private EditText userNameEditText, captcha;
	private ImageView captcha_img;
	private TextView changCaptcha;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	private EditText et_email;
	private EditText c_address;
	private EditText com_name;
	private EditText trueName;
	private EditText mobile;
	private Button basic_submit, basic_back;

	private CheckBox cb_pt, cb_cd, cb_hd, cb_cdon;
	private CheckBox agree;
	private TextView oneyd;
	private boolean cd = false, cdon = false;
//	private boolean pt = false, cd = false, hd = false, cdon = false;
	private boolean isagree = false;
	
	private RadioGroup mRadioGroup;
	private RadioButton prb, erb;
	private UserTypeCode utc;

	private Spinner p, c, a;
	private String pSelect = "";
	List<String> pn = new ArrayList<String>();
	List<String> cn = new ArrayList<String>();
	List<String> an = new ArrayList<String>();
//	List<String> one;
//	List<List<String>> two;
//	YydArea[][][] three;
	OnItemSelectedListener pl, cl, al;
	int pposition = 0;
	int cposition = 0;
	String areaNo = "";// 广州市区

	Data_loader data;
	DialogUtil dialogUtil;
	ProgressDialog dialog;

	private String phoneNumber = "0";
	private String simNum = "";

	List<YydProvince> plist = new ArrayList<YydProvince>();
	List<YydCity> clist = new ArrayList<YydCity>();
	List<YydArea> alist = new ArrayList<YydArea>();
	private String aSelect = "";
	private int selCitySize = -1;// 选择的城市下区级的个数
	private String selCityNo = "";
	
	private Bitmap bitmap = null;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case RECAPTCHA:
		        //显示  
		        captcha_img.setImageBitmap(bitmap);  
				break;
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		setTitle("注册");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_register_new);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_back = (Button) this.findViewById(R.id.basic_back);
		basic_submit.setOnClickListener(this);
		basic_back.setOnClickListener(this);
		userNameEditText = (EditText) findViewById(R.id.phonenum);
		mobile = (EditText) findViewById(R.id.mobile);
		trueName = (EditText) findViewById(R.id.trueName);
		passwordEditText = (EditText) findViewById(R.id.pwd);
		confirmPwdEditText = (EditText) findViewById(R.id.pwd_again);
		et_email = (EditText) findViewById(R.id.register_email);
		c_address = (EditText) findViewById(R.id.c_address);
		com_name = (EditText) findViewById(R.id.com_name);
//		cb_pt = (CheckBox) this.findViewById(R.id.cb_pt);
		cb_cd = (CheckBox) this.findViewById(R.id.cb_cd);
//		cb_hd = (CheckBox) this.findViewById(R.id.cb_hd);
		cb_cdon = (CheckBox) this.findViewById(R.id.cb_cdon);
		agree = (CheckBox) this.findViewById(R.id.agree);
		oneyd = (TextView) this.findViewById(R.id.oneyd);
		
		captcha = (EditText) this.findViewById(R.id.captcha);
		captcha_img = (ImageView) this.findViewById(R.id.captcha_img);
		changCaptcha = (TextView) this.findViewById(R.id.changCaptcha);
		changCaptcha.setOnClickListener(this);
		
		//得到可用的图片  
        getHttpBitmap(); 
		
		p = (Spinner) findViewById(R.id.p);
		c = (Spinner) findViewById(R.id.c);
		a = (Spinner) findViewById(R.id.a);
		pl = new PSpinnerOnSelectedListener();
		cl = new CSpinnerOnSelectedListener();
		al = new ASpinnerOnSelectedListener();
		p.setOnItemSelectedListener(pl);
		c.setOnItemSelectedListener(cl);
		a.setOnItemSelectedListener(al);
		
		utc = UserTypeCode.person;
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		prb = (RadioButton) findViewById(R.id.person);
		erb = (RadioButton) findViewById(R.id.enterprise);
		mRadioGroup.setOnCheckedChangeListener(mChangeRadio);
		
		setview();
//		synloadPCA();

		oneyd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RegisterActivity.this,
						EydProtocolActivity.class);
				startActivity(intent);
			}
		});

		agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// Toast.makeText(MyActivity.this, arg1 ? "选中了" : "取消了选中",
				// Toast.LENGTH_LONG).show();
				isagree = arg1;
			}
		});
//		cb_pt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				pt = arg1;
//			}
//		});
		cb_cd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				cd = arg1;
			}
		});
//		cb_hd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				hd = arg1;
//			}
//		});
		cb_cdon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				cdon = arg1;
			}
		});

		SIMCardInfo sim = SIMCardInfo.getInstance(this);
		phoneNumber = sim.getPhoneNumber();
		if (!phoneNumber.equals("0"))
			userNameEditText.setText(phoneNumber);
		simNum = sim.getSimCardNumber();

	}
	private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == prb.getId()) {
				utc = UserTypeCode.person;
			} else if (checkedId == erb.getId()) {
				utc = UserTypeCode.enterprise;
			}
		}
	};
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
					RegisterActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							refreshProvince();
						}
					});

				} else {

					Toast.makeText(RegisterActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(RegisterActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				// dialog.dismiss();
			}
		};
		// 传参请求服务
		data.getApiResult(handler, "/mobile/login/getregister");

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void refreshProvince() {
		ArrayAdapter paa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, pn);
		p.setAdapter(paa);
		p.setPrompt("省");
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
							RegisterActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									refreshCity();
								}
							});
						} else {
							Toast.makeText(RegisterActivity.this,
									(CharSequence) map.get("message"),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if (content != null
								&& content.equals("can't resolve host"))
							Toast.makeText(RegisterActivity.this, "网络连接异常",
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
							RegisterActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									refreshArea();
								}
							});
						} else {
							Toast.makeText(RegisterActivity.this,
									(CharSequence) map.get("message"),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if (content != null
								&& content.equals("can't resolve host"))
							Toast.makeText(RegisterActivity.this, "网络连接异常",
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

	public void register() {
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		String email = et_email.getText().toString().trim();
		String address = c_address.getText().toString().trim();
		String cname = com_name.getText().toString().trim();
		String mobiles = mobile.getText().toString().trim();
		String trueNames = trueName.getText().toString().trim();
		String captchas = captcha.getText().toString().trim();
		// if (TextUtils.isEmpty(username)) {
		// Toast.makeText(this, "登录名不能为空！", Toast.LENGTH_SHORT).show();
		// userNameEditText.requestFocus();
		// return;
		// }

		if (TextUtils.isEmpty(mobiles)) {
			Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
			mobile.requestFocus();
			return;
		} else if (mobiles.length() != 11) {
			Toast.makeText(this, "设置11位手机号！", Toast.LENGTH_SHORT).show();
			mobile.requestFocus();
			return;
		} else if (TextUtils.isEmpty(trueNames)) {
			Toast.makeText(this, "真实姓名不能为空！", Toast.LENGTH_SHORT).show();
			trueName.requestFocus();
			return;

		}
		// else if (TextUtils.isEmpty(cname)) {
		// Toast.makeText(this, "请输入公司名称！", Toast.LENGTH_SHORT).show();
		// com_name.requestFocus();
		// return;
		//
		// }
		else if (TextUtils.isEmpty(email)) {
			Toast.makeText(this, "邮箱不能为空，用于找回密码！", Toast.LENGTH_SHORT).show();
			et_email.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
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
		} else if (TextUtils.isEmpty(captchas)) {
			Toast.makeText(this, "请输入验证码！", Toast.LENGTH_SHORT).show();
			captcha.requestFocus();
			return;

		} else if (!isagree) {
			Toast.makeText(this, "请先阅读并同意《易运达货运电商平台服务协议》", Toast.LENGTH_LONG)
					.show();
			return;
		}
		toRegister(username, pwd, email, address, cname, mobiles, trueNames,
				captchas);
	}

	private void toRegister(final String username, String pwd, String email,
			String address, String cname, final String mobiles, String trueNames, String captchas) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("正在注册!", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					Toast.makeText(RegisterActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(RegisterActivity.this,
							LoginActivity.class)
							.putExtra("loginName", mobiles));
					finish();
				} else {

					Toast.makeText(RegisterActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(RegisterActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		};
		// 传参请求服务
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("userType", utc);
		params.put("loginName", "");
		params.put("password", pwd);
		params.put("email", email);
		params.put("address", address);
		params.put("unitName", cname);
		params.put("mobile", mobiles);
		params.put("trueName", trueNames);
		
//		if (areaNo.equals("")) {
//			if (pSelect.equals("澳门特别行政区"))
//				areaNo = "82";
//			else if (pSelect.equals("香港特别行政区"))
//				areaNo = "81";
//			else if (pSelect.equals("台湾省"))
//				areaNo = "71";
//			else if ("选择区域".equals(aSelect) && selCitySize == 0)
//				areaNo = selCityNo;
//			else if ("选择区域".equals(aSelect)||selCitySize == -1) {
//				Toast.makeText(RegisterActivity.this, "请选择所在地区",
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//		}
//		params.put("areaCode", areaNo);
		
		// CONSIGNOR("1", "货主"),CARGOPROXY("2", "货代"),SHIPMASTER("3",
		// "船东"),SHIPPROXY("4", "船代"),CHILDUSER("5", "子帐号");
		// boolean pt=false,cd=false,hd=false,cdon=false;
		String roles = "";
//		if (cdon)
//			roles += "2";
//		if (cd)
//			roles += "1";
//		if("".equals(roles)){
//			Toast.makeText(RegisterActivity.this, "请选择角色",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}else if(roles.length()==2){
			roles ="1,2";
//		}
		params.put("roles", UserRoleCode.member.ordinal());
		// if(!simNum.equals(""))
		// params.put("simCardNo", simNum);
		
		params.put("uuid", uuid);
		params.put("captcha", captchas);
		data.getApiResult(handler, "/mobile/login/register", params);
	}

	public void back(View view) {
		finish();
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.basic_submit:

			try {
				register();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.basic_back:
			Intent intent = new Intent(this, MenuActivity.class);

			startActivity(intent);

			break;
		case R.id.changCaptcha:
			 //得到可用的图片  
	        getHttpBitmap();  
			break;
		default:
			break;
		}

	}
	private void getHttpBitmap(){  
		new Thread(new Runnable() {
			@Override
			public void run() {
				uuid  = UUID.randomUUID().toString();
				String urls = url + "?uuid=" + uuid;
				
		        URL myFileURL;  
		        try{  
		            myFileURL = new URL(urls);  
		            //获得连接  
		            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();  
		            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制  
		            conn.setConnectTimeout(6000);  
		            //连接设置获得数据流  
		            conn.setDoInput(true);  
		            //不使用缓存  
		            conn.setUseCaches(false);  
		            //这句可有可无，没有影响  
		            //conn.connect();  
		            //得到数据流  
		            InputStream is = conn.getInputStream();  
		            //解析得到图片  
		            bitmap = BitmapFactory.decodeStream(is);  
		            mHandler.sendEmptyMessage(RegisterActivity.RECAPTCHA);
		            //关闭数据流  
		            is.close();  
		        }catch(Exception e){  
		            e.printStackTrace();  
		        }  
		          
			}
		}).start();
    }  
}
