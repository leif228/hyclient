package com.eyunda.third.adapters.chat.manager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.NewChatAllHistoryActivity;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.NotifyMessage;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.MessageEvent;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.chat.ChatRoomData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ChatRoomManager {
	private static ChatRoomManager instance = null;
	private LinkedHashMap<User, ChatRoom> chatRooms = new LinkedHashMap<User, ChatRoom>(); // 所有聊天室

	Data_loader data = new Data_loader();

	public static synchronized ChatRoomManager getInstance() {
		if (instance == null)
			instance = new ChatRoomManager();
		return instance;
	}

	public LinkedHashMap<User, ChatRoom> getChatRooms() {
		return chatRooms;
	}

	@SuppressWarnings("rawtypes")
	public void setChatRooms(LinkedHashMap<User, ChatRoom> crs) {
		// 可能内存中某个聊天室有聊天内容，将原来内容存入新地址
		if (!chatRooms.isEmpty()) {
			Iterator iter = chatRooms.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				ChatRoom u = (ChatRoom) entry.getValue();
				// 循环异步过来的
				Iterator i = crs.entrySet().iterator();
				while (i.hasNext()) {
					Map.Entry e = (Map.Entry) i.next();
					ChatRoom cr = (ChatRoom) e.getValue();
					if (u.getRoomName().equals(cr.getRoomName()))
						// cr.setMessages(u.getMessages());
						cr.setUnreadMsgCount(u.getUnreadMsgCount());
				}

			}
		}
		this.chatRooms = crs;
	}

	@SuppressWarnings("rawtypes")
	public ChatMessage getById(Long msgId) {
		Iterator iter = chatRooms.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ChatRoom u = (ChatRoom) entry.getValue();
			List<ChatMessage> msgs = u.getMessages();
			for (ChatMessage c : msgs) {
				if (c.getId().equals(msgId)) {
					return c;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public ChatRoom getLocalChatRoom(User toChatUser) {
		ChatRoom toUserChatRoom = null;

		Iterator iter = chatRooms.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			User u = (User) entry.getKey();
			if (u.getUserData().getId()
					.equals(toChatUser.getUserData().getId())) {
				toUserChatRoom = (ChatRoom) entry.getValue();
			}
		}
		return toUserChatRoom;
	}

	/** 有则用，没有去增加一个聊天室，规定先要有聊天室才可以发送消息 */
	@SuppressWarnings("rawtypes")
	public ChatRoom getChatRoom(User toChatUser) {
		try {
			UserData currentUser = GlobalApplication.getInstance()
					.getUserData();// 为登入用户
			ChatRoom toUserChatRoom = getLocalChatRoom(toChatUser);

			if (toUserChatRoom == null) {
				toUserChatRoom = new ChatRoom();
				toUserChatRoom.setToChatUser(toChatUser);
				toUserChatRoom.setCreateTime(Calendar.getInstance());

				if (currentUser.getId() < toChatUser.getUserData().getId()) {
					toUserChatRoom.setRoomName(currentUser.getId() + "_"
							+ toChatUser.getUserData().getId());
				} else {
					toUserChatRoom.setRoomName(toChatUser.getUserData().getId()
							+ "_" + currentUser.getId());
				}
				chatRooms.put(toChatUser, toUserChatRoom);
			}
			return toUserChatRoom;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 消息过来，有则用，没有就去取一个聊天室 */
	@SuppressWarnings("rawtypes")
	public ChatRoom toGetChatRoom(String roomName, Long userId) {

		ChatRoom chatRoom = null;
		Iterator iter = chatRooms.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			chatRoom = (ChatRoom) entry.getValue();
			if (chatRoom.getRoomName().equals(roomName)) {
				return chatRoom;
			}
		}
		chatRoom = toSGetCR(userId);
		if (chatRoom != null) {
			return chatRoom;
		}
		return null;
	}

	/** 有联系人不一定有聊天室，有聊天室一定有联系人(规定了要发送消息，得先建立一个聊天室，所以这里可以取得一个聊天室) */
	@SuppressWarnings("unchecked")
	private ChatRoom toSGetCR(Long userId) {
		Gson gson = new Gson();
		SynData_loader syn = new SynData_loader();
		Map<String, Object> map = new HashMap<String, Object>();

		User toUser = ContactManager.getInstance().getContact(userId);
		map.put("toUserId", userId);
		String result = syn.getApiResult("/mobile/chat/toGetChatRoom", map,
				"get");
		HashMap<String, Object> rmap = gson.fromJson((String) result,
				new TypeToken<Map<String, Object>>() {
				}.getType());

		if (rmap.get("returnCode").equals("Success")) {
			Map<String, Object> cmap = (Map<String, Object>) rmap
					.get("content");
			ChatRoomData chatRoomData = new ChatRoomData(
					(Map<String, Object>) cmap.get("chatRoomData"));
			if (toUser == null) {
				UserData userData = new UserData(
						(Map<String, Object>) cmap.get("userData"));
				toUser = new User();
				toUser.setUserData(userData);
			}

			ChatRoom chatRoom = new ChatRoom();
			chatRoom.setId(chatRoomData.getId());
			chatRoom.setRoomName(chatRoomData.getRoomName());
			chatRoom.setToChatUser(toUser);
			chatRoom.setRecentlyTitle(chatRoomData.getRecentlyTitle());
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(Long.valueOf(chatRoomData.getRecentlyTime()));
			chatRoom.setRecentlyTime(c);
			chatRooms.put(toUser, chatRoom);
			return chatRoom;
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void deleteChatRoom(final NewChatAllHistoryActivity activity,
			final ChatRoom delRoom) {
		NotifyMessage notify = delRoom.getNotifyMsg();
		if (notify != null) {
			// chatRooms去删除一个通知聊天室
			NotifyManager.getInstance().delNotify(activity, delRoom, notify);

		} else {
			// todo删除的为一个聊天消息的聊天室
			AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String content) {
					Gson gson = new Gson();
					HashMap<String, Object> rmap = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());
					if (rmap.get("returnCode").equals("Success")) {
						Iterator iter = chatRooms.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							ChatRoom c = (ChatRoom) entry.getValue();
							if (c.getNotifyMsg() == null) {
								if (c.getId().equals(delRoom.getId())) {
									iter.remove();
								}
							}
						}
						activity.refresh();
					} else {
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
			map.put("roomId", delRoom.getId());
			data.getApiResult(handler, "/mobile/chat/delChatRoom", map, "post");
		}
	}

	@SuppressWarnings("rawtypes")
	public void delOnlyNotifyChatRoom(ChatRoom notifyRoom, NotifyMessage notify) {
		Iterator iter = chatRooms.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ChatRoom c = (ChatRoom) entry.getValue();
			if (c.getNotifyMsg() != null) {
				if (c.getNotifyMsg().getId().equals(notify.getId())) {
					iter.remove();
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void delOnlyNotifyChatRoomById(Long notifyId) {
		Iterator iter = chatRooms.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ChatRoom c = (ChatRoom) entry.getValue();
			if (c.getNotifyMsg() != null) {
				if (c.getNotifyMsg().getId().equals(notifyId)) {
					iter.remove();
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void delChatRoomById(Long id) {
		Iterator iter = chatRooms.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ChatRoom c = (ChatRoom) entry.getValue();
			if (c.getId().equals(id))
				iter.remove();

		}
	}

	@SuppressWarnings("rawtypes")
	public ChatRoom getOrCreatCR(MessageEvent messageEvent) {
		Long toUserId = messageEvent.getToUserId();
		Long frUserId = messageEvent.getFromUserId();
		String roomName = null;
		if (toUserId > frUserId)
			roomName = frUserId + "_" + toUserId;
		else
			roomName = toUserId + "_" + frUserId;

		ChatRoom chatRoom = null;
		Iterator iter = chatRooms.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			chatRoom = (ChatRoom) entry.getValue();
			if (chatRoom.getRoomName().equals(roomName)) {
				return chatRoom;
			}
		}
		if (chatRoom == null) {
			chatRoom = new ChatRoom();
			User tu = new User();
			tu.getUserData().setId(frUserId);
			// tu.setUserLogo(messageEvent.getUserLogo());
			chatRoom.setToChatUser(tu);
			chatRoom.setRoomName(roomName);
			chatRooms.put(tu, chatRoom);
		}
		return chatRoom;
	}

}
