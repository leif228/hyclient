package com.eyunda.third.chat.mina;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import android.content.Intent;
import android.util.Log;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Status;
import com.eyunda.third.chat.event.BaseEvent;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.chat.event.MessageEvent;
import com.eyunda.third.chat.utils.MessageConstants;

public class MessageThread extends Thread {
	private long rc = new Date().getTime();
	private int lc = 0;

	public MessageThread() {
	}

	public void run() {
		while (true) {
			try {
				if (!MinaClientConn.getInstance().isConned()) {
					MinaClientConn.getInstance().connect();
				} else {
//					if (GlobalApplication.getInstance().getLoginStatus() == LoginStatusCode.noLogin) {
//						// 发送登录事件
//						MessageSender.getInstance().sendLoginEvent();
//					}
//					if (GlobalApplication.getInstance().getLoginStatus() == LoginStatusCode.logining && lc++ >= 3) {
//						lc = 0;
//						// 发送登录事件
//						MessageSender.getInstance().sendLoginEvent();
//					}
					if ((new Date().getTime() - rc) >= 60 * 1000L) {
						// 空闲30s，发送报到事件
						MessageSender.getInstance().sendReportEvent();
						rc = new Date().getTime();
					}
				}

				Thread.sleep(1000L);

				BaseEvent bv;
				while ((bv = MessageQueue.getInstance().poll()) != null) {

					if (MinaClientConn.getInstance().isConned()) {
						IoSession session = MinaClientConn.getInstance().getSession();

						WriteFuture writeResult = session.write(bv.toJson());
						writeResult.awaitUninterruptibly(3, TimeUnit.SECONDS);
						if (writeResult.isWritten()) {
							sendBroadcast(bv, Status.SUCCESS.toString());
							Log.e("发送消息成功:", bv.toJson());
						} else {
							if (bv.getMessageType() == MessageConstants.MESSAGE_EVENT
									|| bv.getMessageType() == MessageConstants.STATUS_EVENT
									|| bv.getMessageType() == MessageConstants.NOTIFY_EVENT) {

								MessageQueue.getInstance().offer(bv);

								Thread.sleep(1000L);

								if (!MinaClientConn.getInstance().isConned()) {
									MinaClientConn.getInstance().connect();
								}
							}
						}
					} else {
						if (bv.getMessageType() == MessageConstants.MESSAGE_EVENT
								|| bv.getMessageType() == MessageConstants.STATUS_EVENT
								|| bv.getMessageType() == MessageConstants.NOTIFY_EVENT) {

							MessageQueue.getInstance().offer(bv);

							Thread.sleep(1000L);

							if (!MinaClientConn.getInstance().isConned()) {
								MinaClientConn.getInstance().connect();
							}
						}
					}
				}
			} catch (Exception e) {
				// 出错立即重来。
			}
		}

	}

	/** 发广播通知聊天页面 */
	private void sendBroadcast(BaseEvent ev, String status) {

		if (ev.getMessageType() == MessageConstants.MESSAGE_EVENT) {
			MessageEvent messageEvent = new MessageEvent(ev.getEventMap());

			Intent intent = new Intent();
			intent.putExtra("msgid", messageEvent.getId());
			intent.putExtra("status", status);
			intent.setAction("android.intent.action.MessageToSendedBroadcast");
			GlobalApplication.getInstance().getApplicationContext().sendBroadcast(intent, null);
		}

	}
}
