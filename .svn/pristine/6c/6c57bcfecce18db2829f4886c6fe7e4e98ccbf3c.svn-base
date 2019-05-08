package com.eyunda.main.home;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.Config;
import com.eyunda.main.SplashActivity;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.reg.LoginActivity;
import com.eyunda.main.util.FavorUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class HomeActivity extends PageHomeActivity {
	ImageButton home_bar_but1, home_add_plug;
	Button home_bar_but2, home_bar_but3;
	LinearLayout home_include1, home_include2;
	RelativeLayout more_tjyx, more_gzyx;
	private TextView include_title1, include_title2,tab_gzyx;
	private ImageView[] logos = new ImageView[6];
	private ImageView[] favors = new ImageView[6];
	private TextView[] titles = new TextView[6];
	private TextView[] contents = new TextView[6];
	private LinearLayout[] tjyx = new LinearLayout[3];
	private LinearLayout[] gzyx = new LinearLayout[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		home_add_plug = (ImageButton) homeView.findViewById(R.id.home_add_plug);
		home_bar_but1 = (ImageButton) homeView.findViewById(R.id.home_bar_but1);
		home_bar_but2 = (Button) homeView.findViewById(R.id.home_bar_but2);
		home_bar_but3 = (Button) homeView.findViewById(R.id.home_bar_but3);
		home_include1 = (LinearLayout) homeView
				.findViewById(R.id.home_include1);
		home_include2 = (LinearLayout) homeView
				.findViewById(R.id.home_include2);

		home_add_plug.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HomeActivity.this, "添加插件", Toast.LENGTH_SHORT)
						.show();
			}
		});
		home_bar_but1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		home_bar_but2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		home_bar_but3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (Config.IFLOGIN) {
			home_include1.setVisibility(View.VISIBLE);
			home_include2.setVisibility(View.VISIBLE);
		} else {
			home_include2.setVisibility(View.VISIBLE);
			home_include1.setVisibility(View.GONE);
			login_name.setText("登录/注册");
			login_type.setText("登录后，推荐更多精彩");
			userHead.setImageResource(R.drawable.login_nologin);
		}
		loadData();
	}

	private void initView() {
		int j = 0;
		include_title1 = (TextView) home_include1.findViewById(R.id.include_title);
		include_title2 = (TextView) home_include2.findViewById(R.id.include_title);
		tab_gzyx= (TextView) findViewById(R.id.tab_gzyx);
		tab_gzyx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		include_title2.setText("推荐院校");
//		home_jian
		include_title1.setText("关注院校");
		more_tjyx = (RelativeLayout) home_include2.findViewById(R.id.more);
		more_gzyx = (RelativeLayout) home_include1.findViewById(R.id.more);
		more_tjyx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

			}
		});
		more_gzyx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		int chileSize = home_include1.getChildCount();
		for (int i = 0; i < chileSize; i++) {
			View tv = home_include1.getChildAt(i);
			if (tv instanceof LinearLayout) {
				if (((LinearLayout) tv).getChildCount() == 3) {
					logos[j] = (ImageView) ((LinearLayout) tv).getChildAt(0);

					LinearLayout v = (LinearLayout) ((LinearLayout) tv)
							.getChildAt(1);
					titles[j] =(TextView) ((LinearLayout) v.getChildAt(0)).getChildAt(0);
					contents[j] = (TextView) v.getChildAt(1);
					favors[j] = (ImageView) ((LinearLayout) tv).getChildAt(2);
					gzyx[j] = (LinearLayout) tv;
					j++;
				}
			}
		}
		chileSize = home_include2.getChildCount();
		for (int i = 0; i < chileSize; i++) {
			View tv = home_include2.getChildAt(i);
			if (tv instanceof LinearLayout) {
				if (((LinearLayout) tv).getChildCount() == 3) {
					logos[j] = (ImageView) ((LinearLayout) tv).getChildAt(0);
					LinearLayout v = (LinearLayout) ((LinearLayout) tv)
							.getChildAt(1);
					titles[j] = (TextView) ((LinearLayout) v.getChildAt(0)).getChildAt(0);
					( ((LinearLayout) v.getChildAt(0)).getChildAt(1)).setVisibility(View.VISIBLE);
					contents[j] = (TextView) v.getChildAt(1);
					tjyx[j - 3] = (LinearLayout) tv;
					favors[j] = (ImageView) ((LinearLayout) tv).getChildAt(2);
					j++;
				}
			}
		}

	}

	private void loadData() {
		data.recommendCollege(new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if(content!=null&&content.equals("can't resolve host"))
					Toast.makeText(HomeActivity.this,"网络连接异常",
							Toast.LENGTH_SHORT).show();

			}
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
//				Map<String, String> m = DataConvert.toMap(content);
//				if (m != null) {
					List<Map<String, String>> list = DataConvert.toArrayList(content);
					int j = 3;
					for (final Map<String, String> tm : list) {
						titles[j].setText(tm.get("schoolname"));
						contents[j].setText( tm.get("infotitle" ));
						image_loader.load_normal_Img(
								"http://www.hbksw.com/images/member/xh/"
										+ tm.get("cid") + ".png", logos[j]);
						favors[j].setImageResource(FavorUtil.isFavor(tm
								.get("cid")) ? R.drawable.home_collect_
								: R.drawable.home_collect);

						favors[j].setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if( Config.USERID.equals(""))
								{
									dialogUtil.showDialogFromConfig("提醒",
											"\u3000\u3000关注需要先登录<br>你是否现在登录？", new Handler() {

												@Override
												public void handleMessage(Message msg) {
													super.handleMessage(msg);

													startActivity(new Intent(
															HomeActivity.this,
															com.eyunda.main.reg.LoginActivity.class));
												}
											});
									return;}
								if (FavorUtil.isFavor(tm.get("cid")))
									delFavorite(tm.get("cid"));
								else
									addFavorite(tm.get("cid"));
							}
						});
						tjyx[j - 3].setVisibility(View.VISIBLE);
						tjyx[j - 3].setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
							}
						});
						j++;
					}
					while ((j - 3) < 3) {
						tjyx[j - 3].setVisibility(View.GONE);
						j++;
					}
