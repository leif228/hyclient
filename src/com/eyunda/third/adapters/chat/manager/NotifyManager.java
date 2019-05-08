package com.eyunda.third.adapters.chat.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.chat.NewChatAllHistoryActivity;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.NotifyMessage;
import com.eyunda.third.adapters.chat.widget.CalendarUtil;
import com.eyunda.third.domain.order.MessageData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.util.http.AsyncHttpResponseHandler;

public class NotifyManager {
	private static NotifyManager instance = null;

	private List<NotifyMessage> notifys = new ArrayList<NotifyMessage>();// 对外使用
	Data_loader data = new Data_loader();

	public static synchronized NotifyManager getInstance() {
		if (instance == null)
			instance = new NotifyManager();
		return instance;
	}

	public List<NotifyMessage> getNotifys() {
		return notifys;
	}

	public void setNotifys(List<NotifyMessage> notifys) {
		
		this.notifys = notifys;
	}
	public NotifyMessage getById(Long notifyId){
		if(!notifys.isEmpty()){
			for(NotifyMessage nm:notifys){
				if(nm.getId().equals(notifyId))
					return nm;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void addNotify(NotifyMessage msg) {
//		boolean had=false;
//		for(NotifyMessage n:notifys){
//			if(n.getId().equals(msg.getId())){
//				had=true;
//				break;
//			}
//		}
//		if(!had)
			notifys.add(msg);
	}

	/** 去服务器删除一条通知消息 */
	public void delNotify(final NewChatAllHistoryActivity activity,
			final ChatRoom delRoom, final NotifyMessage notify) {
		final DialogUtil dialogUtil = new DialogUtil(activity);
		Long notifyId = notify.getId();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("消息删除中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> rmap = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());
				if (rmap.get("returnCode").equals("Success")) {
					NotifyManager.getInstance().delLocalNotify(notify);
					ChatRoomManager.getInstance().delOnlyNotifyChatRoom(
							delRoom, notify);
					activity.refresh();
				}else{
					activity.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(activity, "删除失败",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					activity.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(activity, "网络连接异常",
									Toast.LENGTH_SHORT).show();
						}
					});
			}
		};
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", notifyId);
		data.getApiResult(handler, "/mobile/message/delete", map, "post");
	}

	/**
	 * 登入后，异步去取通知消息；同异步去取聊天室前15条消息一样处理
	 * 
	 * @param chatAllHistoryActivity
	 */
	ProgressDialog dialog;
	public void getServerNotifys(
			final NewChatAllHistoryActivity chatAllHistoryActivity) {
		final DialogUtil dialogUtil = new DialogUtil(chatAllHistoryActivity);
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("消息加载中", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				try {
					dialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson((String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
							boolean contentMD5Changed = (Boolean) map.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils("/mobile/message/myMessage",null);
							if(contentMD5Changed&&NetworkUtils.isNetworkAvailable()){
								s.setParam(content);
							}else{
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion, new TypeToken<Map<String, Object>>() {
								}.getType());
							}
						}
						
						Map<String, Object> cmap = (Map<String, Object>) map
								.get("content");
						List<Map<String, Object>> messageDatas = (List<Map<String, Object>>) cmap
								.get("messageDatas");
						List<NotifyMessage> nms = new ArrayList<NotifyMessage>();
						if (messageDatas != null && messageDatas.size() > 0) {
							for (int i = 0; i < messageDatas.size(); i++) {
								MessageData data = new MessageData(
										messageDatas.get(i));
								NotifyMessage nm = toChang(data);
								nms.add(nm);
							}
							NotifyManager.getInstance().setNotifys(nms);
						}
						chatAllHistoryActivity.refresh();
					} else {
						Log.i("通知", "通知从服务器取得失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
			}
		};
		data.getApiResult(handler, "/mobile/message/myMessage");
	}

	public NotifyMessage toChang(MessageData data) {
		NotifyMessage nm = new NotifyMessage();
		// Calendar c = Calendar.getInstance();
		// c.setTimeInMillis(Long.valueOf(data.getCreateTime()));
		nm.setCreateTime(CalendarUtil.parse(data.getCreateTime()));
		nm.setHandleStatus(data.getHandleStatus());
		nm.setId(data.getId());
		nm.setMessage(data.getMessage());
		nm.setMsgType(data.getMsgType());
		nm.setReadStatus(data.getReadStatus());
		nm.setReceiverId(data.getReceiverId());
		nm.setSenderId(data.getSenderId());
		nm.setTitle(data.getTitle());

		return nm;
	}

	public void clearNotify() {
		notifys.clear();
	}

	/** 内存中删除一条消息 */
	@SuppressWarnings("rawtypes")
	public void delLocalNotify(NotifyMessage notify) {
		Iterator ite = notifys.iterator();
		while (ite.hasNext()) {
			NotifyMessage cmsg = (NotifyMessage) ite.next();
			if (cmsg.getId().equals(notify.getId()))
				ite.remove();
		}
	}
	/** 内存中删除一条消息 */
	@SuppressWarnings("rawtypes")
	public void delLocalNotifyById(Long notifyId) {
		Iterator ite = notifys.iterator();
		while (ite.hasNext()) {
			NotifyMessage cmsg = (NotifyMessage) ite.next();
			if (cmsg.getId().equals(notifyId))
				ite.remove();
		}
	}
}
