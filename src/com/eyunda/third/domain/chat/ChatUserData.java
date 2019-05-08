package com.eyunda.third.domain.chat;

public class ChatUserData {

	@Override
	public String toString() {
		return "[userLogo=" + userLogo + ", userName=" + userName + ", userRole=" + userRole + ", userId="
				+ userId + ", userPhone=" + userPhone + "]";
	}
	private String userLogo;
	private String	userName;
	private String	userRole;//托运人，承运人
	private Long userId;
	private String userPhone;
	public String getUserLogo() {
		return userLogo;
	}
	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
}
