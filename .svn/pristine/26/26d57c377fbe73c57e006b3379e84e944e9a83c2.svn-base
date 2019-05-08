package com.eyunda.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import kankan.wheel.widget.OnWheelChangedListener;
//import kankan.wheel.widget.OnWheelScrollListener;
//import kankan.wheel.widget.WheelView;
//import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
//import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.order.PortData;
import com.hy.client.R;

public class AreaSelect {
	private TextView currentCity;// 当前选择的地区按钮

	private String curCityCode;//当前选择的地区code
	
	private int width;// 窗口宽度
	private int height;// 窗口高度
	private Context cx;
	// Scrolling flag
	private boolean scrolling = false;
	private TextView tv;
	private Button button_ok;

	private Button btnAddArea;// 新增地区

	public static String current;
	public static String current_Start;
	public static String current_end;
	private Map<PortCityCode,List<PortData>> areas;
	private ArrayList<ArrayList<String>> aCities = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> aCid = new ArrayList<ArrayList<String>>();

	private String mCities[][];
	private String mCid[][];
	private String mPid[];
	private String mPrivences[];
	private String[] mPrivenceDes;
	
	public AreaSelect(Context cx, TextView currentCity) {
		this.cx = cx;
		// 获取屏幕的高度和宽度
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager winManager = (WindowManager) cx
				.getSystemService(Context.WINDOW_SERVICE);
		winManager.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		this.currentCity = currentCity;
		areas = LocalFileUtil.getAreaFile(cx);
		//初始化city数组
		//int i=0;
		mPid = new String[areas.size()];
		mPrivences = new String[areas.size()];
		mPrivenceDes= new String[areas.size()];
		for(int i=0;i<PortCityCode.values().length; i++){
			mPid[i] = PortCityCode.values()[i].getCode();
			//mPrivences[i] = key.getDescription();
			mPrivences[i] =  PortCityCode.values()[i].getFullName();
			mPrivenceDes[i] =  PortCityCode.values()[i].getDescription();
			List<PortData> value = areas.get(PortCityCode.values()[i]);
			ArrayList<String> tempCid = new ArrayList<String>();
			ArrayList<String> tempCities = new ArrayList<String>();
			for(int j=0; j<value.size(); j++){
				tempCid.add(value.get(j).getPortNo().toString());
				tempCities.add( value.get(j).getPortName());
			}
			aCities.add(tempCities);
			aCid.add(tempCid);	
		}
		
//		for (PortCityCode key : areas.keySet()) {
//			mPid[i] = key.getCode(); 
//			//mPrivences[i] = key.getDescription();
//			mPrivences[i] = key.getFullName();
//			List<PortData> value = areas.get(key);
//			ArrayList<String> tempCid = new ArrayList<String>();
//			ArrayList<String> tempCities = new ArrayList<String>();
//			for(int j=0; j<value.size(); j++){
//				tempCid.add(value.get(j).getPortNo().toString());
//				tempCities.add( value.get(j).getPortName());
//			}
//			aCities.add(tempCities);
//			aCid.add(tempCid);
//			i++;
//		}
		//转换成二维数组,TODO:效率太低，待修改
		mCid = new String[aCid.size()][];
		mCities = new String[aCid.size()][];
		for(int ti=0; ti< aCid.size();ti++){
			ArrayList<String> tCid = aCid.get(ti);
			ArrayList<String> tCity = aCities.get(ti);
			mCid[ti] = new String[tCid.size()];
			mCities[ti] = new String[tCid.size()];
			for(int tj=0; tj < tCid.size(); tj++){
				mCid[ti][tj] = tCid.get(tj);
				mCities[ti][tj] = tCity.get(tj);
			}
		}
		clean();
		
	}

