package com.eyunda.third.activities.user;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.account.BindBankCardActivity;
import com.eyunda.third.activities.chat.AlertDialog;
import com.eyunda.third.adapters.chat.manager.UserSetManager;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.YydArea;
import com.eyunda.third.domain.YydCity;
import com.eyunda.third.domain.YydProvince;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.domain.enumeric.UserTypeCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.eyunda.tools.ImageCompress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class BasicInfoActivity extends CommonListActivity implements
		OnClickListener {

	private static final String TAG = "BasicInfoActivity";
	private static final int REQUEST_CODE_SIGNATURE = 1;
	private static final int REQUEST_CODE_SIGNATURE_SUCCEED = 2;
	private static final int REQUEST_CODE_GETSYSIMG = 3;
	public static final int RESULT_OK_BTNTRUE = 4;
	private static final int REQUEST_CODE_CROPIMG_HEAD = 5;
	protected static final int RPCA = 6;
	protected static final int RC = 7;
	protected static final int RA = 8;
	private static final int RVIEW = 9;
	private static final int REQUEST_CODE_CROPIMG_SEAL = 10;
	protected static final int SYNCMOBILE = 0;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	ProgressDialog dialogpca;
	Data_loader data;
	ImageCompress compress;
	ImageCompress.CompressOptions options;
	
	private RadioGroup mRadioGroup;
	private RadioButton prb, erb;
	private UserTypeCode utc;
	
	protected String serialNo = "";
	protected boolean isSyncMobile = false;

	private TextView pingan_accountNo, mobile4;
	private Button basic_submit, basic_back, syncMobil_btn;
	private ImageView head_img, name_img, seal_img;
	private EditText login_name, true_name, nick_name, basic_email,
			basic_phone, basic_unitCode, address, messageCode;

	private String picPath = null;
	private int type = 0;
	private BasePopupWindow popupWindow;// 弹出菜单

	private String head_imgPath = null;
	private String name_imgPath = null;
	private String seal_imgPath = null;

	ImageLoader mImageLoader;
	String headUrl;
	String nameUrl;
	String sealUrl;

	private LinearLayout messageCode_ll;
	protected String mobilestr = "";

	private CheckBox cb_cd, cb_cdon;
	private boolean cd = false, cdon = false;

	private Spinner p, c, a;
	List<String> pn = new ArrayList<String>();
	List<String> cn = new ArrayList<String>();
	List<String> an = new ArrayList<String>();
	OnItemSelectedListener pl, cl, al;
	int pposition = 0;
	int cposition = 0;
	String areaNo = "";// 广州市区
	private boolean pfirst = false;
	
	private boolean load = true;//选择图片时不加载

	List<YydProvince> plist = new ArrayList<YydProvince>();
	List<YydCity> clist = new ArrayList<YydCity>();
	List<YydArea> alist = new ArrayList<YydArea>();
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case RPCA:
				refreshpca();
				break;
			case RC:
				refreshCity();
				break;
			case RA:
				refreshArea();
				break;
			case RVIEW:
				setView();
				break;
			case SYNCMOBILE:
				String str = (String) msg.obj;
				messageCode_ll.setVisibility(View.VISIBLE);
				mobile4.setText("手机后四位"+str);
				basic_phone.setText(mobilestr);
				break;
			}
		}
	};
	public YydProvince currp;
	public YydCity currc;
	public YydArea curra;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter paa;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter caa;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter aaa;
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;
	private View btnH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_basic_info_new);

		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		compress = new ImageCompress();
		options = new ImageCompress.CompressOptions();
		setPopupWindow();

		mobile4 = (TextView) this.findViewById(R.id.mobile4);
		messageCode = (EditText) this.findViewById(R.id.messageCode);
		messageCode_ll = (LinearLayout) this.findViewById(R.id.messageCode_ll);
		syncMobil_btn = (Button) this.findViewById(R.id.syncMobil_btn);
		syncMobil_btn.setOnClickListener(this);
		
		pingan_accountNo = (TextView) this.findViewById(R.id.pingan_accountNo);
