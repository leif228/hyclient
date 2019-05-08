package com.eyunda.third.activities.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.adapters.user.UserChildManager;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.ship.ShipNameData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UserChildSelShipActivity extends CommonListActivity implements
		OnClickListener {
	private Button basic_submit;
	TableLayout tableLayout;
	// UserData user;
	List<ShipNameData> list;
	private String mmsis = "";
	private String selmmsis = "";
	private String selshipName = "";
	public static final int REQUEST_CODE_SELECT = 0;
	public static final int NOTIFY = 1;
	protected static final int CHECK_OK = 2;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String hShipName = (String) msg.obj;
			switch (msg.what) {
			case NOTIFY:
				Toast.makeText(UserChildSelShipActivity.this,
						"该船舶已经有人上报了:" + hShipName, Toast.LENGTH_LONG).show();
				break;
			case CHECK_OK:
//				Toast.makeText(UserChildSelShipActivity.this, hShipName, 1)
//						.show();
				ok(selmmsis, selshipName);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_userchildselship);

		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		list = UserChildManager.getInstance().getShipList();
		mmsis = getIntent().getStringExtra("mmsis");
		selmmsis = getIntent().getStringExtra("selmmsis") == null ? ""
				: getIntent().getStringExtra("selmmsis");

		initial();
		creatTable();
	}

	private void creatTable() {
		int col = 3;
		int num = 0;
		lable: if (list.size() > 0) {
			int row = list.size() % 3 == 0 ? list.size() / 3
					: list.size() / 3 + 1;
			for (int i = 0; i < row; i++) {
				TableRow tableRow = new TableRow(UserChildSelShipActivity.this);
				for (int j = 0; j < col; j++) {
					num++;
					if ((num - 1) == list.size()) {
						tableLayout.addView(tableRow);
						break lable;
					} else {
						ShipNameData sd = list.get(num - 1);
						CheckBox bx = new CheckBox(
								UserChildSelShipActivity.this);
						bx.setId(Integer.parseInt(sd.getMmsi()));
						bx.setText(sd.getShipName());
						if (!selmmsis.equals("")) {
							if (selmmsis.indexOf(sd.getMmsi()) >= 0)
								bx.setChecked(true);
						} else {
							if (mmsis != null && !"".equals(mmsis)) {
								if (mmsis.indexOf(sd.getMmsi()) >= 0)
									bx.setChecked(true);
							}
						}
						tableRow.addView(bx);
					}
				}
				tableLayout.addView(tableRow);
			}
		} else {
			Toast.makeText(this, "没有船舶数据!", Toast.LENGTH_LONG).show();
		}

	}

	private void initial() {
		tableLayout = (TableLayout) this.findViewById(R.id.tablelayout);
		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_submit.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("授权船舶");
	}

	public void ok(String mmsis, String shipNames) {
		setResult(
				RESULT_OK,
				new Intent().putExtra("mmsis", mmsis).putExtra("shipNames",
						shipNames));
		finish();

	}

	private void check() {
		String checkms = "";
		String shipNames = "";
		if (list.size() > 0) {
			for (ShipNameData sd : list) {
				String ids = sd.getMmsi();
				int id = getResources().getIdentifier(ids, "id",
						"com.eyunda.main");
				if (id != 0) {
					CheckBox bx = (CheckBox) this.findViewById(id);
					if (bx.isChecked()) {
						checkms += sd.getMmsi() + ",";
						shipNames += sd.getShipName() + ",";
					}
				}
			}
		}
		if (checkms.length() > 0 && shipNames.length() > 0) {
			checkms = checkms.substring(0, checkms.length() - 1);
			shipNames = shipNames.substring(0, shipNames.length() - 1);
		}
		selmmsis = checkms;
		selshipName = shipNames;
		checkSelMmsi(checkms, shipNames);
	}

	private void checkSelMmsi(String selMmsi, String shipNames) {
		if (selMmsi != null && !"".equals(selMmsi)) {
			boolean flag = false;
			String[] selMmsiarr = selMmsi.split(",");
			List<String> list = new LinkedList<String>(
					Arrays.asList(selMmsiarr));
			if (mmsis != null && !"".equals(mmsis)) {
				for (int s = list.size() - 1; s >= 0; s--) {
					if (mmsis.indexOf(list.get(s)) > -1) {
						list.remove(s);
						flag = true;
					}
				}
			}
			String chms = "";
			if (list.size() > 0) {
				for (String m : list) {
					chms += m + ",";
				}
				chms = chms.length() > 0 ? chms.substring(0, chms.length() - 1)
						: "";
				synCheck(chms, shipNames);
			} else if (list.size() == 0 && flag) {
				ok(selMmsi, shipNames);
			} else if (list.size() == 0 && !flag) {
				Toast.makeText(this, "请选择上报的船舶!", Toast.LENGTH_LONG).show();
			}

		} else
			Toast.makeText(this, "请选择上报的船舶", Toast.LENGTH_LONG).show();
	}

	private void synCheck(final String chms, final String shipNames) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("检查中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				try {
					dialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Failure")) {
						final String hs = (String) map.get("hadShipName");
						final String hm = (String) map.get("hadMmsi");
						UserChildSelShipActivity.this
								.runOnUiThread(new Runnable() {
									public void run() {
										Message message = new Message();
										message.obj = hs;
										message.what = NOTIFY;
										mhandler.sendMessage(message);
										String[] hmarr = hm.split(",");
										for (String s : hmarr) {
											int id = getResources()
													.getIdentifier(s, "id",
															"com.eyunda.main");
											if (id != 0) {
												CheckBox bx = (CheckBox) UserChildSelShipActivity.this
														.findViewById(id);
												bx.setChecked(false);
											}
										}
									}
								});
					} else {
						Message message = new Message();
						message.obj = map.get("message").toString();
						message.what = CHECK_OK;
						mhandler.sendMessage(message);
					}
				} catch (Exception e) {
					Toast.makeText(UserChildSelShipActivity.this,
							e.getMessage(), 1).show();
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")) {
					UserChildSelShipActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(UserChildSelShipActivity.this,
									"网络连接异常", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		};
		// 手动登入
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ms", chms);
		data.getApiResult(handler, "/mobile/account/checkSBShip", params);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.basic_submit:
			try {
				check();

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
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
}
