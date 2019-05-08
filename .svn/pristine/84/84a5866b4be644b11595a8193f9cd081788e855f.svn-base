package com.eyunda.third.domain.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.eyunda.third.chat.event.OnlineStatusCode;
import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.ApplyStatusCode;
import com.eyunda.third.domain.enumeric.UserRoleCode;
import com.eyunda.third.domain.enumeric.UserTypeCode;
import com.eyunda.third.domain.wallet.UserBankData;

public class UserData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private String simCardNo = ""; // 手机SIM卡号
	private String bindingCode = ""; // 手机绑定编码
	
	private UserTypeCode userType = UserTypeCode.person; // 用户类型
	private String loginName = ""; // 登录名
	private String password = ""; // 登录密码
	private String paypwd = ""; // 支付密码
	private String trueName = ""; // 姓名
	private String nickName = ""; // 昵称
	private String email = ""; // 电子邮箱
	private String mobile = ""; // 手机

	private String roles = ""; // 角色
	private String roleDesc = "";// 角色描述;
	private String shortRoleDesc = "";// 第一角色名称;
	private List<UserRoleCode> roleCodes = new ArrayList<UserRoleCode>();
	private boolean isChildUser = false;
	private boolean isRealUser = false;
	private boolean isCreatSite = false; //是否建站

	private String postCode = ""; // 邮编
	private String areaCode = ""; // 地区编码
	private String address = ""; // 小地址
	private String unitAddr = ""; // 完整地址
	private String unitName = ""; // 公司名称

	private String userLogo = ""; // 图标图片
	private String signature = ""; // 个性签名
	private String stamp = ""; // 图章
	private String createTime = ""; // 注册时间

	private OnlineStatusCode onlineStatus = OnlineStatusCode.ofline;
	private String sessionId = "";
	private ApplyStatusCode applyStatus = ApplyStatusCode.approve;

	private String ships = "";// 子账号授权的船舶名称如：shipName,shipName

	private AccountData accountData = null;
	private UserBankData userBankData = null;

	
	
	public UserData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.simCardNo = (String) params.get("simCardNo");
			this.bindingCode = (String) params.get("bindingCode");
			if(params.get("userType")!=null)
				this.userType = UserTypeCode.valueOf((String) params.get("userType"));
			this.loginName = (String) params.get("loginName");
			this.password = (String) params.get("password");
			this.paypwd = (String) params.get("paypwd");
			this.trueName = (String) params.get("trueName");
			this.nickName = (String) params.get("nickName");
			this.email = (String) params.get("email");
			this.mobile = (String) params.get("mobile");
			this.ships = (String) params.get("ships");

			this.isRealUser = (Boolean) params.get("isRealUser");
			this.isCreatSite = (Boolean) params.get("isCreatSite");

			this.roles = (String) params.get("roles");
			if (this.roles != null && !"".equals(this.roles)) {
				List<UserRoleCode> rcs = new ArrayList<UserRoleCode>();
				String rDesc = "";
				String[] rs = this.roles.split(",");
				for (String r : rs) {
					int ri = Integer.parseInt(r);
					UserRoleCode rc = UserRoleCode.values()[ri];
					rcs.add(rc);
					rDesc += rc.getDescription() + ",";
				}
				if (!"".equals(rDesc))
					rDesc = rDesc.substring(0, rDesc.length() - 1);

				this.roleDesc = rDesc;
				this.shortRoleDesc = rcs.get(0).getDescription();
				this.roleCodes = rcs;

				if (rcs.contains(UserRoleCode.handler) || rcs.contains(UserRoleCode.sailor))
					this.isChildUser = true;
			}

			this.postCode = (String) params.get("postCode");
			this.areaCode = (String) params.get("areaCode");
			this.address = (String) params.get("address");
			this.unitAddr = (String) params.get("unitAddr");
			this.unitName = (String) params.get("unitName");

			this.userLogo = (String) params.get("userLogo");
			this.signature = (String) params.get("signature");
			this.stamp = (String) params.get("stamp");
			this.createTime = (String) params.get("createTime");
			if(params.get("accountData") != null){
				this.accountData = new AccountData((Map<String, Object>) params.get("accountData"));
			}
			if(params.get("userBankData") != null){
				this.userBankData = new UserBankData((Map<String, Object>) params.get("userBankData"));
			}
			this.applyStatus = ApplyStatusCode.valueOf((String) params.get("applyStatus"));
			this.onlineStatus = OnlineStatusCode.valueOf((String) params.get("onlineStatus"));
			this.sessionId = (String) params.get("sessionId");
		}
	}

	public UserData() {
		super();
	}

	public String getShips() {
		return ships;
	}

	public void setShips(String ships) {
		this.ships = ships;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserTypeCode getUserType() {
		return userType;
	}

	public void setUserType(UserTypeCode userType) {
		this.userType = userType;
	}

	public String getSimCardNo() {
		return simCardNo;
	}

	public void setSimCardNo(String simCardNo) {
		this.simCardNo = simCardNo;
	}

	public String getBindingCode() {
		return bindingCode;
	}

	public void setBindingCode(String bindingCode) {
		this.bindingCode = bindingCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPaypwd() {
		return paypwd;
	}

	public void setPaypwd(String paypwd) {
		this.paypwd = paypwd;
	}

	public String getTrueName() {
		if(TextUtils.isEmpty(this.trueName)) this.trueName = this.loginName;
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

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public List<UserRoleCode> getRoleCodes() {
		return roleCodes;
	}

	public String getShortRoleDesc() {
		return shortRoleDesc;
	}

	public void setShortRoleDesc(String shortRoleDesc) {
		this.shortRoleDesc = shortRoleDesc;
	}

	public void setRoleCodes(List<UserRoleCode> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public boolean isChildUser() {
		return isChildUser;
	}

	public void setChildUser(boolean isChildUser) {
		this.isChildUser = isChildUser;
	}

	public boolean isRealUser() {
		return isRealUser;
	}

	public void setRealUser(boolean isRealUser) {
		this.isRealUser = isRealUser;
	}

	public boolean isCreatSite() {
		return isCreatSite;
	}

	public void setCreatSite(boolean isCreatSite) {
		this.isCreatSite = isCreatSite;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUnitAddr() {
		return unitAddr;
	}

	public void setUnitAddr(String unitAddr) {
		this.unitAddr = unitAddr;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public OnlineStatusCode getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatusCode onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ApplyStatusCode getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(ApplyStatusCode applyStatus) {
		this.applyStatus = applyStatus;
	}

	public AccountData getAccountData() {
		return accountData;
	}

	public void setAccountData(AccountData accountData) {
		this.accountData = accountData;
	}

	public UserBankData getUserBankData() {
		return userBankData;
	}

	public void setUserBankData(UserBankData userBankData) {
		this.userBankData = userBankData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
