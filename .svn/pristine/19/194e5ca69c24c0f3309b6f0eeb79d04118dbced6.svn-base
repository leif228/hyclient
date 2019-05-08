package com.eyunda.third.activities.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.oil.GasOrderActivity;
import com.eyunda.tools.pay.PayEntity;
import com.eyunda.tools.pay.SignUtils;
import com.hy.client.R;

public class ShipGasOrderPaymentActivity extends FragmentActivity {

	public static final String PARTNER = ApplicationConstants.ALIPAY_PARTNER;
	public  String SELLER = ApplicationConstants.ALIPAY_SELLER;
	public static final String RSA_PRIVATE = ApplicationConstants.ALIPAY_RSA_PRIVATE;
	public static final String RSA_PUBLIC = ApplicationConstants.ALIPAY_RSA_PUBLIC;

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Button payBtn;
	protected DialogUtil dialogUtil;
	protected ProgressDialog dialog;
	
	private TextView product_subject;
	private TextView product_descript;
	private TextView product_price;
	private PayEntity payData = null;
	private String payType = "2";
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(ShipGasOrderPaymentActivity.this, "支付成功",Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(ShipGasOrderPaymentActivity.this,ShipGasOrderActivity.class);
					Intent intent = new Intent(ShipGasOrderPaymentActivity.this,GasOrderActivity.class);
					startActivity(intent);
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(ShipGasOrderPaymentActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(ShipGasOrderPaymentActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				if("true".equalsIgnoreCase((String)msg.obj)){
					Toast.makeText(ShipGasOrderPaymentActivity.this, "您已经安装支付宝客户端，可以正常支付",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ShipGasOrderPaymentActivity.this, "请先安装支付宝客户端，才能完成支付",Toast.LENGTH_SHORT).show();
				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
		
		Intent intent = this.getIntent();
		payData = (PayEntity) intent.getSerializableExtra("payData");
//		shipId = (String) intent.getStringExtra("shipId");
//		shipName = (String) intent.getStringExtra("shipName");
		//获取当前登录者信息，当前order信息,初始化页面信息
		initView();
		setView();
	}

	private void initView(){
		payBtn = (Button)findViewById(R.id.pay);
		payBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(payType.equalsIgnoreCase("2")){
					pay(v);
				}else{
					//跳转到webView
					Intent intent = new Intent();
					intent.setClass(ShipGasOrderPaymentActivity.this, ShipGasOrderPayActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("payData",payData);
//					bundle.putString("shipId", shipId);//shipId
//					bundle.putString("shipName", shipName);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}

			}
		});
		payBtn.setClickable(false);
		product_subject = (TextView)findViewById(R.id.product_subject);
		product_descript = (TextView)findViewById(R.id.product_descript);
		product_price = (TextView)findViewById(R.id.product_price);
		
	}
	
	/**
	 * 根据order状态，以及当前登录者身份确认支付金额及内容
	 */
	public void setView(){	
		product_subject.setText(payData.getSubject());
		product_descript.setText(payData.getBody());
		product_price.setText(payData.getPrice());
		payBtn.setClickable(true);
	}
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		//根据订单确定当前支付状态
		String orderInfo = getOrderInfo();
		//TODO:异步从服务器获取签名结果
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(ShipGasOrderPaymentActivity.this);
				// 调用支付接口
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(ShipGasOrderPaymentActivity.this);
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo() {
		String orderInfo = "";
		if(payData !=null){
			String subject = payData.getSubject();
			String body = payData.getBody();
			String price = payData.getPrice();
			String oid = payData.getOrderNo();
			String sellerId = payData.getSeller();
			
			String royalty = payData.getRoyaltyParameters();//分润
			// 合作者身份ID
			orderInfo += "partner=" + "\"" + PARTNER + "\"";
			// 卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + sellerId + "\"";
			// 商户网站唯一订单号
			orderInfo += "&out_trade_no=" + "\"" + oid + "\"";
			// 商品名称
			orderInfo += "&subject=" + "\"" + subject + "\"";
			// 商品详情
			orderInfo += "&body=" + "\"" + body + "\"";
			// 商品金额
			orderInfo += "&total_fee=" + "\"" + price + "\"";
			// 服务器异步通知页面路径
			orderInfo += "&notify_url=" + "\"" + ApplicationConstants.NOTIFY_URL_GASORDERPAY
					+ "\"";
			// 接口名称， 固定值
			orderInfo += "&service=\"mobile.securitypay.pay\"";
			// 支付类型， 固定值
			orderInfo += "&payment_type=\"1\"";
			// 参数编码， 固定值
			orderInfo += "&_input_charset=\"utf-8\"";
			
			if(!royalty.equals("")){
				orderInfo += "&royalty_type=\"10\"";
				orderInfo += "&royalty_parameters=\""+royalty+"\"";
				//orderInfo += "&royalty_parameters=\"13539870394@163.com^0.03^一次性付款\"";
			}
			
			// 设置未付款交易的超时时间
			// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
			// 取值范围：1m～15d。
			// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
			// 该参数数值不接受小数点，如1.5h，可转换为90m。
			orderInfo += "&it_b_pay=\"2h\"";
	
			// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
			orderInfo += "&return_url=\"m.alipay.com\"";
	
			// 调用银行卡支付，需配置此参数，参与签名， 固定值
			// orderInfo += "&paymethod=\"expressGateway\"";
		}
//		Log.setLog2FileEnabled(true);
//		Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
//		Log.d(orderInfo);
		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
