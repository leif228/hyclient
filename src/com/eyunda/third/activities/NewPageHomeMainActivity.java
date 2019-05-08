package com.eyunda.third.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.CloseActivityClass;
import com.hangyi.tools.ParseJson;
import com.hangyi.zd.ClearService;
import com.hangyi.zd.PHClearService;
import com.hangyi.zd.activity.HXDownPopupWindow.GroupResultListener;
import com.hangyi.zd.activity.HXUpPopupWindow;
import com.hangyi.zd.activity.LoginActivity;
import com.hangyi.zd.activity.NewContentFragment;
import com.hangyi.zd.activity.NewContentFragment.MyListener;
import com.hangyi.zd.activity.ShipSeachActivity;
import com.hangyi.zd.domain.GroupData;
import com.hangyi.zd.domain.MapPortData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hangyi.zd.widge.SearchView;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ygl.android.ui.BaseActivity;

public class NewPageHomeMainActivity extends BaseActivity implements SearchView.SearchViewListener,MyListener,GroupResultListener{
//	protected MenuHorizontalScrollView scrollView;
//	private LinearLayout menuList;
	protected View homeView;
//	protected View menuView;
	private Button menuBtn, top_right_but;
//	private View[] children;
	private LayoutInflater inflater;

	protected Data_loader data;
	protected Image_loader image_loader;

	protected TextView top_text;
	protected TextView login_type, login_name;
	protected ImageView userHead;

	protected ImageView showpoint;
	protected DialogUtil dialogUtil;
	
	private ArrayAdapter<String> autoCompleteAdapter;
	private List<String> autoCompleteData;
	public static int hintSize = 8;
	private List<UserPowerShipData> upsdList;
	private UserPowerShipData selectShip = null;
	private com.hangyi.zd.activity.BasePopupWindow popupWindow;// 弹出菜单
	
//	@Bind(R.id.mainContent) ScrollView mainContent; 
	
	ImageLoader mImageLoader;
	private LinearLayout ll_search;
	private EditText search_et_input;
	private ImageView search_iv_delete;
	boolean onSearchState = true;
	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
//	.showStubImage(R.drawable.home_default)//去掉加载图片闪问题
//	.showImageForEmptyUri(R.drawable.home_default)
	.showImageOnFail(R.drawable.home_default)
//	.cacheInMemory(true).cacheOnDisc(false)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.imageScaleType(ImageScaleType.EXACTLY).build();
	
	HXUpPopupWindow hxPopupWindow;
	
	List<UserPowerShipData> shipSearchCooordDatas = new ArrayList<UserPowerShipData>();
	private List<UserPowerShipData> shipCooordDatas = new ArrayList<UserPowerShipData>();
	public static final String noSearchReasultStr = "未搜到相关船舶";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		
		CloseActivityClass.activityList.add(this);
		
		dialogUtil = new DialogUtil(this);
		inflater = LayoutInflater.from(this);
		data = new Data_loader();
		image_loader = new Image_loader(this, (TAApplication) getApplication());
		
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
		
//		menuView = inflater.inflate(R.layout.menu_scroll_view, null);
//		setContentView(menuView);
//		showpoint = (ImageView) findViewById(R.id.showpoint);
//
//		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
//		this.menuList = (LinearLayout) findViewById(R.id.menuList);

		this.homeView = inflater.inflate(R.layout.new_part_home_main, null);
		setContentView(homeView);
		this.top_text = (TextView) this.homeView.findViewById(R.id.top_text);
		this.search_et_input = (EditText) this.homeView.findViewById(R.id.search_et_input);
		this.search_iv_delete = (ImageView) this.homeView.findViewById(R.id.search_iv_delete);
		this.ll_search = (LinearLayout) this.homeView.findViewById(R.id.ll_search);
		this.ll_search.setOnClickListener(onClickListener);
		
		search_iv_delete.setOnClickListener(onClickListener);
		search_et_input.addTextChangedListener(new EditChangedListener());
		search_et_input.setOnClickListener(onClickListener);
//		search_et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView textView, int actionId,
//					KeyEvent keyEvent) {
//				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//				}
//				return true;
//			}
//		});
		
