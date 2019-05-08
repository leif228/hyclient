package com.eyunda.tools;

import java.lang.reflect.Field;

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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hy.client.R;

public class BigAreaSelect {
	private TextView currentCity;// 当前选择的地区按钮

	private int width;// 窗口宽度
	private int height;// 窗口高度
	private Context cx;
	// Scrolling flag
	private boolean scrolling = false;
	private TextView tv;
	private Button btn_ok;

	//private Button btnAddArea;// 新增地区

	public static String current;
	public static String current_Start;
	public static String current_end;

	public BigAreaSelect(Context cx, TextView currentCity) {
		this.cx = cx;
		// 获取屏幕的高度和宽度
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager winManager = (WindowManager) cx
				.getSystemService(Context.WINDOW_SERVICE);
		winManager.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		this.currentCity = currentCity;
	}

	// 创建一个包含自定义view的PopupWindow
//	public PopupWindow makePopupWindow() {
//		final PopupWindow window;
//		window = new PopupWindow(cx);
//		LayoutInflater inflater = (LayoutInflater) cx
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View contentView = inflater.inflate(R.layout.area_layout, null);
//		window.setContentView(contentView);
//
//		tv = (TextView) contentView.findViewById(R.id.tv_cityName);
//
//		final WheelView country = (WheelView) contentView
//				.findViewById(R.id.province);
//		country.setVisibleItems(0);
//		country.setViewAdapter(new CountryAdapter(cx));
//
//		final String cities[][] = BigAreaData.CITIES;
//		final WheelView city = (WheelView) contentView.findViewById(R.id.cities);
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
//		btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
//		btn_ok.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			
//			current =BigAreaData.C_ID[country.getCurrentItem()][city.getCurrentItem()]+"";
//			
////				currentCity.setText(BigAreaData.PROVINCES[country.getCurrentItem()]+ "-"
////					 + BigAreaData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
//			currentCity.setText(BigAreaData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
//				
//				window.dismiss(); // 隐藏
//			}
//		});
//		// 点击事件处理
////		btnAddArea = (Button) contentView.findViewById(R.id.btnAddArea);
////		btnAddArea.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				window.dismiss(); // 隐藏
////				// TODO 创建一个输入弹框
////
////				AlertDialog.Builder builder = new AlertDialog.Builder(cx);
////				LayoutInflater factory = LayoutInflater.from(cx);
////				final View textEntryView = factory.inflate(
////						R.layout.eyd_alert_input_dialog, null);
////				TextView dialogNotice = (TextView) textEntryView
////						.findViewById(R.id.dialogNotice);
////				dialogNotice.setVisibility(View.INVISIBLE);
////				EditText areaCountry = (EditText) textEntryView
////						.findViewById(R.id.areaCountry);
////				areaCountry.setText(BigAreaData.PROVINCES[country
////						.getCurrentItem()]);
////				areaCountry.setEnabled(false);
////				builder.setIcon(R.drawable.eyd_caht_msg_state_fail_resend);
////				builder.setTitle("新增港口");
////				builder.setView(textEntryView);
////				builder.setPositiveButton("确定",
////						new DialogInterface.OnClickListener() {
////							public void onClick(DialogInterface dialog,
////									int whichButton) {
////								EditText areaCity = (EditText) textEntryView
////										.findViewById(R.id.areaCity);
////								String ac = areaCity.getText().toString();
////								if (!ac.equals("")) {
////									try {
////										Field field = dialog.getClass()
////												.getSuperclass()
////												.getDeclaredField("mShowing");
////										field.setAccessible(true);
////										// 设置mShowing值，欺骗android系统
////										field.set(dialog, true);
////										new AlertDialog.Builder(cx)
////												.setMessage("确认要新增一个港口么？")
////												.setPositiveButton(
////														"确定",
////														new DialogInterface.OnClickListener() {
////															public void onClick(
////																	DialogInterface arg0,
////																	int arg1) {
////																EditText areaCountry = (EditText) textEntryView
////																		.findViewById(R.id.areaCountry);
////																EditText areaCity = (EditText) textEntryView
////																		.findViewById(R.id.areaCity);
////																currentCity
////																		.setText(areaCountry
////																				.getText()
////																				.toString()
////																				+ "-"
////																				+ areaCity
////																						.getText()
////																						.toString());
////
////															}
////														})
////												.setNegativeButton(
////														"取消",
////														new DialogInterface.OnClickListener() {
////															public void onClick(
////																	DialogInterface arg0,
////																	int arg1) {
////
////															}
////														}).setCancelable(false).show();
////									} catch (Exception e) {
////										e.printStackTrace();
////									}
////
////								} else {
////									try {
////										Field field = dialog.getClass()
////												.getSuperclass()
////												.getDeclaredField("mShowing");
////										field.setAccessible(true);
////										// 设置mShowing值，欺骗android系统
////										field.set(dialog, false);
////									} catch (Exception e) {
////										e.printStackTrace();
////									}
////									TextView dialogNotice = (TextView) textEntryView
////											.findViewById(R.id.dialogNotice);
////									dialogNotice.setVisibility(View.VISIBLE);
////								}
////							}
////						});
////				builder.setNegativeButton("取消",
////						new DialogInterface.OnClickListener() {
////							public void onClick(DialogInterface dialog,
////									int whichButton) {
////								try {
////									Field field = dialog.getClass()
////											.getSuperclass()
////											.getDeclaredField("mShowing");
////									field.setAccessible(true);
////									// 设置mShowing值，欺骗android系统
////									field.set(dialog, true);
////								} catch (Exception e) {
////									e.printStackTrace();
////								}
////							}
////						});
////				builder.setCancelable(false);
////				builder.create().show();
////
////			}
////
////		});
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
//		private String countries[] = BigAreaData.PROVINCES;
//
//		/**
//		 *  Constructor
//		 */
//		protected CountryAdapter(Context context) {
//			super(context, R.layout.country_layout, NO_RESOURCE);
//
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

}
