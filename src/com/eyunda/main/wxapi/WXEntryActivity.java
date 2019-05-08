package com.eyunda.main.wxapi;


import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.GetFromWXActivity;
import net.sourceforge.simcpux.SendToWXActivity;
import net.sourceforge.simcpux.ShowFromWXActivity;
import net.sourceforge.simcpux.uikit.MMAlert;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hy.client.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private Button gotoBtn, regBtn, launchBtn, checkBtn;
	
	// IWXAPI 锟角碉拷锟斤拷锟斤拷app锟斤拷微锟斤拷通锟脚碉拷openapi锟接匡拷
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        
        // 通锟斤拷WXAPIFactory锟斤拷锟斤拷锟斤拷锟斤拷取IWXAPI锟斤拷实锟斤拷
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);

    	regBtn = (Button) findViewById(R.id.reg_btn);
    	regBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 锟斤拷锟斤拷app注锟结到微锟斤拷
			    api.registerApp(Constants.APP_ID);    	
			}
		});
    	
        gotoBtn = (Button) findViewById(R.id.goto_send_btn);
        gotoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
		        finish();
			}
		});
        
        launchBtn = (Button) findViewById(R.id.launch_wx_btn);
        launchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(WXEntryActivity.this, "launch result = " + api.openWXApp(), Toast.LENGTH_LONG).show();
			}
		});
        
        checkBtn = (Button) findViewById(R.id.check_timeline_supported_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				int wxSdkVersion = api.getWXAppSupportAPI();
//				if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
//					Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported", Toast.LENGTH_LONG).show();
//				} else {
//					Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported", Toast.LENGTH_LONG).show();
//				}
				
				

				
				final EditText editor = new EditText(WXEntryActivity.this);
				editor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				editor.setText(R.string.send_text_default);
								
				MMAlert.showAlert(WXEntryActivity.this, "send text", editor, getString(R.string.app_share), getString(R.string.app_cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = editor.getText().toString();
						if (text == null || text.length() == 0) {
							return;
						}
						
						// 锟斤拷始锟斤拷一锟斤拷WXTextObject锟斤拷锟斤拷
						WXTextObject textObj = new WXTextObject();
						textObj.text = text;

						// 锟斤拷WXTextObject锟斤拷锟斤拷锟绞硷拷锟揭伙拷锟絎XMediaMessage锟斤拷锟斤拷
						WXMediaMessage msg = new WXMediaMessage();
						msg.mediaObject = textObj;
						// 锟斤拷锟斤拷锟侥憋拷锟斤拷锟酵碉拷锟斤拷息时锟斤拷title锟街段诧拷锟斤拷锟斤拷锟斤拷
						// msg.title = "Will be ignored";
						msg.description = text;

						// 锟斤拷锟斤拷一锟斤拷Req
						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.transaction = buildTransaction("text"); // transaction锟街讹拷锟斤拷锟斤拷唯一锟斤拷识一锟斤拷锟斤拷锟斤拷
						req.message = msg;
						req.scene =  SendMessageToWX.Req.WXSceneTimeline  ;
						
						// 锟斤拷锟斤拷api锟接口凤拷锟斤拷锟斤拷锟捷碉拷微锟斤拷
						api.sendReq(req);
					}
				}, null);
			}
		});
        
        api.handleIntent(getIntent(), this);
    }
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// 微锟脚凤拷锟斤拷锟斤拷锟襟到碉拷锟斤拷锟斤拷应锟斤拷时锟斤拷锟斤拷氐锟斤拷锟斤拷梅锟斤拷锟�
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 锟斤拷锟斤拷锟斤拷应锟矫凤拷锟酵碉拷微锟脚碉拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷应锟斤拷锟斤拷锟斤拷锟截碉拷锟斤拷锟矫凤拷锟斤拷
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		finish();
	}
	
	private void goToGetMsg() {
		Intent intent = new Intent(this, GetFromWXActivity.class);
		intent.putExtras(getIntent());
		startActivity(intent);
		finish();
	}
	
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;		
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		
		StringBuffer msg = new StringBuffer(); // 锟斤拷织一锟斤拷锟斤拷锟斤拷示锟斤拷锟斤拷息锟斤拷锟斤拷
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);
		
		Intent intent = new Intent(this, ShowFromWXActivity.class);
		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
		startActivity(intent);
		finish();
	}
}