		this.menuBtn = (Button) this.homeView.findViewById(R.id.top_left_but);
		this.menuBtn.setOnClickListener(onClickListener);
		top_right_but = (Button) this.homeView.findViewById(R.id.top_right_but);
		top_right_but.setOnClickListener(onClickListener);
		
//		View leftView = new View(this);
//		leftView.setBackgroundColor(Color.TRANSPARENT);
//		// 左右布局
//		children = new View[] { leftView, homeView };
//		this.scrollView.initViews(children, new SizeCallBackForMenu(this.menuBtn), this.menuList);
//		this.scrollView.setMenuBtn(this.menuBtn);
		
		initUserPower();
		initMapPort();
//		getTestGps();
		setPopupWindow();
		
		SharedPreferences sp = getSharedPreferences(ApplicationConstants.UserInfoConfig_SharedPreferences, Context.MODE_PRIVATE);
		String un = sp.getString("UserName", "");
		if(un.equals("")){
			startActivity(new Intent(NewPageHomeMainActivity.this,LoginActivity.class));
			finish();
		}
	}
	@Override
    public void hideBasePP(){
    	if(popupWindow!=null&&popupWindow.isOpen())
    		popupWindow.closePopWindow();
    	
    	//隐藏软键盘
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_et_input.getWindowToken(), 0);
    }

	private void setPopupWindow() {
		popupWindow = new com.hangyi.zd.activity.BasePopupWindow(this);
		popupWindow.setListener(this);
		
		hxPopupWindow = new HXUpPopupWindow(this);
		NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
		fragment.setPopupWindow(hxPopupWindow);
	}


	private void initMapPort() {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		data.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				
				List<MapPortData> datas = ParseJson.parserMapPort(arg2);
				if(datas != null){
					SharedPreferences sp = NewPageHomeMainActivity.this.getSharedPreferences(ApplicationConstants.MapPortData_SharedPreferences, Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					String s = new Gson().toJson(datas);
					editor.putString("MapPort", s);
					
					editor.commit();
				}
				
//				Gson gson = new Gson();
//				final UserPowerData rmap = gson.fromJson(
//						(String) s, new TypeToken<List<MapPortData>>() {
//						}.getType());
//				String r = rmap.getUserPowerShipDatas().get(0).getShipModels().get(0).getModel().getN()+"";
//				s =r;
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		}, ApplicationUrls.mapPort+"SELECT * FROM portposition".replace(" ", "%20"), apiParams, "get");
	
	}


	private void getTestGps() {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		data.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				
				 try {  
			            String fileName = "gps128"+ Calendar.getInstance().getTime().toString() + ".log";  
			            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
			                String path = "/mnt/sdcard/zd/log/";  
			                File dir = new File(path);  
			                if (!dir.exists()) {  
			                    dir.mkdirs();  
			                }  
			                FileOutputStream fos = new FileOutputStream(path + fileName);  
			                fos.write(arg2.getBytes());  
			                fos.close();  
			            }  
			        } catch (Exception e) {  
			        } 
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

			}
