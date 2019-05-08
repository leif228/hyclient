package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;


public class UserShipNamesData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private String loginName = ""; // 登录名
	private String trueName = ""; // 姓名
	private String nickName = ""; // 昵称
	private String email = ""; // 电子邮箱
	private String mobile = ""; // 手机
	private String userLogo = ""; // 图标图片

	private String shipNames = ""; // shipName,shipName
	
	public UserShipNamesData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.loginName = (String) params.get("loginName");
			this.trueName = (String) params.get("trueName");
			this.nickName = (String) params.get("nickName");
			this.email = (String) params.get("email");
			this.mobile = (String) params.get("mobile");
			this.userLogo = (String) params.get("userLogo");
			this.shipNames = (String) params.get("shipNames");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getShipNames() {
		return shipNames;
	}

	public void setShipNames(String shipNames) {
		this.shipNames = shipNames;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
