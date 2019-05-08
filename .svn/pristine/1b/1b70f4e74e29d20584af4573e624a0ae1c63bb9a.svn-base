package com.eyunda.third.activities.chat;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.chat.event.NotifyEvent;
import com.eyunda.third.chat.mina.MessageSender;
import com.eyunda.third.chat.utils.MessageConstants;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.LoginSourceCode;
import com.eyunda.third.domain.enumeric.MassNotify;
import com.eyunda.third.domain.enumeric.NotifyTypeCode;
import com.eyunda.tools.DateUtils;
import com.hy.client.R;

public class SendNotifyActivity extends CommonListActivity implements
		OnClickListener {
	private Button basic_submit, basic_back;
	private EditText title, to_name, content;
	private String to = "";
	private String toUserName = "";
	private String msgType = "";
	private String contentStr = "";
	private String titleStr = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_chat_activity_sendnotify);

		title = (EditText) this.findViewById(R.id.title);
		to_name = (EditText) this.findViewById(R.id.to_name);
		content = (EditText) this.findViewById(R.id.content);
		basic_submit = (Button) this.findViewById(R.id.basic_submit);
		basic_back = (Button) this.findViewById(R.id.basic_back);

		basic_submit.setOnClickListener(this);
		basic_back.setOnClickListener(this);

		// to = dlr, cyr, tyr;
		// to = toUserId;
		to = getIntent().getStringExtra("to");
		toUserName = getIntent().getStringExtra("toUserName");

		if ("dlr".equals(to)) {
//			to_name.setText("所有代理人");
			to_name.setText("所有会话 ");
			to_name.setEnabled(false);
		} 
		else if ("ower".equals(to)) {
			to_name.setText("所有货主");
			to_name.setEnabled(false);
		}
		else if ("master".equals(to)) {
			to_name.setText("所有船东");
			to_name.setEnabled(false);
		}
		else if ("tyr".equals(to)) {
//			to_name.setText("所有托运人");
			to_name.setText("所有子账号");
			to_name.setEnabled(false);
		} else {
			to_name.setText(toUserName);
			to_name.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.basic_submit:
			boolean pass = check();
			if (!pass) {
				return;
			}

			if ("dlr".equals(to)) {
				msgType = MassNotify.allfriends.toString();
				sendNotify(msgType);
			} 
			else if ("ower".equals(to)) {
				msgType = MassNotify.allowner.toString();
				sendNotify(msgType);
			} 
			else if ("master".equals(to)) {
				msgType = MassNotify.allmaster.toString();
				sendNotify(msgType);
			} 
			else if ("tyr".equals(to)) {
				msgType = MassNotify.allchildren.toString();
				sendNotify(msgType);
			} else {
				msgType = to;
				sendNotify(msgType);
			}
			Toast.makeText(this, "通知已发送", 0).show();
			finish();
			break;
		case R.id.basic_back:
			finish();
			break;
		default:
			break;
		}
	}

	private boolean check() {
		titleStr = title.getText().toString().trim();
		contentStr = content.getText().toString().trim();
		if ("".equals(titleStr)) {
			Toast.makeText(this, "通知标题不能为空", 0).show();
			return false;
		} else if ("".equals(contentStr)) {
			Toast.makeText(this, "内容不能为空", 0).show();
			return false;
		}
		return true;
	}

	private void sendNotify(String msgt) {
		Map<String, String> map = new HashMap<String, String>();
		NotifyEvent ne = new NotifyEvent(map);
		ne.setContent(contentStr);
		ne.setLoginSource(LoginSourceCode.mobile);
		ne.setTitle(titleStr);
		ne.setMessageType(MessageConstants.NOTIFY_EVENT);
		
		UserData data = GlobalApplication.getInstance().getUserData();
		if (data != null) {
			ne.setFromUserId(data.getId());
			if(!"".equals(data.getNickName()))
				ne.setFromUserName(data.getNickName());
			else if(!"".equals(data.getTrueName()))
				ne.setFromUserName(data.getTrueName());
			else if(!"".equals(data.getLoginName()))
				ne.setFromUserName(data.getLoginName());
		}
		if ("dlr".equals(to)) {
			ne.setMsgType(NotifyTypeCode.allfriends.toString());
		} 
		else if ("ower".equals(to)) {
			ne.setMsgType(NotifyTypeCode.allowner.toString());
		}
		else if ("master".equals(to)) {
			ne.setMsgType(NotifyTypeCode.allmaster.toString());
		}
		else if ("tyr".equals(to)) {
			ne.setMsgType(NotifyTypeCode.allchildren.toString());
		} else {
			ne.setMsgType(NotifyTypeCode.friend.toString());
			ne.setToUserId(Long.valueOf(to));
			ne.setToUserName(toUserName);
		}
		//内容增加（时间、发送者）信息
		contentStr = contentStr+"("+ DateUtils.getTime("yyyy-MM-dd HH:mm") + "发送者:" + ne.getFromUserName() +")";
		ne.setContent(contentStr);
		
		MessageSender.getInstance().sendNotifyEvent(ne);
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("发送通知");
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
