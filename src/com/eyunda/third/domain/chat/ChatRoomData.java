package com.eyunda.third.domain.chat;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;

public class ChatRoomData extends BaseData {

	private static final long serialVersionUID = -1L;
	private Long id = 0L;
	private String roomName = ""; // 聊天室
	private String recentlyTitle = ""; // 最近聊天主题
	private String recentlyTime = ""; // 最近聊天时间

	private Integer noReadCount = 0;
	private UserData toUserData = null;

	@SuppressWarnings("unchecked")
	public ChatRoomData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.roomName = (String) params.get("roomName");
			this.recentlyTitle = (String) params.get("recentlyTitle");
			this.recentlyTime = (String) params.get("recentlyTime");
			this.noReadCount = ((Double) params.get("noReadCount")).intValue();
			if (true) {
				Map<String, Object> map = (Map<String, Object>) params
						.get("toUserData");
				if (map != null && !map.isEmpty()) {
					UserData data = new UserData(map);
					this.toUserData = data;
				}
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRecentlyTitle() {
		return recentlyTitle;
	}

	public void setRecentlyTitle(String recentlyTitle) {
		this.recentlyTitle = recentlyTitle;
	}

	public String getRecentlyTime() {
		return recentlyTime;
	}

	public void setRecentlyTime(String recentlyTime) {
		this.recentlyTime = recentlyTime;
	}

	public Integer getNoReadCount() {
		return noReadCount;
	}

	public void setNoReadCount(Integer noReadCount) {
		this.noReadCount = noReadCount;
	}

	public UserData getToUserData() {
		return toUserData;
	}

	public void setToUserData(UserData toUserData) {
		this.toUserData = toUserData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