//				}
			}

		},Config.USERID,"101");
		if (!Config.USERID.equals(""))
			data.myfavors(new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					super.onFailure(arg0);
				}

				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					// Map<String, String> m = DataConvert.toMap(content);
					// if (m != null) {
					List<Map<String, String>> list = DataConvert
							.toArrayList(content);
					int j = 0;
					for (final Map<String, String> tm : list) {
						titles[j].setText(tm.get("schoolname"));
						contents[j].setText( tm.get("infotitle") );
						favors[j].setImageResource(FavorUtil.isFavor(tm
								.get("cid")) ? R.drawable.home_collect_
								: R.drawable.home_collect);
						favors[j].setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if( Config.USERID.equals(""))
								{
									dialogUtil.showDialogFromConfig("提醒",
											"\u3000\u3000关注需要先登录<br>你是否现在登录？", new Handler() {

												@Override
												public void handleMessage(Message msg) {
													super.handleMessage(msg);

													startActivity(new Intent(
															HomeActivity.this,
															com.eyunda.main.reg.LoginActivity.class));
												}
											});
									return;}
								if (FavorUtil.isFavor(tm.get("cid")))
									delFavorite(tm.get("cid"));
								else
									addFavorite(tm.get("cid"));
							}
						});

						image_loader.load_normal_Img(
								"http://www.hbksw.com/images/member/xh/"
										+ tm.get("cid") + ".png", logos[j]);
						gzyx[j].setVisibility(View.VISIBLE);
						gzyx[j].setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
							}
						});
						j++;
					}

					while (j < 3) {
						gzyx[j].setVisibility(View.GONE);
						j++;
					}

				}

			}, Config.USERID,"101");

	};

	private void addFavorite(final String id) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("提示", "添加关注中...");
			}

			@Override
			public void onSuccess(String content) {
				// {"result":0,"content":"关注成功"}
				dialog.dismiss();
				Map<String, String> tm = DataConvert.toMap(content);

				if (tm.get("result").equals("0")) {
					FavorUtil.addFavor(id) ;
					Toast.makeText(HomeActivity.this, "关注成功",
							Toast.LENGTH_SHORT).show();
				} else if (tm.get("result").equals("2")) {
					Toast.makeText(HomeActivity.this, "关注已经存在",
							Toast.LENGTH_SHORT).show();
				} else if (tm.get("result").equals("3")) {
					Toast.makeText(HomeActivity.this, "关注超过限制",
							Toast.LENGTH_SHORT).show();
				}
				loadData();

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if(content!=null&&content.equals("can't resolve host"))
					Toast.makeText(HomeActivity.this,"网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialog.dismiss();

			}

		};

		data.addFavorite(handler, Config.USERID, id,"101");
	}

	ProgressDialog dialog;

	private void delFavorite(final String id) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("提示", "取消关注中...");
			}

			@Override
			public void onSuccess(String content) {
				// {"result":0,"content":"关注成功"}
				dialog.dismiss();
				
				Map<String, String> tm = DataConvert.toMap(content);
				if (tm.get("result").equals("0")) {
					FavorUtil.removeFavor(id);
					Toast.makeText(HomeActivity.this, "取消关注成功",
							Toast.LENGTH_SHORT).show();
				}
				loadData();

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if(content!=null&&content.equals("can't resolve host"))
					Toast.makeText(HomeActivity.this,"网络连接异常",
							Toast.LENGTH_SHORT).show();
				dialog.dismiss();

			}


		};

		data.delFavorite(handler, Config.USERID, id,"101");
	}

}