	private void clean(){
		areas = null;
		aCities = null;
		aCid = null;
	}
	// 创建一个包含自定义view的PopupWindow
//	public PopupWindow makePopupWindow() {
//		final PopupWindow window;
//		window = new PopupWindow(cx);
//		LayoutInflater inflater = (LayoutInflater) cx
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View contentView = inflater.inflate(R.layout.cities_layout, null);
//		window.setContentView(contentView);
//
//		tv = (TextView) contentView.findViewById(R.id.tv_cityName);
//
//		final WheelView country = (WheelView) contentView.findViewById(R.id.country);
//		country.setVisibleItems(0);
//		
//		CountryAdapter countryAdapter = new CountryAdapter(cx,this.mPrivences);
//		country.setViewAdapter(countryAdapter);
//		
//		//final String cities[][] = AddressData.CITIES;
//		final String cities[][] = this.mCities;
//		
//		final WheelView city = (WheelView) contentView.findViewById(R.id.city);
//		city.setVisibleItems(0);
//
//		country.addChangingListener(new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				if (!scrolling) {
//					updateCities(city, cities, newValue);
//				}
//			}
//		});
//
//		country.addScrollingListener(new OnWheelScrollListener() {
//			public void onScrollingStarted(WheelView wheel) {
//				scrolling = true;
//			}
//
//			public void onScrollingFinished(WheelView wheel) {
//				scrolling = false;
//				updateCities(city, cities, country.getCurrentItem());
//			}
//		});
//
//		city.addChangingListener(new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				if (!scrolling) {
//				}
//			}
//		});
//
//		city.addScrollingListener(new OnWheelScrollListener() {
//			public void onScrollingStarted(WheelView wheel) {
//				scrolling = true;
//			}
//
//			public void onScrollingFinished(WheelView wheel) {
//				scrolling = false;
//			}
//		});
//
//		country.setCurrentItem(1);
//
//		// 点击事件处理
//		button_ok = (Button) contentView.findViewById(R.id.button_ok);
//		button_ok.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			
//			//current =AddressData.C_ID[country.getCurrentItem()][city.getCurrentItem()]+"";
//				//current = mCid[country.getCurrentItem()][city.getCurrentItem()]+"_"+mPid[country.getCurrentItem()];
//
//		       current = mCid[country.getCurrentItem()][city.getCurrentItem()]+"";
//			//currentCity.setText(AddressData.PROVINCES[country.getCurrentItem()]+ "-"+ AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
//				currentCity.setText(mPrivenceDes[country.getCurrentItem()]+ "."+ mCities[country.getCurrentItem()][city.getCurrentItem()]);
//				currentCity.setTag(current);
//				
//				window.dismiss(); // 隐藏
//			}
//		});
//		// 点击事件处理
//		btnAddArea = (Button) contentView.findViewById(R.id.btnAddArea);
//		btnAddArea.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			    current = "";
//				currentCity.setText("");
//				currentCity.setTag(current);
//				window.dismiss(); // 隐藏
//			}
//		});
//
//		window.setWidth(width);
//		window.setHeight(height / 2);
//
//		// 设置PopupWindow外部区域是否可触摸
//		window.setFocusable(true); // 设置PopupWindow可获得焦点
//		window.setTouchable(true); // 设置PopupWindow可触摸
//		window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
//		return window;
//	}

	/**
	 * Updates the city wheel
	 */
//	private void updateCities(WheelView city, String cities[][], int index) {
//		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(cx,
//				cities[index]);
//		adapter.setTextSize(18);
//		city.setViewAdapter(adapter);
//		city.setCurrentItem(cities[index].length / 2);
//	}

	// /**
	// * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	// */
	// private void initJsonData(Context cx, String jsonFile)
	// {
	// try
	// {
	// StringBuffer sb = new StringBuffer();
	// //InputStream is = cx.getAssets().open("city.json");
	// InputStream is = cx.getAssets().open(jsonFile);
	// int len = -1;
	// byte[] buf = new byte[1024];
	// while ((len = is.read(buf)) != -1)
	// {
	// sb.append(new String(buf, 0, len, "gbk"));
	// }
	// is.close();
	// mJsonObj = new JSONObject(sb.toString());
	// } catch (IOException e)
	// {
	// e.printStackTrace();
	// } catch (JSONException e)
	// {
	// e.printStackTrace();
	// }
	// }
	/**
	 * Adapter for countries
	 */
//	private class CountryAdapter extends AbstractWheelTextAdapter {
//		// Countries names
//		private String countries[];
//
//		/**
//		 *  Constructor
//		 */
//		protected CountryAdapter(Context context,String[] st) {
//			super(context, R.layout.country_layout, NO_RESOURCE);
//			countries = st;
//			setItemTextResource(R.id.country_name);
//		}
//
//		@Override
//		public View getItem(int index, View cachedView, ViewGroup parent) {
//			View view = super.getItem(index, cachedView, parent);
//			return view;
//		}
//
//		@Override
//		public int getItemsCount() {
//			return countries.length;
//		}
//
//		@Override
//		protected CharSequence getItemText(int index) {
//			return countries[index];
//		}
//	}

	public int getHeight() {
		return this.height;
	}
	public String getCurCityCode(){
		return curCityCode;
	}
}