//		notc = (RelativeLayout) this.findViewById(R.id.notc);
		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_back = (Button) this.findViewById(R.id.basic_back);
		head_img = (ImageView) this.findViewById(R.id.head_img);
		name_img = (ImageView) this.findViewById(R.id.name_img);
		seal_img = (ImageView) this.findViewById(R.id.seal_img);
		head_img.setOnClickListener(this);
		name_img.setOnClickListener(this);
		seal_img.setOnClickListener(this);
		basic_submit.setOnClickListener(this);
		basic_back.setOnClickListener(this);

		login_name = (EditText) this.findViewById(R.id.login_name);
		true_name = (EditText) this.findViewById(R.id.true_name);
		nick_name = (EditText) this.findViewById(R.id.nick_name);
		basic_email = (EditText) this.findViewById(R.id.basic_email);
		basic_phone = (EditText) this.findViewById(R.id.basic_phone);
		basic_unitCode = (EditText) this.findViewById(R.id.basic_unitCode);
		address = (EditText) this.findViewById(R.id.address);

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());

		cb_cd = (CheckBox) this.findViewById(R.id.cb_cd);
		cb_cdon = (CheckBox) this.findViewById(R.id.cb_cdon);
		p = (Spinner) findViewById(R.id.p);
		c = (Spinner) findViewById(R.id.c);
		a = (Spinner) findViewById(R.id.a);
		pl = new PSpinnerOnSelectedListener();
		cl = new CSpinnerOnSelectedListener();
		al = new ASpinnerOnSelectedListener();
		p.setOnItemSelectedListener(pl);
		c.setOnItemSelectedListener(cl);
		a.setOnItemSelectedListener(al);
		
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		prb = (RadioButton) findViewById(R.id.person);
		erb = (RadioButton) findViewById(R.id.enterprise);
		mRadioGroup.setOnCheckedChangeListener(mChangeRadio);

		setView();
		getImg();
		initpca();
		cb_cd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				cd = arg1;
			}
		});
		cb_cdon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				cdon = arg1;
			}
		});

		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null ) {
			synloadPCA();
		}
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

	private void setPopupWindow() {
		popupWindow = new BasePopupWindow(this);
		btnA = popupWindow.getBtnA();
		btnA.setVisibility(View.GONE);
		// 账户信息
		btnB = popupWindow.getBtnB();
		btnB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load = true;
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BasicInfoActivity.this,
						AccountListActivity.class));
			}
		});
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load = true;
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BasicInfoActivity.this,
						ApplyAgentActivity.class));
			}
		});
		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load = true;
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BasicInfoActivity.this,
						AgentActivity.class));
			}
		});
		// 修改密码
		btnF = popupWindow.getBtnF();
		btnF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load = true;
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BasicInfoActivity.this,
						UpdatePwd.class));
			}
		});
		// 管理子账号
		btnG = popupWindow.getBtnG();
		btnG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load = true;
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BasicInfoActivity.this,
						UserChildListActivity.class));
			}
		});
		btnH = popupWindow.getBtnH();
		btnH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				load = true;
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(BasicInfoActivity.this,BindBankCardActivity.class));
			}
		});
	}

	private void setView() {
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			
			String syncMobile = UserSetManager.getInstance().getSyncMobile();
			if("yes".equals(syncMobile)){
				syncMobil_btn.setVisibility(View.VISIBLE);
			}else{
				syncMobil_btn.setVisibility(View.GONE);
			}
			
			pingan_accountNo.setText(UserSetManager.getInstance().getPinganAccountNo());
			utc = user.getUserType();
			if(utc == UserTypeCode.person){
				prb.setChecked(true);
				erb.setChecked(false);
			}else{
				prb.setChecked(false);
				erb.setChecked(true);
			}
			
			headUrl = user.getUserLogo();
			nameUrl = user.getSignature();
			sealUrl = user.getStamp();
			login_name.setText(user.getLoginName());
			true_name.setText(user.getTrueName());
			basic_phone.setText(user.getMobile());
			nick_name.setText(user.getNickName());
			basic_email.setText(user.getEmail());
			basic_unitCode.setText(user.getUnitName());
			address.setText(user.getAddress());
			areaNo = user.getAreaCode();

			List<UserRoleCode> rcs = user.getRoleCodes();
			if (rcs.contains(UserRoleCode.broker)) {
				cb_cdon.setChecked(true);
				cdon = true;
			}
			if (rcs.contains(UserRoleCode.owner)) {
				cb_cd.setChecked(true);
				cd = true;
			}
			if (user.isChildUser()) {
				btnB.setVisibility(View.GONE);
				btnC.setVisibility(View.GONE);
				btnD.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnG.setVisibility(View.GONE);
				btnH.setVisibility(View.VISIBLE);
//				notc.setVisibility(View.GONE);
			}else {
				btnB.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnH.setVisibility(View.VISIBLE);
//				notc.setVisibility(View.VISIBLE);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initpca() {
		pn.add("选择省份");
		paa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, pn);
		p.setAdapter(paa);
		p.setPrompt("省");

		cn.add("选择城市");
		caa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, cn);
		c.setAdapter(caa);
		c.setPrompt("市");

		an.add("选择区域");
		aaa = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.contentTextView, an);
		a.setAdapter(aaa);
		a.setPrompt("区");
	}

	private void refreshCity() {
		caa.notifyDataSetChanged();
	}

	private void refreshArea() {
		aaa.notifyDataSetChanged();
	}

	class PSpinnerOnSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings({ "unchecked" })
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {

			if (pfirst) {

				final String proCode = plist.get(position).getProvinceNo();
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
							clist.clear();
							cn.clear();
							alist.clear();
							an.clear();
							Map<String, Object> allMap = (Map<String, Object>) map
									.get("content");
							List<Map<String, Object>> citysMap = (List<Map<String, Object>>) allMap
									.get("citys");
							List<Map<String, Object>> areasMap = (List<Map<String, Object>>) allMap
									.get("areas");
							for (Map<String, Object> m : citysMap) {
								YydCity c = new YydCity(m);
								clist.add(c);
								cn.add(c.getCityName());
							}
							for (Map<String, Object> m : areasMap) {
								YydArea c = new YydArea(m);
								alist.add(c);
								an.add(c.getAreaName());
							}
							if (clist.size() == 0)
								areaNo = proCode;
							else if (clist.size() > 0 && alist.size() == 0)
								areaNo = clist.get(0).getCityNo();
							else
								areaNo = alist.get(0).getAreaNo();
							mHandler.sendEmptyMessage(RC);
							mHandler.sendEmptyMessage(RA);
						} else {
							Toast.makeText(BasicInfoActivity.this,
									(CharSequence) map.get("message"),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if (content != null
								&& content.equals("can't resolve host"))
							Toast.makeText(BasicInfoActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}, "/mobile/account/getCitys?proCode=" + proCode);
			}
			pfirst = true;
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}

	private boolean cfirst = false;

	class CSpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			if (cfirst) {
				final String cityCode = clist.get(position).getCityNo();
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
							List<Map<String, Object>> allMap = (List<Map<String, Object>>) map
									.get("content");
							for (Map<String, Object> m : allMap) {
								YydArea a = new YydArea(m);
								alist.add(a);
								an.add(a.getAreaName());
							}
							if (alist.size() == 0)
								areaNo = cityCode;
							else
								areaNo = alist.get(0).getAreaNo();
							mHandler.sendEmptyMessage(RA);
						} else {
							Toast.makeText(BasicInfoActivity.this,
									(CharSequence) map.get("message"),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if (content != null
								&& content.equals("can't resolve host"))
							Toast.makeText(BasicInfoActivity.this, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}, "/mobile/account/getAreas?cityCode=" + cityCode);

			}
			cfirst = true;
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}

	private boolean afirst = false;

	class ASpinnerOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			if (afirst) {
				areaNo = alist.get(position).getAreaNo();
			}
			afirst = true;
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}

	private void refreshpca() {
		paa.notifyDataSetChanged();
		caa.notifyDataSetChanged();
		aaa.notifyDataSetChanged();
	}

	private void synloadPCA() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				dialogpca = dialogUtil.loading("正在加载信息!", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				dialogpca.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)) {
						boolean contentMD5Changed = (Boolean) map
								.get(ApplicationConstants.CONTENTMD5CHANGED);
						SharedPreferencesUtils s = new SharedPreferencesUtils(
								"/mobile/account/myAccount/getpca", null);
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
					Map<String, Object> contentMap = (Map<String, Object>) map
							.get("content");
					alist.clear();
					clist.clear();
					plist.clear();
					an.clear();
					cn.clear();
					pn.clear();

					if (contentMap.get("currenta") != null) {
						List<Map<String, Object>> lms = (List<Map<String, Object>>) contentMap
								.get("currenta");
						for (Map<String, Object> m : lms) {
							YydArea a = new YydArea(m);
							alist.add(a);
							an.add(a.getAreaName());
						}
					}
					if (contentMap.get("currentc") != null) {
						List<Map<String, Object>> lms = (List<Map<String, Object>>) contentMap
								.get("currentc");
						for (Map<String, Object> m : lms) {
							YydCity c = new YydCity(m);
							clist.add(c);
							cn.add(c.getCityName());
						}
					}
					List<Map<String, Object>> allProvinceMap = (List<Map<String, Object>>) contentMap
							.get("allProvince");
					for (Map<String, Object> m : allProvinceMap) {
						YydProvince p = new YydProvince(m);
						plist.add(p);
						pn.add(p.getProvinceName());
					}
					mHandler.sendEmptyMessage(RPCA);
				} else {
					Toast.makeText(BasicInfoActivity.this,
							(CharSequence) map.get("message"),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(BasicInfoActivity.this, "网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialogpca.dismiss();
			}
		};
		// 传参请求服务
		data.getApiResult(handler, "/mobile/account/myAccount/getpca");

	}

	private void getImg() {
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			headUrl = user.getUserLogo();
			nameUrl = user.getSignature();
			sealUrl = user.getStamp();
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + headUrl,
					head_img, MyshipAdapter.displayImageOptions);
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + nameUrl,
					name_img, MyshipAdapter.displayImageOptions);
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + sealUrl,
					seal_img, MyshipAdapter.displayImageOptions);
		}
	}

	private void todosubmit() {
		UserData userData = GlobalApplication.getInstance().getUserData();
		String unitCode = "";
		String addr = "";
		String username = login_name.getText().toString().trim();
		String email = basic_email.getText().toString().trim();
		String phone = basic_phone.getText().toString().trim();
		String messagecode = messageCode.getText().toString().trim();
		if (userData != null && !userData.isChildUser()) {
			unitCode = basic_unitCode.getText().toString().trim();
			addr = address.getText().toString().trim();
		}
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "登入名不能为空！", Toast.LENGTH_SHORT).show();
			login_name.requestFocus();
			return;
		} else if (TextUtils.isEmpty(email)) {
			Toast.makeText(this, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
			basic_email.requestFocus();
			return;

		} 
//		else if (TextUtils.isEmpty(phone)) {
//			Toast.makeText(this, "手机不能为空！", Toast.LENGTH_SHORT).show();
//			basic_phone.requestFocus();
//			return;
//		} else if (phone != null && phone.length() != 11) {
//			Toast.makeText(this, "手机号长度为11位！", Toast.LENGTH_SHORT).show();
//			basic_phone.requestFocus();
//			return;
//		}
//		else if (userData != null && !userData.isChildUser()
//				&& TextUtils.isEmpty(unitCode)) {
//			Toast.makeText(this, "请输入公司名称！", Toast.LENGTH_SHORT).show();
//			basic_unitCode.requestFocus();
//			return;
//		} else if (userData != null && !userData.isChildUser()
//				&& TextUtils.isEmpty(addr)) {
//			Toast.makeText(this, "请输入公司详细地址！", Toast.LENGTH_SHORT).show();
//			address.requestFocus();
//			return;
//		}else if (isSyncMobile && TextUtils.isEmpty(messagecode)) {
//			Toast.makeText(this, "请输入短信验证码！", Toast.LENGTH_SHORT).show();
//			messageCode.requestFocus();
//			return;
//		}
		loadDate();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_img:
			load = false;
			
			type = 1;
			getSystemPic();
			break;
		case R.id.name_img:
			load = false;
			
			startActivityForResult(new Intent(this, AlertDialog.class)
					.putExtra("titleIsCancel", true)
					.putExtra("msg", "是否重新设置签名").putExtra("cancel", true),
					REQUEST_CODE_SIGNATURE);
			break;
		case R.id.seal_img:
			load = false;
			
			type = 3;
			getSystemPic();
			break;
		case R.id.basic_submit:
			load = true;
			
			try {
				todosubmit();

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.basic_back:
			load = true;
			
			Intent intent = new Intent(this, MenuActivity.class);

			startActivity(intent);
			break;
		case R.id.syncMobil_btn:
			load = true;
			syncMobile();
			break;
		default:
			break;
		}
	}

	private void syncMobile() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("同步中", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					Map<String, Object> contentMap=(Map<String, Object>) map.get("content");
					serialNo = (String) contentMap.get("serialNo");
					String revMobilePhone = (String) contentMap.get("revMobilePhone");
					mobilestr = (String) contentMap.get("mobile");
					isSyncMobile  = true;
					 Message msg2 = Message.obtain();  
			            msg2.obj = revMobilePhone; 
			            msg2.what = SYNCMOBILE;  
			            mHandler.sendMessage(msg2);  
				} else {
					Toast.makeText(BasicInfoActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(BasicInfoActivity.this, "失败", 1).show();
				dialog.dismiss();

			}

		};
		Map<String, Object> params = new HashMap<String, Object>();
		data.getApiResult(handler, "/mobile/account/myAccount/preSyncMobile", params, "get");
	}

	private void getSystemPic() {
		/***
		 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
		 */
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_CODE_GETSYSIMG);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			// 重写签名
			if (requestCode == REQUEST_CODE_SIGNATURE) {
				boolean btnSure = data.getBooleanExtra("btnSure", true);
				if (btnSure) {
					Intent intent = new Intent(this, SignatureActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("orderId", "");// 合同ID
					intent.putExtras(bundle);
					startActivityForResult(intent,
							REQUEST_CODE_SIGNATURE_SUCCEED);
				} else {
					type = 2;
					getSystemPic();
				}

			}
			// 重写签名成功返回路径
			if (requestCode == REQUEST_CODE_SIGNATURE_SUCCEED) {
				String signaturePath = data.getStringExtra("path");
				name_imgPath = signaturePath;
				Bitmap bitmap = BitmapFactory.decodeFile(signaturePath);
				name_img.setImageBitmap(bitmap);
			}
			// 裁剪后图片
			if (requestCode == REQUEST_CODE_CROPIMG_HEAD) {
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					head_imgPath = cropedImgPath;
					head_img.setImageBitmap(BitmapFactory
							.decodeFile(cropedImgPath));
				} else {
					Toast.makeText(BasicInfoActivity.this, "裁剪图片失败", 1).show();
				}
			}
			if (requestCode == REQUEST_CODE_CROPIMG_SEAL) {
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					seal_imgPath = cropedImgPath;
					seal_img.setImageBitmap(BitmapFactory
							.decodeFile(cropedImgPath));
				} else {
					Toast.makeText(BasicInfoActivity.this, "裁剪图片失败", 1).show();
				}
			}
			// 取图库图片
			if (requestCode == REQUEST_CODE_GETSYSIMG) {
				/**
				 * 当选择的图片不为空的话，再获取到图片的途径
				 */
				Uri uri = data.getData();
				try {
					// 处理图片分辨率太大imageview不能显视出来
					options.uri = uri;
					options.maxWidth = getWindowManager().getDefaultDisplay()
							.getWidth();
					options.maxHeight = getWindowManager().getDefaultDisplay()
							.getHeight();
					Bitmap bitmap = compress.compressFromUri(this, options);

					String[] pojo = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(uri, pojo, null, null, null);
					if (cursor != null) {
						ContentResolver cr = this.getContentResolver();
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String picPath = cursor.getString(colunm_index);
						/***
						 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，
						 * 你选择的文件就不一定是图片了， 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
						 */
						if (picPath.endsWith("jpg") || picPath.endsWith("png")
								|| picPath.endsWith("jpeg")) {
							if (type == 1) {
								// 进入头像裁剪页面
								Intent intent = new Intent(
										BasicInfoActivity.this,
										CropImageActivity.class);
								intent.putExtra("picPath", uri);
								startActivityForResult(intent,
										REQUEST_CODE_CROPIMG_HEAD);

								// Bitmap bitmap = BitmapFactory.decodeStream(cr
								// .openInputStream(uri));
								// head_imgPath = picPath;
								// head_img.setImageBitmap(bitmap);
							} else if (type == 2) {
								// Bitmap bitmap = BitmapFactory.decodeStream(cr
								// .openInputStream(uri));
								name_imgPath = picPath;
								name_img.setImageBitmap(bitmap);
							} else {
								Intent intent = new Intent(
										BasicInfoActivity.this,
										CropImageActivity.class);
								intent.putExtra("picPath", uri);
								startActivityForResult(intent,
										REQUEST_CODE_CROPIMG_SEAL);
								// Bitmap bitmap = BitmapFactory.decodeStream(cr
								// .openInputStream(uri));
								// seal_imgPath = picPath;
								// seal_img.setImageBitmap(bitmap);
							}

						} else {
							alert();
						}
					} else {
						alert();
					}

				} catch (Exception e) {
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void alert() {
		Dialog dialog = new android.app.AlertDialog.Builder(this)
				.setTitle("提示").setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("基本信息");
		setRight(R.drawable.commen_top_right, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.togglePopWindow(v);
			}
		});
		if(load)
			UserSetManager.getInstance().synLoadDate(this);

	}

	// @Override
	// protected void onDestroy() {
	// finish();
	// }
	public void refresh() {
		mHandler.sendEmptyMessage(RVIEW);

	}

	@Override
	protected BaseAdapter setAdapter() {
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

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					SharedPreferencesUtils currUser = new SharedPreferencesUtils(
							"currUser", null);
					currUser.setParam(content);
					UserData userData = new UserData(
							(Map<String, Object>) map.get("content"));
					// 写文件
					Context ctx = BasicInfoActivity.this;
					SharedPreferences sp = ctx.getSharedPreferences(
							"eyundaBindingCode", MODE_PRIVATE);
					// 存入数据
					Editor editor = sp.edit();
					editor.putString("loginName", userData.getLoginName());
					editor.putString("nickName", userData.getNickName());
					editor.putString("userLogo", userData.getUserLogo());
					editor.putString("roleDesc", userData.getShortRoleDesc());
					editor.commit();

					GlobalApplication.getInstance().getUserData()
							.setUserType(userData.getUserType());
					GlobalApplication.getInstance().getUserData()
							.setLoginName(userData.getLoginName());
					GlobalApplication.getInstance().getUserData()
							.setTrueName(userData.getTrueName());
					GlobalApplication.getInstance().getUserData()
							.setNickName(userData.getNickName());
					GlobalApplication.getInstance().getUserData()
							.setEmail(userData.getEmail());
					GlobalApplication.getInstance().getUserData()
							.setMobile(userData.getMobile());
					GlobalApplication.getInstance().getUserData()
							.setUserLogo(userData.getUserLogo());
					GlobalApplication.getInstance().getUserData()
							.setSignature(userData.getSignature());
					GlobalApplication.getInstance().getUserData()
							.setStamp(userData.getStamp());
					GlobalApplication.getInstance().getUserData()
							.setUnitName(userData.getUnitName());
					GlobalApplication.getInstance().getUserData()
							.setAddress(userData.getAddress());
					GlobalApplication.getInstance().getUserData()
							.setAreaCode(userData.getAreaCode());
					GlobalApplication.getInstance().getUserData()
							.setRoles(userData.getRoles());
					GlobalApplication.getInstance().getUserData()
							.setRoleCodes(userData.getRoleCodes());
					GlobalApplication.getInstance().getUserData()
							.setRoleDesc(userData.getRoleDesc());
					GlobalApplication.getInstance().getUserData()
							.setShortRoleDesc(userData.getShortRoleDesc());

//					refresh();
					getImg();
					Toast.makeText(BasicInfoActivity.this, map.get("message").toString(), 1)
							.show();
				} else {
					Toast.makeText(BasicInfoActivity.this,
							map.get("message").toString(), 1).show();
				}
				UserSetManager.getInstance().synLoadDate(BasicInfoActivity.this);

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(BasicInfoActivity.this, "基本信息提交失败", 1).show();
				dialog.dismiss();

			}

		};
		// 手动登入
		Map<String, Object> params = new HashMap<String, Object>();
		UserData userData = GlobalApplication.getInstance().getUserData();
		if (userData != null)
			params.put("id", userData.getId());
		params.put("userType", utc);
		params.put("loginName", login_name.getText());
		params.put("trueName", true_name.getText());
		params.put("nickName", nick_name.getText());
		params.put("email", basic_email.getText());
		params.put("mobile", basic_phone.getText());
		if (head_imgPath != null)
			params.put("userLogoFile", new File(head_imgPath));
		if (name_imgPath != null)
			params.put("signatureFile", new File(name_imgPath));
		if (seal_imgPath != null)
			params.put("stampFile", new File(seal_imgPath));
		
//		if (userData != null && !userData.isChildUser()) {
			params.put("unitName", basic_unitCode.getText());
			params.put("address", address.getText());
			params.put("areaCode", areaNo);
//			String roles = "";
//			if (cdon)
//				roles += "2";
//			if (cd)
//				roles += "1";
//			if ("".equals(roles)) {
//				Toast.makeText(BasicInfoActivity.this, "请选择角色",
//						Toast.LENGTH_SHORT).show();
//				return;
//			} else if (roles.length() == 2) {
//				roles = "1,2";
//			}

//			params.put("roles", roles);
//		}
		if (isSyncMobile) {
			params.put("syncMobile", "yes");
			params.put("serialNo", serialNo);
			params.put("syncMobileNo", messageCode.getText().toString().trim());
			
			isSyncMobile = false;
			messageCode_ll.setVisibility(View.GONE);
		}
		
		data.getApiResult(handler, "/mobile/account/myAccount/saveBaseInfo",
				params);

	}

}
