package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.AttrTypeCode;
import com.eyunda.third.domain.ship.AttrNameData;
import com.eyunda.third.domain.ship.AttrValueData;
import com.eyunda.third.domain.ship.ShipAttrId;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipAttrActivity extends ShipinfoActivity implements OnClickListener,OnCheckedChangeListener {
	private int mYear;
	private int mMonth;
	private int mDay;
	Data_loader data;
	Data_loader loader = new Data_loader();
	private ArrayList<SpinnerItem> res_lists; // 保存spinner的下拉列表值
	Data_loader dataLoader;
	private LinearLayout mainLayout;
	private String id;
	private LinearLayout.LayoutParams lp1;
	private LinearLayout mainLayout1;
	protected String intnum, dblnum, datetype;
	private static final int SHOW_DATAPICK = 0;
	private int ids = 7;
	protected ArrayList<ShipAttrId> charCodelist;
	protected Button btn;
	private static final int DATE_DIALOG_ID = 1;
	private TextView currentBtn;//当前选择的日期按钮
	protected String seletedDate;
	private RadioButton radioButton;
	private FrameLayout fl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.eyd_ship_attr);
		dataLoader = new Data_loader();
		setView();
		setContentView(mainLayout);
		Intent intent =getIntent();
		id= intent.getStringExtra("shipId");
		loadDate();
	}
	@Override
	protected void onStart() {
		super.onStart();
		menu_class.setOnClickListener(null);
		menu_basic.setBackgroundColor(Color.parseColor("#3B79C4"));
		menu_class.setBackgroundColor(0xFF6db7ff);
		setTitle("分类属性");
	}
	private void setView() {
		// TODO Auto-generated method stub
		// 主Linearlyout布局
		// 装载一个layout
		mainLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.eyd_ship_attr, null);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundResource(R.color.white);
		mainLayout1 = new LinearLayout(this);
		mainLayout1.setOrientation(LinearLayout.VERTICAL);
		lp1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
        fl = new FrameLayout(this);
        ProgressBar pb = new ProgressBar(this);
        fl.addView(pb);
        fl.setVisibility(View.GONE);
        mainLayout1.addView(fl,lp1);

		// 加一个ScrollView
		ScrollView scroll = new ScrollView(this);
		scroll.addView(mainLayout1);
		mainLayout.addView(scroll);
		dialogUtil = new DialogUtil(this);
	}

	AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			fl.setVisibility(View.VISIBLE);
		}

		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			fl.setVisibility(View.GONE);
			Toast.makeText(ShipAttrActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
		}

		@SuppressWarnings("unchecked")
		public void onSuccess(String arg0) {
			fl.setVisibility(View.GONE);
			Gson gson = new Gson();
			final HashMap<String, Object> rmap = gson.fromJson((String) arg0,
					new TypeToken<Map<String, Object>>() {
			}.getType());

			if (rmap.get("returnCode").equals("Success")) {
				HashMap<String, Object> map2 = (HashMap<String, Object>) rmap.get("content");
				HashMap<String, Object> attr = (HashMap<String, Object>) map2.get("shipData");
				List attrvalue = (List<String>) attr.get("attrNameDatas");

				charCodelist = new ArrayList<ShipAttrId>();
				for (int i = 0; i < attrvalue.size(); i++) {
					final AttrNameData name = new AttrNameData((Map<String, Object>) attrvalue.get(i));
					String names = name.getAttrName();// 获取显示的标签名
					//System.out.println(name.getAttrNameCode());// 标签名编码
					name.getAttrValues();  //标签名所有内容
					String curr = name.getCurrAttrValue().getAttrValue();

					ids = ids + 1;
					ShipAttrId att = new ShipAttrId();
					att.setName("a" + name.getAttrNameCode()); // 提交的标签名编码
					att.setId(ids); // 存放id值
					switch (name.getAttrType()) {// 判断展示类型，输入框、下拉框。。。
					case charcode:// 下拉框
						List<AttrValueData> valuelists = name.getAttrValueDatas();// 获取下拉框列表
						res_lists = null;
						res_lists = new ArrayList<SpinnerItem>();
						// 添加一个linerLearLayout
						final Spinner spinner = new Spinner(
								ShipAttrActivity.this);

						spinner.setId(ids);
						spinner.setGravity(Gravity.CENTER);
						att.setTypCode(AttrTypeCode.charcode);
						// TODO 添加一个textView
						TextView tv_spinner = new TextView(
								ShipAttrActivity.this);
						for (int j = 0; j < valuelists.size(); j++) {
							final AttrValueData valuedata = (AttrValueData) valuelists
									.get(j);
							String code = name.getAttrNameCode(); // 编码key
							String data = valuedata.getAttrValue(); // 显示在界面的值
							String dataCode = valuedata.getAttrValueCode(); // 需要提交的value
							SpinnerItem si = new SpinnerItem(code, data,dataCode);
							res_lists.add(si);

							tv_spinner.setWidth(250);
							tv_spinner.setGravity(Gravity.CENTER);
							tv_spinner.setText(names);
							tv_spinner.setTextSize(15);

						}

						// 添加一个子LinearLayout
						LinearLayout linearLayout1 = new LinearLayout(
								ShipAttrActivity.this);
						// 子LinearLayout水平布局
						linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
						// 给这个layout加入下拉框和一个TextView
						LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						// lp0.setMargins(10, 0, 10, 10);
						lp0.weight = 1;
						// Linearlayout1加入TextView和spinner
						linearLayout1.addView(tv_spinner, lp0);
						linearLayout1.addView(spinner, lp0);
						// mainLayout.addView(Navigation_area, lp1);

						// 子布局加入到主布局中

						mainLayout1.addView(linearLayout1, lp1);
						ArrayAdapter<SpinnerItem> adapter1 = new ArrayAdapter<SpinnerItem>(
								ShipAttrActivity.this, R.layout.spinner_item,
								R.id.contentTextView, res_lists);
						spinner.setAdapter(adapter1);
						// 监听
						//	spinnerValue = ((SpinnerItem) spinner.getSelectedItem()).getData();

						break;

					case charstr: // 输入框
						// 添加一个EditText和TextView
						TextView tv_editText = new TextView(
								ShipAttrActivity.this);
						EditText et_charstr = new EditText(ShipAttrActivity.this);
						// et.setId(1);
						String charstr = name.getAttrNameCode();// 标签名编码
						// charCodelist.add(charstr);
						et_charstr.setText(name.getCurrAttrValue().getAttrValue()); // 当前的值
						tv_editText.setText(names);
						et_charstr.setWidth(350);
						tv_editText.setHeight(100);
						tv_editText.setGravity(Gravity.CENTER);
						tv_editText.setTextSize(15);

						et_charstr.setId(ids);
						att.setTypCode(AttrTypeCode.charstr);
						// 添加一个子LinearLayout
						LinearLayout linearLayout2 = new LinearLayout(
								ShipAttrActivity.this);
						// 子LinearLayout水平布局
						linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						lp2.weight = 1;
						linearLayout2.addView(tv_editText, lp2);
						linearLayout2.addView(et_charstr, lp2);
						mainLayout1.addView(linearLayout2, lp1);

						break;
					case booltype: //RadioButton
						TextView tv_boolean = new TextView(ShipAttrActivity.this);
						RadioGroup group = new RadioGroup(ShipAttrActivity.this);
						group.setOnCheckedChangeListener(ShipAttrActivity.this);
						group.setOrientation(RadioGroup.HORIZONTAL);// RadioGroup水平
						//动态生成RadioButton
						List<AttrValueData> valuelists2 = name.getAttrValueDatas();
						//循环得出RdioButton的数量和标签名
						att.setTypCode(AttrTypeCode.booltype);
						for(int k=0;k<valuelists2.size();k++){
							AttrValueData valuedata = (AttrValueData) valuelists2.get(k);
							RadioButton bt = new RadioButton(ShipAttrActivity.this);
							bt.setText(valuedata.getAttrValue());
							bt.setTag(valuedata.getAttrValueCode());
							group.addView(bt);
							//bt.setId(ids);
						}
						tv_boolean.setText(names);
						tv_boolean.setWidth(200);
						tv_boolean.setGravity(Gravity.CENTER);
						LinearLayout radiolayout = new LinearLayout(ShipAttrActivity.this);
						// 子LinearLayout水平布局
						radiolayout.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						lp6.weight=1;
						radiolayout.addView(tv_boolean, lp6);
						radiolayout.addView(group, lp6);
						mainLayout1.addView(radiolayout, lp1);

						break;
					case datetype: //日期
						// 添加一个EditText和TextView

						TextView tv = new TextView(ShipAttrActivity.this);
						Button btnDate = new Button(ShipAttrActivity.this);
						btnDate.setWidth(350);
						tv.setGravity(Gravity.CENTER);
						btn= new Button(ShipAttrActivity.this);
						btnDate.setBackgroundResource(R.drawable.info_item4_serch_normal_but);
						// 设置监听
						datetype = name.getAttrNameCode();// 标签名编码
						btnDate.setOnClickListener(dateListener);
						btnDate.setText(name.getCurrAttrValue().getAttrValue());
						tv.setText(names);
						btnDate.setId(ids);
						att.setTypCode(AttrTypeCode.datetype);
						// 添加一个子LinearLayout
						LinearLayout linearLayout3 = new LinearLayout(ShipAttrActivity.this);
						// 子LinearLayout水平布局
						linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						lp3.weight = 1;
						linearLayout3.addView(tv, lp3);
						linearLayout3.addView(btnDate, lp3);
						mainLayout1.addView(linearLayout3, lp1);

						break;
					case dblnum:  
						// 添加一个EditText和TextView
						TextView tv_editText2 = new TextView(ShipAttrActivity.this);
						dblnum = name.getAttrNameCode();// 标签名编码
						EditText et_dblum = new EditText(ShipAttrActivity.this);
						et_dblum.setText(name.getCurrAttrValue().getAttrValue());

						et_dblum.setId(ids);
						att.setTypCode(AttrTypeCode.dblnum);
						et_dblum.setInputType(InputType.TYPE_CLASS_NUMBER);
						tv_editText2.setText(names);
						et_dblum.setWidth(450); 
						tv_editText2.setHeight(100);
						tv_editText2.setWidth(200);
						tv_editText2.setGravity(Gravity.CENTER);
						tv_editText2.setTextSize(15);
						// 添加一个子LinearLayout
						LinearLayout linearLayout4 = new LinearLayout(ShipAttrActivity.this);
						// 子LinearLayout水平布局
						linearLayout4.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						lp4.weight = 1;
						linearLayout4.addView(tv_editText2, lp4);
						linearLayout4.addView(et_dblum, lp4);
						mainLayout1.addView(linearLayout4, lp1);

						break;
					case intnum:
						TextView tv_editText3 = new TextView(
								ShipAttrActivity.this);
						intnum = name.getAttrNameCode();// 标签名编码
						EditText et_intnum = new EditText(ShipAttrActivity.this);
						et_intnum.setText(name.getCurrAttrValue().getAttrValue());
						et_intnum.setId(ids);
						att.setTypCode(AttrTypeCode.intnum);
						et_intnum.setInputType(InputType.TYPE_CLASS_NUMBER);
						tv_editText3.setText(names);
						et_intnum.setWidth(450);
						tv_editText3.setHeight(100);
						tv_editText3.setWidth(200);
						tv_editText3.setGravity(Gravity.CENTER);
						tv_editText3.setTextSize(15);
						// 添加一个子LinearLayout
						LinearLayout linearLayout5 = new LinearLayout(
								ShipAttrActivity.this);
						// 子LinearLayout水平布局
						linearLayout5.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						lp5.weight = 1;
						linearLayout5.addView(tv_editText3, lp5);
						linearLayout5.addView(et_intnum, lp5);
						mainLayout1.addView(linearLayout5, lp1);

						break;
					default:
						break;
					}
					charCodelist.add(att);
					Log.v("charCodelist", charCodelist.toString());

				}
				// 在最后添加一个Button
				Button btnSave = new Button(ShipAttrActivity.this);
				btnSave.setOnClickListener(ShipAttrActivity.this);
				btnSave.setBackgroundColor(Color.parseColor("#E44647"));
				btnSave.setText("保存");
				btnSave.setTextColor(Color.parseColor("#ffffff"));
				btnSave.setId(2);
				if(attrvalue.isEmpty()){
					btnSave.setVisibility(View.GONE);
				}
				mainLayout1.addView(btnSave);

			}
		}

	};


	OnClickListener dateListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Message msg = new Message();
			msg.what = ShipAttrActivity.SHOW_DATAPICK;
			ShipAttrActivity.this.dateandtimeHandler.sendMessage(msg);
			for (int i = 0; i < charCodelist.size(); i++) {
				ShipAttrId sa = charCodelist.get(i);
				switch (sa.getTypCode()) {
				case datetype:
					Button bt = (Button)findViewById(sa.getId());
					if(v.getId()==sa.getId()){
						currentBtn =bt;
					}
					break;
				default:
					break;
				}
			}
		}
	};



	protected void loadDate() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("id", id);
		dataLoader.getApiResult(showHandler, "/mobile/ship/myShip/edit",
				apiParams, "get");

	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 2: // 保存
			HashMap<String, Object> apiParams = new HashMap<String, Object>();
			for (int i = 0; i < charCodelist.size(); i++) {
				ShipAttrId sa = charCodelist.get(i);
				switch (sa.getTypCode()) {
				case charcode:
					Spinner tempSp = (Spinner) findViewById(sa.getId());
					SpinnerItem si = (SpinnerItem) tempSp.getSelectedItem();
					apiParams.put(sa.getName(), si.getData());

					break;
				case booltype:
					//RadioButton rb = (RadioButton)findViewById(sa.getId());

					apiParams.put(sa.getName(), radioButton.getTag());

					break;
				case datetype:
					Button tempBt = (Button)findViewById(sa.getId());
					apiParams.put(sa.getName(), tempBt.getText());
					break;
				case intnum:
				case dblnum:
				case charstr:
					EditText tempET = (EditText) findViewById(sa.getId());
					apiParams.put(sa.getName(), tempET.getText().toString().trim());
				
					break;
				default:
					break;
				}
			}
			apiParams.put("id",id);
			saveAttr(apiParams);
			break;

		case 1: 
			break;
		}
	}

	private void saveAttr(HashMap<String, Object> params) {
		AsyncHttpResponseHandler savehandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("保存中", "请稍候...");
			}

			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				Toast.makeText(ShipAttrActivity.this, "保存" + cd.getMessage(),
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(Throwable error) {
				super.onFailure(error);
				Toast.makeText(ShipAttrActivity.this, "保存失败",
						Toast.LENGTH_SHORT).show();

			}
		};

		Log.v("params:", params.toString());
		dataLoader.getApiResult(savehandler,
				"/mobile/ship/myShip/saveTypeSort", params, "post");

	}

	/**
	 * 设置当前日期
	 * 
	 * @return
	 */
	private String getCurrentDateTime() {
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		String newDate = updateDateDisplay();
		return newDate;
	}

	/**
	 * 更新日期显示
	 */
	private String updateDateDisplay() {
		String newDate = new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay).toString();

		return newDate;
	}


	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			seletedDate = updateDateDisplay();
			currentBtn.setText(seletedDate);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			getCurrentDateTime();
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	Handler dateandtimeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ShipAttrActivity.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}

	};



	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int radioButtonId =group.getCheckedRadioButtonId();
		radioButton =(RadioButton)ShipAttrActivity.this.findViewById(radioButtonId);
		// Toast.makeText(this, radioButton.getTag().toString(), Toast.LENGTH_SHORT).show();
	}



}
