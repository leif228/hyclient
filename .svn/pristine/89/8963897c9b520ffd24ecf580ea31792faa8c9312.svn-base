package com.eyunda.third.chat;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.chat.NewChatAllHistoryActivity;
import com.eyunda.third.activities.chat.NotifyDetailActivity;
import com.eyunda.third.adapters.chat.domain.NotifyMessage;
import com.eyunda.third.adapters.chat.manager.NotifyManager;
import com.eyunda.third.chat.event.NotifyEvent;
import com.eyunda.third.chat.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;

public class Notifier {
	private static final String LOGTAG = LogUtil.makeLogTag(Notifier.class);

	private static final Random random = new Random(System.currentTimeMillis());

	private Context context;
	

	private SharedPreferences sharedPrefs;
	private NotificationManager notificationManager;

	public Notifier(Context context) {
		this.context = context;
		this.sharedPrefs = context.getSharedPreferences(
				ApplicationConstants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		this.notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@SuppressWarnings("deprecation")
	public void chatMsgNotify() {
		int icon = getNotificationIcon();
		long when = System.currentTimeMillis();
			
			
			Intent intent = new Intent(context, NewChatAllHistoryActivity.class);
//			设置只有一个聊天消息提醒在通知栏
//			intent.putExtra("hadOne", true);
			intent.putExtra("notifyGoto", true);
			
			
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			Notification notification = new Notification();
			notification.icon = icon;
			notification.defaults = Notification.DEFAULT_LIGHTS;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.when = when;
			notification.tickerText = "易运达";
			notification.setLatestEventInfo(context, "易运达", "您有未读消息!", contentIntent);
			notificationManager.notify(random.nextInt(), notification);
	}

	@SuppressWarnings("deprecation")
	public void notify(String message) {
		try {
			Gson gson = new Gson();
			Map<String, String> eventmap = gson.fromJson(message,
					new TypeToken<Map<String, String>>() {
					}.getType());

			if (eventmap != null && !eventmap.isEmpty()) {
				NotifyEvent event = new NotifyEvent(eventmap);

				Long notifyId = event.getId();
				String title = event.getTitle();
				String content = event.getContent();
				// 存内存
				NotifyMessage nm = new NotifyMessage();
				nm.setId(notifyId);
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(event.getDateTime());
				nm.setCreateTime(c);
				nm.setMessage(content);
				nm.setTitle(title);
				nm.setReceiverId(event.getToUserId());
				nm.setSenderId(event.getFromUserId());
				NotifyManager.getInstance().addNotify(nm);

				// 发通知
				int icon = getNotificationIcon();
				long when = event.getDateTime();
				Intent intent = new Intent(context,
						NotifyDetailActivity.class).putExtra("notifyId", nm.getId())
													.putExtra("notifyTitle", title)
													.putExtra("notifyContent", content);
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, random.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

				Notification notification = new Notification();
				notification.icon = icon;
				notification.defaults = Notification.DEFAULT_LIGHTS;
				notification.defaults |= Notification.DEFAULT_SOUND;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.when = when;
				notification.tickerText = title;
				notification.setLatestEventInfo(context, title, content,
						contentIntent);
				notificationManager.notify(random.nextInt(), notification);

				// 要android api为16用的
				// Notification notify=new Notification.Builder(context)
				// .setAutoCancel(true)
				// .setTicker("有新消息")
				// .setSmallIcon(icon)
				// .setContentTitle("一条新通知")
				// .setContentText(message)
				// .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)
				// .setWhen(when)
				// .setContentIntent(contentIntent)
				// .build();
				// notificationManager.notify(random.nextInt(),notify);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getNotificationIcon() {
		return sharedPrefs.getInt(ApplicationConstants.NOTIFICATION_ICON,
				R.drawable.zd_icon);
	}

}
