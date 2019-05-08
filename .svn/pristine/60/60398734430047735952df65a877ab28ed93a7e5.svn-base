package com.eyunda.third.adapters.chat;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.NotifyMessage;
import com.eyunda.third.adapters.chat.widget.DateUtils;
import com.eyunda.third.adapters.chat.widget.SmileUtils;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.enumeric.OrderStatusCode;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 显示所有聊天记录adpater
 * 
 */
public class ChatAllHistoryAdapter extends ArrayAdapter<ChatRoom> {

	private LayoutInflater inflater;
	ImageLoader mImageLoader;

	public ChatAllHistoryAdapter(Context context, int textViewResourceId,
			List<ChatRoom> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.eyd_chat_row_chat_history,
					parent, false);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView
					.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.msgState = convertView.findViewById(R.id.msg_state);
			holder.list_item_layout = (RelativeLayout) convertView
					.findViewById(R.id.list_item_layout);
			convertView.setTag(holder);
		}
		if (position % 2 == 0) {
			holder.list_item_layout
					.setBackgroundResource(R.drawable.eyd_chat_mm_listitem);
		} else {
			holder.list_item_layout
					.setBackgroundResource(R.drawable.eyd_chat_mm_listitem_grey);
		}

		// 获取与此用户的会话
		ChatRoom conversation = getItem(position);

		NotifyMessage notifyMsg = conversation.getNotifyMsg();
		if (notifyMsg != null) {
			holder.avatar.setImageResource(R.drawable.zd_icon);
// 分类通知显示，不同通知不同图标
//			if (notifyMsg.getMsgType() == OrderStatusCode.endsign) {
//				holder.avatar.setImageResource(R.drawable.ic_launcher);
//			}
//			if (notifyMsg.getMsgType() == OrderStatusCode.refund) {
//				holder.avatar.setImageResource(R.drawable.eyd_chat_pay);
//			}
//			if (notifyMsg.getMsgType() == OrderStatusCode.approval) {
//				holder.avatar.setImageResource(R.drawable.eyd_chat_pinja);
//			}
//			if (notifyMsg.getMsgType() == OrderStatusCode.transmoney) {
//				holder.avatar.setImageResource(R.drawable.eyd_chat_fenzang);
//			}
//			if (notifyMsg.getMsgType() == OrderStatusCode.edit) {
//				holder.avatar.setImageResource(R.drawable.eyd_chat_firstpay);
//			}
			holder.name.setText(notifyMsg.getTitle());
			holder.message.setText(notifyMsg.getMessage());
			holder.time.setText(DateUtils.getTimestampString(notifyMsg
					.getCreateTime().getTime()));
		} else {
			// 获取用户username
			String username = conversation.getToChatUser().getUserData().getNickName();
			// 本地或者服务器获取用户详情，以用来显示头像和nick
			//holder.avatar.setImageResource(R.drawable.eyd_chat_default_avatar);
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + conversation.getToChatUser().getUserData().getUserLogo(),
					holder.avatar, MyshipAdapter.displayImageOptions);
			if(!"".equals(username))
			  holder.name.setText(username);
			else if(!"".equals(conversation.getToChatUser().getUserData().getTrueName()))
				holder.name.setText(conversation.getToChatUser().getUserData().getTrueName());
			else if(!"".equals(conversation.getToChatUser().getUserData().getLoginName()))
				holder.name.setText(conversation.getToChatUser().getUserData().getLoginName());
		}
		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			holder.unreadLabel.setText(String.valueOf(conversation
					.getUnreadMsgCount()));
			holder.unreadLabel.setVisibility(View.VISIBLE);
		}
//		else {
//			holder.unreadLabel.setVisibility(View.INVISIBLE);
//		}

		if (conversation.getRecentlyTime() != null&&conversation.getRecentlyTitle()!=null) {
			holder.message.setText(SmileUtils.getSmiledText (getContext(),conversation.getRecentlyTitle()),BufferType.SPANNABLE);
			holder.time.setText(DateUtils.getTimestampString(new Date(conversation.getRecentlyTime().getTime().getTime())));
//		if (conversation.getMsgCount() != 0) {
//			// 把最后一条消息的内容作为item的message内容
//			ChatMessage lastMessage = conversation.getLastMsg();
//			holder.message
//					.setText(
//							SmileUtils.getSmiledText(
//									getContext(),
//									getMessageDigest(lastMessage,
//											(this.getContext()))),
//							BufferType.SPANNABLE);
//
//			holder.time.setText(DateUtils.getTimestampString(new Date(
//					lastMessage.getCreateTime().getTime().getTime())));
//			if (lastMessage.getDirect() == ChatMessage.Direct.SEND
//					&& lastMessage.getStatus() == ChatMessage.Status.FAIL) {
//				holder.msgState.setVisibility(View.VISIBLE);
//			} else {
//				holder.msgState.setVisibility(View.GONE);
//			}
		}

		return convertView;
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(ChatMessage lastMessage, Context context) {
		String digest = lastMessage.getContent();

		return digest;
	}

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		ImageView avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;

	}

}
