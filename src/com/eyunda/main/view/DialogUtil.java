package com.eyunda.main.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.eyunda.main.localdata.LocalData;
import com.hy.client.R;
import com.ta.TAApplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * comment:弹出框工具类
 * 
 * @author:WuJiaHong Date:2012-11-15
 */
public class DialogUtil {
	private Activity context;
	private String showErrorMsg = "请检查Style是否有dialog样式,布局文件是否有confirm_dialog.xml,是否有两个TextView ID分别为pTitle,pMsg，是否有两个Button ID为别为:confirm_btn,cancel_btn";
	public static int WIDTH = 0;

	public DialogUtil(Activity context) {
		this.context = context;
		if (WIDTH == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			WIDTH = dm.widthPixels;
		}
	}

	/***
	 * 自定义退出弹框
	 * 
	 * @param title
	 * @param msg
	 */
	public void showQuitDialogFromConfig(String title, String msg) {
		try {
			QuitDialog view = new QuitDialog(context).Show2();
			final Dialog builder = new Dialog(context,
					ThemeUtils.getResourceId(context, "dialog", "style"));
			// builder.setContentView(ThemeUtils.getResourceId(context,"confirm_dialog",Constants.LAYOUT));
			builder.setContentView(view);
			// TextView ptitle = (TextView)
			// builder.findViewById(ThemeUtils.findResClassId(context,Constants.R_ID,"pTitle"));
			// TextView pMsg = (TextView)
			// builder.findViewById(ThemeUtils.findResClassId(context,Constants.R_ID,"pMsg"));

			TextView ptitle = view.title;
			TextView pMsg = view.content;
			ptitle.setText(title);
			pMsg.setText(msg);
			// final Button confirm_btn = (Button)
			// builder.findViewById(ThemeUtils.findResClassId(context,Constants.R_ID,"confirm_btn"));
			// Button cancel_btn = (Button)
			// builder.findViewById(ThemeUtils.findResClassId(context,Constants.R_ID,"cancel_btn"));
			final Button confirm_btn = view.ok;
			Button cancel_btn = view.cancle;
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					confirm_btn.setEnabled(false);
					// AutoLogin.doLogoutOnline(context);//注销在线状态
					// 停止更新服务
					// context.stopService(new
					// Intent(context,AutoUpdateDataService.class));
					sysExit();
				}
			});

			cancel_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					builder.dismiss();
				}
			});
			builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			builder.show();
		} catch (Exception e) {
			// Toast.makeText(context, showErrorMsg, Toast.LENGTH_LONG).show();
			Log.v("自定义弹框：", showErrorMsg);
			e.printStackTrace();
		}

	}

	/***
	 * 自定义弹出框
	 * 
	 * @param title
	 * @param msg
	 */
	public void showDialogFromConfig(String title, String msg,
			final Handler handler) {
		try {
			QuitDialog view = new QuitDialog(context).Show2();
			final Dialog builder = new Dialog(context,
					ThemeUtils.getResourceId(context, "dialog", "style"));
			builder.setContentView(view);
			TextView ptitle = view.title;
			TextView pMsg = view.content;
			ptitle.setText(title);
			pMsg.setText(Html.fromHtml(msg));
			final Button confirm_btn = view.ok;
			Button cancel_btn = view.cancle;
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					confirm_btn.setEnabled(false);
					handler.sendMessage(handler.obtainMessage());
					builder.dismiss();
				}
			});

			cancel_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					builder.dismiss();
				}
			});
			builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			builder.show();
		} catch (Exception e) {
			Log.v("自定义弹框：", showErrorMsg);
			e.printStackTrace();
		}

	}
	public void showDialogFromConfig2(String title, String msg,
			final Handler handler) {
		try {
			QuitDialog view = new QuitDialog(context).Show2();
			final Dialog builder = new Dialog(context,
					ThemeUtils.getResourceId(context, "dialog", "style"));
			builder.setContentView(view);
			TextView ptitle = view.title;
			TextView pMsg = view.content;
			ptitle.setText(title);
			pMsg.setText(Html.fromHtml(msg));
			final Button confirm_btn = view.ok;
			Button cancel_btn = view.cancle;
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					confirm_btn.setEnabled(false);
					Message m = new Message();
					m.what=1;
					handler.sendMessage(m);
					builder.dismiss();
				}
			});
			
			cancel_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Message m = new Message();
					m.what=2;
					handler.sendMessage(m);
					builder.dismiss();
				}
			});
			builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			builder.show();
		} catch (Exception e) {
			Log.v("自定义弹框：", showErrorMsg);
			e.printStackTrace();
		}
		
	}

	/***
	 * 自定义弹出框
	 * 
	 * @param title
	 * @param msg
	 */
	public void showDialogForFile(final String mPhotoPath) {
		try {
			QuitDialog view = new QuitDialog(context).show3();
			final Dialog builder = new Dialog(context,
					ThemeUtils.getResourceId(context, "dialog", "style"));
			builder.setContentView(view);

			final Button confirm_btn = view.ok;
			Button cancel_btn = view.cancle;
			final Button center_btn = view.center;
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					confirm_btn.setEnabled(false);
					Intent intent = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					// String dir = Utils.getDiskCacheDir(context,
					// "afinalCache")
					// .getAbsolutePath();
					// if (!new File(dir).exists())
					// new File(dir).mkdirs();
					//
					// mPhotoPath = dir + File.separator
					// + System.currentTimeMillis() + ".jpg";
					try {
						new RandomAccessFile(mPhotoPath, "rw");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					File mPhotoFile = new File(mPhotoPath);

					if (!mPhotoFile.exists()) {

						try {

							mPhotoFile.createNewFile();
						} catch (IOException e) {
							Toast.makeText(context, "文件创建失败", Toast.LENGTH_LONG)
									.show();
							e.printStackTrace();
						}

					}

					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(mPhotoFile));

					context.startActivityForResult(intent, 1);
					builder.dismiss();
				}
			});
			center_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					center_btn.setEnabled(false);
					Intent intent = new Intent();

					/* 开启Pictures画面Type设定为image */

					intent.setType("image/*");

					/* 使用Intent.ACTION_GET_CONTENT这个Action */

					intent.setAction(Intent.ACTION_GET_CONTENT);

					/* 取得相片后返回本画面 */

					context.startActivityForResult(intent, 2);
					builder.dismiss();
				}
			});
			cancel_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					builder.dismiss();
				}
			});
			builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			builder.show();
		} catch (Exception e) {
			Log.v("自定义弹框：", showErrorMsg);
			e.printStackTrace();
		}

	}

	/***
	 * 自定义弹出框
	 * 
	 * @param title
	 * @param msg
	 */
	public void showDialog(String top, String msg, String button1,
			String button2, String button3, final Handler handler,
			final Object ob) {
		try {
			QuitDialog view = new QuitDialog(context).show3(top, msg, button1,
					button2, button3);
			final Dialog builder = new Dialog(context,
					ThemeUtils.getResourceId(context, "dialog", "style"));
			builder.setContentView(view);

			final Button confirm_btn = view.ok;
			Button cancel_btn = view.cancle;
			final Button center_btn = view.center;
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					confirm_btn.setEnabled(false);
					Message message = new Message();
					message.arg1 = 1;
					message.obj = ob;
					handler.sendMessage(message);
					builder.dismiss();
				}
			});
			center_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					center_btn.setEnabled(false);
					Message message = new Message();
					message.arg1 = 2;
					message.obj = ob;
					handler.sendMessage(message);
					builder.dismiss();
				}
			});
			cancel_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Message message = new Message();
					message.arg1 = 3;
					message.obj = ob;
					handler.sendMessage(message);
					builder.dismiss();
				}
			});
			builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			builder.show();
		} catch (Exception e) {
			Log.v("自定义弹框：", showErrorMsg);
			e.printStackTrace();
		}

	}

	// 退出
	public void sysExit() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startMain);

		System.exit(0);
		//android.os.Process.killProcess(android.os.Process.myPid());

	}

	public ProgressDialog loading(String title, String msg) {

		ProgressDialog dialog = new ProgressDialog(this.context);
		// 设置进度条风格，风格为圆形，旋转的
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// 设置ProgressDialog 标题
		dialog.setTitle(title);
		// 设置ProgressDialog 提示信息
		dialog.setMessage(msg);
		// 设置ProgressDialog 标题图标
		dialog.setIcon(R.drawable.logo3);
		// 设置ProgressDialog 的进度条是否不明确
		dialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		dialog.setCancelable(true);
		// 显示
		dialog.show();
		//超时关闭
		DailogCountDown mc = new DailogCountDown(30000,1000,dialog); //定义倒计时5s，每一秒tick一次
		mc.start();//启动

		return dialog;
	}

	class DailogCountDown extends CountDownTimer {
		private ProgressDialog pd;
		public DailogCountDown(long millisInFuture, long countDownInterval,ProgressDialog progressDialog) {
			super(millisInFuture, countDownInterval);
			pd = progressDialog;
		}

		@Override
		public void onFinish() {
			if(pd !=null){
				pd.dismiss();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			
		}
	}

}
