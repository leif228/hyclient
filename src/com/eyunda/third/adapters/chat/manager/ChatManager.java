package com.eyunda.third.adapters.chat.manager;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import android.util.Log;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Direct;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Status;
import com.eyunda.third.adapters.chat.domain.ChatMessage.Type;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.MessageEvent;
import com.eyunda.third.chat.mina.MessageSender;
import com.eyunda.third.chat.utils.MessageConstants;
import com.eyunda.third.domain.chat.ChatMessageData;
import com.eyunda.third.domain.enumeric.LoginSourceCode;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ChatManager {
	private static ChatManager instance = null;
	 Data_loader data=new Data_loader();

	public static synchronized ChatManager getInstance() {
		if (instance == null)
			instance = new ChatManager();
		return instance;
	}

	public void removeMessage(final ChatRoom chatroom,final ChatMessage msg,final ChatActivity chatActivity) {
		if(chatroom==null)
			return;
		//内存中删除
		chatroom.removeMessage(msg);
		chatActivity.getRhandler().sendEmptyMessage(chatActivity.REF);
		// 更新数据库，msg增加以删标记，下次显视时判断,
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				Log.e("聊天信息",content );
				Gson gson = new Gson();
				 HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());
				if (map.get("returnCode").equals("Success")) {
//					chatroom.removeMessage(msg);
//					chatActivity.getRhandler().sendEmptyMessage(chatActivity.REF);
				}else{
//					Toast.makeText(chatActivity, "删除失败",
//							Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(chatActivity, "网络连接异常",
//							Toast.LENGTH_SHORT).show();
			}
		};
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roomName", msg.getRoomName());
		map.put("msgId", msg.getId());
		data.getApiResult(handler,"/mobile/chat/delChatMsg", map, "post");
		
		
	}

//	public ChatMessage addMessage(ChatMessage msg) {
//		// todo数据库增加一条记录
//
//		Gson gson = new Gson();
//
//		SynData_loader syn = new SynData_loader();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("roomName", msg.getRoomName());
//		map.put("receiverId", msg.getReceiverId());
//		map.put("senderId", msg.getSenderId());
//		map.put("content", msg.getContent());
//		String result = syn
//				.getApiResult("/mobile/chat/addChatMsg", map, "post");
//		HashMap<String, Object> rmap = gson.fromJson((String) result,
//				new TypeToken<Map<String, Object>>() {
//				}.getType());
//		if (rmap == null) {
//			return null;
//		}
//		if (rmap.get("returnCode").equals("Success")) {
//			ChatMessageData chatMessageData = new ChatMessageData(
//					(Map<String, Object>) rmap.get("content"));
//			msg.setId(chatMessageData.getId());
//			msg.setCreateTime(Calendar.getInstance());
//			return msg;
//		} else {
//			// 数据库增加失败
//			return null;
//		}
//
//	}

	public ChatMessageData toChang(ChatMessage chatMessage) {
		ChatMessageData chatMessageData = new ChatMessageData();
		chatMessageData.setContent(chatMessage.getContent());
		chatMessageData.setCreateTime(String.valueOf(chatMessage
				.getCreateTime().getTime().getTime()));
		chatMessageData.setFstDel(chatMessage.isFstDel());
		chatMessageData.setId(chatMessage.getId());
		chatMessageData.setReadStatus(chatMessage.getReadStatus());
		chatMessageData.setReceiverId(chatMessage.getReceiverId());
		chatMessageData.setRoomName(chatMessage.getRoomName());
		chatMessageData.setSenderId(chatMessage.getSenderId());
		chatMessageData.setSndDel(chatMessage.isSndDel());
		return chatMessageData;
	}

	public ChatMessage toChangLocal(ChatMessageData chatMessageData) {
		ChatMessage cm = new ChatMessage();

		cm.setContent(chatMessageData.getContent());
		cm.setFstDel(chatMessageData.getFstDel());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.valueOf(chatMessageData.getCreateTime()));
		cm.setCreateTime(c);
		cm.setId(chatMessageData.getId());
		cm.setReadStatus(chatMessageData.getReadStatus());
		cm.setReceiverId(chatMessageData.getReceiverId());
		cm.setRoomName(chatMessageData.getRoomName());
		cm.setSenderId(chatMessageData.getSenderId());
		cm.setSndDel(chatMessageData.getSndDel());
		if (chatMessageData.getReceiverId().equals(
				GlobalApplication.getInstance().getUserData().getId()))
			cm.setDirect(ChatMessage.Direct.RECEIVE);
		else
			cm.setDirect(ChatMessage.Direct.SEND);
		//分类处理
		if(chatMessageData.getContent().startsWith(Type.IMAGE.toString())){
			cm.setType(Type.IMAGE);
			cm.setStatus(Status.SUCCESS);
		}else if(chatMessageData.getContent().startsWith(Type.FILE.toString())){
			cm.setType(Type.FILE);
			cm.setStatus(Status.SUCCESS);
		}else if(chatMessageData.getContent().startsWith(Type.VOICE.toString())){
			if(cm.getDirect()==ChatMessage.Direct.RECEIVE){
				String voice = chatMessageData.getContent();
				String[] arr = voice.split(":");
				final String voiceName = arr[3];
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/eyunda/download/voice/" + voiceName);
				if(file!=null&&file.exists()){
					cm.setStatus(Status.SUCCESS);
				}else{
					cm.setStatus(Status.INPROGRESS);
					cm.isAcked=false;//设置为未读过
				}
			}else{
				cm.setStatus(Status.SUCCESS);
			}
			cm.setType(Type.VOICE);
		}else
			cm.setType(Type.TXT);
		
		return cm;
	}

	public ChatMessage getChatMessage(MessageEvent messageEvent) {
		try {
			ChatMessage cm=new ChatMessage();
			cm.setId(messageEvent.getId());
			if(messageEvent.getContent().startsWith(Type.IMAGE.toString())){
				cm.setType(Type.IMAGE);
				cm.setStatus(Status.SUCCESS);
			}else if(messageEvent.getContent().startsWith(Type.FILE.toString())){
				//判断同名文件是否存在，如果存在就删除原文件
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/eyunda/download/file/");
				if (f.exists()) {
					String content = messageEvent.getContent();
					content = content.substring(ChatMessage.Type.FILE.toString().length());
					final String[] arr = content.split(":");
					File file = new File(Environment.getExternalStorageDirectory()
							+ "/eyunda/download/file/" + arr[2]);
					if(file.exists())
						file.delete();
				}
				cm.setType(Type.FILE);
				cm.setStatus(Status.SUCCESS);
			}
			else if(messageEvent.getContent().startsWith(Type.VOICE.toString())){
				cm.setType(Type.VOICE);
				cm.setStatus(Status.INPROGRESS);
				cm.isAcked=false;//设置mina过来的消息为未读消息
			}else
				cm.setType(Type.TXT);
			cm.setContent(messageEvent.getContent());
			Calendar c=Calendar.getInstance();
			c.setTimeInMillis(messageEvent.getDateTime());
			cm.setCreateTime(c);
			cm.setRoomName(messageEvent.getChatRoomName());
			cm.setDirect(Direct.RECEIVE);
			cm.setSenderId(messageEvent.getFromUserId());
			cm.setReceiverId(messageEvent.getToUserId());
			
			return cm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
