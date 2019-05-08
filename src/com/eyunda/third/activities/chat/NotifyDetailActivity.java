package com.eyunda.third.activities.chat;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.SplashActivity;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.adapters.chat.domain.NotifyMessage;
import com.eyunda.third.adapters.chat.manager.NotifyManager;
import com.eyunda.third.chat.utils.LogUtil;
import com.eyunda.tools.SystemUtil;

public class NotifyDetailActivity extends CommonListActivity {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotifyDetailActivity.class);

	private String callbackActivityPackageName;

	private String callbackActivityClassName;
	String notificationId = "";
	String notificationTime = "";
	String notificationTitle = "";
	String notificationMessage = "";
	String notificationUri = "";
	Context context = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// SharedPreferences sharedPrefs = this.getSharedPreferences(
		// Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		// callbackActivityPackageName = sharedPrefs.getString(
		// Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
		// callbackActivityClassName = sharedPrefs.getString(
		// Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");

		
	}
	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		Long notificationId = intent.getLongExtra("notifyId", -1);
		if (notificationId != -1) {
			NotifyMessage nm = NotifyManager.getInstance().getById(
					Long.valueOf(notificationId));
			if (nm != null) {
				notificationTitle = nm.getTitle();
				notificationMessage = nm.getMessage();
				System.out.println("@@notificationMessage:"+notificationMessage);
				notificationTime = nm.getCreateTime().toString();
			}else{
				notificationTitle = intent.getStringExtra("notifyTitle");
				notificationMessage = intent.getStringExtra("notifyContent");
			}
		}

		View rootView = createView(notificationTitle, notificationMessage);
		setContentView(rootView);
	}

	private View createView(final String title, final String message) {

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(0xffeeeeee);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(5, 5, 5, 5);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		linearLayout.setLayoutParams(layoutParams);

		TextView textTitle = new TextView(this);
		textTitle.setText(title);
		textTitle.setTextSize(18);
		// textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		textTitle.setTextColor(0xff000000);
		textTitle.setGravity(Gravity.CENTER);

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 30, 30, 0);
		textTitle.setLayoutParams(layoutParams);
		linearLayout.addView(textTitle);

		TextView textDetails = new TextView(this);
		textDetails.setText(message);
		textDetails.setTextSize(14);
		// textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		textDetails.setTextColor(0xff333333);
		textDetails.setGravity(Gravity.CENTER);

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 10, 30, 20);
		textDetails.setLayoutParams(layoutParams);
		linearLayout.addView(textDetails);

		Button okButton = new Button(this);
		okButton.setText("确定");
		okButton.setWidth(100);

		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//用户退出后，直接进入登入页面
				SharedPreferences sp = context.getSharedPreferences(
						"eyundaBindingCode", MODE_PRIVATE);
				String bindingCode = sp.getString("bindingCode", "");
				if("".equals(bindingCode)){
					startActivity(new Intent(
							context,
							com.eyunda.third.activities.user.LoginActivity.class));
					finish();
					return;
				}
				
				//判断app进程是否存活
				 if(SystemUtil.isAppAlive(context, "com.eyunda.main")){
					 Intent mainIntent = new Intent(context, MenuActivity.class);
					 	//如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
			            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
			            //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
			            //DetailActivity前，要先启动MainActivity。
					 mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					 	//将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
			            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
			            //如果Task栈不存在MainActivity实例，则在栈顶创建
					 Intent goIntent = null;
					 if(notificationMessage.contains("发送者:")){ //用户自己写的通知消息，发送时系统带上“发送者:” 字样。
						 	goIntent = new Intent(NotifyDetailActivity.this,
									NewChatAllHistoryActivity.class).putExtra("selTab", "notify");
						}else if(notificationMessage.startsWith("货号")){ //系统生成的通知消息，消息内容不带“发送者:”。
							String cargoId = notificationMessage.substring(2, notificationMessage.indexOf(","));
							goIntent = new Intent(NotifyDetailActivity.this,
									CargoPreviewActivity.class).putExtra("id", cargoId);
						}else if(notificationMessage.startsWith("船舶")){  //系统生成的通知消息，消息内容不带“发送者:”。  处理船舶加油
							goIntent = new Intent(NotifyDetailActivity.this,
									MyshipActivity.class);
						} else {
							goIntent = new Intent(NotifyDetailActivity.this,
									MyOrderActivity.class);
						}
					 	Intent[] intents = {mainIntent, goIntent};

			            context.startActivities(intents);
			            NotifyDetailActivity.this.finish();
				 }else{
					 Intent launchIntent = context.getPackageManager().
			                    getLaunchIntentForPackage("com.eyunda.main");
			            launchIntent.setFlags(
			                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			            Bundle args = new Bundle();
			            if(notificationMessage.contains("发送者:")){ //用户自己写的通知消息，发送时系统带上“发送者:” 字样。
			            	args.putString("activity", "NewChatAllHistoryActivity");
			            	args.putString("selTab", "notify");
						}else if(notificationMessage.startsWith("货号")){ //系统生成的通知消息，消息内容不带“发送者:”。
							String cargoId = notificationMessage.substring(2, notificationMessage.indexOf(","));
							args.putString("activity", "CargoPreviewActivity");
							args.putString("id", cargoId);
						}else if(notificationMessage.startsWith("船舶")){  //系统生成的通知消息，消息内容不带“发送者:”。  处理船舶加油
							args.putString("activity", "MyshipActivity");
						} else {
							args.putString("activity", "MyOrderActivity");
						}
			            launchIntent.putExtra("NotifyGotoActivity", args);
			            context.startActivity(launchIntent);
			            NotifyDetailActivity.this.finish();
				 }
			}
		});

		LinearLayout innerLayout = new LinearLayout(this);
		innerLayout.setGravity(Gravity.CENTER);
		innerLayout.addView(okButton);

		linearLayout.addView(innerLayout);

		return linearLayout;
	}

	// protected void onPause() {
	// super.onPause();
	// finish();
	// }
	//
	// protected void onStop() {
	// super.onStop();
	// finish();
	// }
	//
	// protected void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// }
	//
	// protected void onNewIntent(Intent intent) {
	// setIntent(intent);
	// }

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub

	}

}
