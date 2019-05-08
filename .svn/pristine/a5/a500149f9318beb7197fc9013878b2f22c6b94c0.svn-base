package com.eyunda.part1.plughome;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.simcpux.Constants;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.Config;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.util.FavorUtil;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.part1.data.PartData_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

public class InformationDetail extends CommonActivity {

	TextView title, time;
	WebView wv1;
	PartData_loader data;

	private String ctitle, id;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.information_detail);
		title = (TextView) findViewById(R.id.title);
		time = (TextView) findViewById(R.id.time);
		wv1 = (WebView) findViewById(R.id.wv1);
		data = new PartData_loader();
		id = getIntent().getStringExtra("id");
		dialogUtil = new DialogUtil(this);
		ctitle = getIntent().getStringExtra("ctitle");
		load(id);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		api.registerApp(Constants.APP_ID);
	}

	DialogUtil dialogUtil;
	boolean isFavor;
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    
	 /**
     * @param htmlStr
     * @return
     *  删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
//        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
//        Matcher m_script = p_script.matcher(htmlStr);
//        htmlStr = m_script.replaceAll(""); // 过滤script标签
//
//        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
//        Matcher m_style = p_style.matcher(htmlStr);
//        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        htmlStr=Html.fromHtml(htmlStr).toString();
        
       
        return htmlStr.trim(); // 返回文本字符串
    }
	@Override
	protected void onStart() {
		super.onStart();
		if (ctitle == null) {
			setTitle("资讯详情");
			isFavor = FavorUtil.isFavorinfo(id);
			top_commit_text
					.setBackgroundResource(isFavor ? R.drawable.zx_unfaver_top
							: R.drawable.zx_faver_top);
			top_commit_text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Config.USERID.equals("")) {
						dialogUtil.showDialogFromConfig("提醒",
								"\u3000\u3000关注需要先登录<br>你是否现在登录？",
								new Handler() {

									@Override
									public void handleMessage(Message msg) {
										super.handleMessage(msg);

										startActivity(new Intent(
												InformationDetail.this,
												com.eyunda.main.reg.LoginActivity.class));
									}
								});
					} else if (FavorUtil.isFavorinfo(id))
						delFavorite(id);
					else
						addFavorite(id);

				}
			});

			setRight(R.drawable.fenxiangandroid1, new OnClickListener() {
				@Override
				public void onClick(View v) {

					String text =delHTMLTag(html);
					if (text == null || text.length() == 0) {
						return;
					}
					if(text.length()>1000)text=text.substring(0, 1000);
					// 初始化一个WXTextObject对象
					WXTextObject textObj = new WXTextObject();
					textObj.text = text;

					// 用WXTextObject对象初始化一个WXMediaMessage对象
					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = textObj;
					// 发送文本类型的消息时，title字段不起作用
					// msg.title = "Will be ignored";
					msg.description = text;

					// 构造一个Req
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
					req.message = msg;
					req.scene =
					// isTimelineCb.isChecked() ?
					SendMessageToWX.Req.WXSceneTimeline
					// : SendMessageToWX.Req.WXSceneSession
					;

					// 调用api接口发送数据到微信
					api.sendReq(req);
				}
			});
		} else
			setTitle(ctitle);

	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private IWXAPI api;

	ProgressDialog dialog;

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
					FavorUtil.addFavorInfo(id);
					Toast.makeText(InformationDetail.this, "关注成功",
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(InformationDetail.this, tm.get("content"),
							Toast.LENGTH_SHORT).show();

				isFavor = FavorUtil.isFavorinfo(id);
				top_commit_text
						.setBackgroundResource(isFavor ? R.drawable.zx_unfaver_top
								: R.drawable.zx_faver_top);
			}

			@Override
			public void onFailure(Throwable error, String content) {

				super.onFailure(error, content);
				dialog.dismiss();
			}

		};

		data.infoFavorService(handler, Config.USERID, id);
	}

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
					FavorUtil.removeFavorInfo(id);
					Toast.makeText(InformationDetail.this, "取消关注成功",
							Toast.LENGTH_SHORT).show();

					isFavor = FavorUtil.isFavorinfo(id);
					top_commit_text
							.setBackgroundResource(isFavor ? R.drawable.zx_unfaver_top
									: R.drawable.zx_faver_top);
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {

				super.onFailure(error, content);
				dialog.dismiss();
			}
		};

		data.delFavoriteInfo(handler, Config.USERID, id);
	}

	String html;

	private void load(String id) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {

				Map<String, String> tm = DataConvert.toMap(content);
				if (tm != null) {

					html = tm.get("content");
					html = html.replaceAll("#", "%23");
					html = html.replaceAll("%", "%25");
					wv1.getSettings().setDefaultTextEncodingName("UTF-8");
					// wv1.loadData(html, "text/html", "UTF-8");
					wv1.loadData(html, "text/html; charset=UTF-8", null);
					// wv1.loadData(tm.get("content"),"text/html; charset=UTF-8",
					// null);
					// WebSettings web = wv1.getSettings();//
					// web.setTextSize(WebSettings.TextSize.SMALLER);
					title.setText(tm.get("title"));
					time.setText(tm.get("addTime"));
				}
			}
		};

		data.inforClinetService(handler, id);
	}

}
