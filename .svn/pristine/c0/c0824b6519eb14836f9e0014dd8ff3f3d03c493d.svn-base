package com.eyunda.third.chat.event;

import java.util.Map;

import com.eyunda.third.domain.enumeric.ApplyStatusCode;
import com.eyunda.third.domain.enumeric.UserTypeCode;

public class LoginEvent extends BaseEvent {
	private static final long serialVersionUID = -1L;

	public static final String USERID = "userId";
	public static final String USERTYPE = "userType";
	public static final String LOGIN_NAME = "loginName";
	public static final String TRUE_NAME = "trueName";
	public static final String NICK_NAME = "nickName";
	public static final String SIM_CARD_NO = "simCardNo"; // 手机SIM卡号
	public static final String BINDING_CODE = "bindingCode"; // 手机绑定编码
	public static final String EMAIL = "email"; // 电子邮箱
	public static final String MOBILE = "mobile"; // 手机
	
	public static final String USER_LOGO = "userLogo"; // 图标图片
	public static final String SIGNATURE = "signature"; // 个性签名
	public static final String STAMP = "stamp"; // 图章
	
	public static final String ROLES = "roles";// 角色
	public static final String IS_REALUSER = "isRealUser";// 角色
	public static final String IS_CREATSITE = "isCreatSite";// 是否建站
	
	public static final String ONLINE_STATUS = "onlineStatus";// 状态
	public static final String APPLY_STATUS = "applyStatus";// 状态
	
	public static final String ADDRESS = "address";// 小地址
	public static final String UNITADDR = "unitAddr";// 全地址
	public static final String UNITNAME = "unitName";// 单位
	public static final String POSTCODE = "postCode";// 邮编
	public static final String AREACODE = "areaCode";// 地区编码
	
	public static final String SHIPS = "ships"; //子账号授权的船舶名称如：shipName,shipName
	
	public LoginEvent(Map<String, String> source) {
		super(source);
	}
	
	public void setUserId(Long userId) {
		eventMap.put(USERID, Long.toString(userId));
	}

	public Long getUserId() {
		return Long.parseLong(eventMap.get(USERID));
	}

	public void setLoginName(String loginName) {
		eventMap.put(LOGIN_NAME, loginName);
	}

	public String getLoginName() {
		return (String) eventMap.get(LOGIN_NAME);
	}

	public void setTrueName(String trueName) {
		eventMap.put(TRUE_NAME, trueName);
	}

	public String getTrueName() {
		return (String) eventMap.get(TRUE_NAME);
	}

	public void setNickName(String nickName) {
		eventMap.put(NICK_NAME, nickName);
	}

	public String getNickName() {
		return (String) eventMap.get(NICK_NAME);
	}

	public String getSimCardNo() {
		return (String) eventMap.get(SIM_CARD_NO);
	}

	public void setSimCardNo(String simCardNo) {
		eventMap.put(SIM_CARD_NO, simCardNo);
	}

	public String getBindingCode() {
		return (String) eventMap.get(BINDING_CODE);
	}

	public void setBindingCode(String bindingCode) {
		eventMap.put(BINDING_CODE, bindingCode);
	}

	public String getEmail() {
		return (String) eventMap.get(EMAIL);
	}

	public void setEmail(String email) {
		eventMap.put(EMAIL, email);
	}

	public String getMobile() {
		return (String) eventMap.get(MOBILE);
	}

	public void setMobile(String mobile) {
		eventMap.put(MOBILE, mobile);
	}

	public String getUserLogo() {
		return (String) eventMap.get(USER_LOGO);
	}

	public void setUserLogo(String userLogo) {
		eventMap.put(USER_LOGO, userLogo);
	}

	public String getSignature() {
		return (String) eventMap.get(SIGNATURE);
	}

	public void setSignature(String signature) {
		eventMap.put(SIGNATURE, signature);
	}

	public String getStamp() {
		return (String) eventMap.get(STAMP);
	}

	public void setStamp(String stamp) {
		eventMap.put(STAMP, stamp);
	}
	public String getRoles() {
		return (String) eventMap.get(ROLES);
	}

	public void setRoles(String stamp) {
		eventMap.put(ROLES, stamp);
	}
	public Boolean isRealUser() {
		return new Boolean(eventMap.get(IS_REALUSER));
	}

	public void setIsRealUser(Boolean stamp) {
		eventMap.put(IS_REALUSER, stamp.toString());
	}
	public Boolean isCreatSite() {
		return new Boolean(eventMap.get(IS_CREATSITE));
	}
	
	public void setIsCreatSite(Boolean stamp) {
		eventMap.put(IS_CREATSITE, stamp.toString());
	}
	public UserTypeCode getUserType() {
		return UserTypeCode.valueOf((String) eventMap.get(USERTYPE));
	}
	
	public void setUserType(UserTypeCode userType) {
		eventMap.put(USERTYPE, userType.toString());
	}
	public OnlineStatusCode getOnlineStatus() {
		return OnlineStatusCode.valueOf((String) eventMap.get(ONLINE_STATUS));
	}

	public void setOnlineStatus(OnlineStatusCode onlineStatus) {
		eventMap.put(ONLINE_STATUS, onlineStatus.toString());
	}
	public ApplyStatusCode getApplyStatus() {
		return ApplyStatusCode.valueOf((String) eventMap.get(APPLY_STATUS));
	}
	
	public void setApplyStatus(ApplyStatusCode onlineStatus) {
		eventMap.put(APPLY_STATUS, onlineStatus.toString());
	}
	public String getAddress() {
		return (String) eventMap.get(ADDRESS);
	}

	public void setAddress(String address) {
		eventMap.put(ADDRESS, address);
	}
	public String getUnitAddr() {
		return (String) eventMap.get(UNITADDR);
	}
	
	public void setUnitAddr(String address) {
		eventMap.put(UNITADDR, address);
	}
	public String getUnitName() {
		return (String) eventMap.get(UNITNAME);
	}

	public void setUnitName(String unitCode) {
		eventMap.put(UNITNAME, unitCode);
	}
	public String getPostCode() {
		return (String) eventMap.get(POSTCODE);
	}
	
	public void setPostCode(String unitCode) {
		eventMap.put(POSTCODE, unitCode);
	}
	public String getAreaCode() {
		return (String) eventMap.get(AREACODE);
	}
	
	public void setAreaCode(String unitCode) {
		eventMap.put(AREACODE, unitCode);
	}
	public String getShips() {
		return (String) eventMap.get(SHIPS);
	}
	
	public void setShips(String ships) {
		eventMap.put(SHIPS, ships);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
