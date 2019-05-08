package com.eyunda.third.domain.account;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.UserRoleCode;


public class UserAgentData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private Long userId = 0L; // 用户ID
	private Long agentedId = 0L; // 被代理人ID
	private UserData agented = null;
	private UserRoleCode roleCode = UserRoleCode.master; // 被代理人角色
	private String createTime = ""; // 建立时间

	@SuppressWarnings("unchecked")
	public UserAgentData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.userId = ((Double) params.get("userId")).longValue();
			this.agentedId = ((Double) params.get("agentedId")).longValue();
			this.createTime = (String) params.get("createTime");

			this.roleCode = UserRoleCode.valueOf((String) params.get("roleCode"));
			this.agented = new UserData((Map<String, Object>) params.get("agented"));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAgentedId() {
		return agentedId;
	}

	public void setAgentedId(Long agentedId) {
		this.agentedId = agentedId;
	}

	public UserData getAgented() {
		return agented;
	}

	public void setAgented(UserData agented) {
		this.agented = agented;
	}

	public UserRoleCode getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(UserRoleCode roleCode) {
		this.roleCode = roleCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
