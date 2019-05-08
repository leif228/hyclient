package com.eyunda.third.chat.mina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Type;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ChatManager;
import com.eyunda.third.adapters.chat.manager.ChatRoomManager;
import com.eyunda.third.adapters.chat.manager.ContactManager;
import com.eyunda.third.chat.Notifier;
import com.eyunda.third.chat.event.BaseEvent;
import com.eyunda.third.chat.event.LoginEvent;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.chat.event.MessageEvent;
import com.eyunda.third.chat.event.ReportEvent;
import com.eyunda.third.chat.event.StatusEvent;
import com.eyunda.third.chat.utils.LogUtil;
import com.eyunda.third.chat.utils.MessageConstants;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MinaClientHandle extends IoHandlerAdapter {
	private static final String LOGTAG = LogUtil.makeLogTag(MinaClientHandle.class);
	private Context context;

	public MinaClientHandle() {
		context = GlobalApplication.getInstance().getApplicationContext();
	}

	public void messageReceived(IoSession session, Object message) throws Exception {
		Log.d(LOGTAG, "客户端接收一个信息：" + message.toString());
		Gson gson = new Gson();
		HashMap<String, String> map = gson.fromJson((String) message, new TypeToken<Map<String, String>>() {
		}.getType());

		BaseEvent baseEvent = new BaseEvent(map);
		String messageType = baseEvent.getMessageType();
		if (messageType != null) {
			if (MessageConstants.LOGIN_EVENT.equals(messageType)) {
				try {
					LoginEvent event = new LoginEvent(baseEvent.getEventMap());
					Log.e(LOGTAG, "=LOGIN_EVENT过来的sessionId为:" + event.getSessionId());

					UserData userData = new UserData();
					userData.setId(event.getUserId());
					userData.setUserType(event.getUserType());
					userData.setLoginName(event.getLoginName());
					userData.setTrueName(event.getTrueName());
					userData.setNickName(event.getNickName());
					userData.setEmail(event.getEmail());
					userData.setMobile(event.getMobile());
					userData.setBindingCode(event.getBindingCode());
					userData.setSimCardNo(event.getSimCardNo());
					userData.setUserLogo(event.getUserLogo());
					userData.setSignature(event.getSignature());
					userData.setStamp(event.getStamp());
					userData.setShips(event.getShips());
					
					userData.setOnlineStatus(event.getOnlineStatus());
					userData.setApplyStatus(event.getApplyStatus());
					
					userData.setSessionId(event.getSessionId());
					userData.setAddress(event.getAddress());
					userData.setUnitAddr(event.getUnitAddr());
					userData.setAreaCode(event.getAreaCode());
					userData.setPostCode(event.getPostCode());
					userData.setUnitName(event.getUnitName());
					
					userData.setRealUser(event.isRealUser());
					userData.setCreatSite(event.isCreatSite());
					userData.setRoles(event.getRoles());
					if (userData.getRoles() != null && !"".equals(userData.getRoles())) {
						List<UserRoleCode> rcs = new ArrayList<UserRoleCode>();
						String rDesc = "";
						String[] rs = userData.getRoles().split(",");
						for (String r : rs) {
							int ri = Integer.valueOf(r);
							UserRoleCode rc = UserRoleCode.values()[ri];
							rcs.add(rc);
							rDesc += rc.getDescription() + ",";
						}
						if (!"".equals(rDesc))
							rDesc = rDesc.substring(0, rDesc.length() - 1);
						
						userData.setShortRoleDesc(rcs.get(0).getDescription());
						userData.setRoleDesc(rDesc);
						userData.setRoleCodes(rcs);

						if (rcs.contains(UserRoleCode.handler) || rcs.contains(UserRoleCode.sailor))
							userData.setChildUser(true);
					}

					GlobalApplication.getInstance().setUserData(userData);
					GlobalApplication.getInstance().setLoginStatus(LoginStatusCode.logined);
				} catch (Exception e) {
					Log.e(LOGTAG, "mina程序异常:" + e.getMessage());
				}
			} else if (MessageConstants.LOGOUT_EVENT.equals(messageType)) {

			} else if (MessageConstants.REPORT_EVENT.equals(messageType)) {
				ReportEvent event = new ReportEvent(baseEvent.getEventMap());
				Log.e(LOGTAG, "=REPORT_EVENT过来的sessionId为:" + event.getSessionId());
				GlobalApplication.getInstance().getUserData().setSessionId(event.getSessionId());
			} else if (MessageConstants.MESSAGE_EVENT.equals(messageType)) {
				try {
					MessageEvent messageEvent = new MessageEvent(baseEvent.getEventMap());
					String roomName = "";
					Long fuserId = messageEvent.getFromUserId();
					Long tuserId = messageEvent.getToUserId();
					if (fuserId > tuserId)
						roomName = tuserId + "_" + fuserId;
					else
						roomName = fuserId + "_" + tuserId;

					// 1.先将聊天消息存入内存
					ChatRoom chatRoom = ChatRoomManager.getInstance().toGetChatRoom(roomName, fuserId);

					if (chatRoom != null) {
						ChatMessage cm = ChatManager.getInstance().getChatMessage(messageEvent);
						if (cm == null)
							return;
						chatRoom.addMessage(cm);// 更新正在聊天的页面
						if (cm.getType().equals(Type.TXT))
							chatRoom.setRecentlyTitle(cm.getContent());// 这里是更新聊天室页面
						else if (cm.getType().equals(Type.IMAGE))
							chatRoom.setRecentlyTitle("[图片]");
						else if (cm.getType().equals(Type.VOICE))
							chatRoom.setRecentlyTitle("[语音]");
						else
							chatRoom.setRecentlyTitle("[文件]");

						chatRoom.setRecentlyTime(cm.getCreateTime());
						chatRoom.addUnreadMsgCount();
						// 2.再发送广播，在广播处理中直接去内存中取消息
						Intent intent = new Intent();
						intent.putExtra("msgId", messageEvent.getId());
						intent.putExtra("roomName", roomName);
						intent.setAction("android.intent.action.NewMessageBroadcast");
						context.sendOrderedBroadcast(intent, null);

						if (!GlobalApplication.getInstance().isChatMsgNotifyed()) {
							// 来消息时提醒(设置只有一个提醒在通知栏)
							if (!GlobalApplication.getInstance().isHaveOneChatMsgNotify()) {
								GlobalApplication.getInstance().setHaveOneChatMsgNotify(true);
								Notifier notifier = new Notifier(context);
								notifier.chatMsgNotify();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (MessageConstants.NOTIFY_EVENT.equals(messageType)) {

				try {
					// 发广播
					String msg = (String) message;
					Notifier notifier = new Notifier(context);
					notifier.notify(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (MessageConstants.STATUS_EVENT.equals(messageType)) {
				try {
					StatusEvent statusEvent = new StatusEvent(baseEvent.getEventMap());
					User user = ContactManager.getInstance().getContact(statusEvent.getFromUserId());
					if (user != null) {
						user.getUserData().setOnlineStatus(statusEvent.getOnlineStatus());
						// 发送广播，通知页面更新
						Intent intent = new Intent();
						intent.setAction("android.intent.action.UserOnlineStatusBroadcast");
						context.sendBroadcast(intent, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void messageSent(IoSession session, Object message) throws Exception {
		Log.d(LOGTAG, "客户端已经发了一个信息：" + message.toString());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
		MinaClientConn.getInstance().setConned(false);
		Log.d(LOGTAG, "服务器启动发生异常，have a exception : " + arg1.toString());
	}
}
