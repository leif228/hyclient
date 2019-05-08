package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.Config;
import com.eyunda.main.SplashActivity;
import com.eyunda.main.data.Data_loader;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.util.FavorUtil;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.main.view.MyBaseAdapter;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 新增合同
 * 
 * @author guoqiang
 *
 */
public class AddOrderActivity extends CommonListActivity implements
		OnClickListener {
	Data_loader data;
	protected boolean state = true;// 上下文菜单是否显示
	private View layout;//
	protected PopupWindow popupWindow;// 弹出菜单
	protected Long orderId;///调用时传递的合同ID或对象，新增时ID为0
	protected int currentMenu;///调用时传递的合同ID或对象，新增时ID为0
	
	protected Button btnAddTyr;
	protected Button btnAddCC;
	protected Button btnAddGSH;
	protected Button btnAddZX;
	protected Button btnAddJS;
	protected Button btnAddTK;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_list);
		
		data = new Data_loader();
		// 弹出菜单
		LayoutInflater inflater = LayoutInflater.from(this);
		layout = inflater.inflate(R.layout.eyd_popup_view, null);
		popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, false);
		// 设置pop的内容
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);

	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if(!orderId.equals(0L)){
			setTitle("编辑合同");

		}else{
			setTitle("新增合同");
			setRight("关闭", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		// 设定弹出菜单监听事件
		// 联系人
		// 新增托运人
		btnAddTyr = (Button) layout.findViewById(R.id.menu_tuoyunren);
		btnAddTyr.setVisibility(View.GONE);

		
		// 新增船舶
		btnAddCC = (Button) layout.findViewById(R.id.menu_chechuan);
		btnAddCC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(AddOrderActivity.this,"这是新增船舶",Toast.LENGTH_LONG).show();
				closePopWindow();//销毁popupwindow
				
				Intent intent = new Intent(AddOrderActivity.this,
						com.eyunda.third.activities.order.AddOrderCCActivity.class);
				Bundle bundle = new Bundle();
				//bundle.putSerializable("user", user);//合同对象
				bundle.putLong("orderId", orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// 新增货物
		btnAddGSH = (Button) layout.findViewById(R.id.menu_ganshanhuo);
		btnAddGSH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(AddOrderActivity.this,"这是新增干散货",Toast.LENGTH_LONG).show();
				closePopWindow();//销毁popupwindow
				
				Intent intent = new Intent(AddOrderActivity.this,
						com.eyunda.third.activities.order.AddOrderGSHActivity.class);
				Bundle bundle = new Bundle();
				//bundle.putSerializable("user", user);//合同对象
				bundle.putLong("orderId", orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// 装卸约定
		btnAddZX = (Button) layout.findViewById(R.id.menu_zhuangxie);
		btnAddZX.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closePopWindow();//销毁popupwindow

				Intent intent = new Intent(AddOrderActivity.this,
						com.eyunda.third.activities.order.AddOrderZXActivity.class);
				Bundle bundle = new Bundle();
				//bundle.putSerializable("user", user);//合同对象
				bundle.putLong("orderId", orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		// 结算约定
		btnAddJS = (Button) layout.findViewById(R.id.menu_jiesuan);
		btnAddJS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closePopWindow();//销毁popupwindow

				Intent intent = new Intent(AddOrderActivity.this,
						com.eyunda.third.activities.order.AddOrderJSActivity.class);
				Bundle bundle = new Bundle();
				//bundle.putSerializable("user", user);//合同对象
				bundle.putLong("orderId", orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		// 合同条款
		btnAddTK = (Button) layout.findViewById(R.id.menu_tiaokuan);
		btnAddTK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closePopWindow();//销毁popupwindow

				Intent intent = new Intent(AddOrderActivity.this,
						com.eyunda.third.activities.order.AddOrderTKActivity.class);
				Bundle bundle = new Bundle();
				//bundle.putSerializable("user", user);//合同对象
				bundle.putLong("orderId", orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}


	public void closePopWindow() {
		state = true;
		popupWindow.dismiss();
	}

	@Override
	protected synchronized void loadDate() {

	}

	MyBaseAdapter adapter;

	@Override
	protected BaseAdapter setAdapter() {
		return adapter;
	}

	@Override
	public void onClick(View v) {

	}

	protected void goBack(){
		Intent intent = new Intent(this,
				com.eyunda.third.activities.order.MyOrderActivity.class);
		Bundle bundle = new Bundle();
		//bundle.putSerializable("user", user);//合同对象
		bundle.putLong("orderId", 0L);//合同ID
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