//		}, "/clientapi/?Function=2&Style=json&ShipName=粤建航128&StartTime=2016-07-27%2010:38:00&EndTime=2016-07-28%2006:55:00", apiParams, "get");
	}, ApplicationUrls.testGps, apiParams, "get");
	
	
	}


	private void initUserPower() {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		data.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				
				UserPowerData userPowerData = ParseJson.parserUserPower(arg2);
				
				if(userPowerData != null){
					SharedPreferences sp = NewPageHomeMainActivity.this.getSharedPreferences(ApplicationConstants.UserPowerData_SharedPreferences, Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					String s = userPowerData.getJsonString();
					editor.putString("UserPower", s);
					
					editor.commit();
				}
				
//				Gson gson = new Gson();
//				final UserPowerData rmap = gson.fromJson(
//						(String) s, new TypeToken<UserPowerData>() {
//						}.getType());
//				String r = rmap.getUserPowerShipDatas().get(0).getShipModels().get(0).getModel().getN()+"";
//				s =r;
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(getActivity(), "网络连接异常",
//							Toast.LENGTH_LONG).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(getActivity(), "连接服务器超时",
//							Toast.LENGTH_LONG).show();
//				} else if (content != null) {
//					Toast.makeText(getActivity(), content,
//							Toast.LENGTH_LONG).show();
//				} else
//					Toast.makeText(getActivity(), "未知异常",
//							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.userPower, apiParams, "get");
	
	}


	@Override
	protected void onStart() {
		super.onStart();	
//		Serializable s = getIntent().getSerializableExtra("groupData");
//		if(getIntent().getSerializableExtra("groupData")!=null){
//			GroupData data = (GroupData) getIntent().getSerializableExtra("groupData");
//			if(data != null)
//				getResult(data);
//		}
		GroupData data = GlobalApplication.getInstance().getGroupData();
		if(data!=null)
			getResult(data);
		GlobalApplication.getInstance().setGroupData(null);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Intent intent = new Intent(this,PHClearService.class); 
		startService(intent);
	}
	

	// 顶部菜单点击
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.top_left_but){// 左边菜单
//				scrollView.clickMenuBtn();
//				top_text.setText("首页");
//				menuBtn.setVisibility(View.INVISIBLE);
				top_text.setVisibility(View.GONE);
				menuBtn.setVisibility(View.GONE);
				ll_search.setVisibility(View.VISIBLE);
				NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
				fragment.hideCurrShip();
			}else if (arg0.getId() == R.id.top_right_but){
//						AlertDialog("查找船舶");
				Intent intent = new Intent(NewPageHomeMainActivity.this,ShipSeachActivity.class);
                startActivityForResult(intent, 0);
			}
//			else if(arg0.getId() == R.id.ll_search){
//				popupWindow.togglePopWindow(arg0);
//				popupWindow.setData(getSearchData());
//			}
			else if(arg0.getId() == R.id.search_iv_delete){
				search_et_input.setText("");
				popupWindow.setData(getSearchData());
			}else if(arg0.getId() == R.id.search_et_input){
				popupWindow.togglePopWindow(arg0);
				popupWindow.setData(getSearchData());
//				search_et_input.requestFocus();
			}
		}
	};
	
	  private void initAutoCompleteData() {
	        if (autoCompleteData == null) {
	            //初始化
	            autoCompleteData = new ArrayList<String>();
	            
	            SharedPreferences sp = getSharedPreferences(ApplicationConstants.UserPowerData_SharedPreferences, Context.MODE_PRIVATE);
				String object = sp.getString("UserPower", "");
				Gson gson = new Gson();
				UserPowerData data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
				
				if(data!=null)
					upsdList = data.getUserPowerShipDatas();
				else
					upsdList = new  ArrayList<UserPowerShipData>();
	        } 
	        if (autoCompleteAdapter == null) {
	            autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autoCompleteData);
	        } 
	    }
	 protected void AlertDialog(String title) {
			LayoutInflater  inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.dialog_custom_search_layout, null);
			
			SearchView searchView = (SearchView) view.findViewById(R.id.main_search_layout);
			//初始化自动补全数据
			initAutoCompleteData();
			
			searchView.setSearchViewListener(this);
			searchView.setDatas(upsdList);
			searchView.setAutoCompleteAdapter(autoCompleteAdapter);
			searchView.setAutoCompleteData(autoCompleteData);
			
			new AlertDialog.Builder(this)
			.setTitle(title)
			.setView(view)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setNegativeButton("取消", null)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(selectShip!=null){
						NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
						fragment.selectShip(selectShip.getShipID(),selectShip.getShipName());
					}
						
				}
			})
			.show();
		}

	@Override
	public boolean backKeyPressed() {
//		if (scrollView.menuOut)
//			scrollView.clickMenuBtn();
//		else
//			finish();
//		// dialogUtil.showQuitDialogFromConfig("提示", "是否退出？");
		
//		if(top_text.getText().toString().equals("首页")){
//			finish();
//		}else{
//			top_text.setText("首页");
//			menuBtn.setVisibility(View.INVISIBLE);
//			NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
//			fragment.hideCurrShip();
//		}
		if(popupWindow.isOpen()){
			popupWindow.closePopWindow();
		}else if(hxPopupWindow.isOpen()){
			hxPopupWindow.closePopWindow();
		}else{
			
			if(onSearchState){
				finish();
			}else{
				onSearchState = true;
				top_text.setVisibility(View.GONE);
				menuBtn.setVisibility(View.GONE);
				ll_search.setVisibility(View.VISIBLE);
				NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
				fragment.hideCurrShip();
			}
		}
		return true;
	}


	@Override
	public void onRefreshAutoComplete(String text) {
		//更新数据
//        getAutoCompleteData(text);
	}


	@Override
	public void onSearch(String text) {
		
//		for(UserPowerShipData u:upsdList){
//			if(u.getShipName().equals(text))
//				selectShip  = u;
//		}
		for(UserPowerShipData sc:shipCooordDatas){
			if(sc.getShipName().equals(text)){
				NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
				fragment.selectShip(sc.getShipID(),sc.getShipName());
				
				popupWindow.closePopWindow();
//				ll_search.setVisibility(View.GONE);
//				onSearchState = false;
				
				//隐藏软键盘
		        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.hideSoftInputFromWindow(search_et_input.getWindowToken(), 0);
		        
		        break;
			}
		}
	}

	@Override
	public void showMessage(String shipName) {
		onSearchState = false;
		ll_search.setVisibility(View.GONE);
		top_text.setVisibility(View.VISIBLE);
		top_text.setText(shipName);
		menuBtn.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void getResult(GroupData groupData) {
		hxPopupWindow.closePopWindow();
		if(groupData.isPort()){
			String x = groupData.getTempx();
			String y = groupData.getTempy();
			NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
			fragment.setPoint(x,y);
			
			top_text.setVisibility(View.GONE);
			menuBtn.setVisibility(View.GONE);
			ll_search.setVisibility(View.VISIBLE);
		}else{
			NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
			fragment.loadChang();
			
			top_text.setVisibility(View.GONE);
			menuBtn.setVisibility(View.GONE);
			ll_search.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { 

		case RESULT_OK:
			if(0==requestCode){
				
				Bundle b = data.getExtras(); 
				String shipId = b.getString("shipId");
				
				NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
				fragment.selectShip(shipId,"");
			}else if(1==requestCode){
				Bundle b = data.getExtras(); 
				boolean isPost = b.getBoolean("isPost", false);
				if(isPost){
					String x = b.getString("x");
					String y = b.getString("y");
					NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
					fragment.setPoint(x,y);
				}else{
					NewContentFragment fragment = (NewContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
					fragment.loadChang();
				}
			}

			break;

		default:

			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
//	public List<ShipCooordData> getSearchData2(){
//		
//		SharedPreferences sp = context.getSharedPreferences(ApplicationConstants.SearchShipData_SharedPreferences,
//				Context.MODE_PRIVATE);
//		String shipCooordDatasJson = sp.getString("SearchShipData", "");
//		
//		shipCooordDatas = null;
//		if (!shipCooordDatasJson.equals("")) {
//			Gson gson = new Gson();
//			shipCooordDatas = gson.fromJson(shipCooordDatasJson,
//					new TypeToken<List<ShipCooordData>>() {
//			}.getType());
//		}
//		return shipCooordDatas;
//	}
	public List<UserPowerShipData> getSearchData(){

		SharedPreferences sp = getSharedPreferences(ApplicationConstants.UserPowerData_SharedPreferences, Context.MODE_PRIVATE);
		String object = sp.getString("UserPower", "");
		
		shipCooordDatas.clear();
		UserPowerData data = null;
		if(!"".equals(object)){
			Gson gson = new Gson();
			data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
			if(data!=null)
				shipCooordDatas=data.getUserPowerShipDatas();
		}
		
		return shipCooordDatas;
	}
	
	private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i1, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
            	search_iv_delete.setVisibility(View.VISIBLE);
                
                shipSearchCooordDatas.clear();
                if (shipCooordDatas != null && shipCooordDatas.size() > 0) {
                	for(int i=0;i<shipCooordDatas.size();i++){
                		if (shipCooordDatas.get(i).getShipName().contains(charSequence.toString().trim())) {
                			shipSearchCooordDatas.add(shipCooordDatas.get(i));
                    	}
                	}
                	
                	if(shipSearchCooordDatas.isEmpty()){
                		UserPowerShipData s = new UserPowerShipData();
                		s.setShipName(noSearchReasultStr);
                		
                		shipSearchCooordDatas.add(s);
                	}
                	
                	popupWindow.setData(shipSearchCooordDatas);
//                	search_et_input.requestFocus();
                }
                
            } else {
            	search_iv_delete.setVisibility(View.GONE);
            	popupWindow.setData(getSearchData());
//            	search_et_input.requestFocus();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }


	
}
