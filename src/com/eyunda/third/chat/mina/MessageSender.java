package com.eyunda.third.chat.mina;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.BaseEvent;
import com.eyunda.third.chat.event.LoginEvent;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.chat.event.LogoutEvent;
import com.eyunda.third.chat.event.MessageEvent;
import com.eyunda.third.chat.event.NotifyEvent;
import com.eyunda.third.chat.event.OnlineStatusCode;
import com.eyunda.third.chat.event.ReportEvent;
import com.eyunda.third.chat.event.StatusEvent;
import com.eyunda.third.chat.utils.MessageConstants;
import com.eyunda.third.chat.utils.SIMCardInfo;
import com.eyunda.third.domain.enumeric.LoginSourceCode;

public class MessageSender {
	public static MessageSender instance = null;

	private MessageSender() {
	}

	public static MessageSender getInstance() {
		if (instance == null)
			instance = new MessageSender();

		return instance;
	}

	public synchronized void send(BaseEvent ev) {
		MessageQueue.getInstance().offer(ev);
	}

	public void sendLogoutEvent() {

		Map<String, String> map = new HashMap<String, String>();

		LogoutEvent event = new LogoutEvent(map);
		if (GlobalApplication.getInstance().getUserData() != null) {
			event.setDateTime(new Date().getTime());
			event.setLoginName(GlobalApplication.getInstance().getUserData().getLoginName());
			event.setLoginSource(LoginSourceCode.mobile);
			event.setMessageType(MessageConstants.LOGOUT_EVENT);
			event.setNickName(GlobalApplication.getInstance().getUserData().getNickName());
			event.setTrueName(GlobalApplication.getInstance().getUserData().getTrueName());
			event.setUserId(GlobalApplication.getInstance().getUserData().getId());
			event.setSessionId(GlobalApplication.getInstance().getUserData().getSessionId());

			send(event);
			
			GlobalApplication.getInstance().setUserData(null);
			GlobalApplication.getInstance().setLoginStatus(LoginStatusCode.noLogin);
		}
	}

	public void sendReportEvent() {
		Map<String, String> rmap = new HashMap<String, String>();
		ReportEvent re = new ReportEvent(rmap);
		if (GlobalApplication.getInstance().getLoginStatus() == LoginStatusCode.logined) {
			re.setDateTime(new Date().getTime());
			re.setLoginName(GlobalApplication.getInstance().getUserData().getLoginName());
			re.setSimNo(SIMCardInfo.getInstance(GlobalApplication.getInstance()).getSimCardNumber());
			re.setLoginSource(LoginSourceCode.mobile);
			re.setMessageType(MessageConstants.REPORT_EVENT);
			re.setNickName(GlobalApplication.getInstance().getUserData().getNickName());
			re.setTrueName(GlobalApplication.getInstance().getUserData().getTrueName());
			re.setUserId(GlobalApplication.getInstance().getUserData().getId());
			re.setSessionId(GlobalApplication.getInstance().getUserData().getSessionId());

			send(re);
		}
	}

	public void sendLoginEvent() {
		Map<String, String> map = new HashMap<String, String>();
		LoginEvent event = new LoginEvent(map);

		Context ctx = GlobalApplication.getInstance();
		SharedPreferences sp = ctx.getSharedPreferences("eyundaBindingCode", Context.MODE_PRIVATE);
		String bindingCode = sp.getString("bindingCode", "");

		String simCardNo = SIMCardInfo.getInstance(ctx).getSimCardNumber();

		if ("".equals(bindingCode)) {
			GlobalApplication.getInstance().setUserData(null);
			GlobalApplication.getInstance().setLoginStatus(LoginStatusCode.noLogin);
		} else {
//			GlobalApplication.getInstance().setUserData(null);
//			GlobalApplication.getInstance().setLoginStatus(LoginStatusCode.logining);

			event.setBindingCode(bindingCode);
			event.setSimCardNo(simCardNo);

			event.setDateTime(new Date().getTime());
			event.setLoginSource(LoginSourceCode.mobile);
			event.setMessageType(MessageConstants.LOGIN_EVENT);
			// 创建一个session发送一个登入事件(LoginEvent表示新建一个session)
			send(event);
		}
	}

	public void sendMessageEvent(ChatMessage message, User toChatUser) {
		Map<String, String> map = new HashMap<String, String>();
		MessageEvent msgEvent = new MessageEvent(map);
		if (GlobalApplication.getInstance().getUserData() != null) {
			msgEvent.setMessageType(MessageConstants.MESSAGE_EVENT);
			msgEvent.setDateTime(new Date().getTime());
			msgEvent.setContent(message.getContent());
			msgEvent.setFromUserId(message.getSenderId());
			msgEvent.setToUserId(message.getReceiverId());
			msgEvent.setLoginSource(LoginSourceCode.mobile);
			msgEvent.setChatRoomName(message.getRoomName());
			msgEvent.setId(message.getId());

			msgEvent.setFromUserName(GlobalApplication.getInstance().getUserData().getNickName());
			msgEvent.setToUserName(toChatUser.getUserData().getNickName());

			send(msgEvent);
		}
	}

	public void sendStatusEvent(OnlineStatusCode osc) {
		Map<String, String> map = new HashMap<String, String>();
		StatusEvent event = new StatusEvent(map);
		if (GlobalApplication.getInstance().getUserData() != null) {
			event.setDateTime(new Date().getTime());
			event.setLoginSource(LoginSourceCode.mobile);
			event.setMessageType(MessageConstants.STATUS_EVENT);
			event.setSessionId(GlobalApplication.getInstance().getUserData().getSessionId());
			event.setFromUserId(GlobalApplication.getInstance().getUserData().getId());
			event.setFromUserName(GlobalApplication.getInstance().getUserData().getNickName());

			event.setOnlineStatus(osc);

			send(event);
		}
	}
	
	public void sendNotifyEvent(NotifyEvent ne){
		if (GlobalApplication.getInstance().getUserData() != null) {
			send(ne);
		}
	}
}
