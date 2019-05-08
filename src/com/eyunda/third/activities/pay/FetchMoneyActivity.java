package com.eyunda.third.activities.pay;

import java.util.HashMap;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//收款
public class FetchMoneyActivity extends CommonActivity {

	private Data_loader mDataLoader;
	private ImageLoader mImageLoader;
	private DialogUtil mDialogUtil;
	private ProgressDialog mDialog;

	private UserData currentUser;
	TimeCount timer;

	@Bind(R.id.btnSave)
	Button btnSave;

	@Bind(R.id.tansNum)
	EditText tansNum;
	@Bind(R.id.sureDate)
	EditText sureDate;

	@Bind(R.id.twoDCode)
	ImageView twoDCode;
	
	@Bind(R.id.descript)
	EditText descript;
	
	@Bind(R.id.twoDCodeContainer)
	LinearLayout twoDCodeContainer;
	
	@Bind(R.id.useSure)
	CheckBox useSure;

	
	int width = 0;
	private int MIN_MARK = 0;
	private int MAX_MARK = 90;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_money_fetch);
		ButterKnife.bind(this);
		Intent intent = getIntent();
		currentUser = GlobalApplication.getInstance().getUserData();

		mDataLoader = new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		mDialogUtil = new DialogUtil(this);
		timer = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		getWidth();
	}

	private void getWidth() {
		Point size = new Point();
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getSize(size);
		width = size.x * 2 / 3;

	}

	@Override
	protected void onStart() {
		super.onStart();
		setImageWidth();
		sureDate.setEnabled(false);
		this.setRegion(sureDate);
		setTitle("收款");
		useSure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                if(isChecked){ 
                	sureDate.setEnabled(true);
                }else{ 
                	sureDate.setEnabled(false);
                } 
            } 
        }); 
	}

	private void setImageWidth() {
		LayoutParams ps = twoDCode.getLayoutParams();
		ps.width = width;
		ps.height = width;
		twoDCode.setLayoutParams(ps);
	}

	// 保存
	@OnClick(R.id.btnSave)
	void translate(View v) {
		btnSave.setClickable(false);
		twoDCodeContainer.setVisibility(View.GONE);
		loadData();
		// timer.start();

	}

	protected void loadData() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}

			@SuppressWarnings({ "unchecked" })
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/qcode/create", params);
				if (cd.getReturnCode().equals("Success")) {
					Map r = (HashMap<String, Object>) cd.getContent();
					String imgUrl = (String) r.get("img");
					// 加载图片
					timer.start();
					mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + imgUrl, twoDCode,
							GlobalApplication.displayImageOptions);
					twoDCodeContainer.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(FetchMoneyActivity.this, cd.getMessage(), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(FetchMoneyActivity.this, "网络连接异常", Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(FetchMoneyActivity.this, "连接服务器超时", Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(FetchMoneyActivity.this, content, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(FetchMoneyActivity.this, "未知异常", Toast.LENGTH_LONG).show();
			}

		};

		if (TextUtils.isEmpty(tansNum.getText().toString())) {
			Toast.makeText(FetchMoneyActivity.this, "请输入大于0的金额", Toast.LENGTH_SHORT).show();
			btnSave.setClickable(true);
		} else {
			if(useSure.isChecked()){
				params.put("useSure", 1);
				params.put("sureDate", Integer.parseInt(sureDate.getText().toString()));
			}else{
				params.put("useSure", 0);
				params.put("sureDate", 0);
			}
			params.put("num", Double.parseDouble(tansNum.getText().toString()));
			params.put("desc", descript.getText().toString());
			mDataLoader.getApiResult(handler, "/mobile/qcode/create", params, "post");
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnSave.setText("重新生成二维码");
			btnSave.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnSave.setClickable(false);
			btnSave.setText(millisUntilFinished / 1000 + "秒后可重新生成");
		}
	}

	private void setRegion(final EditText et) {
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (start > 1) {
					if (MIN_MARK != -1 && MAX_MARK != -1) {
						int num = Integer.parseInt(s.toString());
						if (num > MAX_MARK) {
							s = String.valueOf(MAX_MARK);
							et.setText(s);
						} else if (num < MIN_MARK)
							s = String.valueOf(MIN_MARK);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null && !s.equals("")) {
					if (MIN_MARK != -1 && MAX_MARK != -1) {
						int markVal = 0;
						try {
							markVal = Integer.parseInt(s.toString());
						} catch (NumberFormatException e) {
							markVal = 0;
						}
						if (markVal > MAX_MARK) {
							Toast.makeText(getBaseContext(), "不能超过" + String.valueOf(MAX_MARK), Toast.LENGTH_SHORT)
									.show();
							et.setText(String.valueOf(MAX_MARK));
						}
						return;
					}
				}
			}
		});
	}
}
